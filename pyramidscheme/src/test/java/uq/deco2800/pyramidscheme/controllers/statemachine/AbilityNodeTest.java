package uq.deco2800.pyramidscheme.controllers.statemachine;

import javafx.stage.Stage;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.testfx.framework.junit.ApplicationTest;
import uq.deco2800.pyramidscheme.board.RecTile;
import uq.deco2800.pyramidscheme.cards.supercards.MinionCard;
import uq.deco2800.pyramidscheme.champions.abilities.Ability;
import uq.deco2800.pyramidscheme.champions.abilities.MinionRevive;
import uq.deco2800.pyramidscheme.champions.abilities.MinionSummon;
import uq.deco2800.pyramidscheme.controllers.BasicMatchController;
import uq.deco2800.pyramidscheme.deck.BasicDeckTest;
import uq.deco2800.pyramidscheme.duckdust.DuckDustPool;
import uq.deco2800.pyramidscheme.game.GameManager;
import uq.deco2800.pyramidscheme.game.GameState;
import uq.deco2800.pyramidscheme.match.Match;
import uq.deco2800.pyramidscheme.player.AI;
import uq.deco2800.pyramidscheme.player.User;
import uq.deco2800.pyramidscheme.pyramid.PyramidType;

import java.util.concurrent.TimeoutException;

import static org.mockito.Mockito.withSettings;
import static org.testfx.api.FxToolkit.setupStage;

//import uq.deco2800.pyramidscheme.controllers.animations.Animations;
//import uq.deco2800.pyramidscheme.controllers.animations.UserAnimations;


/**
 * Created by Jacob on 17/10/16.
 */
@RunWith(MockitoJUnitRunner.class)
public class AbilityNodeTest extends ApplicationTest {

    /**
     * Mock the GameManager class with Mockito
     */
    GameManager gameManager;
    RecTile playerTile;

    private Stage stage;
    
    @Override
    //Set up and Mock GameManager at an Instance
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
    	//Check That Ability node is not Null
    	//Could not run further Tests due to class restrictions
        User user = new User("Guest", BasicDeckTest.createDeck());
        AI opponent = new AI("Harding", BasicDeckTest.createDeck());
        Match match = new Match(user, opponent);
        StateCallback sb = new BasicMatchController();
        DuckDustPool ddp = new DuckDustPool(3);
    	AbilityNode an = new AbilityNode(match, sb, 3, ddp);
    	Assert.assertNotNull(an);
    }
    
    @Test
    //Testing Click & Match
    public void testClickOnBoard() {
    	User user = new User("Guest", BasicDeckTest.createDeck());
        AI opponent = new AI("Harding", BasicDeckTest.createDeck());
    	Match match = new Match(user, opponent);
    	StateCallback sb = new BasicMatchController();
    	DuckDustPool ddp = new DuckDustPool(3);
    	AbilityNode an = new AbilityNode(match, sb, 3, ddp);
    	// Testing process click
    	StateNode actual = an.processClick(0, 0);
    	Assert.assertNotNull(actual);
    	//Test Match
    	Match actualMatch = actual.match;
    	Assert.assertEquals(actualMatch, match);
    }
    	
    @Test
    //Testing click when DeadTiles is empty
    public void testClickOnBoardEmpty() {
    	User user = new User("Guest", BasicDeckTest.createDeck());
        AI opponent = new AI("Harding", BasicDeckTest.createDeck());
    	Match match = new Match(user, opponent);
    	GameState gs = new GameState(match);
    	StateCallback sb = new BasicMatchController();
    	DuckDustPool ddp = new DuckDustPool(3);
    	AbilityNode an = new AbilityNode(match, sb, 3, ddp);
    	// Testing process click
    	StateNode actual = an.processClick(1, 1);
    	Assert.assertNotNull(actual);
    	//Check If animation is Empty
    	Assert.assertTrue(gs.getBoard().getDeadTiles().isEmpty());
    
    }
    @Test
    //Test checks for available spaces are accurate
    public void CheckAbilitiesMinionRevive() {
    	User user = new User("Guest", BasicDeckTest.createDeck());
        AI opponent = new AI("Harding", BasicDeckTest.createDeck());
    	Match match = new Match(user, opponent);
    	StateCallback sb = new BasicMatchController();
    	DuckDustPool ddp = new DuckDustPool(3);
    	AbilityNode an = new AbilityNode(match, sb, 3, ddp);
    	StateNode expected = new Idle(match, sb);
    	Ability a = new MinionRevive("a", "/gameImages/brick.png", 3);
    	StateNode actual = an.checkAbilities(a);
    	Assert.assertEquals(expected.match, actual.match);
    	//MinionSummon(Str, int, img)
    	//MinionCard(str, img, str, int, int, int, int)
    	a = new MinionSummon("a", "/gameImages/brick.png", 3, new MinionCard("a", "/gameImages/brick.png", "a", 1, 1, 1, 1));
    	
    }

	@AfterClass
	public static void cleanUp() throws TimeoutException {
		setupStage((stage) -> stage.close());
	}

}
