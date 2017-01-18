package uq.deco2800.pyramidscheme.player;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

import uq.deco2800.pyramidscheme.board.RecBoard;
import uq.deco2800.pyramidscheme.board.RecTile;
import uq.deco2800.pyramidscheme.deck.Deck;
import uq.deco2800.pyramidscheme.game.BasicGameGenerator;
import uq.deco2800.pyramidscheme.match.Match;
import uq.deco2800.pyramidscheme.match.MatchAI;
import uq.deco2800.pyramidscheme.match.MatchPlayer;
import uq.deco2800.pyramidscheme.pyramid.Pyramid;
import uq.deco2800.pyramidscheme.pyramid.PyramidType;

/**
 * Basic AI Test - Split from PlayerTest
 *
 * @author Billy-7
 * @author bijelo9
 */
public class AiTest {

	BasicGameGenerator gameGen = new BasicGameGenerator();

	@Test
	public void nameTest() {
		AI ai = gameGen.createBasicAI();
		Assert.assertEquals("Sir Steel", ai.getName()); // currently hard-coded!
	}
	
	@Test
	public void nameBossTest() {
		AI ai = gameGen.createBossAI(1,gameGen.createBasicDeck());
		Assert.assertEquals("Bro'ck", ai.getName());
	}
		

	@Test
	public void healthTest() {
		AI ai = gameGen.createBasicAI();
		Assert.assertEquals(Player.MAX_HEALTH, ai.getMaxHealth()); // currently hard-coded to Player.MAX_HEALTH!
	}
	
	@Test
	public void setBoardTest() {
		//create test deck, AI and user players
		Deck deck = gameGen.createBasicDeck();
		User user = new User("testUser", deck);
		AI ai = gameGen.createBasicAI();
				
		//create a fake match (comes with board)
		Match match = new Match(user, ai);
		//got numbers from BasicMatchController
		RecBoard testBoard = new RecBoard(225, 75, match.getUser(), match.getOpponent());
		match.getOpponent().setBoard(testBoard);
	}
	
	@Test
	public void setPyramidTest() {
		//create test deck, AI and user players
		Deck deck = gameGen.createBasicDeck();
		User user = new User("testUser", deck);
		AI ai = gameGen.createBasicAI();
		
		//create a fake match (comes with board)
		Match match = new Match(user, ai);
		Pyramid pyramid = new Pyramid(40,100, PyramidType.TRIANGLE, true); //got numbers form pyramidTest
		match.getOpponent().setPyramid(pyramid);
	}
	
	@Test
	public void getUserTilesTest() {
		//create test deck, AI and user players
		Deck deck = gameGen.createBasicDeck();
		User user = new User("testUser", deck);
		AI ai = gameGen.createBasicAI();
		
		//create a fake match (comes with board)
		Match match = new Match(user, ai);
		//got numbers from BasicMatchController
		RecBoard testBoard = new RecBoard(225, 75, match.getUser(), match.getOpponent());
		match.getOpponent().setBoard(testBoard);
	
		//true if tiles don't belong to AI player
		Boolean notAI;
		
		for (RecTile testTile : testBoard) {
			if (testTile.getOwner().equals(match.getUser())) {
				notAI = true;
				Assert.assertTrue(notAI);
			}
		}
	}
}
