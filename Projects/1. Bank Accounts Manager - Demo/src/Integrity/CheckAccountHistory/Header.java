package Integrity.CheckAccountHistory;

import Accounts.Account;
import Integrity.IntegrityCheckTableInsertable;
import Shared.Constants;
import javafx.beans.property.SimpleStringProperty;

public class Header implements IntegrityCheckTableInsertable, Constants{
    
    private SimpleStringProperty sspAccountName = new SimpleStringProperty();
    private SimpleStringProperty sspDescription = new SimpleStringProperty();
    private SimpleStringProperty sspStatus = new SimpleStringProperty();
    
    public Header(Account a, String sDescription)
    {
        sspAccountName.set(a.getName());
        sspDescription.set(sDescription);
    }
    
    //External API -------------------------------------------------------------
    @Override
    public SimpleStringProperty padProperty()
    {
        return new SimpleStringProperty("");
    }
    @Override
    public SimpleStringProperty accountNameProperty()
    {
        return sspAccountName;
    }
    @Override
    public SimpleStringProperty descriptionProperty()
    {
        return sspDescription;
    }
    @Override
    public SimpleStringProperty statusProperty()
    {
        return sspStatus;
    }
    
    public void setStatus(String sStatus)
    {
        sspStatus.set(sStatus);
    }
}
