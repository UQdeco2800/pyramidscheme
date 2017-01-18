package uq.deco2800.pyramidscheme.duckdust;


/**
 * Created by hugokawamata on 22/09/2016.
 */
public class DuckDustPool {

    /**
     * Invariant: maxDust <= dustCap &&
     * currentDust <= maxDust
     */

    // The amount of dust you have at the start of your turn.
    private int maxDust;
    // The amount of dust you have at a point in time (at the start of the turn
    // it is equal to maxDust)
    private int currentDust;
    // The largest value maxDust can have.
    private int dustCap;
    // A flag for whether the player has ground their one card or not
    private boolean playerHasGround = false;

    public DuckDustPool(int dustCap) {
        this.dustCap = dustCap;
        maxDust = 0;
        currentDust = 0;
    }

    /**
     * Returns the amount of dust you have at that point of time. At the start of
     * a turn it is equal to maxDust
     *
     * @return the currentDust value
     */
    public int getCurrentDust() {
        return currentDust;
    }

    /**
     * Returns the amount of dust the player will have at the start of the turn
     *
     * @return the maxDust value
     */
    public int getMaxDust() {
        return maxDust;
    }

    /**
     * Returns the maximum amount of dust the player can have.
     *
     * @return the dustCap value
     */
    public int getDustCap() {
        return dustCap;
    }

    /**
     * Increments maxDust and currentDust if the cap has not been reached.
     */
    public void grind() {
        int addedDust = 1;
        // Cap isn't preventing addition of dust
        if (maxDust + addedDust <= dustCap) {
            maxDust += addedDust;
            currentDust += addedDust;
        } else { // Added dust is more than the difference
            // Add so that maxDust is at the cap.
            maxDust = dustCap;
        }
        // Mark player has played
        playerHasGround = true;
    }

    /**
     * Determines whether or not the player has used their grind for the turn.
     *
     * @return true if the player has ground a card this turn, false otherwise.
     */
    public boolean getGround() {
        return playerHasGround;
    }

    /**
     * Marks the player has played as false so the player can grind another card.
     */
    public void playerTurnOver() {
        playerHasGround = false;
    }

    /**
     * Checks if the card can be grinded; situations where
     */
    public boolean canGrind() {
        return (maxDust + 1 <= dustCap && !playerHasGround);
    }

    /**
     * Returns whether a card with cost of `cost` is playable.
     *
     * @param cost of card being played
     * @return true if playable, false otherwise
     */
    public boolean isPlayable(int cost) {
        return cost <= currentDust;
    }

    /**
     * Attempts to spend an amount of dust. If it is too much, return 1.
     *
     * @param cost The amount of dust to spend.
     * @return 0 if the cost is less than or equal to the current amount of dust.
     * 1 otherwise. The controller will interpret this as an invalid play
     * and prevent it.
     */
    public int spend(int cost) {
        if (cost <= currentDust) {
            currentDust -= cost;
        } else {
            return 1;
        }
        return 0;
    }

    /**
     * Attempts to refund an amount of dust. If it is too much, return 1 and
     * and do not refund it.
     *
     * @param cost the amount of dust to refund.
     * @return 0 if the cost plus the current dust is less than or equal to the
     * max dust, and 1 otherwise.
     */
    public int refund(int cost) {
        if (cost + currentDust > maxDust) {
            return 1;
        } else {
            currentDust += cost;
        }
        return 0;
    }

    /**
     * Called at the start of the turn. Makes any spent dust unspent.
     * i.e. if you spent 6 dust out of 6 dust (leaving you with 0 currentDust) last turn,
     * you will have 6 dust again at the start of your turn.
     */
    public void refreshDust() {
        currentDust = maxDust;
    }

    /**
     * Gives you crazy amounts of duck dust (for testing purposes)
     */
    public void avatarState() {
        dustCap = 100;
        maxDust = dustCap;
        currentDust = maxDust;
    }

    /**
     * Resets the amount of dust you have (back to 0) for testing purposes.
     */
    public void resetDust() {
        currentDust = 0;
        maxDust = 0;
    }

}
