package uq.deco2800.pyramidscheme.board;

import javafx.scene.canvas.GraphicsContext;

import java.util.Optional;

public abstract class Board<T extends Tile> implements Iterable<T> {

    /**
     * Draws the board and its children to the screen
     *
     * @param gc The GraphicsContext to draw to
     */
    public abstract void draw(GraphicsContext gc);

    /**
     * Gets an optional of a tile if it exists, at the specified screenspace
     * coordinates
     *
     * @param x The horizontal screenspace coordinate
     * @param y The vertial screenspace coordinate
     * @return An optional possibly containing the specified tile
     */
    public abstract Optional<T> getTileAt(int x, int y);


}
