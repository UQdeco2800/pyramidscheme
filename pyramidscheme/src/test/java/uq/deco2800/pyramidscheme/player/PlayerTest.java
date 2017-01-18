package uq.deco2800.pyramidscheme.player;

import org.junit.Assert;
import org.junit.Test;
import uq.deco2800.pyramidscheme.cards.supercards.Card;
import uq.deco2800.pyramidscheme.cards.supercards.CardNotFoundException;
import uq.deco2800.pyramidscheme.cards.supercards.MinionCard;
import uq.deco2800.pyramidscheme.deck.BasicDeckTest;
import uq.deco2800.pyramidscheme.deck.CardList;
import uq.deco2800.pyramidscheme.deck.Deck;

import java.util.ArrayList;
import java.util.List;

/**
 * Basic Player Test
 *
 * @author Billy-7
 */
public class PlayerTest {

	private User createUser() {
		Deck deck = BasicDeckTest.createDeck();
		CardList list = new CardList();
		return new User("user name", deck);
	}

	private User createUser2() throws CardNotFoundException {
		Deck deck = BasicDeckTest.createDeck();
		CardList list = new CardList();
		list.addCard(MinionCard.get("Tutanquackum"), 3);
		return new User("user name", Player.createDecks(deck, Player.DECK_SLOTS), list);
	}

	@Test
	public void nameTest() {
		Deck deck = BasicDeckTest.createDeck();
		User user = new User("user name", deck);
		Assert.assertEquals("user name", user.getName());
	}

	@Test
	public void healthTest() {
		User user = createUser();
		Assert.assertEquals(user.getMaxHealth(), Player.MAX_HEALTH);
	}
	
	@Test
	public void currencyTest() {
		User user = createUser();
		Assert.assertEquals(100, user.getCurrency());
		user.setCurrency(500);
		Assert.assertEquals(500, user.getCurrency());
		
	}
	
	@Test
	public void modifyCurrencyTest() {
		User user = createUser();
		Assert.assertEquals(100, user.getCurrency());
		user.modifyCurrency(200);
		Assert.assertEquals(300, user.getCurrency());
		user.modifyCurrency(-1000);
		Assert.assertEquals(0, user.getCurrency());
	}

	@Test
	public void initDeckTest() {
		Deck deck = BasicDeckTest.createDeck();
		User user = new User("user name", deck);
		Assert.assertTrue(deck.equalTo(user.getDeck()));
	}

	@Test
	public void allDeckTest() {
		Deck deck = BasicDeckTest.createDeck();
		User user = new User("user name", deck);
		for (int i = 0; i < Player.DECK_SLOTS; i++) {
			Assert.assertTrue(deck.equalTo(user.getDeck(i)));
		}
		// idx < 0 therefore getDeck should return NULL
		Assert.assertNull(user.getDeck(-1));
		// idx !< 4 therefore getDeck should return NULL
		Assert.assertNull(user.getDeck(4));
		// idx > 4 therefore getDeck should return NULL
		Assert.assertNull(user.getDeck(5));
	}

	@Test
	public void initSlotTest() {
		User user = createUser();
		Assert.assertEquals(0, user.getDeckSlot());
	}

	@Test
	public void setSlotTest() {
		int value = Player.DECK_SLOTS - 1;
		User user = createUser();
		user.setDeckSlot(value);
		Assert.assertEquals(value, user.getDeckSlot());
	}

	@Test
	public void setMainDeckTest() {
		User user = createUser();
		Deck deck = BasicDeckTest.createDeck();
		user.setDeck(deck);
		Assert.assertTrue(deck.equalTo(user.getDeck()));
	}

	@Test
	public void setOtherDeckTest1() {
		int value = Player.DECK_SLOTS - 1;
		User user = createUser();
		Deck deck = BasicDeckTest.createDeck();
		user.setDeck(value, deck);
		Assert.assertTrue(deck.equalTo(user.getDeck(value)));
	}
	
	@Test
	public void setOtherDeckTest2() {
		int value = 0;
		User user = createUser();
		Deck deck = BasicDeckTest.createDeck();
		user.setDeck(value, deck);
		Assert.assertTrue(deck.equalTo(user.getDeck(value)));
	}

	@Test
	public void cardAmountTest() throws CardNotFoundException {
		User user = createUser2();
		Card card = MinionCard.get("King Duck");
		Assert.assertEquals(0, user.cardAmount(card));
	}

	@Test
	public void unlockCardTest() throws CardNotFoundException {
		User user = createUser2();
		Card card = MinionCard.get("King Duck");
		user.unlockCard(card, 2);
		Assert.assertEquals(2, user.cardAmount(card));
	}

	@Test
	public void removeCardTest() throws CardNotFoundException {
		User user = createUser2();
		Card card = MinionCard.get("Tutanquackum");
		user.removeCard(card, 1);
		Assert.assertEquals(2, user.cardAmount(card));
	}

	@Test
	public void hasCardTest() throws CardNotFoundException {
		User user = createUser2();
		Card card = MinionCard.get("Tutanquackum");
		Assert.assertTrue(user.hasCard(card));
	}

	@Test
	public void invalidGetDeckTest() {
		User user = createUser();
		Assert.assertNull(user.getDeck(Player.DECK_SLOTS + 1));
	}

	@Test
	public void invalidSetDeckTest() {
		User user1 = createUser();
		Deck deck1 = BasicDeckTest.createDeck();
		User user2 = createUser();
		Deck deck2 = BasicDeckTest.createDeck();
		User user3 = createUser();
		Deck deck3 = BasicDeckTest.createDeck();
		int val = -1;
		user1.setDeck(val, deck1);
		Assert.assertFalse(deck1.equalTo(user1.getDeck(val)));
		val = Player.DECK_SLOTS;
		user1.setDeck(val, deck2);
		Assert.assertFalse(deck2.equalTo(user2.getDeck(val)));
		val = Player.DECK_SLOTS + 1;
		user1.setDeck(val, deck3);
		Assert.assertFalse(deck3.equalTo(user3.getDeck(val)));
	}

	@Test
	public void invalidSetSlotTest() {
		User user = createUser();
		int before = user.getDeckSlot();
		user.setDeckSlot(Player.DECK_SLOTS + 1);
		Assert.assertEquals(before, user.getDeckSlot());
		
		user.setDeckSlot(Player.DECK_SLOTS - 5);
		Assert.assertEquals(before, user.getDeckSlot());

		user.setDeckSlot(Player.DECK_SLOTS);
		Assert.assertEquals(before, user.getDeckSlot());

	}
}
