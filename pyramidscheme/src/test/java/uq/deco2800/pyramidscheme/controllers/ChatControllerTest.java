package uq.deco2800.pyramidscheme.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.AfterClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.testfx.framework.junit.ApplicationTest;
import uq.deco2800.pyramidscheme.game.GameManager;
import uq.deco2800.pyramidscheme.settings.Sound;
import uq.deco2800.singularity.clients.pyramidscheme.PyramidSchemeClient;
import uq.deco2800.singularity.clients.realtime.messaging.MessagingClient;
import uq.deco2800.singularity.common.representations.MessageChannel;
import uq.deco2800.singularity.common.representations.Token;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.api.FxToolkit.setupStage;
import static org.testfx.matcher.base.NodeMatchers.isNotNull;

/**
 * Tests for MenuController.
 *
 * @author Justin
 */

@RunWith(MockitoJUnitRunner.class)
public class ChatControllerTest extends ApplicationTest {

    /**
     * Mock the GameManager class with Mockito
     */
    GameManager gameManager;

    PyramidSchemeClient pyramidClient;
    MessagingClient messagingClient;

    @Override
    public void start(Stage stage) throws Exception {
        gameManager = GameManager.getInstance();
        pyramidClient = mock(PyramidSchemeClient.class);
        messagingClient = mock(MessagingClient.class);
        GameManager.getInstance().setSound(new Sound(true, 0, 0)); // mute sound



        List<MessageChannel> channels = new ArrayList<>();
        MessageChannel channel = new MessageChannel();
        channel.setChannelId("test");
        channel.setUserId("testing");
        channels.add(channel);

        when(pyramidClient.getUsername()).thenReturn("test");
        when(pyramidClient.getUsersChannels()).thenReturn(channels);


        when(pyramidClient.renewIfNeededAndGetToken()).thenReturn(new Token("hi"));
        gameManager.setPyramidSchemeClient(pyramidClient);
        gameManager.setMessagingClient(messagingClient);

        Parent root = FXMLLoader
                .load(getClass().getResource("/fxml/ChatRoomScreen.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Test to see if all buttons exist
     */
    @Test
    public void testButtonsExist() {
        verifyThat("#sendMessageButton", isNotNull());
        verifyThat("#channelField", isNotNull());
        verifyThat("#channelList", isNotNull());
        verifyThat("#messageList", isNotNull());
        verifyThat("#newMessageField", isNotNull());
        verifyThat("#sendMessageButton", isNotNull());
        verifyThat("#channelTitleLabel", isNotNull());
        verifyThat("#chatRoomScreen", isNotNull());
    }

    /**
     * @throws TimeoutException
     */
    @AfterClass
    public static void cleanUp() throws TimeoutException {
        setupStage((stage) -> stage.close());
    }

}
