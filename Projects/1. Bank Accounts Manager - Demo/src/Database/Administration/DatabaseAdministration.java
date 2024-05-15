package Database.Administration;

import Database.Administration.ImportExport.Importer;
import Database.Administration.ImportExport.Exporter;
import Accounts.Account;
import Accounts.Accounts;
import Administration.Administration;
import AutoCategories.AutoCategory;
import Categories.Category;
import Shared.Constants;
import Database.Database;
import History.Entry;
import Refunds.Refund;
import Reminders.Reminder;
import Shared.Blank;
import Shared.ContextMenuFactory;
import Shared.Formatter;
import Shared.Table;
import Watches.Watch;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SplitPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;

public class DatabaseAdministration implements Constants{
    
    private static AnchorPane apDatabaseTable;
    private static RadioButton rbDatabaseCurrent, rbDatabaseSaving, rbDatabaseISA;
    private static RadioButton rbDatabaseTableEntries, rbDatabaseTableCategories, rbDatabaseTableAutoCategories, rbDatabaseTableWatches, rbDatabaseTableRefunds;
    private static DatePicker dpDatabaseViewDate;
    private static Button btDatabaseViewDate, btDatabaseViewAll, btDatabaseOverview;
    private static Button btDatabaseImport, btDatabaseImportConfirm, btDatabaseImportClose;
    private static Button btDatabaseExport, btDatabaseExportConfirm, btDatabaseExportClose;
    private static Button btDatabaseResetAccount, btDatabaseResetAll, btDatabaseClose;
    private static Label labDatabaseStatusMessage, labDatabaseStatusRange;
    private static ImageView ivInfoDatabaseAdministration;
    
    private static ToggleGroup tgAccounts = new ToggleGroup();
    private static ToggleGroup tgTable = new ToggleGroup();
    
    private static Table<DatabaseAdministrationOverviewTableInsertable, Item> tbOverview;
    private static Table<DatabaseAdministrationEntriesTableInsertable, Entry> tbEntries;
    private static Table<DatabaseAdministrationCategoriesTableInsertable, Category> tbCategories;
    private static Table<DatabaseAdministrationAutoCategoriesTableInsertable, AutoCategory> tbAutoCategories;
    private static Table<DatabaseAdministrationWatchesTableInsertable, Watch> tbWatches;
    private static Table<DatabaseAdministrationRefundsTableInsertable, Refund> tbRefunds;
    private static Table<DatabaseAdministrationRemindersTableInsertable, Reminder> tbReminders;
    
    private static Blank dbBlank = new Blank();
    private static List<Integer> lstTargetDates = new ArrayList<>();
    
    private static Importer obImporter;
    private static Exporter obExporter;
    
    private static Account aSelected;
    private static int iSelectedTable, iMode;
    
    public DatabaseAdministration(SplitPane spDatabaseTables, AnchorPane apDatabaseTable,
                    RadioButton rbDatabaseCurrent, RadioButton rbDatabaseSaving, RadioButton rbDatabaseISA,
                    RadioButton rbDatabaseTableEntries, RadioButton rbDatabaseTableCategories, RadioButton rbDatabaseTableAutoCategories, RadioButton rbDatabaseTableWatches, RadioButton rbDatabaseTableRefunds,
                    Label labDatabaseStatusMessage, Label labDatabaseStatusRange, DatePicker dpDatabaseViewDate,   
                    Button btDatabaseViewDate, Button btDatabaseViewAll, Button btDatabaseOverview,
                    Button btDatabaseImport, Button btDatabaseImportConfirm, Button btDatabaseImportClose,
                    Button btDatabaseExport, Button btDatabaseExportConfirm, Button btDatabaseExportClose,
                    Button btDatabaseResetAccount, Button btDatabaseResetAll, Button btDatabaseClose, ImageView ivInfoDatabaseAdministration)
    {
        this.apDatabaseTable                    = apDatabaseTable;
        
        this.rbDatabaseCurrent                  = rbDatabaseCurrent;
        this.rbDatabaseSaving                   = rbDatabaseSaving;
        this.rbDatabaseISA                      = rbDatabaseISA;
        
        this.rbDatabaseTableEntries             = rbDatabaseTableEntries;
        this.rbDatabaseTableCategories          = rbDatabaseTableCategories;
        this.rbDatabaseTableAutoCategories      = rbDatabaseTableAutoCategories;
        this.rbDatabaseTableWatches             = rbDatabaseTableWatches;
        this.rbDatabaseTableRefunds             = rbDatabaseTableRefunds;
        
        this.btDatabaseViewDate                 = btDatabaseViewDate;
        this.btDatabaseViewAll                  = btDatabaseViewAll;
        this.btDatabaseOverview                 = btDatabaseOverview;
        
        this.btDatabaseImport                   = btDatabaseImport;
        this.btDatabaseImportConfirm            = btDatabaseImportConfirm;
        this.btDatabaseImportClose              = btDatabaseImportClose;
        
        this.btDatabaseExport                   = btDatabaseExport;
        this.btDatabaseExportConfirm            = btDatabaseExportConfirm;
        this.btDatabaseExportClose              = btDatabaseExportClose;
        
        this.btDatabaseResetAccount             = btDatabaseResetAccount;
        this.btDatabaseResetAll                 = btDatabaseResetAll;
        this.btDatabaseClose                    = btDatabaseClose;
        
        this.labDatabaseStatusMessage           = labDatabaseStatusMessage;
        this.labDatabaseStatusRange             = labDatabaseStatusRange;
        this.dpDatabaseViewDate                 = dpDatabaseViewDate;
        
        this.ivInfoDatabaseAdministration       = ivInfoDatabaseAdministration;
        
        obImporter = new Importer(spDatabaseTables, apDatabaseTable, btDatabaseImportConfirm);
        obExporter = new Exporter(spDatabaseTables, apDatabaseTable, btDatabaseExportConfirm);
        
        initTables(apDatabaseTable);
        initRadioButtons();
        initEventListeners();
        initEventHandlers();
        initTargetDates();
        
        clearAndReset();
    }
    
    //Internal -----------------------------------------------------------------
    private static void initTables(AnchorPane ap)
    {
        tbOverview          = new Table(TABLEVIEW_ID_DATABASE_OVERVIEW, Item.class, ContextMenuFactory::buildContextMenuForDatabaseOverview);
        tbEntries           = new Table(TABLEVIEW_ID_DATABASE_ENTRIES, Entry.class, ContextMenuFactory::buildContextMenuForDatabaseTables);
        tbCategories        = new Table(TABLEVIEW_ID_DATABASE_CATEGORIES, Category.class, ContextMenuFactory::buildContextMenuForDatabaseTables);
        tbAutoCategories    = new Table(TABLEVIEW_ID_DATABASE_AUTO_CATEGORIES, AutoCategory.class, ContextMenuFactory::buildContextMenuForDatabaseTables);
        tbWatches           = new Table(TABLEVIEW_ID_DATABASE_WATCHES, Watch.class, ContextMenuFactory::buildContextMenuForDatabaseTables);
        tbRefunds           = new Table(TABLEVIEW_ID_DATABASE_REFUNDS, Refund.class, ContextMenuFactory::buildContextMenuForDatabaseTables);
        tbReminders         = new Table(TABLEVIEW_ID_DATABASE_REMINDERS, Reminder.class, ContextMenuFactory::buildContextMenuForDatabaseTables);
        
        ap.getChildren().add(tbOverview.getTableView());
        ap.getChildren().add(tbEntries.getTableView());
        ap.getChildren().add(tbCategories.getTableView());
        ap.getChildren().add(tbAutoCategories.getTableView());
        ap.getChildren().add(tbWatches.getTableView());
        ap.getChildren().add(tbRefunds.getTableView());
        ap.getChildren().add(tbReminders.getTableView());
    }
    private static void initRadioButtons()
    {
        rbDatabaseCurrent.setToggleGroup(tgAccounts);
        rbDatabaseSaving.setToggleGroup(tgAccounts);
        rbDatabaseISA.setToggleGroup(tgAccounts);
        
        rbDatabaseTableEntries.setToggleGroup(tgTable);
        rbDatabaseTableCategories.setToggleGroup(tgTable);
        rbDatabaseTableAutoCategories.setToggleGroup(tgTable);
        rbDatabaseTableWatches.setToggleGroup(tgTable);
        rbDatabaseTableRefunds.setToggleGroup(tgTable);
    }
    private static void initEventHandlers()
    {
        tbOverview.getTableView().addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
            if(me.getButton() == MouseButton.PRIMARY && me.getClickCount() == 2)
                showOverviewItem();
        });
    }
    private static void initEventListeners()
    {
        tgAccounts.selectedToggleProperty().addListener(new ChangeListener()
        {
            @Override
            public void changed(ObservableValue o, Object oldVal, Object newVal)
            {
                if      (tgAccounts.getSelectedToggle() == rbDatabaseCurrent)           aSelected = Accounts.get(BARCLAYS_CURRENT);
                else if (tgAccounts.getSelectedToggle() == rbDatabaseSaving)            aSelected = Accounts.get(BARCLAYS_SAVING);
                else if (tgAccounts.getSelectedToggle() == rbDatabaseISA)               aSelected = Accounts.get(BARCLAYS_ISA);
                
                clearAllTables();
                setMode(MODE_VIEW);
                
                if(iSelectedTable != NOT_DEFINED)
                    showTable();
            }
        });
        
        tgTable.selectedToggleProperty().addListener(new ChangeListener()
        {
            @Override
            public void changed(ObservableValue o, Object oldVal, Object newVal)
            {
                if      (tgTable.getSelectedToggle() == rbDatabaseTableEntries)         iSelectedTable = DATABASE_TABLE_ENTRY;
                else if (tgTable.getSelectedToggle() == rbDatabaseTableCategories)      iSelectedTable = DATABASE_TABLE_CATEGORY;
                else if (tgTable.getSelectedToggle() == rbDatabaseTableAutoCategories)  iSelectedTable = DATABASE_TABLE_AUTO_CATEGORY;
                else if (tgTable.getSelectedToggle() == rbDatabaseTableWatches)         iSelectedTable = DATABASE_TABLE_WATCH;
                else if (tgTable.getSelectedToggle() == rbDatabaseTableRefunds)         iSelectedTable = DATABASE_TABLE_REFUND;
                
                if(aSelected != null)
                    showTable();
            }
        });
    }
    private static void initTargetDates()
    {
        for(int iAccountID = 0 ; iAccountID<NUMBER_OF_ACCOUNTS ; iAccountID++)
            lstTargetDates.add(NOT_DEFINED);
    }
    private static void clearAndReset()
    {
        aSelected = null;
        iSelectedTable = NOT_DEFINED;
        
        clearAllTables();
        clearAccountRadioButtons();
        clearTableRadioButtons();
        
        for(int i = 0 ; i<lstTargetDates.size() ; i++)
            lstTargetDates.set(i, NOT_DEFINED);
        
        showOverviewTable();
        
        setMode(MODE_OVERVIEW);
    }
    
    //Mode
    private static void setMode(int i)
    {
        iMode = i;
        
        refreshStatusMessage();
        refreshStatusRange();
        refreshButtons();
    }
    
    //Import/Export
    private static String buildExportFilename()
    {
        String s = aSelected.getName();
        
        if(Database.hasEntries(aSelected))  s += " - (" + Formatter.convert(DATE, DATABASE, TABLEVIEW, Database.getEntryDate(aSelected, EARLIEST)) + ") - (" + Formatter.convert(DATE, DATABASE, TABLEVIEW, Database.getEntryDate(aSelected, LATEST)) + ")";
        else                                s += " - [No Entries]";
            
        return s;
    }
    private static File openImportFileChooser()
    {
        FileChooser fileChooser = new FileChooser();
        
        fileChooser.setTitle("Import Account From File");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("txt", "*.txt"));
        
        return fileChooser.showOpenDialog(Administration.getWindow());
    }
    private static File openExportFileChooser()
    {
        FileChooser fileChooser = new FileChooser();
        
        fileChooser.setTitle("Export Account To File");
        fileChooser.setInitialFileName(buildExportFilename());
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("txt", "*.txt"));
        
        return fileChooser.showSaveDialog(Administration.getWindow());
    }
    
    //Table Management
    private static void clearTable(int iTable)
    {
        getTable(iTable).clear();
    }
    private static void clearAllTables()
    {
        for(int iTable = DATABASE_TABLE_ENTRY ; iTable<=DATABASE_TABLE_REMINDER ; iTable++)
            clearTable(iTable);
    }
    private static void checkAddBlankToEmptyTable(int iTable, boolean bShowNoRecords)
    {
        Table tb = getTable(iTable);
        
        if(tb.isEmpty())
            tb.setItem(new Blank(BLANK_FIELD_PRIMARY_KEY, bShowNoRecords ? "No Records" : ""));
    }
    private static Table getTable(int iTable)
    {
        switch(iTable)
        {
            case DATABASE_TABLE_ENTRY:              return tbEntries;
            case DATABASE_TABLE_CATEGORY:           return tbCategories;
            case DATABASE_TABLE_AUTO_CATEGORY:      return tbAutoCategories;
            case DATABASE_TABLE_WATCH:              return tbWatches;
            case DATABASE_TABLE_REFUND:             return tbRefunds;
            case DATABASE_TABLE_REMINDER:           return tbReminders;
        }
        
        return null;
    }
    
    //Status Labelling/Buttons
    private static void setAccountRadioButtonsEnable(boolean bState)
    {
        rbDatabaseCurrent.setDisable(!bState);
        rbDatabaseSaving.setDisable(!bState);
        rbDatabaseISA.setDisable(!bState);
    }
    private static void clearAccountRadioButtons()
    {
        rbDatabaseCurrent.setSelected(false);
        rbDatabaseSaving.setSelected(false);
        rbDatabaseISA.setSelected(false);
    }
    private static void setTableRadioButtonsVisible(boolean bVisible)
    {
        rbDatabaseTableEntries.setVisible(bVisible);
        rbDatabaseTableCategories.setVisible(bVisible);
        rbDatabaseTableAutoCategories.setVisible(bVisible);
        rbDatabaseTableWatches.setVisible(bVisible);
        rbDatabaseTableRefunds.setVisible(bVisible);
    }
    private static void setTableRadioButtonsEnable(boolean bState)
    {
        rbDatabaseTableEntries.setDisable(!bState);
        rbDatabaseTableCategories.setDisable(!bState);
        rbDatabaseTableAutoCategories.setDisable(!bState);
        rbDatabaseTableWatches.setDisable(!bState);
        rbDatabaseTableRefunds.setDisable(!bState);
    }
    private static void clearTableRadioButtons()
    {
        rbDatabaseTableEntries.setSelected(false);
        rbDatabaseTableCategories.setSelected(false);
        rbDatabaseTableAutoCategories.setSelected(false);
        rbDatabaseTableWatches.setSelected(false);
        rbDatabaseTableRefunds.setSelected(false);
    }
    private static void setViewButtonsVisible(boolean bVisible)
    {
        btDatabaseViewDate.setVisible(bVisible);
        btDatabaseViewAll.setVisible(bVisible);
    }
    private static void setOverviewButtonVisible(boolean bVisible)
    {
        btDatabaseOverview.setVisible(bVisible);
    }
    private static void setImportExportButtonsVisible(boolean bVisible)
    {
        btDatabaseImport.setVisible(bVisible);
        btDatabaseExport.setVisible(bVisible);
    }
    private static void setImportExportButtonsEnable(boolean bState)
    {
        btDatabaseImport.setDisable(!bState);
        btDatabaseExport.setDisable(!bState);
    }
    private static void setImportingButtonsVisible(boolean bVisible)
    {
        btDatabaseImportConfirm.setVisible(bVisible);
        btDatabaseImportClose.setVisible(bVisible);
    }
    private static void setExportingButtonsVisible(boolean bVisible)
    {
        btDatabaseExportConfirm.setVisible(bVisible);
        btDatabaseExportClose.setVisible(bVisible);
    }
    private static void setResetAccountButtonsEnable(boolean bState)
    {
        btDatabaseResetAccount.setDisable(!bState);
    }
    private static void setResetAndCloseButtonsVisible(boolean bVisible)
    {
        btDatabaseResetAccount.setVisible(bVisible);
        btDatabaseResetAll.setVisible(bVisible);
        btDatabaseClose.setVisible(bVisible);
    }
    private static void setInfoVisible(boolean bVisible)
    {
        ivInfoDatabaseAdministration.setVisible(bVisible);
    }
    private static void refreshStatusMessage()
    {
        String sMessage = "Records: ";
        
        if      (iMode != MODE_VIEW)                                    sMessage = "";
        else if (aSelected == null || iSelectedTable == NOT_DEFINED)    sMessage = "";
        else if (isSelectedTableEmpty())                                sMessage = "No Records";
        else                                                            sMessage += getTable(iSelectedTable).numberOfItems();

        labDatabaseStatusMessage.setText(sMessage);
    }
    private static void setStatusMessage(String sStatus)
    {
        labDatabaseStatusMessage.setText(sStatus);
    }
    private static void refreshStatusRange()
    {
        int iTargetDate;
        
        
        if(iMode != MODE_VIEW)                                                  labDatabaseStatusRange.setText("");
        else if(aSelected == null || iSelectedTable != DATABASE_TABLE_ENTRY)    labDatabaseStatusRange.setText("");
        else if(isSelectedTableEmpty())                                         labDatabaseStatusRange.setText("");
        else
        {
            iTargetDate = lstTargetDates.get(aSelected.getAccountID());
            
            if(iTargetDate == NOT_DEFINED)                                      labDatabaseStatusRange.setText("All Entries");
            else                                                                labDatabaseStatusRange.setText("Date From: " + (Formatter.convert(DATE, DATABASE, TABLEVIEW, iTargetDate)));
        }
    }
    private static void setStatusRange(String sStatus)
    {
        labDatabaseStatusRange.setText(sStatus);
    }
    private static void refreshButtons()
    {
        switch(iMode)
        {
            case MODE_OVERVIEW:
            case MODE_VIEW:     setAccountRadioButtonsEnable(true);     break;
            case MODE_IMPORT:
            case MODE_EXPORT:   setAccountRadioButtonsEnable(false);    break;
        }
        
        switch(iMode)
        {
            case MODE_OVERVIEW: setTableRadioButtonsVisible(true);      break;
            case MODE_VIEW:     setTableRadioButtonsVisible(true);      break;
            case MODE_IMPORT:
            case MODE_EXPORT:   setTableRadioButtonsVisible(false);     break;
        }
        
        switch(iMode)
        {
            case MODE_OVERVIEW: setTableRadioButtonsEnable(false);      break;
            case MODE_VIEW:     setTableRadioButtonsEnable(true);       break;
            case MODE_IMPORT:
            case MODE_EXPORT:   setTableRadioButtonsEnable(false);      break;
        }
        
        switch(iMode)
        {
            case MODE_OVERVIEW: setViewButtonsVisible(false);           break;
            case MODE_VIEW:
            {
                if(iSelectedTable == DATABASE_TABLE_ENTRY && !isSelectedTableEmpty())   setViewButtonsVisible(true);
                else                                                                    setViewButtonsVisible(false);
                
                break;
            }
            case MODE_IMPORT:
            case MODE_EXPORT:   setViewButtonsVisible(false);           break;
        }
        
        switch(iMode)
        {
            case MODE_OVERVIEW:
            case MODE_VIEW:     setOverviewButtonVisible(true);         break;
            case MODE_IMPORT:
            case MODE_EXPORT:   setOverviewButtonVisible(false);        break;
        }
        
        switch(iMode)
        {
            case MODE_OVERVIEW:
            case MODE_VIEW:     setImportExportButtonsVisible(true);    break;
            case MODE_IMPORT:
            case MODE_EXPORT:   setImportExportButtonsVisible(false);   break;
        }
        
        switch(iMode)
        {
            case MODE_OVERVIEW: setImportExportButtonsEnable(false);    break;
            case MODE_VIEW:     setImportExportButtonsEnable(true);     break;
            case MODE_IMPORT:
            case MODE_EXPORT:   setImportExportButtonsEnable(false);    break;
        }
        
        switch(iMode)
        {
            case MODE_OVERVIEW: setResetAccountButtonsEnable(false);    break;
            case MODE_VIEW:     setResetAccountButtonsEnable(true);     break;
            case MODE_IMPORT:
            case MODE_EXPORT:   setResetAccountButtonsEnable(false);    break;
        }
        
        switch(iMode)
        {
            case MODE_OVERVIEW: setResetAndCloseButtonsVisible(true);   break;
            case MODE_VIEW:     setResetAndCloseButtonsVisible(true);   break;
            case MODE_IMPORT:
            case MODE_EXPORT:   setResetAndCloseButtonsVisible(false);  break;
        }
        
        switch(iMode)
        {
            case MODE_OVERVIEW:
            case MODE_VIEW:     setImportingButtonsVisible(false);      break;
            case MODE_IMPORT:   setImportingButtonsVisible(true);       break;
            case MODE_EXPORT:   setImportingButtonsVisible(false);      break;
        }
        
        switch(iMode)
        {
            case MODE_OVERVIEW:
            case MODE_VIEW:     setExportingButtonsVisible(false);      break;
            case MODE_IMPORT:   setExportingButtonsVisible(false);      break;
            case MODE_EXPORT:   setExportingButtonsVisible(true);       break;
        }
        
        switch(iMode)
        {
            case MODE_OVERVIEW: setInfoVisible(true);                   break;
            case MODE_VIEW:
            case MODE_IMPORT:
            case MODE_EXPORT:   setInfoVisible(false);                  break;
        }
    }

    //Component Deletion
    private static void deleteEntries()
    {
        for(Entry e : tbEntries.getSelectedItems())
            Database.deleteEntry(e);
        
        tbEntries.clearAndRemoveSelection();
        tbEntries.clearSelection();
    }
    private static void deleteCategories()
    {
        for(Category c : tbCategories.getSelectedItems())
            Database.deleteCategory(c);
        
        tbCategories.clearAndRemoveSelection();
        tbCategories.clearSelection();
    }
    private static void deleteAutoCategories()
    {
        for(AutoCategory ac : tbAutoCategories.getSelectedItems())
            Database.deleteAutoCategory(ac);
        
        tbAutoCategories.clearAndRemoveSelection();
        tbAutoCategories.clearSelection();
    }
    private static void deleteWatches()
    {
        for(Watch w : tbWatches.getSelectedItems())
            Database.deleteWatch(w);
        
        tbWatches.clearAndRemoveSelection();
        tbWatches.clearSelection();
    }
    private static void deleteRefunds()
    {
        for(Refund r : tbRefunds.getSelectedItems())
            Database.deleteRefund(r);
        
        tbRefunds.clearAndRemoveSelection();
        tbRefunds.clearSelection();
    }
    private static void deleteReminders()
    {
        for(Reminder r : tbReminders.getSelectedItems())
            Database.deleteReminder(r);
        
        tbReminders.clearAndRemoveSelection();
        tbReminders.clearSelection();
    }
    
    //Display Content
    private static String getTab()
    {
        return "\t";
    }
    private static void setTargetDate(int iDate)
    {
        lstTargetDates.set(aSelected.getAccountID(), iDate);
    }
    private static void buildTable(Account a, int iTable)
    {
        switch(iTable)
        {
            case DATABASE_TABLE_ENTRY:              tbEntries.setItems(Database.getEntries(a, lstTargetDates.get(aSelected.getAccountID())));       break;
            case DATABASE_TABLE_CATEGORY:           tbCategories.setItems(Database.getCategories(a));                                               break;
            case DATABASE_TABLE_AUTO_CATEGORY:      tbAutoCategories.setItems(Database.getAutoCategories(a));                                       break;
            case DATABASE_TABLE_WATCH:              tbWatches.setItems(Database.getWatches(a));                                                     break;
            case DATABASE_TABLE_REFUND:             tbRefunds.setItems(Database.getRefunds(a));                                                     break;
            case DATABASE_TABLE_REMINDER:           tbReminders.setItems(Database.getReminders());                                                  break;
        }
        
        checkAddBlankToEmptyTable(iTable, true);
    }
    private static void showTable()
    {
        Table tb = getTable(iSelectedTable);
        
        if(tb == null)
        {
            showOverviewTable();
            return;
        }
        
        if(tb.isEmpty())
            buildTable(aSelected, iSelectedTable);

        tb.scrollToStart();
        
        apDatabaseTable.getChildren().clear();
        apDatabaseTable.getChildren().add(tb.getTableView());
        
        refreshButtons();
        refreshStatusMessage();
        refreshStatusRange();
    }
    private static void showEmptyTable(int iTable)
    {
        clearTable(iTable);
        
        refreshStatusMessage();
        refreshStatusRange();
    }
    private static void showOverviewTable()
    {
        String sNumberOfEntries;
        
        tbOverview.setItem(new Blank());
        
        for(Account a : Accounts.getAccounts())
        {
            sNumberOfEntries = String.valueOf(Database.getNumberOfEntries(a));
            
            if(!sNumberOfEntries.equals("0"))
                sNumberOfEntries += getTab() + getTab() + "(" + Formatter.convert(DATE, DATABASE, TABLEVIEW, Database.getEntryDate(a, EARLIEST)) + " - " + Formatter.convert(DATE, DATABASE, TABLEVIEW, Database.getEntryDate(a, LATEST)) + ")";
            
            tbOverview.addItem(new Item(a, DATABASE_TABLE_ENTRY, "Entries", sNumberOfEntries));
            tbOverview.addItem(new Item(a, DATABASE_TABLE_CATEGORY, "Categories", Database.getNumberOfCategories(a)));
            tbOverview.addItem(new Item(a, DATABASE_TABLE_AUTO_CATEGORY, "Auto Assigns", Database.getNumberOfAutoCategories(a)));
            tbOverview.addItem(new Item(a, DATABASE_TABLE_WATCH, "Watches", Database.getNumberOfWatches(a)));
            tbOverview.addItem(new Item(a, DATABASE_TABLE_REFUND, "Refunds", Database.getNumberOfRefunds(a)));
            tbOverview.addItem(new Blank());
        }
        
        tbOverview.addItem(new Item(DATABASE_TABLE_REMINDER, "Reminders", Database.getNumberOfReminders()));
        tbOverview.addItem(new Blank());
            
        apDatabaseTable.getChildren().clear();
        apDatabaseTable.getChildren().add(tbOverview.getTableView());
    }

    //External API -------------------------------------------------------------
    public static void init()
    {
        clearAndReset();
    }
    
    //UI Buttons
    public static void showOverview()
    {
        clearAndReset();
    }
    public static void showOverviewItem()
    {
        int iTable = tbOverview.getSelectedItem().getTable();
            
        switch(tbOverview.getSelectedItem().getAccountID())
        {
            case BARCLAYS_CURRENT:      rbDatabaseCurrent.setSelected(true);                break;
            case BARCLAYS_SAVING:       rbDatabaseSaving.setSelected(true);                 break;
            case BARCLAYS_ISA:          rbDatabaseISA.setSelected(true);                    break;
        }
        
        switch(iTable)
        {
            case DATABASE_TABLE_ENTRY:              rbDatabaseTableEntries.setSelected(true);               break;
            case DATABASE_TABLE_CATEGORY:           rbDatabaseTableCategories.setSelected(true);            break;
            case DATABASE_TABLE_AUTO_CATEGORY:      rbDatabaseTableAutoCategories.setSelected(true);        break;
            case DATABASE_TABLE_WATCH:              rbDatabaseTableWatches.setSelected(true);               break;
            case DATABASE_TABLE_REFUND:             rbDatabaseTableRefunds.setSelected(true);               break;
            case DATABASE_TABLE_REMINDER:           iSelectedTable = DATABASE_TABLE_REMINDER;               break;
        }
        
        if(iTable == DATABASE_TABLE_REMINDER)
            showTable();
    }
    
    //Import
    public static void showDialogImportFromFile()
    {
        File fileImport = openImportFileChooser();
        
        if(fileImport != null)
        {
            setMode(MODE_IMPORT);
            obImporter.assess(fileImport, aSelected);
        }
    }
    public static void importFromFile()
    {
        obImporter.run();
    }
    public static void closeImportFromFile()
    {
        obImporter.cancel();
        clearAndReset();
    }
    
    //Export
    public static void showDialogExportToFile()
    {
        File fileExport = openExportFileChooser();
        
        if(fileExport != null)
        {
            setMode(MODE_EXPORT);
            obExporter.assess(fileExport, aSelected);
        }
    }
    public static void exportToFile()
    {
        obExporter.run();
    }
    public static void closeExportToFile()
    {
        obExporter.cancel();
        clearAndReset();
    }
    
    //Entry Filter
    public static void findViewDate()
    {
        Entry e = tbEntries.getSelectedItem();

        if(e !=  null)
            dpDatabaseViewDate.setValue(Formatter.convert(e.getDate()));
    }
    public static void viewDate()
    {
        setTargetDate(Formatter.convert(dpDatabaseViewDate.getValue()));
        clearTable(iSelectedTable);
        showTable();
    }
    public static void viewAll()
    {
        setTargetDate(NOT_DEFINED);
        clearTable(iSelectedTable);
        showTable();
    }
    
    //Record Delete
    public static void delete()
    {
        if(getTable(iSelectedTable).areAllItemsSelected())
        {
            deleteAll();
            return;
        }
        
        switch(iSelectedTable)
        {
            case DATABASE_TABLE_ENTRY:          deleteEntries();            break;
            case DATABASE_TABLE_CATEGORY:       deleteCategories();         break;
            case DATABASE_TABLE_AUTO_CATEGORY:  deleteAutoCategories();     break;
            case DATABASE_TABLE_WATCH:          deleteWatches();            break;
            case DATABASE_TABLE_REFUND:         deleteRefunds();            break;
            case DATABASE_TABLE_REMINDER:       deleteReminders();          break;
        }

        checkAddBlankToEmptyTable(iSelectedTable, true);
        refreshStatusMessage();
        refreshStatusRange();
    }
    public static void deleteAll()
    {
        boolean bDatabaseDeletedAll = false;
        
        switch(iSelectedTable)
        {
            case DATABASE_TABLE_ENTRY:          bDatabaseDeletedAll = Database.deleteAllEntries(aSelected);             break;
            case DATABASE_TABLE_CATEGORY:       bDatabaseDeletedAll = Database.deleteAllCategories(aSelected);          break;
            case DATABASE_TABLE_AUTO_CATEGORY:  bDatabaseDeletedAll = Database.deleteAllAutoCategories(aSelected);      break;
            case DATABASE_TABLE_WATCH:          bDatabaseDeletedAll = Database.deleteAllWatches(aSelected);             break;
            case DATABASE_TABLE_REFUND:         bDatabaseDeletedAll = Database.deleteAllRefunds(aSelected);             break;
            case DATABASE_TABLE_REMINDER:       bDatabaseDeletedAll = Database.deleteAllReminders();                    break;
        }
        
        if(bDatabaseDeletedAll)
            showEmptyTable(iSelectedTable);
        
        checkAddBlankToEmptyTable(iSelectedTable, true);
        refreshStatusMessage();
        refreshStatusRange();
    }
    public static void resetAccount()
    {
        if(Database.reset(aSelected))
        {
            clearAndReset();
            setStatusMessage("Account successfully reset, all data and tables cleared");
            setStatusRange("");
        }
    }
    public static void resetAll()
    {
        if(Database.resetAll())
        {
            clearAndReset();
            setStatusMessage("Database successfully reset, all data and tables cleared");
            setStatusRange("");
        }
    }
    
    //State
    public static boolean hasItemsSelected()
    {
        return !getTable(iSelectedTable).hasSelected();
    }
    public static boolean isSelectedTableEmpty()
    {
        return getTable(iSelectedTable).isEmpty();
    }
}
