package uq.deco2800.pyramidscheme.story;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

/**
 *  @author Bianca
 * Tests to see if the CutsceneModel class is behaving the way it is supposed to.
 */
 
public class CutsceneModelTest {
    @Test
    public void loadTest() throws IOException{
        CutsceneModel scene;
        try {
            scene = new CutsceneModel("0t");

        } catch (IOException io){
            throw new IOException("File failed to load: "+ io);
        }

        Assert.assertEquals(scene.getImage(), "image");
        Assert.assertEquals(scene.getLine(), "dialoguea");
        Assert.assertEquals(scene.nextImage(), "image");
        Assert.assertEquals(scene.textInScene(), 6);

    }

}
