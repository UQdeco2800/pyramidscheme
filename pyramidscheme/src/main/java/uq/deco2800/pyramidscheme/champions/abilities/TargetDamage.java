package uq.deco2800.pyramidscheme.champions.abilities;

import uq.deco2800.pyramidscheme.animations.emitter.Emitter;
import uq.deco2800.pyramidscheme.match.MatchCard;

/**
 * An ability that deals a flat amount of damage to a target card.
 *
 * @author Josh Fry
 */
public class TargetDamage extends TargetAbility {

    private int damageValue;

    /**
     * Create a new instance of TargetDamage.
     *
     * @param abilityName String representing the name of the new ability.
     * @param imgPath     String representing the file path to ability icon.
     * @param damage      Integer value, the damage to be dealt
     */
    public TargetDamage(String abilityName, String imgPath, int cost, int damage) {
        super(abilityName, imgPath, cost);
        this.damageValue = damage;

    }

    /**
     * Deal a flat amount of damage to a target MatchCard.
     *
     * @param target  MatchCard to which damage will be dealt.
     * @param notUsed Must be Null, this parameter is not used.
     */
    @Override
    public boolean activateAbility(Object target, Object notUsed) {
        // check parameter types
        if (target instanceof MatchCard && notUsed == null) {
            // cast target to MatchCard
            MatchCard targetMatchCard = (MatchCard) target;

            logger.info(
                    "Used " + this.getName() + ", damaged " + target.toString() + " for " + damageValue + " damage");
            targetMatchCard.changeHealth(-damageValue);

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
     * Returns the Ability's emitter. Returns null if there is no emitter for
     * this ability
     *
     * @return an Emitter type
     */
    @Override
    public Emitter getEmitter() {
        if ("Smite".equals(getName())) {
            return Emitter.SMITE;
        } else if ("Scratch".equals(getName())) {
            return Emitter.SCRATCH;
        }
        return null;
    }

    public String toString() {
        return this.getName() + ":\nDeal " + damageValue + " damage to a target card\nDust: " + getCost();
    }
}
