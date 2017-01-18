package uq.deco2800.pyramidscheme.cards;

import org.junit.Assert;
import org.junit.Test;
import uq.deco2800.pyramidscheme.cards.supercards.Card;
import uq.deco2800.pyramidscheme.cards.supercards.CardComparator;
import uq.deco2800.pyramidscheme.cards.supercards.CardNotFoundException;
import uq.deco2800.pyramidscheme.cards.supercards.MinionCard;

import java.util.ArrayList;
import java.util.List;

/**
 * Tests for CardComparator 
 * @author Billy-7
 */
public class CardCompareTest {

	private List<Card> createCards() throws CardNotFoundException {
		List<Card> cards = new ArrayList<>();
		cards.add(MinionCard.get("King Duck"));
		cards.add(MinionCard.get("Tutanquackum"));
		cards.add(MinionCard.get("Sir Galaduck"));
		return cards;
	}

	@Test
	public void smallerTest() throws CardNotFoundException {
		// test KingDuck smaller then Tutanquackum
		CardComparator comparator = new CardComparator();
		List<Card> cards = createCards();
		Assert.assertTrue(0 > comparator.compare(cards.get(0), cards.get(1)));
	}

	@Test
	public void largerTest() throws CardNotFoundException {
		// test KingDuck greater then Sir Gala
		CardComparator comparator = new CardComparator();
		List<Card> cards = createCards();
		Assert.assertTrue(0 < comparator.compare(cards.get(2), cards.get(0)));
	}

	@Test
	public void equalTest() throws CardNotFoundException {
		// test KingDuck equal to itself
		CardComparator comparator = new CardComparator();
		List<Card> cards = createCards();
		Assert.assertEquals(0, comparator.compare(cards.get(0), cards.get(0)));
	}

	@Test
	public void sortTest() throws CardNotFoundException {
		List<Card> cards = createCards();
		cards.sort(new CardComparator());
		Assert.assertTrue(cards.get(0).equals(MinionCard.get("King Duck")));
		Assert.assertTrue(cards.get(1).equals(MinionCard.get("Sir Galaduck")));
		Assert.assertTrue(cards.get(2).equals(MinionCard.get("Tutanquackum")));
	}
}
