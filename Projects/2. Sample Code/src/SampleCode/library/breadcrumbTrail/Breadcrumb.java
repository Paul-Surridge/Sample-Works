/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SampleCode.library.breadcrumbTrail;

import SampleCode.shared.*;
import SampleCode.library.Node;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

/**
 *
 * @author PaulSu
 */
public class Breadcrumb extends Label implements Constants{

    private final TreeItem<Node> tiNode;
    
    public Breadcrumb(TreeItem<Node> tiNode, String sTitle)
    {
        super(sTitle);
        
        this.tiNode = tiNode;

        initEventHandler();
    }
    public Breadcrumb()
    {
        this.tiNode = null;

        initEventHandler();
    }
 
    
    private void initEventHandler()
    {
        final EventHandler<MouseEvent>  ehBreadCrumbEntered = (MouseEvent me) -> {
            setBreadcrumbTextColour(BREADCRUMB_TEXT_BLUE);
        };
        
        final EventHandler<MouseEvent>  ehBreadCrumbExited = (MouseEvent me) -> {
            setBreadcrumbTextColour(BREADCRUMB_TEXT_BLACK);
        };
        
        addEventHandler(MouseEvent.MOUSE_ENTERED, ehBreadCrumbEntered);
        addEventHandler(MouseEvent.MOUSE_EXITED, ehBreadCrumbExited);
    }
    private void setBreadcrumbTextColour(Integer iColour)
    {
        switch(iColour)
        {
            case BREADCRUMB_TEXT_BLUE:  super.setTextFill(Color.web("#0076a3"));    break;
            case BREADCRUMB_TEXT_BLACK: super.setTextFill(Color.web("#000000"));    break;
        } 
    }
    
    
    //External Interface
    public TreeItem<Node> getTreeItem()
    {
        return tiNode;
    }
    public int getNodeID()
    {
        return tiNode.getValue().getNodeID();
    }
    public String getName()
    {
        return tiNode.getValue().getName();
    }
}