package uq.deco2800.pyramidscheme.animations.emitter;


import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlendMode;
import javafx.scene.paint.Paint;

/**
 * Created by Louis on 18/10/2016.
 * <p>
 * The following class defines a Particle.
 * <p>
 * The Class is set using the following variables;
 * - x: 	        x location of the particle.
 * - y:	        y location of the particle.
 * - velocity: 	velocity of the particle in the x and y direction.
 * - radius: 	    radius of the particle.
 * - life:         life of the particle.
 * - decay:        decay rate of the particle.
 * - color:        color of the particle.
 * - blendMode:    the type of blendMode used for the particle.
 */
public class Particle {

    private double x;
    private double y;

    private Point2D velocity;

    private double radius;
    private double life = 1.0;
    private double decay;

    private Paint color;
    private BlendMode blendMode;

    /**
     * Particle is a method which takes the x and y location to determine where
     * the Particle should 'spawn', with a velocity which determines the direction
     * and speed of the particle. It also takes a radius to change the size of
     * the required Particles including an expireTime to give the particles a
     * 'lifetime' so that they dissapear from the screen. Particle can also be set
     * a colour with a special blendMode to determine other colour effects.
     *
     * @param x          the x location of the particle
     * @param y          the y location of the particle
     * @param velocity   the velocity of the particle in x and y direction
     * @param radius     the size of the particle
     * @param expireTime the expireTime of a particle
     * @param color      color of the particle
     * @param blendMode  mode set for the particle
     */
    public Particle(double x, double y, Point2D velocity, double radius,
                    double expireTime, Paint color, BlendMode blendMode) {
        this.x = x;
        this.y = y;
        this.velocity = velocity;
        this.radius = radius;

        this.color = color;
        this.blendMode = blendMode;

        this.decay = 0.016 / expireTime;
    }

    /**
     * isAlive is a method to determine the wether or not the Particle
     * is below it's lifetime and should be removed/made transparrent on
     * the screen.
     *
     * @return false if life is less than 0.
     */
    public boolean isAlive() {
        return life > 0;
    }

    /**
     * Update is a method to update the Particles x and y location as well as
     * decrementing the Particles life each time it's called.
     */
    public void update() {
        x += velocity.getX();
        y += velocity.getY();

        life -= decay;
    }

    /**
     * Render is a method to render the particle to the graphicscontext
     * being used (ie. draw it). It also makes the particle transparrent
     * as the life continues to decrement to 0.
     *
     * @param g GraphicsContext being used.
     */
    public void render(GraphicsContext g) {
        // to do with transparency when life hits 0, it's fully transparent
        g.setGlobalAlpha(life);
        g.setGlobalBlendMode(blendMode);
        g.setFill(color);
        g.fillOval(x, y, radius, radius);
    }

    /**
     * A helper method to be used for testing to return the current position of
     * the particle in x and y location
     *
     * @return Point2D with an x and y locaiton of the particle
     */
    public Point2D getPosition() {
        return new Point2D(x, y);
    }

    /**
     * A helper method to be used for testing to return the current velocity of
     * the particle
     *
     * @return Point2D with an x and y velocity of the particle
     */
    public Point2D getVelocity() {
        return velocity;
    }

    /**
     * A helper method to be used for testing to return the current radius of
     * the particle
     *
     * @return Double radius of the particle
     */
    public Double getRadius() {
        return radius;
    }

    /**
     * A helper method to be used for testing to return the current life of
     * the particle
     *
     * @return Double life of the particle
     */
    public Double getLife() {
        return life;
    }

    /**
     * Returns a string of the form;
     * <p>
     * "The Particle is located at: Point2D traveling at: velocity"
     * <p>
     * Where Point2D is the location of the Particle being used and velocity
     * is the velocity of the particle.
     */
    @Override
    public String toString() {
        // Set up a StringBuilder
        StringBuilder sBuilder = new StringBuilder();

        // adds to the stringbuilder
        sBuilder.append("The Particle is located at: " + getPosition().getX() + " " + getPosition().getY() +
                " traveling at: " + velocity.getX() + " " + velocity.getY());
        return sBuilder.toString(); // returns the string
    }


    /**
     * Returns true if and only if the given object is a Particle with the
     * same Particle type, origin, velocity, life, color, radius.
     * <p>
     * (Any two Partiles are considered to be the same when they are equal
     * according to their equals method.)
     */
    @Override
    public boolean equals(Object object) {

        //verify's if it's an instanceof Pyramid
        if (!(object instanceof Particle)) {
            return false;
        }

        //Cast down as it's passed "instanceof" test
        Particle p = (Particle) object;

        int r = p.getRadius().intValue();
        int l = p.getLife().intValue();

        //make sonar
        int thisRadius = (int) radius;
        int thisLife = (int) life;

        return velocity.equals(p.getVelocity()) && (thisRadius == r) &&
                thisLife == l && p.getPosition().equals(this.getPosition());

    }


    /**
     * Returns the hash code of this Particle.
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        // for combining hash codes
        final int prime = 27;

        // in a list which can be easily accessed
        int hashResult = 1;

        hashResult = prime * hashResult + velocity.hashCode();

        return hashResult;
    }
}

