package uq.deco2800.pyramidscheme.controllers;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testfx.framework.junit.ApplicationTest;
import uq.deco2800.pyramidscheme.deck.BasicDeckTest;
import uq.deco2800.pyramidscheme.game.GameManager;
import uq.deco2800.pyramidscheme.player.User;
import uq.deco2800.pyramidscheme.pyramid.PyramidType;
import uq.deco2800.pyramidscheme.settings.Sound;

import java.awt.*;
import java.awt.event.KeyEvent;
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
public class ChampionScreenControllerTest extends ApplicationTest {

    /**
     * Mock the GameManager class with Mockito
     */

    @InjectMocks
    private ChampionScreenController championScreenController;

    @Mock
    GameManager gameManager;

    Logger logger = LoggerFactory.getLogger(ChampionScreenController.class);

    private Button champButton;

    Stage stage;

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        gameManager = GameManager.getInstance();
        User user = new User("Guest", BasicDeckTest.createDeck());
        gameManager.setPrimaryStage(stage);
        gameManager.setUser(user);
        GameManager.getStatisticsTracking().createGuestUser();
        GameManager.getInstance().setSound(new Sound(true, 0, 0)); // mute sound


        // getResource() looks in src/main/resources
        URL url = this.getClass().getResource("/fxml/ChampionScreen.fxml");
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
        championScreenController = fxmlLoader.getController();
        championScreenController.setPyramidTypes(userType, aiType);

        //Display the scene
        Scene scene = new Scene(root);
        scene.setCamera(new PerspectiveCamera());
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Test to see if all nodes exist
     */
    @Test
    public void testAllNodesExist() {
        verifyThat("#button1", isNotNull());
    }

    @Before
    public void findButton() {
        champButton = find("#button1");
    }

    @Test
    public void testChampButton() throws InterruptedException {
        Scene scene = stage.getScene();
        Platform.runLater(() -> champButton.fire());
        waitForRunLater();
        Assert.assertNotEquals(scene, gameManager.getStage().getScene());
    }

    @Ignore
    @Test
    public void openChatScreen() throws AWTException, InterruptedException {

        //when(pyramidSchemeClient.renewIfNeededAndGetToken()).thenReturn(new Token("test"));

        Robot ted = new Robot();

        // Test F4
        ted.keyPress(KeyEvent.VK_F4);
        ted.keyRelease(KeyEvent.VK_F4);

        // Test something else
        ted.keyPress(KeyEvent.VK_SPACE);
        ted.keyRelease(KeyEvent.VK_SPACE);
        Thread.sleep(10000);
    }



    public static void waitForRunLater() throws InterruptedException {
        Semaphore semaphore = new Semaphore(0);
        Platform.runLater(semaphore::release);
        semaphore.acquire();
    }

    @AfterClass
    public static void cleanUp() throws TimeoutException {
        setupStage((stage) -> stage.close());
    }
}
