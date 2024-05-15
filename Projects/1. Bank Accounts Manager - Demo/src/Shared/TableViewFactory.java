package Shared;

import Accounts.AccountsTableInsertable;
import Categories.CategoriesTableInsertable;
import java.util.List;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.CheckBox;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

public class TableViewFactory implements Constants{

    private static <T> TableView buildTable(String[] sColumnNames, String[] sColumnProperties, int[] iColumnWidths, double dTopOffset, int iFontSize, SelectionMode enSelectionMode, T blankItem)
    {
        TableView<T> tv                         = new TableView<>();
        TableColumn<T,String>[] tcTableColumns  = new TableColumn[sColumnNames.length];
        
        for(int i = 0 ; i<tcTableColumns.length ; i++)
        {
            tcTableColumns[i] = new TableColumn(sColumnNames[i]);
            tcTableColumns[i].setCellValueFactory(new PropertyValueFactory<>(sColumnProperties[i]));
            tcTableColumns[i].setMinWidth(iColumnWidths[i]);
            tcTableColumns[i].setMaxWidth(iColumnWidths[i]);
            tcTableColumns[i].setSortable(false);
            tcTableColumns[i].setEditable(false);
        }
        
        tv.getColumns().addAll(tcTableColumns);
        tv.getItems().add(blankItem);
        tv.setEditable(false);
        tv.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);        
        tv.getSelectionModel().setSelectionMode(enSelectionMode);
        
        if(iFontSize != DEFAULT_FONT)
            tv.setStyle("-fx-font: " + iFontSize + " calibri;");
        
        AnchorPane.setTopAnchor(tv, dTopOffset);
        AnchorPane.setBottomAnchor(tv, 0.0);
        AnchorPane.setLeftAnchor(tv, 0.0);
        AnchorPane.setRightAnchor(tv, 0.0);
        
        return tv;
    }
    private static <T extends AccountsTableInsertable> TableView buildTableForAccounts(String[] sColumnNames, String[] sColumnProperties, int[] iColumnWidths, int[] iColumnTypes, double dTopOffset, List<T> lstData)
    {
        TableView<T> tv = new TableView<>();
        TableColumn<T,T>[] tcTableColumns = new TableColumn[sColumnNames.length];
        
        for(int i = 0 ; i<tcTableColumns.length ; i++)
        {
            tcTableColumns[i] = new TableColumn(sColumnNames[i]);
            
            tcTableColumns[i].setMinWidth(iColumnWidths[i]);
            tcTableColumns[i].setMaxWidth(iColumnWidths[i]);
            tcTableColumns[i].setSortable(false);
            tcTableColumns[i].setEditable(false);
            
            switch(iColumnTypes[i])
            {
                case TC_STRING:
                {
                    tcTableColumns[i].setCellValueFactory(new PropertyValueFactory<>(sColumnProperties[i]));
                    
                    break;
                }
                case TC_CHECKBOX:
                {
                    tcTableColumns[i].setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue()));
                    tcTableColumns[i].setStyle("-fx-alignment: CENTER;");
                    tcTableColumns[i].setCellFactory(tc -> new TableCell<T, T>()
                    {
                        @Override
                        protected void updateItem(T item, boolean bEmpty) {
                            super.updateItem(item, bEmpty);

                            if (item == null || bEmpty || item instanceof Blank || item instanceof Accounts.All.All)
                                setGraphic(null);
                            else
                            {
                                CheckBox cb = new CheckBox();
                                
                                item.initViewCheckbox(cb);
                                setGraphic(cb);
                            }
                        }
                    });
                    
                    break;
                }
            }
        }
        
        tv.getColumns().addAll(tcTableColumns);
        tv.getItems().addAll(lstData);
        tv.setEditable(false);
        tv.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tv.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tv.setMinHeight(0);

        AnchorPane.setTopAnchor(tv, dTopOffset);
        AnchorPane.setBottomAnchor(tv, 0.0);
        AnchorPane.setLeftAnchor(tv, 0.0);
        AnchorPane.setRightAnchor(tv, 0.0);
        
        return tv;
    }
    private static <T extends CategoriesTableInsertable> TableView buildTableForCategories(String[] sColumnNames, String[] sColumnProperties, int[] iColumnWidths, int[] iColumnTypes, double dTopOffset, T blankItem)
    {
        TableView<T> tv = new TableView<>();
        TableColumn<T,T>[] tcTableColumns = new TableColumn[sColumnNames.length];
        
        for(int i = 0 ; i<tcTableColumns.length ; i++)
        {
            tcTableColumns[i] = new TableColumn(sColumnNames[i]);
            
            tcTableColumns[i].setMinWidth(iColumnWidths[i]);
            tcTableColumns[i].setMaxWidth(iColumnWidths[i]);
            tcTableColumns[i].setSortable(false);
            tcTableColumns[i].setEditable(false);
            
            switch(iColumnTypes[i])
            {
                case TC_STRING:
                {
                    tcTableColumns[i].setCellValueFactory(new PropertyValueFactory<>(sColumnProperties[i]));
                    
                    break;
                }
                case TC_CHECKBOX:
                {
                    tcTableColumns[i].setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue()));
                    tcTableColumns[i].setStyle( "-fx-alignment: CENTER;");
                    tcTableColumns[i].setCellFactory(tc -> new TableCell<T, T>()
                    {
                        @Override
                        protected void updateItem(T item, boolean bEmpty) {
                            super.updateItem(item, bEmpty);

                            if(item == null || bEmpty || item instanceof Blank)
                                setGraphic(null);
                            else
                            {
                                CheckBox cb = new CheckBox();
                                
                                item.initViewCheckbox(cb);
                                setGraphic(cb);
                            }
                        }
                    });
                    
                    break;
                }
            }
        }
        
        tv.getColumns().addAll(tcTableColumns);
        tv.getItems().add(blankItem);
        tv.setEditable(false);
        tv.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tv.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tv.setMinHeight(0);

        AnchorPane.setTopAnchor(tv, dTopOffset);
        AnchorPane.setBottomAnchor(tv, 0.0);
        AnchorPane.setLeftAnchor(tv, 0.0);
        AnchorPane.setRightAnchor(tv, 0.0);
        
        return tv;
    }
    
    private static TableView buildTableForCategories()
    {
        String[] sColumnNames       = {"View Hist.", "", "Freq.", "Total"};
        String[] sColumnProperties  = {"View", "name", "frequency", "total"};
        int[] iColumnTypes          = {TC_CHECKBOX, TC_STRING, TC_STRING, TC_STRING};
        int[] iColumnWidths         = {75, 425, 60, 75};
        
        return TableViewFactory.buildTableForCategories(sColumnNames, sColumnProperties, iColumnWidths, iColumnTypes, TC_TOP_OFFSET, new Blank());
    }
    private static TableView buildTableForCategoriesAdministration()
    {
        String[] sColumnNames       = {"Category Name", "Direction", "Frequency", "First Entry", "Last Entry", "Total"};
        String[] sColumnProperties  = {"name", "direction", "frequencyAll", "firstAll", "lastAll", "totalAll"};
        int[] iColumnWidths         = {695, 75, 75, 75, 75, 75};
        
        return TableViewFactory.buildTable(sColumnNames, sColumnProperties, iColumnWidths, 0, DEFAULT_FONT, SelectionMode.MULTIPLE, new Blank());
    }
    private static TableView buildTableForAutoCategories()
    {
        String[] sColumnNames       = {"Entry Name", "Auto Assigned", "Direction", "Frequency", "First Entry", "Last Entry"};
        String[] sColumnProperties  = {"entryName", "categoryName", "direction", "frequency", "first", "last"};
        int[] iColumnWidths         = {380, 390, 75, 75, 75, 75};
        
        return TableViewFactory.buildTable(sColumnNames, sColumnProperties, iColumnWidths, 0, DEFAULT_FONT, SelectionMode.MULTIPLE, new Blank());
    }
    private static TableView buildTableForHistory()
    {
        String[] sColumnNames       = {"Date", "Acc.", "Name", "Type", "Category", "In", "Out", "Balance"};
        String[] sColumnProperties  = {"date", "accountNameAbbreviated" ,"name", "type", "categoryName", "in", "out", "balance"};
        int[] iColumnWidths         = {75, 75, 250, 40, 150, 75, 75, 90};
        
        return TableViewFactory.buildTable(sColumnNames, sColumnProperties, iColumnWidths, 0, DEFAULT_FONT, SelectionMode.MULTIPLE, new Blank());
    }
    private static TableView buildTableForHistoryWithHeaderOffset()
    {
        String[] sColumnNames       = {"Date", "Acc.", "Name", "Type", "Category", "In", "Out", "Balance"};
        String[] sColumnProperties  = {"date", "accountNameAbbreviated" ,"name", "type", "categoryName", "in", "out", "balance"};
        int[] iColumnWidths         = {75, 75, 250, 40, 150, 75, 75, 90};
        
        return TableViewFactory.buildTable(sColumnNames, sColumnProperties, iColumnWidths, TC_TOP_OFFSET, DEFAULT_FONT, SelectionMode.MULTIPLE, new Blank());
    }
    private static TableView buildTableForStatement()
    {
        String[] sColumnNames       = {"Date", "Name", "Type", "Category", "In", "Out", "Balance"};
        String[] sColumnProperties  = {"date", "name", "type", "categoryName", "in", "out", "balance"};
        int[] iColumnWidths         = {100, 400, 75, 270, 75, 75, 75};
        
        return TableViewFactory.buildTable(sColumnNames, sColumnProperties, iColumnWidths, 0, DEFAULT_FONT, SelectionMode.MULTIPLE, new Blank());
    }
    private static TableView buildTableForStatementErrors()
    {
        String[] sColumnNames       = {"No.", "Error", "Line/Details"};
        String[] sColumnProperties  = {"number", "error", "details"};
        int[] iColumnWidths         = {50, 320, 700};
        
        return TableViewFactory.buildTable(sColumnNames, sColumnProperties, iColumnWidths, 0, DEFAULT_FONT, SelectionMode.SINGLE, new Blank());
    }
    private static TableView buildTableForRefunds()
    {
        String[] sColumnNames       = {"Date", "Name/Merchant", "Lookout Keywords", "Amount", "Status"};
        String[] sColumnProperties  = {"date", "name", "keywords", "amount", "status"};
        int[] iColumnWidths         = {75, 200, 440, 75, 280};
        
        return TableViewFactory.buildTable(sColumnNames, sColumnProperties, iColumnWidths, 0, DEFAULT_FONT, SelectionMode.MULTIPLE, new Blank());
    }
    private static TableView buildTableForRefundPotentials()
    {
        String[] sColumnNames       = {"Date", "Acc.", "Name", "Type", "Category", "In", "Out", "Balance"};
        String[] sColumnProperties  = {"date", "accountNameAbbreviated" ,"name", "type", "categoryName", "in", "out", "balance"};
        int[] iColumnWidths         = {75, 75, 300, 40, 340, 75, 75, 90};
        
        return TableViewFactory.buildTable(sColumnNames, sColumnProperties, iColumnWidths, 0, DEFAULT_FONT, SelectionMode.SINGLE, new Blank());
    }
    private static TableView buildTableForWatches()
    {
        String[] sColumnNames       = {"Category Name", "From", "Amount", "Status"};
        String[] sColumnProperties  = {"categoryName", "lastDate", "lastAmount", "status"};
        int[] iColumnWidths         = {400, 75, 75, 520};
        
        return TableViewFactory.buildTable(sColumnNames, sColumnProperties, iColumnWidths, 0, DEFAULT_FONT, SelectionMode.MULTIPLE, new Blank());
    }
    private static TableView buildTableForHistoryWithinAdministration()
    {
        String[] sColumnNames       = {"Date", "Acc.", "Name", "Type", "Category", "In", "Out", "Balance"};
        String[] sColumnProperties  = {"date", "accountNameAbbreviated" ,"name", "type", "categoryName", "in", "out", "balance"};
        int[] iColumnWidths         = {75, 75, 370, 40, 270, 75, 75, 90};
        
        return TableViewFactory.buildTable(sColumnNames, sColumnProperties, iColumnWidths, 0, DEFAULT_FONT, SelectionMode.MULTIPLE, new Blank());
    }
    private static TableView buildTableForReminders()
    {
        String[] sColumnNames       = {"", "Remind", "Name", "Details", "Created"};
        String[] sColumnProperties  = {"status", "dateAlert", "name", "details", "date"};
        int[] iColumnWidths         = {50, 75, 300, 545, 100};
        
        return TableViewFactory.buildTable(sColumnNames, sColumnProperties, iColumnWidths, 0, DEFAULT_FONT, SelectionMode.MULTIPLE, new Blank());
    }
    private static TableView buildTableForIntegrityChecks()
    {
        String[] sColumnNames       = {" ", "Account", "Check", "Errors"};
        String[] sColumnProperties  = {"pad", "accountName", "description", "status"};
        int[] iColumnWidths         = {50, 200, 250, 570};
        
        return TableViewFactory.buildTable(sColumnNames, sColumnProperties, iColumnWidths, 0, DEFAULT_FONT, SelectionMode.SINGLE, new Blank());
    }
    private static TableView buildTableForIntegrityErrors()
    {
        String[] sColumnNames       = {"No.", "Error", "Details"};
        String[] sColumnProperties  = {"number", "error", "details"};
        int[] iColumnWidths         = {50, 400, 620};
        
        return TableViewFactory.buildTable(sColumnNames, sColumnProperties, iColumnWidths, 0, DEFAULT_FONT, SelectionMode.SINGLE, new Blank());
    }
    private static TableView buildTableForDatabaseOverview()
    {
        String[] sColumnNames       = {" ", "Account", "Table", "Number of Records"};
        String[] sColumnProperties  = {"pad", "account", "table", "numberOfRecords"};
        int[] iColumnWidths         = {50, 200, 250, 570};
        
        return TableViewFactory.buildTable(sColumnNames, sColumnProperties, iColumnWidths, 0, DEFAULT_FONT, SelectionMode.SINGLE, new Blank());
    }
    private static TableView buildTableForDatabaseEntries()
    {
        String[] sColumnNames       = {"PRIKEY", "RECORD_ID", "ENTRY_DATE", "ENTRY_NAME", "ENTRY_TYPE", "ENTRY_CATEGORY", "ENTRY_IN", "ENTRY_OUT", "ENTRY_BAL."};
        String[] sColumnProperties  = {"primaryKey", "recordID", "date", "name", "type", "categoryName", "in", "out", "balance"};
        int[] iColumnWidths         = {75, 75, 75, 280, 75, 265, 75, 75, 75};
        
        return TableViewFactory.buildTable(sColumnNames, sColumnProperties, iColumnWidths, 0, DEFAULT_FONT, SelectionMode.MULTIPLE, new Blank());
    }
    private static TableView buildTableForDatabaseCategories()
    {
        String[] sColumnNames       = {"PRIKEY", "RECORD_ID", "CATEGORY_NAME", "CATEGORY_DIRECTION"};
        String[] sColumnProperties  = {"primaryKey", "recordID", "name", "direction"};
        int[] iColumnWidths         = {75, 75, 469, 456};
        
        return TableViewFactory.buildTable(sColumnNames, sColumnProperties, iColumnWidths, 0, DEFAULT_FONT, SelectionMode.MULTIPLE, new Blank());
    }
    private static TableView buildTableForDatabaseAutoCategories()
    {
        String[] sColumnNames       = {"PRIKEY", "RECORD_ID", "ENTRY_NAME", "ENTRY_DIRECTION", "ASSIGN_NAME"};
        String[] sColumnProperties  = {"primaryKey", "recordID", "entryName", "direction", "categoryName"};
        int[] iColumnWidths         = {75, 75, 313, 299, 313};
        
        return TableViewFactory.buildTable(sColumnNames, sColumnProperties, iColumnWidths, 0, DEFAULT_FONT, SelectionMode.MULTIPLE, new Blank());
    }
    private static TableView buildTableForDatabaseWatches()
    {
        String[] sColumnNames       = {"PRIKEY", "RECORD_ID", "CATEGORY_NAME", "CATEGORY_DIRECTION", "LAST_DATE", "LAST_WATCH"};
        String[] sColumnProperties  = {"primaryKey", "recordID", "categoryName", "direction", "lastDate", "lastAmount"};
        int[] iColumnWidths         = {75, 75, 379, 379, 75, 87};
        
        return TableViewFactory.buildTable(sColumnNames, sColumnProperties, iColumnWidths, 0, DEFAULT_FONT, SelectionMode.MULTIPLE, new Blank());
    }
    private static TableView buildTableForDatabaseRefunds()
    {
        String[] sColumnNames       = {"PRIKEY", "RECORD_ID", "REFUND_DATE", "REFUND_NAME", "REFUND_KEYWORDS", "REFUND_AMOUNT", "REC. DATE", "REC. PRIKEY"};
        String[] sColumnProperties  = {"primaryKey", "recordID", "date", "name", "keywords", "amount", "receivedDate", "receivedPrimaryKey"};
        int[] iColumnWidths         = {75, 75, 100, 250, 300, 120, 75, 75};
        
        return TableViewFactory.buildTable(sColumnNames, sColumnProperties, iColumnWidths, 0, DEFAULT_FONT, SelectionMode.MULTIPLE, new Blank());
    }
    private static TableView buildTableForDatabaseReminders()
    {
        String[] sColumnNames       = {"PRIKEY", "RECORD_ID", "REMINDER_DATE", "REMINDER_NAME", "REMINDER_DETAILS", "REMINDER_DATE_ALERT"};
        String[] sColumnProperties  = {"primaryKey", "recordID", "date", "name", "details", "dateAlert"};
        int[] iColumnWidths         = {75, 75, 150, 200, 420, 150};
        
        return TableViewFactory.buildTable(sColumnNames, sColumnProperties, iColumnWidths, 0, DEFAULT_FONT, SelectionMode.MULTIPLE, new Blank());
    }
    private static TableView buildTableForDatabaseErrors()
    {
        String[] sColumnNames       = {"No.", "Method", "SQL", "Exception Details"};
        String[] sColumnProperties  = {"number", "method", "SQL", "exceptionDetails"};
        int[] iColumnWidths         = {50, 420, 500, 500};
        
        return TableViewFactory.buildTable(sColumnNames, sColumnProperties, iColumnWidths, 0, DEFAULT_FONT, SelectionMode.SINGLE, new Blank());
    }
    private static TableView buildTableForDatabaseImport()
    {
        String[] sColumnNames       = {" ", "Import", "Details"};
        String[] sColumnProperties  = {"pad", "item", "details"};
        int[] iColumnWidths         = {50, 400, 620};
        
        return TableViewFactory.buildTable(sColumnNames, sColumnProperties, iColumnWidths, 0, DEFAULT_FONT, SelectionMode.SINGLE, new Blank());
    }
    private static TableView buildTableForDatabaseExport()
    {
        String[] sColumnNames       = {" ", "Export", "Details"};
        String[] sColumnProperties  = {"pad", "item", "details"};
        int[] iColumnWidths         = {50, 400, 620};
        
        return TableViewFactory.buildTable(sColumnNames, sColumnProperties, iColumnWidths, 0, DEFAULT_FONT, SelectionMode.SINGLE, new Blank());
    }
    private static TableView buildTableForDatabaseImportExportError()
    {
        String[] sColumnNames       = {"No.", "Error", "Details"};
        String[] sColumnProperties  = {"number", "error", "details"};
        int[] iColumnWidths         = {50, 400, 620};
        
        return TableViewFactory.buildTable(sColumnNames, sColumnProperties, iColumnWidths, 0, DEFAULT_FONT, SelectionMode.SINGLE, new Blank());
    }
    
    //External API -------------------------------------------------------------
    public static TableView buildTable(List<AccountsTableInsertable> lst)
    {
        String[] sColumnNames       = {"View", "Account", "Bal Start", "In", "Out", "-/+", "Bal End"};
        String[] sColumnProperties  = {"View", "Name", "balanceAtStart", "incomingTotal", "outgoingTotal", "balanceChange", "balanceAtEnd"};
        int[] iColumnTypes          = {TC_CHECKBOX, TC_STRING, TC_STRING, TC_STRING, TC_STRING, TC_STRING, TC_STRING};
        int[] iColumnWidths         = {75, 185, 75, 75, 75, 75, 75};
        
        return TableViewFactory.buildTableForAccounts(sColumnNames, sColumnProperties, iColumnWidths, iColumnTypes, TC_TOP_OFFSET, lst);
    }
    public static TableView buildTable(int iID)
    {
        switch(iID)
        {
            case TABLEVIEW_ID_CATEGORIES:                       return buildTableForCategories();
            case TABLEVIEW_ID_CATEGORIES_ADMINISTRATION:        return buildTableForCategoriesAdministration();
            case TABLEVIEW_ID_HISTORY:                          return buildTableForHistory();
            case TABLEVIEW_ID_HISTORY_WITH_HEADER_OFFSET:       return buildTableForHistoryWithHeaderOffset();
            case TABLEVIEW_ID_HISTORY_WITHIN_ADMINISTRATION:    return buildTableForHistoryWithinAdministration();
            case TABLEVIEW_ID_AUTO_CATEGORIES:                  return buildTableForAutoCategories();
            case TABLEVIEW_ID_REMINDERS:                        return buildTableForReminders();
            
            case TABLEVIEW_ID_DATABASE_OVERVIEW:                return buildTableForDatabaseOverview();
            case TABLEVIEW_ID_DATABASE_ENTRIES:                 return buildTableForDatabaseEntries();
            case TABLEVIEW_ID_DATABASE_CATEGORIES:              return buildTableForDatabaseCategories();
            case TABLEVIEW_ID_DATABASE_AUTO_CATEGORIES:         return buildTableForDatabaseAutoCategories();
            case TABLEVIEW_ID_DATABASE_WATCHES:                 return buildTableForDatabaseWatches();
            case TABLEVIEW_ID_DATABASE_REFUNDS:                 return buildTableForDatabaseRefunds();
            case TABLEVIEW_ID_DATABASE_REMINDERS:               return buildTableForDatabaseReminders();
            case TABLEVIEW_ID_DATABASE_ERRORS:                  return buildTableForDatabaseErrors();
            
            case TABLEVIEW_ID_DATABASE_IMPORT:                  return buildTableForDatabaseImport();
            case TABLEVIEW_ID_DATABASE_EXPORT:                  return buildTableForDatabaseExport();
            case TABLEVIEW_ID_DATABASE_IMPORT_EXPORT_ERRROR:    return buildTableForDatabaseImportExportError();
            
            case TABLEVIEW_ID_STATEMENT:                        return buildTableForStatement();
            case TABLEVIEW_ID_STATEMENT_ERRORS:                 return buildTableForStatementErrors();
            case TABLEVIEW_ID_REFUNDS:                          return buildTableForRefunds();
            case TABLEVIEW_ID_REFUNDS_POTENTIALS:               return buildTableForRefundPotentials();
            case TABLEVIEW_ID_WATCHES:                          return buildTableForWatches();
            case TABLEVIEW_ID_INTEGRITY_CHECKS:                 return buildTableForIntegrityChecks();
            case TABLEVIEW_ID_INTEGRITY_ERRORS:                 return buildTableForIntegrityErrors();
        }
        return null;
    }
}
