package Shared;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class Popup implements Constants{
    
    private static List<AnchorPane> lstPopups = new ArrayList<>();
    private static List<AnchorPane> lstPopupsFixed = new ArrayList<>();
    private static Pane pnPopup;
    private static double dXStore, dYStore;
    
    public Popup(Pane pnPopup)
    {
        this.pnPopup = pnPopup;
    }
    
    //Internal -----------------------------------------------------------------
    private static double findNormalisedXCoordinate(double AnchorPaneWidth, double dX)
    {
        if(dX<AnchorPaneWidth)                      return 50;
        else if(dX<SCREEN_WIDTH - AnchorPaneWidth)  return dX;
        else                                        return SCREEN_WIDTH - AnchorPaneWidth - 50;
    }
    private static boolean areAllPopupsInvisible()
    {
        for(AnchorPane ap : lstPopups)
            if(ap.isVisible())
                return false;
        
        for(AnchorPane ap : lstPopupsFixed)
            if(ap.isVisible())
                return false;
        
        return true;
    }

    //External API -------------------------------------------------------------
    public static void init(AnchorPane... apPopups)
    {
        for(AnchorPane ap : apPopups)
            lstPopups.add(ap);
    }
    public static void initFixed(AnchorPane... apPopups)
    {
        for(AnchorPane ap : apPopups)
            lstPopupsFixed.add(ap);
    }
    public static void add(AnchorPane ap)
    {
        lstPopups.add(ap);
    }
    public static void show(int iIndex)
    {
        show(dXStore, dYStore, iIndex);
    }
    public static void show(MouseEvent me, int iIndex)
    {
        show(me.getSceneX(), me.getSceneY(), iIndex);
    }
    public static void show(double dX, double dY, int iIndex)
    {
        if(iIndex < lstPopups.size())
        {
            AnchorPane ap = lstPopups.get(iIndex);
            
            if(dX<=SCREEN_WIDTH)    ap.setLayoutX(findNormalisedXCoordinate(ap.getWidth(), dX));
            else                    ap.setLayoutX(findNormalisedXCoordinate(ap.getWidth(), dX-SCREEN_WIDTH));
            
            ap.setLayoutY(dY);
            ap.setVisible(true);
            pnPopup.setVisible(true);
        }
    }
    public static void hide()
    {
        for(AnchorPane ap : lstPopups)
            ap.setVisible(false);
        
        if(areAllPopupsInvisible())
            pnPopup.setVisible(false);
    }
    public static void hide(int iIndex)
    {
        lstPopups.get(iIndex).setVisible(false);
        
        if(areAllPopupsInvisible())
            pnPopup.setVisible(false);
    }
    public static void hideAndShowWithOffset(MouseEvent me, int iIndexHide, int iIndexShow, double dXOffset, double dYOffset)
    {
        lstPopups.get(iIndexHide).setVisible(false);
        show(me.getSceneX() + dXOffset, me.getSceneY() + dYOffset, iIndexShow);
    }
    public static void hideAllAndShow(MouseEvent me, int iIndex)
    {
        hide();
        show(me, iIndex);
    }
    public static void showFixed(int iIndex)
    {
        if(iIndex < lstPopupsFixed.size())
            lstPopupsFixed.get(iIndex).setVisible(true);
        
        pnPopup.setVisible(true);
    }
    public static void hideFixed(int iIndex)
    {
        if(iIndex < lstPopupsFixed.size())
            lstPopupsFixed.get(iIndex).setVisible(false);
        
        if(areAllPopupsInvisible())
            pnPopup.setVisible(false);
    }
    public static void setPosition(MouseEvent me)
    {
        dXStore = me.getSceneX();
        dYStore = me.getSceneY();
    }
    public static void setPosition(MenuItem mi)
    {
        dXStore = mi.getParentPopup().getAnchorX();
        dYStore = mi.getParentPopup().getAnchorY();
    }
    public static boolean isVisible()
    {
        return pnPopup.isVisible();
    }
}
