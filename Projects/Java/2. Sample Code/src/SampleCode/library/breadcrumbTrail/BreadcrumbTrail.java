/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SampleCode.library.breadcrumbTrail;

import SampleCode.shared.*;
import SampleCode.library.Node;

import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.ToolBar;
import javafx.scene.control.TreeItem;

/**
 *
 * @author PaulSu
 */
public class BreadcrumbTrail implements Constants{

    private Breadcrumb[] Breadcrumbs;
    private ToolBar tbToolbar;
    
    public BreadcrumbTrail(ToolBar tbToolBar)
    {
        this.tbToolbar = tbToolBar;
    }

    
    private void clearToolBar()
    {
        tbToolbar.getItems().clear(); 
    }
    private Integer findPathLength(TreeItem<Node> tiNode)
    {
        int iLength;
        
        iLength = 0;
        
        while(tiNode.getParent() != null)
        {
            tiNode = tiNode.getParent();           
            iLength++;
        }

        return iLength;
    }
    private void setNumberOfBreadcrumbs(Integer iNumberOfBreadcrumbs)
    {
        clearToolBar();
        
        for(Integer i = 0 ; i<iNumberOfBreadcrumbs ; i++)
            tbToolbar.getItems().add(new Label(""));
        
        Breadcrumbs = new Breadcrumb[iNumberOfBreadcrumbs];
    }
    private void setBreadcrumb(TreeItem<Node> tiNode, Integer iPosition)
    {
        Breadcrumbs[iPosition] = new Breadcrumb(tiNode, tiNode.getValue().getName());
        
        tbToolbar.getItems().set(iPosition, Breadcrumbs[iPosition]);
    }
    private void setSeparator(Integer iPosition)
    {
        Breadcrumbs[iPosition] = new Breadcrumb();
        
        tbToolbar.getItems().set(iPosition, new Separator());
    }
    
    
    //External Interface
    public Breadcrumb getSelectedBreadcrumb()
    {
        ObservableList<javafx.scene.Node>   olBreadcrumbTrail;
        Integer                             iBreadcrumbTrailSize;

        olBreadcrumbTrail       = tbToolbar.getItems();
        iBreadcrumbTrailSize    = olBreadcrumbTrail.size();

        for(Integer i = 0 ; i < iBreadcrumbTrailSize ; i++)
            if(olBreadcrumbTrail.get(i).isHover())
                return Breadcrumbs[i];
        
        return Breadcrumbs[iBreadcrumbTrailSize-1];
    }
    public void buildBreadcrumbTrail(TreeItem<Node> tiBuild)
    {
        Integer iNumberOfIndentationsOfSelected,
                iNumberOfBreadcrumbs;

        iNumberOfIndentationsOfSelected = findPathLength(tiBuild);
        iNumberOfBreadcrumbs            = (iNumberOfIndentationsOfSelected * 2) - 1;

        setNumberOfBreadcrumbs(iNumberOfBreadcrumbs);
        
        for(Integer i = (iNumberOfBreadcrumbs-1) ; i >= 0 ; i = i-2)
        {
            setBreadcrumb(tiBuild, i);
            
            if(i-1>0)
                setSeparator(i-1);
            
            tiBuild = tiBuild.getParent();
        }
    }
    public void clear()
    {
        clearToolBar(); 
    }
}