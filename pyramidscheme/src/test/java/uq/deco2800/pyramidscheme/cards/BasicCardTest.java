package uq.deco2800.pyramidscheme.cards;

import org.junit.Assert;
import org.junit.Test;

import uq.deco2800.pyramidscheme.cards.supercards.Card;
import uq.deco2800.pyramidscheme.cards.supercards.MinionCard;

public class BasicCardTest {

	@Test
	public void createCardTest() {
		// Create a card
		Card card = new BasicMinion();

		// Check the name is correct
		String cardName = card.getName();
		Assert.assertEquals("Basic Minion", cardName);

		// Check the toString() method works as expected
		String cardStringExpected = "MC Basic Minion: A3 D2 R1 Dust";
		String cardStringActual = card.toString();
		Assert.assertEquals(cardStringExpected, cardStringActual);

		// Check the type string is correct
		Assert.assertEquals("MC", card.getType());
	}

}
