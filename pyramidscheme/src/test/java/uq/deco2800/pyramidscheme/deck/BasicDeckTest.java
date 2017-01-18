package uq.deco2800.pyramidscheme.deck;

import org.junit.Assert;
import org.junit.Test;
import uq.deco2800.pyramidscheme.cards.BasicMinion;
import uq.deco2800.pyramidscheme.cards.supercards.Card;
import uq.deco2800.pyramidscheme.cards.supercards.NullCard;

import java.util.ArrayList;
import java.util.List;

public class BasicDeckTest {

	public static Deck createDeck() {
		// Create a deck
		CardList list = new CardList();
		Card card = createCard();
		list.addCard(card);
		list.addCard(card);
		list.addCard(card);
		list.addCard(card);
		list.addCard(card);
		list.addCard(card);
		return new Deck(list, "deck name");
	}

	private static Card createCard() {
		Card card = new BasicMinion();
		return card;
	}

	@Test
	public void getCardsTest() {
		// Create a deck
		Deck deck = createDeck();

		// Create a list to check against 
		Card card = createCard();
		List<Card> checkList = new ArrayList<Card>();
		checkList.add(card);
		checkList.add(card);
		checkList.add(card);
		checkList.add(card);
		checkList.add(card);
		checkList.add(card);

		Assert.assertEquals(checkList.toString(), deck.getCards().toString());
	}

//	@Test
//	public void addRemoveCardTest() {
//		// Create a deck and get its initial size
//		Deck deck = createDeck();
//		int startSize = deck.size();
//
//		// Add a card and check the size of the deck incremented
//		deck.addCard(createCard());
//		Assert.assertEquals(startSize + 1, deck.size());
//
//		// Remove a card and check the size of the deck decremented
//	}

	@Test
	public void nullCardTest() {
		// Create a deck and a NullCard to check against
		Deck deck = createDeck();
		NullCard nullCard = new NullCard("NullCard", "/cardImages/nullCard.png");

		// Check getNullCard returns a nullcard
		Assert.assertEquals(nullCard.toString(), Deck.getNullCard().toString());
	}

	@Test
	public void nameTest() {
		Deck deck = createDeck();
		Assert.assertEquals("deck name", deck.getName());
	}

//	@Test
//	public void setNameTest() {
//		Deck deck = createDeck();
//		deck.setName("new name");
//		Assert.assertEquals("new name", deck.getName());
//	}

	@Test
	public void sizeTest() {
		Deck deck = createDeck();
		Assert.assertEquals(6, deck.size());
	}

	@Test
	public void equalsTest() {
		Deck deck1 = createDeck();
		Deck deck2 = createDeck();
		Assert.assertTrue(deck1.equalTo(deck2));
	}

	@Test
	public void init1Test() {
		Deck deck1 = new Deck();
		Deck deck2 = new Deck("New Deck");
		Assert.assertTrue(deck1.equalTo(deck2));
	}

	@Test
	public void init2Test() {
		Deck deck1 = createDeck();
		Deck deck2 = new Deck(deck1);
		Assert.assertTrue(deck1.equalTo(deck2));
	}

	@Test
	public void init3Test() {
		CardList cardList = new CardList(createDeck());
		Deck deck1 = createDeck();
		Deck deck2 = new Deck(cardList, "deck name");
		Assert.assertTrue(deck1.equalTo(deck2));
	}
}
