package Accounts;

import Accounts.All.All;
import DateRange.DateRange;
import Categories.Categories;
import Charts.Charts;
import History.History;
import Shared.Blank;
import Shared.Constants;
import Shared.ContextMenuFactory;
import Shared.TableViewFactory;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.ListChangeListener;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class Accounts implements Constants
{
    private static List<Account> lstAccounts = new ArrayList<>();
    private static TableView<AccountsTableInsertable> tvAccounts;
    private static All all = new All("All Accounts");
    private static int iSelectedSource;
    
    public Accounts(AnchorPane apAccounts)
    {
        initAccounts();
        initAccountsTableView(apAccounts);
        initEventListners();
        initEventHandlersForMouse();
        initEventHandlersForKeyboard();
    }
    
    //Internal -----------------------------------------------------------------
    private static void initAccounts()
    {
        lstAccounts.add(new Account(BARCLAYS_CURRENT,   "Barclays Current",     "Current",  "BARCLAYS_CURRENT"));
        lstAccounts.add(new Account(BARCLAYS_SAVING,    "Barclays Saving",      "Save",     "BARCLAYS_SAVING"));
        lstAccounts.add(new Account(BARCLAYS_ISA,       "Barclays ISA",         "ISA",      "BARCLAYS_ISA"));
        
        for(Account a : lstAccounts)
            a.loadInternalTransfers();
    }
    private static void initAccountsTableView(AnchorPane ap)
    {
        List<AccountsTableInsertable> lstAccountsTableView = new ArrayList<>();
        
        lstAccountsTableView.addAll(lstAccounts);
        lstAccountsTableView.add(new Blank());
        lstAccountsTableView.add(all);
        
        tvAccounts = TableViewFactory.buildTable(lstAccountsTableView);
        
        ap.getChildren().add(tvAccounts);
    }
    private static void initEventListners()
    {
        tvAccounts.getSelectionModel().getSelectedIndices().addListener(new ListChangeListener()
        {
            @Override
            public void onChanged(ListChangeListener.Change c)
            {
                rebuildContextMenu();
            }
        });
    }
    private static void initEventHandlersForMouse()
    {
        tvAccounts.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
            if(me.getButton() == MouseButton.PRIMARY && me.getClickCount() == 2)
                sendToCharts();
        });
    }
    private static void initEventHandlersForKeyboard()
    {
        tvAccounts.addEventHandler(KeyEvent.KEY_PRESSED, (KeyEvent ke) -> {
            
            if(ke.getCode() == KeyCode.ENTER)
                sendToCharts();
        });
    }
    private static int findSelectedSource()
    {
        boolean bAccountFound = false;
        boolean bAllFound = false;
        Account a;
        
        for(AccountsTableInsertable item : tvAccounts.getSelectionModel().getSelectedItems())
        {
            if(item instanceof Account)
            {
                a = (Account)item;
                if(a.getView() && !a.isEmpty())
                    bAccountFound = true;
            }
            else if(item instanceof All && !all.isEmpty())
                bAllFound = true;
        }
        
        if(bAllFound)           return ALL;
        else if(bAccountFound)  return ACCOUNT;
        else                    return NOT_DEFINED;
    }
    private static List<Account> getSelectedAccounts()
    {
        List<Account> lst = new ArrayList<>();
        Account a;
        
        for(AccountsTableInsertable item : tvAccounts.getSelectionModel().getSelectedItems())
        {
            if(item instanceof Account)
            {
                a = (Account)item;
                if(a.getView() && !a.isEmpty())
                    lst.add(a);
            }
        }
        
        return lst;
    }
    private static void refresh()
    {
        if(DateRange.isRangeValid())
        {
            Categories.refresh();

            all.clear();

            for(Account a : lstAccounts)
            {
                if(a.getView()) a.showBalanceProperties();
                else            a.hideBalanceProperties();

                all.add(a);
            }

            all.finaliseBalanceProperties();
            all.rebuildChartData();

            if(!all.isEmpty())      History.refresh();
            else                    History.clear();

            if(!all.isEmpty())      Charts.refreshAll();
            else                    Charts.clearAll();
        }
    }
    
    //External API -------------------------------------------------------------
    
    //Accounts
    public static List<Account> getAccounts()
    {
        return lstAccounts;
    }
    public static List<Account> getAccountsViewed()
    {
        List<Account> lst = new ArrayList<>();
        
        for(Account a : lstAccounts)
            if(a.getView())
                lst.add(a);
                
        return lst;
    }
    public static List<Account> getAccountsSelected()
    {
        iSelectedSource = findSelectedSource();

        switch(iSelectedSource)
        {
            case ACCOUNT:   return getSelectedAccounts();
            case ALL:       return Accounts.getAccounts();
        }
        
        return new ArrayList<>();
    }
    public static Account get(int iAccountID)
    {
        return lstAccounts.get(iAccountID);
    }
    public static Account get(String sNameAbbreviated)
    {
        for(Account a : lstAccounts)
            if(a.getNameAbbreviated().equals(sNameAbbreviated))
                return a;
        
        return null;
    }
    public static Account getFirstAccountViewed()
    {
        for(Account a : lstAccounts)
            if(a.getView())
                return a;
        
        return null;
    }
    public static int getNumberOfAccountsViewed()
    {
        return getAccountsViewed().size();
    }
    public static boolean areAllAccountsViewed()
    {
        return getAccountsViewed().size() == NUMBER_OF_ACCOUNTS;
    }
    
    //Accounts All
    public static All getAll()
    {
        return all;
    }
    
    //Accounts Rebuild and Refresh
    public static void rebuildAndRefresh(Account a)
    {
        if(DateRange.isRangeValid())
        {
            a.rebuild(DateRange.getFrom(), DateRange.getTo());
            refresh();
        }
    }
    public static void rebuildAllAndRefresh()
    {
        if(DateRange.isRangeValid())
        {
            for(Account a : lstAccounts)
                a.rebuild(DateRange.getFrom(), DateRange.getTo());

            refresh();
        }
    }
    public static void refresh(Account a)
    {
        if(DateRange.isRangeValid())
        {
            Categories.refresh();

            if(a.getView()) a.showBalanceProperties();
            else            a.hideBalanceProperties();
            
            if(!all.isEmpty())  History.refresh();
            else                History.clear();
        }
    }
    
    //Enter/Double Click/Context Menu
    public static void sendToCharts()
    {
        iSelectedSource = findSelectedSource();
        
        switch(iSelectedSource)
        {
            case ACCOUNT:       Charts.viewAccounts(getSelectedAccounts());     break;
            case ALL:           Charts.viewAll();                               break;
        }
    }
    public static void selectAllAndSendToCharts()
    {
        tvAccounts.getSelectionModel().selectRange(0, 3);
        
        sendToCharts();
    }
    
    //Accounts Tableview Context Menu
    public static void rebuildContextMenu()
    {
        iSelectedSource = findSelectedSource();
        
        if(iSelectedSource == ACCOUNT || iSelectedSource == ALL)    tvAccounts.setContextMenu(ContextMenuFactory.buildContextMenuForAccounts(getAccountsSelected(), iSelectedSource));
        else                                                        tvAccounts.setContextMenu(null);
    }
}