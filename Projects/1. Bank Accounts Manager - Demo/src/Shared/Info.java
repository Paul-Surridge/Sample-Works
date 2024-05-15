package Shared;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class Info implements Constants{
    
    private static List<AnchorPane> lstInfos = new ArrayList<>();
    private static Pane pnInfo;
    private static AnchorPane apInfoHide;
    
    public Info(Pane pnInfo, AnchorPane... apInfos)
    {
        this.pnInfo = pnInfo;
        
        for(AnchorPane ap : apInfos)
            lstInfos.add(ap);
        
        hide();
    }

    //External API -------------------------------------------------------------
    public static void show(int iIndex)
    {
        if(iIndex < lstInfos.size())
        {
            lstInfos.get(iIndex).setVisible(true);
            pnInfo.setVisible(true);
        }
    }
    public static void hide()
    {
        for(AnchorPane ap : lstInfos)
            ap.setVisible(false);
        
        pnInfo.setVisible(false);
    }
    public static boolean isVisible()
    {
        return pnInfo.isVisible();
    }
}
