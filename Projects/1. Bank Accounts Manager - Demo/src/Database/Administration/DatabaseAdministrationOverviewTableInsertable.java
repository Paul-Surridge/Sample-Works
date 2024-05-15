package Database.Administration;

import javafx.beans.property.SimpleStringProperty;

public interface DatabaseAdministrationOverviewTableInsertable {
    
    SimpleStringProperty padProperty();
    SimpleStringProperty accountProperty();
    SimpleStringProperty tableProperty();
    SimpleStringProperty numberOfRecordsProperty();
}
