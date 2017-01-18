package uq.deco2800.pyramidscheme;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import uq.deco2800.pyramidscheme.game.GameManager;

import java.util.prefs.Preferences;

public class GameLauncher extends Application {

    private String version = "0.1";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        GameManager.getInstance().setPrimaryStage(primaryStage);

        // Import splash screen FXML
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/SplashScreen.fxml"));

        // get user settings from last use
        Preferences prefs = Preferences.userNodeForPackage(GameLauncher.class);
        double x = prefs.getDouble("window_x", 100);
        double y = prefs.getDouble("window_y", 100);

        // Create scene
        Scene scene = new Scene(root);
        primaryStage.setTitle("Pyramid Scheme v" + version);
        primaryStage.setMinHeight(600);
        primaryStage.setMinWidth(900);
        primaryStage.setResizable(false);
        primaryStage.setX(x);
        primaryStage.setY(y);
        primaryStage.setScene(scene);
        primaryStage.show();

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                GameManager.saveWindowSettings();
                System.exit(0);
            }
        });
    }
}
