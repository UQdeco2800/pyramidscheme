package uq.deco2800.pyramidscheme.settings;

import javafx.scene.media.AudioClip;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uq.deco2800.pyramidscheme.game.GameManager;

import java.util.HashMap;

/**
 * Created by billy on 16/10/16.
 */
public class ClipCache {

    private static Logger logger = LoggerFactory.getLogger(GameManager.class);
    private static HashMap<String, AudioClip> sounds = new HashMap();

    private ClipCache() {
    }

    public static AudioClip getClip(String resource) {
        if (sounds.keySet().contains(resource)) {
            return sounds.get(resource);
        } else {
            try {
                AudioClip clip = new AudioClip(ClipCache.class.getResource("/sounds/" + resource).toExternalForm());
                sounds.put(resource, clip);
                logger.info("ClipCache: Loaded new clip - " + resource);
                return clip;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }


}
