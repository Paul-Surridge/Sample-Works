package History;

import Accounts.Accounts;
import History.Entry;
import Shared.Constants;
import Shared.Formatter;

public class EntryInternalTransfer implements Constants{
    
    private Entry eSource, eDestination;
    private int iDestinationAccountID;

    public EntryInternalTransfer(Entry eSource, int iDestinationAccountID)
    {
        this.eSource = eSource;
        this.eDestination = null;
        this.iDestinationAccountID = iDestinationAccountID;
    }

    //External API ---------------------------------------------------------
    
    //Source
    public Entry getSourceEntry()
    {
        return eSource;
    }
    public String getSourceEntryName()
    {
        return eSource.getName();
    }
    public int getDate()
    {
        return eSource.getDate();
    }
    public String getDateForTableView()
    {
        return Formatter.convert(DATE, DATABASE, TABLEVIEW, eSource.getDate());
    }
    public int getDirection()
    {
        return eSource.getDirection();
    }
    public int getAmount()
    {
        return eSource.getAmount();
    }
    public String getAmountText()
    {
        return Formatter.convert(CURRENCY, DATABASE, TABLEVIEW, getAmount());
    }
    
    //Destination
    public int getDestinationAccountID()
    {
        return iDestinationAccountID;
    }
    public String getDestinationAccountNameAbbreviated()
    {
        return Accounts.get(iDestinationAccountID).getNameAbbreviated();
    }
    
    //Mapped
    public boolean isMapped()
    {
        return eDestination != null;
    }
    public void setMapped(Entry eDestination)
    {
        this.eDestination = eDestination;
    }
    public Entry getDestinationEntry()
    {
        return eDestination;
    }
}
