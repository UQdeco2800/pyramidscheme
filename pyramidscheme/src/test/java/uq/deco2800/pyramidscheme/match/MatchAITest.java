package uq.deco2800.pyramidscheme.match;

import org.junit.Assert;
import org.junit.Test;
import uq.deco2800.pyramidscheme.board.RecBoard;
import uq.deco2800.pyramidscheme.board.RecTile;
import uq.deco2800.pyramidscheme.cards.BasicAction;
import uq.deco2800.pyramidscheme.cards.BasicMinion;
import uq.deco2800.pyramidscheme.cards.supercards.CardNotFoundException;
import uq.deco2800.pyramidscheme.cards.supercards.MinionCard;
import uq.deco2800.pyramidscheme.deck.Deck;
import uq.deco2800.pyramidscheme.player.AI;
import uq.deco2800.pyramidscheme.player.User;
import uq.deco2800.pyramidscheme.pyramid.Pyramid;
import uq.deco2800.pyramidscheme.pyramid.PyramidType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Max on 9/09/2016.
 */
public class MatchAITest {

    User user = new User("User", new Deck());
    MatchPlayer matchPlayer = new MatchPlayer(user, false);

    AI ai = new AI("AI", new Deck());
    MatchAI matchAI = new MatchAI(ai);

    // These have to be reinitialized each test
    RecBoard board;
    Pyramid pyramid;
    List<MatchCard> cards;
    List<MatchCard> cards2;
    List<MatchCard> cards3;

    int userOffset = RecBoard.WIDTH;

    private void setup() throws CardNotFoundException {
        board = new RecBoard(0, 0, matchPlayer, matchAI);
        pyramid = new Pyramid(0, 0, PyramidType.DIAMOND, true);
        cards = new ArrayList<>();
        cards2 = new ArrayList<>();
        cards3 = new ArrayList<>();

        cards.add(new MatchCard(MinionCard.get("King Duck"), 0, 0));
        cards.add(new MatchCard(new BasicMinion(), 0, 0));
        cards.add(new MatchCard(new BasicMinion(), 0, 0));
        cards.add(new MatchCard(new BasicMinion(), 0, 0));
        cards.add(new MatchCard(new BasicMinion(), 0, 0));
        cards.add(new MatchCard(new BasicMinion(), 0, 0));

        cards2.add(new MatchCard(MinionCard.get("Mummy Duck"), 0, 0));
        cards2.add(new MatchCard(MinionCard.get("King Duck"), 0, 0));
        cards2.add(new MatchCard(MinionCard.get("Tutanquackum"), 0, 0));

        cards3.add(new MatchCard(MinionCard.get("Useless Duck"), 0, 0));
        cards3.add(new MatchCard(MinionCard.get("Sir Galaduck"), 0, 0));
        cards3.add(new MatchCard(MinionCard.get("Tutanquackum"), 0, 0));
        cards3.add(new MatchCard(MinionCard.get("Ducthulhu"), 0, 0));

        matchAI.setBoard(board);
        matchAI.setPyramid(pyramid);
    }

    @Test
    public void testGetUserTiles() throws CardNotFoundException {
        setup();
        matchAI.duckDustPool.avatarState();

        List<RecTile> userTiles = matchAI.getUserTiles();

        Assert.assertEquals(userTiles.size(), RecBoard.WIDTH);
        Assert.assertTrue(userTiles
                .stream()
                .allMatch(x -> x.getOwner().equals(matchPlayer)));
    }

    @Test
    public void testImagineCombat() throws CardNotFoundException {
        matchAI.duckDustPool.avatarState();
        MatchCard basicAction = new MatchCard(new BasicAction(), 0, 0);

        // 3 2
        MatchCard basicMinion = new MatchCard(new BasicMinion(), 0, 0);

        // 2 8
        MatchCard sirGala = new MatchCard(MinionCard.get("Sir Galaduck"), 0, 0);

        Assert.assertEquals(matchAI.imagineCombat(basicAction, basicMinion), -1);

        Assert.assertEquals(matchAI.imagineCombat(basicMinion, basicAction), -1);

        Assert.assertEquals(matchAI.imagineCombat(sirGala, basicMinion), 1);

        Assert.assertEquals(matchAI.imagineCombat(basicMinion, basicMinion), 2);

        Assert.assertEquals(matchAI.imagineCombat(sirGala, sirGala), 3);

        Assert.assertEquals(matchAI.imagineCombat(basicMinion, sirGala), 4);

    }

    @Test
    public void testGetKillableCards() throws CardNotFoundException {
        // Pick free kills over trades
        setup();
        matchAI.duckDustPool.avatarState();
        pyramid.loadCards(cards);

        board.getTiles().get(0 + userOffset).setContents(new MatchCard(MinionCard.get("King Duck"), 0, 0));
        board.getTiles().get(1 + userOffset).setContents(new MatchCard(MinionCard.get("Mummy Duck"), 0, 0));

        ArrayList<RecTile> killableCards = new ArrayList<>();
        killableCards.add(board.getTiles().get(1 + userOffset));
        Assert.assertEquals(matchAI.getKillableCards(), killableCards);

        // Picks both free kills if there are multiple
        setup();
        matchAI.duckDustPool.avatarState();
        pyramid.loadCards(cards);

        board.getTiles().get(0 + userOffset).setContents(new MatchCard(MinionCard.get("Mummy Duck"), 0, 0));
        board.getTiles().get(1 + userOffset).setContents(new MatchCard(MinionCard.get("Mummy Duck"), 0, 0));

        killableCards = new ArrayList<>();
        killableCards.add(board.getTiles().get(0 + userOffset));
        killableCards.add(board.getTiles().get(1 + userOffset));
        Assert.assertEquals(matchAI.getKillableCards(), killableCards);

        // Pick trades over stalemates
        setup();
        matchAI.duckDustPool.avatarState();
        pyramid.loadCards(cards);

        board.getTiles().get(0 + userOffset).setContents(new MatchCard(new BasicMinion(), 0, 0));
        board.getTiles().get(1 + userOffset).setContents(new MatchCard(MinionCard.get("Sir Galaduck"), 0, 0));

        killableCards = new ArrayList<>();
        killableCards.add(board.getTiles().get(0 + userOffset));
        Assert.assertEquals(matchAI.getKillableCards(), killableCards);

        // Pick stalemates over nothing
        setup();
        matchAI.duckDustPool.avatarState();
        pyramid.loadCards(cards);

        board.getTiles().get(0 + userOffset).setContents(new MatchCard(MinionCard.get("Ducthulhu"), 0, 0));
        board.getTiles().get(1 + userOffset).setContents(new MatchCard(MinionCard.get("Sir Galaduck"), 0, 0));

        killableCards = new ArrayList<>();
        killableCards.add(board.getTiles().get(1 + userOffset));
        Assert.assertEquals(matchAI.getKillableCards(), killableCards);

        // Make sure you don't pick minions with something in front of them
        setup();
        matchAI.duckDustPool.avatarState();
        pyramid.loadCards(cards);

        board.getTiles().get(0 + userOffset).setContents(new MatchCard(MinionCard.get("Tutanquackum"), 0, 0));
        board.getTiles().get(1 + userOffset).setContents(new MatchCard(MinionCard.get("Tutanquackum"), 0, 0));
        board.getTiles().get(0).setContents(new MatchCard(MinionCard.get("Tutanquackum"), 0, 0));

        killableCards = new ArrayList<>();
        killableCards.add(board.getTiles().get(1 + userOffset));
        Assert.assertEquals(matchAI.getKillableCards(), killableCards);
    }

    @Test
    public void testYankFromPyramid() throws CardNotFoundException {
        setup();
        matchAI.duckDustPool.avatarState();
        pyramid.loadCards(cards);

        // Tests grabbing the highest value enemy from the killable cards
        // and getting a minion that can free kill it.
        board.getTiles().get(1 + userOffset).setContents(new MatchCard(MinionCard.get("Mummy Duck"), 0, 0));
        board.getTiles().get(0 + userOffset).setContents(new MatchCard(MinionCard.get("Crazed Duckling"), 0, 0));

        Assert.assertEquals(cards.get(0), matchAI.yankFromPyramid());

        setup();
        matchAI.duckDustPool.avatarState();
        pyramid.loadCards(cards2);
        // Tests getting the first minion that can trade when there are multiple
        board.getTiles().get(0 + userOffset).setContents(new MatchCard(MinionCard.get("Tutanquackum"), 0, 0));

        Assert.assertEquals(cards2.get(0), matchAI.yankFromPyramid());

        setup();
        matchAI.duckDustPool.avatarState();
        pyramid.loadCards(cards3);

        // Loops through all cases
        board.getTiles().get(0 + userOffset).setContents(new MatchCard(MinionCard.get("King Duck"), 0, 0));

        Assert.assertEquals(cards3.get(3), matchAI.yankFromPyramid());
    }

    @Test
    public void testChoosePositionHighestValue() throws CardNotFoundException {
        setup();
        matchAI.duckDustPool.avatarState();

        board.getTiles().get(userOffset + 1).setContents(new MatchCard(MinionCard.get("King Duck"), 0, 0));
        board.getTiles().get(userOffset + 0).setContents(new MatchCard(MinionCard.get("Tutanquackum"), 0, 0));

        pyramid.loadCards(cards);

        // King Duck can trade with both, so the the MummyDuck should be targeted
        Assert.assertEquals(matchAI.choosePosition()
                , board.getTiles().get(userOffset + 1).getOppositeTile());
    }

    @Test
    public void testChoosePositionNoKillable() throws CardNotFoundException {
        setup();
        matchAI.duckDustPool.avatarState();

        board.getTiles().get(0 + userOffset).setContents(new MatchCard(MinionCard.get("Ducthulhu"), 0, 0));

        pyramid.loadCards(cards);

        Assert.assertEquals(matchAI.choosePosition()
                , board.getTiles().get(1));
    }

    @Test
    public void testChoosePositionNoHope() throws CardNotFoundException {
        setup();
        matchAI.duckDustPool.avatarState();
        board.getTiles().get(0 + userOffset).setContents(new MatchCard(MinionCard.get("Ducthulhu"), 0, 0));
        board.getTiles().get(1 + userOffset).setContents(new MatchCard(MinionCard.get("Ducthulhu"), 0, 0));
        board.getTiles().get(2 + userOffset).setContents(new MatchCard(MinionCard.get("Ducthulhu"), 0, 0));
        board.getTiles().get(3 + userOffset).setContents(new MatchCard(MinionCard.get("Ducthulhu"), 0, 0));
        board.getTiles().get(4 + userOffset).setContents(new MatchCard(MinionCard.get("Ducthulhu"), 0, 0));
        board.getTiles().get(5 + userOffset).setContents(new MatchCard(MinionCard.get("Ducthulhu"), 0, 0));

        pyramid.loadCards(cards);
        Assert.assertEquals(matchAI.choosePosition(), null);
    }

    @Test
    public void testDeadOnBoard() throws CardNotFoundException {
        setup();
        matchAI.duckDustPool.avatarState();
        pyramid.loadCards(cards);
        matchAI.changeHealth(-20);

        // Not dead
        Assert.assertEquals(null, matchAI.checkDeadOnBoard());

        // Not dead
        board.getTiles().get(0 + userOffset).setContents(new MatchCard(MinionCard.get("Mummy Duck"), 0, 0));
        Assert.assertEquals(null, matchAI.checkDeadOnBoard());

        // Not dead
        board.getTiles().get(1 + userOffset).setContents(new MatchCard(MinionCard.get("Ducthulhu"), 0, 0));
        Assert.assertEquals(null, matchAI.checkDeadOnBoard());

        // Yes dead
        board.getTiles().get(2 + userOffset).setContents(new MatchCard(MinionCard.get("King Duck"), 0, 0));
        board.getTiles().get(3 + userOffset).setContents(new MatchCard(MinionCard.get("King Duck"), 0, 0));
        Assert.assertEquals(board.getTiles().get(1), matchAI.checkDeadOnBoard());

        // Not dead
        board.getTiles().get(1).setContents(new MatchCard(MinionCard.get("King Duck"), 0, 0));
        Assert.assertEquals(null, matchAI.checkDeadOnBoard());

        // Yes dead
        board.getTiles().get(4 + userOffset).setContents(new MatchCard(MinionCard.get("Ducthulhu"), 0, 0));
        Assert.assertEquals(board.getTiles().get(4), matchAI.checkDeadOnBoard());
    }

    @Test
    public void testPlayCardUserLethal() throws CardNotFoundException {
        setup();
        matchAI.duckDustPool.avatarState();
        matchAI.changeHealth(-20);
        pyramid.loadCards(cards2);
        board.getTiles().get(0 + userOffset).setContents(new MatchCard(MinionCard.get("King Duck"), 0, 0));
        board.getTiles().get(1 + userOffset).setContents(new MatchCard(MinionCard.get("King Duck"), 0, 0));
        board.getTiles().get(2 + userOffset).setContents(new MatchCard(MinionCard.get("King Duck"), 0, 0));
        board.getTiles().get(3 + userOffset).setContents(new MatchCard(MinionCard.get("King Duck"), 0, 0));

        List<Object> expected = new ArrayList<>();
        expected.add(cards2.get(1));
        expected.add(matchAI.checkDeadOnBoard());
        Assert.assertEquals(expected, matchAI.playCard());
    }

    @Test
    public void testPlayCardNoLethal() throws CardNotFoundException {
        setup();
        matchAI.duckDustPool.avatarState();
        pyramid.loadCards(cards2);
        List<Object> expected = new ArrayList<>();
        expected.add(matchAI.yankFromPyramid());
        expected.add(matchAI.choosePosition());
        Assert.assertEquals(expected, matchAI.playCard());
    }

    @Test
    public void testPickToGrind() throws CardNotFoundException {
        setup();
        pyramid.loadCards(cards3);
        Assert.assertEquals(cards3.get(1), matchAI.pickToGrind());
        matchAI.duckDustPool.grind();
        Assert.assertEquals(cards3.get(3), matchAI.pickToGrind());
        matchAI.duckDustPool.grind();
        matchAI.duckDustPool.grind();
        matchAI.duckDustPool.grind();
        matchAI.duckDustPool.grind();
        matchAI.duckDustPool.grind();
        Assert.assertEquals(cards3.get(0), matchAI.pickToGrind());

        matchAI.duckDustPool.avatarState();
        Assert.assertEquals(null, matchAI.pickToGrind());
    }
}
