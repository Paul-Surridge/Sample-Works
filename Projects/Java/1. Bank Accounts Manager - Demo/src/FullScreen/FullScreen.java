package FullScreen;

import Charts.LineChart.LineChart;
import Charts.PieChart.PieChart;
import Charts.StackedBarChart.StackedBarChart;
import DateRange.DateRange;
import Shared.Constants;
import Shared.Debug;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;

public class FullScreen implements Constants{
    
    private static SplitPane spFullScreen;
    private static AnchorPane apFullScreen;
    private static Rectangle recDialogBackground;
    private static Label labDateRange;
    private static int iChartID;
    
    public FullScreen(SplitPane spFullScreen, Rectangle recDialogBackground)
    {
        this.spFullScreen = spFullScreen;
        this.recDialogBackground = recDialogBackground;
        
        iChartID = NOT_DEFINED;
        
        initDateLabel();
    }
    
    //Internal -----------------------------------------------------------------
    private static void initDateLabel()
    {
        labDateRange = new Label();
        
        AnchorPane.setTopAnchor(labDateRange, 2.0);
        AnchorPane.setLeftAnchor(labDateRange, 4.0);
    }
    
    //External API -------------------------------------------------------------
    public static void show(int iID, AnchorPane ap)
    {
        apFullScreen = ap;
        
        labDateRange.setText(DateRange.getLabelText());
        
        if(!apFullScreen.getChildren().contains(labDateRange))
            apFullScreen.getChildren().add(labDateRange);
        
        spFullScreen.getItems().clear();
        spFullScreen.getItems().add(apFullScreen);
        
        recDialogBackground.setVisible(true);
        spFullScreen.setVisible(true);
        
        iChartID = iID;
    }
    public static void hide()
    {
        spFullScreen.getItems().clear();
        
        recDialogBackground.setVisible(false);
        spFullScreen.setVisible(false);
        
        iChartID = NOT_DEFINED;
    }
    public static boolean isActive()
    {
        return iChartID != NOT_DEFINED;
    }
    public static void restoreDate()
    {
        if(!apFullScreen.getChildren().contains(labDateRange))
            apFullScreen.getChildren().add(labDateRange);
    }
    public static void refreshDate()
    {
        labDateRange.setText(DateRange.getLabelText());
    }
    public static void escape()
    {
        switch(iChartID)
        {
            case CHART_STACKED_BAR:     StackedBarChart.hideFullScreen();   break;
            case CHART_PIE:             PieChart.hideFullScreen();          break;
            case CHART_LINE:            LineChart.hideFullScreen();         break;
        }
    }
}