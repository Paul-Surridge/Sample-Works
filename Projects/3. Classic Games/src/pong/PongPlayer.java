package pong;

import java.util.Timer;
import java.util.TimerTask;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

class PongPlayer implements Constants{

    private Timer       timerPlayerMoveUp, timerPlayerMoveDown;
    private Rectangle   recPlayer;
    private Line        lineGoal;
    
    private Timeline    tlGoalHitIllumination;
    private TranslateTransition ttPlayerMovement;
    
    private int         iPlayerNumber;
    private boolean     bPlayerMoving;
    
    PongPlayer(Rectangle recPlayer, Line lineGoal, int iPlayerNumber)
    { 
        this.recPlayer = recPlayer;
        this.lineGoal = lineGoal;
        this.iPlayerNumber = iPlayerNumber;

        bPlayerMoving = false;
                
        this.lineGoal.setOpacity(0);
                
        tlGoalHitIllumination = new Timeline();
        
        tlGoalHitIllumination.getKeyFrames().add(new KeyFrame(Duration.millis(0),       new KeyValue(lineGoal.opacityProperty(),   0)));
        tlGoalHitIllumination.getKeyFrames().add(new KeyFrame(Duration.millis(200),     new KeyValue(lineGoal.opacityProperty(),   1)));
        tlGoalHitIllumination.getKeyFrames().add(new KeyFrame(Duration.millis(700),     new KeyValue(lineGoal.opacityProperty(),   1)));
        tlGoalHitIllumination.getKeyFrames().add(new KeyFrame(Duration.millis(1200),    new KeyValue(lineGoal.opacityProperty(),   0)));
        
        tlGoalHitIllumination.setCycleCount(1);
        
        initTranslateTransitions();
        eventListnerPlayerRectangle();
        
        resetPlayerPosition();
    }
    
    
    
    private void initTranslateTransitions()
    {
        //Initialise and define the ball movement translate transition
        ttPlayerMovement = new TranslateTransition();
        ttPlayerMovement.setNode(recPlayer);
        ttPlayerMovement.setCycleCount(1);
        ttPlayerMovement.setDuration(Duration.millis(100));
        ttPlayerMovement.setInterpolator(Interpolator.LINEAR);
    }
    
    
    
    void resetPlayerPosition()
    {
        switch(iPlayerNumber)
        {
            case PLAYER_1:      recPlayer.setLayoutX(PLAYER_1_LAYOUT_X_POSITION_INITIALISE);        break;
            case PLAYER_2:      recPlayer.setLayoutX(PLAYER_2_LAYOUT_X_POSITION_INITIALISE);        break;
        }
        
        recPlayer.setLayoutY(TABLE_CENTRE_Y_POSITION_INITIALISE - (recPlayer.getHeight() / 2));
        
        recPlayer.setTranslateX(0);
        recPlayer.setTranslateY(0);
    }
    void moveUp()
    {
        if( (!bPlayerMoving) && (getPlayerTopYCoordinate() > PLAYING_AREA_TOP_Y_COORDINATE) )
        {
            bPlayerMoving = true;
            timerPlayerMoveUp = new Timer("timerPlayerMoveUp", true);

            timerPlayerMoveUp.schedule(new TimerTask() {
                @Override
                public void run() {
                    ttPlayerMovement.stop();
                    ttPlayerMovement.setByY(-100);
                    ttPlayerMovement.play();
                }
            }, 0, 100);
        }
    }
    void moveDown()
    {
        if( (!bPlayerMoving) && (getPlayerBottomYCoordinate() < PLAYING_AREA_BOTTOM_Y_COORDINATE) )
        {
            bPlayerMoving = true;
            timerPlayerMoveDown = new Timer("timerPlayerMoveDown", true);

            timerPlayerMoveDown.schedule(new TimerTask() {
                @Override
                public void run() {
                    ttPlayerMovement.stop();
                    ttPlayerMovement.setByY(100);
                    ttPlayerMovement.play();
                }
            }, 0, 100);
        }
    }
    void moveStopDown()
    {
        bPlayerMoving = false;
        ttPlayerMovement.stop();
        timerPlayerMoveDown.cancel();
    }
    void moveStopUp()
    {
        bPlayerMoving = false;
        ttPlayerMovement.stop();
        timerPlayerMoveUp.cancel();
    }
    int getPlayerTopYCoordinate()
    {
        return (int)(recPlayer.getTranslateY() - (0.5*recPlayer.getHeight()));
    }
    int getPlayerBottomYCoordinate()
    {
        return (int)(recPlayer.getTranslateY() + (0.5*recPlayer.getHeight()));
    }
    void runGoalHitIllumination()
    {
        tlGoalHitIllumination.playFromStart();
    }
    void setPlayerBatSize(int iBatSize)
    {
        recPlayer.setHeight((50 + (10*iBatSize)));
        
        resetPlayerPosition();
    }
    
    
    
    private void eventListnerPlayerRectangle()
    {
        recPlayer.translateYProperty().addListener(new ChangeListener()
        {
            @Override
            public void changed(ObservableValue o, Object oldVal, Object newVal)
            {
                if((Double)newVal <= (PLAYING_AREA_TOP_Y_COORDINATE + (recPlayer.getHeight()/2)))
                {
                    moveStopUp();
                    recPlayer.setTranslateY( (PLAYING_AREA_TOP_Y_COORDINATE + (recPlayer.getHeight()/2)));
                }                
                if((Double)newVal >= (PLAYING_AREA_BOTTOM_Y_COORDINATE - (recPlayer.getHeight()/2)))
                {
                    moveStopDown();
                    recPlayer.setTranslateY( (PLAYING_AREA_BOTTOM_Y_COORDINATE - (recPlayer.getHeight()/2)));
                }
            }
        });
    }
}
