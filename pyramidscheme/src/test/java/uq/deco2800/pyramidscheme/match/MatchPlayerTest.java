package uq.deco2800.pyramidscheme.match;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import uq.deco2800.pyramidscheme.cards.supercards.CardNotFoundException;
import uq.deco2800.pyramidscheme.cards.supercards.MinionCard;
import uq.deco2800.pyramidscheme.deck.Deck;
import uq.deco2800.pyramidscheme.game.GameManager;
import uq.deco2800.pyramidscheme.player.User;

/**
 * Created by Max on 8/09/2016.
 */
public class MatchPlayerTest {

    User user = new User("User", new Deck());
    MatchPlayer player = new MatchPlayer(user, true);
    @Before
    public void before() {
    	GameManager.getStatisticsTracking().createGuestUser();
    }

    @Test
    public void testGetters() {
        Assert.assertTrue(player.getPlayer() == user);
        Assert.assertTrue(player.getHealth() == player.getPlayer().getMaxHealth());
    }

    @Test
    public void testAttack() throws CardNotFoundException {
        int before = player.getHealth();

        MatchCard mummyDuck = new MatchCard(MinionCard.get("Mummy Duck"), 0, 0);

        int expected = before - mummyDuck.getAttack();

        player.attackedBy(mummyDuck);

        Assert.assertTrue(player.getHealth() == expected);
    }
}
