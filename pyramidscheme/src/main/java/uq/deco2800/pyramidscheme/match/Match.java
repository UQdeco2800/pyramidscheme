package uq.deco2800.pyramidscheme.match;

import uq.deco2800.pyramidscheme.game.GameState;
import uq.deco2800.pyramidscheme.player.AI;
import uq.deco2800.pyramidscheme.player.User;

/**
 * Contains the data and variables required during a match.
 *
 * @author Millie
 */

public class Match {

    // User data
    private MatchPlayer user;

    // Opponent data
    private MatchAI opponent;

    //The gameState with all the data such as pyramids, board and matchdecks
    private GameState gameState;


    public Match(User user, AI opponent) {
        this.user = new MatchPlayer(user, true);
        this.opponent = new MatchAI(opponent);
        gameState = new GameState(this);
    }

    public MatchPlayer getUser() {
        return user;
    }

    public GameState gameState() {
        return gameState;
    }

    public MatchAI getOpponent() {
        return opponent;
    }

}
