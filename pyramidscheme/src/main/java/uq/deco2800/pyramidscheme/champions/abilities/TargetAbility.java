package uq.deco2800.pyramidscheme.champions.abilities;

/**
 * Abstract class that represents Abilities that take cards as targets.
 *
 * @author Josh
 */
public abstract class TargetAbility extends Ability {

    public TargetAbility(String abilityName, String imgPath, int cost) {
        super(abilityName, imgPath, cost);
    }

}
