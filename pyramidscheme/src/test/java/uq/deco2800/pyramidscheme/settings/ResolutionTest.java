package uq.deco2800.pyramidscheme.settings;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by billy on 22/10/16.
 */
public class ResolutionTest {

	private double width = 900.0;
	private double height = 600.0;
	private String ratio = "3:2";
	private String repr = "900 x 600  3:2";

	private Resolution createRes() {
		return new Resolution(width, height, ratio);
	}

	@Test
	public void getWidthTest() {
		Resolution res = createRes();
		Assert.assertEquals(width, res.getWidth(), 0.0005);
	}

	@Test
	public void getHeightTest() {
		Resolution res = createRes();
		Assert.assertEquals(height, res.getHeight(), 0.0005);
	}

	@Test
	public void getStringTest() {
		Resolution res = createRes();
		Assert.assertTrue(repr.equals(res.toString()));
	}

}
