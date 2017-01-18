package uq.deco2800.pyramidscheme.actions;

import uq.deco2800.pyramidscheme.board.RecBoard;
import uq.deco2800.pyramidscheme.board.RecTile;
import uq.deco2800.pyramidscheme.match.MatchCard;

/**
 * Created by Nathan on 16/09/16.
 */
public class Explode extends Action {

    public Explode() {
        super("Explode", null, "/actionImages/Explode.jpg",
                "Explode Description");
    }

    public void turn(RecTile attackingTile, RecBoard board) {
        this.log(this.getTarget().getName() + " has exploded!");

        for (RecTile tile : board) {
            if (tile.getContents().isPresent()) {
                if (tile.getOwner() != attackingTile.getOwner() && tile.getContents().isPresent()) {
                    tile.getContents().get().changeHealth(-1);
                }
                if (tile.getContents().get().getCard().getName() == "Exploduck") {
                    tile.getContents().get().setHealth(-1);
                }
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
