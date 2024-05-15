package pong;

import javafx.scene.control.Label;

class ScoreManager implements Constants{

    private int     iPlayer1ScoreAmount, iPlayer2ScoreAmount, iGameWinAmount;
    private Label   labPlayer1Score, labPlayer2Score;
    
    ScoreManager(Label labPlayer1Score, Label labPlayer2Score)
    {
        this.labPlayer1Score = labPlayer1Score;
        this.labPlayer2Score = labPlayer2Score;
        
        resetScores();
        refreshLabelScores();
    }
    
    
    
    private void refreshLabelScores()
    {
        labPlayer1Score.setText(Integer.toString(iPlayer1ScoreAmount));
        labPlayer2Score.setText(Integer.toString(iPlayer2ScoreAmount));
    }
    
    
    
    int getGameWinAmount()
    {
        return iGameWinAmount;
    }
    void setGameWinAmount(int iGameWinAmount)
    {
        this.iGameWinAmount = iGameWinAmount;
    }
    void resetScores()
    {
        iPlayer1ScoreAmount = 0;
        iPlayer2ScoreAmount = 0;
        
        refreshLabelScores();
    }
    void addPoint(int iPlayer)
    {
        switch(iPlayer)
        {
            case(PLAYER_1):         iPlayer1ScoreAmount++;      break;  
            case(PLAYER_2):         iPlayer2ScoreAmount++;      break;
        }
        
        refreshLabelScores();
    }
    int getPlayerPoints(int iPlayer)
    {
        if(iPlayer == PLAYER_1)     return iPlayer1ScoreAmount;
        else                        return iPlayer2ScoreAmount;
    }
}
