package uq.deco2800.pyramidscheme.board;

import org.junit.Assert;
import org.junit.Test;
import uq.deco2800.pyramidscheme.cards.BasicMinion;
import uq.deco2800.pyramidscheme.cards.supercards.NullCard;
import uq.deco2800.pyramidscheme.deck.Deck;
import uq.deco2800.pyramidscheme.match.MatchAI;
import uq.deco2800.pyramidscheme.match.MatchCard;
import uq.deco2800.pyramidscheme.match.MatchPlayer;
import uq.deco2800.pyramidscheme.player.AI;
import uq.deco2800.pyramidscheme.player.User;

import java.util.Iterator;
import java.util.List;
/**
 * Created by Jacob on 17/09/16.
 */

public class RecBoardTest {
	//Player player, int maxPyramidCards, int maxBoardCards
	User user = new User("User", new Deck());
	MatchPlayer matchUser = new MatchPlayer(user, true);

	AI ai = new AI("AI", new Deck());
	MatchAI matchAI = new MatchAI(ai);

	@Test
	//Test For 12 tiles
	public void testTileGeneration() {
		//int xCoord, int yCoord, MatchPlayer user, MatchPlayer matchAI
		RecBoard rec = new RecBoard(0, 0, matchUser, matchAI);
		int i = 0;
		Iterator<RecTile> it = rec.iterator();
		for(i = 0; it.hasNext(); i++) it.next();
		Assert.assertEquals(12, i);

	}
	
	@Test
	//Comparison of seperate iterated tiles
	public void testBoardSetup() {
		//int xCoord, int yCoord, MatchPlayer user, MatchPlayer matchAI
		RecBoard rec1 = new RecBoard(0, 0, matchUser, matchAI);
		RecBoard rec2 = new RecBoard(0, 0, matchUser, matchAI);
		
		Iterator<RecTile> rec1It = rec1.iterator();
		Iterator<RecTile> rec2It = rec2.iterator();
		
		while(rec1It.hasNext()) {
			RecTile rec1Tile = rec1It.next();
			RecTile rec2Tile = rec2It.next();
			//assertEquals fails when rec1 & rec 2 tiles are compared
			//Therefore convert to string values
			Assert.assertEquals(rec1Tile.toString(), rec2Tile.toString());
		}
	}
	
	@Test
	//Testing X coord outside containsCoords bounds
	public void testXTilesNeg() {
		//int xCoord, int yCoord, MatchPlayer user, MatchPlayer matchAI
		RecBoard rec1 = new RecBoard(0, 0, matchUser, matchAI);
		Iterator<RecTile> rec1It = rec1.iterator();
		RecTile tile = rec1It.next();
		//containsCoords returns boolean value
		Assert.assertFalse(tile.containsCoords(-1, 0));
	}
	
	
	@Test
	//Testing X coords inside containsCoords bounds
	public void testXTilesFive() {
		//int xCoord, int yCoord, MatchPlayer user, MatchPlayer matchAI
		RecBoard rec1 = new RecBoard(0, 0, matchUser, matchAI);
		Iterator<RecTile> rec1It = rec1.iterator();
		RecTile tile = rec1It.next();
		//containsCoords returns boolean value
		Assert.assertTrue(tile.containsCoords(5, 0));
	}
	
	@Test
	//Testing X coords inside containsCoords bounds
	public void testXTilesSixtyNine() {
		//int xCoord, int yCoord, MatchPlayer user, MatchPlayer matchAI
		RecBoard rec1 = new RecBoard(0, 0, matchUser, matchAI);
		Iterator<RecTile> rec1It = rec1.iterator();
		RecTile tile = rec1It.next();
		//containsCoords returns boolean value
		Assert.assertTrue(tile.containsCoords(69, 0));
	}
	
	
	@Test
	//Testing X coords outside containsCoords bounds
	public void testXTilesSeventyTwo() {
		//int xCoord, int yCoord, MatchPlayer user, MatchPlayer matchAI
		RecBoard rec1 = new RecBoard(0, 0, matchUser, matchAI);
		Iterator<RecTile> rec1It = rec1.iterator();
		RecTile tile = rec1It.next();
		//containsCoords returns boolean value
		Assert.assertFalse(tile.containsCoords(72, 0));
	}

	@Test
	//Testing Y coords outside containsCoords bounds
	public void testYTilesNeg() {
		//int xCoord, int yCoord, MatchPlayer user, MatchPlayer matchAI
		RecBoard rec1 = new RecBoard(0, 0, matchUser, matchAI);
		Iterator<RecTile> rec1It = rec1.iterator();
		RecTile tile = rec1It.next();
		//containsCoords returns boolean value
		Assert.assertFalse(tile.containsCoords(0, -1));
	}
	
	
	@Test
	//Testing Y coords inside containsCoords bounds
	public void testYTilesFive() {
		//int xCoord, int yCoord, MatchPlayer user, MatchPlayer matchAI
		RecBoard rec1 = new RecBoard(0, 0, matchUser, matchAI);
		Iterator<RecTile> rec1It = rec1.iterator();
		RecTile tile = rec1It.next();
		//containsCoords returns boolean value
		Assert.assertTrue(tile.containsCoords(0,5));
	}
	
	@Test
	//Testing Y coords inside containsCoords bounds
	public void testYTilesNinetyNine() {
		//int xCoord, int yCoord, MatchPlayer user, MatchPlayer matchAI
		RecBoard rec1 = new RecBoard(0, 0, matchUser, matchAI);
		Iterator<RecTile> rec1It = rec1.iterator();
		RecTile tile = rec1It.next();
		//containsCoords returns boolean value
		Assert.assertTrue(tile.containsCoords(0,99));
	}
	
	@Test
	//Testing Y coords outside containsCoords bounds
	public void testYTilesOneHundredAndTwo() {
		//int xCoord, int yCoord, MatchPlayer user, MatchPlayer matchAI
		RecBoard rec1 = new RecBoard(0, 0, matchUser, matchAI);
		Iterator<RecTile> rec1It = rec1.iterator();
		RecTile tile = rec1It.next();
		//containsCoords returns boolean value
		Assert.assertFalse(tile.containsCoords(0,102));
	}

	@Test
	public void testSetContents() {
		RecBoard board = new RecBoard(0, 0, matchUser, matchAI);
		RecTile tile = board.iterator().next();

		tile.setContents(null);
		Assert.assertFalse(tile.getContents().isPresent());

		MatchCard nullMatchCard = new MatchCard(new NullCard("", ""), 0, 0);
		tile.setContents(nullMatchCard);

		Assert.assertTrue(tile.getContents().isPresent());
		
		MatchCard basicMatchCard = new MatchCard(new BasicMinion(), 0, 0);
		tile.setContents(basicMatchCard);
		// should be false since tile is not null
		Assert.assertFalse(tile.getContents().equals(basicMatchCard));
	}

	@Test
	public void testGetOppositeTileRoundtrip() {
		RecBoard board = new RecBoard(0, 0, matchUser, matchAI);
		RecTile tile = board.iterator().next();

		Assert.assertTrue(tile.getOppositeTile().getOppositeTile().equals(tile));
	}

	@Test
	public void testGetTileAt() {
		RecBoard board = new RecBoard(0, 0, matchUser, matchAI);
		Assert.assertTrue(board.getTileAt(5, 5).isPresent());
		Assert.assertFalse(board.getTileAt(-5, -5).isPresent());
	}

	@Test
	public void testGetTiles() {
		RecBoard board = new RecBoard(0, 0, matchUser, matchAI);
		Assert.assertEquals(board.getTiles().size(), RecBoard.WIDTH * RecBoard.HEIGHT);
	}


	@Test
	public void testGetDeadTiles() {
		RecBoard board = new RecBoard(0, 0, matchUser, matchAI);

		MatchCard usersMinion = new MatchCard(new BasicMinion(), 0, 0);
		MatchCard aisMinion = new MatchCard(new BasicMinion(), 0, 0);

		board.getTiles().get(0).setContents(aisMinion);
		board.getTiles().get(RecBoard.WIDTH).setContents(usersMinion);

		aisMinion.setIsToDie(false);
		usersMinion.setIsToDie(true);

		List<RecTile> deadTiles = board.getDeadTiles();

		Assert.assertFalse(deadTiles.contains(board.getTiles().get(0)));
		Assert.assertTrue(deadTiles.contains(board.getTiles().get(RecBoard.WIDTH)));
	}

	@Test
	public void testGetters() {
		RecBoard board = new RecBoard(0, 0, matchUser, matchAI);
		Assert.assertEquals(board.getTileAt(5, 5).get().getX(), 0);
		Assert.assertEquals(board.getTileAt(5, 5).get().getY(), 0);
	}

	@Test
	public void testNeighborGetters() {
		RecBoard board = new RecBoard(0, 0, matchUser, matchAI);

		// Leftmost checks
		Assert.assertFalse(board.getTiles().get(0).getLeftNeighbor().isPresent());
		Assert.assertTrue(board.getTiles().get(0).getRightNeighbor().isPresent());

		Assert.assertFalse(board.getTiles().get(RecBoard.WIDTH)
				.getLeftNeighbor().isPresent());

		Assert.assertTrue(board.getTiles().get(RecBoard.WIDTH)
				.getRightNeighbor().isPresent());


		// Rightmost checks
		Assert.assertFalse(board.getTiles().get(RecBoard.WIDTH - 1)
				.getRightNeighbor().isPresent());

		Assert.assertTrue(board.getTiles().get(RecBoard.WIDTH - 1)
				.getLeftNeighbor().isPresent());

		Assert.assertFalse(board.getTiles().get(RecBoard.WIDTH * 2 - 1)
				.getRightNeighbor().isPresent());

		Assert.assertTrue(board.getTiles().get(RecBoard.WIDTH * 2 - 1)
				.getLeftNeighbor().isPresent());


	}
}