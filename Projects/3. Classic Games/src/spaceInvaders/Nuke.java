package spaceInvaders;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

class Nuke implements Constants{

    private AnchorPane  apSpaceInvaders;
    private Circle      cirPlayerMissileSprite;
   
    private Timeline    tlNukeExplosion = new Timeline();

    Nuke(AnchorPane apSpaceInvaders)
    { 
        this.apSpaceInvaders = apSpaceInvaders;
        
        cirPlayerMissileSprite = new Circle(0, 0, 3, Paint.valueOf("#ffffff"));

        this.apSpaceInvaders.getChildren().add(cirPlayerMissileSprite);
        
        initTimelines();
    }
    
    
    private void initTimelines()
    {
        tlNukeExplosion.setCycleCount(1);
        
        tlNukeExplosion.getKeyFrames().add(new KeyFrame(Duration.millis(0),     new KeyValue(cirPlayerMissileSprite.radiusProperty(),   0)));
        tlNukeExplosion.getKeyFrames().add(new KeyFrame(Duration.millis(0),     new KeyValue(cirPlayerMissileSprite.fillProperty(),     Color.WHITE)));
        tlNukeExplosion.getKeyFrames().add(new KeyFrame(Duration.millis(200),   new KeyValue(cirPlayerMissileSprite.radiusProperty(),   80)));
        tlNukeExplosion.getKeyFrames().add(new KeyFrame(Duration.millis(450),   new KeyValue(cirPlayerMissileSprite.fillProperty(),     Color.YELLOW)));
        tlNukeExplosion.getKeyFrames().add(new KeyFrame(Duration.millis(700),   new KeyValue(cirPlayerMissileSprite.fillProperty(),     Color.RED)));
        tlNukeExplosion.getKeyFrames().add(new KeyFrame(Duration.millis(700),   new KeyValue(cirPlayerMissileSprite.radiusProperty(),   0)));
    }
    
    
    void explodeNuke(double dTargetXCoordinate, double dTargetYCoordinate)
    {       
        cirPlayerMissileSprite.setVisible(true);
        cirPlayerMissileSprite.setRadius(3);
        cirPlayerMissileSprite.setFill(Color.WHITE);
           
        cirPlayerMissileSprite.setCenterX(dTargetXCoordinate);
        cirPlayerMissileSprite.setCenterY(dTargetYCoordinate + 100);
        
        tlNukeExplosion.playFromStart();
    }
}
