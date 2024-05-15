package Categories;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.CheckBox;

public interface CategoriesTableInsertable {

    SimpleStringProperty nameProperty();
    SimpleStringProperty frequencyProperty();
    SimpleStringProperty totalProperty();
    boolean getView();
    void initViewCheckbox(CheckBox cb);
}
