/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SampleCode.library;

import SampleCode.library.codeEditor.CodeEditor;
import SampleCode.library.breadcrumbTrail.BreadcrumbTrail;
import SampleCode.library.contextMenu.ContextMenu;
import SampleCode.shared.*;
import static SampleCode.shared.Constants.SEARCH_TEXT_FIELD_STYLE_NOT_IN_USE;
import static SampleCode.shared.Constants.SEARCH_TEXT_FIELD_STYLE_IN_USE;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;

/**
 *
 * @author PaulSu
 */
public class Library implements Constants {

    private final SplitPane                 spGlobal;
 
    private final TextField                 tfSearch;
    private final ToolBar                   tbBreadcrumbTrail;
    
    private final Button                    btnSearchByName;
    private final Button                    btnSearchByCode;
    
    private final AnchorPane                apDeleteConfirmation;
    private final Button                    btnDeleteConfirm;
    private final Button                    btnDeleteCancel;
    
    private final AnchorPane                apContentExceedMaximum;
    private final Label                     labContentExceedMaximum;
    private final Button                    btnContentExceedMaximumClose;

    private final BreadcrumbTrail           objBreadcrumbTrail;
    private final Search                    objSearch;
    private final ContextMenu               objContextMenu;
    private final CodeEditor                objCodeEditor;
    
    private final TreeView<Node>            tvLibrary;
    private final TreeItem<Node>            tiDefault;
    private final TreeItem<Node>            tiRoot;
    
    private TreeItem<Node>                  tiSearchOrigin;
    private TreeItem<Node>                  tiSearchRoot;
    private TreeItem<Node>                  tiSelectedPrevious;
    
    private TreeItem<Node>                  tiSelected;
    private TreeItem<Node>                  tiClipboard;
    private String                          sClipboardContent;
    
    private Timeline                        tlSaveIconShowFade;
    private Timeline                        tlSaveIconShowFadeFullScreen;
    private Timeline                        tlSaveIconShowFadeFullScreenWithSearch;
    
    private ImageView                       ivSaveIcon;
    private ImageView                       ivSaveIconFullScreen;
    private ImageView                       ivSaveIconFullScreenWithSearch;
    
    private String[]                        arrSearchFragments;
    
    private boolean                         bSearchActive;
    private boolean                         bSearchByName;
    private boolean                         bSearchByCode;
    
    private int                             iSearchTextFieldStyle;

    public Library(SplitPane spGlobal,
                        TreeView<Node> tvLibrary,       TextField tfSearch,                                                     //Library
                        Button btnSearchByName,         Button btnSearchByCode,                                                 //Library
                        TabPane tpViewer,
                            TextField tfHeader,         ToolBar tbBreadcrumbTrail,      ImageView ivSaveIcon,  TextArea taCode, //Viewer - Tab - Code
                            Button btnSearchPrevious,   Button btnSearchNext,           Label labSearchNumberOfMatches,         //Viewer - Tab - Code
                            TextArea taAsciiTable,                                                                              //Viewer - Tab - Ascii Table
                            TextArea taEscapeSequences,                                                                         //Viewer - Tab - Escape Sequences
                            TextArea taInstructions,                                                                            //Viewer - Tab - Instructions
                        AnchorPane apFullScreen,        TextArea taFullScreen,                                                  //Full Screen
                            Button btnSearchPreviousFullScreen, Button btnSearchNextFullScreen,                                 //Full Screen
                            Label labSearchNumberOfMatchesFullScreen,                                                           //Full Screen
                            ImageView ivSaveIconFullScreen, ImageView ivSaveIconFullScreenWithSearch,                           //Full Screen
                        AnchorPane apDeleteConfirmation, Button btnDeleteConfirm, Button btnDeleteCancel,                       //Delete Confirmation
                        AnchorPane apContentExceedMaximum, Label labContentExceedMaximum,                                       //Content Exceed Maximum
                        Button btnContentExceedMaximumClose)                                                                    //Content Exceed Maximum
    {
        this.spGlobal               = spGlobal;
        this.tvLibrary              = tvLibrary;
       
        this.tfSearch               = tfSearch;
        this.tbBreadcrumbTrail      = tbBreadcrumbTrail;
        this.ivSaveIcon             = ivSaveIcon;

        this.btnSearchByName        = btnSearchByName;
        this.btnSearchByCode        = btnSearchByCode;
        
        this.apDeleteConfirmation   = apDeleteConfirmation;
        this.btnDeleteConfirm       = btnDeleteConfirm;
        this.btnDeleteCancel        = btnDeleteCancel;
        
        this.apContentExceedMaximum             = apContentExceedMaximum;
        this.labContentExceedMaximum            = labContentExceedMaximum;
        this.btnContentExceedMaximumClose       = btnContentExceedMaximumClose;
                
        this.ivSaveIconFullScreen               = ivSaveIconFullScreen;
        this.ivSaveIconFullScreenWithSearch     = ivSaveIconFullScreenWithSearch;
        
        objBreadcrumbTrail          = new BreadcrumbTrail(tbBreadcrumbTrail);

        tlSaveIconShowFade                      = new Timeline();
        tlSaveIconShowFadeFullScreen            = new Timeline();
        tlSaveIconShowFadeFullScreenWithSearch  = new Timeline();
        
        tiRoot                      = new TreeItem<>(new Node(NODE_LIBRARY_ROOT));
        tiDefault                   = new TreeItem<>(new Node(0, "New Node", NODE_ICON_ID_BLACK));
        tiSelected                  = new TreeItem<>(new Node(0, "", NODE_ICON_ID_BLANK));
        tiSelected                  = tiRoot;
        tiSearchOrigin              = tiRoot;
        tiSelectedPrevious          = tiRoot;

        objContextMenu              = new ContextMenu(this);
        objCodeEditor               = new CodeEditor(this, tpViewer, tfHeader, taCode,
                                                    btnSearchPrevious, btnSearchNext,
                                                    btnSearchPreviousFullScreen, btnSearchNextFullScreen,
                                                    labSearchNumberOfMatches, labSearchNumberOfMatchesFullScreen,
                                                    apFullScreen, taFullScreen,
                                                    taAsciiTable, taEscapeSequences, taInstructions);
        objSearch                   = new Search();
        
        spGlobal.setDividerPositions(0.2);
        
        setSearchByName(true);
        setSearchByCode(true);
        setSearchTextStyle(SEARCH_TEXT_FIELD_STYLE_NOT_IN_USE);
        
        loadLibrary();
        
        initSaveIconShowFade();
        initButtonTooltips();
        initMouseEventHandlers();
        initKeyEventHandlersGlobal();
        initKeyEventHandlersLibrary();
        initKeyEventHandlersSearch();
        initKeyEventHandlersDelete();
        initEventListeners();
        
        expandDecendents();
        
        refreshSelectedContextMenu();
        
        objCodeEditor.setTabPane(TAB_PANE_INSTRUCTIONS);
    }
    
    
    //Misc
    private void refreshSelectedTreeItem()
    {
        tiSelected = tvLibrary.getSelectionModel().getSelectedItem();
    }
    private void refreshTreeviewFocusPosition()
    {
        tvLibrary.getSelectionModel().select(tiSelected);
    }
    private void refreshSelectedContextMenu()
    {
        if(tiSelected != null)
        {
            if      (isSelectedRoot())      tvLibrary.setContextMenu(objContextMenu.getContextMenu(NODE_TYPE_ROOT));
            else if (!tiSelected.isLeaf())  tvLibrary.setContextMenu(objContextMenu.getContextMenu(NODE_TYPE_PARENT));
            else                            tvLibrary.setContextMenu(objContextMenu.getContextMenu(NODE_TYPE_CHILD));
        }
    }
    private int getIndexOnBranch(TreeItem<Node> tiNode)
    {
        return tiNode.getParent().getChildren().indexOf(tiNode);
    }
    private TreeItem<Node> getOnBranch(TreeItem<Node> tiParent, int i)
    {
        return tiParent.getChildren().get(i);
    }
    private int getSizeOfBranch(TreeItem<Node> tiParent)
    {
        return tiParent.getChildren().size();
    }
    private TreeItem<Node> getLastOnBranch(TreeItem<Node> tiParent)
    {
        return getOnBranch(tiParent, getSizeOfBranch(tiParent)-1);
    }
    private void setTreeItemGraphic(TreeItem<Node> tiNode)
    {
        tiNode.setGraphic(Icons.getIconImageView(tiNode.getValue().getIconID()));
    }
    private int getParentID(TreeItem<Node> tiNode)
    {
        return tiNode.getParent().getValue().getNodeID();
    }
    private int getType(TreeItem<Node> tiNode)
    {
        return tiNode.getValue().getType();
    }
    private void reloadTreeItem(TreeItem<Node> tiNode)
    {
        int iIndex;
        
        iIndex = tiNode.getParent().getChildren().indexOf(tiNode);

        tiNode.getParent().getChildren().set(iIndex, tiNode);
        
        setTreeItemGraphic(tiNode);
    }
    private void refreshLibraryFocusModel()
    {
        int i = tvLibrary.getRow(tiSelected);
        
        tvLibrary.getFocusModel().focus(i);
    }
    private void syncFocusAndSelected()
    {
        tvLibrary.getFocusModel().focus(tvLibrary.getRow(tiSelected));
    }
    private void initSaveIconShowFade()
    {
        tlSaveIconShowFade.getKeyFrames().add(new KeyFrame(Duration.millis(0), new KeyValue(ivSaveIcon.opacityProperty(), 0)));
        tlSaveIconShowFade.getKeyFrames().add(new KeyFrame(Duration.millis(100), new KeyValue(ivSaveIcon.opacityProperty(), 1)));
        tlSaveIconShowFade.getKeyFrames().add(new KeyFrame(Duration.millis(5000), new KeyValue(ivSaveIcon.opacityProperty(), 0)));
        
        tlSaveIconShowFadeFullScreen.getKeyFrames().add(new KeyFrame(Duration.millis(0), new KeyValue(ivSaveIconFullScreen.opacityProperty(), 0)));
        tlSaveIconShowFadeFullScreen.getKeyFrames().add(new KeyFrame(Duration.millis(100), new KeyValue(ivSaveIconFullScreen.opacityProperty(), 1)));
        tlSaveIconShowFadeFullScreen.getKeyFrames().add(new KeyFrame(Duration.millis(5000), new KeyValue(ivSaveIconFullScreen.opacityProperty(), 0)));
        
        tlSaveIconShowFadeFullScreenWithSearch.getKeyFrames().add(new KeyFrame(Duration.millis(0), new KeyValue(ivSaveIconFullScreenWithSearch.opacityProperty(), 0)));
        tlSaveIconShowFadeFullScreenWithSearch.getKeyFrames().add(new KeyFrame(Duration.millis(100), new KeyValue(ivSaveIconFullScreenWithSearch.opacityProperty(), 1)));
        tlSaveIconShowFadeFullScreenWithSearch.getKeyFrames().add(new KeyFrame(Duration.millis(5000), new KeyValue(ivSaveIconFullScreenWithSearch.opacityProperty(), 0)));
    }
    private boolean checkContentExceedMaximumLength(String sText)
    {
        boolean bCheck = (sText.length() >= SAVE_CONTENT_MAXIMUM_AMOUNT);
        
        if(bCheck)
            labContentExceedMaximum.setText("The amount of text in code/notes (" + sText.length() + " characters) exceeds the maximum of " + SAVE_CONTENT_MAXIMUM_AMOUNT + " characters." + "\n"
                                          + "Only the inital " + SAVE_CONTENT_MAXIMUM_AMOUNT + " characters will be saved only, copy and paste the remaining characters to a new node to save.");
        else
            labContentExceedMaximum.setText("");
        
        apContentExceedMaximum.setVisible(bCheck);
        
        return bCheck;
    }
    
    //Library Load        
    private void populateBranch(TreeItem<Node> tiParent)
    {
        TreeItem<Node> tiChild;
        int iNumberOfTreeItemsOnBranch;
        
        iNumberOfTreeItemsOnBranch = getSizeOfBranch(tiParent);

        for(int i = 0 ; i<iNumberOfTreeItemsOnBranch ; i++)
        {
            tiChild = getOnBranch(tiParent, i);

            Database.getAndAppendBranchToTreeItem(tiChild, getNodeParameter(tiChild, NODE_ID));

            if(!tiChild.isLeaf())
                populateBranch(tiChild);
        }
    }    
    private void loadLibrary()
    {
        Database.getAndAppendBranchToTreeItem(tiRoot, getNodeParameter(tiRoot, NODE_ID));
        
        populateBranch(tiRoot);
        
        setRoot(tiRoot);
        
        expand();
    }
    
    
    //Expand/Collapse All
    private void setTreeItemExpandedState(TreeItem<Node> tiNode, int iState)
    {
        switch(iState)
        {
            case TREEITEM_EXPAND:   tiNode.setExpanded(true);   break;
            case TREEITEM_COLLAPSE: tiNode.setExpanded(false);  break;
        }
    }
    private void setTreeItemExpandedStateOnAllImmediateDecendents(TreeItem<Node> tiParent, int iState)
    {
        TreeItem<Node> tiChildNode;
        int iNumberOfTreeItemsOnBranch;
        
        iNumberOfTreeItemsOnBranch = getSizeOfBranch(tiParent);

        for(int i = 0 ; i<iNumberOfTreeItemsOnBranch ; i++)
        {
            tiChildNode = getOnBranch(tiParent, i);
            
            if(!tiChildNode.isLeaf())
            {
                setTreeItemExpandedState(tiChildNode, iState);
                
                setTreeItemExpandedStateOnAllImmediateDecendents(tiChildNode, iState);
            }
        } 
    }

    
    //Adding Node
    private void appendToBranch(TreeItem<Node> tiParent, TreeItem<Node> tiChild)
    {
        tiParent.getChildren().add(tiChild);
        
        setTreeItemGraphic(tiChild);
    }
    private void insertToBranch(TreeItem<Node> tiSelected, TreeItem<Node> tiChild)
    {
        tiSelected.getParent().getChildren().add(getIndexOnBranch(tiSelected), tiChild);
        
        setTreeItemGraphic(tiChild);
    }
    private int findLastTreeItemOnBranchIcon()
    {
        TreeItem<Node> tiLastTreeItemOnBranch;

        if(getSizeOfBranch(tiSelected) == 0)        return getNodeParameter(tiDefault, ICON_ID);
        else
        {
            tiLastTreeItemOnBranch = getLastOnBranch(tiSelected);

            if(tiLastTreeItemOnBranch.isLeaf())     return getNodeParameter(tiLastTreeItemOnBranch, ICON_ID);
            else                                    return getNodeParameter(tiDefault, ICON_ID);
        }
    }
    private String findGenerateTreeItemName(int iType)
    {
        switch(iType)
        {
            case NODE_ADD_NEW:          return getNodeName(tiDefault);
            case NODE_ADD_COPIED:       return getNodeName(tiClipboard);
            case NODE_INSERT_NEW:       return getNodeName(tiDefault);
            case NODE_INSERT_COPIED:    return getNodeName(tiClipboard);
            default:                    return getNodeName(tiDefault);
        }
    }
    private int findGenerateTreeItemIcon(int iType)
    {
        switch(iType)
        {
            case NODE_ADD_NEW:
            {
                if(tiSelected.isLeaf()) return getNodeParameter(tiSelected, ICON_ID);
                else                    return findLastTreeItemOnBranchIcon();
            }
            case NODE_ADD_COPIED:       return getNodeParameter(tiClipboard, ICON_ID);
            case NODE_INSERT_NEW:
            {
                if(tiSelected.isLeaf()) return getNodeParameter(tiSelected, ICON_ID);
                else                    return getNodeParameter(tiDefault, ICON_ID);
                
            }
            case NODE_INSERT_COPIED:    return getNodeParameter(tiClipboard, ICON_ID);
            default:                    return findLastTreeItemOnBranchIcon();
        }
    }
    private TreeItem<Node> generateTreeItem(int iType)
    {
        TreeItem<Node> tiNew;
        int iParentID, iOrderID;
        
        tiNew = new TreeItem<>(new Node(
            Database.getNextNodeID(),
            findGenerateTreeItemName(iType),
            findGenerateTreeItemIcon(iType)));
        
        if((iType == NODE_INSERT_NEW) || (iType == NODE_INSERT_COPIED))
        {
            iParentID   = getNodeParameter(tiSelected.getParent(), NODE_ID);
            iOrderID    = getIndexOnBranch(tiSelected);
        }
        else
        {
            if(isSelectedRoot())            iParentID   = 0;
            else                            iParentID   = getNodeParameter(tiSelected, NODE_ID);

            if(isSelectedRoot())            iOrderID    = getSizeOfBranch(tiRoot);
            else if(tiSelected.isLeaf())    iOrderID    = 0;
            else                            iOrderID    = getSizeOfBranch(tiSelected);
        }
        
        if((iType == NODE_INSERT_NEW) || (iType == NODE_INSERT_COPIED))
            Database.incrementBranchNodeOrderID(iParentID, iOrderID);
        
        switch(iType)
        {
            case NODE_ADD_NEW:          Database.addNode(tiNew.getValue(), iParentID, iOrderID);                            break;
            case NODE_ADD_COPIED:       Database.addNode(tiNew.getValue(), iParentID, iOrderID, sClipboardContent);         break;
            case NODE_INSERT_NEW:       Database.addNode(tiNew.getValue(), iParentID, iOrderID);                            break;
            case NODE_INSERT_COPIED:    Database.addNode(tiNew.getValue(), iParentID, iOrderID , sClipboardContent);        break;
        }
                
        return tiNew;
    }

    
    //Removing Node
    private void removeFromBranch(TreeItem<Node> tiNode)
    {
        Database.removeNode(getNodeParameter(tiNode ,NODE_ID));

        tiNode.getParent().getChildren().remove(tiNode);
    }
    private void removeAllFromBranch(TreeItem<Node> tiParent)
    {
        TreeItem<Node> tiRemove;
        int iNumberOfNodesOnBranch;
        
        iNumberOfNodesOnBranch = getSizeOfBranch(tiParent);

        for(int i = (iNumberOfNodesOnBranch-1) ; i>=0 ; i--)
        {
            tiRemove = getOnBranch(tiParent, i);
            
            removeFromBranch(tiRemove);

            if(!tiRemove.isLeaf())
                removeAllFromBranch(tiRemove);
        }  
    }
    
    
    //Move Node
    private void swap(TreeItem<Node> tiSelected, TreeItem<Node> tiSibling, int iDirection)
    {
        int iSelectedNodeID, iSiblingNodeID,
            iSelectedPosition, iSiblingPosition,
            iSelectedIndex;
        
        iSelectedIndex = tvLibrary.getSelectionModel().getSelectedIndex();
        
        //Swap TreeItems within Treeview
        iSelectedPosition = getIndexOnBranch(tiSelected);
        iSiblingPosition  = getIndexOnBranch(tiSibling);
        
        iSelectedNodeID = getNodeParameter(tiSelected ,NODE_ID);
        iSiblingNodeID  = getNodeParameter(tiSibling ,NODE_ID);
        
        tiSelected.getParent().getChildren().set(iSiblingPosition, tiSelected);
        tiSelected.getParent().getChildren().set(iSelectedPosition, tiSibling);

        //Swap ORDER_ID within the elements within Library/Database
        Database.swapNodeOrderID(iSelectedNodeID, iSelectedPosition, iSiblingNodeID, iSiblingPosition);
        
        //Set the focus on TrreView<Node> to correct position
        switch(iDirection)
        {
            case NODE_UP:       tvLibrary.getSelectionModel().select(iSelectedIndex-1);    break;
            case NODE_DOWN:     tvLibrary.getSelectionModel().select(iSelectedIndex+1);    break;
        }
    }
    private void moveUp()
    {
        if( (getSizeOfBranch(tiSelected.getParent()) > 1) && (getIndexOnBranch(tiSelected) > 0) )
            swap(tiSelected, tiSelected.previousSibling(), NODE_UP);
    }
    private void moveDown()
    {
        if( (getSizeOfBranch(tiSelected.getParent()) > 1) && (getIndexOnBranch(tiSelected) < (getSizeOfBranch(tiSelected.getParent())-1)) )
            swap(tiSelected, tiSelected.nextSibling(), NODE_DOWN);
    }

    
    //Open/Save/Delete Node
    private int getNodeParameter(TreeItem<Node> tiNode, int iParameter)
    {
        switch(iParameter)
        {
            case NODE_ID:       return tiNode.getValue().getNodeID();
            case ICON_ID:       return tiNode.getValue().getIconID();
            case TYPE:          return tiNode.getValue().getType();
        }
        
        return 0;
    }
    private void setNodeParameter(TreeItem<Node> tiNode, int iParameter, int iValue)
    {
        if((tiSelected != null))
        {
            switch(iParameter)
            {
                case NODE_ID:
                {
                    tiNode.getValue().setNodeID(iValue);
                    
                    break;
                }
                case ICON_ID:
                {
                    tiNode.getValue().setIconID(iValue);
                    tiNode.setGraphic(tiNode.getValue().getIconResource());
                    
                    break;
                }
            }
            
            reloadTreeItem(tiNode);
        }   
    }
    private String getNodeName(TreeItem<Node> tiNode)
    {
        return tiNode.getValue().getName();
    }
    private void setNodeName(TreeItem<Node> tiNode, String sValue)
    {
        tiNode.getValue().setName(sValue);
            
        reloadTreeItem(tiNode);
    }
    private void open()
    {
        if((tiSelectedPrevious != null) && (tiSelectedPrevious != tiRoot) && (objCodeEditor.hasCodeChanged()))
            save(SAVE_SELECTED_PREVIOUS);
        
        objCodeEditor.setHeader(getNodeName(tiSelected));

        objCodeEditor.setText(Database.getContent(getNodeParameter(tiSelected, NODE_ID)));
        
        objCodeEditor.resetCodeChangeListener();

        objBreadcrumbTrail.buildBreadcrumbTrail(tiSelected);
        
        objCodeEditor.setTabPane(TAB_PANE_CODE);

        refreshSelectedContextMenu();
        
        if(bSearchActive)
            objCodeEditor.setSearch(true, arrSearchFragments);
        
        tiSelectedPrevious = tiSelected;
    }
    private void delete()
    {
        int iParentID, iOrderID;
        
        iParentID   = getParentID(tiSelected);
        iOrderID    = getIndexOnBranch(tiSelected);
        
        if(tiSelected.isLeaf())
            removeFromBranch(tiSelected);
        else
        {
            removeAllFromBranch(tiSelected);
            removeFromBranch(tiSelected);
        }
        
        Database.decrementBranchNodeOrderID(iParentID, iOrderID);
        
        setDeleteConfirmation(false);
        
        tiSelectedPrevious = null;
        
        if(isSelectedStandard())
            open();
    }
    private void showSaveIcon()
    {
        if(objCodeEditor.isFullScreen())
            if(bSearchActive)           tlSaveIconShowFadeFullScreenWithSearch.playFromStart();
            else                        tlSaveIconShowFadeFullScreen.playFromStart();
        else                            tlSaveIconShowFade.playFromStart();
    }
    
    //Context Menu Interface
    private void setAllNodesParameterOnBranch(TreeItem<Node> tiParent, int iParameter, int iScope, int iDepth, int iValue)
    {
        TreeItem<Node> tiNode;
        int iNumberOfNodesOnBranch;
        
        iNumberOfNodesOnBranch = getSizeOfBranch(tiParent);

        for(int i = 0 ; i<iNumberOfNodesOnBranch ; i++)
        {
            tiNode = getOnBranch(tiParent, i);

            if(tiNode.isLeaf() && (iScope == CONTEXT_MENU_SCOPE_ALL_CHILD_NODES))
                setNodeParameter(tiNode, iParameter, iValue); 
            
            if(!tiNode.isLeaf())
            {
                if(iScope == CONTEXT_MENU_SCOPE_ALL_PARENT_NODES)
                    setNodeParameter(tiNode, iParameter, iValue);
                
                if(iDepth == CONTEXT_MENU_DEPTH_ALL_DECENDENTS)
                    setAllNodesParameterOnBranch(tiNode, iParameter, iScope, iDepth, iValue);
            } 
        }
    }       
    
    
    //Search
    private void refreshSearchButtonStates()
    {
        if(bSearchByName)       btnSearchByName.setTextFill(Color.web("#0076a3"));
        else                    btnSearchByName.setTextFill(Color.web("#000000"));
        
        if(bSearchByCode)       btnSearchByCode.setTextFill(Color.web("#0076a3"));
        else                    btnSearchByCode.setTextFill(Color.web("#000000"));
    }
    private void setSearchActive(boolean bState)
    {
        btnSearchByName.setDisable(bState);
        btnSearchByCode.setDisable(bState);
        
        bSearchActive = bState;
    }
    private void toggleSearchByName()
    {
        bSearchByName = !bSearchByName;
        
        refreshSearchButtonStates();
    }
    private void toggleSearchByCode()
    {
        bSearchByCode = !bSearchByCode;
        
        refreshSearchButtonStates();
    }
    private void setSearchByName(boolean bState)
    {
        bSearchByName = bState;
        
        refreshSearchButtonStates();
    }
    private void setSearchByCode(boolean bState)
    {
        bSearchByCode = bState;
        
        refreshSearchButtonStates();
    }
    private void setSearchTextStyle(int iStyle)
    {
        switch(iStyle)
        {
            case SEARCH_TEXT_FIELD_STYLE_NOT_IN_USE:
            {       
                tfSearch.setStyle("-fx-text-fill: #bdbebf; -fx-font-size: 12;");

                tfSearch.setText("Search");
                
                break;
            }
            case SEARCH_TEXT_FIELD_STYLE_IN_USE:
            {       
                tfSearch.setStyle("-fx-text-fill: #000000; -fx-font-size: 12;");
                
                tfSearch.setText("");
                
                break;
            }
        }
        
        iSearchTextFieldStyle = iStyle;
    }
    private void clearSearch()
    {
        objSearch.clear();

        setSearchTextStyle(SEARCH_TEXT_FIELD_STYLE_NOT_IN_USE);
        
        setRoot(tiRoot);
        setSelected(tiRoot);
        expandDecendents();
        
        objCodeEditor.setSearch(false, null);
        setSearchActive(false);
    }
    private void parseSearchField(String sSearch)
    {
        if(sSearch.contains("|"))
        {
            arrSearchFragments = sSearch.split("[|]");

            for(int i = 0 ; i < arrSearchFragments.length ; i++)
                arrSearchFragments[i] = arrSearchFragments[i].trim();
        }
        else
        {
            arrSearchFragments = new String[1];
            arrSearchFragments[0] = sSearch;
        }
    }
    private void search()
    {
        if(!bSearchActive)
        {
            tiSearchOrigin = tiRoot;

            if(getSelected().isLeaf())  tiSearchOrigin = getSelected().getParent();
            else                        tiSearchOrigin = getSelected();
        }
        
        parseSearchField(tfSearch.getText().toLowerCase());

        tiSearchRoot = objSearch.generateSearchTreeview(tiSearchOrigin, arrSearchFragments, bSearchByName, bSearchByCode);

        setRoot(tiSearchRoot);

        expandDecendents(tiSearchRoot);
        
        setSearchActive(true);
    }


    //External Interface
    public void setRoot(TreeItem<Node> tiRoot)
    {
        tvLibrary.setRoot(tiRoot);
    }
    public TreeItem<Node> getSelected()
    {
        return tiSelected;
    }
    public TreeItem<Node> getClipboard()
    {
        return tiClipboard;
    }
    public boolean isSelectedStandard()
    {
        return((tiSelected != null) && (getType(tiSelected) == NODE_STANDARD));
    }
    public void setSelected(TreeItem<Node> tiNode)
    {
        tvLibrary.getSelectionModel().select(tiNode);
    }
    public boolean isSelectedRoot()
    {
        return (tiSelected == tvLibrary.getRoot());
    }
    public boolean isSelectedNull()
    {
        return (tiSelected == null);
    }
    public void expandDecendents()
    {
        setTreeItemExpandedState(tiSelected, TREEITEM_EXPAND);

        setTreeItemExpandedStateOnAllImmediateDecendents(tiSelected, TREEITEM_EXPAND);

        refreshTreeviewFocusPosition();
    }
    public void expandDecendents(TreeItem<Node> tiNode)
    {
        setTreeItemExpandedState(tiNode, TREEITEM_EXPAND);

        setTreeItemExpandedStateOnAllImmediateDecendents(tiNode, TREEITEM_EXPAND);

        refreshTreeviewFocusPosition();
    }
    public void collapseDecendents()
    {
        setTreeItemExpandedStateOnAllImmediateDecendents(tiSelected, TREEITEM_COLLAPSE);
     
        setTreeItemExpandedState(tiSelected, TREEITEM_COLLAPSE);

        refreshTreeviewFocusPosition();
    }
    public void expand()
    {
        setTreeItemExpandedState(tiSelected, TREEITEM_EXPAND);
    }
    public void collapse()
    {
        setTreeItemExpandedState(tiSelected, TREEITEM_COLLAPSE);
    }
    public void cut()
    {
        copy();
        delete();
    }
    public void copy()
    {
        tiClipboard         = tiSelected;
        sClipboardContent   = Database.getContent(getNodeParameter(tiClipboard, NODE_ID));
        
        objContextMenu.addClipboardMenuItems();
    }
    public void add(int iType)
    {
        appendToBranch(tiSelected, generateTreeItem(iType));

        if(iType == NODE_ADD_COPIED)
        {
            tiClipboard = null;
            
            objContextMenu.removeClipboardMenuItems();
        }
                    
        if(!isSelectedRoot())
        {
            setNodeParameter(tiSelected, ICON_ID, NODE_ICON_ID_FOLDER);
            
            expand();
        }
        
        refreshLibraryFocusModel();
    }
    public void insert(int iType)
    {
        insertToBranch(tiSelected, generateTreeItem(iType));
        
        if(iType == NODE_INSERT_COPIED)
        {
            tiClipboard = null;
            
            objContextMenu.removeClipboardMenuItems();
        }
        
        refreshSelectedTreeItem();
        refreshLibraryFocusModel();
    }
    public void setDeleteConfirmation(boolean bState)
    {
        apDeleteConfirmation.setVisible(bState);
        
        if(bState)
            btnDeleteConfirm.requestFocus();
    }
    public void save(int iType)
    {
        TreeItem<Node> tiSave;

        switch(iType)
        {
            case SAVE_SELECTED:                 tiSave = tiSelected;            break;
            case SAVE_SELECTED_PREVIOUS:        tiSave = tiSelectedPrevious;    break;
            default:                            tiSave = tiSelected;
        }

        setNodeName(tiSave, objCodeEditor.getHeader());
        
        if(checkContentExceedMaximumLength(objCodeEditor.getText()))
            Database.setNodeParameter(getNodeParameter(tiSave, NODE_ID), CONTENT, objCodeEditor.getText().substring(0, SAVE_CONTENT_MAXIMUM_AMOUNT));
        else
            Database.setNodeParameter(getNodeParameter(tiSave, NODE_ID), CONTENT, objCodeEditor.getText());

        objBreadcrumbTrail.buildBreadcrumbTrail(tiSave);

        objCodeEditor.resetCodeChangeListener();

        showSaveIcon();
    }
    
    
    
    //Context Menu
    public void setNodeIcon(int iScope, int iDepth, int iIcon)
    {
        if(iScope == CONTEXT_MENU_SCOPE_NODE_ONLY)      setNodeParameter(tiSelected, ICON_ID, iIcon);
        else                                            setAllNodesParameterOnBranch(tiSelected, ICON_ID, iScope, iDepth, iIcon);
    }
    

    //Event Handlers and Listeners
    private void initButtonTooltips()
    {
        Tooltip ttSearchByName          = new Tooltip("Search by Name");
        Tooltip ttSearchByCode          = new Tooltip("Search by Code/Notes");
        
        Font ttExplorerStandardFont     = new Font("System", 12);
        
        ttSearchByName.setFont(ttExplorerStandardFont);
        ttSearchByCode.setFont(ttExplorerStandardFont);
        
        btnSearchByName.setTooltip(ttSearchByName);
        btnSearchByCode.setTooltip(ttSearchByCode);
    }
    private void initMouseEventHandlers()
    {
        final EventHandler<MouseEvent>  ehMouseClick_tbBreadcrumbTrail = (MouseEvent me) -> {
            
            setSelected(objBreadcrumbTrail.getSelectedBreadcrumb().getTreeItem());
            
            if(isSelectedStandard())
            {
                open();
                
                refreshTreeviewFocusPosition();
            }
        };
        final EventHandler<MouseEvent>  ehMouseClick_tvLibrary = (MouseEvent me) -> {
            
            if(isSelectedStandard())
                open();
        };
        
        //Search
        final EventHandler<MouseEvent>  ehMouseClick_btnSearchByName = (MouseEvent me) -> {
            toggleSearchByName();
        };
        final EventHandler<MouseEvent>  ehMouseClick_btnSearchByCode = (MouseEvent me) -> {
            toggleSearchByCode();
        };
        final EventHandler<MouseEvent>  ehSearchClick = (MouseEvent me) -> {
            if(iSearchTextFieldStyle == SEARCH_TEXT_FIELD_STYLE_NOT_IN_USE)
                tfSearch.positionCaret(0);
        };
        
        //Delete
        final EventHandler<MouseEvent>  ehMouseClick_btnDeleteConfirm = (MouseEvent me) -> {
            delete();
        };
        final EventHandler<MouseEvent>  ehMouseClick_btnDeleteCancel = (MouseEvent me) -> {
            setDeleteConfirmation(false);
        };
        
        
        //Exceed Maximum Content
        final EventHandler<MouseEvent>  ehMouseClick_btnContentExceedMaximumClose = (MouseEvent me) -> {
            apContentExceedMaximum.setVisible(false);
        };
        
        tbBreadcrumbTrail.addEventHandler(MouseEvent.MOUSE_CLICKED, ehMouseClick_tbBreadcrumbTrail);
        tvLibrary.addEventHandler(MouseEvent.MOUSE_CLICKED, ehMouseClick_tvLibrary);
        
        btnSearchByName.addEventHandler(MouseEvent.MOUSE_CLICKED, ehMouseClick_btnSearchByName);
        btnSearchByCode.addEventHandler(MouseEvent.MOUSE_CLICKED, ehMouseClick_btnSearchByCode);
        
        btnDeleteConfirm.addEventHandler(MouseEvent.MOUSE_CLICKED, ehMouseClick_btnDeleteConfirm);
        btnDeleteCancel.addEventHandler(MouseEvent.MOUSE_CLICKED, ehMouseClick_btnDeleteCancel);
        
        btnContentExceedMaximumClose.addEventHandler(MouseEvent.MOUSE_CLICKED, ehMouseClick_btnContentExceedMaximumClose);
        
        tfSearch.addEventHandler(MouseEvent.MOUSE_CLICKED, ehSearchClick);
    }
    private void initKeyEventHandlersGlobal()
    {
        final EventHandler<KeyEvent>  ehKeyPressed_spGlobal = (KeyEvent ke) -> {
            
            if(ke.isControlDown())
            {
                switch(ke.getCode())
                {
                    case S:
                    {
                        if(isSelectedStandard())
                            save(SAVE_SELECTED);

                        break;
                    }
                    case F:
                    {
                        objCodeEditor.setFullScreen(true, bSearchActive);
                        
                        break;
                    }
                    case T:
                    {
                        System.out.println("Database Rows: " + Database.getNumberOfRowsInDatabase());
                        System.out.println("Loaded Nodes: " + Database.getNumberOfLoadedNodes());
                    }
                }
            }
            
            if(bSearchActive)
            {
                switch(ke.getCode())
                {
                    case UP:            objCodeEditor.setSearchHighlight(SEARCH_HIGHLIGHT_PREVIOUS);        break;
                    case DOWN:          objCodeEditor.setSearchHighlight(SEARCH_HIGHLIGHT_NEXT);            break;
                    case ESCAPE:
                    {
                        if(bSearchActive)
                        {
                            clearSearch();
                            
                            tvLibrary.requestFocus();
                        }

                        break;
                    }
                }
                
                //In order to stop library from moving up/down when coming out of full screen
                ke.consume();
            }
        };
            
        spGlobal.addEventFilter(KeyEvent.KEY_PRESSED, ehKeyPressed_spGlobal);
    }
    private void initKeyEventHandlersLibrary()
    {
        final EventHandler<KeyEvent>  ehKeyPress_tvLibrary = (KeyEvent ke) -> {

            switch(ke.getCode())
            {
                case ENTER:
                {
                    if(isSelectedStandard())
                        open();

                    break;
                }
                default:
                {
                    if( (!ke.isControlDown()) && (ke.getCode().isLetterKey()) || (ke.getCode().isDigitKey()) || (ke.getCode().isKeypadKey()) || (ke.getCode() == KeyCode.TAB) )
                    {
                        tfSearch.requestFocus();
                        
                        setSearchTextStyle(SEARCH_TEXT_FIELD_STYLE_IN_USE);
                    }
                }
            }
            if(!bSearchActive)
            {
                if(ke.isControlDown())
                {
                    if(isSelectedStandard())
                        switch(ke.getCode())
                        {
                            case A:                                     add(NODE_ADD_NEW);          break;
                            case I:
                            {
                                if(tiClipboard != null)                 insert(NODE_INSERT_COPIED);
                                else                                    insert(NODE_INSERT_NEW);

                                break;
                            }
                            case X:         if(tiSelected.isLeaf())     cut();                      break;
                            case C:         if(tiSelected.isLeaf())     copy();                     break;
                            case V:         if(tiClipboard != null)     add(NODE_ADD_COPIED);       break;
                            case UP:                                    moveUp();                   break;
                            case DOWN:                                  moveDown();                 break;
                        }
                    
                    if(isSelectedRoot())
                        switch(ke.getCode())
                        {
                            case A:                                     add(NODE_ADD_NEW);          break;
                        }
                }
                else
                {
                    switch(ke.getCode())
                    {
                        case DELETE:
                        {
                            if(isSelectedStandard())
                                setDeleteConfirmation(true);
                            
                            break;
                        }
                    }
                }
            }
        };
        
        tvLibrary.addEventFilter(KeyEvent.KEY_PRESSED, ehKeyPress_tvLibrary);
    }
    private void initKeyEventHandlersSearch()
    {
        final EventHandler<KeyEvent>  ehKeyPress_tfSearch = (KeyEvent ke) -> {
            
            switch(ke.getCode())
            {
                case ENTER:
                {
                    if( (tfSearch.getLength() > 0) && (bSearchByName || bSearchByCode) )
                        search();
                    
                    break;
                }
                case BACK_SPACE:
                {
                    if(tfSearch.getLength() == 1)
                        setSearchTextStyle(SEARCH_TEXT_FIELD_STYLE_NOT_IN_USE);
                    
                    break;
                }
                default:
                {
                    if(iSearchTextFieldStyle == SEARCH_TEXT_FIELD_STYLE_NOT_IN_USE)
                        setSearchTextStyle(SEARCH_TEXT_FIELD_STYLE_IN_USE);
                }
            }
        };
        
        tfSearch.addEventHandler(KeyEvent.KEY_PRESSED, ehKeyPress_tfSearch);
    }
    private void initKeyEventHandlersDelete()
    {
        final EventHandler<KeyEvent>  ehKeyPress_btnDeleteConfirm = (KeyEvent ke) -> {
            
            switch(ke.getCode())
            {
                case ENTER:     delete();       break;
            }
        };
        
        btnDeleteConfirm.addEventHandler(KeyEvent.KEY_PRESSED, ehKeyPress_btnDeleteConfirm);
    }
    private void initEventListeners()
    {
        tvLibrary.getSelectionModel().selectedItemProperty().addListener(new ChangeListener()
        {
            @Override
            public void changed(ObservableValue o, Object oldVal, Object newVal)
            {
                if(newVal != null)
                {
                    refreshSelectedTreeItem();
                    refreshSelectedContextMenu();
                }
            }
        });
        
        tvLibrary.getFocusModel().focusedIndexProperty().addListener(new ChangeListener()
        {
            @Override
            public void changed(ObservableValue o, Object oldVal, Object newVal)
            {
                syncFocusAndSelected();
            }
        });
        
        tfSearch.focusedProperty().addListener(new ChangeListener()
        {
            @Override
            public void changed(ObservableValue o, Object oldVal, Object newVal)
            {
                if(tfSearch.isFocused() && (iSearchTextFieldStyle == SEARCH_TEXT_FIELD_STYLE_NOT_IN_USE))
                    setSearchTextStyle(SEARCH_TEXT_FIELD_STYLE_IN_USE);
                else if (tfSearch.getLength() == 0)
                    setSearchTextStyle(SEARCH_TEXT_FIELD_STYLE_NOT_IN_USE);
            }
        });
    }
}