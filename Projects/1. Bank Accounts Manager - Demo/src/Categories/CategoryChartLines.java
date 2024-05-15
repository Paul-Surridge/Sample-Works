package Categories;

import Accounts.Account;
import Charts.LineChart.Line;
import History.Entry;
import Shared.Constants;
import java.util.ArrayList;
import java.util.List;

public class CategoryChartLines implements Constants
{
    private List<Line> lstLines = new ArrayList<>();
    private Account a;
    private Category c;
    
    public CategoryChartLines(Account a, Category c)
    {
        this.a = a;
        this.c = c;
        
        lstLines.add(new Line(c, CHART_LINE_ID_ENTRIES,         NOT_DEFINED,        CHART_LINE_BUILD_BEHAVIOUR_UNALTERED,       "Entries"));
        lstLines.add(new Line(c, CHART_LINE_ID_AGGREGATE,       NOT_DEFINED,        CHART_LINE_BUILD_BEHAVIOUR_AGGREGATED,      "Aggregate"));
        lstLines.add(new Line(c, CHART_LINE_ID_PER_WEEK,        INTERVAL_WEEKS,     CHART_LINE_BUILD_BEHAVIOUR_INTERVAL,        "Per Week"));
        lstLines.add(new Line(c, CHART_LINE_ID_PER_MONTH,       INTERVAL_MONTHS,    CHART_LINE_BUILD_BEHAVIOUR_INTERVAL,        "Per Month"));
        lstLines.add(new Line(c, CHART_LINE_ID_PER_YEAR,        INTERVAL_YEARS,     CHART_LINE_BUILD_BEHAVIOUR_INTERVAL,        "Per Year"));
    }

    //Internal -----------------------------------------------------------------
    private Line buildLine(int iLineID)
    {
        Line l = lstLines.get(iLineID);
        
        for(Entry e : a.getEntriesByDirection(l.getDirection()))
            if(e.getCategoryName().equals(c.getName()))
                l.addDataItem(e);
        
        if(l.getBuildBehaviour() == CHART_LINE_BUILD_BEHAVIOUR_INTERVAL)
            l.setDataSeries();
        
        return l;
    }
    
    //External API -------------------------------------------------------------
    public Line getLine(int iLineID)
    {
        return lstLines.get(iLineID).isEmpty() ? buildLine(iLineID) : lstLines.get(iLineID);
    }
}
