package History;

import Accounts.Account;
import Accounts.Accounts;
import Charts.Charts;
import Shared.Blank;
import Shared.Constants;
import Shared.ContextMenuFactory;
import Shared.Debug;
import Shared.Popup;
import Shared.Table;
import Shared.Windows;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;

public class History implements Constants{
    
    private static Table<HistoryTableInsertable, Entry> tbHistory;
    private static List<Entry> lstEntriesIn, lstEntriesOut, lstEntriesCombined, lstEntriesSearch;
    private static ImageView ivHistorySearchClose;
    private static Search obSearch;
    
    public History(AnchorPane apHistory, AnchorPane apHistorySearchListView, TextField tfHistorySearch, Rectangle recHistorySearch, ImageView ivHistorySearchClose)
    {
        this.ivHistorySearchClose = ivHistorySearchClose;
        
        lstEntriesIn = new ArrayList<>();
        lstEntriesOut = new ArrayList<>();
        lstEntriesCombined = new ArrayList<>();
        lstEntriesSearch = new ArrayList<>();
        
        obSearch = new Search(apHistorySearchListView, tfHistorySearch, recHistorySearch);
        
        initTable(apHistory);
        initEventHandlersForMouse();
        initEventHandlersForKeyboard();
    }
    
    //Internal -----------------------------------------------------------------
    private static void initTable(AnchorPane ap)
    {
        tbHistory = new Table(TABLEVIEW_ID_HISTORY_WITH_HEADER_OFFSET, Entry.class, ContextMenuFactory::buildContextMenuForHistory);
        
        ap.getChildren().add(tbHistory.getTableView());
    }
    private static void initEventHandlersForMouse()
    {
        tbHistory.getTableView().addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
            if(me.getButton() == MouseButton.PRIMARY && me.getClickCount() == 2)
                Windows.toggleWindow(WINDOW_HISTORY);
            
            Popup.setPosition(me);
        });

        ivHistorySearchClose.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
            Popup.hide();
            History.searchClear();
        });
    }
    private static void initEventHandlersForKeyboard()
    {
        tbHistory.getTableView().addEventHandler(KeyEvent.KEY_PRESSED, (KeyEvent ke) -> {
            if(ke.getCode() == KeyCode.ENTER)
                sendToCharts(tbHistory.getSelectedItems());
        });
    }
    private static boolean entriesToBeProcessed(List<Deque<Entry>> lst)
    {
        for(Deque<Entry> dq : lst)
            if(!dq.isEmpty())
                return true;
        
        return false;
    }
    private static void refreshShowDate(List<Entry> lst)
    {
        if(!lst.isEmpty())
            lst.get(0).setShowDateTextForTableView(true);
        
        for(int i = 1 ; i<lst.size() ; i++)
        {
            Entry e = lst.get(i);
            if(e.getDate() == lst.get(i-1).getDate())   e.setShowDateTextForTableView(false);
            else                                        e.setShowDateTextForTableView(true);
        }
    }
    
    //Charts
    private static List<Entry> removeDuplicateEntries(List<Entry> lst)
    {
        Map<String, Entry> mp = new TreeMap<>();
        
        for(Entry e : lst)
            if(!mp.containsKey(e.getName()))
                mp.put(e.getName(), e);
        
        return new ArrayList<>(mp.values());
    }
    
    //Search
    private static void showSearchResults()
    {
        refreshShowDate(lstEntriesSearch);
        
        if(lstEntriesSearch.isEmpty())  tbHistory.setItem(new Blank(BLANK_FIELD_NAME, "No Entries"));
        else                            tbHistory.setItems(lstEntriesSearch);
        
        tbHistory.requestFocus();
        ivHistorySearchClose.setVisible(true);
    }

    //External API -------------------------------------------------------------
    public static List<Entry> getEntriesInTableView(Entry e)
    {
        List<Entry> lst;
        List<Entry> lstBuild = new ArrayList<>();
        
        if(e.getDirection() == IN)  lst = lstEntriesIn;
        else                        lst = lstEntriesOut;
        
        for(Entry eBuild : lst)
            if(eBuild.getName().equals(e.getName()))
                lstBuild.add(eBuild);
        
        return lstBuild;
    }
    public static boolean isEmpty()
    {
        return tbHistory.isEmpty();
    }
    public static void refresh()
    {
        List<Deque<Entry>> lst = new ArrayList<>();
        
        Entry eEarliest;
        int iEarliest;
        
        lstEntriesIn.clear();
        lstEntriesOut.clear();
        lstEntriesCombined.clear();
        
        for(Account a : Accounts.getAccountsViewed())
            lst.add(a.getEntriesViewed(BOTH));
        
        while(entriesToBeProcessed(lst))
        {
            iEarliest = -1;
            
            for(int i = 0 ; i<lst.size() && iEarliest == -1 ; i++)
                if(!lst.get(i).isEmpty())
                    iEarliest = i;
            
            for(int i = iEarliest+1 ; i<lst.size() ; i++)
                if(!lst.get(i).isEmpty())
                    if(lst.get(i).peekFirst().getDate() < lst.get(iEarliest).peekFirst().getDate())
                        iEarliest = i;
            
            eEarliest = lst.get(iEarliest).removeFirst();
            
            switch(eEarliest.getDirection())
            {
                case IN:    lstEntriesIn.add(eEarliest);    break;
                case OUT:   lstEntriesOut.add(eEarliest);   break;
            }
            
            lstEntriesCombined.add(eEarliest);
        }
        
        refreshShowDate(lstEntriesCombined);
        
        if(lstEntriesCombined.isEmpty())    tbHistory.setItem(new Blank(BLANK_FIELD_NAME, "No Entries"));
        else                                tbHistory.setItems(lstEntriesCombined);
        
        ivHistorySearchClose.setVisible(false);
        
        Debug.print("History Refreshed");
    }
    public static void clear()
    {
        lstEntriesIn.clear();
        lstEntriesOut.clear();
        lstEntriesCombined.clear();
        
        tbHistory.setItem(new Blank(BLANK_FIELD_NAME, "No Entries"));
    }
    public static void rebuildContextMenu()
    {
        tbHistory.rebuildContextMenu();
    }
    public static void rebuildEntries(List<Entry> lst)
    {
        for(Entry e : lst)
            e.initChartBarsLines();
    }
    
    //Charts
    public static boolean isSendToChartsValid(List<Entry> lst)
    {
        Entry eSample = lst.get(0);
        
        if(lst.isEmpty())
            return false;
        
        for(Entry e : lst)
            if(e.isUndefined())
                return false;
        
        for(Entry e : lst)
            if(e.getAccountID() != eSample.getAccountID())
                return false;
        
        for(Entry e : lst)
            if(e.getDirection()!= eSample.getDirection())
                return false;
        
        for(Entry e : lst)
            if(e.isUserDefined() != eSample.isUserDefined())
                return false;
        
        return true;
    }
    public static void sendToCharts(List<Entry> lst)
    {
        List<Entry> lstFiltered = removeDuplicateEntries(lst);
        
        if(!isSendToChartsValid(lstFiltered))
            return;
        
        for(Entry e : lstFiltered)
            e.initChartBarsLines();
        
        Charts.viewEntries(lstFiltered);
    }
    
    //Search
    public static void showDialogSearch()
    {
        if(tbHistory.hasFocus() && tbHistory.hasSelected())
            Popup.show(POPUP_HISTORY_SEARCH);
        else
            if(Windows.isLarge(WINDOW_HISTORY))     Popup.show(1100, 200, POPUP_HISTORY_SEARCH);
            else                                    Popup.show(1500, 200, POPUP_HISTORY_SEARCH);
        
        History.search();
    }
    public static void showDialogSearch(MouseEvent me)
    {
        Popup.show(me, POPUP_HISTORY_SEARCH);
        
        History.search();
    }
    public static void search()
    {
        obSearch.init(lstEntriesCombined);
    }
    public static void search(String sSearch)
    {
        obSearch.init(sSearch);
    }
    public static void searchClear()
    {
        refreshShowDate(lstEntriesCombined);
        
        tbHistory.setItems(lstEntriesCombined);
        
        obSearch.clear();
        
        ivHistorySearchClose.setVisible(false);
    }
    public static void showSearchEntries(String sEntryName)
    {
        lstEntriesSearch.clear();
        
        for(Entry e : lstEntriesCombined)
            if(e.getName().equals(sEntryName))
                lstEntriesSearch.add(e);
        
        showSearchResults();
    }
    public static void searchAccountProperty(int iAccountProperty)
    {
        lstEntriesSearch.clear();
        
        for(Entry e : lstEntriesCombined)
            if(e.getAccountProperty() == iAccountProperty)
                lstEntriesSearch.add(e);

        showSearchResults();
    }
    public static void searchAccountPropertySpend()
    {
        lstEntriesSearch.clear();
        
        for(Entry e : lstEntriesCombined)
            if(e.isSpend())
                lstEntriesSearch.add(e);

        showSearchResults();
    }
    public static void searchDirection(int iDirection)
    {
        lstEntriesSearch.clear();
        
        switch(iDirection)
        {
            case IN:    lstEntriesSearch.addAll(lstEntriesIn);      break;
            case OUT:   lstEntriesSearch.addAll(lstEntriesOut);     break;
        }

        showSearchResults();
    }
    public static boolean isSearchActive()
    {
        return ivHistorySearchClose.isVisible();
    }
}
