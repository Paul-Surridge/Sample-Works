package DDSO;

import Accounts.Account;
import Accounts.Accounts;
import Database.Database;
import DateRange.DateRange;
import History.Entry;
import Shared.Constants;
import Shared.ContextMenuFactory;
import Shared.Debug;
import Shared.Formatter;
import Shared.Popup;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;

public class DDSO implements Constants{
    
    private static SplitPane spDDSO;
    private static AnchorPane apDDSO;
    private static ImageView ivInfoDDSO;
    private static javafx.scene.chart.BarChart chtDDSO;
    private static List<Series> lstSeries = new ArrayList<>();
    private static List<Entry> lstEntries = new ArrayList<>();
    private static ContextMenu cmChart;
    private static Rectangle recDialogBackground, recDDSONodeValue, recDDSOZoomAmount;
    private static Label labDDSONodeValue, labAccountsViewed;
    private static TextField tfDDSOZoomAmount;
    private static Button btDDSOZoomSet;
    private static LocalDate ldDate;
    private static int iZoom, iLimit, iBarValuesField;
    private static boolean bBarValuesVisible;
    
    private static List<Account> lstAccounts;
    
    public DDSO(SplitPane spDDSO, AnchorPane apDDSO, ImageView ivInfoDDSO, Rectangle recDDSONodeValue, Label labDDSONodeValue, Rectangle recDDSOZoomAmount, TextField tfDDSOZoomAmount, Button btDDSOZoomSet, Rectangle recDialogBackground)
    {
        this.spDDSO = spDDSO;
        this.apDDSO = apDDSO;
        this.ivInfoDDSO = ivInfoDDSO;
        this.recDDSONodeValue = recDDSONodeValue;
        this.labDDSONodeValue = labDDSONodeValue;
        this.recDDSOZoomAmount = recDDSOZoomAmount;
        this.tfDDSOZoomAmount = tfDDSOZoomAmount;
        this.btDDSOZoomSet = btDDSOZoomSet;
        this.recDialogBackground = recDialogBackground;
        
        bBarValuesVisible = true;
        iBarValuesField = CATEGORY_NAME;
        
        initEventHandlers();
        initEventListeners();
        initEventHandlersForKeyboard();
        initAccountsViewedLabel();
    }
    
    //Internal -----------------------------------------------------------------
    private static class Series
    {
        private XYChart.Series<String, Double> ser = new XYChart.Series<>();
        private Map<XYChart.Data<String, Double>, Entry> mpNodeEntries = new HashMap<>();
        private Map<XYChart.Data<String, Double>, Label> mpNodeLabels = new HashMap<>();
        
        public Series(int iNumberOfDaysInMonth)
        {
            buildSeriesBase(iNumberOfDaysInMonth);
        }
        public Series(Entry e)
        {
            add(e);
        }
        
        //Internal -------------------------------------------------------------
        private void buildSeriesBase(int iNumberOfDaysInMonth)
        {
            for(int iDay = 1 ; iDay<=iNumberOfDaysInMonth ; iDay++)
                ser.getData().add(new XYChart.Data<>(String.valueOf(iDay), 0.0));
        }
        private void setLabelLayoutPositionX(XYChart.Data<String, Double> di, Label l)
        {
            l.setLayoutX(di.getNode().getLayoutX() + chtDDSO.getYAxis().getWidth() + 17);
            
            if(l.getWidth() + l.getLayoutX() > chtDDSO.getWidth())
                l.setLayoutX(l.getLayoutX() - l.getWidth());
        }
        private void setLabelLayoutPositionY(XYChart.Data<String, Double> di, Label l)
        {
            l.setLayoutY(di.getNode().getLayoutY() + 23);
        }
        private String findLabelText(Entry e, int iField)
        {
            String sBuild = "";
            
            if(iField == NOT_DEFINED)
                return sBuild;
            
            switch(iField)
            {
                case ENTRY_NAME:        sBuild = e.getName() + " (£" + e.getAmountText() + ")";             break;
                case CATEGORY_NAME:     sBuild = e.getCategoryName() + " (£" + e.getAmountText() + ")";     break;
            }

            if(iZoom != ZOOM_100 && e.getAmount() > iLimit)     sBuild += " ^";
            else                                                sBuild += "";
            
            return sBuild;
        }
        private Label buildLabel(XYChart.Data<String, Double> di, Entry e)
        {
            Label l = new Label(findLabelText(e, iBarValuesField));
            
            setLabelLayoutPositionX(di, l);
            setLabelLayoutPositionY(di, l);
            
            di.getNode().layoutXProperty().addListener(new ChangeListener()
            {
                @Override
                public void changed(ObservableValue o, Object oldVal, Object newVal)
                {
                    setLabelLayoutPositionX(di, l);
                }
            });
            di.getNode().layoutYProperty().addListener(new ChangeListener()
            {
                @Override
                public void changed(ObservableValue o, Object oldVal, Object newVal)
                {
                    setLabelLayoutPositionY(di, l);
                }
            });
            di.getNode().addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
                    
                    hideBarValue();
            
                    if(me.getButton() == MouseButton.PRIMARY)
                        for(Series s : lstSeries)
                            if(s.containsNode(di))
                            {
                                if(apDDSO.getChildren().contains(l))    showBarValue(me, di);
                                else                                    apDDSO.getChildren().add(l);
                                
                                break;
                            }
                    
                    me.consume();
                });
            
            l.setVisible(bBarValuesVisible);
            
            return l;
        }
        
        //External API ---------------------------------------------------------
        
        //Chart Series
        public void add(Entry e)
        {
            XYChart.Data<String, Double> di;

            if(iZoom != ZOOM_100 && e.getAmount() > iLimit)     di = new XYChart.Data<>(e.getDateDayText(), Formatter.convert(DATABASE, CHART, iLimit));
            else                                                di = new XYChart.Data<>(e.getDateDayText(), Formatter.convert(DATABASE, CHART, e.getAmount()));
            
            ser.getData().add(di);
            mpNodeEntries.put(di, e);
        }
        public XYChart.Series<String, Double> getSeries()
        {
            return ser;
        }
        public boolean containsNode(XYChart.Data<String, Double> di)
        {
            return mpNodeEntries.containsKey(di);
        }
        
        //Lablling
        public void buildLabels()
        {
            mpNodeLabels.clear();
            
            for(XYChart.Data<String, Double> di : mpNodeEntries.keySet())
                mpNodeLabels.put(di, buildLabel(di, mpNodeEntries.get(di)));
        }
        public List<Label> getLabels()
        {
            return new ArrayList<>(mpNodeLabels.values());
        }
        public Label getLabel(XYChart.Data<String, Double> di)
        {
            return mpNodeLabels.get(di);
        }
    }
    
    private static void initChart()
    {
        chtDDSO = new javafx.scene.chart.BarChart(new CategoryAxis(), new NumberAxis());
    }
    private static void initEventHandlers()
    {
        recDDSOZoomAmount.addEventHandler(MouseEvent.MOUSE_PRESSED, (MouseEvent me) -> {
            me.consume();
        });
    }
    private static void initEventListeners()
    {
        tfDDSOZoomAmount.textProperty().addListener(new ChangeListener()
        {
            @Override
            public void changed(ObservableValue o, Object oldVal, Object newVal)
            {
                btDDSOZoomSet.setDisable(!isZoomCustomValid());
            }
        });
    }
    private static void initEventHandlerForChart()
    {
        cmChart = ContextMenuFactory.buildContextMenuForDDSO();
        
        chtDDSO.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
            
            cmChart.hide();
            hideBarValue();
            
            switch(me.getButton())
            {
                case SECONDARY:
                {
                    cmChart.show(chtDDSO, me.getScreenX(), me.getScreenY());
                    break;
                }
            }
        });
        
        chtDDSO.addEventHandler(KeyEvent.KEY_PRESSED, (KeyEvent ke) -> {
            switch(ke.getCode())
            {
                case ESCAPE:    hide();                 break;
            }
        });
    }
    private static void initEventHandlersForKeyboard()
    {
        tfDDSOZoomAmount.addEventHandler(KeyEvent.KEY_PRESSED, (KeyEvent ke) -> {
            if(ke.getCode() == KeyCode.ENTER)
                setZoom(ZOOM_CUSTOM);
        });
    }
    private static void initAccountsViewedLabel()
    {
        labAccountsViewed = new Label();
        
        AnchorPane.setTopAnchor(labAccountsViewed, 2.0);
        AnchorPane.setLeftAnchor(labAccountsViewed, 4.0);
    }
    private static void setChart()
    {
        initEventHandlerForChart();
        
        AnchorPane.setTopAnchor(chtDDSO, 0.0);
        AnchorPane.setBottomAnchor(chtDDSO, 0.0);
        AnchorPane.setLeftAnchor(chtDDSO, 0.0);
        AnchorPane.setRightAnchor(chtDDSO, 0.0);
    }
    private static void setInfoButton()
    {
        apDDSO.getChildren().add(ivInfoDDSO);
    }
    private static void setAccountsViewedLabel()
    {
        switch(lstAccounts.size())
        {
            case 0:     labAccountsViewed.setText("");                                                  break;
            case 1:     labAccountsViewed.setText(lstAccounts.get(0).getName());                        break;
            default:    labAccountsViewed.setText(Formatter.buildTitleBaseForAccounts(lstAccounts));    break;
        }
        
        if(!apDDSO.getChildren().contains(labAccountsViewed))
            apDDSO.getChildren().add(labAccountsViewed);
    }

    //Bars Value Labelling
    private static void showBarValue(MouseEvent me, XYChart.Data<String, Double> di)
    {
        Rectangle r = recDDSONodeValue;
        Label l = labDDSONodeValue;
        
        for(Series ser : lstSeries)
            if(ser.containsNode(di))
                l.setText(ser.getLabel(di).getText());
        
        l.setLayoutX(me.getX() + di.getNode().getLayoutX() + 25);
        l.setLayoutY(me.getY() + di.getNode().getLayoutY() + 22);
        l.autosize();
        
        r.setLayoutX(me.getX() + di.getNode().getLayoutX() + 20);
        r.setLayoutY(me.getY() + di.getNode().getLayoutY() + 20);
        r.setWidth(l.getWidth() + 10);
        
        r.setVisible(true);
        l.setVisible(true);
        
        if(!apDDSO.getChildren().contains(r))
        {
            if(r.getWidth() + r.getLayoutX() > chtDDSO.getWidth())
            {
                double dLeftAmount = r.getLayoutX() + r.getWidth() - chtDDSO.getWidth() + 10;
                
                r.setLayoutX(r.getLayoutX() - dLeftAmount);
                l.setLayoutX(l.getLayoutX() - dLeftAmount);
            }
            
            apDDSO.getChildren().add(r);
            apDDSO.getChildren().add(l);
        }
    }
    private static void hideBarValue()
    {
        apDDSO.getChildren().remove(recDDSONodeValue);
        apDDSO.getChildren().remove(labDDSONodeValue);
    }
    private static void buildBarValues()
    {
        clearBarValues();

        for(Series ser : lstSeries)
            ser.buildLabels();   
        
        for(Series ser : lstSeries)
            apDDSO.getChildren().addAll(ser.getLabels());
    }
    private static void clearBarValues()
    {
        apDDSO.getChildren().clear();
        apDDSO.getChildren().add(chtDDSO);
    }
    private static void showBarValues(int iField)
    {
        bBarValuesVisible = true;
        iBarValuesField = iField;
        
        buildAndShowChart();
    }
    private static void hideBarValues()
    {
        bBarValuesVisible = false;
        
        clearBarValues();
    }
    
    //Date Range
    private static int findFirstDayOfMonthDate()
    {
        return Formatter.convert(ldDate.withDayOfMonth(1));
    }
    private static int findLastDayOfMonthDate()
    {
        return Formatter.convert(LocalDate.of(ldDate.getYear(), ldDate.getMonthValue(), ldDate.lengthOfMonth()));
    }
    
    //Zoom
    private static int findZoomLimit(int iZoom)
    {
        int iLargestAmount = 0;
        
        if(iZoom != ZOOM_CUSTOM)
            tfDDSOZoomAmount.clear();
        
        switch(iZoom)
        {
            case ZOOM_CUSTOM:   return Formatter.convert(CURRENCY, POUNDS, DATABASE, tfDDSOZoomAmount.getText());
            case ZOOM_100:      return NOT_DEFINED;
        }
        
        for(Entry e : lstEntries)
            if(e.getAmount() > iLargestAmount)
                iLargestAmount = e.getAmount();
        
        switch(iZoom)
        {
            case ZOOM_10:       return (iLargestAmount/100) * 10;
            case ZOOM_25:       return (iLargestAmount/100) * 25;
            case ZOOM_50:       return (iLargestAmount/100) * 50;
            case ZOOM_75:       return (iLargestAmount/100) * 75;
        }
        
        return NOT_DEFINED;
    }
    private static void showZoomCustomDialog()
    {
        Popup.show(POPUP_DDSO_ZOOM);
        
        tfDDSOZoomAmount.requestFocus();
        tfDDSOZoomAmount.selectRange(0, tfDDSOZoomAmount.getText().length());
    }
    private static boolean isZoomCustomValid()
    {
        String s = tfDDSOZoomAmount.getText();
        
        if(s.isEmpty())
            return false;
        
        for(int i = 0 ; i<s.length() ; i++)
            if(!Character.isDigit(s.charAt(i)))
                return false;
        
        return true;
    }
    
    //Build and Show Chart
    private static void buildEntries()
    {
        List<Entry> lstEntriesAll = new ArrayList<>();
        
        int iFirstDayOfMonth = findFirstDayOfMonthDate();
        int iLastDayOfMonth = findLastDayOfMonthDate();
        
        lstEntries.clear();
        
        for(Account a : lstAccounts)
            lstEntriesAll.addAll(Database.getEntries(a, iFirstDayOfMonth, iLastDayOfMonth));
        
        for(Entry e : lstEntriesAll)
            if(e.isDDSO())
                lstEntries.add(e);
    }
    private static void buildChart()
    {
        Map<Integer, Integer> mp = new HashMap<>();
        int iNumberOfDaysInMonth = ldDate.lengthOfMonth();
        
        for(int i = 1 ; i<=iNumberOfDaysInMonth ; i++)
            mp.put(i, 0);
        
        lstSeries.clear();
        lstSeries.add(new Series(iNumberOfDaysInMonth));
        
        for(Entry e : lstEntries)
        {
            if(mp.get(e.getDateDay()) == 0)                         lstSeries.get(0).add(e);
            else if(mp.get(e.getDateDay()) >= lstSeries.size())     lstSeries.add(new Series(e));
            else                                                    lstSeries.get(mp.get(e.getDateDay())).add(e);
            
            mp.replace(e.getDateDay(), mp.get(e.getDateDay()) + 1);
        }
    }
    private static void showChart()
    {
        initChart();
        
        for(Series ser : lstSeries)
            chtDDSO.getData().add(ser.getSeries());
        
        setChart();
        
        apDDSO.getChildren().clear();
        apDDSO.getChildren().add(chtDDSO);
        
        chtDDSO.setStyle("-fx-font: " + FONT_SIZE_WINDOW_LARGE + " calibri;");
        chtDDSO.setTitle("Direct Debits / Standing Orders - " + Formatter.getMonthYear(ldDate));
        chtDDSO.setLegendVisible(false);
        
        buildBarValues();
        setInfoButton();
        setAccountsViewedLabel();
        
        recDialogBackground.setVisible(true);
        spDDSO.setVisible(true);
    }
    private static void buildAndShowChart()
    {
        buildEntries();
        
        iLimit = findZoomLimit(iZoom);

        buildChart();
        showChart();

        chtDDSO.requestFocus();
    }
    
    //External API -------------------------------------------------------------
    public static void init()
    {
        lstAccounts = Accounts.getAccountsViewed();

        ldDate = DateRange.getFrom();
        iZoom = ZOOM_100;
        
        buildAndShowChart();
    }
    public static void hide()
    {
        bBarValuesVisible = true;
        tfDDSOZoomAmount.clear();
        
        recDialogBackground.setVisible(false);
        spDDSO.setVisible(false);
    }
    public static boolean isActive()
    {
        return spDDSO.isVisible();
    }
    public static void setZoom(int iValue)
    {
        iZoom = iValue;
        Popup.hide();
        
        buildAndShowChart();
    }
    
    //Cursor Month Shift
    public static void showPreviousMonth()
    {
        ldDate = ldDate.minusMonths(1);
        buildAndShowChart();
    }
    public static void showNextMonth()
    {
        ldDate = ldDate.plusMonths(1);
        buildAndShowChart();
    }
    
    //Line Chart Context Menu --------------------------------------------------
    public static void view(int... iParam)
    {
        switch(iParam[0])
        {
            case SHOW_VALUES_ENTRY_NAMES:       showBarValues(ENTRY_NAME);      break;
            case SHOW_VALUES_CATEGORY_NAMES:    showBarValues(CATEGORY_NAME);   break;
            case HIDE_VALUES:                   hideBarValues();                break;
            case ZOOM_10:                       setZoom(ZOOM_10);               break;
            case ZOOM_25:                       setZoom(ZOOM_25);               break;
            case ZOOM_50:                       setZoom(ZOOM_50);               break;
            case ZOOM_75:                       setZoom(ZOOM_75);               break;
            case ZOOM_100:                      setZoom(ZOOM_100);              break;
            case ZOOM_CUSTOM:                   showZoomCustomDialog();         break;
            case PREVIOUS_MONTH:                showPreviousMonth();            break;
            case NEXT_MONTH:                    showNextMonth();                break;
            case CLOSE_CHART:                   hide();                         break;
        }
    }
}