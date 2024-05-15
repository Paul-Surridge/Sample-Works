package Charts.LineChart;

import Accounts.Account;
import Categories.Category;
import Charts.Charts;
import History.Entry;
import Shared.Constants;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LineChartState implements Constants{

    private List<Account> lstAccounts;
    private List<Category> lstCategories;
    private List<Entry> lstEntries;
    private List<Line> lstLinesInChart = new ArrayList<>();
    private Map<Integer, Integer> mpLineID = new HashMap<>();
    private int iSource, iDirection, iIntervalCompareSize;
    private boolean bLineValuesViewed;
    private String sTitleSuffix;
    
    public LineChartState()
    {
        initLineIDMap();
        clear();
        
        bLineValuesViewed = true;
    }
    
    //Internal -----------------------------------------------------------------
    private void init(Line l)
    {
        iSource = l.getSource();
        iDirection = l.getDirection();
        sTitleSuffix = l.getTitleSuffix();
    }
    private void initLineIDMap()
    {
        mpLineID.put(ACCOUNT, CHART_LINE_ID_BALANCE);
        mpLineID.put(ALL, CHART_LINE_ID_BALANCE);
        mpLineID.put(CATEGORY, CHART_LINE_ID_ENTRIES);
        mpLineID.put(ENTRY, CHART_LINE_ID_ENTRIES);
    }
    private String buildTitle()
    {
        String s = "";
        
        if(lstLinesInChart.isEmpty())
            return "";

        switch(iSource)
        {
            case ACCOUNT:
            {
                Charts.refreshAccountTitleBase(lstAccounts);
                
                if(lstAccounts.size() == 1)     s = lstAccounts.get(0).getName();
                else                            s = Charts.getTitleBase(ACCOUNT);
                
                break;
            }
            case CATEGORY:
            {
                if(lstCategories.size() == 1)   s = lstCategories.get(0).getNameInTable();
                else                            s = Charts.getTitleBase(CATEGORY);
                
                break;
            }
            case ENTRY:
            {
                if(lstEntries.size() == 1)      s = lstEntries.get(0).getName();
                else                            s = Charts.getTitleBase(ENTRY);
                
                break;
            }
            case ALL:                           s = Charts.getTitleBase(ALL);
        }
        
        return s + " - " + sTitleSuffix;
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
    public int getDirection()
    {
        return iDirection;
    }
    public int getLineID(int iSource)
    {
        return mpLineID.get(iSource);
    }
    public void setLineID(int iSource, int iLineID)
    {
        mpLineID.replace(iSource, iLineID);
        
        switch(iSource)
        {
            case ACCOUNT:   mpLineID.replace(ALL, iLineID);         break;
            case ALL:       mpLineID.replace(ACCOUNT, iLineID);     break;
        }
    }
    public boolean isEmpty()
    {
        for(Line l : lstLinesInChart)
            if(!l.isEmpty())
                return false;
        
        return true;
    }
    public boolean isFull()
    {
        return lstLinesInChart.size()>=MAXIMUM_NUMBER_OF_CHART_LINES;
    }
    public int getIntervalCompareSize()
    {
        return iIntervalCompareSize;
    }
    public void setIntervalCompareSize(int i)
    {
        iIntervalCompareSize = i;
    }
    public boolean isIntervalCompare()
    {
        return iIntervalCompareSize != NOT_DEFINED;
    }
    public boolean isInterval()
    {
        return lstLinesInChart.get(0).getBuildBehaviour() == CHART_LINE_BUILD_BEHAVIOUR_INTERVAL;
    }
    
    //Line Values
    public void setLineValuesViewed(boolean bState)
    {
        bLineValuesViewed = bState;
    }
    public boolean areLineValuesViewed()
    {
        return bLineValuesViewed;
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
    public void addLine(Line l)
    {
        if(lstLinesInChart.contains(l))
            return;

        if(lstLinesInChart.isEmpty())
            init(l);
        
        lstLinesInChart.add(l);
    }
    public List<Line> getLines()
    {
        return lstLinesInChart;
    }
    public Line getLine()
    {
        return lstLinesInChart.isEmpty() ? null : lstLinesInChart.get(0);
    }
    public int getNumberOfLines()
    {
        return lstLinesInChart.size();
    }
    public void clear()
    {
        lstLinesInChart.clear();
        iSource = NOT_DEFINED;
        iDirection = NOT_DEFINED;
        iIntervalCompareSize = NOT_DEFINED;
    }
}
