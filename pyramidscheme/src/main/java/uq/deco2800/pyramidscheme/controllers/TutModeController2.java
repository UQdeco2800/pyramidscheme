package uq.deco2800.pyramidscheme.controllers;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import uq.deco2800.pyramidscheme.game.GameManager;

import java.net.URL;
import java.util.ResourceBundle;

public class TutModeController2 implements Initializable {

    @FXML
    Button skipButton;
    @FXML
    Button nextButton2;


    //	@skip button -> go back to mainmenu
    //next button 2 is set to go to screen 3
    public void initialize(URL arg0, ResourceBundle arg1) {
        skipButton.setOnAction(event -> GameManager.changeScene("MenuScreen.fxml"));
        nextButton2.setOnAction(event -> GameManager.changeScene("TutScreen3.fxml"));
    }


}

	
