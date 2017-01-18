package uq.deco2800.pyramidscheme.actions;

import uq.deco2800.pyramidscheme.board.RecBoard;
import uq.deco2800.pyramidscheme.board.RecTile;
import uq.deco2800.pyramidscheme.match.MatchCard;

/**
 * Created by sanchez on 20/10/16.
 */
public class Charge extends Action {

    public Charge() {
        super("Charge", null, "/actionImages/Charge.jpg", "Can attack the same turn it's played");
    }

    public void turn(RecTile attackingTile, RecBoard board) {
        // This function is to remain empty because there is no needed action for each turn
    }

    public void summoned(RecTile summonedTile, RecBoard board) {
        this.log(this.getTarget().getName() + " has been summoned");
        if (summonedTile.getOppositeTile().getContents().isPresent()) {
            summonedTile.getOppositeTile().getContents().get().changeHealth(
                    summonedTile.getContents().get().getAttack() * -1);
            if (summonedTile.getOppositeTile().getContents().get().getHealth() < 0) {
                summonedTile.getOppositeTile().getContents().get().setIsToDie(true);
            }
        }
    }

    public boolean attacked(MatchCard victim, MatchCard attacker) {
        return false;
    }

    public void killed(RecTile tile, RecBoard board) {
        // This action trigger is not needed and so it can be left empty
    }
}
