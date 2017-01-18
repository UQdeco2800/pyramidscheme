package uq.deco2800.pyramidscheme.deck;

import org.junit.Assert;
import org.junit.Test;
import uq.deco2800.pyramidscheme.cards.BasicAction;
import uq.deco2800.pyramidscheme.cards.supercards.Card;
import uq.deco2800.pyramidscheme.cards.supercards.CardNotFoundException;
import uq.deco2800.pyramidscheme.cards.supercards.MinionCard;

import java.util.ArrayList;
import java.util.List;

/**
 * Tests for CardList
 * @Author Billy-7
 */
public class CardListTest {

	@Test
	public void containsTest() throws CardNotFoundException {
		CardList cardList = new CardList();
		cardList.addCard(MinionCard.get("King Duck"));
		Assert.assertTrue(cardList.containsCard(MinionCard.get("King Duck")));
	}

	@Test
	public void invalidAddTest() throws CardNotFoundException {
		CardList cardList = new CardList();
		cardList.addCard(null);
		Assert.assertEquals(cardList.getCards(), new ArrayList<Card>());
	}

	@Test
	public void invalidAdd2Test() throws CardNotFoundException {
		CardList cardList = new CardList();
		cardList.addCard(MinionCard.get("King Duck"), -1);
		Assert.assertEquals(cardList.getCards(), new ArrayList<Card>());
	}

	@Test
	public void removeTest() throws CardNotFoundException {
		CardList cardList = new CardList();
		cardList.addCard(MinionCard.get("King Duck"));
		cardList.removeCard(MinionCard.get("King Duck"));
		Assert.assertEquals(0, cardList.getSize());
	}

	@Test
	public void remove2Test() throws CardNotFoundException {
		CardList cardList = new CardList();
		cardList.addCard(MinionCard.get("King Duck"), 3);
		cardList.removeCard(MinionCard.get("King Duck"), 2);
		Assert.assertEquals(1, cardList.getSize());
	}

	@Test
	public void invalidRemoveTest() throws CardNotFoundException {
		CardList cardList = new CardList();
		cardList.addCard(MinionCard.get("King Duck"));
		cardList.removeCard(null);
		Assert.assertEquals(1, cardList.getSize());
	}

	@Test
	public void invalidRemove2Test() throws CardNotFoundException {
		CardList cardList = new CardList();
		cardList.addCard(MinionCard.get("King Duck"));
		cardList.removeCard(MinionCard.get("King Duck"), -1);
		Assert.assertEquals(1, cardList.getSize());
	}

	@Test
	public void invalidRemove3Test() throws CardNotFoundException {
		CardList cardList = new CardList();
		cardList.addCard(MinionCard.get("King Duck"));
		cardList.removeCard(MinionCard.get("Tutanquackum"));
		Assert.assertEquals(1, cardList.getSize());
	}

	@Test
	public void getDistinctCardsTest() throws CardNotFoundException {
		CardList cardList = new CardList();
		cardList.addCard(MinionCard.get("King Duck"), 3);
		cardList.addCard(MinionCard.get("Tutanquackum"), 5);
		cardList.addCard(MinionCard.get("King Penduckling"), 1);
		List<Card> cards = new ArrayList<>();
		cards.add(MinionCard.get("King Duck"));
		cards.add(MinionCard.get("King Penduckling"));
		cards.add(MinionCard.get("Tutanquackum"));
		for (int i = 0; i < 3; i++) {
			Assert.assertTrue(cardList.getDistinctCards().get(i).equals(cards.get(i)));
		}
	}

	@Test
	public void sizeTest() throws CardNotFoundException {
		CardList cardList = new CardList();
		cardList.addCard(MinionCard.get("King Duck"), 3);
		cardList.addCard(MinionCard.get("Tutanquackum"), 5);
		Assert.assertEquals(8, cardList.getSize());
	}

	@Test
	public void distinctSizeTest() throws CardNotFoundException {
		CardList cardList = new CardList();
		cardList.addCard(MinionCard.get("King Duck"), 3);
		cardList.addCard(MinionCard.get("Tutanquackum"), 5);
		Assert.assertEquals(2, cardList.getDistinctSize());
	}

	@Test
	public void filterTest() throws CardNotFoundException {
		CardList cardList = new CardList();
		cardList.addCard(MinionCard.get("King Duck"), 3);
		cardList.addCard(new BasicAction(), 5);
		CardList cardList2 = new CardList();
		cardList2.addCard(MinionCard.get("King Duck"), 3);
		Assert.assertTrue(cardList2.equalTo(cardList.getFiltered("king", "MC")));
	}

	@Test
	public void filter2Test() throws CardNotFoundException {
		CardList cardList = new CardList();
		cardList.addCard(MinionCard.get("King Duck"), 3);
		cardList.addCard(new BasicAction(), 5);
		CardList cardList2 = new CardList();
		cardList2.addCard(MinionCard.get("King Duck"), 3);
		Assert.assertTrue(cardList2.equalTo(cardList.getFiltered("duck", null)));
	}
}
