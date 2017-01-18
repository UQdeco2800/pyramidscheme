package uq.deco2800.pyramidscheme.champions.abilities;

import uq.deco2800.pyramidscheme.animations.emitter.Emitter;
import uq.deco2800.pyramidscheme.match.MatchCard;

/**
 * An ability that heals a target for a flat amount.
 *
 * @author Fry
 */
public class TargetHeal extends TargetAbility {

    private int healValue;

    /**
     * Create a new instance of TargetEnrage.
     *
     * @param abilityName  String representing the name of the new ability.
     * @param imgPath      String representing the file path to ability icon.
     * @param health       Integer value of how much health is to be restored to the
     *                     target.
     * @param attackBuffed Integer value of how much to buff the targets attack by.
     */
    public TargetHeal(String abilityName, String imgPath, int cost, int health) {
        super(abilityName, imgPath, cost);
        this.healValue = health;
    }

    /**
     * Heal the target MatchCard for a flat amount.
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

            logger.info("Used " + this.getName() + ", healed " + target.toString() + " for " + healValue);
            targetMatchCard.changeHealth(healValue);

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
        return Emitter.RADIANT_SUN;
    }

    public String toString() {
        return this.getName() + ":\nHeal a target card for " + healValue + " health\nDust: " + getCost();
    }
}
