package uq.deco2800.pyramidscheme.animations;

import javafx.geometry.Point2D;
import javafx.scene.effect.BlendMode;
import javafx.scene.paint.Color;
import org.junit.Assert;
import org.junit.Test;
import uq.deco2800.pyramidscheme.animations.emitter.Particle;

/**
 * Created by Louis on 22/10/2016.
 *      Tests for evaluating if the Particle Class is acting the
 *      way it was intended for and that all the helper methods
 *      function properly.
 */
public class ParticleTest {

    private int x = 10;
    private int y = 10;
    private int radius = 2;
    private double expireTime = 0.064;
    private Point2D velocity = new Point2D(2, 2);

    /**
     * Creates a normal single Partile to run tests with.
     */
    public Particle createParticle() {
        Particle p = new Particle(x, y, velocity, radius, expireTime, Color.rgb(225, 40, 45), BlendMode.DARKEN);
        return p;
    }

    public Particle createDeadParticle() {
        // set the expireTime to be 0.016 so that the decay time is equal to 1 and since the
        // life of a particle is 1.0, after one update call the particle life should be 0.
        Particle p = new Particle(x, y, velocity, radius, 0.016, Color.rgb(225, 40, 45), BlendMode.DARKEN);
        return p;
    }

    /**
     * Test the small helper method within the particle class to determine that it returns the correct
     * x and y location of the particle created.
     */
    @Test(timeout = 5000)
    public void testVelocity(){
        Particle pTest = createParticle();

        Double pTestX = pTest.getVelocity().getX();
        Double pTestY = pTest.getVelocity().getY();

        Assert.assertTrue(pTestX == 2);
        Assert.assertTrue(pTestY == 2);

        Assert.assertFalse(pTestX == 3);
        Assert.assertFalse(pTestY == 3);
    }


    /**
     * Test the small helper method within the particle class to determine that it returns the correct
     * x and y location of the particle created.
     */
    @Test(timeout = 5000)
    public void testPosition(){
        Particle pTest = createParticle();

        Double pTestX = pTest.getPosition().getX();
        Double pTestY = pTest.getPosition().getY();

        Assert.assertTrue(pTestX == 10);
        Assert.assertTrue(pTestY == 10);

        Assert.assertFalse(pTestX == 11);
        Assert.assertFalse(pTestY == 11);
    }

    /**
     * Test the method update() to determine whether or not the particles location and
     * life is being updated correctly based off the values set for the particle in
     * expireTime, velocity and x and y values.
     */
    @Test(timeout = 5000)
    public void testUpdate() {
        Particle pTest = createParticle();

        // For the update to work in decrementing the life of the particle
        // the life should be 0 after 4 update calls.

        for (int i = 0; i < 3; i++){
            // Particle should be alive for the first 3 calls of update
            Assert.assertTrue(pTest.isAlive());
            pTest.update();
        }
        // the particle should have moved 3 times, and since the velocity is 2 in the
        // x and y direction and the origin of the particle is at 10, 10 the new
        // x and y should be 16, 16.

        Double pTestX = pTest.getPosition().getX();
        Double pTestY = pTest.getPosition().getY();

        Assert.assertTrue(pTestX == 16);
        Assert.assertTrue(pTestY == 16);

        pTest.update();

        // particle life should be 0 now
        Assert.assertFalse(pTest.isAlive());
        Assert.assertTrue(!pTest.isAlive());
    }

    /**
     * Test the method isAlive() to ensure that it returns the correct booleans depending
     * on the life of the particle.
     */
    @Test(timeout = 5000)
    public void testAlive() {
        Particle pTest = createParticle();

        // should be alive since it was created and no updates were run
        Assert.assertTrue(pTest.isAlive());

        Particle pDead = createDeadParticle();

        // Particle should be dead after the update call
        pDead.update();

        // set particle life to be 0 (ie. dead) and check if it returns false
        Assert.assertFalse(pDead.isAlive());
        Assert.assertTrue(!pDead.isAlive());

        Assert.assertTrue(pDead.getLife() <= 0);
        Assert.assertFalse(!(pDead.getLife() <= 0));
    }

    /** Check that the equals and hashCode methods are correct. */
    @Test(timeout = 5000)
    public void testEquals() {


        Particle p1 =  new Particle(0, 0, new Point2D(2, 2), 1, 0.1, Color.RED, BlendMode.DARKEN);
        Particle p2 = new Particle(0, 0, new Point2D(2, 2), 1, 0.1, Color.RED, BlendMode.DARKEN);
        Object p3 = new Particle(0, 0, new Point2D(2, 2), 1, 0.1, Color.RED, BlendMode.DARKEN);
        Particle p4 = new Particle(0, 0, new Point2D(4, 4), 1, 0.1, Color.RED, BlendMode.DARKEN);
        Particle p5 = new Particle(8, 8, new Point2D(2, 2), 1, 0.1, Color.RED, BlendMode.DARKEN);
        String string = new String("random");

        // equals cases
        Assert.assertEquals("Equals should return true", p1, p2);
        Assert.assertEquals("Equal objects should have equal hash codes",
                p1.hashCode(), p2.hashCode());
        Assert.assertEquals("Equals should return true", p1, p3);
        Assert.assertEquals("Equals should return true for the string test",
                p1.toString(), "The Particle is located at: 0.0 0.0 traveling at: 2.0 2.0");

        // unequal cases
        Assert.assertFalse("Equals should return false", p1.equals(p4));
        Assert.assertFalse("Equals should return false", p1.equals(p5));
        Assert.assertFalse("Equals should return false", p1.equals(string));
    }

}
