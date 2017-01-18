package uq.deco2800.pyramidscheme.match;

import uq.deco2800.pyramidscheme.deck.Deck;
import uq.deco2800.pyramidscheme.duckdust.DuckDustPool;
import uq.deco2800.pyramidscheme.game.GameManager;
import uq.deco2800.pyramidscheme.player.Player;

/**
 * A representation of players during matches. Designed to separate player variables which can change
 * during matches (represented here) from those which shouldn't (represented in Player).
 *
 * @author Millie
 */

public class MatchPlayer {

    private Player player;
    private int health;
    public final DuckDustPool duckDustPool;
    // True for user, false for ai
    private boolean aiOrUser;

    public MatchPlayer(Player player, boolean aiOrUser) {
        this.player = player;
        this.health = player.getMaxHealth();
        this.aiOrUser = aiOrUser;
        this.duckDustPool = new DuckDustPool(10);
    }

    /**
     * Returns the Player of this MatchPlayer object.
     *
     * @return the Player of this MatchPlayer object.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Returns the health of this MatchPlayer.
     *
     * @return the health of this MatchPlayer.
     */
    public int getHealth() {
        return health;
    }

    /**
     * Returns true if this MatchPlayer is a user and false if it is AI.
     *
     * @return true if this MatchPlayer is a user and false if it is AI.
     */
    public boolean aiOrUser() {
        return aiOrUser;
    }

    /**
     * Returns the deck belonging to this MatchPlayer.
     *
     * @return the deck belonging to this MatchPlayer.
     */
    public Deck getDeck() {
        return player.getDeck();
    }

    /**
     * Adds a specified value to add to this players health. Can be negative to deal
     * damage to this player.
     *
     * @param diff The health to add
     */
    public void changeHealth(int diff) {
        health += diff;
    }

    /**
     * Takes another MatchCard and attempts to uses its attack stat to take away
     * health from this MatchPlayer.
     *
     * @param attacker The card that needs to fight this player.
     */
    public void attackedBy(MatchCard attacker) {
        changeHealth(-attacker.getAttack());

        // Update statistics tracking
        if (this.aiOrUser) {
            GameManager.getStatisticsTracking().addToHealthLost(attacker.getAttack());
        } else {
            GameManager.getStatisticsTracking().addToHealthTaken(attacker.getAttack());
        }
    }
}
