package uq.deco2800.pyramidscheme.controllers;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import uq.deco2800.pyramidscheme.game.GameManager;

import java.net.URL;
import java.util.ResourceBundle;

public class TutModeController3 implements Initializable {

    @FXML
    Button skipButton;
    @FXML
    Button nextButton3;


    public void initialize(URL arg0, ResourceBundle arg1) {
        skipButton.setOnAction(event -> GameManager.changeScene("MenuScreen.fxml"));
        nextButton3.setOnAction(event -> GameManager.changeScene("TutScreen4.fxml"));
    }

}

	
