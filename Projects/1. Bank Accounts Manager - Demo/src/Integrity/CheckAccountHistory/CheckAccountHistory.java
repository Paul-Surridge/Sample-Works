package Integrity.CheckAccountHistory;

import History.EntryInternalTransfer;
import Integrity.Check;
import Accounts.Account;
import Accounts.Accounts;
import Database.Database;
import History.Entry;
import Integrity.Integrity;
import Refunds.Refund;
import Shared.Constants;
import Shared.Debug;
import Shared.Formatter;
import java.util.ArrayList;
import java.util.List;

public class CheckAccountHistory implements Constants{
    
    private List<Check> lstChecks = new ArrayList<>();
    private List<Entry> lstEntries = new ArrayList<>();
    
    private Account a;
    private Header h;
    
    public CheckAccountHistory(Account a)
    {
        this.a = a;
        this.h = new Header(a, "Number of entries");
        
        initChecks();
    }
                
    //Internal -----------------------------------------------------------------
    private void initChecks()
    {
        lstChecks.clear();
        
        lstChecks.add(new Check(ACCOUNT_HISTORY_BALANCE_INCONSISTENCY, "Balance inconsistencies", "View Entries"));
        lstChecks.add(new Check(ACCOUNT_HISTORY_DOUBLE_CHARGE_PURCHASE, "Double charging within purchases", "View Day"));
        lstChecks.add(new Check(ACCOUNT_HISTORY_DOUBLE_CHARGE_DDSO, "Double charging within SO/DD", "View Quarter"));
        lstChecks.add(new Check(ACCOUNT_HISTORY_TRANSFERS, "Internal Transfers", "View Internal Transfer"));
        lstChecks.add(new Check(ACCOUNT_HISTORY_REFUNDS_OUTSTANDING, "Outstanding refunds", "View Refunds"));
    }
    private void clearAndReset()
    {
        lstEntries.clear();
        
        for(Check c : lstChecks)
            c.clearAndReset();
        
        h.setStatus(DASH);
    }
    private void addCheckError(int iCheck, String sError, String sDetails, Entry... elst)
    {
        List<Entry> lst = new ArrayList<>();
        
        for(Entry e : elst)
            lst.add(e);
        
        lstChecks.get(iCheck).addError(sError, sDetails, lst);
    }
    private void addCheckError(int iCheck, String sError, String sDetails, Refund... rlst)
    {
        List<Refund> lst = new ArrayList<>();
        
        for(Refund r : rlst)
            lst.add(r);
        
        lstChecks.get(iCheck).addError(sError, sDetails, lst);
    }
    private void refreshCheckStatus(int iCheck)
    {
        lstChecks.get(iCheck).refreshStatus();
    }
    
    //Balance inconsistencies
    private void checkForBalanceInconsistencies()
    {
        Entry ePrevious, e;
        String sBalanceInconsistency;
        
        for(int i = 1 ; i<lstEntries.size() ; i++)
        {
            ePrevious = lstEntries.get(i-1);
            e = lstEntries.get(i);
            
            sBalanceInconsistency = Integrity.checkBalanceInconsistency(ePrevious, e, true);
            
            if(!sBalanceInconsistency.isEmpty())
                addCheckError(ACCOUNT_HISTORY_BALANCE_INCONSISTENCY, "Balance inconsistency on " + e.getDateTextForTableView(), e.getName() + " (" + sBalanceInconsistency + ")", ePrevious, e);
        }
        
        refreshCheckStatus(ACCOUNT_HISTORY_BALANCE_INCONSISTENCY);
    }
    
    //Double charge (Purchases)
    private void checkDayForDoubleChargeOnPurchases(int iEntryPositionInList)
    {
        Entry eNext;
        Entry e             = lstEntries.get(iEntryPositionInList);
        int iTotalFound     = 0;
        
        for(int i = iEntryPositionInList+1 ; i<lstEntries.size() ; i++)
        {
            eNext = lstEntries.get(i);
            
            if(eNext.getDate() != e.getDate())
                break;
            
            if(eNext.isPurchase()&& (e.getName().equals(eNext.getName())) && (e.getAmount() == eNext.getAmount()) )
                iTotalFound++;
        }
        
        if(iTotalFound > 0)
        {
            String s;
            String sDate = e.getDateTextForTableView();

            if(iTotalFound == 1)    s = "Double charge on " + sDate;
            else                    s = "Mulitple charge (x" + iTotalFound+1 + ") on " + sDate;
            
            addCheckError(ACCOUNT_HISTORY_DOUBLE_CHARGE_PURCHASE, s, e.getName() + " (£" + e.getAmountText() + ")", e);
        }
    }
    private void checkForDoubleChargeOnPurchases()
    {
        for(int i = 0 ; i<lstEntries.size()-1 ; i++)
            if(lstEntries.get(i).isPurchase())
                checkDayForDoubleChargeOnPurchases(i);

        refreshCheckStatus(ACCOUNT_HISTORY_DOUBLE_CHARGE_PURCHASE);
    }
    
    //Double charge (DD/SO)
    private void checkMonthForDoubleChargeOnDDSO(int iEntryPositionInList)
    {
        Entry eNext;
        Entry e             = lstEntries.get(iEntryPositionInList);
        int iLastDayOfMonth = Formatter.findLastDayOfCalendarMonth(e);
        int iTotalFound     = 0;
        
        for(int i = iEntryPositionInList+1 ; i<lstEntries.size() ; i++)
        {
            eNext = lstEntries.get(i);
            
            if(eNext.getDate() > iLastDayOfMonth)
                break;
            
            if(eNext.isDDSO() && (eNext.getDirection() == OUT) && (e.getName().equals(eNext.getName())) )
                iTotalFound++;
        }
        
        if(iTotalFound > 0)
        {
            String s;
            String sDate = e.getDateTextForTableView();

            if(iTotalFound == 1)    s = "Double DD/SO in same month from " + sDate;
            else                    s = "Mulitple DD/SO (x" + iTotalFound+1 + ") in same month from " + sDate;
            
            addCheckError(ACCOUNT_HISTORY_DOUBLE_CHARGE_DDSO, s, e.getName() + " (£" + e.getAmountText() + ")", e);
        }
    }
    private void checkForDoubleChargeOnDDOS()
    {
        for(int i = 0 ; i<lstEntries.size() ; i++)
            if(lstEntries.get(i).isDDSO())
                checkMonthForDoubleChargeOnDDSO(i);
        
        refreshCheckStatus(ACCOUNT_HISTORY_DOUBLE_CHARGE_DDSO);
    }
    
    //Transfers
    private void checkMappingInDestinationAccount(EntryInternalTransfer itSource, int iDestinationAccountID)
    {
        List<EntryInternalTransfer> lstDest = Accounts.get(iDestinationAccountID).getInternalTransferEntries();
        EntryInternalTransfer itDest;
        
        for(int i = lstDest.size()-1 ; i>=0 ; i--)
        {
            itDest = lstDest.get(i);

            if(itDest.getDate()<itSource.getDate())
                break;
            else if(itDest.getDirection() == IN && !itDest.isMapped() && itSource.getAmount() == itDest.getAmount())
            {
                itSource.setMapped(itDest.getSourceEntry());
                itDest.setMapped(itSource.getSourceEntry());
            }
        }
    }
    private void checkIncomingTransfers()
    {
        for(EntryInternalTransfer it : a.getInternalTransferEntries())
            if(it.getDirection() == IN && !it.isMapped())
                addCheckError(ACCOUNT_HISTORY_TRANSFERS, a.getNameAbbreviated() + " received on " + it.getDateForTableView() + " but not sent from " + it.getDestinationAccountNameAbbreviated(), it.getSourceEntryName() + " (£" + it.getAmountText() + ")", it.getSourceEntry());
        
        refreshCheckStatus(ACCOUNT_HISTORY_TRANSFERS);
    }
    private void checkOutgoingTransfers()
    {
        List<EntryInternalTransfer> lstSource = a.getInternalTransferEntries();
        EntryInternalTransfer itSource;

        for(int i = lstSource.size()-1 ; i>=0 ; i--)
        {
            itSource = lstSource.get(i);
            
            if(itSource.getDirection() == OUT)
                checkMappingInDestinationAccount(itSource, itSource.getDestinationAccountID());
        }
        
        for(EntryInternalTransfer it : lstSource)
            if(it.getDirection() == OUT && !it.isMapped())
                addCheckError(ACCOUNT_HISTORY_TRANSFERS, a.getNameAbbreviated() + " sent on " + it.getDateForTableView() + " but not received in " + it.getDestinationAccountNameAbbreviated(), it.getSourceEntryName() + " (£" + it.getAmountText() + ")", it.getSourceEntry());
        
        refreshCheckStatus(ACCOUNT_HISTORY_TRANSFERS);
    }
    
    //Outstanding Refunds
    private void checkOutstandingRefunds()
    {
        for(Refund r : a.getRefunds())
            if(!r.isReceived())
                addCheckError(ACCOUNT_HISTORY_REFUNDS_OUTSTANDING, "Refund outstanding from " + r.getDateTextForTableView(), r.getName() + " (£" + r.getAmountText() + ")", r);
        
        refreshCheckStatus(ACCOUNT_HISTORY_REFUNDS_OUTSTANDING);
    }
    
    //External API -------------------------------------------------------------
    public Header getHeader()
    {
        return h;
    }
    public List<Check> getChecks()
    {
        return lstChecks;
    }
    public void runChecks(int iDateFrom)
    {
        clearAndReset();
        
        lstEntries = Database.getEntries(a, iDateFrom);
        
        h.setStatus("(" + String.valueOf(lstEntries.size()) + ")");
        
        checkForBalanceInconsistencies();
        checkForDoubleChargeOnPurchases();
        checkForDoubleChargeOnDDOS();
        checkOutgoingTransfers();
        checkOutstandingRefunds();
    }
    public void runPostChecks()
    {
        checkIncomingTransfers();
    }
}
