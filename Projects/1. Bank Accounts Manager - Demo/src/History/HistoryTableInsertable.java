package History;

import javafx.beans.property.SimpleStringProperty;

public interface HistoryTableInsertable {

    SimpleStringProperty dateProperty();
    SimpleStringProperty accountNameAbbreviatedProperty();
    SimpleStringProperty nameProperty();
    SimpleStringProperty typeProperty();
    SimpleStringProperty categoryNameProperty();
    SimpleStringProperty inProperty();
    SimpleStringProperty outProperty();
    SimpleStringProperty balanceProperty();
}
