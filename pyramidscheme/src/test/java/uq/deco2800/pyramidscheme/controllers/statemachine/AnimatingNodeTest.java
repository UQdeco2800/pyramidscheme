package uq.deco2800.pyramidscheme.controllers.statemachine;


import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import uq.deco2800.pyramidscheme.deck.BasicDeckTest;
import uq.deco2800.pyramidscheme.match.Match;
import uq.deco2800.pyramidscheme.player.AI;
import uq.deco2800.pyramidscheme.player.User;

import java.util.concurrent.TimeoutException;

import static org.testfx.api.FxToolkit.setupStage;


/**
 * Created by nick on 12/10/16.
 */
public class AnimatingNodeTest {

    AnimatingNode animatingNode;

    @Before
    public void createUserPyramidRefill() {
        // Create a new match - needs User and an opponent
        User user = new User("Guest", BasicDeckTest.createDeck());
        AI opponent = new AI("Harding", BasicDeckTest.createDeck());
        Match match = new Match(user, opponent);

        StateCallback stateCallback = new StateCallback() {
            @Override
            public void goToIdle() {

            }

            @Override
            public void gameOver(boolean userWon) {

            }

            @Override
            public void turnOver() {

            }
        };

        animatingNode = new AnimatingNode(match, stateCallback);
    }

    @Test
    public void testProcessClick() {
        // Get state from processClick
        StateNode state = animatingNode.processClick(0, 0);
        // Test same state
        Assert.assertEquals(animatingNode, state);
    }

    @Test
    public void testProcessMoved() {
        // Get state from processClick
        StateNode state = animatingNode.processMoved(0, 0);
        // Test same state
        Assert.assertEquals(animatingNode, state);
    }

    @AfterClass
    public static void cleanUp() throws TimeoutException {
        setupStage((stage) -> stage.close());
    }

}