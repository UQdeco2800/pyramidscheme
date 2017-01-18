package uq.deco2800.pyramidscheme.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.testfx.framework.junit.ApplicationTest;
import uq.deco2800.pyramidscheme.deck.BasicDeckTest;
import uq.deco2800.pyramidscheme.game.GameManager;
import uq.deco2800.pyramidscheme.player.User;
import uq.deco2800.pyramidscheme.settings.Sound;
import uq.deco2800.singularity.common.representations.pyramidscheme.UserStatistics;

import java.util.concurrent.TimeoutException;

import static org.junit.Assert.assertEquals;
import static org.loadui.testfx.GuiTest.find;
import static org.mockito.Mockito.withSettings;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.api.FxToolkit.setupStage;
import static org.testfx.matcher.base.NodeMatchers.isNotNull;

/**
 * Tests for DeckViewController.
 *
 * @author Justin
 */

@RunWith(MockitoJUnitRunner.class)
public class StoryCutsceneControllerTest extends ApplicationTest {

    /**
     * Mock the GameManager class with Mockito
     */
    GameManager gameManager;
    private static Button nextButton;
    private static StackPane storyCutsceneScreen1;
    private static Text dialogue;
   

    @Override
    public void start(Stage stage) throws Exception {
        gameManager = Mockito.mock(GameManager.class, withSettings().stubOnly());
        GameManager.getInstance().setPrimaryStage(stage);
        GameManager.getInstance().setUser(new User("Test", BasicDeckTest.createDeck()));
        GameManager.getInstance().setBoss(1);
        GameManager.getInstance().setSound(new Sound(true, 0, 0)); // mute sound

        UserStatistics userStats = new UserStatistics();
        userStats.setUserLevel("1");
        GameManager.getStatisticsTracking().setUserStats(userStats);
        Parent root = FXMLLoader
                .load(getClass().getResource("/fxml/StoryCutsceneScreen1a.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }
    @BeforeClass
    public static void setupHeadless() {
        // Set to headless testing
        System.setProperty("testfx.robot", "glass");
        System.setProperty("testfx.headless", "true");
        System.setProperty("java.awt.headless", "true");
        // JavaFX Rendering options which allow headless testing
        System.setProperty("prism.order", "sw");
        System.setProperty("prism.text", "t2k");
    }
    
    @Before
    public void mapNodes() {

        nextButton = find("#nextButton");
        storyCutsceneScreen1 = find("#storyCutsceneScreen1");
        dialogue = find("dialogue");
    }
    /**
     * Test to see if all buttons exist
     */
    @Test
    public void testButtonsExist() {

        verifyThat("#nextButton", isNotNull());
        
    }
    
    /**
     * Check next button fires correctly
     */
    @Test
    public void testnextButton() {
        nextButton.fire();        
    }
    /**
     * @throws TimeoutException
     */
    @AfterClass
    public static void cleanUp() throws TimeoutException {
        setupStage((stage) -> stage.close());
    }

}
