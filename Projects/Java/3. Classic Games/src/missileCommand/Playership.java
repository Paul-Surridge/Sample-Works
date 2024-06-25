package missileCommand;

import app.App; 
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

class Playership implements Constants{
    
    private AnchorPane              apMissileCommand;
    private ImageView               ivPlayershipSprite;
    private PlayerMissile           PlayerMissiles[] = new PlayerMissile[NUMBER_OF_PLAYER_MISSILES + 1];
    private AlienMissilesManager    objAlienMissilesManager;

    private Image                   imgPlayershipDestroyedExplosion   = new Image(App.class.getResource("images/missileCommand/Explosion.png").toString());
    private Image                   imgPlayerShip                     = new Image(App.class.getResource("images/missileCommand/Player Ship.png").toString());    
    private Label                   labPlayerShipMissileAmount, labShootingArea;
    
    private Timeline                tlPlayershipDestroyedExplosionFade = new Timeline();

    private boolean                 bPlayershipAliveState;
    private int                     iShipNumber, iAmmunition;

    Playership(AnchorPane apMissileCommand, int iShipNumber, ImageView ivPlayerShipSprite, Label labPlayerShipMissileAmount, Label labShootingArea, AlienMissilesManager objAlienMissilesManager)
    {
        this.apMissileCommand                       = apMissileCommand;
        this.ivPlayershipSprite                     = ivPlayerShipSprite; 
        this.labPlayerShipMissileAmount             = labPlayerShipMissileAmount;
        this.labShootingArea                        = labShootingArea;
        this.iShipNumber                            = iShipNumber;
        this.objAlienMissilesManager                = objAlienMissilesManager;

        initTimelines();
        
        initPlayership();
        initPlayerMissiles();
        
        eventHandlerMouseEvents();
    }
    
    
    private void initPlayerMissiles()
    {
        //Create x20 circles which would be used as missiles i.e. only max x20 missiles can be fired at any one time per ship...
        for(int i = 1 ; i <= NUMBER_OF_PLAYER_MISSILES ; i++)
            PlayerMissiles[i] = new PlayerMissile(apMissileCommand, iShipNumber, objAlienMissilesManager);
    }
    private void initTimelines()
    {
        //Define timeliine when playership is destroyed
        tlPlayershipDestroyedExplosionFade.getKeyFrames().add(new KeyFrame(Duration.millis(0),          new KeyValue(ivPlayershipSprite.opacityProperty(), 1)));
        tlPlayershipDestroyedExplosionFade.getKeyFrames().add(new KeyFrame(Duration.millis(500),        new KeyValue(ivPlayershipSprite.opacityProperty(), 0)));
        
        //Define the OnFinished aspect of the timeline
        tlPlayershipDestroyedExplosionFade.setOnFinished((ActionEvent ae) -> {
            bPlayershipAliveState = false;
        });
    }
    private void initPlayership()
    {
        bPlayershipAliveState = true;
        ivPlayershipSprite.setImage(imgPlayerShip);
    }   
    private void updateAmmunitionUserDisplay()
    {
        labPlayerShipMissileAmount.setText(Integer.toString(iAmmunition));
    }
    private void reduceAmmunition(int iAmount)
    {
        iAmmunition -= iAmount;
        
        updateAmmunitionUserDisplay();
    }
    
    
    boolean getAliveState()
    {
        return bPlayershipAliveState;
    }
    int getAmmunition()
    {
        return iAmmunition;
    }
    void setAmmunition(int iAmmunition)
    {
        this.iAmmunition = iAmmunition;
        
        updateAmmunitionUserDisplay();
    }
    void setAliveState(boolean state)
    {
        bPlayershipAliveState = state;
        
        ivPlayershipSprite.setImage(imgPlayerShip);
        ivPlayershipSprite.setOpacity(1);
    }
    void hitPlayership()
    {
        bPlayershipAliveState = false;

        ivPlayershipSprite.setImage(imgPlayershipDestroyedExplosion);
        tlPlayershipDestroyedExplosionFade.playFromStart();
    }
    void fireMissile(double dTargetXCoordinate, double dTargetYCoordinate)
    {
        //Find the next available missile and fire...
        for(int i = 1 ; i <= NUMBER_OF_PLAYER_MISSILES ; i++)
        {
            if(!PlayerMissiles[i].getFiringState())
            {
                PlayerMissiles[i].fire(dTargetXCoordinate, dTargetYCoordinate);
                reduceAmmunition(1);
                
                break;
            }
        }
    }
    
    
    private void eventHandlerMouseEvents()
    {
        // - - - - Handlers - - - -
        final EventHandler<MouseEvent>  ehMouseMove = (MouseEvent me) -> {
            switch(iShipNumber)
            {
                case(PLAYER_SHIP_LEFT):     ivPlayershipSprite.rotateProperty().setValue(Math.toDegrees(Math.atan(((me.getX()-68)/(SHOOTING_AREA_HEIGHT-me.getY())))));                             break;
                case(PLAYER_SHIP_RIGHT):    ivPlayershipSprite.rotateProperty().setValue( (360-Math.toDegrees(Math.atan((SHOOTING_AREA_WIDTH-me.getX()-68)/(SHOOTING_AREA_HEIGHT-me.getY())))));    break;
            }
        };
        
        final EventHandler<MouseEvent>  ehMouseEntered = (MouseEvent me) -> {
            labShootingArea.setCursor(Cursor.CROSSHAIR);
        };
        final EventHandler<MouseEvent>  ehMouseExited = (MouseEvent me) -> {
            labShootingArea.setCursor(Cursor.DEFAULT);
        };
        
        labShootingArea.addEventHandler(MouseEvent.MOUSE_MOVED, ehMouseMove);
        labShootingArea.addEventHandler(MouseEvent.MOUSE_ENTERED, ehMouseEntered);
        labShootingArea.addEventHandler(MouseEvent.MOUSE_EXITED, ehMouseExited);
    }
}