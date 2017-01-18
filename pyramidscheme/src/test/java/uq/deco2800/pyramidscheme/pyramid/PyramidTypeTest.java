package uq.deco2800.pyramidscheme.pyramid;

import javafx.geometry.Point2D;
import org.junit.Assert;
import org.junit.Test;
import uq.deco2800.pyramidscheme.cards.BasicMinion;
import uq.deco2800.pyramidscheme.match.MatchCard;

import java.util.ArrayList;

/**
 * Created by nick on 14/09/2016.
 * Edited by Louis on 17/09/2016.
 */
public class PyramidTypeTest {

    private final int CARD_HEIGHT = 90;
    private final int CARD_WIDTH = 65;
    private final int ROW_SPACING = CARD_HEIGHT - 60;
    private final int CARD_SPACING = CARD_WIDTH + 5;


    /**
     * Testing if the getSize() method would return the correct size for all the
     * pyramids involved in PyramidType
     */
    @Test(timeout = 5000)
    public void testGetSize() {
        //Test Triangle
        PyramidType pyramidType = PyramidType.TRIANGLE;
        Assert.assertEquals(pyramidType.getSize(),6);

        //Test inverted triangle
        pyramidType = PyramidType.INVERTED;
        Assert.assertEquals(pyramidType.getSize(),6);

        //Test Diamond
        pyramidType = PyramidType.DIAMOND;
        Assert.assertEquals(pyramidType.getSize(),6);

        //Test Square
        pyramidType = PyramidType.SQUARE;
        Assert.assertEquals(pyramidType.getSize(),6);

        // Test rectangle
        pyramidType = PyramidType.RECTANGLE;
        Assert.assertEquals(pyramidType.getSize(),6);

        // Test triangle corner
        pyramidType = PyramidType.TRIANGLE_CORNER;
        Assert.assertEquals(pyramidType.getSize(),6);

        // Test two pyramids
        pyramidType = PyramidType.TWO_PYRAMIDS;
        Assert.assertEquals(pyramidType.getSize(),6);

        // Test two inverse
        pyramidType = PyramidType.TWO_INVERSE;
        Assert.assertEquals(pyramidType.getSize(),6);
    }


    /**
     * Testing whether or not the Pyramid Shapes can be generated correctly with
     * no issues.
     */
    @Test(timeout = 5000)
    public void testGenerateShape() {
        // Test Triangle generation
        PyramidType pyramidType = PyramidType.TRIANGLE;
        Assert.assertNotNull(pyramidType.generateShape());

        // Test Diamond generation
        pyramidType = PyramidType.DIAMOND;
        Assert.assertNotNull(pyramidType.generateShape());

        // Test Inverted generation
        pyramidType = PyramidType.INVERTED;
        Assert.assertNotNull(pyramidType.generateShape());

        // Test square generation
        pyramidType = PyramidType.SQUARE;
        Assert.assertNotNull(pyramidType.generateShape());

        // Test rectangle generation
        pyramidType = PyramidType.RECTANGLE;
        Assert.assertNotNull(pyramidType.generateShape());

        // Test triangle corner generation
        pyramidType = PyramidType.TRIANGLE_CORNER;
        Assert.assertNotNull(pyramidType.generateShape());

        // Test two pyramids generation
        pyramidType = PyramidType.TWO_PYRAMIDS;
        Assert.assertNotNull(pyramidType.generateShape());

        // Test two inverse generation
        pyramidType = PyramidType.TWO_INVERSE;
        Assert.assertNotNull(pyramidType.generateShape());


    }

    /**
     * Testing that the each pyramid shape can/is being arranged properly and that
     * there's no left over cards afterwards.
     */
    @Test(timeout = 5000)
    public void testArrange() {

        // Test the triangle pyramid
        PyramidType pyramidType = PyramidType.TRIANGLE;
        ArrayList<PyramidCard> cards = new ArrayList<>();
        // Generate 6 pyramid cards
        for (int i = 0; i < 6; i++) {
            cards.add(new PyramidCard(new MatchCard(new BasicMinion(),0,0)));
        }
        pyramidType.arrange(new Point2D(1,1), cards);

        // Check that there are no more cards at 0,0
        for (int i = 0; i < 6; i++){
            Assert.assertNotEquals(cards.get(i).getPyramidCardLocation(),new Point2D(0,0));
        }


        // Test the inverted pyramid
        PyramidType pyramidType2 = PyramidType.INVERTED;
        ArrayList<PyramidCard> cards2 = new ArrayList<>();
        // Generate 6 pyramid cards
        for (int i = 0; i < 6; i++) {
            cards2.add(new PyramidCard(new MatchCard(new BasicMinion(),0,0)));
        }
        pyramidType2.arrange(new Point2D(1,1), cards2);

        // Check that there are no more cards at 0,0
        for (int i = 0; i < 6; i++){
            Assert.assertNotEquals(cards2.get(i).getPyramidCardLocation(),new Point2D(0,0));
        }


        // Test the diamond pyramid
        PyramidType pyramidType3 = PyramidType.DIAMOND;
        ArrayList<PyramidCard> cards3 = new ArrayList<>();
        // Generate 6 pyramid cards
        for (int i = 0; i < 6; i++) {
            cards3.add(new PyramidCard(new MatchCard(new BasicMinion(),0,0)));
        }
        pyramidType3.arrange(new Point2D(1,1), cards2);

        // Check that there are no more cards at 0,0
        for (int i = 0; i < 6; i++){
            Assert.assertNotEquals(cards3.get(i).getPyramidCardLocation(),new Point2D(0,0));
        }


        // Test the square pyramid
        PyramidType pyramidType4 = PyramidType.SQUARE;
        ArrayList<PyramidCard> cards4 = new ArrayList<>();
        // Generate 6 pyramid cards
        for (int i = 0; i < 6; i++) {
            cards4.add(new PyramidCard(new MatchCard(new BasicMinion(),0,0)));
        }
        pyramidType4.arrange(new Point2D(1,1), cards2);

        // Check that there are no more cards at 0,0
        for (int i = 0; i < 6; i++){
            Assert.assertNotEquals(cards4.get(i).getPyramidCardLocation(),new Point2D(0,0));
        }


        // Test the rectangle pyramid
        PyramidType pyramidType5 = PyramidType.RECTANGLE;
        ArrayList<PyramidCard> cards5 = new ArrayList<>();
        // Generate 6 pyramid cards
        for (int i = 0; i < 6; i++) {
            cards5.add(new PyramidCard(new MatchCard(new BasicMinion(),0,0)));
        }
        pyramidType5.arrange(new Point2D(1,1), cards2);

        // Check that there are no more cards at 0,0
        for (int i = 0; i < 6; i++){
            Assert.assertNotEquals(cards5.get(i).getPyramidCardLocation(),new Point2D(0,0));
        }


        // Test the triangle corner pyramid
        PyramidType pyramidType6 = PyramidType.TRIANGLE_CORNER;
        ArrayList<PyramidCard> cards6 = new ArrayList<>();
        // Generate 6 pyramid cards
        for (int i = 0; i < 6; i++) {
            cards6.add(new PyramidCard(new MatchCard(new BasicMinion(),0,0)));
        }
        pyramidType6.arrange(new Point2D(1,1), cards2);

        // Check that there are no more cards at 0,0
        for (int i = 0; i < 6; i++){
            Assert.assertNotEquals(cards6.get(i).getPyramidCardLocation(),new Point2D(0,0));
        }


        // Test the two pyramids
        PyramidType pyramidType7 = PyramidType.TWO_PYRAMIDS;
        ArrayList<PyramidCard> cards7 = new ArrayList<>();
        // Generate 6 pyramid cards
        for (int i = 0; i < 6; i++) {
            cards7.add(new PyramidCard(new MatchCard(new BasicMinion(),0,0)));
        }
        pyramidType7.arrange(new Point2D(1,1), cards2);

        // Check that there are no more cards at 0,0
        for (int i = 0; i < 6; i++){
            Assert.assertNotEquals(cards7.get(i).getPyramidCardLocation(),new Point2D(0,0));
        }


        // Test the two inverse
        PyramidType pyramidType8 = PyramidType.TWO_INVERSE;
        ArrayList<PyramidCard> cards8 = new ArrayList<>();
        // Generate 6 pyramid cards
        for (int i = 0; i < 6; i++) {
            cards8.add(new PyramidCard(new MatchCard(new BasicMinion(),0,0)));
        }
        pyramidType2.arrange(new Point2D(1,1), cards2);

        // Check that there are no more cards at 0,0
        for (int i = 0; i < 6; i++){
            Assert.assertNotEquals(cards8.get(i).getPyramidCardLocation(),new Point2D(0,0));
        }


    }

}
