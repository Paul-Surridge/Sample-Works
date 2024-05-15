package Accounts.All;

import Accounts.BalanceProperty;
import Accounts.Account;
import Accounts.AccountProperty;
import Accounts.Accounts;
import Accounts.AccountsTableInsertable;
import History.Entry;
import Shared.Constants;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.CheckBox;

public class All implements AccountsTableInsertable, Constants{
    
    private Deque<Entry> dqEntries = new ArrayDeque<>();
    private List<Deque<Entry>> lstEntriesByAccount = new ArrayList<>();
    
    private List<BalanceProperty> lstBalanceProperties = new ArrayList<>();
    private List<AccountProperty> lstAccountProperties = new ArrayList<>();
    
    //Balance
    private BalanceProperty bpBalanceAtStart                = new BalanceProperty("Balance Start");
    private BalanceProperty bpIncoming                      = new BalanceProperty("Incoming");
    private BalanceProperty bpOutgoing                      = new BalanceProperty("Outgoing");
    private BalanceProperty bpBalanceChange                 = new BalanceProperty("Balance Change");
    private BalanceProperty bpBalanceAtEnd                  = new BalanceProperty("Balance End");
    
    //Incoming
    private AccountProperty apIncomingIncome                = new AccountProperty("Income", IN,                               AP_INCOMING_INCOME);
    private AccountProperty apIncomingInternalTransfers     = new AccountProperty("Internal Transfers", IN,                   AP_INCOMING_INTERNAL_TRANSFER);
    private AccountProperty apIncomingTotal                 = new AccountProperty("Total Incoming", IN,                       AP_INCOMING_TOTAL);
    
    //Outgoing
    private AccountProperty apOutgoingPurchases             = new AccountProperty("Purchases", OUT,                           AP_OUTGOING_PURCHASE);
    private AccountProperty apOutgoingDDSO                  = new AccountProperty("Direct Debit and Standing Orders", OUT,    AP_OUTGOING_DDSO);
    private AccountProperty apOutgoingOther                 = new AccountProperty("Other", OUT,                               AP_OUTGOING_OTHER);
    private AccountProperty apOutgoingSpend                 = new AccountProperty("Total Spend", OUT,                         AP_OUTGOING_SPEND);
    private AccountProperty apOutgoingInternalTransfers     = new AccountProperty("Internal Transfers", OUT,                  AP_OUTGOING_INTERNAL_TRANSFER);
    private AccountProperty apOutgoingTotal                 = new AccountProperty("Total Outgoing", OUT,                      AP_OUTGOING_TOTAL);
    
    private AllChartBars acbChartBars;
    private AllChartLines aclChartLines;
    
    private CheckBox cbView = new CheckBox();
    private String sName;
    
    public All(String sName)
    {
        this.sName = sName;
        
        lstBalanceProperties.add(bpBalanceAtStart);
        lstBalanceProperties.add(bpIncoming);
        lstBalanceProperties.add(bpOutgoing);
        lstBalanceProperties.add(bpBalanceChange);
        lstBalanceProperties.add(bpBalanceAtEnd);
        
        lstAccountProperties.add(apIncomingIncome);
        lstAccountProperties.add(apIncomingInternalTransfers);
        lstAccountProperties.add(apIncomingTotal);
        
        lstAccountProperties.add(apOutgoingPurchases);
        lstAccountProperties.add(apOutgoingDDSO);
        lstAccountProperties.add(apOutgoingOther);
        lstAccountProperties.add(apOutgoingSpend);
        lstAccountProperties.add(apOutgoingInternalTransfers);
        lstAccountProperties.add(apOutgoingTotal);
    }
    
    //Internal -----------------------------------------------------------------
    private boolean entriesToBeProcessed(List<Deque<Entry>> lst)
    {
        for(Deque<Entry> dq : lst)
            if(!dq.isEmpty())
                return true;
        
        return false;
    }
    
    //External API -------------------------------------------------------------
    
    //Accounts Tableview
    @Override
    public SimpleStringProperty balanceAtStartProperty()
    {
        return bpBalanceAtStart.getProperty();
    }
    @Override
    public SimpleStringProperty incomingTotalProperty()
    {
        return bpIncoming.getProperty();
    }
    @Override
    public SimpleStringProperty outgoingTotalProperty()
    {
        return bpOutgoing.getProperty();
    }
    @Override
    public SimpleStringProperty balanceChangeProperty()
    {
        return bpBalanceChange.getProperty();
    }
    @Override
    public SimpleStringProperty balanceAtEndProperty()
    {
        return bpBalanceAtEnd.getProperty();
    }
    @Override
    public boolean getView()
    {
        return cbView.isSelected();
    }
    @Override
    public void initViewCheckbox(CheckBox cb)
    {
        cbView = cb;
    }
    @Override
    public String getName()
    {
        return sName;
    }
    
    //Balance
    public void add(Account a)
    {
        List<BalanceProperty> lst = a.getBalanceProperties();

        for(int i = 0 ; i<lstBalanceProperties.size() ; i++)
            lstBalanceProperties.get(i).set(lstBalanceProperties.get(i).getValue()+ lst.get(i).getValue());
    }
    public void finaliseBalanceProperties()
    {
        for(BalanceProperty bp : lstBalanceProperties)
            bp.set();
    }
    public List<BalanceProperty> getBalanceProperties()
    {
        return new ArrayList<>(lstBalanceProperties);
    }
    public BalanceProperty getBalanceProperty(int iID)
    {
        return iID < lstBalanceProperties.size() ? lstBalanceProperties.get(iID) : null;
    }
    public boolean isEmpty()
    {
        return dqEntries.isEmpty();
    }
    
    //Chart Bars and Lines
    public void rebuildChartData()
    {
        List<Deque<Entry>> lst = new ArrayList<>();
        int iEarliest;
        Entry e;
        
        for(Account a : Accounts.getAccounts())
        {
            lstEntriesByAccount.add(a.getEntries());
            lst.add(a.getEntries());
        }
        
        while(entriesToBeProcessed(lst))
        {
            iEarliest = -1;
            
            for(int i = 0 ; i<lst.size() && iEarliest == -1 ; i++)
                if(!lst.get(i).isEmpty())
                    iEarliest = i;
            
            for(int i = iEarliest+1 ; i<lst.size() ; i++)
                if(!lst.get(i).isEmpty())
                    if(lst.get(i).peekFirst().getDate() < lst.get(iEarliest).peekFirst().getDate())
                        iEarliest = i;
            
            e = lst.get(iEarliest).removeFirst();
            
            switch(e.getDirection())
            {
                case IN:    apIncomingTotal.add(e);           break;
                case OUT:   apOutgoingTotal.add(e);           break;
            }

            if(e.getDirection() == OUT && !e.isInternalTransfer())
                apOutgoingSpend.add(e);

            lstAccountProperties.get(e.getAccountProperty()).add(e);
            
            dqEntries.add(e);
        }
    }
    public void clear()
    {
        acbChartBars = new AllChartBars(this);
        aclChartLines = new AllChartLines(this);
        
        for(BalanceProperty bp : lstBalanceProperties)
            bp.clear();

        for(AccountProperty ap : lstAccountProperties)
            ap.clear();

        dqEntries.clear();
        lstEntriesByAccount.clear();
    }
    public void clearChartData()
    {
        acbChartBars = new AllChartBars(this);
        aclChartLines = new AllChartLines(this);
    }
    public Deque<Entry> getEntries()
    {
        return new ArrayDeque<>(dqEntries);
    }
    public Deque<Entry> getEntriesByAccountProperty(int iAccountProperty)
    {
        if(iAccountProperty < lstAccountProperties.size())      return new ArrayDeque<>(lstAccountProperties.get(iAccountProperty).getEntries());
        else                                                    return new ArrayDeque<>();
    }
    public List<Deque<Entry>> getEntriesByAccount()
    {
        return lstEntriesByAccount;
    }
    public AllChartBars getChartBars()
    {
        return acbChartBars;
    }
    public AllChartLines getChartLines()
    {
        return aclChartLines;
    }
}
