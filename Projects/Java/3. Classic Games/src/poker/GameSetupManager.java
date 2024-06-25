package poker;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

class GameSetupManager implements Constants{
    
    private Timeline            tlShowSetupObjects, tlHideSetupObjects;
    
    private PokerGame           objPokerGame;
    private AnchorPane          apGameSetup;
    private Slider              sliGameStartAmount, sliAnteAmount;
    private TextField           txtGameStartAmount, txtAnteAmount;
    private Button              btnGameStart;
    
    private Integer iAnteAmount, iGameStartAmount;
   
    GameSetupManager(PokerGame objPokerGame, AnchorPane apGameSetup, TextField txtGameStartAmount, Slider sliGameStartAmount, TextField txtAnteAmount, Slider sliAnteAmount, Button btnGameStart) {
        
        this.objPokerGame = objPokerGame;
        this.apGameSetup = apGameSetup;

        this.txtGameStartAmount = txtGameStartAmount;
        this.sliGameStartAmount = sliGameStartAmount;
        
        this.txtAnteAmount = txtAnteAmount;
        this.sliAnteAmount = sliAnteAmount;
        
        this.btnGameStart = btnGameStart;
        
        this.sliGameStartAmount.setValue(500);
        
        apGameSetup.setVisible(true);
        
        initTimelines();
        
        eventHandlerMouseEvents();
        eventHandlerKeyboardEvents();
        eventListnersSlidersChange();
    }
    
    
    
    private void initTimelines()
    {        
        //Revealing the Setup Game ObjectslabInGameStatusFeed
        tlShowSetupObjects = new Timeline();
        tlShowSetupObjects.setCycleCount(1);        
        tlShowSetupObjects.getKeyFrames().add(new KeyFrame(Duration.millis(0),          new KeyValue(apGameSetup.opacityProperty(), 0)));
        tlShowSetupObjects.getKeyFrames().add(new KeyFrame(Duration.millis(0),          new KeyValue(sliGameStartAmount.valueProperty(), sliGameStartAmount.getMin())));
        tlShowSetupObjects.getKeyFrames().add(new KeyFrame(Duration.millis(0),          new KeyValue(sliAnteAmount.valueProperty(), sliAnteAmount.getMin())));
        tlShowSetupObjects.getKeyFrames().add(new KeyFrame(Duration.millis(200),        new KeyValue(apGameSetup.opacityProperty(), 1)));
               
        //Hiding the Setup Game Objects
        tlHideSetupObjects = new Timeline();
        tlHideSetupObjects.setCycleCount(1);
        tlHideSetupObjects.getKeyFrames().add(new KeyFrame(Duration.millis(0),          new KeyValue(apGameSetup.opacityProperty(), 1)));
        tlHideSetupObjects.getKeyFrames().add(new KeyFrame(Duration.millis(500),        new KeyValue(apGameSetup.opacityProperty(), 0)));
    }
    private void setGameParameter(Slider sliSlider, String sAmount, int iParameter)
    {
        Slider sliLocalSlider = sliSlider;
        
        if(isNumeric(sAmount))
        {
            Integer iTextFieldValue = Integer.valueOf(sAmount);
            Integer iMinimum = sliLocalSlider.minProperty().intValue();
            Integer iMaximum = sliLocalSlider.maxProperty().intValue();

            if      (iTextFieldValue < iMinimum)        sliLocalSlider.setValue(iMinimum);
            else if (iTextFieldValue > iMaximum)        sliLocalSlider.setValue(iMaximum);
            else                                        sliLocalSlider.setValue(iTextFieldValue);
            
            if      (iTextFieldValue < iMinimum)        iTextFieldValue = iMinimum;
            else if (iTextFieldValue > iMaximum)        iTextFieldValue = iMaximum;
            
            switch(iParameter)
            {
                case ANTE_AMOUNT:               iAnteAmount         = iTextFieldValue;  break;
                case GAME_START_AMOUNT:         iGameStartAmount    = iTextFieldValue;  break;
            }
        }
        else
        {
            setGameParameter(sliSlider, Integer.toString(sliLocalSlider.minProperty().intValue()), iParameter);
        }
    }
    private boolean isNumeric(String sNumber)
    {
        if(sNumber.isEmpty())
            return false;
        
        for(int i = 0 ; i < sNumber.length() ; i++)
            if( (sNumber.charAt(i) < 48) || (sNumber.charAt(i) > 57) )
                return false;
        
        return true;
    }
    
    
    
    void hideSetupObjects()
    {
        tlHideSetupObjects.playFromStart();
    }
    void showSetupObjects()
    {
        btnGameStart.requestFocus();
        
        tlShowSetupObjects.playFromStart();
        
        iAnteAmount = (int)sliAnteAmount.getValue();
        iGameStartAmount = (int)sliGameStartAmount.getValue();
        
        //Ensure that the TextFields read the correct values...
        txtAnteAmount.setText(Integer.toString(iAnteAmount));
        txtGameStartAmount.setText(Integer.toString(iGameStartAmount));
    }
    int getAnteAmount()
    {
        return iAnteAmount; 
    }
    int getGameStartAmount()
    {
        return iGameStartAmount; 
    }

    
    
    private void eventHandlerMouseEvents()
    {
        // - - - - Handlers - - - -
        final EventHandler<MouseEvent>  ehGameStart = (MouseEvent me) -> {
            
            //Hide the current Game Setup parameters...
            setGameParameter(sliAnteAmount, txtAnteAmount.getText(), ANTE_AMOUNT);
            setGameParameter(sliGameStartAmount, txtGameStartAmount.getText(), GAME_START_AMOUNT);
            
            hideSetupObjects();
            
            objPokerGame.initialiseNewGame();
            
            me.consume();
        };
        
        btnGameStart.addEventHandler(MouseEvent.MOUSE_CLICKED, ehGameStart);
    }
    private void eventHandlerKeyboardEvents()
    {
        final EventHandler<KeyEvent>  ehAnteAmount = (KeyEvent ke) -> {

            switch(ke.getCode())
            {
                case ENTER:     setGameParameter(sliAnteAmount, txtAnteAmount.getText(), ANTE_AMOUNT);                      break;
            }
        };
        final EventHandler<KeyEvent>  ehGameStartAmount = (KeyEvent ke) -> {

            switch(ke.getCode())
            {
                case ENTER:     setGameParameter(sliGameStartAmount, txtGameStartAmount.getText(), GAME_START_AMOUNT);      break;
            }
        };
        
        txtAnteAmount.addEventHandler(KeyEvent.KEY_PRESSED, ehAnteAmount);
        txtGameStartAmount.addEventHandler(KeyEvent.KEY_PRESSED, ehGameStartAmount);
    }
    private void eventListnersSlidersChange()
    {
        // - - - - Handlers - - - -
        sliGameStartAmount.valueProperty().addListener((ObservableValue<? extends Number> ov, Number old_val, Number new_val) -> {
            iGameStartAmount = new_val.intValue();
            txtGameStartAmount.setText(Integer.toString(new_val.intValue()));
        });        

        sliAnteAmount.valueProperty().addListener((ObservableValue<? extends Number> ov, Number old_val, Number new_val) -> {
            iAnteAmount = new_val.intValue();
            txtAnteAmount.setText(Integer.toString(new_val.intValue()));
        });
    }
}
