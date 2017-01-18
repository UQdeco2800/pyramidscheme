package uq.deco2800.pyramidscheme.actions;

import uq.deco2800.pyramidscheme.board.RecBoard;
import uq.deco2800.pyramidscheme.board.RecTile;
import uq.deco2800.pyramidscheme.match.MatchCard;

/**
 * Created by sanchez on 20/10/16.
 */
public class Sneak extends Action {

    public Sneak() {
        super("Sneak", null, "/actionImages/Sneak.jpg",
                "Creature can attack the player directly should the power and defense of the opposing creature " +
                        "be more than the card with this feature");
    }

    public void turn(RecTile attackingTile, RecBoard board) {
        this.log(this.getTarget().getName() + " has triggered Sneak");
        MatchCard attackCard = attackingTile.getContents().get();
        if (attackingTile.getOppositeTile().getContents().isPresent()) {
            MatchCard oppositeCard = attackingTile.getOppositeTile().getContents().get();
            if (attackCard.getHealth() > oppositeCard.getHealth() || attackCard.getAttack() > oppositeCard.getAttack()) {
                oppositeCard.attackedBy(attackCard);
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
