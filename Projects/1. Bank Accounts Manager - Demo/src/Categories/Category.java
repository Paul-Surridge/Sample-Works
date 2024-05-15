package Categories;

import Accounts.Account;
import Accounts.Accounts;
import Categories.Administration.CategoriesAdministrationTableInsertable;
import History.History;
import Shared.Constants;
import Shared.Formatter;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.CheckBox;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import Database.Administration.DatabaseAdministrationCategoriesTableInsertable;
import Database.Database;

public class Category implements CategoriesTableInsertable, CategoriesAdministrationTableInsertable, DatabaseAdministrationCategoriesTableInsertable, Comparable, Constants{
    
    //Dashboard
    private SimpleStringProperty sspPrimaryKey = new SimpleStringProperty();
    private SimpleStringProperty sspRecordID = new SimpleStringProperty();
    private SimpleStringProperty sspName = new SimpleStringProperty();
    private SimpleStringProperty sspDirection = new SimpleStringProperty();
    private SimpleStringProperty sspFrequency = new SimpleStringProperty();
    private SimpleStringProperty sspTotal = new SimpleStringProperty();
    
    //Administration
    private SimpleStringProperty sspFrequencyAll = new SimpleStringProperty();
    private SimpleStringProperty sspFirstAll = new SimpleStringProperty();
    private SimpleStringProperty sspLastAll = new SimpleStringProperty();
    private SimpleStringProperty sspTotalAll = new SimpleStringProperty();
    
    private CategoryChartBars ccbChartBars;
    private CategoryChartLines cclChartLines;
    private CheckBox cbView;
    
    private long lPrimaryKey, lRecordID;
    private int iAccountID, iDirection, iFrequency, iTotal, iTotalAll;
    private int iType, iAccountProperty;
    private String sName;
    private boolean bView, bAppenedAccountName;
    
    //Via Database
    public Category(int iAccountID, long lPrimaryKey, long lRecordID, String sName, String sDirection)
    {
        this.iAccountID = iAccountID;
        this.lPrimaryKey = lPrimaryKey;
        this.lRecordID = lRecordID;
        this.sName = sName;
        this.iDirection = sDirection.equals(IN_TEXT) ? IN : OUT;
        this.iFrequency = 0;
        this.iTotal = 0;
        this.bView = true;
        this.iType = CATEGORY_TYPE_USER_DEFINED;
        this.iAccountProperty = NOT_DEFINED;
        
        initSimpleStringProperties(sName, sDirection);
    }
    //Via (Database Import) | (ContextMenu Create)
    public Category(int iAccountID, String sName, int iDirection)
    {
        this(iAccountID, NOT_DEFINED, NOT_DEFINED, sName, iDirection == IN ? IN_TEXT : OUT_TEXT);
    }
    //Via (Internal Transfer) | (Account Property Total)
    public Category(int iAccountID, String sName, int iDirection, int iType)
    {
        this(iAccountID, NOT_DEFINED, NOT_DEFINED, sName, iDirection == IN ? IN_TEXT : OUT_TEXT);
        
        this.iType = iType;
    }
    
    //Internal -----------------------------------------------------------------
    private void initSimpleStringProperties(String sName, String sDirection)
    {
        sspPrimaryKey.set(String.valueOf(lPrimaryKey));
        sspRecordID.set(String.valueOf(lRecordID));
        sspName.set(sName);
        sspDirection.set(sDirection);
        sspFrequency.set(String.valueOf(iFrequency));
        sspTotal.set(Formatter.convert(CURRENCY, DATABASE, TABLEVIEW, iTotal));
    }
    
    //External API -------------------------------------------------------------
    
    //Categories In/Out Tableview
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
    public SimpleStringProperty nameProperty()
    {
        return sspName;
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
    public SimpleStringProperty totalProperty()
    {
        return sspTotal;
    }
    
    //Administration
    @Override
    public SimpleStringProperty frequencyAllProperty()
    {
        return sspFrequencyAll;
    }
    @Override
    public SimpleStringProperty firstAllProperty()
    {
        return sspFirstAll;
    }
    @Override
    public SimpleStringProperty lastAllProperty()
    {
        return sspLastAll;
    }
    @Override
    public SimpleStringProperty totalAllProperty()
    {
        return sspTotalAll;
    }
    
    //Comparable
    @Override
    public int compareTo(Object o)
    {
        Category c = (Category)o;
        
        return sspName.get().compareTo(c.nameProperty().get());
    }
    
    //Account
    public int getAccountID()
    {
        return iAccountID;
    }
    public String getAccountName()
    {
        return Accounts.get(iAccountID).getName();
    }
    public String getAccountNameAbbreviated()
    {
        return Accounts.get(iAccountID).getNameAbbreviated();
    }
    public void appendAccountName()
    {
        if(!bAppenedAccountName)
            sspName.set(sspName.get() + " (" + getAccountNameAbbreviated() + ")");
        
        //clear the chart data so that the appended name is applied to the labels of the bars and lines
        clearChartData();
        
        bAppenedAccountName = true;
    }
    public void removeAccountName()
    {
        sspName.set(sName);
        
        bAppenedAccountName = false;
    }
    public boolean hasAppendedAccountName()
    {
        return bAppenedAccountName;
    }

    //ID/Category
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
    public String getName()
    {
        return sName;
    }
    public String getNameInTable()
    {
        return sspName.get();
    }
    public void setName(String s)
    {
        sName = s;
        sspName.set(sName);
    }
    public int getType()
    {
        return iType;
    }
    public boolean isUserDefined()
    {
        return iType == CATEGORY_TYPE_USER_DEFINED;
    }
    public boolean isUndefined()
    {
        return iType == CATEGORY_TYPE_USER_DEFINED && sName.equals(UNDEFINED_TEXT);
    }
    public boolean isInternalTransfer()
    {
        return iType == CATEGORY_TYPE_INTERNAL_TRANSFER;
    }
    public boolean isAccountProperty()
    {
        return iType == CATEGORY_TYPE_ACCOUNT_PROPERTY;
    }
    
    //Reserved Category - Account Property
    public void setAccountProperty(String sName, int iFrequency, int iTotal, int iProperty)
    {
        iAccountProperty = iProperty;
        
        this.sName = sName;
        sspName.set(sName);
        
        sspFrequency.set(String.valueOf(iFrequency));
        sspTotal.set(Formatter.convert(CURRENCY, DATABASE, TABLEVIEW, iTotal));
    }
    public int getAccountProperty()
    {
        return iAccountProperty;
    }
    
    //Direction
    public int getDirection()
    {
        return iDirection;
    }
    public String getDirectionText()
    {
        return sspDirection.get();
    }
    
    //Frequency
    public String getFrequency()
    {
        return sspFrequency.get();
    }
    public void incFrequency()
    {
        iFrequency++;
        sspFrequency.set(String.valueOf(iFrequency));
    }
    
    //Total
    public int getTotal()
    {
        return iTotal;
    }
    public void addTotal(int iAdd)
    {
        iTotal += iAdd;
        sspTotal.set(Formatter.convert(CURRENCY, DATABASE, TABLEVIEW, iTotal));
    }
    public void clearTotal()
    {
        iFrequency = 0;
        iTotal = 0;
        
        sspFrequency.set(String.valueOf(iFrequency));
        sspTotal.set(Formatter.convert(CURRENCY, DATABASE, TABLEVIEW, iTotal));
    }
    
    //Viewability
    @Override
    public boolean getView()
    {
        return bView;
    }
    public void setViaViewAll(boolean bState)
    {
        bView = bState;
        
        if(cbView != null)
            cbView.setSelected(bView);
    }
    @Override
    public void initViewCheckbox(CheckBox cb)
    {
        cbView = cb;
        
        if(isAccountProperty())
        {
            cbView.setVisible(false);
            return;
        }

        cbView.setSelected(bView);
        cbView.selectedProperty().addListener(new ChangeListener()
        {
            @Override
            public void changed(ObservableValue o, Object oldVal, Object newVal)
            {
                bView = (boolean)newVal;
                
                if(!Categories.isViewAllInProgress())
                {
                    History.refresh();
                    Categories.checkAndSynchroniseViewAll(iDirection);
                }
            }
        });
    }
    
    //Chart Bars and Lines
    public void clearChartData()
    {
        ccbChartBars = new CategoryChartBars(Accounts.get(iAccountID), this);
        cclChartLines = new CategoryChartLines(Accounts.get(iAccountID), this);
    }
    public CategoryChartBars getChartBars()
    {
        return ccbChartBars;
    }
    public CategoryChartLines getChartLines()
    {
        return cclChartLines;
    }
    
    //Administration
    public void initProperties()
    {
        Account a = Accounts.get(iAccountID);
        
        iTotalAll = 0;
        
        String sFirst = Formatter.convert(DATE, DATABASE, TABLEVIEW, Database.getFirstOccurence(a, this));
        String sLast = Formatter.convert(DATE, DATABASE, TABLEVIEW, Database.getLastOccurence(a, this));
        
        sspFrequencyAll.set(String.valueOf(Database.getFrequency(a, this)));
        sspFirstAll.set(sFirst.isEmpty() ? "-" : sFirst);
        sspLastAll.set(sLast.isEmpty() ? "-" : sLast);
        sspTotalAll.set(Formatter.convert(CURRENCY, DATABASE, TABLEVIEW, iTotalAll));
    }
    public void addTotalAll(int iAmount)
    {
        iTotalAll += iAmount;
    }
    public void setTotalAll()
    {
        sspTotalAll.set(Formatter.convert(CURRENCY, DATABASE, TABLEVIEW, iTotalAll));
    }
}
