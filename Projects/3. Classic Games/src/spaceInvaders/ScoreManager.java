package spaceInvaders;

import java.text.NumberFormat;
import java.util.Locale;
import javafx.scene.control.Label;

class ScoreManager implements Constants{

    private int     iScoreAmount;
    private Label   labScore;
    
    ScoreManager(Label labScore)
    {
        this.labScore = labScore;
        iScoreAmount = 0;
        this.labScore.setText("");
    }
    
    
    
    private String formatAmount(int iAmount)
    {
        return NumberFormat.getInstance(Locale.UK).format((double)iAmount);
    }
    
    
    
    void add(int iAmount)
    {
        iScoreAmount = iScoreAmount + iAmount;

        labScore.setText(formatAmount(iScoreAmount));
    }
    void reset()
    {
        iScoreAmount = 0;

        labScore.setText("");
    }
}
