package uq.deco2800.pyramidscheme.chat;

import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uq.deco2800.pyramidscheme.game.GameManager;
import uq.deco2800.singularity.clients.pyramidscheme.PyramidSchemeClient;
import uq.deco2800.singularity.clients.realtime.messaging.MessagingClient;
import uq.deco2800.singularity.clients.realtime.messaging.MessagingEventListener;
import uq.deco2800.singularity.common.representations.MessageChannel;
import uq.deco2800.singularity.common.representations.User;
import uq.deco2800.singularity.common.representations.realtime.IncomingMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Controller for managing the chat system.
 */
public class ChatController {

    boolean showing = false;
    private ChatStore chatStore = new ChatStore();
    private MessagingClient messagingClient;
    private PyramidSchemeClient schemeClient;

    private DoubleProperty progress;

    private static Logger logger = LoggerFactory.getLogger(ChatController.class);

    public ChatController(PyramidSchemeClient schemeClient, MessagingClient messagingClient) {
        this.schemeClient = schemeClient;
        this.messagingClient = messagingClient;

        this.progress = new SimpleDoubleProperty(this, "progress", -1.0D);

        setupMessaging();

        // Get the list of channels from the server and add them to the GUI
        Optional<List<MessageChannel>> channels = getChannels();
        channels.ifPresent(this::populateChannelList);
    }

    /**
     * Set up messaging
     *
     * @param
     * @return
     **/
    private void setupMessaging() {
        Thread messagingThread = createMessagingClientThread();
        messagingThread.start();
    }

    /**
     * create Thread for Messaging Client
     *
     * @param
     * @return Thread
     **/
    private Thread createMessagingClientThread() {
        Runnable task = () ->
                messagingClient.addListener(new MessagingEventListener() {
                    @Override
                    public void didReceiveMessage(IncomingMessage message) {
                        Platform.runLater(() -> {
                            ChatMessage chatMessage = new ChatMessage(message, schemeClient.getUsername());
                            chatStore.addMessage(chatMessage);

                            Stage stage = GameManager.getPrimaryStage();
                            //if got message then popup notification say got a message from who
                            if (!schemeClient.getUsername().equals(chatMessage.authorUsername)) {
                                String messagepop = new String(message.getFromUserName() + " : " + chatMessage.message);

                                final Popup popup = createNotificationMessage(messagepop);
                                popup.setOnShown(new EventHandler<WindowEvent>() {
                                    @Override
                                    public void handle(WindowEvent e) {

                                        popup.setX(1660);
                                        popup.setY(1500);

                                    }
                                });
                                if (!showing) {
                                    popup.show(stage);
                                    showing = true;
                                } else {
                                    try {
                                        popup.wait(5000);
                                    } catch (InterruptedException e1) {
                                        logger.error("Interrupted", e1);
                                        Thread.currentThread().interrupt();
                                    }
                                }
                            }

						
						/* Check if the message targets a known channel */
                            if (!chatStore.getChannelById(chatMessage.channelId).isPresent()) {
                                ChatChannel channel = channelFromId(chatMessage.channelId);
                                chatStore.addChannel(channel);
                            }
                        });
                    }
                });
        return new Thread(task);
    }

    /**
     * Function to create a notification message popup.
     *
     * @param message wanted to show
     * @return Popup
     **/
    private Popup createNotificationMessage(String text) {
        final Popup messagePopup = new Popup();
        messagePopup.setAutoFix(true);
        messagePopup.setAutoHide(true);
        messagePopup.setHideOnEscape(true);
        Label popupText = new Label(text);
        popupText.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                messagePopup.hide();
                popupText.setText("");
                showing = false;
            }
        });


        popupText.getStylesheets().add("/css/ChatScreenStyle.css");
        popupText.getStyleClass().add("popup");

        messagePopup.getContent().add(popupText);

        return messagePopup;
    }

    /**
     * Function to get the chat store.
     *
     * @param
     * @return ChatStore
     **/
    public ChatStore getChatStore() {
        return chatStore;
    }

    /**
     * Function to get the messaging Client.
     */
    public MessagingClient getMessagingClient() {
        return messagingClient;
    }

    /**
     * Function to get channel by using channel ID.
     *
     * @param channelId
     * @return ChatChannel
     **/

    private ChatChannel channelFromId(String channelId) {
        ChatChannel channel = null;
        try {
            List<MessageChannel> participants = schemeClient.getChannelParticipants(channelId);
            List<String> usernames = participants.stream()
                    .map(p -> schemeClient.getUserInformationById(p.getUserId()))
                    .map(User::getUsername)
                    .filter(u -> !(u.equals(schemeClient.getUsername())))
                    .collect(Collectors.toList());
            String channelName = String.join(", ", usernames);
            channel = new ChatChannel(channelId, channelName);
        } catch (IOException e) {
            logger.error("Failed to get channel participant info", e);
            System.exit(1);
        }

        return channel;
    }

    /**
     * Function to populate Channel List.
     *
     * @param list of channels
     * @return
     **/
    private void populateChannelList(List<MessageChannel> channels) {
        final int max = channels.size();
        Task<List<ChatChannel>> task = new Task<List<ChatChannel>>() {
            @Override
            protected List<ChatChannel> call() throws Exception {
                List<ChatChannel> chatChannels = new ArrayList<>();

                IntStream.range(0, channels.size()).forEach(i -> {

                    MessageChannel channel = channels.get(i);
                    String channelId = channel.getChannelId();
                    ChatChannel chatChannel = channelFromId(channelId);
                    chatChannels.add(chatChannel);

                    updateProgress(i + 1, max);
                });
                return chatChannels;
            }
        };

        task.setOnSucceeded(e -> {
            List<ChatChannel> chatChannel = task.getValue();
            chatChannel.forEach(chatStore::addChannel);
        });

        this.progress.bind(task.progressProperty());

        new Thread(task).start();
    }

    /**
     * Function to get Channels.
     *
     * @param
     * @return Optional<List<MessageChannel>>
     */
    private Optional<List<MessageChannel>> getChannels() {
        try {
            List<MessageChannel> channels = schemeClient.getUsersChannels();
            return Optional.of(channels);
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    public final ReadOnlyDoubleProperty progressProperty() {
        return this.progress;
    }
}
