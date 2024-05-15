/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import SampleCode.SampleCode;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;

/**
 *
 * @author PaulSu
 */
public class App extends Application {
    
    private static Stage    stageApplication;
    private static Scene    sceneApplication;

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
        stageApplication.getIcons().add(new Image(App.class.getResource("images/Icon - Sample Code.png").toString()));
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        
        //The 'stage' which has been passed to the application by the operating system is passed
        //assigned to the local reference variable stage
        stageApplication = stage;
        
        //The parent is used to generate a scene which is then applied to the stage
        sceneApplication = new Scene(FXMLLoader.load(SampleCode.class.getResource("SampleCode.fxml")));
        stageApplication.setScene(sceneApplication);
        
        //The stage is made to display its associated scene
        initialiseStageSize();
        initialiseIcon();
        
        stageApplication.show();
        stageApplication.setTitle("Sample Code");
        
        stageApplication.setMaximized(true);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }    
}
