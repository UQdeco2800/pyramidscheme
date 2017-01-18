package uq.deco2800.pyramidscheme.pyramid;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import uq.deco2800.pyramidscheme.cards.supercards.CardHiddenState;
import uq.deco2800.pyramidscheme.match.MatchCard;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author QuackPack
 *         <p>
 *         The following class defines a Pyramid and implements Iterable<MatchCard>.
 *         <p>
 *         The Class is set using the following variables;
 *         - CARD_HEIGHT: 	      final int describing the height value of the cards.
 *         - CARD_WIDTH:	      final int describing the width value of the cards.
 *         - origin: 		      private Point2D which describes the position of the top left corner of the Pyramid.
 *         - pyramidType: 	      private enumerator of PyramidType which contains the data of various Pyramid Shapes.
 *         - isUserPyramid:      Boolean that determines whether or not the Pyramid is a user's or AI's.
 *         - animateingCardLoad: Boolean that determines the animation of a cardload.
 */
public class Pyramid implements Iterable<MatchCard> {

    // Variables for how wide the cards are
    private static final int CARD_HEIGHT = 90;
    private static final int CARD_WIDTH = 65;

    // The position of the top left corner of the pyramid
    private Point2D origin;

    // A list of cards in the pyramid
    private ArrayList<PyramidCard> cards;

    // The enum that contains data about the pyramid shape
    private PyramidType pyramidType;

    // Allows the pyramid to know whether it is an AI pyramid or User pyramid so we know whether to draw hidden or not
    // i.e. .draw(gc, isUserPyramid)
    private Boolean isUserPyramid;

    private Boolean animatingCardLoad;

    /**
     * Pyramid is a method which takes 4 parameters of originX, Y the shape and
     * the type of Pyramid (ie. User or AI). It overall creates the pyramid that's going
     * to be used within the class and sets the origin of the new Pyramid, as well
     * as the shape that's going to be used the type and if the animatingCardLoad is true.
     *
     * @param originX
     * @param originY
     * @param shape
     * @param isUserPyramid
     */
    public Pyramid(int originX, int originY, PyramidType shape, boolean isUserPyramid) {
        origin = new Point2D(originX, originY);
        this.pyramidType = shape;
        cards = new ArrayList<>();
        this.isUserPyramid = isUserPyramid;
        this.animatingCardLoad = true;
    }

    /**
     * Loads cards into the pyramid, causing them to be rendered later with a
     * this.draw() call.
     *
     * @param cards The cards to be added to the pyramid
     */
    public void loadCards(List<MatchCard> cards) {
        for (MatchCard card : cards) {
            this.cards.add(new PyramidCard(card));
        }
    }

    /**
     * Sets the animatingCardLoad to false or true depending on whether
     * or not the cards have been loaded or not.
     *
     * @param loadingCards
     */
    public void setLoadingCards(Boolean loadingCards) {
        animatingCardLoad = loadingCards;
    }

    /**
     * Returns the origin of the pyramid in a Point2D format. The Origin of
     * the pyramid is taken from the top most card and the left most card such that the top card has a
     * y value of 0 and the left card has an x value of 0. There may not be a
     *
     * @return the pyramid origin
     */
    public Point2D getOrigin() {
        return origin;
    }

    /**
     * Arranges the Pyramid and the cards within the pyramid as well
     * as which cards are playable
     */
    public void arrangePyramid() {
        if (this.size() <= pyramidType.getSize()) {
            pyramidType.arrange(origin, cards);
        } else {
            throw new IndexOutOfBoundsException("Too many cards in the pyramid");
        }
        //Figure out what cards should be hidden.
        setPlayableCards();
    }

    /**
     * Shuffles the pyramid by collecting the respective X and Y coordinates as well as
     * the Z index of each card. This is then used in conjunction with a seed to shuffle
     * the respective orders of the X, Y and Z values. Then the pyramid cards are re-added
     * (setY/X/Z) and getPlayableCards() are called to determine which cards are meant to
     * be hidden with the new shuffled pyramid.
     * <p>
     * Note: Draw must be called after in order for this to update the scene.
     */
    void shufflePyramid() {
        //load locations
        ArrayList<PyramidCardLocation> oldLocations = getCardLocations();
        ArrayList<PyramidCardLocation> locations = getCardLocations();
        //Loop to make sure a change has been made to the locations
        do {
            //Create a seed for the shuffle function
            long seed = System.nanoTime();
            //Use the same seed so both collections are shuffled the same
            Collections.shuffle(locations, new Random(seed));
        } while (!oldLocations.equals(locations));
        // Set the new coordinates
        for (int i = 0; i < locations.size(); i++) {
            cards.get(i).setX(locations.get(i).getX());
            cards.get(i).setY(locations.get(i).getY());
            cards.get(i).setZ(locations.get(i).getZ());
        }

        //Figure out what cards are hidden now with the new configuration
        setPlayableCards();
    }

    /**
     * Returns an ArrayList of the PyramidCardLocations of the PyramidCards in cards.
     *
     * @return Returns an ArrayList of the PyramidCardLocations of the PyramidCards in cards.
     */
    private ArrayList<PyramidCardLocation> getCardLocations() {
        ArrayList<PyramidCardLocation> locations = new ArrayList<>(100);
        for (PyramidCard card : cards) {
            locations.add(card.getPyramidCardLocation());
        }
        return locations;
    }

    /**
     * Set the shape of the next arrangePyramid()
     *
     * @param shape the PyramidType of the pyramid
     */
    public void setShape(PyramidType shape) {
        this.pyramidType = shape;
    }

    /**
     * Determined the Min and Max Z values that will be used to draw the particular pyramid.
     * Once the Min and Max Z values are set, the "draw" method continues to draw the cards
     * with the Max Z values first (ie. Drawing the bottom cards of the pyramid first), and
     * continues to draw the pyramid from bottom to top whilst determining whether or not
     * the card being drawn is meant to be drawn face up or down based off the
     * CardHiddenState.
     *
     * @param gc The GraphicsContext to draw to
     */
    public void draw(GraphicsContext gc) {
        //Figure out what the bottom and top index that will be drawn.
        int maxZ = 0;
        int minZ = 1000;
        for (PyramidCard card : cards) {
            //Set bottom z index
            if (minZ > card.getPyramidCardLocation().getZ()) {
                minZ = card.getPyramidCardLocation().getZ();
            }
            //Set top index
            if (maxZ < card.getPyramidCardLocation().getZ()) {
                maxZ = card.getPyramidCardLocation().getZ();
            }
        }

        // Draw the bottom cards first
        for (int z = maxZ; z >= minZ; z--) {
            for (PyramidCard card : cards) {
                if (z == card.getPyramidCardLocation().getZ()) {
                    drawChecks(gc, card);
                }
            }
        }
    }

    /**
     * Helper method for the draw method. Draws card on screen in different
     * ways depending on its z index and CardHiddenState.
     *
     * @param gc   The Graphics context to draw to
     * @param card The card to draw on screen
     */
    private void drawChecks(GraphicsContext gc, PyramidCard card) {
        if (card.getClass().equals(Optional.class)) {
            return;
        }
        if (size() == getPyramidType().getSize() && isUserPyramid && animatingCardLoad && card.getFaceDown()) {
            return;
        }
        if (card.getHidden()) {
            return;
        }
        if (card.getFaceDown() && !animatingCardLoad && isUserPyramid) { // Draw face up
            card.draw(gc);
        } else if (isUserPyramid) { //Draw backside of user pyramid
            card.drawHidden(gc, CardHiddenState.USERHIDDEN);
        } else { //Draw backside of ai pyramid
            card.drawHidden(gc, CardHiddenState.AIHIDDEN);
        }
    }


    /**
     * This method calculates the PyramidCoordinates of the Pyramid this
     * method is being called on and depending on the PyramidShape being used.
     *
     * @return A list of PyramidCardLocation based off of the generatedShape()
     */
    public List<PyramidCardLocation> calculatePyramidCoords() {
        return pyramidType.generateShape();
    }

    /**
     * Updates the origin of the Pyramid and updates the location of the cards.
     *
     * @param pyramidX the new X origin
     * @param pyramidY the new Y origin
     */
    public void setOrigin(int pyramidX, int pyramidY) {
        origin = new Point2D(pyramidX, pyramidY);
        // Update all the cards to the new location
        arrangePyramid();
    }

    /**
     * An iterator of all playable MatchCards contained within the pyramid
     *
     * @return An iterator of the cards
     */
    @Override
    public Iterator<MatchCard> iterator() {
        ArrayList<MatchCard> playableCards = new ArrayList<>();
        for (PyramidCard c : cards) {
            if (c.getCard().isPresent() && isPlayable(c)) {
                playableCards.add(c.getCard().get());
            }
        }
        return playableCards.iterator();
    }

    /**
     * An iterator of all MatchCards contained within the pyramid
     *
     * @return An iterator of the cards
     */
    public Iterator<MatchCard> iteratorAllCards() {
        ArrayList<MatchCard> playableCards = new ArrayList<>();
        for (PyramidCard c : cards) {
            if (c.getCard().isPresent()) {
                playableCards.add(c.getCard().get());
            }
        }
        return playableCards.iterator();
    }

    /**
     * The method gets the MatchCards from the PyramidCards using
     * the getCard method as well as determining if the cards
     * are present and if they're present they're added to the list of
     * MatchCards being returned.
     *
     * @return An list of MatchCards contained within the pyramid
     */
    public List<MatchCard> getCards() {
        return cards
                .stream()
                .map(PyramidCard::getCard)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    /**
     * Gets an optional of a MatchCard, if it exists, at the specified
     * screenspace coordinates
     *
     * @param x is an x coordinate on screen
     * @param y is an y coordinate on screen
     * @return An optional of the card at the position (x,y)
     * on screen if that card is available.
     * If there is no active card at that coordinate, return an empty
     * optional.
     */
    public Optional<MatchCard> getMatchCardAt(int x, int y) {
        for (PyramidCard card : cards) {
            if (card.containsCoords(x, y) && isPlayable(card) && !card.getHidden()) {
                return card.getCard();
            }
        }
        return Optional.empty();
    }

    /**
     * Attempts to remove the MatchCard at the specified screenspace coordinates
     * from the pyramid, and removes an optional containing the MatchCard
     * if it was remove.
     *
     * @param x is an x coordinate on screen
     * @param y is an y coordinate on screen
     * @return An optional of the card at the position (x,y)
     * on screen if that card is available.
     * If there is no active card at that coordinate, return an empty
     * optional.
     */
    public Optional<MatchCard> yankMatchCardAt(int x, int y) {
        for (PyramidCard card : cards) {
            if (card.containsCoords(x, y) && isPlayable(card) && !card.getHidden()) {
                System.out.println("Yanking");
                return card.yankCard();
            }
        }
        return Optional.empty();
    }


    /**
     * If a card was yanked from the pyramid, but the user did something that
     * was not allowed, use this method to return the MatchCard to its proper
     * place in the pyramid.
     *
     * @param card The MatchCard to be returned.
     */
    public void returnYankedCard(MatchCard card) {
        for (PyramidCard c : cards) {
            if (!c.getCard().isPresent()) {
                c.setCard(card);
                return;
            }
        }
    }

    /**
     * This method determines whether or not the card being checked has any
     * overlaying cards on any of the current cards' 4 corners. (ie. is there a card
     * covering the current card being checked, thus making it un-playable by the
     * game rules). This is done by checking if the X and Y coordinates in the corners
     * are being used by another card.
     *
     * @param card being checked
     * @return True if the card is playable otherwise false
     */
    boolean isPlayable(PyramidCard card) {
        int failCount = 0;
        for (PyramidCard otherCard : cards) {
            //Check if it is under a card by index
            failCount += isOnTop(card, otherCard);
        }
        return failCount == 0;
    }

    /**
     * Returns true if card is not under otherCard
     *
     * @param card      card being checked
     * @param otherCard card being checked against card
     * @return true if card is not under otherCard
     */
    private Integer isOnTop(PyramidCard card, PyramidCard otherCard) {
        if (otherCard.getPyramidCardLocation().getZ() < card.getPyramidCardLocation().getZ()) {
            int x = card.getPyramidCardLocation().getX();
            int y = card.getPyramidCardLocation().getY();
            //Check top left corner
            if (otherCard.containsCoords(x + 1, y + 1)) {
                return 1;
            }
            //Check bottom left corner
            if (otherCard.containsCoords(x + 1, y + (CARD_HEIGHT - 1))) {
                return 1;
            }
            //Check top right corner
            if (otherCard.containsCoords(x + CARD_WIDTH - 1, y + 1)) {
                return 1;
            }
            //Check bottom right corner
            if (otherCard.containsCoords(x + (CARD_WIDTH - 1), y + (CARD_HEIGHT - 1))) {
                return 1;
            }
        }
        return 0;
    }


    /**
     * Evaluates the deck and determines if a card is hidden or not. Should be called anytime the deck is rearranged
     * or a card is removed.
     */
    public void setPlayableCards() {
        for (PyramidCard c : cards) {
            c.setFaceDown(this.isPlayable(c));
            c.setHidden(false, c.getCard().get());
        }
    }

    public ArrayList<PyramidCard> getFlipCards() {
        ArrayList<PyramidCard> returnCards = new ArrayList<>();
        for (PyramidCard c : cards) {
            if (!c.getFaceDown() && this.isPlayable(c)) {
                returnCards.add(c);
                c.setHidden(true, c.getCard().get());
            }
        }
        return returnCards;
    }

    /**
     * Informs the pyramid that the previously removed MatchCard won't be
     * returning
     */
    public void confirmRemoval() {
        cards.removeIf(c -> !c.getCard().isPresent());
    }

    /**
     * Informs the pyramid that the MatchCard inside this pyramid matching the
     * MatchCard parameter needs to be removed, and won't be returning.
     * <p>
     * Does nothing if the provided MatchCard isn't in this pyramid.
     * <p>
     * Will probably cause "undefined behaviour" if the same
     * (as in, reference equality) MatchCard is in the Pyramid multiple times.
     *
     * @param card A MatchCard to be removed from the Pyramid
     */
    public void confirmRemoval(MatchCard card) {
        cards.removeIf(c -> c.getCard().orElse(null).equals(card));
    }


    /**
     * Method iterates through the PyramidCards and checks whether or not a MatchCard is present
     * using the getCard() and isPresent() methods to achieve this.
     *
     * @return returns the number of cards contained within the pyramid.
     */
    public int size() {
        int count = 0;
        for (PyramidCard c : cards) {
            //Check for a MatchCard
            if (c.getCard().isPresent()) {
                count++;
            }
        }
        return count;
    }

    /**
     * Method checks the Pyramid deck of cards whether it's empty or not using isEmpty()
     *
     * @return true for empty set of cards in Pyramid/ false if cards (Pyramid) contains cards
     */
    public boolean isEmpty() {
        return cards.isEmpty();
    }

    /**
     * Method gets a list of the current PyramidCards contained within the pyramid
     *
     * @return A list of PyramidCards contained within the pyramid
     */
    List<PyramidCard> getPyramidCards() {
        return cards;
    }


    /**
     * Method which returns the type/shape of the pyramid being used
     *
     * @return pyramidType Type/Shape
     */
    public PyramidType getPyramidType() {
        return pyramidType;
    }

    /**
     * Returns a string of the form;
     * <p>
     * "You're currently using the Pyramid Shape: PYRAMIDTYYPE"
     * <p>
     * Where PYRAMIDTYPE is the shape/type of the pyramid being used.
     */
    @Override
    public String toString() {
        // Set up a StringBuilder
        StringBuilder sBuilder = new StringBuilder();

        // adds to the stringbuilder
        sBuilder.append("You're currently using the Pyramid Shape: " + pyramidType.toString());
        return sBuilder.toString(); // returns the string
    }

    /**
     * <p>
     * Returns true if and only if the given object is a Pyramid with the
     * same PyramidType, origin and cards.
     * </p>
     * <p>
     * (Any two Pyramids are considered to be the same when they are equal
     * according to their equals method.)
     * </p>
     */
    @Override
    public boolean equals(Object object) {

        //verify's if it's an instanceof Pyramid
        if (!(object instanceof Pyramid)) {
            return false;
        }

        //Cast down as it's passed "instanceof" test
        Pyramid p = (Pyramid) object;

        //return true if all are equal
        return pyramidType.equals(p.getPyramidType()) && cards.equals(p.cards) && origin.equals(p.getOrigin());
    }

    /**
     * Returns the hash code of this Pyramid;
     *
     * @return the hash code of this event
     */
    @Override
    public int hashCode() {
        // for combining hash codes
        final int prime = 27;

        // in a list which can be easily accessed
        int hashResult = 1;

        hashResult = prime * hashResult + pyramidType.hashCode();
        hashResult = prime * hashResult + cards.hashCode();
        hashResult = prime * hashResult + origin.hashCode();

        return hashResult;
    }
}




