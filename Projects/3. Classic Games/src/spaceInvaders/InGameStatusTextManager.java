package spaceInvaders;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.util.Duration;

class InGameStatusTextManager implements Constants{

    Timeline            tlGameStart, tlNextLevel;
    
    private Timeline    tlGameOver;
    private Label       labDisplayMessage, labGameOverMessage;
    private Button      btnPlayAgainYes, btnPlayAgainNo;

    InGameStatusTextManager(Label labDisplayMessage, Label labGameOverMessage, Button btnPlayAgainYes, Button btnPlayAgainNo)
    {
        this.labDisplayMessage  = labDisplayMessage;
        this.labGameOverMessage = labGameOverMessage;
        this.btnPlayAgainYes    = btnPlayAgainYes;
        this.btnPlayAgainNo     = btnPlayAgainNo;
        
        initTimelines();
    }
    private void initTimelines()
    {
        //Game Start Timeline
        tlGameStart = new Timeline();
        
        tlGameStart.getKeyFrames().add(new KeyFrame(Duration.millis(0),     new KeyValue(labDisplayMessage.textProperty(),    "Protect The Cities")));
        tlGameStart.getKeyFrames().add(new KeyFrame(Duration.millis(0),     new KeyValue(labDisplayMessage.opacityProperty(), 0)));
        tlGameStart.getKeyFrames().add(new KeyFrame(Duration.millis(1500),  new KeyValue(labDisplayMessage.opacityProperty(), 1)));
        tlGameStart.getKeyFrames().add(new KeyFrame(Duration.millis(2000),  new KeyValue(labDisplayMessage.opacityProperty(), 1)));
        tlGameStart.getKeyFrames().add(new KeyFrame(Duration.millis(2500),  new KeyValue(labDisplayMessage.opacityProperty(), 0)));
        tlGameStart.getKeyFrames().add(new KeyFrame(Duration.millis(2500),  new KeyValue(labDisplayMessage.textProperty(),    "Destroy The Alien Hoardes!")));
        tlGameStart.getKeyFrames().add(new KeyFrame(Duration.millis(2500),  new KeyValue(labDisplayMessage.opacityProperty(), 0)));
        tlGameStart.getKeyFrames().add(new KeyFrame(Duration.millis(3000),  new KeyValue(labDisplayMessage.opacityProperty(), 1)));
        tlGameStart.getKeyFrames().add(new KeyFrame(Duration.millis(3500),  new KeyValue(labDisplayMessage.opacityProperty(), 1)));
        tlGameStart.getKeyFrames().add(new KeyFrame(Duration.millis(4000),  new KeyValue(labDisplayMessage.opacityProperty(), 0)));
        
        tlGameStart.setCycleCount(1);
        
        //Next Level Timeline
        tlNextLevel = new Timeline();
        
        tlNextLevel.getKeyFrames().add(new KeyFrame(Duration.millis(0),     new KeyValue(labDisplayMessage.textProperty(),    "Level Complete")));
        tlNextLevel.getKeyFrames().add(new KeyFrame(Duration.millis(0),     new KeyValue(labDisplayMessage.opacityProperty(), 0)));
        tlNextLevel.getKeyFrames().add(new KeyFrame(Duration.millis(500),   new KeyValue(labDisplayMessage.opacityProperty(), 1)));
        tlNextLevel.getKeyFrames().add(new KeyFrame(Duration.millis(1000),  new KeyValue(labDisplayMessage.opacityProperty(), 1)));
        tlNextLevel.getKeyFrames().add(new KeyFrame(Duration.millis(1500),  new KeyValue(labDisplayMessage.opacityProperty(), 0)));
        tlNextLevel.getKeyFrames().add(new KeyFrame(Duration.millis(1500),  new KeyValue(labDisplayMessage.textProperty(),    "Next Level")));
        tlNextLevel.getKeyFrames().add(new KeyFrame(Duration.millis(1500),  new KeyValue(labDisplayMessage.opacityProperty(), 0)));
        tlNextLevel.getKeyFrames().add(new KeyFrame(Duration.millis(2000),  new KeyValue(labDisplayMessage.opacityProperty(), 1)));
        tlNextLevel.getKeyFrames().add(new KeyFrame(Duration.millis(2500),  new KeyValue(labDisplayMessage.opacityProperty(), 1)));
        tlNextLevel.getKeyFrames().add(new KeyFrame(Duration.millis(3000),  new KeyValue(labDisplayMessage.opacityProperty(), 0)));
        
        
        tlNextLevel.setCycleCount(1);
        
        //Gameover Timeline
        tlGameOver = new Timeline();

        tlGameOver.getKeyFrames().add(new KeyFrame(Duration.millis(0),      new KeyValue(labGameOverMessage.textProperty(),    "Game Over")));
        tlGameOver.getKeyFrames().add(new KeyFrame(Duration.millis(1000),   new KeyValue(labGameOverMessage.opacityProperty(), 0)));
        tlGameOver.getKeyFrames().add(new KeyFrame(Duration.millis(1500),   new KeyValue(labGameOverMessage.opacityProperty(), 1)));
        tlGameOver.getKeyFrames().add(new KeyFrame(Duration.millis(3500),   new KeyValue(labGameOverMessage.opacityProperty(), 1)));
        tlGameOver.getKeyFrames().add(new KeyFrame(Duration.millis(4000),   new KeyValue(labGameOverMessage.opacityProperty(), 0)));
        tlGameOver.getKeyFrames().add(new KeyFrame(Duration.millis(4000),   new KeyValue(labGameOverMessage.textProperty(),    "Press to play again or return to the main menu...")));
        tlGameOver.getKeyFrames().add(new KeyFrame(Duration.millis(4000),   new KeyValue(labGameOverMessage.opacityProperty(), 0)));
        tlGameOver.getKeyFrames().add(new KeyFrame(Duration.millis(4000),   new KeyValue(btnPlayAgainYes.opacityProperty(), 0)));
        tlGameOver.getKeyFrames().add(new KeyFrame(Duration.millis(4000),   new KeyValue(btnPlayAgainNo.opacityProperty(), 0)));
        tlGameOver.getKeyFrames().add(new KeyFrame(Duration.millis(4500),   new KeyValue(labGameOverMessage.opacityProperty(), 1)));
        tlGameOver.getKeyFrames().add(new KeyFrame(Duration.millis(4500),   new KeyValue(btnPlayAgainYes.opacityProperty(), 1)));
        tlGameOver.getKeyFrames().add(new KeyFrame(Duration.millis(4500),   new KeyValue(btnPlayAgainNo.opacityProperty(), 1)));
        
        tlGameOver.setCycleCount(1);
    }
    
    
    
    void runGameStart()
    {
        tlGameStart.playFromStart();
    }
    void runNextLevel(int iLevel)
    {                     
        tlNextLevel.playFromStart();
    }
    void runGameOver()
    { 
        tlGameOver.playFromStart();
    }
    void clearIntro()
    {
        labDisplayMessage.setText("");
    }
    void clearGameOver()
    {
        labGameOverMessage.setText("");
        labGameOverMessage.setOpacity(0);
    }
}
