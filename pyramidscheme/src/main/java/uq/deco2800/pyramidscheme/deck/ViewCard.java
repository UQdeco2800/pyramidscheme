package uq.deco2800.pyramidscheme.deck;

import javafx.scene.canvas.GraphicsContext;
import uq.deco2800.pyramidscheme.cards.supercards.Card;

/**
 * A card placed on a canvas with an x and y coordinate
 *
 * @author Billy-7
 */
public class ViewCard {

    private Card card;
    private int x;
    private int y;
    private double scale;

    /**
     * Create a ViewCard at coords (`x`, `y`)
     *
     * @param card Card base card
     * @param x    int x coord
     * @param y    int yu coord
     */
    public ViewCard(Card card, int x, int y) {
        this(card, x, y, 1);
    }

    /**
     * Create a ViewCard at coords (`x`, `y`), with `scale`
     *
     * @param card  Card base card
     * @param x     int x coord
     * @param y     int y coord
     * @param scale double scale of card image
     */
    public ViewCard(Card card, int x, int y, double scale) {
        this.card = card;
        this.x = x;
        this.y = y;
        this.scale = scale;
    }

    /**
     * Draw card on `gc`, at this card coords
     *
     * @param gc GraphicsContext to draw onto
     */
    public void drawCard(GraphicsContext gc) {
        gc.drawImage(card.getImage(), (double) x, (double) y,
                (double) Card.getCardWidth() * scale, (double) Card.getCardHeight() * scale);
    }

    /**
     * Check if click at (`x`, `y`) was on this card
     *
     * @param x int x coord of click
     * @param y int y coord of click
     * @return true if click was on this card, false otherwise
     */
    public boolean containsClick(int x, int y) {
        return !(x < this.x || x > (this.x + getWidth()) ||
                y < this.y || y > (this.y + getHeight()));
    }

    /**
     * Get x coord
     *
     * @return int x coord
     */
    public int getX() {
        return x;
    }

    /**
     * Get y coord
     *
     * @return int y coord
     */
    public int getY() {
        return y;
    }

    /**
     * Get card height
     *
     * @return double card height
     */
    public double getWidth() {
        return Card.getCardWidth() * scale;
    }

    /**
     * Get card width
     *
     * @return double card width
     */
    public double getHeight() {
        return Card.getCardHeight() * scale;
    }

    /**
     * Get the base card
     *
     * @return Card base card
     */
    public Card getCard() {
        return card;
    }

    /**
     * Get the scale of the drawn card
     *
     * @return double of card scale
     */
    public double getScale() {
        return scale;
    }

    /**
     * Set the x position of the drawn card.
     *
     * @param x the new x coordinate.
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Set the y position of the drawn card.
     *
     * @param y the new y coordinate.
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Set the scale of the drawn card.
     *
     * @param scale the new scale.
     */
    public void setScale(double scale) {
        this.scale = scale;
    }
}
