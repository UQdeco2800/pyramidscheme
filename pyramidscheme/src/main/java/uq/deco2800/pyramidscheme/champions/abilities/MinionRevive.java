package uq.deco2800.pyramidscheme.champions.abilities;

import uq.deco2800.pyramidscheme.animations.emitter.Emitter;
import uq.deco2800.pyramidscheme.board.RecTile;
import uq.deco2800.pyramidscheme.match.MatchCard;
import uq.deco2800.pyramidscheme.match.MatchDeck;

/**
 * An ability that revives the most recently destroyed minion
 *
 * @author Josh Fry
 */
public class MinionRevive extends Ability {

    /**
     * Create a new instance of MinionRevive
     *
     * @param abilityName String representing the name of the new ability.
     * @param imgPath     String representing the file path to ability icon.
     */
    public MinionRevive(String abilityName, String imgPath, int cost) {
        super(abilityName, imgPath, cost);
    }

    /**
     * Revives the most recently destroyed minion, AKA the minion at the top of
     * the players graveyard.
     *
     * @param reviveFrom MatchDeck representation of the player's graveyard.
     * @param reviveTo   RecTile that the card will be revived to.
     */
    @Override
    public boolean activateAbility(Object reviveFrom, Object reviveTo) {
        // check parameter types
        if (reviveFrom instanceof MatchDeck && reviveTo instanceof RecTile) {
            // cast parameters to their respective types
            MatchDeck graveYard = (MatchDeck) reviveFrom;
            RecTile targetTile = (RecTile) reviveTo;

            // check if game board is full
            if (!graveYard.isEmpty()) {
                // get the top card, reset its stats, and play it to the
                // targeted tile
                MatchCard reviveTarget = graveYard.popCards(1).get(0);
                reviveTarget.resetAttack();
                reviveTarget.resetHealth();
                reviveTarget.setIsToDie(false);
                targetTile.setContents(reviveTarget);

                logger.info("Used " + this.getName() + ", reviving " + reviveTarget.toString() + " from "
                        + graveYard.toString());
                return true;
            }
            logger.info("No minions to revive");
            return true;

        }
        logger.info("DEBUG: wrong parameters brah");
        return false;

    }

    /**
     * Returns the Ability's emitter. Returns null if there is no emitter for this ability
     *
     * @return an Emitter type
     */
    @Override
    public Emitter getEmitter() {
        return Emitter.ARISE;
    }

    public String toString() {
        return this.getName() + ":\nRevive your most recently destroyed card\nDust: " + getCost();
    }

}
