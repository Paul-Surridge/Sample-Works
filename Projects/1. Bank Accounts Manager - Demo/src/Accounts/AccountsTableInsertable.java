package Accounts;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.CheckBox;

public interface AccountsTableInsertable {
    
    SimpleStringProperty balanceAtStartProperty();
    SimpleStringProperty incomingTotalProperty();
    SimpleStringProperty outgoingTotalProperty();
    SimpleStringProperty balanceChangeProperty();
    SimpleStringProperty balanceAtEndProperty();
    
    String getName();
    boolean getView();
    void initViewCheckbox(CheckBox cb);
}
