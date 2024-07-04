package History;

import DateRange.DateRange;
import Charts.StackedBarChart.Bars;
import Shared.Constants;
import java.util.ArrayList;
import java.util.List;

public class EntryChartBars implements Constants
{
    private List<Bars> lstBars = new ArrayList<>();

    public EntryChartBars(Entry e)
    {
        lstBars.add(new Bars(e, CHART_BARS_ID_PER_WEEK,    INTERVAL_WEEKS,      "Per Week"));
        lstBars.add(new Bars(e, CHART_BARS_ID_PER_MONTH,   INTERVAL_MONTHS,     "Per Month"));
        lstBars.add(new Bars(e, CHART_BARS_ID_PER_YEAR,    INTERVAL_YEARS,      "Per Year"));
    }

    //External API -------------------------------------------------------------
    
    //Data Items/Series
    public void addDataItemToBars(Entry e)
    {
        for(Bars b : lstBars)
            b.addDataItem(e);
    }
    public void setDataSeries()
    {
        for(Bars b : lstBars)
            b.setDataSeries();
    }
    
    //Bars
    public Bars getBars(int iBar)
    {
        return lstBars.get(iBar);
    }
    public Bars getRecommendedIntervalBars()
    {
        switch(DateRange.getRecommendedIntervals())
        {
            case INTERVAL_WEEKS:    return lstBars.get(CHART_BARS_ID_PER_WEEK);
            case INTERVAL_MONTHS:   return lstBars.get(CHART_BARS_ID_PER_MONTH);
            case INTERVAL_YEARS:    return lstBars.get(CHART_BARS_ID_PER_YEAR);
        }
        
        return lstBars.get(CHART_BARS_ID_PER_WEEK);
    }
}
