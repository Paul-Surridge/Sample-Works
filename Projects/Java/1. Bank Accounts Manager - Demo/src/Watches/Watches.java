package Watches;

import Accounts.Account;
import Accounts.Accounts;
import Administration.Windows;
import Categories.Category;
import Database.Database;
import History.Entry;
import History.HistoryTableInsertable;
import Shared.Blank;
import Shared.Constants;
import Shared.ContextMenuFactory;
import Shared.Popup;
import Shared.Table;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class Watches implements Constants{
    
    private static RadioButton rbWatchesCurrent, rbWatchesSaving, rbWatchesISA, rbWatchesIncoming, rbWatchesOutgoing;
    private static ToggleGroup tgWatchesAccounts = new ToggleGroup();
    private static ToggleGroup tgWatchesDirection = new ToggleGroup();
    
    private static Table<WatchesTableInsertable, Watch> tbWatches;
    private static Table<HistoryTableInsertable, Entry> tbHistory;
    
    private static Windows obWindows;
    private static Account aSelected;
    private static int iSelectedDirection;
    
    public Watches(SplitPane spWatchesTables, AnchorPane apWatchesTable,
                    RadioButton rbWatchesCurrent, RadioButton rbWatchesSaving, RadioButton rbWatchesISA,
                    RadioButton rbWatchesIncoming, RadioButton rbWatchesOutgoing)
    {
        this.rbWatchesCurrent               = rbWatchesCurrent;
        this.rbWatchesSaving                = rbWatchesSaving;
        this.rbWatchesISA                   = rbWatchesISA;
        
        this.rbWatchesIncoming              = rbWatchesIncoming;
        this.rbWatchesOutgoing              = rbWatchesOutgoing;
        
        obWindows = new Windows(spWatchesTables);
        
        initTables(apWatchesTable);
        initEventHandlers();
        initEventListeners();
        initRadioButtons();
        
        clearAndReset();
    }
    
    //Internal -----------------------------------------------------------------
    private static void initTables(AnchorPane ap)
    {
        tbWatches = new Table(TABLEVIEW_ID_WATCHES, Watch.class, ContextMenuFactory::buildContextMenuForWatches);
        tbHistory = new Table(TABLEVIEW_ID_HISTORY_WITHIN_ADMINISTRATION, Entry.class);
        
        ap.getChildren().add(tbWatches.getTableView());
    }
    private static void initEventHandlers()
    {
        toggleWindow(tbWatches, 0);
        toggleWindow(tbHistory, 1);
    }
    private static void initEventListeners()
    {
        tgWatchesAccounts.selectedToggleProperty().addListener(new ChangeListener()
        {
            @Override
            public void changed(ObservableValue o, Object oldVal, Object newVal)
            {                                              
                if      (tgWatchesAccounts.getSelectedToggle() == rbWatchesCurrent)             aSelected = Accounts.get(BARCLAYS_CURRENT);
                else if (tgWatchesAccounts.getSelectedToggle() == rbWatchesSaving)              aSelected = Accounts.get(BARCLAYS_SAVING);
                else if (tgWatchesAccounts.getSelectedToggle() == rbWatchesISA)                 aSelected = Accounts.get(BARCLAYS_ISA);
                
                if(aSelected != null && iSelectedDirection != NOT_DEFINED)
                    showWatches();
            }
        });
        
        tgWatchesDirection.selectedToggleProperty().addListener(new ChangeListener()
        {
            @Override
            public void changed(ObservableValue o, Object oldVal, Object newVal)
            {                                              
                if      (tgWatchesDirection.getSelectedToggle() == rbWatchesIncoming)           iSelectedDirection = IN;
                else if (tgWatchesDirection.getSelectedToggle() == rbWatchesOutgoing)           iSelectedDirection = OUT;
                
                if(aSelected != null && iSelectedDirection != NOT_DEFINED)
                    showWatches();
            }
        });
    }
    private static void initRadioButtons()
    {
        rbWatchesCurrent.setToggleGroup(tgWatchesAccounts);
        rbWatchesSaving.setToggleGroup(tgWatchesAccounts);
        rbWatchesISA.setToggleGroup(tgWatchesAccounts);
        
        rbWatchesIncoming.setToggleGroup(tgWatchesDirection);
        rbWatchesOutgoing.setToggleGroup(tgWatchesDirection);
    }
    private static void toggleWindow(Table tb, int iWindow)
    {
        tb.getTableView().addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
            if(me.getButton() == MouseButton.PRIMARY && me.getClickCount() == 2)
                obWindows.toggleWindow(iWindow);
        });
    }
    
    //Buttons/UI
    private static void clearAndReset()
    {
        clearRadioButtons();
        
        aSelected = null;
        iSelectedDirection = NOT_DEFINED;
        
        obWindows.reset();
        obWindows.removeAllWindows();
        
        showEmpty();
    }
    private static void clearRadioButtons()
    {
        for(Toggle t : tgWatchesAccounts.getToggles())
            t.setSelected(false);
        
        for(Toggle t : tgWatchesDirection.getToggles())
            t.setSelected(false);
    }
    
    //Check/Update
    private static void check(Account a, Watch w)
    {
        List<Entry> lst;
        
        if(Database.getLastWatchedOccurence(a, w) == null)
        {
            w.setStatus("No entries in account with this category '" + w.getCategoryName() + "'");
            return;
        }
        
        lst = Database.getEntriesWatched(a, w);
        
        w.clearStatus();
        
        for(Entry e : lst)
        {
            if(e.getDate() > w.getLastDate() && e.getAmount() != w.getLastAmount())
            {
                if(e.getAmount() == w.getLastAmount())      w.incEqualCounter();
                else if(e.getAmount() > w.getLastAmount())  w.incIncreaseCounter();
                else                                        w.incDecreaseCounter();
            }
        
            w.addEntry(e);
        }
        
        w.refreshStatus();
    }
    private static void checkAllAccounts()
    {
        for(Account a : Accounts.getAccounts())
        {
            for(Watch w : a.getWatches(IN))
                check(a, w);
            for(Watch w : a.getWatches(OUT))
                check(a, w);
        }
    }
    private static void update(Account a, Watch w)
    {
        a.update(w);
        
        if(w.getLastDate() == NOT_DEFINED)      w.setStatus("No entries in account with the category '" + w.getCategoryName() + "'");
        else                                    check(a, w);
    }
    
    //Show Tables
    private static void showEmpty()
    {
        tbWatches.setItem(new Blank());
        tbHistory.setItem(new Blank());
    }
    private static void showWatches()
    {
        if(aSelected.numberOfWatches(iSelectedDirection) == 0)      tbWatches.setItem(new Blank(BLANK_FIELD_CATEGORY_NAME, "No " + (iSelectedDirection == IN ? "Incoming" : "Outgoing") + " Watches"));
        else                                                        tbWatches.setItems(aSelected.getWatches(iSelectedDirection));
        
        obWindows.removeAllWindows();
    }
    private static void showHistory(Watch w)
    {
        tbHistory.setItems(w.getEntries());
        
        if(tbHistory.isEmpty())     tbHistory.setItem(new Blank(BLANK_FIELD_NAME, "No Entries"));
        else                        tbHistory.addItem(new Blank());
        
        tbHistory.scrollToEnd();
        
        obWindows.addWindow(tbHistory, 1);
    }
    
    //External API -------------------------------------------------------------
    public static void init()
    {
        clearAndReset();
        checkAllAccounts();
    }
    
    //UI Buttons
    public static void viewHistory()
    {
        showHistory(tbWatches.getSelectedItem());
    }
    public static void update()
    {
        for(Watch w : tbWatches.getSelectedItems())
            update(aSelected, w);
    }
    public static void delete()
    {
        Popup.hide();
        
        if(tbWatches.areAllItemsSelected())
            aSelected.deleteAllWatches();
        else
            for(Watch w : tbWatches.getSelectedItems())
                aSelected.deleteWatch(w);

        showWatches();
    }
    
    //Watches Tableview - Context Menu
    public static void add(Category c)
    {
        Watch w = new Watch(c);
        
        aSelected.addWatch(w);
        
        update(aSelected, w);
        check(aSelected, w);
        
        showWatches();
    }
    
    //Context Menu Miscellanous Data
    public static List<Category> getUnwatchedCategories()
    {
        List<Category> lst = new ArrayList<>();
        
        for(Category c : aSelected.getCategories(iSelectedDirection))
            if(!aSelected.isWatched(c))
                lst.add(c);
        
        return lst;
    }
    public static int getSelectedDirection()
    {
        return iSelectedDirection;
    }
    public static boolean isContextMenuReady()
    {
        return aSelected != null && iSelectedDirection != NOT_DEFINED;
    }
}
