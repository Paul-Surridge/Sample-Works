package Database;

import javafx.beans.property.SimpleStringProperty;

public interface DatabaseErrorTableInsertable {

    SimpleStringProperty numberProperty();
    SimpleStringProperty methodProperty();
    SimpleStringProperty SQLProperty();
    SimpleStringProperty exceptionDetailsProperty();
}
