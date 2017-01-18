package uq.deco2800.pyramidscheme.addinfo;

import static org.testfx.api.FxToolkit.setupStage;

import java.util.concurrent.TimeoutException;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.testfx.framework.junit.ApplicationTest;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;
import uq.deco2800.pyramidscheme.cards.supercards.MinionCard;
import uq.deco2800.pyramidscheme.game.GameState;
import uq.deco2800.pyramidscheme.match.Match;
import uq.deco2800.pyramidscheme.match.MatchCard;
import uq.deco2800.pyramidscheme.match.MatchDeck;

/**
 * Test cases for the AddInfo class.
 *
 * @author Jaymes Branch 
 */
@RunWith(MockitoJUnitRunner.class)
public class AddInfoTest extends ApplicationTest{
	
	@Mock
	AddInfo addinfo;
    
    @Override
    public void start(Stage stage) throws Exception {
        stage.show();
    }
    
    @AfterClass
    public static void cleanUp() throws TimeoutException {
        setupStage((stage) -> stage.close());
    }
	
	@Test
	public void testDrawInfo() throws Exception {
	
		MatchCard exploDuck = new MatchCard(MinionCard.get("Exploduck"), 0, 0);
	    MatchCard mummyDuck = new MatchCard(MinionCard.get("Mummy Duck"), 0, 0);
		AddInfo cardExploduck = new AddInfo(exploDuck);
		AddInfo cardMummyDuck = new AddInfo(mummyDuck);	
	
	Canvas canvas = new Canvas(10, 10);
    GraphicsContext gc = canvas.getGraphicsContext2D();
    //True for Exploduck
    Assert.assertTrue(cardExploduck.drawInfo(gc, 667, 90, 200, 297)&& (cardExploduck.getActionImage()!=null));
    //False for 
    Assert.assertFalse(cardMummyDuck.drawInfo(gc, 667, 90, 200, 297) && (cardMummyDuck.getActionImage()!=null));
	}
	
	@Test
	public void testGetters() throws Exception {
		MatchCard exploDuck = new MatchCard(MinionCard.get("Exploduck"), 0, 0);
	    MatchCard mummyDuck = new MatchCard(MinionCard.get("Mummy Duck"), 0, 0);
		AddInfo cardExploduck = new AddInfo(exploDuck);
		AddInfo cardMummyDuck = new AddInfo(mummyDuck);
		//Should return no action image for mummyduck
		Assert.assertNull(cardMummyDuck.getActionImage());
		
		//Should return for blank AddInfo
		Assert.assertNull(addinfo.getActionImage());
		
		//Non null will mean image has been returned successfully 
		Assert.assertNotNull(cardExploduck.getActionImage());
		Assert.assertNotNull(cardExploduck.getDustImage());
		}
}

