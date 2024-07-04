package Integrity;

import javafx.beans.property.SimpleStringProperty;

public interface IntegrityCheckTableInsertable {

    SimpleStringProperty padProperty();
    SimpleStringProperty accountNameProperty();
    SimpleStringProperty descriptionProperty();
    SimpleStringProperty statusProperty();
}
