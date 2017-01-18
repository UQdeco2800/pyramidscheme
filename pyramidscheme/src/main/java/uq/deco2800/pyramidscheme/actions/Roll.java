package uq.deco2800.pyramidscheme.actions;

import uq.deco2800.pyramidscheme.board.RecBoard;
import uq.deco2800.pyramidscheme.board.RecTile;
import uq.deco2800.pyramidscheme.match.MatchCard;

import java.util.Random;

/*
 * CREATED BY XINYUAN YAN 15/09/2016
 */
public class Roll extends Action {
    public Roll() {
        super("Roll", null, "/actionImages/Roll.jpg",
                "Roll Description");
    }

    public void turn(RecTile attackingTile, RecBoard board) {
        this.log("Let's test your luck on " + this.getTarget().getName());
        //create a random number between 1 to 10
        Random ran = new Random();
        int num = ran.nextInt(11) + 1;
        if (num > 5) {
            //turn card of the tile's attack to be doubled
            //if the random number >5
            MatchCard card = attackingTile.getContents().get();
            card.changeAttack(card.getAttack() * 2);
        } else {
            //turn oppsite card of the tile's health to 0
            //if the random number <= 5
            MatchCard card = attackingTile.getOppositeTile().
                    getContents().get();
            card.changeHealth(card.getAttack() * (-1));
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
