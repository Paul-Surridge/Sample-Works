package missileCommand;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.util.Duration;

class InGameStatusTextManager implements Constants{

    private MissileCommand      objMissileCommand;
    private Timeline            tlDisplayMessageIntro, tlDisplayMessageNextLevel, tlDisplayMessageGameOver;
    private Label               labDisplayOutput, labGameOverMessage;
    
    InGameStatusTextManager(MissileCommand objMissileCommand, Label labDisplayOutput, Label labGameOverMessage, Button btnPlayAgainYes, Button btnPlayAgainNo)
    {
        this.objMissileCommand      = objMissileCommand;
        this.labDisplayOutput       = labDisplayOutput;
        this.labGameOverMessage     = labGameOverMessage;
        
        //Intro Message Timeline
        tlDisplayMessageIntro = new Timeline();
        tlDisplayMessageIntro.getKeyFrames().add(new KeyFrame(Duration.millis(0),           new KeyValue(labDisplayOutput.textProperty(), "Protect The Cities")));
        tlDisplayMessageIntro.getKeyFrames().add(new KeyFrame(Duration.millis(0),           new KeyValue(labDisplayOutput.opacityProperty(), 0)));
        tlDisplayMessageIntro.getKeyFrames().add(new KeyFrame(Duration.millis(4500),        new KeyValue(btnPlayAgainYes.disableProperty(), true)));
        tlDisplayMessageIntro.getKeyFrames().add(new KeyFrame(Duration.millis(4500),        new KeyValue(btnPlayAgainNo.disableProperty(), true)));
        tlDisplayMessageIntro.getKeyFrames().add(new KeyFrame(Duration.millis(1500),        new KeyValue(labDisplayOutput.opacityProperty(), 1)));
        tlDisplayMessageIntro.getKeyFrames().add(new KeyFrame(Duration.millis(2000),        new KeyValue(labDisplayOutput.opacityProperty(), 1)));
        tlDisplayMessageIntro.getKeyFrames().add(new KeyFrame(Duration.millis(2500),        new KeyValue(labDisplayOutput.opacityProperty(), 0)));
        tlDisplayMessageIntro.getKeyFrames().add(new KeyFrame(Duration.millis(2500),        new KeyValue(labDisplayOutput.textProperty(), "Destroy The Alien Missiles!")));
        tlDisplayMessageIntro.getKeyFrames().add(new KeyFrame(Duration.millis(2500),        new KeyValue(labDisplayOutput.opacityProperty(), 0)));
        tlDisplayMessageIntro.getKeyFrames().add(new KeyFrame(Duration.millis(3000),        new KeyValue(labDisplayOutput.opacityProperty(), 1)));
        tlDisplayMessageIntro.getKeyFrames().add(new KeyFrame(Duration.millis(3500),        new KeyValue(labDisplayOutput.opacityProperty(), 1)));
        tlDisplayMessageIntro.getKeyFrames().add(new KeyFrame(Duration.millis(4000),        new KeyValue(labDisplayOutput.opacityProperty(), 0)));
        tlDisplayMessageIntro.setCycleCount(1);
        
        tlDisplayMessageIntro.setOnFinished((ActionEvent ae) -> {
            objMissileCommand.newGame();
        });
        
        //Next Level Timeline
        tlDisplayMessageNextLevel = new Timeline();
        
        //Gameover Message Timeline Definition
        tlDisplayMessageGameOver = new Timeline();
        tlDisplayMessageGameOver.getKeyFrames().add(new KeyFrame(Duration.millis(0),            new KeyValue(labGameOverMessage.textProperty(), "Game Over")));
        tlDisplayMessageGameOver.getKeyFrames().add(new KeyFrame(Duration.millis(1000),         new KeyValue(labGameOverMessage.opacityProperty(), 0)));
        tlDisplayMessageGameOver.getKeyFrames().add(new KeyFrame(Duration.millis(1500),         new KeyValue(labGameOverMessage.opacityProperty(), 1)));
        tlDisplayMessageGameOver.getKeyFrames().add(new KeyFrame(Duration.millis(3500),         new KeyValue(labGameOverMessage.opacityProperty(), 1)));
        tlDisplayMessageGameOver.getKeyFrames().add(new KeyFrame(Duration.millis(4000),         new KeyValue(labGameOverMessage.opacityProperty(), 0)));
        tlDisplayMessageGameOver.getKeyFrames().add(new KeyFrame(Duration.millis(4000),         new KeyValue(labGameOverMessage.textProperty(), "Press to play again or return to the main menu...")));
        tlDisplayMessageGameOver.getKeyFrames().add(new KeyFrame(Duration.millis(4000),         new KeyValue(labGameOverMessage.opacityProperty(), 0)));
        tlDisplayMessageGameOver.getKeyFrames().add(new KeyFrame(Duration.millis(4000),         new KeyValue(btnPlayAgainYes.opacityProperty(), 0)));
        tlDisplayMessageGameOver.getKeyFrames().add(new KeyFrame(Duration.millis(4000),         new KeyValue(btnPlayAgainNo.opacityProperty(), 0)));
        tlDisplayMessageGameOver.getKeyFrames().add(new KeyFrame(Duration.millis(4500),         new KeyValue(labGameOverMessage.opacityProperty(), 1)));
        tlDisplayMessageGameOver.getKeyFrames().add(new KeyFrame(Duration.millis(4500),         new KeyValue(btnPlayAgainYes.opacityProperty(), 1)));
        tlDisplayMessageGameOver.getKeyFrames().add(new KeyFrame(Duration.millis(4500),         new KeyValue(btnPlayAgainNo.opacityProperty(), 1)));
        tlDisplayMessageGameOver.getKeyFrames().add(new KeyFrame(Duration.millis(4500),         new KeyValue(btnPlayAgainYes.disableProperty(), false)));
        tlDisplayMessageGameOver.getKeyFrames().add(new KeyFrame(Duration.millis(4500),         new KeyValue(btnPlayAgainNo.disableProperty(), false)));
        
        tlDisplayMessageGameOver.setCycleCount(1);
    }

    
    
    
    void runGameStartIntroMessage()
    {
        tlDisplayMessageIntro.playFromStart();
    }
    void runLevelIntroMessage(int iLevel)
    {
        tlDisplayMessageNextLevel = new Timeline();
        
        labDisplayOutput.setText("Level " + Integer.toString(iLevel));
        
        tlDisplayMessageNextLevel.getKeyFrames().add(new KeyFrame(Duration.millis(0),           new KeyValue(labDisplayOutput.opacityProperty(), 0)));
        tlDisplayMessageNextLevel.getKeyFrames().add(new KeyFrame(Duration.millis(500),         new KeyValue(labDisplayOutput.opacityProperty(), 1)));
        tlDisplayMessageNextLevel.getKeyFrames().add(new KeyFrame(Duration.millis(1000),        new KeyValue(labDisplayOutput.opacityProperty(), 1)));
        tlDisplayMessageNextLevel.getKeyFrames().add(new KeyFrame(Duration.millis(1500),        new KeyValue(labDisplayOutput.opacityProperty(), 0))); 
        tlDisplayMessageNextLevel.setCycleCount(1);
        
        tlDisplayMessageNextLevel.setOnFinished((ActionEvent ae) -> {
            objMissileCommand.showCurrentLevel();
        });
        
        tlDisplayMessageNextLevel.playFromStart();
    }
    void runGameoverMessage()
    { 
        tlDisplayMessageGameOver.playFromStart();
    }
}
