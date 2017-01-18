package uq.deco2800.pyramidscheme.animations;

import javafx.animation.Animation;
import org.junit.Before;
import org.junit.Test;
import uq.deco2800.pyramidscheme.board.RecTile;
import uq.deco2800.pyramidscheme.cards.BasicMinion;
import uq.deco2800.pyramidscheme.deck.Deck;
import uq.deco2800.pyramidscheme.game.GameState;
import uq.deco2800.pyramidscheme.match.Match;
import uq.deco2800.pyramidscheme.match.MatchCard;
import uq.deco2800.pyramidscheme.player.AI;
import uq.deco2800.pyramidscheme.player.User;

import static org.mockito.Mockito.*;

/**
 * Created by nick on 23/10/16.
 */
public class AnimationsTest {

    UserAnimations userAnimations;
    Match match;
    GameState gameState;

    AnimationCallback callback;

    @Before
    public void setup() {
        // Mock callback
        callback = mock(AnimationCallback.class);

        User user = new User("User", new Deck());
        AI ai = new AI("AI", new Deck());
        //create a fake match
        match = new Match(user, ai);
        gameState = match.gameState();
        userAnimations = new UserAnimations(gameState, callback);
    }
    @Test
    public void testAddToAnimationQueue() {
        Animation animationToBePlayed = mock(Animation.class);
        Animation animationNotPlayed = mock(Animation.class);

        // Add items that should be played sequentially
        userAnimations.addToAnimationQueue(animationToBePlayed, Animations.AI_PLAYING);
        userAnimations.addToAnimationQueue(animationToBePlayed, Animations.GRAVEYARD);
        userAnimations.addToAnimationQueue(animationToBePlayed, Animations.AI_GRINDER);

        // Add another to the other queues that shouldn't be played
        userAnimations.addToAnimationQueue(animationNotPlayed, Animations.AI_PLAYING);
        userAnimations.addToAnimationQueue(animationNotPlayed, Animations.AI_REFILLING);
        userAnimations.addToAnimationQueue(animationNotPlayed, Animations.USER_REFILLING);

        // verify it played
        verify(animationToBePlayed, times(3)).play();
        // verify the queued ddin't play
        verify(animationNotPlayed, times(0)).play();
    }

    @Test
    public void testGraveyard() {
        MatchCard card = new MatchCard((new BasicMinion()), 0, 0);
        RecTile tile = gameState.getBoard().getTilesOf(match.getUser()).get(0);
        tile.setContents(card);
        card.setIsToDie(true);
    }
}
