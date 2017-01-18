package uq.deco2800.pyramidscheme.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import uq.deco2800.pyramidscheme.game.GameManager;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * An abstract class controllers for match modes should extend. Includes the
 * basic methods required by matches.
 *
 * @author Millie
 */

public abstract class MatchController implements Initializable {
    @FXML
    private StackPane matchPane;
    @FXML
    private Button backButton;
    @FXML
    private Button passTurnButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        backButton.setOnAction(e -> returnToMenu());
        passTurnButton.setOnAction(this::handlePassTurn);

        startMatch();
    }

    abstract void startMatch();

    /**
     * EVENT HANDLERS
     **/

    protected void returnToMenu() {
        // reset boss variable
        if (GameManager.getInstance().getBoss() > 0) {
            GameManager.getInstance().setBoss(0);
            GameManager.changeScene("StoryModeScreen.fxml");
        } else {
            GameManager.getInstance().setBoss(0);
            GameManager.changeScene("MenuScreen.fxml");
        }
    }

    protected void goToEndGameScreenVictory() {
        GameManager.changeScene("EndGameScreen.fxml");
    }

    protected void goToEndGameScreenDefeat() {
        GameManager.changeScene("EndGameScreenDefeat.fxml");
    }

    abstract void handlePassTurn(ActionEvent event);
}
