package uq.deco2800.pyramidscheme.cards;

import org.junit.Assert;
import org.junit.Test;
import uq.deco2800.pyramidscheme.actions.DoubleStrike;
import uq.deco2800.pyramidscheme.cards.supercards.ActionCard;
import uq.deco2800.pyramidscheme.cards.supercards.Card;
import uq.deco2800.pyramidscheme.cards.supercards.CardNotFoundException;
import uq.deco2800.pyramidscheme.cards.supercards.MinionCard;
import uq.deco2800.pyramidscheme.cards.supercards.NullCard;

import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class CardCreationTest {

	@Test
	public void testNumCards() throws CardNotFoundException {
		int num = MinionCard.getAllCards().values().size();


	}

	@Test
	public void testDupSibling() throws CardNotFoundException {
		MinionCard mc = MinionCard.get("Tutanquackum");
		mc.setStack("Hello", "World");
		mc.setStack("Hello", "Tests");
		Assert.assertEquals(mc.getStackKeys().size(), 1);
		MinionCard testMC = new MinionCard("Hello", "/cardImages/null.png", null, 1, 1, 1, 1);
		MinionCard testMC2 = new MinionCard("Hello2", "/cardImages/null.png", null, 1, 1, 1, 1);
        Assert.assertNull(mc.getParent(testMC));
		Assert.assertNull(mc.getParent(testMC2));
	}

	@Test
	public void testActionGetter() throws CardNotFoundException {
		Assert.assertNull(MinionCard.get("Tutanquackum").getAction());
		Assert.assertNotNull(MinionCard.get("Quack the Ripper").getAction());
	}

	@Test
	public void testActionCardInitializer() {
		ActionCard ac = new ActionCard("Test1", null, null, new DoubleStrike(), 1);
		Assert.assertEquals(ac.getAction().getName(), new DoubleStrike().getName());
		Assert.assertNotEquals(ac.getAction(), null);
		Assert.assertEquals(ac.toString(), "AC " + ac.getName() + ": " + ac.getAction().getName());
	}

	@Test
	public void testCacheReBuild() {
		Map<String, MinionCard> oldAllMap = MinionCard.getAllCards();
        Map<String, MinionCard> oldHiddenMap = MinionCard.getHiddenCards();
		Map<String, MinionCard> oldVisibleMap = MinionCard.getCards();
		MinionCard.clearCache();
		Assert.assertEquals(oldAllMap, MinionCard.getAllCards());
		MinionCard.clearCache();
		Assert.assertEquals(oldHiddenMap, MinionCard.getHiddenCards());
		MinionCard.clearCache();
		Assert.assertEquals(oldVisibleMap, MinionCard.getCards());
	}

	@Test
	public void testCardEquals() throws CardNotFoundException {
		Assert.assertEquals(MinionCard.get("Tutanquackum"), MinionCard.get("Tutanquackum"));
		Assert.assertNotEquals(MinionCard.get("Tutanquackum"), MinionCard.get("Useless Duck"));
		Card c = MinionCard.get("Tutanquackum");
		Assert.assertEquals(c, MinionCard.get("Tutanquackum"));
        Assert.assertNotEquals(c, "Test");
	}

	@Test
	public void testCardParent() throws CardNotFoundException {
		MinionCard mc = MinionCard.get("King Penduckling");
		Assert.assertEquals(mc.getParent(mc), MinionCard.get("King Duck"));
	}

	@Test
	public void testNoDups() throws CardNotFoundException {
		int keysNum = MinionCard.getCards().keySet().size();
		int valuesNum = MinionCard.getCards().values().size();
		Assert.assertEquals("Tests the keys and values equal same length",
				keysNum, valuesNum);
		keysNum = MinionCard.getHiddenCards().keySet().size();
		valuesNum = MinionCard.getHiddenCards().values().size();
		Assert.assertEquals(keysNum, valuesNum);
		ArrayList<MinionCard> a = new ArrayList<MinionCard>();
		for (MinionCard mc : MinionCard.getAllCards().values()) {
			if (a.contains(mc)) {
				System.out.println(a);
				System.out.println(mc);
				Assert.assertFalse("Dup cards detected", true);
			} else {
				a.add(mc);
			}
		}
	}

	@Test
	public void testExists() throws CardNotFoundException {
		Assert.assertNotNull(MinionCard.get("Ace Hentura"));
		Assert.assertNotNull(MinionCard.get("Adventurous Duckling"));
		Assert.assertNotNull(MinionCard.get("Alexander the Duck"));
		Assert.assertNotNull(MinionCard.get("Atilla The Duck"));
		Assert.assertNotNull(MinionCard.get("Big Fat ol' Duck"));
		Assert.assertNotNull(MinionCard.get("Charging Duck"));
		Assert.assertNotNull(MinionCard.get("Crazed Duckling"));
		Assert.assertNotNull(MinionCard.get("Double Headed Duck"));
		Assert.assertNotNull(MinionCard.get("Duckling Reinforcements"));
		Assert.assertNotNull(MinionCard.get("Duck Norris"));
		Assert.assertNotNull(MinionCard.get("Duck Norris Jr"));
		Assert.assertNotNull(MinionCard.get("Duck Trapper"));
		Assert.assertNotNull(MinionCard.get("Duckzilla"));
		Assert.assertNotNull(MinionCard.get("Edgar Allen Crow"));
		Assert.assertNotNull(MinionCard.get("Eggolus"));
        Assert.assertNotNull(MinionCard.get("Exploduck"));
		Assert.assertNotNull(MinionCard.get("King Duck"));
		Assert.assertNotNull(MinionCard.get("King Penduckling"));
		Assert.assertNotNull(MinionCard.get("Little Fat ol' Duck"));
		Assert.assertNotNull(MinionCard.get("Mace Wielding Duck"));
		Assert.assertNotNull(MinionCard.get("Medical Duck"));
		Assert.assertNotNull(MinionCard.get("Mummy Duck"));
		Assert.assertNotNull(MinionCard.get("On Duty Armoury"));
		Assert.assertNotNull(MinionCard.get("Poisoned Tipped Duck"));
		Assert.assertNotNull(MinionCard.get("Quack the Ripper"));
		Assert.assertNotNull(MinionCard.get("Shielded Sorceress"));
		Assert.assertNotNull(MinionCard.get("Sir Galaduck"));
        Assert.assertNotNull(MinionCard.get("Squawk Leader"));
		Assert.assertNotNull(MinionCard.get("Sweduck"));
		Assert.assertNotNull(MinionCard.get("Tutanquackum"));
		Assert.assertNotNull(MinionCard.get("Useless Duck"));
		Assert.assertNotNull(MinionCard.get("Relentless Borker"));
		Assert.assertNotNull(MinionCard.get("Atilla The Duck"));
		Assert.assertNotNull(MinionCard.get("Holy Duck"));
		Assert.assertNotNull(MinionCard.get("Super Duck"));
		Assert.assertNotNull(MinionCard.get("Duck of the Heavens"));
	}

	@Test(expected = CardNotFoundException.class)
	public void testNotValidCard() throws CardNotFoundException {
		MinionCard.get("");
	}

	@Test
	public void testImageNull() {
		Assert.assertNotNull((new MinionCard("Base Card",
				"/cardImages/no.png", null, 1, 1, 1, 1)).getDeckImg());
		Assert.assertNull((new MinionCard("Base Card",
				"/cardImages/no.png", null, 1, 1, 1, 1)).getImage());
		Assert.assertNull((new MinionCard("Base Card",
				"/cardImages/no.png", null, 1, 1, 1, 1)).getUnboxImg());
	}
	
	@Test
	public void testCardToString() throws CardNotFoundException {
		MinionCard card = MinionCard.get("Useless Duck");
		String actual = card.toString();
		String expected = "MC Useless Duck: A1 D1 R1 Dust1";
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testCardGetType() throws CardNotFoundException {
		NullCard card = new NullCard("nullCard", 
				"/cardImages/nullCard.png");
		String actual = card.getType();
		String expected = "NC";
		Assert.assertEquals(expected, actual);
	}

}
