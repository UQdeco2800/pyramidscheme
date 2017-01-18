package uq.deco2800.pyramidscheme.story;

import org.junit.Assert;
import org.junit.Test;
/**
 * @author Bianca
 * Tests to see if the dialogue class is behaving the way it is supposed to.
 */
public class DialogueTest {

    
    private Dialogue d1 = new Dialogue("duckcell", "Quack");
    private Dialogue d2 = new  Dialogue("Brockcell","hello");
    private Dialogue d3 = new  Dialogue("duckcell", "wak");
    
    @Test
    public void getImageTest(){
        Assert.assertEquals(d1.getImage(),"duckcell");
        Assert.assertEquals(d2.getImage(),"Brockcell");
        Assert.assertEquals(d3.getImage(),"duckcell");
    }
    @Test
    public void getLineTest(){
        Assert.assertEquals(d1.getLine(),"Quack");
        Assert.assertEquals(d2.getLine(),"hello");
        Assert.assertEquals(d3.getLine(),"wak");
    }

    @Test
    public void compareImage(){
        Assert.assertFalse(d1.compareImage(d2.getImage()));
        Assert.assertTrue(d1.compareImage(d3.getImage()));
    }


}
