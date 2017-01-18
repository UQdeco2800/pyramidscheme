package uq.deco2800.pyramidscheme.controllers.statemachine;

import javafx.stage.Stage;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;
import uq.deco2800.pyramidscheme.controllers.BasicMatchController;
import uq.deco2800.pyramidscheme.deck.BasicDeckTest;
import uq.deco2800.pyramidscheme.match.Match;
import uq.deco2800.pyramidscheme.player.AI;
import uq.deco2800.pyramidscheme.player.User;

import java.util.concurrent.TimeoutException;

import static org.testfx.api.FxToolkit.setupStage;

//import uq.deco2800.pyramidscheme.controllers.animations.Animations;
//import uq.deco2800.pyramidscheme.controllers.animations.UserAnimations;


/**
 * Created by Jacob on 17/10/16.
 */

public class MatchAITurnUnitTest extends ApplicationTest {


    @Test
    //Testing Click & Match
    public void testClickOnBoard() {
    	User user = new User("Guest", BasicDeckTest.createDeck());
        AI opponent = new AI("Harding", BasicDeckTest.createDeck());
    	Match match = new Match(user, opponent);
    	StateCallback sb = new BasicMatchController();
    	MatchAITurn ai = new MatchAITurn(match, sb);
    }
    
    @Test
    //Testing Click & Match
    public void testGraveyardDone() {
    	User user = new User("Guest", BasicDeckTest.createDeck());
        AI opponent = new AI("Harding", BasicDeckTest.createDeck());
    	Match match = new Match(user, opponent);
    	BasicMatchController sb = new BasicMatchController();
    	MatchAITurn ai = new MatchAITurn(match, sb);
    	
    	// Refill to set turnOver
    	//ai.aiRefillDone();
    	
    	try {
    		ai.graveyardDone();
    	} catch (NullPointerException npe) {
    		// Success - unable to have non-null match 
    		// in BasicMatchController without starting a game
    		Assert.assertTrue(true);
    		return;
    	}
    	
    	Assert.fail("Excepted NPE to be thrown");
    }
    
    
    @Test
    //Testing Click & Match
    public void testGrindDone() {
    	User user = new User("Guest", BasicDeckTest.createDeck());
        AI opponent = new AI("Harding", BasicDeckTest.createDeck());
    	Match match = new Match(user, opponent);
    	BasicMatchController sb = new BasicMatchController();
    	MatchAITurn ai = new MatchAITurn(match, sb);
    	
    	// Refill to set turnOver
    	//ai.aiRefillDone();
    	
    	try {
    		ai.grindDone();
    	} catch (NullPointerException npe) {
    		// Success - unable to have non-null match 
    		// in BasicMatchController without starting a game
    		Assert.assertTrue(true);
    		return;
    	}
    	
    	Assert.fail("Excepted NPE to be thrown");
    }
    
    @Test
    //Testing Click & Match
    public void aiPlayingDone() {
    	User user = new User("Guest", BasicDeckTest.createDeck());
        AI opponent = new AI("Harding", BasicDeckTest.createDeck());
    	Match match = new Match(user, opponent);
    	BasicMatchController sb = new BasicMatchController();
    	MatchAITurn ai = new MatchAITurn(match, sb);
    	
    	// Refill to set turnOver
    	//ai.aiRefillDone();
    	
    	try {
    		ai.aiPlayingDone();
    	} catch (NullPointerException npe) {
    		// Success - unable to have non-null match 
    		// in BasicMatchController without starting a game
    		Assert.assertTrue(true);
    		return;
    	}
    	
    	Assert.fail("Excepted NPE to be thrown");
    }
    
	@Override
	public void start(Stage stage) throws Exception {

	}

	@AfterClass
	public static void cleanUp() throws TimeoutException {
		setupStage((stage) -> stage.close());
	}
}


 