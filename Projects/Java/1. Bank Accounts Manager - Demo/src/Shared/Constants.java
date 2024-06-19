package Shared;

public interface Constants {

    //General
        int NUMBER_OF_ACCOUNTS                                  = 3;
        int SCREEN_WIDTH                                        = 1920;
        int NOT_DEFINED                                         = -1;
        String NOT_DEFINED_TEXT                                 = "-1";
        String DASH                                             = "-";
                
        int ACCOUNT                                             = 10;
        int ALL                                                 = 11;
        int CATEGORY                                            = 12;
        int ENTRY                                               = 13;
        
        int IN                                                  = 30;
        int OUT                                                 = 31;
        int BOTH                                                = 32;

    //TableView Factory
        int TC_STRING                                           = 10;
        int TC_CHECKBOX                                         = 11;
        double TC_TOP_OFFSET                                    = 33.0;
                        
    //Popup
        int POPUP_DATE_RANGE                                    = 0;
        int POPUP_DATE_RANGE_QUICK_JUMP                         = 1;
        int POPUP_DATE_RANGE_MOST_RECENT                        = 2;
        int POPUP_HISTORY_SEARCH                                = 3;
        int POPUP_STACKED_BAR_ZOOM                              = 4;
        int POPUP_DDSO_ZOOM                                     = 5;
        int POPUP_REFUND_CREATE_EDIT                            = 6;
        int POPUP_REFUND_DELETE_CONFIRM                         = 7;
        int POPUP_WATCH_DELETE_CONFIRM                          = 8;
        int POPUP_CATEGORY_CREATE                               = 9;
        int POPUP_CATEGORY_RENAME                               = 10;
        int POPUP_CATEGORY_DELETE_CONFIRM                       = 11;
        int POPUP_AUTO_CATEGORY_DELETE_CONFIRM                  = 12;
        int POPUP_REMINDER_CREATE_EDIT                          = 13;
        int POPUP_REMINDER_DELETE_CONFIRM                       = 14;
        int POPUP_DATABASE_DELETE_CONFIRM                       = 15;
        int POPUP_DATABASE_DELETE_ALL_CONFIRM                   = 16;
        int POPUP_DATABASE_RESET_ACCOUNT_CONFIRM                = 17;
        int POPUP_DATABASE_RESET_ALL_CONFIRM                    = 18;
        int POPUP_DATABASE_VIEW_DATE                            = 19;
        int POPUP_PLEASE_RESTART_PROGRAM                        = 20;
        
        //Fixed
        int POPUP_REMINDER_ALERT                                = 0;
        int POPUP_DATABASE_ERROR                                = 1;
        int POPUP_DATABASE_ERROR_DETAILS                        = 2;
    
    //Blank
        int BLANK_FIELD_PRIMARY_KEY                             = 50;
        int BLANK_FIELD_ERROR                                   = 51;
        int BLANK_FIELD_NAME                                    = 52;
        int BLANK_FIELD_ENTRY_NAME                              = 53;
        int BLANK_FIELD_CATEGORY_NAME                           = 54;
        int BLANK_FIELD_DETAILS                                 = 55;
        int BLANK_FIELD_FREQUENCY                               = 56;
        int BLANK_FIELD_TOTAL                                   = 57;
        
    //TableView
        int TABLEVIEW_ID_ACCOUNTS                               = 70;
        int TABLEVIEW_ID_CATEGORIES                             = 71;
        int TABLEVIEW_ID_CATEGORIES_ADMINISTRATION              = 72;
        int TABLEVIEW_ID_HISTORY                                = 73;
        int TABLEVIEW_ID_HISTORY_WITH_HEADER_OFFSET             = 74;
        int TABLEVIEW_ID_HISTORY_WITHIN_ADMINISTRATION          = 75;
        int TABLEVIEW_ID_STATEMENT                              = 76;
        int TABLEVIEW_ID_STATEMENT_ERRORS                       = 77;
        int TABLEVIEW_ID_REFUNDS                                = 78;
        int TABLEVIEW_ID_REFUNDS_POTENTIALS                     = 79;
        int TABLEVIEW_ID_WATCHES                                = 80;
        int TABLEVIEW_ID_AUTO_CATEGORIES                        = 81;
        int TABLEVIEW_ID_REMINDERS                              = 82;
        int TABLEVIEW_ID_INTEGRITY_CHECKS                       = 83;
        int TABLEVIEW_ID_INTEGRITY_ERRORS                       = 84;
        int TABLEVIEW_ID_DATABASE_OVERVIEW                      = 85;
        int TABLEVIEW_ID_DATABASE_ENTRIES                       = 86;
        int TABLEVIEW_ID_DATABASE_CATEGORIES                    = 87;
        int TABLEVIEW_ID_DATABASE_AUTO_CATEGORIES               = 88;
        int TABLEVIEW_ID_DATABASE_WATCHES                       = 89;
        int TABLEVIEW_ID_DATABASE_REFUNDS                       = 90;
        int TABLEVIEW_ID_DATABASE_REMINDERS                     = 91;
        int TABLEVIEW_ID_DATABASE_ERRORS                        = 92;
        int TABLEVIEW_ID_DATABASE_IMPORT                        = 93;
        int TABLEVIEW_ID_DATABASE_EXPORT                        = 94;
        int TABLEVIEW_ID_DATABASE_IMPORT_EXPORT_ERRROR          = 95;
        
    //Administration
        int ADMINISTRATION_TAB_STATEMENT                        = 0;
        int ADMINISTRATION_TAB_REFUNDS                          = 1;
        int ADMINISTRATION_TAB_WATCHES                          = 2;
        int ADMINISTRATION_TAB_CATEGORIES                       = 3;
        int ADMINISTRATION_TAB_AUTO_CATEGORIES                  = 4;
        int ADMINISTRATION_TAB_REMINDERS                        = 5;
        int ADMINISTRATION_TAB_INTEGRITY                        = 6;
        int ADMINISTRATION_TAB_DATABASE_ADMINISTRATION          = 7;
        
    //Windows
        int WINDOW_ACCOUNTS                                     = 110;
        int WINDOW_CATEGORIES_IN                                = 111;
        int WINDOW_CATEGORIES_OUT                               = 112;
        int WINDOW_CHART_STACKED_BAR                            = 113;
        int WINDOW_CHART_PIE                                    = 114;
        int WINDOW_CHART_LINE                                   = 115;
        int WINDOW_HISTORY                                      = 116;
        
        int WINDOW_GROUP_ACC_CAT                                = 130;
        int WINDOW_GROUP_CHARTS                                 = 131;
        int WINDOW_GROUP_HISTORY                                = 132;
        
        double APP_DEFAULT_DIVIDER_LEFT                         = 0.342;
        double APP_DEFAULT_DIVIDER_RIGHT                        = 0.66;
        
        double ACC_CAT_DEFAULT_DIVIDER_UPPER                    = 0.20;
        double ACC_CAT_DEFAULT_DIVIDER_LOWER                    = 0.60;
        
        double CHARTS_DEFAULT_DIVIDER_UPPER                     = 0.33;
        double CHARTS_DEFAULT_DIVIDER_LOWER                     = 0.66;
    
    //Accounts
        int BARCLAYS_CURRENT                                    = 0;
        int BARCLAYS_SAVING                                     = 1;
        int BARCLAYS_ISA                                        = 2;

        int BLANK_LINE                                          = -1;
        int NOT_APPLICABLE                                      = -2;
        String NOT_APPLICABLE_TEXT                              = "n/a";

            //Balance Property
            int BP_BALANCE_AT_START                             = 0;
            int BP_INCOMING                                     = 1;
            int BP_OUTGOING                                     = 2;
            int BP_BALANCE_CHANGE                               = 3;
            int BP_BALANCE_AT_END                               = 4;
            
            //Account Property
            int AP_INCOMING_INCOME                              = 0;
            int AP_INCOMING_INTERNAL_TRANSFER                   = 1;
            int AP_INCOMING_TOTAL                               = 2;
            
            int AP_OUTGOING_PURCHASE                            = 3;
            int AP_OUTGOING_DDSO                                = 4;
            int AP_OUTGOING_OTHER                               = 5;
            int AP_OUTGOING_SPEND                               = 6;
            int AP_OUTGOING_INTERNAL_TRANSFER                   = 7;
            int AP_OUTGOING_TOTAL                               = 8;
            
    //Categories
        String IN_TEXT                                          = "In";
        String OUT_TEXT                                         = "Out";
        String UNDEFINED_TEXT                                   = "Undefined";
        String CREATE_CATEGORY_TEXT                             = "New Category Name";
        String WATCHED_TEXT                                     = "Watched";
        
        int CATEGORY_CREATE                                     = 150;
        int CATEGORY_RENAME                                     = 151;
        int CATEGORY_DELETE                                     = 152;
        
        int CATEGORY_TYPE_USER_DEFINED                          = 155;
        int CATEGORY_TYPE_INTERNAL_TRANSFER                     = 156;
        int CATEGORY_TYPE_ACCOUNT_PROPERTY                      = 157;
    
    //Auto Categories
        int AUTO_CATEGORY_DELETE                                = 170;
        
    //Date Range
        int DATE_RANGE_MODE_CUSTOM                              = 190;
        int DATE_RANGE_MODE_PERIOD                              = 191;

        int DAY                                                 = 210;
        int MONTH                                               = 211;
        int YEAR                                                = 212;
        
        int DATE_RANGE_MOST_RECENT_MONTH_1                      = 230;
        int DATE_RANGE_MOST_RECENT_MONTH_3                      = 231;
        int DATE_RANGE_MOST_RECENT_MONTH_6                      = 232;
        int DATE_RANGE_MOST_RECENT_MONTH_12                     = 233;
        int DATE_RANGE_MOST_RECENT_MONTH_18                     = 234;
        
        int PERIOD_MONTH                                        = 250;
        int PERIOD_YEAR                                         = 251;
        
        int PERIOD_PREVIOUS                                     = 270;
        int PERIOD_NEXT                                         = 271;
        
        int INTERVAL_WEEKS                                      = 0;
        int INTERVAL_MONTHS                                     = 1;
        int INTERVAL_YEARS                                      = 2;
        
        int NUMBER_OF_INTERVAL_SIZE                             = 3;
    
    //Formatter
        int DATE                                                = 290;
        int CURRENCY                                            = 291;
        int STATEMENT                                           = 292;
        int TABLEVIEW                                           = 293;
        int DATABASE                                            = 294;
        int CHART                                               = 295;
        int LOCALDATE                                           = 296;
        int POUNDS                                              = 297;
        int FILE                                                = 298;
    
    //History Search
        double TEXT_LINE_HEIGHT                                 = 24;
        
        int ENTRY_SET_CATEGORY                                  = 300;
        int ENTRY_SET_AUTO_CATEGORY                             = 301;
    
    //Statement
        int AUTO_CATEGORISED                                    = 310;
        int UNDEFINED                                           = 311;
    
        String CATEGORISED_TEXT                                 = "Categorised";
        String ALL_TEXT                                         = "All";
        
    //Watch
        int WATCH_VIEW_HISTORY                                  = 330;
        int WATCH_UPDATE                                        = 331;
        int WATCH_DELETE                                        = 332;
    
    //Refund
        int REFUND_CREATE                                       = 350;
        int REFUND_EDIT                                         = 351;
        int REFUND_VIEW_POTENTIAL                               = 352;
        int REFUND_VIEW_ALL_INCOMING                            = 353;
        int REFUND_VIEW_CONFIRMED                               = 354;
        int REFUND_DELETE                                       = 355;
        int REFUND_CONFIRM                                      = 356;
        String REFUND_RECEIVED_PREFIX                           = "Refund received on";
    
    //Reminder
        int REMINDER_CREATE                                     = 370;
        int REMINDER_EDIT                                       = 371;
        int REMINDER_DELETE                                     = 372;
        int REMINDER_VIEW_ALERTS                                = 373;
        int REMINDER_VIEW_ALL                                   = 374;
        
    //Integrity Check
        int ACCOUNT_HISTORY_BALANCE_INCONSISTENCY               = 0;
        int ACCOUNT_HISTORY_DOUBLE_CHARGE_PURCHASE              = 1;
        int ACCOUNT_HISTORY_DOUBLE_CHARGE_DDSO                  = 2;
        int ACCOUNT_HISTORY_TRANSFERS                           = 3;
        int ACCOUNT_HISTORY_REFUNDS_OUTSTANDING                 = 4;
        
        int ACCOUNT_DATABASE_ENTRIES                            = 0;
        int ACCOUNT_DATABASE_CATEGORIES                         = 1;
        int ACCOUNT_DATABASE_AUTO_CATEGORIES                    = 2;
        int ACCOUNT_DATABASE_WATCHES                            = 3;
        int ACCOUNT_DATABASE_REFUNDS                            = 4;
        
        int TRANSFERS                                           = 0;
        
        int INTEGRITY_MODE_ACCOUNT_HISTORY                      = 390;
        int INTEGRITY_MODE_ACCOUNT_DATABASE                     = 391;
        
        int INTEGRITY_RANGE_ALL                                 = 410;
        int INTEGRITY_RANGE_FROM                                = 411;
        
        int INTEGRITY_VIEW_ERRORS                               = 430;
        int INTEGRITY_VIEW_ITEMS                                = 431;
        
    //History Context Menus
        int FILTER_INCOMING_INCOME                              = 470;
        int FILTER_INCOMING_INCOME_CATEGORIES                   = 471;
        int FILTER_INCOMING_INTERNAL_TRANSFERS                  = 472;
        int FILTER_INCOMING_TOTAL                               = 473;
        
        int FILTER_OUTGOING_PURCHASES                           = 490;
        int FILTER_OUTGOING_DDSO                                = 491;
        int FILTER_OUTGOING_OTHER                               = 492;
        int FILTER_OUTGOING_SPEND                               = 493;
        int FILTER_OUTGOING_INTERNAL_TRANSFERS                  = 494;
        int FILTER_OUTGOING_TOTAL                               = 495;
        int FILTER_INSTANCES                                    = 496;
        
        int VIEW_ENTRIES                                        = 500;
    
    //Categories
        int VIEW_CATEGORIES                                     = 510;
        int VIEW_OUTGOING_DDSO_DISTRIBUTION                     = 511;
        
    //Database
        int DATABASE_RECORD_MAXIMUM_LENGTH_4                    = 4;
        int DATABASE_RECORD_MAXIMUM_LENGTH_8                    = 8;
        int DATABASE_RECORD_MAXIMUM_LENGTH_256                  = 256;
        int DATABASE_RECORD_MAXIMUM_LENGTH_1024                 = 1024;
        
        int DATABASE_TABLE_ENTRY                                = 520;
        int DATABASE_TABLE_CATEGORY                             = 521;
        int DATABASE_TABLE_AUTO_CATEGORY                        = 522;
        int DATABASE_TABLE_WATCH                                = 523;
        int DATABASE_TABLE_REFUND                               = 524;
        int DATABASE_TABLE_REMINDER                             = 525;
        
        int DATABASE_IMPORT_TYPE_ENTRY                          = 540;
        int DATABASE_IMPORT_TYPE_CATEGORY                       = 541;
        int DATABASE_IMPORT_TYPE_AUTO_CATEGORY                  = 542;
        int DATABASE_IMPORT_TYPE_WATCH                          = 543;
        int DATABASE_IMPORT_TYPE_REFUND                         = 544;
        
        int EARLIEST                                            = 560;
        int LATEST                                              = 561;
        
        int MODE_OVERVIEW                                       = 580;
        int MODE_VIEW                                           = 581;
        int MODE_IMPORT                                         = 582;
        int MODE_EXPORT                                         = 583;
        
        String MARKER_ENTRIES                                   = "Entries";
        String MARKER_CATGEGORIES                               = "Categories";
        String MARKER_AUTO_CATEGORIES                           = "Auto Assign";
        String MARKER_WATCHES                                   = "Watches";
        String MARKER_REFUNDS                                   = "Refunds";
        
        int DATABASE_OVERVIEW_VIEW                              = 600;
        int DATABASE_DELETE_RECORDS                             = 601;
        int DATABASE_DELETE_RECORDS_ALL                         = 602;
        
        //Administration
        int DEFAULT_FONT                                        = -1;
        int DEFAULT_DATABASE_TABLE_FONT                         = 12;
    
    //DDSO
        int ENTRY_NAME                                          = 620;
        int CATEGORY_NAME                                       = 621;
        
    //Charts
        int CHART_STACKED_BAR                                   = 640;
        int CHART_PIE                                           = 641;
        int CHART_LINE                                          = 642;
        
        int MAXIMUM_NUMBER_OF_CHART_BARS                        = 8;
        int MAXIMUM_NUMBER_OF_CHART_LINES                       = 8;
        int MAXIMUM_NUMBER_OF_CHART_X_AXIS_ITEMS                = 110;
        
        int CHART_SIZE_TRANSLATE_DURATION                       = 100;
        
        int SHOW_VALUES                                         = 660;
        int SHOW_VALUES_ENTRY_NAMES                             = 661;
        int SHOW_VALUES_CATEGORY_NAMES                          = 662;
        
        int HIDE_VALUES                                         = 680;
        int SHOW_LEGEND                                         = 681;
        int HIDE_LEGEND                                         = 682;
        
        int SHOW_FULL_SCREEN                                    = 700;
        int HIDE_FULL_SCREEN                                    = 701;
        
        int COMPARE_WEEKS                                       = 720;
        int COMPARE_MONTHS                                      = 721;
        int COMPARE_YEARS                                       = 722;
        
        int ZOOM_10                                             = 740;
        int ZOOM_25                                             = 741;
        int ZOOM_50                                             = 742;
        int ZOOM_75                                             = 743;
        int ZOOM_100                                            = 744;
        int ZOOM_CUSTOM                                         = 745;
        
        int PREVIOUS_MONTH                                      = 760;
        int NEXT_MONTH                                          = 761;
        
        int CLOSE_CHART                                         = 780;
        
        int SELECTED_ACCOUNTS                                   = 800;
        int SELECTED_CATEGORIES                                 = 801;
        int SELECTED_ENTRIES                                    = 802;
        int SELECTED_ALL                                        = 803;
        
        int FONT_SIZE_WINDOW_SMALL                              = 10;
        int FONT_SIZE_WINDOW_LARGE                              = 15;
        int FONT_SIZE_WINDOW_CONTEXT_MENU                       = 12;
        
        //StackedBar Chart -----------------------------------------------------
        
        //All/AccountsChartBars
        int CHART_BARS_ID_BALANCE                               = 0;
        
        //All/AccountChartBars - Balance Change
        int CHART_BARS_ID_BALANCE_CHANGE_PER_WEEK               = 1;
        int CHART_BARS_ID_BALANCE_CHANGE_PER_MONTH              = 2;
        int CHART_BARS_ID_BALANCE_CHANGE_PER_YEAR               = 3;
        
        //All/AccountChartBars - Incoming
        int CHART_BARS_ID_INCOMING_INCOME_PER_WEEK              = 4;
        int CHART_BARS_ID_INCOMING_INCOME_PER_MONTH             = 5;
        int CHART_BARS_ID_INCOMING_INCOME_PER_YEAR              = 6;
        
        int CHART_BARS_ID_INCOMING_INTERNAL_TRANSFERS_PER_WEEK  = 7;
        int CHART_BARS_ID_INCOMING_INTERNAL_TRANSFERS_PER_MONTH = 8;
        int CHART_BARS_ID_INCOMING_INTERNAL_TRANSFERS_PER_YEAR  = 9;
        
        int CHART_BARS_ID_INCOMING_TOTAL_PER_WEEK               = 10;
        int CHART_BARS_ID_INCOMING_TOTAL_PER_MONTH              = 11;
        int CHART_BARS_ID_INCOMING_TOTAL_PER_YEAR               = 12;
        
        int CHART_BARS_ID_INCOMING_PER_CATEGORY                 = 13;
        
        //All/AccountChartBars - Outgoing
        int CHART_BARS_ID_OUTGOING_PURCHASES_PER_WEEK           = 14;
        int CHART_BARS_ID_OUTGOING_PURCHASES_PER_MONTH          = 15;
        int CHART_BARS_ID_OUTGOING_PURCHASES_PER_YEAR           = 16;
        
        int CHART_BARS_ID_OUTGOING_DDSO_PER_WEEK                = 17;
        int CHART_BARS_ID_OUTGOING_DDSO_PER_MONTH               = 18;
        int CHART_BARS_ID_OUTGOING_DDSO_PER_YEAR                = 19;
        
        int CHART_BARS_ID_OUTGOING_OTHER_PER_WEEK               = 20;
        int CHART_BARS_ID_OUTGOING_OTHER_PER_MONTH              = 21;
        int CHART_BARS_ID_OUTGOING_OTHER_PER_YEAR               = 22;
        
        int CHART_BARS_ID_OUTGOING_SPEND_PER_WEEK               = 23;
        int CHART_BARS_ID_OUTGOING_SPEND_PER_MONTH              = 24;
        int CHART_BARS_ID_OUTGOING_SPEND_PER_YEAR               = 25;
        
        int CHART_BARS_ID_OUTGOING_INTERNAL_TRANSFERS_PER_WEEK  = 26;
        int CHART_BARS_ID_OUTGOING_INTERNAL_TRANSFERS_PER_MONTH = 27;
        int CHART_BARS_ID_OUTGOING_INTERNAL_TRANSFERS_PER_YEAR  = 28;
        
        int CHART_BARS_ID_OUTGOING_TOTAL_PER_WEEK               = 29;
        int CHART_BARS_ID_OUTGOING_TOTAL_PER_MONTH              = 30;
        int CHART_BARS_ID_OUTGOING_TOTAL_PER_YEAR               = 31;
        
        int CHART_BARS_ID_OUTGOING_PER_CATEGORY                 = 32;
        
        //Category/EntryChartBars
        int CHART_BARS_ID_PER_WEEK                              = 0;
        int CHART_BARS_ID_PER_MONTH                             = 1;
        int CHART_BARS_ID_PER_YEAR                              = 2;
        int CHART_BARS_ID_PER_CATEGORY                          = 3;
        int CHART_BARS_ID_PER_ENTRY                             = 4;
        
        //Pie Chart ------------------------------------------------------------
        
        //Account
        int CHART_PIE_ID_BALANCE_DISTRIBUTION_AT_START          = 0;
        int CHART_PIE_ID_BALANCE_DISTRIBUTION_AT_END            = 1;
        int CHART_PIE_ID_INCOMING_CATEGORIES                    = 2;
        int CHART_PIE_ID_OUTGOING_CATEGORIES                    = 3;
        int CHART_PIE_ID_INCOME_V_SPEND                         = 4;
        int CHART_PIE_ID_INCOMING_V_OUTGOING                    = 5;
        
        //Category
        int CHART_PIE_ID_INCOMING_CATEGORIES_V_INCOME           = 6;
        int CHART_PIE_ID_INCOMING_CATEGORIES_V_INCOMING         = 7;
        int CHART_PIE_ID_OUTGOING_CATEGORIES_V_SPEND            = 8;
        int CHART_PIE_ID_OUTGOING_CATEGORIES_V_OUTGOING         = 9;
        
        //Entry
        int CHART_PIE_ID_INCOMING_ENTRIES_V_INCOME              = 10;
        int CHART_PIE_ID_INCOMING_ENTRIES_V_INCOMING            = 11;
        int CHART_PIE_ID_OUTGOING_ENTRIES_V_SPEND               = 12;
        int CHART_PIE_ID_OUTGOING_ENTRIES_V_OUTGOING            = 13;
        
        int CHART_PIE_CATEGORIES_V_LOCAL                        = 810;
        int CHART_PIE_CATEGORIES_V_ALL                          = 811;
        
        int CHART_PIE_ENTRIES_V_LOCAL                           = 812;
        int CHART_PIE_ENTRIES_V_ALL                             = 813;
        
        //Line Chart -----------------------------------------------------------
        
        //Build Behaviour
        int CHART_LINE_BUILD_BEHAVIOUR_UNALTERED                = 820;
        int CHART_LINE_BUILD_BEHAVIOUR_AGGREGATED               = 821;
        int CHART_LINE_BUILD_BEHAVIOUR_INTERVAL                 = 822;
        
        //All/AccountChartLines
        int CHART_LINE_ID_BALANCE                                   = 0;
        
        //All/AccountChartLines - Incoming
        int CHART_LINE_ID_INCOMING_INCOME_ENTRIES                   = 1;
        int CHART_LINE_ID_INCOMING_INCOME_AGGREGATE                 = 2;
        int CHART_LINE_ID_INCOMING_INCOME_PER_WEEK                  = 3;
        int CHART_LINE_ID_INCOMING_INCOME_PER_MONTH                 = 4;
        int CHART_LINE_ID_INCOMING_INCOME_PER_YEAR                  = 5;
        
        int CHART_LINE_ID_INCOMING_INTERNAL_TRANSFERS_ENTRIES       = 6;
        int CHART_LINE_ID_INCOMING_INTERNAL_TRANSFERS_AGGREGATE     = 7;
        int CHART_LINE_ID_INCOMING_INTERNAL_TRANSFERS_PER_WEEK      = 8;
        int CHART_LINE_ID_INCOMING_INTERNAL_TRANSFERS_PER_MONTH     = 9;
        int CHART_LINE_ID_INCOMING_INTERNAL_TRANSFERS_PER_YEAR      = 10;
        
        int CHART_LINE_ID_INCOMING_TOTAL_ENTRIES                    = 11;
        int CHART_LINE_ID_INCOMING_TOTAL_AGGREGATE                  = 12;
        int CHART_LINE_ID_INCOMING_TOTAL_PER_WEEK                   = 13;
        int CHART_LINE_ID_INCOMING_TOTAL_PER_MONTH                  = 14;
        int CHART_LINE_ID_INCOMING_TOTAL_PER_YEAR                   = 15;
        
        //All/AccountChartLines - Outgoing
        int CHART_LINE_ID_OUTGOING_PURCHASES_ENTRIES                = 16;
        int CHART_LINE_ID_OUTGOING_PURCHASES_AGGREGATE              = 17;
        int CHART_LINE_ID_OUTGOING_PURCHASES_PER_WEEK               = 18;
        int CHART_LINE_ID_OUTGOING_PURCHASES_PER_MONTH              = 19;
        int CHART_LINE_ID_OUTGOING_PURCHASES_PER_YEAR               = 20;
        
        int CHART_LINE_ID_OUTGOING_DDSO_ENTRIES                     = 21;
        int CHART_LINE_ID_OUTGOING_DDSO_AGGREGATE                   = 22;
        int CHART_LINE_ID_OUTGOING_DDSO_PER_WEEK                    = 23;
        int CHART_LINE_ID_OUTGOING_DDSO_PER_MONTH                   = 24;
        int CHART_LINE_ID_OUTGOING_DDSO_PER_YEAR                    = 25;
       
        int CHART_LINE_ID_OUTGOING_OTHER_ENTRIES                    = 26;
        int CHART_LINE_ID_OUTGOING_OTHER_AGGREGATE                  = 27;
        int CHART_LINE_ID_OUTGOING_OTHER_PER_WEEK                   = 28;
        int CHART_LINE_ID_OUTGOING_OTHER_PER_MONTH                  = 29;
        int CHART_LINE_ID_OUTGOING_OTHER_PER_YEAR                   = 30;
        
        int CHART_LINE_ID_OUTGOING_SPEND_ENTRIES                    = 31;
        int CHART_LINE_ID_OUTGOING_SPEND_AGGREGATE                  = 32;
        int CHART_LINE_ID_OUTGOING_SPEND_PER_WEEK                   = 33;
        int CHART_LINE_ID_OUTGOING_SPEND_PER_MONTH                  = 34;
        int CHART_LINE_ID_OUTGOING_SPEND_PER_YEAR                   = 35;
        
        int CHART_LINE_ID_OUTGOING_INTERNAL_TRANSFERS_ENTRIES       = 36;
        int CHART_LINE_ID_OUTGOING_INTERNAL_TRANSFERS_AGGREGATE     = 37;
        int CHART_LINE_ID_OUTGOING_INTERNAL_TRANSFERS_PER_WEEK      = 38;
        int CHART_LINE_ID_OUTGOING_INTERNAL_TRANSFERS_PER_MONTH     = 39;
        int CHART_LINE_ID_OUTGOING_INTERNAL_TRANSFERS_PER_YEAR      = 40;
        
        int CHART_LINE_ID_OUTGOING_TOTAL_ENTRIES                    = 41;
        int CHART_LINE_ID_OUTGOING_TOTAL_AGGREGATE                  = 42;
        int CHART_LINE_ID_OUTGOING_TOTAL_PER_WEEK                   = 43;
        int CHART_LINE_ID_OUTGOING_TOTAL_PER_MONTH                  = 44;
        int CHART_LINE_ID_OUTGOING_TOTAL_PER_YEAR                   = 45;
        
        //Category/EntryChartLines
        int CHART_LINE_ID_ENTRIES                               = 0;
        int CHART_LINE_ID_AGGREGATE                             = 1;
        int CHART_LINE_ID_PER_WEEK                              = 2;
        int CHART_LINE_ID_PER_MONTH                             = 3;
        int CHART_LINE_ID_PER_YEAR                              = 4;
        
        //Line Chart - X Axis
        int CHART_LINE_X_AXIS_MODE_DIRECT                       = 840;
        int CHART_LINE_X_AXIS_MODE_COMPRESSED                   = 841;
        int CHART_LINE_X_AXIS_MODE_INTERVAL                     = 842;
        int CHART_LINE_X_AXIS_MODE_INTERVAL_COMPARE             = 843;
}