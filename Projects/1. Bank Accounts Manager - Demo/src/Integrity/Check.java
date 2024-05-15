package Integrity;

import Shared.Constants;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;

public class Check implements IntegrityCheckTableInsertable, Constants{
    
    private SimpleStringProperty sspAccountName = new SimpleStringProperty();
    private SimpleStringProperty sspDescription = new SimpleStringProperty();
    private SimpleStringProperty sspStatus = new SimpleStringProperty();
    
    private List<Error> lstErrors = new ArrayList<>();
    private int iID;
    private String sErrorContextMenuLabel;
    
    public Check(int iID, String sDescription, String sErrorContextMenuLabel)
    {
        this("", iID, sDescription, sErrorContextMenuLabel);
    }
    public Check(String sAccountName, int iID, String sDescription, String sErrorContextMenuLabel)
    {
        this.iID = iID;
        
        sspAccountName.set(sAccountName);
        sspDescription.set(sDescription);
        sspStatus.set(DASH);
        
        this.sErrorContextMenuLabel = sErrorContextMenuLabel;
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
    
    //Check ID
    public int getID()
    {
        return iID;
    }
    public String getErrorContextMenuLabel()
    {
        return sErrorContextMenuLabel;
    }
    
    //Error
    public void addError(String sError, String sDetails, List<?> lst)
    {
        lstErrors.add(new Error(lstErrors.size()+1, iID, sError, sDetails, lst));
    }
    public void setErrors(List<Error> lst)
    {
        lstErrors = lst;
    }
    public boolean hasErrors()
    {
        return !lstErrors.isEmpty();
    }
    public List<Error> getErrors()
    {
        return lstErrors;
    }
    
    //Reset
    public void clearAndReset()
    {
        lstErrors.clear();
        setStatus(DASH);
    }
    
    //Status
    public String getStatus()
    {
        return sspStatus.get();
    }
    public void setStatus(String sStatus)
    {
        sspStatus.set(sStatus);
    }
    public void refreshStatus()
    {
        setStatus(String.valueOf(lstErrors.size()));
    }
}
