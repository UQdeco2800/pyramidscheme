package uq.deco2800.pyramidscheme.controllers;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.util.Duration;
import uq.deco2800.pyramidscheme.game.GameManager;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * A controller for the end game screen
 */

public class EndGameScreenController implements Initializable {

    // Declare the FX objects this class uses

    @FXML
    private Button backButton;
    @FXML
    private Button openCardPackButton;
    @FXML
    private StackPane endGame;
    @FXML
    private StackPane notification;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        backButton.setOnAction(this::handleBackButton);
        openCardPackButton.setOnAction(e -> GameManager.changeScene("CardRewardScreen.fxml"));
        endGame.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.F4) {
                GameManager.getInstance().toggleChatScreen();
            }
        });
        if (GameManager.getInstance().getBoss() > 0
                || GameManager.getInstance().getCutscene() == 'b') {
            //player victory - set button to continue
            backButton.setText("Continue");
        } else if (GameManager.getInstance().getBoss() > 0
                || GameManager.getInstance().getCutscene() == 'a') {
            //player defeat - set button to try again
            backButton.setText("Try Again");
            this.checkAchievements();
        }
    }

    /**
     * Handle the back button
     * <p>
     * If the current game is in story mode go to next cutscene if player won
     * or previous cutscene if player lost
     *
     * @param event
     */
    private void handleBackButton(ActionEvent event) {
        if (GameManager.getInstance().getBoss() > 0) {
            // boss match
            if (GameManager.getInstance().getCutscene() == 'b') {
                // player won
                int level = Integer.parseInt(GameManager.getStatisticsTracking()
                        .getUserStats().getUserLevel());
                if (level < 12) {
                    level++;
                    GameManager.getStatisticsTracking().getUserStats().addToUserLevel(1);
                    GameManager.changeScene("StoryCutsceneScreen"
                            + GameManager.getInstance().getBoss() + "b.fxml");
                } else {
                    // max level
                    GameManager.getInstance().setBoss(0);
                    GameManager.changeScene("StoryModeScreen.fxml");
                }

            } else {
                // player lost - try again
                GameManager.changeScene("StoryCutsceneScreen"
                        + GameManager.getInstance().getBoss() + "a.fxml");
            }
        } else {
            // basic match
            GameManager.changeScene("MenuScreen.fxml");
        }

    }

    private void checkAchievements() {
        //display notification for each achivement they got, with image and name
        //construct stack pane with image and text
        StackPane notificationAchieve = new StackPane();
        notificationAchieve.setStyle("-fx-background-color: FFFFFF;" + "-fx-text-fill: black;");
        notificationAchieve.setMaxHeight(100);
        notificationAchieve.setMaxWidth(200);
        Text notify = new Text("Achievement Got");
        notificationAchieve.getChildren().add(notify);
        notify.relocate(-50, -50);
        notification.getChildren().add(notificationAchieve);

        FadeTransition ft = new FadeTransition(Duration.millis(3000), notification);
        ft.setFromValue(1.0);
        ft.setToValue(0.0);
        ft.setCycleCount(1);
        ft.setDelay(new Duration(2));
        ft.play();
    }


}
