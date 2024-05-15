package Shared;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.TableView;

public class Table<T, V> implements Constants{
    
    private TableView<T> tv;
    private Class obClass;
    
    private V itemSelected;
    private List<V> lstSelected = new ArrayList<>();
    
    private infCMBuilder cmBuilder;
    private infCMBuilderWithList cmBuilderWithList;
    
    public Table(int iTableViewID, Class obClass)
    {
        this.tv = TableViewFactory.buildTable(iTableViewID);
        this.obClass = obClass;
        
        initEventListner();
    }
    public Table(int iTableViewID, Class obClass, infCMBuilder cmBuilder)
    {
        this(iTableViewID, obClass);
        this.cmBuilder = cmBuilder;
    }
    public Table(int iTableViewID, Class obClass, infCMBuilderWithList cmBuilderWithList)
    {
        this(iTableViewID, obClass);
        this.cmBuilderWithList = cmBuilderWithList;
    }
    
    //Internal -----------------------------------------------------------------
    private void initEventListner()
    {
        tv.getSelectionModel().getSelectedIndices().addListener(new ListChangeListener()
        {
            @Override
            public void onChanged(ListChangeListener.Change cl)
            {
                refreshSelected();
                rebuildContextMenu();
            }
        });
    }
    private void refreshSelected()
    {
        List lst = new ArrayList<>();
        
        for(T item : tv.getSelectionModel().getSelectedItems())
            if(item.getClass().getSimpleName().equals(obClass.getSimpleName()))
                lst.add(item);

        lstSelected = lst;
        
        if(lstSelected.isEmpty())   itemSelected = null;
        else                        itemSelected = lstSelected.get(0);
    }

    //External API -------------------------------------------------------------
    
    //Context Menu Interfaces
    public interface infCMBuilder
    {
        ContextMenu rebuildContextMenu();
    }
    public interface infCMBuilderWithList
    {
        ContextMenu rebuildContextMenu(List lst);
    }
    
    //TableView
    public TableView getTableView()
    {
        return tv;
    }
    public boolean hasFocus()
    {
        return tv == null ? false : tv.isFocused();
    }
    
    //Select Items
    public List<V> getAllItems()
    {
        List lst = new ArrayList<>();
        
        for(T item : tv.getItems())
            if(item.getClass().getSimpleName().equals(obClass.getSimpleName()))
                lst.add(item);

        return lst;
    }
    public List<V> getSelectedItems()
    {
        return lstSelected;
    }
    public V getSelectedItem()
    {
        return itemSelected;
    }
    public boolean hasSelected()
    {
        return !lstSelected.isEmpty();
    }
    public boolean isSelectedItemBlank()
    {
        ObservableList ol = tv.getSelectionModel().getSelectedItems();
        
        return (ol.size() == 1 && ol.get(0) instanceof Blank);
    }
    public void select(T t)
    {
        tv.getSelectionModel().select(t);
    }
    public boolean areNoItemsSelected()
    {
        return lstSelected.isEmpty();
    }
    public boolean areAllItemsSelected()
    {
        return lstSelected.size() == tv.getItems().size();
    }
    public void clearSelection()
    {
        tv.getSelectionModel().clearSelection();
    }
    
    //Adding/Setting Items
    public void addItem(T t)
    {
        tv.getItems().add(t);
    }
    public void addItems(List lst)
    {
        tv.getItems().addAll(lst);
    }
    public void setItem(T t)
    {
        clear();
        tv.getItems().add(t);
    }
    public void setItems(Collection col)
    {
        clear();
        tv.getItems().addAll(col);
    }
    public void setItems(List lst)
    {
        clear();
        tv.getItems().addAll(lst);
    }
    
    //Remove Items
    public void clear()
    {
        tv.getItems().clear();
    }
    public void clearAndRemoveSelection()
    {
        tv.getItems().removeAll(lstSelected);
    }
    
    //State
    public boolean isCleared()
    {
        return tv.getItems().isEmpty();
    }
    public boolean isEmpty()
    {
        return tv.getItems().isEmpty() || (tv.getItems().size() == 1 && tv.getItems().get(0) instanceof Blank);
    }
    public int numberOfItems()
    {
        return tv.getItems().size();
    }
    public void scrollToStart()
    {
        tv.scrollTo(0);
    }
    public void scrollToEnd()
    {
        tv.scrollTo(tv.getItems().size()-1);
    }
    public void requestFocus()
    {
        tv.requestFocus();
    }
    
    //Context Menu
    public void rebuildContextMenu()
    {
        if(lstSelected == null)                 tv.setContextMenu(null);
        else if (cmBuilder != null)             tv.setContextMenu(cmBuilder.rebuildContextMenu());
        else if (cmBuilderWithList != null)     tv.setContextMenu(cmBuilderWithList.rebuildContextMenu(lstSelected));
        else                                    tv.setContextMenu(null);
    }
}
