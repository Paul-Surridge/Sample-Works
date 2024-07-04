package Charts.LineChart;

import Accounts.Account;
import Accounts.Accounts;
import Accounts.All.All;
import Categories.Category;
import DateRange.DateRange;
import History.Entry;
import Shared.Constants;
import Shared.Formatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javafx.scene.chart.XYChart;

public class Line implements Constants{
    
    private XYChart.Series<String, Double> ser;
    private XYChart.Data<String, Double> diLast;
    private Map<Integer, Integer> mpAllBalance;
    private Map<String, Integer> mpIntervals;
    private Account a;
    private Category c;
    private All all;
    private Entry e, ePrevious;
    private int iID, iSource, iIntervalSize, iBuildBehaviour, iAccountProperty, iDirection, iAggregateAll;
    private String sLabel, sTitleSuffix;
    
    public Line(Account a, int iID, int iIntervalSize, int iBuildBehaviour, int iAccountProperty, String sTitleSuffix)
    {
        this.a = a;
        this.iAccountProperty = iAccountProperty;
        this.iSource = ACCOUNT;
        
        init(iID, iIntervalSize, iBuildBehaviour, a.getName(), sTitleSuffix);
    }
    public Line(Category c, int iID, int iIntervalSize, int iBuildBehaviour, String sTitleSuffix)
    {
        this.a = Accounts.get(c.getAccountID());
        this.c = c;
        this.iSource = CATEGORY;
        
        init(iID, iIntervalSize, iBuildBehaviour, c.getNameInTable(), sTitleSuffix);
    }
    public Line(All all, int iID, int iIntervalSize, int iBuildBehaviour, int iAccountProperty, String sTitleSuffix)
    {
        this.all = all;
        this.iAccountProperty = iAccountProperty;
        this.iSource = ALL;
        
        init(iID, iIntervalSize, iBuildBehaviour, "All Accounts", sTitleSuffix);
    }
    public Line(Entry e, int iID, int iIntervalSize, int iBuildBehaviour, String sTitleSuffix)
    {
        this.e = e;
        this.iSource = ENTRY;
        
        init(iID, iIntervalSize, iBuildBehaviour, e.getName() + " (" + e.getAccountNameAbbreviated() + ")", sTitleSuffix);
    }
    
    //Internal -----------------------------------------------------------------
    private void init(int iID, int iIntervalSize, int iBuildBehaviour, String sLabel, String sTitleSuffix)
    {
        this.iID = iID;
        this.iIntervalSize = iIntervalSize;
        this.iBuildBehaviour = iBuildBehaviour;
        this.sLabel = sLabel;
        this.sTitleSuffix = sTitleSuffix;
        
        initSeries();
        initDirection();
        
        if(iIntervalSize != NOT_DEFINED)
            initIntervalMap();
    }
    private void initSeries()
    {
        ser = new XYChart.Series<>();
        ser.setName(sLabel);
        
        iAggregateAll = 0;
    }
    private void initDirection()
    {
        switch(iSource)
        {
            case ACCOUNT:
            case ALL:
            {
                if(iID == CHART_LINE_ID_BALANCE)                                                                        iDirection = NOT_DEFINED;
                else if(iID >= CHART_LINE_ID_INCOMING_INCOME_ENTRIES && iID <= CHART_LINE_ID_INCOMING_TOTAL_PER_YEAR)   iDirection = IN;
                else                                                                                                    iDirection = OUT;
                
                break;
            }
            case CATEGORY:  iDirection = c.getDirection();  break; 
            case ENTRY:     iDirection = e.getDirection();  break;
        }
    }
    private void initIntervalMap()
    {
        mpIntervals = new HashMap<>();
        
        for(String s : DateRange.getIntervals(iIntervalSize))
            mpIntervals.put(s, 0);
    }
    private int findAllBalanceYValue(Entry e)
    {
        int iAllBalance = 0;
        
        mpAllBalance.replace(e.getAccountID(), e.getBalance());
        
        for(int i : mpAllBalance.values())
            iAllBalance += i;
        
        return iAllBalance;
    }
    private double findDataItemYValue(Entry e)
    {
        int iValue = 0;
        
        switch(iSource)
        {
            case ACCOUNT:
            case ALL:
            {
                if(iID == CHART_LINE_ID_BALANCE)
                    switch(iSource)
                    {
                        case ACCOUNT:                                   iValue = e.getBalance();                    break;
                        case ALL:                                       iValue = findAllBalanceYValue(e);           break;
                    }
                else
                    switch(iBuildBehaviour)
                    {
                        case CHART_LINE_BUILD_BEHAVIOUR_UNALTERED:      iValue = e.getAmount();                     break;
                        case CHART_LINE_BUILD_BEHAVIOUR_AGGREGATED:     iValue = iAggregateAll += e.getAmount();    break;
                    }

                break;
            }
            case CATEGORY:
            case ENTRY:
            {
                switch(iBuildBehaviour)
                {
                    case CHART_LINE_BUILD_BEHAVIOUR_UNALTERED:          iValue = e.getAmount();                     break;
                    case CHART_LINE_BUILD_BEHAVIOUR_AGGREGATED:         iValue = iAggregateAll += e.getAmount();    break;
                }
                
                break;
            }
        }
        
        return Formatter.convert(DATABASE, CHART, iValue);
    }
    private double findCombinedDataItemYValue(Entry e)
    {
        int iValue = 0;
        
        switch(iSource)
        {
            case ACCOUNT:
            case ALL:
            {
                if(iID == CHART_LINE_ID_BALANCE)
                    switch(iSource)
                    {
                        case ACCOUNT:                                   iValue = e.getBalance();                                    break;
                        case ALL:                                       iValue = findAllBalanceYValue(e);                           break;
                    }
                else
                    switch(iBuildBehaviour)
                    {
                        case CHART_LINE_BUILD_BEHAVIOUR_UNALTERED:      iValue = (int)(diLast.getYValue()*100) + e.getAmount();     break;
                        case CHART_LINE_BUILD_BEHAVIOUR_AGGREGATED:     iValue = iAggregateAll += e.getAmount();
                    }

                break;
            }
            case CATEGORY:
            case ENTRY:
            {
                switch(iBuildBehaviour)
                {
                    case CHART_LINE_BUILD_BEHAVIOUR_UNALTERED:          iValue = (int)(diLast.getYValue()*100) + e.getAmount();     break;
                    case CHART_LINE_BUILD_BEHAVIOUR_AGGREGATED:         iValue = iAggregateAll += e.getAmount();
                }
                
                break;
            }
        }
        
        return Formatter.convert(DATABASE, CHART, iValue);
    }
    private void addToLine(Entry e)
    {
        ser.getData().add(new XYChart.Data(e.getDateTextForChart(), findDataItemYValue(e)));
    }
    private void addToLastEntryOnLine(Entry e)
    {
        diLast = getLastDataItem();
        diLast.setYValue(findCombinedDataItemYValue(e));
    }
    private void addToIntervalMap(Entry e)
    {
        String s = e.getIntervalLabel(iIntervalSize);
        
        mpIntervals.replace(s, mpIntervals.get(s) + e.getAmount());
    }
    private double findCompressed(List<Double> lst)
    {
        double dTotal = 0;
        
        for(double d : lst)
            dTotal += d;
        
        if(iID == CHART_LINE_ID_BALANCE)                                        return Formatter.round(2, dTotal/lst.size());
        else if(iBuildBehaviour == CHART_LINE_BUILD_BEHAVIOUR_AGGREGATED)       return Formatter.round(2, lst.get(lst.size()-1));
        else                                                                    return Formatter.round(2, dTotal);
    }
    
    //External API -------------------------------------------------------------
    
    //State
    public int getAccountID()
    {
        if(iSource == ACCOUNT)  return a.getAccountID();
        else                    return c.getAccountID();
    }
    public int getID()
    {
        return iID;
    }
    public int getSource()
    {
        return iSource;
    }
    public int getDirection()
    {
        return iDirection;
    }
    public int getBuildBehaviour()
    {
        return iBuildBehaviour;
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
    public String getTitleSuffix()
    {
        return sTitleSuffix;
    }

    //Line Data
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
    public XYChart.Series<String, Double> getSeriesCloneCompressed(Map<Integer, Integer> mpXAxisDatesCompressed)
    {
        XYChart.Series<String, Double> serClone = new XYChart.Series<>();
        
        Map<Integer, List<Double>> mp = new TreeMap<>();
            //Key:      Dates which will be present in this chart
            //Value:    List of all the node values which are closest to the key date
        Map<Integer, Double> mpCompressed = new TreeMap<>();
            //Key:      Dates which will be present in this chart
            //Value:    Averaged/consolidated value of all node values which are closest to the key date
        
        serClone.setName(sLabel);
        
        for(XYChart.Data<String, Double> di : ser.getData())
        {
            int iDate = Formatter.convert(DATE, CHART, DATABASE, di.getXValue());
            int iDateCompressed = mpXAxisDatesCompressed.get(iDate);

            if(mp.containsKey(iDateCompressed))
                mp.get(iDateCompressed).add(di.getYValue());
            else
            {
                List<Double> lst = new ArrayList<>();
                lst.add(di.getYValue());
                mp.put(iDateCompressed, lst);
            }
        }
        
        for(int iDate : mp.keySet())
            mpCompressed.put(iDate, findCompressed(mp.get(iDate)));
        
        for(int iDate : mpCompressed.keySet())
            serClone.getData().add(new XYChart.Data(Formatter.convert(DATE, DATABASE, CHART, iDate), mpCompressed.get(iDate)));

        return serClone;
    }
    public List<XYChart.Series<String, Double>> getSeriesCloneIntervals(int iIntervalCompareSize)
    {
        Map<String, XYChart.Series<String, Double>> mp = new HashMap<>();
            //Key:      Label of the series contained in value
            //Value:    Series which is built up containing the nodes which are within the particular interval time period
        List<XYChart.Series<String, Double>> lst = new ArrayList<>();
            //List:     List of series which are this line broken up into individual series of iIntervalCompareSize in time length which is past back and display in chart
            
        String[] sFields;
        String sIntervalLabel;
        
        for(XYChart.Data<String, Double> di : ser.getData())
        {
            sFields = di.getXValue().split("/");
            sIntervalLabel = Formatter.findIntervalLabel(Integer.valueOf(sFields[0]), Integer.valueOf(sFields[1]), Integer.valueOf(sFields[2]), iIntervalCompareSize);
            
            if(!mp.containsKey(sIntervalLabel))
            {
                XYChart.Series<String, Double> serInterval = new XYChart.Series<>();
                serInterval.setName(sIntervalLabel);
                mp.put(sIntervalLabel, serInterval);
            }
            
            switch(iIntervalCompareSize)
            {
                case INTERVAL_WEEKS:    mp.get(sIntervalLabel).getData().add(new XYChart.Data(Formatter.getDayOfWeek(sFields[0], sFields[1], sFields[2]), di.getYValue()));     break;
                case INTERVAL_MONTHS:   mp.get(sIntervalLabel).getData().add(new XYChart.Data(sFields[0], di.getYValue()));                                                     break;
            }
        }
        
        for(String s : DateRange.getIntervals(iIntervalCompareSize))
            if(mp.containsKey(s))
                lst.add(mp.get(s));
        
        if(lst.size() > MAXIMUM_NUMBER_OF_CHART_LINES)
            for(int i = lst.size() - MAXIMUM_NUMBER_OF_CHART_LINES ; i > 0 ; i--)
                lst.remove(0);
        
        return lst;
    }
    public List<XYChart.Series<String, Double>> getSeriesCloneIntervalsYears()
    {
        Map<String, Map<String, List<Double>>> mp = new TreeMap<>();
            //Key (outer):      Series label i.e. year, 2018
            //Key (Inner):      Month in which this data items occurs
            //List:             Corresponding values for this month which is then used to calculate final node value depending upon the line ID
        Map<String, Map<String, Double>> mpCompressed = new TreeMap<>();
            //Key (outer):      Series label i.e. year, 2018
            //Key (Inner):      Month in which this data items occurs
            //Double:           Final node value for this month depending upon the line ID
        List<XYChart.Series<String, Double>> lst = new ArrayList<>();
            //List:             List of series which are this line broken up into individual series of years which is past back and display in chart
            
        String[] sFields;
        String sMonth, sYear;
        
        for(XYChart.Data<String, Double> di : ser.getData())
        {
            sFields = di.getXValue().split("/");
            
            sMonth = Formatter.getMonthName(sFields[1]);
            sYear = Formatter.getYear(sFields[2]);
            
            if(!mp.containsKey(sYear))
            {
                mp.put(sYear, new HashMap<>());
                mpCompressed.put(sYear, new HashMap<>());
            }

            if(!mp.get(sYear).containsKey(sMonth))
            {
                mp.get(sYear).put(sMonth, new ArrayList<>());
                mpCompressed.get(sYear).put(sMonth, 0.0);
            }
            
            mp.get(sYear).get(sMonth).add(di.getYValue());
        }
        
        for(String sYearComp : mp.keySet())
            for(String sMonthComp : mp.get(sYearComp).keySet())
                mpCompressed.get(sYearComp).put(sMonthComp, findCompressed(mp.get(sYearComp).get(sMonthComp)));
        
        for(String sYearSer : mpCompressed.keySet())
        {
            XYChart.Series<String, Double> serYear = new XYChart.Series<>();
            
            serYear.setName(sYearSer);
            
            for(String sMonthSer : mpCompressed.get(sYearSer).keySet())
                serYear.getData().add(new XYChart.Data(sMonthSer, mpCompressed.get(sYearSer).get(sMonthSer)));
            
            lst.add(serYear);
        }
        
        if(lst.size() > MAXIMUM_NUMBER_OF_CHART_LINES)
            for(int i = lst.size() - MAXIMUM_NUMBER_OF_CHART_LINES ; i > 0 ; i--)
                lst.remove(0);
        
        return lst;
    }
    public XYChart.Data<String, Double> getLastDataItem()
    {
        return ser.getData().get(ser.getData().size()-1);
    }
    public void addDataItem(Entry e)
    {
        switch(iBuildBehaviour)
        {
            case CHART_LINE_BUILD_BEHAVIOUR_UNALTERED:
            case CHART_LINE_BUILD_BEHAVIOUR_AGGREGATED:
            {
                if(isEmpty() || e.getDate() != ePrevious.getDate())     addToLine(e);
                else                                                    addToLastEntryOnLine(e);
                
                break;
            }
            case CHART_LINE_BUILD_BEHAVIOUR_INTERVAL:
            {
                addToIntervalMap(e);
                
                break;
            }
        }
        
        ePrevious = e;
    }
    public void setDataSeries()
    {
        for(String s : DateRange.getIntervals(iIntervalSize))
            ser.getData().add(new XYChart.Data<>(s, Formatter.convert(DATABASE, CHART, mpIntervals.get(s))));
    }
    public int getNumberOfDataItems()
    {
        return ser.getData().size();
    }
    public void setAllBalanceMap(Map<Integer, Integer> mpAllBalance)
    {
        this.mpAllBalance = mpAllBalance;
    }
    public boolean isEmpty()
    {
        return ser.getData().isEmpty();
    }
    public void clear()
    {
        ser.getData().clear();
    }
}
