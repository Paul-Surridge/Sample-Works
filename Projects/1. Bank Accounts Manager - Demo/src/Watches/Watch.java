package Watches;

import Categories.Category;
import Shared.Constants;
import Shared.Formatter;
import javafx.beans.property.SimpleStringProperty;
import Database.Administration.DatabaseAdministrationWatchesTableInsertable;
import History.Entry;
import java.util.ArrayList;
import java.util.List;

public class Watch implements WatchesTableInsertable, DatabaseAdministrationWatchesTableInsertable, Constants{
    
    private SimpleStringProperty sspPrimaryKey = new SimpleStringProperty();
    private SimpleStringProperty sspRecordID = new SimpleStringProperty();
    private SimpleStringProperty sspCategoryName = new SimpleStringProperty();
    private SimpleStringProperty sspDirection = new SimpleStringProperty();
    private SimpleStringProperty sspLastDate = new SimpleStringProperty();
    private SimpleStringProperty sspLastAmount = new SimpleStringProperty();
    private SimpleStringProperty sspStatus = new SimpleStringProperty();
    
    private List<Entry> lstEntries = new ArrayList<>();
    
    private long lPrimaryKey, lRecordID;
    private int iAccountID, iDirection, iLastDate, iLastAmount;
    private int iDecreaseCounter, iEqualCounter, iIncreaseCounter;
    
    //Via Database
    public Watch(int iAccountID, long lPrimaryKey, long lRecordID, String sCategoryName, String sDirection, int iLastDate, int iLastAmount)
    {
        this.iAccountID = iAccountID;
        this.lPrimaryKey = lPrimaryKey;
        this.lRecordID = lRecordID;
        this.iLastDate = iLastDate;
        this.iLastAmount = iLastAmount;
        
        initDirection(sDirection);
        initSimpleStringProperties(lPrimaryKey, lRecordID, sCategoryName, sDirection, iLastDate, iLastAmount);
    }
    //Via Datebase Import
    public Watch(int iAccountID, String sCategoryName, String sDirection, int iLastDate, int iLastAmount)
    {
        this(iAccountID, NOT_DEFINED, NOT_DEFINED, sCategoryName, sDirection, iLastDate, iLastAmount);
    }
    //Via Context Menu
    public Watch(Category c)
    {
        this.iAccountID = c.getAccountID();
        this.lPrimaryKey = NOT_DEFINED;
        this.lRecordID = NOT_DEFINED;
        
        String sDirection = c.getDirection() == IN ? IN_TEXT : OUT_TEXT;
        
        initDirection(sDirection);
        initSimpleStringProperties(lPrimaryKey, lRecordID, c.getName(), sDirection, NOT_DEFINED, NOT_DEFINED);
    }
    
    //Internal -----------------------------------------------------------------
    private void initDirection(String sDirection)
    {
        switch(sDirection)
        {
            case IN_TEXT:   iDirection = IN;            break;
            case OUT_TEXT:  iDirection = OUT;           break;
            default:        iDirection = NOT_DEFINED;   break;
        }
    }
    private void initSimpleStringProperties(long lPrimaryKey, long lRecordID, String sCategoryName, String sDirection, int iLastDate, int iLastAmount)
    {
        sspPrimaryKey.set(String.valueOf(lPrimaryKey));
        sspRecordID.set(String.valueOf(lRecordID));
        sspCategoryName.set(sCategoryName);
        sspDirection.set(sDirection);
        sspStatus.set(DASH);
        
        setLastDate(iLastDate);
        setLastAmount(iLastAmount);
    }
    
    //External API -------------------------------------------------------------
    
    //Auto Categories Tableview 
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
    public SimpleStringProperty categoryNameProperty()
    {
        return sspCategoryName;
    }
    @Override
    public SimpleStringProperty directionProperty()
    {
        return sspDirection;
    }
    @Override
    public SimpleStringProperty lastDateProperty()
    {
        return sspLastDate;
    }
    @Override
    public SimpleStringProperty lastAmountProperty()
    {
        return sspLastAmount;
    }
    @Override
    public SimpleStringProperty statusProperty()
    {
        return sspStatus;
    }
    
    //ID
    public int getAccountID()
    {
        return iAccountID;
    }
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
    
    //Watch
    public String getCategoryName()
    {
        return sspCategoryName.get();
    }
    public void setCategoryName(String sCategoryName)
    {
        sspCategoryName.set(sCategoryName);
    }
    public int getDirection()
    {
        return iDirection;
    }
    public String getDirectionText()
    {
        return sspDirection.get();
    }
    public int getLastDate()
    {
        return iLastDate;
    }
    public void setLastDate(int iDate)
    {
        iLastDate = iDate;
        
        if(iDate == NOT_DEFINED)    sspLastDate.set(DASH);
        else                        sspLastDate.set(Formatter.convert(DATE, DATABASE, TABLEVIEW, iDate));
    }
    public int getLastAmount()
    {
        return iLastAmount;
    }
    public void setLastAmount(int iAmount)
    {
        iLastAmount = iAmount;
        
        if(iAmount == NOT_DEFINED)  sspLastAmount.set(DASH);
        else                        sspLastAmount.set(Formatter.convert(CURRENCY, DATABASE, TABLEVIEW, iAmount));
    }
    
    //Status
    public void clearStatus()
    {
        lstEntries.clear();
        
        iDecreaseCounter = 0;
        iEqualCounter = 0;
        iIncreaseCounter = 0;
        
        sspStatus.set(DASH);
    }
    public void incDecreaseCounter()
    {
        iDecreaseCounter++;
    }
    public void incEqualCounter()
    {
        iEqualCounter++;
    }
    public void incIncreaseCounter()
    {
        iIncreaseCounter++;
    }
    public void setStatus(String sStatus)
    {
        sspStatus.set(sStatus);
    }
    public void addEntry(Entry e)
    {
        lstEntries.add(e);
    }
    public List<Entry> getEntries()
    {
        return lstEntries;
    }
    public void refreshStatus()
    {
        String s = "";
        
        if(iDecreaseCounter == 0 && iEqualCounter == 0 && iIncreaseCounter == 0)
            s = "No entries with the category '" + sspCategoryName.get() + "' in account since " + sspLastDate.get();
        else
        {
            if(iDecreaseCounter > 0)    s = "Decrease (x" + iDecreaseCounter + ") ";
            if(iEqualCounter > 0)       s += "Equal (x" + iEqualCounter + ") ";
            if(iIncreaseCounter > 0)    s += "Increase (x" + iIncreaseCounter + ") ";

            s += "since " + sspLastDate.get();
        }
        
        sspStatus.set(s);
    }
}
