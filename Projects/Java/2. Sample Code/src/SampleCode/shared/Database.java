/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SampleCode.shared;


import SampleCode.library.Node;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.scene.control.TreeItem;

/**
 *
 * @author PaulSu
 */
public class Database implements Constants
{
    private static Connection       objConnection;
    private static Statement        objStatement;
    
    private static int              iNumberOfLoadedNodes;
    
    static
    {
        iNumberOfLoadedNodes = 0;
    }
    
    private static int extractIntegerValueFromResultSet(ResultSet objResultSet, int iField)
    {
        try
        {
            switch(iField)
            {
                case NODE_ID:      return objResultSet.getInt(1);
                case ICON_ID:      return objResultSet.getInt(5);
            }
            
            return 0;
        }
        catch(SQLException ex)
        {
            System.out.println(ex.toString());
        }
        
        return 0;
    }
    private static String extractStringValueFromResultSet(ResultSet objResultSet, int iField)
    {
        try
        {
            switch(iField)
            {
                case NAME:         return objResultSet.getString(4);
                case CONTENT:      return objResultSet.getString(1);
            }
            
            return "Error - No Returned Data";
        }
        catch(SQLException ex)
        {
            System.out.println(ex.toString());
        }
        
        return "Error - No Returned Data";
    }
    private static TreeItem<Node> extractTreeItemFromResultSet(ResultSet objResultSet)
    {
        TreeItem<Node> tiAdd;

        tiAdd = new TreeItem<>(new Node(
                    extractIntegerValueFromResultSet(objResultSet, NODE_ID),
                    extractStringValueFromResultSet(objResultSet, NAME),
                    extractIntegerValueFromResultSet(objResultSet, ICON_ID)));
        
        return tiAdd;
    }
    private static ResultSet getResultSet(int iQuery, int iValue)
    {
        try
        {
            switch(iQuery)
            {
                case DB_GET_NODES_ON_BRANCH:    return objStatement.executeQuery("SELECT * FROM NBUSER.LIBRARY WHERE PARENT_ID = " + iValue + " ORDER BY ORDER_ID");
                case DB_GET_CONTENT:            return objStatement.executeQuery("SELECT CONTENT FROM NBUSER.LIBRARY WHERE NODE_ID = " + iValue);
            }
        }
        catch(SQLException ex)
        {
            System.out.println(ex.toString());
        }
        
        return null;
    }
    private static void setTreeItemGraphic(TreeItem<Node> tiNode)
    {
        tiNode.setGraphic(Icons.getIconImageView(tiNode.getValue().getIconID()));
    }
    private static void appendTreeItemToBranch(TreeItem<Node> tiParent, TreeItem<Node> tiChild)
    {
        tiParent.getChildren().add(tiChild);
        
        setTreeItemGraphic(tiChild);
    }
    private static String formatSQLInsert(String sValue)
    {
        if(sValue.contains("\'"))  return sValue.replace("\'", "\'\'");
        else                       return sValue;
    }
    
    
    //External Interface    
    public static void initDatabaseConnection()
    {
        try
        {
            objConnection = DriverManager.getConnection("jdbc:derby:db\\dbLibrary;user=nbuser;password=nbuser");
            
            objStatement = objConnection.createStatement();            
        }
        catch(SQLException ex)
        {
            System.out.println(ex.toString());
        }
    }
    public static boolean getAndAppendBranchToTreeItem(TreeItem<Node> tiParent, int iParentID)
    {
        ResultSet objResultSet;
        
        try
        {
            objResultSet = getResultSet(DB_GET_NODES_ON_BRANCH, iParentID);
        
            while(objResultSet.next())
            {
                appendTreeItemToBranch(tiParent, extractTreeItemFromResultSet(objResultSet)); 
                
                iNumberOfLoadedNodes++;
            }
        }
        catch(SQLException ex)
        {
            System.out.println(ex.toString());
            
            return false;
        }
        
        return true;
    }
    public static int getNextNodeID()
    {
        ResultSet objResultSet;
        
        try
        {
            objResultSet = objStatement.executeQuery("SELECT NODE_ID FROM NBUSER.LIBRARY ORDER BY NODE_ID DESC FETCH FIRST 1 ROWS ONLY");
        
            if(objResultSet.next())     return extractIntegerValueFromResultSet(objResultSet, NODE_ID) + 1;
            else                        return 1;
        }
        catch(SQLException ex)
        {
            System.out.println(ex.toString());
        }
        
        return 0;
    }   
    public static String getContent(int iNodeID)
    {
        ResultSet objResultSet;
        
        try
        {
            objResultSet = getResultSet(DB_GET_CONTENT, iNodeID);
        
            objResultSet.next();
            
            return extractStringValueFromResultSet(objResultSet, CONTENT);    
        }
        catch(SQLException ex)
        {
            System.out.println(ex.toString());
            
            return "";
        }
    }
    public static boolean setNodeParameter(int iNodeID, int iParameter, int iValue)
    {
        try
        {
            switch(iParameter)
            {
                case ORDER_ID:  objStatement.executeUpdate("UPDATE NBUSER.LIBRARY SET ORDER_ID = " + iValue + " WHERE NODE_ID = " + iNodeID);      break;
                case ICON_ID:   objStatement.executeUpdate("UPDATE NBUSER.LIBRARY SET ICON_ID = " + iValue + " WHERE NODE_ID = " + iNodeID);       break;          
            }
        }
        catch(SQLException ex)
        {
            System.out.println(ex.toString());
            
            return false;
        }
        
        return true;
    }
    public static boolean setNodeParameter(int iNodeID, int iParameter, String sValue)
    {
        try
        {
            switch(iParameter)
            {
                case NAME:      if(sValue.length() > DB_SIZE_OF_NAME)       sValue = sValue.substring(0, DB_SIZE_OF_NAME-1);        break;
                case CONTENT:   if(sValue.length() > DB_SIZE_OF_CONTENT)    sValue = sValue.substring(0, DB_SIZE_OF_CONTENT-1);     break;
            }
            
            switch(iParameter)
            {
                case NAME:      objStatement.executeUpdate("UPDATE NBUSER.LIBRARY SET NAME = '" + formatSQLInsert(sValue) + "' WHERE NODE_ID = " + iNodeID);        break;
                case CONTENT:   objStatement.executeUpdate("UPDATE NBUSER.LIBRARY SET CONTENT = '" + formatSQLInsert(sValue) + "' WHERE NODE_ID = " + iNodeID);     break;
            }
        }
        catch(SQLException ex)
        {
            System.out.println(ex.toString());
            
            return false;
        }
        
        return true;
    } 
    public static boolean swapNodeOrderID(int iSelectedNodeID, int iSelectedOrderID, int iSiblingNodeID, int iSiblingOrderID)
    {
        try
        {
            objStatement.executeUpdate("UPDATE NBUSER.LIBRARY SET ORDER_ID = NULL WHERE NODE_ID = " + iSiblingNodeID);
            objStatement.executeUpdate("UPDATE NBUSER.LIBRARY SET ORDER_ID = " + iSiblingOrderID + " WHERE NODE_ID = " + iSelectedNodeID);
            objStatement.executeUpdate("UPDATE NBUSER.LIBRARY SET ORDER_ID = " + iSelectedOrderID + " WHERE ORDER_ID IS NULL"); 
        }
        catch(SQLException ex)
        {
            System.out.println(ex.toString());
            
            return false;
        }
        
        return true;
    }
    public static boolean addNode(Node nNode, int iParentID, int iOrderID)
    {
        try
        {   
            objStatement.executeUpdate("INSERT INTO NBUSER.LIBRARY (NODE_ID, PARENT_ID, ORDER_ID, NAME, ICON_ID, CONTENT) VALUES (" + nNode.getNodeID() + ", " + iParentID + ", " + iOrderID + ", '" + formatSQLInsert(nNode.getName()) + "', " + nNode.getIconID() + ",'...')");            
        }            
        catch(SQLException ex)
        {
            System.out.println(ex.toString());
            
            return false;
        }
        
        return true;
    }
    public static boolean addNode(Node nNode, int iParentID, int iOrderID, String sContent)
    {
        try
        {   
            objStatement.executeUpdate("INSERT INTO NBUSER.LIBRARY (NODE_ID, PARENT_ID, ORDER_ID, NAME, ICON_ID, CONTENT) VALUES (" + nNode.getNodeID() + ", " + iParentID + ", " + iOrderID + ", '" + formatSQLInsert(nNode.getName()) + "', " + nNode.getIconID() + ",'" + formatSQLInsert(sContent) + "')");            
        }            
        catch(SQLException ex)
        {
            System.out.println(ex.toString());
            
            return false;
        }
        
        return true;
    }
    public static boolean removeNode(int iNodeID)
    {
        try
        {
            objStatement.executeUpdate("DELETE FROM NBUSER.LIBRARY WHERE NODE_ID = " + iNodeID);
        }
        catch(SQLException ex)
        {
            System.out.println(ex.toString());
            
            return false;
        }
        
        return true;
    }
    public static boolean decrementBranchNodeOrderID(int iParentID, int iOrderID)
    {
        try
        {
            objStatement.executeUpdate("UPDATE NBUSER.LIBRARY SET ORDER_ID = ORDER_ID - 1 WHERE ORDER_ID > " + iOrderID + " AND PARENT_ID = " + iParentID); 
        }
        catch(SQLException ex)
        {
            System.out.println(ex.toString());
            
            return false;
        }
        
        return true;
    }
    public static boolean incrementBranchNodeOrderID(int iParentID, int iOrderID)
    {
        try
        {
            objStatement.executeUpdate("UPDATE NBUSER.LIBRARY SET ORDER_ID = ORDER_ID + 1 WHERE ORDER_ID >= " + iOrderID + " AND PARENT_ID = " + iParentID); 
        }
        catch(SQLException ex)
        {
            System.out.println(ex.toString());
            
            return false;
        }
        
        return true;
    }
    public static int getNumberOfRowsInDatabase()
    {
        ResultSet objResultSet;
        
        try
        {
            objResultSet = objStatement.executeQuery("SELECT COUNT(*) AS NUMBER_OF_ROWS FROM NBUSER.LIBRARY");
        
            if(objResultSet.next())     return objResultSet.getInt(1);
            else                        return 0;
        }
        catch(SQLException ex)
        {
            System.out.println(ex.toString());
        }
        
        return 0;
    }
    public static int getNumberOfLoadedNodes()
    {
        return iNumberOfLoadedNodes;
    }
}