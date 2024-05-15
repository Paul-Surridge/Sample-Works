package splash;

import app.App;
import app.Scenes;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class Help implements Initializable {

    @FXML
    private ImageView ivExitHelp;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        eventHandlerMouseEvents();
    }

    private void eventHandlerMouseEvents()
    {
        final EventHandler<MouseEvent>  ehExitClassicGamesHelp = (MouseEvent me) -> {
            try{
                App.loadScene(Scenes.SPLASH);
            }
            catch(Exception ex){
                System.out.println(ex.toString()); 
            }
        };
        
        ivExitHelp.addEventHandler(MouseEvent.MOUSE_CLICKED, ehExitClassicGamesHelp); 
    }
}
