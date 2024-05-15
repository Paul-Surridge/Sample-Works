package Database;


import Accounts.Account;
import Accounts.Accounts;
import Categories.Category;
import History.Entry;
import AutoCategories.AutoCategory;
import Categories.Categories;
import Refunds.Refund;
import Reminders.Reminder;
import Shared.Blank;
import Shared.Constants;
import Shared.Formatter;
import Shared.Popup;
import Shared.TableViewFactory;
import Watches.Watch;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

/*
Tables:
    BARCLAYS_CURRENT
    BARCLAYS_SAVING
    BARCLAYS_ISA

    Account Entries:

        PRIMARY_KEY             [BIGINT][19]
        RECORD_ID               [BIGINT][19]
        ENTRY_DATE              [INTEGER][10]
        ENTRY_NAME              [VARCHAR][256]
        ENTRY_TYPE              [VARCHAR][8]
        ENTRY_CATEGORY          [VARCHAR][256]
        ENTRY_INTERNAL_TRANSFER [VARCHAR][32]
        ENTRY_IN                [INTEGER][10]
        ENTRY_OUT               [INTEGER][10]
        ENTRY_BALANCE           [INTEGER][10]

Tables:
    BARCLAYS_CURRENT_CATEGORIES
    BARCLAYS_SAVING_CATEGORIES
    BARCLAYS_ISA_CATEGORIES

    Account Categories
        PRIMARY_KEY             [BIGINT][19]
        RECORD_ID               [BIGINT][19]
        CATEGORY_NAME           [VARCHAR][256]
        CATEGORY_DIRECTION      [VARCHAR][4]

Tables:
    BARCLAYS_CURRENT_AUTO_CATEGORIES
    BARCLAYS_SAVING_AUTO_CATEGORIES
    BARCLAYS_ISA_AUTO_CATEGORIES

    Account Auto Categories
        PRIMARY_KEY             [BIGINT][19]
        RECORD_ID               [BIGINT][19]
        ENTRY_NAME              [VARCHAR][256]
        ENTRY_DIRECTION         [VARCHAR][4]
        CATEGORY_NAME           [VARCHAR][256]

Tables:
    BARCLAYS_CURRENT_WATCHES
    BARCLAYS_SAVING_WATCHES
    BARCLAYS_ISA_WATCHES

    Watches
        PRIMARY_KEY             [BIGINT][19]
        RECORD_ID               [BIGINT][19]
        CATEGORY_NAME           [VARCHAR][256]
        CATEGORY_DIRECTION      [VARCHAR][4]
        LAST_DATE               [INTEGER][10]
        LAST_AMOUNT             [INTEGER][10]

Tables:
    BARCLAYS_CURRENT_REFUNDS
    BARCLAYS_SAVING_REFUNDS
    BARCLAYS_ISA_REFUNDS

    Refunds
        PRIMARY_KEY             [BIGINT][19]
        RECORD_ID               [BIGINT][19]
        REFUND_DATE             [INTEGER][10]
        REFUND_NAME             [VARCHAR][256]
        REFUND_KEYWORDS         [VARCHAR][1024]
        REFUND_AMOUNT           [INTEGER][10]
        RECEIVED_DATE           [INTEGER][10]
        RECEIVED_PRIMARY_KEY    [INTEGER][10]

Tables:
    REMINDERS

    Refunds
        PRIMARY_KEY             [BIGINT][19]
        RECORD_ID               [BIGINT][19]
        REMINDER_DATE           [INTEGER][10]
        REMINDER_NAME           [VARCHAR][256]
        REMINDER_DETAILS        [VARCHAR][1024]
        REMINDER_ALERT_DATE     [INTEGER][10]

Tables:
    TYPES

    Types
        PRIMARY_KEY             [BIGINT][19]
        RECORD_ID               [BIGINT][19]
        TYPE_IDENTIFIER         [VARCHAR][8]
        TYPE_GROUP              [VARCHAR][256]
        TYPE_DIRECTION          [VARCHAR][4]
*/

public class Database implements Constants
{
    private static Connection obConnection;
    private static Statement obStatement;
    
    private static TableView<DatabaseErrorTableInsertable> tvErrors;
    private static TextArea taDatabaseErrorDetailsSQL, taDatabaseErrorDetailsExceptionDetails;
    private static Label labDatabaseErrorDetailsMethod;
    private static int iErrorCounter;
    
    public Database(AnchorPane apDatabaseErrorTable, Label labDatabaseErrorDetailsMethod, TextArea taDatabaseErrorDetailsSQL, TextArea taDatabaseErrorDetailsExceptionDetails)
    {
        this.labDatabaseErrorDetailsMethod          = labDatabaseErrorDetailsMethod;
        this.taDatabaseErrorDetailsSQL              = taDatabaseErrorDetailsSQL;
        this.taDatabaseErrorDetailsExceptionDetails = taDatabaseErrorDetailsExceptionDetails;
        
        initTableViewOfErrors(apDatabaseErrorTable);
        initEventHandlersForMouse();
        clearError();
        initConnection();
    }
    
    //Internal -----------------------------------------------------------------
    private static void initConnection()
    {
        try
        {
            obConnection = DriverManager.getConnection("jdbc:derby:db\\dbAccounts;user=Accounts;password=password");
            obStatement = obConnection.createStatement();            
        }
        catch(SQLException ex)
        {
            showError("initConnection()", NOT_APPLICABLE_TEXT, ex.toString());
        }
    }
    private static void initTableViewOfErrors(AnchorPane ap)
    {
        tvErrors = TableViewFactory.buildTable(TABLEVIEW_ID_DATABASE_ERRORS);
        
        ap.getChildren().add(tvErrors);
    }
    private static void initEventHandlersForMouse()
    {
        tvErrors.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
            
            if(me.getClickCount() == 2)
            {
                List<Error> lst = getSelectedErrors();
                
                if(lst.size() == 1)
                    showErrorDetails(lst.get(0));
            }
        });
    }
    private static List<Error> getSelectedErrors()
    {
        List<Error> lst = new ArrayList<>();
        ObservableList<DatabaseErrorTableInsertable> ol = tvErrors.getSelectionModel().getSelectedItems();

        for(DatabaseErrorTableInsertable item : ol)
            if(item instanceof Error)
                lst.add((Error)item);
        
        return lst;
    }
    private static void showError(String sMethod, String sSQL, String sExceptionDetails)
    {
        if(tvErrors.getItems().size() == 1 && tvErrors.getItems().get(0) instanceof Blank)
            tvErrors.getItems().clear();
        
        tvErrors.getItems().add(new Error(iErrorCounter, sMethod, sSQL, sExceptionDetails));
        
        iErrorCounter++;
        
        Popup.showFixed(POPUP_DATABASE_ERROR);
    }
    private static void showErrorDetails(Error e)
    {
        labDatabaseErrorDetailsMethod.setText(e.getMethod());
        taDatabaseErrorDetailsSQL.setText(e.getSQL());
        taDatabaseErrorDetailsExceptionDetails.setText(e.getExceptionDetails());
        
        Popup.showFixed(POPUP_DATABASE_ERROR_DETAILS);
    }
    
    //SQL - Get table name
    private static String getTableName(Account a, int iTable)
    {
        String sTable = "ACCOUNTS." + a.getNameDatabaseTablePrefix();
        
        switch(iTable)
        {
            case DATABASE_TABLE_ENTRY:          sTable += "";                   break;
            case DATABASE_TABLE_CATEGORY:       sTable += "_CATEGORIES";        break;
            case DATABASE_TABLE_AUTO_CATEGORY:  sTable += "_AUTO_CATEGORIES";   break;
            case DATABASE_TABLE_WATCH:          sTable += "_WATCHES";           break;
            case DATABASE_TABLE_REFUND:         sTable += "_REFUNDS";           break;
        }
        
        return sTable;
    }
    private static String getTableName(Category c, int iTable)
    {
        return getTableName(Accounts.get(c.getAccountID()), iTable);
    }
    private static String getTableName(AutoCategory ac, int iTable)
    {
        return getTableName(Accounts.get(ac.getAccountID()), iTable);
    }
    private static String getTableName(Entry e, int iTable)
    {
        return getTableName(Accounts.get(e.getAccountID()), iTable);
    }
    private static String getTableName(Watch w, int iTable)
    {
        return getTableName(Accounts.get(w.getAccountID()), iTable);
    }
    private static String getTableName(Refund r, int iTable)
    {
        return getTableName(Accounts.get(r.getAccountID()), iTable);
    }
    private static String getTableName(int iTable)
    {
        switch(iTable)
        {
            case DATABASE_TABLE_REMINDER:       return "REMINDERS";
        }
        
        return "";
    }
    
    //SQL - Get next available Record ID
    private static long getNextRecordID(String sTableName)
    {
        ResultSet objResultSet;
        String s = "";
        
        try
        {
            s = "SELECT RECORD_ID FROM " + sTableName + " ORDER BY RECORD_ID DESC FETCH FIRST 1 ROWS ONLY";
            
            objResultSet = obStatement.executeQuery(s);
        
            if(objResultSet.next())     return objResultSet.getLong(1) + 1;
            else                        return 1;
        }
        catch(SQLException ex)
        {
            showError("long getNextRecordID(String sTableName)", s, ex.toString());
        }
        
        return NOT_DEFINED;
    }
    private static long getNextRecordID(Category c, int iTable)
    {
        return getNextRecordID(getTableName(c, iTable));
    }
    private static long getNextRecordID(AutoCategory ac, int iTable)
    {
        return getNextRecordID(getTableName(ac, iTable));
    }
    private static long getNextRecordID(Entry e, int iTable)
    {
        return getNextRecordID(getTableName(e, iTable));
    }
    private static long getNextRecordID(Watch w, int iTable)
    {
        return getNextRecordID(getTableName(w, iTable));
    }
    private static long getNextRecordID(Refund r, int iTable)
    {
        return getNextRecordID(getTableName(r, iTable));
    }
    private static long getNextRecordID(int iTable)
    {
        return getNextRecordID(getTableName(iTable));
    }
    
    //Set Primary Key within object
    private static long getPrimaryKey(String sTableName, long lRecordID)
    {
        ResultSet objResultSet;
        String s = "";
        
        try
        {
            s = "SELECT PRIMARY_KEY FROM " + sTableName + " WHERE RECORD_ID = " +  lRecordID;
            
            objResultSet = obStatement.executeQuery(s);
        
            if(objResultSet.next())     return objResultSet.getLong(1);
            else                        showError("long getPrimaryKey(String sTableName, int iRecordID)", s, "Empty objResultSet: Unable to retrieve primary key to assign to object");
        }
        catch(SQLException ex)
        {
            showError("long getPrimaryKey(String sTableName, long lRecordID)", s, ex.toString());
        }
        
        return NOT_DEFINED;
    }
    private static void setPrimaryKey(Entry e)
    {
        e.setPrimaryKey(getPrimaryKey(getTableName(e, DATABASE_TABLE_ENTRY), e.getRecordID()));
    }
    private static void setPrimaryKey(Category c)
    {
        c.setPrimaryKey(getPrimaryKey(getTableName(c, DATABASE_TABLE_CATEGORY), c.getRecordID()));
    }
    private static void setPrimaryKey(AutoCategory ac)
    {
        ac.setPrimaryKey(getPrimaryKey(getTableName(ac, DATABASE_TABLE_AUTO_CATEGORY), ac.getRecordID()));
    }
    private static void setPrimaryKey(Watch w)
    {
        w.setPrimaryKey(getPrimaryKey(getTableName(w, DATABASE_TABLE_WATCH), w.getRecordID()));
    }
    private static void setPrimaryKey(Refund r)
    {
        r.setPrimaryKey(getPrimaryKey(getTableName(r, DATABASE_TABLE_REFUND), r.getRecordID()));
    }
    private static void setPrimaryKey(Reminder r)
    {
        r.setPrimaryKey(getPrimaryKey(getTableName(DATABASE_TABLE_REMINDER), r.getRecordID()));
    }
    
    //Generate entry(ies)
    private static List<Entry> generateEntries(ResultSet obResultSet, int iAccountID)
    {
        List<Entry> lst = new ArrayList<>();
        Entry e;
        
        try
        {
            while(obResultSet.next())
            {
                e = new Entry(iAccountID, obResultSet.getLong(1), obResultSet.getLong(2), obResultSet.getInt(3), obResultSet.getString(4),
                              obResultSet.getString(5), obResultSet.getString(6), obResultSet.getInt(7), obResultSet.getInt(8), obResultSet.getInt(9));

                lst.add(e);
            }
        }
        catch(SQLException ex)
        {
            showError("List<Entry> generateEntries(ResultSet obResultSet, int iAccountID)", NOT_APPLICABLE_TEXT, ex.toString());
        }
        
        return lst;
    }
    
    //Send SQL commands
    private static boolean runSQLUpdate(String s)
    {
        try
        {
            obStatement.executeUpdate(s);
        }
        catch(SQLException ex)
        {
            showError("boolean runSQLUpdate(String s)", s, ex.toString());
            return false;
        }
        
        return true;
    }
    private static String formatSQLInsert(String sValue, int iMaxLength)
    {
        String s;
        
        if(sValue.length() > iMaxLength)    s = sValue.substring(0, iMaxLength);
        else                                s = sValue;
        
        if(sValue.contains("\'"))   return "'" + s.replace("\'", "\'\'") + "'";
        else                        return "'" + s + "'";
    }
    
    //Miscellaneous
    private static long getNumberOfRecords(Account a, int iTable)
    {
        ResultSet obResultSet;
        String s = "";
        
        try
        {
            s = "SELECT COUNT(*) AS NUMBER_OF_ROWS FROM " + getTableName(a, iTable);
        
            obResultSet = obStatement.executeQuery(s);
            
            if(obResultSet.next())  return obResultSet.getLong(1);
            else                    return 0;
        }
        catch(SQLException ex)
        {
            showError("long getNumberOfRecords(Account a, int iTable)", s, ex.toString());
        }
        
        return 0;
    }
    private static long getNumberOfRecords(int iTable)
    {
        ResultSet obResultSet;
        String s = "";
        
        try
        {
            s = "SELECT COUNT(*) AS NUMBER_OF_ROWS FROM " + getTableName(iTable);
        
            obResultSet = obStatement.executeQuery(s);
            
            if(obResultSet.next())  return obResultSet.getLong(1);
            else                    return 0;
        }
        catch(SQLException ex)
        {
            showError("long getNumberOfRecords(int iTable)", s, ex.toString());
        }
        
        return 0;
    }
    private static String getDirection(int iDirection)
    {
        switch(iDirection)
        {
            case IN:    return " ENTRY_OUT = " + NOT_DEFINED;
            case OUT:   return " ENTRY_IN = " + NOT_DEFINED;
        }
        
        return "";
    }
    
    //External API -------------------------------------------------------------
    
    //Administration
    public static boolean deleteAllEntries(Account a)
    {
        return runSQLUpdate("DELETE FROM " + getTableName(a, DATABASE_TABLE_ENTRY));
    }
    public static boolean deleteAllCategories(Account a)
    {
        return runSQLUpdate("DELETE FROM " + getTableName(a, DATABASE_TABLE_CATEGORY));
    }
    public static boolean deleteAllAutoCategories(Account a)
    {
        return runSQLUpdate("DELETE FROM " + getTableName(a, DATABASE_TABLE_AUTO_CATEGORY));
    }
    public static boolean deleteAllWatches(Account a)
    {
        return runSQLUpdate("DELETE FROM " + getTableName(a, DATABASE_TABLE_WATCH));
    }
    public static boolean deleteAllRefunds(Account a)
    {
        return runSQLUpdate("DELETE FROM " + getTableName(a, DATABASE_TABLE_REFUND));
    }
    public static boolean deleteAllReminders()
    {
        return runSQLUpdate("DELETE FROM " + getTableName(DATABASE_TABLE_REMINDER));
    }
    public static boolean reset(Account a)
    {
        if(!deleteAllEntries(a))            return false;
        if(!deleteAllCategories(a))         return false;
        if(!deleteAllAutoCategories(a))     return false;
        if(!deleteAllWatches(a))            return false;
        if(!deleteAllRefunds(a))            return false;
        
        return true;
    }
    public static boolean resetAll()
    {
        for(Account a : Accounts.getAccounts())
            if(!reset(a))
                return false;
        
        return deleteAllReminders();
    }
      
    //Entry
    public static List<Entry> getEntries(Account a)
    {
        return getEntries(a, NOT_DEFINED, NOT_DEFINED);
    }
    public static List<Entry> getEntries(Account a, int iFrom)
    {
        return getEntries(a, iFrom, NOT_DEFINED);
    }
    public static List<Entry> getEntries(Account a, int iFrom, int iTo)
    {
        ResultSet obResultSet;
        List<Entry> lst = new ArrayList<>();
        String s = "";
        
        try
        {
            if      (iFrom == NOT_DEFINED)      s = "SELECT * FROM " + getTableName(a, DATABASE_TABLE_ENTRY);
            else if (iTo == NOT_DEFINED)        s = "SELECT * FROM " + getTableName(a, DATABASE_TABLE_ENTRY) + " WHERE ENTRY_DATE >= " + iFrom;
            else if (iFrom == iTo)              s = "SELECT * FROM " + getTableName(a, DATABASE_TABLE_ENTRY) + " WHERE ENTRY_DATE = " + iFrom;
            else                                s = "SELECT * FROM " + getTableName(a, DATABASE_TABLE_ENTRY) + " WHERE ENTRY_DATE >= " + iFrom + " AND ENTRY_DATE <= " + iTo;
            
            obResultSet = obStatement.executeQuery(s);
            
            lst = generateEntries(obResultSet, a.getAccountID());
        }
        catch(SQLException ex)
        {
            showError("List<Entry> getEntries(Account a, int iFrom, int iTo)", s, ex.toString());
        }
        
        return lst;
    }
    public static List<Entry> getEntries(Account a, LocalDate ldFrom, LocalDate ldTo)
    {
        return getEntries(a, Formatter.convert(ldFrom), Formatter.convert(ldTo));
    }
    public static List<Entry> getEntriesWithDirection(Account a, int iFrom, int iDirection)
    {
        ResultSet obResultSet;
        List<Entry> lst = new ArrayList<>();
        String s = "";
        
        try
        {
            s = "SELECT * FROM " + getTableName(a, DATABASE_TABLE_ENTRY) + " WHERE " + getDirection(iDirection);
            
            if(iFrom != NOT_DEFINED)
                s += " AND ENTRY_DATE >= " + iFrom;
            
            obResultSet = obStatement.executeQuery(s);
            
            lst = generateEntries(obResultSet, a.getAccountID());
        }
        catch(SQLException ex)
        {
            showError("List<Entry> getEntriesWithDirection(Account a, int iFrom, int iDirection)", s, ex.toString());
        }
        
        return lst;
    }
    public static List<Entry> getEntriesWatched(Account a, Watch w)
    {
        ResultSet obResultSet;
        List<Entry> lst = new ArrayList<>();
        String s = "";
        
        try
        {
            s = "SELECT * FROM " + getTableName(a, DATABASE_TABLE_ENTRY) + " WHERE ENTRY_CATEGORY = " + formatSQLInsert(w.getCategoryName(), DATABASE_RECORD_MAXIMUM_LENGTH_256) + " AND " + getDirection(w.getDirection());
            
            obResultSet = obStatement.executeQuery(s);
            
            lst = generateEntries(obResultSet, a.getAccountID());
        }
        catch(SQLException ex)
        {
            showError("List<Entry> getEntriesWatched(Account a, Watch w)", s, ex.toString());
        }
        
        return lst;
    }
    public static List<Entry> getEntriesWithPrimaryKey(Account a, long lPrimaryKey)
    {
        ResultSet obResultSet;
        List<Entry> lst = new ArrayList<>();
        String s = "";
        
        try
        {
            s = "SELECT * FROM " + getTableName(a, DATABASE_TABLE_ENTRY) + " WHERE PRIMARY_KEY = " + lPrimaryKey;
            
            obResultSet = obStatement.executeQuery(s);
            
            lst = generateEntries(obResultSet, a.getAccountID());
        }
        catch(SQLException ex)
        {
            showError("List<Entry> getEntriesWithPrimaryKey(Account a, long lPrimaryKey)", s, ex.toString());
        }
        
        return lst;
    }
    public static boolean hasEntries(Account a)
    {
        return getNumberOfEntries(a) > 0;
    }
    public static boolean addEntry(Entry e)
    {
        e.setRecordID(getNextRecordID(e, DATABASE_TABLE_ENTRY));
        
        String sBuild = "INSERT INTO " + getTableName(e, DATABASE_TABLE_ENTRY) + " (RECORD_ID, ENTRY_DATE, ENTRY_NAME, ENTRY_TYPE, ENTRY_CATEGORY, ENTRY_IN, ENTRY_OUT, ENTRY_BALANCE) VALUES (" +
                e.getRecordID() + ", " +
                e.getDate() + ", " +
                formatSQLInsert(e.getName(), DATABASE_RECORD_MAXIMUM_LENGTH_256) + ", " +
                formatSQLInsert(e.getType(), DATABASE_RECORD_MAXIMUM_LENGTH_8) + ", " +
                formatSQLInsert(e.getCategoryName(), DATABASE_RECORD_MAXIMUM_LENGTH_256) + ", " +
                e.getIn() + ", " +
                e.getOut() + ", " +
                e.getBalance() + ")";
        
        if(runSQLUpdate(sBuild))    setPrimaryKey(e);
        else                        return false;
        
        return true;
    }
    public static boolean addEntries(List<Entry> lst)
    {
        for(Entry e : lst)
            if(!addEntry(e))
                return false;
        
        return true;
    }
    public static boolean deleteEntry(Entry e)
    {
        return runSQLUpdate("DELETE FROM " + getTableName(e, DATABASE_TABLE_ENTRY) + " WHERE PRIMARY_KEY = " + e.getPrimaryKey());
    }
    public static boolean doesEntryExist(Entry e)
    {
        ResultSet obResultSet;
        String s = "";
        
        try
        {
            s = "SELECT * FROM " + getTableName(e, DATABASE_TABLE_ENTRY) + " WHERE " +
                "ENTRY_DATE = "             + e.getDate() +
                "AND ENTRY_NAME = "         + formatSQLInsert(e.getName(), DATABASE_RECORD_MAXIMUM_LENGTH_256) +
                "AND ENTRY_TYPE = "         + formatSQLInsert(e.getType(), DATABASE_RECORD_MAXIMUM_LENGTH_8) +
                "AND ENTRY_IN = "           + e.getIn() +
                "AND ENTRY_OUT = "          + e.getOut() +
                "AND ENTRY_BALANCE = "      + e.getBalance();
            
            obResultSet = obStatement.executeQuery(s);
        
            return obResultSet.next();
        }
        catch(SQLException ex)
        {
            showError("boolean doesEntryExist(Entry e)", s, ex.toString());
        }
        
        return false;
    }
    public static boolean setCategory(Entry e)
    {
        return runSQLUpdate("UPDATE " + getTableName(e, DATABASE_TABLE_ENTRY) + " SET ENTRY_CATEGORY = " + formatSQLInsert(e.getCategoryName(), DATABASE_RECORD_MAXIMUM_LENGTH_256) + " WHERE PRIMARY_KEY = " + e.getPrimaryKey());
    }
    public static boolean setCategoryOnAllEntries(AutoCategory ac)
    {
        String s = "UPDATE " + getTableName(ac, DATABASE_TABLE_ENTRY) + " SET ENTRY_CATEGORY = " + formatSQLInsert(ac.getCategoryName(), DATABASE_RECORD_MAXIMUM_LENGTH_256) + " WHERE ENTRY_NAME = " + formatSQLInsert(ac.getEntryName(), DATABASE_RECORD_MAXIMUM_LENGTH_256) + " AND " + getDirection(ac.getDirection());
        
        return runSQLUpdate(s);
    }
    public static Entry getEntryAtPosition(Account a, int iPosition)
    {
        ResultSet obResultSet;
        List<Entry> lst = new ArrayList<>();
        String s = "";
        
        try
        {
            switch(iPosition)
            {
                case EARLIEST:      s = "SELECT * FROM " + getTableName(a, DATABASE_TABLE_ENTRY) + " ORDER BY ENTRY_DATE ASC FETCH FIRST 1 ROWS ONLY";      break;
                case LATEST:        s = "SELECT * FROM " + getTableName(a, DATABASE_TABLE_ENTRY) + " ORDER BY ENTRY_DATE DESC FETCH FIRST 1 ROWS ONLY";     break;
            }
            
            obResultSet = obStatement.executeQuery(s);
            
            lst = generateEntries(obResultSet, a.getAccountID());
        }
        catch(SQLException ex)
        {
            showError("Entry getEntry(Account a, int iPosition)", s, ex.toString());
        }
        
        return lst.isEmpty() ? null : lst.get(0);
    }
    public static Entry getLastWatchedOccurence(Account a, Watch w)
    {
        ResultSet obResultSet;
        List<Entry> lst = new ArrayList<>();
        String s = "";
        
        try
        {
            s = "SELECT * FROM " + getTableName(a, DATABASE_TABLE_ENTRY) + " WHERE ENTRY_CATEGORY = " + formatSQLInsert(w.getCategoryName(), DATABASE_RECORD_MAXIMUM_LENGTH_256) + " AND " + getDirection(w.getDirection()) + " ORDER BY ENTRY_DATE DESC FETCH FIRST 1 ROWS ONLY";
            
            obResultSet = obStatement.executeQuery(s);
            
            lst = generateEntries(obResultSet, a.getAccountID());
        }
        catch(SQLException ex)
        {
            showError("Entry getLastWatchedOccurence(Account a, Watch w)", s, ex.toString());
        }
        
        return lst.isEmpty() ? null : lst.get(0);
    }
    public static int getEntryDate(Account a, int iPosition)
    {
        ResultSet obResultSet;
        String s = "";
        
        try
        {
            switch(iPosition)
            {
                case EARLIEST:  s = "MIN";   break;
                case LATEST:    s = "MAX";   break;       
            }
            
            s = "SELECT " + s + "(ENTRY_DATE) as Entry_Date FROM " + getTableName(a, DATABASE_TABLE_ENTRY);
            
            obResultSet = obStatement.executeQuery(s);
            
            if(obResultSet.next())
                return obResultSet.getInt(1) == 0 ? NOT_DEFINED : obResultSet.getInt(1);
        }
        catch(SQLException ex)
        {
            showError("int getEntryDate(Account a, int iPosition)", s, ex.toString());
        }
        
        return NOT_DEFINED;
    }
    public static int getEntryDateFromAccounts(List<Account> lst, int iPosition)
    {
        int iDate;
        int iDateResult = NOT_DEFINED;
        
        for(Account a : lst)
        {
            iDate = getEntryDate(a, iPosition);

            if(iDate != NOT_DEFINED)
            {
                switch(iPosition)
                {
                    case EARLIEST:
                    {
                        if(iDateResult == NOT_DEFINED || iDate<iDateResult)
                            iDateResult = iDate;
                        break;
                    }
                    case LATEST:
                    {
                        if(iDateResult == NOT_DEFINED || iDate>iDateResult)
                            iDateResult = iDate;
                        break;
                    }
                }
            }
                
        }
        
        return iDateResult;
    }
    public static long getNumberOfEntries(Account a)
    {
        return getNumberOfRecords(a, DATABASE_TABLE_ENTRY);
    }
    
    //Category
    public static List<Category> getCategories(Account a)
    {
        ResultSet obResultSet;
        List<Category> lst = new ArrayList<>();
        String s = "";
        
        try
        {
            s = "SELECT * FROM " + getTableName(a, DATABASE_TABLE_CATEGORY);
            obResultSet = obStatement.executeQuery(s);
        
            while(obResultSet.next())
                lst.add(new Category(a.getAccountID(), obResultSet.getLong(1), obResultSet.getLong(2),  obResultSet.getString(3), obResultSet.getString(4)));
        }
        catch(SQLException ex)
        {
            showError("List<Category> getCategories(Account a)", s, ex.toString());
        }
        
        return lst;
    }
    public static boolean addCategory(Category c)
    {
        c.setRecordID(getNextRecordID(c, DATABASE_TABLE_CATEGORY));
        
        String sBuild = "INSERT INTO " + getTableName(c, DATABASE_TABLE_CATEGORY) + " (RECORD_ID, CATEGORY_NAME, CATEGORY_DIRECTION) VALUES (" +
                c.getRecordID()+ ", " +
                formatSQLInsert(c.getName(), DATABASE_RECORD_MAXIMUM_LENGTH_256) + ", " +
                formatSQLInsert(c.getDirectionText(), DATABASE_RECORD_MAXIMUM_LENGTH_4) + ")";
        
        if(runSQLUpdate(sBuild))    setPrimaryKey(c);
        else                        return false;
        
        return true;
    }
    public static boolean addCategories(List<Category> lst)
    {
        for(Category c : lst)
            if(!addCategory(c))
                return false;
        
        return true;
    }
    public static boolean deleteCategory(Category c)
    {
        if(!runSQLUpdate("UPDATE " + getTableName(c, DATABASE_TABLE_ENTRY) + " SET ENTRY_CATEGORY = " + formatSQLInsert(Categories.getUndefinedName(), DATABASE_RECORD_MAXIMUM_LENGTH_256) + " WHERE ENTRY_CATEGORY = " + formatSQLInsert(c.getName(), DATABASE_RECORD_MAXIMUM_LENGTH_256) + " AND " + getDirection(c.getDirection())))
            return false;
        
        return runSQLUpdate("DELETE FROM " + getTableName(c, DATABASE_TABLE_CATEGORY) + " WHERE PRIMARY_KEY = " + c.getPrimaryKey());
    }
    public static boolean deleteAllCategories(Account a, int iDirection)
    {
        switch(iDirection)
        {
            case IN:        return runSQLUpdate("DELETE FROM " + getTableName(a, DATABASE_TABLE_CATEGORY) + " WHERE CATEGORY_DIRECTION = " + formatSQLInsert(IN_TEXT, DATABASE_RECORD_MAXIMUM_LENGTH_4));
            case OUT:       return runSQLUpdate("DELETE FROM " + getTableName(a, DATABASE_TABLE_CATEGORY) + " WHERE CATEGORY_DIRECTION = " + formatSQLInsert(OUT_TEXT, DATABASE_RECORD_MAXIMUM_LENGTH_4));
        }
        
        return false;
    }
    public static boolean renameCategory(Category c, String sOldName)
    {
        if(!runSQLUpdate("UPDATE " + getTableName(c, DATABASE_TABLE_ENTRY) + " SET ENTRY_CATEGORY = " + formatSQLInsert(c.getName(), DATABASE_RECORD_MAXIMUM_LENGTH_256) + " WHERE ENTRY_CATEGORY = " + formatSQLInsert(sOldName, DATABASE_RECORD_MAXIMUM_LENGTH_256) + " AND " + getDirection(c.getDirection())))
            return false;
        
        return runSQLUpdate("UPDATE " + getTableName(c, DATABASE_TABLE_CATEGORY) + " SET CATEGORY_NAME = " + formatSQLInsert(c.getName(), DATABASE_RECORD_MAXIMUM_LENGTH_256) + " WHERE PRIMARY_KEY = " + c.getPrimaryKey());
    }
    public static int getFirstOccurence(Account a, Category c)
    {
        ResultSet obResultSet;
        String s = "";
        
        try
        {
            s = "SELECT * FROM " + getTableName(a, DATABASE_TABLE_ENTRY) + " WHERE ENTRY_CATEGORY = " + formatSQLInsert(c.getName(), DATABASE_RECORD_MAXIMUM_LENGTH_256) + " AND " + getDirection(c.getDirection()) + " ORDER BY ENTRY_DATE ASC FETCH FIRST 1 ROWS ONLY";
            
            obResultSet = obStatement.executeQuery(s);
        
            if(obResultSet.next())  return obResultSet.getInt(3);
            else                    return NOT_DEFINED;
        }
        catch(SQLException ex)
        {
            showError("int getFirstOccurence(Account a, Category c)", s, ex.toString());
        }
        
        return NOT_DEFINED;
    }
    public static int getLastOccurence(Account a, Category c)
    {
        ResultSet obResultSet;
        String s = "";
        
        try
        {
            s = "SELECT * FROM " + getTableName(a, DATABASE_TABLE_ENTRY) + " WHERE ENTRY_CATEGORY = " + formatSQLInsert(c.getName(), DATABASE_RECORD_MAXIMUM_LENGTH_256) + " AND " + getDirection(c.getDirection()) + " ORDER BY ENTRY_DATE DESC FETCH FIRST 1 ROWS ONLY";
            
            obResultSet = obStatement.executeQuery(s);
        
            if(obResultSet.next())  return obResultSet.getInt(3);
            else                    return NOT_DEFINED;
        }
        catch(SQLException ex)
        {
            showError("int getLastOccurence(Account a, Category c)", s, ex.toString());
        }
        
        return NOT_DEFINED;
    }
    public static long getFrequency(Account a, Category c)
    {
        ResultSet obResultSet;
        String s = "";
        
        try
        {
            s = "SELECT COUNT(*) AS NUMBER_OF_ROWS FROM " + getTableName(a, DATABASE_TABLE_ENTRY) + " WHERE ENTRY_CATEGORY = " + formatSQLInsert(c.getName(), DATABASE_RECORD_MAXIMUM_LENGTH_256) + " AND " + getDirection(c.getDirection());
            
            obResultSet = obStatement.executeQuery(s);
        
            if(obResultSet.next())  return obResultSet.getLong(1);
            else                    return NOT_DEFINED;
        }
        catch(SQLException ex)
        {
            showError("long getFrequency(Account a, Category c)", s, ex.toString());
        }
        
        return NOT_DEFINED;
    }
    public static long getNumberOfCategories(Account a)
    {
        return getNumberOfRecords(a, DATABASE_TABLE_CATEGORY);
    }
    
    //AutoCategory
    public static List<AutoCategory> getAutoCategories(Account a)
    {
        ResultSet obResultSet;
        List<AutoCategory> lst = new ArrayList<>();
        String s = "";
        
        try
        {
            s = "SELECT * FROM " + getTableName(a, DATABASE_TABLE_AUTO_CATEGORY);
            
            obResultSet = obStatement.executeQuery(s);
        
            while(obResultSet.next())
                lst.add(new AutoCategory(a.getAccountID(), obResultSet.getLong(1), obResultSet.getLong(2), obResultSet.getString(3), obResultSet.getString(4), obResultSet.getString(5)));
        }
        catch(SQLException ex)
        {
            showError("List<AutoCategory> getAutoCategories(Account a)", s, ex.toString());
        }
        
        return lst;
    }
    public static boolean addAutoCategory(AutoCategory ac)
    {
        ac.setRecordID(getNextRecordID(ac, DATABASE_TABLE_AUTO_CATEGORY));
        
        String sBuild = "INSERT INTO " + getTableName(ac, DATABASE_TABLE_AUTO_CATEGORY) + " (RECORD_ID, ENTRY_NAME, ENTRY_DIRECTION, CATEGORY_NAME) VALUES (" +
                ac.getRecordID() + ", " +
                formatSQLInsert(ac.getEntryName(), DATABASE_RECORD_MAXIMUM_LENGTH_256) + ", " +
                formatSQLInsert(ac.getDirectionText(), DATABASE_RECORD_MAXIMUM_LENGTH_4) + ", " +
                formatSQLInsert(ac.getCategoryName(), DATABASE_RECORD_MAXIMUM_LENGTH_256) + ")";
        
        if(runSQLUpdate(sBuild))    setPrimaryKey(ac);
        else                        return false;
        
        return true;
    }
    public static boolean addAutoCategories(List<AutoCategory> lst)
    {
        for(AutoCategory ac : lst)
            if(!addAutoCategory(ac))
                return false;
        
        return true;
    }
    public static boolean deleteAutoCategory(AutoCategory ac)
    {
        return runSQLUpdate("DELETE FROM " + getTableName(ac, DATABASE_TABLE_AUTO_CATEGORY) + " WHERE PRIMARY_KEY = " + ac.getPrimaryKey());
    }
    public static boolean deleteAllAutoCategories(Account a, int iDirection)
    {
        switch(iDirection)
        {
            case IN:        return runSQLUpdate("DELETE FROM " + getTableName(a, DATABASE_TABLE_AUTO_CATEGORY) + " WHERE ENTRY_DIRECTION = " + formatSQLInsert(IN_TEXT, DATABASE_RECORD_MAXIMUM_LENGTH_4));
            case OUT:       return runSQLUpdate("DELETE FROM " + getTableName(a, DATABASE_TABLE_AUTO_CATEGORY) + " WHERE ENTRY_DIRECTION = " + formatSQLInsert(OUT_TEXT, DATABASE_RECORD_MAXIMUM_LENGTH_4));
        }
        
        return false;
    }
    public static boolean setAutoCategory(AutoCategory ac)
    {
        return runSQLUpdate("UPDATE " + getTableName(ac, DATABASE_TABLE_AUTO_CATEGORY) +
                " SET CATEGORY_NAME = " + formatSQLInsert(ac.getCategoryName(), DATABASE_RECORD_MAXIMUM_LENGTH_256) +
                " WHERE PRIMARY_KEY = " + ac.getPrimaryKey());
    }
    public static int getFirstOccurence(Account a, AutoCategory ac)
    {
        ResultSet obResultSet;
        String s = "";
        
        try
        {
            s = "SELECT * FROM " + getTableName(a, DATABASE_TABLE_ENTRY) + " WHERE ENTRY_NAME = " + formatSQLInsert(ac.getEntryName(), DATABASE_RECORD_MAXIMUM_LENGTH_256) + " ORDER BY ENTRY_DATE ASC FETCH FIRST 1 ROWS ONLY";
            
            obResultSet = obStatement.executeQuery(s);
        
            if(obResultSet.next())  return obResultSet.getInt(3);
            else                    return NOT_DEFINED;
        }
        catch(SQLException ex)
        {
            showError("int getFirstOccurence(Account a, AutoCategory ac)", s, ex.toString());
        }
        
        return NOT_DEFINED;
    }
    public static int getLastOccurence(Account a, AutoCategory ac)
    {
        ResultSet obResultSet;
        String s = "";
        
        try
        {
            s = "SELECT * FROM " + getTableName(a, DATABASE_TABLE_ENTRY) + " WHERE ENTRY_NAME = " + formatSQLInsert(ac.getEntryName(), DATABASE_RECORD_MAXIMUM_LENGTH_256) + " ORDER BY ENTRY_DATE DESC FETCH FIRST 1 ROWS ONLY";
            
            obResultSet = obStatement.executeQuery(s);
        
            if(obResultSet.next())  return obResultSet.getInt(3);
            else                    return NOT_DEFINED;
        }
        catch(SQLException ex)
        {
            showError("int getLastOccurence(Account a, AutoCategory ac)", s, ex.toString());
        }
        
        return NOT_DEFINED;
    }
    public static long getFrequency(Account a, AutoCategory ac)
    {
        ResultSet obResultSet;
        String s = "";
        
        try
        {
            s = "SELECT COUNT(*) AS NUMBER_OF_ROWS FROM " + getTableName(a, DATABASE_TABLE_ENTRY) + " WHERE ENTRY_NAME = " + formatSQLInsert(ac.getEntryName(), DATABASE_RECORD_MAXIMUM_LENGTH_256);
            
            obResultSet = obStatement.executeQuery(s);
        
            if(obResultSet.next())  return obResultSet.getLong(1);
            else                    return NOT_DEFINED;
        }
        catch(SQLException ex)
        {
            showError("long getFrequency(Account a, AutoCategory ac)", s, ex.toString());
        }
        
        return NOT_DEFINED;
    }
    public static long getNumberOfAutoCategories(Account a)
    {
        return getNumberOfRecords(a, DATABASE_TABLE_AUTO_CATEGORY);
    }
    
    //Watch
    public static List<Watch> getWatches(Account a)
    {
        ResultSet obResultSet;
        List<Watch> lst = new ArrayList<>();
        String s = "";
        
        try
        {
            s = "SELECT * FROM " + getTableName(a, DATABASE_TABLE_WATCH);
            
            obResultSet = obStatement.executeQuery(s);
        
            while(obResultSet.next())
                lst.add(new Watch(a.getAccountID(), obResultSet.getLong(1), obResultSet.getLong(2),  obResultSet.getString(3), obResultSet.getString(4), obResultSet.getInt(5), obResultSet.getInt(6)));
        }
        catch(SQLException ex)
        {
            showError("List<Watch> getWatches(Account a)", s, ex.toString());
        }
        
        return lst;
    }
    public static boolean addWatch(Watch w)
    {
        w.setRecordID(getNextRecordID(w, DATABASE_TABLE_WATCH));
        
        String sBuild = "INSERT INTO " + getTableName(w, DATABASE_TABLE_WATCH) + " (RECORD_ID, CATEGORY_NAME, CATEGORY_DIRECTION, LAST_DATE, LAST_AMOUNT) VALUES (" +
                w.getRecordID()+ ", " +
                formatSQLInsert(w.getCategoryName(), DATABASE_RECORD_MAXIMUM_LENGTH_256) + ", " +
                formatSQLInsert(w.getDirectionText(), DATABASE_RECORD_MAXIMUM_LENGTH_4) + ", " +
                w.getLastDate()+ ", " +
                w.getLastAmount() + ")";
        
        if(runSQLUpdate(sBuild))    setPrimaryKey(w);
        else                        return false;
        
        return true;
    }
    public static boolean addWatches(List<Watch> lst)
    {
        for(Watch w : lst)
            if(!addWatch(w))
                return false;
        
        return true;
    }
    public static boolean deleteWatch(Watch w)
    {
        return runSQLUpdate("DELETE FROM " + getTableName(w, DATABASE_TABLE_WATCH) + " WHERE PRIMARY_KEY = " + w.getPrimaryKey());
    }
    public static boolean updateLastDateAndLastAmount(Account a, Watch w)
    {
        if(!runSQLUpdate("UPDATE " + getTableName(a, DATABASE_TABLE_WATCH) + " SET LAST_DATE = " + w.getLastDate() + ", LAST_AMOUNT = " + w.getLastAmount() + " WHERE PRIMARY_KEY = " + w.getPrimaryKey()))
            return false;
        
        return false;
    }
    public static long getNumberOfWatches(Account a)
    {
        return getNumberOfRecords(a, DATABASE_TABLE_WATCH);
    }
    
    //Refund
    public static List<Refund> getRefunds(Account a)
    {
        ResultSet obResultSet;
        List<Refund> lst = new ArrayList<>();
        String s = "";
        
        try
        {
            s = "SELECT * FROM " + getTableName(a, DATABASE_TABLE_REFUND);
            
            obResultSet = obStatement.executeQuery(s);
            
            while(obResultSet.next())
                lst.add(new Refund(a.getAccountID(), obResultSet.getLong(1), obResultSet.getLong(2), obResultSet.getInt(3), obResultSet.getString(4), obResultSet.getString(5), obResultSet.getInt(6), obResultSet.getInt(7), obResultSet.getInt(8)));
        }
        catch(SQLException ex)
        {
            showError("List<Refund> getRefunds(Account a)", s, ex.toString());
        }
        
        return lst;
    }
    public static boolean addRefund(Refund r)
    {
        r.setRecordID(getNextRecordID(r, DATABASE_TABLE_REFUND));
        
        String sBuild = "INSERT INTO " + getTableName(r, DATABASE_TABLE_REFUND) + " (RECORD_ID, REFUND_DATE, REFUND_NAME, REFUND_KEYWORDS, REFUND_AMOUNT, RECEIVED_DATE, RECEIVED_PRIMARY_KEY) VALUES (" +
                r.getRecordID()+ ", " +
                r.getDate()+ ", " +
                formatSQLInsert(r.getName(), DATABASE_RECORD_MAXIMUM_LENGTH_256) + ", " +
                formatSQLInsert(r.getKeywordsSerialised(), DATABASE_RECORD_MAXIMUM_LENGTH_1024) + ", " +
                r.getAmount() + ", " +
                r.getReceivedDate() + ", " +
                r.getReceivedPrimaryKey() + ")";
        
        if(runSQLUpdate(sBuild))    setPrimaryKey(r);
        else                        return false;
        
        return true;
    }
    public static boolean addRefunds(List<Refund> lst)
    {
        for(Refund r : lst)
            if(!addRefund(r))
                return false;
        
        return true;
    }
    public static boolean deleteRefund(Refund r)
    {
        return runSQLUpdate("DELETE FROM " + getTableName(r, DATABASE_TABLE_REFUND) + " WHERE PRIMARY_KEY = " + r.getPrimaryKey());
    }
    public static boolean updateRefundReceived(Account a, Refund r)
    {
        if(!runSQLUpdate("UPDATE " + getTableName(a, DATABASE_TABLE_REFUND) + " SET " +
                "RECEIVED_DATE = " + r.getReceivedDate() + ", " +
                "RECEIVED_PRIMARY_KEY = " + r.getReceivedPrimaryKey() + " " +
                "WHERE PRIMARY_KEY = " + r.getPrimaryKey()))
            
            return false;
        
        return false;
    }
    public static boolean updateRefund(Account a, Refund r)
    {
        String sBuild = "UPDATE " + getTableName(r, DATABASE_TABLE_REFUND) + " SET " +
                "REFUND_DATE = " + r.getDate() + ", " +
                "REFUND_NAME = " + formatSQLInsert(r.getName(), DATABASE_RECORD_MAXIMUM_LENGTH_256) + ", " +
                "REFUND_KEYWORDS = " + formatSQLInsert(r.getKeywordsSerialised(), DATABASE_RECORD_MAXIMUM_LENGTH_1024) + ", " +
                "REFUND_AMOUNT = " + r.getAmount() + " " +
                "WHERE PRIMARY_KEY = " + r.getPrimaryKey();
        
        if(!runSQLUpdate(sBuild))
            return false;
        
        return true;
    }
    public static long getNumberOfRefunds(Account a)
    {
        return getNumberOfRecords(a, DATABASE_TABLE_REFUND);
    }
    
    //Reminders
    public static List<Reminder> getReminders()
    {
        ResultSet obResultSet;
        List<Reminder> lst = new ArrayList<>();
        String s = "";
        
        try
        {
            s = "SELECT * FROM " + getTableName(DATABASE_TABLE_REMINDER);
            obResultSet = obStatement.executeQuery(s);
        
            while(obResultSet.next())
                lst.add(new Reminder(obResultSet.getLong(1), obResultSet.getLong(2), obResultSet.getInt(3), obResultSet.getString(4), obResultSet.getString(5), obResultSet.getInt(6)));
        }
        catch(SQLException ex)
        {
            showError("List<Reminder> getReminders()", s, ex.toString());
        }
        
        return lst;
    }
    public static boolean addReminder(Reminder r)
    {
        r.setRecordID(getNextRecordID(DATABASE_TABLE_REMINDER));
        
        String sBuild = "INSERT INTO " + getTableName(DATABASE_TABLE_REMINDER) + " (RECORD_ID, REMINDER_DATE, REMINDER_NAME, REMINDER_DETAILS, REMINDER_DATE_ALERT) VALUES (" +
                r.getRecordID()+ ", " +
                r.getDate()+ ", " +
                formatSQLInsert(r.getName(), DATABASE_RECORD_MAXIMUM_LENGTH_256) + ", " +
                formatSQLInsert(r.getDetails(), DATABASE_RECORD_MAXIMUM_LENGTH_1024) + ", " +
                r.getDateAlert() + ")";
        
        if(runSQLUpdate(sBuild))    setPrimaryKey(r);
        else                        return false;
        
        return true;
    }
    public static boolean addReminders(List<Reminder> lst)
    {
        for(Reminder r : lst)
            if(!addReminder(r))
                return false;
        
        return true;
    }
    public static boolean deleteReminder(Reminder r)
    {
        return runSQLUpdate("DELETE FROM " + getTableName(DATABASE_TABLE_REMINDER) + " WHERE PRIMARY_KEY = " + r.getPrimaryKey());
    }
    public static boolean updateReminder(Reminder r)
    {
        String sBuild = "UPDATE " + getTableName(DATABASE_TABLE_REMINDER) + " SET " +
                "REMINDER_DATE = " + r.getDate() + ", " +
                "REMINDER_NAME = " + formatSQLInsert(r.getName(), DATABASE_RECORD_MAXIMUM_LENGTH_256) + ", " +
                "REMINDER_DETAILS = " + formatSQLInsert(r.getDetails(), DATABASE_RECORD_MAXIMUM_LENGTH_1024) + ", " +
                "REMINDER_DATE_ALERT = " + r.getDateAlert()+ " " +
                "WHERE PRIMARY_KEY = " + r.getPrimaryKey();
        
        if(!runSQLUpdate(sBuild))
            return false;
        
        return true;
    }
    public static long getNumberOfReminders()
    {
        return getNumberOfRecords(DATABASE_TABLE_REMINDER);
    }
    
    //Error
    public static void clearError()
    {
        iErrorCounter = 1;
        tvErrors.getItems().clear();
        tvErrors.getItems().add(new Blank());
    }
}