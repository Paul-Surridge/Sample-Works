package poker;

interface Constants {

    //Constants
    int NUMBER_OF_CARDS                                     = 52;
    int NUMBER_OF_CARDS_PER_SUIT                            = 14;
    int NUMBER_OF_SUITS                                     = 4;
    int NUMBER_OF_CARDS_PER_HAND                            = 5;
    int NUMBER_OF_PLAYERS                                   = 4;
    
    int GAME_RUNNING                                        = 1;
    int GAME_PAUSE                                          = 2;
    int GAME_OVER                                           = 3;

    int PLAYER_1                                            = 1;
    int PLAYER_2                                            = 2;
    int PLAYER_3                                            = 3;
    int PLAYER_4                                            = 4;
    
    int CARD_1                                              = 1;
    int CARD_2                                              = 2;
    int CARD_3                                              = 3;
    int CARD_4                                              = 4;
    int CARD_5                                              = 5;
    
    int ALL_PLAYERS_SEEN_TABLE                              = 99;
    int ALL_PLAYERS_CHANGED_CARDS                           = 98;
    
    int ANTE                                                = 100;
    int SEE                                                 = 101;
    int RAISE                                               = 102;
    
    int BETTING_ROUND_1                                     = 101;
    int BETTING_ROUND_2                                     = 102;
    
    int BETTING_ROUND                                       = 121;
    int CHANGE_CARDS                                        = 122;
    int ALL_PLAYER_BUTTONS_INACTIVE                         = 123;
    
    int COMPUTER_PLAYER_FOLD                                = 111;
    int COMPUTER_PLAYER_CHECK                               = 112;
    int COMPUTER_PLAYER_SEE                                 = 113;
    int COMPUTER_PLAYER_RAISE                               = 114;
    
    int HUMAN_PLAYER                                        = 1;
    int COMPUTER_PLAYER                                     = 2;
    
    int POT_NOT_RAISED                                      = 1;
    int POT_RAISED                                          = 2;
    
    int CARD_NOT_DEALT                                      = 10;
    int CARD_DEALT                                          = 11;
    
    int IMMEDIATE                                           = 1;
    int FADE                                                = 2;
    
    int ALL_OTHER_PLAYERS_FOLDED                            = 1;
    int HIGHEST_HAND                                        = 2;
    
    double CARD_HIGHLIGHT_OPACITY_ON                        = 0.5;
    double CARD_HIGHLIGHT_OPACITY_OFF                       = 0;
    
    int ANTE_AMOUNT                                         = 1;
    int GAME_START_AMOUNT                                   = 2;
    
    int CARD_HOLD_ON                                        = 1;
    int CARD_HOLD_OFF                                       = 2;
    
    int CARD_FOLD_ON                                        = 3;
    int CARD_FOLD_OFF                                       = 4;

    int CLUBS                                               = 1;
    int HEARTS                                              = 2;
    int SPADES                                              = 3;
    int DIAMONDS                                            = 4;
    
    int JACK                                                = 11;
    int QUEEN                                               = 12;
    int KING                                                = 13;
    int ACE                                                 = 14;
    int BACK                                                = 99;

    int NEW_GAME_INITIALISATION_COMPLETE                    = 1;
    int CARDS_DEALT_COMPLETE                                = 2;
    int PLAYERS_ANTE_COMPLETE                               = 3;
    int BETTING_ROUND_ONE_COMPLETE                          = 4;
    int CHANGE_CARDS_COMPLETE                               = 5;
    int BETTING_ROUND_TWO_COMPLETE                          = 6; 

    int ROYAL_FLUSH                                         = 10;
    int STRIAGHT_FLUSH                                      = 9;
    int FOUR_OF_A_KIND                                      = 8;
    int FULL_HOUSE                                          = 7;
    int FLUSH                                               = 6;
    int STRAIGHT                                            = 5;
    int THREE_OF_KIND                                       = 4;
    int TWO_PAIR                                            = 3;
    int ONE_PAIR                                            = 2;
    int HIGH_CARD                                           = 1;
    
    int FOUR_CARD_STRAIGHT                                  = 11;
    int FOUR_SAME_SUIT                                      = 12;
    int THREE_CARD_STRAIGHT                                 = 13;
    int KEEP_HIGHEST_CARD                                   = 14;
    int REPLACE_ALL_CARDS                                   = 15;

    int NULL                                                = 0;
    int RESET                                               = 0;
    
    int ON                                                  = 1;
    int OFF                                                 = 2;
    
    int NOT_VISIBLE                                         = 0;
    int VISIBLE                                             = 1;
    
    int CARD_WIDTH_VERTICAL                                 = 80;
    int CARD_HEIGHT_VERTICAL                                = 110;
    
    int CARD_WIDTH_HORIZONTAL                               = 110;
    int CARD_HEIGHT_HORIZONTAL                              = 76;
    
    int CARD_COVER_WIDTH_VERTICAL                           = 74;
    int CARD_COVER_HEIGHT_VERTICAL                          = 98;
    
    int CARD_COVER_WIDTH_HORIZONTAL                         = 98;
    int CARD_COVER_HEIGHT_HORIZONTAL                        = 74;
      
    int CARD_TEXT_HEIGHT_VERTICAL                           = 98;
    int CARD_TEXT_WIDTH_VERTICAL                            = 74;
    
    int CARD_TEXT_HEIGHT_HORIZONTAL                         = 74;
    int CARD_TEXT_WIDTH_HORIZONTAL                          = 98;
    
    int CARD_TEXT_X_OFFSET_VERTICAL                         = 7;
    int CARD_TEXT_Y_OFFSET_VERTICAL                         = 10;
    
    int CARD_TEXT_X_OFFSET_HORIZONTAL                       = 23;
    int CARD_TEXT_Y_OFFSET_HORIZONTAL                       = 0;
    
    int X_LAYOUT_COORDINATE                                 = 0;
    int Y_LAYOUT_COORDINATE                                 = 1;
    
    //Player 1
    int PLAYER_1_CARDS_LAYOUT_Y_COORDINATE                  = 479;
        
    int PLAYER_1_CARD_1_LAYOUT_X_COORDINATE                 = 290;
    int PLAYER_1_CARD_2_LAYOUT_X_COORDINATE                 = 365;
    int PLAYER_1_CARD_3_LAYOUT_X_COORDINATE                 = 440;
    int PLAYER_1_CARD_4_LAYOUT_X_COORDINATE                 = 515;
    int PLAYER_1_CARD_5_LAYOUT_X_COORDINATE                 = 590;
   
    
    //Player 2
    int PLAYER_2_CARDS_LAYOUT_X_COORDINATE                  = 40;
 
    int PLAYER_2_CARD_1_LAYOUT_Y_COORDINATE                 = 150;
    int PLAYER_2_CARD_2_LAYOUT_Y_COORDINATE                 = 225;
    int PLAYER_2_CARD_3_LAYOUT_Y_COORDINATE                 = 300;
    int PLAYER_2_CARD_4_LAYOUT_Y_COORDINATE                 = 375;
    int PLAYER_2_CARD_5_LAYOUT_Y_COORDINATE                 = 450;
    
    
    //Player 3
    int PLAYER_3_CARDS_LAYOUT_Y_COORDINATE                  = 95;
         
    int PLAYER_3_CARD_1_LAYOUT_X_COORDINATE                 = 290;
    int PLAYER_3_CARD_2_LAYOUT_X_COORDINATE                 = 365;
    int PLAYER_3_CARD_3_LAYOUT_X_COORDINATE                 = 440;
    int PLAYER_3_CARD_4_LAYOUT_X_COORDINATE                 = 515;
    int PLAYER_3_CARD_5_LAYOUT_X_COORDINATE                 = 590;
    
    
    //Player 4
    int PLAYER_4_CARDS_LAYOUT_X_COORDINATE                  = 800;
      
    int PLAYER_4_CARD_1_LAYOUT_Y_COORDINATE                 = 150;
    int PLAYER_4_CARD_2_LAYOUT_Y_COORDINATE                 = 225;
    int PLAYER_4_CARD_3_LAYOUT_Y_COORDINATE                 = 300;
    int PLAYER_4_CARD_4_LAYOUT_Y_COORDINATE                 = 375;
    int PLAYER_4_CARD_5_LAYOUT_Y_COORDINATE                 = 450;
        
    int GAME_TIE                                            = 99;
    
    int NEXT_ROUND                                          = 1;
    int NEW_GAME                                            = 2;
    
    int PLAYER_LEAVES_GAME                                  = 1;
    
    int HIGHEST_VALUE_CARD                                  = 5;
}
