package uq.deco2800.pyramidscheme.controllers;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit.ApplicationTest;

import uq.deco2800.pyramidscheme.deck.BasicDeckTest;
import uq.deco2800.pyramidscheme.game.GameManager;
import uq.deco2800.pyramidscheme.player.User;
import uq.deco2800.pyramidscheme.settings.Sound;
import uq.deco2800.singularity.clients.pyramidscheme.PyramidSchemeClient;
import uq.deco2800.singularity.clients.realtime.messaging.MessagingClient;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeoutException;

import static org.loadui.testfx.GuiTest.find;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.api.FxToolkit.setupStage;
import static org.testfx.matcher.base.NodeMatchers.isNotNull;

/**
 * Tests for RewardScreenController.
 *
 */

@RunWith(MockitoJUnitRunner.class)
public class RewardScreenControllerTest extends ApplicationTest {

    /**
     * Mock the GameManager class with Mockito
     */
    GameManager gameManager;
    private Button openButton;
    private Button buyButton;
    private Button backButton;
    private StackPane displayPane;
    private Stage stage;

    PyramidSchemeClient pyramidClient;
    MessagingClient messagingClient;

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        gameManager = GameManager.getInstance();
        gameManager.setPrimaryStage(stage);
        User user = new User("Test", BasicDeckTest.createDeck());
        gameManager.setUser(user);
        GameManager.getStatisticsTracking().createGuestUser();
        GameManager.getInstance().getUser().setCurrency(1000);
        GameManager.getInstance().getUser().setCardPacks(1);
        GameManager.getInstance().setSound(new Sound(true, 0, 0)); // mute sound


        Parent root = FXMLLoader
                .load(getClass().getResource("/fxml/CardRewardScreen.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @Before
    public void findButtons() {
        openButton = find("#openButton");
        buyButton = find("#buyButton");
        backButton = find("#backButton");
        displayPane = find("#displayPane");
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
    public void testElementsExist() {
        verifyThat("#displayPane", isNotNull());
        verifyThat("#packListPane", isNotNull());
        verifyThat("#openButton", isNotNull());
        verifyThat("#buyButton", isNotNull());
        verifyThat("#backButton", isNotNull());
        verifyThat("#cardViewButton", isNotNull());
        verifyThat("#numPacks", isNotNull());
        verifyThat("#currencyLabel", isNotNull());
        verifyThat("#goldenEggs", isNotNull());
    }

    /**
     * Test that the open button works, consumes a card pack, and
     * is disabled when no packs are available.
     * 
     * @throws InterruptedException
     */
    @Test
    public void testOpenButton() throws InterruptedException {
        Platform.runLater(() -> openButton.fire());
        waitForRunLater();
        Assert.assertTrue(openButton.isDisable());
        Assert.assertTrue(gameManager.getUser().getCardPacks() == 0);
    }
    
    /**
     * Test that the buy button works, consumes 500 currency, and
     * is disabled when currency is too low.
     * 
     * @throws InterruptedException
     */
    @Test
    public void testBuyButton() throws InterruptedException {
        Platform.runLater(() -> buyButton.fire());
        waitForRunLater();
        Platform.runLater(() -> buyButton.fire());
        waitForRunLater();
        Assert.assertTrue(buyButton.isDisable());
        Assert.assertTrue(GameManager.getInstance().getUser().getCurrency() == 0);
    }

    /**
     * Test the hover animation.
     * 
     * @throws InterruptedException
     */
    @Test
    public void testHandleHover() throws InterruptedException {
        // fix this horrible hack
        Platform.runLater(() -> openButton.fire());
        waitForRunLater();
        FxRobot sam = new FxRobot();
        sam.moveTo(stage.getScene());
        sam.moveBy(0, -300);
        sam.moveBy(0, 300);
    }
    
    /**
     * Test that the back button works.
     * 
     * @throws InterruptedException
     */
    @Test
    public void testBackButton() throws InterruptedException {
    	Platform.runLater(() -> backButton.fire());
        waitForRunLater();
    }

    /** 
     * Facilitates thread-safe testing
     * 
     * @throws InterruptedException
     */
    public static void waitForRunLater() throws InterruptedException {
        Semaphore semaphore = new Semaphore(0);
        Platform.runLater(semaphore::release);
        semaphore.acquire();
    }

    /**
     * Clean up.
     * 
     * @throws TimeoutException
     */
    @AfterClass
    public static void cleanUp() throws TimeoutException {
        setupStage(Stage::close);
    }

}
