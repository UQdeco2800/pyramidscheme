package uq.deco2800.pyramidscheme.champions.abilities;

import uq.deco2800.pyramidscheme.animations.emitter.Emitter;
import uq.deco2800.pyramidscheme.match.MatchCard;

public class TargetAnnihilate extends TargetAbility {
    /**
     * Initialises the Annihilate ability, which instantly kills the targeted minion card
     *
     * @param abilityName The name of this ability.
     * @param imgPath     Image file for this ability.
     */
    public TargetAnnihilate(String abilityName, String imgPath, int cost) {
        super(abilityName, imgPath, cost);
    }

    /**
     * @param target  Minion card to kill
     * @param notUsed Required null parameter
     */
    @Override
    public boolean activateAbility(Object target, Object notUsed) {
        // check parameter types
        if (target instanceof MatchCard && notUsed == null) {
            // cast target to MatchCard
            MatchCard targetMatchCard = (MatchCard) target;
            // Instantly kills the minion
            targetMatchCard.setHealth(0);
            targetMatchCard.setIsToDie(true);
            return true;
        }
        return false;
    }

    /**
     * Returns the Ability's emitter. Returns null if there is no emitter for this ability
     *
     * @return an Emitter type
     */
    @Override
    public Emitter getEmitter() {
        return null;
    }

    public String toString() {
        return this.getName() + ":\nInstantly destroy a target card\nDust: " + getCost();
    }
}
