package uq.deco2800.pyramidscheme.controllers.statemachine;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import uq.deco2800.pyramidscheme.board.RecTile;
import uq.deco2800.pyramidscheme.deck.BasicDeckTest;
import uq.deco2800.pyramidscheme.game.GameManager;
import uq.deco2800.pyramidscheme.match.Match;
import uq.deco2800.pyramidscheme.match.MatchCard;
import uq.deco2800.pyramidscheme.player.AI;
import uq.deco2800.pyramidscheme.player.User;
import uq.deco2800.pyramidscheme.pyramid.Pyramid;

import java.util.Optional;
import java.util.concurrent.TimeoutException;

import static org.testfx.api.FxToolkit.setupStage;


/**
 * Created by nick on 13/10/16.
 */
public class HeldCardTest {

    HeldCard heldCard;
    RecTile playerTile;

    @Before
    public void createHeldCard() {
        // Create a new match - needs User and an opponent
        User user = new User("Guest", BasicDeckTest.createDeck());
        AI opponent = new AI("Harding", BasicDeckTest.createDeck());
        Match match = new Match(user, opponent);

        GameManager.getStatisticsTracking().createGuestUser();

        //Load pyramid
        Pyramid pyramid = match.gameState().getUserPyramid();
        pyramid.loadCards(match.gameState().getUserMatchDeck().popCards(pyramid.getPyramidType().getSize()));

        //Set held card to first card in pyramid
        Optional<MatchCard> held = match.gameState().getUserPyramid().yankMatchCardAt(
                match.gameState().getUserPyramid().getCards().get(0).getX()+1,
                match.gameState().getUserPyramid().getCards().get(0).getY()+1);
        match.gameState().setHeld(held);

        // Give the player mana to play with
        match.getUser().duckDustPool.avatarState();

        // Get player rectile
        playerTile = match.gameState().getBoard().getTilesOf(match.getUser()).get(0);

        // Create empty callback
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

        //Set player tile
        heldCard = new HeldCard(match, stateCallback);
    }

    @Test
    public void testClickOnBoard() {
        // Test click on board
        StateNode state = heldCard.processClick(playerTile.getX() + 1, playerTile.getY() + 1);

        // Check if tile contains a card
        Assert.assertTrue(playerTile.getContents().isPresent());

        //Check state returned was correct
        Assert.assertTrue(state instanceof Idle);
    }

    @Test
    public void testClickElsewhere() {
        // Test click on board
        StateNode state = heldCard.processClick(0, 0);

        // Check pyramid has card back
        Assert.assertTrue(heldCard.gs.getUserPyramid().size() == 6);

        // Check card has been dropped
        Assert.assertFalse(heldCard.gs.getHeld().isPresent());

        //Check state returned was correct
        Assert.assertTrue(state instanceof Idle);
    }

    @Test
    public void testProcessMove() {
        StateNode state = heldCard.processMoved(0,0);
        Assert.assertEquals(state, heldCard);
    }

    @AfterClass
    public static void cleanUp() throws TimeoutException {
        setupStage((stage) -> stage.close());
    }

}