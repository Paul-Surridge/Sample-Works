package Integrity.CheckAccountDatabase;

import Accounts.Account;
import Database.Database;
import Integrity.Error;
import Shared.Constants;
import Watches.Watch;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckAccountDatabaseWatches implements Constants{

    private Map<String, List<Watch>> mpDuplicates = new HashMap<>();
    private List<Error> lstErrors = new ArrayList<>();
    private Account a;
    private int iCheckID, iNumberOfRecords;
    
    public CheckAccountDatabaseWatches(Account a, int iCheckID)
    {
        this.a = a;
        this.iCheckID = iCheckID;
    }
    
    //Internal -----------------------------------------------------------------
    private void clearAndReset()
    {
        mpDuplicates.clear();
        lstErrors.clear();
    }
    
    //Check
    private void addError(String sError, String sDetails, Watch... lst)
    {
        List<Watch> lstBuild = new ArrayList<>();
        
        for(Watch w : lst)
            lstBuild.add(w);
        
        lstErrors.add(new Error(lstErrors.size()+1, iCheckID, sError, sDetails, lstBuild));
    }
    private void addError(String sError, String sDetails, List<Watch> lst)
    {
        lstErrors.add(new Error(lstErrors.size()+1, iCheckID, sError, sDetails, lst));
    }
    private void addDuplicate(String sKey, Watch cOriginal, Watch cDuplicate)
    {
        if(mpDuplicates.containsKey(sKey))
            mpDuplicates.get(sKey).add(cDuplicate);
        else
        {
            List<Watch> lst = new ArrayList<>();
            lst.add(cOriginal);
            lst.add(cDuplicate);
            mpDuplicates.put(sKey, lst);
        }
    }
    private void checkForDuplicates(List<Watch> lst)
    {
        Map<String, Watch> mp = new HashMap<>();
        
        for(Watch w : lst)
        {
            String sKey = w.getCategoryName()+ "|" + w.getDirectionText();

            if(mp.containsKey(sKey))    addDuplicate(sKey, mp.get(sKey), w);
            else                        mp.put(sKey, w);
        }
        
        if(!mpDuplicates.isEmpty())
            for(String s : mpDuplicates.keySet())
                addError("Duplicate watch", "Duplicate watch found in database", mpDuplicates.get(s));
    }
    private void checkForInconsistencies(List<Watch> lst)
    {
        Watch wPrevious, w;
        
        for(int i = 1 ; i<lst.size() ; i++)
        {
            wPrevious = lst.get(i-1);
            w = lst.get(i);
            
            if(wPrevious.getPrimaryKey() == w.getPrimaryKey())          addError("Primary Key", "Duplicate Primary Key found in database", wPrevious, w);
            else if(wPrevious.getPrimaryKey() > w.getPrimaryKey())      addError("Primary Key", "Decreasing Primary Key found in database", wPrevious, w);
            
            if(wPrevious.getRecordID() == w.getRecordID())              addError("Record ID", "Duplicate Record ID found in database", wPrevious, w);
            else if(wPrevious.getRecordID() > w.getRecordID())          addError("Record ID", "Decreasing Record ID found in database", wPrevious, w);
        }
    }
    private void checkFields(List<Watch> lst)
    {
        for(Watch w : lst)
        {
            if(w.getPrimaryKey() <= 0)                                  addError("Primary Key",     "Non positive value found in database: " + w.getPrimaryKey(), w);
            if(w.getRecordID() <= 0)                                    addError("Record ID",       "Non positive value found in database: " + w.getRecordID(), w);
            
            if(w.getCategoryName().isEmpty())                           addError("Category Name",   "Category name field is empty", w);
            if(w.getDirectionText().isEmpty())                          addError("Direction",       "Direction field is empty", w);
        }
    }
    private void checkWatches(List<Watch> lst)
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
        checkWatches(Database.getWatches(a));
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
