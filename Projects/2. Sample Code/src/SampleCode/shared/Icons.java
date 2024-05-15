/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SampleCode.shared;


import app.App;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author PaulSu
 */
public class Icons implements Constants
{    
    private static ImageView findIconAsImageView(int iIcon)
    {
        return new ImageView(new Image(App.class.getResource(findIconImageViewResource(iIcon)).toString()));
    }
    private static String findIconImageViewResource(int iIcon)
    {
        switch(iIcon)
        {
            case NODE_ICON_ID_BLACK:        return "images/Icon - Node - Black - 8x8.png";
            case NODE_ICON_ID_BLANK:        return "images/Icon - Node - Blank - 8x8.png";
            case NODE_ICON_ID_FOLDER:       return "images/Icon - Node - Folder - Closed - 18x17.png";
            case NODE_ICON_ID_BLUE:         return "images/Icon - Node - Blue - 8x8.png";
            case NODE_ICON_ID_GREEN:        return "images/Icon - Node - Green - 8x8.png";
            case NODE_ICON_ID_GREY:         return "images/Icon - Node - Grey - 8x8.png";
            case NODE_ICON_ID_ORANGE:       return "images/Icon - Node - Orange - 8x8.png";
            case NODE_ICON_ID_PURPLE:       return "images/Icon - Node - Purple - 8x8.png";
            case NODE_ICON_ID_RED:          return "images/Icon - Node - Red - 8x8.png";
            case NODE_ICON_ID_YELLOW:       return "images/Icon - Node - Yellow - 8x8.png";
            case NODE_ICON_ID_CPLUSPLUS:    return "images/Icon - Node - CPlusPlus - 17x17.png";
            case NODE_ICON_ID_JAVA:         return "images/Icon - Node - Java - 15x17.png";
            case NODE_ICON_ID_SQL:          return "images/Icon - Node - SQL - 14x17.png";
            default:                        return "images/Icon - Node - Black - 8x8.png";
        }
    }
    private static String findIconName(int iIcon)
    {
        switch(iIcon)
        {
            case NODE_ICON_ID_BLACK:        return "Black";
            case NODE_ICON_ID_BLANK:        return "Blank";
            case NODE_ICON_ID_FOLDER:       return "Folder";
            case NODE_ICON_ID_BLUE:         return "Blue";
            case NODE_ICON_ID_GREEN:        return "Green";
            case NODE_ICON_ID_GREY:         return "Grey";
            case NODE_ICON_ID_ORANGE:       return "Orange";
            case NODE_ICON_ID_PURPLE:       return "Purple";
            case NODE_ICON_ID_RED:          return "Red";
            case NODE_ICON_ID_YELLOW:       return "Yellow";
            case NODE_ICON_ID_CPLUSPLUS:    return "C++";
            case NODE_ICON_ID_JAVA:         return "Java";
            case NODE_ICON_ID_SQL:          return "SQL";
            default:                        return "Black";
        }
    }
    
    
    //External Interface
    public static final String getIconName(int iIcon)
    {
        return findIconName(iIcon);
    }
    public static final ImageView getIconImageView(int iIcon)
    {
        return findIconAsImageView(iIcon);
    }
}