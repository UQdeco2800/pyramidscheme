package uq.deco2800.pyramidscheme.actions;

import javafx.scene.image.Image;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uq.deco2800.pyramidscheme.board.RecBoard;
import uq.deco2800.pyramidscheme.board.RecTile;
import uq.deco2800.pyramidscheme.cards.supercards.Card;
import uq.deco2800.pyramidscheme.match.MatchCard;

import java.util.HashMap;

/**
 * Abstract class that represents actions ActionCards can take. All specific
 * actions should implement this class.
 *
 * @author Millie
 */

public abstract class Action {

    private static HashMap<String, Image> tabImageCache;
    private Logger logger = LoggerFactory.getLogger(Action.class);
    private String actionName;
    private String actionDes;
    private Card actionTarget;
    private String actionImagePath;

    public Action(String name, Card target, String imgPath, String description) {
        this.actionName = name;
        this.actionDes = description;
        this.actionTarget = target;
        this.actionImagePath = imgPath;

        if (Action.tabImageCache == null) {
            Action.tabImageCache = new HashMap<>();
        }

    }

    /**
     * Logs the given string in the form "ACTION: + [string]"
     *
     * @param string the string to log.
     */
    public void log(String string) {
        logger.info("ACTION: " + string);
    }

    /**
     * Returns the name of this Action.
     *
     * @return the name of this Action.
     */
    public String getName() {
        return actionName;
    }

    /**
     * return the description of this Action.
     *
     * @return the description of this Action.
     */
    public String getDescription() {
        return actionDes;
    }

    /**
     * Returns this Action's target card.
     *
     * @return this Action's target card.
     */
    public Card getTarget() {
        return actionTarget;
    }

    /**
     * Sets the target of this Action.
     *
     * @param card the new target of this Action.
     */
    public void setTarget(Card card) {
        this.actionTarget = card;
    }

    /**
     * Returns the ActionTabImage of this Action.
     *
     * @return
     */
    public Image getActionTabImage() {
        if (!Action.tabImageCache.containsKey(actionName)) {
            Action.tabImageCache.put(actionName, new Image(actionImagePath));
        }
        return Action.tabImageCache.get(actionName);
    }

    public abstract void turn(RecTile attackingTile, RecBoard board);

    public abstract boolean attacked(MatchCard victim, MatchCard attacker);

    public abstract void summoned(RecTile summonedTile, RecBoard board);

    public abstract void killed(RecTile tile, RecBoard board);

}
