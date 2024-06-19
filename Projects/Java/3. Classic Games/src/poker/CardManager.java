package poker;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.scene.control.CheckBox;

class CardManager implements Constants {
      
    private PokerGame           objPokerGame;
    private CheckBox            chkShowComputerPlayerCards;
    
    private Player              Players[]                       = new Player[NUMBER_OF_PLAYERS + 1];
    private Integer             iDealtCards[][]                 = new Integer[NUMBER_OF_SUITS + 1][NUMBER_OF_CARDS_PER_SUIT + 1];
    private Integer             iCardLayoutCoordinates[][][]    = new Integer[NUMBER_OF_PLAYERS + 1][NUMBER_OF_CARDS_PER_HAND + 1][2];
    
    private Timer               timerComputerPlayersChangeCardsMacro;
    private TimerTask           ttFindNextEligibleComputerPlayerToHoldCards, ttRemoveComputerPlayersHeldCards, ttChangeComputerPlayersHeldCards;
    
    private Random              objRandom = new Random();
    
    private int iChangeCardsCurrentPlayerTurn, iNumberOfComputerPlayersInChangeCardsRound, iNumberOfComputerPlayersChangedCards;
    
    CardManager(PokerGame objPokerGame, Player Players[], CheckBox chkShowComputerPlayerCards) {

        this.objPokerGame = objPokerGame;
        this.Players = Players;
        this.chkShowComputerPlayerCards = chkShowComputerPlayerCards;
        
        declareCardPositions();
    }
    
    
    
    private void initTimerTasks()
    {
        /*
            It is only possible to interact with components in the .fxml file from the JavaFX Application Thread, therefore necessary to somehow place a Runnable on the
            JavaFX Application Thread (rather than running within its own thread).
        
            The TimerTask enables this given that the static Platform.runLater() method places the Runnable onto the Event Queue for the JavaFX Application thread,
            this is then run at the next available moment (which is normally instantly, should the JavaFX Application thread become idle).
         */
        
        ttFindNextEligibleComputerPlayerToHoldCards = new TimerTask() {
            @Override
            public void run() {
                
                //Run the passed Runnable on the JavaFX Application Thread at some unspecified time in the future.
                Platform.runLater(() -> {
                    //Show the players turn indicator and as it is a Computer Player have its AI show the cards that it has decided to hold
                    findNextPlayerToChangeCards();
                });
            }
        };
        ttRemoveComputerPlayersHeldCards = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    //Show blank on the cards which have not been held
                    Players[iChangeCardsCurrentPlayerTurn].removePlayerCardsNotHeld();
                });
            }
        };
        ttChangeComputerPlayersHeldCards = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    //Have the cards changed but show the back of the cards only...
                    playerChangeCards(iChangeCardsCurrentPlayerTurn);
                    
                    Players[iChangeCardsCurrentPlayerTurn].hidePlayerTurnIndicator();
                    
                    iNumberOfComputerPlayersChangedCards++;
                    
                    if(iNumberOfComputerPlayersInChangeCardsRound == iNumberOfComputerPlayersChangedCards)
                    {
                        timerComputerPlayersChangeCardsMacro.cancel();
                        
                        //All computer players still in the round have changed cards...
                        objPokerGame.stageComplete(CHANGE_CARDS_COMPLETE);
                    }
                });
            }
        };
    }
    private void declareCardPositions()
    {
        //In order to prevent Scene Builder from crashing have the cards created locally and added to the scene via the program
        iCardLayoutCoordinates[PLAYER_1][CARD_1][X_LAYOUT_COORDINATE] = PLAYER_1_CARD_1_LAYOUT_X_COORDINATE;
        iCardLayoutCoordinates[PLAYER_1][CARD_1][Y_LAYOUT_COORDINATE] = PLAYER_1_CARDS_LAYOUT_Y_COORDINATE;        
        iCardLayoutCoordinates[PLAYER_1][CARD_2][X_LAYOUT_COORDINATE] = PLAYER_1_CARD_2_LAYOUT_X_COORDINATE;
        iCardLayoutCoordinates[PLAYER_1][CARD_2][Y_LAYOUT_COORDINATE] = PLAYER_1_CARDS_LAYOUT_Y_COORDINATE;      
        iCardLayoutCoordinates[PLAYER_1][CARD_3][X_LAYOUT_COORDINATE] = PLAYER_1_CARD_3_LAYOUT_X_COORDINATE;
        iCardLayoutCoordinates[PLAYER_1][CARD_3][Y_LAYOUT_COORDINATE] = PLAYER_1_CARDS_LAYOUT_Y_COORDINATE;     
        iCardLayoutCoordinates[PLAYER_1][CARD_4][X_LAYOUT_COORDINATE] = PLAYER_1_CARD_4_LAYOUT_X_COORDINATE;
        iCardLayoutCoordinates[PLAYER_1][CARD_4][Y_LAYOUT_COORDINATE] = PLAYER_1_CARDS_LAYOUT_Y_COORDINATE;      
        iCardLayoutCoordinates[PLAYER_1][CARD_5][X_LAYOUT_COORDINATE] = PLAYER_1_CARD_5_LAYOUT_X_COORDINATE;
        iCardLayoutCoordinates[PLAYER_1][CARD_5][Y_LAYOUT_COORDINATE] = PLAYER_1_CARDS_LAYOUT_Y_COORDINATE;
        
        iCardLayoutCoordinates[PLAYER_2][CARD_1][X_LAYOUT_COORDINATE] = PLAYER_2_CARDS_LAYOUT_X_COORDINATE;
        iCardLayoutCoordinates[PLAYER_2][CARD_1][Y_LAYOUT_COORDINATE] = PLAYER_2_CARD_1_LAYOUT_Y_COORDINATE;        
        iCardLayoutCoordinates[PLAYER_2][CARD_2][X_LAYOUT_COORDINATE] = PLAYER_2_CARDS_LAYOUT_X_COORDINATE;
        iCardLayoutCoordinates[PLAYER_2][CARD_2][Y_LAYOUT_COORDINATE] = PLAYER_2_CARD_2_LAYOUT_Y_COORDINATE;      
        iCardLayoutCoordinates[PLAYER_2][CARD_3][X_LAYOUT_COORDINATE] = PLAYER_2_CARDS_LAYOUT_X_COORDINATE;
        iCardLayoutCoordinates[PLAYER_2][CARD_3][Y_LAYOUT_COORDINATE] = PLAYER_2_CARD_3_LAYOUT_Y_COORDINATE;     
        iCardLayoutCoordinates[PLAYER_2][CARD_4][X_LAYOUT_COORDINATE] = PLAYER_2_CARDS_LAYOUT_X_COORDINATE;
        iCardLayoutCoordinates[PLAYER_2][CARD_4][Y_LAYOUT_COORDINATE] = PLAYER_2_CARD_4_LAYOUT_Y_COORDINATE;      
        iCardLayoutCoordinates[PLAYER_2][CARD_5][X_LAYOUT_COORDINATE] = PLAYER_2_CARDS_LAYOUT_X_COORDINATE;
        iCardLayoutCoordinates[PLAYER_2][CARD_5][Y_LAYOUT_COORDINATE] = PLAYER_2_CARD_5_LAYOUT_Y_COORDINATE;
        
        iCardLayoutCoordinates[PLAYER_3][CARD_1][X_LAYOUT_COORDINATE] = PLAYER_3_CARD_1_LAYOUT_X_COORDINATE;
        iCardLayoutCoordinates[PLAYER_3][CARD_1][Y_LAYOUT_COORDINATE] = PLAYER_3_CARDS_LAYOUT_Y_COORDINATE;        
        iCardLayoutCoordinates[PLAYER_3][CARD_2][X_LAYOUT_COORDINATE] = PLAYER_3_CARD_2_LAYOUT_X_COORDINATE;
        iCardLayoutCoordinates[PLAYER_3][CARD_2][Y_LAYOUT_COORDINATE] = PLAYER_3_CARDS_LAYOUT_Y_COORDINATE;      
        iCardLayoutCoordinates[PLAYER_3][CARD_3][X_LAYOUT_COORDINATE] = PLAYER_3_CARD_3_LAYOUT_X_COORDINATE;
        iCardLayoutCoordinates[PLAYER_3][CARD_3][Y_LAYOUT_COORDINATE] = PLAYER_3_CARDS_LAYOUT_Y_COORDINATE;     
        iCardLayoutCoordinates[PLAYER_3][CARD_4][X_LAYOUT_COORDINATE] = PLAYER_3_CARD_4_LAYOUT_X_COORDINATE;
        iCardLayoutCoordinates[PLAYER_3][CARD_4][Y_LAYOUT_COORDINATE] = PLAYER_3_CARDS_LAYOUT_Y_COORDINATE;      
        iCardLayoutCoordinates[PLAYER_3][CARD_5][X_LAYOUT_COORDINATE] = PLAYER_3_CARD_5_LAYOUT_X_COORDINATE;
        iCardLayoutCoordinates[PLAYER_3][CARD_5][Y_LAYOUT_COORDINATE] = PLAYER_3_CARDS_LAYOUT_Y_COORDINATE;
        
        iCardLayoutCoordinates[PLAYER_4][CARD_1][X_LAYOUT_COORDINATE] = PLAYER_4_CARDS_LAYOUT_X_COORDINATE;
        iCardLayoutCoordinates[PLAYER_4][CARD_1][Y_LAYOUT_COORDINATE] = PLAYER_4_CARD_1_LAYOUT_Y_COORDINATE;        
        iCardLayoutCoordinates[PLAYER_4][CARD_2][X_LAYOUT_COORDINATE] = PLAYER_4_CARDS_LAYOUT_X_COORDINATE;
        iCardLayoutCoordinates[PLAYER_4][CARD_2][Y_LAYOUT_COORDINATE] = PLAYER_4_CARD_2_LAYOUT_Y_COORDINATE;      
        iCardLayoutCoordinates[PLAYER_4][CARD_3][X_LAYOUT_COORDINATE] = PLAYER_4_CARDS_LAYOUT_X_COORDINATE;
        iCardLayoutCoordinates[PLAYER_4][CARD_3][Y_LAYOUT_COORDINATE] = PLAYER_4_CARD_3_LAYOUT_Y_COORDINATE;     
        iCardLayoutCoordinates[PLAYER_4][CARD_4][X_LAYOUT_COORDINATE] = PLAYER_4_CARDS_LAYOUT_X_COORDINATE;
        iCardLayoutCoordinates[PLAYER_4][CARD_4][Y_LAYOUT_COORDINATE] = PLAYER_4_CARD_4_LAYOUT_Y_COORDINATE;      
        iCardLayoutCoordinates[PLAYER_4][CARD_5][X_LAYOUT_COORDINATE] = PLAYER_4_CARDS_LAYOUT_X_COORDINATE;
        iCardLayoutCoordinates[PLAYER_4][CARD_5][Y_LAYOUT_COORDINATE] = PLAYER_4_CARD_5_LAYOUT_Y_COORDINATE;
        
    }
    private int findNumberOfComputerPlayersInChangeCardsRound()
    {
        int iNumberOfComputerPlayersInChangeCardsRoundCalculated;
        
        iNumberOfComputerPlayersInChangeCardsRoundCalculated = RESET;
        
        for(int i = 2 ; i<=NUMBER_OF_PLAYERS ; i++)
            if(Players[i].getInRound())
                iNumberOfComputerPlayersInChangeCardsRoundCalculated++;
        
        return iNumberOfComputerPlayersInChangeCardsRoundCalculated;
    }
    private boolean cardHasBeenDealtBefore(int iRandomSuit, int iRandomValue)
    {        
        if(iDealtCards[iRandomSuit][iRandomValue] == CARD_NOT_DEALT)
        {
            iDealtCards[iRandomSuit][iRandomValue] = CARD_DEALT;
            
            return false;
        }
        
        return true;
    }
    private void findPlayerCardSuitAndValue(int iPlayer, int iCard)
    {
        int iRandomSuit, iRandomValue;
        
        do
        {
            iRandomSuit     = objRandom.nextInt(NUMBER_OF_SUITS) + 1;
            iRandomValue    = objRandom.nextInt(NUMBER_OF_CARDS_PER_SUIT - 1) + 2;      //2 - 14    No 2 - ACE

        }while(cardHasBeenDealtBefore(iRandomSuit, iRandomValue));
        
        Players[iPlayer].setCardSuit(iCard, iRandomSuit);
        Players[iPlayer].setCardValue(iCard, iRandomValue);
        //Store the cards intial Suit and Value...
        Players[iPlayer].setCardSuitStore(iCard, iRandomSuit);
        Players[iPlayer].setCardValueStore(iCard, iRandomValue);
        
        //Store the original locations...
        Players[iPlayer].setCardTablePosition(iCard, iCard);
    }
    private void findNextPlayerToChangeCards()
    {
        //Check if all players have seen the table...
        switch(findNextEligiblePlayerToChangeCards())
        {
            case(PLAYER_2):                     Players[PLAYER_2].participateChangeCardsRound();       break;
            case(PLAYER_3):                     Players[PLAYER_3].participateChangeCardsRound();       break;
            case(PLAYER_4):                     Players[PLAYER_4].participateChangeCardsRound();       break;
            case(ALL_PLAYERS_CHANGED_CARDS):    objPokerGame.stageComplete(CHANGE_CARDS_COMPLETE);     break;
        }
    }
    private int findNextEligiblePlayerToChangeCards()
    {
        for(int i = (iChangeCardsCurrentPlayerTurn + 1) ; i <= NUMBER_OF_PLAYERS ; i++)
            if(Players[i].getInRound())
            {
                iChangeCardsCurrentPlayerTurn = i;
                
                return iChangeCardsCurrentPlayerTurn;
            }

        return ALL_PLAYERS_CHANGED_CARDS;
    }
    
    
    
    void startChangeCardsRound()
    {
        iChangeCardsCurrentPlayerTurn = RESET;
        
        if(Players[PLAYER_1].getInRound())
        {
            iChangeCardsCurrentPlayerTurn = PLAYER_1;
            Players[iChangeCardsCurrentPlayerTurn].participateChangeCardsRound();
        }
        else
        {
            runComputerPlayersChangeCardsMacro();
        }
    }
    int getCardLayoutCoordinates(int iPlayerNumber, int iTablePosition, int xyPosition)
    {
        return iCardLayoutCoordinates[iPlayerNumber][iTablePosition][xyPosition];
    }
    void runComputerPlayersChangeCardsMacro()
    {
        initTimerTasks();
        
        timerComputerPlayersChangeCardsMacro = new Timer("timerComputerPlayersChangeCardsMacro", true);
        
        iNumberOfComputerPlayersInChangeCardsRound = findNumberOfComputerPlayersInChangeCardsRound();
        iNumberOfComputerPlayersChangedCards = RESET;
 
        timerComputerPlayersChangeCardsMacro.scheduleAtFixedRate(ttFindNextEligibleComputerPlayerToHoldCards, 1000, 2500);        
        timerComputerPlayersChangeCardsMacro.scheduleAtFixedRate(ttRemoveComputerPlayersHeldCards, 1500, 2500);
        timerComputerPlayersChangeCardsMacro.scheduleAtFixedRate(ttChangeComputerPlayersHeldCards, 2000, 2500);
    }
    void collectAndResetCards()
    {
        for(int i = 1 ; i <= NUMBER_OF_SUITS ; i++)
            for(int j = 1 ; j <= NUMBER_OF_CARDS_PER_SUIT ; j++)
                iDealtCards[i][j] = CARD_NOT_DEALT;
    }   
    void dealCardsToAllSolventPlayers()
    {
        //For all solvent players, deal/show the backs fo the cards...
        for(int i = 1 ; i <= NUMBER_OF_PLAYERS ; i++)
            if(Players[i].getInGame())
            {
                Players[i].dealCards();
                
                for(int j = 1 ; j <= NUMBER_OF_CARDS_PER_HAND ; j++)
                    findPlayerCardSuitAndValue(i, j);
            }
    }
    void playerChangeCards(int iPlayer)
    {
        //Cycle through all of the players cards and change all cards not held...
        for(int i = 1 ; i <= NUMBER_OF_CARDS_PER_HAND ; i++)
            if(Players[iPlayer].getCardHold(i) == CARD_HOLD_OFF)     //Then change cards
            {
                findPlayerCardSuitAndValue(iPlayer, i);
                
                if(iPlayer == PLAYER_1)                             Players[iPlayer].showCard(i);
                else if(chkShowComputerPlayerCards.isSelected())    Players[iPlayer].showCard(i);
                else                                                Players[iPlayer].setCardOpactity(i, VISIBLE);
            }
        
        Players[iPlayer].clearAllHoldIndicators();
    }
}
