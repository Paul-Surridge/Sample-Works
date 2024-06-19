package Shared;

import javafx.beans.property.SimpleStringProperty;

public class Error implements ErrorTableInsertable{
    
    private SimpleStringProperty sspNumber = new SimpleStringProperty();
    private SimpleStringProperty sspError = new SimpleStringProperty();
    private SimpleStringProperty sspDetails = new SimpleStringProperty();
    
    public Error(int iNumber, String sError, String sDetails)
    {
        sspNumber.set(String.valueOf(iNumber));
        sspError.set(sError);
        sspDetails.set(sDetails);
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
}
