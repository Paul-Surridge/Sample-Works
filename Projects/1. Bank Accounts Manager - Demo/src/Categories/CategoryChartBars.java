package Categories;

import Accounts.Account;
import DateRange.DateRange;
import Charts.StackedBarChart.Bars;
import History.Entry;
import Shared.Constants;
import java.util.ArrayList;
import java.util.List;

public class CategoryChartBars implements Constants
{
    private List<Bars> lstBars = new ArrayList<>();
    private Account a;
    
    public CategoryChartBars(Account a, Category c)
    {
        this.a = a;
        
        lstBars.add(new Bars(c, CHART_BARS_ID_PER_WEEK,    INTERVAL_WEEKS,  "Per Week"));
        lstBars.add(new Bars(c, CHART_BARS_ID_PER_MONTH,   INTERVAL_MONTHS, "Per Month"));
        lstBars.add(new Bars(c, CHART_BARS_ID_PER_YEAR,    INTERVAL_YEARS,  "Per Year"));
    }

    //Internal -----------------------------------------------------------------
    private Bars buildIntervalBars(int iBarsID)
    {
        Bars b = lstBars.get(iBarsID);
        
        for(Entry e : a.getEntriesByDirection(b.getDirection()))
            if(e.getCategoryName().equals(b.getCategory().getName()))
                b.addDataItem(e);
        
        b.setDataSeries();
        
        return b;
    }
    
    //External API -------------------------------------------------------------
    public Bars getBars(int iBarsID)
    {
        return lstBars.get(iBarsID).isEmpty() ? buildIntervalBars(iBarsID) : lstBars.get(iBarsID);
    }
    public Bars getRecommendedIntervalBars()
    {
        switch(DateRange.getRecommendedIntervals())
        {
            case INTERVAL_WEEKS:    return getBars(CHART_BARS_ID_PER_WEEK);
            case INTERVAL_MONTHS:   return getBars(CHART_BARS_ID_PER_MONTH);
            case INTERVAL_YEARS:    return getBars(CHART_BARS_ID_PER_YEAR);
        }
        
        return getBars(CHART_BARS_ID_PER_MONTH);
    }
}
