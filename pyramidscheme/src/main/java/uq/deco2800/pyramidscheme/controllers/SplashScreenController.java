package uq.deco2800.pyramidscheme.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.text.TextFlow;
import uq.deco2800.pyramidscheme.GameLauncher;
import uq.deco2800.pyramidscheme.clock.Clock;
import uq.deco2800.pyramidscheme.game.GameManager;
import uq.deco2800.pyramidscheme.network.LoginException;
import uq.deco2800.pyramidscheme.network.NetworkException;
import uq.deco2800.pyramidscheme.player.User;
import uq.deco2800.singularity.clients.pyramidscheme.PyramidSchemeClient;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;


/**
 * A controller for the initial splash screen.
 *
 * @author Millie
 */

public class SplashScreenController implements Initializable {

    private static final String BAD_LOGIN_MESSAGE = "Bad username or password, try again";
    private static final String BAD_CONNECTION = "Can't talk to server";
    // Declare the FX objects this class uses
    @FXML
    private StackPane splashScreen;
    @FXML
    private Button loginButton;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Hyperlink registerButton;
    @FXML
    private Hyperlink loginGuestButton;
    @FXML
    private Label errorLabel;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        Preferences prefs = Preferences.userNodeForPackage(GameLauncher.class);
        usernameField.setText(prefs.get("login_username", ""));
        GameManager.playBackgroundMusic("duck_tales.wav");

        loginButton.setOnAction(this::handleLogin);
        usernameField.setOnAction(this::handleLogin);
        passwordField.setOnAction(this::handleLogin);

        registerButton.setOnAction(e -> switchToRegisterScreen());
        loginGuestButton.setOnAction(this::handleGuestLogin);
        usernameField.textProperty().addListener(e -> clearErrorMessage());
        passwordField.textProperty().addListener(e -> clearErrorMessage());

        Clock clock = new Clock();
        splashScreen.getChildren().add(1, new TextFlow(clock.getTimeUnderFormating())); //adding time to splashscreen stackpane

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                setFocus();
            }
        });
    }

    private void setFocus() {
        if (usernameField.getText().equals("")) {
            usernameField.requestFocus();
        } else {
            passwordField.requestFocus();
        }
    }

    private void clearErrorMessage() {
        errorLabel.setText("");
    }

    private void handleLogin(ActionEvent event) {
        GameManager.playClip("error.wav");
        String username = getUsernameField();
        String password = getPasswordField();
        passwordField.clear();

        try {
            PyramidSchemeClient schemeClient = GameManager.loginUser(username, password);
            GameManager.getStatisticsTracking().grabAllStats(username);
            GameManager.getAchieveTracking().grabAchievements();
            createGame(schemeClient.getUsername());
            Preferences prefs = Preferences.userNodeForPackage(GameLauncher.class);
            prefs.put("login_username", usernameField.getText());
            switchToMainMenu();
        } catch (LoginException e) {
            GameManager.playClip("error2.wav");
            errorLabel.setText(BAD_LOGIN_MESSAGE);
        } catch (NetworkException e) {
            GameManager.playClip("error2.wav");
            errorLabel.setText(BAD_CONNECTION);
        }
    }

    private void handleGuestLogin(ActionEvent event) {
        String username = "Guest";

        GameManager.getStatisticsTracking().createGuestUser();
        createGame(username);
        switchToMainMenu();
    }

    private void switchToRegisterScreen() {
        GameManager.changeScene("RegistrationScreen.fxml");
    }

    private void switchToMainMenu() {
        GameManager.changeScene("MenuScreen.fxml");
    }

    /*
     * Sets up a User and Game with the GameManager - this will be used throughout the rest of the code base.
     */
    private void createGame(String name) {
        // Get the user's name, create a User and create a new Game
        GameManager gameManager = GameManager.getInstance();

        User user = new User(name, gameManager.getGameGenerator().createBasicDeck());
        gameManager.setUser(user);
    }

    private String getUsernameField() {
        return usernameField.getText();
    }

    private String getPasswordField() {
        return passwordField.getText();
    }
}
