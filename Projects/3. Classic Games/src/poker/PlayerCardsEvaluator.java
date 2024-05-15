package poker;

class PlayerCardsEvaluator implements Constants{

    private Player                          objPlayer;                  //To provide access of the object to other methods in the class
    private TemporaryEvaluationCard         Cards[] = new TemporaryEvaluationCard[NUMBER_OF_CARDS_PER_HAND + 1];
    
    PlayerCardsEvaluator()
    {
        for(int i = 1 ; i<=NUMBER_OF_CARDS_PER_HAND ; i++)
            Cards[i] = new TemporaryEvaluationCard();
    } 
    
    
    
    private void initPlayerCardsEvaluator()
    {
        //On each Betting Round re-initialise the local cards array which is used for evaluation purposes
        for(int i = 1 ; i<=NUMBER_OF_CARDS_PER_HAND ; i++)
        {
            objPlayer.setHandHighCard(i, RESET);
            setPlayerCardInSet(i, true);
        }            
    }
    private boolean arePlayerCardsSequential()
    {
	//Now starting with the lowest [1] see if all are sequential...
	for(int i = 1 ; i <= (NUMBER_OF_CARDS_PER_HAND - 1) ; i++)
            if( (Cards[i].getValue() + 1) != (Cards[i+1].getValue()) )
                return false;

	return true;
    }
    private boolean arePlayerCardsSameSuit()
    {
	for(int i = 1 ; i<NUMBER_OF_CARDS_PER_HAND ; i++)
            if(Cards[i].getSuit() != Cards[i+1].getSuit())
                return false;
        
        return true;
    }
    private boolean arePlayerCardsRoyalFlush()
    {
        //Evaluate for a Royal Flush (10-ACE of any suit)
   	if( (arePlayerCardsSequential() == true) && (arePlayerCardsSameSuit() == true) && (Cards[NUMBER_OF_CARDS_PER_HAND].getValue() == ACE) )
	{
            objPlayer.setHandHighCard(1, Cards[5].getValue());           
            return true;
	}
	else
            return false;
    }
    private boolean arePlayerCardsStraightFlush()
    {
        if( (arePlayerCardsSequential() == true) && (arePlayerCardsSameSuit() == true) )
	{
            objPlayer.setHandHighCard(1, Cards[5].getValue());
            return true;
	}
	else
            return false;
    }
    private boolean arePlayerCardsFourOfAKind()
    {
        for(int i = 1 ; i <= (NUMBER_OF_CARDS_PER_HAND - 3) ; i++)
	{
            if( (Cards[i].getValue() == Cards[i+1].getValue()) && (Cards[i].getValue() == Cards[i+2].getValue()) && (Cards[i].getValue() == Cards[i+3].getValue()) )
            {
                switch(i)
                {   //11112
                    case(1):	
                    {
                        setPlayerCardInSet(5, false);

                        objPlayer.setHandHighCard(1, Cards[1].getValue());
                        objPlayer.setHandHighCard(2, Cards[5].getValue());
                        
                        break;
                    }
                    //12222
                    case(2):
                    {
                        setPlayerCardInSet(1, false);

                        objPlayer.setHandHighCard(1, Cards[5].getValue());
                        objPlayer.setHandHighCard(2, Cards[1].getValue());
                        
                        break;
                    }
                }
                
                return true;
            }
	}

	return false;
    }
    private boolean arePlayerCardsFullHouse()
    {
        int iMatches_Found;
    
	iMatches_Found = 0;

        //If first three are a match then proceed to check the remaining 2
	for(int i = 1 ; i <= (NUMBER_OF_CARDS_PER_HAND - 3) ; i++)
            if(Cards[1].getValue() == Cards[i+1].getValue())
                iMatches_Found = iMatches_Found + 1;

	if(iMatches_Found == 2)
	{
            //Now check the last two cards
            if(Cards[4].getValue() == Cards[5].getValue())
            {
                if(Cards[1].getValue() > Cards[5].getValue())
                {
                        objPlayer.setHandHighCard(1, Cards[1].getValue());
                        objPlayer.setHandHighCard(2, Cards[5].getValue());
                }
                else
                {
                        objPlayer.setHandHighCard(1, Cards[5].getValue());
                        objPlayer.setHandHighCard(2, Cards[1].getValue());
                }
                
                return true;
            }
            else
                return false;
	}

	if(iMatches_Found == 1)
	{
            //Now check the last three cards
            if( (Cards[3].getValue() == Cards[4].getValue()) && (Cards[3].getValue() == Cards[5].getValue()) )
            {
                if(Cards[1].getValue() > Cards[5].getValue())
                {
                    objPlayer.setHandHighCard(1, Cards[1].getValue());
                    objPlayer.setHandHighCard(2, Cards[5].getValue());
                }
                else
                {
                    objPlayer.setHandHighCard(1, Cards[5].getValue());
                    objPlayer.setHandHighCard(2, Cards[1].getValue());
                }

                return true;
            }
            else
                return false;
	}
        
        return false;
    }
    private boolean arePlayerCardsFlush()
    {
        //Are all of the cards the same suit
	for(int i = 1 ; i <= NUMBER_OF_CARDS_PER_HAND ; i++)
            if(Cards[1].getSuit() != Cards[i].getSuit())
                return false;
    
        objPlayer.setHandHighCard(1, Cards[5].getValue());
        objPlayer.setHandHighCard(2, Cards[4].getValue());
        objPlayer.setHandHighCard(3, Cards[3].getValue());
        objPlayer.setHandHighCard(4, Cards[2].getValue());
        objPlayer.setHandHighCard(5, Cards[1].getValue());

	return true;
    }
    private boolean arePlayerCardsStraight()
    {
        //Are all of the cards sequential
	if(arePlayerCardsSequential())
	{
            objPlayer.setHandHighCard(1, Cards[HIGHEST_VALUE_CARD].getValue());
            
            objPlayer.setHandHighCard(1, Cards[5].getValue());
            objPlayer.setHandHighCard(2, Cards[4].getValue());
            objPlayer.setHandHighCard(3, Cards[3].getValue());
            objPlayer.setHandHighCard(4, Cards[2].getValue());
            objPlayer.setHandHighCard(5, Cards[1].getValue());

            return true;
	}
	else
            return false;
    }
    private boolean arePlayerCardsThreeOfAKind()
    {
        for(int i = 1 ; i<=(NUMBER_OF_CARDS_PER_HAND - 2) ; i++)
	{
            if( (Cards[i].getValue() == Cards[i+1].getValue()) && (Cards[i].getValue() == Cards[i+2].getValue()))
            {
                switch(i)
                {
                    case(1):
                    {
                        setPlayerCardInSet(4, false);
                        setPlayerCardInSet(5, false);

                        //Find the value of the second and third value cards
                        if(Cards[4].getValue() > Cards[5].getValue())
                        {
                            objPlayer.setHandHighCard(2, Cards[4].getValue());
                            objPlayer.setHandHighCard(3, Cards[5].getValue());
                        }
                        else if(Cards[4].getValue() < Cards[5].getValue())
                        {
                            objPlayer.setHandHighCard(2, Cards[5].getValue());
                            objPlayer.setHandHighCard(3, Cards[4].getValue());
                        }
                        
                        break;
                    }
                    case(2):
                    {
                        setPlayerCardInSet(1, false);
                        setPlayerCardInSet(5, false);

                        //Find the value of the second and third value cards
                        if(Cards[1].getValue() > Cards[5].getValue())
                        {
                            objPlayer.setHandHighCard(2, Cards[1].getValue());
                            objPlayer.setHandHighCard(3, Cards[5].getValue());
                        }
                        else if(Cards[1].getValue() < Cards[5].getValue())
                        {
                            objPlayer.setHandHighCard(2, Cards[5].getValue());
                            objPlayer.setHandHighCard(3, Cards[1].getValue());
                        }
                        
                        break;
                    }
                    case(3):
                    {
                        setPlayerCardInSet(1, false);
                        setPlayerCardInSet(2, false);

                        //Find the value of the second and third value cards
                        if(Cards[1].getValue() > Cards[2].getValue())
                        {
                            objPlayer.setHandHighCard(2, Cards[1].getValue());
                            objPlayer.setHandHighCard(3, Cards[2].getValue());
                        }
                        else if(Cards[1].getValue() < Cards[2].getValue())
                        {
                            objPlayer.setHandHighCard(2, Cards[2].getValue());
                            objPlayer.setHandHighCard(3, Cards[1].getValue());
                        }
                        
                        break;
                    }
                }

                objPlayer.setHandHighCard(1, Cards[i].getValue());

                return true;
            }
	}

	return false;
    }
    private boolean arePlayerCardsTwoPair()
    {
        for(int i = 1 ; i<=(NUMBER_OF_CARDS_PER_HAND - 3) ; i++)
	{
            if(Cards[i].getValue() == Cards[i+1].getValue())
            {
                for(int j = i+2 ; j<=(NUMBER_OF_CARDS_PER_HAND - 1) ; j++)
                {
                    //Now find the second pair...
                    if(Cards[j].getValue() == Cards[j+1].getValue())
                    {
                        //11223
                        if((i == 1) && (j == 3))
                        {	
                            if( (Cards[1].getValue()) > (Cards[3].getValue()) )
                            {
                                objPlayer.setHandHighCard(1, Cards[1].getValue());
                                objPlayer.setHandHighCard(2, Cards[3].getValue());
                            }
                            else
                            {
                                objPlayer.setHandHighCard(1, Cards[3].getValue());
                                objPlayer.setHandHighCard(2, Cards[1].getValue());
                            }

                            setPlayerCardInSet(5, false);
                            objPlayer.setHandHighCard(3, Cards[5].getValue());
                        }//11233
                        else if((i == 1) && (j == 4))
                        {
                            if( (Cards[1].getValue()) > (Cards[4].getValue()) )
                            {	
                                    objPlayer.setHandHighCard(1, Cards[1].getValue());
                                    objPlayer.setHandHighCard(2, Cards[4].getValue());
                            }
                            else
                            {	
                                    objPlayer.setHandHighCard(1, Cards[4].getValue());
                                    objPlayer.setHandHighCard(2, Cards[1].getValue());
                            }

                            setPlayerCardInSet(3, false);
                            objPlayer.setHandHighCard(3, Cards[3].getValue());
                        }//12233
                        else if((i == 2) && (j == 4))
                        {
                            if( (Cards[2].getValue()) > (Cards[4].getValue()) )
                            {	
                                objPlayer.setHandHighCard(1, Cards[2].getValue());
                                objPlayer.setHandHighCard(2, Cards[4].getValue());
                            }
                            else
                            {	
                                objPlayer.setHandHighCard(1, Cards[4].getValue());
                                objPlayer.setHandHighCard(2, Cards[2].getValue());
                            }
                            
                            setPlayerCardInSet(1, false);
                            objPlayer.setHandHighCard(3, Cards[1].getValue());
                        }

                    return true;
                    }
                }
            }
        }

	return false;
    }
    private boolean isHighestCardGreaterThanTen()
    {
	//If the last playing card is greater than 10 then hold it and replace the others...
	if(Cards[5].getValue() > 10)
	{
            for(int i = 1 ; i <= NUMBER_OF_CARDS_PER_HAND ; i++)
                setPlayerCardInSet(i, false);

            setPlayerCardInSet(5, true);

            return true;
	}

        return false;
    }
    private boolean arePlayerCardsFourCardStraight()
    {
        //Now starting with the lowest and see if encounter a four card straight...
	for(int i = 1 ; i<=(NUMBER_OF_CARDS_PER_HAND - 3) ; i++)
	{
            if((Cards[i].getValue() + 1) == (Cards[i+1].getValue()) && (Cards[i].getValue() + 2) == (Cards[i+2].getValue()) && (Cards[i].getValue() + 3) == (Cards[i+3].getValue()))
            {
                if(i == 1)
                {
                    setPlayerCardInSet(5, false);
                    setPlayerCardInSet(4, true);
                    setPlayerCardInSet(3, true);
                    setPlayerCardInSet(2, true);
                    setPlayerCardInSet(1, true);  
                }
                else if(i == 2)
                {
                    setPlayerCardInSet(5, true);
                    setPlayerCardInSet(4, true);
                    setPlayerCardInSet(3, true);
                    setPlayerCardInSet(2, true);
                    setPlayerCardInSet(1, false);
                }

                return true;
            }
        }

        return false;
    }
    private boolean arePlayerCardsFourSameSuit()
    {
        int iNumberOfSpades, iNumberOfClubs, iNumberOfHearts, iNumberOfDiamonds;

	iNumberOfSpades         = RESET;
	iNumberOfClubs          = RESET;
	iNumberOfHearts         = RESET;
	iNumberOfDiamonds       = RESET;

	for(int i = 1 ; i<= NUMBER_OF_CARDS_PER_HAND ; i++)
	{
            switch(Cards[i].getSuit())
            {
                case(SPADES):		iNumberOfSpades++;              break;
                case(CLUBS):		iNumberOfClubs++;               break;
                case(HEARTS):		iNumberOfHearts++;              break;
                case(DIAMONDS):         iNumberOfDiamonds++;            break;
            }
	}
	
	if(iNumberOfSpades == 4)
	{
            //Find the non spade and replace...
            for(int i = 1 ; i <= NUMBER_OF_CARDS_PER_HAND ; i++)
                if(Cards[i].getSuit() != SPADES)    setPlayerCardInSet(i, false);
                else                                setPlayerCardInSet(i, true);

            return true;
	}
	else if(iNumberOfClubs == 4)
	{
            //Find the non spade and replace...
            for(int i = 1 ; i <= NUMBER_OF_CARDS_PER_HAND ; i++)
                if(Cards[i].getSuit() != CLUBS)     setPlayerCardInSet(i, false);
                else                                setPlayerCardInSet(i, true);

            return true;
	}
	else if(iNumberOfHearts == 4)
	{
            //Find the non spade and replace...
            for(int i = 1 ; i <= NUMBER_OF_CARDS_PER_HAND ; i++)
                if(Cards[i].getSuit() != HEARTS)    setPlayerCardInSet(i, false);
                else                                setPlayerCardInSet(i, true);

            return true;
	}
	else if(iNumberOfDiamonds == 4)
	{
            //Find the non spade and replace...
            for(int i = 1 ; i <= NUMBER_OF_CARDS_PER_HAND ; i++)
                if(Cards[i].getSuit() != DIAMONDS)  setPlayerCardInSet(i, false);
                else                                setPlayerCardInSet(i, true);

            return true;
	}
        
        return false;
    }
    private boolean arePlayerCardsThreeCardStraight()
    {
        //Now starting with the lowest and see if encounter a three card straight...
	for(int i = 1 ; i<=(NUMBER_OF_CARDS_PER_HAND - 2) ; i++)
	{
            if( ((Cards[i].getValue() + 1) == (Cards[i+1].getValue())) && ((Cards[i].getValue() + 2) == (Cards[i+2].getValue())) )
            {
                switch(i)
                {
                    case 1:
                    {
                        setPlayerCardInSet(1, true);
                        setPlayerCardInSet(2, true);
                        setPlayerCardInSet(3, true);
                        setPlayerCardInSet(4, false);
                        setPlayerCardInSet(5, false);
                        
                        break;
                    }
                    case 2:
                    {
                        setPlayerCardInSet(1, false);
                        setPlayerCardInSet(2, true);
                        setPlayerCardInSet(3, true);
                        setPlayerCardInSet(4, true);
                        setPlayerCardInSet(5, false);
                        
                        break;
                    }
                    case 3:
                    {
                        setPlayerCardInSet(1, false);
                        setPlayerCardInSet(2, false);
                        setPlayerCardInSet(3, true);
                        setPlayerCardInSet(4, true);
                        setPlayerCardInSet(5, true);
                        
                        break;
                    }
                }

                return true;
            }
	}

	return false;
    }
    private boolean arePlayerCardsOnePair()
    {
        for(int i = 1 ; i<=(NUMBER_OF_CARDS_PER_HAND - 1) ; i++)
	{
            if((Cards[i].getValue()) == (Cards[i+1].getValue()))
            {
                switch(i)
                {
                    case(1):
                    {
                        setPlayerCardInSet(3, false);
                        setPlayerCardInSet(4, false);
                        setPlayerCardInSet(5, false);

                        objPlayer.setHandHighCard(2, Cards[5].getValue());
                        objPlayer.setHandHighCard(3, Cards[4].getValue());
                        objPlayer.setHandHighCard(4, Cards[3].getValue());
                        
                        break;
                    }
                    case(2):
                    {
                        setPlayerCardInSet(1, false);
                        setPlayerCardInSet(4, false);
                        setPlayerCardInSet(5, false);

                        objPlayer.setHandHighCard(2, Cards[5].getValue());
                        objPlayer.setHandHighCard(3, Cards[4].getValue());
                        objPlayer.setHandHighCard(4, Cards[1].getValue());
                        
                        break;
                    }
                    case(3):
                    {
                        setPlayerCardInSet(1, false);
                        setPlayerCardInSet(2, false);
                        setPlayerCardInSet(5, false);

                        objPlayer.setHandHighCard(2, Cards[5].getValue());
                        objPlayer.setHandHighCard(3, Cards[2].getValue());
                        objPlayer.setHandHighCard(4, Cards[1].getValue());
                        
                        break;
                    }
                    case(4):
                    {
                        setPlayerCardInSet(1, false);
                        setPlayerCardInSet(2, false);
                        setPlayerCardInSet(3, false);

                        objPlayer.setHandHighCard(2, Cards[3].getValue());
                        objPlayer.setHandHighCard(3, Cards[2].getValue());
                        objPlayer.setHandHighCard(4, Cards[1].getValue());
                        
                        break;
                    }
                }
            
                objPlayer.setHandHighCard(1, Cards[i].getValue());

                return true;
            }
	}

	return false;	

    }
    private void replaceAllCards()
    {
        for(int i = 1 ; i <= NUMBER_OF_CARDS_PER_HAND ; i++)
            setPlayerCardInSet(i, false);
    }
    private void findPlayerHighestCard()
    {
	for(int i = 1 ; i<=NUMBER_OF_CARDS_PER_HAND ; i++)
            setPlayerCardInSet(i, false);

        setPlayerCardInSet(5, true);

        objPlayer.setHandHighCard(1, Cards[5].getValue());
        objPlayer.setHandHighCard(2, Cards[4].getValue());
        objPlayer.setHandHighCard(3, Cards[3].getValue());
        objPlayer.setHandHighCard(4, Cards[2].getValue());
        objPlayer.setHandHighCard(5, Cards[1].getValue());
    }
    private void sortPlayerCardsIntoAscendingOrder()
    {
        //This method shall, arrange the cards into ascending order locally within this object (i.e. not on the actual
        //cards on the table.
        int iSortLength,
            CardLowerTempValue, CardLowerTempSuit, CardLowerTempPosition,
            CardHigherTempValue, CardHigherTempSuit, CardHigherTempPosition;
        
        boolean bSwapped;
        
        iSortLength = NUMBER_OF_CARDS_PER_HAND;
        
        do
	{
            bSwapped = false;
            for(int i = 2 ; i<= iSortLength ; i++)
            {
                if(Cards[i-1].getValue() > Cards[i].getValue())
                {
                    CardLowerTempValue          = Cards[i].getValue();
                    CardLowerTempSuit           = Cards[i].getSuit();
                    CardLowerTempPosition       = Cards[i].getTablePosition();

                    CardHigherTempValue         = Cards[i-1].getValue();
                    CardHigherTempSuit          = Cards[i-1].getSuit();
                    CardHigherTempPosition      = Cards[i-1].getTablePosition();

                    //Swap around...

                    Cards[i].setValue(CardHigherTempValue);
                    Cards[i].setSuit(CardHigherTempSuit);
                    Cards[i].setTablePosition(CardHigherTempPosition);

                    Cards[i-1].setValue(CardLowerTempValue);
                    Cards[i-1].setSuit(CardLowerTempSuit);
                    Cards[i-1].setTablePosition(CardLowerTempPosition);
                    
                    bSwapped = true;
                }
            }
		
            iSortLength = iSortLength - 1;
	}while(bSwapped);
    }
    
    
    
    void setPlayerCardInSet(int iCard, boolean bState)
    {
        objPlayer.setCardInSet(Cards[iCard].getTablePosition(), bState);
    }
    int findPlayerSetValue(Player objPlayer)
    {
        this.objPlayer = objPlayer;
        //Transfer the cards to local Cards array to make the program clearer

        for(int i = 1 ; i<=NUMBER_OF_CARDS_PER_HAND ; i++)
        {
            Cards[i].setSuit(objPlayer.getCardSuit(i));
            Cards[i].setValue(objPlayer.getCardValue(i));
            Cards[i].setTablePosition(objPlayer.getCardTablePosition(i));
        }
        
        initPlayerCardsEvaluator();
        
        sortPlayerCardsIntoAscendingOrder();
        
        if      (arePlayerCardsRoyalFlush()){                   return(ROYAL_FLUSH);}
        else if (arePlayerCardsStraightFlush()){                return(STRIAGHT_FLUSH);}  
        else if (arePlayerCardsFourOfAKind()){                  return(FOUR_OF_A_KIND);}
        else if (arePlayerCardsFullHouse()){                    return(FULL_HOUSE);}
        else if (arePlayerCardsFlush()){                        return(FLUSH);}
        else if (arePlayerCardsStraight()){                     return(STRAIGHT);}
        else if (arePlayerCardsThreeOfAKind()){                 return(THREE_OF_KIND);}
        else if (arePlayerCardsTwoPair()){                      return(TWO_PAIR);}
        else if (arePlayerCardsOnePair()){                      return(ONE_PAIR);}
        else{    findPlayerHighestCard();                       return(HIGH_CARD);}
    }
    int findPlayerSetValueExact(Player objPlayer)
    {
        this.objPlayer = objPlayer;
        //Transfer the cards to local Cards array to make the program clearer
        
        for(int i = 1 ; i<=NUMBER_OF_CARDS_PER_HAND ; i++)
        {
            Cards[i].setSuit(objPlayer.getCardSuit(i));
            Cards[i].setValue(objPlayer.getCardValue(i));
            Cards[i].setTablePosition(objPlayer.getCardTablePosition(i));
        }
        
        initPlayerCardsEvaluator();
        
        sortPlayerCardsIntoAscendingOrder();
        
        if(arePlayerCardsFourCardStraight())        return(FOUR_CARD_STRAIGHT);
        else if(arePlayerCardsFourSameSuit())       return(FOUR_SAME_SUIT);
        else if(arePlayerCardsOnePair())            return(ONE_PAIR);
        else if(arePlayerCardsThreeCardStraight())  return(THREE_CARD_STRAIGHT);
        else if(isHighestCardGreaterThanTen())      return(KEEP_HIGHEST_CARD);
        else
        {
            replaceAllCards();
            
            return(REPLACE_ALL_CARDS);    
        }           
    }
}
