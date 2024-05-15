package Accounts;

import Categories.CategoriesTableInsertable;
import History.Entry;
import Shared.Constants;
import Shared.Formatter;
import java.util.ArrayDeque;
import java.util.Deque;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.CheckBox;

public class AccountProperty implements CategoriesTableInsertable, Constants{
    
    private SimpleStringProperty sspName = new SimpleStringProperty();
    private SimpleStringProperty sspFrequency = new SimpleStringProperty();
    private SimpleStringProperty sspTotal = new SimpleStringProperty();
    
    private Deque<Entry> dqEntries = new ArrayDeque<>();
    
    private Account a;
    private int iDirection, iTotal, iProperty;
    
    public AccountProperty(Account a, String sName, int iDirection, int iProperty)
    {
        this.a = a;
        this.sspName.set(sName);
        this.iDirection = iDirection;
        this.iTotal = 0;
        this.iProperty = iProperty;
    }
    public AccountProperty(String sName, int iDirection, int iProperty)
    {
        this.a = null;
        this.sspName.set(sName);
        this.iDirection = iDirection;
        this.iTotal = 0;
        this.iProperty = iProperty;
    }
    
    //External API -------------------------------------------------------------
    
    //TableView
    @Override
    public SimpleStringProperty nameProperty()
    {
        return sspName;
    }
    @Override
    public SimpleStringProperty frequencyProperty()
    {
        return sspFrequency;
    }
    @Override
    public SimpleStringProperty totalProperty()
    {
        return sspTotal;
    }
    @Override
    public boolean getView()
    {
        return false;
    }
    @Override
    public void initViewCheckbox(CheckBox cb)
    {

    }
    
    //Property
    public String getName()
    {
        return sspName.get();
    }
    public int getDirection()
    {
        return iDirection;
    }
    public int getTotal()
    {
        return iTotal;
    }
    public int getFrequency()
    {
        return dqEntries.size();
    }
    public int getProperty()
    {
        return iProperty;
    }
    public String getAccountNameAbbreviated()
    {
        return a.getNameAbbreviated();
    }
    public Deque<Entry> getEntries()
    {
        return dqEntries;
    }
    public void add(Entry e)
    {
        iTotal += e.getAmount();
        dqEntries.add(e);
    }
    public void set()
    {
        sspFrequency.set(String.valueOf(getFrequency()));
        sspTotal.set(Formatter.convert(CURRENCY, DATABASE, TABLEVIEW, iTotal));
    }
    public void clear()
    {
        dqEntries.clear();
        iTotal = 0;
        
        set();
    }
}
