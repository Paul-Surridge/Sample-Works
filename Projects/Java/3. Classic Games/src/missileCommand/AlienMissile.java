package missileCommand;

import java.util.Random;
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
import javafx.scene.shape.Line;
import javafx.util.Duration;

class AlienMissile implements Constants{

    private MissileCommand      objMissileCommand;
    private Line                liAlienMissile;
    private Timeline            tlMissileTranslation, tlMissileExplosion;
    private Circle              cirMissileExplosion;
    private Random              objRandom = new Random();
        
    private int                 iLayoutXStart, iLayoutXDestination, iEndXNetProperty;
    private int                 iTarget, iIgnitionTime;
    private boolean             bMissileActive;
    
    AlienMissile(MissileCommand objMissileCommand, AnchorPane apMissileCommand)
    {
        this.objMissileCommand = objMissileCommand;
        
        liAlienMissile = new Line();
        liAlienMissile.setStroke(Color.YELLOW);
        
        //Immediately add the new missile to the AnchorPane and have it assign start and end coordinates...
        liAlienMissile.setVisible(false);
        apMissileCommand.getChildren().addAll(liAlienMissile); 
        
        //Declare, create and add the object to the AnchorPane
        cirMissileExplosion = new Circle(0, 0, 3, Paint.valueOf("#ffffff"));
        apMissileCommand.getChildren().add(cirMissileExplosion);
        
        eventHandlerAlienMissile();
        
        initTimelines();
    }
    
    
    private void initTimelines()
    {
        tlMissileExplosion = new Timeline();
        
        tlMissileExplosion.setCycleCount(1);

        tlMissileExplosion.getKeyFrames().add(new KeyFrame(Duration.millis(100),        new KeyValue(cirMissileExplosion.radiusProperty(),    20)));
        tlMissileExplosion.getKeyFrames().add(new KeyFrame(Duration.millis(200),        new KeyValue(cirMissileExplosion.fillProperty(),      Color.WHITE)));
        tlMissileExplosion.getKeyFrames().add(new KeyFrame(Duration.millis(350),        new KeyValue(cirMissileExplosion.fillProperty(),      Color.YELLOW)));
        tlMissileExplosion.getKeyFrames().add(new KeyFrame(Duration.millis(600),        new KeyValue(cirMissileExplosion.fillProperty(),      Color.RED)));
        tlMissileExplosion.getKeyFrames().add(new KeyFrame(Duration.millis(600),        new KeyValue(cirMissileExplosion.radiusProperty(),    0)));
        tlMissileExplosion.getKeyFrames().add(new KeyFrame(Duration.millis(600),        new KeyValue(cirMissileExplosion.fillProperty(),      Color.WHITE)));
        
        tlMissileExplosion.setOnFinished((ActionEvent ae) -> {
            hideAlienMissile();
        });
    }
    private int findTargetCity()
    {
        if( (iLayoutXDestination >= 45) && (iLayoutXDestination <= 122))            return PLAYER_SHIP_LEFT;
        else if( (iLayoutXDestination >= 150) && (iLayoutXDestination <= 250))      return CITY_1;
        else if( (iLayoutXDestination >= 288) && (iLayoutXDestination <= 388))      return CITY_2;
        else if( (iLayoutXDestination >= 423) && (iLayoutXDestination <= 523))      return CITY_3;
        else if( (iLayoutXDestination >= 555) && (iLayoutXDestination <= 655))      return CITY_4;
        else if( (iLayoutXDestination >= 688) && (iLayoutXDestination <= 788))      return CITY_5;
        else if( (iLayoutXDestination >= 819) && (iLayoutXDestination <= 896))      return PLAYER_SHIP_RIGHT;
        else                                                                        return NO_CITY_TARGETED;
    }
    private void findAlienMissileTrajectory()
    {
        iLayoutXStart = 70 + objRandom.nextInt(801);            //Range 70 - 870

        //Move the Alien Missile to the random start position...
        
        liAlienMissile.setLayoutX((double)iLayoutXStart);
        liAlienMissile.setLayoutY((double)ALL_ALIEN_MISSILES_START_Y_POSITION);
        
        iLayoutXDestination = 30 + objRandom.nextInt(931);      //Range 30 - 930

        iEndXNetProperty = (iLayoutXDestination - iLayoutXStart);
        
        iTarget = findTargetCity();
        
        //Define Alien Missile Translation
        tlMissileTranslation = new Timeline();

        tlMissileTranslation.setCycleCount(1);

        tlMissileTranslation.getKeyFrames().add(new KeyFrame(Duration.millis(0),          new KeyValue(liAlienMissile.startXProperty(),       0)));
        tlMissileTranslation.getKeyFrames().add(new KeyFrame(Duration.millis(0),          new KeyValue(liAlienMissile.startYProperty(),       0)));
        tlMissileTranslation.getKeyFrames().add(new KeyFrame(Duration.millis(3000),       new KeyValue(liAlienMissile.endXProperty(),         iEndXNetProperty)));
        tlMissileTranslation.getKeyFrames().add(new KeyFrame(Duration.millis(3000),       new KeyValue(liAlienMissile.endYProperty(),         ALL_ALIEN_MISSILES_DESTINATION_Y_POSITION)));

        tlMissileTranslation.setOnFinished((ActionEvent ae) -> {
            tlMissileTranslation.getKeyFrames().clear();
        });
    }
    private void hideAlienMissile()
    {                            
        liAlienMissile.setEndX(0);
        liAlienMissile.setEndY(0);
        
        liAlienMissile.setVisible(false);
 
        tlMissileTranslation.stop(); 
        
        bMissileActive = false;
        
        objMissileCommand.isEndOfLevel();
    }
    
    
    void initialise()
    {
        liAlienMissile.setStartX(0);
        liAlienMissile.setStartY(0);
        liAlienMissile.setEndX(0);
        liAlienMissile.setEndY(0);
        
        liAlienMissile.setLayoutX(0);
        liAlienMissile.setLayoutY(0);
    }
    int getLayoutXStart()
    {
        return iLayoutXStart;
    }
    double getLayoutYStart()
    {
        return liAlienMissile.getLayoutY();
    }
    double getEndXProperty()
    {
        return liAlienMissile.getEndX();
    }
    double getEndYProperty()
    {
        return liAlienMissile.getEndY();
    }
    void alienMissileFire(int iLevel)
    {
        double dTimelineRate;
        
        switch(iLevel)
        {
            case(1):    dTimelineRate = 1;              break;
            case(2):    dTimelineRate = 1.2;            break;
            case(3):    dTimelineRate = 1.5;            break;
            case(4):    dTimelineRate = 2;              break;
            case(5):    dTimelineRate = 2.5;            break;
            default:    dTimelineRate = 3;              break;
        }
        bMissileActive = true;
        
        //Start the Alien Missile at the designated speed and send it to the designated city...
        findAlienMissileTrajectory();
        
        liAlienMissile.setVisible(true);
        tlMissileTranslation.setRate(dTimelineRate);
        tlMissileTranslation.playFromStart();
    }
    void hitRunExplosion()
    {
        //Show a small explosion to indicate that the missile has been hit, whilst hiding the trail...
        tlMissileTranslation.stop(); 
        
        cirMissileExplosion.setCenterX(liAlienMissile.getLayoutX() + liAlienMissile.getEndX());
        cirMissileExplosion.setCenterY(liAlienMissile.getLayoutY() + liAlienMissile.getEndY());
        
        tlMissileExplosion.playFromStart(); 
    }
    boolean isMissileActive()
    {
        return bMissileActive;
    }
    int getIgnitionTime()
    {
        return iIgnitionTime;
    }
    void setIgnitionTime(int iIgnitionTime)
    {
        this.iIgnitionTime = iIgnitionTime;
    }
    
    
    private void eventHandlerAlienMissile()
    {
        liAlienMissile.endYProperty().addListener(new ChangeListener()
        {
            @Override
            public void changed(ObservableValue o, Object oldVal, Object newVal)
            {
                if(liAlienMissile.getEndY() > (ALL_ALIEN_MISSILES_DESTINATION_Y_POSITION - 5))
                {
                    if      ( (iTarget >= CITY_1) && (iTarget <= CITY_5) )                            objMissileCommand.hitCity(iTarget);                       
                    else if ( (iTarget >= PLAYER_SHIP_LEFT) && (iTarget <= PLAYER_SHIP_RIGHT) )       objMissileCommand.hitPlayerShip(iTarget);
                    
                    hitRunExplosion();
                }
            }
        }); 
    }
}
