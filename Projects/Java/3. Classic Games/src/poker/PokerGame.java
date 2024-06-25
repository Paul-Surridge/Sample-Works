package poker;

import app.App;
import app.Scenes;

import java.net.URL;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import static poker.Constants.BETTING_ROUND_2;

public class PokerGame implements Initializable, Constants {

    //Populate and bring in the necessary objects  
    @FXML
    private AnchorPane                      apPokerTable;
    @FXML
    private AnchorPane                      apGameSetup;
    @FXML
    private Label                           labInGameStatusFeed;
    @FXML
    private Label                           labPlayer1Name;
    @FXML
    private Label                           labPlayer2Name;
    @FXML
    private Label                           labPlayer3Name;
    @FXML
    private Label                           labPlayer4Name;
    @FXML
    private ImageView                       ivPlayer1CoinsIcon;
    @FXML
    private ImageView                       ivPlayer2CoinsIcon;
    @FXML
    private ImageView                       ivPlayer3CoinsIcon;
    @FXML
    private ImageView                       ivPlayer4CoinsIcon;
    @FXML
    private Rectangle                       recPlayer1TurnIndicator;
    @FXML
    private Rectangle                       recPlayer2TurnIndicator;
    @FXML
    private Rectangle                       recPlayer3TurnIndicator;
    @FXML
    private Rectangle                       recPlayer4TurnIndicator;
    @FXML
    private CheckBox                        chkShowComputerPlayerCards;
    @FXML
    private Button                          btnGameStart;
    @FXML
    private Button                          btnCheckOrSee;
    @FXML
    private Button                          btnRaise;
    @FXML
    private Button                          btnFold;
    @FXML
    private Button                          btnChangeCards;
    @FXML
    private ImageView                       ivHelp;     
    @FXML
    private ImageView                       ivClosePoker;
    @FXML
    private Slider                          sliGameStartAmount;
    @FXML
    private Slider                          sliAnteAmount;
    @FXML
    private TextField                       txtGameStartAmount;
    @FXML
    private TextField                       txtAnteAmount;
    @FXML
    private TextField                       txtBetAmount;
    @FXML
    private Label                           labPotAmount;
    @FXML
    private Label                           labBetAmountHeader;
    @FXML
    private Label                           labPlayer1Amount;
    @FXML
    private Label                           labPlayer2Amount;
    @FXML
    private Label                           labPlayer3Amount;
    @FXML
    private Label                           labPlayer4Amount;
    @FXML
    private Ellipse                         elPot;
    @FXML
    private Button                          btnNextRoundProceed;
    @FXML
    private Label                           labInGameStatusFeedPlayerWinOrExit;
    
            
    private GameSetupManager                objGameSetupManager;
    private CardManager                     objCardManager;
    private BettingRoundManager             objBettingRoundManager;
    private Player                          objPlayer1;
    private Player                          objPlayer2;
    private Player                          objPlayer3;
    private Player                          objPlayer4;
    private WinnerFinder                    objWinnerFinder;  
    private NextRoundManager                objNextRoundManager;

    private Player Players[] = new Player[NUMBER_OF_PLAYERS + 1];
    
    private Timer       timerDealCards, timerChangeCards, timerBettingRound2, timerShowCardsAndFindWinner;
    
    private TimerTask   ttDealCards,
                        ttShowPlayer1Cards,
                        ttAllPlayersAnte,
                        ttBettingRound1,
                        ttChangeCards,
                        ttBettingRound2,
                        ttShowCards,
                        ttFindWinner;
    
    private Integer iPlayersToLeaveGame[] = new Integer[NUMBER_OF_PLAYERS + 1];

    private int iNumberOfPlayersLeavingGame;
    
    InGameStatusFeedManager objInGameStatusFeedManager;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        objInGameStatusFeedManager      = new InGameStatusFeedManager(labInGameStatusFeed, labInGameStatusFeedPlayerWinOrExit);
        objGameSetupManager             = new GameSetupManager(this, apGameSetup, txtGameStartAmount, sliGameStartAmount, txtAnteAmount, sliAnteAmount, btnGameStart);
        objNextRoundManager             = new NextRoundManager(this, objGameSetupManager, Players, btnNextRoundProceed);  
        objBettingRoundManager          = new BettingRoundManager(this, Players, labPotAmount);
        objCardManager                  = new CardManager(this, Players, chkShowComputerPlayerCards);
        objPlayer1                      = new Player(objCardManager, objBettingRoundManager, apPokerTable, PLAYER_1, labPlayer1Amount, labPlayer1Name,
                                                     ivPlayer1CoinsIcon, recPlayer1TurnIndicator, labInGameStatusFeed, labBetAmountHeader, txtBetAmount,
                                                     btnCheckOrSee, btnRaise, btnFold, btnChangeCards, labPotAmount, elPot);    
        objPlayer2                      = new Player(objCardManager, objBettingRoundManager, apPokerTable, PLAYER_2, labPlayer2Amount, labPlayer2Name, ivPlayer2CoinsIcon, recPlayer2TurnIndicator);
        objPlayer3                      = new Player(objCardManager, objBettingRoundManager, apPokerTable, PLAYER_3, labPlayer3Amount, labPlayer3Name, ivPlayer3CoinsIcon, recPlayer3TurnIndicator);
        objPlayer4                      = new Player(objCardManager, objBettingRoundManager, apPokerTable, PLAYER_4, labPlayer4Amount, labPlayer4Name, ivPlayer4CoinsIcon, recPlayer4TurnIndicator);
        
        objWinnerFinder                 = new WinnerFinder(Players);
       
        Players[PLAYER_1]               = objPlayer1;
        Players[PLAYER_2]               = objPlayer2;
        Players[PLAYER_3]               = objPlayer3;
        Players[PLAYER_4]               = objPlayer4;
        
        eventHandlerMouseEvents();
        
        objPlayer1.hidePlayerGameControlObjects(IMMEDIATE);
        
        initTimerTasks();
        
        objGameSetupManager.showSetupObjects();
    }
    
    /*
        1. GameSetupManager:        objPokerGame.initialiseNewGame()
        2. PokerGame:               stageComplete(NEW_GAME_INITIALISATION_COMPLETE)
        3. PokerGame:               runDealCardsToBettingRound1Macro()
        4. BettingRoundManager:     stageComplete(BETTING_ROUND_ONE_COMPLETE)
        5. PokerGame:               runChangeCardsMacro()
        6. CardManager:             objPokerGame.stageComplete(CHANGE_CARDS_COMPLETE);
        7. PokerGame:               runBettingRound2Macro()
    */
    
    private void initTimerTasks()
    {
        /*
            It is only possible to interact with components in the .fxml file from the JavaFX Application Thread, therefore necessary to somehow place a Runnable on the
            JavaFX Application Thread (rather than running within its own thread).
        
            The TimerTask enables this given that the static Platform.runLater() method places the Runnable onto the Event Queue for the JavaFX Application thread,
            this is then run at the next available moment (which is normally instantly, should the JavaFX Application thread become idle).
         */
        
        //Deal Cards to Betting Round 1 Macro
        ttDealCards = new TimerTask() {
            @Override
            public void run() {
                //Run the passed Runnable on the JavaFX Application Thread at some unspecified time in the future.
                Platform.runLater(() -> {
                    objInGameStatusFeedManager.showMessage("Deal Cards");
                    objCardManager.collectAndResetCards();
                    objCardManager.dealCardsToAllSolventPlayers();
                    
                    Players[PLAYER_1].showCards();
                });
            }
        };
        ttShowPlayer1Cards = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    Players[PLAYER_1].showCards(); 
                    
                    if(chkShowComputerPlayerCards.isSelected())
                    {
                        Players[PLAYER_2].showCards();
                        Players[PLAYER_3].showCards();
                        Players[PLAYER_4].showCards();
                    }
                });
            }
        };
        ttAllPlayersAnte = new TimerTask() {            
            @Override
            public void run() {
                Platform.runLater
                (() -> {
                    allSolventPlayersAnte();
                });
            }
        };
        ttBettingRound1 = new TimerTask() {            
            @Override
            public void run() {
                Platform.runLater
                (() -> {
                    objInGameStatusFeedManager.showMessage("Betting Round 1 - Check, fold or enter raise amount");
                    objBettingRoundManager.startNewBettingRound(BETTING_ROUND_1);
                });
            }
        };
        ttChangeCards = new TimerTask() {            
            @Override
            public void run() {
                Platform.runLater
                (() -> {
                    objInGameStatusFeedManager.showMessage("Change Cards");
                    objCardManager.startChangeCardsRound();
                });
            }
        };
        ttBettingRound2 = new TimerTask() {            
            @Override
            public void run() {
                Platform.runLater
                (() -> {
                    objInGameStatusFeedManager.showMessage("Betting Round 2 - Check, fold or enter raise amount");
                    objBettingRoundManager.startNewBettingRound(BETTING_ROUND_2);
                });
            }
        };
        ttShowCards = new TimerTask() {            
            @Override
            public void run() {
                Platform.runLater
                (() -> {
                    showCards();
                });
            }
        };
        ttFindWinner = new TimerTask() {            
            @Override
            public void run() {
                Platform.runLater
                (() -> {
                    findWinner();
                });
            }
        };
    }
    private void runDealCardsToBettingRound1Macro()
    {
        initTimerTasks();   //TimerTasks can only be used once, therefore need to reinitialised each round
        timerDealCards = new Timer("timerDealCards", true);
        timerDealCards.schedule(ttDealCards,            500);        
        timerDealCards.schedule(ttShowPlayer1Cards,     1500);
        timerDealCards.schedule(ttAllPlayersAnte,       2500);
        timerDealCards.schedule(ttBettingRound1,        4000);
    }
    private void runChangeCardsMacro()
    {
        timerChangeCards = new Timer("timerChangeCards", true);
        timerChangeCards.schedule(ttChangeCards,        1500);
    }
    private void runBettingRound2Macro()
    {
        timerBettingRound2 = new Timer("timerBettingRound2", true);
        timerBettingRound2.schedule(ttBettingRound2,    1500);
    }
    private void runShowCardsAndFindWinnerMacro()
    {
        timerShowCardsAndFindWinner = new Timer("timerShowCardsAndFindWinner", true);
        timerShowCardsAndFindWinner.schedule(ttShowCards,       2000);
        timerShowCardsAndFindWinner.schedule(ttFindWinner,      3000);
    }                
    private void showCards()
    {
        for(int i = 1 ; i <= NUMBER_OF_PLAYERS ; i++)
            Players[i].showCards();
    }
    private void findWinner()
    {
        playerHasWonRound(objWinnerFinder.findWinnerOfRound(Players), HIGHEST_HAND);
    }
    private void playerWinsGame()
    {
        for(int i = 1 ; i<=NUMBER_OF_PLAYERS ; i++)
            if(Players[i].getInGame())
                objInGameStatusFeedManager.showWinGameMessage("Player " + Integer.toString(i) + " Wins Game!");
    }
    private boolean checkForAnySingleRemainingPlayersInGame()
    {
        int iNumberOfPlayersInGame;
        
        iNumberOfPlayersInGame = RESET;
        
        for(int i = 1 ; i<=NUMBER_OF_PLAYERS ; i++)
            if(Players[i].getInGame())
                iNumberOfPlayersInGame++;
        
        return (iNumberOfPlayersInGame == 1);
    }
    private void checkCanAllPlayersStillAnte()
    {
        iNumberOfPlayersLeavingGame = RESET;
        
        for(int i = 1; i<=NUMBER_OF_PLAYERS; i++)
        {
            iPlayersToLeaveGame[i] = RESET;
            
            if( (Players[i].getAmount() < objGameSetupManager.getAnteAmount()) && (Players[i].getInGame()) )
            {
                //Player insolvent, exits game
                Players[i].setInGame(false);
                Players[i].hideTableObjects();
                
                iNumberOfPlayersLeavingGame++;
                
                iPlayersToLeaveGame[i] = PLAYER_LEAVES_GAME;
            }
        }
    }
    private void showAnyPlayersExitingGame()
    {
        if(iNumberOfPlayersLeavingGame != RESET)
            objInGameStatusFeedManager.showPlayerExitMessage("Player " + findPlayersToExitGame() + " Exits Game");
    }
    private String findPlayersToExitGame()
    {
        String sBuildString = new String();
        boolean bFirstPlayerFound;
        
        bFirstPlayerFound = false;
        
        for(int i = 1 ; i<=NUMBER_OF_PLAYERS ; i++)
        {
            if( (iPlayersToLeaveGame[i] == PLAYER_LEAVES_GAME) && (!bFirstPlayerFound) )
            {
                sBuildString = Integer.toString(i);
                
                bFirstPlayerFound = true;
            }
            else if(iPlayersToLeaveGame[i] == PLAYER_LEAVES_GAME)
            {
                sBuildString = sBuildString.concat(" + " + Integer.toString(i));
            }
        }

        return sBuildString;
    }
    private void allSolventPlayersAnte()
    {    
        objInGameStatusFeedManager.showMessage("All Players Ante");

        for(int i = 1; i<=NUMBER_OF_PLAYERS; i++)
            if(Players[i].getInGame() == true)
            {
                Players[i].ante(objGameSetupManager.getAnteAmount());
                
                objBettingRoundManager.addPotAmount(objGameSetupManager.getAnteAmount());
            }
    }    
    private String formatAmount(int iAmount)
    {
        return NumberFormat.getInstance(Locale.UK).format((double)iAmount);
    }
    
    
    
    void initialiseNewGame()
    {
        objInGameStatusFeedManager.hidePlayerWinOrExitMessage();

        Players[PLAYER_1].showPlayerGameControlObjects(FADE);
        
        //Ensure all parameters are set as required for a new game...
        for(int i = 1 ; i <= NUMBER_OF_PLAYERS ; i++)
        {
            Players[i].initialiseNewGame();
            Players[i].setPlayerAmount(objGameSetupManager.getGameStartAmount()); 
            
            Players[i].showTableObjects();
        }

        objBettingRoundManager.resetPotAmount();

        //Notify central class that this particular stage has completed
        stageComplete(NEW_GAME_INITIALISATION_COMPLETE);
    }
    void initialiseNewRound()
    {
        objInGameStatusFeedManager.hidePlayerWinOrExitMessage();
        
        //Ensure all cards are hidden regardless...
        for(int i = 1 ; i <= NUMBER_OF_PLAYERS ; i++)
            Players[i].hideCards();
        
        //Check and initialise all players that are still in the game
        for(int i = 1 ; i <= NUMBER_OF_PLAYERS ; i++)
            if(Players[i].getInGame())
                Players[i].initialiseNewRound();

        runDealCardsToBettingRound1Macro();
    }
    void stageComplete(int iStage)
    {
        //Stage 1:        Cards dealt to all solvent players
        //Stage 2:        All solvent players ante
        //Stage 3:        Betting Round 1
        //Stage 4:        All players still in game, change cards
        //Stage 5:        Betting Round 2
        //Stage 6:        Show cards
        //Stage 7:        Find winner and update players winnings
        //Stage 8:        Check for any insolvent or players who can not ante
        
        switch(iStage)
        {
            case(NEW_GAME_INITIALISATION_COMPLETE):
            {
                runDealCardsToBettingRound1Macro();
                
                break;
            }
            case(BETTING_ROUND_ONE_COMPLETE):
            {
                objInGameStatusFeedManager.showMessage("Betting Round 1 Complete");
                
                runChangeCardsMacro();  
                
                break;
            }
            case(CHANGE_CARDS_COMPLETE):
            {
                objInGameStatusFeedManager.showMessage("Change Cards Complete");
                
                runBettingRound2Macro();
                
                break;
            }
            case(BETTING_ROUND_TWO_COMPLETE):
            {
                objInGameStatusFeedManager.showMessage("Betting Round 2 Complete");
           
                runShowCardsAndFindWinnerMacro();
                
                break;
            }
        }
    }
    void playerHasWonRound(int iWinner, int iMethod)
    {
        if(iWinner == GAME_TIE)
        {
            objInGameStatusFeedManager.showMessage("Game Tie - Play Again");
            
            for(int i = 1 ; i<=NUMBER_OF_PLAYERS ; i++)
                if(Players[i].getInGame())
                {
                    //Refund the amount the player has placed into the pot this round
                    Players[i].addPlayerAmount(Players[i].getPlayerBetInTotalAmount());
                    
                    Players[i].addPlayerAmount(objGameSetupManager.getAnteAmount());
                }
        }
        else
        {
            //Add pot amount to the winning player amount
            Players[iWinner].addPlayerAmount(objBettingRoundManager.getPotAmount());

            //Once a round is complete, check to see if we have an overall winner, which players are still solvent (that can ante) if not then start a new round

            //Now show the winning cards in the winning players hand
            switch(iMethod)
            {
                case(HIGHEST_HAND):
                {
                    for(int i = 1 ; i<=NUMBER_OF_PLAYERS ; i++)
                        if(i == iWinner)                    Players[iWinner].showWinningCards();
                        else if(Players[i].getInGame())     Players[i].coverAllCards();
                    
                    break;
                }
                case(ALL_OTHER_PLAYERS_FOLDED):
                {
                    showCards();
                    
                    for(int i = 1 ; i<=NUMBER_OF_PLAYERS ; i++)
                        if( (i != iWinner) && (Players[i].getInGame()) )
                            Players[i].coverAllCards();
                    
                    break;
                }
            }

            objInGameStatusFeedManager.showWinRoundMessage(Players[iWinner], formatAmount(objBettingRoundManager.getPotAmount()), iMethod);
        }
        
        if(iWinner != GAME_TIE)
        {
            //Check to see if any players have less than Ante amount, if so they leave the game.
            checkCanAllPlayersStillAnte();
                
            if(checkForAnySingleRemainingPlayersInGame())
            {
                playerWinsGame();
                
                objNextRoundManager.showNextGame();
            }
            else
            {
                showAnyPlayersExitingGame();
                
                objNextRoundManager.showNextRound();
            }           
        }
        
        objBettingRoundManager.resetPotAmount();
    }
    
    
    
    private void eventHandlerMouseEvents()
    {
        // - - - - Handlers - - - -
        final EventHandler<MouseEvent>  ehClosePoker = (MouseEvent me) -> {
            try{
                App.loadScene(Scenes.SPLASH);
            }
            catch(Exception ex){
                System.out.println(ex.toString());
            }
        };

        ivClosePoker.addEventHandler(MouseEvent.MOUSE_CLICKED, ehClosePoker);
    }
}
