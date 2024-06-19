package Shared;

import javafx.beans.property.SimpleStringProperty;

public interface ErrorTableInsertable {

    SimpleStringProperty numberProperty();
    SimpleStringProperty errorProperty();
    SimpleStringProperty detailsProperty();
}
