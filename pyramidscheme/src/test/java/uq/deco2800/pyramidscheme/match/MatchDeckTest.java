package uq.deco2800.pyramidscheme.match;

import org.junit.Assert;
import org.junit.Test;
import uq.deco2800.pyramidscheme.cards.BasicMinion;
import uq.deco2800.pyramidscheme.cards.supercards.Card;
import uq.deco2800.pyramidscheme.cards.supercards.CardNotFoundException;
import uq.deco2800.pyramidscheme.cards.supercards.MinionCard;
import uq.deco2800.pyramidscheme.deck.CardList;
import uq.deco2800.pyramidscheme.deck.Deck;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Max on 8/09/2016.
 */
public class MatchDeckTest {

    MatchCard crazedDuckling;
    MatchCard mummyDuck;

    public MatchDeckTest() throws CardNotFoundException {
        crazedDuckling = new MatchCard(MinionCard.get("Crazed Duckling"), 0, 0);
        mummyDuck = new MatchCard(MinionCard.get("Mummy Duck"), 0, 0);
    }

    @Test
    public void testEmptyGraveyard() {
        MatchDeck deck = new MatchDeck(0, 0);

        Assert.assertTrue(deck.isEmpty());

        // Should return an empty list
        Assert.assertTrue(deck.popCards(1).equals(new ArrayList<>()));

        deck.shuffle();

        Assert.assertTrue(deck.isEmpty());
    }

    @Test
    public void testPushGraveyard() {
        MatchDeck deck = new MatchDeck(0, 0);

        deck.pushCard(mummyDuck);
        deck.pushCard(crazedDuckling);

        // Expect the first card at the top of the deck to be the crazedDuckling
        Assert.assertTrue(deck.iterator().next().equals(crazedDuckling));
        Assert.assertTrue(deck.size() == 2);

        deck.shuffle();

        Assert.assertTrue(deck.size() == 2);
    }

    @Test
    public void testDeckConstructor() {
        CardList list = new CardList();
        Card card = new BasicMinion();
        list.addCard(card);
        list.addCard(card);
        list.addCard(card);
        Deck deck = new Deck(list, "Deck Name");

        MatchDeck matchDeck = new MatchDeck(0, 0, deck);

        Assert.assertEquals(matchDeck.size(), 3);
        Assert.assertEquals(matchDeck.popCards(1).get(0).getCard(), card);
    }

    @Test
    public void testPushOrder() throws CardNotFoundException {
        // Last card on the list is the last card to be pushed onto the deck
        // Therefore, the top card of the deck is the last card of the list
        MatchCard basicMinion = new MatchCard(new BasicMinion(), 0, 0);
        MatchCard duckzilla = new MatchCard(MinionCard.get("Duckzilla"), 0, 0);

        List<MatchCard> cards = new ArrayList<>();
        cards.add(basicMinion);
        cards.add(duckzilla);

        MatchDeck matchDeck = new MatchDeck(0, 0);
        matchDeck.pushCards(cards);

        Assert.assertEquals(matchDeck.popCards(1).get(0), duckzilla);
    }
}
