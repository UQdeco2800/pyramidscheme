package uq.deco2800.pyramidscheme.champions.abilities;

import uq.deco2800.pyramidscheme.animations.emitter.Emitter;
import uq.deco2800.pyramidscheme.match.MatchCard;

/**
 * Grants an increase in attack damage at the cost of max health
 *
 * @author Silin Liu
 */
public class TargetBerserk extends TargetAbility {

    // Recommended value ~0.67
    private double healthReduction;
    // Recommended value ~2.5
    private double attackIncrease;

    /**
     * Initialises the Berserk ability, specifying the amount of health traded
     * for an increase in damage.
     *
     * @param abilityName     The name of this ability.
     * @param imgPath         Image file for this ability.
     * @param healthDecrement Percentage based health increase
     * @param attackIncrement Percentage based increase in attack damage
     */
    public TargetBerserk(String abilityName, String imgPath, int cost, double healthDecrement, double attackIncrement) {
        super(abilityName, imgPath, cost);
        this.healthReduction = healthDecrement;
        this.attackIncrease = attackIncrement;

    }

    /**
     * @param Target  Minion card to have it's damage increased
     * @param notUsed Required null parameter
     */
    @Override
    public boolean activateAbility(Object target, Object notUsed) {
        // check parameter types
        if (target instanceof MatchCard && notUsed == null) {
            // cast target to MatchCard
            MatchCard targetMatchCard = (MatchCard) target;
            // Decrease health
            targetMatchCard.multiplyHealth(this.healthReduction);
            // Increase damage
            targetMatchCard.multiplyAttack(this.attackIncrease);

            if (targetMatchCard.getHealth() <= 0) {
                targetMatchCard.setIsToDie(true);
            }

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
        return this.getName() + ":\nRemove " + (100 - ((int) (healthReduction * 100))) + "% of a targets health, increase its attack by "
                + (((int) attackIncrease * 100) - 100) + "%\nDust: " + getCost();
    }
}
