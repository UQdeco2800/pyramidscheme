package uq.deco2800.pyramidscheme.controllers;

import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uq.deco2800.pyramidscheme.game.GameManager;
import uq.deco2800.pyramidscheme.pyramid.PyramidType;
import uq.deco2800.pyramidscheme.story.CutsceneModel;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * A controller for the main menu.
 *
 * @author Justin
 * @author Bianca
 */

public class StoryCutsceneController implements Initializable {

    // Declare the FX objects this class uses
    @FXML
    StackPane storyCutsceneScreen;
    @FXML
    StackPane titleScreen;
    @FXML
    StackPane endtitleScreen;
    @FXML
    StackPane storyCutsceneScreen1;
    @FXML
    StackPane storyCutsceneScreen2;
    @FXML
    StackPane storyCutsceneScreen3;
    @FXML
    StackPane storyCutsceneScreen4;
    @FXML
    StackPane storyCutsceneScreen5;
    @FXML
    StackPane storyCutsceneScreen6;
    @FXML
    StackPane storyCutsceneScreen7;
    @FXML
    StackPane storyCutsceneScreen8;
    @FXML
    StackPane storyCutsceneScreen9;
    @FXML
    StackPane storyCutsceneScreen10;
    @FXML
    HBox textboard;
    @FXML
    HBox textbox;
    @FXML
    StackPane portal;
    @FXML
    ImageView duck;
    @FXML
    BorderPane background;
    @FXML
    Text dialogue;
    @FXML
    ImageView title;
    @FXML
    ImageView endtitle;
    @FXML
    Button nextButton;

    CutsceneModel cutscene;
    int playerLevel = Integer.parseInt(
            GameManager.getStatisticsTracking().getUserStats().getUserLevel());
    int numDialogues;
    int currentDialogue = 0;
    boolean playing = true;
    final MeshView pyramid = new MeshView();
    private Timeline animation;
    private Timeline animation2;

    private static Logger logger = LoggerFactory
            .getLogger(StoryCutsceneController.class);

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        // Add event handler for skipping cutscene
        storyCutsceneScreen.setOnMouseClicked(this::handleMouseClicks);
        nextButton.setOnAction(this::handleSpacebar);
        // press f4 to open chat room

        storyCutsceneScreen.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.ESCAPE && playing) {
                GameManager.getInstance().setBoss(0);
                GameManager.getInstance().setCutscene('a');
                GameManager.changeScene("StoryModeScreen.fxml");
            }
        });

        try {
            // load the script
            cutscene = new CutsceneModel(
                    "" + GameManager.getInstance().getBoss()
                            + GameManager.getInstance().getCutscene());
        } catch (IOException e) {
            logger.error("Failed to load cutscene", e);
            System.exit(1);
        }
        numDialogues = cutscene.textInScene();
        // fade in
        FadeTransition ft = new FadeTransition(Duration.millis(2000), title);
        FadeTransition ft2 = new FadeTransition(Duration.millis(1000), title);
        ft2.setFromValue(1.0);
        ft2.setToValue(0);

        ft.setOnFinished(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                ft2.play();
            }
        });
        ft2.setOnFinished(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                titleScreen.setVisible(false);
            }
        });
        ft.play();

        // add portal
        if (GameManager.getInstance().getCutscene() != 'a') {
            portal.getChildren().addAll(createContent());
            play();
        }
    }

    private void handleSpacebar(ActionEvent event) {
        playDialogue();
    }

    private void playDialogue() {
        // next scene
        currentDialogue++;
        if (currentDialogue <= numDialogues) {
            // get rid of the title screen if animation is still going
            titleScreen.setVisible(false);

            // get the next line to display
            String dialogueText = cutscene.getLine();
            final Animation animation = new Transition() {
                {
                    setCycleDuration(Duration.millis(1000));
                }

                protected void interpolate(double frac) {
                    int length = dialogueText.length();
                    int n = Math.round(length * (float) frac);
                    dialogue.setText(dialogueText.substring(0, n));
                }

            };

            animation.play();
            dialogue.setText(cutscene.getLine());

            // get the next image to display
            switch (cutscene.nextImage()) {
                case "1":
                    // do nothing
                    break;
                case "2":
                    // next scene
                    storyCutsceneScreen1.setVisible(false);
                    break;
                case "3":
                    // next scene
                    storyCutsceneScreen2.setVisible(false);
                    break;
                case "4":
                    // next scene
                    storyCutsceneScreen3.setVisible(false);
                    break;
                case "5":
                    // next scene
                    storyCutsceneScreen4.setVisible(false);
                    break;
                case "6":
                    // next scene
                    storyCutsceneScreen5.setVisible(false);
                    break;
                case "7":
                    // next scene
                    storyCutsceneScreen6.setVisible(false);
                    break;
                case "8":
                    // next scene
                    storyCutsceneScreen7.setVisible(false);
                    break;
                case "9":
                    // next scene
                    storyCutsceneScreen8.setVisible(false);
                    break;
                case "10":
                    // next scene
                    storyCutsceneScreen9.setVisible(false);
                    break;
                case "0":
                    // show portal
                    storyCutsceneScreen1.setVisible(false);
                    storyCutsceneScreen2.setVisible(false);
                    storyCutsceneScreen3.setVisible(false);
                    storyCutsceneScreen4.setVisible(false);
                    storyCutsceneScreen5.setVisible(false);
                    storyCutsceneScreen6.setVisible(false);
                    storyCutsceneScreen7.setVisible(false);
                    storyCutsceneScreen8.setVisible(false);
                    storyCutsceneScreen9.setVisible(false);
                    storyCutsceneScreen10.setVisible(false);
                    textboard.setVisible(false);
                    textbox.setVisible(false);
                    endtitleScreen.setVisible(true);
                    FadeTransition ft = new FadeTransition(Duration.millis(500),
                            endtitle);
                    ft.setFromValue(1.0);
                    ft.setToValue(0.0);
                    ft.play();

                    break;
                default:
                    // do nothing
                    break;

            }

        } else if (playing) {
            // fade out to boss fight or next cutscene
            // make sure animation only plays once
            playing = false;

            endtitleScreen.setVisible(true);
            FadeTransition ft = new FadeTransition(Duration.millis(2000),
                    endtitle);
            ft.setFromValue(0.0);
            ft.setToValue(1.0);
            FadeTransition ft2 = new FadeTransition(Duration.millis(1000),
                    endtitle);
            ft2.setOnFinished(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent event) {
                    if (GameManager.getInstance().getCutscene() == 'a') {
                        // switch to boss fight
                        GameManager.gameScene("BasicMatchScreen.fxml",
                                PyramidType.TRIANGLE, PyramidType.TRIANGLE);
                    } else {
                        // switch to next cut-scene
                        if (GameManager.getInstance().getBoss() == 4 || GameManager.getInstance()
                                .getBoss() == 12) {
                            // play additional cut-scene on level 4 and 12
                            if (GameManager.getInstance()
                                    .getCutscene() == 'b') {
                                GameManager.getInstance().setCutscene('c');
                                GameManager.changeScene("StoryCutsceneScreen"
                                        + GameManager.getInstance().getBoss()
                                        + "c.fxml");

                            } else if (GameManager.getInstance()
                                    .getBoss() == 12) {
                                // game over
                                GameManager.getInstance().setBoss(0);
                                GameManager.getInstance().setCutscene('a');
                                GameManager.changeScene("BossSelectionScreen.fxml");
                            } else if (GameManager.getInstance()
                                    .getCutscene() == 'c') {
                                GameManager.getInstance().setCutscene('d');
                                GameManager.changeScene(
                                        "StoryCutsceneScreen4d.fxml");

                            } else {
                                // scene d just played so start next chapter
                                GameManager.getInstance().setCutscene('a');
                                GameManager.getInstance().setBoss(
                                        GameManager.getInstance().getBoss()
                                                + 1);
                                GameManager.changeScene("StoryCutsceneScreen"
                                        + GameManager.getInstance().getBoss()
                                        + "a.fxml");
                            }
                        } else if (GameManager.getInstance().getBoss() < 12) {
                            GameManager.getInstance().setCutscene('a');
                            GameManager.getInstance().setBoss(
                                    GameManager.getInstance().getBoss() + 1);
                            GameManager.changeScene("StoryCutsceneScreen"
                                    + GameManager.getInstance().getBoss()
                                    + "a.fxml");
                        } else {
                            // no more bosses
                            GameManager.getInstance().setCutscene('a');
                            GameManager.getInstance().setBoss(0);
                            GameManager.changeScene("StoryModeScreen.fxml");

                        }
                    }

                }
            });
            ft.setOnFinished(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent event) {
                    ft2.play();
                }
            });
            ft.play();

        }

    }

    private void handleMouseClicks(MouseEvent event) {
        playDialogue();
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
        pyramidMesh.getPoints().addAll(0, 0, 0, // Point 0 - Top
                -s / 2, s / 2, h, -s / 2, -s / 2, h, // Point 2 - Left
                s / 2, -s / 2, h, // Point 3 - Back
                s / 2, s / 2, h // Point 4 - Right
        );
        pyramidMesh.getFaces().addAll(0, 0, 2, 0, 1, 0, // Front left face
                0, 0, 1, 0, 4, 0, // Front right face
                0, 0, 4, 0, 3, 0, // Back right face
                0, 0, 3, 0, 2, 0, // Back left face
                4, 0, 1, 0, 3, 0, // Bottom rear face
                4, 0, 3, 0, 3, 0 // Bottom front face
        );
        int[] smoothingGroups = {10, 1110, 10, 112, 1, 500};
        pyramidMesh.getFaceSmoothingGroups().addAll(smoothingGroups);
        pyramid.setMesh(pyramidMesh);
        pyramid.setDrawMode(DrawMode.FILL);

        final PhongMaterial pyrMaterial = new PhongMaterial();

        pyramid.setMaterial(pyrMaterial);
        pyramid.setTranslateX(0);
        pyramid.setTranslateY(0);
        pyramid.setTranslateZ(0);

        pyramid.setRotationAxis(Rotate.Z_AXIS);
        // end of pyramid

        // animate pyramid

        PerspectiveCamera camera = new PerspectiveCamera(true);

        camera.setNearClip(0.1);
        camera.setFarClip(10000.0);
        camera.setTranslateZ(-270);
        camera.setRotate(180);

        PointLight pl = new PointLight();
        pl.setTranslateX(0);
        pl.setTranslateY(0);
        pl.setTranslateZ(-400);
        pl.setColor(Color.PURPLE);
        Group root = new Group();
        root.getChildren().addAll(pyramid, pl);
        SubScene subScene = new SubScene(root, 900, 600, true,
                SceneAntialiasing.BALANCED);
        subScene.setCamera(camera);
        animation = new Timeline();
        animation.getKeyFrames()
                .addAll(new KeyFrame(Duration.ZERO,
                                new KeyValue(pyramid.rotateProperty(), 0d)),
                        new KeyFrame(Duration.seconds(2),
                                new KeyValue(pyramid.rotateProperty(), 360d)));
        animation.setCycleCount(Timeline.INDEFINITE);

        return new Group(subScene);
    }

    /**
     * Starts rotating the pyramid
     */
    public void play() {
        animation.play();
        animation2 = new Timeline();
        animation2.getKeyFrames()
                .addAll(new KeyFrame(Duration.ZERO,
                                new KeyValue(duck.rotateProperty(), 0d)),
                        new KeyFrame(Duration.seconds(5),
                                new KeyValue(duck.rotateProperty(), 360d)));
        animation2.setCycleCount(Timeline.INDEFINITE);
        animation2.play();
    }

}
