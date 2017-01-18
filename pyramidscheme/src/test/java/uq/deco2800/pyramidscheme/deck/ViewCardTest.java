package uq.deco2800.pyramidscheme.deck;

import org.junit.Assert;
import org.junit.Test;
import uq.deco2800.pyramidscheme.cards.supercards.Card;
import uq.deco2800.pyramidscheme.cards.supercards.CardNotFoundException;
import uq.deco2800.pyramidscheme.cards.supercards.MinionCard;

/**
 * Tests for ViewCard
 * @author Billy-7
 */
public class ViewCardTest {

	@Test
	public void getCardTest() throws CardNotFoundException {
		Card card = MinionCard.get("King Duck");
		ViewCard viewCard = new ViewCard(card, 100, 100);
		Assert.assertTrue(card.equals(viewCard.getCard()));
	}

	@Test
	public void getScaleTest() throws CardNotFoundException {
		Card card = MinionCard.get("King Duck");
		ViewCard viewCard = new ViewCard(card, 100, 100, 2.1);
		Assert.assertEquals(2.1, viewCard.getScale(), 1e-5);
	}

	@Test
	public void getHeightTest() throws CardNotFoundException {
		Card card = MinionCard.get("King Duck");
		ViewCard viewCard = new ViewCard(card, 100, 100, 2.1);
		Assert.assertEquals(Card.getCardHeight() * 2.1, viewCard.getHeight(), 1e-5);
	}

	@Test
	public void getWidthTest() throws CardNotFoundException {
		Card card = MinionCard.get("King Duck");
		ViewCard viewCard = new ViewCard(card, 100, 100, 2.1);
		Assert.assertEquals(Card.getCardWidth() * 2.1, viewCard.getWidth(), 1e-5);
	}

	@Test
	public void click1Test() throws CardNotFoundException {
		Card card = MinionCard.get("King Duck");
		ViewCard viewCard = new ViewCard(card, 100, 100, 2.1);
		Assert.assertTrue(viewCard.containsClick(100, 100));
	}

	@Test
	public void click2Test() throws CardNotFoundException {
		Card card = MinionCard.get("King Duck");
		ViewCard viewCard = new ViewCard(card, 100, 100, 2.1);
		Assert.assertTrue(viewCard.containsClick(
				100 + (int)(viewCard.getScale() * Card.getCardWidth()),
				100 + (int)(viewCard.getScale() * Card.getCardHeight())));
	}

	@Test
	public void click3Test() throws CardNotFoundException {
		Card card = MinionCard.get("King Duck");
		ViewCard viewCard = new ViewCard(card, 100, 100, 2.1);
		Assert.assertFalse(viewCard.containsClick(99, 100));
	}

	@Test
	public void click4Test() throws CardNotFoundException {
		Card card = MinionCard.get("King Duck");
		ViewCard viewCard = new ViewCard(card, 100, 100, 2.1);
		Assert.assertFalse(viewCard.containsClick(100, 99));
	}

	@Test
	public void click5Test() throws CardNotFoundException {
		Card card = MinionCard.get("King Duck");
		ViewCard viewCard = new ViewCard(card, 100, 100, 2.1);
		Assert.assertFalse(viewCard.containsClick(101 + (int)viewCard.getWidth(), 100));
	}

	@Test
	public void click6Test() throws CardNotFoundException {
		Card card = MinionCard.get("King Duck");
		ViewCard viewCard = new ViewCard(card, 100, 100, 2.1);
		Assert.assertFalse(viewCard.containsClick(100, 101 + (int)viewCard.getHeight()));
	}
}
