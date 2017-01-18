package uq.deco2800.pyramidscheme.actions;

import uq.deco2800.pyramidscheme.board.RecBoard;
import uq.deco2800.pyramidscheme.board.RecTile;
import uq.deco2800.pyramidscheme.match.MatchCard;

/**
 * Created by sanchez on 15/09/16.
 */
public class AttackCounter extends Action {

    public AttackCounter() {
        super("Attack Counter", null, "/actionImages/Counter.jpg",
                "Attack Counter description");
    }

    public void turn(RecTile attackingTile, RecBoard board) {
        // This is not needed for a turn by turn basis
    }

    public boolean attacked(MatchCard victim, MatchCard attacker) {
        this.log(this.getTarget().getName() + " has triggered a Counter Attack");
        attacker.changeAttack(1);
        attacker.changeHealth(victim.getAttack() * -1);
        attacker.resetAttack();
        return false;
    }

    public void summoned(RecTile summonedTile, RecBoard board) {
        // This action trigger is not needed and so it can be left empty
    }

    public void killed(RecTile tile, RecBoard board) {
        // This action trigger is not needed and so it can be left empty
    }
}
