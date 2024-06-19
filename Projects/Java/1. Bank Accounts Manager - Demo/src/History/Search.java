package History;

import Shared.Constants;
import Shared.Popup;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;

public class Search implements Constants
{
    private Map<String, Entry> mpFilter = new TreeMap<>();
    private List<Character> lstPath = new ArrayList<>();
    private List<Entry> lstEntries;
    private ListView<String> lvEntries = new ListView();
    private AnchorPane apHistorySearchListView;
    private TextField tfHistorySearch;
    private Rectangle recHistorySearch;

    public Search(AnchorPane apHistorySearchListView, TextField tfHistorySearch, Rectangle recHistorySearch)
    {
        this.apHistorySearchListView    = apHistorySearchListView;
        this.tfHistorySearch            = tfHistorySearch;
        this.recHistorySearch           = recHistorySearch;
        
        initEventListeners();
        initMouseEventHandlers();
        initKeyEventHandlers();
        setLayoutAnchors(lvEntries);
    }

    //Internal -----------------------------------------------------------------
    private void initEventListeners()
    {
        tfHistorySearch.textProperty().addListener(new ChangeListener()
        {
            @Override
            public void changed(ObservableValue o, Object oldVal, Object newVal)
            {
                if(tfHistorySearch.getText().isEmpty()) clear();
                else                                    findEntries(tfHistorySearch.getText());
            }
        });
    }
    private void initMouseEventHandlers()
    {
        lvEntries.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
            showEntries(lvEntries.getSelectionModel().getSelectedItem());
        });
    }
    private void initKeyEventHandlers()
    {
        tfHistorySearch.addEventHandler(KeyEvent.KEY_PRESSED, (KeyEvent ke) -> {
            switch(ke.getCode())
            {
                case UP:
                {
                    lvEntries.requestFocus();
                    lvEntries.getSelectionModel().selectLast();
                    break;
                }
                case DOWN:
                {
                    lvEntries.requestFocus();
                    lvEntries.getSelectionModel().selectFirst();
                    break;
                }
            }
        });
        lvEntries.addEventHandler(KeyEvent.KEY_PRESSED, (KeyEvent ke) -> {
            if(ke.getCode() == KeyCode.ENTER)
                showEntries(lvEntries.getSelectionModel().getSelectedItem());
        });
    }
    private void setLayoutAnchors(Node n)
    {
        AnchorPane.setTopAnchor(n, 0.0);
        AnchorPane.setBottomAnchor(n, 0.0);
        AnchorPane.setLeftAnchor(n, 0.0);
        AnchorPane.setRightAnchor(n, 0.0);
    }
    private void findEntries(String sFragment)
    {
        Map<String, Entry> mp = new HashMap<>();
        
        for(Entry e : lstEntries)
            if(e.getName().toLowerCase().contains(sFragment.toLowerCase()))
                if(!mp.containsKey(e.getName()))
                    mp.put(e.getName(), e);

        recHistorySearch.setHeight(TEXT_LINE_HEIGHT*mp.size() + TEXT_LINE_HEIGHT);
        apHistorySearchListView.setPrefHeight(TEXT_LINE_HEIGHT*mp.size());
        
        lvEntries.getItems().clear();
        
        for(String s : mp.keySet())
            lvEntries.getItems().add(s);
        
        if(apHistorySearchListView.getChildren().isEmpty())
            apHistorySearchListView.getChildren().add(lvEntries);
    }
    private void showEntries(String sEntryName)
    {
        Popup.hide();
        History.showSearchEntries(sEntryName);
        clear();
    }
    
    //External -----------------------------------------------------------------
    public void init(List<Entry> lst)
    {
        clear();
        
        lstEntries = lst;
        tfHistorySearch.requestFocus();
    }
    public void init(String sSearch)
    {
        clear();
        
        showEntries(sSearch);
    }
    public void clear()
    {
        apHistorySearchListView.getChildren().clear();
        recHistorySearch.setHeight(TEXT_LINE_HEIGHT);
        apHistorySearchListView.setPrefHeight(0);
        
        tfHistorySearch.clear();
        lvEntries.getItems().clear();
    }
}
    

