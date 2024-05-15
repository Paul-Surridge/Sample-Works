package Reminders;

import Database.Administration.DatabaseAdministrationRemindersTableInsertable;
import Shared.Constants;
import Shared.Formatter;
import javafx.beans.property.SimpleStringProperty;

public class Reminder implements RemindersTableInsertable, DatabaseAdministrationRemindersTableInsertable, Comparable<Reminder>, Constants{
    
    private SimpleStringProperty sspPrimaryKey = new SimpleStringProperty();
    private SimpleStringProperty sspRecordID = new SimpleStringProperty();
    private SimpleStringProperty sspStatus = new SimpleStringProperty();
    private SimpleStringProperty sspDateAlert = new SimpleStringProperty();
    private SimpleStringProperty sspName = new SimpleStringProperty();
    private SimpleStringProperty sspDetails = new SimpleStringProperty();
    private SimpleStringProperty sspDate = new SimpleStringProperty();
    
    private long lPrimaryKey, lRecordID;
    private int iDate, iDateAlert;
    
    //Via Database
    public Reminder(long lPrimaryKey, long lRecordID, int iDate, String sName, String sDetails, int iDateAlert)
    {
        this.lPrimaryKey = lPrimaryKey;
        this.lRecordID = lRecordID;
        this.iDate = iDate;
        this.iDateAlert = iDateAlert;
        
        initSimpleStringProperties(lPrimaryKey, lRecordID, iDate, sName, sDetails, iDateAlert);
    }
    //Via Context Menu Create || Datebase Import
    public Reminder(int iDate, String sName, String sDetails, int iDateAlert)
    {
        this(NOT_DEFINED, NOT_DEFINED, iDate, sName, sDetails, iDateAlert);
    }
    
    //Internal -----------------------------------------------------------------
    private void initSimpleStringProperties(long lPrimaryKey, long lRecordID, int iDate, String sName, String sDetails, int iDateAlert)
    {
        sspPrimaryKey.set(String.valueOf(lPrimaryKey));
        sspRecordID.set(String.valueOf(lRecordID));
        sspStatus.set("");
        sspDateAlert.set(Formatter.convert(DATE, DATABASE, TABLEVIEW, iDateAlert));
        sspName.set(sName);
        sspDetails.set(sDetails);
        sspDate.set(Formatter.convert(DATE, DATABASE, TABLEVIEW, iDate));
    }
    
    //External API -------------------------------------------------------------

    //Tableview 
    @Override
    public SimpleStringProperty padProperty()
    {
        return new SimpleStringProperty("");
    }
    @Override
    public SimpleStringProperty primaryKeyProperty()
    {
        return sspPrimaryKey;
    }
    @Override
    public SimpleStringProperty recordIDProperty()
    {
        return sspRecordID;
    }
    @Override
    public SimpleStringProperty statusProperty()
    {
        return sspStatus;
    }
    @Override
    public SimpleStringProperty dateAlertProperty()
    {
        return sspDateAlert;
    }
    @Override
    public SimpleStringProperty nameProperty()
    {
        return sspName;
    }
    @Override
    public SimpleStringProperty detailsProperty()
    {
        return sspDetails;
    }
    @Override
    public SimpleStringProperty dateProperty()
    {
        return sspDate;
    }
    @Override
    public int compareTo(Reminder r)
    {
        if      (r.getDateAlert() == iDateAlert)        return 0;
        else if (iDateAlert < r.getDateAlert())         return -1;
        else                                            return 1;
    }
    
    //ID
    public long getPrimaryKey()
    {
        return lPrimaryKey;
    }
    public void setPrimaryKey(long lKey)
    {
        lPrimaryKey = lKey;
        sspPrimaryKey.set(String.valueOf(lPrimaryKey));
    }
    public long getRecordID()
    {
        return lRecordID;
    }
    public void setRecordID(long lID)
    {
        lRecordID = lID;
        sspRecordID.set(String.valueOf(lRecordID));
    }
    
    //Reminder
    public int getDate()
    {
        return iDate;
    }
    public void setDate(int i)
    {
        iDate = i;
        
        if(iDate == NOT_DEFINED)    sspDate.set("-");
        else                        sspDate.set(Formatter.convert(DATE, DATABASE, TABLEVIEW, iDate));
    }
    public String getName()
    {
        return sspName.get();
    }
    public void setName(String sName)
    {
        sspName.set(sName);
    }
    public String getDetails()
    {
        return sspDetails.get();
    }
    public void setDetails(String sDetails)
    {
        sspDetails.set(sDetails);
    }
    public int getDateAlert()
    {
        return iDateAlert;
    }
    public void setDateAlert(int i)
    {
        iDateAlert = i;
        
        if(iDateAlert == NOT_DEFINED)   sspDateAlert.set("-");
        else                            sspDateAlert.set(Formatter.convert(DATE, DATABASE, TABLEVIEW, iDateAlert));
    }
    public void setStatus(String s)
    {
        sspStatus.set(s);
    }
}
