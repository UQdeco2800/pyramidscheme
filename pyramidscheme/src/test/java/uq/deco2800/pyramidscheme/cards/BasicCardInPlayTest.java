package uq.deco2800.pyramidscheme.cards;

import org.junit.Assert;
import org.junit.Test;


import uq.deco2800.pyramidscheme.cards.supercards.Card;
import uq.deco2800.pyramidscheme.cards.supercards.MinionCard;
import uq.deco2800.pyramidscheme.match.MatchCard;

public class BasicCardInPlayTest {

	private MatchCard createCardInPlay(int x, int y) {
		MatchCard cardIP = new MatchCard(createCard(), x, y);
		return cardIP;
	}

	private Card createCard() {
		Card card = new BasicMinion();
		return card;
	}

	@Test
	public void createCardInPlayTest() {
		// Create a Card and CardInPlay using it
		// Can't use createCardInPlay because that would use a different Card object
		Card card = createCard();
		int x = 10;
		int y = 20;
		MatchCard cardIP = new MatchCard(card, x, y);

		// Check you can retrieve the Card from the CardInPlay
		Assert.assertEquals(card, cardIP.getCard());

		// Check the toString() method returns the correct string
		String expected = "Basic Minion[3, 2] at (" + x + ", " + y + ")";
		String actual = cardIP.toString();
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void checkSetCoords() {
		// Create a CardInPlay
		int x = 10;
		int y = 20;
		MatchCard cardIP = createCardInPlay(x, y);

		// Check the coord setting methods work correctly
		int newX = 30;
		cardIP.setX(newX);
		Assert.assertEquals(newX, cardIP.getX());

		int newY = 40;
		cardIP.setY(newY);
		Assert.assertEquals(newY, cardIP.getY());
	}

	@Test
	public void checkContainsCoord() {
		// Create a CardInPlay
		int x = 10;
		int y = 20;
		MatchCard cardIP = createCardInPlay(x, y);

		// Check the containsCoord() method works correctly
		// Check point inside the card - should return true

		// Check point to the left of the card - should return false

		// Check point above the card - should return false

		// Check point to the right of the card - should return false
		int checkX = x + Card.getCardWidth() + 1;

		// Check point below the card - should return false
		int checkY = y + Card.getCardHeight() + 1;
	}
}