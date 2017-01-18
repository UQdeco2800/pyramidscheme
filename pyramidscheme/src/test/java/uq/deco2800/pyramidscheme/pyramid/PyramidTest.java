package uq.deco2800.pyramidscheme.pyramid;

import org.junit.Assert;
import org.junit.Test;
import uq.deco2800.pyramidscheme.cards.BasicMinion;
import uq.deco2800.pyramidscheme.cards.supercards.CardNotFoundException;
import uq.deco2800.pyramidscheme.cards.supercards.MinionCard;
import uq.deco2800.pyramidscheme.match.MatchCard;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 * @author G. Louis Nieuwoudt
 *         Tests for evaluating if the Pyramid Class is acting the
 *         way it was intended for.
 */
public class PyramidTest {

    /**
     * Creates a default Pyramid to be used throughout the tests.
     *
     * @return A pyramid at location 0,0 and of type TRIANGLE
     */
    public Pyramid createDefaultPyramid() {
        return new Pyramid(0,0,PyramidType.TRIANGLE,true);
    }


    /**
     * Method to generate a list of match cards alternating between two types of cards
     * (ie. BasicMinion and Tutanquackum). This list of cards are used throughout in
     * some test cases.
     *
     * @param size of the list of cards wanted
     *
     * @return a list of MatchCards
     */
    // Method for generating a list of MatchCards
    public List<MatchCard> generateDeck(int size) throws CardNotFoundException {

        List<MatchCard> cardList = new ArrayList<MatchCard>();

        // Generate a deck of n cards
        for (int i = 0; i < size; i++) {
            if (i % 2 == 0) {
                // Every even position should be a Basic Minion
                cardList.add(new MatchCard(new BasicMinion(), 0, 0));
            } else {
                // Every odd position should be a Tutanquackum
                cardList.add(new MatchCard(MinionCard.get("Tutanquackum"), 0, 0));
            }
        }
        return cardList;
    }

    /**
     * A test to determine if the pyramid is being created properly
     * and that loading cards to the pyramid works properly
     */
    @Test(timeout = 5000)
    public void testGetters() throws CardNotFoundException {
        Pyramid pyramid = createDefaultPyramid();

        // Test pyramid Empty
        Assert.assertTrue(pyramid.isEmpty());
        pyramid.loadCards(generateDeck(6));
        Assert.assertFalse(pyramid.isEmpty());

    }

    /**
     * Throws an IndexOutOfBoundsException if the pyramid is
     * arranged with the wrong amount of cards loaded.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    // Test too many cards to arrange
    public void badArrangePyramidTest() throws CardNotFoundException {
        Pyramid badPyramid = createDefaultPyramid();
        badPyramid.loadCards(generateDeck(7));
        badPyramid.arrangePyramid();
    }


    /**
     * Evaluates that the addCard methods behaves correctly.
     */
    @Test(timeout = 5000)
    public void arrangePyramidTest() {
        Pyramid pyramid = createDefaultPyramid();


        //Check that the cards all have the correct x and y
        for (Iterator iterator = pyramid.iterator(); iterator.hasNext(); ) {
            MatchCard card = (MatchCard) iterator.next();
            if (card.getY() != 0 || card.getX() != 0) {
                Assert.fail("Cards were not in the right load position");
            }
        }

        // Arrange the pyramid in a triangle
        pyramid.arrangePyramid();

        //Check that the cards have been moved from their initial position
        for (Iterator iterator = pyramid.iterator(); iterator.hasNext(); ) {
            MatchCard card = (MatchCard) iterator.next();
            if (card.getY() == 0 || card.getX() == 0) {
                Assert.fail("Failed to arrange cards");
            }
        }
    }

    /**
     * Test that loadCards works properly for the max cards, which happen to be 6
     * currently.
     */
    @Test(timeout = 5000)
    public void loadCardsTest() throws CardNotFoundException {
        Pyramid pyramid = new Pyramid(0, 0, PyramidType.TRIANGLE, true);

        // Generate a deck of 6 cards (which happens to be the max deck size
        // currently
        pyramid.loadCards(generateDeck(6));

        Assert.assertEquals(pyramid.size(), 6);

        if (pyramid.size() > 6) {
            Assert.fail("Pyramid size is wrong (it's too large!");
        }
        if (pyramid.size() < 6) {
            Assert.fail("Pyramid size is wrong (it's too small!");
        }

    }

    /**
     * Evaluate if the shuffle function is properly shuffling the cards around
     * in the pyramid and correctly changed the order.
     */
    @Test(timeout = 5000)
    public void shufflePyramidTest() throws CardNotFoundException {
        Pyramid pyramid = new Pyramid(0, 0, PyramidType.TRIANGLE, true);
        ArrayList<Integer> beforeShuffle = new ArrayList<>(100);
        ArrayList<Integer> afterShuffle = new ArrayList<>(100);

        // Generate a deck of 6 cards
        pyramid.loadCards(generateDeck(6));
        pyramid.arrangePyramid();

        for (int card = 0; card < pyramid.size(); card++) {
            beforeShuffle.add(pyramid.getPyramidCards().get(card).getPyramidCardLocation().getX());
        }

        //Shuffle the pyramid 100 times to be sure it doesn't shuffle to the same order
        for (int i = 0; i < 100; i++) {
            pyramid.shufflePyramid();

            for (int card = 0; card < pyramid.size(); card++) {
                afterShuffle.add(pyramid.getPyramidCards().get(card).getPyramidCardLocation().getX());
            }

            // If the card order has change, break because the shuffle worked
            if (!beforeShuffle.equals(afterShuffle)) {
                break;
            }
            afterShuffle.clear();
        }
        // Test the order
        Assert.assertNotEquals(beforeShuffle, afterShuffle);
    }

    /**
     * Test that removing cards from the pyramid works properly for a max deck size
     * pyramid of 6.
     */
    @Test(timeout = 5000)
    public void pyramidInteraction() throws CardNotFoundException {
        int x, y;

        Pyramid pyramid = new Pyramid(0, 0, PyramidType.TRIANGLE, true);

        // Generate a deck of 6 cards (which happens to be the max deck size
        // currently
        pyramid.loadCards(generateDeck(6));
        pyramid.arrangePyramid();

        //Should be a minion
        MatchCard minionCard = pyramid.iterator().next();

        //Should be a king
        MatchCard kingCard = pyramid.iterator().next();


        // Verifying if getMatchCard returns the correct card
        Assert.assertEquals(pyramid.getMatchCardAt(minionCard.getX(), minionCard.getY()).get(), minionCard);
        Assert.assertEquals(pyramid.getMatchCardAt(kingCard.getX(), kingCard.getY()).get(), kingCard);


        x = minionCard.getX();
        y = minionCard.getY();

        // Test if the correct card was yanked and if it's yanked again
        // that it returns null as it's meant to
        MatchCard yankedCard = pyramid.yankMatchCardAt(x, y).get();
        if (!yankedCard.equals(minionCard)) {
            Assert.fail("Yank returned the wrong MatchCard!");
        }
        if (pyramid.yankMatchCardAt(x, y).isPresent()) {
            Assert.fail("Yank should've returned null since the previous call removed the MatchCard!");
        }

        //Test size()
        Assert.assertEquals(pyramid.size(), 5);
        //Place card back
        pyramid.returnYankedCard(yankedCard);

        //Try place card again
        pyramid.returnYankedCard(yankedCard);

        //Test size()
        Assert.assertEquals(pyramid.size(), 6);

        // Test click not on pyramid
        Assert.assertEquals(pyramid.getMatchCardAt(-1,-1), Optional.empty());
    }

    /**
     * Test whether a card within a default pyramid functions properly
     * according to the rules of the game for when they're meant to
     * be 'playable' and when they're 'not playable'.
     */
    @Test(timeout = 5000)
    public void isPlayableTest() {
        Pyramid pyramid = createDefaultPyramid();
        ArrayList<MatchCard> cards = new ArrayList<>();
        cards.add(new MatchCard(new BasicMinion(),65,90));
        cards.add(new MatchCard(new BasicMinion(),65,0));

        pyramid.loadCards(cards);
        pyramid.getPyramidCards().get(0).setZ(1);

        //Test top edge touching
        Assert.assertTrue(pyramid.isPlayable(pyramid.getPyramidCards().get(0)));

        //Test left edge touching
        pyramid.getPyramidCards().get(1).setX(0);
        pyramid.getPyramidCards().get(1).setY(90);
        Assert.assertTrue(pyramid.isPlayable(pyramid.getPyramidCards().get(0)));

        //Test right edge touching
        pyramid.getPyramidCards().get(1).setX(130);
        pyramid.getPyramidCards().get(1).setY(90);
        Assert.assertTrue(pyramid.isPlayable(pyramid.getPyramidCards().get(0)));

        //Test bottom edge touching
        pyramid.getPyramidCards().get(1).setX(65);
        pyramid.getPyramidCards().get(1).setY(180);
        Assert.assertTrue(pyramid.isPlayable(pyramid.getPyramidCards().get(0)));

        //Test top left overlapping
        pyramid.getPyramidCards().get(1).setX(1);
        pyramid.getPyramidCards().get(1).setY(1);
        Assert.assertFalse(pyramid.isPlayable(pyramid.getPyramidCards().get(0)));

        //Test top right overlapping
        pyramid.getPyramidCards().get(1).setX(129);
        Assert.assertFalse(pyramid.isPlayable(pyramid.getPyramidCards().get(0)));

        //Test bottom right overlapping
        pyramid.getPyramidCards().get(1).setY(89);
        Assert.assertFalse(pyramid.isPlayable(pyramid.getPyramidCards().get(0)));

        //Test bottom left overlapping
        pyramid.getPyramidCards().get(1).setX(1);
        Assert.assertFalse(pyramid.isPlayable(pyramid.getPyramidCards().get(0)));
    }

    /** Check that the equals and hashCode methods are correct. */
    @Test(timeout = 5000)
    public void testEquals() {


        Pyramid p1 = new Pyramid(0,0,PyramidType.TRIANGLE,true);
        Pyramid p2 = new Pyramid(0,0,PyramidType.TRIANGLE,true);
        Object p3 = new Pyramid(0,0,PyramidType.TRIANGLE,true);
        Pyramid p4 = new Pyramid(0,0,PyramidType.RECTANGLE,true);
        Pyramid p5 = new Pyramid(0,0,PyramidType.SQUARE,true);
        String string = new String("random");

        // equals cases
        Assert.assertEquals("Equals should return true", p1, p2);
        Assert.assertEquals("Equal objects should have equal hash codes",
                p1.hashCode(), p2.hashCode());
        Assert.assertEquals("Equals should return true", p1, p3);
        Assert.assertEquals("Equals should return true for the string test",
                p1.toString(), "You're currently using the Pyramid Shape: TRIANGLE");

        // unequal cases
        Assert.assertFalse("Equals should return false", p1.equals(p4));
        Assert.assertFalse("Equals should return false", p1.equals(p5));
        Assert.assertFalse("Equals should return false", p1.equals(string));
    }


}
