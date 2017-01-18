package uq.deco2800.pyramidscheme.controllers;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import uq.deco2800.pyramidscheme.game.GameManager;

import java.net.URL;
import java.util.ResourceBundle;

public class TutModeController implements Initializable {

    @FXML
    Button skipButton;
    @FXML
    Button nextButton1;

    //
    //skip button is used  to go back to main menu
    //next button1 is used to go to next green
    public void initialize(URL arg0, ResourceBundle arg1) {
        skipButton.setOnAction(event -> GameManager.changeScene("MenuScreen.fxml"));
        nextButton1.setOnAction(event -> GameManager.changeScene("TutScreen2.fxml"));

    }
}

	
