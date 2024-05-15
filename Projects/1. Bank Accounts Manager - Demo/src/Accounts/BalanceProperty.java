package Accounts;

import Shared.Constants;
import Shared.Formatter;
import javafx.beans.property.SimpleStringProperty;

public class BalanceProperty implements Constants{
    
    private String sName;
    private int iValue;
    private SimpleStringProperty sspValue = new SimpleStringProperty();
    
    public BalanceProperty(String sName)
    {
        this.sName = sName;
        this.iValue = 0;
        this.sspValue.set("-");
    }
    
    //Internal -----------------------------------------------------------------
    private String format(int iTotal)
    {
        return Formatter.convert(CURRENCY, DATABASE, TABLEVIEW, iTotal);
    }
    
    //External API -------------------------------------------------------------
    public SimpleStringProperty getProperty()
    {
        return sspValue;
    }
    public String getName()
    {
        return sName;
    }
    public int getValue()
    {
        return iValue;
    }
    public void add(int i)
    {
        iValue += i;
    }
    public void set(int i)
    {
        iValue = i;
    }
    public void set()
    {
        sspValue.set(format(iValue));
    }
    public void hide()
    {
        sspValue.set("-");
    }
    public void show()
    {
        sspValue.set(format(iValue));
    }
    public void clear()
    {
        iValue = 0;
        hide();
    }
}
