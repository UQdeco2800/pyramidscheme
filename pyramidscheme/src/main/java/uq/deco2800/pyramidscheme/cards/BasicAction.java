package uq.deco2800.pyramidscheme.cards;

import uq.deco2800.pyramidscheme.actions.Action;
import uq.deco2800.pyramidscheme.board.RecBoard;
import uq.deco2800.pyramidscheme.board.RecTile;
import uq.deco2800.pyramidscheme.cards.supercards.ActionCard;
import uq.deco2800.pyramidscheme.match.MatchCard;

/**
 * Created by sanchez on 9/1/16.
 */
public class BasicAction extends ActionCard {

    public BasicAction() {
        super("Basic Action", "/cardImages/SirGala.png", null, null, 3);
        this.setAction(new TriggeredAction());
    }

    class TriggeredAction extends Action {
        public TriggeredAction() {
            super("Basic Action", null, "/actionImages/SpellShield.jpg",
                    "Just a basic action to test gameplay");
        }

        public void turn(RecTile attackingTile, RecBoard board) {
            // This action trigger is not needed and so it can be left empty
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
}
