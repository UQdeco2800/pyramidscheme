package uq.deco2800.pyramidscheme.controllers;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.text.TextFlow;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import uq.deco2800.pyramidscheme.clock.Clock;
import uq.deco2800.pyramidscheme.game.GameManager;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * A controller for the main menu.
 *
 * @author Justin
 */

public class StoryModeController implements Initializable {

    final MeshView pyramid = new MeshView();
    // Declare the FX objects this class uses
    // Buttons used
    @FXML
    Button continueStoryButton;
    @FXML
    Button newStoryButton;
    @FXML
    Button backButton;
    @FXML
    Button newCampaignButton;
    @FXML
    Button backButton1;
    @FXML
    Button bossSelectionButton;
    @FXML
    Button backButton2;
    // Containers used
    @FXML
    StackPane storyModeScreen1;
    @FXML
    StackPane storyModeScreen;
    @FXML
    HBox popupforeground;
    @FXML
    HBox popupbackground;
    @FXML
    HBox popupforeground2;
    @FXML
    StackPane black;

    // Variables
    private Timeline animation;
    private boolean locked = true;
    private FadeTransition fadeOut;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        // Add event handler buttons
        continueStoryButton.setOnAction(this::continueStoryButton);
        newStoryButton.setOnAction(this::newStoryButton);
        backButton.setOnAction(this::handleBackButton);
        newCampaignButton.setOnAction(this::handleNewCampaignButton);
        backButton1.setOnAction(this::handleBackButton1);
        bossSelectionButton.setOnAction(this::handleBossSelectionButton);
        backButton2.setOnAction(this::handleBackButton2);

        // add pyramid
        storyModeScreen1.getChildren().addAll(createContent());
        play();
        // unlock boss select with G
        storyModeScreen.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke) {
                if (KeyCode.G.equals(ke.getCode())) {
                    locked = false;
                    bossSelectionButton.setId("bossSelectionButton1");
                }
            }
        });
        //check if already unlocked
        if (GameManager.getStatisticsTracking().getUserStats().getUserLevel() == "13") {
            locked = false;
            bossSelectionButton.setId("bossSelectionButton1");
        }
        // fade in
        FadeTransition fadeIn = new FadeTransition(Duration.millis(800),
                black);
        fadeIn.setFromValue(1.0);
        fadeIn.setToValue(0);

        fadeIn.setOnFinished(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                black.setVisible(false);
            }
        });
        fadeIn.play();

        fadeOut = new FadeTransition(Duration.millis(800), black);
        fadeOut.setFromValue(0);
        fadeOut.setToValue(1.0);


        Clock clock = new Clock();
        storyModeScreen1.getChildren().add(1, new TextFlow(clock.getTimeUnderFormating())); //adding time to splashscreen stackpane

    }

    /**
     * Switches to the boss selection screen if unlocked Otherwise brings up
     * pop-up2 explaining how to unlock
     *
     * @param event
     */
    private void handleBossSelectionButton(ActionEvent event) {

        if (!locked) {
            // fade out
            black.setVisible(true);
            fadeOut.setOnFinished(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent event) {

                    GameManager.changeScene("BossSelectionScreen.fxml");

                }
            });
            fadeOut.play();

        } else {
            popupforeground2.setVisible(true);
            popupbackground.setVisible(true);
        }
    }

    /**
     * Closes pop-up2
     *
     * @param event
     */
    private void handleBackButton2(ActionEvent event) {
        popupforeground2.setVisible(false);
        popupbackground.setVisible(false);
    }

    /**
     * Closes pop-up1
     *
     * @param event
     */
    private void handleBackButton1(ActionEvent event) {
        popupforeground.setVisible(false);
        popupbackground.setVisible(false);
    }

    /**
     * Opens pop-up1 confirming new campaign
     *
     * @param event
     */
    private void handleNewCampaignButton(ActionEvent event) {
        popupforeground.setVisible(true);
        popupbackground.setVisible(true);
    }

    /**
     * Continues the story based on the players level
     *
     * @param event
     */
    private void continueStoryButton(ActionEvent event) {
        int level = Integer.parseInt(GameManager.getStatisticsTracking().getUserStats().getUserLevel());

        // fade out
        black.setVisible(true);
        fadeOut.setOnFinished(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                if (level < 12 && level > 0) {

                    GameManager.getInstance().setBoss(level);
                    GameManager.getInstance().setCutscene('a');
                    GameManager.changeScene("StoryCutsceneScreen" + level + "a.fxml");
                } else {
                    GameManager.getInstance().setBoss(12);
                    GameManager.getInstance().setCutscene('a');
                    GameManager.changeScene("StoryCutsceneScreen" + 12 + "a.fxml");
                }
            }
        });
        fadeOut.play();
    }

    /**
     * Sets the player level to one and starts the first cut-scene
     *
     * @param event
     */
    private void newStoryButton(ActionEvent event) {
        GameManager.getStatisticsTracking().getUserStats().setUserLevel("1");
        GameManager.getInstance().setBoss(1);
        black.setVisible(true);
        fadeOut.setOnFinished(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                GameManager.getInstance().setBoss(1);
                GameManager.getInstance().setCutscene('a');
                GameManager.changeScene("StoryCutsceneScreen" + 1 + "a.fxml");
            }
        });
        fadeOut.play();

    }

    /**
     * Build the 3D pyramid
     *
     * @return a sub-scene with the 3d pyramid
     */
    public Parent createContent() {

        // add pyramid

        TriangleMesh pyramidMesh = new TriangleMesh();
        pyramidMesh.getTexCoords().addAll(0, 0);
        float h = 150; // Height
        float s = 300; // Side
        pyramidMesh.getPoints().addAll(0, h, 0, // Point 0 - Top
                -s / 2, 0, 0, // Point 1 - Front
                0, 0, -s / 2, // Point 2 - Left
                0, 0, s / 2, // Point 3 - Back
                s / 2, 0, 0 // Point 4 - Right
        );
        pyramidMesh.getFaces().addAll(0, 0, 2, 0, 1, 0, // Front left face
                0, 0, 1, 0, 3, 0, // Front right face
                0, 0, 3, 0, 4, 0, // Back right face
                0, 0, 4, 0, 2, 0, // Back left face
                4, 0, 1, 0, 2, 0, // Bottom rear face
                4, 0, 3, 0, 1, 0 // Bottom front face
        );
        int[] smoothingGroups = {0, 1110, 0, 112, 1, 0 // not sure what to put
                // here
        };
        pyramidMesh.getFaceSmoothingGroups().addAll(smoothingGroups);
        pyramid.setMesh(pyramidMesh);
        pyramid.setDrawMode(DrawMode.FILL);

        final PhongMaterial pyrMaterial = new PhongMaterial();

        Image img = new Image(
                getClass().getResourceAsStream("/storyModeImages/texture.png"));

        pyrMaterial.setDiffuseMap(img);
        pyrMaterial.setSelfIlluminationMap(img);

        pyramid.setMaterial(pyrMaterial);
        pyramid.setTranslateX(0);
        pyramid.setTranslateY(-80);
        pyramid.setTranslateZ(0);
        pyramid.setRotationAxis(Rotate.Y_AXIS);
        // end of pyramid

        // animate pyramid
        animation = new Timeline();
        animation.getKeyFrames()
                .addAll(new KeyFrame(Duration.ZERO,
                                new KeyValue(pyramid.rotateProperty(), 0d)),
                        new KeyFrame(Duration.seconds(60),
                                new KeyValue(pyramid.rotateProperty(), 360d)));
        animation.setCycleCount(Timeline.INDEFINITE);

        PerspectiveCamera camera = new PerspectiveCamera(true);

        camera.setNearClip(0.1);
        camera.setFarClip(10000.0);
        camera.setTranslateZ(-270);
        camera.setRotate(180);

        PointLight pl = new PointLight();
        pl.setTranslateX(-35);
        pl.setTranslateY(180);
        pl.setTranslateZ(0);
        pl.setColor(Color.BEIGE);

        Group root = new Group();
        root.getChildren().addAll(pyramid, pl);
        SubScene subScene = new SubScene(root, 900, 600, true,
                SceneAntialiasing.BALANCED);
        subScene.setCamera(camera);

        return new Group(subScene);
    }

    /**
     * Returns to main menu
     *
     * @param event
     */
    private void handleBackButton(ActionEvent event) {
        black.setVisible(true);
        fadeOut.setOnFinished(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                GameManager.changeScene("MenuScreen.fxml");
            }
        });
        fadeOut.play();
    }

    /**
     * Starts rotating the pyramid
     */
    public void play() {
        animation.play();
    }
}
