package uq.deco2800.pyramidscheme.champions;

import org.junit.Assert;
import org.junit.Test;
import uq.deco2800.pyramidscheme.champions.abilities.*;
import uq.deco2800.pyramidscheme.match.MatchCard;
import uq.deco2800.pyramidscheme.cards.supercards.MinionCard;

/**
 * Test cases for the Ability class.
 *
 * @author Josh Fry
 */
public class AbilityTest {
	MinionCard testCard = new MinionCard("Minion", "/cardImages/null.png", "/cardImages/null.png", 10, 10, 10, 10);

	Ability minionRevive = new MinionRevive("Revive", "/championImages/arise.gif", 5);
	Ability minionSummon = new MinionSummon("Summon", "/championImages/arise.gif", 5, testCard);
	Ability targetAnnihilate = new TargetAnnihilate("Annihilate", "/championImages/arise.gif", 5);
	Ability targetBerserk = new TargetBerserk("Berserk", "/championImages/arise.gif", 5, 0.6, 2);
	Ability targetDamage = new TargetDamage("Damage", "/championImages/arise.gif", 5, 5);
	Ability targetDefenseAura = new TargetDefenseAura("DefenseAura", "/championImages/arise.gif", 5, 2, 0.6);
	Ability targetEnrage = new TargetEnrage("Enrage", "/championImages/arise.gif", 5, 2, 3);
	Ability targetHeal = new TargetHeal("Heal", "/championImages/arise.gif", 5, 5);
	Ability targetIronSkin = new TargetIronSkin("IronSkin", "/championImages/arise.gif", 5, 2);

	// Check ability tostring. Should also cover names, as getName() is used in
	// tostring.
	@Test
	public void testToString() {
		String toString;

		toString = minionRevive.toString();
		Assert.assertEquals("minionRevive toString does not match",
				"Revive:\nRevive your most recently destroyed card\nDust: 5", toString);

		toString = minionSummon.toString();
		Assert.assertEquals("minionSummon toString does not match", "Summon:\nSummon a Minion to your board\nDust: 5",
				toString);

		toString = targetAnnihilate.toString();
		Assert.assertEquals("targetAnnihilate toString does not match",
				"Annihilate:\nInstantly destroy a target card\nDust: 5", toString);

		toString = targetBerserk.toString();
		Assert.assertEquals("targetBerserk toString does not match",
				"Berserk:\nRemove 40% of a targets health, increase its attack by 100%\nDust: 5", toString);

		toString = targetDamage.toString();
		Assert.assertEquals("targetDamage toString does not match", "Damage:\nDeal 5 damage to a target card\nDust: 5",
				toString);

		toString = targetDefenseAura.toString();
		Assert.assertEquals("targetDefenseAura toString does not match",
				"DefenseAura:\nLower a targets attack by 40%, increase its health by 100%\nDust: 5", toString);

		toString = targetEnrage.toString();
		Assert.assertEquals("targetEnrage toString does not match",
				"Enrage:\nDeal 2 damage to a target card, increase its attack by 3\nDust: 5", toString);

		toString = targetHeal.toString();
		Assert.assertEquals("targetHeal toString does not match", "Heal:\nHeal a target card for 5 health\nDust: 5",
				toString);

		toString = targetIronSkin.toString();
		Assert.assertEquals("targetIronSkin toString does not match",
				"IronSkin:\nIncrease a targets health by 100%\nDust: 5", toString);

		// taunt is broken
	}

	@Test
	public void testImg() {
		String path = "/championImages/arise.gif";

		Assert.assertEquals("minionRevive image path does not match", path, minionRevive.getImagePath());

		Assert.assertEquals("minionSummon image path does not match", path, minionSummon.getImagePath());

		Assert.assertEquals("targetAnnihilate image path does not match", path, targetAnnihilate.getImagePath());

		Assert.assertEquals("targetBerserk image path does not match", path, targetBerserk.getImagePath());

		Assert.assertEquals("targetDamage image path does not match", path, targetDamage.getImagePath());

		Assert.assertEquals("targetDefenseAura image path does not match", path, targetDefenseAura.getImagePath());

		Assert.assertEquals("targetEnrage image path does not match", path, targetEnrage.getImagePath());

		Assert.assertEquals("targetHeal image path does not match", path, targetHeal.getImagePath());

		Assert.assertEquals("targetIronSkin image path does not match", path, targetIronSkin.getImagePath());

	}

	// Testing ability Activation below here. Couldn't figure out an efficient
	// way to test MinionRevive and MinionSummon, as it requires a board, decks,
	// players, etc.

	@Test
	public void testActivateTargetAnnihilate() {
		MatchCard testMatchCard1 = new MatchCard(testCard, 0, 0);

		// proper usage
		Assert.assertTrue("TargetAnnihilate should have worked", targetAnnihilate.activateAbility(testMatchCard1, null)
				&& testMatchCard1.getHealth() == 0 && testMatchCard1.isToDie());

		// incorrect parameters
		Assert.assertFalse("TargetAnnihilate didn't reject incorrect parameters",
				targetAnnihilate.activateAbility(null, testMatchCard1));
		Assert.assertFalse("TargetAnnihilate didn't reject incorrect parameters",
				targetAnnihilate.activateAbility(testMatchCard1, testMatchCard1));

	}

	@Test
	public void testActivateTargetBerserk() {
		MatchCard testMatchCard1 = new MatchCard(testCard, 0, 0);
		MatchCard testMatchCard2 = new MatchCard(testCard, 0, 0);

		// proper usage
		Assert.assertTrue("TargetBerserk should have worked", targetBerserk.activateAbility(testMatchCard1, null)
				&& testMatchCard1.getHealth() == 6 && testMatchCard1.getAttack() == 20);

		// proper usage, target died
		testMatchCard2.setHealth(0);
		Assert.assertTrue("TargetBerserk should have killed card", targetBerserk.activateAbility(testMatchCard2, null)
				&& testMatchCard2.getHealth() == 0 && testMatchCard2.getAttack() == 20 && testMatchCard2.isToDie());

		// incorrect parameters
		Assert.assertFalse("TargetBerserk didn't reject incorrect parameters",
				targetBerserk.activateAbility(null, testMatchCard1));
		Assert.assertFalse("TargetBerserk didn't reject incorrect parameters",
				targetBerserk.activateAbility(testMatchCard1, testMatchCard1));

	}

	@Test
	public void testActivateTargetDamage() {
		MatchCard testMatchCard1 = new MatchCard(testCard, 0, 0);
		MatchCard testMatchCard2 = new MatchCard(testCard, 0, 0);

		// proper usage
		Assert.assertTrue("TargetDamage should have worked",
				targetDamage.activateAbility(testMatchCard1, null) && testMatchCard1.getHealth() == 5);

		// proper usage, target died
		testMatchCard2.setHealth(5);
		Assert.assertTrue("TargetDamage should have killed card", targetDamage.activateAbility(testMatchCard2, null)
				&& testMatchCard2.getHealth() == 0 && testMatchCard2.isToDie());

		// incorrect parameters
		Assert.assertFalse("TargetDamage didn't reject incorrect parameters",
				targetDamage.activateAbility(null, testMatchCard1));
		Assert.assertFalse("TargetDamage didn't reject incorrect parameters",
				targetDamage.activateAbility(testMatchCard1, testMatchCard1));

	}

	@Test
	public void testActivateTargetDefenseAura() {
		MatchCard testMatchCard1 = new MatchCard(testCard, 0, 0);

		// proper usage
		Assert.assertTrue("TargetDefenseAura should have worked",
				targetDefenseAura.activateAbility(testMatchCard1, null) && testMatchCard1.getHealth() == 20
						&& testMatchCard1.getAttack() == 6);

		// incorrect parameters
		Assert.assertFalse("TargetDefenseAura didn't reject incorrect parameters",
				targetDefenseAura.activateAbility(null, testMatchCard1));
		Assert.assertFalse("TargetDefenseAura didn't reject incorrect parameters",
				targetDefenseAura.activateAbility(testMatchCard1, testMatchCard1));

	}

	@Test
	public void tesActivatetTargetEnrage() {
		MatchCard testMatchCard1 = new MatchCard(testCard, 0, 0);
		MatchCard testMatchCard2 = new MatchCard(testCard, 0, 0);

		// proper usage
		Assert.assertTrue("TargetEnrage should have worked", targetEnrage.activateAbility(testMatchCard1, null)
				&& testMatchCard1.getHealth() == 8 && testMatchCard1.getAttack() == 13);

		// proper usage, target died
		testMatchCard2.setHealth(2);
		Assert.assertTrue("TargetEnrage should have killed card", targetEnrage.activateAbility(testMatchCard2, null)
				&& testMatchCard2.getHealth() == 0 && testMatchCard2.getAttack() == 13 && testMatchCard2.isToDie());

		// incorrect parameters
		Assert.assertFalse("TargetEnrage didn't reject incorrect parameters",
				targetEnrage.activateAbility(null, testMatchCard1));
		Assert.assertFalse("TargetEnrage didn't reject incorrect parameters",
				targetEnrage.activateAbility(testMatchCard1, testMatchCard1));

	}

	@Test
	public void testActivateTargetHeal() {
		MatchCard testMatchCard1 = new MatchCard(testCard, 0, 0);

		// proper usage
		Assert.assertTrue("TargetHeal should have worked",
				targetHeal.activateAbility(testMatchCard1, null) && testMatchCard1.getHealth() == 15);

		// incorrect parameters
		Assert.assertFalse("TargetHeal didn't reject incorrect parameters",
				targetHeal.activateAbility(null, testMatchCard1));
		Assert.assertFalse("TargetHeal didn't reject incorrect parameters",
				targetHeal.activateAbility(testMatchCard1, testMatchCard1));

	}

	@Test
	public void testActivateTargetIronSkin() {
		MatchCard testMatchCard1 = new MatchCard(testCard, 0, 0);

		// proper usage
		Assert.assertTrue("TargetIronSkin should have worked",
				targetIronSkin.activateAbility(testMatchCard1, null) && testMatchCard1.getHealth() == 20);

		// incorrect parameters
		Assert.assertFalse("TargetIronSkin didn't reject incorrect parameters",
				targetIronSkin.activateAbility(null, testMatchCard1));
		Assert.assertFalse("TargetIronSkin didn't reject incorrect parameters",
				targetIronSkin.activateAbility(testMatchCard1, testMatchCard1));
	}

}
