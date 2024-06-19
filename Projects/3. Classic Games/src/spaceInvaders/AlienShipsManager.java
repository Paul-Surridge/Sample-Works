package spaceInvaders;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;

class AlienShipsManager implements Constants{
    
    private Circle      cirAlienMissile;
    private Timer       timerAlienShipsFireMissile, timerAlienShipsMove;
    private TimerTask   ttAlienShipsFireMissile, ttAlienShipsMove;
    private Random      objRandom               = new Random();
    private AlienShip   arrAliens[][]           = new AlienShip [NUMBER_OF_ALIEN_ROWS + 1][NUMBER_OF_ALIENSHIPS_PER_ROW + 1]; 
    private Integer     iAlienShipsLayout[][]   = new Integer   [NUMBER_OF_ALIEN_ROWS + 1][NUMBER_OF_ALIENSHIPS_PER_ROW + 1];
    
    private int iNewAlienShipXPosition, iNewAlienShipYPosition, iAlienHitCounter, iCityTargeted, iAlienMissileDuration, iAlienShipsMoveDirection;

    AlienShipsManager(AnchorPane apSpaceInvaders, Circle cirAlienMissile)
    {
        //Create all the new Alien Ships rather than import them all manually from the fxml file
        for(int i = 1; i <=NUMBER_OF_ALIEN_ROWS; i++){
            for(int j = 1; j <=NUMBER_OF_ALIENSHIPS_PER_ROW; j++){
                arrAliens[i][j] = new AlienShip(iNewAlienShipXPosition, iNewAlienShipYPosition);
                apSpaceInvaders.getChildren().add(arrAliens[i][j].getAlienShipImageView());
            }
        }
        
        resetAlienShipPositions();
        
        this.cirAlienMissile = cirAlienMissile;
    }
    
    
    
    private void alienShipsMoveOnce() 
    {
        //Find the ships movement direction - if the direction is right then check to see if furthest alive ship at edge of screen
        for(int i = 1; i <=NUMBER_OF_ALIEN_ROWS; i++)
            for(int j = 1; j <=NUMBER_OF_ALIENSHIPS_PER_ROW; j++)
                if(arrAliens[i][j].getAliveState())
                    if      (arrAliens[i][j].getAlienShipXPosition() >= 868)        iAlienShipsMoveDirection = -30;
                    else if (arrAliens[i][j].getAlienShipXPosition() <= 70)         iAlienShipsMoveDirection = 30;
        
        for(int i = 1; i <=NUMBER_OF_ALIEN_ROWS; i++)
            for(int j = 1; j <=NUMBER_OF_ALIENSHIPS_PER_ROW; j++)
                if(arrAliens[i][j].getAliveState())
                    arrAliens[i][j].moveAlienShip(iAlienShipsMoveDirection);
    }
    private int alienShipsFireMissile() 
    {       
        //Randomly choose the row and ship
        int iActiveShipRow, iActiveShipNumber;

        //Find Ship - Find ship that will fire the missile...
        do
        {
            iActiveShipRow      = objRandom.nextInt(NUMBER_OF_ALIEN_ROWS) + 1;
            iActiveShipNumber   = objRandom.nextInt(NUMBER_OF_ALIENSHIPS_PER_ROW) + 1;
            
        }while(!arrAliens[iActiveShipRow][iActiveShipNumber].getAliveState());
        
        //Pass it the same object used for all missiles associated with the AlienShips object
        return arrAliens[iActiveShipRow][iActiveShipNumber].fireMissile(cirAlienMissile, iAlienMissileDuration);    
    }
    
    
    void initAlienTypes()
    {
        iAlienShipsLayout[1][1]     = ALIEN_GREEN;
        iAlienShipsLayout[1][2]     = ALIEN_GREEN;
        iAlienShipsLayout[1][3]     = ALIEN_GREEN;
        iAlienShipsLayout[1][4]     = ALIEN_GREEN;
        iAlienShipsLayout[1][5]     = ALIEN_GREEN;
        iAlienShipsLayout[1][6]     = ALIEN_GREEN;
        iAlienShipsLayout[1][7]     = ALIEN_GREEN;
        iAlienShipsLayout[1][8]     = ALIEN_GREEN;
        iAlienShipsLayout[1][9]     = ALIEN_GREEN;
        iAlienShipsLayout[1][10]    = ALIEN_GREEN;
        iAlienShipsLayout[1][11]    = ALIEN_GREEN;
        iAlienShipsLayout[1][12]    = ALIEN_GREEN;
        iAlienShipsLayout[1][13]    = ALIEN_GREEN;
        iAlienShipsLayout[1][14]    = ALIEN_GREEN;

        iAlienShipsLayout[2][1]     = ALIEN_GREEN;
        iAlienShipsLayout[2][2]     = ALIEN_GREEN;
        iAlienShipsLayout[2][3]     = ALIEN_BLUE;
        iAlienShipsLayout[2][4]     = ALIEN_BLUE;
        iAlienShipsLayout[2][5]     = ALIEN_BLUE;
        iAlienShipsLayout[2][6]     = ALIEN_BLUE;
        iAlienShipsLayout[2][7]     = ALIEN_BLUE;
        iAlienShipsLayout[2][8]     = ALIEN_BLUE;
        iAlienShipsLayout[2][9]     = ALIEN_BLUE;
        iAlienShipsLayout[2][10]    = ALIEN_BLUE;
        iAlienShipsLayout[2][11]    = ALIEN_BLUE;
        iAlienShipsLayout[2][12]    = ALIEN_BLUE;
        iAlienShipsLayout[2][13]    = ALIEN_GREEN;
        iAlienShipsLayout[2][14]    = ALIEN_GREEN;

        iAlienShipsLayout[3][1]     = ALIEN_GREEN;
        iAlienShipsLayout[3][2]     = ALIEN_BLUE;
        iAlienShipsLayout[3][3]     = ALIEN_BLUE;
        iAlienShipsLayout[3][4]     = ALIEN_BLUE;
        iAlienShipsLayout[3][5]     = ALIEN_RED;
        iAlienShipsLayout[3][6]     = ALIEN_RED;
        iAlienShipsLayout[3][7]     = ALIEN_RED;
        iAlienShipsLayout[3][8]     = ALIEN_RED;
        iAlienShipsLayout[3][9]     = ALIEN_RED;
        iAlienShipsLayout[3][10]    = ALIEN_RED;
        iAlienShipsLayout[3][11]    = ALIEN_BLUE;
        iAlienShipsLayout[3][12]    = ALIEN_BLUE;
        iAlienShipsLayout[3][13]    = ALIEN_BLUE;
        iAlienShipsLayout[3][14]    = ALIEN_GREEN;

        iAlienShipsLayout[4][1]     = ALIEN_BLUE;
        iAlienShipsLayout[4][2]     = ALIEN_BLUE;
        iAlienShipsLayout[4][3]     = ALIEN_RED;
        iAlienShipsLayout[4][4]     = ALIEN_RED;
        iAlienShipsLayout[4][5]     = ALIEN_PURPLE;
        iAlienShipsLayout[4][6]     = ALIEN_PURPLE;
        iAlienShipsLayout[4][7]     = ALIEN_PURPLE;
        iAlienShipsLayout[4][8]     = ALIEN_PURPLE;
        iAlienShipsLayout[4][9]     = ALIEN_PURPLE;
        iAlienShipsLayout[4][10]    = ALIEN_PURPLE;
        iAlienShipsLayout[4][11]    = ALIEN_RED;
        iAlienShipsLayout[4][12]    = ALIEN_RED;
        iAlienShipsLayout[4][13]    = ALIEN_BLUE;
        iAlienShipsLayout[4][14]    = ALIEN_BLUE;
 
        iAlienShipsLayout[5][1]     = ALIEN_BLUE;
        iAlienShipsLayout[5][2]     = ALIEN_RED;
        iAlienShipsLayout[5][3]     = ALIEN_RED;
        iAlienShipsLayout[5][4]     = ALIEN_PURPLE;
        iAlienShipsLayout[5][5]     = ALIEN_PURPLE;
        iAlienShipsLayout[5][6]     = ALIEN_ORANGE;
        iAlienShipsLayout[5][7]     = ALIEN_ORANGE;
        iAlienShipsLayout[5][8]     = ALIEN_ORANGE;
        iAlienShipsLayout[5][9]     = ALIEN_ORANGE;
        iAlienShipsLayout[5][10]    = ALIEN_PURPLE;
        iAlienShipsLayout[5][11]    = ALIEN_PURPLE;
        iAlienShipsLayout[5][12]    = ALIEN_RED;
        iAlienShipsLayout[5][13]    = ALIEN_RED;
        iAlienShipsLayout[5][14]    = ALIEN_BLUE;
    }
    void findTypes(int iLevel)
    {
        int iNonGreenLineStart, iNonGreenLineCounter;
        
        if(iLevel <= 2)
        {
            iNonGreenLineStart       = (iLevel + 1);
            iNonGreenLineCounter     = (iLevel + 2);
        }
        else
        {
            iNonGreenLineStart       = 4;
            iNonGreenLineCounter     = 5;            
        }
        
        for(int i = 1; i <=NUMBER_OF_ALIEN_ROWS; i++){
            for(int j = 1; j <=NUMBER_OF_ALIENSHIPS_PER_ROW; j++)
                if(i <= iNonGreenLineStart)     arrAliens[i][j].setAlienShipType(iAlienShipsLayout[iNonGreenLineCounter][j]);                   
                else                            arrAliens[i][j].setAlienShipType(iAlienShipsLayout[1][j]);

            iNonGreenLineCounter--;
        }
    }
    void startMoving()
    {
        //Reset the Alien Hit Counter
        iAlienHitCounter = 0;
        iAlienShipsMoveDirection = 30;
    
        for(int i = 1; i <=NUMBER_OF_ALIEN_ROWS; i++)
            for(int j = 1; j <=NUMBER_OF_ALIENSHIPS_PER_ROW; j++)
                arrAliens[i][j].setAliveState(true);
        
        timerAlienShipsMove = new Timer("AlienShips Move", true);
        ttAlienShipsMove    = new TimerTask() {
            @Override
            public void run() {

                Platform.runLater(() -> {
                    alienShipsMoveOnce();
                });
            }
        };

        timerAlienShipsMove.scheduleAtFixedRate(ttAlienShipsMove, 100, 1000);
    }
    void stopMoving()
    {
        timerAlienShipsMove.cancel();
    }
    void startMissiles(int iLevel)
    { 
        int iPeriod;
        
        switch(iLevel)
        {
            case(1):    iPeriod = 7000;     break;
            case(2):    iPeriod = 5000;     break;
            default:    iPeriod = 4000;     break;
        }

        //Find the speed of the Alien Ships Missiles
        switch(iLevel)
        {
            case(1):    iAlienMissileDuration = 1500;       break;
            case(2):    iAlienMissileDuration = 1200;       break;
            case(3):    iAlienMissileDuration = 1000;       break;
            case(4):    iAlienMissileDuration = 800;        break;
            default:    iAlienMissileDuration = 600;        break;
        }
        
        timerAlienShipsFireMissile = new Timer("AlienShips Fire Missile", true);
        ttAlienShipsFireMissile    = new TimerTask() {
            @Override
            public void run() {

                Platform.runLater(() -> {
                    iCityTargeted = alienShipsFireMissile();
                });
            }
        };

        timerAlienShipsFireMissile.scheduleAtFixedRate(ttAlienShipsFireMissile, 1500, iPeriod);
    }
    void stopMissiles()
    {
        cirAlienMissile.setVisible(false);
        
        timerAlienShipsFireMissile.cancel();
    }
    int getAlienShipsTargetedCity()
    {
        return iCityTargeted;
    }
    void showAllAlienShips()
    {
        for(int i = 1; i <=NUMBER_OF_ALIEN_ROWS; i++)
            for(int j = 1; j <=NUMBER_OF_ALIENSHIPS_PER_ROW; j++)
                arrAliens[i][j].show();
    }
    void hideAllAlienShips()
    {
        for(int i = 1; i <=NUMBER_OF_ALIEN_ROWS; i++)
            for(int j = 1; j <=NUMBER_OF_ALIENSHIPS_PER_ROW; j++)
                if(arrAliens[i][j].getAliveState())
                    arrAliens[i][j].hide();
    }
    void resetAlienShipPositions()
    { 
        //Initialise the positions of the alien ships
        for(int i = 1; i <=NUMBER_OF_ALIEN_ROWS; i++)
            for(int j = 1; j <=NUMBER_OF_ALIENSHIPS_PER_ROW; j++)
            {
                arrAliens[i][j].setAlienShipXPosition((j * 43));
                arrAliens[i][j].setAlienShipYPosition((i * 43 + 100));
                arrAliens[i][j].getAlienShipImageView().setTranslateX(0);
            }
    }
    int checkIfAlienHit(int iRow, Playership objPlayership, Nuke objNuke, ScoreManager objScoreManager)
    {
        double dAlienShipXOriginPosition;
        
        objPlayership.setRowChecked(iRow);
        
        for(int j = 1; j <=NUMBER_OF_ALIENSHIPS_PER_ROW; j++){
            
            dAlienShipXOriginPosition = (arrAliens[iRow][j].getAlienShipImageView().getLayoutX() + arrAliens[iRow][j].getAlienShipImageView().getTranslateX());
            
            if( (objPlayership.getPlayershipMissileXPosition() >= dAlienShipXOriginPosition) && (objPlayership.getPlayershipMissileXPosition() <= (dAlienShipXOriginPosition + 46)) && (arrAliens[iRow][j].getAliveState() == true))
            { 
                switch(objPlayership.getActiveWeapon())
                {
                    case(MISSILES):
                    {
                        objPlayership.missileStopAndReset();
                        
                        arrAliens[iRow][j].hitAlien();
                        iAlienHitCounter++;
                        objScoreManager.add(arrAliens[iRow][j].getScoreAmount());
                        
                        break;
                    }
                    case(HPMISSILES):
                    {
                        arrAliens[iRow][j].hitAlien();
                        iAlienHitCounter++;
                        objScoreManager.add(arrAliens[iRow][j].getScoreAmount());
                        
                        break;
                    }
                    case(NUKES):
                    {
                        objPlayership.missileStopAndReset();
                        
                        objNuke.explodeNuke(objPlayership.getPlayershipMissileXPosition(), (objPlayership.getPlayershipMissileYPosition() - 97));
                        
                        //Now destroy all Aliens within Radius
                        for(int k = (iRow - 1); k < (iRow + 1); k++)
                        {
                            if( (k >= 1) && (k <= 5) )
                            {
                                if( ((j-1) >= 1) && ((j-1) <= 14) )
                                {
                                    if(arrAliens[k][j-1].getAliveState())
                                    {
                                        arrAliens[k][j-1].hitAlien();
                                        iAlienHitCounter++;
                                        objScoreManager.add(arrAliens[iRow][j].getScoreAmount());
                                    }
                                }
                                if( ((j) >= 1) && ((j) <= 14) )
                                {
                                    if(arrAliens[k][j].getAliveState())
                                    {
                                        arrAliens[k][j].hitAlien();
                                        iAlienHitCounter++;
                                        objScoreManager.add(arrAliens[iRow][j].getScoreAmount());
                                    }
                                }
                                if( ((j+1) >= 1) && ((j+1) <= 14) )
                                {
                                    if(arrAliens[k][j+1].getAliveState())
                                    {
                                        arrAliens[k][j+1].hitAlien();
                                        iAlienHitCounter++;
                                        objScoreManager.add(arrAliens[iRow][j].getScoreAmount());
                                    }
                                }
                            }
                        }
                        
                        break;
                    }
                }
                
                if(iAlienHitCounter >= 70)  return LEVEL_COMPLETE;                   
                else                        return 0;
            }
        }
        
        return 0;
    }
}
