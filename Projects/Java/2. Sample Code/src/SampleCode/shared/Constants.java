/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SampleCode.shared;

/**
 *
 * @author PaulSu
 */
public interface Constants {

    //Menu
    int MENUITEM_FILE_CLOSE                                 = 0;
    int MENUITEM_NODE_TYPE_SETUP                            = 1;
    int MENUITEM_ABOUT                                      = 2;
   
    //Nodes Parameters
    int NODE_ID                                             = 10;
    int PARENT_ID                                           = 11;
    int ORDER_ID                                            = 12;
    int NAME                                                = 13;
    int ICON_ID                                             = 14;
    int CONTENT                                             = 15;
    int TYPE                                                = 16;

    //Node Types
    int NODE_LIBRARY_ROOT                                   = 10;
    int NODE_STANDARD                                       = 11;
    int NODE_SEARCH_RESULTS_ROOT                            = 12;
    int NODE_SEARCH_RESULTS_NOT_FOUND                       = 13;
    
    int NODE_TYPE_ROOT                                      = 20;
    int NODE_TYPE_PARENT                                    = 21;
    int NODE_TYPE_CHILD                                     = 22;
    
    int NODE_ADD_NEW                                        = 30;
    int NODE_ADD_COPIED                                     = 31;
    
    int NODE_INSERT_NEW                                     = 40;
    int NODE_INSERT_COPIED                                  = 41;
    
       
    //Database Queries
    int DB_GET_NODES_ON_BRANCH                              = 10;
    int DB_GET_LIBRARY_ROOT_BRANCH                          = 11;
    int DB_GET_LIBRARY_LATEST_PRIMARY_KEY                   = 12;
    int DB_GET_CONTENT                                      = 13;
    
    int DB_SIZE_OF_NAME                                     = 256;
    int DB_SIZE_OF_CONTENT                                  = 32000;
    
    //Library
    int TREEITEM_EXPAND                                     = 10;
    int TREEITEM_COLLAPSE                                   = 11;
    
    int GENERATE_TREEITEM_MIRROR                            = 10;
    int GENERATE_TREEITEM_CLIPBOARD                         = 11;
    
    int SAVE_SELECTED                                       = 10;
    int SAVE_SELECTED_PREVIOUS                              = 11;
    int SAVE_SELECTED_AND_DISCARD                           = 12;
    int SAVE_CONTENT_MAXIMUM_AMOUNT                         = DB_SIZE_OF_CONTENT;
    
    //Context Menu
    int CONTEXT_MENU_SCOPE_NODE_ONLY                        = 10;
    int CONTEXT_MENU_SCOPE_ALL_PARENT_NODES                 = 11;
    int CONTEXT_MENU_SCOPE_ALL_CHILD_NODES                  = 12;
    
    int CONTEXT_MENU_DEPTH_LOCAL_BRANCH                     = 10;
    int CONTEXT_MENU_DEPTH_ALL_DECENDENTS                   = 11;

    
    //Breadcrumb Trail
    int BREADCRUMB_TEXT_BLUE                                = 10;
    int BREADCRUMB_TEXT_BLACK                               = 11;
    
    int BREADCRUMB_TYPE_NODE                                = 10;
    int BREADCRUMB_TYPE_SEPARATOR                           = 11;

    //Icon ID
    int NODE_ICON_ID_FOLDER                                 = 1;

    int NODE_ICON_ID_JAVA                                   = 2;
    int NODE_ICON_ID_CPLUSPLUS                              = 3;
    int NODE_ICON_ID_SQL                                    = 4;

    int NODE_ICON_ID_BLANK                                  = 5;
    int NODE_ICON_ID_BLACK                                  = 6;
    int NODE_ICON_ID_BLUE                                   = 7;
    int NODE_ICON_ID_GREEN                                  = 8;
    int NODE_ICON_ID_GREY                                   = 9;
    int NODE_ICON_ID_ORANGE                                 = 10;
    int NODE_ICON_ID_PURPLE                                 = 11;
    int NODE_ICON_ID_RED                                    = 12;
    int NODE_ICON_ID_YELLOW                                 = 13;
    
    int NUMBER_OF_NODE_ICONS                                = 15;
    
    int NODE_UP                                             = 10;
    int NODE_DOWN                                           = 11;

    //Search Field
    int SEARCH_TEXT_FIELD_STYLE_NOT_IN_USE                  = 10;
    int SEARCH_TEXT_FIELD_STYLE_IN_USE                      = 11;
    
    int PAGE_SHOW                                           = 10;
    int PAGE_HIDE                                           = 11;
    
    int SEARCH_HIGHLIGHT_FIRST                              = 10;
    int SEARCH_HIGHLIGHT_PREVIOUS                           = 11;
    int SEARCH_HIGHLIGHT_NEXT                               = 12;
    
    //Code Editor
    int TAB_SIZE                                            = 4;
    
    int TAB_FORWARD                                         = 10;
    int TAB_BACKWARD                                        = 11;
    
    int TAB_PANE_CODE                                       = 0;
    int TAB_PANE_ASCII_TABLE                                = 1;
    int TAB_PANE_ESCAPE_SEQUENCE                            = 2;
    int TAB_PANE_INSTRUCTIONS                               = 3;
    
    int NORMAL_TO_FULL                                      = 10;
    int FULL_TO_NORMAL                                      = 11;
    
    //Misc
    boolean BUTTONS_VISIBLE                                 = true;
    boolean BUTTONS_NOT_VISIBLE                             = false;
}