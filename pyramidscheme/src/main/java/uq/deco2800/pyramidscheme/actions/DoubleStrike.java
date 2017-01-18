package uq.deco2800.pyramidscheme.actions;

import uq.deco2800.pyramidscheme.board.RecBoard;
import uq.deco2800.pyramidscheme.board.RecTile;
import uq.deco2800.pyramidscheme.match.MatchCard;

/**
 * Created by sanchez on 9/7/16.
 */
public class DoubleStrike extends Action {

    public DoubleStrike() {
        super("Double Strike", null, "/actionImages/DoubleAttack.jpg",
                "Attacks all the enemy cards");
    }

    public void turn(RecTile attackingTile, RecBoard board) {
        this.log(this.getTarget().getName() + " has triggered Double Strike");
        for (RecTile tile : board) {
            if (tile.getOwner() != attackingTile.getOwner() && tile.getContents().isPresent()) {
                tile.getContents().get().changeHealth(attackingTile.getContents().get().getAttack() * -1);
            }
        }
    }

    public boolean attacked(MatchCard victim, MatchCard attacker) {
        return false;
    }

    public void summoned(RecTile summonedTile, RecBoard board) {
        // This action trigger is not needed and so it can be left empty
    }

    public void killed(RecTile tile, RecBoard board) {
        // This action trigger is not needed and so it can be left empty
    }

}

