package Shared;

import Accounts.Account;
import Accounts.Accounts;
import AutoCategories.AutoCategories;
import AutoCategories.AutoCategory;
import Categories.Administration.CategoriesAdministration;
import Categories.Categories;
import Categories.Category;
import Charts.Charts;
import Charts.LineChart.LineChart;
import Charts.LineChart.LineChartState;
import Charts.PieChart.PieChart;
import Charts.PieChart.PieChartState;
import Charts.StackedBarChart.StackedBarChart;
import Charts.StackedBarChart.StackedBarChartState;
import DDSO.DDSO;
import Database.Administration.DatabaseAdministration;
import Database.Administration.Item;
import DateRange.DateRange;
import FullScreen.FullScreen;
import History.Entry;
import History.History;
import Integrity.Check;
import Integrity.Integrity;
import Refunds.Refund;
import Refunds.Refunds;
import Reminders.Reminder;
import Reminders.Reminders;
import Statement.Statement;
import Watches.Watch;
import Watches.Watches;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;

public class ContextMenuFactory implements Constants{
    
    //Internal -----------------------------------------------------------------
    private static void addToContextMenu(ContextMenu cm, Menu mAdd)
    {
        cm.getItems().add(mAdd);
    }
    private static void addToContextMenu(ContextMenu cm, MenuItem miAdd)
    {
        cm.getItems().add(miAdd);
    }
    private static void addToMenu(Menu m, Menu mAdd)
    {
        m.getItems().add(mAdd);
    }
    private static void addToMenu(Menu m, MenuItem miAdd)
    {
        m.getItems().add(miAdd);
    }
    
    //History/Statement
    private static Menu generateMenuForHistorySetCategory(List<Entry> lst, Category c, int iSource)
    {
        Menu m = new Menu(c.getName());
        
        addToMenu(m, generateMenuItemForHistory(lst, c, "Entry Only", ENTRY_SET_CATEGORY, iSource));
        addToMenu(m, generateMenuItemForHistory(lst, c, "All", ENTRY_SET_AUTO_CATEGORY, iSource));
        
        return m;
    }
    private static Menu generateMenuForHistorySetCategoryInternalTransfer(List<Entry> lst, int iSource)
    {
        Menu m = new Menu("Internal Transfer");
        Entry e = lst.get(0);
        
        for(Category c : Accounts.get(e.getAccountID()).getCategories(e.getDirection()))
            if(c.isInternalTransfer())
                addToMenu(m, generateMenuForHistorySetCategory(lst, c, iSource));
        
        return m;
    }
    private static Menu generateMenuForHistorySetCategories(List<Entry> lst, int iSource)
    {
        Menu m = new Menu("Set Category");
        Entry e = lst.get(0);
        
        for(Category c : Accounts.get(e.getAccountID()).getCategories(e.getDirection()))
            if(c.isUserDefined())
                switch(iSource)
                {
                    case TABLEVIEW_ID_HISTORY:      addToMenu(m, generateMenuForHistorySetCategory(lst, c, iSource));                               break;
                    case TABLEVIEW_ID_STATEMENT:    addToMenu(m, generateMenuItemForHistory(lst, c, c.getName(), ENTRY_SET_CATEGORY, iSource));     break;
                }
        
        if(m.getItems().isEmpty())
            addToMenu(m, new MenuItem("[No Categories]"));
        
        addToMenu(m, new SeparatorMenuItem());
        addToMenu(m, generateMenuForHistorySetCategoryInternalTransfer(lst, iSource));
        addToMenu(m, new SeparatorMenuItem());
        addToMenu(m, generateMenuItemForHistory(lst, "New Category", CATEGORY_CREATE));
        
        return m;
    }
    private static Menu generateMenuForHistoryFilterIncoming(List<Entry> lst)
    {
        Menu m = new Menu("Incoming");
        
        addToMenu(m, generateMenuItemForHistory(lst, "Income", FILTER_INCOMING_INCOME));
        addToMenu(m, generateMenuItemForHistory(lst, "Internal Transfers", FILTER_INCOMING_INTERNAL_TRANSFERS));
        addToMenu(m, new SeparatorMenuItem());
        addToMenu(m, generateMenuItemForHistory(lst, "All Incoming", FILTER_INCOMING_TOTAL));
        
        return m;
    }
    private static Menu generateMenuForHistoryFilterOutgoing(List<Entry> lst)
    {
        Menu m = new Menu("Outgoing");
        
        addToMenu(m, generateMenuItemForHistory(lst, "Purchases", FILTER_OUTGOING_PURCHASES));
        addToMenu(m, generateMenuItemForHistory(lst, "DDSO", FILTER_OUTGOING_DDSO));
        addToMenu(m, generateMenuItemForHistory(lst, "Other", FILTER_OUTGOING_OTHER));
        addToMenu(m, generateMenuItemForHistory(lst, "Internal Transfers", FILTER_OUTGOING_INTERNAL_TRANSFERS));
        addToMenu(m, new SeparatorMenuItem());
        addToMenu(m, generateMenuItemForHistory(lst, "All Spend", FILTER_OUTGOING_SPEND));
        addToMenu(m, generateMenuItemForHistory(lst, "All Outgoing", FILTER_OUTGOING_TOTAL));
        
        return m;
    }
    private static Menu generateMenuForHistoryFilter(List<Entry> lst)
    {
        Menu m = new Menu("Filter");
        
        addToMenu(m, generateMenuItemForHistory(lst, "Instances", FILTER_INSTANCES));
        addToMenu(m, new SeparatorMenuItem());
        addToMenu(m, generateMenuForHistoryFilterIncoming(lst));
        addToMenu(m, generateMenuForHistoryFilterOutgoing(lst));

        return m;
    }
    private static boolean entriesHaveSameDirection(List<Entry> lst)
    {
        int iDirection = lst.get(0).getDirection();
        
        for(Entry e : lst)
            if(e.getDirection() != iDirection)
                return false;
        
        return true;
    }
    
    //DDSO
    private static Menu generateMenuForDDSOShowValues()
    {
        Menu m = new Menu("Show Values");
        
        addToMenu(m, generateMenuItemForDDSO("Entry Names", SHOW_VALUES_ENTRY_NAMES));
        addToMenu(m, generateMenuItemForDDSO("Category Names", SHOW_VALUES_CATEGORY_NAMES));
        
        return m;
    }
    private static Menu generateMenuForDDSOZoom()
    {
        Menu m = new Menu("Zoom");
        
        addToMenu(m, generateMenuItemForDDSO("Zoom 75%", ZOOM_75));
        addToMenu(m, generateMenuItemForDDSO("Zoom 50%", ZOOM_50));
        addToMenu(m, generateMenuItemForDDSO("Zoom 25%", ZOOM_25));
        addToMenu(m, generateMenuItemForDDSO("Zoom 10%", ZOOM_10));
        addToMenu(m, new SeparatorMenuItem());
        addToMenu(m, generateMenuItemForDDSO("Zoom Custom", ZOOM_CUSTOM));
        addToMenu(m, generateMenuItemForDDSO("Zoom Reset", ZOOM_100));
        
        return m;
    }
    
    //Categories
    private static boolean isUserDefinedCategoryPresent(List<Category> lst)
    {
        for(Category c : lst)
            if(c.isUserDefined() && !c.isUndefined())
                return true;
        
        return false;
    }
    
    //Watches
    private static Menu generateMenuForWatches(List<Category> lst, int iDirection)
    {
        Menu m = new Menu("Create Watch");
        
        for(Category c : lst)
            if(c.isUserDefined() && !c.isUndefined())
                addToMenu(m, generateMenuItemForWatches(c));
        
        if(m.getItems().isEmpty())
        {
            switch(iDirection)
            {
                case IN:    addToMenu(m, new MenuItem("[No Incoming Categories]"));     break;
                case OUT:   addToMenu(m, new MenuItem("[No Outgoing Categories]"));     break;
            }
        }
        
        return m;
    }
    
    //Stacked Bar Chart
    private static Menu generateMenuForStackedBarChartSelectedAccountsItem(String sLabel, int iSource, int iBarsID)
    {
        Menu m = new Menu(sLabel);
        
        addToMenu(m, generateMenuItemForChartsInterval(CHART_STACKED_BAR, INTERVAL_WEEKS,  iSource, iBarsID));
        addToMenu(m, generateMenuItemForChartsInterval(CHART_STACKED_BAR, INTERVAL_MONTHS, iSource, iBarsID+1));
        addToMenu(m, generateMenuItemForChartsInterval(CHART_STACKED_BAR, INTERVAL_YEARS,  iSource, iBarsID+2));
        
        return m;
    }
    private static Menu generateMenuForStackedBarChartSelectedAccountsIncoming(int iSource)
    {
        Menu m = new Menu("Incoming");
        
        addToMenu(m, generateMenuForStackedBarChartSelectedAccountsItem("Income",               iSource, CHART_BARS_ID_INCOMING_INCOME_PER_WEEK));
        addToMenu(m, generateMenuForStackedBarChartSelectedAccountsItem("Internal Transfers",   iSource, CHART_BARS_ID_INCOMING_INTERNAL_TRANSFERS_PER_WEEK));
        addToMenu(m, new SeparatorMenuItem());
        addToMenu(m, generateMenuItemForCharts(CHART_STACKED_BAR,       "Categories",           iSource, CHART_BARS_ID_INCOMING_PER_CATEGORY));
        addToMenu(m, new SeparatorMenuItem());
        addToMenu(m, generateMenuForStackedBarChartSelectedAccountsItem("Total",                iSource, CHART_BARS_ID_INCOMING_TOTAL_PER_WEEK));

        return m;
    }
    private static Menu generateMenuForStackedBarChartSelectedAccountsOutgoing(int iSource)
    {
        Menu m = new Menu("Outgoing");
        
        addToMenu(m, generateMenuForStackedBarChartSelectedAccountsItem("Purchases",        iSource, CHART_BARS_ID_OUTGOING_PURCHASES_PER_WEEK));
        addToMenu(m, generateMenuForStackedBarChartSelectedAccountsItem("DD/SO",            iSource, CHART_BARS_ID_OUTGOING_DDSO_PER_WEEK));
        addToMenu(m, generateMenuForStackedBarChartSelectedAccountsItem("Other",            iSource, CHART_BARS_ID_OUTGOING_OTHER_PER_WEEK));
        addToMenu(m, new SeparatorMenuItem());
        addToMenu(m, generateMenuForStackedBarChartSelectedAccountsItem("Spend",            iSource, CHART_BARS_ID_OUTGOING_SPEND_PER_WEEK));
        addToMenu(m, generateMenuForStackedBarChartSelectedAccountsItem("Internal Transfers", iSource, CHART_BARS_ID_OUTGOING_INTERNAL_TRANSFERS_PER_WEEK));
        addToMenu(m, new SeparatorMenuItem());
        addToMenu(m, generateMenuItemForCharts(CHART_STACKED_BAR,       "Categories",       iSource, CHART_BARS_ID_OUTGOING_PER_CATEGORY));
        addToMenu(m, new SeparatorMenuItem());
        addToMenu(m, generateMenuForStackedBarChartSelectedAccountsItem("Total", iSource, CHART_BARS_ID_OUTGOING_TOTAL_PER_WEEK));

        return m;
    }
    private static Menu generateMenuForStackedBarChartSelectedAccounts(String sLabel)
    {
        Menu m;
        
        m = new Menu(sLabel);
        
        addToMenu(m, generateMenuItemForCharts(CHART_STACKED_BAR, "Balance", SELECTED_ACCOUNTS, CHART_BARS_ID_BALANCE));
        addToMenu(m, new SeparatorMenuItem());
        addToMenu(m, generateMenuForStackedBarChartSelectedAccountsIncoming(SELECTED_ACCOUNTS));
        addToMenu(m, generateMenuForStackedBarChartSelectedAccountsOutgoing(SELECTED_ACCOUNTS));
        addToMenu(m, new SeparatorMenuItem());
        addToMenu(m, generateMenuForStackedBarChartSelectedAccountsItem("-/+", SELECTED_ACCOUNTS, CHART_BARS_ID_BALANCE_CHANGE_PER_WEEK));
        
        return m;
    }
    private static Menu generateMenuForStackedBarChartAllAccounts()
    {
        Menu m;
        
        m = new Menu("All Accounts");
        
        addToMenu(m, generateMenuItemForCharts(CHART_STACKED_BAR, "Balance", SELECTED_ALL, CHART_BARS_ID_BALANCE));
        addToMenu(m, new SeparatorMenuItem());
        addToMenu(m, generateMenuForStackedBarChartSelectedAccountsIncoming(SELECTED_ALL));
        addToMenu(m, generateMenuForStackedBarChartSelectedAccountsOutgoing(SELECTED_ALL));
        addToMenu(m, new SeparatorMenuItem());
        addToMenu(m, generateMenuForStackedBarChartSelectedAccountsItem("-/+", SELECTED_ALL, CHART_BARS_ID_BALANCE_CHANGE_PER_WEEK));

        return m;
    }
    private static Menu generateMenuForStackedBarChartSelectedCategories(String sLabel1, String sLabel2, boolean bAppendPerCategory)
    {
        Menu m = new Menu(sLabel1);
        
        addToMenu(m, generateMenuItemForChartsInterval(CHART_STACKED_BAR, INTERVAL_WEEKS,  SELECTED_CATEGORIES, CHART_BARS_ID_PER_WEEK));
        addToMenu(m, generateMenuItemForChartsInterval(CHART_STACKED_BAR, INTERVAL_MONTHS, SELECTED_CATEGORIES, CHART_BARS_ID_PER_MONTH));
        addToMenu(m, generateMenuItemForChartsInterval(CHART_STACKED_BAR, INTERVAL_YEARS,  SELECTED_CATEGORIES, CHART_BARS_ID_PER_YEAR));
        
        if(bAppendPerCategory)
            addToMenu(m, generateMenuItemForCharts(CHART_STACKED_BAR, sLabel2, SELECTED_CATEGORIES, CHART_BARS_ID_PER_CATEGORY));
        
        return m;
    }
    private static Menu generateMenuForStackedBarChartSelectedEntries(String sLabel, boolean bAppendPerEntry)
    {
        Menu m = new Menu(sLabel);
        
        addToMenu(m, generateMenuItemForChartsInterval(CHART_STACKED_BAR, INTERVAL_WEEKS,  SELECTED_ENTRIES, CHART_BARS_ID_PER_WEEK));
        addToMenu(m, generateMenuItemForChartsInterval(CHART_STACKED_BAR, INTERVAL_MONTHS, SELECTED_ENTRIES, CHART_BARS_ID_PER_MONTH));
        addToMenu(m, generateMenuItemForChartsInterval(CHART_STACKED_BAR, INTERVAL_YEARS,  SELECTED_ENTRIES, CHART_BARS_ID_PER_YEAR));
        
        if(bAppendPerEntry)
            addToMenu(m, generateMenuItemForCharts(CHART_STACKED_BAR, "Per Entry", SELECTED_ENTRIES, CHART_BARS_ID_PER_ENTRY));
        
        return m;
    }
    private static Menu generateMenuForStackedBarChartZoom()
    {
        Menu m = new Menu("Zoom");
        
        addToMenu(m, generateMenuItemForCharts(CHART_STACKED_BAR, "75%", ZOOM_75));
        addToMenu(m, generateMenuItemForCharts(CHART_STACKED_BAR, "50%", ZOOM_50));
        addToMenu(m, generateMenuItemForCharts(CHART_STACKED_BAR, "25%", ZOOM_25));
        addToMenu(m, generateMenuItemForCharts(CHART_STACKED_BAR, "10%", ZOOM_10));
        addToMenu(m, new SeparatorMenuItem());
        addToMenu(m, generateMenuItemForCharts(CHART_STACKED_BAR, "Custom", ZOOM_CUSTOM));
        addToMenu(m, generateMenuItemForCharts(CHART_STACKED_BAR, "Reset", ZOOM_100));
        
        return m;
    }

    //Pie Chart
    private static Menu generateMenuForPieChartSelectedAccountsBalanceDistribution(int iSource)
    {
        Menu m = new Menu("Balance Distribution");
        
        addToMenu(m, generateMenuItemForCharts(CHART_PIE, "on " + Formatter.convert(DateRange.getFrom(), CHART),   iSource, CHART_PIE_ID_BALANCE_DISTRIBUTION_AT_START));
        addToMenu(m, generateMenuItemForCharts(CHART_PIE, "on " + Formatter.convert(DateRange.getTo(), CHART),     iSource, CHART_PIE_ID_BALANCE_DISTRIBUTION_AT_END));
        
        return m;
    }
    private static Menu generateMenuForPieChartSelectedAccounts(String sLabel, int iSource)
    {
        Menu m = new Menu(sLabel);

        addToMenu(m, generateMenuItemForCharts(CHART_PIE, "Incoming Categories", iSource, CHART_PIE_ID_INCOMING_CATEGORIES));
        addToMenu(m, generateMenuItemForCharts(CHART_PIE, "Outgoing Categories", iSource, CHART_PIE_ID_OUTGOING_CATEGORIES));
        addToMenu(m, new SeparatorMenuItem());
        addToMenu(m, generateMenuItemForCharts(CHART_PIE, "Income / Spend",      iSource, CHART_PIE_ID_INCOME_V_SPEND));
        addToMenu(m, generateMenuItemForCharts(CHART_PIE, "Incoming / Outgoing", iSource, CHART_PIE_ID_INCOMING_V_OUTGOING));

        return m;
    }
    private static void appendMenuItemsForPieChartSelectedCategories(ContextMenu cm, PieChartState stChart)
    {
        String s;
                
        if(stChart.getCategories().size() == 1) s = stChart.getCategories().get(0).getNameInTable();
        else                                    s = "Selected Categories";

        switch(stChart.getDirection())
        {
            case IN:
            {
                addToContextMenu(cm, generateMenuItemForCharts(CHART_PIE, s + " / Income",      SELECTED_CATEGORIES, CHART_PIE_ID_INCOMING_CATEGORIES_V_INCOME));
                addToContextMenu(cm, generateMenuItemForCharts(CHART_PIE, s + " / Incoming",    SELECTED_CATEGORIES, CHART_PIE_ID_INCOMING_CATEGORIES_V_INCOMING));

                break;
            }
            case OUT:
            {
                addToContextMenu(cm, generateMenuItemForCharts(CHART_PIE, s + " / Spend",       SELECTED_CATEGORIES, CHART_PIE_ID_OUTGOING_CATEGORIES_V_SPEND));
                addToContextMenu(cm, generateMenuItemForCharts(CHART_PIE, s + " / Outgoing",    SELECTED_CATEGORIES, CHART_PIE_ID_OUTGOING_CATEGORIES_V_OUTGOING));

                break;
            }
        }
    }
    private static void appendMenuItemsForPieChartSelectedEntries(ContextMenu cm, PieChartState stChart)
    {
        String s;
                
        if(stChart.getEntries().size() == 1)    s = stChart.getEntries().get(0).getName();
        else                                    s = "Selected Entries";

        switch(stChart.getDirection())
        {
            case IN:
            {
                addToContextMenu(cm, generateMenuItemForCharts(CHART_PIE, s + " / Income",      SELECTED_ENTRIES, CHART_PIE_ID_INCOMING_ENTRIES_V_INCOME));
                addToContextMenu(cm, generateMenuItemForCharts(CHART_PIE, s + " / Incoming",    SELECTED_ENTRIES, CHART_PIE_ID_INCOMING_ENTRIES_V_INCOMING));

                break;
            }
            case OUT:
            {
                addToContextMenu(cm, generateMenuItemForCharts(CHART_PIE, s + " / Spend",       SELECTED_ENTRIES, CHART_PIE_ID_OUTGOING_ENTRIES_V_SPEND));
                addToContextMenu(cm, generateMenuItemForCharts(CHART_PIE, s + " / Outgoing",    SELECTED_ENTRIES, CHART_PIE_ID_OUTGOING_ENTRIES_V_OUTGOING));

                break;
            }
        }
    }
    
    //Line Chart
    private static Menu generateMenuForLineChartSelectedAccountsItem(String sLabel, int iSource, int iLineID)
    {
        Menu m = new Menu(sLabel);
        
        addToMenu(m, generateMenuItemForCharts(CHART_LINE, "Per Entry",   iSource, iLineID));
        addToMenu(m, generateMenuItemForCharts(CHART_LINE, "Per Entry (Aggregate)", iSource, iLineID+1));
        addToMenu(m, new SeparatorMenuItem());
        addToMenu(m, generateMenuItemForChartsInterval(CHART_LINE, INTERVAL_WEEKS,  iSource, iLineID+2));
        addToMenu(m, generateMenuItemForChartsInterval(CHART_LINE, INTERVAL_MONTHS, iSource, iLineID+3));
        addToMenu(m, generateMenuItemForChartsInterval(CHART_LINE, INTERVAL_YEARS,  iSource, iLineID+4));
        
        return m;
    }
    private static Menu generateMenuForLineChartSelectedAccountsIncoming(int iSource)
    {
        Menu m = new Menu("Incoming");
        
        addToMenu(m, generateMenuForLineChartSelectedAccountsItem("Income",                     iSource, CHART_LINE_ID_INCOMING_INCOME_ENTRIES));
        addToMenu(m, generateMenuForLineChartSelectedAccountsItem("Internal Transfers",     iSource, CHART_LINE_ID_INCOMING_INTERNAL_TRANSFERS_ENTRIES));
        addToMenu(m, new SeparatorMenuItem());
        addToMenu(m, generateMenuForLineChartSelectedAccountsItem("Total",                  iSource, CHART_LINE_ID_INCOMING_TOTAL_ENTRIES));

        return m;
    }
    private static Menu generateMenuForLineChartSelectedAccountsOutgoing(int iSource)
    {
        Menu m = new Menu("Outgoing");
        
        addToMenu(m, generateMenuForLineChartSelectedAccountsItem("Purchases",      iSource, CHART_LINE_ID_OUTGOING_PURCHASES_ENTRIES));
        addToMenu(m, generateMenuForLineChartSelectedAccountsItem("DD/SO",          iSource, CHART_LINE_ID_OUTGOING_DDSO_ENTRIES));
        addToMenu(m, generateMenuForLineChartSelectedAccountsItem("Other",          iSource, CHART_LINE_ID_OUTGOING_OTHER_ENTRIES));
        addToMenu(m, new SeparatorMenuItem());
        addToMenu(m, generateMenuForLineChartSelectedAccountsItem("Spend",          iSource, CHART_LINE_ID_OUTGOING_SPEND_ENTRIES));
        addToMenu(m, generateMenuForLineChartSelectedAccountsItem("Internal Transfers", iSource, CHART_LINE_ID_OUTGOING_INTERNAL_TRANSFERS_ENTRIES));
        addToMenu(m, new SeparatorMenuItem());
        addToMenu(m, generateMenuForLineChartSelectedAccountsItem("Total", iSource, CHART_LINE_ID_OUTGOING_TOTAL_ENTRIES));

        return m;
    }
    private static Menu generateMenuForLineChartSelectedAccounts(String sLabel)
    {
        Menu m;
        
        m = new Menu(sLabel);
        
        addToMenu(m, generateMenuItemForCharts(CHART_LINE, "Balance", SELECTED_ACCOUNTS, CHART_LINE_ID_BALANCE));
        addToMenu(m, new SeparatorMenuItem());
        addToMenu(m, generateMenuForLineChartSelectedAccountsIncoming(SELECTED_ACCOUNTS));
        addToMenu(m, generateMenuForLineChartSelectedAccountsOutgoing(SELECTED_ACCOUNTS));
        
        return m;
    }
    private static Menu generateMenuForLineChartAllAccounts()
    {
        Menu m;
        
        m = new Menu("All Accounts");
        
        addToMenu(m, generateMenuItemForCharts(CHART_LINE, "Balance", SELECTED_ALL, CHART_LINE_ID_BALANCE));
        addToMenu(m, new SeparatorMenuItem());
        addToMenu(m, generateMenuForLineChartSelectedAccountsIncoming(SELECTED_ALL));
        addToMenu(m, generateMenuForLineChartSelectedAccountsOutgoing(SELECTED_ALL));
        
        return m;
    }
    private static Menu generateMenuForLineChartSelectedCategories(String sLabel)
    {
        Menu m = new Menu(sLabel);
        
        addToMenu(m, generateMenuItemForCharts(CHART_LINE, "Per Entry",             SELECTED_CATEGORIES, CHART_LINE_ID_ENTRIES));
        addToMenu(m, generateMenuItemForCharts(CHART_LINE, "Per Entry (Aggregate)", SELECTED_CATEGORIES, CHART_LINE_ID_AGGREGATE));
        addToMenu(m, new SeparatorMenuItem());
        addToMenu(m, generateMenuItemForChartsInterval(CHART_LINE, INTERVAL_WEEKS,  SELECTED_CATEGORIES, CHART_LINE_ID_PER_WEEK));
        addToMenu(m, generateMenuItemForChartsInterval(CHART_LINE, INTERVAL_MONTHS, SELECTED_CATEGORIES, CHART_LINE_ID_PER_MONTH));
        addToMenu(m, generateMenuItemForChartsInterval(CHART_LINE, INTERVAL_YEARS,  SELECTED_CATEGORIES, CHART_LINE_ID_PER_YEAR));
        
        return m;
    }
    private static Menu generateMenuForLineChartSelectedEntries(String sLabel)
    {
        Menu m = new Menu(sLabel);
        
        addToMenu(m, generateMenuItemForCharts(CHART_LINE, "Per Entry",             SELECTED_ENTRIES, CHART_LINE_ID_ENTRIES));
        addToMenu(m, generateMenuItemForCharts(CHART_LINE, "Per Entry (Aggregate)", SELECTED_ENTRIES, CHART_LINE_ID_AGGREGATE));
        addToMenu(m, new SeparatorMenuItem());
        addToMenu(m, generateMenuItemForChartsInterval(CHART_LINE, INTERVAL_WEEKS,  SELECTED_ENTRIES, CHART_LINE_ID_PER_WEEK));
        addToMenu(m, generateMenuItemForChartsInterval(CHART_LINE, INTERVAL_MONTHS, SELECTED_ENTRIES, CHART_LINE_ID_PER_MONTH));
        addToMenu(m, generateMenuItemForChartsInterval(CHART_LINE, INTERVAL_YEARS,  SELECTED_ENTRIES, CHART_LINE_ID_PER_YEAR));
        
        return m;
    }
    private static Menu generateMenuForLineChartIntervalCompare(String sLabel)
    {
        Menu m = new Menu(sLabel);
        
        addToMenu(m, generateMenuItemForChartsCompare(CHART_LINE, INTERVAL_WEEKS, COMPARE_WEEKS));
        addToMenu(m, generateMenuItemForChartsCompare(CHART_LINE, INTERVAL_MONTHS, COMPARE_MONTHS));
        addToMenu(m, generateMenuItemForChartsCompare(CHART_LINE, INTERVAL_YEARS, COMPARE_YEARS));
        
        return m;
    }
    private static boolean isIntervalCompareValid(LineChartState stChart)
    {
        if(stChart.getNumberOfLines() != 1 || stChart.isInterval())
            return false;
        
        if(DateRange.getIntervals(INTERVAL_WEEKS).size() > 1)       return true;
        if(DateRange.getIntervals(INTERVAL_MONTHS).size() > 1)      return true;
        if(DateRange.getIntervals(INTERVAL_YEARS).size() > 1)       return true;
        
        return false;
    }
    
    //MenuItems ----------------------------------------------------------------
    
    //Charts
    private static MenuItem generateMenuItemForChartsInterval(int iChart, int iIntervalSize, int iSource, int iLineID)
    {
        MenuItem mi;
        String sName = "";
        
        switch(iIntervalSize)
        {
            case INTERVAL_WEEKS:    sName = "Per Week";     break;
            case INTERVAL_MONTHS:   sName = "Per Month";    break;
            case INTERVAL_YEARS:    sName = "Per Year";     break;
        }
        
        mi = generateMenuItemForCharts(iChart, sName, iSource, iLineID);
        mi.setDisable(Charts.intervalsExceedChartXAxisMaximum(iIntervalSize) || DateRange.getPeriodMaximum() < iIntervalSize);
        
        return mi;
    }
    private static MenuItem generateMenuItemForChartsCompare(int iChart, int iIntervalSize, int iCompare)
    {
        MenuItem mi;
        String sName = "";
        
        switch(iCompare)
        {
            case COMPARE_WEEKS:     sName = "Weeks";     break;
            case COMPARE_MONTHS:    sName = "Months";    break;
            case COMPARE_YEARS:     sName = "Years";     break;
        }
        
        mi = generateMenuItemForCharts(iChart, sName, iCompare);
        mi.setDisable(Charts.intervalsExceedChartXAxisMaximum(iIntervalSize) || DateRange.getPeriodMaximum() < iIntervalSize);
        
        return mi;
    }
    private static MenuItem generateMenuItemForCharts(int iChart, String sLabel, int... iParam)
    {
        MenuItem miGenerate = new MenuItem(sLabel);
        
        miGenerate.setOnAction((ActionEvent ae) -> {
            
            Popup.setPosition(miGenerate);
            switch(iChart)
            {
                case CHART_STACKED_BAR:     StackedBarChart.view(iParam);   break;
                case CHART_PIE:             PieChart.view(iParam);          break;
                case CHART_LINE:            LineChart.view(iParam);         break;
            }
        });
        
        return miGenerate;
    }
    
    //Tableviews
    private static MenuItem generateMenuItemForAccounts(String sLabel)
    {
        MenuItem miGenerate = new MenuItem(sLabel);
        
        miGenerate.setOnAction((ActionEvent ae) -> {
            Accounts.sendToCharts();
        });
        
        return miGenerate;
    }
    private static MenuItem generateMenuItemForCategories(List<Category> lst, String sLabel, int... iParam)
    {
        MenuItem miGenerate = new MenuItem(sLabel);
        
        miGenerate.setOnAction((ActionEvent ae) -> {
            switch(iParam[0])
            {
                case VIEW_CATEGORIES:                       Charts.viewCategories(lst);     break;
                case VIEW_OUTGOING_DDSO_DISTRIBUTION:       DDSO.init();                    break;
            }
        });
        
        return miGenerate;
    }
    private static MenuItem generateMenuItemForHistory(List<Entry> lst, String sLabel, int... iParam)
    {
        MenuItem miGenerate = new MenuItem(sLabel);
        
        miGenerate.setOnAction((ActionEvent ae) -> {
            Popup.setPosition(miGenerate);
            switch(iParam[0])
            {
                case CATEGORY_CREATE:                           CategoriesAdministration.showDialogCreate(lst.get(0));          break;
                case FILTER_INSTANCES:                          History.search(lst.get(0).getName());                           break;
                case FILTER_INCOMING_INCOME:                    History.searchAccountProperty(AP_INCOMING_INCOME);              break;
                case FILTER_INCOMING_INTERNAL_TRANSFERS:        History.searchAccountProperty(AP_INCOMING_INTERNAL_TRANSFER);   break;
                case FILTER_INCOMING_TOTAL:                     History.searchDirection(IN);                                    break;
                case FILTER_OUTGOING_SPEND:                     History.searchAccountPropertySpend();                           break;
                case FILTER_OUTGOING_PURCHASES:                 History.searchAccountProperty(AP_OUTGOING_PURCHASE);            break;
                case FILTER_OUTGOING_DDSO:                      History.searchAccountProperty(AP_OUTGOING_DDSO);                break;
                case FILTER_OUTGOING_INTERNAL_TRANSFERS:        History.searchAccountProperty(AP_OUTGOING_INTERNAL_TRANSFER);   break;
                case FILTER_OUTGOING_OTHER:                     History.searchAccountProperty(AP_OUTGOING_OTHER);               break;
                case FILTER_OUTGOING_TOTAL:                     History.searchDirection(OUT);                                   break;
                case VIEW_ENTRIES:                              History.sendToCharts(lst);                                      break;
            }
        });
        
        return miGenerate;
    }
    private static MenuItem generateMenuItemForHistory(List<Entry> lst, Category c, String sLabel, int iAction, int iSource)
    {
        MenuItem miGenerate = new MenuItem();
        
        miGenerate.setText(sLabel);
        miGenerate.setOnAction((ActionEvent ae) -> {
            switch(iAction)
            {
                case ENTRY_SET_CATEGORY:
                {
                    switch(iSource)
                    {
                        case TABLEVIEW_ID_HISTORY:      Accounts.get(lst.get(0).getAccountID()).setCategory(lst, c);    break;
                        case TABLEVIEW_ID_STATEMENT:    Statement.setCategory(lst, c);                                  break;
                    }
                    
                    break;
                }                          
                case ENTRY_SET_AUTO_CATEGORY:           AutoCategories.set(lst, c, iSource);                            break;
            }
        });
        
        return miGenerate;
    }
    private static MenuItem generateMenuItemForDDSO(String sLabel, int... iParam)
    {
        MenuItem miGenerate = new MenuItem(sLabel);
        
        miGenerate.setOnAction((ActionEvent ae) -> {
            DDSO.view(iParam);
        });
        
        return miGenerate;
    }
    private static MenuItem generateMenuItemForWatches(Category c)
    {
        MenuItem miGenerate = new MenuItem(c.getName());
        
        miGenerate.setOnAction((ActionEvent ae) -> {
            Watches.add(c);
        });
        
        return miGenerate;
    }
    private static MenuItem generateMenuItemForWatches(String sLabel, int... iParam)
    {
        MenuItem miGenerate = new MenuItem(sLabel);
        
        miGenerate.setOnAction((ActionEvent ae) -> {
            
            Popup.setPosition(miGenerate);
            switch(iParam[0])
            {
                case WATCH_VIEW_HISTORY:        Watches.viewHistory();                      break;
                case WATCH_UPDATE:              Watches.update();                           break;
                case WATCH_DELETE:              Popup.show(POPUP_WATCH_DELETE_CONFIRM);     break;
            }
        });
        
        return miGenerate;
    }
    private static MenuItem generateMenuItemForRefunds(String sLabel, int... iParam)
    {
        MenuItem miGenerate = new MenuItem(sLabel);
        
        miGenerate.setOnAction((ActionEvent ae) -> {
            
            Popup.setPosition(miGenerate);
            switch(iParam[0])
            {
                case REFUND_CREATE:             Refunds.showDialogCreate();                 break;
                case REFUND_EDIT:               Refunds.showDialogEdit();                   break;
                case REFUND_VIEW_POTENTIAL:     Refunds.viewPotentials();                   break;
                case REFUND_VIEW_ALL_INCOMING:  Refunds.viewAllIncoming();                  break;
                case REFUND_VIEW_CONFIRMED:     Refunds.viewConfirmed();                    break;
                case REFUND_DELETE:             Popup.show(POPUP_REFUND_DELETE_CONFIRM);    break;
            }
        });
        
        return miGenerate;
    }
    private static MenuItem generateMenuItemForRefundPotentials(String sLabel, int... iParam)
    {
        MenuItem miGenerate = new MenuItem(sLabel);
        
        miGenerate.setOnAction((ActionEvent ae) -> {
            if(iParam[0] == REFUND_CONFIRM)
                Refunds.received();
        });
        
        return miGenerate;
    }
    private static MenuItem generateMenuItemForCategoriesAdministration(String sLabel, int... iParam)
    {
        MenuItem miGenerate = new MenuItem(sLabel);
        
        miGenerate.setOnAction((ActionEvent ae) -> {
            
            double dX = miGenerate.getParentPopup().getAnchorX();
            double dY = miGenerate.getParentPopup().getAnchorY();
            
            switch(iParam[0])
            {
                case CATEGORY_CREATE:           CategoriesAdministration.showDialogCreate(dX, dY);     break;
                case CATEGORY_RENAME:           CategoriesAdministration.showDialogRename(dX, dY);     break;
                case CATEGORY_DELETE:           Popup.show(dX, dY, POPUP_CATEGORY_DELETE_CONFIRM);     break;
            }
        });
        
        return miGenerate;
    }
    private static MenuItem generateMenuItemForAutoCategories(String sLabel, int... iParam)
    {
        MenuItem miGenerate = new MenuItem(sLabel);
        
        miGenerate.setOnAction((ActionEvent ae) -> {
            Popup.setPosition(miGenerate);
            if(iParam[0] == AUTO_CATEGORY_DELETE)
                Popup.show(POPUP_AUTO_CATEGORY_DELETE_CONFIRM);
        });
        
        return miGenerate;
    }
    private static MenuItem generateMenuItemForReminders(String sLabel, int... iParam)
    {
        MenuItem miGenerate = new MenuItem(sLabel);
        
        miGenerate.setOnAction((ActionEvent ae) -> {
            
            Popup.setPosition(miGenerate);
            switch(iParam[0])
            {
                case REMINDER_CREATE:       Reminders.showDialogCreate();                   break;
                case REMINDER_EDIT:         Reminders.showDialogEdit();                     break;
                case REMINDER_DELETE:       Popup.show(POPUP_REMINDER_DELETE_CONFIRM);      break;
            }
        });
        
        return miGenerate;
    }
    private static MenuItem generateMenuItemForIntegrity(String sLabel, int... iParam)
    {
        MenuItem miGenerate = new MenuItem(sLabel);
        
        miGenerate.setOnAction((ActionEvent ae) -> {
            switch(iParam[0])
            {
                case INTEGRITY_VIEW_ERRORS:         Integrity.viewErrors();         break;
                case INTEGRITY_VIEW_ITEMS:          Integrity.viewItems();          break;
            }
        });
        
        return miGenerate;
    }
    private static MenuItem generateMenuItemForDatabaseAdministration(String sLabel, int... iParam)
    {
        MenuItem miGenerate = new MenuItem(sLabel);
        
        miGenerate.setOnAction((ActionEvent ae) -> {
            
            Popup.setPosition(miGenerate);
            switch(iParam[0])
            {
                case DATABASE_OVERVIEW_VIEW:            DatabaseAdministration.showOverviewItem();          break;
                case DATABASE_DELETE_RECORDS:           Popup.show(POPUP_DATABASE_DELETE_CONFIRM);          break;
                case DATABASE_DELETE_RECORDS_ALL:       Popup.show(POPUP_DATABASE_DELETE_ALL_CONFIRM);      break;
            }
        });
        
        return miGenerate;
    }
    
    //External API -------------------------------------------------------------
    
    //Dashboard
    public static ContextMenu buildContextMenuForAccounts(List<Account> lst, int iSource)
    {
        ContextMenu cm = new ContextMenu();

        switch(iSource)
        {
            case ACCOUNT:
            {
                if(lst.size() == 1)     addToContextMenu(cm, generateMenuItemForAccounts("View Account"));
                else                    addToContextMenu(cm, generateMenuItemForAccounts("View Accounts"));
                
                break;
            }
            case ALL:
            {
                addToContextMenu(cm, generateMenuItemForAccounts("View All Accounts"));
                
                break;
            }
        }
        
        return cm;
    }
    public static ContextMenu buildContextMenuForCategories(List<Category> lst)
    {
        ContextMenu cm = new ContextMenu();

        if(lst.isEmpty() || !Categories.areSelectedValid(lst))
            return cm;
        
        Category c = lst.get(0);
        String sLabel = "";
        
        switch(c.getType())
        {
            case CATEGORY_TYPE_USER_DEFINED:            sLabel = (lst.size() == 1) ? "View Category" : "View Categories";                       break;
            case CATEGORY_TYPE_INTERNAL_TRANSFER:       sLabel = (lst.size() == 1) ? "View Internal Transfer" : "View Internal Transfers";      break;
            case CATEGORY_TYPE_ACCOUNT_PROPERTY:        sLabel = (lst.size() == 1) ? "View " + c.getName() : "";                                break;
        }
        
        if(!sLabel.isEmpty())
            addToContextMenu(cm, generateMenuItemForCategories(lst, sLabel, VIEW_CATEGORIES));

        if(lst.size() == 1 && c.getAccountProperty() == AP_OUTGOING_DDSO)
            addToContextMenu(cm, generateMenuItemForCategories(lst, sLabel + " Distribution" , VIEW_OUTGOING_DDSO_DISTRIBUTION));
        
        return cm;
    }
    public static ContextMenu buildContextMenuForStackedBarChart(StackedBarChartState stChart)
    {
        ContextMenu cm = new ContextMenu();
        
        switch(stChart.getSource())
        {
            case ACCOUNT:
            {
                if(stChart.getAccounts().size() == 1)   addToContextMenu(cm, generateMenuForStackedBarChartSelectedAccounts(stChart.getAccounts().get(0).getName()));
                else                                    addToContextMenu(cm, generateMenuForStackedBarChartSelectedAccounts("Selected Accounts"));
                
                break;
            }
            case CATEGORY:
            {
                String sLabel1 = "";
                String sLabel2 = "";
                
                Category c = stChart.getCategories().get(0);
                
                switch(c.getType())
                {
                    case CATEGORY_TYPE_USER_DEFINED:        sLabel1 = "Selected Categories";            break;
                    case CATEGORY_TYPE_INTERNAL_TRANSFER:   sLabel1 = "Selected Internal Transfers";    break;
                }
                
                switch(c.getType())
                {
                    case CATEGORY_TYPE_USER_DEFINED:        sLabel2 = "Per Category";           break;
                    case CATEGORY_TYPE_INTERNAL_TRANSFER:   sLabel2 = "Per Internal Transfer";  break;
                }
                
                if(stChart.getCategories().size() == 1)                                     addToContextMenu(cm, generateMenuForStackedBarChartSelectedCategories(c.getNameInTable(), sLabel2, false));
                else if(stChart.getCategories().size() <= MAXIMUM_NUMBER_OF_CHART_BARS)     addToContextMenu(cm, generateMenuForStackedBarChartSelectedCategories(sLabel1, sLabel2, true));

                break;
            }
            case ENTRY:
            {
                if(stChart.getEntries().size() == 1)                                        addToContextMenu(cm, generateMenuForStackedBarChartSelectedEntries(stChart.getEntries().get(0).getName(), false));
                else if(stChart.getEntries().size() <= MAXIMUM_NUMBER_OF_CHART_BARS)        addToContextMenu(cm, generateMenuForStackedBarChartSelectedEntries("Selected Entries", true));
                
                break;
            }
            case ALL:
            {
                addToContextMenu(cm, generateMenuForStackedBarChartAllAccounts());
                
                break;
            }
        }
        
        if(Windows.isLarge(WINDOW_CHART_STACKED_BAR) || FullScreen.isActive())
        {
            if(stChart.numberOfBars() == 1)
            {
                addToContextMenu(cm, new SeparatorMenuItem());
                addToContextMenu(cm, generateMenuForStackedBarChartZoom());
            }
            
            if(cm.getItems().size()>0)
                addToContextMenu(cm, new SeparatorMenuItem());
            
            addToContextMenu(cm, generateMenuItemForCharts(CHART_STACKED_BAR, "Show Values", SHOW_VALUES));
            addToContextMenu(cm, generateMenuItemForCharts(CHART_STACKED_BAR, "Hide Values", HIDE_VALUES));
            addToContextMenu(cm, new SeparatorMenuItem());
            
            if(FullScreen.isActive())                               addToContextMenu(cm, generateMenuItemForCharts(CHART_STACKED_BAR, "Close", HIDE_FULL_SCREEN));
            else if(Windows.isLarge(WINDOW_CHART_STACKED_BAR))      addToContextMenu(cm, generateMenuItemForCharts(CHART_STACKED_BAR, "View Full Screen", SHOW_FULL_SCREEN));
        }
        
        cm.setStyle("-fx-font: " + FONT_SIZE_WINDOW_CONTEXT_MENU + " calibri;");
        
        return cm;
    }
    public static ContextMenu buildContextMenuForPieChart(PieChartState stChart)
    {
        ContextMenu cm = new ContextMenu();

        switch(stChart.getSource())
        {
            case ACCOUNT:
            {
                if(stChart.getAccounts().size() == 1)   addToContextMenu(cm, generateMenuForPieChartSelectedAccounts(stChart.getAccounts().get(0).getName(), SELECTED_ACCOUNTS));
                else                                    addToContextMenu(cm, generateMenuForPieChartSelectedAccounts("Selected Accounts", SELECTED_ACCOUNTS));
                
                break;
            }
            case ALL:           addToContextMenu(cm, generateMenuForPieChartSelectedAccounts("All Accounts", SELECTED_ALL));            break;
            case CATEGORY:      appendMenuItemsForPieChartSelectedCategories(cm, stChart);                                              break;
            case ENTRY:         appendMenuItemsForPieChartSelectedEntries(cm, stChart);                                                 break;
        }
        
        switch(stChart.getSource())
        {
            case ACCOUNT:
            case ALL:           addToContextMenu(cm, new SeparatorMenuItem());
        }
        
        switch(stChart.getSource())
        {
            case ACCOUNT:       addToContextMenu(cm, generateMenuForPieChartSelectedAccountsBalanceDistribution(SELECTED_ACCOUNTS));    break;
            case ALL:           addToContextMenu(cm, generateMenuForPieChartSelectedAccountsBalanceDistribution(SELECTED_ALL));         break;
        }
        
        if(cm.getItems().size()>0)
            addToContextMenu(cm, new SeparatorMenuItem());
        
        addToContextMenu(cm, generateMenuItemForCharts(CHART_PIE, "Show Legend", SHOW_LEGEND));
        addToContextMenu(cm, generateMenuItemForCharts(CHART_PIE, "Hide Legend", HIDE_LEGEND));
        
        if(Windows.isLarge(WINDOW_CHART_PIE) || FullScreen.isActive())
        {
            if(cm.getItems().size()>0)
                addToContextMenu(cm, new SeparatorMenuItem());
            
            if(FullScreen.isActive())                       addToContextMenu(cm, generateMenuItemForCharts(CHART_PIE, "Close", HIDE_FULL_SCREEN));
            else if(Windows.isLarge(WINDOW_CHART_PIE))      addToContextMenu(cm, generateMenuItemForCharts(CHART_PIE, "View Full Screen", SHOW_FULL_SCREEN));
        }
        
        cm.setStyle("-fx-font: " + FONT_SIZE_WINDOW_CONTEXT_MENU + " calibri;");

        return cm;
    }
    public static ContextMenu buildContextMenuForLineChart(LineChartState stChart)
    {
        ContextMenu cm = new ContextMenu();
        
        switch(stChart.getSource())
        {
            case ACCOUNT:
            {
                if(stChart.getAccounts().size() == 1)       addToContextMenu(cm, generateMenuForLineChartSelectedAccounts(stChart.getAccounts().get(0).getName()));
                else                                        addToContextMenu(cm, generateMenuForLineChartSelectedAccounts("Selected Accounts"));
                
                break;
            }
            case CATEGORY:
            {
                String sLabel = "";
                Category c  = stChart.getCategories().get(0);
                
                switch(c.getType())
                {
                    case CATEGORY_TYPE_USER_DEFINED:        sLabel = "Selected Categories";             break;
                    case CATEGORY_TYPE_INTERNAL_TRANSFER:   sLabel = "Selected Internal Transfers";     break;
                }
                
                if(stChart.getCategories().size() == 1)                                     addToContextMenu(cm, generateMenuForLineChartSelectedCategories(c.getNameInTable()));
                else if(stChart.getCategories().size() <= MAXIMUM_NUMBER_OF_CHART_LINES)    addToContextMenu(cm, generateMenuForLineChartSelectedCategories(sLabel));
                
                break;
            }
            case ENTRY:
            {
                if(stChart.getEntries().size() == 1)                                        addToContextMenu(cm, generateMenuForLineChartSelectedEntries(stChart.getEntries().get(0).getName()));
                else if(stChart.getEntries().size() <= MAXIMUM_NUMBER_OF_CHART_LINES)       addToContextMenu(cm, generateMenuForLineChartSelectedEntries("Selected Entries"));
                
                break;
            }
            case ALL:
            {
                addToContextMenu(cm, generateMenuForLineChartAllAccounts());
                
                break;
            }
        }
        
        if(isIntervalCompareValid(stChart))
        {
            if(cm.getItems().size()>0)
                addToContextMenu(cm, new SeparatorMenuItem());
            
            addToContextMenu(cm, generateMenuForLineChartIntervalCompare("Compare Recent"));
        }
        
        if(Windows.isLarge(WINDOW_CHART_LINE) || FullScreen.isActive())
        {
            if(cm.getItems().size()>0)
                addToContextMenu(cm, new SeparatorMenuItem());
            
            addToContextMenu(cm, generateMenuItemForCharts(CHART_LINE, "Show Values", SHOW_VALUES));
            addToContextMenu(cm, generateMenuItemForCharts(CHART_LINE, "Hide Values", HIDE_VALUES));
            
            if(cm.getItems().size()>0)
                addToContextMenu(cm, new SeparatorMenuItem());
            
            if(FullScreen.isActive())                       addToContextMenu(cm, generateMenuItemForCharts(CHART_LINE, "Close", HIDE_FULL_SCREEN));
            else if(Windows.isLarge(WINDOW_CHART_LINE))     addToContextMenu(cm, generateMenuItemForCharts(CHART_LINE, "View Full Screen", SHOW_FULL_SCREEN));
        }
        
        cm.setStyle("-fx-font: " + FONT_SIZE_WINDOW_CONTEXT_MENU + " calibri;");
        
        return cm;
    }
    public static ContextMenu buildContextMenuForHistory(List<Entry> lst)
    {
        ContextMenu cm = new ContextMenu();
        
        if(lst.isEmpty())
            return cm;
        
        addToContextMenu(cm, generateMenuForHistorySetCategories(lst, TABLEVIEW_ID_HISTORY));
        
        if(lst.size() == 1)
        {
            addToContextMenu(cm, generateMenuForHistoryFilter(lst));
            addToContextMenu(cm, new SeparatorMenuItem());
            addToContextMenu(cm, generateMenuItemForHistory(lst, "View Entry", VIEW_ENTRIES));
        }
        else
        {
            addToContextMenu(cm, new SeparatorMenuItem());
            addToContextMenu(cm, generateMenuItemForHistory(lst, "View Entries", VIEW_ENTRIES));
        }

        if(!History.isSendToChartsValid(lst))
            cm.getItems().get(cm.getItems().size()-1).setDisable(true);
        
        return cm;
    }
    public static ContextMenu buildContextMenuForDDSO()
    {
        ContextMenu cm = new ContextMenu();
        
        addToContextMenu(cm, generateMenuItemForDDSO("Next Month", NEXT_MONTH));
        addToContextMenu(cm, generateMenuItemForDDSO("Previous Month", PREVIOUS_MONTH));
        addToContextMenu(cm, new SeparatorMenuItem());
        addToContextMenu(cm, generateMenuForDDSOZoom());
        addToContextMenu(cm, new SeparatorMenuItem());
        addToContextMenu(cm, generateMenuForDDSOShowValues());
        addToContextMenu(cm, generateMenuItemForDDSO("Hide Values", HIDE_VALUES));
        addToContextMenu(cm, new SeparatorMenuItem());
        addToContextMenu(cm, generateMenuItemForDDSO("Close", CLOSE_CHART));
        
        cm.setStyle("-fx-font: " + FONT_SIZE_WINDOW_CONTEXT_MENU + " calibri;");
        
        return cm;
    }
    
    //Administration
    public static ContextMenu buildContextMenuForStatement(List<Entry> lst)
    {
        ContextMenu cm = new ContextMenu();
        
        if(lst.isEmpty())
            return cm;
        
        if(lst.size() == 1 || entriesHaveSameDirection(lst))
            addToContextMenu(cm, generateMenuForHistorySetCategories(lst, TABLEVIEW_ID_STATEMENT));

        return cm;
    }
    public static ContextMenu buildContextMenuForWatches(List<Watch> lst)
    {
        ContextMenu cm = new ContextMenu();
        
        if(!Watches.isContextMenuReady())
            return cm;
        
        List<Category> lstCategories    = Watches.getUnwatchedCategories();
        int iDirection                  = Watches.getSelectedDirection();
        
        if(lst.isEmpty())
        {
            addToContextMenu(cm, generateMenuForWatches(lstCategories, iDirection));
            return cm;
        }
        
        if(lst.size() == 1)
        {
            addToContextMenu(cm, generateMenuForWatches(lstCategories, iDirection));
            addToContextMenu(cm, new SeparatorMenuItem());
            addToContextMenu(cm, generateMenuItemForWatches("View History", WATCH_VIEW_HISTORY));
        }
        else
            addToContextMenu(cm, generateMenuForWatches(lstCategories, iDirection));
        
        addToContextMenu(cm, new SeparatorMenuItem());
        addToContextMenu(cm, generateMenuItemForWatches("Update", WATCH_UPDATE));
        addToContextMenu(cm, generateMenuItemForWatches("Delete", WATCH_DELETE));
        
        return cm;
    }
    public static ContextMenu buildContextMenuForRefunds(List<Refund> lst)
    {
        ContextMenu cm = new ContextMenu();
        
        if(!Refunds.isContextMenuReady())
            return cm;
        
        if(lst.isEmpty())
        {
            addToContextMenu(cm, generateMenuItemForRefunds("Create", REFUND_CREATE));
            return cm;
        }
        
        if(lst.size() == 1)
        {
            addToContextMenu(cm, generateMenuItemForRefunds("Create", REFUND_CREATE));
            
            if(!lst.get(0).isReceived())
                addToContextMenu(cm, generateMenuItemForRefunds("Edit", REFUND_EDIT));
            
            addToContextMenu(cm, new SeparatorMenuItem());
            
            if(lst.get(0).isReceived())
                addToContextMenu(cm, generateMenuItemForRefunds("View Refund", REFUND_VIEW_CONFIRMED));
            else
            {
                addToContextMenu(cm, generateMenuItemForRefunds("View Potential Refunds", REFUND_VIEW_POTENTIAL));
                addToContextMenu(cm, generateMenuItemForRefunds("View All Incoming", REFUND_VIEW_ALL_INCOMING));
            }
            
            addToContextMenu(cm, new SeparatorMenuItem());
            addToContextMenu(cm, generateMenuItemForRefunds("Delete", REFUND_DELETE));
        }
        else
        {
            addToContextMenu(cm, generateMenuItemForRefunds("Create", REFUND_CREATE));
            addToContextMenu(cm, new SeparatorMenuItem());
            addToContextMenu(cm, generateMenuItemForRefunds("Delete", REFUND_DELETE));
        }
        
        return cm;
    }
    public static ContextMenu buildContextMenuForRefundPotentials(List<Entry> lst)
    {
        ContextMenu cm = new ContextMenu();
        
        if(!lst.isEmpty())
            addToContextMenu(cm, generateMenuItemForRefundPotentials("Confirm Refund", REFUND_CONFIRM));
        
        return cm;
    }
    public static ContextMenu buildContextMenuForCategoriesAdministration(List<Category> lst)
    {
        ContextMenu cm = new ContextMenu();

        if(lst.isEmpty())
            return cm;
        
        int iDirection = lst.get(0).getDirection();
        
        addToContextMenu(cm, generateMenuItemForCategoriesAdministration("Create", CATEGORY_CREATE, iDirection));

        if(lst.size() == 1 && lst.get(0).isUserDefined() && !lst.get(0).isUndefined())
            addToContextMenu(cm, generateMenuItemForCategoriesAdministration("Rename", CATEGORY_RENAME, iDirection));
        
        if(isUserDefinedCategoryPresent(lst))
            addToContextMenu(cm, generateMenuItemForCategoriesAdministration("Delete", CATEGORY_DELETE, iDirection));
        
        return cm;
    }
    public static ContextMenu buildContextMenuForAutoCategories(List<AutoCategory> lst)
    {
        ContextMenu cm = new ContextMenu();
        
        if(!lst.isEmpty())
            addToContextMenu(cm, generateMenuItemForAutoCategories("Delete", AUTO_CATEGORY_DELETE));
        
        return cm;
    }
    public static ContextMenu buildContextMenuForReminders(List<Reminder> lst)
    {
        ContextMenu cm = new ContextMenu();
        
        addToContextMenu(cm, generateMenuItemForReminders("Create", REMINDER_CREATE));
        
        if(lst.isEmpty())
            return cm;
        
        if(lst.size() == 1)
        {
            addToContextMenu(cm, generateMenuItemForReminders("Edit", REMINDER_EDIT));
            addToContextMenu(cm, generateMenuItemForReminders("Delete", REMINDER_DELETE));
        }
        else
            addToContextMenu(cm, generateMenuItemForReminders("Delete", REMINDER_DELETE));
        
        return cm;
    }
    public static ContextMenu buildContextMenuForIntegrityChecks(List<Check> lst)
    {
        ContextMenu cm = new ContextMenu();
        
        if(!lst.isEmpty())
            addToContextMenu(cm, generateMenuItemForIntegrity("View", INTEGRITY_VIEW_ERRORS));
        
        return cm;
    }
    public static ContextMenu buildContextMenuForIntegrityErrors(List<Error> lst)
    {
        ContextMenu cm = new ContextMenu();
        
        if(!lst.isEmpty() && Integrity.isErrorsContextMenuReady())
            addToContextMenu(cm, generateMenuItemForIntegrity(Integrity.getErrorLabel(), INTEGRITY_VIEW_ITEMS));
        
        return cm;
    }
    public static ContextMenu buildContextMenuForDatabaseOverview(List<Item> lst)
    {
        ContextMenu cm = new ContextMenu();
        
        if(!lst.isEmpty())
            addToContextMenu(cm, generateMenuItemForDatabaseAdministration("View", DATABASE_OVERVIEW_VIEW));
        
        return cm;
    }
    public static ContextMenu buildContextMenuForDatabaseTables()
    {
        ContextMenu cm = new ContextMenu();
        
        addToContextMenu(cm, generateMenuItemForDatabaseAdministration("Delete", DATABASE_DELETE_RECORDS));
        addToContextMenu(cm, generateMenuItemForDatabaseAdministration("Delete All", DATABASE_DELETE_RECORDS_ALL));
        
        return cm;
    }
}