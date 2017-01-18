package uq.deco2800.pyramidscheme.match;

import org.junit.Assert;
import org.junit.Test;
import uq.deco2800.pyramidscheme.cards.BoardDeSpellAction;
import uq.deco2800.pyramidscheme.cards.supercards.CardNotFoundException;
import uq.deco2800.pyramidscheme.cards.supercards.MinionCard;

/**
 * Created by Max on 8/09/2016.
 */
public class MatchCardTest {

    @Test
    public void testGetters() throws CardNotFoundException {
        MatchCard crazedDuckling = new MatchCard(MinionCard.get("Crazed Duckling"), 0, 0);

        MatchCard mummyDuck = new MatchCard(MinionCard.get("Mummy Duck"), 0, 0);

        MatchCard boardDeSpellAction = new MatchCard(new BoardDeSpellAction(), 0, 0);

        Assert.assertTrue(crazedDuckling.getAttack() == 2);
        Assert.assertTrue(crazedDuckling.getHealth() == 5);
        Assert.assertTrue(crazedDuckling.getValue() == 7);
        Assert.assertFalse(crazedDuckling.isToDie());

        Assert.assertTrue(mummyDuck.getAttack() == 2);
        Assert.assertTrue(mummyDuck.getHealth() == 2);
        Assert.assertTrue(mummyDuck.getValue() == 4);
        Assert.assertFalse(mummyDuck.isToDie());

        Assert.assertTrue(boardDeSpellAction.getAttack() == 0);
        Assert.assertTrue(boardDeSpellAction.getHealth() == 0);
        Assert.assertTrue(boardDeSpellAction.getValue() == 0);
        Assert.assertFalse(mummyDuck.isToDie());
    }

    @Test
    public void testFightDirectionOne() throws CardNotFoundException {
        MatchCard crazedDuckling = new MatchCard(MinionCard.get("Crazed Duckling"), 0, 0);

        MatchCard mummyDuck = new MatchCard(MinionCard.get("Mummy Duck"), 0, 0);

        crazedDuckling.attackedBy(mummyDuck);

        Assert.assertTrue(crazedDuckling.getHealth() == 3);
        Assert.assertFalse(crazedDuckling.isToDie());

        Assert.assertTrue(mummyDuck.getHealth() == 0);
        Assert.assertTrue(mummyDuck.isToDie());
    }

    @Test
    public void testFightDirectionTwo() throws CardNotFoundException {
        MatchCard crazedDuckling = new MatchCard(MinionCard.get("Crazed Duckling"), 0, 0);

        MatchCard mummyDuck = new MatchCard(MinionCard.get("Mummy Duck"), 0, 0);

        // Other direction now
        mummyDuck.attackedBy(crazedDuckling);

        Assert.assertTrue(crazedDuckling.getHealth() == 3);
        Assert.assertFalse(crazedDuckling.isToDie());

        Assert.assertTrue(mummyDuck.getHealth() == 0);
        Assert.assertTrue(mummyDuck.isToDie());
    }

    @Test
    public void testActionCardDoesNoDamage() throws CardNotFoundException {
        MatchCard mummyDuck = new MatchCard(MinionCard.get("Mummy Duck"), 0, 0);

        MatchCard boardDeSpellAction = new MatchCard(new BoardDeSpellAction(), 0, 0);

        int healthBefore = mummyDuck.getHealth();

        mummyDuck.attackedBy(boardDeSpellAction);

        Assert.assertFalse(mummyDuck.isToDie());
        Assert.assertTrue(mummyDuck.getHealth() == healthBefore);
        Assert.assertFalse(boardDeSpellAction.isToDie());

        healthBefore = mummyDuck.getHealth();

        boardDeSpellAction.attackedBy(mummyDuck);
    }
    
    @Test
    public void testSetHealthAndAttack() throws CardNotFoundException {
    	MatchCard alexanderTheDuck = new MatchCard(MinionCard.get("Alexander the Duck"), 0, 0);
    	MatchCard sweduck = new MatchCard(MinionCard.get("Sweduck"), 0, 0);
    	
    	sweduck.setHealth(34);
    	Assert.assertTrue(sweduck.getHealth() == 34);
    	sweduck.setHealth(0);
    	Assert.assertTrue(sweduck.getHealth() == 0);
    	
    	alexanderTheDuck.setAttack(22);
    	Assert.assertTrue(alexanderTheDuck.getAttack() == 22);
    	alexanderTheDuck.setAttack(-1);
    	Assert.assertTrue(alexanderTheDuck.getAttack() == -1);
    }
    
    @Test
    public void testMultiplyHealthAnAttack() throws CardNotFoundException {
    	MatchCard alexanderTheDuck = new MatchCard(MinionCard.get("Alexander the Duck"), 0, 0);
    	MatchCard sweduck = new MatchCard(MinionCard.get("Sweduck"), 0, 0);
    	
    	sweduck.setHealth(5);
    	sweduck.multiplyHealth(3);
    	Assert.assertTrue(sweduck.getHealth() == 15);
    	sweduck.multiplyHealth(0.5);
    	Assert.assertTrue(sweduck.getHealth() == 7);
    	sweduck.multiplyHealth(-3);
    	Assert.assertTrue(sweduck.getHealth() == -21);
    	
    	alexanderTheDuck.setAttack(5);
    	alexanderTheDuck.multiplyAttack(3);
    	Assert.assertTrue(alexanderTheDuck.getAttack() == 15);
    	alexanderTheDuck.multiplyAttack(0.5);
    	Assert.assertTrue(alexanderTheDuck.getAttack() == 7);
    	alexanderTheDuck.multiplyAttack(-3);
    	Assert.assertTrue(alexanderTheDuck.getAttack() == -21);
    }
}
