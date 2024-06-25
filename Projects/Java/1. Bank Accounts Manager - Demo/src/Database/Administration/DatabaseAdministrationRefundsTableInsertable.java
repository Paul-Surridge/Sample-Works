package Database.Administration;

import javafx.beans.property.SimpleStringProperty;

public interface DatabaseAdministrationRefundsTableInsertable {
    
    SimpleStringProperty primaryKeyProperty();
    SimpleStringProperty recordIDProperty();
    SimpleStringProperty dateProperty();
    SimpleStringProperty nameProperty();
    SimpleStringProperty keywordsProperty();
    SimpleStringProperty amountProperty();
    SimpleStringProperty receivedDateProperty();
    SimpleStringProperty receivedPrimaryKeyProperty();
}
