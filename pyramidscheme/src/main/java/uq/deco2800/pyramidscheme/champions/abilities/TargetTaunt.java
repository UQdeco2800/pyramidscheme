package uq.deco2800.pyramidscheme.champions.abilities;

import uq.deco2800.pyramidscheme.animations.emitter.Emitter;
import uq.deco2800.pyramidscheme.match.MatchCard;

/**
 * Ability to force a minion to attack this hero
 *
 * @author Silin Liu
 */
public class TargetTaunt extends TargetAbility {

    private boolean forceToggle;

    /**
     * Initialize the Taunt ability
     *
     * @param abilityName The name of the new ability.
     * @param imgPath     The file path to ability icon.
     * @param isForce     Determines if the targeted minion is forced to attack the tank.
     */
    public TargetTaunt(String abilityName, String imgPath, int cost) {
        super(abilityName, imgPath, cost);
    }


    @Override
    public boolean activateAbility(Object target, Object notUsed) {
        // check parameter types
        if (target instanceof MatchCard && notUsed == null) {
            // cast target to MatchCard
            MatchCard targetMatchCard = (MatchCard) target;


            if (forceToggle) {
                targetMatchCard.attackedBy(targetMatchCard);
            }
            /* if true: force the minion in question to target this hero and correspondingly
            reduce it's health pool */
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
        return Emitter.TAUNT;
    }

    public String toString() {
        return "?";
    }


}
