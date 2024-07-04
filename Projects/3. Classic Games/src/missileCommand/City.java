package missileCommand;

import app.App;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

class City{

    private boolean     bAliveState;
    private ImageView   ivCitySprite;

    private Timeline    tlDestroyExplosionFade = new Timeline();
    
    private Image       imgNuke = new Image(App.class.getResource("images/missileCommand/Nuke.png").toString());
    private Image       imgCity = new Image(App.class.getResource("images/missileCommand/City.png").toString());
    
    City(ImageView im)
    {
        ivCitySprite    = im;
        bAliveState     = true;
        
        KeyValue kvDestroyExplosionFade = new KeyValue(ivCitySprite.opacityProperty(), 0);
        KeyFrame kfDestroyExplosionFade = new KeyFrame(Duration.millis(2000), kvDestroyExplosionFade);
        
        tlDestroyExplosionFade.getKeyFrames().addAll(kfDestroyExplosionFade);
    }    
    
    
    boolean getAliveState()
    {
        return bAliveState;
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
