package uq.deco2800.pyramidscheme.settings;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by billy on 22/10/16.
 */
public class SoundTest {

	private double sfx = 0.78;
	private double background = 0.135;
	private boolean muted = false;
	private String repr = "900 x 600  3:2";

	private Sound createSound() {
		return new Sound(muted, sfx, background);
	}

	@Test
	public void isMutedTest() {
		Sound sound = createSound();
		Assert.assertEquals(muted, sound.isMuted());
	}

	@Test
	public void getSfxTest() {
		Sound sound = createSound();
		Assert.assertEquals(sfx, sound.getSFXVolume(), 0.0005);
	}

	@Test
	public void getSfxUnderTest() {
		Sound sound = new Sound(false, -1, -1);
		Assert.assertEquals(0, sound.getSFXVolume(), 0.0005);
	}

	@Test
	public void getSfxOverTest() {
		Sound sound = new Sound(false, 2, 2);
		Assert.assertEquals(1, sound.getSFXVolume(), 0.0005);
	}

	@Test
	public void getBackgroundTest() {
		Sound sound = createSound();
		Assert.assertEquals(background, sound.getBackgroundVolume(), 0.0005);
	}

	@Test
	public void getBackgroundUnderTest() {
		Sound sound = new Sound(false, -1, -1);
		Assert.assertEquals(0, sound.getBackgroundVolume(), 0.0005);
	}

	@Test
	public void getBackgroundOverTest() {
		Sound sound = new Sound(false, 2, 2);
		Assert.assertEquals(1, sound.getBackgroundVolume(), 0.0005);
	}

}