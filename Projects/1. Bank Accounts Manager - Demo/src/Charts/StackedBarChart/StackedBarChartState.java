package Charts.StackedBarChart;

import Accounts.Account;
import Accounts.Accounts;
import Categories.Category;
import Charts.Charts;
import History.Entry;
import Shared.Constants;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javafx.scene.chart.XYChart;

public class StackedBarChartState implements Constants{

    private List<Account> lstAccounts;
    private List<Category> lstCategories;
    private List<Entry> lstEntries;
    private List<Bars> lstBarsInChart = new ArrayList<>();
    private Map<Integer, Integer> mpBarsID = new HashMap<>();
    private int iSource, iDirection;
    private String sTitle;
    private boolean bBarValuesViewed;
    
    public StackedBarChartState()
    {
        initBarsIDMap();
        clear();
        
        bBarValuesViewed = true;
    }
    
    //Internal -----------------------------------------------------------------
    private void init(Bars b)
    {
        sTitle = b.getTitle();
        iSource = b.getSource();
        iDirection = b.getDirection();
    }
    private void initBarsIDMap()
    {
        mpBarsID.put(ACCOUNT, CHART_BARS_ID_BALANCE);
        mpBarsID.put(ALL, CHART_BARS_ID_BALANCE);
        mpBarsID.put(CATEGORY, CHART_BARS_ID_PER_CATEGORY);
        mpBarsID.put(ENTRY, CHART_BARS_ID_PER_ENTRY);
    }
    private boolean isSortRequired()
    {
        int iBarsID = mpBarsID.get(iSource);
        
        return (iBarsID == CHART_BARS_ID_INCOMING_PER_CATEGORY || iBarsID == CHART_BARS_ID_OUTGOING_PER_CATEGORY || iBarsID == CHART_BARS_ID_PER_CATEGORY);
    }
    private List<Bars> getBarsSorted()
    {
        List<Bars> lstBarSorted = new ArrayList<>(lstBarsInChart);
        Map<String, Double> mpBase = new TreeMap<>();
        Bars bBase = new Bars(lstBarsInChart.get(0));
        
        for(Bars b : lstBarsInChart)
            for(XYChart.Data<String, Double> di : b.getSeries().getData())
                if(!mpBase.containsKey(di.getXValue()))
                    mpBase.put(di.getXValue(), 0.0);
        
        for(XYChart.Data<String, Double> di : bBase.getSeries().getData())
            mpBase.replace(di.getXValue(), di.getYValue());
        
        bBase.replaceDataSeries(mpBase);
        bBase = moveInternalTransfersToEnd(bBase);
        
        lstBarSorted.set(0, bBase);
        
        return lstBarSorted;
    }
    private Bars moveInternalTransfersToEnd(Bars b)
    {
        Map<String, Integer> mp = new HashMap<>();
        List<String> lst = new ArrayList<>();
        
        for(Account a : Accounts.getAccounts())
            for(String s : a.getInternalTransferNames())
                mp.put(s, 0);
        
        for(XYChart.Data<String, Double> di : b.getSeries().getData())
            if(mp.containsKey(di.getXValue()))
                lst.add(di.getXValue());
        
        for(String s : lst)
        {
            XYChart.Data<String, Double> diMove = null;
            
            for(XYChart.Data<String, Double> di : b.getSeries().getData())
                if(di.getXValue().equals(s))
                {
                    diMove = di;
                    break;
                }

            if(diMove != null)
            {
                b.getSeries().getData().remove(diMove);
                b.getSeries().getData().add(diMove);
            }
        }
        
        return b;
    }
    private String buildTitle()
    {
        if(lstBarsInChart.isEmpty())
            return "";

        switch(iSource)
        {
            case ACCOUNT:
            {
                Charts.refreshAccountTitleBase(lstAccounts);
                
                if(lstAccounts.size() == 1)     return lstAccounts.get(0).getName() + " - " + sTitle;
                else                            return Charts.getTitleBase(ACCOUNT) + " - " + sTitle;
            }
            case CATEGORY:
            {
                if(lstCategories.size() == 1)   return lstCategories.get(0).getNameInTable() + " - " + sTitle;
                else                            return Charts.getTitleBase(CATEGORY) + " - " + sTitle;
            }
            case ENTRY:
            {
                if(lstEntries.size() == 1)      return lstEntries.get(0).getName() + " - " + sTitle;
                else                            return Charts.getTitleBase(ENTRY) + " - " + sTitle;
            }
            case ALL:
            {
                return Charts.getTitleBase(ALL) + " - " + sTitle;
            }
        }
        
        return "";
    }
    
    //External API -------------------------------------------------------------
    
    //State
    public String getTitle()
    {
        return buildTitle();
    }
    public int getSource()
    {
        return iSource;
    }
    public void setSource(int i)
    {
        iSource = i;
    }
    public int getDirection()
    {
        return iDirection;
    }
    public int getBarsID(int iSource)
    {
        return mpBarsID.get(iSource);
    }
    public void setBarsID(int iSource, int iBarsID)
    {
        mpBarsID.replace(iSource, iBarsID);
        
        switch(iSource)
        {
            case ACCOUNT:   mpBarsID.replace(ALL, iBarsID);         break;
            case ALL:       mpBarsID.replace(ACCOUNT, iBarsID);     break;
        }
    }
    public int getIntervalSize()
    {
        if(lstBarsInChart.isEmpty())
            return NOT_DEFINED;
        
        return lstBarsInChart.get(0).getIntervalSize();
    }
    public int numberOfBars()
    {
        return lstBarsInChart.size();
    }
    
    //Bar Values
    public void setBarValuesViewed(boolean bState)
    {
        bBarValuesViewed = bState;
    }
    public boolean areBarValuesViewed()
    {
        return bBarValuesViewed;
    }

    //Source Components
    public void setAccounts(List<Account> lst)
    {
        lstAccounts = lst;
    }
    public List<Account> getAccounts()
    {
        return lstAccounts;
    }
    public void setCategories(List<Category> lst)
    {
        lstCategories = lst;
    }
    public List<Category> getCategories()
    {
        return lstCategories;
    }
    public void setEntries(List<Entry> lst)
    {
        lstEntries = lst;
    }
    public List<Entry> getEntries()
    {
        return lstEntries;
    }
    
    //Build Chart
    public void addBars(Bars b)
    {
        if(lstBarsInChart.contains(b))
            return;

        if(lstBarsInChart.isEmpty())
            init(b);
        
        lstBarsInChart.add(b);
    }
    public List<Bars> getBars()
    {
        if(isSortRequired())
            return getBarsSorted();
        return lstBarsInChart;
    }
    public void clear()
    {
        lstBarsInChart.clear();
        sTitle = "";
        iSource = NOT_DEFINED;
        iDirection = NOT_DEFINED;
    }
}
