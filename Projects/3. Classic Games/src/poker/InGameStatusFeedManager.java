package poker;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.util.Duration;

class InGameStatusFeedManager implements Constants{

    private Timeline    tlShowInGameStatusMessage, tlShowInGameStatusPlayerWinOrExitMessage, tlHideInGameStatusPlayerWinOrExitMessage;
    private Label       labGameStatusUpdate;
    private Label       labInGameStatusFeedPlayerWinOrExit;
    
    InGameStatusFeedManager(Label labGameStatusUpdate, Label labInGameStatusFeedPlayerWinOrExit) {
        
        this.labGameStatusUpdate = labGameStatusUpdate;
        this.labInGameStatusFeedPlayerWinOrExit = labInGameStatusFeedPlayerWinOrExit;

        labGameStatusUpdate.setOpacity(NOT_VISIBLE);
        labGameStatusUpdate.setText("");
    }
    
    
    
    private String getWinningSetValueName(int iSetValue)
    {
        switch(iSetValue)
        {
            case(ROYAL_FLUSH):          return "Royal Flush";
            case(STRIAGHT_FLUSH):       return "Straight Flush";
            case(FOUR_OF_A_KIND):       return "Four of a Kind";
            case(FULL_HOUSE):           return "Full House";
            case(FLUSH):                return "Flush";
            case(STRAIGHT):             return "Straight";
            case(THREE_OF_KIND):        return "Three of a Kind";
            case(TWO_PAIR):             return "Two Pair";
            case(ONE_PAIR):             return "One Pair";
            case(HIGH_CARD):            return "High Card";
            default:                    return "High Card";
        }
    }
    private void showPlayerWinOrExitMessage(String sMessage)
    {
        tlShowInGameStatusPlayerWinOrExitMessage = new Timeline();
        tlShowInGameStatusPlayerWinOrExitMessage.setCycleCount(1);

        tlShowInGameStatusPlayerWinOrExitMessage.getKeyFrames().add(new KeyFrame(Duration.millis(0),       new KeyValue(labInGameStatusFeedPlayerWinOrExit.opacityProperty(), 1)));
        tlShowInGameStatusPlayerWinOrExitMessage.getKeyFrames().add(new KeyFrame(Duration.millis(200),     new KeyValue(labInGameStatusFeedPlayerWinOrExit.opacityProperty(), 0)));
        tlShowInGameStatusPlayerWinOrExitMessage.getKeyFrames().add(new KeyFrame(Duration.millis(300),     new KeyValue(labInGameStatusFeedPlayerWinOrExit.textProperty(), sMessage)));
        tlShowInGameStatusPlayerWinOrExitMessage.getKeyFrames().add(new KeyFrame(Duration.millis(400),     new KeyValue(labInGameStatusFeedPlayerWinOrExit.opacityProperty(), 0)));
        tlShowInGameStatusPlayerWinOrExitMessage.getKeyFrames().add(new KeyFrame(Duration.millis(600),     new KeyValue(labInGameStatusFeedPlayerWinOrExit.opacityProperty(), 1)));
        
        tlShowInGameStatusPlayerWinOrExitMessage.playFromStart();
    }
    
    
    
    void showMessage(String sMessage)
    {
        //Run the show message timeline...
        tlShowInGameStatusMessage = new Timeline();
        tlShowInGameStatusMessage.setCycleCount(1);

        tlShowInGameStatusMessage.getKeyFrames().add(new KeyFrame(Duration.millis(0),       new KeyValue(labGameStatusUpdate.opacityProperty(), 1)));
        tlShowInGameStatusMessage.getKeyFrames().add(new KeyFrame(Duration.millis(200),     new KeyValue(labGameStatusUpdate.opacityProperty(), 0)));
        tlShowInGameStatusMessage.getKeyFrames().add(new KeyFrame(Duration.millis(300),     new KeyValue(labGameStatusUpdate.textProperty(), sMessage)));
        tlShowInGameStatusMessage.getKeyFrames().add(new KeyFrame(Duration.millis(400),     new KeyValue(labGameStatusUpdate.opacityProperty(), 0)));
        tlShowInGameStatusMessage.getKeyFrames().add(new KeyFrame(Duration.millis(600),     new KeyValue(labGameStatusUpdate.opacityProperty(), 1)));
        
        tlShowInGameStatusMessage.playFromStart();
    }
    void hidePlayerWinOrExitMessage()
    {
        if(labInGameStatusFeedPlayerWinOrExit.getOpacity() == VISIBLE)
        {
            tlHideInGameStatusPlayerWinOrExitMessage = new Timeline();
            tlHideInGameStatusPlayerWinOrExitMessage.setCycleCount(1);

            tlHideInGameStatusPlayerWinOrExitMessage.getKeyFrames().add(new KeyFrame(Duration.millis(0),       new KeyValue(labInGameStatusFeedPlayerWinOrExit.opacityProperty(), 1)));
            tlHideInGameStatusPlayerWinOrExitMessage.getKeyFrames().add(new KeyFrame(Duration.millis(500),     new KeyValue(labInGameStatusFeedPlayerWinOrExit.opacityProperty(), 0)));

            tlHideInGameStatusPlayerWinOrExitMessage.playFromStart();
        }
        else
        {
            labInGameStatusFeedPlayerWinOrExit.setOpacity(NOT_VISIBLE);
        }
    }
    void showWinRoundMessage(Player objWinningPlayer, String sWinningAmount, int iMethod)
    {
        String sWinningHighCard;
        
        switch(iMethod)
        {
            case(HIGHEST_HAND):
            {
               switch(objWinningPlayer.getHandHighCard(1))
               {
                   case(14):       sWinningHighCard = "Ace";                                                    break;
                   case(13):       sWinningHighCard = "King";                                                   break;
                   case(12):       sWinningHighCard = "Queen";                                                  break;
                   case(11):       sWinningHighCard = "Jack";                                                   break;
                   default:        sWinningHighCard = Integer.toString(objWinningPlayer.getHandHighCard(1));    break;
               }

               if(objWinningPlayer.getSetValue() == ROYAL_FLUSH)
                   showMessage("Player " + Integer.toString(objWinningPlayer.getPlayerNumber()) + " Wins £" + sWinningAmount + " - Royal Flush");
               else
                   showMessage("Player " + Integer.toString(objWinningPlayer.getPlayerNumber()) + " Wins £" + sWinningAmount + " - "
                                                        + getWinningSetValueName(objWinningPlayer.getSetValue()) + " - " + sWinningHighCard + " High!");
               
               break;
            }
            case(ALL_OTHER_PLAYERS_FOLDED):
            {
                showMessage("Player " + Integer.toString(objWinningPlayer.getPlayerNumber()) + " Wins £" + sWinningAmount + " - All Players Folded");
                
                break;
            }
        }
    }
    void showWinGameMessage(String sMessage)
    {
        showPlayerWinOrExitMessage(sMessage);
    }
    void showPlayerExitMessage(String sMessage)
    {
        showPlayerWinOrExitMessage(sMessage);
    }
}