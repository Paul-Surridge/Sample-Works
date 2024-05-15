package Administration;

import AutoCategories.AutoCategories;
import Categories.Administration.CategoriesAdministration;
import Database.Administration.DatabaseAdministration;
import Integrity.Integrity;
import Refunds.Refunds;
import Reminders.Reminders;
import Shared.Constants;
import Shared.Popup;
import Statement.Statement;
import Watches.Watches;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TabPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.stage.Window;

public class Administration implements Constants{
    
    private static ScrollPane spAdministration;
    private static TabPane tpAdministration;
    private static Rectangle recAdministrationBackground;
    
    public Administration(ScrollPane spAdministration, TabPane tpAdministration, Rectangle recAdministrationBackground)
    {
        this.spAdministration               = spAdministration;
        this.tpAdministration               = tpAdministration;
        this.recAdministrationBackground    = recAdministrationBackground;
        
        initEventHandlersForMouse();
        initEventListners();
    }
    
    //Internal -----------------------------------------------------------------
    private static void initEventHandlersForMouse()
    {
        recAdministrationBackground.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
            Administration.hide();
        });
    }
    private static void initEventListners()
    {
        tpAdministration.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener()
        {
            @Override
            public void changed(ObservableValue o, Object oldVal, Object newVal)
            {
                switch(tpAdministration.getSelectionModel().getSelectedIndex())
                {
                    case ADMINISTRATION_TAB_REFUNDS:                    Refunds.init();                     break;
                    case ADMINISTRATION_TAB_WATCHES:                    Watches.init();                     break;
                    case ADMINISTRATION_TAB_DATABASE_ADMINISTRATION:    DatabaseAdministration.init();      break;
                }
            }
        });
    }
    private static void initTab(int iTabIndex)
    {
        switch(iTabIndex)
        {
            case ADMINISTRATION_TAB_STATEMENT:                  Statement.init();                   break;
            case ADMINISTRATION_TAB_REFUNDS:                    Refunds.init();                     break;
            case ADMINISTRATION_TAB_WATCHES:                    Watches.init();                     break;
            case ADMINISTRATION_TAB_CATEGORIES:                 CategoriesAdministration.init();    break;
            case ADMINISTRATION_TAB_AUTO_CATEGORIES:            AutoCategories.init();              break;
            case ADMINISTRATION_TAB_REMINDERS:                  Reminders.init();                   break;
            case ADMINISTRATION_TAB_INTEGRITY:                  Integrity.init();                   break;
            case ADMINISTRATION_TAB_DATABASE_ADMINISTRATION:    DatabaseAdministration.init();      break;
        }
    }
    
    //External API -------------------------------------------------------------
    public static void show(int iTabIndex)
    {
        Popup.hide();
        
        recAdministrationBackground.setVisible(true);
        spAdministration.setVisible(true);
        tpAdministration.getSelectionModel().select(iTabIndex);
        
        initTab(iTabIndex);
    }
    public static void hide()
    {
        recAdministrationBackground.setVisible(false);
        spAdministration.setVisible(false);
        
        for(int i = 0 ; i<tpAdministration.getTabs().size() ; i++)
            initTab(i);
    }
    public static boolean isVisible()
    {
        return spAdministration.isVisible();
    }
    public static boolean isVisible(int iTabIndex)
    {
        return spAdministration.isVisible() && tpAdministration.getSelectionModel().isSelected(iTabIndex);
    }
    public static Window getWindow()
    {
        return tpAdministration.getScene().getWindow();
    }
}
