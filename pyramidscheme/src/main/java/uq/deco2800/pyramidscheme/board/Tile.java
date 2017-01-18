package uq.deco2800.pyramidscheme.board;

import javafx.scene.canvas.GraphicsContext;
import uq.deco2800.pyramidscheme.match.MatchPlayer;

import java.util.Optional;

public abstract class Tile<T extends TileOccupant> {

    /**
     * The contents of the tile
     */
    protected T contents = null;

    /**
     * The owner of the tile.
     * <p>
     * Should never be null.
     */
    protected MatchPlayer owner;

    /**
     * Draws the tile to the screen
     *
     * @param gc The GraphicsContext to draw to
     */
    public abstract void draw(GraphicsContext gc);

    /**
     * Returns an optional of the contents of the tile
     *
     * @return Returns an empty optional if the contents is null, otherwise
     * an optional containing whatever is inside the tile. The contents must
     * extend the TileOccupant class
     */
    public Optional<T> getContents() {
        return Optional.ofNullable(contents);
    }

    /**
     * Sets the contents of the tile
     *
     * @param contents An object that inherits from TileOccupant
     */
    public abstract void setContents(T contents);

    /**
     * Returns a reference to the owner of the tile. This can used to determine
     * whether certain TileOccupants are allowed to be set into the tile.
     *
     * @return A MatchPlayer that owns this tile
     */
    public MatchPlayer getOwner() {
        return owner;
    }

    /**
     * Removes the contents of the tile.
     *
     * @return Returns an empty optional if the contents is null, otherwise
     * an optional containing whatever is inside the tile. The contents must
     * extend the TileOccupant class
     */
    public Optional<T> removeContents() {
        Optional<T> toReturn = getContents();
        this.contents = null;
        return toReturn;
    }
}

