package uq.deco2800.pyramidscheme.controllers;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testfx.framework.junit.ApplicationTest;
import uq.deco2800.pyramidscheme.controllers.statemachine.AbilityNode;
import uq.deco2800.pyramidscheme.controllers.statemachine.Idle;
import uq.deco2800.pyramidscheme.deck.BasicDeckTest;
import uq.deco2800.pyramidscheme.game.GameManager;
import uq.deco2800.pyramidscheme.player.User;
import uq.deco2800.pyramidscheme.pyramid.PyramidType;
import uq.deco2800.pyramidscheme.settings.Sound;

import javax.inject.Inject;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeoutException;

import static org.loadui.testfx.GuiTest.find;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.api.FxToolkit.setupStage;
import static org.testfx.matcher.base.NodeMatchers.isNotNull;

/**
 * Tests for BasicMathController.
 *
 * @author Justin
 */

@RunWith(MockitoJUnitRunner.class)
public class BasicMatchControllerTest extends ApplicationTest {

    /**
     * Mock the GameManager class with Mockito
     */
    @Inject
    private GameManager gameManager;

    BasicMatchController basicMatchController;

    Logger logger = LoggerFactory.getLogger(BasicMatchControllerTest.class);

    private static Button passTurnButton;
    private static Button primaryButton;
    private static Button secondaryButton;
    private Stage stage;
    
    @Override
    public void start(Stage stage) throws Exception {
	    this.stage = stage;
	    gameManager = GameManager.getInstance();
        gameManager.setPrimaryStage(stage);
        GameManager.getStatisticsTracking().createGuestUser();
        gameManager.setUser(new User("Guest", BasicDeckTest.createDeck()));

        // getResource() looks in src/main/resources
        URL url = this.getClass().getResource("/fxml/BasicMatchScreen.fxml");
        PyramidType userType = PyramidType.TRIANGLE;
        PyramidType aiType = PyramidType.TRIANGLE;


        // Create loader from url
        FXMLLoader fxmlLoader = new FXMLLoader(url);

        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            logger.error("Failed to load resource", e);
            System.exit(1);
        }

        // Set the pyramid type in the class
        basicMatchController = fxmlLoader.getController();
        basicMatchController.setPyramidTypes(userType, aiType);

        //Display the scene
        Scene scene = new Scene(root);
        scene.setCamera(new PerspectiveCamera());
        stage.setScene(scene);
        stage.show();

    }
    
    @Before
    public void mapNodes() {
	    // Give the user and ai some duck dust
	    basicMatchController.setTestMode();

	    // Find buttons
	    passTurnButton = find("#passTurnButton");
	    primaryButton = find("#primaryAbilityButton");
        secondaryButton = find("#secondaryAbilityButton");
        GameManager.getInstance().setSound(new Sound(true, 0, 0)); // mute sound
    }

    public static void waitForRunLater() throws InterruptedException {
        Semaphore semaphore = new Semaphore(0);
        Platform.runLater(semaphore::release);
        semaphore.acquire();
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
     * Test to see if all nodes exist
     */
    @Test
    public void testAllNodesExist() {

        verifyThat("#AnchorPane", isNotNull());
        verifyThat("#userGraveyard", isNotNull());
        verifyThat("#userDeck", isNotNull());
        verifyThat("#primaryAbilityButton", isNotNull());
        verifyThat("#secondaryAbilityButton", isNotNull());
        verifyThat("#passTurnButton", isNotNull());
        verifyThat("#backButton", isNotNull());
    }

    /**
     * Check pass turn button works correctly
     */
    @Test
    public void testPassTurnButton() {
        // Test in idle
        basicMatchController.goToIdle();
        logger.debug("checking change to idle");
        Assert.assertTrue(basicMatchController.getState() instanceof Idle);

        // Fire pass turn button
        passTurnButton.fire();
        logger.debug("checking change to matchaiturn");
        Assert.assertFalse(basicMatchController.getState() instanceof Idle);

        // Fire again while state is not idle
        passTurnButton.fire();
        logger.debug("checking passTurnButton during matchaiturn");
        Assert.assertFalse(basicMatchController.getState() instanceof Idle);
    }

    @Test
    public void testHandleCanvasClick() {
        clickOn(stage.getScene(), MouseButton.PRIMARY);
    }

    @Test
    public void testHandleChampAbilityButton() {
        // Set idle
        basicMatchController.goToIdle();

        // Fire primary button
        primaryButton.fire();

        // Verify in Ability state
        Assert.assertTrue(basicMatchController.getState() instanceof AbilityNode);

        // Reset state
        basicMatchController.goToIdle();

        // Test Secondary button
        secondaryButton.fire();

        // Verify in Ability state
        Assert.assertTrue(basicMatchController.getState() instanceof AbilityNode);

        // Reset state
        basicMatchController.goToIdle();

        // Fire while in another state
        passTurnButton.fire();
        primaryButton.fire();
        Assert.assertFalse(basicMatchController.getState() instanceof AbilityNode);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHandleChampWrongButton() {
        // Set idle
        basicMatchController.goToIdle();

        //Test wrong event handler
        passTurnButton.setOnAction(primaryButton.getOnAction());
        passTurnButton.fire();
        Assert.assertTrue(basicMatchController.getState() instanceof Idle);
    }

    @Test
    public void testStartMatch() {
        basicMatchController.startMatch();
    }

    @Test
    public void testGameOverUser() throws InterruptedException {
        Scene firstScene = stage.getScene();
        Platform.runLater(() -> basicMatchController.gameOver(true));
        waitForRunLater();
        Assert.assertNotEquals(firstScene, gameManager.getStage().getScene());
    }


    @Test
    public void testGameOverAI() throws InterruptedException {
        Scene firstScene = stage.getScene();
        Platform.runLater(() -> basicMatchController.gameOver(false));
        waitForRunLater();
        Assert.assertNotEquals(firstScene, gameManager.getStage().getScene());
    }

    @AfterClass
    public static void cleanUp() throws TimeoutException {
        setupStage((stage) -> stage.close());
    }
}
