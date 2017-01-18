package uq.deco2800.pyramidscheme.duckdust;

import org.junit.Assert;
import org.junit.Test;
import uq.deco2800.pyramidscheme.cards.supercards.CardNotFoundException;
import uq.deco2800.pyramidscheme.cards.supercards.MinionCard;
import uq.deco2800.pyramidscheme.match.MatchCard;

/**
 * Created by hugokawamata on 21/10/16.
 */
public class DuckDustPoolTest {

    DuckDustPool pool = new DuckDustPool(10);

    private void setup() {
        pool.resetDust();
        pool.playerTurnOver();
    }

    @Test
    public void testGetDustCap() {
    	Assert.assertEquals(10, pool.getDustCap());
    }
    
    @Test
    public void testGrindAndSpendandRefund() throws CardNotFoundException {
        setup();
        // Both current and max dust should be 0
        int initialCurrent = pool.getCurrentDust();
        int initialMax = pool.getMaxDust();
        MatchCard card = new MatchCard(MinionCard.get("Mummy Duck"), 0, 0);
        // Player hasn't grinded anything yet
        Assert.assertEquals(false, pool.getGround());
        Assert.assertEquals(true, pool.canGrind());
        pool.grind();
        // because player has already grinded once, cannot grind now
        Assert.assertEquals(false, pool.canGrind());
        Assert.assertEquals(true, pool.getGround());
        Assert.assertEquals(initialCurrent + 1, pool.getCurrentDust());
        Assert.assertEquals(initialMax + 1, pool.getMaxDust());
        pool.grind();
        pool.grind();
        pool.grind();
        pool.grind();
        pool.grind();
        pool.grind();
        pool.grind();
        pool.grind();
        pool.grind();
        // Ten grinds - now we're at the dustCap
        Assert.assertEquals(initialCurrent + 10, pool.getCurrentDust());
        Assert.assertEquals(initialMax + 10, pool.getMaxDust());
        Assert.assertEquals(false, pool.canGrind());
        pool.grind();
        // Can't grind any more, max and current should not go up
        Assert.assertEquals(initialCurrent + 10, pool.getCurrentDust());
        Assert.assertEquals(initialMax + 10, pool.getMaxDust());
        pool.spend(6);
        Assert.assertEquals(initialCurrent + 4, pool.getCurrentDust());
        Assert.assertEquals(initialMax + 10, pool.getMaxDust());
        pool.spend(5);
        // Can't spend 5 since it is lesser than current dust
        Assert.assertEquals(initialCurrent + 4, pool.getCurrentDust());
        Assert.assertEquals(initialMax + 10, pool.getMaxDust());
        pool.spend(4);
        // Should be able to spend the entire amount
        Assert.assertEquals(initialCurrent, pool.getCurrentDust());
        Assert.assertEquals(initialMax + 10, pool.getMaxDust());
        pool.refund(6);
        Assert.assertEquals(initialCurrent + 6, pool.getCurrentDust());
        Assert.assertEquals(initialMax + 10, pool.getMaxDust());
        pool.refund(5);
        // refunding 5 would exceed dustCap, current should not go up
        Assert.assertEquals(initialCurrent + 6, pool.getCurrentDust());
        Assert.assertEquals(initialMax + 10, pool.getMaxDust());
        pool.refund(4);
        // should be able to refund such that current dust = dustCap
        Assert.assertEquals(initialCurrent + 10, pool.getCurrentDust());
        Assert.assertEquals(initialMax + 10, pool.getMaxDust());;
    }

    @Test
    public void testRefreshDustandCost() throws CardNotFoundException {
    	// Both current and max dust should be 0
        int initialCurrent = pool.getCurrentDust();
        int initialMax = pool.getMaxDust();
        MatchCard card = new MatchCard(MinionCard.get("Mummy Duck"), 0, 0);
        pool.grind();
        pool.grind();
        pool.grind();
        pool.grind();
        pool.grind();
        pool.grind();
        pool.spend(6);
        pool.refund(6);
        Assert.assertEquals(initialCurrent + 6, pool.getCurrentDust());
        Assert.assertEquals(initialMax + 6, pool.getMaxDust());
        // Now say the player spends some of their dust
        pool.spend(4);
        Assert.assertEquals(initialCurrent + 2, pool.getCurrentDust());
        Assert.assertEquals(initialMax + 6, pool.getMaxDust());
        pool.refreshDust();
        // currentDust should be same as maxDust now
        Assert.assertEquals(initialCurrent + 6, pool.getCurrentDust());
        Assert.assertEquals(initialMax + 6, pool.getMaxDust());
        
        // check if cards of different costs are playable
        Assert.assertEquals(true, pool.isPlayable(3));
        Assert.assertEquals(true, pool.isPlayable(6));
        Assert.assertEquals(false, pool.isPlayable(8));        
    }
    
    @Test
    public void testPlayerTurnover() throws CardNotFoundException {
    	// Before grinding anything
    	Assert.assertEquals(false, pool.getGround());
    	MatchCard card = new MatchCard(MinionCard.get("Mummy Duck"), 0, 0);
    	pool.grind();
    	Assert.assertEquals(true, pool.getGround());
    	
    	pool.playerTurnOver();
    	// Since player's turn is over, should go back to false
    	Assert.assertEquals(false, pool.getGround());
    }
    
    @Test
    public void testGetGround() throws CardNotFoundException {
        setup();
        MatchCard card = new MatchCard(MinionCard.get("Mummy Duck"), 0, 0);
        boolean beforeGrind = pool.getGround();
        Assert.assertFalse(beforeGrind);
        pool.grind();
        boolean afterGrind = pool.getGround();
        Assert.assertTrue(afterGrind);
    }

}
