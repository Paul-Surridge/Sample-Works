package Watches;

import javafx.beans.property.SimpleStringProperty;

public interface WatchesTableInsertable {

    SimpleStringProperty categoryNameProperty();
    SimpleStringProperty directionProperty();
    SimpleStringProperty lastDateProperty();
    SimpleStringProperty lastAmountProperty();
    SimpleStringProperty statusProperty();
}
