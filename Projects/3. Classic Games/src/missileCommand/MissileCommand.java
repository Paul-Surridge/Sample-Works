package missileCommand;

import app.App;
import app.Scenes;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.Animation.Status;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class MissileCommand implements Initializable, Constants {

    //Populate and bring in the necessary objects from the .fxml file.
    @FXML
    private AnchorPane                      apMissileCommand;   
    @FXML
    private ImageView                       ivCloseMissileCommand;       
    @FXML
    private ImageView                       ivCity1;
    @FXML
    private ImageView                       ivCity2;
    @FXML
    private ImageView                       ivCity3;
    @FXML
    private ImageView                       ivCity4;
    @FXML
    private ImageView                       ivCity5;
    @FXML
    private ImageView                       ivPlayerShipLeft;
    @FXML
    private ImageView                       ivPlayerShipRight;
    @FXML
    private Label                           labPlayerShipLeftAmmunitionAmount;
    @FXML
    private Label                           labPlayerShipRightAmmunitionAmount;
    @FXML
    private Label                           labDisplayMessage;
    @FXML
    private Label                           labCurrentScore;
    @FXML
    private Label                           labLevel; 
    @FXML
    private Button                          btnPlayAgainYes;
    @FXML
    private Button                          btnPlayAgainNo;
    @FXML
    private Label                           labGameOverMessage;
    @FXML
    private Label                           labShootingArea;
    @FXML
    private ProgressBar                     pbarLevelProgression;
    @FXML
    private AnchorPane                      apLevelCompleteScore;
    @FXML
    private Label                           labNumberOfPlayerShips;
    @FXML
    private Label                           labNumberOfNukes;
    @FXML
    private Label                           labNumberOfCities;
    @FXML
    private Label                           labNumberOfPlayerShipsScore;
    @FXML
    private Label                           labNumberOfNukesScore;
    @FXML
    private Label                           labNumberOfCitiesScore;
    @FXML
    private Label                           labTotalScoreAmount;
    @FXML
    private Label                           labTotalScoreHeader;
 
    private Playership                      objPlayershipLeft;
    private Playership                      objPlayershipRight;
    private AlienMissilesManager            objAlienMissilesManager;
    private ScoreManager                    objScoreManager;
    private City                            Cities[] = new City[NUMBER_OF_CITIES + 1];   
    
    private InGameStatusTextManager         objInGameStatusTextManager;

    private int iCurrentLevel;
    private boolean bGameActive;
  
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    { 
        //Initialise all objects and event handling code...
        initAlienMissiles();
        initPlayerships();
        initCities();       
        initMiscItems();
        initScoreManager();
        
        objInGameStatusTextManager.runGameStartIntroMessage();
        
        eventHandlerMouseEvents();
        
        objPlayershipLeft.setAmmunition(100);
        objPlayershipRight.setAmmunition(100);
        
        labLevel.setText("");
    }
    
    
    private void initMiscItems()
    {
        objInGameStatusTextManager = new InGameStatusTextManager(this, labDisplayMessage, labGameOverMessage, btnPlayAgainYes, btnPlayAgainNo);
    }
    private void initScoreManager()
    {
        objScoreManager = new ScoreManager(this, apLevelCompleteScore, labNumberOfPlayerShips, labNumberOfNukes, labNumberOfCities,
                                           labNumberOfPlayerShipsScore, labNumberOfNukesScore, labNumberOfCitiesScore,
                                           labTotalScoreAmount, labCurrentScore,
                                           labTotalScoreHeader);
    }
    private void initAlienMissiles()
    {
        objAlienMissilesManager = new AlienMissilesManager(this, apMissileCommand, pbarLevelProgression);
    }
    private void initPlayerships()
    {     
        objPlayershipLeft   = new Playership(apMissileCommand, PLAYER_SHIP_LEFT, ivPlayerShipLeft, labPlayerShipLeftAmmunitionAmount, labShootingArea, objAlienMissilesManager);
        objPlayershipRight  = new Playership(apMissileCommand, PLAYER_SHIP_RIGHT, ivPlayerShipRight, labPlayerShipRightAmmunitionAmount, labShootingArea, objAlienMissilesManager);        
    }
    private void initCities()
    {
        Cities[1] = new City(ivCity1);
        Cities[2] = new City(ivCity2);
        Cities[3] = new City(ivCity3);
        Cities[4] = new City(ivCity4);
        Cities[5] = new City(ivCity5);
    }    
    private void hidePlayAgainObjects()
    {
        labGameOverMessage.setOpacity(0);
        btnPlayAgainYes.setOpacity(0);
        btnPlayAgainNo.setOpacity(0);
        
        btnPlayAgainYes.setDisable(true);
        btnPlayAgainNo.setDisable(true);
    }
    private void playAgain()
    {
        iCurrentLevel = 1;

        hidePlayAgainObjects();
        
        objPlayershipLeft.setAmmunition(100);
        objPlayershipRight.setAmmunition(100);
        
        Cities[1].setAliveState(true);
        Cities[2].setAliveState(true);
        Cities[3].setAliveState(true);
        Cities[4].setAliveState(true);
        Cities[5].setAliveState(true);
        
        objPlayershipLeft.setAliveState(true);
        objPlayershipRight.setAliveState(true);
        
        labLevel.setText("Level " + Integer.toString(iCurrentLevel));
        
        objInGameStatusTextManager.runGameStartIntroMessage();
        
        objScoreManager.initNewGame();
        objAlienMissilesManager.initNewGame();
    }
    private boolean isGameOver()
    {
        boolean bAllCitiesDestroyed;
        
        bAllCitiesDestroyed = true;
        
        for(int i = 1 ; i <= NUMBER_OF_CITIES ; i++)
            if(Cities[i].getAliveState())
                bAllCitiesDestroyed = false;
        
        //Now check if all cities are destroyed, if so then end of game...
        if(bAllCitiesDestroyed)
            return true;
        
        //Now check if both ships are destroyed, if so then end of game...
        return (!(objPlayershipLeft.getAliveState()) && !(objPlayershipRight.getAliveState()));
    }
    private void gameOver()
    {
        if(bGameActive)
        {
            //Check and ensure that the end of level macro has not begun to run...
            if(objScoreManager.getScoreRevealTimelineStatus() == Status.RUNNING)
                objScoreManager.stopAndHideAllObjects();   
            
            bGameActive = false;
            
            objAlienMissilesManager.stopAlienMissiles();

            objInGameStatusTextManager.runGameoverMessage();
        }
        
        System.gc();
    }
    
    
    void newGame()
    {
        iCurrentLevel = 1;
        
        objAlienMissilesManager.startAlienMissiles(iCurrentLevel);
        
        objPlayershipLeft.setAmmunition(100);
        objPlayershipRight.setAmmunition(100);
        
        labLevel.setText("Level " + Integer.toString(iCurrentLevel));
        
        bGameActive = true;
    }
    void nextLevel()
    {
        if(objPlayershipLeft.getAliveState())       objPlayershipLeft.setAmmunition(100);
        else                                        objPlayershipLeft.setAmmunition(0);
        
        if(objPlayershipRight.getAliveState())      objPlayershipRight.setAmmunition(100);
        else                                        objPlayershipRight.setAmmunition(0);
        
        iCurrentLevel++;
        
        objInGameStatusTextManager.runLevelIntroMessage(iCurrentLevel);
    }
    void showCurrentLevel()
    {
        labLevel.setText("Level " + Integer.toString(iCurrentLevel));
                
        objAlienMissilesManager.startAlienMissiles(iCurrentLevel);
    }
    void isEndOfLevel()
    {
        if( objAlienMissilesManager.hasAlienMissilesCompleted() && (bGameActive) )
        {
            labLevel.setText("Level " + Integer.toString(iCurrentLevel) + " Complete");
            objScoreManager.findAndShowLevelScore(iCurrentLevel, objPlayershipLeft, objPlayershipRight, Cities);
        }
    }
    void hitCity(int iCity)
    {
        Cities[iCity].hitCity();
        
        //Now check if all cities are destroyed, if so then end of game...
        if(isGameOver())
            gameOver();
    }
    void hitPlayerShip(int iPlayerShip)
    {
        switch(iPlayerShip)
        {
            case(PLAYER_SHIP_LEFT):         {if(objPlayershipLeft.getAliveState())  {objPlayershipLeft.hitPlayership();}    break;}
            case(PLAYER_SHIP_RIGHT):        {if(objPlayershipRight.getAliveState()) {objPlayershipRight.hitPlayership();}   break;}
        }
        
        //Now check if both ships are destroyed, if so then end of game...
        if(isGameOver())
            gameOver();
    }
    int getLevel()
    {
        return iCurrentLevel;
    }
    
    

    private void eventHandlerMouseEvents()
    {
        // - - - - Handlers - - - -
        final EventHandler<MouseEvent>  ehMouseClick = (MouseEvent me) -> {
            //Depending on what side the cursor is currently in, fire the closest playership..
            if(me.getX() <  (SHOOTING_AREA_WIDTH/2))
            {
                if( (objPlayershipLeft.getAliveState() == true) && (objPlayershipLeft.getAmmunition() > 0) )
                {
                    objPlayershipLeft.fireMissile( (me.getX() + SHOOTING_AREA_LAYOUT_X_OFFSET), (me.getY() + SHOOTING_AREA_LAYOUT_Y_OFFSET) );
                }
                else if( (objPlayershipRight.getAliveState() == true) && (objPlayershipRight.getAmmunition() > 0) )
                {
                    objPlayershipRight.fireMissile( (me.getX() + SHOOTING_AREA_LAYOUT_X_OFFSET), (me.getY() + SHOOTING_AREA_LAYOUT_Y_OFFSET) );
                }
            }
            else
            {
                if( (objPlayershipRight.getAliveState() == true) && (objPlayershipRight.getAmmunition() > 0) )
                {
                    objPlayershipRight.fireMissile( (me.getX() + SHOOTING_AREA_LAYOUT_X_OFFSET), (me.getY() + SHOOTING_AREA_LAYOUT_Y_OFFSET) );
                }
                else if( (objPlayershipLeft.getAliveState() == true) && (objPlayershipLeft.getAmmunition() > 0) )
                {
                    objPlayershipLeft.fireMissile( (me.getX() + SHOOTING_AREA_LAYOUT_X_OFFSET), (me.getY() + SHOOTING_AREA_LAYOUT_Y_OFFSET) );
                }
            }
        };
        final EventHandler<MouseEvent>  ehCloseMissileCommand = (MouseEvent me) -> {
            try{
                if(bGameActive)
                {
                    objAlienMissilesManager.stopAlienMissiles();

                    App.loadScene(Scenes.SPLASH);
                }
            }
            catch(Exception ex){
                System.out.println(ex.toString());
            }
        };
        final EventHandler<MouseEvent>  ehPlayAgainNo = (MouseEvent me) -> {
            try{
                objAlienMissilesManager.stopAlienMissiles();

                App.loadScene(Scenes.SPLASH);
            }
            catch(Exception ex){
                System.out.println(ex.toString());
            }
        };
                
        final EventHandler<MouseEvent>  ehPlayAgainYes = (MouseEvent me) -> {
            playAgain();
        };
        
        labShootingArea.addEventHandler(MouseEvent.MOUSE_CLICKED, ehMouseClick);
        ivCloseMissileCommand.addEventHandler(MouseEvent.MOUSE_CLICKED, ehCloseMissileCommand);
        btnPlayAgainYes.addEventHandler(MouseEvent.MOUSE_CLICKED, ehPlayAgainYes);
        btnPlayAgainNo.addEventHandler(MouseEvent.MOUSE_CLICKED, ehPlayAgainNo);
    }
}
