package App;

import BankAccountsManager.BankAccountsManager;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class App extends Application {
    
    private static Stage    stageApplication;
    private static Scene    sceneApplication;

    //Internal -----------------------------------------------------------------
    private void initialiseStageSize()
    {
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        stageApplication.setX(primaryScreenBounds.getMinX());
        stageApplication.setY(primaryScreenBounds.getMinY());

        stageApplication.setMaxWidth(primaryScreenBounds.getWidth());
        stageApplication.setMinWidth(primaryScreenBounds.getWidth());

        stageApplication.setMaxHeight(primaryScreenBounds.getHeight());
        stageApplication.setMinHeight(primaryScreenBounds.getHeight());
    }
    private void initialiseIcon()
    {
        stageApplication.getIcons().add(new Image(App.class.getResource("images/Icon - Bank Accounts Manager.png").toString()));
    }
    
    //External API -------------------------------------------------------------
    @Override
    public void start(Stage stage) throws Exception
    {
        //The 'stage' which has been passed to the application by the operating system is passed and assigned to the local reference variable stage
        stageApplication = stage;
        
        //The parent is used to generate a scene which is then applied to the stage
        sceneApplication = new Scene(FXMLLoader.load(BankAccountsManager.class.getResource("BankAccountsManager.fxml")));
        stageApplication.setScene(sceneApplication);
        
        //The stage is made to display its associated scene
        initialiseStageSize();
        initialiseIcon();
        
        stageApplication.show();
        stageApplication.setTitle("Bank Accounts Manager - Demo");
        
        stageApplication.setMaximized(true);
    }
    public static void main(String[] args)
    {
        launch(args);
    }
}
