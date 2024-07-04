package spaceInvaders;

import app.App;
import app.Scenes;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import static spaceInvaders.Constants.NUKES;

public class SpaceInvaders implements Initializable, Constants {

    //Populate and bring in the necessary objects

    @FXML
    private AnchorPane                      apSpaceInvaders;
    @FXML
    private AnchorPane                      apKeyboardControls;
    @FXML
    private ImageView                       ivPlayerShip;   
    @FXML
    private ImageView                       ivCloseSpaceInvaders;       
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
    private ImageView                       ivMothership;
    @FXML
    private ImageView                       ivMissilesSelect;
    @FXML
    private ImageView                       ivHPMissilesSelect;
    @FXML
    private ImageView                       ivNukesSelect;
    @FXML
    private Label                           labDisplayMessage;
    @FXML
    private Label                           labScore;
    @FXML
    private Label                           labLevel;
    @FXML
    private Label                           labPlayershipLives;
    @FXML
    private Label                           labHPMissilesAmount;
    @FXML
    private Label                           labNukesAmount;
    @FXML
    private ImageView                       ivWeaponPowerup; 
    @FXML
    private Button                          btnPlayAgainYes;
    @FXML
    private Button                          btnPlayAgainNo;
    @FXML
    private Label                           labGameOverMessage;
    
    private Playership                      objPlayership;
    private AlienShipsManager               objAlienShipsManager;  
    private Mothership                      objMotherShip;
    private Nuke                            objNuke;
    private City                            Cities[];
    private InGameStatusTextManager         objInGameStatusTextManager;
    private ScoreManager                    objScoreManager;
    private WeaponPowerup                   objWeaponPowerup; 
    
    private Circle                          cirAlienMissile = new Circle(0, 0, 5, Paint.valueOf("#fff500"));

    private int iCurrentLevel;
    private boolean bGameInitialised, bGameActive;
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        bGameInitialised    = false;
        bGameActive         = false;
        
        initPlayership();
        initAlienShips();
        initCities();
        initMothership();
        initMiscItems();
        initTimelines();
        initEventHandlers();
        
        apKeyboardControls.setVisible(true);
    }
    
    
    
    private void initPlayership()
    {     
        objPlayership = new Playership(apSpaceInvaders, ivPlayerShip, labNukesAmount, labHPMissilesAmount, ivMissilesSelect, ivHPMissilesSelect, ivNukesSelect, labPlayershipLives);
    }
    private void initAlienShips()
    {
        objAlienShipsManager = new AlienShipsManager(apSpaceInvaders, cirAlienMissile);
    }
    private void initCities()
    {
        Cities = new City[NUMBER_OF_CITIES + 1];
        
        Cities[1] = new City(ivCity1);
        Cities[2] = new City(ivCity2);
        Cities[3] = new City(ivCity3);
        Cities[4] = new City(ivCity4);
        Cities[5] = new City(ivCity5);
    }
    private void initTimelines()
    {
        objInGameStatusTextManager.tlGameStart.setOnFinished((ActionEvent ae) -> {
            gameStart();
        });
        
        objInGameStatusTextManager.tlNextLevel.setOnFinished((ActionEvent ae) -> {
            levelInit();
        });       
    }
    private void initMothership()
    {
        objMotherShip = new Mothership(ivMothership);
    }
    private void initEventHandlers()
    {
        eventHandlerPlayerShip();
        eventHandlerPlayerMissile();
        eventHandlerAlienMissile();
        eventHandlerMouseEvents();
    }
    private void initMiscItems()
    {
        objWeaponPowerup            = new WeaponPowerup(ivWeaponPowerup);
        objNuke                     = new Nuke(apSpaceInvaders);
        objScoreManager             = new ScoreManager(labScore);
        objInGameStatusTextManager  = new InGameStatusTextManager(labDisplayMessage, labGameOverMessage, btnPlayAgainYes, btnPlayAgainNo);
        
        //Initialise Alien Missile - Add circle to apSpaceInvaders root node
        cirAlienMissile.setVisible(false);
        apSpaceInvaders.getChildren().addAll(cirAlienMissile);
    }
    
    

    
    private void gameStart()
    {       
        iCurrentLevel = 1;
        
        objScoreManager.reset();
        
        for(int i = 1 ; i <= NUMBER_OF_CITIES ; i++)
            Cities[i].setAliveState(true);

        //Ready Playership for new game
        objPlayership.initPlayership();
        objPlayership.setActiveWeapon(MISSILES);
        objPlayership.show();

        levelInit();
    }  
    private void levelInit()
    {
        //Ready Alienships for new level
        objAlienShipsManager.initAlienTypes();
        objAlienShipsManager.findTypes(iCurrentLevel);        
        objAlienShipsManager.resetAlienShipPositions();
        objAlienShipsManager.showAllAlienShips();
        
        objAlienShipsManager.startMoving();                        
        objAlienShipsManager.startMissiles(iCurrentLevel);
        objWeaponPowerup.start();
        objMotherShip.start();
        
        labLevel.setText("Level " + Integer.toString(iCurrentLevel));
        
        //Ensure that the Anchor Pane has the focus for keyboard inputs
        apSpaceInvaders.requestFocus();
        
        bGameInitialised = true;
        bGameActive = true;
    }
    private void levelComplete()
    {
        bGameActive = false;
        
        objAlienShipsManager.stopMissiles();
        objAlienShipsManager.stopMoving();
        objWeaponPowerup.stop();
        objMotherShip.stop();
                
        iCurrentLevel++;
        
        objInGameStatusTextManager.runNextLevel(iCurrentLevel);
    }
    private void gameOver()
    {
        bGameActive = false;
        
        objWeaponPowerup.stop();

        objInGameStatusTextManager.runGameOver();
    }
    private void restartGame()
    {
        objAlienShipsManager.stopMoving();
        objAlienShipsManager.stopMissiles();
        objWeaponPowerup.stop();
        objMotherShip.stop();
        
        iCurrentLevel = 1;
        
        labLevel.setText("Level " + Integer.toString(iCurrentLevel));
        
        objScoreManager.reset();
        
        //Ensure buttons are hidden
        objInGameStatusTextManager.clearGameOver();
        btnPlayAgainYes.setOpacity(0);
        btnPlayAgainNo.setOpacity(0);
        
        for(int i = 1 ; i <= NUMBER_OF_CITIES ; i++)
            Cities[i].setAliveState(true);
        
        //Ready Playership for new game
        objPlayership.initPlayership();
        objPlayership.setActiveWeapon(MISSILES);
        objPlayership.show();
        
        objAlienShipsManager.hideAllAlienShips();
    
        objInGameStatusTextManager.runGameStart();
    }

    
    
    void gameClose()
    {
        bGameActive = false;
         
        objAlienShipsManager.stopMoving();
        objAlienShipsManager.stopMissiles();
        objWeaponPowerup.stop();
        objMotherShip.stop();
    }
    
    
    
    //Event Handlers                                                            
    private void eventHandlerMouseEvents()
    {
        final EventHandler<MouseEvent>  ehCloseSpaceInvaders = (MouseEvent me) -> {
            try{
                if(apKeyboardControls.isVisible())
                    App.loadScene(Scenes.SPLASH);
                
                if(bGameInitialised)
                {
                    gameClose();

                    App.loadScene(Scenes.SPLASH);
                }
            }
            catch(Exception ex){
                System.out.println(ex.toString());
            }
        };
        
        final EventHandler<MouseEvent>  ehCloseKeyboardControls = (MouseEvent me) -> {
            apKeyboardControls.setVisible(false);
            objInGameStatusTextManager.runGameStart();
        };
                
        final EventHandler<MouseEvent>  ehPlayAgainYes = (MouseEvent me) -> {
            restartGame();
        };
        
        ivCloseSpaceInvaders.addEventHandler(MouseEvent.MOUSE_CLICKED, ehCloseSpaceInvaders);
        apKeyboardControls.addEventHandler(MouseEvent.MOUSE_CLICKED, ehCloseKeyboardControls);
        btnPlayAgainYes.addEventHandler(MouseEvent.MOUSE_CLICKED, ehPlayAgainYes);
        btnPlayAgainNo.addEventHandler(MouseEvent.MOUSE_CLICKED, ehCloseSpaceInvaders);
    }
    private void eventHandlerPlayerShip()
    {
        //Declare an Event Handler to handle all key input commands on the Space Invaders game
        final EventHandler<KeyEvent>  ehKeyPressed = (KeyEvent ke) -> {
            if(objPlayership.getAliveState())
            {
                switch(ke.getCode())
                {
                    case LEFT:          objPlayership.playershipMoveLeft();                 break;
                    case RIGHT:         objPlayership.playershipMoveRight();                break;
                    case SPACE:         objPlayership.fireCurrentWeapon();                  break;
                    case M:             objPlayership.setActiveWeapon(MISSILES);            break;
                    case H:             objPlayership.setActiveWeapon(HPMISSILES);          break;
                    case N:             objPlayership.setActiveWeapon(NUKES);               break;
                }
                
                ke.consume();
            }
        };
        
        apSpaceInvaders.addEventHandler(KeyEvent.KEY_PRESSED, ehKeyPressed);

        objPlayership.ivPlayershipSprite.translateXProperty().addListener(new ChangeListener()
        {
            int iPlayerShipXPosition;
            
            @Override
            public void changed(ObservableValue o, Object oldVal, Object newVal)
            {
                iPlayerShipXPosition = PLAYERSHIP_START_X_COORDINATE + (int)Double.parseDouble(newVal.toString());
                
                if(objWeaponPowerup.ivPowerupSprite.getOpacity() > 0)
                {
                    if( (iPlayerShipXPosition >= (objWeaponPowerup.iPowerupTypeOriginXPosition)) && (iPlayerShipXPosition <= (objWeaponPowerup.iPowerupTypeOriginXPosition + 64)) )
                    {
                        objWeaponPowerup.collected();

                        switch(objWeaponPowerup.getPowerupType())
                        {
                            case(NUKES):        objPlayership.addNukes(1);                  break;
                            case(HPMISSILES):   objPlayership.addHPMissiles(5);             break;
                        }
                        
                        objWeaponPowerup.start();
                    }
                }
            }
        });
    }
    private void eventHandlerPlayerMissile()
    {
        objPlayership.cirPlayershipMissileSprite.centerYProperty().addListener(new ChangeListener()
        {
            private double dPlayershipMissileYPosition;
            
            @Override
            public void changed(ObservableValue o, Object oldVal, Object newVal)
            {
                if(bGameActive)
                {
                    dPlayershipMissileYPosition = (double)o.getValue();

                    if(dPlayershipMissileYPosition < 100)
                    {
                        objMotherShip.checkIfMothershipHit(objPlayership, objScoreManager);
                    }
                    else if( (dPlayershipMissileYPosition < 120) && (!objPlayership.getRowChecked(1)) )
                    {
                        if(objAlienShipsManager.checkIfAlienHit(1, objPlayership, objNuke, objScoreManager) == LEVEL_COMPLETE)
                            levelComplete();
                    }
                    else if( (dPlayershipMissileYPosition < 170) && (!objPlayership.getRowChecked(2)) )
                    {
                        if(objAlienShipsManager.checkIfAlienHit(2, objPlayership, objNuke, objScoreManager) == LEVEL_COMPLETE)
                            levelComplete();
                    }
                    else if( (dPlayershipMissileYPosition < 220) && (!objPlayership.getRowChecked(3)) )
                    {
                        if(objAlienShipsManager.checkIfAlienHit(3, objPlayership, objNuke, objScoreManager) == LEVEL_COMPLETE)
                            levelComplete();
                    }
                    else if( (dPlayershipMissileYPosition < 270) && (!objPlayership.getRowChecked(4)) )
                    {
                        if(objAlienShipsManager.checkIfAlienHit(4, objPlayership, objNuke, objScoreManager) == LEVEL_COMPLETE)
                            levelComplete();
                    }
                    else if( (dPlayershipMissileYPosition < 320) && (!objPlayership.getRowChecked(5)) )
                    {
                        if(objAlienShipsManager.checkIfAlienHit(5, objPlayership, objNuke, objScoreManager) == LEVEL_COMPLETE)
                            levelComplete();
                    }
                }
            }
        }); 
    }
    private void eventHandlerAlienMissile()
    {
        cirAlienMissile.centerYProperty().addListener(new ChangeListener()
        {
            @Override
            public void changed(ObservableValue o, Object oldVal, Object newVal)
            {
                if( (cirAlienMissile.getCenterY() > 580) && (cirAlienMissile.isVisible()) )
                {
                    //Check to see if within range of a city...
                    if(objAlienShipsManager.getAlienShipsTargetedCity() > 0)
                    {
                        Cities[objAlienShipsManager.getAlienShipsTargetedCity()].hitCity();
                        
                        if( (!Cities[1].bAliveState) && (!Cities[2].bAliveState) && (!Cities[3].bAliveState) && (!Cities[4].bAliveState) && (!Cities[5].bAliveState)  && (bGameActive))
                            gameOver();
                    }
                    
                    if(objPlayership.getAliveState() && (cirAlienMissile.getCenterX() >= (ivPlayerShip.getLayoutX() + ivPlayerShip.getTranslateX())) && (cirAlienMissile.getCenterX() <= (ivPlayerShip.getLayoutX() + ivPlayerShip.getTranslateX() + 50)) && (bGameActive))
                    {
                        objPlayership.hitPlayership();
                        
                        if(objPlayership.getNumberOfLives() == 0)
                            gameOver();
                    }
                    
                    cirAlienMissile.setVisible(false);
                }
            }
        }); 
    }
}