package uq.deco2800.pyramidscheme.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import uq.deco2800.pyramidscheme.GameLauncher;
import uq.deco2800.pyramidscheme.game.GameManager;
import uq.deco2800.pyramidscheme.network.NetworkException;
import uq.deco2800.pyramidscheme.network.RegistrationException;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

/**
 * A controller for the initial splash screen.
 *
 * @author Millie
 */

public class RegistrationScreenController implements Initializable {
    private static final int MIN_USERNAME_LENGTH = 1;
    private static final int MIN_PASSWORD_LENGTH = 1;


    private static final String USER_EXISTS_ALREADY = "That username is not available";
    private static final String USERNAME_TOO_SHORT =
            "Username must be at least " + MIN_USERNAME_LENGTH + " characters";
    private static final String USERNAME_INVALID = "Username not valid";


    private static final String PWRD_MISMATCH = "Passwords do not match";
    private static final String PWRD_TOO_SHORT =
            "Password must be at least " + MIN_PASSWORD_LENGTH + " characters";
    private static final String PWRD_INVALID = "Password not valid";

    private static final String BAD_CONNECTION = "Can't connect to the server";

    // Declare the FX objects this class uses
    @FXML
    private Button createUserButton;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField verifyPasswordField;
    @FXML
    private Hyperlink backtoLoginButton;
    @FXML
    private Label errorLabel;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        // Add event handler for clicking the new match button
        createUserButton.setOnAction(this::handleCreateUser);
        usernameField.setOnAction(this::handleCreateUser);
        passwordField.setOnAction(this::handleCreateUser);

        verifyPasswordField.setOnAction(this::handleCreateUser);
        backtoLoginButton.setOnAction(e -> switchToLoginScreen());
    }


    private void switchToLoginScreen() {
        GameManager.changeScene("SplashScreen.fxml");
    }

    private String getUsernameField() {
        return usernameField.getText();
    }

    private String getPasswordField() {
        return passwordField.getText();
    }

    private String getVerifyPasswordField() {
        return verifyPasswordField.getText();
    }

    private void handleCreateUser(ActionEvent event) {
        String username = getUsernameField();
        String password = getPasswordField();
        String verifyPassword = getVerifyPasswordField();

        boolean usernameValid = true;
        boolean passwordValid = true;

        if (username.length() < MIN_USERNAME_LENGTH) {
            errorLabel.setText(USERNAME_TOO_SHORT);
            usernameValid = false;
        } else if (username.contains(" ")) {
            errorLabel.setText(USERNAME_INVALID);
            usernameValid = false;
        } else if (password.length() < MIN_PASSWORD_LENGTH) {
            errorLabel.setText(PWRD_TOO_SHORT);
            passwordValid = false;
        } else if (password.contains(" ")) {
            errorLabel.setText(PWRD_INVALID);
            passwordValid = false;
        } else if (!password.equals(verifyPassword)) {
            errorLabel.setText(PWRD_MISMATCH);
            passwordValid = false;
        }


        if (!passwordValid) {
            GameManager.playClip("error2.wav");
            passwordField.clear();
            verifyPasswordField.clear();
        } else if (!usernameValid) {
            GameManager.playClip("error2.wav");
            usernameField.clear();
            passwordField.clear();
            verifyPasswordField.clear();
        } else {
            try {
                GameManager.createUser(username, password);
                GameManager.getStatisticsTracking().createNewUser(username, password);
            } catch (NetworkException e) {
                GameManager.playClip("error2.wav");
                errorLabel.setText(BAD_CONNECTION);
                return;
            } catch (RegistrationException e) {
                GameManager.playClip("error2.wav");
                errorLabel.setText(USER_EXISTS_ALREADY);
                return;
            }

            GameManager.playClip("victory.wav");
            Preferences prefs = Preferences.userNodeForPackage(GameLauncher.class);
            prefs.put("login_username", usernameField.getText());
            switchToLoginScreen();
        }
    }
}
