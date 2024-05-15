package poker;

import java.text.NumberFormat;
import java.util.Locale;
import javafx.scene.control.Label;

class BettingRoundManager implements Constants{

    private PokerGame                   objPokerGame;
    private Label                       labPotAmount;

    private Player Players[] = new Player[NUMBER_OF_PLAYERS + 1];
    
    private int iCurrentPlayerTurn,                 //The current player betting during a round
                iRoundNumber,                       //Either first or second
                iTableSeeAmount,                    //The maximum amount a player has placed onto the table, that all other players must either match or fold
                iPotAmount,                         //The current amount of money added to the pot
                iPotRaiseStatus,                    //A Status variable which determines whether the table has been raised or not
                iMaximumBetAmountThisRound;  
    
    BettingRoundManager(PokerGame objPokerGame, Player Players[], Label labPotAmount) {
        
        this.objPokerGame       = objPokerGame;
        this.labPotAmount       = labPotAmount;
        this.Players            = Players;
        
        iPotAmount              = RESET;            //The current amount of money added to the pot
    }
    
    
    
    private String formatAmount(int iAmount)
    {
        return NumberFormat.getInstance(Locale.UK).format((double)iAmount);
    }
    private int findMaximumBetAmount()
    {
        int iMaximumBetAmountThisRoundCalculate;
        
        iMaximumBetAmountThisRoundCalculate = RESET;
        
        //The maximum bet amount that can be bet by any player this round is the player with the smallest amount of funds available.
	for(int i = 1 ; i<=NUMBER_OF_PLAYERS ; i++)
            if(Players[i].getInRound())
                if(iMaximumBetAmountThisRoundCalculate == RESET)                            iMaximumBetAmountThisRoundCalculate = Players[i].getAmount();
                else if(iMaximumBetAmountThisRoundCalculate > Players[i].getAmount())       iMaximumBetAmountThisRoundCalculate = Players[i].getAmount();

        Players[1].resetBettingAmount();
                
	return iMaximumBetAmountThisRoundCalculate;
    }
    private int findNextEligiblePlayerToBet()
    {
        //Given the table position of the current person who has just finished betting, find the next eligible player to bet if any...
        
        int iPlayerToBeEvaluated;
                
        /*  When each players turn has finished the loop below will look ahead at the next x3 players.
        
            If the pot has not been raised and meet a player who has already checked indicates that have gone around the table = End of Betting Round
            If the pot has been raised and all players have seen the table = End of Betting Round
        */
        
        for(int i = 1; i <= (NUMBER_OF_PLAYERS - 1); i++)
        {       
            if( (iCurrentPlayerTurn + i) > NUMBER_OF_PLAYERS )      //i.e. looped around
            {
                iPlayerToBeEvaluated = (iCurrentPlayerTurn + i) - NUMBER_OF_PLAYERS;
                
                if(!Players[iPlayerToBeEvaluated].getInRound())
                    continue;
            }
            else
            {
                iPlayerToBeEvaluated = (iCurrentPlayerTurn + i);
                
                if(!Players[iPlayerToBeEvaluated].getInRound())
                    continue;
            }
            
            if(iPotRaiseStatus == POT_RAISED)
            {
                if(!Players[iPlayerToBeEvaluated].seenTable(iTableSeeAmount))
                {
                    Players[iPlayerToBeEvaluated].showPlayerTurnIndicator();

                    objPokerGame.objInGameStatusFeedManager.showMessage("Player " + Integer.toString(iPlayerToBeEvaluated) + " To See £" + formatAmount(iTableSeeAmount - Players[iPlayerToBeEvaluated].getPlayerBetInTotalAmount()));
                    
                    iCurrentPlayerTurn = iPlayerToBeEvaluated;
                
                    return iPlayerToBeEvaluated;
                }
                else
                {   
                    //Will continue to check the next x3 players to see if they have all meet the table, if so then return ALL_PLAYERS_SEEN_TABLE
                    continue;
                }
            }
            else        //No one has raised the pot i.e. still in Check
            {
                if(Players[iPlayerToBeEvaluated].getChecked())      //Then gone around the table and all players are still in Check state
                {
                    return ALL_PLAYERS_SEEN_TABLE;
                }
                else
                {
                    Players[iPlayerToBeEvaluated].showPlayerTurnIndicator();

                    iCurrentPlayerTurn = iPlayerToBeEvaluated;

                    return iPlayerToBeEvaluated;
                }
            }   
        } 
        
        return ALL_PLAYERS_SEEN_TABLE;
    }
    private boolean checkForAnySingleRemainingPlayers()
    {
        int iNumberOfPlayersInRound;
        
        iNumberOfPlayersInRound = RESET;
        
        for(int i = 1 ; i<=NUMBER_OF_PLAYERS ; i++)
            if(Players[i].getInRound())
                iNumberOfPlayersInRound++;
        
        return(iNumberOfPlayersInRound == 1);
    }
    
    
    
    int getMaximumBetAmountThisRound()
    {
        return iMaximumBetAmountThisRound;
    }
    void startNewBettingRound(int iRoundNumber)
    {
        iCurrentPlayerTurn              = RESET;                    //The current player betting during a round
        this.iRoundNumber               = iRoundNumber;             //Either first or second
        iPotRaiseStatus                 = POT_NOT_RAISED;           //Whether a player has checked or not during the round
        iTableSeeAmount                 = RESET;                    //The maximum amount a player has placed onto the table, that all other players must either match or fold
        
        iMaximumBetAmountThisRound      = findMaximumBetAmount();
        
        //Update player 1 bet amount bar to the maximum bet amount...
        for(int i = 1 ; i<=NUMBER_OF_PLAYERS ; i++)
            if(Players[i].getInRound())
            {
                Players[i].initialiseBettingRound(iRoundNumber);
                
                //Find the first person in this round i.e. player 1 may have already folded therefore need to start with the next player who is still in the round
                if(iCurrentPlayerTurn == RESET)
                    iCurrentPlayerTurn = i;
            }

        Players[iCurrentPlayerTurn].showPlayerTurnIndicator();
        
        Players[iCurrentPlayerTurn].participateBettingRound(iRoundNumber);
    }
    void findNextPlayerToBet()
    {
        final int iNextPlayerToBet;
        
        //Check to see if we have winner i.e. only one person remains in round
        if(checkForAnySingleRemainingPlayers())
        {
            for(int i = 1 ; i<=NUMBER_OF_PLAYERS ; i++)
                if(Players[i].getInRound())
                    objPokerGame.playerHasWonRound(i, ALL_OTHER_PLAYERS_FOLDED);
        }
        else
        {
            //Check if all players have seen the table...        
            iNextPlayerToBet = findNextEligiblePlayerToBet();

            if(iNextPlayerToBet == ALL_PLAYERS_SEEN_TABLE)
            {
                switch(iRoundNumber)
                {
                    case(BETTING_ROUND_1):     objPokerGame.stageComplete(BETTING_ROUND_ONE_COMPLETE);      break;
                    case(BETTING_ROUND_2):     objPokerGame.stageComplete(BETTING_ROUND_TWO_COMPLETE);      break;
                }
            }
            else
            {
                if(Players[iNextPlayerToBet].seenTable(iTableSeeAmount))    Players[iNextPlayerToBet].participateBettingRound(iRoundNumber);
                else
                {
                    Players[iNextPlayerToBet].participateBettingRound(iRoundNumber);
                }
            }
        }
    }
    void playerCheck(int iPlayer)
    {
        objPokerGame.objInGameStatusFeedManager.showMessage("Player " + Integer.toString(iPlayer) + " Check");
        
        Players[iPlayer].setCheckState(true);
    }
    void playerSee(int iPlayer)
    {
        int iAmount;
        
        //The player is reduced the Table See Amount - Amount Already bet so far...
        iAmount = iTableSeeAmount - Players[iPlayer].getPlayerBetInTotalAmount();
                
        //It has already been confirmed that the player can see the table as it is not possible for a player to raise the table any more than any other player has available
        Players[iPlayer].reducePlayerAmount(iAmount);
        
        addPotAmount(iAmount);
        
        objPokerGame.objInGameStatusFeedManager.showMessage("Player " + Integer.toString(iPlayer) + " Sees" + " £" + Integer.toString(iAmount));

        //Update the variable which contains the amount the player has bet...
        Players[iPlayer].setPlayerBetInTotalAmount(Players[iPlayer].getPlayerBetInTotalAmount() + iAmount);
        
        Players[iPlayer].setCheckState(false);
    }
    void playerRaise(int iPlayer, int iRaiseAmount)
    {
        //The player is therefore to be reduced the amount to see the table and then the amount raised...
        
        int iAmountToSee;
        
        iAmountToSee = iTableSeeAmount - Players[iPlayer].getPlayerBetInTotalAmount();
        
        if(iPotRaiseStatus == POT_RAISED)   objPokerGame.objInGameStatusFeedManager.showMessage("Player " + Integer.toString(iPlayer) + " Sees £" + formatAmount(iAmountToSee) + " and Raise £" + formatAmount(iRaiseAmount));
        else                                objPokerGame.objInGameStatusFeedManager.showMessage("Player " + Integer.toString(iPlayer) + " Raise £" + formatAmount(iRaiseAmount));       
        
        //Add amount to see the table by raise amount...
        iTableSeeAmount = (iTableSeeAmount + iRaiseAmount);
        
        Players[iPlayer].reducePlayerAmount(iAmountToSee + iRaiseAmount);
        
        addPotAmount(iAmountToSee + iRaiseAmount);
        
        //Update the variable which contains the amount the player has bet...
        Players[iPlayer].setPlayerBetInTotalAmount(Players[iPlayer].getPlayerBetInTotalAmount() + iAmountToSee + iRaiseAmount);
        
        iPotRaiseStatus = POT_RAISED;
        
        Players[iPlayer].setCheckState(false);
        
        //Automatically change the button text on the Human player
        Players[PLAYER_1].setBtnCheckOrSeeText("See");
        
        iMaximumBetAmountThisRound      = findMaximumBetAmount();
    }
    void playerFold(int iPlayer)
    {
        objPokerGame.objInGameStatusFeedManager.showMessage("Player " + Integer.toString(iPlayer) + " Fold");
        
        Players[iPlayer].setInRound(false);
        Players[iPlayer].setCheckState(false);
        
        Players[iPlayer].showFoldedCards();
    }
    int getCurrentTableSeeAmount()
    {
        return iTableSeeAmount;
    }
    int getPotAmount()
    {
        return iPotAmount;
    }
    int getPotRaiseStatus()
    {
        return iPotRaiseStatus;
    }
    void resetPotAmount()
    {
        iPotAmount = RESET;
        
        labPotAmount.setText("");
    }
    void addPotAmount(int iAmount)
    {
        iPotAmount = (iPotAmount + iAmount);
        
        labPotAmount.setText("£" + formatAmount(iPotAmount));
    }
}
