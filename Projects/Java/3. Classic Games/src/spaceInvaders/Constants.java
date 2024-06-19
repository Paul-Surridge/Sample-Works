package spaceInvaders;

import app.App;
import javafx.scene.image.Image;

interface Constants {

    int ALIEN_GREEN                           = 1;
    int ALIEN_BLUE                            = 2;
    int ALIEN_RED                             = 3;
    int ALIEN_PURPLE                          = 4;
    int ALIEN_ORANGE                          = 5;
    
    Image imgExplosion                        = new Image(App.class.getResource("images/spaceInvaders/Explosion.png").toString());
    
    Image imgAlienGreen                       = new Image(App.class.getResource("images/spaceInvaders/Alien - Green.png").toString());
    Image imgAlienBlue                        = new Image(App.class.getResource("images/spaceInvaders/Alien - Blue.png").toString());
    Image imgAlienRed                         = new Image(App.class.getResource("images/spaceInvaders/Alien - Red.png").toString());
    Image imgAlienPurple                      = new Image(App.class.getResource("images/spaceInvaders/Alien - Purple.png").toString());
    Image imgAlienOrange                      = new Image(App.class.getResource("images/spaceInvaders/Alien - Orange.png").toString());
    
    Image imgNuke                             = new Image(App.class.getResource("images/spaceInvaders/Nuke.png").toString());
    Image imgCity                             = new Image(App.class.getResource("images/spaceInvaders/City.png").toString());
    
    Image imgCollectNuke                      = new Image(App.class.getResource("images/spaceInvaders/Collect - Nuke.png").toString());
    Image imgCollectHPMissiles                = new Image(App.class.getResource("images/spaceInvaders/Collect - High Powered Missile.png").toString());
    
    Image imgMothership                       = new Image(App.class.getResource("images/spaceInvaders/Mothership.png").toString());
    Image imgMothershipExplosion              = new Image(App.class.getResource("images/spaceInvaders/Explosion.png").toString());
    
    int NOT_DEFINED                           = 0;
    
    int LEFT                                  = 1;
    int RIGHT                                 = 2;
    
    int START                                 = 1;
    int STOP                                  = 2;
    
    int LEVEL_COMPLETE                        = 99;
    
    int NUKES                                 = 1;
    int HPMISSILES                            = 2;
    int MISSILES                              = 3;
    
    int NUMBER_OF_ALIEN_ROWS                  = 5;
    int NUMBER_OF_ALIENSHIPS_PER_ROW          = 14;

    int PLAYING_AREA_LEFT_X_COORDINATE        = 25;
    int PLAYING_AREA_RIGHT_X_COORDINATE       = 922;
    
    int POWER_UP_MINIMUM_X_COORDINATE         = -400;
    int POWER_UP_MAXIMUM_X_COORDINATE         = 300;
    int POWER_UP_X_RANGE                      = 700;

    int NUMBER_OF_CITIES                      = 5;
    
    int PLAYERSHIP_START_X_COORDINATE         = 480;
}
