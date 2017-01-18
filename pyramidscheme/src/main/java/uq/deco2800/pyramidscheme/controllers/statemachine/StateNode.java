package uq.deco2800.pyramidscheme.controllers.statemachine;

import javafx.scene.canvas.GraphicsContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uq.deco2800.pyramidscheme.board.RecTile;
import uq.deco2800.pyramidscheme.game.GameState;
import uq.deco2800.pyramidscheme.match.Match;
import uq.deco2800.pyramidscheme.match.MatchCard;

import java.util.Optional;

/**
 * An abstract superclass for nodes in the statemachine. This class should
 * provide abstract definitions of means of triggering transitions between
 * nodes in the statemachine (eg. processClick), and utility functions
 * used by extending StateNodes (eg. getClicked)
 */
public abstract class StateNode {

    final ThreadLocal<Optional<MatchCard>> newClickOnPyramid = new ThreadLocal<>();
    final ThreadLocal<Optional<RecTile>> newClickOnTile = new ThreadLocal<>();
    boolean newClickOnDustPool;

    Match match;
    GameState gs;

    Logger logger = LoggerFactory.getLogger(StateNode.class);
    StateCallback stateCallback;

    public StateNode(Match match, StateCallback stateCallback) {
        this.match = match;
        this.gs = match.gameState();
        this.stateCallback = stateCallback;
    }

    /**
     * Given an x and y screenspace coordinate
     *
     * @param x The x screenspace coordinate
     * @param y The y screenspace coordinate
     */
    void getClicked(int x, int y) {
        newClickOnPyramid.set(gs.getUserPyramid().getMatchCardAt(x, y));
        newClickOnTile.set(gs.getBoard().getTileAt(x, y));
        newClickOnDustPool = gs.getGrinder().containsCoords(x, y);
    }


    /**
     * Given an x and y screenspace coordinate, returns either a new statenode
     * or the same statenode that the method was called on.
     * <p>
     * This method is designed to transition the statemachine in response to
     * user clicks.
     *
     * @param x The x screenspace coordinate
     * @param y The y screenspace coordinate
     */
    public abstract StateNode processClick(int x, int y);

    /**
     * Given an x and y screenspace coordinate, returns either a new statenode
     * or the same statenode that the method was called on.
     * <p>
     * This method is designed to transition the statemachine in response to
     * user mouse movement.
     *
     * @param x The x screenspace coordinate
     * @param y The y screenspace coordinate
     */
    public abstract StateNode processMoved(int x, int y);

    /**
     * Given a GraphicsContext, draw the GameState encapsulated within the
     * statemachine.
     *
     * @param gc The GraphicsContext to draw to
     */
    public void draw(GraphicsContext gc) {
        gs.draw(gc);
    }


}
