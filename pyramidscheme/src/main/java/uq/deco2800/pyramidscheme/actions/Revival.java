package uq.deco2800.pyramidscheme.actions;

import uq.deco2800.pyramidscheme.board.RecBoard;
import uq.deco2800.pyramidscheme.board.RecTile;
import uq.deco2800.pyramidscheme.cards.supercards.MinionCard;
import uq.deco2800.pyramidscheme.match.MatchCard;

/**
 * Created by sanchez on 20/10/16.
 */
public class Revival extends Action {

    public Revival() {
        super("Revival", null, "/actionImages/Revival.jpg", "When destroyed, this minion will summon a secondary" +
                " minion to place of it.");
    }

    public void turn(RecTile attackingTile, RecBoard board) {
        // This action trigger is not needed and so it can be left empty
    }

    public void killed(RecTile oldPosition, RecBoard board) {
        this.log("Reviving a new duck");
        try {
            MatchCard card = new MatchCard(MinionCard.get("Useless Duck"), 0, 0);
            oldPosition.removeContents();
            oldPosition.setContents(card);
        } catch (Exception ex) {
            this.log("Error occurred while parsing revival");
            this.log(ex.getMessage());
        }
    }

    public boolean attacked(MatchCard victim, MatchCard attacker) {
        return false;
    }

    public void summoned(RecTile summonedTile, RecBoard board) {
        // This action trigger is not needed and so it can be left empty
    }
}
