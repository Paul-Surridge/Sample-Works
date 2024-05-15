package spaceInvaders;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

class AlienShip implements Constants{
    
    private TranslateTransition     ttAlienShipMoveAmount = new TranslateTransition();
    private Timeline                tlAlienShipReveal, tlAlienShipMissileFire, tlAlienShipHitExplosionFade;

    private int         iAlienShipMissileXPosition, iRow, iScoreAmount, iShipType;
    private boolean     bAliveState;
    private ImageView   ivAlienShipSprite;

    AlienShip(int iAlienShipXPosition, int iAlienShipYPosition)
    {
        ivAlienShipSprite = new ImageView(imgAlienGreen);
        ivAlienShipSprite.setOpacity(0);
        ivAlienShipSprite.setFitHeight(40);  
        ivAlienShipSprite.setFitWidth(40);
        
        tlAlienShipMissileFire = new Timeline();
        tlAlienShipMissileFire.setCycleCount(1);
        
        //Initialise AlienShip position
        ivAlienShipSprite.setLayoutX((double) iAlienShipXPosition);
        ivAlienShipSprite.setLayoutY((double) iAlienShipYPosition);

        bAliveState = true;

        ttAlienShipMoveAmount.setNode(ivAlienShipSprite);
        ttAlienShipMoveAmount.setDuration(Duration.millis(300));

        initTimelines();
    }
    
    
    
    private void initTimelines()
    {
        tlAlienShipReveal = new Timeline();
        tlAlienShipReveal.setCycleCount(1);
        tlAlienShipReveal.getKeyFrames().add(new KeyFrame(Duration.millis(0),       new KeyValue(ivAlienShipSprite.opacityProperty(),   0)));
        tlAlienShipReveal.getKeyFrames().add(new KeyFrame(Duration.millis(500),     new KeyValue(ivAlienShipSprite.opacityProperty(),   1)));
        
        tlAlienShipHitExplosionFade = new Timeline();
        tlAlienShipHitExplosionFade.setCycleCount(1);
        tlAlienShipHitExplosionFade.getKeyFrames().add(new KeyFrame(Duration.millis(0),       new KeyValue(ivAlienShipSprite.opacityProperty(),   1)));
        tlAlienShipHitExplosionFade.getKeyFrames().add(new KeyFrame(Duration.millis(300),     new KeyValue(ivAlienShipSprite.opacityProperty(),   0)));
    }
    
    
    
    boolean getAliveState()
    {
        return bAliveState;
    }
    void setAliveState(boolean state)
    {
        bAliveState = state;
    }
    void setAlienRow(int iRowAssign)
    {
        iRow = iRowAssign;
    }
    void show()
    {
        tlAlienShipReveal.playFromStart();
    }
    void hide()
    {
        ivAlienShipSprite.setOpacity(0);
    }
    int getAlienShipXPosition()
    {
        return (int)(ivAlienShipSprite.getLayoutX() + ivAlienShipSprite.getTranslateX() + 23);
    }
    int getAlienShipYPosition()
    {
        return (int)(ivAlienShipSprite.getLayoutY() + 46);
    }
    void setAlienShipXPosition(double dXPosition)
    {
        ivAlienShipSprite.setLayoutX(dXPosition);
    }
    void setAlienShipYPosition(double dYPosition)
    {
        ivAlienShipSprite.setLayoutY(dYPosition);
    }
    void moveAlienShip(int iAmount)
    {
        ttAlienShipMoveAmount.setByX(iAmount);                               
        ttAlienShipMoveAmount.play(); 
    }
    int fireMissile(Circle cirAlienShipMissile, int iAlienMissileDuration)
    {
        //Find Missile Position - Now find and set the coordinates of the missile and fire towards the cities... 
        iAlienShipMissileXPosition = getAlienShipXPosition();
        
        cirAlienShipMissile.setCenterX(iAlienShipMissileXPosition);
        cirAlienShipMissile.setCenterY(getAlienShipYPosition());

        tlAlienShipMissileFire.getKeyFrames().clear();
        tlAlienShipMissileFire.getKeyFrames().add(new KeyFrame(Duration.millis(iAlienMissileDuration),       new KeyValue(cirAlienShipMissile.centerYProperty(),   600)));

        cirAlienShipMissile.setVisible(true);
        tlAlienShipMissileFire.playFromStart();
               
        //Find the missiles target city if any...
        if( (iAlienShipMissileXPosition >= 150) && (iAlienShipMissileXPosition <= 250))         return 1;
        else if( (iAlienShipMissileXPosition >= 288) && (iAlienShipMissileXPosition <= 388))    return 2;
        else if( (iAlienShipMissileXPosition >= 423) && (iAlienShipMissileXPosition <= 523))    return 3;
        else if( (iAlienShipMissileXPosition >= 555) && (iAlienShipMissileXPosition <= 655))    return 4;
        else if( (iAlienShipMissileXPosition >= 688) && (iAlienShipMissileXPosition <= 788))    return 5;
        else                                                                                    return 0;
    }
    int getAlienShipMissileXPosition()
    {
        return iAlienShipMissileXPosition;
    }
    int getAlienRow()
    {
        return iRow;
    }
    int getScoreAmount()
    {
        return iScoreAmount;
    }
    int getAlienShipType()
    {
        return iShipType;
    }
    ImageView getAlienShipImageView()
    {
        return ivAlienShipSprite;
    }
    void setAlienShipType(int iType)
    {
        switch(iType)
        {
            case(ALIEN_GREEN):    ivAlienShipSprite.setImage(imgAlienGreen);         break;
            case(ALIEN_BLUE):     ivAlienShipSprite.setImage(imgAlienBlue);          break;
            case(ALIEN_RED):      ivAlienShipSprite.setImage(imgAlienRed);           break;
            case(ALIEN_PURPLE):   ivAlienShipSprite.setImage(imgAlienPurple);        break;
            case(ALIEN_ORANGE):   ivAlienShipSprite.setImage(imgAlienOrange);        break;
        }
        switch(iType)
        {
            case(ALIEN_GREEN):    iScoreAmount = 1000;           break;
            case(ALIEN_BLUE):     iScoreAmount = 1200;           break;
            case(ALIEN_RED):      iScoreAmount = 1500;           break;
            case(ALIEN_PURPLE):   iScoreAmount = 2000;           break;
            case(ALIEN_ORANGE):   iScoreAmount = 3000;           break;
        }
        
        iShipType = iType;
    }
    void hitAlien()
    {
        bAliveState = false;
        ivAlienShipSprite.setImage(imgExplosion);

        tlAlienShipHitExplosionFade.playFromStart();
    }
}
