package uq.deco2800.pyramidscheme.deck;

import org.junit.Assert;
import org.junit.Test;
import uq.deco2800.pyramidscheme.cards.supercards.CardNotFoundException;
import uq.deco2800.pyramidscheme.cards.supercards.MinionCard;

import java.util.ArrayList;
import java.util.List;

/**
 * Tests for ViewCardList
 * @author Billy-7
 */
public class ViewCardListTest {

	@Test
	@SuppressWarnings("unchecked")
	public void getCardsTest() throws CardNotFoundException {
		ViewCardList list = new ViewCardList();
		ViewCard card = new ViewCard(MinionCard.get("King Duck"), 100, 100);
		list.addCard(card);
		List array = new ArrayList<ViewCard>();
		array.add(card);
		Assert.assertEquals(list.getCards(), array);
	}

	@Test
	public void getCardTest() throws CardNotFoundException {
		ViewCardList list = new ViewCardList();
		ViewCard card = new ViewCard(MinionCard.get("King Duck"), 100, 100);
		list.addCard(card);
		Assert.assertEquals(list.getCard(0), card);
	}

	@Test
	public void addInvalidCardTest() throws CardNotFoundException {
		ViewCardList list = new ViewCardList();
		ViewCard card = new ViewCard(MinionCard.get("King Duck"), 100, 100);
		list.addCard(card);
		list.addCard(card);
		list.addCard(card);
		list.addCard(card);
		list.addCard(card);
		list.addCard(card);
		list.addCard(card);
		Assert.assertEquals(6, list.getCards().size());
	}

	@Test
	public void addCardTest() throws CardNotFoundException {
		ViewCardList list = new ViewCardList();
		list.addCard(MinionCard.get("King Duck"), 100, 100);
		Assert.assertTrue(list.getCard(0).getCard().equals(MinionCard.get("King Duck")));
	}

	@Test
	public void removeCardTest() throws CardNotFoundException {
		ViewCardList list = new ViewCardList();
		ViewCard card = new ViewCard(MinionCard.get("King Duck"), 100, 100);
		list.addCard(card);
		list.removeCard(0);
		Assert.assertEquals(0, list.getCards().size());
	}

	@Test
	public void removeAllCardsTest() throws CardNotFoundException {
		ViewCardList list = new ViewCardList();
		ViewCard card = new ViewCard(MinionCard.get("King Duck"), 100, 100);
		list.addCard(card);
		list.removeAllCards();
		Assert.assertEquals(list.getCards(), new ArrayList<ViewCard>());
	}

	@Test
	public void getClickedTest() throws CardNotFoundException {
		ViewCardList list = new ViewCardList();
		ViewCard card = new ViewCard(MinionCard.get("King Duck"), 100, 100);
		list.addCard(card);
		Assert.assertEquals(card, list.getClickedCard(100, 100));
	}

	@Test
	public void getClickedNullTest() throws CardNotFoundException {
		ViewCardList list = new ViewCardList();
		ViewCard card = new ViewCard(MinionCard.get("King Duck"), 100, 100);
		list.addCard(card);
		Assert.assertNull(list.getClickedCard(99, 99));
	}
}
