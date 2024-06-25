/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SampleCode.library.contextMenu;

import SampleCode.library.Library;
import SampleCode.shared.*;
import java.util.Arrays;
import javafx.event.ActionEvent;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;

/**
 *
 * @author PaulSu
 */
public class ContextMenu implements Constants {
    
    private final Library objLibrary;

    private final javafx.scene.control.ContextMenu cmRoot;
    private final javafx.scene.control.ContextMenu cmParent;
    private final javafx.scene.control.ContextMenu cmChild;
    
    MenuItem miClipboardRootAddCopied;
    MenuItem miClipboardParentAddCopied, miClipboardParentInsertCopied, miClipboardParentSeparator;
    MenuItem miClipboardChildAddCopied, miClipboardChildInsertCopied, miClipboardChildSeparator;

    public ContextMenu(Library objLibrary)
    {
        this.objLibrary = objLibrary;
        
        cmRoot                              = new javafx.scene.control.ContextMenu();
        cmParent                            = new javafx.scene.control.ContextMenu();
        cmChild                             = new javafx.scene.control.ContextMenu();
        
        //NB Need individual/unique nodes for each context menu, cannot add same node to multiple context menus
        miClipboardRootAddCopied            = generateMenuItem("Add Copied");
        
        miClipboardParentAddCopied          = generateMenuItem("Add Copied");
        miClipboardParentInsertCopied       = generateMenuItem("Insert Copied");
        miClipboardParentSeparator          = new SeparatorMenuItem();
                
        miClipboardChildAddCopied           = generateMenuItem("Add Copied");
        miClipboardChildInsertCopied        = generateMenuItem("Insert Copied");
        miClipboardChildSeparator           = new SeparatorMenuItem();
        
        buildContextMenus();
    }

    
    //Build Context Menu
    private ContextMenuNodeIconMenuItem generateNodeIconMenuItem(Integer iIcon, Integer iScope, Integer iDepth)
    {
        ContextMenuNodeIconMenuItem miGenerate = new ContextMenuNodeIconMenuItem(new ContextMenuNodeIcon(iIcon), iScope, iDepth);
        
        miGenerate.setOnAction((ActionEvent e) -> {
            objLibrary.setNodeIcon(miGenerate.getScope(), miGenerate.getDepth(), miGenerate.getIconID());
        });
        
        return miGenerate;
    }
    private MenuItem generateExpandMenuItem(String sName)
    {
        MenuItem miGenerate = new MenuItem(sName);
        
        miGenerate.setOnAction((ActionEvent e) -> {
            switch(sName)
            {
                case "Node":    objLibrary.expand();                    break;
                case "All":     objLibrary.expandDecendents();          break;
            }
        });
        
        return miGenerate;
    }
    private MenuItem generateCollapseMenuItem(String sName)
    {
        MenuItem miGenerate = new MenuItem(sName);
        
        miGenerate.setOnAction((ActionEvent e) -> {
            switch(sName)
            {
                case "Node":    objLibrary.collapse();                  break;
                case "All":     objLibrary.collapseDecendents();        break;
            }
        });
        
        return miGenerate;
    }
    private MenuItem generateMenuItem(String sName)
    {
        MenuItem miGenerate = new MenuItem(sName);
        
        miGenerate.setOnAction((ActionEvent e) -> {
            switch(sName)
            {
                case "Save":            objLibrary.save(SAVE_SELECTED);                 break;
                case "Add":             objLibrary.add(NODE_ADD_NEW);                   break;
                case "Add Copied":      objLibrary.add(NODE_ADD_COPIED);                break;
                case "Insert":          objLibrary.insert(NODE_INSERT_NEW);             break;
                case "Insert Copied":   objLibrary.insert(NODE_INSERT_COPIED);          break;
                case "Cut":             objLibrary.cut();                               break;
                case "Copy":            objLibrary.copy();                              break;
                case "Delete":          objLibrary.setDeleteConfirmation(true);         break;
            }
        });
        
        return miGenerate;
    }
    private MenuItem[] buildMenuItemsNodeIcon(Integer iScope, Integer iDepth)
    {
        MenuItem[] miMenuItems = new MenuItem[NUMBER_OF_NODE_ICONS];
        
        miMenuItems[0] = generateNodeIconMenuItem(NODE_ICON_ID_FOLDER, iScope, iDepth);
        miMenuItems[1] = new SeparatorMenuItem();
        miMenuItems[2] = generateNodeIconMenuItem(NODE_ICON_ID_JAVA, iScope, iDepth);
        miMenuItems[3] = generateNodeIconMenuItem(NODE_ICON_ID_CPLUSPLUS, iScope, iDepth);
        miMenuItems[4] = generateNodeIconMenuItem(NODE_ICON_ID_SQL, iScope, iDepth);
        
        miMenuItems[5] = new SeparatorMenuItem();
        miMenuItems[6] = generateNodeIconMenuItem(NODE_ICON_ID_BLACK, iScope, iDepth);
        miMenuItems[7] = generateNodeIconMenuItem(NODE_ICON_ID_BLUE, iScope, iDepth);
        miMenuItems[8] = generateNodeIconMenuItem(NODE_ICON_ID_GREEN, iScope, iDepth);
        miMenuItems[9] = generateNodeIconMenuItem(NODE_ICON_ID_GREY, iScope, iDepth);
        
        miMenuItems[10] = generateNodeIconMenuItem(NODE_ICON_ID_ORANGE, iScope, iDepth);
        miMenuItems[11] = generateNodeIconMenuItem(NODE_ICON_ID_PURPLE, iScope, iDepth);
        miMenuItems[12] = generateNodeIconMenuItem(NODE_ICON_ID_RED, iScope, iDepth);
        miMenuItems[13] = generateNodeIconMenuItem(NODE_ICON_ID_YELLOW, iScope, iDepth);
        miMenuItems[14] = generateNodeIconMenuItem(NODE_ICON_ID_BLANK, iScope, iDepth);
        
        return miMenuItems;
    }
    private MenuItem[] buildMenuItemsExpand()
    {
        MenuItem[] miMenuItems = new MenuItem[2];
        
        miMenuItems[0] = generateExpandMenuItem("Node");
        miMenuItems[1] = generateExpandMenuItem("All");
        
        return miMenuItems;
    }
    private MenuItem[] buildMenuItemsCollapse()
    {
        MenuItem[] miMenuItems = new MenuItem[2];
        
        miMenuItems[0] = generateCollapseMenuItem("Node");
        miMenuItems[1] = generateCollapseMenuItem("All");
        
        return miMenuItems;
    }
    private Menu buildMenu(String sName, Menu... mMenus)
    {
        Menu mMenu = new Menu(sName);
        
        mMenu.getItems().addAll(Arrays.asList(mMenus));

        return mMenu;
    }
    private Menu buildMenu(String sName, MenuItem... miMenuItems)
    {
        Menu mMenu = new Menu(sName);
        
        mMenu.getItems().addAll(Arrays.asList(miMenuItems));

        return mMenu;
    }
    private void buildContextMenus()
    {
        //Root
        cmRoot.getItems().add(generateMenuItem("Add"));
        cmRoot.getItems().add(new SeparatorMenuItem());
        cmRoot.getItems().add
            (buildMenu("Assign Icon",
                buildMenu("Local Branch Only",
                    buildMenu("All Parent Nodes",
                        buildMenuItemsNodeIcon(CONTEXT_MENU_SCOPE_ALL_PARENT_NODES,      CONTEXT_MENU_DEPTH_LOCAL_BRANCH)),
                    buildMenu("All Child Nodes",
                        buildMenuItemsNodeIcon(CONTEXT_MENU_SCOPE_ALL_CHILD_NODES,       CONTEXT_MENU_DEPTH_LOCAL_BRANCH))),
                buildMenu("All Branches",
                    buildMenu("All Parent Nodes",
                        buildMenuItemsNodeIcon(CONTEXT_MENU_SCOPE_ALL_PARENT_NODES,      CONTEXT_MENU_DEPTH_ALL_DECENDENTS)),
                    buildMenu("All Child Nodes",
                        buildMenuItemsNodeIcon(CONTEXT_MENU_SCOPE_ALL_CHILD_NODES,       CONTEXT_MENU_DEPTH_ALL_DECENDENTS)))));
        
        cmRoot.getItems().add(new SeparatorMenuItem());
        
        cmRoot.getItems().addAll(
             buildMenu("Expand",
                buildMenuItemsExpand()),
             buildMenu("Collapse",
                buildMenuItemsCollapse())
            );
        
        //Parent
        cmParent.getItems().add(generateMenuItem("Save"));
        cmParent.getItems().add(new SeparatorMenuItem());
        cmParent.getItems().add(generateMenuItem("Add"));
        cmParent.getItems().add(generateMenuItem("Insert"));
        cmParent.getItems().add(new SeparatorMenuItem());
        cmParent.getItems().add
            (buildMenu("Assign Icon",
                buildMenu("Node Only",
                    buildMenuItemsNodeIcon(CONTEXT_MENU_SCOPE_NODE_ONLY,                    CONTEXT_MENU_DEPTH_LOCAL_BRANCH)),
                buildMenu("Local Branch Only",
                    buildMenu("All Parent Nodes",
                        buildMenuItemsNodeIcon(CONTEXT_MENU_SCOPE_ALL_PARENT_NODES,         CONTEXT_MENU_DEPTH_LOCAL_BRANCH)),
                    buildMenu("All Child Nodes",
                        buildMenuItemsNodeIcon(CONTEXT_MENU_SCOPE_ALL_CHILD_NODES,          CONTEXT_MENU_DEPTH_LOCAL_BRANCH))),
                buildMenu("All Branches",
                    buildMenu("All Parent Nodes",
                        buildMenuItemsNodeIcon(CONTEXT_MENU_SCOPE_ALL_PARENT_NODES,         CONTEXT_MENU_DEPTH_ALL_DECENDENTS)),
                    buildMenu("All Child Nodes",
                        buildMenuItemsNodeIcon(CONTEXT_MENU_SCOPE_ALL_CHILD_NODES,          CONTEXT_MENU_DEPTH_ALL_DECENDENTS)))));
        
        cmParent.getItems().add(generateMenuItem("Delete"));
        cmParent.getItems().add(new SeparatorMenuItem());
        cmParent.getItems().addAll(
             buildMenu("Expand",
                buildMenuItemsExpand()),
             buildMenu("Collapse",
                buildMenuItemsCollapse())
            );
        
        //Child
        cmChild.getItems().add(generateMenuItem("Save"));
        cmChild.getItems().add(new SeparatorMenuItem());
        cmChild.getItems().add(generateMenuItem("Add"));
        cmChild.getItems().add(generateMenuItem("Insert"));
        cmChild.getItems().add(new SeparatorMenuItem());
        cmChild.getItems().add(generateMenuItem("Cut"));
        cmChild.getItems().add(generateMenuItem("Copy"));
        cmChild.getItems().add(new SeparatorMenuItem());
        cmChild.getItems().add
            (buildMenu("Assign Icon",
                buildMenuItemsNodeIcon(CONTEXT_MENU_SCOPE_NODE_ONLY, CONTEXT_MENU_DEPTH_LOCAL_BRANCH)));
        cmChild.getItems().add(new SeparatorMenuItem());
        cmChild.getItems().add(generateMenuItem("Delete"));
    }
    
    
    //External Interface
    public void addClipboardMenuItems()
    {
        cmRoot.getItems().add   (1, miClipboardRootAddCopied);
        
        cmParent.getItems().add  (4, miClipboardParentSeparator);
        cmParent.getItems().add  (5, miClipboardParentAddCopied);
        cmParent.getItems().add  (6, miClipboardParentInsertCopied);
        
        cmChild.getItems().add  (4, miClipboardChildSeparator);
        cmChild.getItems().add  (5, miClipboardChildAddCopied);
        cmChild.getItems().add  (6, miClipboardChildInsertCopied);
    }
    public void removeClipboardMenuItems()
    {
        cmRoot.getItems().remove(miClipboardRootAddCopied);
        cmParent.getItems().remove(miClipboardParentAddCopied);
        
        cmChild.getItems().remove(miClipboardChildSeparator);
        cmChild.getItems().remove(miClipboardChildAddCopied);
        cmChild.getItems().remove(miClipboardChildInsertCopied);
    }
    public javafx.scene.control.ContextMenu getContextMenu(Integer iNodeType)
    {
        switch(iNodeType)
        {
            case NODE_TYPE_ROOT:    return cmRoot;
            case NODE_TYPE_PARENT:  return cmParent;
            case NODE_TYPE_CHILD:   return cmChild;
        }
        
        return cmParent;
    }
}