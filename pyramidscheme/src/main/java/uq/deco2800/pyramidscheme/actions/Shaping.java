package uq.deco2800.pyramidscheme.actions;

import uq.deco2800.pyramidscheme.board.RecBoard;
import uq.deco2800.pyramidscheme.board.RecTile;
import uq.deco2800.pyramidscheme.match.MatchCard;

/*
 * Create by XINYUAN YAN 13/09/2016
 */
public class Shaping extends Action {
    public Shaping() {
        super("Shaping", null, "/actionImages/shapeshift.jpg",
                "Shaping Description");
    }

    public void turn(RecTile attackingTile, RecBoard board) {
        this.log(this.getTarget().getName() + " has been shaped into a Sheep!");
        if (attackingTile.getOppositeTile().getContents().isPresent()) {
            MatchCard card = attackingTile.getOppositeTile().getContents().get();
            card.changeAttack(card.getAttack() * (-1));
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
