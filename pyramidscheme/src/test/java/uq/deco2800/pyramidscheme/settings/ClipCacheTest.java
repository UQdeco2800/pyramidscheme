package uq.deco2800.pyramidscheme.settings;

import javafx.scene.media.AudioClip;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by billy on 22/10/16.
 */
public class ClipCacheTest {

	@Test
	public void getClipTest() {
		// NOTE: click.wav must not be removed from resources/sounds
		// could create a test sound that is guaranteed to not be removed
		AudioClip clip = ClipCache.getClip("click.wav");
		Assert.assertNotNull(clip);
	}

	@Test
	public void getClipNotFoundTest() {
		AudioClip clip = ClipCache.getClip("asdfghjkl.wav");
		Assert.assertNull(clip);
	}

	@Test
	public void getClipNullTest() {
		AudioClip clip = ClipCache.getClip(null);
		Assert.assertNull(clip);
	}

	@Test
	public void getClipInvalidTest() {
		AudioClip clip = ClipCache.getClip("##########");
		Assert.assertNull(clip);
	}

	@Test
	public void getClipSameTest() {
		AudioClip clip1 = ClipCache.getClip("click.wav");
		AudioClip clip2 = ClipCache.getClip("click.wav");
		Assert.assertEquals(clip1, clip2);
	}
}
