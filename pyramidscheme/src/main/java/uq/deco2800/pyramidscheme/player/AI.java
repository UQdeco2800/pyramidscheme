package uq.deco2800.pyramidscheme.player;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uq.deco2800.pyramidscheme.deck.Deck;

/**
 * A representation of an AI player.
 *
 * @author Millie
 * @author bijelo9
 */

public class AI extends Player {

    Logger logger = LoggerFactory.getLogger(AI.class);

    /**
     * Create a new AI player with `name` and `deck`
     *
     * @param name AI name
     * @param deck AI deck
     */
    public AI(String name, Deck deck) {
        super(name, deck);
    }

    /**
     * Create a new AI player with `name` and `deck`
     *
     * @param name   AI name
     * @param deck   AI deck
     * @param health AI health
     */
    public AI(String name, Deck deck, int health) {
        super(name, deck, health);
    }


}