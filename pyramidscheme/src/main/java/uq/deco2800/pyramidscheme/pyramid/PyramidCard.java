package uq.deco2800.pyramidscheme.pyramid;

import javafx.scene.canvas.GraphicsContext;
import uq.deco2800.pyramidscheme.cards.supercards.CardHiddenState;
import uq.deco2800.pyramidscheme.match.MatchCard;

import java.util.Optional;

/**
 * @author QuackPack
 *         <p>
 *         The following class defines a PyramidCard to be used within the Pyramid Class
 *         <p>
 *         The Class is set using the following variables;
 *         - card: 	           private MatchCard to be used to create a PyramidCard
 *         - pyramidCardLocation: private PyramidCardLocation is the location of a PyramidCard
 *         - faceDown:              private boolean which is orignally set as false (ie. it's initiated
 *         originally as not being faceDown)
 */
public class PyramidCard {

    private MatchCard card;
    private PyramidCardLocation pyramidCardLocation;
    private boolean faceDown;
    private boolean hidden;

    /**
     * Creates a new PyramidCard using the MatchCard being passed through and
     * sets the pyramidCardLocation for the MatchCard as well.
     *
     * @param card the MatchCard to be contained within this PyramidCard
     */
    PyramidCard(MatchCard card) {
        this.card = card;
        this.pyramidCardLocation = new PyramidCardLocation(card.getX(),
                card.getY(), 0);
        this.faceDown = true;
        this.hidden = false;
    }


    /**
     * Gets an optional of the contained MatchCard if it exists
     *
     * @return An optional possibly containing a MatchCard
     */
    public Optional<MatchCard> getCard() {
        return Optional.ofNullable(card);
    }

    /**
     * Puts a MatchCard into the PyramidCard
     *
     * @param card The MatchCard to be put into this PyramidCard
     */
    public void setCard(MatchCard card) {
        this.card = card;
    }

    /**
     * Removes the MatchCard from the PyramidCard if it exists, and returns
     * an optional of that removed MatchCard
     *
     * @return An optional possibly containing the specified MatchCard
     */
    public Optional<MatchCard> yankCard() {
        Optional<MatchCard> toReturn = getCard();
        card = null;
        return toReturn;
    }

    /**
     * Sets the visablity of the card in the pyramid.
     *
     * @param faceDown Pyramid card is faceDown if true
     */
    public void setFaceDown(boolean faceDown) {
        this.faceDown = faceDown;
    }

    /**
     * Returns true if the card should be faceDown
     */
    public boolean getFaceDown() {
        return faceDown;
    }


    /**
     * Returns the presence of the x and y coordinates within the bounding box
     * of this PyramidCard
     *
     * @param x is an x screenspace coordinate.
     * @param y is a y screenspace coordinate.
     * @return returns true if the provided x and y coordinates are contained
     * within this PyramidCard on screen.
     */
    boolean containsCoords(int x, int y) {
        if (getCard().isPresent()) {
            return getCard().get().containsCoords(x, y);
        }
        return false;
    }

    /**
     * Returns true if o is a PyramidCard that contains the same card, is
     * located at the same PyramidCardLocation and has the same CardHiddenState
     * as this PyramidCard.
     *
     * @param o An object to be compared to this PyramidCard
     * @return returns true if o is a PyramidCard that contains the same card,
     * is located at the same PyramidCardLocation and has the same
     * CardHiddenState as this PyramidCard.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PyramidCard that = (PyramidCard) o;

        if (card != null ? !card.equals(that.card) : that.card != null) {
            return false;
        }

        if (!this.pyramidCardLocation.equals(that.pyramidCardLocation)) {
            return false;
        }
        return this.faceDown == that.faceDown;
    }

    /**
     * Returns a hash code value for the PyramidCard.
     *
     * @return returns a hash code value for the PyramidCard.
     */
    @Override
    public int hashCode() {
        int result = card != null ? card.hashCode() : 0;
        result = 31 * result + (pyramidCardLocation !=
                null ? pyramidCardLocation.hashCode() : 0);
        result = 31 * result + (faceDown ? 1 : 0);
        return result;
    }


    /**
     * Sets the x coordinate of the PyramidCard, and also alters the child
     * MatchCard coordinates if it exists
     *
     * @param x The x screenspace coordinate
     */
    void setX(int x) {
        this.pyramidCardLocation.setX(x);
        getCard().ifPresent(o -> o.setX(this.pyramidCardLocation.getX()));
    }

    /**
     * Sets the y coordinate of the PyramidCard, and also alters the child
     * MatchCard coordinates if it exists
     *
     * @param y The y screenspace coordinate
     */
    void setY(int y) {
        this.pyramidCardLocation.setY(y);
        getCard().ifPresent(o -> o.setY(this.pyramidCardLocation.getY()));
    }

    /**
     * Sets the z index of the PyramidCard
     *
     * @param z The z index of the card
     */
    void setZ(int z) {
        this.pyramidCardLocation.setZ(z);
    }


    /**
     * Draws the child MatchCard (if it exists) at the coordinates of the
     * PyramidCard
     *
     * @param gc The GraphicsContext to draw to
     */
    public void draw(GraphicsContext gc) {
        // The pyramid can be rearranged when cards aren't in it
        setX(this.pyramidCardLocation.getX());
        setY(this.pyramidCardLocation.getY());
        getCard().ifPresent(o -> o.draw(gc));
    }

    /**
     * Draws the PyramidCard at the coordinates of the MatchCard as a faceDown
     * card specified in hiddenState
     *
     * @param gc          The GraphicsContext to draw to...
     * @param hiddenState The type of CardHiddenState to be drawn
     */
    public void drawHidden(GraphicsContext gc, CardHiddenState hiddenState) {
        // The pyramid can be rearranged when cards aren't in it
        setX(this.pyramidCardLocation.getX());
        setY(this.pyramidCardLocation.getY());
        getCard().ifPresent(o -> o.drawHidden(gc, hiddenState));
    }

    /**
     * Returns the PyramidCardLocation of the PyramidCard
     *
     * @return the PyramidCardLocation of the PyramidCard
     */
    public PyramidCardLocation getPyramidCardLocation() {
        return pyramidCardLocation;
    }

    public void setHidden(boolean hidden, MatchCard card) {
        if (card.equals(this.card)) {
            this.hidden = hidden;
        }
    }

    public boolean getHidden() {
        return hidden;
    }
}