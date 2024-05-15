package Accounts;

import Charts.StackedBarChart.Bars;
import History.Entry;
import Shared.Constants;
import java.util.ArrayList;
import java.util.List;

public class AccountChartBars implements Constants
{
    private List<Bars> lstBars = new ArrayList<>();
    private Account a;

    public AccountChartBars(Account a)
    {
        this.a = a;
        
        lstBars.add(new Bars(a, CHART_BARS_ID_BALANCE,                                                                                                  "Balance"));
        lstBars.add(new Bars(a, CHART_BARS_ID_BALANCE_CHANGE_PER_WEEK,                  INTERVAL_WEEKS,                                                 "Balance Change - Per Week"));
        lstBars.add(new Bars(a, CHART_BARS_ID_BALANCE_CHANGE_PER_MONTH,                 INTERVAL_MONTHS,                                                "Balance Change - Per Month"));
        lstBars.add(new Bars(a, CHART_BARS_ID_BALANCE_CHANGE_PER_YEAR,                  INTERVAL_YEARS,                                                 "Balance Change - Per Year"));
        
        //Incoming
        lstBars.add(new Bars(a, CHART_BARS_ID_INCOMING_INCOME_PER_WEEK,                 INTERVAL_WEEKS,         AP_INCOMING_INCOME,                     "Income - Per Week"));
        lstBars.add(new Bars(a, CHART_BARS_ID_INCOMING_INCOME_PER_MONTH,                INTERVAL_MONTHS,        AP_INCOMING_INCOME,                     "Income - Per Month"));
        lstBars.add(new Bars(a, CHART_BARS_ID_INCOMING_INCOME_PER_YEAR,                 INTERVAL_YEARS,         AP_INCOMING_INCOME,                     "Income - Per Year"));
        
        lstBars.add(new Bars(a, CHART_BARS_ID_INCOMING_INTERNAL_TRANSFERS_PER_WEEK,     INTERVAL_WEEKS,         AP_INCOMING_INTERNAL_TRANSFER,          "Incoming Internal Transfers - Per Week"));
        lstBars.add(new Bars(a, CHART_BARS_ID_INCOMING_INTERNAL_TRANSFERS_PER_MONTH,    INTERVAL_MONTHS,        AP_INCOMING_INTERNAL_TRANSFER,          "Incoming Internal Transfers - Per Month"));
        lstBars.add(new Bars(a, CHART_BARS_ID_INCOMING_INTERNAL_TRANSFERS_PER_YEAR,     INTERVAL_YEARS,         AP_INCOMING_INTERNAL_TRANSFER,          "Incoming Internal Transfers - Per Year"));
        
        lstBars.add(new Bars(a, CHART_BARS_ID_INCOMING_TOTAL_PER_WEEK,                  INTERVAL_WEEKS,         AP_INCOMING_TOTAL,                      "Total Incoming - Per Week"));
        lstBars.add(new Bars(a, CHART_BARS_ID_INCOMING_TOTAL_PER_MONTH,                 INTERVAL_MONTHS,        AP_INCOMING_TOTAL,                      "Total Incoming - Per Month"));
        lstBars.add(new Bars(a, CHART_BARS_ID_INCOMING_TOTAL_PER_YEAR,                  INTERVAL_YEARS,         AP_INCOMING_TOTAL,                      "Total Incoming - Per Year"));
        
        lstBars.add(new Bars(a, CHART_BARS_ID_INCOMING_PER_CATEGORY,                                                                                    "Incoming Categories"));
        
        //Outgoing
        
        lstBars.add(new Bars(a, CHART_BARS_ID_OUTGOING_PURCHASES_PER_WEEK,              INTERVAL_WEEKS,         AP_OUTGOING_PURCHASE,                   "Purchases - Per Week"));
        lstBars.add(new Bars(a, CHART_BARS_ID_OUTGOING_PURCHASES_PER_MONTH,             INTERVAL_MONTHS,        AP_OUTGOING_PURCHASE,                   "Purchases - Per Month"));
        lstBars.add(new Bars(a, CHART_BARS_ID_OUTGOING_PURCHASES_PER_YEAR,              INTERVAL_YEARS,         AP_OUTGOING_PURCHASE,                   "Purchases - Per Year"));
        
        lstBars.add(new Bars(a, CHART_BARS_ID_OUTGOING_DDSO_PER_WEEK,                   INTERVAL_WEEKS,         AP_OUTGOING_DDSO,                       "DD/SO - Per Week"));
        lstBars.add(new Bars(a, CHART_BARS_ID_OUTGOING_DDSO_PER_MONTH,                  INTERVAL_MONTHS,        AP_OUTGOING_DDSO,                       "DD/SO - Per Month"));
        lstBars.add(new Bars(a, CHART_BARS_ID_OUTGOING_DDSO_PER_YEAR,                   INTERVAL_YEARS,         AP_OUTGOING_DDSO,                       "DD/SO - Per Year"));
        
        lstBars.add(new Bars(a, CHART_BARS_ID_OUTGOING_OTHER_PER_WEEK,                  INTERVAL_WEEKS,         AP_OUTGOING_OTHER,                      "Outgoing Other - Per Week"));
        lstBars.add(new Bars(a, CHART_BARS_ID_OUTGOING_OTHER_PER_MONTH,                 INTERVAL_MONTHS,        AP_OUTGOING_OTHER,                      "Outgoing Other - Per Month"));
        lstBars.add(new Bars(a, CHART_BARS_ID_OUTGOING_OTHER_PER_YEAR,                  INTERVAL_YEARS,         AP_OUTGOING_OTHER,                      "Outgoing Other - Per Year"));
        
        lstBars.add(new Bars(a, CHART_BARS_ID_OUTGOING_SPEND_PER_WEEK,                  INTERVAL_WEEKS,         AP_OUTGOING_SPEND,                      "Spend - Per Week"));
        lstBars.add(new Bars(a, CHART_BARS_ID_OUTGOING_SPEND_PER_MONTH,                 INTERVAL_MONTHS,        AP_OUTGOING_SPEND,                      "Spend - Per Month"));
        lstBars.add(new Bars(a, CHART_BARS_ID_OUTGOING_SPEND_PER_YEAR,                  INTERVAL_YEARS,         AP_OUTGOING_SPEND,                      "Spend - Per Year"));
 
        lstBars.add(new Bars(a, CHART_BARS_ID_OUTGOING_INTERNAL_TRANSFERS_PER_WEEK,     INTERVAL_WEEKS,         AP_OUTGOING_INTERNAL_TRANSFER,          "Outgoing Internal Transfers - Per Week"));
        lstBars.add(new Bars(a, CHART_BARS_ID_OUTGOING_INTERNAL_TRANSFERS_PER_MONTH,    INTERVAL_MONTHS,        AP_OUTGOING_INTERNAL_TRANSFER,          "Outgoing Internal Transfers - Per Month"));
        lstBars.add(new Bars(a, CHART_BARS_ID_OUTGOING_INTERNAL_TRANSFERS_PER_YEAR,     INTERVAL_YEARS,         AP_OUTGOING_INTERNAL_TRANSFER,          "Outgoing Internal Transfers - Per Year"));
        
        lstBars.add(new Bars(a, CHART_BARS_ID_OUTGOING_TOTAL_PER_WEEK,                  INTERVAL_WEEKS,         AP_OUTGOING_TOTAL,                      "Total Outgoing - Per Week"));
        lstBars.add(new Bars(a, CHART_BARS_ID_OUTGOING_TOTAL_PER_MONTH,                 INTERVAL_MONTHS,        AP_OUTGOING_TOTAL,                      "Total Outgoing - Per Month"));
        lstBars.add(new Bars(a, CHART_BARS_ID_OUTGOING_TOTAL_PER_YEAR,                  INTERVAL_YEARS,         AP_OUTGOING_TOTAL,                      "Total Outgoing - Per Year"));
        
        lstBars.add(new Bars(a, CHART_BARS_ID_OUTGOING_PER_CATEGORY,                                                                                    "Outgoing Categories"));  
    }
    
    //Internal -----------------------------------------------------------------
    private void buildIntervalBars(int iBarsID)
    {
        Bars b = lstBars.get(iBarsID);
        
        for(Entry e : a.getEntriesByAccountProperty(b.getAccountProperty()))
            b.addDataItem(e);
        
        b.setDataSeries();
    }
    private void buildBalanceChangeBars(int iIntervalSize)
    {
        Bars bIncome = getBars(CHART_BARS_ID_INCOMING_INCOME_PER_WEEK);
        Bars bSpend = getBars(CHART_BARS_ID_OUTGOING_SPEND_PER_WEEK);
        
        switch(iIntervalSize)
        {
            case INTERVAL_WEEKS:    bIncome = getBars(CHART_BARS_ID_INCOMING_INCOME_PER_WEEK);         break;
            case INTERVAL_MONTHS:   bIncome = getBars(CHART_BARS_ID_INCOMING_INCOME_PER_MONTH);        break;
            case INTERVAL_YEARS:    bIncome = getBars(CHART_BARS_ID_INCOMING_INCOME_PER_YEAR);         break;
        }
        
        switch(iIntervalSize)
        {
            case INTERVAL_WEEKS:    bSpend = getBars(CHART_BARS_ID_OUTGOING_SPEND_PER_WEEK);           break;
            case INTERVAL_MONTHS:   bSpend = getBars(CHART_BARS_ID_OUTGOING_SPEND_PER_MONTH);          break;
            case INTERVAL_YEARS:    bSpend = getBars(CHART_BARS_ID_OUTGOING_SPEND_PER_YEAR);           break;
        }
        
        switch(iIntervalSize)
        {
            case INTERVAL_WEEKS:    lstBars.get(CHART_BARS_ID_BALANCE_CHANGE_PER_WEEK).setDataSeriesForBalanceChange(bIncome.getSeries(), bSpend.getSeries());          break;
            case INTERVAL_MONTHS:   lstBars.get(CHART_BARS_ID_BALANCE_CHANGE_PER_MONTH).setDataSeriesForBalanceChange(bIncome.getSeries(), bSpend.getSeries());         break;
            case INTERVAL_YEARS:    lstBars.get(CHART_BARS_ID_BALANCE_CHANGE_PER_YEAR).setDataSeriesForBalanceChange(bIncome.getSeries(), bSpend.getSeries());          break;
        }
    }
    private Bars buildBars(int iBarsID)
    {
        switch(iBarsID)
        {
            case CHART_BARS_ID_BALANCE:                         lstBars.get(CHART_BARS_ID_BALANCE).setDataSeriesForBalanceProperties(a.getBalanceProperties());         break;
            case CHART_BARS_ID_INCOMING_PER_CATEGORY:           lstBars.get(iBarsID).setDataSeriesForCategories(a.getCategoriesNonZero(IN), IN);                        break;
            case CHART_BARS_ID_OUTGOING_PER_CATEGORY:           lstBars.get(iBarsID).setDataSeriesForCategories(a.getCategoriesNonZero(OUT), OUT);                      break;
            case CHART_BARS_ID_BALANCE_CHANGE_PER_WEEK:         buildBalanceChangeBars(INTERVAL_WEEKS);                                                                 break;
            case CHART_BARS_ID_BALANCE_CHANGE_PER_MONTH:        buildBalanceChangeBars(INTERVAL_MONTHS);                                                                break;
            case CHART_BARS_ID_BALANCE_CHANGE_PER_YEAR:         buildBalanceChangeBars(INTERVAL_YEARS);                                                                 break;
            default:                                            buildIntervalBars(iBarsID);
        }
        
        return lstBars.get(iBarsID);
    }
    
    //External API -------------------------------------------------------------
    
    //Bars
    public Bars getBars(int iBarsID)
    {
        return lstBars.get(iBarsID).isEmpty() ? buildBars(iBarsID) : lstBars.get(iBarsID);
    }
}
