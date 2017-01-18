package uq.deco2800.pyramidscheme.control;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uq.deco2800.pyramidscheme.chat.EmojiReader;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

/**
 * JavaFX component for selecting emojis.
 */
public class EmojiPicker extends VBox {
    private static Logger logger = LoggerFactory.getLogger(MessageListCell.class);

    private String eventEmoji;

    private static Map<String, Image> emojiImages = null;

    @FXML
    private GridPane gridPane;

    public EmojiPicker() {
        super();

        load();

        EmojiReader reader = new EmojiReader();

        if (emojiImages == null) {
            emojiImages = reader.readImages();
        }

        int column = 0;
        for (Map.Entry<String, Image> entry : emojiImages.entrySet()) {
            String key = entry.getKey();
            Image image = entry.getValue();

            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(32.0);
            imageView.setFitWidth(32.0);
            imageView.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> handleClick(e, key));

            gridPane.add(imageView, column, 0);
            GridPane.setMargin(imageView, new Insets(10, 10, 10, 1));

            column++;
        }
    }

    private void load() {
        final String resource = "/fxml/EmojiPicker.fxml";
        URL url = getClass().getResource(resource);
        if (url == null) {
            logger.error("Failed to find resource " + resource);
            System.exit(1);
        }

        FXMLLoader loader = new FXMLLoader(url);
        loader.setController(this);
        loader.setRoot(this);

        try {
            loader.load();
        } catch (IOException e) {
            logger.error("Failed to load resource", e);
            System.exit(1);
        }
    }

    private void handleClick(MouseEvent e, String emoji) {
        eventEmoji = emoji;

        fireEvent(new ActionEvent());
    }

    public String getEventEmoji() {
        return eventEmoji;
    }
}
