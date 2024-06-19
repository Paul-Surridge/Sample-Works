package pong;

interface Constants {

    int GAME_RUNNING                                = 1;
    int GAME_PAUSE                                  = 2;
    int GAME_OVER                                   = 3;

    int DEFAULT_BEST_OF_AMOUNT                      = 5;
    int DEFAULT_BALL_SPEED                          = 5;
    int DEFAULT_BAT_SIZE                            = 5;
    
    int START                                       = 1;
    int STOP                                        = 2;
    int PLAY_MODE                                   = 1;
    int PRACTICE_MODE                               = 2;
    int INITIAL_PLAY                                = 3;
    int BALL_REFLECT                                = 4;
    int BALL_ABSORB                                 = 5;
    
    int BALL_RATE_FULL                              = 1;
    int BALL_RATE_HALF                              = 2;
    
    int UP                                          = 1;
    int DOWN                                        = 2;
    int LEFT                                        = 3;
    int RIGHT                                       = 4;
    
    int TOP_BOUNDARY                                = 5;
    int BOTTOM_BOUNDARY                             = 6;
    int LEFT_BOUNDARY                               = 7;
    int RIGHT_BOUNDARY                              = 8;
    
    int PLAYING_AREA_TOP_Y_COORDINATE               = -270;
    int PLAYING_AREA_BOTTOM_Y_COORDINATE            = 270;
    int PLAYING_AREA_LEFT_X_COORDINATE              = -445;
    int PLAYING_AREA_RIGHT_X_COORDINATE             = 445;
    
    int PLAYING_AREA_WIDTH                          = 900;
    int PLAYING_AREA_HEIGHT                         = 540;
    
    double  ANGLE_OF_INFLECTION                     = 1.30899694;   //Radians (15 Degrees)
    
    double  PLAYER_1_LAYOUT_X_POSITION_INITIALISE   = 55;
    double  PLAYER_2_LAYOUT_X_POSITION_INITIALISE   = 885;
    double  TABLE_CENTRE_Y_POSITION_INITIALISE      = 350;
    
    int PLAYER_1                                    = 1;
    int PLAYER_2                                    = 2;
}
