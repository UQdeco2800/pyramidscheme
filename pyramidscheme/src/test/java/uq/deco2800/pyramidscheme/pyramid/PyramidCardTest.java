package uq.deco2800.pyramidscheme.pyramid;

import org.junit.Assert;
import org.junit.Test;
import uq.deco2800.pyramidscheme.cards.BasicMinion;
import uq.deco2800.pyramidscheme.cards.supercards.CardNotFoundException;
import uq.deco2800.pyramidscheme.cards.supercards.MinionCard;
import uq.deco2800.pyramidscheme.match.MatchCard;

import java.util.Optional;

/**
 * @author G. Louis Nieuwoudt
 *         Tests for evaluating if the PyramidCard Class is acting the
 *         way it was intended for.
 */
public class PyramidCardTest {

    MatchCard mummyDuck;
    PyramidCard card;

    public PyramidCardTest() throws CardNotFoundException {
        mummyDuck = new MatchCard(MinionCard.get("Mummy Duck"), 0, 0);
        card = new PyramidCard(mummyDuck);
    }

    @Test(timeout = 5000)
    public void testGetters() {
        // Test z index
        Assert.assertEquals(0, card.getPyramidCardLocation().getZ());

        // Test getCard
        Assert.assertEquals(card.getCard(),Optional.ofNullable(mummyDuck));

        // Test getCard when there is no card
        Optional<MatchCard> yankedCard = card.yankCard();
        Assert.assertEquals(card.getCard(),Optional.empty());
        card.setCard(yankedCard.get());

        // Check if the x, y values are set to the values of the MatchCard
        Assert.assertEquals(card.getCard().get().getX(), mummyDuck.getX());
        Assert.assertEquals(card.getCard().get().getY(), mummyDuck.getY());
    }

    // Get the card, pull it out, and put it back in again
    @Test(timeout = 5000)
    public void checkSetCard() {
        Assert.assertTrue(card.getCard().isPresent());

        Optional<MatchCard> yankedCard = card.yankCard();

        Assert.assertTrue(yankedCard.isPresent());

        Assert.assertFalse(card.getCard().isPresent());

        card.setCard(yankedCard.get());

        Assert.assertTrue(card.getCard().isPresent());
    }

    @Test(timeout = 5000)
    public void testEquals() {
        // Reference equality
        Assert.assertTrue(card.equals(card));

        // Non-compatible class
        Assert.assertFalse(card.equals(mummyDuck));

        // Comparison on null
        Assert.assertFalse(card.equals(null));

        // Different card
        PyramidCard card1 = new PyramidCard(new MatchCard(new BasicMinion(),0,0));
        Assert.assertFalse(card.equals(card1));
        Assert.assertNotEquals(card.hashCode(),card1.hashCode());


        // Different location
        PyramidCard card2 = new PyramidCard(mummyDuck);
        card2.setX(1);
        Assert.assertFalse(card.equals(card2));
        Assert.assertNotEquals(card.hashCode(),card2.hashCode());

        // Test different hidden
        card2.setX(0);
        card2.setFaceDown(false);
        Assert.assertFalse(card.equals(card2));
        Assert.assertNotEquals(card.hashCode(),card2.hashCode());

        // Test same card
        card2.setFaceDown(true);
        Assert.assertTrue(card.equals(card2));
        Assert.assertEquals(card.hashCode(),card2.hashCode());
    }

    @Test(timeout = 5000)
    public void testContainsCoords() {
        // Card inside at 0, 0
        Assert.assertTrue(card.containsCoords(1, 1));

        Optional<MatchCard> yanked = card.yankCard();

        // Card no longer inside
        Assert.assertFalse(card.containsCoords(1, 1));

        // Put our card back
        card.setCard(yanked.get());

        Assert.assertTrue(card.containsCoords(1, 1));
    }

    @Test(timeout = 5000)
    public void testPositionPipeThrough() {
        card.setX(5);
        card.setY(5);
        card.setZ(5);

        Assert.assertEquals(card.getPyramidCardLocation().getX(), 5);
        Assert.assertEquals(card.getPyramidCardLocation().getY(), 5);
        Assert.assertEquals(card.getPyramidCardLocation().getZ(), 5);
    }

    @Test(timeout = 5000)
    public void testHidden() {
        // Test default hidden state
        Assert.assertEquals(card.getFaceDown(), true);

        // Test setFaceDown false
        card.setFaceDown(false);
        Assert.assertEquals(card.getFaceDown(), false);

        // Test setFaceDown true
        card.setFaceDown(true);
        Assert.assertEquals(card.getFaceDown(), true);
    }
}
