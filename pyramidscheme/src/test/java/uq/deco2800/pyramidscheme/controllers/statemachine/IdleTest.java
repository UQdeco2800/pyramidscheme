package uq.deco2800.pyramidscheme.controllers.statemachine;

import com.google.common.collect.ImmutableList;
import javafx.stage.Stage;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.testfx.framework.junit.ApplicationTest;
import uq.deco2800.pyramidscheme.cards.supercards.Card;
import uq.deco2800.pyramidscheme.cards.supercards.MinionCard;
import uq.deco2800.pyramidscheme.controllers.BasicMatchController;
import uq.deco2800.pyramidscheme.deck.BasicDeckTest;
import uq.deco2800.pyramidscheme.game.GameManager;
import uq.deco2800.pyramidscheme.game.GameState;
import uq.deco2800.pyramidscheme.match.Match;
import uq.deco2800.pyramidscheme.match.MatchCard;
import uq.deco2800.pyramidscheme.player.AI;
import uq.deco2800.pyramidscheme.player.User;
import uq.deco2800.pyramidscheme.pyramid.PyramidType;

import java.util.concurrent.TimeoutException;

import static org.mockito.Mockito.withSettings;
import static org.testfx.api.FxToolkit.setupStage;

/**
 * Created by Jacob on 17/10/16.
 */
@RunWith(MockitoJUnitRunner.class)
public class IdleTest extends ApplicationTest {
    /**
     * Mock the GameManager class with Mockito
     */
	GameManager gameManager;
	HeldCard heldCard;
	
    private Stage stage;
	
    @Override
    public void start(Stage stage) throws Exception {
	    gameManager = Mockito.mock(GameManager.class, withSettings().stubOnly());
	    this.stage = stage;
	    gameManager = GameManager.getInstance();
        gameManager.setPrimaryStage(stage);
        GameManager.getStatisticsTracking().createGuestUser();
        gameManager.setUser(new User("Guest", BasicDeckTest.createDeck()));
        gameManager.gameScene("BasicMatchScreen.fxml", PyramidType.TRIANGLE, PyramidType.TRIANGLE);
        stage.show();
    }
    
    
    @Test
    public void checkGameManager() throws Exception {
        User user = new User("Guest", BasicDeckTest.createDeck());
        AI opponent = new AI("Harding", BasicDeckTest.createDeck());
        Match match = new Match(user, opponent);
        StateCallback sb = new BasicMatchController();
        // Function under test
        Idle actual = new Idle(match, sb);
        Assert.assertNotNull(actual);
    }
  
    @Test
    //Test first if statement in process click
    public void testClickOnBoard() {
    	
   
    	User user = new User("Guest", BasicDeckTest.createDeck());
        AI opponent = new AI("Harding", BasicDeckTest.createDeck());
    	Match match = new Match(user, opponent);
    	StateCallback sb = new BasicMatchController();
    	Idle id = new Idle(match, sb);
    	// Testing process click
    	StateNode actual = id.processClick(0, 0);
    	Assert.assertNotNull(actual);
    	//Unable to test Equalities at the top level
    	Match actualMatch = actual.match;
    	Assert.assertEquals(actualMatch, match);
    }
    
    @Test
    //Test click on board where no cards are present
    public void testClickOnBoardYankNoCards() {
    	User user = new User("Guest", BasicDeckTest.createDeck());
        AI opponent = new AI("Harding", BasicDeckTest.createDeck());
    	Match match = new Match(user, opponent);
    	
    	GameState gs = new GameState(match);
    	StateCallback sb = new BasicMatchController();
    	Idle id = new Idle(match, sb);
    	int expectedCount = gs.getUserPyramid().getCards().size();
    	// Testing process click
    	StateNode actual = id.processClick(1, 1);
    	//Test if no card is true
    	Assert.assertEquals(expectedCount, gs.getUserPyramid().size());
    }
    
    
    //@Test
    //Test click on board where a card is present
    public void testClickOnBoardYank() {
    	User user = new User("Guest", BasicDeckTest.createDeck());
        AI opponent = new AI("Harding", BasicDeckTest.createDeck());
    	Match match = new Match(user, opponent);
    	GameState gs = new GameState(match);
    	
    	Card c = new MinionCard("card", "/gameImages/brick.png", "/gameImages/brick.png", 1, 1, 1, 1);
    	ImmutableList<MatchCard> cards = ImmutableList.<MatchCard>of(
    			new MatchCard(c, 0, 0),
    			new MatchCard(c, 1, 1));
    	gs.getUserPyramid().loadCards(cards);
    	StateCallback sb = new BasicMatchController();
    	Idle id = new Idle(match, sb);
    	
    	int expectedCount = gs.getUserPyramid().getCards().size() -1;
    	// Testing process click
    	StateNode actual = id.processClick(1, 1);
    	//Test if object is Yanked
    	Assert.assertEquals(expectedCount, gs.getUserPyramid().size());
    	
    }
    
    @Test
  //Test click on board where a card is present
    public void testproccessMoved() {
    	User user = new User("Guest", BasicDeckTest.createDeck());
        AI opponent = new AI("Harding", BasicDeckTest.createDeck());
    	Match match = new Match(user, opponent);
    	StateCallback sb = new BasicMatchController();
    	Idle i = new Idle(match, sb);
    	StateNode state = i.processMoved(0,0);
        Assert.assertNotNull(state);
    }

	@AfterClass
	public static void cleanUp() throws TimeoutException {
		setupStage((stage) -> stage.close());
	}
}
