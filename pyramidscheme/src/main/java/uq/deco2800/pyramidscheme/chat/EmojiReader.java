package uq.deco2800.pyramidscheme.chat;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;
import javafx.scene.image.Image;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Reads emoji images from file.
 */
public class EmojiReader {
    private static Logger logger = LoggerFactory.getLogger(EmojiReader.class);

    static final String EMOJI_LOCATION = "/emojiImages/";
    static final String CONFIG_FILE = "/emojiImages/emoji.yaml";

    private Map<String, String> pathMap;
    private Map<String, Image> imageMap;

    public EmojiReader() {
        //This is an empty constructor.
    }

    public Map<String, String> read() {
        if (pathMap == null) {
            URL url = getClass().getResource(CONFIG_FILE);
            YamlReader reader = null;
            try {
                reader = new YamlReader(new FileReader(url.getPath()));
            } catch (FileNotFoundException e) {
                logger.error("Can't find emoji config file", e);
                System.exit(1);
            }
            Object o = null;
            try {
                o = reader.read(Map.class);
            } catch (YamlException e) {
                logger.error("Can't read from emoji config file", e);
                System.exit(1);
            }
            Map<String, String> map = (Map<String, String>) o;
            logger.debug("Emoji image config: {}", map);
            pathMap = map;
        }
        return pathMap;
    }

    public Map<String, Image> readImages() {
        if (imageMap == null) {
            Map<String, String> paths = read();

            Map<String, Image> images = new HashMap<>();
            for (Map.Entry<String, String> entry : paths.entrySet()) {
                String key = entry.getKey();
                String path = entry.getValue();

                String resource = EMOJI_LOCATION + path;

                Image image = new Image(resource);
                images.put(key, image);
            }
            imageMap = images;
        }
        return imageMap;
    }
}
