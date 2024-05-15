package missileCommand;

import java.text.NumberFormat;
import java.util.Locale;
import javafx.animation.Animation.Status;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

class ScoreManager implements Constants{

    private MissileCommand      objMissileCommand;
    private AnchorPane          apLevelCompleteScore;
    private Label               labNumberOfPlayerShips;
    private Label               labNumberOfNukes;
    private Label               labNumberOfCities;
    private Label               labNumberOfPlayerShipsScore;
    private Label               labNumberOfNukesScore;
    private Label               labNumberOfCitiesScore;
    private Label               labTotalScoreAmount;
    private Label               labCurrentScore;
    private Label               labTotalScoreHeader;
    
    private Timeline tlLevelCompleteScoreReveal = new Timeline();
    
    private int                 iCurrentScoreAmount;
    private int                 iScoreAmountPlayerShips, iScoreAmountCities, iScoreAmountNukes;

    ScoreManager(MissileCommand objMissileCommand, AnchorPane apLevelCompleteScore, Label labNumberOfPlayerShips, Label labNumberOfNukes, Label labNumberOfCities,
                 Label labNumberOfPlayerShipsScore, Label labNumberOfNukesScore, Label labNumberOfCitiesScore,
                 Label labTotalScoreAmount, Label labCurrentScore,
                 Label labTotalScoreHeader)
    {
        this.objMissileCommand                  = objMissileCommand;
        this.apLevelCompleteScore               = apLevelCompleteScore;
        this.labNumberOfPlayerShips             = labNumberOfPlayerShips;
        this.labNumberOfNukes                   = labNumberOfNukes;
        this.labNumberOfCities                  = labNumberOfCities;
        this.labNumberOfPlayerShipsScore        = labNumberOfPlayerShipsScore;
        this.labNumberOfNukesScore              = labNumberOfNukesScore;
        this.labNumberOfCitiesScore             = labNumberOfCitiesScore;
        this.labTotalScoreAmount                = labTotalScoreAmount;
        this.labCurrentScore                    = labCurrentScore;
        this.labTotalScoreHeader                = labTotalScoreHeader;
        
        iCurrentScoreAmount = RESET;
        this.labCurrentScore.setText("");
        
        hideAllObjects();
        
        initTimelines();
    }
    
    
    private void initTimelines()
    {
        //Define timeline...
        tlLevelCompleteScoreReveal.getKeyFrames().add(new KeyFrame(Duration.millis(0),      new KeyValue(apLevelCompleteScore.disableProperty(), false)));
        tlLevelCompleteScoreReveal.getKeyFrames().add(new KeyFrame(Duration.millis(0),      new KeyValue(apLevelCompleteScore.opacityProperty(), 0)));
        tlLevelCompleteScoreReveal.getKeyFrames().add(new KeyFrame(Duration.millis(500),    new KeyValue(apLevelCompleteScore.opacityProperty(), 1)));
        tlLevelCompleteScoreReveal.getKeyFrames().add(new KeyFrame(Duration.millis(500),    new KeyValue(labTotalScoreHeader.opacityProperty(), 1)));
        
        tlLevelCompleteScoreReveal.getKeyFrames().add(new KeyFrame(Duration.millis(1000),    new KeyValue(labNumberOfPlayerShips.opacityProperty(), 0)));
        tlLevelCompleteScoreReveal.getKeyFrames().add(new KeyFrame(Duration.millis(1000),    new KeyValue(labNumberOfPlayerShipsScore.opacityProperty(), 0)));
        tlLevelCompleteScoreReveal.getKeyFrames().add(new KeyFrame(Duration.millis(1500),    new KeyValue(labNumberOfPlayerShips.opacityProperty(), 1)));
        tlLevelCompleteScoreReveal.getKeyFrames().add(new KeyFrame(Duration.millis(1500),    new KeyValue(labNumberOfPlayerShipsScore.opacityProperty(), 1)));
        
        tlLevelCompleteScoreReveal.getKeyFrames().add(new KeyFrame(Duration.millis(2000),    new KeyValue(labNumberOfNukes.opacityProperty(), 0)));
        tlLevelCompleteScoreReveal.getKeyFrames().add(new KeyFrame(Duration.millis(2000),    new KeyValue(labNumberOfNukesScore.opacityProperty(), 0)));
        tlLevelCompleteScoreReveal.getKeyFrames().add(new KeyFrame(Duration.millis(2500),    new KeyValue(labNumberOfNukes.opacityProperty(), 1)));
        tlLevelCompleteScoreReveal.getKeyFrames().add(new KeyFrame(Duration.millis(2500),    new KeyValue(labNumberOfNukesScore.opacityProperty(), 1)));
        
        tlLevelCompleteScoreReveal.getKeyFrames().add(new KeyFrame(Duration.millis(3000),    new KeyValue(labNumberOfCities.opacityProperty(), 0)));
        tlLevelCompleteScoreReveal.getKeyFrames().add(new KeyFrame(Duration.millis(3000),    new KeyValue(labNumberOfCitiesScore.opacityProperty(), 0)));
        tlLevelCompleteScoreReveal.getKeyFrames().add(new KeyFrame(Duration.millis(3500),    new KeyValue(labNumberOfCities.opacityProperty(), 1)));
        tlLevelCompleteScoreReveal.getKeyFrames().add(new KeyFrame(Duration.millis(3500),    new KeyValue(labNumberOfCitiesScore.opacityProperty(), 1)));
        
        tlLevelCompleteScoreReveal.getKeyFrames().add(new KeyFrame(Duration.millis(4000),    new KeyValue(labTotalScoreAmount.opacityProperty(), 0)));
        tlLevelCompleteScoreReveal.getKeyFrames().add(new KeyFrame(Duration.millis(4500),    new KeyValue(labTotalScoreAmount.opacityProperty(), 1)));
        
        tlLevelCompleteScoreReveal.getKeyFrames().add(new KeyFrame(Duration.millis(6000),    new KeyValue(apLevelCompleteScore.opacityProperty(), 1)));
        tlLevelCompleteScoreReveal.getKeyFrames().add(new KeyFrame(Duration.millis(6500),    new KeyValue(apLevelCompleteScore.opacityProperty(), 0)));
        tlLevelCompleteScoreReveal.getKeyFrames().add(new KeyFrame(Duration.millis(6500),    new KeyValue(apLevelCompleteScore.disableProperty(), true)));
        
        //Define the OnFinished aspect of the timeline
        tlLevelCompleteScoreReveal.setOnFinished((ActionEvent ae) -> {
            //Update the players current running score
            labCurrentScore.setText(formatPoints(iCurrentScoreAmount));
            
            labNumberOfPlayerShips.setOpacity(NOT_VISIBLE);
            labNumberOfNukes.setOpacity(NOT_VISIBLE);
            labNumberOfCities.setOpacity(NOT_VISIBLE);
            
            labNumberOfPlayerShipsScore.setOpacity(NOT_VISIBLE);
            labNumberOfNukesScore.setOpacity(NOT_VISIBLE);
            labNumberOfCitiesScore.setOpacity(NOT_VISIBLE);
            
            labTotalScoreAmount.setOpacity(NOT_VISIBLE);
            
            objMissileCommand.nextLevel();
        });
    }
    private String formatPoints(int iPoints)
    {
        return NumberFormat.getInstance(Locale.UK).format((double)iPoints);
    }
    private void hideAllObjects()
    {
        //Ensure that they are all not visible...
        labNumberOfPlayerShips.setOpacity(NOT_VISIBLE);
        labNumberOfNukes.setOpacity(NOT_VISIBLE);
        labNumberOfCities.setOpacity(NOT_VISIBLE);
        
        labNumberOfPlayerShipsScore.setOpacity(NOT_VISIBLE);
        labNumberOfNukesScore.setOpacity(NOT_VISIBLE);
        labNumberOfCitiesScore.setOpacity(NOT_VISIBLE);
        
        labTotalScoreAmount.setOpacity(NOT_VISIBLE);
        
        labTotalScoreHeader.setOpacity(NOT_VISIBLE);
        
        this.apLevelCompleteScore.setOpacity(NOT_VISIBLE);
        this.apLevelCompleteScore.setDisable(true);
    }
    
    
    void initNewGame()
    {
        iCurrentScoreAmount = RESET;
        labCurrentScore.setText("");
    }
    void stopAndHideAllObjects()
    {
        tlLevelCompleteScoreReveal.stop();
        
        hideAllObjects();
    }
    Status getScoreRevealTimelineStatus()
    {
        return tlLevelCompleteScoreReveal.getStatus();
    }
    void findAndShowLevelScore(int iLevel, Playership objPlayershipLeft,  Playership objPlayershipRight, City Cities[])
    {
        int iTotalNumberOfActivePlayerShips, iTotalNumberOfActiveCities, iTotalNumberOfRemainingNukes;
                
        iTotalNumberOfActivePlayerShips     = RESET;
        iTotalNumberOfActiveCities          = RESET;
        iTotalNumberOfRemainingNukes        = RESET;     
        
        for(int i = 1 ; i<=NUMBER_OF_CITIES ; i++)
            if(Cities[i].getAliveState())
                iTotalNumberOfActiveCities++;
        
        if(objPlayershipLeft.getAliveState())
        {
            iTotalNumberOfActivePlayerShips++;
            iTotalNumberOfRemainingNukes = objPlayershipLeft.getAmmunition();
        }
        
        if(objPlayershipRight.getAliveState())
        {
            iTotalNumberOfActivePlayerShips++;
            iTotalNumberOfRemainingNukes = iTotalNumberOfRemainingNukes + objPlayershipRight.getAmmunition();
        }
        
        iScoreAmountPlayerShips     = (iTotalNumberOfActivePlayerShips*SCORE_AMOUNT_PER_SHIP);
        iScoreAmountCities          = (iTotalNumberOfActiveCities*SCORE_AMOUNT_PER_CITY);
        iScoreAmountNukes           = (iTotalNumberOfRemainingNukes*SCORE_AMOUNT_PER_NUKE);
        
        labNumberOfPlayerShips.setText("x"+Integer.toString(iTotalNumberOfActivePlayerShips));
        labNumberOfNukes.setText("x"+Integer.toString(iTotalNumberOfRemainingNukes));
        labNumberOfCities.setText("x"+Integer.toString(iTotalNumberOfActiveCities));
        
        labNumberOfPlayerShipsScore.setText(formatPoints(iScoreAmountPlayerShips));
        labNumberOfNukesScore.setText(formatPoints(iScoreAmountNukes));
        labNumberOfCitiesScore.setText(formatPoints(iScoreAmountCities));
        
        labTotalScoreAmount.setText(formatPoints(iScoreAmountPlayerShips + iScoreAmountCities + iScoreAmountNukes));
        
        iCurrentScoreAmount = (iCurrentScoreAmount + (iScoreAmountPlayerShips + iScoreAmountCities + iScoreAmountNukes));
        
        tlLevelCompleteScoreReveal.playFromStart();
    }
}
