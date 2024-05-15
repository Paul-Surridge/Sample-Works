package Integrity.CheckAccountDatabase;

import Accounts.Account;
import Database.Database;
import History.Entry;
import Integrity.Error;
import Shared.Constants;
import Shared.Formatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckAccountDatabaseEntries implements Constants{
    
    private Map<String, List<Entry>> mpDuplicates = new HashMap<>();
    private List<Error> lstErrors = new ArrayList<>();
    private Account a;
    private int iCheckID, iNumberOfRecords;
    
    public CheckAccountDatabaseEntries(Account a, int iCheckID)
    {
        this.a = a;
        this.iCheckID = iCheckID;
    }
    
    //Internal -----------------------------------------------------------------
    private void clearAndReset()
    {
        mpDuplicates.clear();
        lstErrors.clear();
        
        iNumberOfRecords = 0;
    }
    
    //Check
    private void addError(String sError, String sDetails, Entry... lst)
    {
        List<Entry> lstBuild = new ArrayList<>();
        
        for(Entry e : lst)
            lstBuild.add(e);
        
        lstErrors.add(new Error(lstErrors.size()+1, iCheckID, sError, sDetails, lstBuild));
    }
    private void addError(String sError, String sDetails, List<Entry> lst)
    {
        lstErrors.add(new Error(lstErrors.size()+1, iCheckID, sError, sDetails, lst));
    }
    private void addDuplicate(String sKey, Entry eOriginal, Entry eDuplicate)
    {
        if(mpDuplicates.containsKey(sKey))
            mpDuplicates.get(sKey).add(eDuplicate);
        else
        {
            List<Entry> lst = new ArrayList<>();
            lst.add(eOriginal);
            lst.add(eDuplicate);
            mpDuplicates.put(sKey, lst);
        }
    }
    private void checkForDuplicates(List<Entry> lst)
    {
        Map<String, Entry> mp = new HashMap<>();
        
        for(Entry e : lst)
        {
            String sKey = e.getDate() + "|" + e.getName() + "|" + e.getIn() + "|" + e.getOut() + "|" + e.getBalance();

            if(mp.containsKey(sKey))    addDuplicate(sKey, mp.get(sKey), e);
            else                        mp.put(sKey, e);
        }
        
        if(!mpDuplicates.isEmpty())
            for(String s : mpDuplicates.keySet())
                addError("Duplicate entry", "Duplicate entry found in database", mpDuplicates.get(s));
    }
    private void checkForInconsistencies(List<Entry> lst)
    {
        Entry ePrevious, e;
        
        for(int i = 1 ; i<lst.size() ; i++)
        {
            ePrevious = lst.get(i-1);
            e = lst.get(i);
            
            if(ePrevious.getPrimaryKey() == e.getPrimaryKey())          addError("Primary Key", "Duplicate Primary Key found in database", ePrevious, e);
            else if(ePrevious.getPrimaryKey() > e.getPrimaryKey())      addError("Primary Key", "Decreasing Primary Key found in database", ePrevious, e);
            
            if(ePrevious.getRecordID() == e.getRecordID())              addError("Record ID", "Duplicate Record ID found in database", ePrevious, e);
            else if(ePrevious.getRecordID() > e.getRecordID())          addError("Record ID", "Decreasing Record ID found in database", ePrevious, e);
            
            if(ePrevious.getDate()> e.getDate())
                addError("Date", "Decreasing Date found in database", ePrevious, e);
        }
    }
    private void checkFields(List<Entry> lst)
    {
        for(Entry e : lst)
        {
            if(e.getPrimaryKey() <= 0)                                  addError("Primary Key",     "Non positive value found in database: " + e.getPrimaryKey(), e);
            if(e.getRecordID() <= 0)                                    addError("Record ID",       "Non positive value found in database: " + e.getRecordID(), e);
            if(e.getDate() <= 0)                                        addError("Date",            "Non positive value found in database: " + e.getDate(), e);
            
            if(e.getName().isEmpty())                                   addError("Name",            "Name field is empty", e);
            if(e.getType().isEmpty())                                   addError("Type",            "Type field is empty", e);
            if(e.getCategoryName().isEmpty())                           addError("Category Name",   "Category Name field is empty", e);
            
            if(e.getIn() == 0)                                          addError("In", "Zero value found in database: 0", e);
            if(e.getOut() == 0)                                         addError("Out", "Zero value found in database: 0", e);
            
            if(e.getIn() < NOT_DEFINED)                                 addError("In", "Negative value found in database: " + Formatter.convert(CURRENCY, DATABASE, TABLEVIEW, e.getIn()), e);
            if(e.getOut() < NOT_DEFINED)                                addError("Out", "Negative value found in database: " + Formatter.convert(CURRENCY, DATABASE, TABLEVIEW, e.getOut()), e);
            
            if(e.getIn() == NOT_DEFINED && e.getOut() == NOT_DEFINED)   addError("In/Out", "In and Out fields are both not defined", e);
            if(e.getIn() != NOT_DEFINED && e.getOut() != NOT_DEFINED)   addError("In/Out", "In and Out fields are both defined, direction unknown", e);
            if(e.getBalance() == NOT_DEFINED)                           addError("Balance", "Balance field is not defined", e);
        }
    }
    private void checkEntries(List<Entry> lst)
    {
        iNumberOfRecords = lst.size();
        
        checkForDuplicates(lst);
        checkForInconsistencies(lst);
        checkFields(lst);
    }
    
    //External API -------------------------------------------------------------
    public void runChecks()
    {
        clearAndReset();
        checkEntries(Database.getEntries(a, NOT_DEFINED));
    }
    public int getNumberOfRecords()
    {
        return iNumberOfRecords;
    }
    public List<Error> getErrors()
    {
        return lstErrors;
    }
    public boolean hasErrors()
    {
        return !lstErrors.isEmpty();
    }
}
