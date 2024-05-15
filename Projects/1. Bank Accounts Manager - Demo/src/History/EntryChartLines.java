package History;

import Charts.LineChart.Line;
import Shared.Constants;
import java.util.ArrayList;
import java.util.List;

public class EntryChartLines implements Constants
{
    private List<Line> lstLines = new ArrayList<>();

    public EntryChartLines(Entry e)
    {
        lstLines.add(new Line(e, CHART_LINE_ID_ENTRIES,         NOT_DEFINED,        CHART_LINE_BUILD_BEHAVIOUR_UNALTERED,       "Entries"));
        lstLines.add(new Line(e, CHART_LINE_ID_AGGREGATE,       NOT_DEFINED,        CHART_LINE_BUILD_BEHAVIOUR_AGGREGATED,      "Aggregate"));
        lstLines.add(new Line(e, CHART_LINE_ID_PER_WEEK,        INTERVAL_WEEKS,     CHART_LINE_BUILD_BEHAVIOUR_INTERVAL,        "Per Week"));
        lstLines.add(new Line(e, CHART_LINE_ID_PER_MONTH,       INTERVAL_MONTHS,    CHART_LINE_BUILD_BEHAVIOUR_INTERVAL,        "Per Month"));
        lstLines.add(new Line(e, CHART_LINE_ID_PER_YEAR,        INTERVAL_YEARS,     CHART_LINE_BUILD_BEHAVIOUR_INTERVAL,        "Per Year"));
    }

    //External API -------------------------------------------------------------
    
    //Data Items/Series
    public void addDataItemToLines(Entry e)
    {
        for(Line l : lstLines)
            l.addDataItem(e);
    }
    public void setDataSeries()
    {
        lstLines.get(CHART_LINE_ID_PER_WEEK).setDataSeries();
        lstLines.get(CHART_LINE_ID_PER_MONTH).setDataSeries();
        lstLines.get(CHART_LINE_ID_PER_YEAR).setDataSeries();
    }
    
    //Line
    public Line getLine(int iLine)
    {
        return lstLines.get(iLine);
    }
}
