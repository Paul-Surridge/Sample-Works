package spaceInvaders;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

class Mothership implements Constants{

    private ImageView               ivMothershipSprite;
    private TranslateTransition     ttMothership;
    private Timeline                tlMothershipExplosion;
    private Timeline                tlMothershipShow;
    private Timer                   timerShowMothership;
    private TimerTask               ttShowMothership;
    private Random                  objRandom;
    
    private int         iScoreAmount, iMothershipLeftXPosition, iMothershipRightXPosition, iMothershipDirection;
    private boolean     bActive, bAlive;
    
    Mothership(ImageView im)
    {
        ivMothershipSprite = im;
        ivMothershipSprite.setImage(imgMothership);
        ivMothershipSprite.setOpacity(0);

        iScoreAmount = 5000;
        
        iMothershipLeftXPosition     = 50;
        iMothershipRightXPosition    = 800;
        
        bActive = false;
        bAlive = false;
        
        objRandom = new Random();
        
        initTimelines();
        initTranslateTransition();
    }
    
    
    
    private void initTimelines()
    {
        tlMothershipExplosion = new Timeline();
        tlMothershipExplosion.getKeyFrames().add(new KeyFrame(Duration.millis(0),       new KeyValue(ivMothershipSprite.opacityProperty(), 1)));
        tlMothershipExplosion.getKeyFrames().add(new KeyFrame(Duration.millis(300),     new KeyValue(ivMothershipSprite.opacityProperty(), 0)));
        tlMothershipExplosion.setCycleCount(1);

        tlMothershipShow = new Timeline();
        tlMothershipShow.getKeyFrames().add(new KeyFrame(Duration.millis(0),        new KeyValue(ivMothershipSprite.opacityProperty(), 0)));
        tlMothershipShow.getKeyFrames().add(new KeyFrame(Duration.millis(500),      new KeyValue(ivMothershipSprite.opacityProperty(), 1)));
        tlMothershipShow.setCycleCount(1);
    }
    private void initTranslateTransition()
    {
        ttMothership = new TranslateTransition(Duration.millis(5000), ivMothershipSprite);
        ttMothership.setCycleCount(1);
        
        ttMothership.setOnFinished((ActionEvent ae) -> {
            if(iMothershipDirection == RIGHT)
            {
                //Change direction...
                ttMothership.stop();
                ttMothership.setToX(iMothershipLeftXPosition);
                ttMothership.play();
                
                iMothershipDirection = LEFT;
            }
            else if(iMothershipDirection == LEFT)
            {
                //Change direction...
                ttMothership.stop();
                ttMothership.setToX(iMothershipRightXPosition);
                ttMothership.play();
                
                iMothershipDirection = RIGHT;
            }
        });
    } 
    private void resetAndShow()
    {
        bAlive = true;

        ivMothershipSprite.setLayoutX(PLAYING_AREA_LEFT_X_COORDINATE);
        ivMothershipSprite.setTranslateX(0);
        ivMothershipSprite.setImage(imgMothership);
        
        iMothershipDirection = RIGHT;
        
        ttMothership.setToX(iMothershipRightXPosition);
        ttMothership.playFromStart();  
    
        tlMothershipShow.playFromStart();
    }
    private void generate()
    {
        timerShowMothership = new Timer("Mothership", true);
        ttShowMothership    = new TimerTask() {
            @Override
            public void run() {

                Platform.runLater(() -> {
                    if(bActive)
                        resetAndShow();

                    timerShowMothership.cancel();
                });
            }
        };

        timerShowMothership.schedule(ttShowMothership, (objRandom.nextInt(3000) + 2000));
    }
    
    
    
    int getScoreAmount()
    {
        return iScoreAmount;
    }
    void start()
    {
        bActive = true;
        
        generate();
    }
    void checkIfMothershipHit(Playership objPlayership, ScoreManager objScoreManager)
    {
        double dMothershipXOriginPosition;

        dMothershipXOriginPosition = (ivMothershipSprite.getLayoutX() + ivMothershipSprite.getTranslateX());
        
        if( (bAlive) && (objPlayership.getPlayershipMissileXPosition() >= dMothershipXOriginPosition) && (objPlayership.getPlayershipMissileXPosition() <= (dMothershipXOriginPosition + ivMothershipSprite.getFitWidth())) )
        {
            objPlayership.missileStopAndReset();

            objScoreManager.add(getScoreAmount());
            
            hitMothership();
        }
    }

    

    void stop()
    {
        bActive = false;
        bAlive = false;
        
        ttMothership.stop();
        
        timerShowMothership.cancel();
        
        ivMothershipSprite.setOpacity(0);
    }
    void hitMothership()
    {
        bAlive = false;
        
        ttMothership.stop();
        
        ivMothershipSprite.setImage(imgMothershipExplosion);
  
        tlMothershipExplosion.play();
        
        generate();
    }
}
