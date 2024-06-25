package Charts;

import Charts.LineChart.LineChart;
import Charts.PieChart.PieChart;
import Charts.StackedBarChart.StackedBarChart;
import Accounts.Account;
import Categories.Category;
import DateRange.DateRange;
import History.Entry;
import Shared.Constants;
import Shared.Debug;
import Shared.Formatter;
import java.util.List;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;

public class Charts implements Constants{
    
    private static StackedBarChart obStackedBarChart;
    private static PieChart obPieChart;
    private static LineChart obLineChart;
    private static String sTitleBaseAccounts, sTitleBaseCategories, sTitleBaseEntries;
    
    public Charts(AnchorPane apStackedBarChart, AnchorPane apPieChart, AnchorPane apLineChart,
                  Rectangle recStackedBarChartNodeValue, Label labStackedBarChartNodeValue, Rectangle recStackedBarChartZoomAmount, TextField tfStackedBarChartZoomAmount, Button btStackedBarChartZoomSet,
                  Rectangle recPieChartNodeValue, Label labPieChartNodeValue,
                  Rectangle recLineChartNodeValue, Label labLineChartNodeValue)
    {
        obStackedBarChart   = new StackedBarChart(apStackedBarChart, recStackedBarChartNodeValue, labStackedBarChartNodeValue, recStackedBarChartZoomAmount, tfStackedBarChartZoomAmount, btStackedBarChartZoomSet);
        obPieChart          = new PieChart(apPieChart, recPieChartNodeValue, labPieChartNodeValue);
        obLineChart         = new LineChart(apLineChart, recLineChartNodeValue, labLineChartNodeValue);
    }

    //External API -------------------------------------------------------------
    
    //Title
    public static String getTitleBase(int iSource)
    {
        switch(iSource)
        {
            case ACCOUNT:   return sTitleBaseAccounts;
            case CATEGORY:  return sTitleBaseCategories;
            case ENTRY:     return sTitleBaseEntries;
            case ALL:       return "All Accounts";
        }
        
        return "";
    }
    public static String refreshAccountTitleBase(List<Account> lst)
    {
        sTitleBaseAccounts = Formatter.buildTitleBaseForAccounts(lst);
        
        return sTitleBaseAccounts;
    }
    
    //Fixed Period - Cursor Scrolling
    public static void refreshAll()
    {
        StackedBarChart.refresh();
        PieChart.refresh();
        LineChart.refresh();
    }
    public static void clearAll()
    {
        StackedBarChart.clear();
        PieChart.clear();
        LineChart.clear();
    }

    //Accounts Tableview Context Menu
    public static void viewAccounts(List<Account> lst)
    {
        sTitleBaseAccounts = Formatter.buildTitleBaseForAccounts(lst);
        
        StackedBarChart.viewAccounts(lst);
        PieChart.viewAccounts(lst);
        LineChart.viewAccounts(lst);
    }
    public static void viewAll()
    {
        StackedBarChart.viewAll();
        PieChart.viewAll();
        LineChart.viewAll();
    }
    
    //Categories Tableview Context Menu
    public static void viewCategories(List<Category> lst)
    {
        sTitleBaseCategories = Formatter.buildTitleBaseForCategories(lst);

        StackedBarChart.viewCategories(lst);
        PieChart.viewCategories(lst);
        LineChart.viewCategories(lst);
    }
    
    //History Tableview Context Menu
    public static void viewEntries(List<Entry> lst)
    {
        sTitleBaseEntries = Formatter.buildTitleBaseForEntries(lst);
        
        StackedBarChart.viewEntries(lst);
        PieChart.viewEntries(lst);
        LineChart.viewEntries(lst);
    }
    
    //Intervals
    public static boolean intervalsExceedChartXAxisMaximum(int iIntervalSize)
    {
        int i = DateRange.getIntervals(iIntervalSize).size();
        
        return (i < 1 || i > MAXIMUM_NUMBER_OF_CHART_X_AXIS_ITEMS);
    }
}
