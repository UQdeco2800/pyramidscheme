package uq.deco2800.pyramidscheme.pyramid;

import javafx.geometry.Point2D;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * @author QuackPack
 *         <p>
 *         PyramidType defines different pyramid shapes with their respective sizes.
 *         <p>
 *         The Class is set using the following variables;
 *         - CARD_HEIGHT: 	      private final int describing the height value of the cards.
 *         - CARD_WIDTH:	      private final int describing the width value of the cards.
 *         - ROW_SPACING: 		  private final int describing the spacing for rows.
 *         - CARD_SPACING:       private final int describing the card spacing between cards.
 *         <p>
 *         Currently Defined Pyramids and sizes;
 *         - DIAMOND           size: 6
 *         - INVERTED          size: 6
 *         - SQUARES           size: 6
 *         - TRIANGLE          size: 6
 *         - RECTANGLE         size: 6
 *         - TRIANGLE_CORNER   size: 6
 *         - TWO_PYRAMIDS      size: 6
 *         - TWO_INVERSE       size: 6
 *         - EIGHT_CARD        size: 8
 */
public enum PyramidType {
    DIAMOND(buildDiamond()),
    INVERTED(buildInverted()),
    SQUARE(buildSquare()),
    TRIANGLE(buildTriangle()),
    RECTANGLE(buildRectangle()),
    TRIANGLE_CORNER(buildTriangleCorner()),
    TWO_PYRAMIDS(buildTwoPyramids()),
    TWO_INVERSE(buildTwoInverse()),
    PYRAMID_TOP(buildPyramidTop()),
    EIGHT_CARD(buildEightCardPyramid());


    private static final int CARD_HEIGHT = 90;
    private static final int CARD_WIDTH = 65;
    private static final int ROW_SPACING = CARD_HEIGHT - 60;
    private static final int CARD_SPACING = CARD_WIDTH + 5;
    private ArrayList<PyramidCardLocation> locations;
    org.slf4j.Logger logger = LoggerFactory.getLogger(PyramidType.class);

    /**
     * Class constructor specifying capacity of PyramidCards.
     *
     * @param locations the ArrayList of PyramidCardLocations that define the shape of this PyramidType.
     */
    PyramidType(ArrayList<PyramidCardLocation> locations) {
        this.locations = locations;
    }

    /**
     * Returns the capacity of this PyramidType.
     *
     * @return returns the capacity of this PyramidType.
     */
    public int getSize() {
        return locations.size();
    }

    /**
     * Sets the coordinates of the PyramidCards in the ArrayList 'cards' to be
     * such that they are arranged into this PyramidType's pyramid shape.
     *
     * @param origin a Point2D that defines the origin point that this pyramid
     *               should be drawn from.
     * @param cards  an ArrayList of Pyramid Cards to be drawn on screen in a
     *               new pyramid.
     */
    void arrange(Point2D origin, ArrayList<PyramidCard> cards) {
        // Blank array to return
        ArrayList<PyramidCardLocation> cardLocations = generateShape();
        //Account arranging pyramids that have had cards played already
        int arrayDiff = cardLocations.size() - cards.size();
        // Set the new coordinates
        for (int i = arrayDiff; i < cardLocations.size(); i++) {
            //Set X
            cards.get(i - arrayDiff).setX(cardLocations.get(i).getX() + (int) origin.getX());
            //Set Y
            cards.get(i - arrayDiff).setY(cardLocations.get(i).getY() + (int) origin.getY());
            //Set Z index
            cards.get(i - arrayDiff).setZ(cardLocations.get(i).getZ());
        }
    }


    /**
     * Returns an ArrayList of PyramidCardLocations corresponding with this
     * PyramidType's shape.
     *
     * @return returns an ArrayList of PyramidCardLocations corresponding with
     * this PyramidType's shape.
     */
    ArrayList<PyramidCardLocation> generateShape() {
        return setOriginPoint(locations);
    }

    /**
     * Returns a list of PyramidCardLocations that is laid out the same as
     * cardLocations but all of the PyramidCardLocations are moved such that
     * the top-leftmost location has x and y values of 0.
     *
     * @param cardLocations a list of PyramidCardLocations
     * @return returns a list of PyramidCardLocations that is layed out the
     * same as cardLocations but all of the PyramidCardLocations are
     * moved such that the leftmost location has an x value of 0 and
     * the rightmost location has a y value of 0.
     */
    private ArrayList<PyramidCardLocation> setOriginPoint(ArrayList<PyramidCardLocation> cardLocations) {
        // Set unrealistic minX and minY variables
        int minX = 10000;
        int minY = 10000;
        for (PyramidCardLocation cardLocation : cardLocations) {
            if (cardLocation.getX() < minX) {
                minX = cardLocation.getX();
            }
            if (cardLocation.getY() < minY) {
                minY = cardLocation.getY();
            }
        }
        // Make final for lambda function
        final int finalMinX = minX;
        final int finalMinY = minY;
        // Move the cards relative to the origin point
        cardLocations.forEach(coord -> {
            coord.setX(coord.getX() - finalMinX);
            coord.setY(coord.getY() - finalMinY);
        });
        return cardLocations;
    }

    /**
     * Returns the coordinates that can be used to display a diamond layout for the
     * player/ai.
     *
     * @return a list of PyramidCardLocations that contains the appropriate coordinates
     * for a diamond layout.
     */
    private static ArrayList<PyramidCardLocation> buildDiamond() {
        ArrayList<PyramidCardLocation> cardLocations = new ArrayList<>(6);
        //Row 1
        cardLocations.add(new PyramidCardLocation(0, 0, 0));
        //Row 2
        cardLocations.add(new PyramidCardLocation(0 - CARD_SPACING / 2, ROW_SPACING, 1));
        cardLocations.add(new PyramidCardLocation(CARD_SPACING / 2, ROW_SPACING, 1));
        //Row 3
        cardLocations.add(new PyramidCardLocation(0 - CARD_SPACING / 2, ROW_SPACING * 2, 2));
        cardLocations.add(new PyramidCardLocation(CARD_SPACING / 2, ROW_SPACING * 2, 2));
        //Row 4
        cardLocations.add(new PyramidCardLocation(0, ROW_SPACING * 3, 3));
        return cardLocations;
    }

    /**
     * Returns the coordinates that can be used to display an inverted pyramid for the
     * player/ai.
     *
     * @return a list of PyramidCardLocations that contains the appropriate coordinates
     * for an inverted pyramid.
     */
    private static ArrayList<PyramidCardLocation> buildInverted() {
        ArrayList<PyramidCardLocation> cardLocations = new ArrayList<>(6);
        //Row 1
        cardLocations.add(new PyramidCardLocation(0, 0, 0));
        cardLocations.add(new PyramidCardLocation(CARD_SPACING, 0, 0));
        cardLocations.add(new PyramidCardLocation(CARD_SPACING * 2, 0, 0));
        //Row 2
        cardLocations.add(new PyramidCardLocation(CARD_SPACING / 2, ROW_SPACING, 1));
        cardLocations.add(new PyramidCardLocation(3 * CARD_SPACING / 2, ROW_SPACING, 1));
        //Row 3
        cardLocations.add(new PyramidCardLocation(CARD_SPACING, ROW_SPACING * 2, 2));
        return cardLocations;
    }


    /**
     * Returns the coordinates that can be used to display a 2 x 3 rectangle for the
     * player/ai.
     *
     * @return a list of PyramidCardLocations that contains the appropriate coordinates
     * for a 2 x 3 card layout.
     */
    private static ArrayList<PyramidCardLocation> buildSquare() {
        ArrayList<PyramidCardLocation> cardLocations = new ArrayList<>(6);
        //Row 1
        cardLocations.add(new PyramidCardLocation(0, 0, 0));
        cardLocations.add(new PyramidCardLocation(CARD_SPACING, 0, 0));
        cardLocations.add(new PyramidCardLocation(CARD_SPACING * 2, 0, 0));
        //Row 2
        cardLocations.add(new PyramidCardLocation(0, ROW_SPACING, 1));
        cardLocations.add(new PyramidCardLocation(CARD_SPACING, ROW_SPACING, 1));
        cardLocations.add(new PyramidCardLocation(CARD_SPACING * 2, ROW_SPACING, 1));
        return cardLocations;
    }

    /**
     * Returns the coordinates that can be used to display a triangle layout for the
     * player/ai.
     *
     * @return a list of PyramidCardLocations that contains the appropriate coordinates
     * for a normal pyramid.
     */
    private static ArrayList<PyramidCardLocation> buildTriangle() {
        ArrayList<PyramidCardLocation> cardLocations = new ArrayList<>(6);
        //Row 1
        cardLocations.add(new PyramidCardLocation(0, 0, 0));
        //Row 2
        cardLocations.add(new PyramidCardLocation(0 - CARD_SPACING / 2, ROW_SPACING, 1));
        cardLocations.add(new PyramidCardLocation(CARD_SPACING / 2, ROW_SPACING, 1));
        //Row 3
        cardLocations.add(new PyramidCardLocation(0 - CARD_SPACING, ROW_SPACING * 2, 2));
        cardLocations.add(new PyramidCardLocation(0, ROW_SPACING * 2, 2));
        cardLocations.add(new PyramidCardLocation(CARD_SPACING, ROW_SPACING * 2, 2));
        return cardLocations;
    }

    /**
     * Returns the coordinates that can be used to display a 3 x 2 rectangle for the
     * player/ai.
     *
     * @return a list of PyramidCardLocations that contains the appropriate coordinates
     * for a 2 x 3 card layout.
     */
    private static ArrayList<PyramidCardLocation> buildRectangle() {
        ArrayList<PyramidCardLocation> cardLocations = new ArrayList<>(6);
        //Row 1
        cardLocations.add(new PyramidCardLocation(0, 0, 0));
        cardLocations.add(new PyramidCardLocation(CARD_SPACING + 20, 0, 0));
        //Row 2
        cardLocations.add(new PyramidCardLocation(5, ROW_SPACING, 1));
        cardLocations.add(new PyramidCardLocation(CARD_SPACING + 15, ROW_SPACING, 1));
        //Row 3
        cardLocations.add(new PyramidCardLocation(10, ROW_SPACING * 2, 2));
        cardLocations.add(new PyramidCardLocation(CARD_SPACING + 10, ROW_SPACING * 2, 2));
        return cardLocations;
    }

    /**
     * Returns the coordinates that can be used to display a triangle layout for the
     * player/ai.  This pyramid plays cards from the corners to the bottom center card
     * then to cards in row 2.
     *
     * @return a list of PyramidCardLocations that contains the appropriate coordinates
     * for a normal triangle. This triangle layout allows for all corners to be
     * played first then the bottom center card and lastly the cards in row 2.
     */
    private static ArrayList<PyramidCardLocation> buildTriangleCorner() {
        ArrayList<PyramidCardLocation> cardLocations = new ArrayList<>(6);
        //Row 1
        cardLocations.add(new PyramidCardLocation(0, 0, 0));
        //Row 2
        cardLocations.add(new PyramidCardLocation(0 - CARD_SPACING / 2, ROW_SPACING, 2));
        cardLocations.add(new PyramidCardLocation(CARD_SPACING / 2, ROW_SPACING, 2));
        //Row 3
        cardLocations.add(new PyramidCardLocation(0 - CARD_SPACING, ROW_SPACING * 2, 0));
        cardLocations.add(new PyramidCardLocation(0, ROW_SPACING * 2, 1));
        cardLocations.add(new PyramidCardLocation(CARD_SPACING, ROW_SPACING * 2, 0));
        return cardLocations;
    }

    /**
     * Returns the coordinates that can be used to display an 8 card layout. Layout is
     * something like this:
     * x
     * x x
     * x x x
     * x x
     * Since this card layout uses eight cards, it will take 3 draws of the deck, not 3
     * when playing a game.
     *
     * @return a list of PyramidCardLocations that contains the appropriate coordinates
     * for a spearheaded 8 card pyramid.
     */
    private static ArrayList<PyramidCardLocation> buildEightCardPyramid() {
        ArrayList<PyramidCardLocation> cardLocations = new ArrayList<>(8);
        //Row 1
        cardLocations.add(new PyramidCardLocation(0, 0, 0));
        //Row 2
        cardLocations.add(new PyramidCardLocation(0 - CARD_SPACING / 2, ROW_SPACING, 1));
        cardLocations.add(new PyramidCardLocation(CARD_SPACING / 2, ROW_SPACING, 1));
        //Row 3
        cardLocations.add(new PyramidCardLocation(0 - CARD_SPACING, ROW_SPACING * 2, 2));
        cardLocations.add(new PyramidCardLocation(0, ROW_SPACING * 2, 2));
        cardLocations.add(new PyramidCardLocation(CARD_SPACING, ROW_SPACING * 2, 2));
        //Row 4
        cardLocations.add(new PyramidCardLocation(0 - CARD_SPACING / 2, ROW_SPACING * 3, 4));
        cardLocations.add(new PyramidCardLocation(CARD_SPACING / 2, ROW_SPACING * 3, 4));
        return cardLocations;
    }

    /**
     * Returns the coordinates that can be used to display two smaller pyramids for the
     * player/ai.
     *
     * @return a list of PyramidCardLocations that contains the appropriate coordinates
     * for two small pyramids.
     */
    private static ArrayList<PyramidCardLocation> buildTwoPyramids() {
        ArrayList<PyramidCardLocation> cardLocations = new ArrayList<>(6);
        //Row 1
        cardLocations.add(new PyramidCardLocation(0, 0, 0));
        cardLocations.add(new PyramidCardLocation(150, 0, 0));
        //Row 2
        cardLocations.add(new PyramidCardLocation(0 - CARD_SPACING / 2, ROW_SPACING, 1));
        cardLocations.add(new PyramidCardLocation(CARD_SPACING / 2, ROW_SPACING, 1));
        cardLocations.add(new PyramidCardLocation(150 - CARD_SPACING / 2, ROW_SPACING, 1));
        cardLocations.add(new PyramidCardLocation(150 + CARD_SPACING / 2, ROW_SPACING, 1));
        return cardLocations;
    }

    /**
     * Returns the coordinates that can be used to display two inverted pyramids for the
     * player/ai.
     *
     * @return a list of PyramidCardLocations that contains the appropriate coordinates
     * for two small inverted pyramids.
     */
    private static ArrayList<PyramidCardLocation> buildTwoInverse() {
        ArrayList<PyramidCardLocation> cardLocations = new ArrayList<>(6);
        //Row 1
        cardLocations.add(new PyramidCardLocation(0 - CARD_SPACING / 2, 0, 0));
        cardLocations.add(new PyramidCardLocation(CARD_SPACING / 2, 0, 0));
        cardLocations.add(new PyramidCardLocation(150 - CARD_SPACING / 2, 0, 0));
        cardLocations.add(new PyramidCardLocation(150 + CARD_SPACING / 2, 0, 0));
        //Row 2
        cardLocations.add(new PyramidCardLocation(0, ROW_SPACING, 1));
        cardLocations.add(new PyramidCardLocation(150, ROW_SPACING, 1));
        return cardLocations;
    }

    /**
     * Returns the coordinates that can be used to display a top down pyramid containing 6
     * cards each for the player/ai.
     *
     * @return a list of PyramidCardLocations that contains the appropriate coordinates
     * for two small inverted pyramids.
     */
    private static ArrayList<PyramidCardLocation> buildPyramidTop() {
        ArrayList<PyramidCardLocation> cardLocations = new ArrayList<>(6);
        //Row 1
        cardLocations.add(new PyramidCardLocation(0, 0, 1));
        cardLocations.add(new PyramidCardLocation(CARD_SPACING, 0, 1));
        //Row 2
        cardLocations.add(new PyramidCardLocation(CARD_SPACING / 2, ROW_SPACING, 0));
        //Row 3
        cardLocations.add(new PyramidCardLocation(0, ROW_SPACING * 3, 1));
        cardLocations.add(new PyramidCardLocation(CARD_SPACING, ROW_SPACING * 3, 1));
        //Row 4
        cardLocations.add(new PyramidCardLocation(CARD_SPACING / 2, ROW_SPACING, 2));
        return cardLocations;
    }
}