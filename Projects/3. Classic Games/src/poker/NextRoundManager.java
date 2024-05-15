package poker;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

class NextRoundManager implements Constants{

    private PokerGame           objPokerGame;
    private GameSetupManager    objGameSetupManager;
    private Player              Players[];
    private Button              btnNextRoundProceed;
    private Timeline            tlShowNextRound;
    
    private int iNewGameOrRound;
    
    NextRoundManager(PokerGame objPokerGame, GameSetupManager objGameSetupManager, Player Players[], Button btnNextRoundProceed) {
        
        this.objPokerGame               = objPokerGame;
        this.btnNextRoundProceed        = btnNextRoundProceed;
        this.objGameSetupManager        = objGameSetupManager;
        this.Players                    = Players;

        iNewGameOrRound = RESET;
        
        hideNextRound();
        
        initTimelines();
        
        eventHandlerMouseEvents();
    }
    
    
    private void initTimelines()
    {
        //Show the Player Game Control Objects
        
        tlShowNextRound = new Timeline();
        tlShowNextRound.setCycleCount(1);
        
        tlShowNextRound.getKeyFrames().add(new KeyFrame(Duration.millis(3000),       new KeyValue(btnNextRoundProceed.visibleProperty(), true)));
        tlShowNextRound.getKeyFrames().add(new KeyFrame(Duration.millis(3000),       new KeyValue(btnNextRoundProceed.opacityProperty(), NOT_VISIBLE)));
        tlShowNextRound.getKeyFrames().add(new KeyFrame(Duration.millis(3500),       new KeyValue(btnNextRoundProceed.opacityProperty(), VISIBLE)));
    }
    private void hideNextRound()
    {
        btnNextRoundProceed.setDisable(true);
        btnNextRoundProceed.setOpacity(NOT_VISIBLE);
        btnNextRoundProceed.setVisible(false);
    }
    
    
    void showNextRound()
    {
        iNewGameOrRound = NEXT_ROUND;
        
        btnNextRoundProceed.setDisable(false);
        btnNextRoundProceed.setText("Next Round");
        tlShowNextRound.playFromStart();
    }
    void showNextGame()
    {
        iNewGameOrRound = NEW_GAME;
        
        btnNextRoundProceed.setDisable(false);
        btnNextRoundProceed.setText("New Game");
        tlShowNextRound.playFromStart();
    }
    
    
    private void eventHandlerMouseEvents()
    {
        final EventHandler<MouseEvent>  ehNextRoundProceed = new EventHandler<MouseEvent> ()
        {
            @Override
            public void handle(MouseEvent me)
            {
                hideNextRound();
                
                switch(iNewGameOrRound)
                {
                    case(NEXT_ROUND):
                    {
                        objPokerGame.initialiseNewRound();
                        
                        break;
                    }
                    case(NEW_GAME):
                    {
                        for(int i = 1 ; i<=NUMBER_OF_PLAYERS ; i++)
                            Players[i].hideCards();
                        
                        objGameSetupManager.showSetupObjects();
                        
                        break;
                    }
                }

                me.consume();
            }
        };
      
        btnNextRoundProceed.addEventHandler(MouseEvent.MOUSE_CLICKED, ehNextRoundProceed);    
    }
}
