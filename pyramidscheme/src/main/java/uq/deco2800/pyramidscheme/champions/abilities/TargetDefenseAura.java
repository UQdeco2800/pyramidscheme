package uq.deco2800.pyramidscheme.champions.abilities;

import uq.deco2800.pyramidscheme.animations.emitter.Emitter;
import uq.deco2800.pyramidscheme.match.MatchCard;

/**
 * Grants 200% increase increase in health at the cost of 33.3% reduction in damage
 *
 * @author Silin Liu
 */
public class TargetDefenseAura extends TargetAbility {

    // Recommended value ~3
    private double healthIncrease;
    // Recommended value ~0.67
    private double attackReduction;

    /**
     * Initialises the Defensive Aura ability, specifying the amount of health traded for
     * an increase in damage.
     *
     * @param abilityName     The name of this ability.
     * @param imgPath         Image file for this ability.
     * @param healthIncrement Percentage based health increase
     * @param attackDecrement Percentage based attack increase
     */
    public TargetDefenseAura(String abilityName, String imgPath, int cost, double healthIncrement,
                             double attackDecrement) {
        super(abilityName, imgPath, cost);
        this.healthIncrease = healthIncrement;
        this.attackReduction = attackDecrement;

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
            // Increase health
            targetMatchCard.multiplyHealth(this.healthIncrease);
            // Decrease damage
            targetMatchCard.multiplyAttack(this.attackReduction);
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
        return this.getName() + ":\nLower a targets attack by " + (100 - ((int) (attackReduction * 100))) + "%, increase its health by "
                + (((int) healthIncrease * 100) - 100) + "%\nDust: " + getCost();
    }
}