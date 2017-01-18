package uq.deco2800.pyramidscheme.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.text.TextFlow;
import uq.deco2800.pyramidscheme.clock.Clock;
import uq.deco2800.pyramidscheme.game.GameManager;
import uq.deco2800.pyramidscheme.player.Player;
import uq.deco2800.pyramidscheme.player.User;
import uq.deco2800.pyramidscheme.pyramid.PyramidType;
import uq.deco2800.singularity.clients.pyramidscheme.PyramidSchemeClient;

import javax.swing.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;


/**
 * A controller for the main menu.
 *
 * @author Millie
 */

public class MenuController implements Initializable {

    // Declare the FX objects this class uses
    @FXML
    private Button tutModeButton; // for tutorial mode

    @FXML
    private Button newBasicMatchButton;
    @FXML
    private Button storyModeButton;
    @FXML
    private Button viewDeckButton;
    @FXML
    private Button openPacksButton;
    @FXML
    private Button chatRoomButton;
    @FXML
    private Label welcomeLabel;
    @FXML
    private Button playerInfoButton;
    @FXML
    private StackPane menuScreen;
    @FXML
    private ComboBox deckSelect;
    @FXML
    private Button achievementButton;
    @FXML
    private Button settingsButton;
    @FXML
    private Button multiplayerButton;

    private String separator = ": ";
    private User user;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        user = GameManager.getInstance().getUser();

        // Shuffle an array of possible pyramid types
        ArrayList<PyramidType> types = new ArrayList<>();
        Collections.addAll(types, PyramidType.values());
        Collections.shuffle(types);

        // Create match using a random pyramid

        newBasicMatchButton.setOnAction(e -> GameManager.gameScene("ChampionScreen.fxml", types.get(0), types.get(1)));
        tutModeButton.setOnAction(event -> GameManager.changeScene("TutStart.fxml"));
        storyModeButton.setOnAction(e -> GameManager.changeScene("StoryModeScreen.fxml"));
        viewDeckButton.setOnAction(e -> GameManager.changeScene("DeckViewScreen.fxml"));
        openPacksButton.setOnAction(e -> GameManager.changeScene("CardRewardScreen.fxml"));
        achievementButton.setOnAction(e -> GameManager.changeScene("Overview.fxml"));
        settingsButton.setOnAction(e -> GameManager.changeScene("SettingsScreen.fxml"));
        playerInfoButton.setOnAction(e -> GameManager.changeScene("PlayerInfo.fxml"));
        multiplayerButton.setOnAction(e -> {
            if (GameManager.isUserLoggedIn()) {
                GameManager.changeScene("LobbyScreen.fxml");
            } else {
                JOptionPane.showMessageDialog(null, "Login to play multiplayer");
            }
        });

        // Start play time tracking
        GameManager.getStatisticsTracking().startUserPlayTimeTracking();
        GameManager.getStatisticsTracking().startPushTimer();


        for (int i = 0; i < Player.DECK_SLOTS; i++) {
            deckSelect.getItems().add(i, (i + 1) + separator + user.getDeck(i).getName());
        }
        deckSelect.getSelectionModel().select(user.getDeckSlot());
        deckSelect.valueProperty().addListener(e -> deckChange());


        chatRoomButton.setOnAction(e -> GameManager.getInstance().toggleChatScreen());

        if (GameManager.isUserLoggedIn()) {
            String username = getUsernameField();
            chatRoomButton.setDisable(false);
            playerInfoButton.setDisable(false);
            welcomeLabel.setText("Welcome" + " " + username);
        }

        Clock clock = new Clock();
        menuScreen.getChildren().add(1, new TextFlow(clock.getTimeUnderFormating()));
    }

    private void deckChange() {
        GameManager.playClip("click2.wav");
        user.setDeckSlot(deckSelect.getSelectionModel().getSelectedIndex());
    }

    private String getUsernameField() {
        GameManager username = GameManager.getInstance();
        PyramidSchemeClient schemeClient = username.getPyramidSchemeClient();

        return schemeClient.getUsername();
    }
}
