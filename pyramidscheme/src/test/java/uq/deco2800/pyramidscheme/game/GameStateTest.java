package uq.deco2800.pyramidscheme.game;

import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlendMode;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.testfx.framework.junit.ApplicationTest;
import uq.deco2800.pyramidscheme.animations.emitter.Particle;
import uq.deco2800.pyramidscheme.board.RecTile;
import uq.deco2800.pyramidscheme.cards.BasicMinion;
import uq.deco2800.pyramidscheme.cards.supercards.CardHiddenState;
import uq.deco2800.pyramidscheme.controllers.statemachine.StateCallback;
import uq.deco2800.pyramidscheme.deck.Deck;
import uq.deco2800.pyramidscheme.match.Match;
import uq.deco2800.pyramidscheme.match.MatchCard;
import uq.deco2800.pyramidscheme.match.MatchDeck;
import uq.deco2800.pyramidscheme.match.MatchPlayer;
import uq.deco2800.pyramidscheme.player.AI;
import uq.deco2800.pyramidscheme.player.User;
import uq.deco2800.pyramidscheme.pyramid.PyramidType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeoutException;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.testfx.api.FxToolkit.setupStage;

/**
 * Created by nick on 19/10/16.
 */
@RunWith(MockitoJUnitRunner.class)
public class GameStateTest extends ApplicationTest {

    @Mock
    GameState gameState;

    @Mock
    MatchDeck userDeck;

    Match match;

    @Override
    public void start(Stage stage) throws Exception {
        User user = new User("User", new Deck());
        AI ai = new AI("AI", new Deck());
        //create a fake match
        match = new Match(user, ai);
        gameState = match.gameState();
        userDeck = gameState.getUserMatchDeck();
        GameManager.getStatisticsTracking().createGuestUser();
        stage.show();
    }

    @AfterClass
    public static void cleanUp() throws TimeoutException {
        setupStage((stage) -> stage.close());
    }

    @Test
    public void testSetPyramidTypes() {
        // Test Triangle
        gameState.setPyramidTypes(PyramidType.TRIANGLE, PyramidType.TRIANGLE);
        Assert.assertEquals(gameState.getUserPyramid().getPyramidType(),PyramidType.TRIANGLE);
        Assert.assertEquals(gameState.getAIPyramid().getPyramidType(),PyramidType.TRIANGLE);

        // Test Diamond
        gameState.setPyramidTypes(PyramidType.DIAMOND, PyramidType.DIAMOND);
        Assert.assertEquals(gameState.getUserPyramid().getPyramidType(),PyramidType.DIAMOND);
        Assert.assertEquals(gameState.getAIPyramid().getPyramidType(),PyramidType.DIAMOND);
    }

    @Test
    public void testInfoPanel() {
        // Test before
        Assert.assertFalse(gameState.getInfoPanel().isPresent());

        // Set getInfo and test
        Optional<MatchCard> card = Optional.of(new MatchCard(new BasicMinion(), 0, 0));
        gameState.addInfoPanel(card.get());
        Assert.assertTrue(gameState.getInfoPanel().isPresent());

        //Remove getInfo and test
        gameState.removeInfoPanel();
        Assert.assertEquals(Optional.empty(), gameState.getInfoPanel());
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testDefaultCardPile() {
        MatchCard card = new MatchCard(new BasicMinion(), 0, 0);

        // Test animating pile
        gameState.addAnimatingCard(card, CardHiddenState.VISIBLE);
        Assert.assertEquals(card, gameState.getAnimatingCard(card, CardHiddenState.VISIBLE));
        gameState.removeAnimatingCard(card, CardHiddenState.VISIBLE);

        // Verify it was removed
        gameState.getAnimatingCard(card, CardHiddenState.VISIBLE);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testUserCardPile() {
        MatchCard card = new MatchCard(new BasicMinion(), 0, 0);

        // Test animating pile
        gameState.addAnimatingCard(card, CardHiddenState.USERHIDDEN);
        Assert.assertEquals(card, gameState.getAnimatingCard(card, CardHiddenState.USERHIDDEN));
        gameState.removeAnimatingCard(card, CardHiddenState.USERHIDDEN);

        // Verify it was removed
        gameState.getAnimatingCard(card, CardHiddenState.USERHIDDEN);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testAICardPile() {
        MatchCard card = new MatchCard(new BasicMinion(), 0, 0);

        // Test animating pile
        gameState.addAnimatingCard(card, CardHiddenState.AIHIDDEN);
        Assert.assertEquals(card, gameState.getAnimatingCard(card, CardHiddenState.AIHIDDEN));
        gameState.removeAnimatingCard(card, CardHiddenState.AIHIDDEN);

        // Verify it was removed
        gameState.getAnimatingCard(card, CardHiddenState.AIHIDDEN);
    }

    private List<Particle> getParticles() {
        List<Particle> particles = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            new Particle(0,0, new Point2D(0,0), 0, 0, Color.rgb(215, 40, 45), BlendMode.ADD);
        }
        return particles;
    }

    @Test
    public void testAddParticles() {
        List<Particle> particles = getParticles();
        gameState.addParticles(particles);

        Assert.assertEquals(particles, gameState.getParticles());
    }

    @Test
    public void runDraw() {
        //Create canvas
        Canvas canvas = new Canvas(10, 10);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Add elements to piles
        MatchCard card = mock(MatchCard.class);
        MatchCard actualCard = new MatchCard(new BasicMinion(), 0, 0);
        gameState.addInfoPanel(actualCard);
        gameState.addAnimatingCard(card, CardHiddenState.USERHIDDEN);
        gameState.addAnimatingCard(card, CardHiddenState.AIHIDDEN);
        gameState.addAnimatingCard(card, CardHiddenState.VISIBLE);

        // Mock a particle
        Particle particle = mock(Particle.class);
        when(particle.isAlive()).thenReturn(true).thenReturn(false);
        List<Particle> particles = new ArrayList<>();
        particles.add(particle);

        //Add particle to gamestate
        gameState.addParticles(particles);

        // Draw particle
        gameState.draw(gc);

        // Check for particle
        Assert.assertFalse(gameState.getParticles().isEmpty());

        gameState.draw(gc);

        // Check particle killed
        Assert.assertTrue(gameState.getParticles().isEmpty());

        // Draw without particle and pane but with heldcard
        gameState.setHeld(Optional.ofNullable(actualCard));
        gameState.removeInfoPanel();
        gameState.draw(gc);



        // Verify the mocked items were accessed
        verify(particle, timeout(1000).times(2)).update();
        verify(particle, timeout(1000).times(1)).render(any());
        verify(card, timeout(1000).times(3)).draw(any());
        verify(card, timeout(1000).times(3)).drawHidden(any(), eq(CardHiddenState.AIHIDDEN));
        verify(card, timeout(1000).times(3)).drawHidden(any(), eq(CardHiddenState.USERHIDDEN));
    }

    @Test
    public void testGetters() {
        Assert.assertTrue(gameState.getUserGraveyard() != null);
        Assert.assertTrue(gameState.getAIGraveyard() != null);
        Assert.assertTrue(gameState.getUserMatchDeck() != null);
        Assert.assertTrue(gameState.getAIMatchDeck() != null);
        Assert.assertTrue(gameState.getUserDuckDust() != null);
        Assert.assertTrue(gameState.getAiDuckDust() != null);
        Assert.assertTrue(gameState.getGrinder() != null);
        Assert.assertTrue(gameState.getUserPyramid() != null);
        Assert.assertTrue(gameState.getAIPyramid() != null);
        Assert.assertEquals(gameState.getHeld(), Optional.empty());
        MatchCard actualCard = new MatchCard(new BasicMinion(), 0, 0);
        gameState.setHeld(Optional.ofNullable(actualCard));
        Assert.assertEquals(gameState.getHeld().get(), actualCard);
        gameState.dropHeld();
        Assert.assertEquals(gameState.getHeld(), Optional.empty());
        Assert.assertTrue(gameState.getBoard() != null);
    }

    @Test
    public void testAttack() {
        StateCallback callback = mock(StateCallback.class);

        // set up users
        MatchPlayer user = match.getUser();
        MatchPlayer ai = match.getOpponent();

        int userHealth = user.getHealth();
        int aiHealth = ai.getHealth();

        // A card
        MatchCard card = new MatchCard(new BasicMinion(), 0, 0);
        MatchCard card2 = new MatchCard(new BasicMinion(), 0, 0);
        MatchCard card3 = new MatchCard(new BasicMinion(), 0, 0);

        // Place card in matchdecks
        gameState.getUserMatchDeck().pushCard(card);
        gameState.getAIMatchDeck().pushCard(card);

        //place two cards opposite each other
        gameState.getBoard().getTilesOf(match.getUser()).get(0).setContents(card);
        gameState.getBoard().getTilesOf(match.getOpponent()).get(0).setContents(card2);

        //place one card not attacking another card
        gameState.getBoard().getTilesOf(match.getUser()).get(1).setContents(card3);

        // User attacks
        gameState.attack(user, ai, callback);

        // Both attacking cards suffer damage
        Assert.assertEquals(card.getHealth(), -1);
        Assert.assertEquals(card2.getHealth(), -1);
        // User card with no opponent suffers no damage
        Assert.assertEquals(card3.getHealth(), 2);
        // User suffers no damage
        Assert.assertEquals(userHealth, user.getHealth());
        // Ai suffers 3 damage
        Assert.assertEquals(aiHealth - 3, ai.getHealth());

        for (RecTile tile : gameState.getBoard().getDeadTiles()) {
            tile.removeContents();
        }
        
        // Ai attacks
        gameState.attack(ai, user, callback);
        // User card with no opponent suffers no damage
        Assert.assertEquals(card3.getHealth(), 2);
        // User suffers no damage
        Assert.assertEquals(userHealth, user.getHealth());
        // Ai suffers no damage
        Assert.assertEquals(aiHealth - 3, ai.getHealth());

        // User non-fatal kill
        gameState.getAIPyramid().loadCards(gameState.getAIMatchDeck().popCards(1));
        gameState.attack(user, ai, callback);

        // User 2x fatal kill
        ai.changeHealth(-100);
        gameState.attack(user, ai, callback);
        ai.changeHealth(150);
        gameState.getAIPyramid().confirmRemoval(card);
        gameState.attack(user, ai, callback);
        gameState.getAIMatchDeck().pushCard(card);

        // AI non fatal kill
        gameState.getUserPyramid().loadCards(gameState.getUserMatchDeck().popCards(1));
        gameState.attack(ai, user, callback);

        // AI 2x fatal kill
        user.changeHealth(-100);
        gameState.attack(ai, user, callback);
        user.changeHealth(150);
        gameState.getUserPyramid().confirmRemoval(card);
        gameState.attack(ai, user, callback);

        verify(callback, times(2)).gameOver(eq(true));
        verify(callback, times(2)).gameOver(eq(false));
    }
}