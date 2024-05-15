package pong;

import java.util.Random;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

class Ball implements Constants{

    private TranslateTransition     ttBallMovement;
    private Circle                  cirBallSprite;
    private Timeline                tlBallReveal;
    private Random                  objRandom = new Random();

    private int iNewDestinationXPosition, iNewDestinationYPosition;
    private int iBallHorizontalDirection, iBallVerticalDirection;    
    private int iLastHitBoundary;
    
    private double dBallMovementDuration;
        
    Ball(AnchorPane ap)
    {
        //Initialise the new ball...
        cirBallSprite = new Circle(475, 351, 5, Paint.valueOf("#fff500")); 
        cirBallSprite.setOpacity(0);
        ap.getChildren().add(cirBallSprite);
        
        initTranslateTransitions(BALL_RATE_HALF);
        
        initTimelines();        
    }
    
    
    
    private void initTranslateTransitions(double iDuration)
    {
        //Initialise and define the ball movement translate transition
        ttBallMovement = new TranslateTransition();
        ttBallMovement.setNode(cirBallSprite);
        ttBallMovement.setCycleCount(1);
        ttBallMovement.setDuration(Duration.millis(iDuration));
        ttBallMovement.setInterpolator(Interpolator.LINEAR);
    }
    private void initTimelines()
    {
        //Initialise and define the Ball Reveal Timeline
        tlBallReveal = new Timeline();

        tlBallReveal.getKeyFrames().add(new KeyFrame(Duration.millis(0),    new KeyValue(cirBallSprite.opacityProperty(),   0)));
        tlBallReveal.getKeyFrames().add(new KeyFrame(Duration.millis(500),  new KeyValue(cirBallSprite.opacityProperty(),   1)));
        
        //Define the OnFinished aspect of the timelines
        tlBallReveal.setOnFinished((ActionEvent ae) -> {
            ttBallMovement.playFromStart();
        });
    }
    private void hitTopBoundary()
    {
        if((iBallVerticalDirection == UP) && (iBallHorizontalDirection == LEFT))            //Hit top and moving leftwards
        {
            iBallHorizontalDirection                        = LEFT;
            iBallVerticalDirection                          = DOWN;

            iNewDestinationXPosition = iNewDestinationXPosition - (int)((Math.tan(ANGLE_OF_INFLECTION)) * PLAYING_AREA_HEIGHT);
            iNewDestinationYPosition = PLAYING_AREA_BOTTOM_Y_COORDINATE;

        }        
        else if((iBallVerticalDirection == UP) && (iBallHorizontalDirection == RIGHT))      //Hit top and moving rightwards
        {
            iBallHorizontalDirection                        = RIGHT;
            iBallVerticalDirection                          = DOWN;

            iNewDestinationXPosition = (int) (((Math.tan(ANGLE_OF_INFLECTION)) * PLAYING_AREA_HEIGHT) + iNewDestinationXPosition);
            iNewDestinationYPosition = PLAYING_AREA_BOTTOM_Y_COORDINATE;
        }       
    }
    private void hitBottomBoundary()
    {
        if((iBallVerticalDirection == DOWN) && (iBallHorizontalDirection == LEFT))          //Hit bottom and moving leftwards
        {
            iBallHorizontalDirection                        = LEFT;
            iBallVerticalDirection                          = UP;

            iNewDestinationXPosition = iNewDestinationXPosition - (int)((Math.tan(ANGLE_OF_INFLECTION)) * PLAYING_AREA_HEIGHT);
            iNewDestinationYPosition = PLAYING_AREA_TOP_Y_COORDINATE;
        }       
        else if((iBallVerticalDirection == DOWN) && (iBallHorizontalDirection == RIGHT))    //Hit bottom and moving rightwards
        {
            iBallHorizontalDirection                        = RIGHT;
            iBallVerticalDirection                          = UP;

            iNewDestinationXPosition = (int) (((Math.tan(ANGLE_OF_INFLECTION)) * PLAYING_AREA_HEIGHT) + iNewDestinationXPosition);
            iNewDestinationYPosition = PLAYING_AREA_TOP_Y_COORDINATE;
        } 
    }
    private void hitLeftBoundary()
    {
        if((iBallVerticalDirection == UP) && (iBallHorizontalDirection == LEFT))            //Hit left boundary and moving upwards
        {
            iNewDestinationXPosition = (int)(PLAYING_AREA_LEFT_X_COORDINATE - ((PLAYING_AREA_TOP_Y_COORDINATE - cirBallSprite.getTranslateY()) * ((Math.tan(ANGLE_OF_INFLECTION)))));
            iNewDestinationYPosition = PLAYING_AREA_TOP_Y_COORDINATE;

            iBallHorizontalDirection                        = RIGHT;
            iBallVerticalDirection                          = UP;
        } 
        else if((iBallVerticalDirection == DOWN) && (iBallHorizontalDirection == LEFT))     //Hit left boundary and moving upwards
        {
            iNewDestinationXPosition = (int)(PLAYING_AREA_LEFT_X_COORDINATE + ((PLAYING_AREA_BOTTOM_Y_COORDINATE - cirBallSprite.getTranslateY()) * ((Math.tan(ANGLE_OF_INFLECTION)))));
            iNewDestinationYPosition = PLAYING_AREA_BOTTOM_Y_COORDINATE;

            iBallHorizontalDirection                        = RIGHT;
            iBallVerticalDirection                          = DOWN;
        }
    }
    private void hitRightBoundary()
    {
        if((iBallVerticalDirection == UP) && (iBallHorizontalDirection == RIGHT))           //Hit right boundary and moving downwards
        {
            iNewDestinationXPosition = (int)(PLAYING_AREA_RIGHT_X_COORDINATE + ((PLAYING_AREA_TOP_Y_COORDINATE - cirBallSprite.getTranslateY()) * ((Math.tan(ANGLE_OF_INFLECTION)))));
            iNewDestinationYPosition = PLAYING_AREA_TOP_Y_COORDINATE;

            iBallHorizontalDirection                        = LEFT;
            iBallVerticalDirection                          = UP;
        }                
        else if((iBallVerticalDirection == DOWN) && (iBallHorizontalDirection == RIGHT))    //Hit right boundary and moving downwards
        {
            iNewDestinationXPosition = (int)(PLAYING_AREA_RIGHT_X_COORDINATE - ((PLAYING_AREA_BOTTOM_Y_COORDINATE - cirBallSprite.getTranslateY()) * ((Math.tan(ANGLE_OF_INFLECTION)))));
            iNewDestinationYPosition = PLAYING_AREA_BOTTOM_Y_COORDINATE;

            iBallHorizontalDirection                        = LEFT;
            iBallVerticalDirection                          = DOWN;
        } 
    }
    private void findBallSpeed()
    {
        if(iBallVerticalDirection == UP)            ttBallMovement.setRate( (PLAYING_AREA_HEIGHT/( (PLAYING_AREA_TOP_Y_COORDINATE - cirBallSprite.getTranslateY()) * -1)));
        else if(iBallVerticalDirection == DOWN)     ttBallMovement.setRate( (PLAYING_AREA_HEIGHT/(PLAYING_AREA_BOTTOM_Y_COORDINATE - cirBallSprite.getTranslateY())));
    }
    private void findInitialBallDirection()
    {
        int iRandomBallDirection;
        
        iRandomBallDirection = objRandom.nextInt(12);

        if( (iRandomBallDirection >= 0) && (iRandomBallDirection <= 2) )
        {
            iBallVerticalDirection          = UP;
            iBallHorizontalDirection        = LEFT;    
        }
        else if( (iRandomBallDirection >= 3) && (iRandomBallDirection <= 6) )
        {
            iBallVerticalDirection          = UP;
            iBallHorizontalDirection        = RIGHT;
        }
        else if( (iRandomBallDirection >= 7) && (iRandomBallDirection <= 9) )
        {
            iBallVerticalDirection          = DOWN;
            iBallHorizontalDirection        = LEFT;
        }
        else
        {
            iBallVerticalDirection          = DOWN;
            iBallHorizontalDirection        = RIGHT;
        }
        
        switch(iBallVerticalDirection)
        {
            case(UP):       hitTopBoundary();       break;
            case(DOWN):     hitBottomBoundary();    break;
        }
    }
    private void setBallNewDestination()
    {
        ttBallMovement.setFromX(cirBallSprite.getTranslateX());
        ttBallMovement.setFromY(cirBallSprite.getTranslateY());

        ttBallMovement.setToX(iNewDestinationXPosition);
        ttBallMovement.setToY(iNewDestinationYPosition);
    }
    
    
    
    void revealAndStartMoving()
    {
        initTranslateTransitions(dBallMovementDuration*2);
        
        tlBallReveal.jumpTo(Duration.ZERO);
        
        resetPosition();
        
        tlBallReveal.playFromStart();     
    }
    void hitBoundary(int iBoundary, int iMode, int iBoundaryType)
    { 
        iLastHitBoundary = iBoundary;
        
        ttBallMovement.stop();
      
        initTranslateTransitions(dBallMovementDuration);
  
        switch(iBoundary)
        {
            case(TOP_BOUNDARY):         hitTopBoundary();                   break;
            case(BOTTOM_BOUNDARY):      hitBottomBoundary();                break;
            case(LEFT_BOUNDARY):        hitLeftBoundary();                  break;
            case(RIGHT_BOUNDARY):       hitRightBoundary();                 break;
        }
        
        if( (iBoundary == TOP_BOUNDARY) || (iBoundary == BOTTOM_BOUNDARY) || (iBoundary == LEFT_BOUNDARY && iBoundaryType == BALL_REFLECT) || (iBoundary == RIGHT_BOUNDARY && iBoundaryType == BALL_REFLECT) || (iBoundary == LEFT_BOUNDARY && iMode == PRACTICE_MODE) || (iBoundary == RIGHT_BOUNDARY && iMode == PRACTICE_MODE) )
        {
            setBallNewDestination();

            findBallSpeed();
            
            ttBallMovement.play();  
        }
    }
    void stopMoving()
    {
        ttBallMovement.stop();      
    }
    void hide()
    {
        cirBallSprite.setOpacity(0);
    }
    void resetPosition()
    {
        cirBallSprite.setCenterX(475);
        cirBallSprite.setCenterY(351);
        
        cirBallSprite.setTranslateX(0);
        cirBallSprite.setTranslateY(0);

        iNewDestinationXPosition = 0;
        iNewDestinationYPosition = 0;
        
        findInitialBallDirection();
        
        setBallNewDestination();
        
        findBallSpeed();
    }
    int getBallHorizontalDirection()
    {
        return iBallHorizontalDirection;
    }
    int getBallVerticalDirection()
    {
        return iBallVerticalDirection;
    }
    int getNextDestinationXPosition()
    {
        return iNewDestinationXPosition;
    }
    int getNextDestinationYPosition()
    {
        return (int)(cirBallSprite.getTranslateY());
    }
    int getBallYPosition()
    {
        return iNewDestinationXPosition;
    }
    int getLastHitBoundary()
    {
        return iLastHitBoundary;
    }
    DoubleProperty getTranslateXAsProperty()
    {
        return cirBallSprite.translateXProperty();
    }
    DoubleProperty getTranslateYAsProperty()
    {
        return cirBallSprite.translateYProperty();
    }
    Double getTranslateXAsValue()
    {
        return cirBallSprite.translateXProperty().getValue();
    }
    Double getTranslateYAsValue()
    {
        return cirBallSprite.translateYProperty().getValue();
    }
    void setBallSpeed(int iSpeed)
    {
        if((iSpeed >= 1) && (iSpeed <= 10))
            dBallMovementDuration = (double)(3000 - (iSpeed*250));
    }
}
