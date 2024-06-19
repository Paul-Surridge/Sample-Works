package Database;

import Shared.Constants;
import javafx.beans.property.SimpleStringProperty;

public class Error implements DatabaseErrorTableInsertable, Constants{
    
    private SimpleStringProperty sspNumber = new SimpleStringProperty();
    private SimpleStringProperty sspMethod = new SimpleStringProperty();
    private SimpleStringProperty sspSQL = new SimpleStringProperty();
    private SimpleStringProperty sspExceptionDetails = new SimpleStringProperty();
    
    public Error(int iNumber, String sMethod, String sSQL, String sExceptionDetails)
    {
        sspNumber.set(String.valueOf(iNumber));
        sspMethod.set(sMethod);
        sspSQL.set(sSQL);
        sspExceptionDetails.set(sExceptionDetails);
    }
    
    //External API -------------------------------------------------------------
    @Override
    public SimpleStringProperty numberProperty()
    {
        return sspNumber;
    }
    @Override
    public SimpleStringProperty methodProperty()
    {
        return sspMethod;
    }
    @Override
    public SimpleStringProperty SQLProperty()
    {
        return sspSQL;
    }
    @Override
    public SimpleStringProperty exceptionDetailsProperty()
    {
        return sspExceptionDetails;
    }
    
    //Dialog: Error Details
    public String getMethod()
    {
        return sspMethod.get();
    }
    public String getSQL()
    {
        return sspSQL.get();
    }
    public String getExceptionDetails()
    {
        return sspExceptionDetails.get();
    }
}
