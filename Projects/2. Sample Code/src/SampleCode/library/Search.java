/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SampleCode.library;

import SampleCode.shared.*;
import java.util.ArrayList;
import javafx.scene.control.TreeItem;
/**
 *
 * @author PaulSu
 */
public class Search implements Constants {

    private final ArrayList<TreeItem<Node>>     tiLibraryNodePath;
    
    private final TreeItem<Node>                tiRoot;
    private final TreeItem<Node>                tiNoResultsFound;

    private int                                 iTotalNodeFound;
    private boolean                             bSearchByName, bSearchByCode;
    private String[]                            arrSearch;
    
    public Search()
    {
        /*
            Class Operation
        
            1. Searches the library and returns matching results.
            2. Builds a TreeItem<Node> structure which is then past to the objLibrary and assigned as the root.
        */
        
        tiRoot                          = new TreeItem<>(new Node(NODE_SEARCH_RESULTS_ROOT));
        tiNoResultsFound                = new TreeItem<>(new Node(NODE_SEARCH_RESULTS_NOT_FOUND));
        tiLibraryNodePath               = new ArrayList<>();
    }

    
    private TreeItem<Node> getChildOnBranch(TreeItem<Node> tiNode, int i)
    {
        return tiNode.getChildren().get(i);
    }
    private int getNumberOfNodesOnBranch(TreeItem<Node> tiNode)
    {
        return tiNode.getChildren().size();
    }
    private String getNodeName(TreeItem<Node> tiNode)
    {
        return tiNode.getValue().getName();
    }
    private void clearSearchResultsTreeView()
    {
        tiRoot.getChildren().clear();
    }
    private void setTreeItemGraphic(TreeItem<Node> tiNode)
    {
        tiNode.setGraphic(Icons.getIconImageView(tiNode.getValue().getIconID()));
    }
    private TreeItem<Node> copyAndGenerateNode(TreeItem<Node>  tiNode)
    {
        TreeItem<Node>  tiNew;
        
        tiNew = new TreeItem<>(tiNode.getValue());
            
        if(tiNode.getValue().getType() == NODE_STANDARD)
            setTreeItemGraphic(tiNew);

        return tiNew;
    }
    private boolean compareNodeID(TreeItem<Node> tiNode1, TreeItem<Node> tiNode2)
    {
        return(tiNode1.getValue().getNodeID() == tiNode2.getValue().getNodeID());    
    }
    private boolean containsChildWithMatchingNodeID(TreeItem<Node> tiParent, TreeItem<Node> tiChild)
    {
        for(int i = 0 ; i < tiParent.getChildren().size() ; i++)
        {
            if(compareNodeID(tiParent.getChildren().get(i), tiChild))
                return true;
        }
        
        return false;
    }
    private TreeItem<Node> findChildWithMatchingNodeID(TreeItem<Node> tiParent, TreeItem<Node> tiChild)
    {
        for(int i = 0 ; i < tiParent.getChildren().size() ; i++)
        {
            if(compareNodeID(tiParent.getChildren().get(i), tiChild))
                return tiParent.getChildren().get(i);
        }
        
        return null;
    }
    private void appendLibraryNodePathToSearchResultsParent(int i, TreeItem<Node> tiParent)
    {
        TreeItem<Node> tiNode;
        
        for(int j = i ; j >= 0 ; j--)
        {
            tiNode = copyAndGenerateNode(tiLibraryNodePath.get(j));
            
            tiParent.getChildren().add(tiNode);
            
            tiParent = tiNode;
        }   
    }
    private void appendLibraryNodePathToSearchResultsTreeview()
    {
        TreeItem<Node>  tiSearchResultsParent, tiLibraryNode;
        boolean         bLibraryNodePathAppended;

        if(tiRoot.getChildren().isEmpty())
        {
            appendLibraryNodePathToSearchResultsParent((tiLibraryNodePath.size()-2), tiRoot);
        }
        else
        {
            tiSearchResultsParent       = tiRoot;
            bLibraryNodePathAppended    = false;
            
            for(int i = (tiLibraryNodePath.size()-2) ; ((i >= 0) && (!bLibraryNodePathAppended)) ; i--)
            {
                tiLibraryNode = tiLibraryNodePath.get(i);
                
                if(containsChildWithMatchingNodeID(tiSearchResultsParent, tiLibraryNode))
                {
                    tiSearchResultsParent = findChildWithMatchingNodeID(tiSearchResultsParent, tiLibraryNode);
                }
                else
                {
                    bLibraryNodePathAppended = true;
                    
                    appendLibraryNodePathToSearchResultsParent(i, tiSearchResultsParent);
                } 
            }
        }
    }
    private void buildLibrayNodePath(TreeItem<Node> tiMatch)
    {
        tiLibraryNodePath.clear();
        tiLibraryNodePath.add(copyAndGenerateNode(tiMatch));     //Found placed into element 0
        
        while(tiMatch.getParent() != null)
        {
            tiMatch = tiMatch.getParent();            
            tiLibraryNodePath.add(copyAndGenerateNode(tiMatch));
        }
    }
    private void addLibraryNodeToSearchResults(TreeItem<Node> tiMatch)
    {
        buildLibrayNodePath(tiMatch);
     
        appendLibraryNodePathToSearchResultsTreeview();
        
        iTotalNodeFound++;
    }
    private boolean searchByName(TreeItem<Node> tiNode)
    {
        if(bSearchByName)
        {
            for(String s : arrSearch)
                if(getNodeName(tiNode).toLowerCase().contains(s))
                    return true;
        }
        
        return false;                    
    }
    private boolean searchByCode(TreeItem<Node> tiNode)
    {
        if(bSearchByCode)
        {
            for(String s : arrSearch)
                if(Database.getContent(tiNode.getValue().getNodeID()).toLowerCase().contains(s))
                    return true;
        }
        
        return false;                    
    }
    private void buildSearchResults(TreeItem<Node> tiParent)
    {
        TreeItem<Node> tiChild;
        int iNumberOfNodesOnBranchInLibrary;

        iNumberOfNodesOnBranchInLibrary = getNumberOfNodesOnBranch(tiParent);

        for(int i = 0 ; i<iNumberOfNodesOnBranchInLibrary ; i++)
        {
            tiChild = getChildOnBranch(tiParent, i);

            if(searchByName(tiChild) || searchByCode(tiChild))
                addLibraryNodeToSearchResults(tiChild);
            
            if(!tiChild.isLeaf())
                buildSearchResults(tiChild); 
        }
        
        /*
            - Starting at the root.
            - Obtain the whole branch attached to root.
            - Starting with first node on branch shift through and check all nodes on branch.
            - If a node is a parent then start same process on that branch.
                - Once reached the end of the branch
                - Return to the previous branch and carry on with the next node after the parent
            - Repeat until end of root branch.
            - The order of execution is below:
        
                root
                    1
                    2
                    3
                    4 - Parent
                    9   5
                    10  6
                    11  7
                    12  8
                    13
                    14 - Parent
                    18  15
                    19  16
                        17
        */
    }
    
    
    //External Interface
    public TreeItem<Node> generateSearchTreeview(TreeItem<Node> tiLibrarySearchOrigin, String[] arrSearch, boolean bSearchByName, boolean bSearchByCode)
    {
        this.arrSearch          = arrSearch;
        this.bSearchByName      = bSearchByName;
        this.bSearchByCode      = bSearchByCode;
        
        iTotalNodeFound = 0;
        
        clearSearchResultsTreeView();

        buildSearchResults(tiLibrarySearchOrigin);
        
        if(iTotalNodeFound == 0)
            tiRoot.getChildren().add(tiNoResultsFound);

        return tiRoot;
    }
    public void clear()
    {
        clearSearchResultsTreeView();
    }
}