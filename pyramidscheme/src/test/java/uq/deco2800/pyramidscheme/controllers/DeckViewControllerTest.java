package uq.deco2800.pyramidscheme.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.loadui.testfx.GuiTest;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.testfx.framework.junit.ApplicationTest;
import uq.deco2800.pyramidscheme.deck.BasicDeckTest;
import uq.deco2800.pyramidscheme.game.GameManager;
import uq.deco2800.pyramidscheme.player.User;
import uq.deco2800.pyramidscheme.settings.Sound;

import java.util.concurrent.TimeoutException;

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
public class DeckViewControllerTest extends ApplicationTest {

    /**
     * Mock the GameManager class with Mockito
     */
    GameManager gameManager;
    
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

    @Override
    public void start(Stage stage) throws Exception {
	    gameManager = Mockito.mock(GameManager.class, withSettings().stubOnly());
	    GameManager.getInstance().setPrimaryStage(stage);
	    GameManager.getInstance().setUser(new User("Test", BasicDeckTest.createDeck()));
        GameManager.getInstance().setSound(new Sound(true, 0, 0)); // mute sound


        Parent root = FXMLLoader
                .load(getClass().getResource("/fxml/DeckViewScreen.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }
    
    /**
     * Double click on unobtained card.
     * (Relies on the first card in the cardList being unobtained)
     */
    @Test
    public void doubleClickOnUnobtained() {
    	doubleClickOn(GuiTest.find("#cardPane").localToScreen(150,150),
    			MouseButton.PRIMARY);
    }

    /**
     * Test to see if all buttons exist
     */
    @Test
    public void testButtonsExist() {

    	verifyThat("#backButton", isNotNull());
        verifyThat("#minionButton", isNotNull());
        verifyThat("#actionButton", isNotNull());
        verifyThat("#ownedToggle", isNotNull());
        verifyThat("#buyButton", isNotNull());
        verifyThat("#sellButton", isNotNull());
    }
    
    /**
     * Test builderTab and MarketTab
     */
    @Test
    public void testTabs() {
    	TabPane tabPane = GuiTest.find("#tabPane");
    	Assert.assertTrue("Builder tab should be selected by default", 
    			tabPane.getTabs().get(0).isSelected());
    	tabPane.getSelectionModel().select(1);
    	Assert.assertTrue("Market tab should be selected", 
    			tabPane.getTabs().get(1).isSelected());
    	tabPane.getSelectionModel().select(0);
    	Assert.assertTrue("Builder tab should be selected", 
    			tabPane.getTabs().get(0).isSelected());
    }

    /**
     * @throws TimeoutException
     */
    @AfterClass
    public static void cleanUp() throws TimeoutException {
        setupStage((stage) -> stage.close());
    }

}
