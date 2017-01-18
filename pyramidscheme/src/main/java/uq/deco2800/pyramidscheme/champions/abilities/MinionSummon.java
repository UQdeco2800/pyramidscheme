package uq.deco2800.pyramidscheme.champions.abilities;

import uq.deco2800.pyramidscheme.animations.emitter.Emitter;
import uq.deco2800.pyramidscheme.board.RecTile;
import uq.deco2800.pyramidscheme.cards.supercards.MinionCard;
import uq.deco2800.pyramidscheme.match.MatchCard;

public class MinionSummon extends Ability {

    // variable to store the mininon to be summoned by this ability
    private MinionCard minionToSummon;

    /**
     * Create a new instance of MinionSummon.
     *
     * @param abilityName String representing the name of the new ability.
     * @param imgPath     String representing the file path to ability icon.
     * @param minion      MinionCard to be summoned
     */
    public MinionSummon(String abilityName, String imgPath, int cost, MinionCard minion) {
        super(abilityName, imgPath, cost);
        this.minionToSummon = minion;
    }

    /**
     * Summon the defined minion to the next available RecTile
     *
     * @param summonTo TecTile that minion will be summoned to.
     * @param notUsed  Must be Null, this parameter is not used.
     */
    @Override
    public boolean activateAbility(Object summonTo, Object notUsed) {
        // check parameter types
        if (summonTo instanceof RecTile && notUsed == null) {
            // cast targetTile to RecTile
            RecTile targetTile = (RecTile) summonTo;

            // create a match instance of the card, and summon it to the given
            // location
            MatchCard newMatchMinion = new MatchCard(minionToSummon, 0, 0);
            targetTile.setContents(newMatchMinion);
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
        return Emitter.COMMANDING_QUACK;
    }

    public String toString() {
        return this.getName() + ":\nSummon a " + minionToSummon.getName() + " to your board\nDust: " + getCost();
    }

}
