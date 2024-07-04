package poker;

import java.util.Random;

class PlayerAI implements Constants{

    private Player                          objPlayer;
    private PlayerCardsEvaluator            objPlayerCardsEvaluator;
    private Card                            Cards[] = new Card[NUMBER_OF_CARDS_PER_HAND + 1];
    private BettingRoundManager             objBettingRoundManager;
    private Random                          objRandom = new Random();
    
    private int iPlayerAmount,                      //Amount player currently has...
                iPlayerAmountToSee,                 //During betting round, corresponds to the (maximum amount a player has placed on the table - the total amoun the current player has already placed on the table)
                iPlayerBetInTotalAmount,
            
                iCalculatedTargetBetAmount,         //The ideal/calculated target bet amount
                iCalculatedMaximumBetAmount,        //The ideal/calculated maximum bet amount that this player should go too...
        
                iRaiseAmount,                       //The amount to be raise this round, this the final value returned Player object

                i50Percent,                         //Calculated during runtime, the amount the player has as a percentage
                i30Percent,
                i20Percent,
                i15Percent,
                i10Percent,
                i5Percent,
                i2Percent,
                i1Percent,
            
                iRisk,
                iBluff;

    PlayerAI(Player objPlayer, BettingRoundManager objBettingRoundManager){
        this.objPlayer                      = objPlayer;
        this.objBettingRoundManager         = objBettingRoundManager;
        
        //Transfer the cards to local Cards array to make the program clearer
        this.Cards                          = objPlayer.getCardsRef();
        
        objPlayerCardsEvaluator             = new PlayerCardsEvaluator();
    }
    
    
    
    private void findPlayerPercentageValues()
    {
        i50Percent = Math.round((iPlayerAmount * 50) / 100);
	i30Percent = Math.round((iPlayerAmount * 30) / 100);
	i20Percent = Math.round((iPlayerAmount * 20) / 100);
	i15Percent = Math.round((iPlayerAmount * 15) / 100);
	i10Percent = Math.round((iPlayerAmount * 10) / 100);
	i5Percent = Math.round((iPlayerAmount * 5) / 100);
	i2Percent = Math.round((iPlayerAmount * 2) / 100);
	i1Percent = Math.round((iPlayerAmount * 1) / 100);
    }
    private void findMaximumBetAmount(int iRound)
    {
        int iPlayerRandomDecision;

	iCalculatedMaximumBetAmount = RESET;

	findPlayerPercentageValues();

	//Find the maximum amount that this player will go to, if the table amount exceeds Maximum Bet amount then player folds
	switch(iRound)
	{
            case(BETTING_ROUND_1):
            {
                //If hand is good then set maximum bet amount fairly high so does not fold
                if(objPlayer.getSetValue() >= STRAIGHT)         iCalculatedMaximumBetAmount = ( i50Percent + (i10Percent * iRisk) );		//If has a very good hand bet a max of 50 - 80%
                else if(objPlayer.getSetValue() >= TWO_PAIR)    iCalculatedMaximumBetAmount = ( i30Percent + (i10Percent * iRisk) );            //If has a good hand bet a max of 30 - 60%
                else if(objPlayer.getSetValue() == ONE_PAIR)
                {
                    iPlayerRandomDecision = objRandom.nextInt(3);
                    
                    if(objPlayer.getHandHighCard(1) >= 7)
                        if(iPlayerRandomDecision == 1)          iCalculatedMaximumBetAmount = i20Percent;
                        else                                    iCalculatedMaximumBetAmount = i10Percent;
                    else
                        if(iPlayerRandomDecision == 1)          iCalculatedMaximumBetAmount = i5Percent;
                        else                                    iCalculatedMaximumBetAmount = i1Percent;
                }
                else    iCalculatedMaximumBetAmount = i5Percent;        //i.e a high card only
                
                break;
            }
            case(BETTING_ROUND_2):
            {
                if(objPlayer.getSetValue() >= STRAIGHT)         iCalculatedMaximumBetAmount = i50Percent;   //If has a really good hand go up to a max of 70-80%
                else if(objPlayer.getSetValue() >= TWO_PAIR)    iCalculatedMaximumBetAmount = i30Percent;   //If has a good hand go up to a max of 35-55%
                else if(objPlayer.getSetValue() == ONE_PAIR)
                {
                    iBluff = objRandom.nextInt(100);

                    if(objPlayer.getHandHighCard(1) >= 10)
                        if      (iBluff <= 60)      iCalculatedMaximumBetAmount = i10Percent;               //For 60% of the time go up to target + 10%
                        else if (iBluff <= 80)      iCalculatedMaximumBetAmount = i5Percent;                //For 20% of the time go up to target + 5%
                        else                        iCalculatedMaximumBetAmount = (iRisk * i2Percent);      //For 20% of the time go up to target + (2-6)%
                    else
                        if      (iBluff <= 30)      iCalculatedMaximumBetAmount = i10Percent;               //For 30% of the time go up to target + 10%
                        else if (iBluff <= 60)      iCalculatedMaximumBetAmount = i5Percent;                //For 30% of the time go up to target + 5%
                        else                        iCalculatedMaximumBetAmount = (iRisk * i2Percent);      //For 40% of the time go up to target + (2-6)%
                }
                else	//i.e a high card only
                {
                    iBluff = objRandom.nextInt(100);                                                        //If have only a high card introduce an element of bluff

                    if      (iBluff <= 30)      iCalculatedMaximumBetAmount = i5Percent;                    //For 30% of the time go up to target + 5%
                    else if (iBluff <= 60)      iCalculatedMaximumBetAmount = i2Percent;                    //For 30% of the time go up to target + 2%
                    else                        iCalculatedMaximumBetAmount = iCalculatedTargetBetAmount;   //For 40% of the time go up to target
                }
                
                break;
            }
        }

	//Should the calculated max bet amount be greater than the players amount, ensure that it stays within bounds and can
	//play next round
	if(iCalculatedMaximumBetAmount > iPlayerAmount)
            iCalculatedMaximumBetAmount = (iPlayerAmount - objPlayer.getAnteAmount() - 1);

	//Ensure that the value is below the maximum bet amount permissible this round
	if(iCalculatedMaximumBetAmount > objBettingRoundManager.getMaximumBetAmountThisRound())
            iCalculatedMaximumBetAmount = objBettingRoundManager.getMaximumBetAmountThisRound();
    }
    private void findTargetBetAmount(int iRound)
    {        
        int iPlayerRandomDecision;

	iCalculatedTargetBetAmount = RESET;

	findPlayerPercentageValues();

	//Find the target amount (which may be significantly less than the max amount) 
	switch(iRound)
	{
            case(BETTING_ROUND_1):
            {
                iPlayerRandomDecision = objRandom.nextInt(5);

                switch(iPlayerRandomDecision)
                {
                    case(1):		iCalculatedTargetBetAmount = i5Percent;         break;
                    case(2):		iCalculatedTargetBetAmount = i2Percent;         break;
                    default:		iCalculatedTargetBetAmount = 0;                 break;		
                }
                
                break;
            }
            case(BETTING_ROUND_2):
            {
                if(objPlayer.getSetValue() >= STRAIGHT)         iCalculatedTargetBetAmount = (i15Percent + (i5Percent * iRisk));              //If has a really good hand bet a target of 20-30%
                else if(objPlayer.getSetValue() >= TWO_PAIR)    iCalculatedTargetBetAmount = (i10Percent + (i5Percent * iRisk));              //If has a good hand bet a target of 15-25%
                else if(objPlayer.getSetValue() == ONE_PAIR)
                {
                    iBluff = objRandom.nextInt(100);                                                //If have only a pair introduce an element of bluff

                    if(objPlayer.getHandHighCard(1) >= 7)
                    {
                        if      (iBluff <= 60)  iCalculatedTargetBetAmount = i10Percent;            //For 60% of the time bet 10% of player amount
                        else if (iBluff <= 80)  iCalculatedTargetBetAmount = i5Percent;             //For 20% of the time bet 5% of player amount
                        else                    iCalculatedTargetBetAmount = (iRisk * i2Percent);   //For 20% of the time bet how risky the player is this round * 2% of player amount
                    }
                    else
                    {
                        if      (iBluff <= 35)  iCalculatedTargetBetAmount = i10Percent;            //For 35% of the time bet 10% of player amount
                        else if (iBluff <= 70)  iCalculatedTargetBetAmount = i5Percent;             //For 35% of the time bet 5% of player amount
                        else                    iCalculatedTargetBetAmount = (iRisk * i2Percent);   //For 30% of the time bet how risky the player is this round * 2% of player amount
                    }					
                }
                else if(objPlayer.getSetValue() == HIGH_CARD)
                {
                    iBluff = objRandom.nextInt(100);                                                //If have only a high card introduce an element of bluff

                    if(iBluff <= 35)                                                                //For 35% of the time bet 10% of player amount
                        iCalculatedTargetBetAmount = i10Percent;
                }
                
                break;
            }
	}

	//Ensure that the value is below the maximum bet amount permissible this round
	if(iCalculatedTargetBetAmount > objBettingRoundManager.getMaximumBetAmountThisRound())
            iCalculatedTargetBetAmount = objBettingRoundManager.getMaximumBetAmountThisRound();
    }
    private int findBettingAction(int iRound)
    {
        switch(iRound)
	{
            case(BETTING_ROUND_1):
            {
                if(objBettingRoundManager.getPotRaiseStatus() == POT_NOT_RAISED)
                {
                    //If no one has raised yet then raise only if the risk is high and has at least a pair
                    if( (objPlayer.getSetValue() >= ONE_PAIR) && ( (iRisk == 2) || (iRisk == 3) ) )
                    {
                        if(iCalculatedTargetBetAmount > 0)
                        {
                            iRaiseAmount = iCalculatedTargetBetAmount;
                            
                            return COMPUTER_PLAYER_RAISE;
                        }
                        else
                            return COMPUTER_PLAYER_CHECK;
                    }
                    else
                        return COMPUTER_PLAYER_CHECK;
                }
                else        //Pot has been raised...
                {
                    if(iPlayerAmountToSee <= iCalculatedMaximumBetAmount)   return COMPUTER_PLAYER_SEE;
                    else                                                    return COMPUTER_PLAYER_FOLD;
                }
            }
            case(BETTING_ROUND_2):
            {
                if(objBettingRoundManager.getPotRaiseStatus() == POT_NOT_RAISED)
                {
                    if(iRisk == 1)                          //If the level of risk this player is currently playin gis low then Check also
                        return COMPUTER_PLAYER_CHECK;
                    else
                    {
                        iRaiseAmount = iCalculatedTargetBetAmount;

                        if(iRaiseAmount != 0)   return COMPUTER_PLAYER_RAISE;
                        else                    return COMPUTER_PLAYER_CHECK;
                    }
                }
                else        //Pot has already been raised by someone else, player to aim for target bet amount until maximum bet amount is exceeded (in which case fold)
                {
                    if      ( (iPlayerAmount - iPlayerBetInTotalAmount) < objPlayer.getAnteAmount() )           return COMPUTER_PLAYER_SEE;
                    else if ( (iPlayerAmount - iPlayerAmountToSee) < objPlayer.getAnteAmount() )                return COMPUTER_PLAYER_FOLD;
                    else if (  iPlayerBetInTotalAmount > iCalculatedMaximumBetAmount)                           return COMPUTER_PLAYER_FOLD;
                    else if (  iPlayerAmountToSee > iCalculatedMaximumBetAmount)                                return COMPUTER_PLAYER_FOLD;
                    else if ( (iPlayerBetInTotalAmount+iPlayerAmountToSee) < iCalculatedTargetBetAmount )
                    {
                        iRaiseAmount = (iCalculatedTargetBetAmount - (iPlayerBetInTotalAmount+iPlayerAmountToSee));

                        if(iRaiseAmount != 0)   return COMPUTER_PLAYER_RAISE;
                        else                    return COMPUTER_PLAYER_SEE;
                    }
                    else if( (iPlayerBetInTotalAmount+iPlayerAmountToSee) < iCalculatedMaximumBetAmount )       return COMPUTER_PLAYER_SEE;
                    else                                                                                        return COMPUTER_PLAYER_FOLD;
                }
            }
	}
        
        //If none of the conditions have been met above then fold by default
        return COMPUTER_PLAYER_FOLD;
    }
    
    
    
    void initialiseBettingRound(int iRound, int iPlayerAmount, int iPlayerBetInTotalAmount)
    {
	//Define/determine how risk adverse the player is going to be on this round 1 = low
	iRisk = objRandom.nextInt(3) + 1;     //Produce values bewteen 1, 2 or 3.
        iBluff = RESET;
        
        this.iPlayerAmount = iPlayerAmount;
        
        iPlayerAmountToSee = (objBettingRoundManager.getCurrentTableSeeAmount() - iPlayerBetInTotalAmount);
        
        //Evaluate whether the player cards contain any sets (or not)...
        objPlayer.setSetValue(objPlayerCardsEvaluator.findPlayerSetValue(objPlayer));
        
        //Finds (iCalculatedMaximumBetAmount)
        findMaximumBetAmount(iRound);
        
        //Finds (iCalculatedTargetBetAmount)
        findTargetBetAmount(iRound);
    }
    int findPlayerAIBettingAction(int iRound, int iPlayerAmount, int iPlayerBetInTotalAmount)
    {        
        //Find and set the values that are specific to this 'go' in the Betting Round...
        this.iPlayerAmount = iPlayerAmount;
        iPlayerAmountToSee = (objBettingRoundManager.getCurrentTableSeeAmount() - iPlayerBetInTotalAmount);
        this.iPlayerBetInTotalAmount = iPlayerBetInTotalAmount;

        return findBettingAction(iRound);
    }
    void findPlayerAIChangeCardsAction()
    {
        /*The set value would have already been discovered from the first betting round, if lower than a Two Pair then
        evaluate to see if any other sets exist*/
        
        if(objPlayer.getSetValue() < TWO_PAIR)
            objPlayerCardsEvaluator.findPlayerSetValueExact(objPlayer);
        
        //Now highlight the cards to keep and those to change...
        for(int i = 1 ; i <= NUMBER_OF_CARDS_PER_HAND ; i++)
            if(Cards[i].getInSet())
                Cards[Cards[i].getTablePosition()].setHoldState(CARD_HOLD_ON);
    }
    int getRaiseAmount()
    {
        return iRaiseAmount;
    }
}
