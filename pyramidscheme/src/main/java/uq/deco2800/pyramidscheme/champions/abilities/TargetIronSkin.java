package uq.deco2800.pyramidscheme.champions.abilities;

import uq.deco2800.pyramidscheme.animations.emitter.Emitter;
import uq.deco2800.pyramidscheme.match.MatchCard;

/**
 * An ability that drastically increases the resilience of a minion.
 *
 * @author Silin Liu
 */
public class TargetIronSkin extends TargetAbility {

    // Specifies the degree of which a minion's health would be increased by.
    private double healthMuliplier;

    /**
     * Initialises the iron skin ability, specifying the amount of which a
     * minion's health would be boosted by.
     *
     * @param abilityName The name of this ability.
     * @param imgPath     Image file for this ability.
     * @param healthMult  Percentage based health increase
     */
    public TargetIronSkin(String abilityName, String imgPath, int cost, double healthMult) {
        super(abilityName, imgPath, cost);
        this.healthMuliplier = healthMult;
    }

    /**
     * @param Target  Minion card to have it's health increased
     * @param notUsed Required null parameter
     */
    @Override
    public boolean activateAbility(Object target, Object notUsed) {
        // check parameter types
        if (target instanceof MatchCard && notUsed == null) {
            // cast target to MatchCard
            MatchCard targetMatchCard = (MatchCard) target;
            // Increase target minion health amount
            targetMatchCard.multiplyHealth(this.healthMuliplier);
            return true;
        }
        return false;
    }

    /**
     * Returns the Ability's emitter. Returns null if there is no emitter for
     * this ability
     *
     * @return an Emitter type
     */
    @Override
    public Emitter getEmitter() {
        return null;
    }

    public String toString() {
        return this.getName() + ":\nIncrease a targets health by " + ((int) healthMuliplier - 1) * 100 + "%\nDust: " + getCost();
    }
}
