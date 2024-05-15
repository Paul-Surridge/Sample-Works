package Database.Administration.ImportExport;

import Accounts.Account;
import Administration.Windows;
import AutoCategories.AutoCategory;
import Categories.Category;
import Database.Database;
import History.Entry;
import Refunds.Refund;
import Shared.Constants;
import Shared.ErrorTableInsertable;
import Shared.Error;
import Shared.Formatter;
import Shared.TableViewFactory;
import Watches.Watch;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class Exporter implements Constants{
    
    private SplitPane spDatabaseTables;
    private AnchorPane apDatabaseTable;
    private Button btDatabaseExportConfirm;
    
    private List<Item> lstItems = new ArrayList<>();
    private List<Error> lstErrors = new ArrayList<>();
    
    private TableView<DatabaseAdministrationExportTableInsertable> tvExport;
    private TableView<ErrorTableInsertable> tvErrors;
    
    private Windows obWindows;
    private File fileExport;
    private Account aExport;
    private int iLineCounter, iErrorCounter;
    
    public Exporter(SplitPane spDatabaseTables, AnchorPane apDatabaseTable, Button btDatabaseExportConfirm)
    {
        this.spDatabaseTables = spDatabaseTables;
        this.apDatabaseTable = apDatabaseTable;
        this.btDatabaseExportConfirm = btDatabaseExportConfirm;
        
        obWindows = new Windows(spDatabaseTables);
        
        initTableViews();
        initEventHandlers();
    }
    
    //Internal -----------------------------------------------------------------
    private void initTableViews()
    {
        tvExport = TableViewFactory.buildTable(TABLEVIEW_ID_DATABASE_EXPORT);
        tvErrors = TableViewFactory.buildTable(TABLEVIEW_ID_DATABASE_IMPORT_EXPORT_ERRROR);
    }
    private void initEventHandlers()
    {
        toggleWindow(tvExport, 0);
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
    public void clearAndReset()
    {
        iLineCounter = 1;
        iErrorCounter = 1;
        
        lstItems.clear();
        lstErrors.clear();
        
        tvExport.getItems().clear();
        tvErrors.getItems().clear();
        
        apDatabaseTable.getChildren().clear();
        
        setConfirmButtonEnable(true);
    }
    private void setConfirmButtonEnable(boolean bState)
    {
        btDatabaseExportConfirm.setDisable(!bState);
    }
    
    //Status/Error
    private void addError(String sMessage, String sDetails)
    {
        lstErrors.add(new Error(iErrorCounter++, sMessage, sDetails));
    }
    private void addStatus(String sItem, String sStatus)
    {
        tvExport.getItems().add(new Item(sItem, sStatus));
    }
    private void updateLastStatus(String sItem, String sStatus)
    {
        tvExport.getItems().remove(tvExport.getItems().size()-1);
        tvExport.getItems().add(new Item(sItem, sStatus));
    }
    
    //File Writing
    private String getTab()
    {
        return "\t";
    }
    private void writeLine(BufferedWriter obBufferedWriter, String sLine)
    {
        try
        {
            obBufferedWriter.write(sLine, 0, sLine.length());
            obBufferedWriter.newLine();
        }
        catch (IOException ex)
        {
            addError("File write", "Unable to write line: " + sLine);
        }
    }
    private void writeLineWithCounter(BufferedWriter obBufferedWriter, String sLine)
    {
        writeLine(obBufferedWriter, iLineCounter + "." + getTab() + sLine);

        iLineCounter++;
    }
    private void writeEntries(BufferedWriter obBufferedWriter, Account a)
    {
        String sIn, sOut;
        
        writeLine(obBufferedWriter, MARKER_ENTRIES);
        
        for(Entry e : Database.getEntries(a, NOT_DEFINED))
        {
            sIn = e.getIn() == NOT_DEFINED ? "-" : String.valueOf(e.getIn());
            sOut = e.getOut() == NOT_DEFINED ? "-" : String.valueOf(e.getOut());
            
            writeLineWithCounter(obBufferedWriter, e.getDate() + getTab() + e.getName() + getTab() + e.getType() + getTab() + e.getCategoryName() + getTab() + sIn + getTab() + sOut + getTab() + e.getBalance());
        }
    }
    private void writeCategories(BufferedWriter obBufferedWriter, Account a)
    {
        writeLine(obBufferedWriter, MARKER_CATGEGORIES);
        
        for(Category c : Database.getCategories(a))
            writeLineWithCounter(obBufferedWriter, c.getName() + getTab() + c.getDirectionText());
    }
    private void writeAutoCategories(BufferedWriter obBufferedWriter, Account a)
    {
        writeLine(obBufferedWriter, MARKER_AUTO_CATEGORIES);
        
        for(AutoCategory ac : Database.getAutoCategories(a))
            writeLineWithCounter(obBufferedWriter, ac.getEntryName() + getTab() + ac.getDirectionText() + getTab() + ac.getCategoryName());
    }
    private void writeWatches(BufferedWriter obBufferedWriter, Account a)
    {
        writeLine(obBufferedWriter, MARKER_WATCHES);
        
        for(Watch w : Database.getWatches(a))
            writeLineWithCounter(obBufferedWriter,  w.getCategoryName() + getTab() +
                                                    w.getDirectionText() + getTab() +
                                                   (w.getLastDate() == NOT_DEFINED ? DASH : w.getLastDate()) + getTab() +
                                                   (w.getLastAmount()== NOT_DEFINED ? DASH : w.getLastAmount()));
    }
    private void writeRefunds(BufferedWriter obBufferedWriter, Account a)
    {
        writeLine(obBufferedWriter, MARKER_REFUNDS);
        
        for(Refund r : Database.getRefunds(a))
            writeLineWithCounter(obBufferedWriter,  r.getDate() + getTab() +
                                                    r.getName() + getTab() +
                                                    r.getKeywordsSerialised() + getTab() +
                                                    r.getAmount() + getTab() +
                                                   (r.getReceivedDate() == NOT_DEFINED ? DASH : r.getReceivedDate()) + getTab() +
                                                   (r.getReceivedPrimaryKey() == NOT_DEFINED ? DASH : r.getReceivedPrimaryKey()));
    }
    private void export()
    {
        try (BufferedWriter obBufferedWriter = new BufferedWriter(new FileWriter(fileExport)))
        {
            addStatus("", "");

            writeLine(obBufferedWriter, aExport.getName());
            writeLine(obBufferedWriter, "");

            addStatus(MARKER_ENTRIES, "Exporting...");
            writeEntries(obBufferedWriter, aExport);
            updateLastStatus(MARKER_ENTRIES, "Exported");
            
            writeLine(obBufferedWriter, "");

            addStatus(MARKER_CATGEGORIES, "Exporting...");
            writeCategories(obBufferedWriter, aExport);
            updateLastStatus(MARKER_CATGEGORIES, "Exported");

            writeLine(obBufferedWriter, "");

            addStatus(MARKER_AUTO_CATEGORIES, "Exporting...");
            writeAutoCategories(obBufferedWriter, aExport);
            updateLastStatus(MARKER_AUTO_CATEGORIES, "Exported");

            writeLine(obBufferedWriter, "");

            addStatus(MARKER_WATCHES, "Exporting...");
            writeWatches(obBufferedWriter, aExport);
            updateLastStatus(MARKER_WATCHES, "Exported");
            
            writeLine(obBufferedWriter, "");

            addStatus(MARKER_REFUNDS, "Exporting...");
            writeRefunds(obBufferedWriter, aExport);
            updateLastStatus(MARKER_REFUNDS, "Exported");
            
            addStatus("", "");
            addStatus("Export Complete", "");
        }
        catch (IOException ex)
        {
            addError("File write", "Unable to open file, create internal buffered writer or write line");
        }
    }
    
    //Show Tables
    private void showExportTable()
    {
        long lNumberOfEntries = Database.getNumberOfEntries(aExport);
        
        lstItems.add(new Item("", ""));
        lstItems.add(new Item("Account", aExport.getName()));
        lstItems.add(new Item("Filepath", fileExport.getPath()));
        lstItems.add(new Item("Filename", fileExport.getName()));
        lstItems.add(new Item("", ""));
        lstItems.add(new Item(MARKER_ENTRIES, lNumberOfEntries));
        
        if(lNumberOfEntries>0)
            lstItems.add(new Item("Dates", Formatter.convert(DATE, DATABASE, TABLEVIEW, Database.getEntryDate(aExport, EARLIEST)) + " - " + Formatter.convert(DATE, DATABASE, TABLEVIEW, Database.getEntryDate(aExport, LATEST))));
        
        lstItems.add(new Item(MARKER_CATGEGORIES, Database.getNumberOfCategories(aExport)));
        lstItems.add(new Item(MARKER_AUTO_CATEGORIES, Database.getNumberOfAutoCategories(aExport)));
        lstItems.add(new Item(MARKER_WATCHES, Database.getNumberOfWatches(aExport)));
        lstItems.add(new Item(MARKER_REFUNDS, Database.getNumberOfRefunds(aExport)));
        lstItems.add(new Item("", ""));
        lstItems.add(new Item("Press Confirm to export.", ""));

        tvExport.getItems().addAll(lstItems);
        apDatabaseTable.getChildren().add(tvExport);
    }
    private void showErrorTable()
    {
        tvErrors.getItems().addAll(lstErrors);
        
        spDatabaseTables.getItems().add(new AnchorPane(tvErrors));
        spDatabaseTables.getDividers().get(0).setPosition(0.555);
    }
    
    //External -----------------------------------------------------------------
    public void assess(File f, Account a)
    {
        clearAndReset();
        
        fileExport = f;
        aExport = a;
        
        showExportTable();
    }
    public void run()
    {
        setConfirmButtonEnable(false);

        export();
        
        if(!lstErrors.isEmpty())
            showErrorTable();
    }
    public void cancel()
    {
        if(spDatabaseTables.getItems().size()>1)
            spDatabaseTables.getItems().remove(1);
    }
}
