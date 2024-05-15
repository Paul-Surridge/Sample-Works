package Categories.Administration;

import Accounts.Account;
import Accounts.Accounts;
import Administration.Administration;
import Administration.Windows;
import AutoCategories.AutoCategories;
import Categories.Categories;
import Categories.Category;
import Database.Database;
import DateRange.DateRange;
import History.Entry;
import History.History;
import Shared.Blank;
import Shared.Constants;
import Shared.ContextMenuFactory;
import Shared.Popup;
import Shared.Table;
import Statement.Statement;
import Watches.Watches;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class CategoriesAdministration implements Constants{
    
    private static Map<Integer, Map<String, Category>> mpIn = new HashMap<>();
    private static Map<Integer, Map<String, Category>> mpOut = new HashMap<>();
    
    private static RadioButton rbCategoriesCurrent, rbCategoriesSaving, rbCategoriesISA;
    private static ToggleGroup tgCategoriesAccounts = new ToggleGroup();
    private static Table<CategoriesAdministrationTableInsertable, Category> tbCategoriesIn, tbCategoriesOut;
    private static TextField tfCategoryCreate, tfCategoryRename;
    private static Button btCategoryCreate, btCategoryRename;
    private static Windows obWindows;
    
    private static Account a;
    private static Category cCreate, cRename;
    private static int iSelectedTableview, iCreateRenameDirection;
    
    public CategoriesAdministration(SplitPane spCategories, AnchorPane apCategoriesInTable, AnchorPane apCategoriesOutTable,
                    RadioButton rbCategoriesCurrent, RadioButton rbCategoriesSaving, RadioButton rbCategoriesISA,
                    TextField tfCategoryCreate, TextField tfCategoryRename, Button btCategoryCreate, Button btCategoryRename)
    {
        this.rbCategoriesCurrent                = rbCategoriesCurrent;
        this.rbCategoriesSaving                 = rbCategoriesSaving;
        this.rbCategoriesISA                    = rbCategoriesISA;
        
        this.tfCategoryCreate                   = tfCategoryCreate;
        this.tfCategoryRename                   = tfCategoryRename;
        
        this.btCategoryCreate                   = btCategoryCreate;
        this.btCategoryRename                   = btCategoryRename;
        
        obWindows = new Windows(spCategories);
        
        initTables(apCategoriesInTable, apCategoriesOutTable);
        initEventHandlersForMouse();
        initEventHandlersForKeyboard();
        initEventListeners();
        initRadioButtons();
        
        clearAndReset();
    }
    
    //Internal -----------------------------------------------------------------
    private static void initTables(AnchorPane apCategoriesInTable, AnchorPane apCategoriesOutTable)
    {
        tbCategoriesIn      = new Table(TABLEVIEW_ID_CATEGORIES_ADMINISTRATION, Category.class, ContextMenuFactory::buildContextMenuForCategoriesAdministration);
        tbCategoriesOut     = new Table(TABLEVIEW_ID_CATEGORIES_ADMINISTRATION, Category.class, ContextMenuFactory::buildContextMenuForCategoriesAdministration);
        
        apCategoriesInTable.getChildren().add(tbCategoriesIn.getTableView());
        apCategoriesOutTable.getChildren().add(tbCategoriesOut.getTableView());
    }
    private static void initEventHandlersForMouse()
    {
        toggleWindow(tbCategoriesIn, 0);
        toggleWindow(tbCategoriesOut, 1);
    }
    private static void initEventHandlersForKeyboard()
    {
        //Category Create
        tfCategoryCreate.addEventHandler(KeyEvent.KEY_PRESSED, (KeyEvent ke) -> {
            if(ke.getCode() == KeyCode.ENTER && isValidCategoryName(tfCategoryCreate))
                create();
        });
        
        //Category Rename
        tfCategoryRename.addEventHandler(KeyEvent.KEY_PRESSED, (KeyEvent ke) -> {
            if(ke.getCode() == KeyCode.ENTER && isValidCategoryName(tfCategoryRename))
                rename();
        });
    }
    private static void initEventListeners()
    {
        tgCategoriesAccounts.selectedToggleProperty().addListener(new ChangeListener()
        {
            @Override
            public void changed(ObservableValue o, Object oldVal, Object newVal)
            {                                              
                if      (tgCategoriesAccounts.getSelectedToggle() == rbCategoriesCurrent)           a = Accounts.get(BARCLAYS_CURRENT);
                else if (tgCategoriesAccounts.getSelectedToggle() == rbCategoriesSaving)            a = Accounts.get(BARCLAYS_SAVING);
                else if (tgCategoriesAccounts.getSelectedToggle() == rbCategoriesISA)               a = Accounts.get(BARCLAYS_ISA);
                
                if(a != null)
                    showCategories();
            }
        });
        
        tbCategoriesIn.getTableView().focusedProperty().addListener(new ChangeListener()
        {
            @Override
            public void changed(ObservableValue o, Object oldVal, Object newVal)
            {
                if((boolean)newVal)
                    iSelectedTableview = IN;
            }
        });

        tbCategoriesOut.getTableView().focusedProperty().addListener(new ChangeListener()
        {
            @Override
            public void changed(ObservableValue o, Object oldVal, Object newVal)
            {
                if((boolean)newVal)
                    iSelectedTableview = OUT;
            }
        });
        
        listenForInvalidCategoryName(tfCategoryCreate, btCategoryCreate);
        listenForInvalidCategoryName(tfCategoryRename, btCategoryRename);
    }
    private static void initRadioButtons()
    {
        rbCategoriesCurrent.setToggleGroup(tgCategoriesAccounts);
        rbCategoriesSaving.setToggleGroup(tgCategoriesAccounts);
        rbCategoriesISA.setToggleGroup(tgCategoriesAccounts);
    }
    private static void toggleWindow(Table tb, int iWindow)
    {
        tb.getTableView().addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
            if(me.getButton() == MouseButton.PRIMARY && me.getClickCount() == 2)
                obWindows.toggleWindow(iWindow);
        });
    }
    private static void listenForInvalidCategoryName(TextField tf, Button bt)
    {
        tf.textProperty().addListener(new ChangeListener()
        {
            @Override
            public void changed(ObservableValue o, Object oldVal, Object newVal)
            {
                if(tf.getText().isEmpty())  bt.setDisable(true);
                else                        bt.setDisable(!isValidCategoryName(tf));
            }
        });
    }
    private static void clearAndReset()
    {
        obWindows.reset();
        
        mpIn.clear();
        mpOut.clear();
        
        a = null;
        cCreate = null;
        cRename = null;
        
        iSelectedTableview = NOT_DEFINED;
        iCreateRenameDirection = NOT_DEFINED;
        
        clearRadioButtons();
        showEmpty();
    }
    
    //Buttons/UI
    private static void clearRadioButtons()
    {
        for(Toggle t : tgCategoriesAccounts.getToggles())
            t.setSelected(false);
        
        a = null;
    }
    
    //Dialog Popups
    private static void setCreateRenameTextField(TextField tf, String sText, int iDirection)
    {
        iCreateRenameDirection = iDirection;
        
        tf.setText(sText);
        tf.requestFocus();
        tf.selectRange(0, tf.getText().length());
    }
    private static boolean isValidCategoryName(TextField tf)
    {
        String s = tf.getText().trim();
        
        return !(a.containsCategory(s, iCreateRenameDirection));
    }
    
    //Create and Rebuild
    private static void rebuildExternal()
    {
        Statement.rebuildContextMenu();
        History.rebuildContextMenu();
        
        if(DateRange.isRangeValid())
            Accounts.getAll().clearChartData();
    }
    
    //Show Tables
    private static void addToMap()
    {
        switch(cCreate.getDirection())
        {
            case IN:    mpIn.get(cCreate.getAccountID()).put(cCreate.getName(), cCreate);       break;
            case OUT:   mpOut.get(cCreate.getAccountID()).put(cCreate.getName(), cCreate);      break;
        }
        
        cCreate.initProperties();
    }
    private static void removeFromMap(List<Category> lst)
    {
        for(Category c : lst)
            switch(iSelectedTableview)
            {
                case IN:    mpIn.get(a.getAccountID()).remove(c.getName());     break;
                case OUT:   mpOut.get(a.getAccountID()).remove(c.getName());    break;
            }
    }
    private static void removeAllFromMap()
    {
        switch(iSelectedTableview)
        {
            case IN:    mpIn.get(a.getAccountID()).clear();     break;
            case OUT:   mpOut.get(a.getAccountID()).clear();    break;
        }
    }
    private static Map<String, Category> buildMap(int iDirection)
    {
        Map<String, Category> mp = new TreeMap<>();
        
        for(Category c : a.getCategories(iDirection))
            mp.put(c.getName(), c);
        
        return mp;
    }
    private static void buildMapForSelected()
    {
        int iID = a.getAccountID();
        
        mpIn.put(iID, buildMap(IN));
        mpOut.put(iID, buildMap(OUT));
        
        for(Category c : mpIn.get(iID).values())
            c.initProperties();
        for(Category c : mpOut.get(iID).values())
            c.initProperties();
        
        for(Entry e : Database.getEntries(a))
            switch(e.getDirection())
            {
                case IN:    mpIn.get(iID).get(e.getCategoryName()).addTotalAll(e.getAmount());    break;
                case OUT:   mpOut.get(iID).get(e.getCategoryName()).addTotalAll(e.getAmount());   break;
            }
        
        for(Category c : mpIn.get(iID).values())
            c.setTotalAll();
        for(Category c : mpOut.get(iID).values())
            c.setTotalAll();
    }
    private static void showCategoriesInTable(Table tb, int iDirection)
    {
        tb.setItems(iDirection == IN ? mpIn.get(a.getAccountID()).values() : mpOut.get(a.getAccountID()).values());
    }
    private static void showCategories()
    {
        if(!mpIn.containsKey(a.getAccountID()))
            buildMapForSelected();
        
        showCategoriesInTable(tbCategoriesIn, IN);
        showCategoriesInTable(tbCategoriesOut, OUT);
    }
    private static void showEmpty()
    {
        tbCategoriesIn.setItem(new Blank());
        tbCategoriesOut.setItem(new Blank());
    }
    
    //External API -------------------------------------------------------------
    public static void init()
    {
        clearAndReset();
    }
    
    //Dialog Popups
    public static void showDialogCreate(double dX, double dY)
    {
        cCreate = new Category(a.getAccountID(), "", iSelectedTableview);

        Popup.show(dX, dY, POPUP_CATEGORY_CREATE);
        setCreateRenameTextField(tfCategoryCreate, CREATE_CATEGORY_TEXT, iSelectedTableview);
    }
    public static void showDialogCreate(Entry e)
    {
        cCreate = new Category(e.getAccountID(), "", e.getDirection());
        a = Accounts.get(e.getAccountID());

        Popup.show(POPUP_CATEGORY_CREATE);
        setCreateRenameTextField(tfCategoryCreate, CREATE_CATEGORY_TEXT, e.getDirection());
    }
    public static void showDialogRename(double dX, double dY)
    {
        switch(iSelectedTableview)
        {
            case IN:    cRename = tbCategoriesIn.getSelectedItem();         break;
            case OUT:   cRename = tbCategoriesOut.getSelectedItem();        break;
        }

        Popup.show(dX, dY, POPUP_CATEGORY_RENAME);
        setCreateRenameTextField(tfCategoryRename, cRename.getName(), cRename.getDirection());
    }
    
    //Actions
    public static void create()
    {
        Popup.hide();
        
        cCreate.setName(tfCategoryCreate.getText());
        a.addCategory(cCreate);
        
        if(Administration.isVisible(ADMINISTRATION_TAB_CATEGORIES))
        {
            addToMap();
            showCategories();
        }
        
        rebuildExternal();
    }
    public static void rename()
    {
        Popup.hide();
        
        a.renameCategory(cRename, tfCategoryRename.getText());
        
        History.rebuildContextMenu();
        showCategories();
    }
    public static void delete()
    {
        Table<CategoriesAdministrationTableInsertable, Category> tb;
        
        Popup.hide();
        
        if(iSelectedTableview == IN)    tb = tbCategoriesIn;
        else                            tb = tbCategoriesOut;
        
        if(tb.areAllItemsSelected())
            a.deleteAllCategories(iSelectedTableview);
        else
            for(Category ac : tb.getSelectedItems())
                a.deleteCategory(ac);
        
        if(tb.areAllItemsSelected())    removeAllFromMap();
        else                            removeFromMap(tb.getSelectedItems());
        
        showCategories();
        Categories.refresh();
        
        Watches.init();
        AutoCategories.init();
    }
}
