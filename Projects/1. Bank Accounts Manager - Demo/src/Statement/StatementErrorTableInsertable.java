package Statement;

import javafx.beans.property.SimpleStringProperty;

public interface StatementErrorTableInsertable {

    SimpleStringProperty numberProperty();
    SimpleStringProperty errorProperty();
    SimpleStringProperty detailsProperty();
}
