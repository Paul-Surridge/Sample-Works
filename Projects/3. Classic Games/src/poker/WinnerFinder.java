package poker;

class WinnerFinder implements Constants {

    private Player Players[]                                        = new Player[NUMBER_OF_PLAYERS + 1];
    private Player PlayersEvaluation[]                              = new Player[NUMBER_OF_PLAYERS + 1];

    private PlayerCardsEvaluator objPlayerCardsEvaluator            = new PlayerCardsEvaluator();

    private int iWinningSetValue, iNumberOfRemainingActivePlayersInRound;                  //The set (if any) of the players current cards
              
    WinnerFinder(Player Players[])
    {        
        this.Players = Players;
    }
    
    
    
    private void findNumberOfRemainingActivePlayersInRound()
    {
        iNumberOfRemainingActivePlayersInRound = RESET;
        
        for(int i = 1 ; i<=NUMBER_OF_PLAYERS ; i++)
            if(Players[i].getInRound())
                iNumberOfRemainingActivePlayersInRound++;
    }
    private void findPlayersSetValueInDescendingOrder()
    {
        Player iLowerPlayer, iHigherPlayer;
        
        int iSortLength, iCounter;        
        boolean bSwapped;
    
	iSortLength = iNumberOfRemainingActivePlayersInRound;

        //Read in their value and then sort...
        iCounter = RESET;
        
        for(int i = 1 ; i<=NUMBER_OF_PLAYERS ; i++)
            PlayersEvaluation[i] = null;
        
        for(int i = 1 ; i<=NUMBER_OF_PLAYERS ; i++)
            if(Players[i].getInRound())
            {
                iCounter++;
                PlayersEvaluation[iCounter] = Players[i];               
            }
        
	do
	{
            bSwapped = false;
            
            for(int i = 2 ; i<=iSortLength ; i++)
            {
                if(PlayersEvaluation[i-1].getSetValue() < PlayersEvaluation[i].getSetValue())
                {
                    iHigherPlayer          = PlayersEvaluation[i];
                    iLowerPlayer           = PlayersEvaluation[i-1];
                    
                    //Swap around...
                    
                    PlayersEvaluation[i]             = iLowerPlayer;
                    PlayersEvaluation[i-1]           = iHigherPlayer;

                    bSwapped = true;
                }
            }
		
            iSortLength = (iSortLength - 1);
	}while(bSwapped);
    }
    private void findPlayersHighCardsInDescendingOrder(int iCard, int iNumberOfPlayersWithSameValueToCompare)
    {
        //This method will sort the highards array into order about iCard e.g. will sort using the second highest card in the players hands (should have the 1st been the same)

        Player iLowerPlayer, iHigherPlayer;
        
        int iSortLength;          
        boolean bSwapped;
    
	iSortLength = iNumberOfPlayersWithSameValueToCompare;
        
	do
	{
            bSwapped = false;
            
            for(int i = 2 ; i<=iSortLength ; i++)
                if(PlayersEvaluation[i-1].getHandHighCard(iCard) < PlayersEvaluation[i].getHandHighCard(iCard))
                {
                    iLowerPlayer    = PlayersEvaluation[i];
                    iHigherPlayer   = PlayersEvaluation[i-1];
                    
                    //Swap around...
                    
                    PlayersEvaluation[i]    = iHigherPlayer;
                    PlayersEvaluation[i-1]  = iLowerPlayer;

                    bSwapped = true;
                }
		
            iSortLength = (iSortLength - 1);
	}while(bSwapped);
    }
    
    
    
    int checkForSingleRemainingWinningPlayer()
    {
        int iNumberOfPlayersInRound = RESET;
        
        //Go around all players and see if found any winners...
        for(int i = 1 ; i <= NUMBER_OF_PLAYERS ; i++)
            if(Players[i].getInRound())
                iNumberOfPlayersInRound++;
        
        //Then only a single person is still in the round therefore the winner.
        if(iNumberOfPlayersInRound == 1)
            for(int i = 1 ; i <= NUMBER_OF_PLAYERS ; i++)
                if(Players[i].getInRound())
                    return i;
        
        return NULL;
    }
    int findWinnerOfRound(Player Players[])
    {
        //Initialise all objects and event handling code...
        this.Players = Players;
        
        int iNumberOfPlayersToCompare, iCardToEvaluate, iPlayerToEvaluate;
        boolean bGameTie, bAllPlayersCurrentHighCardBeingEvaluatedSame;
        
        findNumberOfRemainingActivePlayersInRound();
        
        //Re-evaluate the players cards setvalue...
        for(int i = 1 ; i<=NUMBER_OF_PLAYERS ; i++)
            if(Players[i].getInRound())
                Players[i].setSetValue(objPlayerCardsEvaluator.findPlayerSetValue(Players[i]));
        
        //Now sort them into assending order using the iSetValue i.e. hopefully find a winner...
        findPlayersSetValueInDescendingOrder();
        
        //The highest set value of this round will never change, therefore this can be assigned at this point, the highcard of the hand is determinied when the
        //actual hand has been found should there be more than one hand with the highest set value
        iWinningSetValue = PlayersEvaluation[1].getSetValue();
        
        //Immediate Clear Winner
        //Then a clear winner has already been found and do not have any x2 players with the same Set Value
        if(PlayersEvaluation[1].getSetValue() > PlayersEvaluation[2].getSetValue())
            return PlayersEvaluation[1].getPlayerNumber();
        
        //If reach this point then no clear winner has been found using Set Value alone i.e. the winning Set Value is with more than one player therefore
        //need to compare high cards of those players who have the highest set value...
        
	iNumberOfPlayersToCompare = RESET;

        //Find the number of players which have matching winning set values...
        for(int i = 1 ; i<=iNumberOfRemainingActivePlayersInRound ; i++)
            if( (PlayersEvaluation[i].getSetValue()) == iWinningSetValue)
                iNumberOfPlayersToCompare++;

        //Evaluate card 1 of the top iNumberOfPlayersToCompare players who have winning sets...
        findPlayersHighCardsInDescendingOrder(1, iNumberOfPlayersToCompare);
        
        //Then a clear winner has been found such that out of the 2 (or more) players who have matching set values have different high cards
        if(PlayersEvaluation[1].getHandHighCard(1) > PlayersEvaluation[2].getHandHighCard(1))
            return PlayersEvaluation[1].getPlayerNumber();
        
        //If by chance find that there are 2 or more players with both the same set value and the same 1st high card then need to cycle through all other cards in hand until find the
        //player with the highest card (if by small chance, 2 or more top players with same set value and identical high card array then game a tie)
        
        //Now compare high cards to determine who is the winner, if by extremely small chance that the all players to compare have exactly the same cards then game is a tie
        bGameTie = false;
        iCardToEvaluate = 1;
        
        iPlayerToEvaluate = iNumberOfPlayersToCompare;
        
        //The first do loop will shift onto the appropriate card that we are looking at e.g. the 1st, 2nd ... card in the sorted highcard array.
        //Once a situation occurs when e.g. looking at a particular card a single player (who already confirmed has the winning set value) has a card which is uniquly the highest
        //then we have a winner...
        do
        {
            iNumberOfPlayersToCompare = iPlayerToEvaluate;

            iPlayerToEvaluate = 1;
            bAllPlayersCurrentHighCardBeingEvaluatedSame = false;

            //Sort the Players as per iCardToEvaluate current value i.e. starting from 1 - 5 until a winner is found...
            findPlayersHighCardsInDescendingOrder(iCardToEvaluate, iNumberOfPlayersToCompare);

            //This inner do loop will now compare all of the iCardToEvaluate(th) cards in the winning players array to find a single unique high card who would be deemed the winner
            do
            {
                if( (PlayersEvaluation[iPlayerToEvaluate].getHandHighCard(iCardToEvaluate)) > (PlayersEvaluation[iPlayerToEvaluate+1].getHandHighCard(iCardToEvaluate)) && (iPlayerToEvaluate == 1) )
                    return PlayersEvaluation[iPlayerToEvaluate].getPlayerNumber();
                else
                    //A winner has not been found therefore need to more on the next remaining players in the highcard array and compare accordingly to find the number of players
                    //which have the same high cards.
                    iPlayerToEvaluate = iPlayerToEvaluate + 1;
                
                if(iPlayerToEvaluate >= iNumberOfPlayersToCompare)
                    bAllPlayersCurrentHighCardBeingEvaluatedSame = true;
                
            }while(!bAllPlayersCurrentHighCardBeingEvaluatedSame);

            //Out of the players who had the same Set Value. all of them also had the same high card in their hand, therefore move onto next card.
            iCardToEvaluate = iCardToEvaluate + 1;

            //Have gone through all cards and have not found a winner i.e. >1 players have same set value comprising of the same card values, therefore a tie, replay game...
            if( (iCardToEvaluate > NUMBER_OF_CARDS_PER_HAND) && (iPlayerToEvaluate != 1) )
                bGameTie = true;

        }while(!bGameTie);

        if(bGameTie)
            return GAME_TIE;

        return NULL;
    }
}
