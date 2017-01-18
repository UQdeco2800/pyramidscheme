package uq.deco2800.pyramidscheme.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.testfx.framework.junit.ApplicationTest;
import uq.deco2800.pyramidscheme.game.GameManager;
import uq.deco2800.pyramidscheme.settings.Sound;
import uq.deco2800.singularity.clients.pyramidscheme.PyramidSchemeClient;
import uq.deco2800.singularity.common.representations.User;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeoutException;

import static org.loadui.testfx.GuiTest.find;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.api.FxToolkit.setupStage;
import static org.testfx.matcher.base.NodeMatchers.isNotNull;

/**
 * Created by nick on 19/10/16.
 */
@RunWith(MockitoJUnitRunner.class)
public class RegistrationScreenControllerTest extends ApplicationTest {
    //Copied from the registration controller
    private static final int MIN_USERNAME_LENGTH = 1;
    private static final int MIN_PASSWORD_LENGTH = 1;
    private static final String USER_EXISTS_ALREADY = "That username is not available";
    private static final String USERNAME_TOO_SHORT =
            "Username must be at least " + MIN_USERNAME_LENGTH + " characters";
    private static final String USERNAME_INVALID = "Username not valid";
    private static final String PASSWORD_ERROR_MISMATCH = "Passwords do not match";
    private static final String PASSWORD_ERROR_TOO_SHORT =
            "Password must be at least " + MIN_PASSWORD_LENGTH + " characters";
    private static final String PASSWORD_ERROR_INVALID = "Password not valid";
    private static final String BAD_CONNECTION = "Can't connect to the server";

    GameManager gameManager;

    PyramidSchemeClient client;
    String username = "test";

    Stage stage;

    Button createUserButton;
    Hyperlink backToLoginButton;
    TextField usernameField;
    PasswordField passwordField;
    PasswordField verifyPassword;
    Label errorLabel;

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        gameManager = gameManager.getInstance();
        GameManager.getInstance().setPrimaryStage(stage);
        GameManager.getInstance().setSound(new Sound(true, 0, 0)); // mute sound


        client = mock(PyramidSchemeClient.class);
        PyramidSchemeClient client = mock(PyramidSchemeClient.class);
        User user = new User(username, "", "", "", "testing");
        when(client.createUser(any(User.class))).thenReturn(user);
        when(client.getUserInformationByUserName(any(String.class))).thenReturn(user);
        gameManager.setPyramidSchemeClient(client);

        Parent root = FXMLLoader
                .load(getClass().getResource("/fxml/RegistrationScreen.fxml"));
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

    @AfterClass
    public static void cleanUp() throws TimeoutException {
        setupStage((stage) -> stage.close());
    }

    @Before
    public void findButtons() {
        createUserButton = find("#createUserButton");
        backToLoginButton = find("#backtoLoginButton");
        usernameField = find("#usernameField");
        passwordField = find("#passwordField");
        verifyPassword = find("#verifyPasswordField");
        errorLabel = find("#errorLabel");
    }
    /**
     * Test to see if all buttons exist
     */
    @Test
    public void testButtonsExist() {
        verifyThat("#createUserButton", isNotNull());
        verifyThat("#backtoLoginButton", isNotNull());
        verifyThat("#usernameField", isNotNull());
        verifyThat("#passwordField", isNotNull());
        verifyThat("#verifyPasswordField", isNotNull());
        verifyThat("#errorLabel", isNotNull());
    }

    public static void waitForRunLater() throws InterruptedException {
        Semaphore semaphore = new Semaphore(0);
        Platform.runLater(semaphore::release);
        semaphore.acquire();
    }

    @Test
    public void testBackToHome() throws InterruptedException {
        Scene scene = stage.getScene();

        // Fire back to home
        Platform.runLater(() -> backToLoginButton.fire());
        waitForRunLater();

        //Test if actually left screen
        Assert.assertNotEquals(scene, gameManager.getStage().getScene());
    }

    @Test
    public void testBadCreateUser() throws InterruptedException {
        String goodUsername = "test";
        String goodPassword = "supersecretpassword";

        // Test short username
        usernameField.setText("");
        Platform.runLater(() -> createUserButton.fire());
        waitForRunLater();
        Assert.assertEquals(USERNAME_TOO_SHORT, errorLabel.getText());

        // Test username with space
        usernameField.setText("as as");
        Platform.runLater(() -> createUserButton.fire());
        waitForRunLater();
        Assert.assertEquals(USERNAME_INVALID, errorLabel.getText());

        // Test password too short
        usernameField.setText(goodUsername);
        passwordField.setText("");
        Platform.runLater(() -> createUserButton.fire());
        waitForRunLater();
        Assert.assertEquals(PASSWORD_ERROR_TOO_SHORT, errorLabel.getText());

        // Test password invalid
        usernameField.setText(goodUsername);
        passwordField.setText("asd asd");
        Platform.runLater(() -> createUserButton.fire());
        waitForRunLater();
        Assert.assertEquals(PASSWORD_ERROR_INVALID, errorLabel.getText());

        // Test password mismatch
        usernameField.setText(goodUsername);
        passwordField.setText(goodPassword);
        verifyPassword.setText("");
        Platform.runLater(() -> createUserButton.fire());
        waitForRunLater();
        Assert.assertEquals(PASSWORD_ERROR_MISMATCH, errorLabel.getText());

        // Test password mismatch
        usernameField.setText(goodUsername);
        passwordField.setText(goodPassword);
        verifyPassword.setText("");
        Platform.runLater(() -> createUserButton.fire());
        waitForRunLater();
        Assert.assertEquals(PASSWORD_ERROR_MISMATCH, errorLabel.getText());
    }

    @Test(timeout = 20000)
    public void testCreateUser() throws InterruptedException, JsonProcessingException {
        Scene scene = stage.getScene();
        String username = "test";
        String password = "testing";

        // Test good create
        usernameField.setText(username);
        passwordField.setText(password);
        verifyPassword.setText(password);
        Platform.runLater(() -> createUserButton.fire());
        waitForRunLater();
        Assert.assertNotEquals(scene, gameManager.getStage().getScene());
    }

}