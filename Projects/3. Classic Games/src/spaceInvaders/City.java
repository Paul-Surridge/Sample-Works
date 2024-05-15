package spaceInvaders;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

class City implements Constants{

    private ImageView   ivCitySprite;
    private Timeline    tlDestroyExplosionFade;
    
    public boolean      bAliveState;
    
    City(ImageView im)
    {
        ivCitySprite       = im;
        bAliveState        = true;
        
        tlDestroyExplosionFade = new Timeline();
        tlDestroyExplosionFade.getKeyFrames().add(new KeyFrame(Duration.millis(2000),    new KeyValue(ivCitySprite.opacityProperty(), 0)));
    }    
    
    
    
    void setAliveState(boolean bState)
    {
        if(bState)
        {
            ivCitySprite.setImage(imgCity);
            ivCitySprite.setOpacity(1);
        }
        
        bAliveState = bState;
    }
    void hitCity()
    {
        bAliveState = false;
        ivCitySprite.setImage(imgNuke);
        tlDestroyExplosionFade.play();
    }
}
