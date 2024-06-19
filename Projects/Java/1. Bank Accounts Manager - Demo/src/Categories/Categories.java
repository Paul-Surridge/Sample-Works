package Categories;

import Accounts.Account;
import Accounts.Accounts;
import Charts.Charts;
import History.History;
import Shared.Blank;
import Shared.Constants;
import Shared.ContextMenuFactory;
import Shared.Table;
import Shared.Windows;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class Categories implements Constants{
    
    private static Label labCategoriesInHeader, labCategoriesOutHeader;
    private static CheckBox cbCategoriesInViewAll, cbCategoriesOutViewAll;
    private static Table<CategoriesTableInsertable, Category> tbCategoriesIn;
    private static Table<CategoriesTableInsertable, Category> tbCategoriesOut;
    private static boolean bViewAllInProgress, bSyncViewAllInProgress;
    
    private static Map<String, Category> mpCategoriesIn = new TreeMap<>();
    private static Map<String, Category> mpCategoriesOut = new TreeMap<>();
    
    public Categories(AnchorPane apCategoriesIn, AnchorPane apCategoriesOut,
                      Label labCategoriesInHeader, Label labCategoriesOutHeader,
                      CheckBox cbCategoriesInViewAll, CheckBox cbCategoriesOutViewAll)
    {
        this.labCategoriesInHeader  = labCategoriesInHeader;
        this.labCategoriesOutHeader = labCategoriesOutHeader;
        
        this.cbCategoriesInViewAll  = cbCategoriesInViewAll;
        this.cbCategoriesOutViewAll = cbCategoriesOutViewAll;
        
        
        initTables(apCategoriesIn, apCategoriesOut);
        initEventListners();
        initEventHandlersForMouse();
        initEventHandlersForKeyboard();
    }
    
    //Internal -----------------------------------------------------------------
    private static void initTables(AnchorPane apCategoriesIn, AnchorPane apCategoriesOut)
    {
        tbCategoriesIn = new Table(TABLEVIEW_ID_CATEGORIES, Category.class, ContextMenuFactory::buildContextMenuForCategories);
        tbCategoriesOut = new Table(TABLEVIEW_ID_CATEGORIES, Category.class, ContextMenuFactory::buildContextMenuForCategories);
        
        apCategoriesIn.getChildren().add(tbCategoriesIn.getTableView());
        apCategoriesOut.getChildren().add(tbCategoriesOut.getTableView());
    }
    private static void initEventListners()
    {
        listenForChangeInViewAllCheckbox(tbCategoriesIn, cbCategoriesInViewAll);
        listenForChangeInViewAllCheckbox(tbCategoriesOut, cbCategoriesOutViewAll);
    }
    private static void initEventHandlersForMouse()
    {
        handleToggleWindow(tbCategoriesIn, IN);
        handleToggleWindow(tbCategoriesOut, OUT);
        
        handleToggleWindow(labCategoriesInHeader, IN);
        handleToggleWindow(labCategoriesOutHeader, OUT);
    }
    private static void initEventHandlersForKeyboard()
    {
        handleKeyboardForTable(tbCategoriesIn);
        handleKeyboardForTable(tbCategoriesOut);
    }
    
    //Event Listeners
    private static void listenForChangeInViewAllCheckbox(Table<CategoriesTableInsertable, Category> tb, CheckBox cb)
    {
        cb.selectedProperty().addListener(new ChangeListener()
        {
            @Override
            public void changed(ObservableValue o, Object oldVal, Object newVal)
            {
                if(!bSyncViewAllInProgress)
                {
                    bViewAllInProgress = true;
                    
                    for(Category c : tb.getAllItems())
                        c.setViaViewAll((boolean) newVal);
                    
                    bViewAllInProgress = false;
                    
                    History.refresh();
                }
            }
        });
    }
    
    //Event Handlers
    private static void handleToggleWindow(Table tb, int iDirection)
    {
        tb.getTableView().addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
            if(me.getClickCount() == 2)
                switch(iDirection)
                {
                    case IN:    Windows.toggleWindow(WINDOW_CATEGORIES_IN);     break;
                    case OUT:   Windows.toggleWindow(WINDOW_CATEGORIES_OUT);    break;
                }
        });
    }
    private static void handleToggleWindow(Label lab, int iDirection)
    {
        lab.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
            if(me.getClickCount() == 2)
                switch(iDirection)
                {
                    case IN:    Windows.toggleWindow(WINDOW_CATEGORIES_IN);     break;
                    case OUT:   Windows.toggleWindow(WINDOW_CATEGORIES_OUT);    break;
                }
        });
    }
    private static void handleKeyboardForTable(Table tb)
    {
        tb.getTableView().addEventHandler(KeyEvent.KEY_PRESSED, (KeyEvent ke) -> {

            if(ke.getCode() == KeyCode.ENTER)
                sendToCharts(tb);
        });
    }
    
    //TableView
    private static Category getAccountProperty(String sName, int iDirection, int iFrequency, int iTotal, int iProperty)
    {
        Category c = new Category(NOT_DEFINED, "", iDirection, CATEGORY_TYPE_ACCOUNT_PROPERTY);
        
        c.setAccountProperty(sName, iFrequency, iTotal, iProperty);
        
        return c;
    }
    private static void removeCategoryAppendedAccountNames(int iDirection)
    {
        for(Account a : Accounts.getAccounts())
            for(Category c : a.getCategories(iDirection))
                c.removeAccountName();
    }
    private static void checkUserDefinedCategories(Account a, Map<String, Category> mp, int iDirection)
    {
        for(Category c : a.getCategoriesNonZero(iDirection, CATEGORY_TYPE_USER_DEFINED))
            if(mp.containsKey(c.getName()))
            {
                mp.get(c.getName()).appendAccountName();
                c.appendAccountName();
            }
            else
                mp.put(c.getName(), c);
    }
    private static void addUserDefinedCategoriesToTable(Account a, Table tb, int iDirection)
    {
        tb.addItems(a.getCategoriesNonZero(iDirection, CATEGORY_TYPE_USER_DEFINED));
    }
    private static void addUserDefinedCategoriesToTableCombinedInOrder(List<Account> lst, Table tb, int iDirection)
    {
        List<Category> lstSorted = new ArrayList<>();
        
        for(Account a : lst)
            lstSorted.addAll(a.getCategoriesNonZero(iDirection, CATEGORY_TYPE_USER_DEFINED));
        
        if(lstSorted.isEmpty())
            return;
        
        Collections.sort(lstSorted);
        
        tb.addItems(lstSorted);
    }
    private static void addInternalTransfersToTable(Account a, Table tb, int iDirection)
    {
        tb.addItems(a.getCategoriesNonZero(iDirection, CATEGORY_TYPE_INTERNAL_TRANSFER));
    }
    private static void addInternalTransfersToTableCombinedInOrder(List<Account> lst, Table tb, int iDirection)
    {
        List<Category> lstSorted = new ArrayList<>();
        
        for(Account a : lst)
            lstSorted.addAll(a.getCategoriesNonZero(iDirection, CATEGORY_TYPE_INTERNAL_TRANSFER));
        
        if(lstSorted.isEmpty())
            return;
        
        Collections.sort(lstSorted);
        
        tb.addItems(lstSorted);
    }
    private static void addAccountPropertyToTable(Table tb, int iProperty)
    {
        int iDirection = NOT_DEFINED;
        int iFrequency = 0;
        int iTotal = 0;
        String sName = "";
        
        for(Account a : Accounts.getAccountsViewed())
        {
            sName       = a.getAccountProperty(iProperty).getName();
            iDirection  = a.getAccountProperty(iProperty).getDirection();
            iFrequency  += a.getAccountProperty(iProperty).getFrequency();
            iTotal      += a.getAccountProperty(iProperty).getTotal();
        }
        
        tb.addItem(getAccountProperty(sName, iDirection, iFrequency, iTotal, iProperty));
    }
    private static void addAccountPropertiesToTable()
    {
        if(tbCategoriesIn.isEmpty())
            tbCategoriesIn.setItem(new Blank(BLANK_FIELD_NAME, "No Incoming"));
        else
        {
            tbCategoriesIn.addItem(new Blank());
            addAccountPropertyToTable(tbCategoriesIn, AP_INCOMING_INCOME);
            addAccountPropertyToTable(tbCategoriesIn, AP_INCOMING_INTERNAL_TRANSFER);
            tbCategoriesIn.addItem(new Blank());
            addAccountPropertyToTable(tbCategoriesIn, AP_INCOMING_TOTAL);
        }
        
        if(tbCategoriesOut.isEmpty())
            tbCategoriesOut.setItem(new Blank(BLANK_FIELD_NAME, "No Outgoing"));
        else
        {
            tbCategoriesOut.addItem(new Blank());
            addAccountPropertyToTable(tbCategoriesOut, AP_OUTGOING_PURCHASE);
            addAccountPropertyToTable(tbCategoriesOut, AP_OUTGOING_DDSO);
            addAccountPropertyToTable(tbCategoriesOut, AP_OUTGOING_OTHER);
            addAccountPropertyToTable(tbCategoriesOut, AP_OUTGOING_SPEND);
            tbCategoriesOut.addItem(new Blank());
            addAccountPropertyToTable(tbCategoriesOut, AP_OUTGOING_INTERNAL_TRANSFER);
            tbCategoriesOut.addItem(new Blank());
            addAccountPropertyToTable(tbCategoriesOut, AP_OUTGOING_TOTAL);
        }
        
        tbCategoriesIn.addItem(new Blank());
        tbCategoriesOut.addItem(new Blank());
    }
    
    //Charts
    private static void sendToCharts(Table tb)
    {
        List<Category> lst = tb.getSelectedItems();
        
        if(!areSelectedValid(lst))
            return;
        
        Charts.viewCategories(lst);
    }
    
    //External -----------------------------------------------------------------

    //TableView
    public static void refresh()
    {
        tbCategoriesIn.clear();
        tbCategoriesOut.clear();
        
        mpCategoriesIn.clear();
        mpCategoriesOut.clear();
        
        removeCategoryAppendedAccountNames(IN);
        removeCategoryAppendedAccountNames(OUT);
        
        switch(Accounts.getNumberOfAccountsViewed())
        {
            case 0:
            {
                tbCategoriesIn.setItem(new Blank());
                tbCategoriesOut.setItem(new Blank());
                
                break;
            }
            case 1:
            {
                Account a = Accounts.getFirstAccountViewed();
                
                addUserDefinedCategoriesToTable(a, tbCategoriesIn, IN);
                addUserDefinedCategoriesToTable(a, tbCategoriesOut, OUT);
                
                addInternalTransfersToTable(a, tbCategoriesIn, IN);
                addInternalTransfersToTable(a, tbCategoriesOut, OUT);

                break;
            }
            default:
            {
                List<Account> lstAccounts = Accounts.getAccountsViewed();
                
                for(Account a : lstAccounts)
                {
                    checkUserDefinedCategories(a, mpCategoriesIn, IN);
                    checkUserDefinedCategories(a, mpCategoriesOut, OUT);
                }
                
                addUserDefinedCategoriesToTableCombinedInOrder(lstAccounts, tbCategoriesIn, IN);
                addUserDefinedCategoriesToTableCombinedInOrder(lstAccounts, tbCategoriesOut, OUT);
                
                addInternalTransfersToTableCombinedInOrder(lstAccounts, tbCategoriesIn, IN);
                addInternalTransfersToTableCombinedInOrder(lstAccounts, tbCategoriesOut, OUT);
            }
        }

        addAccountPropertiesToTable();
        
        checkAndSynchroniseViewAll(IN);
        checkAndSynchroniseViewAll(OUT);
    }
    public static void rebuildContextMenus()
    {
        tbCategoriesIn.rebuildContextMenu();
        tbCategoriesOut.rebuildContextMenu();
    }
    
    //View All Checkbox
    public static boolean isViewAllInProgress()
    {
        return bViewAllInProgress;
    }
    public static void checkAndSynchroniseViewAll(Table<CategoriesTableInsertable, Category> tb, CheckBox cb)
    {
        boolean bAllViewed = true;
        
        for(Category c : tb.getAllItems())
            if(!c.getView())
                bAllViewed = false;
        
        bSyncViewAllInProgress = true;
        cb.setSelected(bAllViewed);
        bSyncViewAllInProgress = false;
    }
    public static void checkAndSynchroniseViewAll(int iDirection)
    {
        switch(iDirection)
        {
            case IN:    checkAndSynchroniseViewAll(tbCategoriesIn, cbCategoriesInViewAll);      break;
            case OUT:   checkAndSynchroniseViewAll(tbCategoriesOut, cbCategoriesOutViewAll);    break;
        }
    }
    
    //Undefined/Blank
    public static Category getUndefined(Account a, int iDirection)
    {
        return new Category(a.getAccountID(), UNDEFINED_TEXT, iDirection);
    }
    public static String getUndefinedName()
    {
        return UNDEFINED_TEXT;
    }
    
    //Context Menu
    public static boolean areSelectedValid(List<Category> lst)
    {
        int iSampleType;
        
        if(lst.isEmpty())
            return false;
        
        iSampleType = lst.get(0).getType();
        
        if(lst.size() == 1 && iSampleType == CATEGORY_TYPE_ACCOUNT_PROPERTY)
            return true;
        
        for(Category c : lst)
            if(c.getType() == CATEGORY_TYPE_ACCOUNT_PROPERTY)
                return false;
        
        return true;
    }
}
