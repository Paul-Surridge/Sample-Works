package Charts.StackedBarChart;

import Accounts.Account;
import Accounts.Accounts;
import Categories.Category;
import DateRange.DateRange;
import FullScreen.FullScreen;
import History.Entry;
import History.History;
import Shared.Constants;
import Shared.ContextMenuFactory;
import Shared.Formatter;
import Shared.Popup;
import Shared.Windows;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;

public class StackedBarChart implements Constants{
    
    private static AnchorPane apStackedBarChart;
    private static javafx.scene.chart.StackedBarChart chtStackedBar;
    private static StackedBarChartState stChart = new StackedBarChartState();
    private static List<Label> lstLabels = new ArrayList<>();
    private static List<NodeLabel> lstNodeLabels = new ArrayList<>();
    private static Rectangle recStackedBarChartNodeValue, recStackedBarChartZoomAmount;
    private static Label labStackedBarChartNodeValue;
    private static TextField tfStackedBarChartZoomAmount;
    private static Button btStackedBarChartZoomAmount;
    private static ContextMenu cmChart;
    private static int iZoom;
    private static double dX, dY, dLimit;
    
    public StackedBarChart(AnchorPane apStackedBarChart, Rectangle recStackedBarChartNodeValue, Label labStackedBarChartNodeValue,
                           Rectangle recStackedBarChartZoomAmount, TextField tfStackedBarChartZoomAmount, Button btStackedBarChartZoomAmount)
    {
        this.apStackedBarChart = apStackedBarChart;
        this.recStackedBarChartNodeValue = recStackedBarChartNodeValue;
        this.labStackedBarChartNodeValue = labStackedBarChartNodeValue;
        
        this.recStackedBarChartZoomAmount = recStackedBarChartZoomAmount;
        this.tfStackedBarChartZoomAmount = tfStackedBarChartZoomAmount;
        this.btStackedBarChartZoomAmount = btStackedBarChartZoomAmount;
        
        iZoom = ZOOM_100;
        
        initEventHandlers();
        initEventListeners();
        initEventHandlersForKeyboard();
    }
    
    //Internal -----------------------------------------------------------------
    private static class NodeLabel
    {
        private XYChart.Data<String, Double> di;
        private Label l;
        
        public NodeLabel(XYChart.Data<String, Double> di)
        {
            this.di = di;
            
            initLabel();
            
            if(di.getYValue() == 0)
                l.setText("");
            else
            {
                initEventListeners();
                initEventHandler();

                setLabelLayoutPositionX();
                setLabelLayoutPositionY();
            }
        }
        
        //Internal -------------------------------------------------------------
        private void initLabel()
        {
            l = new Label(Formatter.convert(CURRENCY, CHART, TABLEVIEW, di.getYValue()));
            l.setStyle("-fx-font: " + FONT_SIZE_WINDOW_LARGE + " calibri;");
            
            if(iZoom != ZOOM_100 && di.getYValue() > dLimit)
                l.setText(l.getText() + "^");
        }
        private void initEventListeners()
        {
            di.getNode().layoutXProperty().addListener(new ChangeListener()
            {
                @Override
                public void changed(ObservableValue o, Object oldVal, Object newVal)
                {
                    setLabelLayoutPositionX();
                }
            });
            di.getNode().layoutYProperty().addListener(new ChangeListener()
            {
                @Override
                public void changed(ObservableValue o, Object oldVal, Object newVal)
                {
                    setLabelLayoutPositionY();
                }
            });
            l.layoutXProperty().addListener(new ChangeListener()
            {
                @Override
                public void changed(ObservableValue o, Object oldVal, Object newVal)
                {
                    if(Windows.isLarge(WINDOW_CHART_STACKED_BAR) || FullScreen.isActive())
                        if(l.getWidth() + l.getLayoutX() > chtStackedBar.getWidth())
                            l.setLayoutX(l.getLayoutX() - l.getWidth());
                }
            });
        }
        private void initEventHandler()
        {
            di.getNode().addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {

                cmChart.hide();
                hideBarValue();

                switch(me.getButton())
                {
                    case PRIMARY:
                    {
                        if(!Windows.isChartLarge() || apStackedBarChart.getChildren().contains(l))      showBarValue(me, di);
                        else if(Windows.isLarge(WINDOW_CHART_STACKED_BAR) || FullScreen.isActive())     apStackedBarChart.getChildren().add(l);

                        break;
                    }
                    case SECONDARY:
                    {
                        cmChart.show(chtStackedBar, me.getScreenX(), me.getScreenY());

                        break;
                    }
                }

                me.consume();
            });
        }
        private void setLabelLayoutPositionX()
        {
            l.setLayoutX(di.getNode().getLayoutX() + chtStackedBar.getYAxis().getWidth() + 27);
        }
        private void setLabelLayoutPositionY()
        {
            if(di.getYValue() >= 0)     l.setLayoutY(di.getNode().getLayoutY() + 23);
            else                        l.setLayoutY(di.getNode().getLayoutY() + 40 + di.getNode().getLayoutBounds().getHeight());
        }
        
        //External API ---------------------------------------------------------
        public Label getLabel()
        {
            return l;
        }
        public void limit()
        {
            if(di.getYValue() > dLimit)
                di.setYValue(dLimit);
        }
        public XYChart.Data<String, Double> getDataItem()
        {
            return di;
        }
    }
    
    private static void initChart()
    {
        chtStackedBar = new javafx.scene.chart.StackedBarChart(new CategoryAxis(), new NumberAxis());
        
        chtStackedBar.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
            
            cmChart.hide();
            hideBarValue();
            
            switch(me.getButton())
            {
                case PRIMARY:
                {
                    if(me.getClickCount() == 2 && !FullScreen.isActive())
                    {
                        Windows.toggleWindow(WINDOW_CHART_STACKED_BAR);
                        rebuildContextMenu();
                    }
                    break;
                }
                case SECONDARY:
                {
                    cmChart.show(chtStackedBar, me.getScreenX(), me.getScreenY());
                    break;
                }
            }
        });
    }
    private static void initEventHandlers()
    {
        recStackedBarChartZoomAmount.addEventHandler(MouseEvent.MOUSE_PRESSED, (MouseEvent me) -> {
            me.consume();
        });
    }
    private static void initEventListeners()
    {
        tfStackedBarChartZoomAmount.textProperty().addListener(new ChangeListener()
        {
            @Override
            public void changed(ObservableValue o, Object oldVal, Object newVal)
            {
                btStackedBarChartZoomAmount.setDisable(!isZoomCustomValid());
            }
        });
    }
    private static void initEventHandlersForKeyboard()
    {
        tfStackedBarChartZoomAmount.addEventHandler(KeyEvent.KEY_PRESSED, (KeyEvent ke) -> {
            if(ke.getCode() == KeyCode.ENTER)
                setZoom(ZOOM_CUSTOM);
        });
    }
    private static void rebuildContextMenu()
    {
        cmChart = ContextMenuFactory.buildContextMenuForStackedBarChart(stChart);
    }
    
    //Anchor Pane
    private static void resetChartInAnchorPane()
    {
        apStackedBarChart.getChildren().clear();
        
        if(chtStackedBar != null)
            apStackedBarChart.getChildren().add(chtStackedBar);
        
        if(FullScreen.isActive())
            FullScreen.restoreDate();
    }
    
    //Node Labels
    private static boolean isNodeLabelOverlapVisible(Label l)
    {
        boolean bVisible;
        
        if((dX == NOT_DEFINED) || (l.getLayoutY() < dY - 10) || (l.getLayoutY() > dY + 10) || (l.getLayoutX() < dX - 40) || (l.getLayoutX() > dX + 40))     bVisible = true;
        else                                                                                                                                                bVisible = false;
        
        if(bVisible)
        {
            dX = l.getLayoutX();
            dY = l.getLayoutY();
        }
        
        return bVisible;
    }
    private static void rebuildNodeLabels()
    {
        XYChart.Series<String, Double> ser;
        
        lstLabels.clear();
        lstNodeLabels.clear();
        
        for(int i = 0 ; i<chtStackedBar.getData().size() ; i++)
        {
            ser = (XYChart.Series<String, Double>)chtStackedBar.getData().get(i);
            
            for(int j = 0 ; j<ser.getData().size() ; j++)
            {
                NodeLabel nl = new NodeLabel((XYChart.Data<String, Double>)ser.getData().get(j));
                
                lstNodeLabels.add(nl);
                lstLabels.add(nl.getLabel());
            }
        }
        
        if(iZoom != ZOOM_100)
            for(NodeLabel nl : lstNodeLabels)
                nl.limit();
    }
    private static String getNodeLabelText(XYChart.Data<String, Double> di)
    {
        String s = "";
        
        for(NodeLabel nl : lstNodeLabels)
            if(nl.getDataItem() == di)
            {
                if(di.getYValue() < 0)  s = " -£" + Formatter.convert(CURRENCY, CHART, TABLEVIEW, di.getYValue()*-1);
                else                    s = " £" + nl.getLabel().getText();
            }
        
        return s;
    }
    
    //Node Value Popup
    private static void showBarValue(MouseEvent me, XYChart.Data<String, Double> di)
    {
        Rectangle r = recStackedBarChartNodeValue;
        Label l = labStackedBarChartNodeValue;
        
        Node n = di.getNode();
        
        l.setText(di.getXValue() + getNodeLabelText(di));
        
        l.setLayoutX(me.getX() + n.getLayoutX() + 25);
        l.setLayoutY(me.getY() + n.getLayoutY() + 22);
        l.autosize();
        
        r.setLayoutX(me.getX() + n.getLayoutX() + 20);
        r.setLayoutY(me.getY() + n.getLayoutY() + 20);
        r.setWidth(l.getWidth() + 10);
        
        r.setVisible(true);
        l.setVisible(true);
        
        if(!apStackedBarChart.getChildren().contains(r))
        {
            if(r.getWidth() + r.getLayoutX() > chtStackedBar.getWidth())
            {
                double dLeftAmount = r.getLayoutX() + r.getWidth() - chtStackedBar.getWidth() + 10;
                
                r.setLayoutX(r.getLayoutX() - dLeftAmount);
                l.setLayoutX(l.getLayoutX() - dLeftAmount);
            }
            
            apStackedBarChart.getChildren().add(r);
            apStackedBarChart.getChildren().add(l);
        }
    }
    private static void hideBarValue()
    {
        apStackedBarChart.getChildren().remove(recStackedBarChartNodeValue);
        apStackedBarChart.getChildren().remove(labStackedBarChartNodeValue);
    }

    //Zoom
    private static double findLargestAmount()
    {
        XYChart.Series<String, Double> ser;
        Map<String, Double> mp = new HashMap<>();
        double d;
        String s;
        double dLargestAmount = 0;
        
        for(Bars b : stChart.getBars())
        {
            ser = b.getSeries();
            
            for(int i = 0 ; i<ser.getData().size() ; i++)
            {
                XYChart.Data<String, Double> di = (XYChart.Data<String, Double>)ser.getData().get(i);
                
                s = di.getXValue();
                d = di.getYValue();
                
                if(!mp.containsKey(di.getXValue()))     mp.put(s, d);
                else                                    mp.replace(s, (mp.get(s) + d));
            }
        }
        
        for(double dTotal : mp.values())
            if(dTotal > dLargestAmount)
                dLargestAmount = dTotal;
        
        return (int)dLargestAmount;
    }
    private static double findZoomLimit()
    {
        double dLargestAmount;

        if(stChart.numberOfBars() > 1)
            iZoom = ZOOM_100;
        
        if(iZoom != ZOOM_CUSTOM)
            tfStackedBarChartZoomAmount.clear();
        
        if(iZoom == ZOOM_100)
            return NOT_DEFINED;
        
        dLargestAmount = findLargestAmount();
        dLargestAmount /= 100;
        
        switch(iZoom)
        {
            case ZOOM_10:       dLargestAmount *= 10;                   break;
            case ZOOM_25:       dLargestAmount *= 25;                   break;
            case ZOOM_50:       dLargestAmount *= 50;                   break;
            case ZOOM_75:       dLargestAmount *= 75;                   break;
            case ZOOM_CUSTOM:   dLargestAmount = Double.valueOf(tfStackedBarChartZoomAmount.getText());
        }
        
        return Formatter.round(2, dLargestAmount);
    }
    private static void showZoomCustomDialog()
    {
        Popup.show(POPUP_STACKED_BAR_ZOOM);
        
        tfStackedBarChartZoomAmount.requestFocus();
        tfStackedBarChartZoomAmount.selectRange(0, tfStackedBarChartZoomAmount.getText().length());
    }
    private static boolean isZoomCustomValid()
    {
        String s = tfStackedBarChartZoomAmount.getText();
        
        if(s.isEmpty())
            return false;
        
        for(int i = 0 ; i<s.length() ; i++)
            if(!Character.isDigit(s.charAt(i)))
                return false;
        
        return true;
    }
    
    //Category - Account Property
    private static boolean isAccountProperty(List<Category> lst)
    {
        return (lst.get(0).getType() == CATEGORY_TYPE_ACCOUNT_PROPERTY && lst.size() == 1);
    }
    private static void viewAccountProperty(List<Category> lst)
    {
        Category c = lst.get(0);
        int iChartID = CHART_BARS_ID_OUTGOING_TOTAL_PER_WEEK;
        
        switch(c.getAccountProperty())
        {
            case AP_INCOMING_INCOME:                iChartID = CHART_BARS_ID_INCOMING_INCOME_PER_WEEK;                  break;
            case AP_INCOMING_INTERNAL_TRANSFER:     iChartID = CHART_BARS_ID_INCOMING_INTERNAL_TRANSFERS_PER_WEEK;      break;
            case AP_INCOMING_TOTAL:                 iChartID = CHART_BARS_ID_INCOMING_TOTAL_PER_WEEK;                   break;
            case AP_OUTGOING_PURCHASE:              iChartID = CHART_BARS_ID_OUTGOING_PURCHASES_PER_WEEK;               break;
            case AP_OUTGOING_DDSO:                  iChartID = CHART_BARS_ID_OUTGOING_DDSO_PER_WEEK;                    break;
            case AP_OUTGOING_OTHER:                 iChartID = CHART_BARS_ID_OUTGOING_OTHER_PER_WEEK;                   break;
            case AP_OUTGOING_SPEND:                 iChartID = CHART_BARS_ID_OUTGOING_SPEND_PER_WEEK;                   break;
            case AP_OUTGOING_INTERNAL_TRANSFER:     iChartID = CHART_BARS_ID_OUTGOING_INTERNAL_TRANSFERS_PER_WEEK;      break;
            case AP_OUTGOING_TOTAL:                 iChartID = CHART_BARS_ID_OUTGOING_TOTAL_PER_WEEK;                   break;
        }
        
        iChartID += DateRange.getRecommendedIntervals();
        
        stChart.setBarsID(ACCOUNT, iChartID);
        
        viewAccounts(Accounts.getAccountsViewed());
    }
    
    //Chart
    private static List<Bars> buildPerCategoryBars()
    {
        List<String> lstAccountNames = new ArrayList<>();
        List<Bars> lstBars = new ArrayList<>();
        int iDirection = stChart.getCategories().get(0).getDirection();
        
        for(Category c : stChart.getCategories())
            if(!lstAccountNames.contains(c.getAccountName()))
                lstAccountNames.add(c.getAccountName());
        
        for(String s : lstAccountNames)
        {
            List<Category> lst = new ArrayList<>();
            Bars b;
            String sTitle;
            
            for(Category c : stChart.getCategories())
                if(s.equals(c.getAccountName()))
                    lst.add(c);
            
            if(iDirection == IN)    sTitle = "Incoming - Per Category";
            else                    sTitle = "Outgoing - Per Category";
            
            b = new Bars(s, CATEGORY, iDirection, CHART_BARS_ID_PER_CATEGORY, sTitle);
            
            b.setDataSeriesForCategories(lst, iDirection);
            lstBars.add(b);
        }
        
        return lstBars;
    }
    private static List<Bars> buildPerEntryBars()
    {
        List<String> lstAccountNames = new ArrayList<>();
        List<Bars> lstBars = new ArrayList<>();
        int iDirection = stChart.getEntries().get(0).getDirection();
        
        for(Entry e : stChart.getEntries())
            if(!lstAccountNames.contains(e.getAccountName()))
                lstAccountNames.add(e.getAccountName());
        
        for(String s : lstAccountNames)
        {
            List<Entry> lst = new ArrayList<>();
            Bars b;
            String sTitle;
            
            for(Entry e : stChart.getEntries())
                if(s.equals(e.getAccountName()))
                    lst.add(e);
            
            if(iDirection == IN)    sTitle = "Incoming - Per Entry";
            else                    sTitle = "Outgoing - Per Entry";
            
            b = new Bars(s, ENTRY, iDirection, CHART_BARS_ID_PER_ENTRY, sTitle);
            
            b.setDataSeriesForEntries(lst);
            lstBars.add(b);
        }
        
        return lstBars;
    }
    private static void buildChart()
    {
        initChart();
        
        dLimit = findZoomLimit();
        
        for(Bars b : stChart.getBars())
            chtStackedBar.getData().add(b.getSeriesClone());
        
        chtStackedBar.setTitle(stChart.getTitle());
        
        AnchorPane.setTopAnchor(chtStackedBar, 0.0);
        AnchorPane.setBottomAnchor(chtStackedBar, 0.0);
        AnchorPane.setLeftAnchor(chtStackedBar, 10.0);
        AnchorPane.setRightAnchor(chtStackedBar, 10.0);
        
        setFontSize();
        rebuildNodeLabels();
        rebuildContextMenu();
    }
    private static void showChart()
    {
        buildChart();
        resetChartInAnchorPane();
        
        if(stChart.areBarValuesViewed() && (Windows.isLarge(WINDOW_CHART_STACKED_BAR) || FullScreen.isActive()))
            Windows.refreshWindow(WINDOW_CHART_STACKED_BAR);
        
        if(FullScreen.isActive())
            FullScreen.refreshDate();
    }
    
    //FullScreen
    private static void setFullScreen(boolean bShow)
    {
        if(bShow)   FullScreen.show(CHART_STACKED_BAR, apStackedBarChart);
        else        FullScreen.hide();
        
        if(!bShow)
        {
            Windows.restoreChart(CHART_STACKED_BAR, apStackedBarChart);
            Windows.setSplitPane(CHART_STACKED_BAR);
            
            resetChartInAnchorPane();
            if(stChart.areBarValuesViewed())
                Windows.refreshWindow(WINDOW_CHART_STACKED_BAR);
        }

        rebuildContextMenu();
    }

    //External API -------------------------------------------------------------
    
    //Tableviews Context Menu - (via Charts Class) -----------------------------
    public static void viewAccounts(List<Account> lst)
    {
        stChart.clear();
        stChart.setAccounts(lst);
        
        for(Account a : lst)
            stChart.addBars(a.getChartBars().getBars(stChart.getBarsID(ACCOUNT)));
        
        showChart();
    }
    public static void viewCategories(List<Category> lst)
    {
        stChart.clear();
        stChart.setCategories(lst);
        
        if(isAccountProperty(lst))
        {
            viewAccountProperty(lst);
            return;
        }
        
        if(stChart.getBarsID(CATEGORY) == CHART_BARS_ID_PER_CATEGORY)
        {
            if(lst.size() == 1)
                stChart.addBars(lst.get(0).getChartBars().getRecommendedIntervalBars());
            else
                for(Bars b : buildPerCategoryBars())
                    stChart.addBars(b);
        }
        else
            for(Category c : lst)
                stChart.addBars(c.getChartBars().getBars(stChart.getBarsID(CATEGORY)));
        
        showChart();
    }
    public static void viewEntries(List<Entry> lst)
    {
        stChart.clear();
        stChart.setEntries(lst);
        
        if(stChart.getBarsID(ENTRY) == CHART_BARS_ID_PER_ENTRY)
        {
            if(lst.size() == 1)
                stChart.addBars(lst.get(0).getChartBars().getRecommendedIntervalBars());
            else
                for(Bars b : buildPerEntryBars())
                    stChart.addBars(b);
        }
        else
            for(Entry e : lst)
                stChart.addBars(e.getChartBars().getBars(stChart.getBarsID(ENTRY)));
        
        showChart();
    }
    public static void viewAll()
    {
        stChart.clear();
        stChart.addBars(Accounts.getAll().getChartBars().getBars(stChart.getBarsID(ALL)));
        showChart();
    }
    
    //Stacked Bar Chart Context Menu -------------------------------------------
    public static void view(int... iParam)
    {
        switch(iParam[0])
        {
            case SELECTED_ACCOUNTS:     stChart.setBarsID(ACCOUNT, iParam[1]);      break;
            case SELECTED_CATEGORIES:   stChart.setBarsID(CATEGORY, iParam[1]);     break;
            case SELECTED_ENTRIES:      stChart.setBarsID(ENTRY, iParam[1]);        break;
            case SELECTED_ALL:          stChart.setBarsID(ALL, iParam[1]);          break;
        }
        
        switch(iParam[0])
        {
            case SELECTED_ACCOUNTS:     viewAccounts(stChart.getAccounts());        break;
            case SELECTED_CATEGORIES:   viewCategories(stChart.getCategories());    break;
            case SELECTED_ENTRIES:      viewEntries(stChart.getEntries());          break;
            case SELECTED_ALL:          viewAll();                                  break;
            case SHOW_VALUES:           showBarValues();                            break;
            case HIDE_VALUES:           hideBarValues();                            break;
            case ZOOM_10:               setZoom(ZOOM_10);                           break;
            case ZOOM_25:               setZoom(ZOOM_25);                           break;
            case ZOOM_50:               setZoom(ZOOM_50);                           break;
            case ZOOM_75:               setZoom(ZOOM_75);                           break;
            case ZOOM_100:              setZoom(ZOOM_100);                          break;
            case ZOOM_CUSTOM:           showZoomCustomDialog();                     break;
            case SHOW_FULL_SCREEN:      setFullScreen(true);                        break;
            case HIDE_FULL_SCREEN:      setFullScreen(false);                       break;
        }
        
        switch(iParam[0])
        {
            case SHOW_VALUES:           stChart.setBarValuesViewed(true);           break;
            case HIDE_VALUES:           stChart.setBarValuesViewed(false);          break;
        }
    }
    
    //Refresh via Cursor Left/Right | Date Set
    public static void refresh()
    {
        //NB: Given that the entries chart data is built 'on the fly' when they are selected and sent to charts from the History table, it is necessary to rebuild the chart data for any current entries
        //which are being viewed in charts when refreshed. This procedure is only needs to be done once in this class given that it is the first class called in Charts.refreshAll() whereby the rebuilt
        //chat data is automatically available in the Pie/Line charts.
        if(stChart.getSource() == ENTRY)
            History.rebuildEntries(stChart.getEntries());
        
        switch(stChart.getSource())
        {
            case ACCOUNT:       viewAccounts(stChart.getAccounts());                break;
            case CATEGORY:      viewCategories(stChart.getCategories());            break;
            case ENTRY:         viewEntries(stChart.getEntries());                  break;
            case ALL:           viewAll();                                          break;
        }
    }
    
    //Windows ------------------------------------------------------------------
    public static void setFontSize()
    {
        if(chtStackedBar != null)
        {
            if(Windows.isLarge(WINDOW_CHART_STACKED_BAR))   chtStackedBar.setStyle("-fx-font: " + FONT_SIZE_WINDOW_LARGE + " calibri;");
            else                                            chtStackedBar.setStyle("-fx-font: " + FONT_SIZE_WINDOW_SMALL + " calibri;");
        }
    }
    public static boolean areBarValuesViewed()
    {
        return chtStackedBar != null && stChart.areBarValuesViewed();
    }
    public static void showBarValues()
    {
        resetChartInAnchorPane();
        
        dX = NOT_DEFINED;
        dY = NOT_DEFINED;
        
        for(Label l : lstLabels)
            if(isNodeLabelOverlapVisible(l))
                apStackedBarChart.getChildren().add(l);
    }
    public static void hideBarValues()
    {
        resetChartInAnchorPane();
    }
    
    //Fixed Period - Cursor Scrolling
    public static void clear()
    {
        apStackedBarChart.getChildren().clear();
    }
    
    //Zoom
    public static void setZoom(int iValue)
    {
        iZoom = iValue;
        Popup.hide();
        
        showChart();
    }
    
    //Full Screen
    public static void hideFullScreen()
    {
        setFullScreen(false);
    }
}