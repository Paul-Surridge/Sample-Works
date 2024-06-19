package Refunds;

import javafx.beans.property.SimpleStringProperty;

public interface RefundPotentialsTableInsertable {

    SimpleStringProperty dateProperty();
    SimpleStringProperty accountNameAbbreviatedProperty();
    SimpleStringProperty nameProperty();
    SimpleStringProperty typeProperty();
    SimpleStringProperty categoryNameProperty();
    SimpleStringProperty inProperty();
    SimpleStringProperty outProperty();
    SimpleStringProperty balanceProperty();
}
