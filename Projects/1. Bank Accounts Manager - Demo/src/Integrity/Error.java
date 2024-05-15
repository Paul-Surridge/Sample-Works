package Integrity;

import AutoCategories.AutoCategory;
import Categories.Category;
import History.Entry;
import Refunds.Refund;
import Shared.Constants;
import Shared.ErrorTableInsertable;
import Watches.Watch;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;

public class Error implements ErrorTableInsertable, Constants{
    
    private SimpleStringProperty sspNumber = new SimpleStringProperty();
    private SimpleStringProperty sspError = new SimpleStringProperty();
    private SimpleStringProperty sspDetails = new SimpleStringProperty();
    
    private List<?> lstItems = new ArrayList<>();
    private int iCheckID;
    
    public Error(int iNumber, int iCheckID, String sError, String sDetails, List<?> lstItems)
    {
        sspNumber.set(String.valueOf(iNumber));
        sspError.set(sError);
        sspDetails.set(sDetails);
        
        this.iCheckID = iCheckID;
        this.lstItems = lstItems;
    }
    
    //External API -------------------------------------------------------------
    @Override
    public SimpleStringProperty numberProperty()
    {
        return sspNumber;
    }
    @Override
    public SimpleStringProperty errorProperty()
    {
        return sspError;
    }
    @Override
    public SimpleStringProperty detailsProperty()
    {
        return sspDetails;
    }
    
    public int getCheckID()
    {
        return iCheckID;
    }
    public Entry getEntry()
    {
        if(lstItems.isEmpty() || !(lstItems.get(0) instanceof Entry))
            return null;
        
        return (Entry)lstItems.get(0);
    }
    public List<Entry> getEntries()
    {
        List<Entry> lst = new ArrayList<>();
        
        if(lstItems.isEmpty() || !(lstItems.get(0) instanceof Entry))
            return lst;
        
        for(int i = 0 ; i<lstItems.size() ; i++)
            lst.add((Entry)lstItems.get(i));
        
        return lst;
    }
    public List<Category> getCategories()
    {
        List<Category> lst = new ArrayList<>();
        
        if(lstItems.isEmpty() || !(lstItems.get(0) instanceof Category))
            return lst;
        
        for(int i = 0 ; i<lstItems.size() ; i++)
            lst.add((Category)lstItems.get(i));
        
        return lst;
    }
    public List<AutoCategory> getAutoCategories()
    {
        List<AutoCategory> lst = new ArrayList<>();
        
        if(lstItems.isEmpty() || !(lstItems.get(0) instanceof AutoCategory))
            return lst;
        
        for(int i = 0 ; i<lstItems.size() ; i++)
            lst.add((AutoCategory)lstItems.get(i));
        
        return lst;
    }
    public List<Watch> getWatches()
    {
        List<Watch> lst = new ArrayList<>();
        
        if(lstItems.isEmpty() || !(lstItems.get(0) instanceof Watch))
            return lst;
        
        for(int i = 0 ; i<lstItems.size() ; i++)
            lst.add((Watch)lstItems.get(i));
        
        return lst;
    }
    public List<Refund> getRefunds()
    {
        List<Refund> lst = new ArrayList<>();
        
        if(lstItems.isEmpty() || !(lstItems.get(0) instanceof Refund))
            return lst;
        
        for(int i = 0 ; i<lstItems.size() ; i++)
            lst.add((Refund)lstItems.get(i));
        
        return lst;
    }
}
