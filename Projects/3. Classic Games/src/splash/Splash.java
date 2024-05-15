package splash;

import app.App;
import app.Scenes;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;

public class Splash implements Initializable {

    @FXML
    private ImageView           ivPokerStart;    
    @FXML
    private ImageView           ivSpaceInvadersStart;
    @FXML
    private ImageView           ivPongStart;
    @FXML
    private ImageView           ivMissileCommandStart;
    @FXML
    private ImageView           ivHelp;
    
    @FXML
    private Label               labPokerStart;
      
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        eventHandlerMouseEvents();
        
        //Scene Builder 2.0 unable to be installed on machine.
        labPokerStart.setFont(new Font("Agent Orange", 38));
    }

    private void eventHandlerMouseEvents()
    {
        final EventHandler<MouseEvent>  ehPokerStart = (MouseEvent me) -> {
            try {
                App.loadScene(Scenes.POKER);
            } catch (Exception ex) {
                Logger.getLogger(Splash.class.getName()).log(Level.SEVERE, null, ex);
            }
        };
        
        final EventHandler<MouseEvent>  ehMissileCommandStart = (MouseEvent me) -> {
            try {
                App.loadScene(Scenes.MISSILE_COMMAND);
            } catch (Exception ex) {
                Logger.getLogger(Splash.class.getName()).log(Level.SEVERE, null, ex);
            }
        };
        
        final EventHandler<MouseEvent>  ehSpaceInvadersStart = (MouseEvent me) -> {
            try {
                App.loadScene(Scenes.SPACE_INVADERS);
            } catch (Exception ex) {
                Logger.getLogger(Splash.class.getName()).log(Level.SEVERE, null, ex);
            }
        };
        
        final EventHandler<MouseEvent>  ehPongStart = (MouseEvent me) -> {
            try {
                App.loadScene(Scenes.PONG);
            } catch (Exception ex) {
                Logger.getLogger(Splash.class.getName()).log(Level.SEVERE, null, ex);
            }
        };
        
        final EventHandler<MouseEvent>  ehHelp = (MouseEvent me) -> {
            try {
                App.loadScene(Scenes.HELP);
            } catch (Exception ex) {
                Logger.getLogger(Splash.class.getName()).log(Level.SEVERE, null, ex);
            }
        };
        
        ivPokerStart            .addEventHandler(MouseEvent.MOUSE_CLICKED, ehPokerStart);
        ivMissileCommandStart   .addEventHandler(MouseEvent.MOUSE_CLICKED, ehMissileCommandStart);
        ivSpaceInvadersStart    .addEventHandler(MouseEvent.MOUSE_CLICKED, ehSpaceInvadersStart);
        ivPongStart             .addEventHandler(MouseEvent.MOUSE_CLICKED, ehPongStart);
        ivHelp                  .addEventHandler(MouseEvent.MOUSE_CLICKED, ehHelp);

        labPokerStart           .addEventHandler(MouseEvent.MOUSE_CLICKED, ehPokerStart);   
    }
}
