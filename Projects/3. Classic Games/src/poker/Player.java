package poker;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

class Player implements Constants{

    private Timeline    tlShowPlayerGameControlObjects,        //Revealing the player game control objects
                        tlHidePlayerGameControlObjects,        //Hiding the player game control objects
                        tlShowDealtCards,
                        tlShowTableObjects,
                        tlHideTableObjects,
                        tlShowPlayerTurnIndicator,
                        tlHidePlayerTurnIndicator;
    
    private Timer       tiComputerPlayerBettingRoundActionComplete;

    private CardManager         objCardManager;
    private BettingRoundManager objBettingRoundManager;
    private Label               labInGameStatusFeed, labPlayerAmount, labBetAmountHeader, labPlayerName;
    private TextField           txtBetAmount;
    private ImageView           ivPlayerCoinsIcon;
    private Button              btnCheckOrSee, btnRaise, btnFold, btnChangeCards;
    private Rectangle           recPlayerTurnIndicator;
    private Ellipse             elPot;
    private Label               labPotAmount;
    
    private Card                Cards[]             = new Card[NUMBER_OF_CARDS_PER_HAND + 1];
    private Integer             iHandHighCards[]    = new Integer[NUMBER_OF_CARDS_PER_HAND + 1];       
    
    //Once the hand has been evaluated the highest cards in hand used when finding a winner and
    //should there be players with matching sets.
    
    private PlayerAI            objPlayerAI;

    private int     iPlayerAmount,              //The amount the player currently has in its bank
                    iPlayerType,                //Whether human or computer
                    iPlayerBetInTotalAmount,    //The amount the player has placed into the pot this betting round
                    iPlayerNumber,              //The position of the player around the table
                    iSetValue,                  //The set (if any) of the players current cards
                    iAnteAmount;
    
    private boolean bInGame,                    //Whether the player is in the game or not i.e. Solvent or Insolvent
                    bInRound,                   //Whether the player is still in the current betting round i.e. has not folded
                    bChecked,                   //Whether the player is still in Check mode or has raised the pot this betting round                    
                    bPlayer1GameControlObjectsActive;
    
    
    Player(CardManager objCardManager, BettingRoundManager objBettingRoundManager, AnchorPane apPokerTable,
           int iPlayerNumber, Label labPlayerAmount, Label labPlayerName, ImageView ivPlayerCoinsIcon, Rectangle recPlayerTurnIndicator,    
           Label labInGameStatusFeed, Label labBetAmountHeader, TextField txtBetAmount, Button btnCheckOrSee, Button btnRaise, Button btnFold,
           Button btnChangeCards, Label labPotAmount, Ellipse elPot) {
         
        this.objCardManager                 = objCardManager;
        this.objBettingRoundManager         = objBettingRoundManager;
        this.iPlayerNumber                  = iPlayerNumber;
        this.labPlayerAmount                = labPlayerAmount;
        this.labInGameStatusFeed            = labInGameStatusFeed;
        this.labBetAmountHeader             = labBetAmountHeader;
        this.txtBetAmount                   = txtBetAmount;
        this.labPlayerName                  = labPlayerName;
        this.ivPlayerCoinsIcon              = ivPlayerCoinsIcon;
        this.recPlayerTurnIndicator         = recPlayerTurnIndicator;
        this.btnCheckOrSee                  = btnCheckOrSee;
        this.btnRaise                       = btnRaise;
        this.btnFold                        = btnFold; 
        this.btnChangeCards                 = btnChangeCards;
        
        this.labPotAmount                   = labPotAmount;
        this.elPot                          = elPot;
        
        //Read in the cards references
        Cards[1]                            = new Card(objCardManager, apPokerTable, iPlayerNumber, 1);
        Cards[2]                            = new Card(objCardManager, apPokerTable, iPlayerNumber, 2);
        Cards[3]                            = new Card(objCardManager, apPokerTable, iPlayerNumber, 3);
        Cards[4]                            = new Card(objCardManager, apPokerTable, iPlayerNumber, 4);
        Cards[5]                            = new Card(objCardManager, apPokerTable, iPlayerNumber, 5);
        
        bInGame                             = true;
        bInRound                            = true;
        bPlayer1GameControlObjectsActive    = false;
        
        iPlayerBetInTotalAmount             = RESET;
        iPlayerAmount                       = RESET;
        
        this.labPlayerAmount.setText("");
        this.labPlayerName.setText("");
        this.ivPlayerCoinsIcon.setOpacity(0);
        
        enableButtons(ALL_PLAYER_BUTTONS_INACTIVE);
        
        iPlayerType                         = HUMAN_PLAYER;
        
        //Ensure that the Game Control Objects are initially hidden...
        hidePlayerGameControlObjects(IMMEDIATE);
        
        eventHandlerMouseEvents();
 
        //Initialise all definition and event handling code...
        initTimelines();
    }
    Player(CardManager objCardManager, BettingRoundManager objBettingRoundManager, AnchorPane apPokerTable, int iPlayerNumber, Label labPlayerAmount,
           Label labPlayerName, ImageView ivPlayerCoinsIcon, Rectangle recPlayerTurnIndicator)
    {
        
        //Initialise all objects and event handling code...
        this.objCardManager                 = objCardManager;
        this.objBettingRoundManager         = objBettingRoundManager;
        this.iPlayerNumber                  = iPlayerNumber;
        this.labPlayerAmount                = labPlayerAmount;
        this.labPlayerName                  = labPlayerName;
        this.ivPlayerCoinsIcon              = ivPlayerCoinsIcon;
        this.recPlayerTurnIndicator         = recPlayerTurnIndicator;
        
        //Read in the cards references
        Cards[1]                            = new Card(objCardManager, apPokerTable, iPlayerNumber, 1);
        Cards[2]                            = new Card(objCardManager, apPokerTable, iPlayerNumber, 2);
        Cards[3]                            = new Card(objCardManager, apPokerTable, iPlayerNumber, 3);
        Cards[4]                            = new Card(objCardManager, apPokerTable, iPlayerNumber, 4);
        Cards[5]                            = new Card(objCardManager, apPokerTable, iPlayerNumber, 5);
        
        bInGame                             = true;
        bInRound                            = true;

        iPlayerBetInTotalAmount             = RESET;
        iPlayerAmount                       = RESET;
        
        this.labPlayerAmount.setText("");
        this.labPlayerName.setText("");
        this.ivPlayerCoinsIcon.setOpacity(0);
        
        for(int i = 1 ; i <= NUMBER_OF_CARDS_PER_HAND ; i++)
            Cards[i].hide();
        
        iPlayerType                         = COMPUTER_PLAYER;
              
        objPlayerAI                         = new PlayerAI(this, objBettingRoundManager);
   
        //Initialise all definition and event handling code...
        initTimelines();
    }
    
    
    
    private void initTimelines()
    {
        if(iPlayerNumber == PLAYER_1)
        {
            //Show the Player Game Control Objects
            tlShowPlayerGameControlObjects = new Timeline();
            tlShowPlayerGameControlObjects.setCycleCount(1);
            
            tlShowPlayerGameControlObjects.getKeyFrames().add(new KeyFrame(Duration.millis(0),       new KeyValue(labInGameStatusFeed.opacityProperty(), 0)));
            tlShowPlayerGameControlObjects.getKeyFrames().add(new KeyFrame(Duration.millis(0),       new KeyValue(labBetAmountHeader.opacityProperty(), 0)));
            tlShowPlayerGameControlObjects.getKeyFrames().add(new KeyFrame(Duration.millis(0),       new KeyValue(txtBetAmount.opacityProperty(), 0)));
            tlShowPlayerGameControlObjects.getKeyFrames().add(new KeyFrame(Duration.millis(0),       new KeyValue(btnCheckOrSee.opacityProperty(), 0)));
            tlShowPlayerGameControlObjects.getKeyFrames().add(new KeyFrame(Duration.millis(0),       new KeyValue(btnRaise.opacityProperty(), 0)));
            tlShowPlayerGameControlObjects.getKeyFrames().add(new KeyFrame(Duration.millis(0),       new KeyValue(btnFold.opacityProperty(), 0)));
            tlShowPlayerGameControlObjects.getKeyFrames().add(new KeyFrame(Duration.millis(0),       new KeyValue(btnChangeCards.opacityProperty(), 0)));
            tlShowPlayerGameControlObjects.getKeyFrames().add(new KeyFrame(Duration.millis(0),       new KeyValue(labPotAmount.opacityProperty(), 0)));
            tlShowPlayerGameControlObjects.getKeyFrames().add(new KeyFrame(Duration.millis(0),       new KeyValue(elPot.opacityProperty(), 0)));
            
            tlShowPlayerGameControlObjects.getKeyFrames().add(new KeyFrame(Duration.millis(1000),       new KeyValue(labInGameStatusFeed.opacityProperty(), 1)));
            tlShowPlayerGameControlObjects.getKeyFrames().add(new KeyFrame(Duration.millis(1000),       new KeyValue(labBetAmountHeader.opacityProperty(), 1)));
            tlShowPlayerGameControlObjects.getKeyFrames().add(new KeyFrame(Duration.millis(1000),       new KeyValue(txtBetAmount.opacityProperty(), 1)));
            tlShowPlayerGameControlObjects.getKeyFrames().add(new KeyFrame(Duration.millis(1000),       new KeyValue(btnCheckOrSee.opacityProperty(), 1)));
            tlShowPlayerGameControlObjects.getKeyFrames().add(new KeyFrame(Duration.millis(1000),       new KeyValue(btnRaise.opacityProperty(), 1)));
            tlShowPlayerGameControlObjects.getKeyFrames().add(new KeyFrame(Duration.millis(1000),       new KeyValue(btnFold.opacityProperty(), 1)));
            tlShowPlayerGameControlObjects.getKeyFrames().add(new KeyFrame(Duration.millis(1000),       new KeyValue(btnChangeCards.opacityProperty(), 1)));
            tlShowPlayerGameControlObjects.getKeyFrames().add(new KeyFrame(Duration.millis(1000),       new KeyValue(labPotAmount.opacityProperty(), 1)));
            tlShowPlayerGameControlObjects.getKeyFrames().add(new KeyFrame(Duration.millis(1000),       new KeyValue(elPot.opacityProperty(), 1)));

            
            //Hide the Player Game Control Objects
            tlHidePlayerGameControlObjects = new Timeline();
            tlHidePlayerGameControlObjects.setCycleCount(1);

            tlHidePlayerGameControlObjects.getKeyFrames().add(new KeyFrame(Duration.millis(0),       new KeyValue(labInGameStatusFeed.opacityProperty(), 1)));
            tlHidePlayerGameControlObjects.getKeyFrames().add(new KeyFrame(Duration.millis(0),       new KeyValue(labBetAmountHeader.opacityProperty(), 1)));
            tlHidePlayerGameControlObjects.getKeyFrames().add(new KeyFrame(Duration.millis(0),       new KeyValue(txtBetAmount.opacityProperty(), 1)));
            tlHidePlayerGameControlObjects.getKeyFrames().add(new KeyFrame(Duration.millis(0),       new KeyValue(btnCheckOrSee.opacityProperty(), 1)));
            tlHidePlayerGameControlObjects.getKeyFrames().add(new KeyFrame(Duration.millis(0),       new KeyValue(btnRaise.opacityProperty(), 1)));
            tlHidePlayerGameControlObjects.getKeyFrames().add(new KeyFrame(Duration.millis(0),       new KeyValue(btnFold.opacityProperty(), 1)));
            tlHidePlayerGameControlObjects.getKeyFrames().add(new KeyFrame(Duration.millis(0),       new KeyValue(btnChangeCards.opacityProperty(), 1)));
            tlHidePlayerGameControlObjects.getKeyFrames().add(new KeyFrame(Duration.millis(0),       new KeyValue(labPotAmount.opacityProperty(), 1)));
            tlHidePlayerGameControlObjects.getKeyFrames().add(new KeyFrame(Duration.millis(0),       new KeyValue(elPot.opacityProperty(), 1)));
            
            tlHidePlayerGameControlObjects.getKeyFrames().add(new KeyFrame(Duration.millis(1000),       new KeyValue(labInGameStatusFeed.opacityProperty(), 0)));
            tlHidePlayerGameControlObjects.getKeyFrames().add(new KeyFrame(Duration.millis(1000),       new KeyValue(labBetAmountHeader.opacityProperty(), 0)));
            tlHidePlayerGameControlObjects.getKeyFrames().add(new KeyFrame(Duration.millis(1000),       new KeyValue(txtBetAmount.opacityProperty(), 0)));
            tlHidePlayerGameControlObjects.getKeyFrames().add(new KeyFrame(Duration.millis(1000),       new KeyValue(btnCheckOrSee.opacityProperty(), 0)));
            tlHidePlayerGameControlObjects.getKeyFrames().add(new KeyFrame(Duration.millis(1000),       new KeyValue(btnRaise.opacityProperty(), 0)));
            tlHidePlayerGameControlObjects.getKeyFrames().add(new KeyFrame(Duration.millis(1000),       new KeyValue(btnFold.opacityProperty(), 0)));
            tlHidePlayerGameControlObjects.getKeyFrames().add(new KeyFrame(Duration.millis(1000),       new KeyValue(btnChangeCards.opacityProperty(), 0)));
            tlHidePlayerGameControlObjects.getKeyFrames().add(new KeyFrame(Duration.millis(1000),       new KeyValue(labPotAmount.opacityProperty(), 0)));
            tlHidePlayerGameControlObjects.getKeyFrames().add(new KeyFrame(Duration.millis(1000),       new KeyValue(elPot.opacityProperty(), 0)));
        }
        
        //Show the dealt cards on table
        tlShowDealtCards = new Timeline();
        tlShowDealtCards.setCycleCount(1);
        
        tlShowDealtCards.getKeyFrames().add(new KeyFrame(Duration.millis(0),        new KeyValue(Cards[1].getCardOpacityProperty(), NOT_VISIBLE)));
        tlShowDealtCards.getKeyFrames().add(new KeyFrame(Duration.millis(100),      new KeyValue(Cards[2].getCardOpacityProperty(), NOT_VISIBLE)));
        tlShowDealtCards.getKeyFrames().add(new KeyFrame(Duration.millis(200),      new KeyValue(Cards[3].getCardOpacityProperty(), NOT_VISIBLE)));
        tlShowDealtCards.getKeyFrames().add(new KeyFrame(Duration.millis(300),      new KeyValue(Cards[4].getCardOpacityProperty(), NOT_VISIBLE)));
        tlShowDealtCards.getKeyFrames().add(new KeyFrame(Duration.millis(400),      new KeyValue(Cards[5].getCardOpacityProperty(), NOT_VISIBLE)));
        
        tlShowDealtCards.getKeyFrames().add(new KeyFrame(Duration.millis(300),      new KeyValue(Cards[1].getCardOpacityProperty(), VISIBLE)));
        tlShowDealtCards.getKeyFrames().add(new KeyFrame(Duration.millis(400),      new KeyValue(Cards[2].getCardOpacityProperty(), VISIBLE)));
        tlShowDealtCards.getKeyFrames().add(new KeyFrame(Duration.millis(500),      new KeyValue(Cards[3].getCardOpacityProperty(), VISIBLE)));
        tlShowDealtCards.getKeyFrames().add(new KeyFrame(Duration.millis(600),      new KeyValue(Cards[4].getCardOpacityProperty(), VISIBLE)));
        tlShowDealtCards.getKeyFrames().add(new KeyFrame(Duration.millis(700),      new KeyValue(Cards[5].getCardOpacityProperty(), VISIBLE)));
        
        
        //Show the player relevant objects on the table
        tlShowTableObjects = new Timeline();
        tlShowTableObjects.setCycleCount(1);
        
        tlShowTableObjects.getKeyFrames().add(new KeyFrame(Duration.millis(0),      new KeyValue(labPlayerAmount.opacityProperty(), NOT_VISIBLE)));
        tlShowTableObjects.getKeyFrames().add(new KeyFrame(Duration.millis(0),      new KeyValue(labPlayerName.opacityProperty(), NOT_VISIBLE)));
        tlShowTableObjects.getKeyFrames().add(new KeyFrame(Duration.millis(0),      new KeyValue(ivPlayerCoinsIcon.opacityProperty(), NOT_VISIBLE)));
        
        tlShowTableObjects.getKeyFrames().add(new KeyFrame(Duration.millis(1000),   new KeyValue(labPlayerAmount.opacityProperty(), VISIBLE)));
        tlShowTableObjects.getKeyFrames().add(new KeyFrame(Duration.millis(1000),   new KeyValue(labPlayerName.opacityProperty(), VISIBLE)));
        tlShowTableObjects.getKeyFrames().add(new KeyFrame(Duration.millis(1000),   new KeyValue(ivPlayerCoinsIcon.opacityProperty(), VISIBLE)));
        
        
        //Hide the player relevant objects on the table
        tlHideTableObjects = new Timeline();
        tlHideTableObjects.setCycleCount(1);
        
        tlHideTableObjects.getKeyFrames().add(new KeyFrame(Duration.millis(0),      new KeyValue(labPlayerAmount.opacityProperty(), VISIBLE)));
        tlHideTableObjects.getKeyFrames().add(new KeyFrame(Duration.millis(0),      new KeyValue(labPlayerName.opacityProperty(), VISIBLE)));
        tlHideTableObjects.getKeyFrames().add(new KeyFrame(Duration.millis(0),      new KeyValue(ivPlayerCoinsIcon.opacityProperty(), VISIBLE)));
        
        tlHideTableObjects.getKeyFrames().add(new KeyFrame(Duration.millis(1000),   new KeyValue(labPlayerAmount.opacityProperty(), NOT_VISIBLE)));
        tlHideTableObjects.getKeyFrames().add(new KeyFrame(Duration.millis(1000),   new KeyValue(labPlayerName.opacityProperty(), NOT_VISIBLE)));
        tlHideTableObjects.getKeyFrames().add(new KeyFrame(Duration.millis(1000),   new KeyValue(ivPlayerCoinsIcon.opacityProperty(), NOT_VISIBLE)));
        
        
        //Show the player indicator when turn
        tlShowPlayerTurnIndicator = new Timeline();
        tlShowPlayerTurnIndicator.setCycleCount(1);
        
        tlShowPlayerTurnIndicator.getKeyFrames().add(new KeyFrame(Duration.millis(0),       new KeyValue(recPlayerTurnIndicator.opacityProperty(), NOT_VISIBLE)));
        tlShowPlayerTurnIndicator.getKeyFrames().add(new KeyFrame(Duration.millis(150),     new KeyValue(recPlayerTurnIndicator.opacityProperty(), VISIBLE)));
        
        
        //Hide the player indicator when turn
        tlHidePlayerTurnIndicator = new Timeline();
        tlHidePlayerTurnIndicator.setCycleCount(1);

        tlHidePlayerTurnIndicator.getKeyFrames().add(new KeyFrame(Duration.millis(0),       new KeyValue(recPlayerTurnIndicator.opacityProperty(), VISIBLE)));
        tlHidePlayerTurnIndicator.getKeyFrames().add(new KeyFrame(Duration.millis(150),     new KeyValue(recPlayerTurnIndicator.opacityProperty(), NOT_VISIBLE)));
    }
    private void enableButtons(int iStage)
    {
        switch(iStage)
        {
            case(BETTING_ROUND):
            {
                btnCheckOrSee.setDisable(false);

                if(objBettingRoundManager.getMaximumBetAmountThisRound() == 0)      btnRaise.setDisable(true);
                else                                                                btnRaise.setDisable(false);
                
                btnFold.setDisable(false); 

                txtBetAmount.setDisable(false);
                
                break;
            }
            case(CHANGE_CARDS):
            {
                btnChangeCards.setDisable(false);             
                
                for(int i = 1 ; i<=NUMBER_OF_CARDS_PER_HAND ; i++)
                    Cards[i].setCardSelectable(true);
                
                break;
            }
            case(ALL_PLAYER_BUTTONS_INACTIVE):
            {
                btnCheckOrSee.setDisable(true);
                btnRaise.setDisable(true);
                btnFold.setDisable(true); 
                btnChangeCards.setDisable(true);
                
                txtBetAmount.setDisable(true);
                
                for(int i = 1 ; i<=NUMBER_OF_CARDS_PER_HAND ; i++)
                    Cards[i].setCardSelectable(false);

                break;
            }
        }
    }
    private boolean isNumeric(String sNumber)
    {
        if(sNumber.isEmpty())
            return false;
        
        for(int i = 0 ; i < sNumber.length() ; i++)
            if( (sNumber.charAt(i) < 48) || (sNumber.charAt(i) > 57) )
                return false;
        
        return true;
    }
    private void activeHumanPlayerGameControlButtons(int iRound)
    {
        switch(iRound)
        {
            case(BETTING_ROUND):
            {
                enableButtons(BETTING_ROUND);
                
                resetBettingAmount();
                
                //Change the text on the Sheck/See button
                if(objBettingRoundManager.getPotRaiseStatus() == POT_RAISED)
                    btnCheckOrSee.setText("See");
                
                break;
            }
            case(CHANGE_CARDS):
            {
                enableButtons(CHANGE_CARDS);
                               
                break;
            }
        }
    }
    private void humanPlayerBettingRoundActionComplete()
    {
        enableButtons(ALL_PLAYER_BUTTONS_INACTIVE);

        objBettingRoundManager.findNextPlayerToBet();

        hidePlayerTurnIndicator();        
    }
    private void humanPlayerChangeCardsRoundActionComplete()
    {
        enableButtons(ALL_PLAYER_BUTTONS_INACTIVE);

        objCardManager.runComputerPlayersChangeCardsMacro();

        hidePlayerTurnIndicator();        
    }
    private void computerPlayerBettingRoundActionComplete()
    {
        tiComputerPlayerBettingRoundActionComplete = new Timer("tiComputerPlayerBettingRoundActionComplete", true);
        tiComputerPlayerBettingRoundActionComplete.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater
                (
                    new Runnable() 
                    {
                        @Override public void run() 
                        {
                            hidePlayerTurnIndicator();
                            objBettingRoundManager.findNextPlayerToBet();
                            
                            tiComputerPlayerBettingRoundActionComplete.cancel();
                        }
                    }
                );
            }
        }, 1000);
    }
    private String formatAmount(int iAmount)
    {
        return NumberFormat.getInstance(Locale.UK).format((double)iAmount);
    }
    
    
    void initialiseNewGame()
    {
        iPlayerAmount = RESET;
        labPlayerAmount.setText("");
        labPlayerAmount.setOpacity(NOT_VISIBLE);
        
        setInGame(true);
        setInRound(true);
    }
    void initialiseNewRound()
    {
        setInRound(true);
    }
    void showPlayerGameControlObjects(int iTransitionType)
    {
        switch(iTransitionType)
        {
            case(IMMEDIATE):
            {
                labInGameStatusFeed.setOpacity(VISIBLE);
                labBetAmountHeader.setOpacity(VISIBLE);
                txtBetAmount.setOpacity(VISIBLE);
                btnCheckOrSee.setOpacity(VISIBLE);
                btnRaise.setOpacity(VISIBLE);
                btnFold.setOpacity(VISIBLE);
                btnChangeCards.setOpacity(VISIBLE);           
                labPotAmount.setOpacity(VISIBLE);
                elPot.setOpacity(VISIBLE);
 
                break;
            }
            case(FADE):
            {
                tlShowPlayerGameControlObjects.playFromStart();
                
                break;
            }
        }
        
        bPlayer1GameControlObjectsActive = true;
    }
    boolean arePlayer1ControlObjectActive()
    {
        return bPlayer1GameControlObjectsActive;
    }
    void hidePlayerGameControlObjects(int iTransitionType)
    {
        switch(iTransitionType)
        {
            case(IMMEDIATE):
            {
                labPlayerAmount.setOpacity(NOT_VISIBLE);
                txtBetAmount.setOpacity(NOT_VISIBLE);
                labBetAmountHeader.setOpacity(NOT_VISIBLE);
                btnCheckOrSee.setOpacity(NOT_VISIBLE);
                btnRaise.setOpacity(NOT_VISIBLE);
                btnFold.setOpacity(NOT_VISIBLE);
                btnChangeCards.setOpacity(NOT_VISIBLE);
                
                break;
            }
            case(FADE):
            {
                tlHidePlayerGameControlObjects.playFromStart();
                
                break;
            }
        }
        
        bPlayer1GameControlObjectsActive = false;
    }
    int getAmount()
    {
        return iPlayerAmount;
    }
    int getType()
    {
        return iPlayerType;
    }
    void setHandHighCard(int iPosition, int iValue)
    {
        iHandHighCards[iPosition] = iValue;
    }
    int getHandHighCard(int iPosition)
    {
        return iHandHighCards[iPosition];
    }
    void ante(int iAnteAmount)
    {
        this.iAnteAmount = iAnteAmount;
        
        reducePlayerAmount(iAnteAmount);
    }
    int getAnteAmount()
    {
        return iAnteAmount;
    }
    void reducePlayerAmount(int iAmount)
    {
        iPlayerAmount = (iPlayerAmount - iAmount);

        labPlayerAmount.setText("£" + formatAmount(iPlayerAmount));
    }
    void showTableObjects()
    {
        tlShowTableObjects.playFromStart();
    }
    void hideTableObjects()
    {
        tlHideTableObjects.playFromStart();
    }
    void initialiseBettingRound(int iRound)
    {
        //Reset appropriate variables...
        iPlayerBetInTotalAmount = RESET;        
        bChecked = false;
        
        if(iPlayerType == COMPUTER_PLAYER)      objPlayerAI.initialiseBettingRound(iRound, iPlayerAmount, iPlayerBetInTotalAmount);
        else                                    btnCheckOrSee.setText("Check");
    }
    void participateBettingRound(int iRound)
    {
        //If human then buttons become enabled, else activate AI and have it act accordingly...
        switch(iPlayerType)
        {
            case(HUMAN_PLAYER):     activeHumanPlayerGameControlButtons(BETTING_ROUND);     break;
            case(COMPUTER_PLAYER):  runComputerPlayerBettingAI(iRound);                     break;
        }
    }
    void runComputerPlayerBettingAI(int iRound)
    {
        switch(objPlayerAI.findPlayerAIBettingAction(iRound, iPlayerAmount, iPlayerBetInTotalAmount))
        {
            case(COMPUTER_PLAYER_FOLD):     playerFold();           break;
            case(COMPUTER_PLAYER_CHECK):    playerCheck();          break;
            case(COMPUTER_PLAYER_SEE):      playerSee();            break;
            case(COMPUTER_PLAYER_RAISE):
            {
                objBettingRoundManager.playerRaise(iPlayerNumber, objPlayerAI.getRaiseAmount()); 
                
                break;
            }
        }
        
        computerPlayerBettingRoundActionComplete();
    }
    void runComputerPlayerChangeCardsAI()
    {       
        objPlayerAI.findPlayerAIChangeCardsAction();
    }
    void showWinningCards()
    {
        for(int i = 1 ; i<=NUMBER_OF_CARDS_PER_HAND ; i++)
            if(!Cards[i].getInSet())
                Cards[Cards[i].getTablePosition()].showCardCover();
    }
    void coverAllCards()
    {
        for(int i = 1 ; i<=NUMBER_OF_CARDS_PER_HAND ; i++)
            Cards[i].showCardCover();
    }
    void resetBettingAmount()
    {
        txtBetAmount.setText("");
    }
    void participateChangeCardsRound()
    {
        showPlayerTurnIndicator();

        //If human then buttons become enabled, else activate AI and have it act accordingly...
        switch(iPlayerType)
        {
            case(HUMAN_PLAYER):     activeHumanPlayerGameControlButtons(CHANGE_CARDS);      break;
            case(COMPUTER_PLAYER):  runComputerPlayerChangeCardsAI();                       break;
        }
    }
    void dealCards()
    {
        for(int i = 1 ; i <= NUMBER_OF_CARDS_PER_HAND ; i++)
        {
            Cards[i].setSuit(BACK);
            Cards[i].setValue(BACK);
            Cards[i].loadCard();
        }
        
        tlShowDealtCards.playFromStart();
    }
    void showCard(int iCard)
    {
        Cards[iCard].loadCard();
        Cards[iCard].setCardOpacity(VISIBLE);
    }
    void setCardOpactity(int iCard, int iOpacity)
    {
        Cards[iCard].setCardOpacity(iOpacity);
    }
    void showCards()
    {
        for(int i = 1 ; i <= NUMBER_OF_CARDS_PER_HAND ; i++)
            Cards[Cards[i].getTablePosition()].loadCard();
    }
    void hideCards()
    {
        for(int i = 1 ; i<=NUMBER_OF_CARDS_PER_HAND ; i++)
        {
            Cards[i].clearCardCoverText();
            Cards[i].hide();
            Cards[i].hideCardCover();
        }
    }
    void clearAllHoldIndicators()
    {
        for(int i = 1 ; i<=NUMBER_OF_CARDS_PER_HAND ; i++)
            Cards[i].setHoldState(CARD_HOLD_OFF);
    }
    boolean seenTable(int iCurrentTableSeeAmount)
    {
        return (iPlayerBetInTotalAmount == iCurrentTableSeeAmount);
    }
    boolean getChecked()
    {
        return bChecked;
    }
    boolean getInGame()
    {
        return bInGame;
    }
    boolean getInRound()
    {
        return bInRound;
    }
    Card[] getCardsRef()
    {
        return Cards;
    }
    int getCardTablePosition(int iCard)
    {
        return Cards[iCard].getTablePosition();
    }
    int getCardSuit(int iCard)
    {
        return Cards[iCard].getSuit();
    }
    int getCardValue(int iCard)
    {
        return Cards[iCard].getValue();
    }
    int getPlayerNumber()
    {
        return iPlayerNumber;
    }
    int getSetValue()
    {
        return iSetValue;
    }
    int getCardHold(int iCard)
    {
        return Cards[iCard].getHold();
    }
    boolean getCheckState()
    {
        return bChecked;
    }
    void setCheckState(boolean bState)
    {
        bChecked = bState;
    }
    void setCardInSet(int iCard, boolean iState)
    {
        Cards[iCard].setInSet(iState);
    }
    void setInGame(boolean iState)
    {
        bInGame = iState;
        
        setInRound(false);
    }
    void setInRound(boolean iState)
    {
        bInRound = iState;
    }
    void setSetValue(int iSetValue)
    {
        this.iSetValue = iSetValue;
    }
    void setBtnCheckOrSeeText(String sText)
    {
        btnCheckOrSee.setText(sText);
    }
    void setCardSuit(int iCard, int iSuit)
    {
        Cards[iCard].setSuit(iSuit);
    }
    void setCardSuitStore(int iCard, int iSuit)
    {
        Cards[iCard].setSuitStore(iSuit);
    }
    void setCardTablePosition(int iCard, int iPosition)
    {
        Cards[iCard].setTablePosition(iPosition);
    }
    void setCardValue(int iCard, int iValue)
    {
        Cards[iCard].setValue(iValue);
    }
    void setCardValueStore(int iCard, int iValue)
    {
        Cards[iCard].setValueStore(iValue);
    }
    void setPlayerAmount(int iPlayerAmount)
    {
        this.iPlayerAmount = iPlayerAmount;
        
        labPlayerAmount.setText("£" + formatAmount(this.iPlayerAmount));
    }
    void addPlayerAmount(int iAmount)
    {
        iPlayerAmount = (iPlayerAmount+iAmount);
        
        labPlayerAmount.setText("£" + formatAmount(this.iPlayerAmount));
    }
    void setPlayerBetInTotalAmount(int iAmount)
    {
        iPlayerBetInTotalAmount = iAmount;
    }
    int getPlayerBetInTotalAmount()
    {
        return iPlayerBetInTotalAmount;
    }
    void playerCheckOrSee()
    {
        //If the Betting Round is still in check and no one has risen the pot, the human player has therefore chosen to also check...
        if(objBettingRoundManager.getPotRaiseStatus() == POT_RAISED)    playerSee();
        else                                                            playerCheck();
    }
    void playerCheck()
    {
        objBettingRoundManager.playerCheck(iPlayerNumber);       
    }
    void playerSee()
    {
        objBettingRoundManager.playerSee(iPlayerNumber);
    }
    void playerRaise()
    {
        int iPlayerRaiseAmount;
        int iMaximumBetAmountThisRound = objBettingRoundManager.getMaximumBetAmountThisRound();

        iPlayerRaiseAmount = Integer.valueOf(txtBetAmount.getText());

        if(iPlayerRaiseAmount > iMaximumBetAmountThisRound)
            iPlayerRaiseAmount = iMaximumBetAmountThisRound;
        
        objBettingRoundManager.playerRaise(iPlayerNumber, iPlayerRaiseAmount);
    }
    void playerFold()
    {
        objBettingRoundManager.playerFold(iPlayerNumber);
    }
    void showFoldedCards()
    {
        for(int i=1 ; i <= NUMBER_OF_CARDS_PER_HAND ; i++)
            Cards[i].setFoldState(CARD_FOLD_ON);
    }
    void showPlayerTurnIndicator()
    {
        //Show the player indicator when turn
        tlShowPlayerTurnIndicator.playFromStart();
    }
    void hidePlayerTurnIndicator()
    {
        //Hide the player indicator when turn
        tlHidePlayerTurnIndicator.playFromStart();
    }
    void removePlayerCardsNotHeld()
    {
        //Show blank in the cards table position
        for(int i = 1 ; i<=NUMBER_OF_CARDS_PER_HAND ; i++)
            if(Cards[i].getHold() == CARD_HOLD_OFF)
                Cards[i].setCardOpacity(NOT_VISIBLE);
    }
    
    
    
    private void    eventHandlerMouseEvents()
    {
        // - - - - Handlers - - - -
        final EventHandler<MouseEvent>  ehCheckOrSee = (MouseEvent me) -> {
            playerCheckOrSee();
            
            humanPlayerBettingRoundActionComplete();
            
            me.consume();
        };
        
        final EventHandler<MouseEvent>  ehRaise = (MouseEvent me) -> {
            
            if(isNumeric(txtBetAmount.getText()))
            {
                playerRaise();

                humanPlayerBettingRoundActionComplete();
            }
            
            me.consume();
        };
        
        final EventHandler<MouseEvent>  ehFold = (MouseEvent me) -> {
            playerFold();
            
            humanPlayerBettingRoundActionComplete();
            
            me.consume();
        };
        
        final EventHandler<MouseEvent>  ehChangeCards = (MouseEvent me) -> {
            objCardManager.playerChangeCards(iPlayerNumber);
            
            humanPlayerChangeCardsRoundActionComplete();
            
            me.consume();
        };
        
        final EventHandler<MouseEvent>  ehPlayerCardCover1 = (MouseEvent me) -> {
            if(Cards[1].getCardSelectable())
            {
                Cards[1].toggleHoldState();
            }
            
            me.consume();
        };
        
        final EventHandler<MouseEvent>  ehPlayerCardCover2 = (MouseEvent me) -> {
            if(Cards[2].getCardSelectable())
            {
                Cards[2].toggleHoldState();
            }
            
            me.consume();
        };
        
        final EventHandler<MouseEvent>  ehPlayerCardCover3 = (MouseEvent me) -> {
            if(Cards[3].getCardSelectable())
            {
                Cards[3].toggleHoldState();
            }
            
            me.consume();
        };
        
        final EventHandler<MouseEvent>  ehPlayerCardCover4 = (MouseEvent me) -> {
            if(Cards[4].getCardSelectable())
            {
                Cards[4].toggleHoldState();
            }
            
            me.consume();
        };
        
        final EventHandler<MouseEvent>  ehPlayerCardCover5 = (MouseEvent me) -> {
            if(Cards[5].getCardSelectable())
            {
                Cards[5].toggleHoldState();
            }
            
            me.consume();
        };
               
        btnCheckOrSee.addEventHandler(MouseEvent.MOUSE_CLICKED, ehCheckOrSee);
        btnRaise.addEventHandler(MouseEvent.MOUSE_CLICKED, ehRaise);
        btnFold.addEventHandler(MouseEvent.MOUSE_CLICKED, ehFold);
        btnChangeCards.addEventHandler(MouseEvent.MOUSE_CLICKED, ehChangeCards);
        
        Cards[1].getRecCardCoverRef().addEventHandler(MouseEvent.MOUSE_CLICKED, ehPlayerCardCover1);
        Cards[2].getRecCardCoverRef().addEventHandler(MouseEvent.MOUSE_CLICKED, ehPlayerCardCover2);
        Cards[3].getRecCardCoverRef().addEventHandler(MouseEvent.MOUSE_CLICKED, ehPlayerCardCover3);
        Cards[4].getRecCardCoverRef().addEventHandler(MouseEvent.MOUSE_CLICKED, ehPlayerCardCover4);
        Cards[5].getRecCardCoverRef().addEventHandler(MouseEvent.MOUSE_CLICKED, ehPlayerCardCover5);
        
        //Append to the labels as well given that that they are higher in the Z-Order
        Cards[1].getLabPlayerCardCoverTextRef().addEventHandler(MouseEvent.MOUSE_CLICKED, ehPlayerCardCover1);
        Cards[2].getLabPlayerCardCoverTextRef().addEventHandler(MouseEvent.MOUSE_CLICKED, ehPlayerCardCover2);
        Cards[3].getLabPlayerCardCoverTextRef().addEventHandler(MouseEvent.MOUSE_CLICKED, ehPlayerCardCover3);
        Cards[4].getLabPlayerCardCoverTextRef().addEventHandler(MouseEvent.MOUSE_CLICKED, ehPlayerCardCover4);
        Cards[5].getLabPlayerCardCoverTextRef().addEventHandler(MouseEvent.MOUSE_CLICKED, ehPlayerCardCover5);
    }
}
