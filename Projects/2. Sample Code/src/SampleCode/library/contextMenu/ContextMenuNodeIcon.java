/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SampleCode.library.contextMenu;

import SampleCode.shared.*;

import javafx.scene.image.ImageView;

/**
 *
 * @author PaulSu
 */
class ContextMenuNodeIcon implements Constants {

    private String              sName;
    private ImageView           ivIcon;
    
    private int                 iIconID;
    
    ContextMenuNodeIcon(int iIconID)
    {
        this.iIconID    = iIconID;
        this.sName      = Icons.getIconName(iIconID);
        this.ivIcon     = Icons.getIconImageView(iIconID);
    }
    
    
    //External Interface
    final String getName()
    {
        return sName;
    }
    final int getIconID()
    {
        return iIconID;
    }
    final ImageView getIconAsImageView()
    {
        return ivIcon;
    }
    final void setIconID(int iIconID)
    {
        this.iIconID = iIconID;
        
        this.sName  = Icons.getIconName(iIconID);
        this.ivIcon = Icons.getIconImageView(iIconID);
    }
    

    @Override
    public String toString() {
        return this.sName;
    }
}