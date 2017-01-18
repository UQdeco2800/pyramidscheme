package uq.deco2800.pyramidscheme.actions;

import uq.deco2800.pyramidscheme.board.RecBoard;
import uq.deco2800.pyramidscheme.board.RecTile;
import uq.deco2800.pyramidscheme.match.MatchCard;

/**
 * Created by sanchez on 20/10/16.
 */
public class SpellShield extends Action {

    public SpellShield() {
        super("Spell Shield", null, "/actionImages/SpellShield.jpg", "When this minion is attack, it's attack will" +
                "decrease instead of health until attack is less than zero");
    }

    public void turn(RecTile tile, RecBoard board) {
        // Overrided and left blank because this action does not need to be called every turn
    }

    public boolean attacked(MatchCard victim, MatchCard attacker) {
        this.log(this.getTarget().getName() + " used it's attack to absorb the damage");
        victim.changeAttack(attacker.getAttack() * -1);
        if (victim.getAttack() < 0) {
            int diff = victim.getAttack() * -1;
            victim.setAttack(0);
            victim.changeHealth(diff * -1);
        }
        attacker.changeAttack(victim.getAttack() * -1);
        return true;
    }

    public void summoned(RecTile summonedTile, RecBoard board) {
        // This action trigger is not needed and so it can be left empty
    }

    public void killed(RecTile tile, RecBoard board) {
        // This action trigger is not needed and so it can be left empty
    }
}
