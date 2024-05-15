package Database.Administration;

import javafx.beans.property.SimpleStringProperty;

public interface DatabaseAdministrationEntriesTableInsertable {
    
    SimpleStringProperty primaryKeyProperty();
    SimpleStringProperty recordIDProperty();
    SimpleStringProperty dateProperty();
    SimpleStringProperty nameProperty();
    SimpleStringProperty typeProperty();
    SimpleStringProperty categoryNameProperty();
    SimpleStringProperty inProperty();
    SimpleStringProperty outProperty();
    SimpleStringProperty balanceProperty();
}
