package Refunds;

import javafx.beans.property.SimpleStringProperty;

public interface RefundsTableInsertable {

    SimpleStringProperty dateProperty();
    SimpleStringProperty nameProperty();
    SimpleStringProperty keywordsProperty();
    SimpleStringProperty amountProperty();
    SimpleStringProperty statusProperty();
}
