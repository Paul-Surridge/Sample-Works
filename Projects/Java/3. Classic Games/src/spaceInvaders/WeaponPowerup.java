package spaceInvaders;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.scene.image.ImageView;

class WeaponPowerup implements Constants{

    public ImageView        ivPowerupSprite;
    
    private Random          objRandom;
    private Timer           timerGenerateAndShowPowerUp;
    private TimerTask       ttGenerateAndShowPowerUp;
            
    public int              iPowerupTypeOriginXPosition;
    private int             iPowerupType;

    WeaponPowerup(ImageView im)
    {
        ivPowerupSprite = im;
        ivPowerupSprite.setOpacity(0);

        objRandom = new Random();
    }
    
    
    private void generateAndShowNewPowerup()
    {
        iPowerupTypeOriginXPosition = (objRandom.nextInt(7) + 1) * 100;

        if(objRandom.nextInt(2) == 1)
        {
            ivPowerupSprite.setImage(imgCollectHPMissiles);
            iPowerupType = HPMISSILES;
        }
        else
        {
            ivPowerupSprite.setImage(imgCollectNuke);
            iPowerupType = NUKES;
        }

        ivPowerupSprite.setLayoutX(iPowerupTypeOriginXPosition);

        ivPowerupSprite.setOpacity(1);
    }
    
    
    int getPowerupType()
    {
        return iPowerupType;
    }    
    void start()
    {
        timerGenerateAndShowPowerUp = new Timer("Weapon PowerUp", true);
        ttGenerateAndShowPowerUp    = new TimerTask() {
            @Override
            public void run() {

                Platform.runLater(() -> {
                    generateAndShowNewPowerup();

                    timerGenerateAndShowPowerUp.cancel();
                });
            }
        };

        timerGenerateAndShowPowerUp.schedule(ttGenerateAndShowPowerUp, (objRandom.nextInt(5000) + 2000));
    }
    void collected()
    {
        ivPowerupSprite.setOpacity(0);
    }
    void stop()
    {
        ivPowerupSprite.setOpacity(0);
        timerGenerateAndShowPowerUp.cancel();
    }
}
