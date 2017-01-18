package uq.deco2800.pyramidscheme.controllers;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import uq.deco2800.pyramidscheme.game.GameManager;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * A controller for the main menu.
 *
 * @author Justin
 */

public class BossSelectController implements Initializable {

    // Declare the FX objects this class uses
    //Buttons used
    @FXML
    Button boss1Button;
    @FXML
    Button boss2Button;
    @FXML
    Button boss3Button;
    @FXML
    Button boss4Button;
    @FXML
    Button boss5Button;
    @FXML
    Button boss6Button;
    @FXML
    Button boss7Button;
    @FXML
    Button boss8Button;
    @FXML
    Button boss9Button;
    @FXML
    Button boss10Button;
    @FXML
    Button boss11Button;
    @FXML
    Button boss12Button;
    @FXML
    Button backButton;


    // Containers used
    @FXML
    StackPane bossSelectScreen1;

    //Variables
    private Timeline animation;
    final MeshView pyramid = new MeshView();

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        // Add event handler buttons
        boss1Button.setOnAction(this::handleBoss1Button);
        boss2Button.setOnAction(this::handleBoss2Button);
        boss3Button.setOnAction(this::handleBoss3Button);
        boss4Button.setOnAction(this::handleBoss4Button);
        boss5Button.setOnAction(this::handleBoss5Button);
        boss6Button.setOnAction(this::handleBoss6Button);
        boss7Button.setOnAction(this::handleBoss7Button);
        boss8Button.setOnAction(this::handleBoss8Button);
        boss9Button.setOnAction(this::handleBoss9Button);
        boss10Button.setOnAction(this::handleBoss10Button);
        boss11Button.setOnAction(this::handleBoss11Button);
        boss12Button.setOnAction(this::handleBoss12Button);
        backButton.setOnAction(this::handleBackButton);

        // add pyramid
        bossSelectScreen1.getChildren().addAll(createContent());

        play();
    }

    /**
     * Starts the cut-scene for the first boss
     *
     * @param event
     */
    private void handleBoss1Button(ActionEvent event) {
        GameManager.getInstance().setBoss(1);
        GameManager.getInstance().setCutscene('a');
        GameManager.changeScene("StoryCutsceneScreen1a.fxml");
    }

    /**
     * Starts the cut-scene for the second boss
     *
     * @param event
     */
    private void handleBoss2Button(ActionEvent event) {
        GameManager.getInstance().setBoss(2);
        GameManager.getInstance().setCutscene('a');
        GameManager.changeScene("StoryCutsceneScreen2a.fxml");
    }

    /**
     * Starts the cut-scene for the third boss
     *
     * @param event
     */
    private void handleBoss3Button(ActionEvent event) {
        GameManager.getInstance().setBoss(3);
        GameManager.getInstance().setCutscene('a');
        GameManager.changeScene("StoryCutsceneScreen3a.fxml");
    }

    /**
     * Starts the cut-scene for the forth boss
     *
     * @param event
     */
    private void handleBoss4Button(ActionEvent event) {
        GameManager.getInstance().setBoss(4);
        GameManager.getInstance().setCutscene('a');
        GameManager.changeScene("StoryCutsceneScreen4a.fxml");
    }

    /**
     * Starts the cut-scene for the fifth boss
     *
     * @param event
     */
    private void handleBoss5Button(ActionEvent event) {
        GameManager.getInstance().setBoss(5);
        GameManager.getInstance().setCutscene('a');
        GameManager.changeScene("StoryCutsceneScreen5a.fxml");
    }

    /**
     * Starts the cut-scene for the sixth boss
     *
     * @param event
     */
    private void handleBoss6Button(ActionEvent event) {
        GameManager.getInstance().setBoss(6);
        GameManager.getInstance().setCutscene('a');
        GameManager.changeScene("StoryCutsceneScreen6a.fxml");
    }

    /**
     * Starts the cut-scene for the seventh boss
     *
     * @param event
     */
    private void handleBoss7Button(ActionEvent event) {
        GameManager.getInstance().setBoss(7);
        GameManager.getInstance().setCutscene('a');
        GameManager.changeScene("StoryCutsceneScreen7a.fxml");
    }

    /**
     * Starts the cut-scene for the eight boss
     *
     * @param event
     */
    private void handleBoss8Button(ActionEvent event) {
        GameManager.getInstance().setBoss(8);
        GameManager.getInstance().setCutscene('a');
        GameManager.changeScene("StoryCutsceneScreen8a.fxml");
    }

    /**
     * Starts the cut-scene for the ninth boss
     *
     * @param event
     */
    private void handleBoss9Button(ActionEvent event) {
        GameManager.getInstance().setBoss(9);
        GameManager.getInstance().setCutscene('a');
        GameManager.changeScene("StoryCutsceneScreen9a.fxml");
    }

    /**
     * Starts the cut-scene for the tenth boss
     *
     * @param event
     */
    private void handleBoss10Button(ActionEvent event) {
        GameManager.getInstance().setBoss(10);
        GameManager.getInstance().setCutscene('a');
        GameManager.changeScene("StoryCutsceneScreen10a.fxml");
    }

    /**
     * Starts the cut-scene for the eleventh boss
     *
     * @param event
     */
    private void handleBoss11Button(ActionEvent event) {
        GameManager.getInstance().setBoss(11);
        GameManager.getInstance().setCutscene('a');
        GameManager.changeScene("StoryCutsceneScreen11a.fxml");
    }

    /**
     * Starts the cut-scene for the twelfth boss
     *
     * @param event
     */
    private void handleBoss12Button(ActionEvent event) {
        GameManager.getInstance().setBoss(12);
        GameManager.getInstance().setCutscene('a');
        GameManager.changeScene("StoryCutsceneScreen12a.fxml");
    }

    /**
     * Build the 3D pyramid
     *
     * @return the subscene with the 3d pyramid
     */
    private Parent createContent() {

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
        int[] smoothingGroups = {0, 1110, 0, 112, 1, 0};
        pyramidMesh.getFaceSmoothingGroups().addAll(smoothingGroups);
        pyramid.setMesh(pyramidMesh);
        pyramid.setDrawMode(DrawMode.FILL);

        final PhongMaterial pyrMaterial = new PhongMaterial();

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
        pl.setColor(Color.AQUA);

        Group root = new Group();
        root.getChildren().addAll(pyramid, pl);
        SubScene subScene = new SubScene(root, 900, 600, true,
                SceneAntialiasing.BALANCED);
        subScene.setCamera(camera);

        return new Group(subScene);
    }

    /**
     * Returns to story menu
     *
     * @param event
     */
    private void handleBackButton(ActionEvent event) {
        GameManager.changeScene("StoryModeScreen.fxml");
    }

    /**
     * Starts rotating the pyramid
     */
    public void play() {
        animation.play();
    }
}
