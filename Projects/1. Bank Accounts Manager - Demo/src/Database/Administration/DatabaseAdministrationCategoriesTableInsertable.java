package Database.Administration;

import javafx.beans.property.SimpleStringProperty;

public interface DatabaseAdministrationCategoriesTableInsertable {
    
    SimpleStringProperty primaryKeyProperty();
    SimpleStringProperty recordIDProperty();
    SimpleStringProperty nameProperty();
    SimpleStringProperty directionProperty();
}
