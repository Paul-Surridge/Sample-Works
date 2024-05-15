package Shared;

import Charts.LineChart.LineChart;
import Charts.PieChart.PieChart;
import Charts.StackedBarChart.StackedBarChart;
import FullScreen.FullScreen;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

public class Windows implements Constants{
    
    private static SplitPane spApp, spAccCat, spCharts;
    
    private static Timeline tl = new Timeline();
    private static List<Window> lstWindows = new ArrayList<>();
    private static Map<Integer, Window> mpWindows = new HashMap<>();
    private static List<Node> lstCharts = new ArrayList<>();
    
    public Windows(SplitPane spApp, SplitPane spAccCat, SplitPane spCharts)
    {
        this.spApp = spApp;
        this.spAccCat = spAccCat;
        this.spCharts = spCharts;
        
        initMap();
        initSplitPanes();
        initEventListner();
    }
    
    //Internal -----------------------------------------------------------------
    private static class Window
    {
        private int iID;
        private int iGroupID;
        
        public Window(int iID, int iGroupID)
        {
            this.iID = iID;
            this.iGroupID = iGroupID;
        }
        
        //External -------------------------------------------------------------
        public int getID()
        {
            return iID;
        }
        public int getGroupID()
        {
            return iGroupID;
        }
    }
    private static void initSplitPanes()
    {
        spApp.getDividers().get(0).setPosition(APP_DEFAULT_DIVIDER_LEFT);
        spApp.getDividers().get(1).setPosition(APP_DEFAULT_DIVIDER_RIGHT);
        
        spAccCat.getDividers().get(0).setPosition(ACC_CAT_DEFAULT_DIVIDER_UPPER);
        spAccCat.getDividers().get(1).setPosition(ACC_CAT_DEFAULT_DIVIDER_LOWER);
        
        spCharts.getDividers().get(0).setPosition(CHARTS_DEFAULT_DIVIDER_UPPER);
        spCharts.getDividers().get(1).setPosition(CHARTS_DEFAULT_DIVIDER_LOWER);
    }
    private static void initMap()
    {
        mpWindows.put(WINDOW_ACCOUNTS,              new Window(WINDOW_ACCOUNTS,             WINDOW_GROUP_ACC_CAT));
        mpWindows.put(WINDOW_CATEGORIES_IN,         new Window(WINDOW_CATEGORIES_IN,        WINDOW_GROUP_ACC_CAT));
        mpWindows.put(WINDOW_CATEGORIES_OUT,        new Window(WINDOW_CATEGORIES_OUT,       WINDOW_GROUP_ACC_CAT));
        mpWindows.put(WINDOW_CHART_STACKED_BAR,     new Window(WINDOW_CHART_STACKED_BAR,    WINDOW_GROUP_CHARTS));
        mpWindows.put(WINDOW_CHART_PIE,             new Window(WINDOW_CHART_PIE,            WINDOW_GROUP_CHARTS));
        mpWindows.put(WINDOW_CHART_LINE,            new Window(WINDOW_CHART_LINE,           WINDOW_GROUP_CHARTS));
        mpWindows.put(WINDOW_HISTORY,               new Window(WINDOW_HISTORY,              WINDOW_GROUP_HISTORY));
    }
    private static void initEventListner()
    {
        tl.statusProperty().addListener(new ChangeListener()
        {
            @Override
            public void changed(ObservableValue o, Object oldVal, Object newVal)
            {
                switch(tl.getStatus())
                {
                    case STOPPED:
                    {
                        if(StackedBarChart.areBarValuesViewed() && isLarge(WINDOW_CHART_STACKED_BAR))
                            StackedBarChart.showBarValues();
                        
                        if(LineChart.areLineValuesViewed() && (isLarge(WINDOW_CHART_LINE) || FullScreen.isActive()))
                            LineChart.showLineValues();
                        
                        break;
                    }
                    case RUNNING:
                    {
                        StackedBarChart.hideBarValues();
                        PieChart.hidesPieValues();
                        LineChart.hideLineValues();
                        
                        break;
                    }
                }
            }
        });
    }
    
    //Timeline
    private static void resetToDefault(SplitPane sp)
    {
        if      (sp == spApp)       setTimeline(spApp,      APP_DEFAULT_DIVIDER_LEFT,       APP_DEFAULT_DIVIDER_RIGHT);
        else if (sp == spAccCat)    setTimeline(spAccCat,   ACC_CAT_DEFAULT_DIVIDER_UPPER,  ACC_CAT_DEFAULT_DIVIDER_LOWER);
        else if (sp == spCharts)    setTimeline(spCharts,   CHARTS_DEFAULT_DIVIDER_UPPER,   CHARTS_DEFAULT_DIVIDER_LOWER);
    }
    private static void setTimeline(SplitPane sp, double... d)
    {
        tl.getKeyFrames().add(new KeyFrame(Duration.millis(CHART_SIZE_TRANSLATE_DURATION),    new KeyValue(sp.getDividers().get(0).positionProperty(),   d[0])));
        tl.getKeyFrames().add(new KeyFrame(Duration.millis(CHART_SIZE_TRANSLATE_DURATION),    new KeyValue(sp.getDividers().get(1).positionProperty(),   d[1])));
        
        if(d.length == 3)
            tl.getKeyFrames().add(new KeyFrame(Duration.millis(CHART_SIZE_TRANSLATE_DURATION),    new KeyValue(sp.getDividers().get(2).positionProperty(),   d[2])));
    }
    private static void setTimelineAccCat(int iID)
    {
        switch(iID)
        {
            case WINDOW_ACCOUNTS:           setTimeline(spAccCat, ACC_CAT_DEFAULT_DIVIDER_UPPER, ACC_CAT_DEFAULT_DIVIDER_LOWER);    break;
            case WINDOW_CATEGORIES_IN:      setTimeline(spAccCat, 0, 1);                                                            break;
            case WINDOW_CATEGORIES_OUT:     setTimeline(spAccCat, 0, 0.041);                                                        break;
        }
    }
    private static void setTimelineCharts(int iID)
    {
        switch(iID)
        {
            case WINDOW_CHART_STACKED_BAR:      setTimeline(spCharts, 0.8, 0.9);      break;
            case WINDOW_CHART_PIE:              setTimeline(spCharts, 0.1, 0.9);      break;
            case WINDOW_CHART_LINE:             setTimeline(spCharts, 0.1, 0.2);      break;
        }
    }
    
    //List Management
    private static int isWindowGroupInList(Window w)
    {
        for(int i = 0 ; i<lstWindows.size() ; i++)
            if(lstWindows.get(i).getGroupID() == w.getGroupID())
                return i;
        
        return NOT_DEFINED;
    }
    private static void addToList(Window w)
    {
        if(lstWindows.isEmpty())
            lstWindows.add(w);
        else
        {
            int iPos = isWindowGroupInList(w);
            
            if(iPos == NOT_DEFINED)
            {
                if(lstWindows.size() == 2)
                    lstWindows.remove(0);
                lstWindows.add(w);
            }
            else
                lstWindows.set(iPos, w);
        }
    }
    private static boolean inList(int iGroupID)
    {
        for(Window w : lstWindows)
            if(w.getGroupID() == iGroupID)
                return true;
        return false;
    }
    
    //Set Windows State
    private static void refreshChartsFontSize()
    {
        StackedBarChart.setFontSize();
        PieChart.setFontSize();
        LineChart.setFontSize();
    }
    private static void refreshWindows()
    {
        tl.getKeyFrames().clear();
        
        if(lstWindows.isEmpty())
        {
            resetToDefault(spApp);
            resetToDefault(spAccCat);
            resetToDefault(spCharts);
        }
        
        switch(lstWindows.size())
        {
            case 1:
            {
                int iID         = lstWindows.get(0).getID();
                int iGroupID    = lstWindows.get(0).getGroupID();
                
                switch(iGroupID)
                {
                    case WINDOW_GROUP_ACC_CAT:      setTimeline(spApp, APP_DEFAULT_DIVIDER_LEFT, APP_DEFAULT_DIVIDER_RIGHT);    break;
                    case WINDOW_GROUP_CHARTS:       setTimeline(spApp, 0.2, 0.8);                                               break;
                    case WINDOW_GROUP_HISTORY:      setTimeline(spApp, APP_DEFAULT_DIVIDER_LEFT, 0.558);                        break;
                }
                
                switch(iGroupID)
                {
                    case WINDOW_GROUP_ACC_CAT:      resetToDefault(spCharts);   break;
                    case WINDOW_GROUP_CHARTS:       resetToDefault(spAccCat);   break;
                    case WINDOW_GROUP_HISTORY:
                    {
                        resetToDefault(spAccCat);
                        resetToDefault(spCharts);
                        
                        break;
                    }
                }
                
                switch(iGroupID)
                {
                    case WINDOW_GROUP_ACC_CAT:      setTimelineAccCat(iID);     break;
                    case WINDOW_GROUP_CHARTS:       setTimelineCharts(iID);     break;
                }
                
                break;
            }
            case 2:
            {  
                if      (inList(WINDOW_GROUP_ACC_CAT) && inList(WINDOW_GROUP_CHARTS))       setTimeline(spApp, APP_DEFAULT_DIVIDER_LEFT, APP_DEFAULT_DIVIDER_RIGHT + 0.8);
                else if (inList(WINDOW_GROUP_ACC_CAT) && inList(WINDOW_GROUP_HISTORY))      setTimeline(spApp, APP_DEFAULT_DIVIDER_LEFT, 0.558);
                else if (inList(WINDOW_GROUP_CHARTS)  && inList(WINDOW_GROUP_HISTORY))      setTimeline(spApp, 0, 0.558);

                for(Window w : lstWindows)
                    switch(w.getGroupID())
                    {
                        case WINDOW_GROUP_ACC_CAT:  setTimelineAccCat(w.getID());   break;
                        case WINDOW_GROUP_CHARTS:   setTimelineCharts(w.getID());   break;
                    }
                
                break;
            }
        }
        
        tl.setCycleCount(1);
        tl.play();
    }
    private static void setSplitPaneDividers(SplitPane sp, double d0, double d1)
    {
        sp.getDividers().get(0).setPosition(d0);
        sp.getDividers().get(1).setPosition(d1);
    }
    
    //External API -------------------------------------------------------------
    public static void toggleWindow(int iWindow)
    {
        Window w = mpWindows.get(iWindow);
        
        if(lstWindows.contains(w))  lstWindows.remove(w);
        else                        addToList(w);
        
        refreshChartsFontSize();
        refreshWindows();
    }
    public static void refreshWindow(int iWindow)
    {
        Window w = mpWindows.get(iWindow);
        
        if(lstWindows.contains(w))
        {
            lstWindows.remove(w);
            addToList(w);
        }
        
        refreshChartsFontSize();
        refreshWindows();
    }
    public static void setSplitPane(int iWindow)
    {
        switch(iWindow)
        {
            case WINDOW_CHART_STACKED_BAR:      setSplitPaneDividers(spCharts, 0.8, 0.9);       break;
            case WINDOW_CHART_PIE:              setSplitPaneDividers(spCharts, 0.1, 0.9);       break;
        }
    }
    public static void resetToDefault()
    {
        Window w = mpWindows.get(WINDOW_ACCOUNTS);
        
        lstWindows.clear();
        addToList(w);
        
        refreshChartsFontSize();
        refreshWindows();
    }
    public static boolean isLarge(int iWindow)
    {
        return lstWindows.contains(mpWindows.get(iWindow));
    }
    public static boolean isChartLarge()
    {
        if(lstWindows.contains(mpWindows.get(WINDOW_CHART_STACKED_BAR)))    return true;
        if(lstWindows.contains(mpWindows.get(WINDOW_CHART_PIE)))            return true;
        if(lstWindows.contains(mpWindows.get(WINDOW_CHART_LINE)))           return true;
        
        return false;
    }
    
    //Full Screen
    public static void restoreChart(int iChartID, AnchorPane ap)
    {
        switch(iChartID)
        {
            case CHART_STACKED_BAR:     spCharts.getItems().set(0, ap);     break;
            case CHART_PIE:             spCharts.getItems().set(1, ap);     break;
            case CHART_LINE:            spCharts.getItems().set(2, ap);     break;
        }
    }
}
