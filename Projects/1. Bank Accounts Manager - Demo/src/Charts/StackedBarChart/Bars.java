package Charts.StackedBarChart;

import DateRange.DateRange;
import Accounts.Account;
import Accounts.All.All;
import Accounts.BalanceProperty;
import Categories.Category;
import History.Entry;
import Shared.Constants;
import Shared.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.scene.chart.XYChart;

public class Bars implements Constants{
    
    private XYChart.Series<String, Double> ser;
    private Map<String, Integer> mpIntervals;
    private Account a;
    private Category c;
    private All all;
    private Entry e;
    
    private int iID, iSource, iDirection, iIntervalSize, iAccountProperty;
    private String sTitle, sLabel;
    
    public Bars(String sLabel, int iSource, int iDirection, int iID, String sTitle)
    {
        this.iID = iID;
        this.sTitle = sTitle;
        this.sLabel = sLabel;
        this.iSource = iSource;
        this.iDirection = iDirection;
        this.iIntervalSize = NOT_DEFINED;
        
        initSeries();
    }
    public Bars(Account a, int iID, String sTitle)
    {
        this.a = a;
        this.iID = iID;
        this.sTitle = sTitle;
        this.sLabel = a.getName();
        this.iSource = ACCOUNT;
        this.iIntervalSize = NOT_DEFINED;

        initSeries();
        initDirection();
    }
    public Bars(Account a, int iID, int iIntervalSize, String sTitle)
    {
        this.a = a;
        this.iID = iID;
        this.sTitle = sTitle;
        this.sLabel = a.getName();
        this.iSource = ACCOUNT;
        this.iIntervalSize = iIntervalSize;
        this.iAccountProperty = NOT_DEFINED;
        
        initSeries();
        initDirection();
        initIntervalMap();
    }
    public Bars(Account a, int iID, int iIntervalSize, int iAccountProperty, String sTitle)
    {
        this.a = a;
        this.iID = iID;
        this.sTitle = sTitle;
        this.sLabel = a.getName();
        this.iSource = ACCOUNT;
        this.iIntervalSize = iIntervalSize;
        this.iAccountProperty = iAccountProperty;
        
        initSeries();
        initDirection();
        initIntervalMap();
    }
    public Bars(Category c, int iID, int iIntervalSize, String sTitle)
    {
        this.c = c;
        this.iID = iID;
        this.sTitle = sTitle;
        this.sLabel = c.getNameInTable();
        this.iSource = CATEGORY;
        this.iIntervalSize = iIntervalSize;
        
        initSeries();
        initDirection();
        initIntervalMap();
    }
    public Bars(All all, int iID, String sTitle)
    {
        this.all = all;
        this.iID = iID;
        this.sTitle = sTitle;
        this.sLabel = "All Accounts";
        this.iSource = ALL;
        this.iIntervalSize = NOT_DEFINED;
        
        initSeries();
        initDirection();
    }
    public Bars(All all, int iID, int iIntervalSize, String sTitle)
    {
        this.all = all;
        this.iID = iID;
        this.sTitle = sTitle;
        this.sLabel = "All Accounts";
        this.iSource = ALL;
        this.iIntervalSize = iIntervalSize;
        this.iAccountProperty = NOT_DEFINED;
        
        initSeries();
        initDirection();
        initIntervalMap();
    }
    public Bars(All all, int iID, int iIntervalSize, int iAccountProperty, String sTitle)
    {
        this.all = all;
        this.iID = iID;
        this.sTitle = sTitle;
        this.sLabel = "All Accounts";
        this.iSource = ALL;
        this.iIntervalSize = iIntervalSize;
        this.iAccountProperty = iAccountProperty;
        
        initSeries();
        initDirection();
        initIntervalMap();
    }
    public Bars(Entry e, int iID, int iIntervalSize, String sTitle)
    {
        this.e = e;
        this.iID = iID;
        this.sTitle = sTitle;
        this.sLabel = e.getName() + " (" + e.getAccountNameAbbreviated() + ")";
        this.iSource = ENTRY;
        this.iIntervalSize = iIntervalSize;
        
        initSeries();
        initDirection();
        initIntervalMap();
    }
    public Bars(Bars b)
    {
        this.ser = b.getSeries();
        this.mpIntervals = b.getIntervalMap();
        this.a = b.getAccount();
        this.c = b.getCategory();
        this.all = b.getAll();
        
        this.iID = b.getID();
        this.iSource = b.getSource();
        this.iDirection = b.getDirection();
        this.iIntervalSize = b.getIntervalSize();
        this.iAccountProperty = b.getAccountProperty();
        this.sTitle = b.getTitle();
        this.sLabel = b.getLabel();
        
        ser.setName(sLabel);
    }
    
    //Internal -----------------------------------------------------------------
    private void initSeries()
    {
        ser = new XYChart.Series<>();
        ser.setName(sLabel);
    }
    private void initDirection()
    {
        switch(iSource)
        {
            case ACCOUNT:
            case ALL:
            {
                if(iID == CHART_BARS_ID_BALANCE)                                                                        iDirection = NOT_DEFINED;
                else if(iID >= CHART_BARS_ID_BALANCE_CHANGE_PER_WEEK && iID <= CHART_BARS_ID_BALANCE_CHANGE_PER_YEAR)   iDirection = NOT_DEFINED;
                else if(iID >= CHART_BARS_ID_INCOMING_INCOME_PER_WEEK && iID <= CHART_BARS_ID_INCOMING_TOTAL_PER_YEAR)  iDirection = IN;
                else                                                                                                    iDirection = OUT;
                
                break;
            }
            case CATEGORY:      iDirection = c.getDirection();      break; 
            case ENTRY:         iDirection = e.getDirection();      break; 
        }
    }
    private void initIntervalMap()
    {
        mpIntervals = new HashMap<>();
        
        for(String s : DateRange.getIntervals(iIntervalSize))
            mpIntervals.put(s, 0);
    }
    
    //External API -------------------------------------------------------------
    
    //State
    public int getAccountID()
    {
        if(iSource == ACCOUNT)      return a.getAccountID();
        else                        return c.getAccountID();
    }
    public int getID()
    {
        return iID;
    }
    public String getLabel()
    {
        return sLabel;
    }
    public int getSource()
    {
        return iSource;
    }
    public int getDirection()
    {
        return iDirection;
    }
    public int getIntervalSize()
    {
        return iIntervalSize;
    }
    public int getAccountProperty()
    {
        return iAccountProperty;
    }
    public Account getAccount()
    {
        return a;
    }
    public Category getCategory()
    {
        return c;
    }
    public All getAll()
    {
        return all;
    }
    public Entry getEntry()
    {
        return e;
    }
    public String getTitle()
    {
        return sTitle;
    }
    
    //Bars Data
    public XYChart.Series<String, Double> getSeries()
    {
        return ser;
    }
    public XYChart.Series<String, Double> getSeriesClone()
    {
        XYChart.Series<String, Double> serClone = new XYChart.Series<>();
        
        serClone.setName(sLabel);
        
        for(XYChart.Data<String, Double> di : ser.getData())
            serClone.getData().add(new XYChart.Data<>(di.getXValue(), di.getYValue()));
        
        return serClone;
    }
    public Map<String, Integer> getIntervalMap()
    {
        return mpIntervals;
    }
    public void addDataItem(Entry e)
    {
        String sXLabel = e.getIntervalLabel(iIntervalSize);
        
        mpIntervals.replace(sXLabel, mpIntervals.get(sXLabel) + e.getAmount());
    }
    public void addDataItemToIntervals(XYChart.Data<String, Double> di)
    {
        String sXLabel = di.getXValue();
        
        mpIntervals.replace(sXLabel, mpIntervals.get(sXLabel) + (int)(di.getYValue()*100));
    }
    public void setDataSeries()
    {
        for(String s : DateRange.getIntervals(iIntervalSize))
            ser.getData().add(new XYChart.Data<>(s, Formatter.convert(DATABASE, CHART, mpIntervals.get(s))));
    }
    public void setDataSeries(Map<String, Integer> mp)
    {
        for(String s : mp.keySet())
            ser.getData().add(new XYChart.Data<>(s, Formatter.convert(DATABASE, CHART, mp.get(s))));
    }
    public void setDataSeriesForCategories(List<Category> lst, int iDirection)
    {
        for(Category c : lst)
            ser.getData().add(new XYChart.Data<>(c.getNameInTable(), Formatter.convert(DATABASE, CHART, c.getTotal())));
    }
    public void setDataSeriesForBalanceProperties(List<BalanceProperty> lst)
    {
        for(BalanceProperty bp : lst)
            ser.getData().add(new XYChart.Data<>(bp.getName(), Formatter.convert(DATABASE, CHART, bp.getValue())));
    }
    public void setDataSeriesForBalanceChange(XYChart.Series<String, Double> ser1, XYChart.Series<String, Double> ser2)
    {
        ser.getData().clear();
        
        for(int i = 0 ; i<ser1.getData().size() ; i++)
        {
            int iDI1 = (int)(Formatter.round(2, ser1.getData().get(i).getYValue()) * 100);
            int iDI2 = (int)(Formatter.round(2, ser2.getData().get(i).getYValue()) * 100);
            
            ser.getData().add(new XYChart.Data<>(ser1.getData().get(i).getXValue(), Formatter.convert(DATABASE, CHART, iDI1 - iDI2)));
        }
    }
    public void setDataSeriesForEntries(List<Entry> lst)
    {
        for(Entry e : lst)
            ser.getData().add(new XYChart.Data<>(e.getName(), Formatter.convert(DATABASE, CHART, e.getTotal())));
    }
    public void replaceDataSeries(Map<String, Double> mp)
    {
        XYChart.Series<String, Double> serReplace = new XYChart.Series<>();
        
        serReplace.setName(sLabel);
        
        for(String s : mp.keySet())
            serReplace.getData().add(new XYChart.Data<>(s, mp.get(s)));
        
        ser = serReplace;
    }
    public boolean isEmpty()
    {
        double dTotal = 0.0;
        
        if(ser.getData().isEmpty())
            return true;
        
        for(XYChart.Data<String, Double> di : ser.getData())
            dTotal += di.getYValue();
        
        return dTotal == 0;
    }
    public void clear()
    {
        ser.getData().clear();
    }
}
