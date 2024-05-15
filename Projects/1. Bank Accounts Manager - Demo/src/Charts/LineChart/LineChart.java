package Charts.LineChart;

import Accounts.Account;
import Accounts.Accounts;
import Categories.Category;
import DateRange.DateRange;
import FullScreen.FullScreen;
import History.Entry;
import Shared.Constants;
import Shared.ContextMenuFactory;
import Shared.Debug;
import Shared.Formatter;
import Shared.Windows;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class LineChart implements Constants{
    
    private static AnchorPane apLineChart;
    private static javafx.scene.chart.LineChart chtLine;
    private static LineChartState stChart = new LineChartState();
    private static List<Label> lstLabels = new ArrayList<>();
    private static Map<Integer, Integer> mpXAxisDatesCompressed = new TreeMap<>();
    private static Rectangle recLineChartNodeValue;
    private static Label labLineChartNodeValue, labInfo;
    private static ContextMenu cmChart;
    private static int iXAxisMode;
    private static double dX, dY;
    
    public LineChart(AnchorPane apLineChart, Rectangle recLineChartNodeValue, Label labLineChartNodeValue)
    {
        this.apLineChart = apLineChart;
        this.recLineChartNodeValue = recLineChartNodeValue;
        this.labLineChartNodeValue = labLineChartNodeValue;
        
        initInfoLabel();
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
            initEventListeners();
            initEventHandler();
            
            setLabelLayoutPositionX();
            setLabelLayoutPositionY();
        }
        
        //Internal -------------------------------------------------------------
        private void initLabel()
        {
            l = new Label(Formatter.convert(CURRENCY, CHART, TABLEVIEW, di.getYValue()));
            l.setStyle("-fx-font: " + FONT_SIZE_WINDOW_LARGE + " calibri;");
            
            if(di.getYValue() == 0)
                l.setText("");
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
                    if(Windows.isLarge(WINDOW_CHART_LINE) || FullScreen.isActive())
                        if(l.getWidth() + l.getLayoutX() > chtLine.getWidth())
                            l.setLayoutX(l.getLayoutX() - l.getWidth());
                }
            });
        }
        private void initEventHandler()
        {
            di.getNode().addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {

                cmChart.hide();
                hideLineValue();

                switch(me.getButton())
                {
                    case PRIMARY:
                    {
                        if(!Windows.isChartLarge() || apLineChart.getChildren().contains(l))    showLineValue(me, di);
                        else if(Windows.isLarge(WINDOW_CHART_LINE) || FullScreen.isActive())    apLineChart.getChildren().add(l);

                        break;
                    }
                    case SECONDARY:
                    {
                        cmChart.show(chtLine, me.getScreenX(), me.getScreenY());

                        break;
                    }
                }

                me.consume();
            });
        }
        private void setLabelLayoutPositionX()
        {
            l.setLayoutX(di.getNode().getLayoutX() + chtLine.getYAxis().getWidth() + 12);
        }
        private void setLabelLayoutPositionY()
        {
            l.setLayoutY(di.getNode().getLayoutY() + 15);
        }
        
        //External API ---------------------------------------------------------
        public Label getLabel()
        {
            return l;
        }
        public XYChart.Data<String, Double> getDataItem()
        {
            return di;
        }
    }
    private static class SortXAxis<T extends String> implements Comparator<T>
    {
        List<String> lstWeekDays = Formatter.getWeekDayNames();
        
        //Internal -------------------------------------------------------------
        private int findDate(T s)
        {
            switch(stChart.getIntervalCompareSize())
            {
                case INTERVAL_WEEKS:    return lstWeekDays.indexOf(s);
                case INTERVAL_MONTHS:   return Integer.valueOf(s);
                case NOT_DEFINED:       return Formatter.convert(DATE, CHART, DATABASE, s);
            }
            
            return NOT_DEFINED;
        }
        
        //External -------------------------------------------------------------
        @Override
        public int compare(T s1, T s2)
        {
            int iDate1 = findDate(s1);
            int iDate2 = findDate(s2);
            
            if(iDate1 > iDate2)     return 1;
            if(iDate1 < iDate2)     return -1;
            
            return 0;
        }
    }
    
    //Initialisation
    private static void initChart()
    {
        chtLine = new javafx.scene.chart.LineChart<>(initXAxis(), new NumberAxis());
        
        chtLine.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
            
            cmChart.hide();
            hideLineValue();
            
            switch(me.getButton())
            {
                case PRIMARY:
                {
                    if(me.getClickCount() == 2 && !FullScreen.isActive())
                    {
                        Windows.toggleWindow(WINDOW_CHART_LINE);
                        rebuildContextMenu();
                    }
                    break;
                }
                case SECONDARY:
                {
                    cmChart.show(chtLine, me.getScreenX(), me.getScreenY());
                    break;
                }
            }
        });
    }
    private static void initInfoLabel()
    {
        labInfo = new Label();
        
        labInfo.setStyle("-fx-font: 12 calibri;");
        labInfo.setTextFill(Color.web("#a9a9a9"));
        
        AnchorPane.setTopAnchor(labInfo, 2.0);
        AnchorPane.setRightAnchor(labInfo, 4.0);
    }
    private static void rebuildContextMenu()
    {
        cmChart = ContextMenuFactory.buildContextMenuForLineChart(stChart);
    }
    private static void redrawChart()
    {
        //NB This manual refresh/redraw of the chart is so that all of the nodes are in correct place and labels can be spaced and shown
        if(stChart.areLineValuesViewed() && (Windows.isLarge(WINDOW_CHART_LINE) || FullScreen.isActive()))
            Windows.refreshWindow(WINDOW_CHART_LINE);
    }
    
    //Anchor Pane
    private static void resetChartInAnchorPane()
    {
        apLineChart.getChildren().clear();
        
        if(chtLine != null)
            apLineChart.getChildren().add(chtLine);
        
        refreshInfoLabel();
        
        if(FullScreen.isActive())
            FullScreen.restoreDate();
    }
    private static void refreshInfoLabel()
    {
        String s = "";
        
        if(!stChart.isEmpty())
            switch(iXAxisMode)
            {
                case CHART_LINE_X_AXIS_MODE_INTERVAL_COMPARE:       s = stChart.getSource() == ENTRY ? "" : stChart.getLine().getTitleSuffix();     break;
                case CHART_LINE_X_AXIS_MODE_COMPRESSED:             s = "Consolidated/Averaged Values";                                                      break;
            }
        
        labInfo.setText(s);
        apLineChart.getChildren().add(labInfo);
    }
    
    //X-Axis/Range
    private static CategoryAxis initXAxis()
    {
        LocalDate ldFrom = DateRange.getFrom();
        LocalDate ldTo = DateRange.getTo();
        
        int iNumberOfDays = DateRange.getNumberOfDays();
        int iSkip = findDateSkip(iNumberOfDays);
        
        iXAxisMode = findXAxisMode(iNumberOfDays);
        
        switch(iXAxisMode)
        {
            case CHART_LINE_X_AXIS_MODE_DIRECT:                 return buildXAxis(ldFrom, ldTo, iNumberOfDays);
            case CHART_LINE_X_AXIS_MODE_COMPRESSED:             return buildXAxis(ldFrom, ldTo, iNumberOfDays, iSkip);
            case CHART_LINE_X_AXIS_MODE_INTERVAL:               return new CategoryAxis();
            case CHART_LINE_X_AXIS_MODE_INTERVAL_COMPARE:       return buildXAxis(stChart.getIntervalCompareSize()); 
            
        }
        
        return null;
    }
    private static int findXAxisMode(int iNumberOfDays)
    {
        if(stChart.isInterval())                                    return CHART_LINE_X_AXIS_MODE_INTERVAL;
        if(stChart.isIntervalCompare())                             return CHART_LINE_X_AXIS_MODE_INTERVAL_COMPARE;
        if(iNumberOfDays > MAXIMUM_NUMBER_OF_CHART_X_AXIS_ITEMS)    return CHART_LINE_X_AXIS_MODE_COMPRESSED;
        
        return CHART_LINE_X_AXIS_MODE_DIRECT;
    }
    private static int findDateSkip(int iNumberOfDays)
    {
        int iMax = MAXIMUM_NUMBER_OF_CHART_X_AXIS_ITEMS;
        
        if(iNumberOfDays <= iMax)               return NOT_DEFINED;
        else if(iNumberOfDays % iMax == 0)      return iNumberOfDays/iMax;
        else                                    return (iNumberOfDays/iMax) + 1;
    }
    private static int findXAxisLeadingBuffer(int iNumberOfDates)
    {
        int iNumberOfExtra = iNumberOfDates / 15;

        return (iNumberOfExtra < 5) ? 5 : iNumberOfExtra;
    }
    private static CategoryAxis buildXAxis(LocalDate ldFrom, LocalDate ldTo, int iNumberOfDays)
    {
        CategoryAxis aX = new CategoryAxis();
        
        ldTo = DateRange.getTo().plusDays(findXAxisLeadingBuffer(iNumberOfDays));
            
        for(int iDay = 0 ; !ldFrom.equals(ldTo) ; ldFrom = ldFrom.plusDays(1), iDay++)
            aX.getCategories().add(Formatter.convert(ldFrom, CHART));
        
        return aX;
    }
    private static CategoryAxis buildXAxis(int iIntervalCompareSize)
    {
        CategoryAxis aX = new CategoryAxis();
        
        switch(iIntervalCompareSize)
        {
            case INTERVAL_WEEKS:
            {
                for(String s : Formatter.getWeekDayNames())
                    aX.getCategories().add(s);
                break;
            }
            case INTERVAL_MONTHS:
            {
                for(int iDay = 1 ; iDay <= 32 ; iDay++)
                    aX.getCategories().add(String.valueOf(iDay));
                break;
            }
            case INTERVAL_YEARS:
            {
                for(String s : Formatter.getMonthNames())
                    aX.getCategories().add(s);
                break;
            }
        }
        
        return aX;
    }
    private static CategoryAxis buildXAxis(LocalDate ldFrom, LocalDate ldTo, int iNumberOfDays, int iSkip)
    {
        CategoryAxis aX = new CategoryAxis();
        List<Integer> lst = new ArrayList<>();

        buildXAxisAndCompressedDatesList(aX, lst, ldFrom, ldTo, iNumberOfDays, iSkip);
        buildXAxisDatesCompressedMap(lst, ldFrom, ldTo, iSkip);

        return aX;
    }
    private static void buildXAxisAndCompressedDatesList(CategoryAxis aX, List<Integer> lst, LocalDate ldFrom, LocalDate ldTo, int iNumberOfDays, int iSkip)
    {
        ldTo = ldTo.plusDays(findXAxisLeadingBuffer(iNumberOfDays));
        
        for(int iDay = 0 ; !ldFrom.equals(ldTo) ; ldFrom = ldFrom.plusDays(1), iDay++)
            if(iDay % iSkip == 0)
            {
                lst.add(Formatter.convert(ldFrom));
                aX.getCategories().add(Formatter.convert(ldFrom, CHART));
            }
    }
    private static void buildXAxisDatesCompressedMap(List<Integer> lst, LocalDate ldFrom, LocalDate ldTo, int iSkip)
    {
        int iIndex = 0;
        int iPointer = 0;
        int iHalfSkip = iSkip/2;
        
        mpXAxisDatesCompressed.clear();
        
        for(int iDay = 0 ; !ldFrom.equals(ldTo.plusDays(1)) ; ldFrom = ldFrom.plusDays(1), iDay++)
        {
            if(iDay == (iIndex + iHalfSkip))
                iPointer++;
            
            if(iDay % iSkip == 0)
                iIndex = iDay;
            
            if(iPointer >= lst.size())
                iPointer = lst.size() - 1;
            
            mpXAxisDatesCompressed.put(Formatter.convert(ldFrom), lst.get(iPointer));
        }
    }
    private static void sortXAxis()
    {
        CategoryAxis aX = (CategoryAxis)chtLine.getXAxis();
        ObservableList<String> olstCategories = aX.getCategories();
        
        olstCategories.sort(new SortXAxis());
        aX.setCategories(olstCategories);
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
        
        for(int i = 0 ; i<chtLine.getData().size() ; i++)
        {
            ser = (XYChart.Series<String, Double>)chtLine.getData().get(i);
            
            for(int j = 0 ; j<ser.getData().size() ; j++)
            {
                NodeLabel nl = new NodeLabel((XYChart.Data<String, Double>)ser.getData().get(j));
                
                lstLabels.add(nl.getLabel());
            }
        }
    }
    
    //Node Value Popup
    private static void showLineValue(MouseEvent me, XYChart.Data<String, Double> di)
    {
        Rectangle r = recLineChartNodeValue;
        Label l = labLineChartNodeValue;
        
        Node n = di.getNode();
        
        l.setText(di.getXValue() +" £" + Formatter.convert(CURRENCY, CHART, TABLEVIEW, di.getYValue()));
        l.setLayoutX(me.getX() + n.getLayoutX() + 25);
        l.setLayoutY(me.getY() + n.getLayoutY() + 22);
        l.autosize();
        
        r.setLayoutX(me.getX() + n.getLayoutX() + 20);
        r.setLayoutY(me.getY() + n.getLayoutY() + 20);
        r.setWidth(l.getWidth() + 10);
        
        r.setVisible(true);
        l.setVisible(true);
        
        if(!apLineChart.getChildren().contains(r))
        {
            if(r.getWidth() + r.getLayoutX() > chtLine.getWidth())
            {
                double dLeftAmount = r.getLayoutX() + r.getWidth() - chtLine.getWidth() + 10;
                
                r.setLayoutX(r.getLayoutX() - dLeftAmount);
                l.setLayoutX(l.getLayoutX() - dLeftAmount);
            }
            
            apLineChart.getChildren().add(r);
            apLineChart.getChildren().add(l);
        }
    }
    private static void hideLineValue()
    {
        apLineChart.getChildren().remove(recLineChartNodeValue);
        apLineChart.getChildren().remove(labLineChartNodeValue);
    }
    
    //Category - Account Property
    private static boolean isAccountProperty(List<Category> lst)
    {
        return (lst.get(0).getType() == CATEGORY_TYPE_ACCOUNT_PROPERTY && lst.size() == 1);
    }
    private static void viewAccountProperty(List<Category> lst)
    {
        Category c = lst.get(0);
        int iChartID = CHART_LINE_ID_OUTGOING_TOTAL_PER_WEEK;
        
        switch(c.getAccountProperty())
        {
            case AP_INCOMING_INCOME:                iChartID = CHART_LINE_ID_INCOMING_INCOME_PER_WEEK;                  break;
            case AP_INCOMING_INTERNAL_TRANSFER:     iChartID = CHART_LINE_ID_INCOMING_INTERNAL_TRANSFERS_PER_WEEK;      break;
            case AP_INCOMING_TOTAL:                 iChartID = CHART_LINE_ID_INCOMING_TOTAL_PER_WEEK;                   break;
            case AP_OUTGOING_PURCHASE:              iChartID = CHART_LINE_ID_OUTGOING_PURCHASES_PER_WEEK;               break;
            case AP_OUTGOING_DDSO:                  iChartID = CHART_LINE_ID_OUTGOING_DDSO_PER_WEEK;                    break;
            case AP_OUTGOING_OTHER:                 iChartID = CHART_LINE_ID_OUTGOING_OTHER_PER_WEEK;                   break;
            case AP_OUTGOING_SPEND:                 iChartID = CHART_LINE_ID_OUTGOING_SPEND_PER_WEEK;                   break;
            case AP_OUTGOING_INTERNAL_TRANSFER:     iChartID = CHART_LINE_ID_OUTGOING_INTERNAL_TRANSFERS_PER_WEEK;      break;
            case AP_OUTGOING_TOTAL:                 iChartID = CHART_LINE_ID_OUTGOING_TOTAL_PER_WEEK;                   break;
        }
        
        iChartID += DateRange.getRecommendedIntervals();
        
        stChart.setLineID(ACCOUNT, iChartID);
        
        viewAccounts(Accounts.getAccountsViewed());
    }
    
    //Chart
    private static void buildChart()
    {
        initChart();
        
        if(iXAxisMode == CHART_LINE_X_AXIS_MODE_INTERVAL_COMPARE)
            if(stChart.getIntervalCompareSize() == INTERVAL_YEARS)  chtLine.getData().addAll(stChart.getLine().getSeriesCloneIntervalsYears());
            else                                                    chtLine.getData().addAll(stChart.getLine().getSeriesCloneIntervals(stChart.getIntervalCompareSize()));
        else
            for(Line l : stChart.getLines())
                switch(iXAxisMode)
                {
                    case CHART_LINE_X_AXIS_MODE_DIRECT:
                    case CHART_LINE_X_AXIS_MODE_INTERVAL:           chtLine.getData().add(l.getSeriesClone());                                      break;
                    case CHART_LINE_X_AXIS_MODE_COMPRESSED:         chtLine.getData().add(l.getSeriesCloneCompressed(mpXAxisDatesCompressed));      break;
                }
        
        chtLine.setTitle(stChart.getTitle());
        
        AnchorPane.setTopAnchor(chtLine, 0.0);
        AnchorPane.setBottomAnchor(chtLine, 0.0);
        AnchorPane.setLeftAnchor(chtLine, 0.0);
        AnchorPane.setRightAnchor(chtLine, 0.0);
        
        if(!stChart.isInterval())
            sortXAxis();
        
        setFontSize();
        rebuildNodeLabels();
        rebuildContextMenu();
    }
    private static void showChart()
    {
        buildChart();
        resetChartInAnchorPane();
        redrawChart();
        
        if(FullScreen.isActive())
            FullScreen.refreshDate();
    }
    
    //FullScreen
    private static void setFullScreen(boolean bShow)
    {
        if(bShow)   FullScreen.show(CHART_LINE, apLineChart);
        else        FullScreen.hide();
        
        if(!bShow)
        {
            Windows.restoreChart(CHART_LINE, apLineChart);
            resetChartInAnchorPane();
            redrawChart();
        }

        rebuildContextMenu();
    }
    
    //Interval Compare
    private static void viewIntervalCompare(int iIntervalCompareSize)
    {
        stChart.setIntervalCompareSize(iIntervalCompareSize);
        showChart();
    }
    
    //External API -------------------------------------------------------------
    
    //Tableviews Context Menu - (via Charts Class) -----------------------------
    public static void viewAccounts(List<Account> lst)
    {
        stChart.clear();
        stChart.setAccounts(lst);

        for(Account a : lst)
            stChart.addLine(a.getChartLines().getLine(stChart.getLineID(ACCOUNT)));
        
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
        
        for(Category c : lst)
            if(!stChart.isFull())
                stChart.addLine(c.getChartLines().getLine(stChart.getLineID(CATEGORY)));
        
        showChart();
    }
    public static void viewEntries(List<Entry> lst)
    {
        stChart.clear();
        stChart.setEntries(lst);

        for(Entry e : lst)
            if(!stChart.isFull())
                stChart.addLine(e.getChartLines().getLine(stChart.getLineID(ENTRY)));
        
        showChart();
    }
    public static void viewAll()
    {
        stChart.clear();
        stChart.addLine(Accounts.getAll().getChartLines().getLine(stChart.getLineID(ALL)));
        
        showChart();
    }
    
    //Line Chart Context Menu --------------------------------------------------
    public static void view(int... iParam)
    {
        switch(iParam[0])
        {
            case SELECTED_ACCOUNTS:     stChart.setLineID(ACCOUNT, iParam[1]);      break;
            case SELECTED_CATEGORIES:   stChart.setLineID(CATEGORY, iParam[1]);     break;
            case SELECTED_ENTRIES:      stChart.setLineID(ENTRY, iParam[1]);        break;
            case SELECTED_ALL:          stChart.setLineID(ALL, iParam[1]);          break;
        }
        
        switch(iParam[0])
        {
            case SELECTED_ACCOUNTS:     viewAccounts(stChart.getAccounts());        break;
            case SELECTED_CATEGORIES:   viewCategories(stChart.getCategories());    break;
            case SELECTED_ENTRIES:      viewEntries(stChart.getEntries());          break;
            case SELECTED_ALL:          viewAll();                                  break;
            case SHOW_VALUES:           showLineValues();                           break;
            case HIDE_VALUES:           hideLineValues();                           break;
            case SHOW_FULL_SCREEN:      setFullScreen(true);                        break;
            case HIDE_FULL_SCREEN:      setFullScreen(false);                       break;
            case COMPARE_WEEKS:         viewIntervalCompare(INTERVAL_WEEKS);        break;
            case COMPARE_MONTHS:        viewIntervalCompare(INTERVAL_MONTHS);       break;
            case COMPARE_YEARS:         viewIntervalCompare(INTERVAL_YEARS);        break;
        }
        
        switch(iParam[0])
        {
            case SHOW_VALUES:           stChart.setLineValuesViewed(true);          break;
            case HIDE_VALUES:           stChart.setLineValuesViewed(false);         break;
        }
    }
    
    //Refresh via Cursor Left/Right
    public static void refresh()
    {
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
        if(chtLine != null)
        {
            if(Windows.isLarge(WINDOW_CHART_LINE))  chtLine.setStyle("-fx-font: " + FONT_SIZE_WINDOW_LARGE + " calibri;");
            else                                    chtLine.setStyle("-fx-font: " + FONT_SIZE_WINDOW_SMALL + " calibri;");
        }
    }
    public static boolean areLineValuesViewed()
    {
        return chtLine != null && stChart.areLineValuesViewed();
    }
    public static void showLineValues()
    {
        resetChartInAnchorPane();
        
        dX = NOT_DEFINED;
        dY = NOT_DEFINED;
        
        for(Label l : lstLabels)
            if(isNodeLabelOverlapVisible(l))
                apLineChart.getChildren().add(l);
    }
    public static void hideLineValues()
    {
        resetChartInAnchorPane();
    }
    
    //Fixed Period - Cursor Scrolling
    public static void clear()
    {
        apLineChart.getChildren().clear();
    }
    
    //Full Screen
    public static void hideFullScreen()
    {
        setFullScreen(false);
    }
}