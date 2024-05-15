package Database.Administration;

import javafx.beans.property.SimpleStringProperty;

public interface DatabaseAdministrationRemindersTableInsertable {
    
    SimpleStringProperty primaryKeyProperty();
    SimpleStringProperty recordIDProperty();
    SimpleStringProperty nameProperty();
    SimpleStringProperty dateProperty();
    SimpleStringProperty dateAlertProperty();
}
