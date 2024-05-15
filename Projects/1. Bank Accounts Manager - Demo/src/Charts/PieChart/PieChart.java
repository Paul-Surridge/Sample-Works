package Charts.PieChart;

import Accounts.Account;
import Accounts.Accounts;
import Categories.Category;
import FullScreen.FullScreen;
import History.Entry;
import Shared.Constants;
import Shared.ContextMenuFactory;
import Shared.Formatter;
import Shared.Windows;
import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;

public class PieChart implements Constants{
    
    private static AnchorPane apPieChart; 
    private static javafx.scene.chart.PieChart chtPie;
    private static javafx.scene.chart.PieChart.Data diOther;
    
    private static PieChartState stChart = new PieChartState();
    private static Rectangle recPieChartNodeValue;
    private static Label labPieChartNodeValue;
    private static ContextMenu cmChart;
    private static int iTotalPie;
    private static double dTotalPie;
    
    public PieChart(AnchorPane apPieChart, Rectangle recPieChartNodeValue, Label labPieChartNodeValue)
    {
        this.apPieChart = apPieChart;
        this.recPieChartNodeValue = recPieChartNodeValue;
        this.labPieChartNodeValue = labPieChartNodeValue;
    }
    
    //Internal -----------------------------------------------------------------
    private static void initChart()
    {
        chtPie = new javafx.scene.chart.PieChart();
        
        iTotalPie = 0;
        dTotalPie = 0;
    }
    private static void initEventHandlerForChart()
    {
        chtPie.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
            
            cmChart.hide();
            hidePieValue();

            switch(me.getButton())
            {
                case PRIMARY:
                {
                    if(me.getClickCount() == 2 && !FullScreen.isActive())
                    {
                        Windows.toggleWindow(WINDOW_CHART_PIE);
                        rebuildContextMenu();
                    }
                    break;
                }
                case SECONDARY:
                {
                    cmChart.show(chtPie, me.getScreenX(), me.getScreenY());
                    break;
                }
            }
        });
    }
    private static void initEventHandlerForPieSegments()
    {
        for(int i = 0 ; i<chtPie.getData().size() ; i++)
        {
            javafx.scene.chart.PieChart.Data di = chtPie.getData().get(i);
            
            di.getNode().addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {

                cmChart.hide();
                hidePieValue();

                switch(me.getButton())
                {
                    case PRIMARY:       showPieValue(me, di);                                       break;
                    case SECONDARY:     cmChart.show(chtPie, me.getScreenX(), me.getScreenY());     break;
                }

                me.consume();
            });
        }
    }
    private static void setChart()
    {
        chtPie.setTitle(stChart.getTitle());
        chtPie.setLegendVisible(stChart.getLegend());
        
        AnchorPane.setTopAnchor(chtPie, 0.0);
        AnchorPane.setBottomAnchor(chtPie, 0.0);
        AnchorPane.setLeftAnchor(chtPie, 0.0);
        AnchorPane.setRightAnchor(chtPie, 0.0);
    }
    private static void rebuildContextMenu()
    {
        cmChart = ContextMenuFactory.buildContextMenuForPieChart(stChart);
    }
    
    //Anchor Pane
    private static void resetChartInAnchorPane()
    {
        apPieChart.getChildren().clear();
        
        if(chtPie != null)
            apPieChart.getChildren().add(chtPie);
    }
    
    //Pie Segment Value Labelling
    private static void showPieValue(MouseEvent me, javafx.scene.chart.PieChart.Data di)
    {
        Rectangle r = recPieChartNodeValue;
        Label l = labPieChartNodeValue;
        
        Node n = di.getNode();
        
        l.setText(di.getName() + " £" + Formatter.convert(CURRENCY, CHART, TABLEVIEW, di.getPieValue()));
        l.setLayoutX(me.getX() + n.getLayoutX() + 25);
        l.setLayoutY(me.getY() + n.getLayoutY() + 22);
        l.autosize();
        
        r.setLayoutX(me.getX() + n.getLayoutX() + 20);
        r.setLayoutY(me.getY() + n.getLayoutY() + 20);
        r.setWidth(l.getWidth() + 10);
        
        r.setVisible(true);
        l.setVisible(true);
        
        if(!apPieChart.getChildren().contains(r))
        {
            if(r.getWidth() + r.getLayoutX() > chtPie.getWidth())
            {
                double dLeftAmount = r.getLayoutX() + r.getWidth() - chtPie.getWidth() + 10;
                
                r.setLayoutX(r.getLayoutX() - dLeftAmount);
                l.setLayoutX(l.getLayoutX() - dLeftAmount);
            }
            
            apPieChart.getChildren().add(r);
            apPieChart.getChildren().add(l);
        }
    }
    private static void hidePieValue()
    {
        apPieChart.getChildren().remove(recPieChartNodeValue);
        apPieChart.getChildren().remove(labPieChartNodeValue);
    }
    private static void showLegend(boolean bVisible)
    {
        stChart.setLegend(bVisible);
        chtPie.setLegendVisible(bVisible);
    }
    
    //Category - Account Property
    private static boolean isAccountProperty(List<Category> lst)
    {
        return (lst.get(0).getType() == CATEGORY_TYPE_ACCOUNT_PROPERTY && lst.size() == 1);
    }
    private static void viewAccountProperty(List<Category> lst)
    {
        Category c = lst.get(0);
        int iChartID = CHART_PIE_ID_INCOMING_V_OUTGOING;
        
        switch(c.getAccountProperty())
        {
            case AP_INCOMING_INCOME:
            case AP_INCOMING_INTERNAL_TRANSFER:     iChartID = CHART_PIE_ID_INCOMING_CATEGORIES;    break;
            case AP_INCOMING_TOTAL:                 iChartID = CHART_PIE_ID_INCOMING_V_OUTGOING;    break;
            case AP_OUTGOING_PURCHASE:
            case AP_OUTGOING_DDSO:
            case AP_OUTGOING_OTHER:
            case AP_OUTGOING_SPEND:
            case AP_OUTGOING_INTERNAL_TRANSFER:     iChartID = CHART_PIE_ID_OUTGOING_CATEGORIES;    break;
            case AP_OUTGOING_TOTAL:                 iChartID = CHART_PIE_ID_INCOMING_V_OUTGOING;    break;
        }
        
        stChart.setPieID(ACCOUNT, iChartID);
        
        viewAccounts(Accounts.getAccountsViewed());
    }
    
    //Pie Segment Percentages
    private static String findPercentageOfTotal(double dValue)
    {
        double dResult = (dValue / dTotalPie) * 100;
        
        return Formatter.formatPercentageValue(dResult) + "%";
    }
    private static String findPercentageRelative(int iSmall, int iLarge)
    {
        if(iSmall == 0)
            return " (100%)";
        
        long lSmall = Integer.toUnsignedLong(iSmall);
        long lLarge = Integer.toUnsignedLong(iLarge);
        
        return " (" + Formatter.formatPercentageValue((lLarge*100)/lSmall) + "%)";
    }
    private static void setPercentageProportional()
    {
        dTotalPie = Formatter.convert(DATABASE, CHART, iTotalPie);

        for(javafx.scene.chart.PieChart.Data dataItem : chtPie.getData())
            dataItem.setName(dataItem.getName() + " (" + findPercentageOfTotal(dataItem.getPieValue()) + ")");
    }
    
    //Build Chart - Data Item Creation and Addition to Chart
    private static void buildDataItem(Category c)
    {
        chtPie.getData().add(new javafx.scene.chart.PieChart.Data(c.getNameInTable(), Formatter.convert(DATABASE, CHART, c.getTotal())));

        iTotalPie += c.getTotal();
    }
    private static void buildDataItem(Entry e)
    {
        String sLabel = e.getName() + " (" + e.getAccountNameAbbreviated() + ")";
        
        chtPie.getData().add(new javafx.scene.chart.PieChart.Data(sLabel, Formatter.convert(DATABASE, CHART, e.getTotal())));

        iTotalPie += e.getTotal();
    }
    private static void buildDataItem(String sLabel, int iValue)
    {
        chtPie.getData().add(new javafx.scene.chart.PieChart.Data(sLabel, Formatter.convert(DATABASE, CHART, iValue)));
        
        iTotalPie += iValue;
    }
    private static void buildDataItem(int iSource, int iIn, int iOut)
    {
        switch(iSource)
        {
            case AP_INCOMING_INCOME:
            {
                if(iIn > iOut)      buildDataItem("Income" + findPercentageRelative(iOut, iIn), iIn);
                else                buildDataItem("Income", iIn);
                
                break;
            }
            case AP_OUTGOING_SPEND:
            {
                if(iOut > iIn)      buildDataItem("Spend" + findPercentageRelative(iIn, iOut), iOut);
                else                buildDataItem("Spend", iOut);
                
                break;
            }
            case AP_INCOMING_TOTAL:
            {
                if(iIn > iOut)      buildDataItem("Incoming" + findPercentageRelative(iOut, iIn), iIn);
                else                buildDataItem("Incoming", iIn);
                
                break;
            }
            case AP_OUTGOING_TOTAL:
            {
                if(iOut > iIn)      buildDataItem("Outgoing" + findPercentageRelative(iIn, iOut), iOut);
                else                buildDataItem("Outgoing", iOut);
                
                break;
            }
        }
    }
    private static void setDataItemOther(int iTotalOther)
    {
        diOther.setPieValue(Formatter.convert(DATABASE, CHART, iTotalOther));
        chtPie.getData().add(diOther);
    }
    
    //Build Chart
    private static void buildBalance(int iBalanceProperty)
    {
        if(Accounts.getAll().getBalanceProperty(iBalanceProperty).getValue() <= 0)
            return;
        
        for(Account a : Accounts.getAccounts())
            if(a.getBalanceProperty(iBalanceProperty).getValue() > 0)
                buildDataItem(a.getNameAbbreviated(), a.getBalanceProperty(iBalanceProperty).getValue());

        setPercentageProportional();
    }
    private static void buildCategories(List<Account> lstAccounts, int iDirection)
    {
        for(Account a : lstAccounts)
            for(Category c : a.getCategoriesNonZero(iDirection))
                buildDataItem(c);
        
        setPercentageProportional();
    }
    private static void buildCategoriesWithOther(List<Category> lstCategories, int iAccountProperty, String sOtherLabel)
    {
        List<Account> lstAccounts = new ArrayList<>();
        int iTotalOther = 0;
        
        diOther = new javafx.scene.chart.PieChart.Data(sOtherLabel, 0);
        
        for(Category c : lstCategories)
        {
            Account a = Accounts.get(c.getAccountID());
            
            if((iAccountProperty == AP_INCOMING_INCOME || iAccountProperty == AP_OUTGOING_SPEND) && c.isInternalTransfer())
                continue;
            
            if(!lstAccounts.contains(a))
                lstAccounts.add(a);
            
            buildDataItem(c);
        }
        
        for(Account a : lstAccounts)
            iTotalOther += a.getAccountProperty(iAccountProperty).getTotal();
        
        iTotalOther -= iTotalPie;
        iTotalPie += iTotalOther;
        
        if(iTotalOther>0)
            setDataItemOther(iTotalOther);
        
        setPercentageProportional();
    }
    private static void buildEntriesWithOther(List<Entry> lstEntries, int iAccountProperty, String sOtherLabel)
    {
        Account a = Accounts.get(lstEntries.get(0).getAccountID());
        int iTotalOther = 0;
        
        diOther = new javafx.scene.chart.PieChart.Data(sOtherLabel, 0);
        
        iTotalOther += a.getAccountProperty(iAccountProperty).getTotal();
        
        for(Entry e : lstEntries)
            buildDataItem(e);
            
        iTotalOther -= iTotalPie;
        iTotalPie += iTotalOther;
        
        if(iTotalOther>0)
            setDataItemOther(iTotalOther);
        
        setPercentageProportional();
    }
    private static void buildIncomeVersusSpend(List<Account> lstAccounts)
    {
        int iTotalIncome = 0;
        int iTotalSpend = 0;
        
        for(Account a : lstAccounts)
        {
            iTotalIncome += a.getAccountProperty(AP_INCOMING_INCOME).getTotal();
            iTotalSpend += a.getAccountProperty(AP_OUTGOING_SPEND).getTotal();
        }

        if(iTotalIncome > 0)
            buildDataItem(AP_INCOMING_INCOME, iTotalIncome, iTotalSpend);

        if(iTotalSpend > 0)
            buildDataItem(AP_OUTGOING_SPEND, iTotalIncome, iTotalSpend);
    }
    private static void buildIncomingTotaVersusOutgoingTotal(List<Account> lstAccounts)
    {
        int iTotalIncoming = 0;
        int iTotalOutgoing = 0;
        
        for(Account a : lstAccounts)
        {
            iTotalIncoming += a.getAccountProperty(AP_INCOMING_TOTAL).getTotal();
            iTotalOutgoing += a.getAccountProperty(AP_OUTGOING_TOTAL).getTotal();
        }

        if(iTotalIncoming > 0)
            buildDataItem(AP_INCOMING_TOTAL, iTotalIncoming, iTotalOutgoing);

        if(iTotalOutgoing > 0)
            buildDataItem(AP_OUTGOING_TOTAL, iTotalIncoming, iTotalOutgoing);
    }
    private static void showNoData()
    {
        Label l = new Label("No Data");
        
        l.setAlignment(Pos.CENTER);
        l.setContextMenu(ContextMenuFactory.buildContextMenuForPieChart(stChart));
        l.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
            
            cmChart.hide();

            switch(me.getButton())
            {
                case PRIMARY:
                {
                    if(me.getClickCount() == 2 && !FullScreen.isActive())
                        Windows.toggleWindow(WINDOW_CHART_PIE);
                    
                    break;
                }
            }
        });
        
        AnchorPane.setTopAnchor(l, 0.0);
        AnchorPane.setBottomAnchor(l, 0.0);
        AnchorPane.setLeftAnchor(l, 0.0);
        AnchorPane.setRightAnchor(l, 0.0);
        
        apPieChart.getChildren().clear();
        apPieChart.getChildren().add(l);
    }
    private static void showChart()
    {
        if(chtPie.getData().isEmpty())
            showNoData();
        else
        {
            setChart();
            
            apPieChart.getChildren().clear();
            apPieChart.getChildren().add(chtPie);
            
            initEventHandlerForChart();
            initEventHandlerForPieSegments();
            
            setFontSize();
        }
        
        if(FullScreen.isActive())
        {
            FullScreen.restoreDate();
            FullScreen.refreshDate();
        }
        
        rebuildContextMenu();
    }
    
    //FullScreen
    private static void setFullScreen(boolean bShow)
    {
        if(bShow)   FullScreen.show(CHART_PIE, apPieChart);
        else        FullScreen.hide();

        if(!bShow)
        {
            resetChartInAnchorPane();
            Windows.restoreChart(CHART_PIE, apPieChart);
            Windows.setSplitPane(WINDOW_CHART_PIE);
        }
        
        rebuildContextMenu();
    }

    //External API -------------------------------------------------------------
    
    //Tableviews Context Menu - (via Charts Class) -----------------------------
    public static void viewAccounts(List<Account> lst)
    {
        initChart();
        
        stChart.setAccounts(lst);

        switch(stChart.getPieID(ACCOUNT))
        {
            case CHART_PIE_ID_BALANCE_DISTRIBUTION_AT_START:        buildBalance(BP_BALANCE_AT_START);              break;
            case CHART_PIE_ID_BALANCE_DISTRIBUTION_AT_END:          buildBalance(BP_BALANCE_AT_END);                break;
            case CHART_PIE_ID_INCOMING_CATEGORIES:                  buildCategories(lst, IN);                       break;
            case CHART_PIE_ID_OUTGOING_CATEGORIES:                  buildCategories(lst, OUT);                      break;
            case CHART_PIE_ID_INCOME_V_SPEND:                       buildIncomeVersusSpend(lst);                    break;
            case CHART_PIE_ID_INCOMING_V_OUTGOING:                  buildIncomingTotaVersusOutgoingTotal(lst);      break;
        }
        
        showChart();
    }
    public static void viewCategories(List<Category> lst)
    {
        if(isAccountProperty(lst))
        {
            viewAccountProperty(lst);
            return;
        }
        
        initChart();
        
        stChart.setCategories(lst);
        
        switch(stChart.getPieID(CATEGORY))
        {
            case CHART_PIE_ID_INCOMING_CATEGORIES_V_INCOME:         buildCategoriesWithOther(lst, AP_INCOMING_INCOME,   "Other Income");        break;
            case CHART_PIE_ID_INCOMING_CATEGORIES_V_INCOMING:       buildCategoriesWithOther(lst, AP_INCOMING_TOTAL,    "Other Incoming");      break;
            case CHART_PIE_ID_OUTGOING_CATEGORIES_V_SPEND:          buildCategoriesWithOther(lst, AP_OUTGOING_SPEND,    "Other Spend");         break;
            case CHART_PIE_ID_OUTGOING_CATEGORIES_V_OUTGOING:       buildCategoriesWithOther(lst, AP_OUTGOING_TOTAL,    "Other Outgoing");      break;
        }
        
        showChart();
    }
    public static void viewEntries(List<Entry>lst)
    {
        initChart();
        
        stChart.setEntries(lst);
        
        switch(stChart.getPieID(ENTRY))
        {
            case CHART_PIE_ID_INCOMING_ENTRIES_V_INCOME:            buildEntriesWithOther(lst, AP_INCOMING_INCOME,   "Other Income");           break;
            case CHART_PIE_ID_INCOMING_ENTRIES_V_INCOMING:          buildEntriesWithOther(lst, AP_INCOMING_TOTAL,    "Other Incoming");         break;
            case CHART_PIE_ID_OUTGOING_ENTRIES_V_SPEND:             buildEntriesWithOther(lst, AP_OUTGOING_SPEND,    "Other Spend");            break;
            case CHART_PIE_ID_OUTGOING_ENTRIES_V_OUTGOING:          buildEntriesWithOther(lst, AP_OUTGOING_TOTAL,    "Other Outgoing");         break;
        }
        
        showChart();
    }
    public static void viewAll()
    {
        initChart();
        
        stChart.setAll();
        
        List<Account> lstAccounts = Accounts.getAccountsViewed();
        
        switch(stChart.getPieID(ALL))
        {
            case CHART_PIE_ID_BALANCE_DISTRIBUTION_AT_START:        buildBalance(BP_BALANCE_AT_START);                      break;
            case CHART_PIE_ID_BALANCE_DISTRIBUTION_AT_END:          buildBalance(BP_BALANCE_AT_END);                        break;
            case CHART_PIE_ID_INCOMING_CATEGORIES:                  buildCategories(lstAccounts, IN);                       break;
            case CHART_PIE_ID_OUTGOING_CATEGORIES:                  buildCategories(lstAccounts, OUT);                      break;
            case CHART_PIE_ID_INCOME_V_SPEND:                       buildIncomeVersusSpend(lstAccounts);                    break;
            case CHART_PIE_ID_INCOMING_V_OUTGOING:                  buildIncomingTotaVersusOutgoingTotal(lstAccounts);      break;
        }

        showChart();
    }
    
    //Pie Chart Context Menu ---------------------------------------------------
    public static void view(int... iParam)
    {
        switch(iParam[0])
        {
            case SELECTED_ACCOUNTS:     stChart.setPieID(ACCOUNT, iParam[1]);       break;
            case SELECTED_CATEGORIES:   stChart.setPieID(CATEGORY, iParam[1]);      break;
            case SELECTED_ENTRIES:      stChart.setPieID(ENTRY, iParam[1]);         break;
            case SELECTED_ALL:          stChart.setPieID(ALL, iParam[1]);           break;
        }
        
        switch(iParam[0])
        {
            case SELECTED_ACCOUNTS:     viewAccounts(stChart.getAccounts());        break;
            case SELECTED_CATEGORIES:   viewCategories(stChart.getCategories());    break;
            case SELECTED_ENTRIES:      viewEntries(stChart.getEntries());          break;
            case SELECTED_ALL:          viewAll();                                  break;
            case SHOW_LEGEND:           showLegend(true);                           break;
            case HIDE_LEGEND:           showLegend(false);                          break;
            case SHOW_FULL_SCREEN:      setFullScreen(true);                        break;
            case HIDE_FULL_SCREEN:      setFullScreen(false);                       break;
        }
    }
    
    //Refresh via Cursor Left/Right
    public static void refresh()
    {
        switch(stChart.getSource())
        {
            case ACCOUNT:       viewAccounts(stChart.getAccounts());        break;
            case CATEGORY:      viewCategories(stChart.getCategories());    break;
            case ENTRY:         viewEntries(stChart.getEntries());          break;
            case ALL:           viewAll();                                  break;
        }
    }
    
    //Windows ------------------------------------------------------------------
    public static void setFontSize()
    {
        if(chtPie != null)
        {
            if(Windows.isLarge(WINDOW_CHART_PIE))   chtPie.setStyle("-fx-font: " + FONT_SIZE_WINDOW_LARGE + " calibri;");
            else                                    chtPie.setStyle("-fx-font: " + FONT_SIZE_WINDOW_SMALL + " calibri;");
        }
    }
    public static void hidesPieValues()
    {
        hidePieValue();
    }
    
    //Fixed Period - Cursor Scrolling
    public static void clear()
    {
        apPieChart.getChildren().clear();
    }
    
    //Full Screen
    public static void hideFullScreen()
    {
        setFullScreen(false);
    }
}
