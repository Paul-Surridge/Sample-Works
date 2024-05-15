package AutoCategories;

import Accounts.Account;
import Accounts.Accounts;
import Administration.Windows;
import Categories.Categories;
import Categories.Category;
import History.Entry;
import Shared.Blank;
import Shared.Constants;
import Shared.ContextMenuFactory;
import Shared.Popup;
import Shared.Table;
import Statement.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class AutoCategories implements Constants{
    
    private static RadioButton rbAutoCategoriesCurrent, rbAutoCategoriesSaving, rbAutoCategoriesISA;
    private static ToggleGroup tgAutoCategoriesAccounts = new ToggleGroup();
    private static Table<AutoCategoriesTableInsertable, AutoCategory> tbAutoCategoriesIn, tbAutoCategoriesOut;
    private static Windows obWindows;
    
    private static Account aSelected;
    private static int iSelectedTableview;
    
    public AutoCategories(SplitPane spAutoCategories, AnchorPane apAutoCategoriesInTable, AnchorPane apAutoCategoriesOutTable,
                    RadioButton rbAutoCategoriesCurrent, RadioButton rbAutoCategoriesSaving, RadioButton rbAutoCategoriesISA)
    {
        this.rbAutoCategoriesCurrent                = rbAutoCategoriesCurrent;
        this.rbAutoCategoriesSaving                 = rbAutoCategoriesSaving;
        this.rbAutoCategoriesISA                    = rbAutoCategoriesISA;
        
        obWindows = new Windows(spAutoCategories);
        
        initTables(apAutoCategoriesInTable, apAutoCategoriesOutTable);
        initEventHandlers();
        initEventListeners();
        initRadioButtons();
        
        clearAndReset();
    }
    
    //Internal -----------------------------------------------------------------
    private static void initTables(AnchorPane apAutoCategoriesInTable, AnchorPane apAutoCategoriesOutTable)
    {
        tbAutoCategoriesIn      = new Table(TABLEVIEW_ID_AUTO_CATEGORIES, AutoCategory.class, ContextMenuFactory::buildContextMenuForAutoCategories);
        tbAutoCategoriesOut     = new Table(TABLEVIEW_ID_AUTO_CATEGORIES, AutoCategory.class, ContextMenuFactory::buildContextMenuForAutoCategories);
        
        apAutoCategoriesInTable.getChildren().add(tbAutoCategoriesIn.getTableView());
        apAutoCategoriesOutTable.getChildren().add(tbAutoCategoriesOut.getTableView());
    }
    private static void initEventHandlers()
    {
        toggleWindow(tbAutoCategoriesIn, 0);
        toggleWindow(tbAutoCategoriesOut, 1);
    }
    private static void initEventListeners()
    {
        tgAutoCategoriesAccounts.selectedToggleProperty().addListener(new ChangeListener()
        {
            @Override
            public void changed(ObservableValue o, Object oldVal, Object newVal)
            {                                              
                if      (tgAutoCategoriesAccounts.getSelectedToggle() == rbAutoCategoriesCurrent)           aSelected = Accounts.get(BARCLAYS_CURRENT);
                else if (tgAutoCategoriesAccounts.getSelectedToggle() == rbAutoCategoriesSaving)            aSelected = Accounts.get(BARCLAYS_SAVING);
                else if (tgAutoCategoriesAccounts.getSelectedToggle() == rbAutoCategoriesISA)               aSelected = Accounts.get(BARCLAYS_ISA);
                
                if(aSelected != null)
                    showAutoCategories();
            }
        });
        
        tbAutoCategoriesIn.getTableView().focusedProperty().addListener(new ChangeListener()
        {
            @Override
            public void changed(ObservableValue o, Object oldVal, Object newVal)
            {
                if((boolean)newVal)
                    iSelectedTableview = IN;
            }
        });

        tbAutoCategoriesOut.getTableView().focusedProperty().addListener(new ChangeListener()
        {
            @Override
            public void changed(ObservableValue o, Object oldVal, Object newVal)
            {
                if((boolean)newVal)
                    iSelectedTableview = OUT;
            }
        });
    }
    private static void initRadioButtons()
    {
        rbAutoCategoriesCurrent.setToggleGroup(tgAutoCategoriesAccounts);
        rbAutoCategoriesSaving.setToggleGroup(tgAutoCategoriesAccounts);
        rbAutoCategoriesISA.setToggleGroup(tgAutoCategoriesAccounts);
    }
    private static void toggleWindow(Table tb, int iWindow)
    {
        tb.getTableView().addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
            if(me.getButton() == MouseButton.PRIMARY && me.getClickCount() == 2)
                obWindows.toggleWindow(iWindow);
        });
    }
    private static void clearAndReset()
    {
        obWindows.reset();
        
        showEmpty();
        clearRadioButtons();
        
        iSelectedTableview = NOT_DEFINED;
    }
    
    //Buttons/UI
    private static void clearRadioButtons()
    {
        for(Toggle t : tgAutoCategoriesAccounts.getToggles())
            t.setSelected(false);
        
        aSelected = null;
    }
    
    //Show Tables
    private static void showAutoCategoriesInTable(Table tb, int iDirection)
    {
        tb.clear();
        
        if(aSelected.numberOfAutoCategories(iDirection) == 0)   tb.setItem(new Blank(BLANK_FIELD_ENTRY_NAME, "No " + (iDirection == IN ? "Incoming" : "Outgoing") + " Auto Assigns"));
        else                                                    tb.setItems(aSelected.getAutoCategories(iDirection));
    }
    private static void showAutoCategories()
    {
        showAutoCategoriesInTable(tbAutoCategoriesIn, IN);
        showAutoCategoriesInTable(tbAutoCategoriesOut, OUT);
    }
    private static void showEmpty()
    {
        tbAutoCategoriesIn.setItem(new Blank());
        tbAutoCategoriesOut.setItem(new Blank());
    }
    
    //External API -------------------------------------------------------------
    public static void init()
    {
        clearAndReset();
    }
    
    //UI Buttons
    public static void delete()
    {
        Table<AutoCategoriesTableInsertable, AutoCategory> tb;
        
        Popup.hide();
        
        if(iSelectedTableview == IN)    tb = tbAutoCategoriesIn;
        else                            tb = tbAutoCategoriesOut;
        
        if(tb.areAllItemsSelected())
            aSelected.deleteAllAutoCategories(iSelectedTableview);
        else
            for(AutoCategory ac : tb.getSelectedItems())
                aSelected.deleteAutoCategory(ac);
        
        showAutoCategories();
    }
    
    //History Tableview Context Menu
    public static void set(List<Entry> lst, Category c, int iSource)
    {
        Map<String, Entry> mpFilter = new HashMap();
        AutoCategory ac;
        
        for(Entry e : lst)
            if(!mpFilter.containsKey(e.getName()))
                mpFilter.put(e.getName(), e);
        
        if(mpFilter.isEmpty())
            return;
        
        for(Entry e : mpFilter.values())
        {
            ac = new AutoCategory(e, c);
            
            Accounts.get(ac.getAccountID()).setAutoCategory(ac);
            
            if(iSource == TABLEVIEW_ID_STATEMENT)
                Statement.setAutoCategory(ac);
        }
        
        Accounts.get(c.getAccountID()).refresh();
        
        Categories.refresh();
        Categories.rebuildContextMenus();
    }
}
