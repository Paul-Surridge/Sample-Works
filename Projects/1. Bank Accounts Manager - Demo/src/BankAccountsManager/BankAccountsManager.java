package BankAccountsManager;

import DateRange.DateRange;
import History.History;
import Accounts.Accounts;
import Administration.Administration;
import AutoCategories.AutoCategories;
import Categories.Categories;
import Categories.Administration.CategoriesAdministration;
import Charts.Charts;
import Charts.StackedBarChart.StackedBarChart;
import DDSO.DDSO;
import Shared.Constants;
import Database.Administration.DatabaseAdministration;
import Database.Database;
import FullScreen.FullScreen;
import Integrity.Integrity;
import Refunds.Refunds;
import Reminders.Reminders;
import Shared.Info;
import Shared.Popup;
import Shared.Windows;
import Statement.Statement;
import Watches.Watches;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

public class BankAccountsManager implements Initializable, Constants {
    
    @FXML
    private AnchorPane apApp;
    
    @FXML
    private SplitPane spApp;
    
        //Split Pane 1 [Left]
        @FXML
        private SplitPane spAccCat;
            @FXML
            private AnchorPane apAccounts;
                @FXML
                private ImageView   ivAdministration;
                @FXML
                private ImageView   ivDateRange;
                @FXML
                private Label       labDateRange;

            @FXML
            private AnchorPane apCategoriesIn;
                @FXML
                private Label       labCategoriesInHeader;
                @FXML
                private CheckBox    cbCategoriesInViewAll;

            @FXML
            private AnchorPane apCategoriesOut;
                @FXML
                private Label       labCategoriesOutHeader;
                @FXML
                private CheckBox    cbCategoriesOutViewAll;
            
        //Split Pane 2 [Middle]
        @FXML
        private SplitPane spCharts;
            @FXML
            private AnchorPane apStackedBarChart;
                @FXML
                private Rectangle   recStackedBarChartNodeValue;
                @FXML
                private Label       labStackedBarChartNodeValue;
                
                //Dialog:   Zoom Custom
                @FXML
                private AnchorPane      apStackedBarChartZoom;
                    @FXML
                    private Rectangle   recStackedBarChartZoomAmount;
                    @FXML
                    private TextField   tfStackedBarChartZoomAmount;
                    @FXML
                    private Button      btStackedBarChartZoomSet;
            @FXML
            private AnchorPane apPieChart;
                @FXML
                private Rectangle   recPieChartNodeValue;
                @FXML
                private Label       labPieChartNodeValue;
            @FXML
            private AnchorPane apLineChart;
                @FXML
                private Rectangle   recLineChartNodeValue;
                @FXML
                private Label       labLineChartNodeValue;
            
        //Split Pane 3 [Right]
        @FXML
        private AnchorPane apHistory;
            @FXML
            private ImageView ivHistorySearch;
                @FXML
                private AnchorPane      apHistorySearch;
                    @FXML
                    private AnchorPane  apHistorySearchListView;
                    @FXML
                    private TextField   tfHistorySearch;
                    @FXML
                    private Rectangle   recHistorySearch;
            @FXML
            private ImageView ivHistorySearchClose;
                
        //Dialog:   Date Range - Set Range
        @FXML
        private AnchorPane      apDateRange;
            @FXML
            private Rectangle   recDateRange;
            @FXML
            private DatePicker  dpDateRangeFrom;
            @FXML
            private DatePicker  dpDateRangeTo;
            @FXML
            private Button      btDateRangeQuickJump;
            @FXML
            private Button      btDateRangeMostRecent;
            @FXML
            private Button      btDateRangeAll;
            @FXML
            private Button      btDateRangeSet;
            
            @FXML
            private AnchorPane      apDateRangeQuickJump;
                @FXML
                private Rectangle   recDateRangeQuickJump;
                @FXML
                private Button      btDateRangeQuickJump2010;
                @FXML
                private Button      btDateRangeQuickJump2011;
                @FXML
                private Button      btDateRangeQuickJump2012;
                @FXML
                private Button      btDateRangeQuickJump2013;
                @FXML
                private Button      btDateRangeQuickJump2014;
                @FXML
                private Button      btDateRangeQuickJump2015;
                @FXML
                private Button      btDateRangeQuickJump2016;
                @FXML
                private Button      btDateRangeQuickJump2017;
                @FXML
                private Button      btDateRangeQuickJump2018;
                @FXML
                private Button      btDateRangeQuickJump2019;
                @FXML
                private Button      btDateRangeQuickJump2020;
                @FXML
                private Button      btDateRangeQuickJump2021;
                @FXML
                private Button      btDateRangeQuickJump2022;
                @FXML
                private Button      btDateRangeQuickJump2023;
                @FXML
                private Button      btDateRangeQuickJump2024;
                @FXML
                private Button      btDateRangeQuickJump2025;
                @FXML
                private Button      btDateRangeQuickJump2026;
                @FXML
                private Button      btDateRangeQuickJump2027;
                @FXML
                private Button      btDateRangeQuickJump2028;
                @FXML
                private Button      btDateRangeQuickJump2029;
                @FXML
                private Button      btDateRangeQuickJumpJan;
                @FXML
                private Button      btDateRangeQuickJumpFeb;
                @FXML
                private Button      btDateRangeQuickJumpMar;
                @FXML
                private Button      btDateRangeQuickJumpApr;
                @FXML
                private Button      btDateRangeQuickJumpMay;
                @FXML
                private Button      btDateRangeQuickJumpJun;
                @FXML
                private Button      btDateRangeQuickJumpJul;
                @FXML
                private Button      btDateRangeQuickJumpAug;
                @FXML
                private Button      btDateRangeQuickJumpSept;
                @FXML
                private Button      btDateRangeQuickJumpOct;
                @FXML
                private Button      btDateRangeQuickJumpNov;
                @FXML
                private Button      btDateRangeQuickJumpDec;
                @FXML
                private Button      btDateRangeQuickJumpSet;

            @FXML
            private AnchorPane      apDateRangeMostRecent;
                @FXML
                private Rectangle   recDateRangeMostRecent;
                @FXML
                private Button      btDateRangeMostRecentMonth1;
                @FXML
                private Button      btDateRangeMostRecentMonth3;
                @FXML
                private Button      btDateRangeMostRecentMonth6;
                @FXML
                private Button      btDateRangeMostRecentMonth12;
                @FXML
                private Button      btDateRangeMostRecentMonth18;
            
    //Dialog:   Administration
    @FXML
    private ScrollPane spAdministration;
        @FXML
        private TabPane tpAdministration;
        
            //Tab:   Statement
            @FXML
            private SplitPane   spStatementTables;
                @FXML
                private AnchorPane  apStatementTable;
            @FXML
            private RadioButton rbStatementCurrent;
            @FXML
            private RadioButton rbStatementSaving;
            @FXML
            private RadioButton rbStatementISA;
            @FXML
            private Button      btStatementOpenFile;
            @FXML
            private Button      btStatementClear;
            @FXML
            private Button      btStatementAdd;
            @FXML
            private Button      btStatementClose;
            @FXML
            private RadioButton rbStatementEntriesCategorised;
            @FXML
            private RadioButton rbStatementEntriesUndefined;
            @FXML
            private RadioButton rbStatementEntriesAll;
            @FXML
            private Label       labStatementStatusMessage;

            //Tab:   Refunds
            @FXML
            private SplitPane   spRefundsTables;
                @FXML
                private AnchorPane  apRefundsTable;
            @FXML
            private RadioButton rbRefundsCurrent;
            @FXML
            private RadioButton rbRefundsSaving;
            @FXML
            private RadioButton rbRefundsISA;
            @FXML
            private Button      btRefundsClose;
            
                //Dialog:   Refund Create/Edit Confirmation
                @FXML
                private AnchorPane      apRefundCreateEdit;
                    @FXML
                    private TextField   tfRefundCreateEditName;
                    @FXML
                    private TextField   tfRefundCreateEditKeywords;
                    @FXML
                    private TextField   tfRefundCreateEditAmountPounds;
                    @FXML
                    private TextField   tfRefundCreateEditAmountPence;
                    @FXML
                    private DatePicker  dpRefundCreateEdit;
                    @FXML
                    private Button      btRefundCreateEdit;
                    
                //Dialog:   Refund Delete Confirmation
                @FXML
                private AnchorPane      apRefundDeleteConfirm;
                    @FXML
                    private Button      btRefundDeleteConfirm;
                    
            //Tab:   Watches
            @FXML
            private SplitPane   spWatchesTables;
                @FXML
                private AnchorPane  apWatchesTable;
            @FXML
            private RadioButton rbWatchesCurrent;
            @FXML
            private RadioButton rbWatchesSaving;
            @FXML
            private RadioButton rbWatchesISA;
            @FXML
            private RadioButton rbWatchesIncoming;
            @FXML
            private RadioButton rbWatchesOutgoing;
            @FXML
            private Button      btWatchesClose;
            
                //Dialog:   Watch Delete Confirmation
                @FXML
                private AnchorPane      apWatchDeleteConfirm;
                    @FXML
                    private Button      btWatchDeleteConfirm;
            
            //Tab:  Categories
            @FXML
            private SplitPane   spCategories;
                @FXML
                private AnchorPane  apCategoriesInTable;
                @FXML
                private AnchorPane  apCategoriesOutTable;
            @FXML
            private RadioButton rbCategoriesCurrent;
            @FXML
            private RadioButton rbCategoriesSaving;
            @FXML
            private RadioButton rbCategoriesISA;
            @FXML
            private Button      btCategoriesClose;
            
                //Dialog:   Category Delete Confirmation
                @FXML
                private AnchorPane      apCategoryDeleteConfirm;
                    @FXML
                    private Button      btCategoryDeleteConfirm;

                //Dialog:   Category Create
                @FXML
                private AnchorPane      apCategoryCreate;
                    @FXML
                    private TextField   tfCategoryCreate;
                    @FXML
                    private Button      btCategoryCreate;

                //Dialog:   Category Rename 
                @FXML
                private AnchorPane      apCategoryRename;
                    @FXML
                    private TextField   tfCategoryRename;
                    @FXML
                    private Button      btCategoryRename;
                    
            //Tab:   Auto Categories
            @FXML
            private SplitPane   spAutoCategories;
                @FXML
                private AnchorPane  apAutoCategoriesInTable;
                @FXML
                private AnchorPane  apAutoCategoriesOutTable;
            @FXML
            private RadioButton rbAutoCategoriesCurrent;
            @FXML
            private RadioButton rbAutoCategoriesSaving;
            @FXML
            private RadioButton rbAutoCategoriesISA;
            @FXML
            private Button      btAutoCategoriesClose;

                //Dialog:   Auto Category Delete Confirmation
                @FXML
                private AnchorPane      apAutoCategoryDeleteConfirm;
                    @FXML
                    private Button      btAutoCategoryDeleteConfirm;
                    
            //Tab:   Reminders
            @FXML
            private SplitPane   spReminders;
                @FXML
                private AnchorPane  apRemindersTable;
            @FXML
            private Button      btRemindersClose;

                //Dialog:   Reminder Create/Edit Confirmation
                @FXML
                private AnchorPane      apReminderCreateEdit;
                    @FXML
                    private TextField   tfReminderCreateEditName;
                    @FXML
                    private TextField   tfReminderCreateEditDetails;
                    @FXML
                    private DatePicker  dpReminderCreateEdit;
                    @FXML
                    private Button      btReminderCreateEdit;
                    
                //Dialog:   Reminder Delete Confirmation
                @FXML
                private AnchorPane      apReminderDeleteConfirm;
                    @FXML
                    private Button      btReminderDeleteConfirm;
                    
                //Dialog:   Reminders Alert
                @FXML
                private AnchorPane  apRemindersAlert;
                    @FXML
                    private Label   labRemindersAlertsStatus;
                    @FXML
                    private Button  btRemindersAlertSnooze;
                    @FXML
                    private Button  btRemindersAlertView;
            
            //Tab:  Integrity
            @FXML
            private SplitPane   spIntegrity;
                @FXML
                private AnchorPane  apIntegrityTable;
            @FXML
            private RadioButton rbIntegrityAccountHistory;
            @FXML
            private RadioButton rbIntegrityAccountDatabase;
            @FXML
            private RadioButton rbIntegrityAccountHistoryAll;
            @FXML
            private RadioButton rbIntegrityAccountHistoryFrom;
            @FXML
            private DatePicker  dpIntegrityAccountHistoryFrom;
            @FXML
            private Button      btIntegrityCheck;
            @FXML
            private Button      btIntegrityClear;
            @FXML
            private Button      btIntegrityClose;
            
            //Tab:   Database
            @FXML
            private SplitPane  spDatabaseTables;
                @FXML
                private AnchorPane  apDatabaseTable;
            @FXML
            private RadioButton rbDatabaseCurrent;
            @FXML
            private RadioButton rbDatabaseSaving;
            @FXML
            private RadioButton rbDatabaseISA;
            @FXML
            private Button      btDatabaseImport;
            @FXML
            private Button      btDatabaseImportConfirm;
            @FXML
            private Button      btDatabaseImportClose;
            @FXML
            private Button      btDatabaseExport;
            @FXML
            private Button      btDatabaseExportConfirm;
            @FXML
            private Button      btDatabaseExportClose;
            @FXML
            private Button      btDatabaseOverview;
            @FXML
            private Button      btDatabaseViewDate;
            @FXML
            private Button      btDatabaseViewAll;
            @FXML
            private Button      btDatabaseResetAccount;
            @FXML
            private Button      btDatabaseResetAll;
            @FXML
            private Button      btDatabaseClose;
            @FXML
            private RadioButton rbDatabaseTableEntries;
            @FXML
            private RadioButton rbDatabaseTableCategories;
            @FXML
            private RadioButton rbDatabaseTableAutoCategories;
            @FXML
            private RadioButton rbDatabaseTableWatches;
            @FXML
            private RadioButton rbDatabaseTableRefunds;
            @FXML
            private Label       labDatabaseStatusMessage;
            @FXML
            private Label       labDatabaseStatusRange;

                //Dialog:   Database Delete Confirmation
                @FXML
                private AnchorPane      apDatabaseDeleteConfirm;
                    @FXML
                    private Button      btDatabaseDeleteConfirm;

                //Dialog:   Database Delete All Confirmation
                @FXML
                private AnchorPane      apDatabaseDeleteAllConfirm;
                    @FXML
                    private Button      btDatabaseDeleteAllConfirm;

                //Dialog:   Database Reset Account Confirmation
                @FXML
                private AnchorPane      apDatabaseResetAccountConfirm;
                    @FXML
                    private Button      btDatabaseResetAccountConfirm;
                    
                //Dialog:   Database Reset All Confirmation
                @FXML
                private AnchorPane      apDatabaseResetAllConfirm;
                    @FXML
                    private Button      btDatabaseResetAllConfirm;

                //Dialog:   Database View Date
                @FXML
                private AnchorPane      apDatabaseViewDate;
                    @FXML
                    private DatePicker  dpDatabaseViewDate;
                    @FXML
                    private Button      btDatabaseViewDateConfirm;
                    
                //Dialog:   Database Error
                @FXML
                private AnchorPane      apDatabaseError;
                    @FXML
                    private AnchorPane  apDatabaseErrorTable;
                    @FXML
                    private Button      btDatabaseErrorClose;
                    
                //Dialog:   Database Error Details
                @FXML
                private AnchorPane      apDatabaseErrorDetails;
                    @FXML
                    private Label       labDatabaseErrorDetailsMethod;;
                    @FXML
                    private TextArea    taDatabaseErrorDetailsSQL;
                    @FXML
                    private TextArea    taDatabaseErrorDetailsExceptionDetails;
                    @FXML
                    private Button      btDatabaseErrorDetailsClose;
        
        //DDSO
        @FXML
        private SplitPane spDDSO;
            @FXML
            private AnchorPane apDDSO;
                @FXML
                private Rectangle   recDDSONodeValue;
                @FXML
                private Label       labDDSONodeValue;
                
                //Dialog:   Zoom Custom
                @FXML
                private AnchorPane      apDDSOZoom;
                    @FXML
                    private Rectangle   recDDSOZoomAmount;
                    @FXML
                    private TextField   tfDDSOZoomAmount;
                    @FXML
                    private Button      btDDSOZoomSet;
        
        @FXML
        private Pane  pnPopup;
            @FXML
            private AnchorPane      apPleaseRestartProgram;
            
        //Full Screen
        @FXML
        private SplitPane spFullScreen;
        
        //Default Background
        @FXML
        private Rectangle recDialogBackground;
        
        @FXML
        private Pane pnInfo;
            @FXML
            private ImageView   ivInfoDashboard;
            @FXML
            private ImageView   ivInfoDDSO;
            @FXML
            private ImageView   ivInfoStatement;
            @FXML
            private ImageView   ivInfoRefunds;
            @FXML
            private ImageView   ivInfoWatches;
            @FXML
            private ImageView   ivInfoCategories;
            @FXML
            private ImageView   ivInfoAutoCategories;
            @FXML
            private ImageView   ivInfoReminders;
            @FXML
            private ImageView   ivInfoIntegrity;
            @FXML
            private ImageView   ivInfoDatabaseAdministration;
            @FXML
            private AnchorPane  apInfoDashboard;
            @FXML
            private AnchorPane  apInfoDDSO;
            @FXML
            private AnchorPane  apInfoStatement;
            @FXML
            private AnchorPane  apInfoRefunds;
            @FXML
            private AnchorPane  apInfoWatches;
            @FXML
            private AnchorPane  apInfoCategories;
            @FXML
            private AnchorPane  apInfoAutoCategories;
            @FXML
            private AnchorPane  apInfoReminders;
            @FXML
            private AnchorPane  apInfoIntegrity;
            @FXML
            private AnchorPane  apInfoDatabaseAdministration;
            @FXML
            private AnchorPane  apInfoHide;

    private Administration              obAdministration;
    private Database                    obDatabase;
    private DatabaseAdministration      obDatabaseAdministration;
    private DateRange                   obDateRange;
    private Statement                   obStatement;
    private Refunds                     obRefunds;
    private Reminders                   obReminders;
    private Watches                     obWatches;
    private Integrity                   obIntegrity;
    private Popup                       obSystemMessage;
    private Accounts                    obAccounts;
    private Categories                  obCategories;
    private CategoriesAdministration    obCategoriesAdministration;
    private AutoCategories              obAutoCategories;
    private Charts                      obCharts;
    private History                     obHistory;
    private DDSO                        obDDSO;
    private FullScreen                  obFullScreen;
    private Popup                       obPopup;
    private Windows                     obWindows;
    private Info                        obInfo;
    
    //Internal -----------------------------------------------------------------
    private void initEventHandlersForMouse()
    {
        //Accounts
        ivAdministration.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
            Administration.show(ADMINISTRATION_TAB_STATEMENT);
        });
        ivDateRange.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
            Popup.show(me, POPUP_DATE_RANGE);
        });
        labDateRange.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
            Popup.show(me, POPUP_DATE_RANGE);
        });
        
        //Date Range
        btDateRangeSet.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
            if(DateRange.set())
            {
                Accounts.rebuildAllAndRefresh();
                Accounts.rebuildContextMenu();
            }
            
            Popup.hide();
        });
        btDateRangeQuickJumpSet.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
            if(DateRange.set())
            {
                Accounts.rebuildAllAndRefresh();
                Accounts.rebuildContextMenu();
            }
            
            Popup.hide();
        });
        btDateRangeQuickJump.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
            Popup.hideAndShowWithOffset(me, POPUP_DATE_RANGE_MOST_RECENT, POPUP_DATE_RANGE_QUICK_JUMP, 0, 30);
        });
        btDateRangeMostRecent.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
            Popup.hideAndShowWithOffset(me, POPUP_DATE_RANGE_QUICK_JUMP, POPUP_DATE_RANGE_MOST_RECENT, 0, 30);
        });
        btDateRangeAll.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
            DateRange.all();
        });
        
        btDateRangeQuickJump2010.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
            DateRange.setYear(2010);
        });
        btDateRangeQuickJump2011.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
            DateRange.setYear(2011);
        });
        btDateRangeQuickJump2012.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
            DateRange.setYear(2012);
        });
        btDateRangeQuickJump2013.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
            DateRange.setYear(2013);
        });
        btDateRangeQuickJump2014.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
            DateRange.setYear(2014);
        });
        btDateRangeQuickJump2015.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
            DateRange.setYear(2015);
        });
        btDateRangeQuickJump2016.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
            DateRange.setYear(2016);
        });
        btDateRangeQuickJump2017.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
            DateRange.setYear(2017);
        });
        btDateRangeQuickJump2018.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
            DateRange.setYear(2018);
        });
        btDateRangeQuickJump2019.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
            DateRange.setYear(2019);
        });
        btDateRangeQuickJump2020.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
            DateRange.setYear(2020);
        });
        btDateRangeQuickJump2021.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
            DateRange.setYear(2021);
        });
        btDateRangeQuickJump2022.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
            DateRange.setYear(2022);
        });
        btDateRangeQuickJump2023.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
            DateRange.setYear(2023);
        });
        btDateRangeQuickJump2024.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
            DateRange.setYear(2024);
        });
        btDateRangeQuickJump2025.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
            DateRange.setYear(2025);
        });
        btDateRangeQuickJump2026.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
            DateRange.setYear(2026);
        });
        btDateRangeQuickJump2027.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
            DateRange.setYear(2027);
        });
        btDateRangeQuickJump2028.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
            DateRange.setYear(2028);
        });
        btDateRangeQuickJump2029.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
            DateRange.setYear(2029);
        });
        
        btDateRangeQuickJumpJan.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
            DateRange.setMonth(1);
        });
        btDateRangeQuickJumpFeb.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
            DateRange.setMonth(2);
        });
        btDateRangeQuickJumpMar.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
            DateRange.setMonth(3);
        });
        btDateRangeQuickJumpApr.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
            DateRange.setMonth(4);
        });
        btDateRangeQuickJumpMay.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
            DateRange.setMonth(5);
        });
        btDateRangeQuickJumpJun.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
            DateRange.setMonth(6);
        });
        btDateRangeQuickJumpJul.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
            DateRange.setMonth(7);
        });
        btDateRangeQuickJumpAug.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
            DateRange.setMonth(8);
        });
        btDateRangeQuickJumpSept.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
            DateRange.setMonth(9);
        });
        btDateRangeQuickJumpOct.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
            DateRange.setMonth(10);
        });
        btDateRangeQuickJumpNov.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
            DateRange.setMonth(11);
        });
        btDateRangeQuickJumpDec.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
            DateRange.setMonth(12);
        });
        
        btDateRangeMostRecentMonth1.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
            DateRange.setMostRecent(DATE_RANGE_MOST_RECENT_MONTH_1);
        });
        btDateRangeMostRecentMonth3.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
            DateRange.setMostRecent(DATE_RANGE_MOST_RECENT_MONTH_3);
        });
        btDateRangeMostRecentMonth6.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
            DateRange.setMostRecent(DATE_RANGE_MOST_RECENT_MONTH_6);
        });
        btDateRangeMostRecentMonth12.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
            DateRange.setMostRecent(DATE_RANGE_MOST_RECENT_MONTH_12);
        });
        btDateRangeMostRecentMonth18.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
            DateRange.setMostRecent(DATE_RANGE_MOST_RECENT_MONTH_18);
        });
        
        recDateRange.addEventHandler(MouseEvent.MOUSE_PRESSED, (MouseEvent me) -> {
            me.consume();
        });
        recDateRangeQuickJump.addEventHandler(MouseEvent.MOUSE_PRESSED, (MouseEvent me) -> {
            me.consume();
        });
        recDateRangeMostRecent.addEventHandler(MouseEvent.MOUSE_PRESSED, (MouseEvent me) -> {
            me.consume();
        });
        
        //Categories -----------------------------------------------------------

            btCategoryDeleteConfirm.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
                CategoriesAdministration.delete();
            });
            btCategoryCreate.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
                CategoriesAdministration.create();
            });
            btCategoryRename.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
                CategoriesAdministration.rename();
            });
        
        //History --------------------------------------------------------------
        
            //Search
            ivHistorySearch.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
                History.showDialogSearch(me);
            });
        
        //Stacked Bar Chart --------------------------------------------------------------
        
            //Zoom
            btStackedBarChartZoomSet.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
                StackedBarChart.setZoom(ZOOM_CUSTOM);
            });
            
        //DDSO --------------------------------------------------------------
        
            //Zoom
            btDDSOZoomSet.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
                DDSO.setZoom(ZOOM_CUSTOM);
            });
    
        //Administraion --------------------------------------------------------
        
            //Statement
            btStatementOpenFile.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
                Statement.showFileChooser();
            });
            btStatementClear.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
                Statement.clear();
            });
            btStatementAdd.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
                Statement.addEntriesToDatabase();
            });
            btStatementClose.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
                Administration.hide();
            });
            
            //Refunds
            btRefundCreateEdit.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
                Refunds.createEdit();
            });
            btRefundDeleteConfirm.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
                Refunds.delete();
            });
            btRefundsClose.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
                Administration.hide();
            });
            
            //Watches
            btWatchDeleteConfirm.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
                Watches.delete();
            });
            btWatchesClose.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
                Administration.hide();
            });
            
            //Categories
            btCategoriesClose.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
                Administration.hide();
            });

            //AutoCategories
            btAutoCategoryDeleteConfirm.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
                AutoCategories.delete();
            });
            btAutoCategoriesClose.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
                Administration.hide();
            });
            
            //Reminders
            btRemindersAlertSnooze.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
                Popup.hideFixed(POPUP_REMINDER_ALERT);
            });
            btRemindersAlertView.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
                Popup.hideFixed(POPUP_REMINDER_ALERT);
                Administration.show(ADMINISTRATION_TAB_REMINDERS);
                Reminders.init();
            });
            btReminderCreateEdit.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
                Reminders.createEdit();
            });
            btReminderDeleteConfirm.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
                Reminders.delete();
            });
            btRemindersClose.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
                Administration.hide();
            });
            
            //Database Integrity
            btIntegrityCheck.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
                Integrity.check();
            });
            btIntegrityClear.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
                Integrity.clear();
            });
            btIntegrityClose.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
                Administration.hide();
            });

            //Database
            btDatabaseOverview.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
                DatabaseAdministration.showOverview();
            });
            btDatabaseImport.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
                DatabaseAdministration.showDialogImportFromFile();
            });
            btDatabaseImportConfirm.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
                DatabaseAdministration.importFromFile();
            });
            btDatabaseImportClose.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
                DatabaseAdministration.closeImportFromFile();
            });
            btDatabaseExport.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
                DatabaseAdministration.showDialogExportToFile();
            });
            btDatabaseExportConfirm.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
                DatabaseAdministration.exportToFile();
            });
            btDatabaseExportClose.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
                DatabaseAdministration.closeExportToFile();
            });
            btDatabaseViewDate.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
                DatabaseAdministration.findViewDate();
                Popup.show(me, POPUP_DATABASE_VIEW_DATE);
            });
            btDatabaseViewAll.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
                DatabaseAdministration.viewAll();
            });
            btDatabaseDeleteConfirm.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
                DatabaseAdministration.delete();
                Popup.hideAllAndShow(me, POPUP_PLEASE_RESTART_PROGRAM);
            });
            btDatabaseDeleteAllConfirm.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
                DatabaseAdministration.deleteAll();
                Popup.hideAllAndShow(me, POPUP_PLEASE_RESTART_PROGRAM);
            });
            btDatabaseResetAccount.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
                Popup.show(me, POPUP_DATABASE_RESET_ACCOUNT_CONFIRM);
            });
            btDatabaseResetAll.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
                Popup.show(me, POPUP_DATABASE_RESET_ALL_CONFIRM);
            });
            btDatabaseResetAccountConfirm.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
                DatabaseAdministration.resetAccount();
                Popup.hideAllAndShow(me, POPUP_PLEASE_RESTART_PROGRAM);
            });
            btDatabaseResetAllConfirm.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
                DatabaseAdministration.resetAll();
                Popup.hideAllAndShow(me, POPUP_PLEASE_RESTART_PROGRAM);
            });
            btDatabaseViewDateConfirm.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
                DatabaseAdministration.viewDate();
                Popup.hide();
            });
            btDatabaseErrorClose.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
                Database.clearError();
                Popup.hideFixed(POPUP_DATABASE_ERROR);
                Popup.hideFixed(POPUP_DATABASE_ERROR_DETAILS);
            });
            btDatabaseErrorDetailsClose.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
                Popup.hideFixed(POPUP_DATABASE_ERROR_DETAILS);
            });
            btDatabaseClose.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
                Administration.hide();
            });
        
        //Popup Background
        pnPopup.addEventHandler(MouseEvent.MOUSE_PRESSED, (MouseEvent me) -> {
            Popup.hide();
        });
        
        //Info
        ivInfoDashboard.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
            Windows.resetToDefault();
            Info.show(0);
        });
        ivInfoDDSO.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
            Info.show(1);
        });
        ivInfoStatement.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
            Info.show(2);
        });
        ivInfoRefunds.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
            Info.show(3);
        });
        ivInfoWatches.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
            Info.show(4);
        });
        ivInfoCategories.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
            Info.show(5);
        });
        ivInfoAutoCategories.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
            Info.show(6);
        });
        ivInfoReminders.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
            Info.show(7);
        });
        ivInfoIntegrity.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
            Info.show(8);
        });
        ivInfoDatabaseAdministration.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
            Info.show(9);
        });
        apInfoHide.addEventHandler(MouseEvent.MOUSE_PRESSED, (MouseEvent me) -> {
            Info.hide();
        });
    }
    private void initEventFiltersForKeyboard()
    {
        apApp.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent ke) -> {
            
            if(!(Administration.isVisible() || Popup.isVisible() || Info.isVisible()))
                if(ke.isControlDown())
                {
                    switch(ke.getCode())
                    {
                        case F:     History.showDialogSearch();     break;
                    }
                }
                else
                {
                    switch(ke.getCode())
                    {
                        case LEFT:
                        {
                            if(DDSO.isActive())
                                DDSO.showPreviousMonth();
                            else if(DateRange.isPeriod())
                            {
                                DateRange.previousFixedPeriod();
                                Accounts.rebuildAllAndRefresh();
                            }

                            break;
                        }
                        case RIGHT:
                        {
                            if(DDSO.isActive())
                                DDSO.showNextMonth();
                            else if(DateRange.isPeriod())
                            {
                                DateRange.nextFixedPeriod();
                                Accounts.rebuildAllAndRefresh();
                            }

                            break;
                        }
                        case ESCAPE:
                        {
                            if(FullScreen.isActive())
                                FullScreen.escape();
                            else if(History.isSearchActive())
                                History.searchClear();

                            Popup.hide();

                            break;
                        }
                    }
                }
        });
    }
    private void initToolTips()
    {
        Map<Node, String> lst = new HashMap<>();
        Font fontStandard = new Font("System", 12);
        
        lst.put(ivAdministration, "Administration");
        lst.put(ivDateRange, "Set Date Range");
        lst.put(labDateRange, "Set Date Range");
        
        lst.put(ivHistorySearch, "Search History");
        lst.put(ivHistorySearchClose, "Clear Search");
        
        for(Node n : lst.keySet())
        {
            Tooltip tt = new Tooltip(lst.get(n));
            tt.setFont(fontStandard);
            Tooltip.install(n, tt);
        }
    }
    private void initPopups()
    {
        Popup.init(apDateRange, apDateRangeQuickJump, apDateRangeMostRecent, apHistorySearch, apStackedBarChartZoom, apDDSOZoom, apRefundCreateEdit, apRefundDeleteConfirm, apWatchDeleteConfirm, apCategoryCreate, apCategoryRename, apCategoryDeleteConfirm, apAutoCategoryDeleteConfirm, apReminderCreateEdit, apReminderDeleteConfirm,
                   apDatabaseDeleteConfirm, apDatabaseDeleteAllConfirm, apDatabaseResetAccountConfirm, apDatabaseResetAllConfirm, apDatabaseViewDate, apPleaseRestartProgram);
        
        Popup.initFixed(apRemindersAlert, apDatabaseError, apDatabaseErrorDetails);
    }
    private void initDemo()
    {
        DateRange.setYear(2022);
        
        if(DateRange.set())
        {
            Accounts.rebuildAllAndRefresh();
            Accounts.rebuildContextMenu();
        }
        
        Accounts.selectAllAndSendToCharts();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        obPopup = new Popup(pnPopup);
        
        initPopups();
        
        obDatabase = new Database(apDatabaseErrorTable, labDatabaseErrorDetailsMethod, taDatabaseErrorDetailsSQL, taDatabaseErrorDetailsExceptionDetails);
         
        obAccounts = new Accounts(apAccounts);
        
        obCategories = new Categories(apCategoriesIn, apCategoriesOut, labCategoriesInHeader, labCategoriesOutHeader, cbCategoriesInViewAll, cbCategoriesOutViewAll);
        
        obCategoriesAdministration = new CategoriesAdministration(spCategories, apCategoriesInTable, apCategoriesOutTable,
                            rbCategoriesCurrent,  rbCategoriesSaving,  rbCategoriesISA,
                            tfCategoryCreate, tfCategoryRename, btCategoryCreate, btCategoryRename);
        
        obAutoCategories = new AutoCategories(spAutoCategories, apAutoCategoriesInTable, apAutoCategoriesOutTable,
                            rbAutoCategoriesCurrent,  rbAutoCategoriesSaving,  rbAutoCategoriesISA);
     
        obRefunds = new Refunds(spRefundsTables, apRefundsTable,
                            rbRefundsCurrent, rbRefundsSaving, rbRefundsISA,
                            tfRefundCreateEditName, tfRefundCreateEditKeywords, tfRefundCreateEditAmountPounds, tfRefundCreateEditAmountPence,
                            dpRefundCreateEdit, btRefundCreateEdit);
        
        obReminders = new Reminders(spReminders, apRemindersTable,
                            tfReminderCreateEditName, tfReminderCreateEditDetails, dpReminderCreateEdit, btReminderCreateEdit, labRemindersAlertsStatus);
        
        obWatches = new Watches(spWatchesTables, apWatchesTable,
                            rbWatchesCurrent, rbWatchesSaving, rbWatchesISA,
                            rbWatchesIncoming, rbWatchesOutgoing);
        
        obCharts = new Charts(apStackedBarChart, apPieChart, apLineChart,
                            recStackedBarChartNodeValue, labStackedBarChartNodeValue, recStackedBarChartZoomAmount, tfStackedBarChartZoomAmount, btStackedBarChartZoomSet,
                            recPieChartNodeValue, labPieChartNodeValue,
                            recLineChartNodeValue, labLineChartNodeValue);

        obHistory = new History(apHistory, apHistorySearchListView, tfHistorySearch, recHistorySearch, ivHistorySearchClose);
        
        obDateRange = new DateRange(dpDateRangeFrom, dpDateRangeTo, recDateRange, labDateRange,
                            btDateRangeQuickJump2010, btDateRangeQuickJump2011, btDateRangeQuickJump2012, btDateRangeQuickJump2013, btDateRangeQuickJump2014,
                            btDateRangeQuickJump2015, btDateRangeQuickJump2016, btDateRangeQuickJump2017, btDateRangeQuickJump2018, btDateRangeQuickJump2019,
                            btDateRangeQuickJump2020, btDateRangeQuickJump2021, btDateRangeQuickJump2022, btDateRangeQuickJump2023, btDateRangeQuickJump2024,
                            btDateRangeQuickJump2025, btDateRangeQuickJump2026, btDateRangeQuickJump2027, btDateRangeQuickJump2028, btDateRangeQuickJump2029);
        
        obStatement = new Statement(spStatementTables, apStatementTable,
                            rbStatementCurrent, rbStatementSaving, rbStatementISA,
                            rbStatementEntriesCategorised, rbStatementEntriesUndefined, rbStatementEntriesAll,
                            btStatementOpenFile, btStatementAdd, labStatementStatusMessage);
        
        obIntegrity = new Integrity(spIntegrity, apIntegrityTable, rbIntegrityAccountHistory, rbIntegrityAccountDatabase,
                            rbIntegrityAccountHistoryAll, rbIntegrityAccountHistoryFrom, dpIntegrityAccountHistoryFrom);
        
        obDDSO = new DDSO(spDDSO, apDDSO, ivInfoDDSO, recDDSONodeValue, labDDSONodeValue, recDDSOZoomAmount, tfDDSOZoomAmount, btDDSOZoomSet, recDialogBackground);
        
        obFullScreen = new FullScreen(spFullScreen, recDialogBackground);
        
        obWindows = new Windows(spApp, spAccCat, spCharts);
        
        obAdministration = new Administration(spAdministration, tpAdministration, recDialogBackground);
        
        obDatabaseAdministration = new DatabaseAdministration(spDatabaseTables, apDatabaseTable,
                            rbDatabaseCurrent, rbDatabaseSaving, rbDatabaseISA,
                            rbDatabaseTableEntries, rbDatabaseTableCategories, rbDatabaseTableAutoCategories, rbDatabaseTableWatches, rbDatabaseTableRefunds,
                            labDatabaseStatusMessage, labDatabaseStatusRange, dpDatabaseViewDate,
                            btDatabaseViewDate, btDatabaseViewAll, btDatabaseOverview,
                            btDatabaseImport, btDatabaseImportConfirm, btDatabaseImportClose,
                            btDatabaseExport, btDatabaseExportConfirm, btDatabaseExportClose,
                            btDatabaseResetAccount, btDatabaseResetAll, btDatabaseClose, ivInfoDatabaseAdministration);
        
        obInfo = new Info(pnInfo, apInfoDashboard, apInfoDDSO, apInfoStatement, apInfoRefunds, apInfoWatches, apInfoCategories, apInfoAutoCategories, apInfoReminders, apInfoIntegrity, apInfoDatabaseAdministration);
        
        initEventHandlersForMouse();
        initEventFiltersForKeyboard();
        initToolTips();
        initDemo();
    }    
}
