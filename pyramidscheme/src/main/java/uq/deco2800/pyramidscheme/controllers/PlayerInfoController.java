package uq.deco2800.pyramidscheme.controllers;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import uq.deco2800.pyramidscheme.game.GameManager;
import uq.deco2800.pyramidscheme.network.NetworkException;
import uq.deco2800.pyramidscheme.network.RegistrationException;
import uq.deco2800.singularity.clients.pyramidscheme.PyramidSchemeClient;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * A controller for the Player Information Screen.
 *
 * @author Liam Simpson
 */

public class PlayerInfoController implements Initializable {

    private static final int MIN_USERNAME_LENGTH = 1;
    private static final int MIN_PASSWORD_LENGTH = 1;

    private static final String DEFAULT_AVATAR = "/chatRoomImages/default_avatar.png";

    private static final String USER_EXISTS_ALREADY = "That username is not available";
    private static final String USERNAME_TOO_SHORT =
            "Username must be at least " + MIN_USERNAME_LENGTH + " characters";
    private static final String USERNAME_INVALID = "Username not valid";

    private static final String PASSWORD_ERROR_MISMATCH = "Passwords do not match";
    private static final String PASSWORD_ERROR_TOO_SHORT =
            "Password must be at least " + MIN_PASSWORD_LENGTH + " characters";
    private static final String PASSWORD_ERROR_INVALID = "Password not valid";

    private static final String BAD_CONNECTION = "Can't connect to the server";

    private PyramidSchemeClient schemeClient;


    // Declare the FX objects this class uses
    @FXML
    private Button playerInfoButton;
    @FXML
    private Button changeDetailbutton;
    @FXML
    private StackPane playerInfo;
    @FXML
    private VBox scroll;
    @FXML
    private VBox leftBox;
    @FXML
    private Button backButtonSettings;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField verifyPasswordField;
    @FXML
    private Label errorLabel;


    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        schemeClient = GameManager.getInstance().getPyramidSchemeClient();

        backButtonSettings.setOnAction(e -> GameManager.changeScene("MenuScreen.fxml"));

        /**
         * Initializes the Porthole image on the Player Settings Screen.
         */
        scroll.setAlignment(Pos.CENTER);
        leftBox.setAlignment(Pos.CENTER);

        ImageView portIMG = new ImageView(new Image(DEFAULT_AVATAR));
        portIMG.setFitHeight(190);
        portIMG.setPreserveRatio(true);
        leftBox.getChildren().add(portIMG);

        Task<String> avatarTask = fetchUsersAvatarTask();

        avatarTask.setOnSucceeded(e -> {
            String avatar = avatarTask.getValue();
            portIMG.setImage(new Image(avatar));
        });

        new Thread(avatarTask).start();


        /**
         * Creates a list of all the avatar images that need to be turned into
         * ImageView Images within the for loop.
         */

        List<String> filenames = new ArrayList<>();
        filenames.add("/chatRoomImages/default_avatar.png");
        filenames.add("/chatRoomImages/avatar1.png");
        filenames.add("/chatRoomImages/avatar2.png");
        filenames.add("/chatRoomImages/avatar3.png");
        filenames.add("/chatRoomImages/avatar4.png");
        filenames.add("/chatRoomImages/avatar5.png");
        filenames.add("/chatRoomImages/avatar6.png");
        filenames.add("/chatRoomImages/avatar7.png");
        filenames.add("/chatRoomImages/avatar8.png");
        filenames.add("/chatRoomImages/avatar9.png");
        filenames.add("/chatRoomImages/avatar10.png");
        filenames.add("/chatRoomImages/avatar11.png");
        filenames.add("/chatRoomImages/avatar12.png");
        filenames.add("/chatRoomImages/avatar13.png");
        filenames.add("/chatRoomImages/avatar14.png");
        filenames.add("/chatRoomImages/avatar15.png");
        filenames.add("/chatRoomImages/avatar16.png");
        filenames.add("/chatRoomImages/avatar17.png");
        filenames.add("/chatRoomImages/avatar18.png");
        filenames.add("/chatRoomImages/avatar19.png");
        filenames.add("/chatRoomImages/avatar20.png");

        /**
         * For every avatar image in the list above the loop is initiated.
         */

        for (String file : filenames) {

            /**
             * Each of the images in the list is turned into an ImageView.
             * The selectblur image is also set to an image view.
             * selectblur is set to invisible and is only displayed when the mouse is hovering over the image.
             * The avatar and the selectblur are grouped together so the avatar is in front of the blur image.
             * The imageview formatting is set.
             * When the mouse is hovering over an avatar the visbility of the blur image is set to true.
             * When the mouse is not hovering over an avatar the visibility of the blur is set to false.
             * The groups for each avatar is placed inside the VBox which is also inside a ScrollPane.
             */

            ImageView avatar = new ImageView(new Image(file));
            ImageView blur = new ImageView(new Image("/chatRoomImages/selectblur.png"));
            blur.setVisible(false);
            Group avatarGROUP = new Group(blur, avatar);
            avatar.setFitHeight(120);
            avatar.setPreserveRatio(true);
            blur.setFitHeight(120);
            blur.setPreserveRatio(true);
            avatar.setOnMouseEntered(e -> avatarImgViewMouseOver(avatarGROUP, true));
            avatar.setOnMouseExited(e -> avatarImgViewMouseOver(avatarGROUP, false));
            avatar.setOnMouseClicked(e -> avatarImgViewClick(avatarGROUP, file, false));
            scroll.getChildren().add(avatarGROUP);

        }
    }

    /**
     * Gets the user id as a string and returns it as the variable "avatar".
     *
     * @return
     */

    private Task<String> fetchUsersAvatarTask() {
        String userId = schemeClient.getUserId();

        return new Task<String>() {
            @Override
            protected String call() throws Exception {
                Optional<String> avatar = schemeClient.getUsersAvatar(userId);

                return avatar.orElse(DEFAULT_AVATAR);
            }
        };
    }

    /**
     * When SetOnMouseEntered and SetOnMouseExited is activated the following code runs.
     * We get the first item inside the group (select blur image) and set its visibility to true.
     *
     * @param avatars
     * @param val
     */

    private void avatarImgViewMouseOver(Group avatars, boolean val) {
        avatars.getChildren().get(0).setVisible(val);
        changeDetailbutton.setOnAction(this::handleChangeUser);
        usernameField.setOnAction(this::handleChangeUser);
        passwordField.setOnAction(this::handleChangeUser);

        verifyPasswordField.setOnAction(this::handleChangeUser);
    }

    /**
     * When SetOnMouseClicked is activated the following code runs.
     * The visibility of the select blur effect is set to false.
     * the avatar inside the porthole is removed.
     * A new image view is created and placed inside the porthole.
     * The avatar placed in the porthole is the avatar the user clicked on.
     *
     * @param avatars
     * @param file
     * @param val
     */

    private void avatarImgViewClick(Group avatars, String file, boolean val) {
        avatars.getChildren().get(0).setVisible(val);
        leftBox.getChildren().clear();
        ImageView portIMG = new ImageView(new Image(file));
        portIMG.setFitHeight(190);
        portIMG.setPreserveRatio(true);
        leftBox.getChildren().add(portIMG);

        new Thread(() -> schemeClient.setUsersAvatar(file)).start();
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

    private void handleChangeUser(ActionEvent event) {
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
            errorLabel.setText(PASSWORD_ERROR_TOO_SHORT);
            passwordValid = false;
        } else if (password.contains(" ")) {
            errorLabel.setText(PASSWORD_ERROR_INVALID);
            passwordValid = false;
        } else if (!password.equals(verifyPassword)) {
            errorLabel.setText(PASSWORD_ERROR_MISMATCH);
            passwordValid = false;
        }

        if (!passwordValid) {
            passwordField.clear();
            verifyPasswordField.clear();
        } else if (!usernameValid) {
            usernameField.clear();
            passwordField.clear();
            verifyPasswordField.clear();
        } else {
            try {
                GameManager.changeUser(username, password);
            } catch (NetworkException e) {
                errorLabel.setText(BAD_CONNECTION);
            } catch (RegistrationException e) {
                errorLabel.setText(USER_EXISTS_ALREADY);
            }
        }

    }
}