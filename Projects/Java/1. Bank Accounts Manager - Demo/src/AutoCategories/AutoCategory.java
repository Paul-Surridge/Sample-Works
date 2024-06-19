package AutoCategories;

import Categories.Category;
import History.Entry;
import Shared.Constants;
import Shared.Formatter;
import javafx.beans.property.SimpleStringProperty;
import Database.Administration.DatabaseAdministrationAutoCategoriesTableInsertable;

public class AutoCategory implements AutoCategoriesTableInsertable, DatabaseAdministrationAutoCategoriesTableInsertable, Constants{
    
    private SimpleStringProperty sspPrimaryKey = new SimpleStringProperty();
    private SimpleStringProperty sspRecordID = new SimpleStringProperty();
    private SimpleStringProperty sspEntryName = new SimpleStringProperty();
    private SimpleStringProperty sspCategoryName = new SimpleStringProperty();
    private SimpleStringProperty sspDirection = new SimpleStringProperty();
    private SimpleStringProperty sspFrequency = new SimpleStringProperty();
    private SimpleStringProperty sspFirst = new SimpleStringProperty();
    private SimpleStringProperty sspLast = new SimpleStringProperty();
    
    private long lPrimaryKey, lRecordID;
    private int iAccountID, iDirection;
    
    //Via Database
    public AutoCategory(int iAccountID, long lPrimaryKey, long lRecordID, String sEntryName, String sDirection, String sCategoryName)
    {
        this.iAccountID = iAccountID;
        this.lPrimaryKey = lPrimaryKey;
        this.lRecordID = lRecordID;
        
        initDirection(sDirection);
        initSimpleStringProperties(lPrimaryKey, lRecordID, sEntryName, sDirection, sCategoryName);
    }
    //Via Database Import
    public AutoCategory(int iAccountID, String sEntryName, String sDirection, String sCategoryName)
    {
        this(iAccountID, NOT_DEFINED, NOT_DEFINED, sEntryName, sDirection, sCategoryName);
    }
    //Via Context Menu
    public AutoCategory(Entry e, Category c)
    {
        this.iAccountID = e.getAccountID();
        this.lPrimaryKey = NOT_DEFINED;
        this.lRecordID = NOT_DEFINED;
        
        String sDirection = e.getDirection() == IN ? IN_TEXT : OUT_TEXT;
        
        initDirection(sDirection);
        initSimpleStringProperties(lPrimaryKey, lRecordID, e.getName(), sDirection, c.getName());
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
    private void initSimpleStringProperties(long lPrimaryKey, long lRecordID, String sEntryName, String sDirection, String sCategoryName)
    {
        sspPrimaryKey.set(String.valueOf(lPrimaryKey));
        sspRecordID.set(String.valueOf(lRecordID));
        sspEntryName.set(sEntryName);
        sspCategoryName.set(sCategoryName);
        sspDirection.set(sDirection);
        
        setFrequency(0);
        setFirst(NOT_DEFINED);
        setLast(NOT_DEFINED);
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
    public SimpleStringProperty entryNameProperty()
    {
        return sspEntryName;
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
    public SimpleStringProperty frequencyProperty()
    {
        return sspFrequency;
    }
    @Override
    public SimpleStringProperty firstProperty()
    {
        return sspFirst;
    }
    @Override
    public SimpleStringProperty lastProperty()
    {
        return sspLast;
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
    
    //Auto Category
    public String getEntryName()
    {
        return sspEntryName.get();
    }
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
    public void setFrequency(long lFrequency)
    {
        sspFrequency.set(String.valueOf(lFrequency));
    }
    public void setFirst(int iDate)
    {
        if(iDate == NOT_DEFINED)    sspFirst.set("-");
        else                        sspFirst.set(Formatter.convert(DATE, DATABASE, TABLEVIEW, iDate));
    }
    public void setLast(int iDate)
    {
        if(iDate == NOT_DEFINED)    sspLast.set("-");
        else                        sspLast.set(Formatter.convert(DATE, DATABASE, TABLEVIEW, iDate));
    }
}
