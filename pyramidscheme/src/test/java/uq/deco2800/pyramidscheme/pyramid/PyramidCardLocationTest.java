package uq.deco2800.pyramidscheme.pyramid;

import org.junit.Assert;
import org.junit.Test;
import uq.deco2800.pyramidscheme.cards.BasicMinion;
import uq.deco2800.pyramidscheme.match.MatchCard;

import static org.junit.Assert.*;

/**
 * Created by nick on 14/09/2016.
 */
public class PyramidCardLocationTest {
    PyramidCardLocation pcl = new PyramidCardLocation(0,0,0);

    @Test(timeout = 5000)
    public void testSetters() {
        // Test set x
        pcl.setX(1);
        Assert.assertEquals(1,pcl.getX());

        // Test set Y
        pcl.setY(1);
        Assert.assertEquals(1,pcl.getY());

        // Test set z
        pcl.setZ(1);
        Assert.assertEquals(1,pcl.getZ());
    }
    @Test(timeout = 5000)
    public void toStringTest() {
        String string = "PyramidCardLocation: x=0, y=0, z=0";
        Assert.assertEquals(pcl.toString(),string);
    }

    @Test(timeout = 5000)
    public void equalsAndHashCodeTest() {
        // Reference equality
        Assert.assertTrue(pcl.equals(pcl));

        // Non-compatible class
        MatchCard card = new MatchCard(new BasicMinion(),0,0);
        Assert.assertFalse(pcl.equals(card));

        // Comparison on null
        Assert.assertFalse(pcl.equals(null));

        // Different z index
        PyramidCardLocation pcl2 = new PyramidCardLocation(0,0,1);
        Assert.assertFalse(pcl.equals(pcl2));
        Assert.assertNotEquals(pcl.hashCode(),pcl2.hashCode());

        // Different x location
        PyramidCardLocation pcl3 = new PyramidCardLocation(1,0,0);
        Assert.assertFalse(pcl.equals(pcl3));
        Assert.assertNotEquals(pcl.hashCode(),pcl3.hashCode());

        // Different y location
        PyramidCardLocation pcl4 = new PyramidCardLocation(0,1,0);
        Assert.assertFalse(pcl.equals(pcl4));
        Assert.assertNotEquals(pcl.hashCode(),pcl4.hashCode());

        // Test same location
        pcl2.setZ(0);
        Assert.assertTrue(pcl.equals(pcl2));
        Assert.assertEquals(pcl.hashCode(),pcl2.hashCode());
    }
}