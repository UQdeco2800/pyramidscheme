package uq.deco2800.pyramidscheme.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.testfx.framework.junit.ApplicationTest;
import uq.deco2800.pyramidscheme.game.GameManager;
import uq.deco2800.pyramidscheme.settings.Sound;

import java.util.concurrent.TimeoutException;

import static org.mockito.Mockito.withSettings;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.api.FxToolkit.setupStage;
import static org.testfx.matcher.base.NodeMatchers.isNotNull;

/**
 * Tests for boss select menu ui.
 *
 * @author Justin
 */

@RunWith(MockitoJUnitRunner.class)
public class BossSelectControllerTest extends ApplicationTest {

    /**
     * Mock the GameManager class with Mockito
     */
    GameManager gameManager;

    @Override
    public void start(Stage stage) throws Exception {
	    gameManager = Mockito.mock(GameManager.class, withSettings().stubOnly());
	    GameManager.getInstance().setPrimaryStage(stage);
        GameManager.getInstance().setSound(new Sound(true, 0, 0)); // mute sound

        Parent root = FXMLLoader
                .load(getClass().getResource("/fxml/BossSelectionScreen.fxml"));
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

    /**
     * Test to see if all buttons exist
     */
    @Test
    public void testButtonsExist() {

        verifyThat("#boss1Button", isNotNull());
        verifyThat("#boss2Button", isNotNull());
        verifyThat("#backButton", isNotNull());

    }

    @AfterClass
    public static void cleanUp() throws TimeoutException {
        setupStage((stage) -> stage.close());
    }

}
