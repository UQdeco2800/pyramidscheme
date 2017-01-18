package uq.deco2800.pyramidscheme.animations.emitter;


import javafx.geometry.Point2D;
import javafx.scene.effect.BlendMode;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import uq.deco2800.pyramidscheme.cards.supercards.Card;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Created by Louis on 18/10/2016.
 * <p>
 * The following enum defines Emitters which contains particles with respective x and y coordinates
 * <p>
 * The enum contains the following Emitters;
 * - SMITE: 	            Hero Ability, isEmitter.
 * - RADIANT_SUN:	        Hero Ability, isEmitter.
 * - ARISE: 		        Hero Ability, !isEmitter.
 * - CLOSER_TO_DEATH: 	    Hero Ability, isEmitter.
 * - COMMANDING_QUACK:     Hero Ability, !isEmitter.
 * - SCRATCH:              Hero Ability, isEmitter.
 * - TAUNT:                Hero Ability, !isEmitter.
 * - ENRAGE:               Hero Ability, !isEmitter.
 * - DUCK_DUST:            Grinder animation, isEmitter.
 * - ATTACK_BY_USER        USER Card attacking animation, isEmitter.
 * - ATTACK_BY_AI          AI Card attacking animation, isEmitter.
 */
public enum Emitter {
    SMITE(true),
    RADIANT_SUN(true),
    ARISE(false),
    CLOSER_TO_DEATH(true),
    COMMANDING_QUACK(false),
    SCRATCH(true),
    TAUNT(false),
    ENRAGE(false),
    DUCK_DUST(true),
    ATTACK_BY_USER(true),
    ATTACK_BY_AI(true);

    private boolean isEmitter;

    /**
     * A constructor for the Emitter enum that defines whether an instance is an emitter or not.
     *
     * @param isEmitter true if the instance is an emitter, otherwise false.
     */
    Emitter(boolean isEmitter) {
        this.isEmitter = isEmitter;
    }

    /**
     * A method which generates a number of Particles to be used in a specific Emitter animation/situation.
     *
     * @param x location of Emitter
     * @param y location of Emitter
     * @return a list of particles conforming to the Emitter which was set
     */
    public List<Particle> emit(double x, double y) {
        List<Particle> particles = new ArrayList<>();
        int numParticles = 150;
        for (int i = 0; i < numParticles; i++) {

            // Java is weird, each case expression doesn't have its own variable
            // scope. They have to be declared here if we don't want a disaster
            Particle particle = null;

            // velocity of the partiles (ie. Direction)
            Function<Integer, Point2D> velocity;
            double radius;

            // lifetime of the particles
            double expireTime;
            Paint color;

            switch (this) {

                case SMITE:

                    velocity = seed -> new Point2D((Math.random() > 0.5 ? 1 : -1) * 1.6, (Math.random() > 0.5 ? 1 : -1) * Math.random() * 0.6);
                    radius = 7;
                    expireTime = 0.4;

                    particle = new Particle(x, y, velocity.apply(0), radius, expireTime, Color.rgb(225, 40, 45), BlendMode.DARKEN);
                    break;

                case ARISE:
                    break;

                case CLOSER_TO_DEATH:

                    velocity = seed -> new Point2D((Math.random() - 0.5) * Math.random() * 2.2, (Math.random() > 0.5 ? 1 : -1) * Math.random() * 1.3);
                    radius = 3.0;
                    expireTime = 1.0;

                    if (i % 3 == 0) {
                        particle = new Particle(x, y, velocity.apply(0), radius, expireTime, Color.rgb(153, 0, 76), BlendMode.DARKEN);
                    } else if (i % 3 == 1) {
                        particle = new Particle(x, y, velocity.apply(0), radius, expireTime, Color.rgb(192, 42, 192), BlendMode.DARKEN);
                    } else {
                        particle = new Particle(x, y, velocity.apply(0), radius, expireTime, Color.rgb(0, 0, 0), BlendMode.DARKEN);
                    }
                    break;

                case COMMANDING_QUACK:
                    break;

                case SCRATCH:

                    velocity = seed -> new Point2D(((Math.random() * (1.1 - 1)) + 1) * 2.2, ((Math.random() * (1.3 - 1.2)) + 1.2) * 3);
                    radius = 4.0;
                    expireTime = 0.5;
                    color = Color.rgb(255, 0, 0);

                    if (i % 4 == 0) {
                        particle = new Particle(x - Card.getCardWidth() / 2, y - Card.getCardHeight() / 2, velocity.apply(0), radius, expireTime, color, BlendMode.DARKEN);
                    } else if (i % 4 == 1) {
                        particle = new Particle(x + 5 - Card.getCardWidth() / 2, y - (Card.getCardHeight() / 2 - 5), velocity.apply(0), radius, expireTime, color, BlendMode.DARKEN);
                    } else if (i % 4 == 2) {
                        particle = new Particle(x + 7 - Card.getCardWidth() / 2, y - (Card.getCardHeight() / 2 - 10), velocity.apply(0), radius, expireTime, color, BlendMode.DARKEN);
                    } else {
                        particle = new Particle(x + 4 - Card.getCardWidth() / 2, y - (Card.getCardHeight() / 2 - 15), velocity.apply(0), radius, expireTime, color, BlendMode.DARKEN);
                    }

                    break;

                case TAUNT:
                    break;

                case ENRAGE:
                    break;

                case RADIANT_SUN:

                    particle = new Particle(x, y - Card.getCardHeight() / 2, new Point2D((Math.random() - 0.5) * Math.random() * 2.2, Math.random() * 2.3),
                            4, 1.0, Color.rgb(255, 255, 51), BlendMode.LIGHTEN);

                    break;
                case DUCK_DUST:

                    velocity = seed -> new Point2D(((Math.random() * (1 - 0.3)) + 0.3) * seed, ((Math.random() * (0.8 - 0.5)) + 0.5) * Math.random() * (-seed + 2));
                    expireTime = 1.0;
                    color = Color.rgb(255, 0, 0);

                    if (i % 4 == 0) {
                        particle = new Particle(x, y, velocity.apply(4), 7, expireTime, color, BlendMode.DARKEN);
                    } else if (i % 4 == 1) {
                        particle = new Particle(x, y, velocity.apply(6), 5, expireTime, color, BlendMode.DARKEN);
                    } else if (i % 4 == 2) {
                        particle = new Particle(x, y, velocity.apply(5), 3, expireTime, color, BlendMode.DARKEN);
                    } else {
                        particle = new Particle(x, y, velocity.apply(3), 4, expireTime, color, BlendMode.DARKEN);
                    }
                    break;

                case ATTACK_BY_USER:

                    particle = new Particle(x, y, new Point2D((Math.random() - 0.5) * Math.random() * 2.2, Math.random() * -2.3),
                            4, 1.0, Color.rgb(255, 0, 0), BlendMode.DARKEN);

                    break;
                case ATTACK_BY_AI:

                    particle = new Particle(x, y, new Point2D((Math.random() - 0.5) * Math.random() * 2.2, Math.random() * 2.3),
                            4, 1.0, Color.rgb(255, 0, 0), BlendMode.DARKEN);

                    break;

                default:
                    throw new NullPointerException("Invalid particle");
            }

            particles.add(particle);
        }
        return particles;
    }

    /**
     * A method which returns true when an Emitter is set and ready to be called.
     *
     * @return true for valid Emitter.
     */
    public boolean isEmitter() {
        return isEmitter;
    }

}
