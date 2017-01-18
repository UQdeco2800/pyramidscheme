package uq.deco2800.pyramidscheme.board;

import javafx.scene.canvas.GraphicsContext;

public abstract class TileOccupant {

    /**
     * A flag that informs wrapping classes (like Boards), whether they should
     * be removed from their datastructures and promptly garbage collected.
     */
    private boolean isToDie = false;

    /**
     * Draws the TileOccupant to the screen
     *
     * @param gc The GraphicsContext to draw to
     */
    public abstract void draw(GraphicsContext gc);

    /**
     * Returns the isToDie status
     *
     * @return true if the TileOccupant should be removed from a datastructure
     */
    public boolean isToDie() {
        return isToDie;
    }

    /**
     * Used to set the isToDie status
     *
     * @param status The new isToDie status
     */
    public void setIsToDie(boolean status) {
        this.isToDie = status;
    }
}
