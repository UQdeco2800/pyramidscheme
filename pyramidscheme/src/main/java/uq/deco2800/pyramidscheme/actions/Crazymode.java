package uq.deco2800.pyramidscheme.actions;

import uq.deco2800.pyramidscheme.board.RecBoard;
import uq.deco2800.pyramidscheme.board.RecTile;
import uq.deco2800.pyramidscheme.match.MatchCard;

/*
 * CREATED BY XINYUAN YAN 15/09/2016
 */
public class Crazymode extends Action {
    public Crazymode() {
        super("Crazymode", null, "/actionImages/Crazymode.jpg",
                "Crazy Mode Description");
    }

    public void turn(RecTile attackingTile, RecBoard board) {
        this.log(this.getTarget().getName() + "goes crazy!");
        // sacrifice 1 hp and get 2 attack
        MatchCard card = attackingTile.getContents().get();
        card.changeAttack(2);
        card.changeHealth(-1);
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
