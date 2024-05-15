package Statement;

import javafx.beans.property.SimpleStringProperty;

public interface StatementTableInsertable {

    SimpleStringProperty dateProperty();
    SimpleStringProperty nameProperty();
    SimpleStringProperty typeProperty();
    SimpleStringProperty categoryNameProperty();
    SimpleStringProperty inProperty();
    SimpleStringProperty outProperty();
    SimpleStringProperty balanceProperty();
}
