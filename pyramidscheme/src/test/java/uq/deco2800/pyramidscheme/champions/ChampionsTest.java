package uq.deco2800.pyramidscheme.champions;

import javafx.scene.image.Image;
import org.junit.Assert;
import org.junit.Test;
import uq.deco2800.pyramidscheme.champions.abilities.*;
import uq.deco2800.pyramidscheme.cards.supercards.CardNotFoundException;
import uq.deco2800.pyramidscheme.cards.supercards.MinionCard;

/**
 * Test cases for the Champion's class.
 *
 * @author Silin Liu 
 */
public class ChampionsTest {

	// Setup of the necessary champion classes for testing
	ChampionCache championCache = new ChampionCache();

	Champion testChampionQuack = championCache.getChampion("Quackubis");
	Champion testChampionFowl = championCache.getChampion("Fowl Sphinx");
	Champion testChampionRal = championCache.getChampion("Rallard");
	
	// Test to check for the integrity of champion names
	@Test
	public void testChampionNames() {
		String champNameToTest;
		champNameToTest = testChampionQuack.getName();
		Assert.assertEquals("Champion names do not match", "Quackubis", champNameToTest);
		
		champNameToTest = testChampionFowl.getName();
		Assert.assertEquals("Champion names do not match", "Fowl Sphinx", champNameToTest);
		
		champNameToTest = testChampionRal.getName();
		Assert.assertEquals("Champion names do not match", "Rallard", champNameToTest);
	}
	
	// Checks if the image patch belongs to a valid directory
	@Test 
	public void testChampionImgPath() {
		Image testImage;
		// If the path is invalid, then image will be null
		testImage = testChampionQuack.getImage();
		Assert.assertNotNull("Quackubis image file does not exist", testImage);
		testImage = testChampionFowl.getImage();
		Assert.assertNotNull("Fowl Sphinx image file does not exist", testImage);
		testImage = testChampionRal.getImage();
		Assert.assertNotNull("Rallard image file does not exist", testImage);
	}
	
	// Checks if the return champion type is correct
	@Test
	public void testChampionType() {
		String testType;
		// Runs the test for each champion
		testType = testChampionQuack.getType();
		Assert.assertEquals("Incorrect champion type", "CH", testType);
		testType = testChampionFowl.getType();
		Assert.assertEquals("Incorrect champion type", "CH", testType);
		testType = testChampionRal.getType();
		Assert.assertEquals("Incorrect champion type", "CH", testType);
	}
	
	// Tests on the champions abilities to check if they match the specified ones
	@Test
	public void testChampionAbility() {
		// Test primary ability (Quackubis)
		Ability testAbility = testChampionQuack.getAbility(0);
		Ability priAbility = new MinionRevive("Arise", "/championImages/arise.gif", 6);
		Assert.assertEquals(testAbility.getName(), priAbility.getName());
		Assert.assertEquals(testAbility.toString(), priAbility.toString());
		
		// Test secondary ability
		testAbility = testChampionQuack.getAbility(1);
		Ability secAbility = new TargetEnrage("Closer to Death", "/championImages/closerToDeath.gif", 3, 2, 3);
		Assert.assertEquals(testAbility.getName(), secAbility.getName());
		
		// Test primary ability (Fowl Sphinx)
		testAbility = testChampionFowl.getAbility(0);
		try {
			priAbility = new MinionSummon("Commanding Quack", "/championImages/commandingQuack.gif", 3, MinionCard.get("Relentless Borker"));
		} catch (CardNotFoundException e) {
			e.printStackTrace();
		}
		Assert.assertEquals(testAbility.getName(), priAbility.getName());

		// Test secondary ability
		testAbility = testChampionFowl.getAbility(1);
		secAbility = new TargetDamage("Scratch", "/championImages/scratch.gif", 2,  1);
		Assert.assertEquals(testAbility.getName(), secAbility.getName());
		
		// Test primary ability (Rallard)
		testAbility = testChampionRal.getAbility(0);
		priAbility = new TargetDamage("Smite", "/championImages/smite.gif", 5, 5);
		Assert.assertEquals(testAbility.getName(), priAbility.getName());
		// Test secondary ability
		testAbility = testChampionRal.getAbility(1);
		secAbility = new TargetHeal("Radiant Sun", "/championImages/radiantSun.gif", 5, 2);
		Assert.assertEquals(testAbility.getName(), secAbility.getName());
	}
	
}
