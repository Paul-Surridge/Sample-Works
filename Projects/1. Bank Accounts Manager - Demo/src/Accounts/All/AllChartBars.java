package Accounts.All;

import Accounts.Account;
import Accounts.Accounts;
import Categories.Category;
import Charts.StackedBarChart.Bars;
import Shared.Constants;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javafx.scene.chart.XYChart;

public class AllChartBars implements Constants
{
    private List<Bars> lstBars = new ArrayList<>();
    private All all;

    public AllChartBars(All all)
    {
        this.all = all;
        
        lstBars.add(new Bars(all, CHART_BARS_ID_BALANCE,                                                                                                    "Balance"));
        lstBars.add(new Bars(all, CHART_BARS_ID_BALANCE_CHANGE_PER_WEEK,                    INTERVAL_WEEKS,                                                 "Balance Change - Per Week"));
        lstBars.add(new Bars(all, CHART_BARS_ID_BALANCE_CHANGE_PER_MONTH,                   INTERVAL_MONTHS,                                                "Balance Change - Per Month"));
        lstBars.add(new Bars(all, CHART_BARS_ID_BALANCE_CHANGE_PER_YEAR,                    INTERVAL_YEARS,                                                 "Balance Change - Per Year"));

        //Incoming
        lstBars.add(new Bars(all, CHART_BARS_ID_INCOMING_INCOME_PER_WEEK,                   INTERVAL_WEEKS,         AP_INCOMING_INCOME,                     "Income - Per Week"));
        lstBars.add(new Bars(all, CHART_BARS_ID_INCOMING_INCOME_PER_MONTH,                  INTERVAL_MONTHS,        AP_INCOMING_INCOME,                     "Income - Per Month"));
        lstBars.add(new Bars(all, CHART_BARS_ID_INCOMING_INCOME_PER_YEAR,                   INTERVAL_YEARS,         AP_INCOMING_INCOME,                     "Income - Per Year"));
        
        lstBars.add(new Bars(all, CHART_BARS_ID_INCOMING_INTERNAL_TRANSFERS_PER_WEEK,       INTERVAL_WEEKS,         AP_INCOMING_INTERNAL_TRANSFER,          "Incoming Internal Transfers - Per Week"));
        lstBars.add(new Bars(all, CHART_BARS_ID_INCOMING_INTERNAL_TRANSFERS_PER_MONTH,      INTERVAL_MONTHS,        AP_INCOMING_INTERNAL_TRANSFER,          "Incoming Internal Transfers - Per Month"));
        lstBars.add(new Bars(all, CHART_BARS_ID_INCOMING_INTERNAL_TRANSFERS_PER_YEAR,       INTERVAL_YEARS,         AP_INCOMING_INTERNAL_TRANSFER,          "Incoming Internal Transfers - Per Year"));
        
        lstBars.add(new Bars(all, CHART_BARS_ID_INCOMING_TOTAL_PER_WEEK,                    INTERVAL_WEEKS,         AP_INCOMING_TOTAL,                      "Total Incoming - Per Week"));
        lstBars.add(new Bars(all, CHART_BARS_ID_INCOMING_TOTAL_PER_MONTH,                   INTERVAL_MONTHS,        AP_INCOMING_TOTAL,                      "Total Incoming - Per Month"));
        lstBars.add(new Bars(all, CHART_BARS_ID_INCOMING_TOTAL_PER_YEAR,                    INTERVAL_YEARS,         AP_INCOMING_TOTAL,                      "Total Incoming - Per Year"));
        
        lstBars.add(new Bars(all, CHART_BARS_ID_INCOMING_PER_CATEGORY,                                                                                      "Incoming Categories"));
        
        //Outgoing
        
        lstBars.add(new Bars(all, CHART_BARS_ID_OUTGOING_PURCHASES_PER_WEEK,                INTERVAL_WEEKS,         AP_OUTGOING_PURCHASE,                   "Purchases - Per Week"));
        lstBars.add(new Bars(all, CHART_BARS_ID_OUTGOING_PURCHASES_PER_MONTH,               INTERVAL_MONTHS,        AP_OUTGOING_PURCHASE,                   "Purchases - Per Month"));
        lstBars.add(new Bars(all, CHART_BARS_ID_OUTGOING_PURCHASES_PER_YEAR,                INTERVAL_YEARS,         AP_OUTGOING_PURCHASE,                   "Purchases - Per Year"));
        
        lstBars.add(new Bars(all, CHART_BARS_ID_OUTGOING_DDSO_PER_WEEK,                     INTERVAL_WEEKS,         AP_OUTGOING_DDSO,                       "DD/SO - Per Week"));
        lstBars.add(new Bars(all, CHART_BARS_ID_OUTGOING_DDSO_PER_MONTH,                    INTERVAL_MONTHS,        AP_OUTGOING_DDSO,                       "DD/SO - Per Month"));
        lstBars.add(new Bars(all, CHART_BARS_ID_OUTGOING_DDSO_PER_YEAR,                     INTERVAL_YEARS,         AP_OUTGOING_DDSO,                       "DD/SO - Per Year"));
        
        lstBars.add(new Bars(all, CHART_BARS_ID_OUTGOING_OTHER_PER_WEEK,                    INTERVAL_WEEKS,         AP_OUTGOING_OTHER,                      "Outgoing Other - Per Week"));
        lstBars.add(new Bars(all, CHART_BARS_ID_OUTGOING_OTHER_PER_MONTH,                   INTERVAL_MONTHS,        AP_OUTGOING_OTHER,                      "Outgoing Other - Per Month"));
        lstBars.add(new Bars(all, CHART_BARS_ID_OUTGOING_OTHER_PER_YEAR,                    INTERVAL_YEARS,         AP_OUTGOING_OTHER,                      "Outgoing Other - Per Year"));
        
        lstBars.add(new Bars(all, CHART_BARS_ID_OUTGOING_SPEND_PER_WEEK,                    INTERVAL_WEEKS,         AP_OUTGOING_SPEND,                      "Spend - Per Week"));
        lstBars.add(new Bars(all, CHART_BARS_ID_OUTGOING_SPEND_PER_MONTH,                   INTERVAL_MONTHS,        AP_OUTGOING_SPEND,                      "Spend - Per Month"));
        lstBars.add(new Bars(all, CHART_BARS_ID_OUTGOING_SPEND_PER_YEAR,                    INTERVAL_YEARS,         AP_OUTGOING_SPEND,                      "Spend - Per Year"));
        
        lstBars.add(new Bars(all, CHART_BARS_ID_OUTGOING_INTERNAL_TRANSFERS_PER_WEEK,       INTERVAL_WEEKS,         AP_OUTGOING_INTERNAL_TRANSFER,          "Outgoing Internal Transfers - Per Week"));
        lstBars.add(new Bars(all, CHART_BARS_ID_OUTGOING_INTERNAL_TRANSFERS_PER_MONTH,      INTERVAL_MONTHS,        AP_OUTGOING_INTERNAL_TRANSFER,          "Outgoing Internal Transfers - Per Month"));
        lstBars.add(new Bars(all, CHART_BARS_ID_OUTGOING_INTERNAL_TRANSFERS_PER_YEAR,       INTERVAL_YEARS,         AP_OUTGOING_INTERNAL_TRANSFER,          "Outgoing Internal Transfers - Per Year"));
        
        lstBars.add(new Bars(all, CHART_BARS_ID_OUTGOING_TOTAL_PER_WEEK,                    INTERVAL_WEEKS,         AP_OUTGOING_TOTAL,                      "Total Outgoing - Per Week"));
        lstBars.add(new Bars(all, CHART_BARS_ID_OUTGOING_TOTAL_PER_MONTH,                   INTERVAL_MONTHS,        AP_OUTGOING_TOTAL,                      "Total Outgoing - Per Month"));
        lstBars.add(new Bars(all, CHART_BARS_ID_OUTGOING_TOTAL_PER_YEAR,                    INTERVAL_YEARS,         AP_OUTGOING_TOTAL,                      "Total Outgoing - Per Year"));
        
        lstBars.add(new Bars(all, CHART_BARS_ID_OUTGOING_PER_CATEGORY,                                                                                      "Outgoing Categories"));
    }
    
    //Internal -----------------------------------------------------------------
    private void buildPerCategory(int iBarsID, int iDirection)
    {
        Map<String, Integer> mp = new TreeMap<>();
        
        for(Account a : Accounts.getAccounts())
            for(Category c : a.getCategoriesNonZero(iDirection))
                if(!mp.containsKey(c.getName()))    mp.put(c.getName(), c.getTotal());
                else                                mp.replace(c.getName(), mp.get(c.getName()) + c.getTotal());
        
        lstBars.get(iBarsID).setDataSeries(mp);
    }
    private void buildPerIntervals(int iBarsID)
    {
        Bars b = lstBars.get(iBarsID);
        
        for(Account a : Accounts.getAccounts())
            if(!a.isEmpty())
                for(XYChart.Data<String, Double> di : a.getChartBars().getBars(b.getID()).getSeries().getData())
                    lstBars.get(b.getID()).addDataItemToIntervals(di);

        lstBars.get(b.getID()).setDataSeries();
    }
    private Bars buildBars(int iBarsID)
    {
        switch(iBarsID)
        {
            case CHART_BARS_ID_BALANCE:                     lstBars.get(CHART_BARS_ID_BALANCE).setDataSeriesForBalanceProperties(all.getBalanceProperties());       break;
            case CHART_BARS_ID_INCOMING_PER_CATEGORY:       buildPerCategory(iBarsID, IN);                                                                          break;
            case CHART_BARS_ID_OUTGOING_PER_CATEGORY:       buildPerCategory(iBarsID, OUT);                                                                         break;
            default:                                        buildPerIntervals(iBarsID);
        }
        
        return lstBars.get(iBarsID);
    }

    //External API -------------------------------------------------------------
    public Bars getBars(int iBarsID)
    {
        return lstBars.get(iBarsID).isEmpty() ? buildBars(iBarsID) : lstBars.get(iBarsID);
    }
}
