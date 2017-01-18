package uq.deco2800.pyramidscheme.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.StackPane;
import javafx.scene.text.TextFlow;
import uq.deco2800.pyramidscheme.clock.Clock;
import uq.deco2800.pyramidscheme.game.GameManager;
import uq.deco2800.pyramidscheme.settings.Sound;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by billy on 3/10/16.
 */
public class SettingsController implements Initializable {

    @FXML
    ToggleButton hour24Toggle;
    @FXML
    Slider sfxSlider;
    @FXML
    Slider backgroundSlider;
    @FXML
    Button backButton;
    @FXML
    CheckBox muteBox;
    @FXML
    StackPane settingsStackPane;

    GameManager gameManager;
    Clock clock;


    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        gameManager = GameManager.getInstance();
        GameManager.stopBackgroundMusic();
        hour24Toggle.setSelected(gameManager.is24Hour());
        hour24Toggle.setOnAction(e -> set24Hour());
        backButton.setOnAction(e -> returnToMenu());

        // slider setup
        sfxSlider.setValue(gameManager.getSound().getSFXVolume());
        sfxSlider.valueProperty().addListener((observable, oldValue, newValue) ->
                gameManager.setSound(new Sound(muteBox.isSelected(), sfxSlider.getValue(), backgroundSlider.getValue())));
        backgroundSlider.setValue(gameManager.getSound().getBackgroundVolume());
        backgroundSlider.valueProperty().addListener((observable, oldValue, newValue) ->
                gameManager.setSound(new Sound(muteBox.isSelected(), sfxSlider.getValue(), backgroundSlider.getValue())));
        muteBox.setSelected(gameManager.getSound().isMuted());
        muteBox.setOnAction(e -> setMute());

        clock = new Clock();
        settingsStackPane.getChildren().add(1, new TextFlow(clock.getTimeUnderFormating()));

    }

    private void setMute() {
        gameManager.setSound(new Sound(muteBox.isSelected(), sfxSlider.getValue(), backgroundSlider.getValue()));
        GameManager.playClip("click2.wav");
    }

    private void set24Hour() {
        gameManager.set24Hour(hour24Toggle.isSelected());
        if (hour24Toggle.isSelected()) {
            clock.setTimeUnderFormating("kk:mm:ss");
        } else {
            clock.setTimeUnderFormating("hh:mm:ss a");
        }
        GameManager.playClip("click.wav");
    }

    private void returnToMenu() {
        GameManager.changeScene("MenuScreen.fxml");
        GameManager.playBackgroundMusic("duck_tales.wav");
    }
}
