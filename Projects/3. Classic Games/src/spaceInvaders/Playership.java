package spaceInvaders;

import app.App; 
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

class Playership implements Constants{
    
    TranslateTransition     ttPlayershipMove;
    
    private Timeline tlPlayershipDestroyedExplosionFade     = new Timeline();
    private Timeline tlPlayershipReveal                     = new Timeline();
    private Timeline tlPlayershipMissileFire                = new Timeline();
    
    private Image imgPlayershipDestroyedExplosion   = new Image(App.class.getResource("images/spaceInvaders/Explosion.png").toString());
    private Image imgPlayerShip                     = new Image(App.class.getResource("images/spaceInvaders/Player Ship.png").toString());
    
    private Image imgMissilesUnactive               = new Image(App.class.getResource("images/spaceInvaders/Bullet.png").toString());
    private Image imgMissilesActive                 = new Image(App.class.getResource("images/spaceInvaders/Bullet - Active.png").toString());
    
    private Image imgHPMissilesUnactive             = new Image(App.class.getResource("images/spaceInvaders/Collect - High Powered Missile.png").toString());
    private Image imgHPMissilesActive               = new Image(App.class.getResource("images/spaceInvaders/Collect - High Powered Missile - Active.png").toString());
    
    private Image imgNukesUnactive                  = new Image(App.class.getResource("images/spaceInvaders/Collect - Nuke.png").toString());
    private Image imgNukesActive                    = new Image(App.class.getResource("images/spaceInvaders/Collect - Nuke - Active.png").toString());
    
    private Label labNukeAmount;
    private Label labHPMissilesAmount;
    private Label labPlayershipLives;
       
    private boolean     bPlayershipAliveState;
    public ImageView    ivPlayershipSprite;
    
    private ImageView   ivMissilesSelectState;
    private ImageView   ivHPMissilesSelectState;
    private ImageView   ivNukesSelectState;
            
    private int         iNumberOfLives;
    
    private int         iNukesAmount;
    private int         iHPMissilesAmount;
    private int         iActiveWeapon;
    
    private double      dPlayerMissileXPosition;
    private Boolean     bRowChecked[] = new Boolean[NUMBER_OF_ALIEN_ROWS + 1];
    
    Circle cirPlayershipMissileSprite;
    
    Playership(AnchorPane apSpaceInvaders, ImageView im, Label labNukeAmount, Label labHPMissilesAmount, ImageView ivMissilesSelect, ImageView ivHPMissilesSelect, ImageView ivNukesSelect, Label labPlayershipLives)
    {
        ivPlayershipSprite = im;
        ivPlayershipSprite.setOpacity(0);
        ivPlayershipSprite.setLayoutX(440);
        ivPlayershipSprite.setLayoutY(550);
        ivPlayershipSprite.setTranslateX(0);
        ivPlayershipSprite.setTranslateY(0);
 
        this.labNukeAmount              = labNukeAmount;
        this.labHPMissilesAmount        = labHPMissilesAmount;
        this.labPlayershipLives         = labPlayershipLives;
        
        ivMissilesSelectState           = ivMissilesSelect;
        ivMissilesSelectState.setImage(imgMissilesActive);
        
        ivHPMissilesSelectState         = ivHPMissilesSelect;
        ivHPMissilesSelectState.setImage(imgHPMissilesUnactive);
        
        ivNukesSelectState              = ivNukesSelect;      
        ivNukesSelectState.setImage(imgNukesUnactive);
        
        cirPlayershipMissileSprite = new Circle(407, 507, 5, Paint.valueOf("#ffffff"));
        cirPlayershipMissileSprite.setVisible(false);
        
        apSpaceInvaders.getChildren().add(cirPlayershipMissileSprite);
        
        initTimelines();
        
        initPlayership();
    }
    
    
    
    private void initTimelines()
    {
        tlPlayershipMissileFire.getKeyFrames().add(new KeyFrame(Duration.millis(0),     new KeyValue(cirPlayershipMissileSprite.centerYProperty(),   564)));
        tlPlayershipMissileFire.getKeyFrames().add(new KeyFrame(Duration.millis(200),    new KeyValue(cirPlayershipMissileSprite.centerYProperty(),   50)));
        
        tlPlayershipMissileFire.setCycleCount(1);
    
        tlPlayershipMissileFire.setOnFinished((ActionEvent ae) -> {
            cirPlayershipMissileSprite.setVisible(false);
            
            tlPlayershipMissileFire.jumpTo(Duration.ZERO);
        });
        
        
        
        
        tlPlayershipDestroyedExplosionFade.getKeyFrames().add(new KeyFrame(Duration.millis(0),      new KeyValue(ivPlayershipSprite.opacityProperty(),   1)));
        tlPlayershipDestroyedExplosionFade.getKeyFrames().add(new KeyFrame(Duration.millis(500),    new KeyValue(ivPlayershipSprite.opacityProperty(),   0)));
        
        tlPlayershipDestroyedExplosionFade.setOnFinished((ActionEvent ae) -> {
            if(iNumberOfLives > 0)  show();
            else                    bPlayershipAliveState = false;
        });
        
        
        
        
        tlPlayershipReveal.getKeyFrames().add(new KeyFrame(Duration.millis(0),      new KeyValue(ivPlayershipSprite.opacityProperty(),   0)));
        tlPlayershipReveal.getKeyFrames().add(new KeyFrame(Duration.millis(500),    new KeyValue(ivPlayershipSprite.opacityProperty(),   1)));
        
        ttPlayershipMove = new TranslateTransition(Duration.millis(100), ivPlayershipSprite);
        ttPlayershipMove.setCycleCount(1); 
    }
    private int getPlayershipXCoordinateRightSide()
    {
        return (int)(ivPlayershipSprite.getLayoutX() + ivPlayershipSprite.getTranslateX() + (ivPlayershipSprite.getFitWidth()));
    }
    private int getPlayershipXCoordinateLeftSide()
    {
        return (int)(ivPlayershipSprite.getLayoutX() + ivPlayershipSprite.getTranslateX());
    }
    
    
    
    void initPlayership()
    {
        iNukesAmount            = 0;
        labNukeAmount.setText(Integer.toString(iNukesAmount));
        
        iHPMissilesAmount       = 0;
        labHPMissilesAmount.setText(Integer.toString(iHPMissilesAmount));
        
        iNumberOfLives          = 3;
        refreshNumberOfLives();
    }
    void missileStopAndReset()
    {
        tlPlayershipMissileFire.stop();
        cirPlayershipMissileSprite.setVisible(false);
        tlPlayershipMissileFire.jumpTo(Duration.ZERO);
    }
    boolean getAliveState()
    {
        return bPlayershipAliveState;
    }
    int getNumberOfLives()
    {
        return iNumberOfLives;
    }
    int getActiveWeapon()
    {
        return iActiveWeapon;
    }
    void refreshActiveWeaponState()
    {  
        switch(iActiveWeapon)
        {
            case(NUKES):
            {
                ivMissilesSelectState.setImage(imgMissilesUnactive);
                ivHPMissilesSelectState.setImage(imgHPMissilesUnactive);
                ivNukesSelectState.setImage(imgNukesActive);
                
                tlPlayershipMissileFire.setRate((double)0.1);
        
                break;
            }
            case(HPMISSILES):
            {
                ivMissilesSelectState.setImage(imgMissilesUnactive);
                ivHPMissilesSelectState.setImage(imgHPMissilesActive);
                ivNukesSelectState.setImage(imgNukesUnactive);
                
                tlPlayershipMissileFire.setRate((double)1);
                
                break;
            }
            case(MISSILES):
            {
                ivMissilesSelectState.setImage(imgMissilesActive);
                ivHPMissilesSelectState.setImage(imgHPMissilesUnactive);
                ivNukesSelectState.setImage(imgNukesUnactive);
                
                tlPlayershipMissileFire.setRate((double)0.5);
                
                break;
            }
        }
    }
    void setActiveWeapon(int iWeaponType)
    {
        switch(iWeaponType)
        {
            case(NUKES):
            {
                if(iNukesAmount > 0)        iActiveWeapon = NUKES;
                else                        iActiveWeapon = MISSILES;
                
                break;
            }
            case(HPMISSILES):
            {
                if(iHPMissilesAmount > 0)   iActiveWeapon = HPMISSILES;
                else                        iActiveWeapon = MISSILES;
                
                break;
            }
            case(MISSILES):
            {
                iActiveWeapon = MISSILES;
                
                break;
            }
        }
        
        refreshActiveWeaponState();
    }
    void playershipMoveRight()
    {
        if( (PLAYING_AREA_RIGHT_X_COORDINATE - getPlayershipXCoordinateRightSide()) > 100)
        {
            ttPlayershipMove.stop();
            ttPlayershipMove.setByX(100);
            ttPlayershipMove.play();
        }
        else
        {
            ttPlayershipMove.setByX(PLAYING_AREA_RIGHT_X_COORDINATE - getPlayershipXCoordinateRightSide());
            ttPlayershipMove.play();
        }
    }
    void playershipMoveLeft()
    {
        if( (getPlayershipXCoordinateLeftSide() - PLAYING_AREA_LEFT_X_COORDINATE) > 100)
        {
            ttPlayershipMove.stop();
            ttPlayershipMove.setByX(-100);
            ttPlayershipMove.play();
        }
        else
        {
            ttPlayershipMove.setByX(-(getPlayershipXCoordinateLeftSide() - PLAYING_AREA_LEFT_X_COORDINATE));
            ttPlayershipMove.play();            
        }
    }
    void playershipMoveStopLeft()
    {
        ttPlayershipMove.stop();
    }
    void playershipMoveStopRight()
    {
        ttPlayershipMove.stop();
    }
    void resetRowChecked()
    {
        for(int i = 0 ; i < (NUMBER_OF_ALIEN_ROWS + 1) ; i++)
            bRowChecked[i] = false;
    }
    boolean getRowChecked(int iRow)
    {
        return bRowChecked[iRow];
    }
    void setRowChecked(int iRow)
    {
        bRowChecked[iRow] = true;
    }
    void fireCurrentWeapon()
    {
        if(tlPlayershipMissileFire.getStatus() == Animation.Status.STOPPED)
        {
            resetRowChecked();
            
            if((iActiveWeapon == NUKES)         && (iNukesAmount == 0))             setActiveWeapon(MISSILES);
            if((iActiveWeapon == HPMISSILES)    && (iHPMissilesAmount == 0))        setActiveWeapon(MISSILES);
            
            dPlayerMissileXPosition = (ivPlayershipSprite.getLayoutX() + ivPlayershipSprite.getTranslateX() + 38);

            cirPlayershipMissileSprite.setCenterX(dPlayerMissileXPosition);
            cirPlayershipMissileSprite.setVisible(true);

            tlPlayershipMissileFire.play();

            switch(getActiveWeapon())
            {
                case(HPMISSILES):         reduceNumberOfHPMissiles(1);      break;
                case(NUKES):              reduceNumberOfNukes(1);           break;       
            }
        }
    }
    void reduceNumberOfHPMissiles(int iAmount)
    {
        iHPMissilesAmount = iHPMissilesAmount - iAmount;
        
        if(iHPMissilesAmount < 0)
            iHPMissilesAmount = 0;
        
        labHPMissilesAmount.setText(Integer.toString(iHPMissilesAmount));
    }
    void reduceNumberOfNukes(int iAmount)
    {
        iNukesAmount = iNukesAmount - iAmount;
        
        if(iNukesAmount < 0)
            iNukesAmount = 0;
        
        labNukeAmount.setText(Integer.toString(iNukesAmount));  
    }
    double getPlayershipMissileXPosition()
    {        
        return dPlayerMissileXPosition;
    }
    double getPlayershipOriginXPosition()
    {        
        return (ivPlayershipSprite.getLayoutX() + ivPlayershipSprite.getTranslateX());
    }
    double getPlayershipMissileYPosition()
    {
        return (cirPlayershipMissileSprite.getCenterY() + cirPlayershipMissileSprite.getTranslateY());
    }
    void setAliveState(boolean state)
    {
        bPlayershipAliveState = state;
    }
    void addNukes(int iAmount)
    {
        iNukesAmount = iNukesAmount + iAmount;
        
        labNukeAmount.setText(Integer.toString(iNukesAmount));
    }
    void addHPMissiles(int iAmount)
    {
        iHPMissilesAmount = iHPMissilesAmount + iAmount;
        
        labHPMissilesAmount.setText(Integer.toString(iHPMissilesAmount));
    }
    void show()
    {
        if(ivPlayershipSprite.getOpacity() == 0)    //If the game is being played again and ship is already on screen, then do not reset and reveal.
        {
            //Ensure that the correct graphic is showing...
            ivPlayershipSprite.setImage(imgPlayerShip);
            bPlayershipAliveState = true;

            //Set the Playership initial position...
            ivPlayershipSprite.setLayoutX(PLAYERSHIP_START_X_COORDINATE);
            ivPlayershipSprite.setTranslateX(0);

            tlPlayershipReveal.setCycleCount(1);

            tlPlayershipReveal.playFromStart();
        }
        else
            ivPlayershipSprite.setOpacity(1);
    }
    void hidePlayership()
    {
        ivPlayershipSprite.setOpacity(0);
    }
    void refreshNumberOfLives()
    {
        labPlayershipLives.setText(Integer.toString(iNumberOfLives));
    }
    void runPlayershipHitExplosion()
    {
        ivPlayershipSprite.setImage(imgPlayershipDestroyedExplosion);
        tlPlayershipDestroyedExplosionFade.playFromStart();
    }
    void hitPlayership()
    {
        bPlayershipAliveState = false;
        
        iNumberOfLives--;
        refreshNumberOfLives();

        runPlayershipHitExplosion();
    }
}
