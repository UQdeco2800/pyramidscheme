package uq.deco2800.pyramidscheme.controllers;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uq.deco2800.pyramidscheme.chat.ChatChannel;
import uq.deco2800.pyramidscheme.chat.ChatController;
import uq.deco2800.pyramidscheme.chat.ChatMessage;
import uq.deco2800.pyramidscheme.chat.ChatStore;
import uq.deco2800.pyramidscheme.control.MessageListCell;
import uq.deco2800.pyramidscheme.game.GameManager;
import uq.deco2800.pyramidscheme.stage.EmojiPickerPopup;
import uq.deco2800.singularity.clients.pyramidscheme.PyramidSchemeClient;
import uq.deco2800.singularity.clients.realtime.messaging.MessagingClient;
import uq.deco2800.singularity.common.representations.MessageChannel;
import uq.deco2800.singularity.common.representations.User;

import javax.ws.rs.WebApplicationException;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * A controller for the chat room.
 *
 * @author pan1501
 */

public class ChatRoomController implements Initializable {
    private static Logger logger = LoggerFactory.getLogger(ChatRoomController.class);

    // Declare the FX objects this class uses

    @FXML
    private TextField channelField;
    @FXML
    private ListView<ChatChannel> channelList;
    @FXML
    private ListView<ChatMessage> messageList;
    @FXML
    private TextField newMessageField;
    @FXML
    private Button sendMessageButton;
    @FXML
    private Label channelTitleLabel;
    @FXML
    private StackPane chatRoomScreen;
    @FXML
    private Button emojiButton;
    @FXML
    private VBox leftBox;
    @FXML
    private ImageView theirAvatar;

    private ChatStore chatStore;
    private PyramidSchemeClient schemeClient;
    private MessagingClient messagingClient;
    private EmojiPickerPopup popup;

    /**
     * get chat message
     *
     * @param incoming message, user name
     * @return
     **/
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        ChatController chatController = GameManager.getChatController();
        chatStore = chatController.getChatStore();
        messagingClient = chatController.getMessagingClient();
        schemeClient = GameManager.getInstance().getPyramidSchemeClient();

        setupTooltips();

        // Set up events
        sendMessageButton.setOnAction(e -> onSendMessage());
        newMessageField.setOnAction(e -> onSendMessage());
        channelList.setOnMouseClicked(event -> {
            try {
                handleRemoveFriend(event);
            } catch (JsonParseException e1) {
                logger.debug("JsonParseException!", e1);
            } catch (JsonMappingException e1) {
                logger.debug("JsonMappingException!", e1);
            } catch (WebApplicationException e1) {
                logger.debug("WebApplicationException!", e1);
            } catch (IOException e1) {
                logger.debug("IOException!", e1);
            }
        });
        emojiButton.setOnAction(this::showEmojiPicker);

        channelField.setOnAction(e -> onAddChannel());

        channelList.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldItem, newItem) -> onChannelSelected(newItem));

        // Overlay a progress bar while the channel list is loading
        ProgressBar bar = new ProgressBar();
        bar.progressProperty().bind(chatController.progressProperty());
        chatRoomScreen.getChildren().add(bar);

        bar.progressProperty().addListener((o, oldValue, newValue) -> {
            logger.debug("progress: {}", newValue.doubleValue());
            if (1 == newValue.intValue()) {
                logger.debug("REMOVE BAR");
                chatRoomScreen.getChildren().remove(bar);
            }
        });

        // In case the channels are already loaded, ensure the progress bar is
        // not shown
        if (bar.progressProperty().getValue().equals(1.0)) {
            chatRoomScreen.getChildren().remove(bar);
        }

        // Hook our observable list of channels up to the channel list so that
        // any changes to the observable list will be reflected in the GUI.
        channelList.setItems(chatStore.getChannels());

        messageList.setCellFactory(list -> new MessageListCell());
    }

    /**
     * handle remove friend by mouse event click twice
     *
     * @param mouse click
     **/
    private void handleRemoveFriend(MouseEvent click) throws WebApplicationException, IOException {
        if (click.getClickCount() == 2) {
            ChatChannel channel = channelList.getSelectionModel().getSelectedItem();
            chatStore.removeChannel(channel);
            schemeClient.removeCurrentChannel(channel.channelId);
        }
    }

    /**
     * click to show Emoji picker
     *
     * @param event
     * @return
     **/
    private void showEmojiPicker(ActionEvent event) {
        if (popup == null) {
            popup = new EmojiPickerPopup();

            popup.addEventHandler(ActionEvent.ACTION, e -> {
                String emoji = popup.getEventEmoji();
                logger.debug(event.toString());
                logger.debug(emoji);

                popup.hide();

                getSelectedChannelId().ifPresent(channelId -> messagingClient.sendMessage(channelId, emoji));
            });
        }

        Stage stage = GameManager.getInstance().getChatScreenStage();
        popup.show(stage);

        Bounds localBounds = emojiButton.getBoundsInLocal();
        Bounds sourceBounds = emojiButton.localToScreen(localBounds);

        double x = sourceBounds.getMinX();
        double y = sourceBounds.getMinY() - popup.getHeight();

        popup.setX(x);
        popup.setY(y);
    }

    /**
     * while hover on channel show info of the channel
     **/
    private void setupTooltips() {
        channelList.setCellFactory(channel -> new ListCell<ChatChannel>() {
            private Tooltip tooltip = new Tooltip();

            @Override
            public void updateItem(ChatChannel channel, boolean empty) {
                super.updateItem(channel, empty);
                if (channel == null) {
                    setText(null);
                    setTooltip(null);
                } else {
                    setText(channel.channelName);
                    tooltip.setText(channel.channelId + " , " + channel.channelName);
                    setTooltip(tooltip);
                }
            }
        });
    }

    /**
     * Gets the name of the user corresponding to the channel and returns sets the text of
     * channelTitleLabel as the name of the user.
     * <p>
     * Each user is given a default avatar of NULL to start with.
     * The function tries to set the avatarURL to the avatar corresponding to the user selected,
     * If this is not possible due to the user selected not having an avatar then they are given
     * the default_avatar by default.
     *
     * @param channel
     */
    private void onChannelSelected(ChatChannel channel) {
        ObservableList<ChatMessage> messages = chatStore.getMessagesForChannel(channel.channelId);
        messageList.setItems(messages);
        channelTitleLabel.setText(channel.channelName);

        Optional<String> avatarUrl = null;

        try {
            avatarUrl = schemeClient
                    .getUsersAvatar(schemeClient.getUserInformationByUserName(channel.channelName).getUserId());

        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        if (avatarUrl == null) {
            theirAvatar.setImage(null);
        } else if (!avatarUrl.isPresent()) {
            theirAvatar.setImage(new Image("/chatRoomImages/default_avatar.png"));
        } else {
            theirAvatar.setImage(new Image(avatarUrl.get()));
        }

    }

    /**
     * add channel to channel list
     **/
    private void onAddChannel() {
        String username = channelField.getText();
        channelField.clear();

        // Get userId for user
        User user = schemeClient.getUserInformationByUserName(username);
        String userId = user.getUserId();

        if (userId == null) {
            logger.info("No userId for the given username: {}", username);
            return;
        }

        try {
            // if chat store contain insert username then return with a error
            for (ChatChannel channel : chatStore.getChannels()) {
                String channelId = channel.channelId;
                List<String> userIds = schemeClient.getChannelParticipants(channelId).stream()
                        .map(MessageChannel::getUserId).collect(Collectors.toList());

                if (userIds.contains(userId)) {
                    logger.info("There is already a channel [{}] that contains that user [{}]", channelId,
                            user.toString());
                    return;
                }
            }
            String channelId = schemeClient.createMessageChannel(userId);
            chatStore.addChannel(new ChatChannel(channelId, username));
        } catch (IOException e) {
            logger.error("Failed to create channel", e);
            System.exit(1);
        }

        channelList.getSelectionModel().selectLast();
    }

    /**
     * sent message
     **/
    private void onSendMessage() {
        String message = newMessageField.getText();

        getSelectedChannelId().ifPresent(channelId -> messagingClient.sendMessage(channelId, message));

        newMessageField.clear();
    }

    /**
     * get selected channel's ID
     *
     * @return Optional String
     **/
    private Optional<String> getSelectedChannelId() {
        ChatChannel channel = channelList.getSelectionModel().getSelectedItem();

        return Optional.ofNullable(channel).map(c -> c.channelId);
    }
}
