package Database.Administration;

import Accounts.Account;
import Shared.Constants;
import javafx.beans.property.SimpleStringProperty;

public class Item implements DatabaseAdministrationOverviewTableInsertable, Constants{
    
    private SimpleStringProperty sspAccount = new SimpleStringProperty();
    private SimpleStringProperty sspTable = new SimpleStringProperty();
    private SimpleStringProperty sspNumberOfRecords = new SimpleStringProperty();
    
    Account a;
    int iTable;
    
    public Item(Account a, int iTable, String sTableName, long lNumberOfRecords)
    {
        this.a = a;
        this.iTable = iTable;
        
        sspAccount.set("");
        sspTable.set(sTableName);
        sspNumberOfRecords.set(String.valueOf(lNumberOfRecords));
    }
    public Item(Account a, int iTable, String sTableName, String sNumberOfRecords)
    {
        this.a = a;
        this.iTable = iTable;
        
        sspAccount.set(a.getName());
        sspTable.set(sTableName);
        sspNumberOfRecords.set(sNumberOfRecords);
    }
    public Item(int iTable, String sTableName, long lNumberOfRecords)
    {
        this.a = null;
        this.iTable = iTable;
        
        sspAccount.set("");
        sspTable.set(sTableName);
        sspNumberOfRecords.set(String.valueOf(lNumberOfRecords));
    }
    
    //External API -------------------------------------------------------------
    @Override
    public SimpleStringProperty padProperty()
    {
        return new SimpleStringProperty();
    }
    @Override
    public SimpleStringProperty accountProperty()
    {
        return sspAccount;
    }
    @Override
    public SimpleStringProperty tableProperty()
    {
        return sspTable;
    }
    @Override
    public SimpleStringProperty numberOfRecordsProperty()
    {
        return sspNumberOfRecords;
    }
    
    public int getAccountID()
    {
        return a == null ? NOT_DEFINED : a.getAccountID();
    }
    public int getTable()
    {
        return iTable;
    }
}