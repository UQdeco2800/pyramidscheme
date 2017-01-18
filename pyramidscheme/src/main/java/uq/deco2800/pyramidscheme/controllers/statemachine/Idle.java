package uq.deco2800.pyramidscheme.controllers.statemachine;

import uq.deco2800.pyramidscheme.game.GameManager;
import uq.deco2800.pyramidscheme.match.Match;

/**
 * A StateNode representing the state of "the user is currently doing nothing"
 */
public class Idle extends StateNode {

    public Idle(Match match, StateCallback stateCallback) {
        super(match, stateCallback);
        logger.info("Idle Node activated");

        // change the cursor back to default
        try {
            GameManager.setCursorDefault();
        } catch (NullPointerException e) {
            logger.error(e + ": failed to set cursor");
        }

    }

    /**
     * Given an x and y screenspace coordinate, returns an Idle, or HeldCard
     * node.
     * <p>
     * In the event of that the x and y coordinates lie on a valid card in the
     * pyramid, a HeldCard node is returned.
     * <p>
     * Otherwise, this Idle node is returned.
     *
     * @param x The x screenspace coordinate
     * @param y The y screenspace coordinate
     */
    public StateNode processClick(int x, int y) {
        this.getClicked(x, y);

        if (newClickOnPyramid.get().isPresent()) {
            gs.setHeld(gs.getUserPyramid().yankMatchCardAt(x, y));
            // Transition to a new state
            return new HeldCard(match, stateCallback);
        }

        // Stay on the current node
        return this;
    }

    /**
     * Given an x and y screenspace coordinate, returns an Idle node.
     * <p>
     * If x and y coordinates lie ontop of a card, display a card information
     * tooltip.
     *
     * @param x The x screenspace coordinate
     * @param y The y screenspace coordinate
     */
    public StateNode processMoved(int x, int y) {
        // Displays the hovercard at the cursor
        gs.hoverCard(x, y);

        // Stay on the current node
        return this;
    }
}
