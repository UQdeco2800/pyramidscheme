package uq.deco2800.pyramidscheme.stage;

import javafx.stage.Popup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uq.deco2800.pyramidscheme.control.EmojiPicker;

/**
 * Popup for selecting emoji.
 */
public class EmojiPickerPopup extends Popup {
    private static Logger logger = LoggerFactory.getLogger(EmojiPickerPopup.class);
    private EmojiPicker picker;

    public EmojiPickerPopup() {
        picker = new EmojiPicker();

        getContent().setAll(picker);

        setAutoHide(true);
    }

    public String getEventEmoji() {
        return picker.getEventEmoji();
    }
}
