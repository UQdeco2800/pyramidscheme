package uq.deco2800.pyramidscheme.animations;

import org.junit.Assert;
import org.junit.Test;
import uq.deco2800.pyramidscheme.animations.emitter.Emitter;

/**
 * Created by Louis on 22/10/2016.
 *      Test(s) for evaluating if the Emitter Class is acting the
 *      way it was intended for.
 */
public class EmitterTest {

    /**
     * An emitter test to test that the emitters are created properly and
     * are set to be the emitter they should be
     */
    @Test(timeout = 5000)
    public void testEmitter(){

        Emitter emitterSun = Emitter.RADIANT_SUN;
        Emitter emitterSmite = Emitter.SMITE;
        Emitter emitterToDeath = Emitter.CLOSER_TO_DEATH;
        Emitter emitterScratch = Emitter.SCRATCH;
        Emitter emitterDuckDust = Emitter.DUCK_DUST;
        Emitter emitterAttackUser = Emitter.ATTACK_BY_USER;
        Emitter emitterAttackAi = Emitter.ATTACK_BY_AI;

        // Test to see if this is a valid emitter
        Assert.assertTrue(emitterSun.isEmitter());
        // Test to see if the emitter was correctly assigned
        Assert.assertEquals(emitterSun, Emitter.RADIANT_SUN);

        // Test to see if this is a valid emitter
        Assert.assertTrue(emitterSmite.isEmitter());
        // Test to see if the emitter was correctly assigned
        Assert.assertEquals(emitterSmite, Emitter.SMITE);

        // Test to see if this is a valid emitter
        Assert.assertTrue(emitterToDeath.isEmitter());
        // Test to see if the emitter was correctly assigned
        Assert.assertEquals(emitterToDeath, Emitter.CLOSER_TO_DEATH);

        // Test to see if this is a valid emitter
        Assert.assertTrue(emitterScratch.isEmitter());
        // Test to see if the emitter was correctly assigned
        Assert.assertEquals(emitterScratch, Emitter.SCRATCH);

        // Test to see if this is a valid emitter
        Assert.assertTrue(emitterDuckDust.isEmitter());
        // Test to see if the emitter was correctly assigned
        Assert.assertEquals(emitterDuckDust, Emitter.DUCK_DUST);

        // Test to see if this is a valid emitter
        Assert.assertTrue(emitterAttackUser.isEmitter());
        // Test to see if the emitter was correctly assigned
        Assert.assertEquals(emitterAttackUser, Emitter.ATTACK_BY_USER);

        // Test to see if this is a valid emitter
        Assert.assertTrue(emitterAttackAi.isEmitter());
        // Test to see if the emitter was correctly assigned
        Assert.assertEquals(emitterAttackAi, Emitter.ATTACK_BY_AI);


    }

}