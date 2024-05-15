package missileCommand;

import java.util.Random;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;
import javafx.util.Duration;

class AlienMissilesManager implements Constants{

    private MissileCommand              objMissileCommand;
    private AlienMissile                AlienMissiles[]             = new AlienMissile[NUMBER_OF_ALIEN_MISSILES+1];
    private Line                        liAlienMissileLaunchDummy   = new Line();
    private Timeline                    tlAlienMissileGeneration, tlLevelProgress;
    private ProgressBar                 pbarLevelProgression;
    private Random                      objRandom = new Random();
    
    private int                         iAlienMissilesLaunchedCounter, iNumberOfAlienMissilesThisLevel, iIgnitionTimeAccumulative;
    private boolean                     bAlienMissileGenerationFinished;

    AlienMissilesManager(MissileCommand objMissileCommand, AnchorPane apMissileCommand, ProgressBar pbarLevelProgression)
    {
        this.objMissileCommand          = objMissileCommand;
        this.pbarLevelProgression       = pbarLevelProgression;
        
        //Create and initialise all of the Alien Missiles...
        for(int i = 1 ; i <= NUMBER_OF_ALIEN_MISSILES ; i++)
            AlienMissiles[i] = new AlienMissile(objMissileCommand, apMissileCommand);
        
        liAlienMissileLaunchDummy.setVisible(false);
        
        eventHandlerliAlienMissileLaunchDummy();
    }
    
    
    private void initAllAlienMissiles()
    {
        for(int i = 1 ; i <= NUMBER_OF_ALIEN_MISSILES ; i++)
            AlienMissiles[i].initialise();
    }
    private void initLevelProgressBar()
    {
        pbarLevelProgression.setProgress(RESET);
    }
    private int findNumberOfAlienMissiles(int iLevel)
    {
        if((iLevel*10) > NUMBER_OF_ALIEN_MISSILES)  return NUMBER_OF_ALIEN_MISSILES;
        else                                        return ((iLevel*10));
    }
    private void findAlienMissileIgnitionTimes()
    {
        int iIgnitionTimeMaximumInteval, iIgnitionTimeCalculated;
        
        for(int i = 1 ; i <= NUMBER_OF_ALIEN_MISSILES ; i++)
            AlienMissiles[i].setIgnitionTime(NOT_USED);
        
        iIgnitionTimeAccumulative = RESET;
        
        switch(objMissileCommand.getLevel())
        {
            case(1):     iIgnitionTimeMaximumInteval = 2000;    break;
            case(2):     iIgnitionTimeMaximumInteval = 1500;    break;
            case(3):     iIgnitionTimeMaximumInteval = 1200;    break;
            case(4):     iIgnitionTimeMaximumInteval = 1000;    break;
            case(5):     iIgnitionTimeMaximumInteval = 700;     break;
            default:     iIgnitionTimeMaximumInteval = 500;     break;
        }
        
        for(int i = 1 ; i <= iNumberOfAlienMissilesThisLevel ; i++)
        {
            iIgnitionTimeCalculated = 200 + objRandom.nextInt(iIgnitionTimeMaximumInteval + 1);            //Range 200 - iIgnitionTimeMaximumInteval
            
            iIgnitionTimeAccumulative = (iIgnitionTimeAccumulative + iIgnitionTimeCalculated);
            
            AlienMissiles[i].setIgnitionTime(iIgnitionTimeAccumulative);
        } 
    }
    
    
    void fireNextAlienMissile()
    {
        //Fire the next missile in this level...
        iAlienMissilesLaunchedCounter++;

        AlienMissiles[iAlienMissilesLaunchedCounter].alienMissileFire(objMissileCommand.getLevel());
    }
    void initNewGame()
    {
        pbarLevelProgression.setProgress(RESET);
    }
    void startAlienMissiles(int iLevel)
    {
        double dLevelProgressIncrementalDelay, dLevelProgressIncrementalAmount;
        
        initAllAlienMissiles();
        
        iNumberOfAlienMissilesThisLevel = findNumberOfAlienMissiles(iLevel);
        
        initLevelProgressBar();
        
        //Find the ignition points that the missiles will be fired...
        findAlienMissileIgnitionTimes();
        
        dLevelProgressIncrementalDelay = (double)iIgnitionTimeAccumulative/100;
        dLevelProgressIncrementalAmount = 0.01;
        
        //Generate the timeline using the time intevals just created...
        tlLevelProgress             = new Timeline();
        tlAlienMissileGeneration    = new Timeline();
        
        tlAlienMissileGeneration.getKeyFrames().clear();
        tlAlienMissileGeneration.setCycleCount(1);
        
        for(int i = 1 ; i <= 100 ; i++)
            tlLevelProgress.getKeyFrames().add(new KeyFrame(Duration.millis((i*dLevelProgressIncrementalDelay)), new KeyValue(pbarLevelProgression.progressProperty(), i*dLevelProgressIncrementalAmount)));
        
        for(int i = 1 ; i <= iNumberOfAlienMissilesThisLevel ; i++)
        {
            tlAlienMissileGeneration.getKeyFrames().add(new KeyFrame(Duration.millis(AlienMissiles[i].getIgnitionTime()),           new KeyValue(liAlienMissileLaunchDummy.visibleProperty(),    false)));
            tlAlienMissileGeneration.getKeyFrames().add(new KeyFrame(Duration.millis((AlienMissiles[i].getIgnitionTime()+50)),      new KeyValue(liAlienMissileLaunchDummy.visibleProperty(),    true)));
        }
        
        tlAlienMissileGeneration.setOnFinished((ActionEvent ae) -> {
            bAlienMissileGenerationFinished = true;
        });
  
        iAlienMissilesLaunchedCounter = RESET;
        pbarLevelProgression.setProgress(RESET);
        
        bAlienMissileGenerationFinished = false;
        
        tlLevelProgress.playFromStart();
        tlAlienMissileGeneration.playFromStart();
    }
    void stopAlienMissiles()
    {
        tlAlienMissileGeneration.stop();
    }
    boolean hasAlienMissilesCompleted()
    {
        boolean bAlienMissileActive;
        
        bAlienMissileActive = false;
        
        //Check all missiles to see if none are active...
        for(int i = 1 ; i<=NUMBER_OF_ALIEN_MISSILES ; i++)
            if(AlienMissiles[i].isMissileActive())
                bAlienMissileActive = true;
        
        return ((bAlienMissileGenerationFinished) && (!bAlienMissileActive));
    }
    int getNumberOfAlienMissilesThisLevel()
    {
        return iNumberOfAlienMissilesThisLevel;
    }
    double getMissileLayoutXStart(int iMissile)
    {
        return AlienMissiles[iMissile].getLayoutXStart();
    }
    double getMissileLayoutYStart(int iMissile)
    {
        return AlienMissiles[iMissile].getLayoutYStart();
    }
    double getMissileIgnitionTime(int iMissile)
    {
        return AlienMissiles[iMissile].getIgnitionTime();
    }
    double getMissileEndXProperty(int iMissile)
    {
        return AlienMissiles[iMissile].getEndXProperty();
    }
    double getMissileEndYProperty(int iMissile)
    {
        return AlienMissiles[iMissile].getEndYProperty();
    }
    void alienMissileHitRunExplosion(int iMissile)
    {
        AlienMissiles[iMissile].hitRunExplosion();
    }
    
    
    private void eventHandlerliAlienMissileLaunchDummy()
    {
        liAlienMissileLaunchDummy.visibleProperty().addListener(new ChangeListener()
        {
            @Override
            public void changed(ObservableValue o, Object oldVal, Object newVal)
            {                                              
                if(liAlienMissileLaunchDummy.visibleProperty().get())
                    fireNextAlienMissile();
            }
        });
    }
}
