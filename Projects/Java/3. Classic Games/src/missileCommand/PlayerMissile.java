package missileCommand;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

class PlayerMissile implements Constants{

    private AnchorPane              apMissileCommand;
    private Circle                  cirPlayerMissileSprite;
    private AlienMissilesManager    objAlienMissilesManager;
    private Timeline                tlMissileExplosion = new Timeline();
    private Timeline                tlMissileTranslate = new Timeline();

    private int                     iShipNumber;
    private boolean                 bFiring;
    private double                  dTargetXCoordinate, dTargetYCoordinate;
      
    PlayerMissile(AnchorPane apMissileCommand, int iShipNumber, AlienMissilesManager objAlienMissilesManager)
    {
        this.apMissileCommand           = apMissileCommand;
        this.iShipNumber                = iShipNumber;
        this.objAlienMissilesManager    = objAlienMissilesManager;
        
        initPlayerMissile();
        
        initTimelines();
        
        eventHandlerPlayerMissile();
    }    
    private void initTimelines()
    {
        tlMissileExplosion = new Timeline();
        tlMissileExplosion.setCycleCount(1);
        
        tlMissileExplosion.getKeyFrames().add(new KeyFrame(Duration.millis(0),          new KeyValue(cirPlayerMissileSprite.radiusProperty(),       0)));
        tlMissileExplosion.getKeyFrames().add(new KeyFrame(Duration.millis(0),          new KeyValue(cirPlayerMissileSprite.fillProperty(),         Color.WHITE)));
        tlMissileExplosion.getKeyFrames().add(new KeyFrame(Duration.millis(200),        new KeyValue(cirPlayerMissileSprite.radiusProperty(),       50)));
        tlMissileExplosion.getKeyFrames().add(new KeyFrame(Duration.millis(200),        new KeyValue(cirPlayerMissileSprite.fillProperty(),         Color.WHITE)));
        tlMissileExplosion.getKeyFrames().add(new KeyFrame(Duration.millis(450),        new KeyValue(cirPlayerMissileSprite.fillProperty(),         Color.YELLOW)));
        tlMissileExplosion.getKeyFrames().add(new KeyFrame(Duration.millis(700),        new KeyValue(cirPlayerMissileSprite.fillProperty(),         Color.RED)));
        tlMissileExplosion.getKeyFrames().add(new KeyFrame(Duration.millis(700),        new KeyValue(cirPlayerMissileSprite.radiusProperty(),       0)));
        
        tlMissileExplosion.setOnFinished((ActionEvent ae) -> {
            bFiring = false;
        });
    }
    private void initPlayerMissile()
    {
        //Declare and create the actual object
        cirPlayerMissileSprite = new Circle(0, 0, 3, Paint.valueOf("#ffffff"));

        //Add the object to the AnchorPane
        this.apMissileCommand.getChildren().add(cirPlayerMissileSprite);
        
        cirPlayerMissileSprite.setVisible(false);
        
        bFiring = false;
    }
    private double findMissileRate(double dTargetXCoordinate, double dTargetYCoordinate)
    {
        //The passed coordinates are absolute/layout values...
        
        double dX, dY, dDistance, dRatio;
        //Find the overall distance the missile needs to travel...

        if(iShipNumber == PLAYER_SHIP_LEFT)
        {
            dX = (dTargetXCoordinate - PLAYER_SHIP_LEFT_LAYOUT_X_START);
            dY = (PLAYER_SHIP_LEFT_LAYOUT_Y_START - dTargetYCoordinate);
        }
        else
        {
            dX = (PLAYER_SHIP_RIGHT_LAYOUT_X_START - dTargetXCoordinate);
            dY = (PLAYER_SHIP_RIGHT_LAYOUT_Y_START - dTargetYCoordinate);
        }        
        
        dDistance   = Math.sqrt((Math.pow(dX, 2) + (Math.pow(dY, 2))));
        
        dRatio      = (dDistance/PLAYER_SHIP_MISSILE_MAXIMUM_TRAVEL_DISTANCE);
        
        return (1/dRatio);
    }
    
    
    void fire(double dTargetXCoordinate, double dTargetYCoordinate)
    {
        //Find the distance between the start and end position, iXStart and iYStart are already known and fixed depending upon
        //which ship fires the missile...

        double dMissileRate;
        
        bFiring = true;
        
        this.dTargetXCoordinate = dTargetXCoordinate;
        this.dTargetYCoordinate = dTargetYCoordinate;
        
        cirPlayerMissileSprite.setVisible(true);
        cirPlayerMissileSprite.setRadius(3);
        cirPlayerMissileSprite.setFill(Color.WHITE);
        
        switch(iShipNumber)
        {
            case(PLAYER_SHIP_LEFT):
            {
                cirPlayerMissileSprite.setCenterX(PLAYER_SHIP_LEFT_LAYOUT_X_START);
                cirPlayerMissileSprite.setCenterY(PLAYER_SHIP_LEFT_LAYOUT_Y_START);
                break;
            }
            case(PLAYER_SHIP_RIGHT):
            {
                cirPlayerMissileSprite.setCenterX(PLAYER_SHIP_RIGHT_LAYOUT_X_START);
                cirPlayerMissileSprite.setCenterY(PLAYER_SHIP_RIGHT_LAYOUT_Y_START);
                break;
            }
        }

        dMissileRate = findMissileRate(dTargetXCoordinate, dTargetYCoordinate);
        
        //tlMissileTranslate = new Timeline();
        tlMissileTranslate.setCycleCount(1);
        tlMissileTranslate.setRate(dMissileRate);

        tlMissileTranslate.getKeyFrames().add(new KeyFrame(Duration.millis(200),        new KeyValue(cirPlayerMissileSprite.centerXProperty(),      dTargetXCoordinate)));
        tlMissileTranslate.getKeyFrames().add(new KeyFrame(Duration.millis(200),        new KeyValue(cirPlayerMissileSprite.centerYProperty(),      dTargetYCoordinate)));
        
        tlMissileTranslate.setOnFinished((ActionEvent ae) -> {
            //Reset the missiles position
            tlMissileTranslate.stop();
            tlMissileTranslate.jumpTo(Duration.ZERO);
            tlMissileTranslate.getKeyFrames().clear();
            
            tlMissileExplosion.playFromStart();
        });
        
        tlMissileTranslate.playFromStart();
    }
    boolean getFiringState()
    {
        return bFiring;
    }
    
    
    private void eventHandlerPlayerMissile()
    {
        cirPlayerMissileSprite.radiusProperty().addListener(new ChangeListener()
        {
            @Override
            public void changed(ObservableValue o, Object oldVal, Object newVal)
            {
                double dX, dY, dDistance;
                
                //Needs to look through the array of missiles to see if any of their End (X, Y) coordinates are within the radius of the explosion...
                for(int i = 1 ; i <= objAlienMissilesManager.getNumberOfAlienMissilesThisLevel() ; i++)
                {
                    if(objAlienMissilesManager.getMissileIgnitionTime(i) != NOT_USED)
                    {
                        //Obtain its end i.e the coordinate of the end of the falling missile...
                        dX = ( (objAlienMissilesManager.getMissileEndXProperty(i) + objAlienMissilesManager.getMissileLayoutXStart(i)) - dTargetXCoordinate);
                        dY = ( (objAlienMissilesManager.getMissileEndYProperty(i) + objAlienMissilesManager.getMissileLayoutYStart(i)) - dTargetYCoordinate);
                        
                        //Ensure that they are absolute values...
                        if(dX < 0)  dX = (dX*-1);
                        if(dY < 0)  dY = (dY*-1);
                        
                        //Find the distance between the center of explosion and end of Alien Missile
                        dDistance = Math.sqrt((Math.pow(dX, 2) + (Math.pow(dY, 2))));
                        
                        if(dDistance < cirPlayerMissileSprite.radiusProperty().doubleValue())
                            objAlienMissilesManager.alienMissileHitRunExplosion(i);
                    }
                }
            }
        }); 
    }
}
