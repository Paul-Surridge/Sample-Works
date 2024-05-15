package History;

import Accounts.Accounts;
import Statement.StatementTableInsertable;
import Shared.Constants;
import Shared.Formatter;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import Database.Administration.DatabaseAdministrationEntriesTableInsertable;
import Database.Database;
import Refunds.RefundPotentialsTableInsertable;
import java.util.HashMap;
import java.util.Map;

public class Entry implements HistoryTableInsertable, StatementTableInsertable, RefundPotentialsTableInsertable, DatabaseAdministrationEntriesTableInsertable, Constants{
    
    private SimpleStringProperty sspAccountName = new SimpleStringProperty();
    private SimpleStringProperty sspAccountNameAbbreviated = new SimpleStringProperty();
    private SimpleStringProperty sspPrimaryKey = new SimpleStringProperty();
    private SimpleStringProperty sspRecordID = new SimpleStringProperty();
    private SimpleStringProperty sspDate = new SimpleStringProperty();
    private SimpleStringProperty sspName = new SimpleStringProperty();
    private SimpleStringProperty sspType = new SimpleStringProperty();
    private SimpleStringProperty sspCategoryName = new SimpleStringProperty();
    private SimpleStringProperty sspIn = new SimpleStringProperty();
    private SimpleStringProperty sspOut = new SimpleStringProperty();
    private SimpleStringProperty sspBalance = new SimpleStringProperty();
    
    private Map<Integer, String> mpIntervalLabels = new HashMap<>();
    
    private long lPrimaryKey, lRecordID;
    private int iAccountID, iDate, iIn, iOut, iBalance, iTotal, iDirection, iAmount, iAccountProperty;
    private int iDateDay, iDateMonth, iDateYear;
    private String sDateTableView, sDateChart, sDateDay, sDateMonth, sDateMonthName, sDateYear, sDirection, sLine, sAmount;
    
    private EntryChartBars ecbChartBars;
    private EntryChartLines eclChartLines;
    
    //Instantiation from Database
    public Entry(int iAccountID, long lPrimaryKey, long lRecordID, int iDate, String sName, String sType, String sCategoryName, int iIn, int iOut, int iBalance)
    {
        this.iAccountID = iAccountID;
        this.lPrimaryKey = lPrimaryKey;
        this.lRecordID = lRecordID;
        this.iDate = iDate;
        this.iIn = iIn;
        this.iOut = iOut;
        this.iBalance = iBalance;
        this.iTotal = 0;
        this.iAccountProperty = NOT_DEFINED;
        
        initDateDayMonthYear();
        initDirection();
        initAmount();
        initSimpleStringProperties(Accounts.get(iAccountID).getName(), Accounts.get(iAccountID).getNameAbbreviated(), sName, sType, sCategoryName);
        initAccountProperty();
    }
    //Instantiation from Database Import
    public Entry(int iAccountID, int iDate, String sName, String sType, String sCategoryName, int iIn, int iOut, int iBalance, String sLine)
    {
        this(iAccountID, NOT_DEFINED, NOT_DEFINED, iDate, sName, sType, sCategoryName, iIn, iOut, iBalance);
        
        this.sLine = sLine;
    }
    //Instantiation from Statement
    public Entry(int iAccountID, String sDate, String sName, String sType, String sCategoryName, int iIn, int iOut, int iBalance)
    {
        this(iAccountID, NOT_DEFINED, NOT_DEFINED, Formatter.convert(DATE, STATEMENT, DATABASE, sDate), sName, sType, sCategoryName, iIn, iOut, iBalance);
    }
    
    //Internal -----------------------------------------------------------------
    private void initDateDayMonthYear()
    {
        iDateYear = iDate / 10000;
        iDateMonth = iDate % 10000;
        iDateDay = iDateMonth % 100;
        iDateMonth = iDateMonth / 100;
        
        sDateDay = String.valueOf(iDateDay);
        sDateMonth = String.valueOf(iDateMonth);
        sDateYear = String.valueOf(iDateYear);
        
        sDateTableView = Formatter.convert(DATE, DATABASE, TABLEVIEW, iDate);
        sDateChart = Formatter.convert(DATE, DATABASE, CHART, iDate);
        
        sDateMonthName = Formatter.getMonthName(iDateMonth);
    }
    private void initDirection()
    {
        if(iIn == NOT_DEFINED && iOut == NOT_DEFINED)   iDirection = NOT_DEFINED;
        else if(iIn == NOT_DEFINED)                     iDirection = OUT;
        else if(iOut == NOT_DEFINED)                    iDirection = IN;
        
        switch(iDirection)
        {
            case IN:    sDirection = IN_TEXT;   break;
            case OUT:   sDirection = OUT_TEXT;  break;
            default:    sDirection = "";        break;
        }
    }
    private void initAmount()
    {
        switch(iDirection)
        {
            case IN:    iAmount = iIn;          break;
            case OUT:   iAmount = iOut;         break;
            default:    iAmount = NOT_DEFINED;
        }
        
        sAmount = Formatter.convert(CURRENCY, DATABASE, TABLEVIEW, iAmount);
    }
    private void initAccountProperty()
    {
        if(Accounts.get(iAccountID).isInternalTransferName(getCategoryName(), iDirection))
            switch(iDirection)
            {
                case IN:    iAccountProperty = AP_INCOMING_INTERNAL_TRANSFER;       break;
                case OUT:   iAccountProperty = AP_OUTGOING_INTERNAL_TRANSFER;       break;
            }
        else
            switch(iDirection)
            {
                case IN:    iAccountProperty = AP_INCOMING_INCOME;                  break;
                case OUT:
                {
                    switch(getType())
                    {
                        case "VIS":
                        case ")))":     iAccountProperty = AP_OUTGOING_PURCHASE;    break;
                        case "DD":
                        case "SO":      iAccountProperty = AP_OUTGOING_DDSO;        break;
                        default:        iAccountProperty = AP_OUTGOING_OTHER;       break;
                    }
                }
            }
    }
    private void initSimpleStringProperties(String sAccountName, String sAccountNameAbbreviated, String sName, String sType, String sCategoryName)
    {
        this.sspAccountName.set(sAccountName);
        this.sspAccountNameAbbreviated.set(sAccountNameAbbreviated);
        this.sspPrimaryKey.set(String.valueOf(lPrimaryKey));
        this.sspRecordID.set(String.valueOf(lRecordID));
        this.sspDate.set(Formatter.convert(DATE, DATABASE, TABLEVIEW, iDate));
        this.sspName.set(sName);
        this.sspType.set(sType);
        this.sspCategoryName.set(sCategoryName);
        this.sspIn.set(Formatter.convert(CURRENCY, DATABASE, TABLEVIEW, iIn));
        this.sspOut.set(Formatter.convert(CURRENCY, DATABASE, TABLEVIEW, iOut));
        this.sspBalance.set(Formatter.convert(CURRENCY, DATABASE, TABLEVIEW, iBalance));
    }
    
    //External API -------------------------------------------------------------
    
    //Tableview
    public SimpleStringProperty accountNameProperty()
    {
        return sspAccountName;
    }
    @Override
    public SimpleStringProperty accountNameAbbreviatedProperty()
    {
        return sspAccountNameAbbreviated;
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
    public SimpleStringProperty dateProperty()
    {
        return sspDate;
    }
    @Override
    public SimpleStringProperty nameProperty()
    {
        return sspName;
    }
    @Override
    public SimpleStringProperty typeProperty()
    {
        return sspType;
    }
    @Override
    public SimpleStringProperty categoryNameProperty()
    {
        return sspCategoryName;
    }
    @Override
    public SimpleStringProperty inProperty()
    {
        return sspIn;
    }
    @Override
    public SimpleStringProperty outProperty()
    {
        return sspOut;
    }
    @Override
    public SimpleStringProperty balanceProperty()
    {
        return sspBalance;
    }

    //Account
    public int getAccountID()
    {
        return iAccountID;
    }
    public String getAccountName()
    {
        return sspAccountName.get();
    }
    public String getAccountNameAbbreviated()
    {
        return sspAccountNameAbbreviated.get();
    }
    
    //ID/Entry
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
        return sspName.get();
    }
    public String getType()
    {
        return sspType.get();
    }
    public String getCategoryName()
    {
        return sspCategoryName.get();
    }
    public void setCategoryName(String sCategoryName, boolean bUpdateDatabase)
    {
        sspCategoryName.set(sCategoryName);
        
        if(bUpdateDatabase)
            Database.setCategory(this);
        
        initAccountProperty();
    }
    public int getIn()
    {
        return iIn;
    }
    public String getInText()
    {
        return sspIn.get();
    }
    public int getOut()
    {
        return iOut;
    }
    public String getOutText()
    {
        return sspOut.get();
    }
    public int getBalance()
    {
        return iBalance;
    }
    public String getBalanceText()
    {
        return sspBalance.get();
    }
    public int getTotal()
    {
        return iTotal;
    }
    public int getDirection()
    {
        return iDirection;
    }
    public String getDirectionText()
    {
        return sDirection;
    }
    public int getAmount()
    {
        return iAmount;
    }
    public String getAmountText()
    {
        return sAmount;
    }
    public int getAccountProperty()
    {
        return iAccountProperty;
    }
    public boolean isPurchase()
    {
        return iAccountProperty == AP_OUTGOING_PURCHASE;
    }
    public boolean isDDSO()
    {
        return iAccountProperty == AP_OUTGOING_DDSO;
    }
    public boolean isSpend()
    {
        return iAccountProperty == AP_OUTGOING_PURCHASE || iAccountProperty == AP_OUTGOING_DDSO || iAccountProperty == AP_OUTGOING_OTHER;
    }
    public boolean isUserDefined()
    {
        if(isUndefined())
            return false;
        
        switch(iDirection)
        {
            case IN:    return iAccountProperty != AP_INCOMING_INTERNAL_TRANSFER;
            case OUT:   return iAccountProperty != AP_OUTGOING_INTERNAL_TRANSFER;
        }
        
        return false;
    }
    public boolean isInternalTransfer()
    {
        if(isUndefined())
            return false;
        
        switch(iDirection)
        {
            case IN:    return iAccountProperty == AP_INCOMING_INTERNAL_TRANSFER;
            case OUT:   return iAccountProperty == AP_OUTGOING_INTERNAL_TRANSFER;
        }
        
        return false;
    }
    public boolean isUndefined()
    {
        return sspCategoryName.get().equals(UNDEFINED_TEXT);
    }
    
    //Date
    public int getDate()
    {
        return iDate;
    }
    public String getDateTextForTableView()
    {
        return sDateTableView;
    }
    public String getDateTextForChart()
    {
        return sDateChart;
    }
    public int getDateDay()
    {
        return iDateDay;
    }
    public int getDateMonth()
    {
        return iDateMonth;
    }
    public int getDateYear()
    {
        return iDateYear;
    }
    public String getDateDayText()
    {
        return sDateDay;
    }
    public String getDateMonthText()
    {
        return sDateMonth;
    }
    public String getDateMonthName()
    {
        return sDateMonthName;
    }
    public String getDateYearText()
    {
        return sDateYear;
    }
    public void setShowDateTextForTableView(boolean bState)
    {
        if(bState)      sspDate.set(sDateTableView);
        else            sspDate.set("");
    }
    
    //Chart Bars and Lines
    public void initChartBarsLines()
    {
        List<Entry> lst = History.getEntriesInTableView(this);
        
        ecbChartBars = new EntryChartBars(this);
        eclChartLines = new EntryChartLines(this);
        
        iTotal = 0;
        
        for(Entry e : lst)
        {
            ecbChartBars.addDataItemToBars(e);
            eclChartLines.addDataItemToLines(e);
            
            iTotal += e.getAmount();
        }
        
        ecbChartBars.setDataSeries();
        eclChartLines.setDataSeries();
    }
    public void initIntervalLabels()
    {
        mpIntervalLabels.clear();
        
        mpIntervalLabels.put(INTERVAL_WEEKS, Formatter.findIntervalLabel(this, INTERVAL_WEEKS));
        mpIntervalLabels.put(INTERVAL_MONTHS, Formatter.findIntervalLabel(this, INTERVAL_MONTHS));
        mpIntervalLabels.put(INTERVAL_YEARS, Formatter.findIntervalLabel(this, INTERVAL_YEARS));
    }
    public EntryChartBars getChartBars()
    {
        return ecbChartBars;
    }
    public EntryChartLines getChartLines()
    {
        return eclChartLines;
    }
    public String getIntervalLabel(int iInterval)
    {
        return mpIntervalLabels.get(iInterval);
    }
    
    //Database Import
    public String getLine()
    {
        return sLine;
    }
}
