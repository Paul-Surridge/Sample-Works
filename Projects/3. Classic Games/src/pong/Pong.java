package pong;

import app.App;
import app.Scenes;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class Pong implements Initializable, Constants{

    //Populate and bring in the necessary objects
    @FXML
    private AnchorPane                      apPong;
    @FXML
    private AnchorPane                      apKeyboardControls;
    @FXML
    private Rectangle                       recPlayer1;
    @FXML
    private Rectangle                       recPlayer2;
    @FXML
    private Line                            linePlayer1GoalHit;
    @FXML
    private Line                            linePlayer2GoalHit;
    @FXML
    private Label                           labPlayer1Score;
    @FXML
    private Label                           labPlayer2Score;
    @FXML
    private Label                           labInGameMessage;
    @FXML
    private Button                          btnGameStart;
    @FXML
    private Button                          btnGameStop;
    @FXML
    private ImageView                       ivClosePong;
    @FXML
    private Slider                          sliBallSpeed;
    @FXML
    private Slider                          sliBatSize;
    @FXML
    private Slider                          sliBestOfAmount;
    @FXML
    private RadioButton                     radPracticeMode;
    @FXML
    private RadioButton                     radPlayMode;
    @FXML
    private Label                           labBallSpeed;
    @FXML
    private Label                           labBatSize;
    @FXML
    private Label                           labBestOfAmount;
    
    PongPlayer      objPongPlayer1, objPongPlayer2;
    Ball            objBall;
    ScoreManager    objScoreManager;
    Timeline        tlIntroMessage, tlPlayerWinMessage;

    String strPlayerWinMessage;

    int iGameState, iGameMode;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        initPongPlayers();
        initBall();

        initTimelines();
        initMisc();
        
        initGameParameters();
        
        apKeyboardControls.setVisible(true);
    }    
    
    
    
    private void initPongPlayers()
    {
        objPongPlayer1 = new PongPlayer(recPlayer1, linePlayer1GoalHit, 1);
        objPongPlayer2 = new PongPlayer(recPlayer2, linePlayer2GoalHit, 2);
        
        eventHandlerPlayers();
    }
    private void initBall()
    {
        objBall = new Ball(apPong);
        
        eventListnerBall();
    }
    private void initTimelines()
    {
        //Game Start Intro Message
        tlIntroMessage = new Timeline();        
        
        tlIntroMessage.getKeyFrames().add(new KeyFrame(Duration.millis(0),      new KeyValue(labInGameMessage.textProperty(),   "3")));
        tlIntroMessage.getKeyFrames().add(new KeyFrame(Duration.millis(1000),   new KeyValue(labInGameMessage.textProperty(),   "2")));
        tlIntroMessage.getKeyFrames().add(new KeyFrame(Duration.millis(2000),   new KeyValue(labInGameMessage.textProperty(),   "1")));
        tlIntroMessage.getKeyFrames().add(new KeyFrame(Duration.millis(3000),   new KeyValue(labInGameMessage.textProperty(),   "")));
        
        tlIntroMessage.setOnFinished((ActionEvent ae) -> {
            objBall.revealAndStartMoving();
            
            iGameState = GAME_RUNNING;
        });
        
        //Game Over Player Win Message
        tlPlayerWinMessage = new Timeline();
        
        tlPlayerWinMessage.getKeyFrames().add(new KeyFrame(Duration.millis(0),      new KeyValue(labInGameMessage.opacityProperty(),   0)));
        tlPlayerWinMessage.getKeyFrames().add(new KeyFrame(Duration.millis(500),    new KeyValue(labInGameMessage.opacityProperty(),   1)));
        tlPlayerWinMessage.getKeyFrames().add(new KeyFrame(Duration.millis(2500),   new KeyValue(labInGameMessage.opacityProperty(),   1)));
        tlPlayerWinMessage.getKeyFrames().add(new KeyFrame(Duration.millis(3000),   new KeyValue(labInGameMessage.opacityProperty(),   0)));
        tlPlayerWinMessage.getKeyFrames().add(new KeyFrame(Duration.millis(4000),   new KeyValue(labInGameMessage.opacityProperty(),   0)));
        
        tlPlayerWinMessage.setOnFinished((ActionEvent ae) -> {
            labInGameMessage.setOpacity(1);
            labInGameMessage.setText("Play Again?");
            
            activateGameProperties();
        });
    }
    private void initGameParameters()
    {
        //Initialise Best Of Amount...
        sliBestOfAmount.setValue(DEFAULT_BEST_OF_AMOUNT);
        labBestOfAmount.setText(Integer.toString(DEFAULT_BEST_OF_AMOUNT));
        objScoreManager.setGameWinAmount(DEFAULT_BEST_OF_AMOUNT - ( (DEFAULT_BEST_OF_AMOUNT - 1) / 2));
        
        //Initialise Ball Speed...
        sliBallSpeed.setValue(DEFAULT_BALL_SPEED);
        labBestOfAmount.setText(Integer.toString(DEFAULT_BALL_SPEED));
        objBall.setBallSpeed(DEFAULT_BALL_SPEED);       
        
        //Initialise Bat Size...
        sliBatSize.setValue(DEFAULT_BAT_SIZE);
        objPongPlayer1.setPlayerBatSize(DEFAULT_BAT_SIZE);
        objPongPlayer2.setPlayerBatSize(DEFAULT_BAT_SIZE);        
        labBatSize.setText(Integer.toString(DEFAULT_BAT_SIZE));
        
        //Initialise the Game Mode
        radPracticeMode.setSelected(true);
        iGameMode = PRACTICE_MODE;
    }
    private void initMisc()
    {
        objScoreManager = new ScoreManager(labPlayer1Score, labPlayer2Score);
        
        labInGameMessage.setText("");
        
        eventHandlerMouseEvents();
        eventListnersSlidersChange();
        eventListnersRadioButtonChange();
    }
    private void deinitialiseGame()
    {
        objBall.stopMoving();
        objBall.hide();
        objBall.resetPosition();

        activateGameProperties();
        
        objPongPlayer1.resetPlayerPosition();
        objPongPlayer2.resetPlayerPosition();
        
        objScoreManager.resetScores();
        labInGameMessage.setText("");
    }
    private void playerWin(int iPlayer)
    {
        labInGameMessage.setOpacity(0);
        labInGameMessage.setText("Player " + Integer.toString(iPlayer) + " Wins!");
          
        tlPlayerWinMessage.getCuePoints().clear();
        tlPlayerWinMessage.playFromStart();
    }
    private void deactivateGameProperties()
    {
        sliBallSpeed.setDisable(true);
        sliBatSize.setDisable(true);
        sliBestOfAmount.setDisable(true);
        
        radPlayMode.setDisable(true);
        radPracticeMode.setDisable(true);
    }
    private void activateGameProperties()
    {
        sliBallSpeed.setDisable(false);
        sliBatSize.setDisable(false);
        sliBestOfAmount.setDisable(false);
        
        radPlayMode.setDisable(false);
        radPracticeMode.setDisable(false);
    }

    
    
    //Event Handlers 
    private void eventListnersSlidersChange()
    {
        // - - - - Handlers - - - -
        sliBallSpeed.valueProperty().addListener((ObservableValue<? extends Number> ov, Number old_val, Number new_val) ->
        {
            labBallSpeed.setText(Integer.toString(new_val.intValue()));
            objBall.setBallSpeed(new_val.intValue());
        });        
        
        sliBatSize.valueProperty().addListener((ObservableValue<? extends Number> ov, Number old_val, Number new_val) ->
        {
            labBatSize.setText(Integer.toString(new_val.intValue()));
            objPongPlayer1.setPlayerBatSize(new_val.intValue());
            objPongPlayer2.setPlayerBatSize(new_val.intValue());
        });
        
        sliBestOfAmount.valueProperty().addListener((ObservableValue<? extends Number> ov, Number old_val, Number new_val) ->
        {
            if((new_val.intValue()%2) == 1) //Odd
            {
                labBestOfAmount.setText(Integer.toString(new_val.intValue()));
                objScoreManager.setGameWinAmount(new_val.intValue() - ( (new_val.intValue() - 1) / 2));
            }
        });
    }
    private void eventListnersRadioButtonChange()
    {
        // - - - - Handlers - - - -
        final ToggleGroup togGrpGameMode = radPracticeMode.getToggleGroup();
        
        togGrpGameMode.selectedToggleProperty().addListener((ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) ->
        {
            if (new_toggle == radPracticeMode) {
                iGameMode = PRACTICE_MODE;
            }
            else if (new_toggle == radPlayMode) {
                objBall.stopMoving();
                objBall.hide();
                objBall.resetPosition();
                
                iGameMode = PLAY_MODE;
            }
        });
    }
    private void eventHandlerMouseEvents()
    {
        // - - - - Handlers - - - -
        final EventHandler<MouseEvent>  ehKeyboardcontrolsClear = (MouseEvent me) ->
        {
            apKeyboardControls.setVisible(false);
            
            me.consume();
        };
        
        final EventHandler<MouseEvent>  ehGameStart = (MouseEvent me) ->
        {
            apKeyboardControls.setVisible(false);
            
            objPongPlayer1.resetPlayerPosition();
            objPongPlayer2.resetPlayerPosition();
            
            tlIntroMessage.playFromStart();
            
            objScoreManager.resetScores();
            
            deactivateGameProperties();
            
            me.consume();
        };
        
        final EventHandler<MouseEvent>  ehGameStop = (MouseEvent me) ->
        {
            objBall.stopMoving();
            objBall.hide();
            objBall.resetPosition();
            
            activateGameProperties();
            
            tlIntroMessage.stop();
            labInGameMessage.setText("");
        };
        
        final EventHandler<MouseEvent>  ehClosePong = (MouseEvent me) ->
        {
            try{
                deinitialiseGame();
                App.loadScene(Scenes.SPLASH);
            }
            catch(Exception ex){
                System.out.println(ex.toString());
            }
        };
        
        apKeyboardControls.addEventHandler(MouseEvent.MOUSE_CLICKED, ehKeyboardcontrolsClear);
        btnGameStart.addEventHandler(MouseEvent.MOUSE_CLICKED, ehGameStart);
        btnGameStop.addEventHandler(MouseEvent.MOUSE_CLICKED, ehGameStop);
        ivClosePong.addEventHandler(MouseEvent.MOUSE_CLICKED, ehClosePong);
    }
    private void eventHandlerPlayers()
    {
        // - - - - Handlers - - - -
        final EventHandler<KeyEvent>  ehKeyPressed = (KeyEvent ke) ->
        {
            switch(ke.getCode())
            {
                case A:  objPongPlayer1.moveUp();       break;
                case Z:  objPongPlayer1.moveDown();     break;
            }
            
            switch(ke.getCode())
            {
                case K:  objPongPlayer2.moveUp();       break;
                case M:  objPongPlayer2.moveDown();     break;
            }
            
            ke.consume();
        };
        
        final EventHandler<KeyEvent>  ehKeyReleased = (KeyEvent ke) ->
        {
            switch(ke.getCode())
            {
                case A:  objPongPlayer1.moveStopUp();       break;
                case Z:  objPongPlayer1.moveStopDown();     break;
            }
            
            switch(ke.getCode())
            {
                case K:  objPongPlayer2.moveStopUp();       break;
                case M:  objPongPlayer2.moveStopDown();     break;
            }
            
            ke.consume();
        };
        
        apPong.addEventHandler(KeyEvent.KEY_PRESSED, ehKeyPressed);
        apPong.addEventHandler(KeyEvent.KEY_RELEASED, ehKeyReleased);
    }
    private void eventListnerBall()
    {
        objBall.getTranslateXAsProperty().addListener(new ChangeListener()
        {
            @Override
            public void changed(ObservableValue o, Object oldVal, Object newVal)
            {
                if( (objBall.getBallHorizontalDirection() == LEFT) && (objBall.getTranslateXAsValue() <= -400) && (objPongPlayer1.getPlayerBottomYCoordinate() >= objBall.getTranslateYAsValue()) && (objPongPlayer1.getPlayerTopYCoordinate() <= objBall.getTranslateYAsValue()) )
                {
                    objBall.hitBoundary(LEFT_BOUNDARY, iGameMode, BALL_REFLECT);
                }
                else if( (objBall.getBallHorizontalDirection() == RIGHT) && (objBall.getTranslateXAsValue() >= 400) && (objPongPlayer2.getPlayerBottomYCoordinate() >= objBall.getTranslateYAsValue()) && (objPongPlayer2.getPlayerTopYCoordinate() <= objBall.getTranslateYAsValue()) )
                {
                    objBall.hitBoundary(RIGHT_BOUNDARY, iGameMode, BALL_REFLECT);
                }
                else if( (objBall.getBallHorizontalDirection() == LEFT) && (objBall.getTranslateXAsValue() <= (PLAYING_AREA_LEFT_X_COORDINATE + 5)) && (objBall.getLastHitBoundary() != LEFT_BOUNDARY) && (iGameState == GAME_RUNNING) )
                {
                    if(iGameMode == PRACTICE_MODE)
                    {
                        objBall.hitBoundary(LEFT_BOUNDARY, iGameMode, BALL_REFLECT);
                    }
                    else
                    {
                        iGameState = GAME_PAUSE;
                        
                        objBall.stopMoving();
                        objBall.hide();
                        objBall.resetPosition(); 

                        objPongPlayer2.runGoalHitIllumination();
                        objScoreManager.addPoint(PLAYER_2);

                        if(objScoreManager.getPlayerPoints(PLAYER_2) == objScoreManager.getGameWinAmount())     playerWin(PLAYER_2);
                        else                                                                                    tlIntroMessage.playFromStart();
                    }   
                }
                else if( (objBall.getBallHorizontalDirection() == RIGHT) && (objBall.getTranslateXAsValue() >= (PLAYING_AREA_RIGHT_X_COORDINATE - 5)) && (objBall.getLastHitBoundary() != RIGHT_BOUNDARY) && (iGameState == GAME_RUNNING) )
                {
                    if(iGameMode == PRACTICE_MODE)
                    {
                        objBall.hitBoundary(RIGHT_BOUNDARY, iGameMode, BALL_REFLECT);
                    }
                    else
                    {
                        iGameState = GAME_PAUSE;
                        
                        objBall.stopMoving();
                        objBall.hide();
                        objBall.resetPosition();

                        objPongPlayer1.runGoalHitIllumination();
                        objScoreManager.addPoint(PLAYER_1);

                        if(objScoreManager.getPlayerPoints(PLAYER_1) == objScoreManager.getGameWinAmount())     playerWin(PLAYER_1);
                        else                                                                                    tlIntroMessage.playFromStart();
                    }
                }
            }
        });
        
        objBall.getTranslateYAsProperty().addListener(new ChangeListener()
        {
            @Override
            public void changed(ObservableValue o, Object oldVal, Object newVal)
            {
                if( (objBall.getBallVerticalDirection() == DOWN) && (objBall.getTranslateYAsValue() >= (PLAYING_AREA_BOTTOM_Y_COORDINATE - 5)) )
                    objBall.hitBoundary(BOTTOM_BOUNDARY, iGameMode, BALL_REFLECT);
                if( (objBall.getBallVerticalDirection() == UP) && (objBall.getTranslateYAsValue() <= (PLAYING_AREA_TOP_Y_COORDINATE + 5)) )
                    objBall.hitBoundary(TOP_BOUNDARY, iGameMode, BALL_REFLECT);
            }
        });
    }
}
