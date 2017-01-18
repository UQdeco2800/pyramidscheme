package uq.deco2800.pyramidscheme.control;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uq.deco2800.pyramidscheme.chat.ChatMessage;
import uq.deco2800.pyramidscheme.chat.EmojiReader;

import java.io.IOException;
import java.util.Map;

/**
 * JavaFX component for displaying chat messages.
 */
public class MessageListCell extends ListCell<ChatMessage> {
    private static Logger logger = LoggerFactory.getLogger(MessageListCell.class);
    private static final String DRAW_BORDER_STYLE_CLASS = "message-body";

    @FXML
    private VBox container;
    @FXML
    private Label authorLabel;
    @FXML
    private Label messageBodyLabel;
    @FXML
    private HBox messageBodyContainer;

    private FXMLLoader loader;

    private Map<String, Image> emojiMap;

    @Override
    protected void updateItem(ChatMessage message, boolean empty) {
        super.updateItem(message, empty);

        if (emojiMap == null) {
            emojiMap = new EmojiReader().readImages();
        }

        if (empty || message == null) {
            setText(null);
            setGraphic(null);
        } else {
            if (loader == null) {
                loader = new FXMLLoader(getClass().getResource("/fxml/Message.fxml"));
                loader.setController(this);
                try {
                    loader.load();
                } catch (IOException e) {
                    logger.error("Failed to load resource", e);
                    System.exit(1);
                }
            }

            authorLabel.setText(message.authorUsername);

            String text = message.message;
            if (emojiMap.containsKey(text)) {
                ImageView imageView = new ImageView(emojiMap.get(text));
                imageView.setFitHeight(64.0);
                imageView.setFitWidth(64.0);

                messageBodyContainer.getStyleClass().clear();
                messageBodyLabel.setText(null);
                messageBodyLabel.setGraphic(imageView);
            } else {
                messageBodyContainer.getStyleClass().add(DRAW_BORDER_STYLE_CLASS);
                messageBodyLabel.setText(text);
                messageBodyLabel.setGraphic(null);
            }

			/* Right-align outgoing messages */
            if (message.authorUsername.equals(message.selfUsername)) {
                this.setAlignment(Pos.CENTER_RIGHT);
            } else {
                this.setAlignment(Pos.CENTER_LEFT);
            }

            setText(null);
            setGraphic(container);
        }
    }
}
