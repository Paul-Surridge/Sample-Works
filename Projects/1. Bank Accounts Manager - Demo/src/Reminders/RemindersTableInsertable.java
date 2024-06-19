package Reminders;

import javafx.beans.property.SimpleStringProperty;

public interface RemindersTableInsertable {

    SimpleStringProperty padProperty();
    SimpleStringProperty statusProperty();
    SimpleStringProperty dateAlertProperty();
    SimpleStringProperty nameProperty();
    SimpleStringProperty detailsProperty();
    SimpleStringProperty dateProperty();
}
