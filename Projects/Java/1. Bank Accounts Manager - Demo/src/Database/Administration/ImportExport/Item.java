package Database.Administration.ImportExport;

import javafx.beans.property.SimpleStringProperty;

public class Item implements DatabaseAdministrationImportTableInsertable, DatabaseAdministrationExportTableInsertable{
    
    private SimpleStringProperty sspItem = new SimpleStringProperty();
    private SimpleStringProperty sspDetails = new SimpleStringProperty();
    
    public Item(String sItem, String sDetails)
    {
        sspItem.set(sItem);
        sspDetails.set(sDetails);
    }
    public Item(String sItem, long lDetails)
    {
        sspItem.set(sItem);
        sspDetails.set(String.valueOf(lDetails));
    }
    
    //External API -------------------------------------------------------------
    @Override
    public SimpleStringProperty padProperty()
    {
        return new SimpleStringProperty();
    }
    @Override
    public SimpleStringProperty itemProperty()
    {
        return sspItem;
    }
    @Override
    public SimpleStringProperty detailsProperty()
    {
        return sspDetails;
    }
}