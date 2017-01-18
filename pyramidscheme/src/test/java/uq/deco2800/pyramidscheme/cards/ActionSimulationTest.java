package uq.deco2800.pyramidscheme.cards;

import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.junit.Assert;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;
import uq.deco2800.pyramidscheme.actions.*;
import uq.deco2800.pyramidscheme.board.RecBoard;
import uq.deco2800.pyramidscheme.board.RecTile;
import uq.deco2800.pyramidscheme.cards.supercards.CardNotFoundException;
import uq.deco2800.pyramidscheme.cards.supercards.MinionCard;
import uq.deco2800.pyramidscheme.controllers.statemachine.StateCallback;
import uq.deco2800.pyramidscheme.deck.Deck;
import uq.deco2800.pyramidscheme.game.GameManager;
import uq.deco2800.pyramidscheme.game.GameState;
import uq.deco2800.pyramidscheme.match.Match;
import uq.deco2800.pyramidscheme.match.MatchCard;
import uq.deco2800.pyramidscheme.match.MatchPlayer;
import uq.deco2800.pyramidscheme.player.AI;
import uq.deco2800.pyramidscheme.player.User;

import static org.mockito.Mockito.mock;

/**
 * Created by sanchez on 21/10/16.
 */
public class ActionSimulationTest {

    RecBoard board;
    RecTile testTile;
    Match match;
    MatchPlayer matchUser;
    MatchPlayer matchAI;
    GameState gameState;

    public ActionSimulationTest() throws CardNotFoundException {
        User user = new User("User", new Deck());
        AI ai = new AI("AI", new Deck());
        match = new Match(user, ai);
        matchUser = match.getUser();
        matchAI = match.getOpponent();
        gameState = match.gameState();
        board = gameState.getBoard();
        testTile = board.getTilesOf(matchUser).get(0);
    }

    public void setup() throws CardNotFoundException {
        testTile.removeContents();
        testTile.getOppositeTile().removeContents();
        testTile.setContents(new MatchCard(MinionCard.get("Tutanquackum"), 0, 0));
        testTile.getOppositeTile().setContents(new MatchCard(MinionCard.get("Tutanquackum"), 0, 0));
    }

    @Test
    public void testAttackCounter() throws CardNotFoundException {
        MatchCard card1 = new MatchCard(MinionCard.get("Tutanquackum"), 0, 0);
        card1.getCard().setAction(new AttackCounter());
        MatchCard card2 = new MatchCard(MinionCard.get("Tutanquackum"), 0, 0);
        card1.attackedBy(card2);
        Assert.assertEquals(card1.getHealth(), -1);
        Assert.assertEquals(card1.getAttack(), 3);
        Assert.assertEquals(card2.getHealth(), -4);
        Assert.assertEquals(card2.getAttack(), 3);
    }

    @Test
    public void testSpellShield() throws CardNotFoundException {
        MatchCard card1 = new MatchCard(MinionCard.get("Tutanquackum"), 0, 0);
        card1.getCard().setAction(new SpellShield());
        MatchCard card2 = new MatchCard(MinionCard.get("Tutanquackum"), 0, 0);
        card1.attackedBy(card2);
        Assert.assertEquals(card1.getHealth(), 2);
        Assert.assertEquals(card1.getAttack(), 0);
        Assert.assertEquals(card2.getHealth(), 2);
        Assert.assertEquals(card2.getAttack(), 3);
        MatchCard card3 = new MatchCard(new MinionCard("Test1", "/cardImages/null.png", null, 5, 1, 1, 1), 0, 0);
        MatchCard card4 = new MatchCard(new MinionCard("Test2", "/cardImages/null.png", null, 2, 1, 1, 1), 0, 0);
        card4.getCard().setAction(new SpellShield());
        card4.attackedBy(card3);
        Assert.assertEquals(card3.getAttack(), 5);
        Assert.assertEquals(card3.getHealth(), 1);
        Assert.assertEquals(card4.getAttack(), 0);
        Assert.assertEquals(card4.getHealth(), -2);
    }

    @Test
    public void testRevival() throws CardNotFoundException {
        setup();
        testTile.getContents().get().getCard().setAction(new Revival());
        Assert.assertEquals(testTile.getContents().get().getCard().getName(), "Tutanquackum");
        testTile.getContents().get().getCard().getAction().killed(testTile, board);
        Assert.assertEquals(testTile.getContents().get().getCard().getName(), "Useless Duck");

        setup();
        addAction(new Revival());
        testDefaults();
        testTile.getContents().get().getCard().processTurn(testTile, board);
        testTile.getContents().get().getCard().getAction().summoned(testTile, board);
        testDefaults();

        setup();
        addAction(new Revival());
        testDefaults();
        Assert.assertFalse(testTile.getContents().get().getCard().getAction().attacked(testTile.getContents().get(),
                testTile.getContents().get()));
        testDefaults();
    }

    public void testDefaults() {
        Assert.assertEquals(testTile.getOppositeTile().getContents().get().getAttack(), 3);
        Assert.assertEquals(testTile.getOppositeTile().getContents().get().getHealth(), 2);
        Assert.assertEquals(testTile.getContents().get().getAttack(), 3);
        Assert.assertEquals(testTile.getContents().get().getHealth(), 2);
    }

    public void addAction(Action a) {
        testTile.getContents().get().getCard().setAction(a);
    }

    @Test
    public void testCharged() throws CardNotFoundException {
        setup();
        addAction(new Charge());
        testDefaults();
        testTile.getContents().get().getCard().getAction().summoned(testTile, board);
        Assert.assertEquals(testTile.getOppositeTile().getContents().get().getHealth(), -1);
        Assert.assertEquals(testTile.getOppositeTile().getContents().get().isToDie(), true);

        setup();
        addAction(new Charge());
        testDefaults();
        testTile.getContents().get().getCard().getAction().turn(testTile, board);
        testDefaults();

        setup();
        addAction(new Charge());
        testDefaults();
        testTile.getContents().get().getCard().getAction().killed(testTile, board);
        testDefaults();

        setup();
        addAction(new Charge());
        testDefaults();
        Assert.assertFalse(testTile.getContents().get().getCard().getAction().attacked(testTile.getContents().get(),
                testTile.getOppositeTile().getContents().get()));
        testDefaults();

        setup();
        addAction(new Charge());
        testTile.getOppositeTile().removeContents();
        testTile.getContents().get().getCard().getAction().summoned(testTile, board);
        testTile.getOppositeTile().setContents(new MatchCard(new MinionCard("Test", null, null, 3, -1, 0, 1), 0, 0));
        testTile.getContents().get().getCard().getAction().summoned(testTile, board);
        Assert.assertEquals(testTile.getOppositeTile().getContents().get().isToDie(), true);
    }

    @Test
    public void testDoubleStrike() throws CardNotFoundException {
        setup();
        addAction(new DoubleStrike());
        testDefaults();
        testTile.getContents().get().getCard().processTurn(testTile, board);
        MatchCard card = testTile.getContents().get();
        Assert.assertEquals(card.getAttack(), 3);
        Assert.assertEquals(card.getHealth(), 2);
        card = testTile.getOppositeTile().getContents().get();
        Assert.assertEquals(card.getAttack(), 3);
        Assert.assertEquals(card.getHealth(), -1);

        setup();
        addAction(new DoubleStrike());
        testDefaults();
        testTile.getContents().get().getCard().getAction().killed(testTile, board);
        testTile.getContents().get().getCard().getAction().summoned(testTile, board);
        testDefaults();

        setup();
        addAction(new DoubleStrike());
        testDefaults();
        Assert.assertFalse(testTile.getContents().get().getCard().getAction().attacked(testTile.getContents().get(),
                testTile.getOppositeTile().getContents().get()));
        testDefaults();
    }

    @Test
    public void testSneak() throws CardNotFoundException {
        setup();
        addAction(new Sneak());
        testDefaults();
        testTile.getContents().get().getCard().processTurn(testTile, board);
        testDefaults();

        setup();
        addAction(new Sneak());
        testDefaults();
        testTile.getContents().get().getCard().getAction().summoned(testTile, board);
        testTile.getContents().get().getCard().getAction().killed(testTile, board);
        Assert.assertFalse(testTile.getContents().get().getCard().getAction().attacked(testTile.getContents().get(),
                testTile.getContents().get()));

        setup();
        addAction(new Sneak());
        testTile.getOppositeTile().removeContents();
        testTile.getOppositeTile().setContents(new MatchCard(new MinionCard("Test", null, null, 1, 1, 1, 1), 0, 0));
        testTile.getContents().get().getCard().getAction().turn(testTile, board);
        Assert.assertEquals(testTile.getContents().get().getHealth(), 1);
        Assert.assertEquals(testTile.getOppositeTile().getContents().get().getHealth(), -2);
    }

    @Test
    public void testBasicAction() throws CardNotFoundException {
        setup();
        testTile.removeContents();
        testTile.setContents(new MatchCard(new BasicAction(), 0, 0));
        Assert.assertEquals(testTile.getContents().get().getCard().getAction().getName(), "Basic Action");
        Assert.assertEquals(testTile.getOppositeTile().getContents().get().getHealth(), 2);
        testTile.getContents().get().getCard().getAction().turn(testTile, board);
        Assert.assertFalse(testTile.getContents().get().getCard().getAction().attacked(testTile.getContents().get(),
                testTile.getContents().get()));
        testTile.getContents().get().getCard().getAction().summoned(testTile, board);
        testTile.getContents().get().getCard().getAction().killed(testTile, board);
        Assert.assertEquals(testTile.getOppositeTile().getContents().get().getHealth(), 2);
    }

    @Test
    public void testActionMethods() {
        Action a = new DoubleStrike();
        Assert.assertEquals(a.getName(), "Double Strike");
        Assert.assertEquals(a.getDescription(), "Attacks all the enemy cards");
        a = new Sneak();
        Assert.assertEquals(a.getName(), "Sneak");
        Assert.assertEquals(a.getDescription(), "Creature can attack the player directly should the power and " +
                "defense of the opposing creature be more than the card with this feature");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTabAction() {
        Action a = new TestAction();
        a.getActionTabImage();
    }

    class TestAction extends Action {

        public TestAction() {
            super("Test", null, "", "");
        }

        @Override
        public void turn(RecTile attackingTile, RecBoard board) {

        }

        @Override
        public boolean attacked(MatchCard victim, MatchCard attacker) {
            return false;
        }

        @Override
        public void summoned(RecTile summonedTile, RecBoard board) {

        }

        @Override
        public void killed(RecTile tile, RecBoard board) {

        }
    }

}
