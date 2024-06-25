package Statement;

import Shared.Error;
import Accounts.Account;
import History.Entry;
import Database.Database;
import Accounts.Accounts;
import Administration.Administration;
import Administration.Windows;
import AutoCategories.AutoCategory;
import Categories.Category;
import DateRange.DateRange;
import Integrity.Integrity;
import Shared.Blank;
import Shared.Constants;
import Shared.ContextMenuFactory;
import Shared.ErrorTableInsertable;
import Shared.Formatter;
import Shared.Table;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;

public class Statement implements Constants{
    
    private static RadioButton  rbStatementCurrent, rbStatementSaving, rbStatementISA;
    private static RadioButton  rbStatementEntriesCategorised, rbStatementEntriesUndefined, rbStatementEntriesAll;
    private static Button       btStatementOpenFile, btStatementAdd;
    private static Label        labStatementStatusMessage;
    
    private static ToggleGroup  tgAccounts = new ToggleGroup();
    private static ToggleGroup  tgEntries = new ToggleGroup();
    
    private static List<Entry> lstEntries = new ArrayList<>();
    private static List<Line> lstLines = new ArrayList<>();
    private static List<Error> lstErrors = new ArrayList<>();
    
    private static Table<StatementTableInsertable, Entry> tbStatement;
    private static Table<ErrorTableInsertable, Error> tbErrors;
    
    private static Windows obWindows;
    private static Account aSelected;
    private static int iNumberOfEntriesCategorised, iNumberOfEntriesUndefined, iErrorCounter;
    
    public Statement(SplitPane spStatementTables, AnchorPane apStatementTable,
                    RadioButton rbStatementCurrent, RadioButton rbStatementSaving, RadioButton rbStatementISA,
                    RadioButton rbStatementEntriesCategorised, RadioButton rbStatementEntriesUndefined, RadioButton rbStatementEntriesAll,
                    Button btStatementOpenFile, Button btStatementAdd, Label labStatementStatusMessage)
    {
        this.rbStatementCurrent                     = rbStatementCurrent;
        this.rbStatementSaving                      = rbStatementSaving;
        this.rbStatementISA                         = rbStatementISA;
        this.rbStatementEntriesCategorised          = rbStatementEntriesCategorised;
        this.rbStatementEntriesUndefined            = rbStatementEntriesUndefined;
        this.rbStatementEntriesAll                  = rbStatementEntriesAll;
        this.btStatementOpenFile                    = btStatementOpenFile;
        this.btStatementAdd                         = btStatementAdd;
        this.labStatementStatusMessage              = labStatementStatusMessage;
        
        obWindows = new Windows(spStatementTables, 0);
        
        initTables(apStatementTable);
        initRadioButtons();
        initEventHandlers();
        initEventListeners();
        
        clearAndReset();
    }
    
    //Internal -----------------------------------------------------------------
    private static void initTables(AnchorPane ap)
    {
        tbStatement = new Table(TABLEVIEW_ID_STATEMENT, Entry.class, ContextMenuFactory::buildContextMenuForStatement);
        tbErrors    = new Table(TABLEVIEW_ID_STATEMENT_ERRORS, Error.class);
        
        ap.getChildren().add(tbStatement.getTableView());
    }
    private static void initRadioButtons()
    {
        rbStatementCurrent.setToggleGroup(tgAccounts);
        rbStatementSaving.setToggleGroup(tgAccounts);
        rbStatementISA.setToggleGroup(tgAccounts);
        
        rbStatementEntriesCategorised.setToggleGroup(tgEntries);
        rbStatementEntriesUndefined.setToggleGroup(tgEntries);
        rbStatementEntriesAll.setToggleGroup(tgEntries);

        rbStatementEntriesAll.setSelected(true);
    }
    private static void initEventHandlers()
    {
        toggleWindow(tbStatement, 0);
        toggleWindow(tbErrors, 1);
    }
    private static void initEventListeners()
    {
        tgAccounts.selectedToggleProperty().addListener(new ChangeListener()
        {
            @Override
            public void changed(ObservableValue o, Object oldVal, Object newVal)
            {
                if      (tgAccounts.getSelectedToggle() == rbStatementCurrent)          aSelected = Accounts.get(BARCLAYS_CURRENT);
                else if (tgAccounts.getSelectedToggle() == rbStatementSaving)           aSelected = Accounts.get(BARCLAYS_SAVING);
                else if (tgAccounts.getSelectedToggle() == rbStatementISA)              aSelected = Accounts.get(BARCLAYS_ISA);
                
                setOpenButtonEnable(true);
            }
        });
        
        tgEntries.selectedToggleProperty().addListener(new ChangeListener()
        {
            @Override
            public void changed(ObservableValue o, Object oldVal, Object newVal)
            {
                if(!lstEntries.isEmpty())
                    if      (tgEntries.getSelectedToggle() == rbStatementEntriesCategorised)       {if(!rbStatementEntriesCategorised.isDisable())   showStatement(AUTO_CATEGORISED);}
                    else if (tgEntries.getSelectedToggle() == rbStatementEntriesUndefined)         {if(!rbStatementEntriesUndefined.isDisable())     showStatement(UNDEFINED);}
                    else if (tgEntries.getSelectedToggle() == rbStatementEntriesAll)               {if(!rbStatementEntriesAll.isDisable())           showStatement(ALL);}
            }
        });
    }
    private static void toggleWindow(Table tb, int iWindow)
    {
        tb.getTableView().addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
            if(me.getButton() == MouseButton.PRIMARY && me.getClickCount() == 2)
            {
                clearAllSelectItems();
                obWindows.toggleWindow(iWindow);
            }
        });
    }
    private static void clearAllSelectItems()
    {
        tbStatement.clearSelection();
        tbErrors.clearSelection();
    }
    
    //Buttons/UI controls
    private static void clearAccountsRadioButtons()
    {
        for(Toggle t : tgAccounts.getToggles())
            t.setSelected(false);
        
        aSelected = null;
    }
    private static void setAccountsRadioButtonsEnable(boolean bState)
    {
        rbStatementCurrent.setDisable(!bState);
        rbStatementSaving.setDisable(!bState);
        rbStatementISA.setDisable(!bState);
    }
    private static void setEntriesRadioButtonsEnable(boolean bState)
    {
        rbStatementEntriesCategorised.setDisable(!bState);
        rbStatementEntriesUndefined.setDisable(!bState);
        rbStatementEntriesAll.setDisable(!bState);
        
        rbStatementEntriesAll.setSelected(true);
    }
    private static void refreshEntriesRadioButtonLabels()
    {
        findEntriesRadioButtonTotals();
        
        rbStatementEntriesCategorised.setText(CATEGORISED_TEXT + " (" + iNumberOfEntriesCategorised + ")");
        rbStatementEntriesUndefined.setText(UNDEFINED_TEXT + " (" + iNumberOfEntriesUndefined + ")");
        rbStatementEntriesAll.setText(ALL_TEXT + " (" + lstEntries.size() + ")");
    }
    private static void setOpenButtonEnable(boolean bState)
    {
        btStatementOpenFile.setDisable(!bState);
    }
    private static void setAddButtonEnable(boolean bState)
    {
        btStatementAdd.setDisable(!bState);
    }
    private static void clearAndReset()
    {
        iErrorCounter = 1;
        labStatementStatusMessage.setText("");
        
        lstLines.clear();
        lstEntries.clear();
        lstErrors.clear();
        
        tbStatement.setItem(new Blank());
        tbErrors.setItem(new Blank());
        
        resetEntriesRadioButtonsLabel();
        setAccountsRadioButtonsEnable(true);
        setEntriesRadioButtonsEnable(false);
        setAddButtonEnable(false);
        
        obWindows.removeAllWindows();
    }
    private static void findEntriesRadioButtonTotals()
    {
        iNumberOfEntriesCategorised = 0;
        iNumberOfEntriesUndefined = 0;
        
        for(Entry e : lstEntries)
            switch(e.getCategoryName())
            {
                case UNDEFINED_TEXT:    iNumberOfEntriesUndefined++;        break;
                default:                iNumberOfEntriesCategorised++;      break;
            }
    }
    private static void resetEntriesRadioButtonsLabel()
    {
        rbStatementEntriesCategorised.setText(CATEGORISED_TEXT);
        rbStatementEntriesUndefined.setText(UNDEFINED_TEXT);
        rbStatementEntriesAll.setText(ALL_TEXT);
    }

    //File Validity
    private static void addError(String sError, String sDetails)
    {
        lstErrors.add(new Error(iErrorCounter++, sError, sDetails));
    }
    private static void addError(String sError, Entry e)
    {
        lstErrors.add(new Error(iErrorCounter++, sError, e.getDateTextForTableView() + " " + e.getType() + " " + e.getName() + " " + e.getOutText() + " " + e.getInText() + " " + e.getBalanceText()));
    }
    private static void checkForBalanceInconsistencies()
    {
        String sBalanceInconsistency;
          
        for(int i = 1 ; i<lstEntries.size() ; i++)
        {
            sBalanceInconsistency = Integrity.checkBalanceInconsistency(lstEntries.get(i-1), lstEntries.get(i), false);

            if(!sBalanceInconsistency.isEmpty())
                addError("Balance inconsistency of " + sBalanceInconsistency, lstEntries.get(i));
        }
    }
    private static void checkForDuplicatesInDatabase()
    {
        for(Entry e : lstEntries)
            if(Database.doesEntryExist(e))
                addError("Entry already exists in database", e);
    }
    private static void checkForNonSequentialDates()
    {
        Entry e, ePrevious;
        int iDateDifference;
        
        for(int i = 1 ; i<lstEntries.size() ; i++)
        {
            ePrevious = lstEntries.get(i-1);
            e = lstEntries.get(i);
            
            iDateDifference = e.getDate() - ePrevious.getDate();
            
            if(iDateDifference < 0)
                addError("Non-sequential dates with " + Formatter.convert(DATE, DATABASE, TABLEVIEW, ePrevious.getDate()), e);
        }
    }
    private static boolean runErrorCheck()
    {
        checkForBalanceInconsistencies();
        checkForDuplicatesInDatabase();
        checkForNonSequentialDates();
        
        return lstErrors.isEmpty();
    }
    
    //File Read
    private static boolean checkInterfaceWithDatabaseDate()
    {
        Entry eDB;
        Line l;
        
        if(Database.hasEntries(aSelected))
        {
            eDB = Database.getEntryAtPosition(aSelected, LATEST);
            l = lstLines.get(0);
            
            if(l.getDate().isEmpty())
                addError("First entry in file has no date", "");
            else
            {
                int iDateDifference = Formatter.convert(DATE, STATEMENT, DATABASE, l.getDate()) - eDB.getDate();

                if      (iDateDifference == 0)      addError("First entry in file has same date as last entry in database", "");
                else if (iDateDifference < 0)       addError("First entry in file is on-sequential with last entry in database which is " + Formatter.convert(DATE, DATABASE, TABLEVIEW, eDB.getDate()), "");
            }
        }
        
        return lstErrors.isEmpty();
    }
    private static boolean checkInterfaceWithDatabaseBalance()
    {
        int iBalance, iDifference;
        
        if(Database.hasEntries(aSelected))
        {
            iBalance = Database.getEntryAtPosition(aSelected, LATEST).getBalance();
            
            for(Line l : lstLines)
            {
                switch(l.getDirection())
                {
                    case IN:    iBalance += l.getIn();       break;
                    case OUT:   iBalance -= l.getOut();      break;
                }
                
                if(l.getBalance() != NOT_DEFINED)
                {
                    if(iBalance == l.getBalance())
                        return true;
                    
                    iDifference = l.getBalance() - iBalance;

                    if(iDifference == -1)   addError("Balance inconsistent with last entry in database", "Balance in file is -0.01 than expected");
                    else                    addError("Balance inconsistent with last entry in database", "Balance in file is " + Formatter.convert(CURRENCY, DATABASE, TABLEVIEW, iDifference) + " than expected");

                    return false;
                }
            }
            
            addError("Balance inconsistent with last entry in database", "No entry in file has a balance defined");
            
            return false;
        }
        
        return true;
    }
    private static int findFirstLineBalance()
    {
        int iIndex = 0;
        int iBalance = 0;
        
        for(int i = 0 ; i<lstLines.size() ; i++)
            if(lstLines.get(i).getBalance() != NOT_DEFINED)
            {
                iIndex = i;
                iBalance = lstLines.get(i).getBalance();
                
                break;
            }
        
        for(int i = iIndex ; i>=0 ; i--)
        {
            Line l = lstLines.get(i);

            switch(l.getDirection())
            {
                case IN:    iBalance -= l.getIn();      break;
                case OUT:   iBalance += l.getOut();     break;
            }
        }
        
        return iBalance;
    }
    private static void populateEmptyDatesAndBalances()
    {
        Line l = lstLines.get(0);
                
        if(l.getBalance() == NOT_DEFINED)
        {
            int iBalance;
            
            if(!Database.hasEntries(aSelected))     iBalance = findFirstLineBalance();
            else                                    iBalance = Database.getEntryAtPosition(aSelected, LATEST).getBalance();

            switch(l.getDirection())
            {
                case IN:    l.setBalance(iBalance + l.getIn());     break;
                case OUT:   l.setBalance(iBalance - l.getOut());    break;
            }
        }
        
        for(int i = 1 ; i<lstLines.size() ; i++)
        {
            l = lstLines.get(i);
            
            if(l.getDate().isEmpty())
                l.setDate(lstLines.get(i-1).getDate());
            
            if(l.getBalance() == NOT_DEFINED)
                switch(l.getDirection())
                {
                    case IN:    l.setBalance(lstLines.get(i-1).getBalance() + l.getIn());       break;
                    case OUT:   l.setBalance(lstLines.get(i-1).getBalance() - l.getOut());      break;
                }
        }
    }
    private static void buildEntriesFromLines()
    {
        for(Line l : lstLines)
            lstEntries.add(new Entry(aSelected.getAccountID(), l.getDate(), l.getName(), l.getType(), aSelected.getAutoCategory(l.getName(), l.getDirection()), l.getIn(), l.getOut(), l.getBalance()));
    }
    private static boolean readFileAndBuildLines(File fileStatement)
    {
        Line l;
        String sLine;
        boolean bIgnoreHeader = true;
        
        try (BufferedReader obBufferedReader = new BufferedReader(new FileReader(fileStatement)))
        {
            while ((sLine = obBufferedReader.readLine()) != null)
            {
                if(!bIgnoreHeader && !sLine.isEmpty())
                {
                    l = new Line(sLine);
                    
                    if(l.hasError())
                        for(String s : l.getErrors())
                            addError(s, sLine);
                    else
                        lstLines.add(l);
                }
                
                bIgnoreHeader = false;
            }
        }
        catch (IOException ex)
        {
            addError("File read", "Unable to open file, read line or create internal buffered reader");
        }
        
        if(lstLines.isEmpty())
            addError("File format", "No valid entries in file");
        
        return lstErrors.isEmpty();
    }
    private static void openFile(File fileStatement)
    {
        clearAndReset();
        setAccountsRadioButtonsEnable(false);
        
        //Errors ---------------------------------------------------------------
        
        if(!readFileAndBuildLines(fileStatement))
        {
            showErrors();
            return;
        }
        
        if(!checkInterfaceWithDatabaseDate())
        {
            showErrors();
            return;
        }
        
        if(!checkInterfaceWithDatabaseBalance())
        {
            showErrors();
            return;
        }
        
        populateEmptyDatesAndBalances();
        buildEntriesFromLines();
        
        if(!runErrorCheck())
        {
            showErrors();
            return;
        }
        
        labStatementStatusMessage.setText("All Entries Valid (" + lstEntries.size() + ")     Errors (0)");
        
        setEntriesRadioButtonsEnable(true);
        setAddButtonEnable(true);
        
        showStatement(ALL);
    }
    
    //Show
    private static void showStatement(int iGroup)
    {
        List<Entry> lst = new ArrayList<>();
        
        tbStatement.clear();
        
        for(Entry e : lstEntries)
            if(iGroup == ALL)                                                                       lst.add(e);
            else if (iGroup == UNDEFINED && e.getCategoryName().equals(UNDEFINED_TEXT))             lst.add(e);
            else if (iGroup == AUTO_CATEGORISED && !e.getCategoryName().equals(UNDEFINED_TEXT))     lst.add(e);
        
        for(int i = 0 ; i<lst.size() ; i++)
            if(i == 0)
                lst.get(0).setShowDateTextForTableView(true);
            else if(lst.get(i).getDate() == lst.get(i-1).getDate())
                lst.get(i).setShowDateTextForTableView(false);
            else
                lst.get(i).setShowDateTextForTableView(true);
        
        if(lstEntries.size()>0)     refreshEntriesRadioButtonLabels();
        else                        resetEntriesRadioButtonsLabel();
        
        if(lst.size()>0)            tbStatement.setItems(lst);
        else                        tbStatement.setItem(new Blank(BLANK_FIELD_NAME, "No Entries"));
    }
    private static void showErrors()
    {
        if(lstErrors.size() == 1)   tbStatement.setItem(new Blank(BLANK_FIELD_NAME, "1 Error Found"));
        else                        tbStatement.setItem(new Blank(BLANK_FIELD_NAME, lstErrors.size() + " Errors Found"));
        
        tbErrors.setItems(lstErrors);
        
        obWindows.addWindow(tbErrors, 1);
        labStatementStatusMessage.setText("Errors (" + lstErrors.size() + ")");
    }
    
    //External API -------------------------------------------------------------
    public static void init()
    {
        obWindows.reset();
        
        clearAndReset();
        clearAccountsRadioButtons();
        setOpenButtonEnable(false);
        
        rbStatementEntriesAll.setSelected(true);
    }
    
    //UI Buttons
    public static void clear()
    {
        clearAndReset();
        
        obWindows.setWindow(NOT_DEFINED);
    }
    public static void showFileChooser()
    {
        FileChooser fileChooser = new FileChooser();
        File fileStatement;
        
        fileChooser.setTitle("Select Statement File (*.txt)");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("txt", "*.txt"));
        
        fileStatement = fileChooser.showOpenDialog(Administration.getWindow());
        
        if(fileStatement == null)   addError("Error opening file", "Null file object");
        else                        openFile(fileStatement);
    }
    public static void addEntriesToDatabase()
    {
        if(lstEntries.size() > 0)
        {
            if(Database.addEntries(lstEntries))
            {
                clearAndReset();
                labStatementStatusMessage.setText("Entries successfully added to the database for account: " + aSelected.getName());
                
                Accounts.rebuildAndRefresh(aSelected);
                DateRange.setQuickJumpButtons();
                aSelected.refreshAutoCategoriesFirstLastFrequency();
            }
        }
        else                                
        {
            tbStatement.setItem(new Blank(BLANK_FIELD_NAME, "No statement file loaded"));
        }
    }
    
    //Statement Context Menu
    public static void setCategory(List<Entry> lst, Category c)
    {
        for(Entry e : lst)
            e.setCategoryName(c.getName(), false);
        
        refreshEntriesRadioButtonLabels();
    }
    public static void setAutoCategory(AutoCategory ac)
    {
        for(Entry e : lstEntries)
            if(e.getDirection() == ac.getDirection() && e.getName().equals(ac.getEntryName()))
                e.setCategoryName(ac.getCategoryName(), false);
        
        refreshEntriesRadioButtonLabels();
    }
    
    //Category Add to Account | Entry Select in Tableview
    public static void rebuildContextMenu()
    {
        tbStatement.rebuildContextMenu();
    }
}
