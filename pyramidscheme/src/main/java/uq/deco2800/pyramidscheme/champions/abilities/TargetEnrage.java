package uq.deco2800.pyramidscheme.champions.abilities;

import uq.deco2800.pyramidscheme.animations.emitter.Emitter;
import uq.deco2800.pyramidscheme.match.MatchCard;

/**
 * An ability that increases a targets attack at the cost of health.
 *
 * @author Fry
 */
public class TargetEnrage extends TargetAbility {

    private int damageDealtValue;
    private int attackBuffValue;

    /**
     * Create a new instance of TargetEnrage.
     *
     * @param abilityName  String representing the name of the new ability.
     * @param imgPath      String representing the file path to ability icon.
     * @param damageDealt  Integer value of how much damage is to be dealt to the target.
     * @param attackBuffed Integer value of how much to buff the targets attack by.
     */
    public TargetEnrage(String abilityName, String imgPath, int cost, int damageDealt, int attackBuffed) {
        super(abilityName, imgPath, cost);
        this.damageDealtValue = damageDealt;
        this.attackBuffValue = attackBuffed;
    }

    /**
     * Deal damage to a target MatchCard, and increase its attack.
     *
     * @param target  MatchCard to which damage will be dealt, and attack increased.
     * @param notUsed Must be Null, this parameter is not used.
     */
    @Override
    public boolean activateAbility(Object target, Object notUsed) {
        // check parameter types
        if (target instanceof MatchCard && notUsed == null) {
            // cast target to MatchCard
            MatchCard targetMatchCard = (MatchCard) target;

            logger.info("Used " + this.getName() + ", increased damage dealt by " +
                    attackBuffValue + " at the cost of " + damageDealtValue + " health");

            targetMatchCard.changeHealth(-damageDealtValue);
            targetMatchCard.changeAttack(attackBuffValue);


            // if the card is dead after damage, make sure it dies
            if (targetMatchCard.getHealth() <= 0) {
                targetMatchCard.setIsToDie(true);
            }
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
        if ("Enrage".equals(getName())) {
            return Emitter.ENRAGE;
        } else if ("Closer to Death".equals(getName())) {
            return Emitter.CLOSER_TO_DEATH;
        }
        return null;
    }

    public String toString() {
        return this.getName() + ":\nDeal " + damageDealtValue + " damage to a target card, increase its attack by " + attackBuffValue + "\nDust: " + getCost();
    }
}
