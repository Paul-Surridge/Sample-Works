package Database.Administration;

import javafx.beans.property.SimpleStringProperty;

public interface DatabaseAdministrationWatchesTableInsertable {
    
    SimpleStringProperty primaryKeyProperty();
    SimpleStringProperty recordIDProperty();
    SimpleStringProperty categoryNameProperty();
    SimpleStringProperty directionProperty();
    SimpleStringProperty lastDateProperty();
    SimpleStringProperty lastAmountProperty();
}
