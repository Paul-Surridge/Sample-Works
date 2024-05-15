/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SampleCode.library;

import SampleCode.shared.Constants;
import SampleCode.shared.Icons;
import SampleCode.shared.Database;
import javafx.scene.image.ImageView;
/**
 *
 * @author PaulSu
 */
public class Node implements Constants {

    private int             iNodeID;        
    private String          sName;
    private int             iIconID;
    private ImageView       ivIcon;    
    private int             iType;

    public Node(int iType)
    {
        this.iNodeID        = 0;
        this.iIconID        = 0;
        this.ivIcon         = null;
        this.iType          = iType;
        
        switch(iType)
        {
            case NODE_LIBRARY_ROOT:             this.sName = "Library";                 break;
            case NODE_SEARCH_RESULTS_ROOT:      this.sName = "Search Results";          break;
            case NODE_SEARCH_RESULTS_NOT_FOUND: this.sName = "No Search Results Found"; break;    
        }
    }
    public Node(int iNodeID, String sName, int iIconID)
    {
        this.iNodeID        = iNodeID;
        this.sName          = sName;
        this.iIconID        = iIconID;
        this.ivIcon         = Icons.getIconImageView(this.iIconID);
        
        this.iType          = NODE_STANDARD;
    }
    public Node(Node objNode)
    {
        this.iNodeID        = objNode.getNodeID();
        this.sName          = objNode.getName();
        this.iIconID        = objNode.getIconID();
        this.ivIcon         = Icons.getIconImageView(this.iIconID);
        
        this.iType          = NODE_STANDARD;
    }

    
    //External Interface
    public int getNodeID()
    {
        return iNodeID;
    }
    public String getName()
    {
        return sName;
    }
    public int getIconID()
    {
        return iIconID;
    }
    public ImageView getIconResource()
    {
        return ivIcon;
    }
    public int getType()
    {
        return iType;
    }
    public final void setNodeID(int iNodeID)
    {
        this.iNodeID = iNodeID;
    }
    public final void setName(String sName)
    {
        if(Database.setNodeParameter(this.iNodeID, NAME, sName))
            this.sName = sName;
    }
    public final void setIconID(int iIconID)
    {
        if(Database.setNodeParameter(this.iNodeID, ICON_ID, iIconID))
        {
            this.iIconID = iIconID;           
            this.ivIcon = Icons.getIconImageView(this.iIconID);
        }
    }
    public final void setContent(String sContent)
    {
        Database.setNodeParameter(this.iNodeID, CONTENT, sContent);
    }
    
    
    @Override
    public String toString() {
        return this.sName;
    }
}