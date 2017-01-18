package uq.deco2800.pyramidscheme.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.testfx.framework.junit.ApplicationTest;
import uq.deco2800.pyramidscheme.game.GameManager;
import uq.deco2800.pyramidscheme.settings.Sound;

import java.util.concurrent.TimeoutException;

import static org.junit.Assert.assertEquals;
import static org.loadui.testfx.GuiTest.find;
import static org.mockito.Mockito.withSettings;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.api.FxToolkit.setupStage;
import static org.testfx.matcher.base.NodeMatchers.isNotNull;

/**
 * Tests for story mode menu ui.
 *
 * @author Justin
 */

@RunWith(MockitoJUnitRunner.class)
public class StoryModeControllerTest extends ApplicationTest {

    /**
     * Mock the GameManager class with Mockito
     */
    GameManager gameManager;

    private static Button newCampaignButton;
    private static Button backButton1;
    private static HBox popupbackground;
    private static HBox popupforeground;

    @Override
    public void start(Stage stage) throws Exception {
	    gameManager = Mockito.mock(GameManager.class, withSettings().stubOnly());
	    GameManager.getInstance().setPrimaryStage(stage);
        GameManager.getInstance().setSound(new Sound(true, 0, 0)); // mute sound


        Parent root = FXMLLoader
                .load(getClass().getResource("/fxml/StoryModeScreen.fxml"));
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

        newCampaignButton = find("#newCampaignButton");
        backButton1 = find("#backButton1");

        popupbackground = find("#popupbackground");
        popupforeground = find("#popupforeground");

    }

    /**
     * Test to see if all buttons exist
     */
    @Test
    public void testButtonsExist() {

        verifyThat("#newCampaignButton", isNotNull());
        verifyThat("#bossSelectionButton", isNotNull());
        verifyThat("#backButton", isNotNull());

    }

    /**
     * Check popup appears correctly
     */
    @Test
    public void testnewCampaignButton() {
        newCampaignButton.fire();
        assertEquals(popupbackground.isVisible(), true);
        assertEquals(popupforeground.isVisible(), true);
    }

    /**
     * Check popup closes correctly
     */
    @Test
    public void testbackButton1() {
        newCampaignButton.fire();
        backButton1.fire();
        assertEquals(popupbackground.isVisible(), false);
        assertEquals(popupforeground.isVisible(), false);
    }

    @AfterClass
    public static void cleanUp() throws TimeoutException {
        setupStage((stage) -> stage.close());
    }

}
