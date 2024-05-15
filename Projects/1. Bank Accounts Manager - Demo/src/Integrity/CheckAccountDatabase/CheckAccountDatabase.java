package Integrity.CheckAccountDatabase;

import Integrity.Check;
import Accounts.Account;
import Shared.Constants;
import java.util.ArrayList;
import java.util.List;

public class CheckAccountDatabase implements Constants{
    
    private List<Check> lstChecks = new ArrayList<>();
    
    private CheckAccountDatabaseEntries cdEntries;
    private CheckAccountDatabaseCategories cdCategories;
    private CheckAccountDatabaseAutoCategories cdAutoCategories;
    private CheckAccountDatabaseWatches cdWatches;
    private CheckAccountDatabaseRefunds cdRefunds;
    
    private Account a;
    
    public CheckAccountDatabase(Account a)
    {
        this.a = a;
        
        cdEntries = new CheckAccountDatabaseEntries(a, ACCOUNT_DATABASE_ENTRIES);
        cdCategories = new CheckAccountDatabaseCategories(a, ACCOUNT_DATABASE_CATEGORIES);
        cdAutoCategories = new CheckAccountDatabaseAutoCategories(a, ACCOUNT_DATABASE_AUTO_CATEGORIES);
        cdWatches = new CheckAccountDatabaseWatches(a, ACCOUNT_DATABASE_WATCHES);
        cdRefunds = new CheckAccountDatabaseRefunds(a, ACCOUNT_DATABASE_REFUNDS);
        
        initChecks();
    }
                
    //Internal -----------------------------------------------------------------
    private void initChecks()
    {
        lstChecks.clear();
        
        lstChecks.add(new Check(a.getName(), ACCOUNT_DATABASE_ENTRIES, "Entries", "View Records"));
        lstChecks.add(new Check(ACCOUNT_DATABASE_CATEGORIES, "Categories", "View Records"));
        lstChecks.add(new Check(ACCOUNT_DATABASE_AUTO_CATEGORIES, "Auto Categories", "View Records"));
        lstChecks.add(new Check(ACCOUNT_DATABASE_WATCHES, "Watches", "View Records"));
        lstChecks.add(new Check(ACCOUNT_DATABASE_REFUNDS, "Refunds", "View Records"));
    }
    private void clearAndReset()
    {
        for(Check c : lstChecks)
            c.clearAndReset();
    }
    
    //External API -------------------------------------------------------------
    public List<Check> getChecks()
    {
        return lstChecks;
    }
    public void runChecks()
    {
        clearAndReset();
        
        cdEntries.runChecks();
        lstChecks.get(ACCOUNT_DATABASE_ENTRIES).setErrors(cdEntries.getErrors());
        lstChecks.get(ACCOUNT_DATABASE_ENTRIES).setStatus(cdEntries.getErrors().size() + ":" + cdEntries.getNumberOfRecords());
        
        cdCategories.runChecks();
        lstChecks.get(ACCOUNT_DATABASE_CATEGORIES).setErrors(cdCategories.getErrors());
        lstChecks.get(ACCOUNT_DATABASE_CATEGORIES).setStatus(cdCategories.getErrors().size() + ":" + cdCategories.getNumberOfRecords());
        
        cdAutoCategories.runChecks();
        lstChecks.get(ACCOUNT_DATABASE_AUTO_CATEGORIES).setErrors(cdAutoCategories.getErrors());
        lstChecks.get(ACCOUNT_DATABASE_AUTO_CATEGORIES).setStatus(cdAutoCategories.getErrors().size() + ":" + cdAutoCategories.getNumberOfRecords());
        
        cdWatches.runChecks();
        lstChecks.get(ACCOUNT_DATABASE_WATCHES).setErrors(cdWatches.getErrors());
        lstChecks.get(ACCOUNT_DATABASE_WATCHES).setStatus(cdWatches.getErrors().size() + ":" + cdWatches.getNumberOfRecords());
        
        cdRefunds.runChecks();
        lstChecks.get(ACCOUNT_DATABASE_REFUNDS).setErrors(cdRefunds.getErrors());
        lstChecks.get(ACCOUNT_DATABASE_REFUNDS).setStatus(cdRefunds.getErrors().size() + ":" + cdRefunds.getNumberOfRecords());
    }
}
