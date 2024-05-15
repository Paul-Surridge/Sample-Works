package Shared;

import Accounts.AccountsTableInsertable;
import AutoCategories.AutoCategoriesTableInsertable;
import Categories.Administration.CategoriesAdministrationTableInsertable;
import Categories.CategoriesTableInsertable;
import Database.Administration.*;
import Database.Administration.ImportExport.DatabaseAdministrationExportTableInsertable;
import Database.Administration.ImportExport.DatabaseAdministrationImportTableInsertable;
import Database.DatabaseErrorTableInsertable;
import History.HistoryTableInsertable;
import Integrity.IntegrityCheckTableInsertable;
import Refunds.RefundPotentialsTableInsertable;
import Refunds.RefundsTableInsertable;
import Reminders.RemindersTableInsertable;
import Statement.StatementErrorTableInsertable;
import Statement.StatementTableInsertable;
import Watches.WatchesTableInsertable;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.CheckBox;

public class Blank implements AccountsTableInsertable, AutoCategoriesTableInsertable, CategoriesTableInsertable, CategoriesAdministrationTableInsertable, DatabaseErrorTableInsertable,
                                DatabaseAdministrationOverviewTableInsertable, DatabaseAdministrationEntriesTableInsertable, DatabaseAdministrationCategoriesTableInsertable,
                                DatabaseAdministrationAutoCategoriesTableInsertable, DatabaseAdministrationWatchesTableInsertable, DatabaseAdministrationRefundsTableInsertable, DatabaseAdministrationRemindersTableInsertable,
                                DatabaseAdministrationImportTableInsertable, DatabaseAdministrationExportTableInsertable,
                                HistoryTableInsertable, IntegrityCheckTableInsertable,
                                RefundPotentialsTableInsertable, RefundsTableInsertable, RemindersTableInsertable,
                                StatementErrorTableInsertable, StatementTableInsertable, WatchesTableInsertable, ErrorTableInsertable, Constants
                                {

    private CheckBox cbView = new CheckBox();
    
    private SimpleStringProperty sspPrimaryKey = new SimpleStringProperty();
    private SimpleStringProperty sspError = new SimpleStringProperty();
    private SimpleStringProperty sspName = new SimpleStringProperty();
    private SimpleStringProperty sspEntryName = new SimpleStringProperty();
    private SimpleStringProperty sspCategoryName = new SimpleStringProperty();
    private SimpleStringProperty sspDetails = new SimpleStringProperty();
    private SimpleStringProperty sspFrequency = new SimpleStringProperty();
    private SimpleStringProperty sspTotal = new SimpleStringProperty();
    
    public Blank()
    {
        sspPrimaryKey.set("");
        sspError.set("");
        sspName.set("");
        sspEntryName.set("");
        sspCategoryName.set("");
        sspDetails.set("");
    }
    public Blank(int iField, String s)
    {
        setField(iField, s);
    }
    public Blank(int iField1, int iField2, int iField3, String s1, String s2, String s3)
    {
        setField(iField1, s1);
        setField(iField2, s2);
        setField(iField3, s3);
    }
    
    //Internal -----------------------------------------------------------------
    private void setField(int iField, String s)
    {
        switch(iField)
        {
            case BLANK_FIELD_PRIMARY_KEY:       sspPrimaryKey.set(s);           break;
            case BLANK_FIELD_ERROR:             sspError.set(s);                break;
            case BLANK_FIELD_NAME:              sspName.set(s);                 break;
            case BLANK_FIELD_ENTRY_NAME:        sspEntryName.set(s);            break;
            case BLANK_FIELD_CATEGORY_NAME:     sspCategoryName.set(s);         break;
            case BLANK_FIELD_DETAILS:           sspDetails.set(s);              break;
            case BLANK_FIELD_FREQUENCY:         sspFrequency.set(s);            break;
            case BLANK_FIELD_TOTAL:             sspTotal.set(s);                break;
        }
    }
    
    //External API -------------------------------------------------------------
    
    //Accounts
    @Override
    public SimpleStringProperty balanceAtStartProperty()
    {
        return new SimpleStringProperty("");
    }
    @Override
    public SimpleStringProperty balanceAtEndProperty()
    {
        return new SimpleStringProperty("");
    }
    @Override
    public SimpleStringProperty balanceChangeProperty()
    {
        return new SimpleStringProperty("");
    }
    @Override
    public SimpleStringProperty incomingTotalProperty()
    {
        return new SimpleStringProperty("");
    }
    @Override
    public SimpleStringProperty outgoingTotalProperty()
    {
        return new SimpleStringProperty("");
    }
    @Override
    public String getName()
    {
        return "";
    }
    @Override
    public boolean getView()
    {
        return cbView == null ? true : cbView.isSelected();
    }
    @Override
    public void initViewCheckbox(CheckBox cb)
    {
        cbView = cb;
    }
    
    //AutoCategories
    @Override
    public SimpleStringProperty entryNameProperty()
    {
        return sspEntryName;
    }
    @Override
    public SimpleStringProperty categoryNameProperty()
    {
        return sspCategoryName;
    }
    @Override
    public SimpleStringProperty directionProperty()
    {
        return new SimpleStringProperty("");
    }
    @Override
    public SimpleStringProperty frequencyProperty()
    {
        return sspFrequency;
    }
    @Override
    public SimpleStringProperty firstProperty()
    {
        return new SimpleStringProperty("");
    }
    @Override
    public SimpleStringProperty lastProperty()
    {
        return new SimpleStringProperty("");
    }
    
    //Categories
    @Override
    public SimpleStringProperty nameProperty()
    {
        return sspName;
    }
    @Override
    public SimpleStringProperty totalProperty()
    {
        return sspTotal;
    }
    
    //Categories Administration
    @Override
    public SimpleStringProperty frequencyAllProperty()
    {
        return new SimpleStringProperty("");
    }
    @Override
    public SimpleStringProperty firstAllProperty()
    {
        return new SimpleStringProperty("");
    }
    @Override
    public SimpleStringProperty lastAllProperty()
    {
        return new SimpleStringProperty("");
    }
    @Override
    public SimpleStringProperty totalAllProperty()
    {
        return new SimpleStringProperty("");
    }
    
    //Database Error
    @Override
    public SimpleStringProperty numberProperty()
    {
        return new SimpleStringProperty();
    }
    @Override
    public SimpleStringProperty methodProperty()
    {
        return new SimpleStringProperty();
    }
    @Override
    public SimpleStringProperty SQLProperty()
    {
        return new SimpleStringProperty();
    }
    @Override
    public SimpleStringProperty exceptionDetailsProperty()
    {
        return new SimpleStringProperty();
    }
    
    //Database Administration: Overview
    @Override
    public SimpleStringProperty accountProperty()
    {
        return new SimpleStringProperty("");
    }
    @Override
    public SimpleStringProperty tableProperty()
    {
        return new SimpleStringProperty("");
    }
    @Override
    public SimpleStringProperty numberOfRecordsProperty()
    {
        return new SimpleStringProperty("");
    }
    
    //Database Administration: Entries
    @Override
    public SimpleStringProperty primaryKeyProperty()
    {
        return sspPrimaryKey;
    }
    @Override
    public SimpleStringProperty recordIDProperty()
    {
        return new SimpleStringProperty("");
    }
    @Override
    public SimpleStringProperty dateProperty()
    {
        return new SimpleStringProperty("");
    }
    @Override
    public SimpleStringProperty typeProperty()
    {
        return new SimpleStringProperty("");
    }
    @Override
    public SimpleStringProperty inProperty()
    {
        return new SimpleStringProperty("");
    }
    @Override
    public SimpleStringProperty outProperty()
    {
        return new SimpleStringProperty("");
    }
    @Override
    public SimpleStringProperty balanceProperty()
    {
        return new SimpleStringProperty("");
    }

    //Database Administration: Watch
    @Override
    public SimpleStringProperty lastDateProperty()
    {
        return new SimpleStringProperty("");
    }
    @Override
    public SimpleStringProperty lastAmountProperty()
    {
        return new SimpleStringProperty("");
    }
    
    //Database Administration: Refund
    @Override
    public SimpleStringProperty keywordsProperty()
    {
        return new SimpleStringProperty("");
    }
    @Override
    public SimpleStringProperty amountProperty()
    {
        return new SimpleStringProperty("");
    }
    @Override
    public SimpleStringProperty statusProperty()
    {
        return new SimpleStringProperty("");
    }
    
    //Database Administration: Import/Export/Error
    @Override
    public SimpleStringProperty padProperty()
    {
        return new SimpleStringProperty("");
    }
    @Override
    public SimpleStringProperty itemProperty()
    {
        return new SimpleStringProperty("");
    }
    @Override
    public SimpleStringProperty detailsProperty()
    {
        return sspDetails;
    }
    @Override
    public SimpleStringProperty errorProperty()
    {
        return sspError;
    }
    
    //History
    @Override
    public SimpleStringProperty accountNameAbbreviatedProperty()
    {
        return new SimpleStringProperty("");
    }
    
    //Refunds
    @Override
    public SimpleStringProperty receivedDateProperty()
    {
        return new SimpleStringProperty("");
    }
    @Override
    public SimpleStringProperty receivedPrimaryKeyProperty()
    {
        return new SimpleStringProperty("");
    }
    
    //Reminders
    @Override
    public SimpleStringProperty dateAlertProperty()
    {
        return new SimpleStringProperty("");
    }
    
    //Integrity Check: Account 
    @Override
    public SimpleStringProperty accountNameProperty()
    {
        return new SimpleStringProperty("");
    }
    @Override
    public SimpleStringProperty descriptionProperty()
    {
        return new SimpleStringProperty("");
    }
}
