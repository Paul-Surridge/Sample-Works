package Reminders;

import Database.Database;
import Shared.Blank;
import Shared.Constants;
import Shared.ContextMenuFactory;
import Shared.Formatter;
import Shared.Popup;
import Shared.Table;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

public class Reminders implements Constants{
    
    private static List<Reminder> lstReminders;
    private static TextField tfReminderCreateEditName, tfReminderCreateEditDetails;
    private static DatePicker dpReminderCreateEdit;
    private static Button btReminderCreateEdit;
    private static Label labRemindersAlertsStatus;
    private static Table<RemindersTableInsertable, Reminder> tbReminders;
    private static int iCreateEditMode;
    
    public Reminders(SplitPane spReminders, AnchorPane apRemindersTable,
                     TextField tfReminderCreateEditName, TextField tfReminderCreateEditDetails, DatePicker dpReminderCreateEdit, Button btReminderCreateEdit, Label labRemindersAlertsStatus)
    {
        this.tfReminderCreateEditName       = tfReminderCreateEditName;
        this.tfReminderCreateEditDetails    = tfReminderCreateEditDetails;
        this.dpReminderCreateEdit           = dpReminderCreateEdit;
        this.btReminderCreateEdit           = btReminderCreateEdit;
        this.labRemindersAlertsStatus       = labRemindersAlertsStatus;
        
        loadFromDatabase();
        
        initTables(apRemindersTable);
        initEventHandlers();
        initEventListeners();
        
        clearAndReset();
        checkReminders();
    }
    
    //Internal -----------------------------------------------------------------
    private void loadFromDatabase()
    {
        lstReminders = Database.getReminders();
        sortReminders();
    }
    private static void initTables(AnchorPane ap)
    {
        tbReminders = new Table(TABLEVIEW_ID_REMINDERS, Reminder.class, ContextMenuFactory::buildContextMenuForReminders);
        
        ap.getChildren().add(tbReminders.getTableView());
    }
    private static void initEventHandlers()
    {
        handleForCreateEdit(tfReminderCreateEditName);
        handleForCreateEdit(tfReminderCreateEditDetails);
        handleForCreateEdit(dpReminderCreateEdit);
    }
    private static void initEventListeners()
    {
        listenForInvalidCreateEdit(tfReminderCreateEditName);
        listenForInvalidCreateEdit(tfReminderCreateEditDetails);
        listenForInvalidCreateEdit(dpReminderCreateEdit);
    }
    private static void handleForCreateEdit(TextField tf)
    {
        tf.addEventHandler(KeyEvent.KEY_PRESSED, (KeyEvent ke) -> {
            if(ke.getCode() == KeyCode.ENTER && isCreateEditFieldsValid())
                createEdit();
        });
    }
    private static void handleForCreateEdit(DatePicker dp)
    {
        dp.addEventHandler(KeyEvent.KEY_PRESSED, (KeyEvent ke) -> {
            if(ke.getCode() == KeyCode.ENTER && isCreateEditFieldsValid())
                createEdit();
        });
    }
    private static void listenForInvalidCreateEdit(TextField tf)
    {
        tf.textProperty().addListener(new ChangeListener()
        {
            @Override
            public void changed(ObservableValue o, Object oldVal, Object newVal)
            {
                btReminderCreateEdit.setDisable(!isCreateEditFieldsValid());
            }
        });
    }
    private static void listenForInvalidCreateEdit(DatePicker dp)
    {
        dp.getEditor().textProperty().addListener(new ChangeListener()
        {
            @Override
            public void changed(ObservableValue o, Object oldVal, Object newVal)
            {
                btReminderCreateEdit.setDisable(!isCreateEditFieldsValid());
            }
        });
    }
    private static void sortReminders()
    {
        Collections.sort(lstReminders);
    }
    private static void clearAndReset()
    {
        clearCreateEdit();
        showReminders();
    }
    
    //Delete
    private static void deleteReminder(Reminder r)
    {
        Database.deleteReminder(r);
        
        if(lstReminders.contains(r))
            lstReminders.remove(r);
    }
    private static void deleteAllReminders()
    {
        Database.deleteAllReminders();
        lstReminders.clear();
    }
    
    //Check
    private static void checkReminders()
    {
        int iDate = Formatter.convert(LocalDate.now());
        
        int iTotal = 0;
        
        for(Reminder r : lstReminders)
            if(iDate >= r.getDateAlert())
            {
                r.setStatus("!");
                iTotal++;
            }
            else
                r.setStatus("");
        
        if(iTotal > 0)
            showAlerts(iTotal);
    }
    
    //Create/Edit
    private static void setCreateEditMode(int i)
    {
        iCreateEditMode = i;
        
        switch(iCreateEditMode)
        {
            case REMINDER_CREATE:     btReminderCreateEdit.setText("Add");      break;
            case REMINDER_EDIT:       btReminderCreateEdit.setText("Edit");     break;
        }
    }
    private static boolean isDigit(char c)
    {
        return (c >= 48 && c <= 57);
    }
    private static boolean isCreateEditFieldsValid()
    {
        if(tfReminderCreateEditName.getText().isEmpty())
            return false;
        
        if(tfReminderCreateEditName.getText().length() > DATABASE_RECORD_MAXIMUM_LENGTH_256)
            return false;
        
        if(tfReminderCreateEditDetails.getText().length() > DATABASE_RECORD_MAXIMUM_LENGTH_1024)
            return false;
        
        for(int i = 0 ; i<dpReminderCreateEdit.getEditor().getText().length() ; i++)
        {
            char c = dpReminderCreateEdit.getEditor().getText().charAt(i);
            
            if(!(isDigit(c) || c == '/'))
                return false;
        }

        return true;
    }
    private static void create()
    {
        Reminder r = new Reminder(Formatter.convert(LocalDate.now()),
                tfReminderCreateEditName.getText().trim(),
                tfReminderCreateEditDetails.getText().trim(),
                Formatter.convert(dpReminderCreateEdit.getValue()));
        
        Database.addReminder(r);
        lstReminders.add(r);
        
        sortReminders();
        showReminders();
    }
    private static void edit()
    {
        Reminder r = tbReminders.getSelectedItem();
        
        r.setName(tfReminderCreateEditName.getText());
        r.setDetails(tfReminderCreateEditDetails.getText());
        r.setDateAlert(Formatter.convert(dpReminderCreateEdit.getValue()));
        
        Database.updateReminder(r);
    }
    private static void clearCreateEdit()
    {
        tfReminderCreateEditName.setText("");
        tfReminderCreateEditDetails.setText("");
        
        dpReminderCreateEdit.setValue(LocalDate.now());
    }
    
    //Show Table
    private static void showReminders()
    {
        tbReminders.setItem(new Blank());
        
        if(lstReminders.isEmpty())          tbReminders.setItem(new Blank(BLANK_FIELD_NAME, "No Reminders"));
        else                                tbReminders.setItems(lstReminders);
    }
    private static void showAlerts(int iTotal)
    {
        String sMessage;
        
        if(iTotal == 1)     sMessage = "You currently have x1 reminder alert";
        else                sMessage = "You currently have x" + iTotal + " reminder alerts";

        labRemindersAlertsStatus.setText(sMessage);
        
        Popup.showFixed(POPUP_REMINDER_ALERT);
    }
    
    //External API -------------------------------------------------------------
    public static void init()
    {
        clearAndReset();
    }
    
    //Reminders Tableview - Context Menu
    public static void showDialogCreate()
    {
        setCreateEditMode(REMINDER_CREATE);
        
        clearCreateEdit();
        
        Popup.show(POPUP_REMINDER_CREATE_EDIT);
        
        tfReminderCreateEditName.requestFocus();
    }
    public static void showDialogEdit()
    {
        Reminder r = tbReminders.getSelectedItem();
        
        setCreateEditMode(REMINDER_EDIT);
        
        tfReminderCreateEditName.setText(r.getName());
        tfReminderCreateEditDetails.setText(r.getDetails());
        dpReminderCreateEdit.setValue(Formatter.convert(r.getDateAlert()));
        
        Popup.show(POPUP_REMINDER_CREATE_EDIT);
        
        tfReminderCreateEditName.requestFocus();
    }
    public static void createEdit()
    {
        switch(iCreateEditMode)
        {
            case REMINDER_CREATE:     create();       break;
            case REMINDER_EDIT:       edit();         break;
        }
        
        clearCreateEdit();
        
        Popup.hide();
    }
    public static void delete()
    {
        Popup.hide();
        
        if(tbReminders.areAllItemsSelected())
            deleteAllReminders();
        else
            for(Reminder r : tbReminders.getSelectedItems())
                deleteReminder(r);
        
        showReminders();
    }
}
