package Database.Administration;

import javafx.beans.property.SimpleStringProperty;

public interface DatabaseAdministrationAutoCategoriesTableInsertable {
    
    SimpleStringProperty primaryKeyProperty();
    SimpleStringProperty recordIDProperty();
    SimpleStringProperty entryNameProperty();
    SimpleStringProperty categoryNameProperty();
    SimpleStringProperty directionProperty();
}
