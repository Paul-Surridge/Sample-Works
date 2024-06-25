package Integrity;

import Integrity.CheckAccountHistory.CheckAccountHistory;
import Accounts.Account;
import Accounts.Accounts;
import Administration.Windows;
import AutoCategories.AutoCategory;
import Categories.Category;
import Shared.Constants;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.SplitPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import Database.Administration.DatabaseAdministrationEntriesTableInsertable;
import Database.Administration.DatabaseAdministrationCategoriesTableInsertable;
import Database.Administration.DatabaseAdministrationAutoCategoriesTableInsertable;
import Database.Administration.DatabaseAdministrationRefundsTableInsertable;
import Database.Administration.DatabaseAdministrationWatchesTableInsertable;
import Database.Database;
import DateRange.DateRange;
import History.Entry;
import History.HistoryTableInsertable;
import Integrity.CheckAccountDatabase.CheckAccountDatabase;
import Refunds.Refund;
import Shared.Blank;
import Shared.ContextMenuFactory;
import Shared.ErrorTableInsertable;
import Shared.Formatter;
import Shared.Table;
import Watches.Watch;
import java.time.LocalDate;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseButton;

public class Integrity implements Constants{
    
    private static RadioButton rbIntegrityAccountHistory, rbIntegrityAccountDatabase, rbIntegrityAccountHistoryAll, rbIntegrityAccountHistoryFrom;
    private static DatePicker dpIntegrityAccountHistoryFrom;
    
    private static Table<IntegrityCheckTableInsertable, Check> tbChecks;
    private static Table<ErrorTableInsertable, Error> tbErrors;
    
    private static Table<HistoryTableInsertable, Entry> tbHistory;
    private static Table<DatabaseAdministrationEntriesTableInsertable, Entry> tbEntries;
    private static Table<DatabaseAdministrationCategoriesTableInsertable, Category> tbCategories;
    private static Table<DatabaseAdministrationAutoCategoriesTableInsertable, AutoCategory> tbAutoCategories;
    private static Table<DatabaseAdministrationWatchesTableInsertable, Watch> tbWatches;
    private static Table<DatabaseAdministrationRefundsTableInsertable, Refund> tbRefunds;
    
    private static List<CheckAccountHistory> lstCheckAccountHistory = new ArrayList<>();
    private static List<CheckAccountDatabase> lstCheckAccountDatabase = new ArrayList<>();
    
    private static ToggleGroup tgMode = new ToggleGroup();
    private static ToggleGroup tgRange = new ToggleGroup();
    private static Windows obWindows;
    private static int iMode, iRange;
    
    public Integrity(SplitPane spIntegrity, AnchorPane apIntegrityTable, RadioButton rbIntegrityAccountHistory, RadioButton rbIntegrityAccountDatabase,
                    RadioButton rbIntegrityAccountHistoryAll, RadioButton rbIntegrityAccountHistoryFrom, DatePicker dpIntegrityAccountHistoryFrom)
    {
        this.rbIntegrityAccountHistory = rbIntegrityAccountHistory;
        this.rbIntegrityAccountDatabase = rbIntegrityAccountDatabase;
        
        this.rbIntegrityAccountHistoryAll = rbIntegrityAccountHistoryAll;
        this.rbIntegrityAccountHistoryFrom = rbIntegrityAccountHistoryFrom;
        this.dpIntegrityAccountHistoryFrom = dpIntegrityAccountHistoryFrom;
        
        obWindows = new Windows(spIntegrity);
        
        initTables(apIntegrityTable);
        initEventHandlers();
        initEventListeners();
        initRadioButtons();
        initCheckAccountHistory();
        initCheckAccountDatabase();
        initRange();
        
        clearAndReset();
    }
    
    //Internal -----------------------------------------------------------------
    private static void initTables(AnchorPane ap)
    {
        tbChecks            = new Table(TABLEVIEW_ID_INTEGRITY_CHECKS, Check.class, ContextMenuFactory::buildContextMenuForIntegrityChecks);
        tbErrors            = new Table(TABLEVIEW_ID_INTEGRITY_ERRORS, Error.class, ContextMenuFactory::buildContextMenuForIntegrityErrors);
        tbHistory           = new Table(TABLEVIEW_ID_HISTORY_WITHIN_ADMINISTRATION, Entry.class);
        tbEntries           = new Table(TABLEVIEW_ID_DATABASE_ENTRIES, Entry.class);
        tbCategories        = new Table(TABLEVIEW_ID_DATABASE_CATEGORIES, Category.class);
        tbAutoCategories    = new Table(TABLEVIEW_ID_DATABASE_AUTO_CATEGORIES, AutoCategory.class);
        tbWatches           = new Table(TABLEVIEW_ID_DATABASE_WATCHES, Watch.class);
        tbRefunds           = new Table(TABLEVIEW_ID_DATABASE_REFUNDS, Refund.class);
        
        ap.getChildren().add(tbChecks.getTableView());
    }
    private static void initEventHandlers()
    {
        toggleWindow(tbChecks, 0);
        toggleWindow(tbErrors, 1);
        toggleWindow(tbHistory, 2);
        toggleWindow(tbEntries, 2);
        toggleWindow(tbCategories, 2);
        toggleWindow(tbAutoCategories, 2);
        toggleWindow(tbWatches, 2);
        toggleWindow(tbRefunds, 2);

        tbHistory.getTableView().addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
            if(tbErrors.getSelectedItem().getCheckID() == ACCOUNT_HISTORY_DOUBLE_CHARGE_PURCHASE || tbErrors.getSelectedItem().getCheckID() == ACCOUNT_HISTORY_DOUBLE_CHARGE_DDSO)
                highlightItems();
        });
    }
    private static void initEventListeners()
    {
        tgMode.selectedToggleProperty().addListener(new ChangeListener()
        {
            @Override
            public void changed(ObservableValue o, Object oldVal, Object newVal)
            {                                              
                if      (tgMode.getSelectedToggle() == rbIntegrityAccountHistory)           setMode(INTEGRITY_MODE_ACCOUNT_HISTORY);
                else if (tgMode.getSelectedToggle() == rbIntegrityAccountDatabase)          setMode(INTEGRITY_MODE_ACCOUNT_DATABASE);
            }
        });
        
        tgRange.selectedToggleProperty().addListener(new ChangeListener()
        {
            @Override
            public void changed(ObservableValue o, Object oldVal, Object newVal)
            {                                              
                if      (tgRange.getSelectedToggle() == rbIntegrityAccountHistoryAll)       setRange(INTEGRITY_RANGE_ALL);
                else if (tgRange.getSelectedToggle() == rbIntegrityAccountHistoryFrom)      setRange(INTEGRITY_RANGE_FROM);
            }
        });
    }
    private static void initRadioButtons()
    {
        rbIntegrityAccountHistory.setToggleGroup(tgMode);
        rbIntegrityAccountDatabase.setToggleGroup(tgMode);
        
        rbIntegrityAccountHistoryAll.setToggleGroup(tgRange);
        rbIntegrityAccountHistoryFrom.setToggleGroup(tgRange);
    }
    private static void initCheckAccountHistory()
    {
        for(Account a : Accounts.getAccounts())
            lstCheckAccountHistory.add(new CheckAccountHistory(a));
    }
    private static void initCheckAccountDatabase()
    {
        for(Account a : Accounts.getAccounts())
            lstCheckAccountDatabase.add(new CheckAccountDatabase(a));
    }
    private static void toggleWindow(Table tb, int iWindow)
    {
        tb.getTableView().addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
            if(me.getButton() == MouseButton.PRIMARY && me.getClickCount() == 2)
                if(tb == tbChecks && obWindows.numberOfWindows() == 1)      showErrors();
                else                                                        obWindows.toggleWindow(iWindow);
        });
    }
    
    //Mode/Range
    private static void initRange()
    {
        rbIntegrityAccountHistory.setSelected(true);
        rbIntegrityAccountHistoryAll.setSelected(true);
        
        if(DateRange.isRangeValid())    dpIntegrityAccountHistoryFrom.setValue(DateRange.getTo());
        else                            dpIntegrityAccountHistoryFrom.setValue(LocalDate.now());
    }
    private static void setRangeButtonsVisible(boolean bVisible)
    {
        rbIntegrityAccountHistoryAll.setVisible(bVisible);
        rbIntegrityAccountHistoryFrom.setVisible(bVisible);
        
        setRangeDatePickerVisible(false);
    }
    private static void setRangeDatePickerVisible(boolean bVisible)
    {
        dpIntegrityAccountHistoryFrom.setVisible(bVisible);
    }
    private static void setRange(int i)
    {
        iRange = i;
        setRangeDatePickerVisible(iRange == INTEGRITY_RANGE_FROM);
    }
    private static void setMode(int i)
    {
        iMode = i;
        rbIntegrityAccountHistoryAll.setSelected(true);
        setRangeButtonsVisible(iMode == INTEGRITY_MODE_ACCOUNT_HISTORY);
    }
    
    //Selection
    private static void clearAndReset()
    {
        tbChecks.setItem(new Blank());
        tbErrors.setItem(new Blank());
        tbHistory.setItem(new Blank());
        tbEntries.setItem(new Blank());
        tbCategories.setItem(new Blank());
        tbAutoCategories.setItem(new Blank());
        tbWatches.setItem(new Blank());
        tbRefunds.setItem(new Blank());
        
        obWindows.reset();
        obWindows.removeAllWindows();
    }
    
    //Show Tables
    private static boolean isHighlightedItem(Entry e, Entry eMatch)
    {
        if(tbErrors.getSelectedItem().getCheckID() == ACCOUNT_HISTORY_DOUBLE_CHARGE_DDSO)
            return (e.getName().equals(eMatch.getName()) && e.getDirection() == OUT);
        else
            return (e.getName().equals(eMatch.getName()) && e.getDirection() == OUT && e.getAmount() == eMatch.getAmount());
    }
    private static void findErrorsTotalValue()
    {
        int iTotal = 0;
        
        for(Error err : tbErrors.getAllItems())
            iTotal += err.getEntries().get(0).getAmount();

        tbErrors.addItem(new Blank());
        tbErrors.addItem(new Blank(BLANK_FIELD_DETAILS, "Total: £" + Formatter.convert(CURRENCY, DATABASE, TABLEVIEW, iTotal)));
    }
    private static void runChecksForAccountHistory()
    {
        for(Account a : Accounts.getAccounts())
            a.initInternalTransfersEntries();
        
        for(CheckAccountHistory cah : lstCheckAccountHistory)
        {
            tbChecks.addItem(cah.getHeader());
            tbChecks.addItems(cah.getChecks());
            tbChecks.addItem(new Blank());
        }

        for(CheckAccountHistory cah : lstCheckAccountHistory)
            switch(iRange)
            {
                case INTEGRITY_RANGE_ALL:   cah.runChecks(NOT_DEFINED);                                                     break;
                case INTEGRITY_RANGE_FROM:  cah.runChecks(Formatter.convert(dpIntegrityAccountHistoryFrom.getValue()));     break;
            }
        
        for(CheckAccountHistory cah : lstCheckAccountHistory)
            cah.runPostChecks();
    }
    private static void runChecksForAccountDatabase()
    {
        for(CheckAccountDatabase cad : lstCheckAccountDatabase)
        {
            tbChecks.addItems(cad.getChecks());
            tbChecks.addItem(new Blank());
        }

        for(CheckAccountDatabase cad : lstCheckAccountDatabase)
            cad.runChecks();
    }
    private static void showChecks()
    {
        tbChecks.setItem(new Blank());
        
        switch(iMode)
        {
            case INTEGRITY_MODE_ACCOUNT_HISTORY:        runChecksForAccountHistory();       break;
            case INTEGRITY_MODE_ACCOUNT_DATABASE:       runChecksForAccountDatabase();      break;
        }
    }
    private static void showErrors()
    {
        if(tbChecks.getSelectedItem().hasErrors())
        {
            tbErrors.clear();

            for(Error e : tbChecks.getSelectedItem().getErrors())
                tbErrors.addItem(e);
            
            if(tbErrors.numberOfItems() > 1 && (tbChecks.getSelectedItem().getID() == ACCOUNT_HISTORY_DOUBLE_CHARGE_PURCHASE || tbChecks.getSelectedItem().getID() == ACCOUNT_HISTORY_DOUBLE_CHARGE_DDSO))
                findErrorsTotalValue();
        }
        else
            tbErrors.setItem(new Blank(BLANK_FIELD_ERROR, "No Errors"));
        
        obWindows.addWindow(tbErrors, 1);
        obWindows.removeWindow(2);
    }
    private static Table findItemsTable()
    {
        switch(iMode)
        {
            case INTEGRITY_MODE_ACCOUNT_HISTORY:
            {
                return tbHistory;
            }
            case INTEGRITY_MODE_ACCOUNT_DATABASE:
            {
                switch(tbErrors.getSelectedItem().getCheckID())
                {
                    case ACCOUNT_DATABASE_ENTRIES:              return tbEntries;
                    case ACCOUNT_DATABASE_CATEGORIES:           return tbCategories;
                    case ACCOUNT_DATABASE_AUTO_CATEGORIES:      return tbAutoCategories;
                    case ACCOUNT_DATABASE_WATCHES:              return tbWatches;
                    case ACCOUNT_DATABASE_REFUNDS:              return tbRefunds;
                }
            }
        }
        
        return null;
    }
    private static void populateItemsTableViewForAccountHistory(Table tb)
    {
        Error err = tbErrors.getSelectedItem();
        Entry eError = err.getEntry();
        
        switch(err.getCheckID())
        {
            case ACCOUNT_HISTORY_BALANCE_INCONSISTENCY:
            case ACCOUNT_HISTORY_TRANSFERS:
            {
                tb.setItems(err.getEntries());
                
                break;
            }
            case ACCOUNT_HISTORY_DOUBLE_CHARGE_PURCHASE:
            {
                for(Entry e : Database.getEntries(Accounts.get(eError.getAccountID()), eError.getDate(), eError.getDate()))
                    tb.addItem(e);
                
                break;
            }
            case ACCOUNT_HISTORY_DOUBLE_CHARGE_DDSO:
            {
                for(Entry e : Database.getEntries(Accounts.get(eError.getAccountID()), Formatter.findQuarterPeriodStart(eError), Formatter.findQuarterPeriodEnd(eError)))
                    tb.addItem(e);
                
                break;
            }
        }
    }
    private static void populateItemsTableViewForAccountDatabase(Table tb)
    {
        Error err = tbErrors.getSelectedItem();
        
        switch(err.getCheckID())
        {
            case ACCOUNT_DATABASE_ENTRIES:            tb.setItems(err.getEntries());            break;
            case ACCOUNT_DATABASE_CATEGORIES:         tb.setItems(err.getCategories());         break;
            case ACCOUNT_DATABASE_AUTO_CATEGORIES:    tb.setItems(err.getAutoCategories());     break;
            case ACCOUNT_DATABASE_WATCHES:            tb.setItems(err.getWatches());            break;
            case ACCOUNT_DATABASE_REFUNDS:            tb.setItems(err.getRefunds());            break;
        }
    }
    private static void showItems()
    {
        Table tb = findItemsTable();
        
        tb.clear();
        
        switch(iMode)
        {
            case INTEGRITY_MODE_ACCOUNT_HISTORY:        populateItemsTableViewForAccountHistory(tb);        break;
            case INTEGRITY_MODE_ACCOUNT_DATABASE:       populateItemsTableViewForAccountDatabase(tb);       break;
        }

        obWindows.addWindow(tb, 2);
        
        if(tbErrors.getSelectedItem().getCheckID() == ACCOUNT_HISTORY_DOUBLE_CHARGE_PURCHASE || tbErrors.getSelectedItem().getCheckID() == ACCOUNT_HISTORY_DOUBLE_CHARGE_DDSO)
            highlightItems();
    }
    
    //External API -------------------------------------------------------------
    public static void init()
    {
        clearAndReset();
    }
    
    //UI Buttons
    public static void check()
    {
        clearAndReset();
        showChecks();
    }
    public static void clear()
    {
        clearAndReset();
    }
    
    //Context Menu
    public static void viewErrors()
    {
        showErrors();
    }
    public static void viewItems()
    {
        showItems();
    }
    public static void highlightItems()
    {
        Entry eMatch = tbErrors.getSelectedItem().getEntry();
        
        tbHistory.clearSelection();
        
        for(Entry e : tbHistory.getAllItems())
            if(isHighlightedItem(e, eMatch))
                tbHistory.select(e);
    }
    
    //Utility
    public static String checkBalanceInconsistency(Entry ePrevious, Entry e, boolean bShowPlusSign)
    {
        int iBalanceInconsistency = 0;
        
        switch(e.getDirection())
        {
            case IN:    iBalanceInconsistency = e.getBalance() - (ePrevious.getBalance() + e.getIn());     break;
            case OUT:   iBalanceInconsistency = e.getBalance() - (ePrevious.getBalance() - e.getOut());    break;
        }

        if(iBalanceInconsistency != 0)
        {
            String s = Formatter.convert(CURRENCY, DATABASE, TABLEVIEW, iBalanceInconsistency);
            
            if(iBalanceInconsistency == -1)     return "-0.01";
            else                                return bShowPlusSign ? "+" + s : s;
        }
        
        return "";
    }
    
    //Context Menu Miscellanous Data
    public static boolean isErrorsContextMenuReady()
    {
        return tbChecks.getSelectedItem().getID() != ACCOUNT_HISTORY_REFUNDS_OUTSTANDING;
    }
    public static String getErrorLabel()
    {
        return tbChecks.getSelectedItem().getErrorContextMenuLabel();
    }
}
