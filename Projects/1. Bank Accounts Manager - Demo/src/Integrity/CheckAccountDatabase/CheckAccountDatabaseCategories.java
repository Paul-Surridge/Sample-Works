package Integrity.CheckAccountDatabase;

import Accounts.Account;
import Categories.Category;
import Database.Database;
import Integrity.Error;
import Shared.Constants;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckAccountDatabaseCategories implements Constants{
    
    private Map<String, List<Category>> mpDuplicates = new HashMap<>();
    private List<Error> lstErrors = new ArrayList<>();
    private Account a;
    private int iCheckID, iNumberOfRecords;

    public CheckAccountDatabaseCategories(Account a, int iCheckID)
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
    private void addError(String sError, String sDetails, Category... lst)
    {
        List<Category> lstBuild = new ArrayList<>();
        
        for(Category c : lst)
            lstBuild.add(c);
        
        lstErrors.add(new Error(lstErrors.size()+1, iCheckID, sError, sDetails, lstBuild));
    }
    private void addError(String sError, String sDetails, List<Category> lst)
    {
        lstErrors.add(new Error(lstErrors.size()+1, iCheckID, sError, sDetails, lst));
    }
    private void addDuplicate(String sKey, Category cOriginal, Category cDuplicate)
    {
        if(mpDuplicates.containsKey(sKey))
            mpDuplicates.get(sKey).add(cDuplicate);
        else
        {
            List<Category> lst = new ArrayList<>();
            lst.add(cOriginal);
            lst.add(cDuplicate);
            mpDuplicates.put(sKey, lst);
        }
    }
    private void checkForDuplicates(List<Category> lst)
    {
        Map<String, Category> mp = new HashMap<>();
        
        for(Category c : lst)
        {
            String sKey = c.getName()+ "|" + c.getDirectionText();

            if(mp.containsKey(sKey))    addDuplicate(sKey, mp.get(sKey), c);
            else                        mp.put(sKey, c);
        }
        
        if(!mpDuplicates.isEmpty())
            for(String s : mpDuplicates.keySet())
                addError("Duplicate category", "Duplicate category found in database", mpDuplicates.get(s));
    }
    private void checkForInconsistencies(List<Category> lst)
    {
        Category cPrevious, c;
        
        for(int i = 1 ; i<lst.size() ; i++)
        {
            cPrevious = lst.get(i-1);
            c = lst.get(i);
            
            if(cPrevious.getPrimaryKey() == c.getPrimaryKey())          addError("Primary Key", "Duplicate Primary Key found in database", cPrevious, c);
            else if(cPrevious.getPrimaryKey() > c.getPrimaryKey())      addError("Primary Key", "Decreasing Primary Key found in database", cPrevious, c);
            
            if(cPrevious.getRecordID() == c.getRecordID())              addError("Record ID", "Duplicate Record ID found in database", cPrevious, c);
            else if(cPrevious.getRecordID() > c.getRecordID())          addError("Record ID", "Decreasing Record ID found in database", cPrevious, c);
        }
    }
    private void checkFields(List<Category> lst)
    {
        for(Category c : lst)
        {
            if(c.getPrimaryKey() <= 0)                                  addError("Primary Key",     "Non positive value found in database: " + c.getPrimaryKey(), c);
            if(c.getRecordID() <= 0)                                    addError("Record ID",       "Non positive value found in database: " + c.getRecordID(), c);
            
            if(c.getName().isEmpty())                                   addError("Name",            "Name field is empty", c);
            if(c.getDirectionText().isEmpty())                          addError("Direction",       "Direction field is empty", c);
        }
    }
    private void checkCategories(List<Category> lst)
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
        checkCategories(Database.getCategories(a));
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
