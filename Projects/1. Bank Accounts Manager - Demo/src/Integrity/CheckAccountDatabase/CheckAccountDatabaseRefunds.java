package Integrity.CheckAccountDatabase;

import Accounts.Account;
import Database.Database;
import Integrity.Error;
import Refunds.Refund;
import Shared.Constants;
import java.util.ArrayList;
import java.util.List;

public class CheckAccountDatabaseRefunds implements Constants{
    
    private List<Error> lstErrors = new ArrayList<>();
    private Account a;
    private int iCheckID, iNumberOfRecords;

    public CheckAccountDatabaseRefunds(Account a, int iCheckID)
    {
        this.a = a;
        this.iCheckID = iCheckID;
    }
    
    //Internal -----------------------------------------------------------------
    private void clearAndReset()
    {
        lstErrors.clear();
    }
    
    //Check
    private void addError(String sError, String sDetails, Refund... lst)
    {
        List<Refund> lstBuild = new ArrayList<>();
        
        for(Refund r : lst)
            lstBuild.add(r);
        
        lstErrors.add(new Error(lstErrors.size()+1, iCheckID, sError, sDetails, lstBuild));
    }
    private void checkForInconsistencies(List<Refund> lst)
    {
        Refund rPrevious, r;
        
        for(int i = 1 ; i<lst.size() ; i++)
        {
            rPrevious = lst.get(i-1);
            r = lst.get(i);
            
            if(rPrevious.getPrimaryKey() == r.getPrimaryKey())          addError("Primary Key", "Duplicate Primary Key found in database", rPrevious, r);
            else if(rPrevious.getPrimaryKey() > r.getPrimaryKey())      addError("Primary Key", "Decreasing Primary Key found in database", rPrevious, r);
            
            if(rPrevious.getRecordID() == r.getRecordID())              addError("Record ID", "Duplicate Record ID found in database", rPrevious, r);
            else if(rPrevious.getRecordID() > r.getRecordID())          addError("Record ID", "Decreasing Record ID found in database", rPrevious, r);
        }
    }
    private void checkFields(List<Refund> lst)
    {
        for(Refund r : lst)
        {
            if(r.getPrimaryKey() <= 0)                                  addError("Primary Key",     "Non positive value found in database: " + r.getPrimaryKey(), r);
            if(r.getRecordID() <= 0)                                    addError("Record ID",       "Non positive value found in database: " + r.getRecordID(), r);
            
            if(r.getDate() < 0)                                         addError("Date",            "Non positive value found in database: " + r.getDate(), r);
            if(r.getName().isEmpty())                                   addError("Name",            "Name field is empty", r);
            if(r.getAmount() < 0)                                       addError("Amount",          "Non positive value found in database: " + r.getAmount(), r);
        }
    }
    private void checkRefunds(List<Refund> lst)
    {
        iNumberOfRecords = lst.size();
        
        checkForInconsistencies(lst);
        checkFields(lst);
    }
    
    //External API -------------------------------------------------------------
    public void runChecks()
    {
        clearAndReset();
        checkRefunds(Database.getRefunds(a));
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
