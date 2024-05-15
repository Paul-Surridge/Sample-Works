package missileCommand;

interface Constants {

    int LEFT                                        = 1;
    int RIGHT                                       = 2;
    
    int START                                       = 1;
    int STOP                                        = 2;
    
    int NOT_VISIBLE                                 = 0;
    int VISIBLE                                     = 1;
    
    int LEVEL_COMPLETE                              = 99;
    
    int NUMBER_OF_CITIES                            = 5;
    int NUMBER_OF_PLAYER_MISSILES                   = 20;
    int NUMBER_OF_ALIEN_MISSILES                    = 100;
     
    int PLAYER_SHIP_LEFT_LAYOUT_X_START             = 81;
    int PLAYER_SHIP_LEFT_LAYOUT_Y_START             = 580;
    
    int PLAYER_SHIP_RIGHT_LAYOUT_X_START            = 859;
    int PLAYER_SHIP_RIGHT_LAYOUT_Y_START            = 580;
    
    int SHOOTING_AREA_LAYOUT_X_OFFSET               = 15;
    int SHOOTING_AREA_LAYOUT_Y_OFFSET               = 68;
    
    int PLAYER_SHIP_MISSILE_MAXIMUM_TRAVEL_DISTANCE = 931;
    
    int NOT_USED                                    = 9999;
    
    int RESET                                       = 0;
    
    int CITY_1                                      = 1;
    int CITY_2                                      = 2;
    int CITY_3                                      = 3;
    int CITY_4                                      = 4;
    int CITY_5                                      = 5;
    int PLAYER_SHIP_LEFT                            = 6;
    int PLAYER_SHIP_RIGHT                           = 7;
    int NO_CITY_TARGETED                            = 99;
    
    int ALL_ALIEN_MISSILES_START_Y_POSITION         = 68;
    int ALL_ALIEN_MISSILES_DESTINATION_Y_POSITION   = 512;
    
    int SHOOTING_AREA_WIDTH                         = 919;
    int SHOOTING_AREA_HEIGHT                        = 512;
    
    int PLAYER_SHIP_GUN_Y_POSITION                  = 570;
    int PLAYER_SHIP_LEFT_GUN_X_POSITION             = 85;
    int PLAYER_SHIP_RIGHT_GUN_X_POSITION            = 860;
    
    int SCORE_AMOUNT_PER_SHIP                       = 2000;
    int SCORE_AMOUNT_PER_CITY                       = 1000;
    int SCORE_AMOUNT_PER_NUKE                       = 10;
    
    double TIMELINE_DUMMY_KEYFRAME_DOUBLE_VALUE_1   = 0.1;
    double TIMELINE_DUMMY_KEYFRAME_DOUBLE_VALUE_2   = 0.2;
    double TIMELINE_DUMMY_KEYFRAME_DOUBLE_VALUE_3   = 0.3;
    double TIMELINE_DUMMY_KEYFRAME_DOUBLE_VALUE_4   = 0.4;
    double TIMELINE_DUMMY_KEYFRAME_DOUBLE_VALUE_5   = 0.5;
    double TIMELINE_DUMMY_KEYFRAME_DOUBLE_VALUE_6   = 0.6;
}
