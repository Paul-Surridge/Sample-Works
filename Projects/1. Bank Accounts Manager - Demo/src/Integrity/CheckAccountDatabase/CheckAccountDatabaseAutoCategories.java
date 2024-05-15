package Integrity.CheckAccountDatabase;

import Accounts.Account;
import AutoCategories.AutoCategory;
import Database.Database;
import Integrity.Error;
import Shared.Constants;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckAccountDatabaseAutoCategories implements Constants{
    
    private Map<String, List<AutoCategory>> mpDuplicates = new HashMap<>();
    private List<Error> lstErrors = new ArrayList<>();
    private Account a;
    private int iCheckID, iNumberOfRecords;
    
    public CheckAccountDatabaseAutoCategories(Account a, int iCheckID)
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
    private void addError(String sError, String sDetails, AutoCategory... lst)
    {
        List<AutoCategory> lstBuild = new ArrayList<>();
        
        for(AutoCategory ac : lst)
            lstBuild.add(ac);
        
        lstErrors.add(new Error(lstErrors.size()+1, iCheckID, sError, sDetails, lstBuild));
    }
    private void addError(String sError, String sDetails, List<AutoCategory> lst)
    {
        lstErrors.add(new Error(lstErrors.size()+1, iCheckID, sError, sDetails, lst));
    }
    private void addDuplicate(String sKey, AutoCategory acOriginal, AutoCategory acDuplicate)
    {
        if(mpDuplicates.containsKey(sKey))
            mpDuplicates.get(sKey).add(acDuplicate);
        else
        {
            List<AutoCategory> lst = new ArrayList<>();
            lst.add(acOriginal);
            lst.add(acDuplicate);
            mpDuplicates.put(sKey, lst);
        }
    }
    private void checkForDuplicates(List<AutoCategory> lst)
    {
        Map<String, AutoCategory> mp = new HashMap<>();
        
        for(AutoCategory ac : lst)
        {
            String sKey = ac.getEntryName() + "|" + ac.getDirectionText();

            if(mp.containsKey(sKey))    addDuplicate(sKey, mp.get(sKey), ac);
            else                        mp.put(sKey, ac);
        }
        
        if(!mpDuplicates.isEmpty())
            for(String s : mpDuplicates.keySet())
                addError("Duplicate auto category", "Duplicate auto category found in database", mpDuplicates.get(s));
    }
    private void checkForInconsistencies(List<AutoCategory> lst)
    {
        AutoCategory acPrevious, ac;
        
        for(int i = 1 ; i<lst.size() ; i++)
        {
            acPrevious = lst.get(i-1);
            ac = lst.get(i);
            
            if(acPrevious.getPrimaryKey() == ac.getPrimaryKey())        addError("Primary Key", "Duplicate Primary Key found in database", acPrevious, ac);
            else if(acPrevious.getPrimaryKey() > ac.getPrimaryKey())    addError("Primary Key", "Decreasing Primary Key found in database", acPrevious, ac);
            
            if(acPrevious.getRecordID() == ac.getRecordID())            addError("Record ID", "Duplicate Record ID found in database", acPrevious, ac);
            else if(acPrevious.getRecordID() > ac.getRecordID())        addError("Record ID", "Decreasing Record ID found in database", acPrevious, ac);
        }
    }
    private void checkFields(List<AutoCategory> lst)
    {
        for(AutoCategory ac : lst)
        {
            if(ac.getPrimaryKey() <= 0)                                 addError("Primary Key",     "Non positive value found in database: " + ac.getPrimaryKey(), ac);
            if(ac.getRecordID() <= 0)                                   addError("Record ID",       "Non positive value found in database: " + ac.getRecordID(), ac);
            
            if(ac.getEntryName().isEmpty())                             addError("Entry Name",      "Entry name field is empty", ac);
            if(ac.getDirectionText().isEmpty())                         addError("Direction",       "Direction field is empty", ac);
        }
    }
    private void checkAutoCategories(List<AutoCategory> lst)
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
        checkAutoCategories(Database.getAutoCategories(a));
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
