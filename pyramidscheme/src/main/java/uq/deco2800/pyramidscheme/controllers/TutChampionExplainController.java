package uq.deco2800.pyramidscheme.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import uq.deco2800.pyramidscheme.champions.Champion;
import uq.deco2800.pyramidscheme.champions.ChampionCache;
import uq.deco2800.pyramidscheme.game.GameManager;
import uq.deco2800.pyramidscheme.pyramid.PyramidType;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * A controller for the Champion Picker Screen.
 *
 * @author Josh
 */

public class TutChampionExplainController implements Initializable {

    // Declare the FX objects this class uses
    GameManager gameManager = GameManager.getInstance();

    ChampionCache champions = gameManager.getChampionCache();

    @FXML
    private Button backButton;
    @FXML
    private BorderPane championScreen;
    @FXML
    GridPane championContainer;

    // Pyramid types to display in the match
    private PyramidType userType = PyramidType.TRIANGLE;
    private PyramidType aiType = PyramidType.TRIANGLE;

    /**
     * Iterates over champions in championCache and adds a selection button for
     * each of them to the championScreen.
     */
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

        // center buttons on the screen
        championContainer.setAlignment(Pos.CENTER);

        int row = 0;
        int col = 0;
        // Procedurally place all champions
        for (int i = 0; i < champions.championCount(); ++i) {

            Champion champion = champions.getChampionByIndex(i);
            ImageView champImg = new ImageView(champion.getImage());
            ImageView glow = new ImageView(new Image("/championImages/glow.png"));

            // group of images with hover glow behind champion
            Group imgGroup = new Group(glow, champImg);

            // make glow effect invisible
            imgGroup.getChildren().get(0).setVisible(false);

            // set image dimensions
            champImg.setFitHeight(200);
            champImg.setPreserveRatio(true);
            glow.setFitHeight(200);
            glow.setPreserveRatio(true);

            // create the button and place both images on it
            Button button = new Button("", imgGroup);
            button.setId("button" + i);

            // set click to select, and hover to show glow effect
            button.setOnAction(e -> handleChampButton(champion.getName()));
            button.setOnMouseEntered(e -> handleChampButtonMouseOver(imgGroup, true));
            button.setOnMouseExited(e -> handleChampButtonMouseOver(imgGroup, false));

            // create both ability icons / tooltips
            ImageView primary = new ImageView(champion.getAbility(0).getImage());
            primary.setFitHeight(40);
            primary.setPreserveRatio(true);
            Tooltip primaryTooltip = new Tooltip(champion.getAbility(0).toString());
            primary.getProperties().put("tooltip", primaryTooltip);
            Tooltip.install(primary, primaryTooltip);

            ImageView secondary = new ImageView(champion.getAbility(1).getImage());
            secondary.setFitHeight(40);
            secondary.setPreserveRatio(true);
            Tooltip secondaryToolTip = new Tooltip(champion.getAbility(1).toString());
            secondary.getProperties().put("tooltip", secondaryToolTip);
            Tooltip.install(secondary, secondaryToolTip);

            // ability icon container
            HBox hb = new HBox();
            hb.getChildren().addAll(primary, secondary);
            hb.setAlignment(Pos.CENTER);

            // container for this champion
            VBox vb = new VBox();
            vb.getChildren().addAll(new Label(champion.getName()), button, hb);
            vb.setAlignment(Pos.CENTER);

            // add the container to the gridPane container
            championContainer.add(vb, col, row);

            // calculate the next row / col
            if (i >= 4) {
                row++;
            }
            col++;
            if (col >= 5) {
                col = 0;
            }
        }
        backButton.setOnAction(e -> GameManager.changeScene("MenuScreen.fxml"));
    }

    /**
     * A handler for each champion button. Sets the selected champion and starts
     * game.
     *
     * @param name The champions name
     */
    private void handleChampButton(String name) {
        gameManager.setSelectedChamp(name);
        GameManager.getStatisticsTracking().startChampPlayTimeTracking();
        GameManager.gameScene("BasicMatchScreen.fxml", userType, aiType);
    }

    /**
     * A handler for champion button mouseovers, turns on / off background glow.
     *
     * @param images The group of images containing background glow and champion
     *               image.
     * @param val    True to show glow, false to hide.
     */
    private void handleChampButtonMouseOver(Group images, boolean val) {
        images.getChildren().get(0).setVisible(val);
    }

    /**
     * Set the pyramid shapes for user and ai.
     *
     * @param userType The pyramid type for the user to use
     * @param aiType   The pyramid type for the ai to use.
     */
    public void setPyramidTypes(PyramidType userType, PyramidType aiType) {
        this.userType = userType;
        this.aiType = aiType;
    }
}