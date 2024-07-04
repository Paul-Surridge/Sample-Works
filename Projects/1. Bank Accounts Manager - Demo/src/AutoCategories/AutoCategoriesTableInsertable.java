package AutoCategories;

import javafx.beans.property.SimpleStringProperty;

public interface AutoCategoriesTableInsertable {

    SimpleStringProperty entryNameProperty();
    SimpleStringProperty categoryNameProperty();
    SimpleStringProperty directionProperty();
    SimpleStringProperty frequencyProperty();
    SimpleStringProperty firstProperty();
    SimpleStringProperty lastProperty();
}
