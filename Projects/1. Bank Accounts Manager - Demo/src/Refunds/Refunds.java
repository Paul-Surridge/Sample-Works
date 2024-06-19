package Refunds;

import Accounts.Account;
import Accounts.Accounts;
import Administration.Windows;
import Database.Database;
import History.Entry;
import Shared.Blank;
import Shared.Constants;
import Shared.ContextMenuFactory;
import Shared.Formatter;
import Shared.Popup;
import Shared.Table;
import java.time.LocalDate;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class Refunds implements Constants{
    
    private static RadioButton rbRefundsCurrent, rbRefundsSaving, rbRefundsISA;
    private static TextField tfRefundCreateEditName, tfRefundCreateEditKeywords, tfRefundCreateEditAmountPounds, tfRefundCreateEditAmountPence;
    private static DatePicker dpRefundCreateEdit;
    private static Button btRefundCreateEdit;
    private static ToggleGroup tgRefundsAccounts = new ToggleGroup();
    
    private static Table<RefundsTableInsertable, Refund> tbRefunds;
    private static Table<RefundPotentialsTableInsertable, Entry> tbPotentials;
    
    private static Windows obWindows;
    private static Account aSelected;
    private static int iCreateEditMode;
    
    public Refunds(SplitPane spRefundsTables, AnchorPane apRefundsTable,
                    RadioButton rbRefundsCurrent, RadioButton rbRefundsSaving, RadioButton rbRefundsISA,
                    TextField tfRefundCreateEditName, TextField tfRefundCreateEditKeywords, TextField tfRefundCreateEditAmountPounds, TextField tfRefundCreateEditAmountPence,
                    DatePicker dpRefundCreateEdit, Button btRefundCreateEdit)
    {
        this.rbRefundsCurrent               = rbRefundsCurrent;
        this.rbRefundsSaving                = rbRefundsSaving;
        this.rbRefundsISA                   = rbRefundsISA;
        
        this.tfRefundCreateEditName             = tfRefundCreateEditName;
        this.tfRefundCreateEditKeywords         = tfRefundCreateEditKeywords;
        this.tfRefundCreateEditAmountPounds     = tfRefundCreateEditAmountPounds;
        this.tfRefundCreateEditAmountPence      = tfRefundCreateEditAmountPence;
        this.dpRefundCreateEdit                 = dpRefundCreateEdit;
        this.btRefundCreateEdit                 = btRefundCreateEdit;
        
        obWindows = new Windows(spRefundsTables);
        
        initTables(apRefundsTable);
        initEventHandlers();
        initEventListeners();
        initRadioButtons();
        
        clearAndReset();
    }
    
    //Internal -----------------------------------------------------------------
    
    private static void initTables(AnchorPane ap)
    {
        tbRefunds       = new Table(TABLEVIEW_ID_REFUNDS, Refund.class, ContextMenuFactory::buildContextMenuForRefunds);
        tbPotentials    = new Table(TABLEVIEW_ID_REFUNDS_POTENTIALS, Entry.class, ContextMenuFactory::buildContextMenuForRefundPotentials);
        
        ap.getChildren().add(tbRefunds.getTableView());
    }
    private static void initEventHandlers()
    {
        handleForCreateEdit(tfRefundCreateEditName);
        handleForCreateEdit(tfRefundCreateEditKeywords);
        handleForCreateEdit(tfRefundCreateEditAmountPounds);
        handleForCreateEdit(tfRefundCreateEditAmountPence);
        handleForCreateEdit(dpRefundCreateEdit);
        
        toggleWindow(tbRefunds, 0);
        toggleWindow(tbPotentials, 1);
    }
    private static void initEventListeners()
    {
        tgRefundsAccounts.selectedToggleProperty().addListener(new ChangeListener()
        {
            @Override
            public void changed(ObservableValue o, Object oldVal, Object newVal)
            {                                              
                if      (tgRefundsAccounts.getSelectedToggle() == rbRefundsCurrent)             aSelected = Accounts.get(BARCLAYS_CURRENT);
                else if (tgRefundsAccounts.getSelectedToggle() == rbRefundsSaving)              aSelected = Accounts.get(BARCLAYS_SAVING);
                else if (tgRefundsAccounts.getSelectedToggle() == rbRefundsISA)                 aSelected = Accounts.get(BARCLAYS_ISA);
                
                if(aSelected != null)
                    showRefunds();
            }
        });
        
        listenForInvalidCreateEdit(tfRefundCreateEditName);
        listenForInvalidCreateEdit(tfRefundCreateEditKeywords);
        listenForInvalidCreateEdit(tfRefundCreateEditAmountPounds);
        listenForInvalidCreateEdit(tfRefundCreateEditAmountPence);
        listenForInvalidCreateEdit(dpRefundCreateEdit);
    }
    private static void initRadioButtons()
    {
        rbRefundsCurrent.setToggleGroup(tgRefundsAccounts);
        rbRefundsSaving.setToggleGroup(tgRefundsAccounts);
        rbRefundsISA.setToggleGroup(tgRefundsAccounts);
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
    private static void toggleWindow(Table tb, int iWindow)
    {
        tb.getTableView().addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
            if(me.getButton() == MouseButton.PRIMARY && me.getClickCount() == 2)
                obWindows.toggleWindow(iWindow);
        });
    }
    private static void listenForInvalidCreateEdit(TextField tf)
    {
        tf.textProperty().addListener(new ChangeListener()
        {
            @Override
            public void changed(ObservableValue o, Object oldVal, Object newVal)
            {
                if(tf == tfRefundCreateEditAmountPounds && tf.getText().contains("."))
                {
                    tf.setText(tf.getText().replace(".", ""));
                    tfRefundCreateEditAmountPence.requestFocus();
                }
                
                btRefundCreateEdit.setDisable(!isCreateEditFieldsValid());
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
                btRefundCreateEdit.setDisable(!isCreateEditFieldsValid());
            }
        });
    }
    
    //Buttons/UI
    private static void clearAndReset()
    {
        aSelected = null;
        
        clearRadioButtons();
        clearCreateEdit();
        setCreateEditMode(REFUND_CREATE);
        
        obWindows.reset();
        obWindows.removeAllWindows();
        
        showEmpty();
    }
    private static void clearRadioButtons()
    {
        for(Toggle t : tgRefundsAccounts.getToggles())
            t.setSelected(false);
    }
    
    //Check
    private static void checkAllAccounts()
    {
        for(Account a : Accounts.getAccounts())
            for(Refund r : a.getRefunds())
                a.check(r);
    }
    
    //Create/Edit
    private static void setCreateEditMode(int i)
    {
        iCreateEditMode = i;
        
        switch(iCreateEditMode)
        {
            case REFUND_CREATE:     btRefundCreateEdit.setText("Add");      break;
            case REFUND_EDIT:       btRefundCreateEdit.setText("Edit");     break;
        }
    }
    private static boolean isDigit(char c)
    {
        return (c >= 48 && c <= 57);
    }
    private static boolean isCreateEditFieldsValid()
    {
        if(tfRefundCreateEditName.getText().isEmpty())
            return false;
        
        if(tfRefundCreateEditAmountPounds.getText().isEmpty() || tfRefundCreateEditAmountPence.getText().isEmpty())
            return false;
        
        if(tfRefundCreateEditAmountPence.getText().isEmpty())
            return false;
        
        if(tfRefundCreateEditName.getText().length() > DATABASE_RECORD_MAXIMUM_LENGTH_256)
            return false;
        
        if(tfRefundCreateEditKeywords.getText().length() > DATABASE_RECORD_MAXIMUM_LENGTH_1024)
            return false;
        
        if(tfRefundCreateEditAmountPounds.getText().length() > 6)
            return false;
        
        for(int i = 0 ; i<tfRefundCreateEditAmountPounds.getText().length() ; i++)
            if(!isDigit(tfRefundCreateEditAmountPounds.getText().charAt(i)))
                return false;
        
        if(tfRefundCreateEditAmountPence.getText().length() != 2)
            return false;
        
        for(int i = 0 ; i<tfRefundCreateEditAmountPence.getText().length() ; i++)
            if(!isDigit(tfRefundCreateEditAmountPence.getText().charAt(i)))
                return false;
        
        for(int i = 0 ; i<dpRefundCreateEdit.getEditor().getText().length() ; i++)
        {
            char c = dpRefundCreateEdit.getEditor().getText().charAt(i);
            
            if(!(isDigit(c) || c == '/'))
                return false;
        }
        
        return true;
    }
    private static void create()
    {
        Refund r = new Refund(aSelected.getAccountID(),
                Formatter.convert(dpRefundCreateEdit.getValue()),
                tfRefundCreateEditName.getText().trim(),
                tfRefundCreateEditKeywords.getText().trim(),
                tfRefundCreateEditAmountPounds.getText(),
                tfRefundCreateEditAmountPence.getText());
        
        aSelected.addRefund(r);
        aSelected.check(r);
        
        showRefunds();
    }
    private static void edit()
    {
        aSelected.updateRefund(tbRefunds.getSelectedItem(), Formatter.convert(dpRefundCreateEdit.getValue()),
                tfRefundCreateEditName.getText(),
                tfRefundCreateEditKeywords.getText(),
                tfRefundCreateEditAmountPounds.getText(),
                tfRefundCreateEditAmountPence.getText());
        
        aSelected.check(tbRefunds.getSelectedItem());
        
        clearCreateEdit();
    }
    private static void clearCreateEdit()
    {
        tfRefundCreateEditName.setText("");
        tfRefundCreateEditKeywords.setText("");
        tfRefundCreateEditAmountPounds.setText("");
        tfRefundCreateEditAmountPence.setText("");
        
        dpRefundCreateEdit.setValue(LocalDate.now());
    }
    
    //Show Tables
    private static void showEmpty()
    {
        tbRefunds.setItem(new Blank());
        tbPotentials.setItem(new Blank());
    }
    private static void showRefunds()
    {
        if(aSelected.numberOfRefunds() == 0)    tbRefunds.setItem(new Blank(BLANK_FIELD_NAME, "No Refunds"));
        else                                    tbRefunds.setItems(aSelected.getRefunds());
        
        obWindows.removeAllWindows();
    }
    private static void showPotentials(Refund r, int iAction)
    {
        switch(iAction)
        {
            case REFUND_VIEW_POTENTIAL:     tbPotentials.setItems(r.getPotentials());                                                          break;
            case REFUND_VIEW_ALL_INCOMING:  tbPotentials.setItems(Database.getEntriesWithDirection(aSelected, r.getDate(), IN));               break;
            case REFUND_VIEW_CONFIRMED:     tbPotentials.setItems(Database.getEntriesWithPrimaryKey(aSelected, r.getReceivedPrimaryKey()));    break;
        }
        
        if(tbPotentials.isEmpty())
        {
            if(iAction == REFUND_VIEW_CONFIRMED)    tbPotentials.setItem(new Blank(BLANK_FIELD_NAME, "Refund entry is no longer in database (Primary Key: " + r.getReceivedPrimaryKey() + ")"));
            else
                switch(iAction)
                {
                    case REFUND_VIEW_POTENTIAL:     tbPotentials.setItem(new Blank(BLANK_FIELD_NAME, "No potential refunds"));      break;
                    case REFUND_VIEW_ALL_INCOMING:  tbPotentials.setItem(new Blank(BLANK_FIELD_NAME, "No incoming since date"));    break;
                }
        }
        else
            tbPotentials.addItem(new Blank());
        
        tbPotentials.scrollToEnd();
        
        obWindows.addWindow(tbPotentials, 1);
    }
    
    //External API -------------------------------------------------------------
    public static void init()
    {
        clearAndReset();
        checkAllAccounts();
    }
    
    //Refunds Tableview - Context Menu
    public static void showDialogCreate()
    {
        setCreateEditMode(REFUND_CREATE);
        
        clearCreateEdit();
        
        Popup.show(POPUP_REFUND_CREATE_EDIT);
        
        tfRefundCreateEditName.requestFocus();
    }
    public static void showDialogEdit()
    {
        Refund r = tbRefunds.getSelectedItem();
        
        setCreateEditMode(REFUND_EDIT);
        
        tfRefundCreateEditName.setText(r.getName());
        tfRefundCreateEditKeywords.setText(r.getKeywordsSerialised());
        tfRefundCreateEditAmountPounds.setText(String.valueOf(r.getPounds()));
        tfRefundCreateEditAmountPence.setText(Formatter.padLeadingZero(String.valueOf(r.getPence())));
        dpRefundCreateEdit.setValue(Formatter.convert(r.getDate()));
        
        Popup.show(POPUP_REFUND_CREATE_EDIT);
        
        tfRefundCreateEditName.requestFocus();
    }
    public static void createEdit()
    {
        switch(iCreateEditMode)
        {
            case REFUND_CREATE:     create();       break;
            case REFUND_EDIT:       edit();         break;
        }
        
        Popup.hide();
    }
    public static void viewPotentials()
    {
        showPotentials(tbRefunds.getSelectedItem(), REFUND_VIEW_POTENTIAL);
    }
    public static void viewAllIncoming()
    {
        showPotentials(tbRefunds.getSelectedItem(), REFUND_VIEW_ALL_INCOMING);
    }
    public static void viewConfirmed()
    {
        showPotentials(tbRefunds.getSelectedItem(), REFUND_VIEW_CONFIRMED);
    }
    public static void delete()
    {
        Popup.hide();
        
        if(tbRefunds.areAllItemsSelected())
            aSelected.deleteAllRefunds();
        else
            for(Refund r : tbRefunds.getSelectedItems())
                aSelected.deleteRefund(r);
        
        showRefunds();
    }
    public static void received()
    {
        aSelected.refundReceived(tbRefunds.getSelectedItem(), tbPotentials.getSelectedItem());
        
        obWindows.removeAllWindows();
    }
    
    //Context Menu Miscellanous Data
    public static boolean isContextMenuReady()
    {
        return aSelected != null;
    }
}
