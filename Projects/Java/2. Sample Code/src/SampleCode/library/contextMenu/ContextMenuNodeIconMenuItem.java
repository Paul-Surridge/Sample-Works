/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SampleCode.library.contextMenu;

import SampleCode.shared.*;

import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;

/**
 *
 * @author PaulSu
 */
class ContextMenuNodeIconMenuItem extends MenuItem implements Constants
{
    private final ContextMenuNodeIcon niNodeIcon;
    private final int iScope, iDepth;
    
    ContextMenuNodeIconMenuItem(ContextMenuNodeIcon niNodeIcon, int iScope, int iDepth)
    {
        super(niNodeIcon.getName());
        
        setGraphic(niNodeIcon.getIconAsImageView());
        
        this.niNodeIcon     = niNodeIcon;
        this.iScope         = iScope;
        this.iDepth         = iDepth;
    }
    
    //External Interface
    final int getIconID()
    {
        return niNodeIcon.getIconID();
    }
    final ImageView getImageView()
    {
        return niNodeIcon.getIconAsImageView();
    }
    final int getScope()
    {
        return iScope;
    }
    final int getDepth()
    {
        return iDepth;
    }
    
    @Override
    public String toString() {
        return niNodeIcon.getName();
    }
}