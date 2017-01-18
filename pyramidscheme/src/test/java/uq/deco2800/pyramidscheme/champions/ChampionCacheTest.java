package uq.deco2800.pyramidscheme.champions;

import org.junit.Assert;
import org.junit.Test;
import uq.deco2800.pyramidscheme.champions.abilities.*;
import uq.deco2800.pyramidscheme.cards.supercards.MinionCard;

/**
 * Test cases for the Champion Cache
 *
 * @author Josh Fry
 */
public class ChampionCacheTest {

	// Check that championCach created champions properly

	@Test 
	public void testChampionCache() {
		ChampionCache championCache = new ChampionCache();
		
		Champion rallardExpected = new Champion("Rallard", "/championImages/rallard.png", 
					new TargetDamage("Smite", "/championImages/smite.gif", 5, 5), 
					new TargetHeal("Radiant Sun", "/championImages/radiantSun.gif", 3, 2));
		
		Champion quackubisExpected = new Champion("Quackubis", "/championImages/quackubis.png", 
				new MinionRevive("Arise", "/championImages/arise.gif", 6), 
				new TargetEnrage("Closer to Death", "/championImages/closerToDeath.gif", 3, 2, 3));
		
		Champion fowlSphinxExpected = new Champion("Fowl Sphinx", "/championImages/fowlSphinx.png", 
				new MinionSummon("Commanding Quack", "/championImages/commandingQuack.gif", 3, new MinionCard("Relentless Borker", "/cardImages/nullCard.png", "/cardImages/nullCard.png", 1, 1, 1, 1)), 
				new TargetDamage("Scratch", "/championImages/scratch.gif", 2, 1));
		
		
		Champion rallardGot = championCache.getChampion("Rallard");
		Assert.assertEquals(rallardGot.toString(), rallardExpected.toString());
		
		Champion quackubisGot = championCache.getChampion("Quackubis");
		Assert.assertEquals(quackubisGot.toString(), quackubisExpected.toString());
		
		Champion fowlSphinxGot = championCache.getChampion("Fowl Sphinx");
		Assert.assertEquals(fowlSphinxGot.toString(), fowlSphinxExpected.toString());
		
	}
}
