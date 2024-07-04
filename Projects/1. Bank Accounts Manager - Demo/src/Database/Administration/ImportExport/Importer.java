package Database.Administration.ImportExport;

import Accounts.Account;
import Administration.Windows;
import AutoCategories.AutoCategory;
import Categories.Category;
import Database.Database;
import History.Entry;
import Integrity.Integrity;
import Refunds.Refund;
import Shared.Constants;
import Shared.ErrorTableInsertable;
import Shared.Error;
import Shared.TableViewFactory;
import Watches.Watch;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class Importer implements Constants{
    
    private SplitPane spDatabaseTables;
    private AnchorPane apDatabaseTable;
    private Button btDatabaseImportConfirm;
    
    private List<Entry> lstEntries = new ArrayList<>();
    private List<Category> lstCategories = new ArrayList<>();
    private List<AutoCategory> lstAutoCategories = new ArrayList<>();
    private List<Watch> lstWatches = new ArrayList<>();
    private List<Refund> lstRefunds = new ArrayList<>();
    
    private List<Item> lstItems = new ArrayList<>();
    private List<Error> lstErrors = new ArrayList<>();
    
    private TableView<DatabaseAdministrationImportTableInsertable> tvImport;
    private TableView<ErrorTableInsertable> tvErrors;
    
    private Windows obWindows;
    private Account aImport;
    private int iErrorCounter;
    
    public Importer(SplitPane spDatabaseTables, AnchorPane apDatabaseTable, Button btDatabaseImportConfirm)
    {
        this.spDatabaseTables = spDatabaseTables;
        this.apDatabaseTable = apDatabaseTable;
        this.btDatabaseImportConfirm = btDatabaseImportConfirm;
        
        obWindows = new Windows(spDatabaseTables);
        
        initTableViews();
        initEventHandlers();
    }
    
    //Internal -----------------------------------------------------------------
    private class DatabaseImportRunningStatus implements Runnable
    {
        @Override
        public void run()
        {
            addStatus("", "");
            addStatus("Database Tables", "Clearing...");

            if(Database.reset(aImport))                             updateLastStatus("Database Tables", "Cleared");
            else                                                    updateLastStatus("Database Tables", "Error unable to clear tables");

            addStatus("Entries", "Importing...");

            if(Database.addEntries(lstEntries))                     updateLastStatus("Entries", "Imported");
            else                                                    updateLastStatus("Entries", "Error unable to import entries into database");

            addStatus("Categories", "Importing...");

            if(Database.addCategories(lstCategories))               updateLastStatus("Categories", "Imported");
            else                                                    updateLastStatus("Categories", "Error unable to import categories into database");

            addStatus("Auto Categories", "Importing...");

            if(Database.addAutoCategories(lstAutoCategories))       updateLastStatus("Auto Categories", "Imported");
            else                                                    updateLastStatus("Auto Categories", "Error unable to import auto categories into database");

            addStatus("Watches", "Importing...");

            if(Database.addWatches(lstWatches))                     updateLastStatus("Watches", "Imported");
            else                                                    updateLastStatus("Watches", "Error unable to import watches into database");
            
            addStatus("Refunds", "Importing...");

            if(Database.addRefunds(lstRefunds))                     updateLastStatus("Refunds", "Imported");
            else                                                    updateLastStatus("Refunds", "Error unable to import refunds into database");

            addStatus("", "");
            addStatus("Import Complete", "Please restart program");
            addStatus("", "");
        }
    }
    
    private void initTableViews()
    {
        tvImport = TableViewFactory.buildTable(TABLEVIEW_ID_DATABASE_IMPORT);
        tvErrors = TableViewFactory.buildTable(TABLEVIEW_ID_DATABASE_IMPORT_EXPORT_ERRROR);
    }
    private void initEventHandlers()
    {
        toggleWindow(tvImport, 0);
        toggleWindow(tvErrors, 1);
    }
    private void toggleWindow(TableView tv, int iWindow)
    {
        tv.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
            
            switch(me.getButton())
            {
                case PRIMARY:
                {
                    if(me.getClickCount() == 2)
                        obWindows.toggleWindow(iWindow);
                    break;
                }
            }
        });
    }
    
    //UI/Buttons
    private void clearAndReset()
    {
        iErrorCounter = 1;
        
        lstEntries.clear();
        lstCategories.clear();
        lstAutoCategories.clear();
        lstWatches.clear();
        lstRefunds.clear();
        lstItems.clear();
        lstErrors.clear();
        
        apDatabaseTable.getChildren().clear();
        
        tvImport.getItems().clear();
        tvErrors.getItems().clear();
        
        setConfirmButtonEnable(true);
    }
    private void setConfirmButtonEnable(boolean bState)
    {
        btDatabaseImportConfirm.setDisable(!bState);
    }
    
    //Status/Error
    private void addError(String sMessage, String sDetails)
    {
        lstErrors.add(new Error(iErrorCounter++, sMessage, sDetails));
    }
    private void addStatus(String sItem, String sStatus)
    {
        tvImport.getItems().add(new Item(sItem, sStatus));
    }
    private void updateLastStatus(String sItem, String sStatus)
    {
        tvImport.getItems().remove(tvImport.getItems().size()-1);
        tvImport.getItems().add(new Item(sItem, sStatus));
    }
    
    //Read File
    private void checkImportForBalanceInconsistencies()
    {
        String sBalanceInconsistency;
        
        for(int i = 1 ; i<lstEntries.size() ; i++)
        {
            sBalanceInconsistency = Integrity.checkBalanceInconsistency(lstEntries.get(i-1), lstEntries.get(i), false);

            if(!sBalanceInconsistency.isEmpty())
                addError("Balance inconsistency of " + sBalanceInconsistency, lstEntries.get(i).getLine());
        }
    }
    private boolean checkMarkers(File fileImport)
    {
        String sLine = "";
        boolean bMarkerEntries = false;
        boolean bMarkerCategories = false;
        boolean bMarkerAutoCategories = false;
        boolean bMarkerWatches = false;
        boolean bMarkerRefunds = false;

        try(BufferedReader obBufferedReader = new BufferedReader(new FileReader(fileImport)))
        {
            while ((sLine = obBufferedReader.readLine()) != null)
            {
                if(sLine.isEmpty())
                    continue;

                switch(sLine)
                {
                    case MARKER_ENTRIES:            bMarkerEntries = true;          break;
                    case MARKER_CATGEGORIES:        bMarkerCategories = true;       break;
                    case MARKER_AUTO_CATEGORIES:    bMarkerAutoCategories = true;   break;
                    case MARKER_WATCHES:            bMarkerWatches = true;          break;
                    case MARKER_REFUNDS:            bMarkerRefunds = true;          break;
                }
            }
            
            if(!bMarkerEntries)         addError("File read", "Unable to find Entries marker");
            if(!bMarkerCategories)      addError("File read", "Unable to find Categories marker");
            if(!bMarkerAutoCategories)  addError("File read", "Unable to find Auto Categories marker");
            if(!bMarkerWatches)         addError("File read", "Unable to find Watches marker");
            if(!bMarkerRefunds)         addError("File read", "Unable to find Refunds marker");
        }
        catch (IOException ex)
        {
            addError("File read", "Unable to open file, create internal buffered reader or read line: " + sLine);
        }
        
        return lstErrors.isEmpty();
    }
    private void processLine(String sLine, int iType)
    {
        Line l = new Line(iType, sLine);

        if(l.hasError())
            for(String s : l.getErrors())
                addError(s, sLine);
        else
            switch(iType)
            {
                case DATABASE_IMPORT_TYPE_ENTRY:            lstEntries.add(new Entry(aImport.getAccountID(), l.getDate(), l.getEntryName(), l.getType(), l.getCategoryName(), l.getIn(), l.getOut(), l.getBalance(), sLine));       break;
                case DATABASE_IMPORT_TYPE_CATEGORY:         lstCategories.add(new Category(aImport.getAccountID(), l.getCategoryName(), l.getDirection()));                                                                         break;
                case DATABASE_IMPORT_TYPE_AUTO_CATEGORY:    lstAutoCategories.add(new AutoCategory(aImport.getAccountID(), l.getEntryName(), l.getDirectionText(), l.getCategoryName()));                                           break;
                case DATABASE_IMPORT_TYPE_WATCH:            lstWatches.add(new Watch(aImport.getAccountID(), l.getCategoryName(), l.getDirectionText(), l.getLastDate(), l.getLastAmount()));                                       break;
                case DATABASE_IMPORT_TYPE_REFUND:           lstRefunds.add(new Refund(aImport.getAccountID(), l.getDate(), l.getRefundName(), l.getKeywords(), l.getAmount(), l.getReceivedDate(), l.getReceivedPrimaryKey()));     break;
            }
    }
    
    //Show Tables
    private String getDateRange()
    {
        return "(" + lstEntries.get(0).getDateTextForTableView() + " - " + lstEntries.get(lstEntries.size()-1).getDateTextForTableView() + ")";
    }
    private void showImportTable(File f)
    {
        lstItems.add(new Item("", ""));
        lstItems.add(new Item("Account", aImport.getName()));
        lstItems.add(new Item("Filepath", f.getPath()));
        lstItems.add(new Item("Filename", f.getName()));
        lstItems.add(new Item("", ""));
        lstItems.add(new Item("Entries", lstEntries.size() + "        " + (!lstEntries.isEmpty() ? getDateRange() : "")));
        lstItems.add(new Item("Categories", lstCategories.size()));
        lstItems.add(new Item("Auto Categories", lstAutoCategories.size()));
        lstItems.add(new Item("Watches", lstWatches.size()));
        lstItems.add(new Item("Refunds", lstRefunds.size()));
        lstItems.add(new Item("", ""));
        lstItems.add(new Item("Import Errors", lstErrors.size()));
        lstItems.add(new Item("", ""));
        
        if(lstErrors.isEmpty())     lstItems.add(new Item("Press Confirm to import.", "Importing will overwrite all existing records of account and replace with the records contained in file."));
        else                        lstItems.add(new Item("Errors have been found, please review and amend errors before importing.", ""));

        tvImport.getItems().addAll(lstItems);
        apDatabaseTable.getChildren().add(tvImport);
    }
    private void showErrorTable()
    {
        tvErrors.getItems().addAll(lstErrors);
        
        spDatabaseTables.getItems().add(new AnchorPane(tvErrors));
        spDatabaseTables.getDividers().get(0).setPosition(0.555);
    }
    
    //External -----------------------------------------------------------------
    public void assess(File fileImport, Account a)
    {
        String sLine = "";
        int iType = NOT_DEFINED;
        
        clearAndReset();
        
        aImport = a;
        
        if(checkMarkers(fileImport))
        {
            try(BufferedReader obBufferedReader = new BufferedReader(new FileReader(fileImport)))
            {
                while ((sLine = obBufferedReader.readLine()) != null)
                {
                    if(sLine.isEmpty())
                        continue;

                    switch(sLine)
                    {
                        case MARKER_ENTRIES:            iType = DATABASE_IMPORT_TYPE_ENTRY;         break;
                        case MARKER_CATGEGORIES:        iType = DATABASE_IMPORT_TYPE_CATEGORY;      break;
                        case MARKER_AUTO_CATEGORIES:    iType = DATABASE_IMPORT_TYPE_AUTO_CATEGORY; break;
                        case MARKER_WATCHES:            iType = DATABASE_IMPORT_TYPE_WATCH;         break;
                        case MARKER_REFUNDS:            iType = DATABASE_IMPORT_TYPE_REFUND;        break;
                        default:                        processLine(sLine, iType);                  break;
                    }
                }

                if(lstErrors.isEmpty())
                    checkImportForBalanceInconsistencies();
            }
            catch (IOException ex)
            {
                addError("File read", "Unable to open file, create internal buffered reader or read line: " + sLine);
            }
        }
        
        showImportTable(fileImport);
        
        if(!lstErrors.isEmpty())
        {
            setConfirmButtonEnable(false);
            showErrorTable();
        }
    }
    public void run()
    {
        setConfirmButtonEnable(false);
        
        Thread t = new Thread(new DatabaseImportRunningStatus());
        
        //Separate thread for the realtime updating of the tableview
        t.start();
    }
    public void cancel()
    {
        if(spDatabaseTables.getItems().size()>1)
            spDatabaseTables.getItems().remove(1);
        
        clearAndReset();
    }
}
