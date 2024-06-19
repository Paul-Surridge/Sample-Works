package Accounts.All;

import Charts.LineChart.Line;
import History.Entry;
import Shared.Constants;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AllChartLines implements Constants
{
    private List<Line> lstLines = new ArrayList<>();
    private All all;
    
    public AllChartLines(All all)
    {
        this.all = all;
        
        lstLines.add(new Line(all, CHART_LINE_ID_BALANCE,                                   NOT_DEFINED,        CHART_LINE_BUILD_BEHAVIOUR_UNALTERED,   NOT_DEFINED,                        "Balance"));
        
        //Incoming
        lstLines.add(new Line(all, CHART_LINE_ID_INCOMING_INCOME_ENTRIES,                   NOT_DEFINED,        CHART_LINE_BUILD_BEHAVIOUR_UNALTERED,   AP_INCOMING_INCOME,                 "Income - Entries"));
        lstLines.add(new Line(all, CHART_LINE_ID_INCOMING_INCOME_AGGREGATE,                 NOT_DEFINED,        CHART_LINE_BUILD_BEHAVIOUR_AGGREGATED,  AP_INCOMING_INCOME,                 "Income - Aggregate"));
        lstLines.add(new Line(all, CHART_LINE_ID_INCOMING_INCOME_PER_WEEK,                  INTERVAL_WEEKS,     CHART_LINE_BUILD_BEHAVIOUR_INTERVAL,    AP_INCOMING_INCOME,                 "Income - Per Week"));
        lstLines.add(new Line(all, CHART_LINE_ID_INCOMING_INCOME_PER_MONTH,                 INTERVAL_MONTHS,    CHART_LINE_BUILD_BEHAVIOUR_INTERVAL,    AP_INCOMING_INCOME,                 "Income - Per Month"));
        lstLines.add(new Line(all, CHART_LINE_ID_INCOMING_INCOME_PER_YEAR,                  INTERVAL_YEARS,     CHART_LINE_BUILD_BEHAVIOUR_INTERVAL,    AP_INCOMING_INCOME,                 "Income - Per Year"));
        
        lstLines.add(new Line(all, CHART_LINE_ID_INCOMING_INTERNAL_TRANSFERS_ENTRIES,       NOT_DEFINED,        CHART_LINE_BUILD_BEHAVIOUR_UNALTERED,   AP_INCOMING_INTERNAL_TRANSFER,      "Incoming Internal Transfers - Entries"));
        lstLines.add(new Line(all, CHART_LINE_ID_INCOMING_INTERNAL_TRANSFERS_AGGREGATE,     NOT_DEFINED,        CHART_LINE_BUILD_BEHAVIOUR_AGGREGATED,  AP_INCOMING_INTERNAL_TRANSFER,      "Incoming Internal Transfers - Aggregate"));
        lstLines.add(new Line(all, CHART_LINE_ID_INCOMING_INTERNAL_TRANSFERS_PER_WEEK,      INTERVAL_WEEKS,     CHART_LINE_BUILD_BEHAVIOUR_INTERVAL,    AP_INCOMING_INTERNAL_TRANSFER,      "Incoming Internal Transfers - Per Week"));
        lstLines.add(new Line(all, CHART_LINE_ID_INCOMING_INTERNAL_TRANSFERS_PER_MONTH,     INTERVAL_MONTHS,    CHART_LINE_BUILD_BEHAVIOUR_INTERVAL,    AP_INCOMING_INTERNAL_TRANSFER,      "Incoming Internal Transfers - Per Month"));
        lstLines.add(new Line(all, CHART_LINE_ID_INCOMING_INTERNAL_TRANSFERS_PER_YEAR,      INTERVAL_YEARS,     CHART_LINE_BUILD_BEHAVIOUR_INTERVAL,    AP_INCOMING_INTERNAL_TRANSFER,      "Incoming Internal Transfers - Per Year"));
        
        lstLines.add(new Line(all, CHART_LINE_ID_INCOMING_TOTAL_ENTRIES,                    NOT_DEFINED,        CHART_LINE_BUILD_BEHAVIOUR_UNALTERED,   AP_INCOMING_TOTAL,                  "Total Incoming - Entries"));
        lstLines.add(new Line(all, CHART_LINE_ID_INCOMING_TOTAL_AGGREGATE,                  NOT_DEFINED,        CHART_LINE_BUILD_BEHAVIOUR_AGGREGATED,  AP_INCOMING_TOTAL,                  "Total Incoming - Aggregate"));
        lstLines.add(new Line(all, CHART_LINE_ID_INCOMING_TOTAL_PER_WEEK,                   INTERVAL_WEEKS,     CHART_LINE_BUILD_BEHAVIOUR_INTERVAL,    AP_INCOMING_TOTAL,                  "Total Incoming - Per Week"));
        lstLines.add(new Line(all, CHART_LINE_ID_INCOMING_TOTAL_PER_MONTH,                  INTERVAL_MONTHS,    CHART_LINE_BUILD_BEHAVIOUR_INTERVAL,    AP_INCOMING_TOTAL,                  "Total Incoming - Per Month"));
        lstLines.add(new Line(all, CHART_LINE_ID_INCOMING_TOTAL_PER_YEAR,                   INTERVAL_YEARS,     CHART_LINE_BUILD_BEHAVIOUR_INTERVAL,    AP_INCOMING_TOTAL,                  "Total Incoming - Per Year"));
        
        //Outgoing
        lstLines.add(new Line(all, CHART_LINE_ID_OUTGOING_PURCHASES_ENTRIES,                NOT_DEFINED,        CHART_LINE_BUILD_BEHAVIOUR_UNALTERED,   AP_OUTGOING_PURCHASE,               "Purchases - Entries"));
        lstLines.add(new Line(all, CHART_LINE_ID_OUTGOING_PURCHASES_AGGREGATE,              NOT_DEFINED,        CHART_LINE_BUILD_BEHAVIOUR_AGGREGATED,  AP_OUTGOING_PURCHASE,               "Purchases - Aggregate"));
        lstLines.add(new Line(all, CHART_LINE_ID_OUTGOING_PURCHASES_PER_WEEK,               INTERVAL_WEEKS,     CHART_LINE_BUILD_BEHAVIOUR_INTERVAL,    AP_OUTGOING_PURCHASE,               "Purchases - Per Week"));
        lstLines.add(new Line(all, CHART_LINE_ID_OUTGOING_PURCHASES_PER_MONTH,              INTERVAL_MONTHS,    CHART_LINE_BUILD_BEHAVIOUR_INTERVAL,    AP_OUTGOING_PURCHASE,               "Purchases - Per Month"));
        lstLines.add(new Line(all, CHART_LINE_ID_OUTGOING_PURCHASES_PER_YEAR,               INTERVAL_YEARS,     CHART_LINE_BUILD_BEHAVIOUR_INTERVAL,    AP_OUTGOING_PURCHASE,               "Purchases - Per Year"));

        lstLines.add(new Line(all, CHART_LINE_ID_OUTGOING_DDSO_ENTRIES,                     NOT_DEFINED,        CHART_LINE_BUILD_BEHAVIOUR_UNALTERED,   AP_OUTGOING_DDSO,                   "DDSO - Entries"));
        lstLines.add(new Line(all, CHART_LINE_ID_OUTGOING_DDSO_AGGREGATE,                   NOT_DEFINED,        CHART_LINE_BUILD_BEHAVIOUR_AGGREGATED,  AP_OUTGOING_DDSO,                   "DDSO - Aggregate"));
        lstLines.add(new Line(all, CHART_LINE_ID_OUTGOING_DDSO_PER_WEEK,                    INTERVAL_WEEKS,     CHART_LINE_BUILD_BEHAVIOUR_INTERVAL,    AP_OUTGOING_DDSO,                   "DDSO - Per Week"));
        lstLines.add(new Line(all, CHART_LINE_ID_OUTGOING_DDSO_PER_MONTH,                   INTERVAL_MONTHS,    CHART_LINE_BUILD_BEHAVIOUR_INTERVAL,    AP_OUTGOING_DDSO,                   "DDSO - Per Month"));
        lstLines.add(new Line(all, CHART_LINE_ID_OUTGOING_DDSO_PER_YEAR,                    INTERVAL_YEARS,     CHART_LINE_BUILD_BEHAVIOUR_INTERVAL,    AP_OUTGOING_DDSO,                   "DDSO - Per Year"));
        
        lstLines.add(new Line(all, CHART_LINE_ID_OUTGOING_OTHER_ENTRIES,                    NOT_DEFINED,        CHART_LINE_BUILD_BEHAVIOUR_UNALTERED,   AP_OUTGOING_OTHER,                  "Outgoing Other - Entries"));
        lstLines.add(new Line(all, CHART_LINE_ID_OUTGOING_OTHER_AGGREGATE,                  NOT_DEFINED,        CHART_LINE_BUILD_BEHAVIOUR_AGGREGATED,  AP_OUTGOING_OTHER,                  "Outgoing Other - Aggregate"));
        lstLines.add(new Line(all, CHART_LINE_ID_OUTGOING_OTHER_PER_WEEK,                   INTERVAL_WEEKS,     CHART_LINE_BUILD_BEHAVIOUR_INTERVAL,    AP_OUTGOING_OTHER,                  "Outgoing Other - Per Week"));
        lstLines.add(new Line(all, CHART_LINE_ID_OUTGOING_OTHER_PER_MONTH,                  INTERVAL_MONTHS,    CHART_LINE_BUILD_BEHAVIOUR_INTERVAL,    AP_OUTGOING_OTHER,                  "Outgoing Other - Per Month"));
        lstLines.add(new Line(all, CHART_LINE_ID_OUTGOING_OTHER_PER_YEAR,                   INTERVAL_YEARS,     CHART_LINE_BUILD_BEHAVIOUR_INTERVAL,    AP_OUTGOING_OTHER,                  "Outgoing Other - Per Year"));
        
        lstLines.add(new Line(all, CHART_LINE_ID_OUTGOING_SPEND_ENTRIES,                    NOT_DEFINED,        CHART_LINE_BUILD_BEHAVIOUR_UNALTERED,   AP_OUTGOING_SPEND,                  "Spend - Entries"));
        lstLines.add(new Line(all, CHART_LINE_ID_OUTGOING_SPEND_AGGREGATE,                  NOT_DEFINED,        CHART_LINE_BUILD_BEHAVIOUR_AGGREGATED,  AP_OUTGOING_SPEND,                  "Spend - Aggregate"));
        lstLines.add(new Line(all, CHART_LINE_ID_OUTGOING_SPEND_PER_WEEK,                   INTERVAL_WEEKS,     CHART_LINE_BUILD_BEHAVIOUR_INTERVAL,    AP_OUTGOING_SPEND,                  "Spend - Per Week"));
        lstLines.add(new Line(all, CHART_LINE_ID_OUTGOING_SPEND_PER_MONTH,                  INTERVAL_MONTHS,    CHART_LINE_BUILD_BEHAVIOUR_INTERVAL,    AP_OUTGOING_SPEND,                  "Spend - Per Month"));
        lstLines.add(new Line(all, CHART_LINE_ID_OUTGOING_SPEND_PER_YEAR,                   INTERVAL_YEARS,     CHART_LINE_BUILD_BEHAVIOUR_INTERVAL,    AP_OUTGOING_SPEND,                  "Spend - Per Year"));

        lstLines.add(new Line(all, CHART_LINE_ID_OUTGOING_INTERNAL_TRANSFERS_ENTRIES,       NOT_DEFINED,        CHART_LINE_BUILD_BEHAVIOUR_UNALTERED,   AP_OUTGOING_INTERNAL_TRANSFER,      "Outgoing Internal Transfers - Entries"));
        lstLines.add(new Line(all, CHART_LINE_ID_OUTGOING_INTERNAL_TRANSFERS_AGGREGATE,     NOT_DEFINED,        CHART_LINE_BUILD_BEHAVIOUR_AGGREGATED,  AP_OUTGOING_INTERNAL_TRANSFER,      "Outgoing Internal Transfers - Aggregate"));
        lstLines.add(new Line(all, CHART_LINE_ID_OUTGOING_INTERNAL_TRANSFERS_PER_WEEK,      INTERVAL_WEEKS,     CHART_LINE_BUILD_BEHAVIOUR_INTERVAL,    AP_OUTGOING_INTERNAL_TRANSFER,      "Outgoing Internal Transfers - Per Week"));
        lstLines.add(new Line(all, CHART_LINE_ID_OUTGOING_INTERNAL_TRANSFERS_PER_MONTH,     INTERVAL_MONTHS,    CHART_LINE_BUILD_BEHAVIOUR_INTERVAL,    AP_OUTGOING_INTERNAL_TRANSFER,      "Outgoing Internal Transfers - Per Month"));
        lstLines.add(new Line(all, CHART_LINE_ID_OUTGOING_INTERNAL_TRANSFERS_PER_YEAR,      INTERVAL_YEARS,     CHART_LINE_BUILD_BEHAVIOUR_INTERVAL,    AP_OUTGOING_INTERNAL_TRANSFER,      "Outgoing Internal Transfers - Per Year"));
        
        lstLines.add(new Line(all, CHART_LINE_ID_OUTGOING_TOTAL_ENTRIES,                    NOT_DEFINED,        CHART_LINE_BUILD_BEHAVIOUR_UNALTERED,   AP_OUTGOING_TOTAL,                  "Total Outgoing - Entries"));
        lstLines.add(new Line(all, CHART_LINE_ID_OUTGOING_TOTAL_AGGREGATE,                  NOT_DEFINED,        CHART_LINE_BUILD_BEHAVIOUR_AGGREGATED,  AP_OUTGOING_TOTAL,                  "Total Outgoing - Aggregate"));
        lstLines.add(new Line(all, CHART_LINE_ID_OUTGOING_TOTAL_PER_WEEK,                   INTERVAL_WEEKS,     CHART_LINE_BUILD_BEHAVIOUR_INTERVAL,    AP_OUTGOING_TOTAL,                  "Total Outgoing - Per Week"));
        lstLines.add(new Line(all, CHART_LINE_ID_OUTGOING_TOTAL_PER_MONTH,                  INTERVAL_MONTHS,    CHART_LINE_BUILD_BEHAVIOUR_INTERVAL,    AP_OUTGOING_TOTAL,                  "Total Outgoing - Per Month"));
        lstLines.add(new Line(all, CHART_LINE_ID_OUTGOING_TOTAL_PER_YEAR,                   INTERVAL_YEARS,     CHART_LINE_BUILD_BEHAVIOUR_INTERVAL,    AP_OUTGOING_TOTAL,                  "Total Outgoing - Per Year"));
    }

    //Internal -----------------------------------------------------------------
    private Line build(int iLineID)
    {
        Line l = lstLines.get(iLineID);
        
        for(Entry e : all.getEntriesByAccountProperty(l.getAccountProperty()))
            l.addDataItem(e);
        
        if(l.getBuildBehaviour() == CHART_LINE_BUILD_BEHAVIOUR_INTERVAL)
            l.setDataSeries();
        
        return l;
    }
    private Line buildBalance()
    {
        Map<Integer, Integer> mpBalance = new HashMap<>();
        Line l = lstLines.get(CHART_LINE_ID_BALANCE);
        
        for(Deque<Entry> dq : all.getEntriesByAccount())
            if(!dq.isEmpty())
            {
                Entry e = dq.peekFirst();
                mpBalance.put(e.getAccountID(), e.getBalance());
            }
        
        l.setAllBalanceMap(mpBalance);
        
        for(Entry e : all.getEntries())
            l.addDataItem(e);
        
        return l;
    }
    private Line buildLine(int iLineID)
    {
        switch(iLineID)
        {
            case CHART_LINE_ID_BALANCE:     return buildBalance();
            default:                        return build(iLineID);
        }
    }
    
    //External API -------------------------------------------------------------
    public Line getLine(int iLineID)
    {
        return lstLines.get(iLineID).isEmpty() ? buildLine(iLineID) : lstLines.get(iLineID);
    }
}
