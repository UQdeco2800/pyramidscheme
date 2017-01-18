package uq.deco2800.pyramidscheme.clock;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.testfx.framework.junit.ApplicationTest;
import uq.deco2800.pyramidscheme.deck.BasicDeckTest;
import uq.deco2800.pyramidscheme.game.GameManager;
import uq.deco2800.pyramidscheme.player.User;
import uq.deco2800.pyramidscheme.settings.Sound;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.mockito.Mockito.withSettings;

@RunWith(MockitoJUnitRunner.class)
public class ClockTest extends ApplicationTest {

	SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a");
	SimpleDateFormat mdf = new SimpleDateFormat("hh:mm a");

	Clock clock;

	/**
	 * Mock the GameManager class with Mockito
	 */
	GameManager gameManager;

	@Override
	public void start(Stage stage) throws Exception {
		gameManager = Mockito.mock(GameManager.class, withSettings().stubOnly());
		GameManager.getInstance().setPrimaryStage(stage);
		GameManager.getInstance().setUser(new User("Test", BasicDeckTest.createDeck()));
		GameManager.getInstance().setSound(new Sound(true, 0, 0)); // mute sound

		Parent root = FXMLLoader
				.load(getClass().getResource("/fxml/MenuScreen.fxml"));
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	@BeforeClass
	public static void setupHeadless() {
		// Set to headless testing
		//System.setProperty("testfx.robot", "glass");
		//System.setProperty("testfx.headless", "true");
		//System.setProperty("java.awt.headless", "true");
		// JavaFX Rendering options which allow headless testing
		//System.setProperty("prism.order", "sw");
		//System.setProperty("prism.text", "t2k");
	}

	@Before
	public void setUp() {
		this.clock = new Clock();
	}

	//get seconds
	@Test
	public void testGetSeconds() throws InterruptedException{
		Date currentDate = clock.getCurrentDate();
		String currentSeconds = sdf.format(currentDate);
		Assert.assertEquals(currentSeconds, clock.getTimeSeconds().getText());
	}

	//get minutes
	@Test
	public void testGetMinutes() throws InterruptedException{
		Date currentDate = clock.getCurrentDate();
		String currentSeconds = mdf.format(currentDate);
		Assert.assertEquals(currentSeconds, clock.getTimeMinutes().getText());
	}
}
