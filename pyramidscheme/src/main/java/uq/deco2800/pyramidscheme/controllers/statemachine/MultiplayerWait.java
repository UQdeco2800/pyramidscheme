package uq.deco2800.pyramidscheme.controllers.statemachine;

import uq.deco2800.pyramidscheme.match.Match;

/**
 * Created by nick on 7/10/16.
 */
public class MultiplayerWait extends StateNode {

    public MultiplayerWait(Match match, StateCallback stateCallback) {
        super(match, stateCallback);
        gs.getUserDuckDust().playerTurnOver();
        logger.info("multiplayer wait activated");
    }

    public StateNode processClick(int x, int y) {
        return this;
    }

    public StateNode processMoved(int x, int y) {
        return this;
    }
}