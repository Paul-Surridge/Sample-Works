package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import missileCommand.MissileCommand;
import poker.PokerGame;
import pong.Pong;
import spaceInvaders.SpaceInvaders;
import splash.Help;
import splash.Splash;

/*
    App:

        - App (which is an extension of Application) encapsulates the whole JavaFX application.
        - The JavaFX Runtime does the following:

            1. Constructs an instance of App.
            2. Calls init()                         Which can be overriden if required.
            3. Calls start(javafx.stage.Stage)      Passes in the Stage from the OS for use by the JavaFX program.

            NB: Hence the lack of need to explicitly call public static void main() within App (for presumably this is all handled within Application).

        - Everything then runs within App e.g.

            1. OS passes in a Stage.
            2. Individual .fxml files are loaded via FXMLLoader.load() and assigned to scenes which instantiate their respective Controller Class as assigned in SceneBuilder.
            3. Upon instantiating their Controller Class, the Controller Class pulls in all of the objects within the .fxml file via each objects fxid.

                Game                Controller Class
                _______________________________________

                Missile Command     missilecommand.java
                Poker               poker.java
                Pong                pong.java
                Space Invaders      spaceinvaders.java

            4. Each games Controller Class instantiates and encapsulates all of the objects that the game needs to run.
            5. The Stage passed in by the OS can be assigned different scenes i.e. games.

        - App is ultimately used to assign the Games (Scenes) to the Stage.
*/
public class App extends Application {

    private static Stage    stageApplication;
    private static Scene    scScene;
    
    @Override
    public void start(Stage stage) throws Exception {
        
        //The 'stage' which has been passed to the application by the operating system is passed/assigned
        //to the reference variable stageApplication so that the Stage can be assigned different Scenes as required.
        stageApplication = stage;
        
        stageApplication.getIcons().add(new Image(App.class.getResource("images/Icon - Classic Games.png").toString()));
        stageApplication.setTitle("Classic Games");

        loadScene(Scenes.SPLASH);

        stageApplication.show();
    }
            
    //The controller class associated with each .fxml file needs to be reloaded in order to call initialize() within the class.
    //Consequently needs to be called each time the game is started/selected.    
    public static void loadScene(Scenes scene) throws Exception
    {
        //The fxml file including the associated controller class is assigned to a parent, whereby
        //it appears that .getResource() must search the passed classes local directory in search of the .fxml file.
        
        switch(scene)
        {
            case SPLASH:                scScene = new Scene(FXMLLoader.load(Splash.class.getResource("splash.fxml")));                      break;
            case HELP:                  scScene = new Scene(FXMLLoader.load(Help.class.getResource("help.fxml")));                          break;
            case MISSILE_COMMAND:       scScene = new Scene(FXMLLoader.load(MissileCommand.class.getResource("missileCommand.fxml")));      break;
            case POKER:                 scScene = new Scene(FXMLLoader.load(PokerGame.class.getResource("pokerGame.fxml")));                break;
            case PONG:                  scScene = new Scene(FXMLLoader.load(Pong.class.getResource("pong.fxml")));                          break;
            case SPACE_INVADERS:        scScene = new Scene(FXMLLoader.load(SpaceInvaders.class.getResource("spaceInvaders.fxml")));        break;
        }
        
        stageApplication.setScene(scScene);
    }
}
