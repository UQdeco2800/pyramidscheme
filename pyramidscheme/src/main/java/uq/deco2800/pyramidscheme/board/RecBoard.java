package uq.deco2800.pyramidscheme.board;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import uq.deco2800.pyramidscheme.match.MatchPlayer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class RecBoard extends Board<RecTile> implements Iterable<RecTile> {

    /**
     * The main datastructure that stores all the tiles in the board.
     * Should not be sorted at any time.
     */
    private final ArrayList<RecTile> tiles;

    /**
     * The number of height and width of the board, tiles wise.
     */
    public static final int WIDTH = 6;
    public static final int HEIGHT = 2;

    /**
     * The position of the top left hand corner of the board
     */
    private Point2D origin;

    /**
     * Creates a rectangular board with rectangular tiles. The board creates and
     * assigns tiles to the player and the AI.
     * <p>
     * This class should only be used as an initializer for RecTiles, and should
     * not be used to manipulate and reason with the gameboard. RecTiles should
     * provide a singular interface to access and reason with the board, and
     * their neighbours
     *
     * @param xCoord The x screenspace coordinate that the top left hand corner will
     *               be rendered at
     * @param yCoord The y screenspace coordinate that the top left hand corner will
     *               be rendered at
     * @param user   The user who will own some of the tiles
     * @param ai     The AI that will own some of the tiles
     */
    public RecBoard(int xCoord, int yCoord, MatchPlayer user, MatchPlayer ai) {
        tiles = new ArrayList<>();
        origin = new Point2D(xCoord, yCoord);

        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {

                int newX = x * RecTile.TILE_WIDTH + (int) origin.getX();
                int newY = y * RecTile.TILE_HEIGHT + (int) origin.getY();

                // Hand it a copy of the board so that it can reason about
                // it's position
                tiles.add(new RecTile(this, newX, newY, x + (y * WIDTH),
                        y == 0 ? ai : user));
            }
        }

    }

    /**
     * An iterator of all the RecTiles contained within the board
     *
     * @return An iterator of RecTiles contained within the board
     */
    @Override
    public Iterator<RecTile> iterator() {
        return new ArrayList<>(tiles).iterator();
    }

    /**
     * An list of all the RecTiles contained within the board
     *
     * @return An list of RecTiles contained within the board
     */
    public List<RecTile> getTiles() {
        return new ArrayList<>(tiles);
    }

    /**
     * Draws the board and its RecTiles to the screen
     *
     * @param gc The GraphicsContext to draw to
     */
    @Override
    public void draw(GraphicsContext gc) {
        for (RecTile tile : this) {
            tile.draw(gc);
        }
    }

    /**
     * Gets the tile opposite the tile parameter
     *
     * @param tile The tile opposite the returned tile
     */
    RecTile getOppositeTile(RecTile tile) {
        if (tile.getBoardIndex() < WIDTH) {
            return tiles.get(tile.getBoardIndex() + WIDTH);
            // If the given tile is on the lower half of the board
        } else {
            return tiles.get(tile.getBoardIndex() - WIDTH);
        }
    }

    /**
     * Gets an Optional of the tile on the left of the tile parameter
     * <p>
     * Returns an empty Optional if there is no tile on the left
     *
     * @param tile The tile on the right of the returned tile
     * @return An Optional of the tile on the left of the tile parameter
     */
    Optional<RecTile> getLeftNeighbor(RecTile tile) {
        int index = tile.getBoardIndex();

        if (index == 0 || index == WIDTH) {
            // If in a "left-most" position on a row
            return Optional.empty();
        } else {
            // Get the card on the left
            return Optional.of(tiles.get(tile.getBoardIndex() - 1));
        }
    }

    /**
     * Gets an Optional of the tile on the right of the tile parameter.
     * <p>
     * Returns an empty Optional if there is no tile on the right.
     *
     * @param tile The tile on the left of the returned tile
     * @return An Optional of the tile on the right of the tile parameter
     */
    Optional<RecTile> getRightNeighbor(RecTile tile) {
        int index = tile.getBoardIndex();

        if (index == WIDTH - 1 || index == (WIDTH * HEIGHT) - 1) {
            // If in a "right-most" position on a row
            return Optional.empty();
        } else {
            // Get the card on the right
            return Optional.of(tiles.get(tile.getBoardIndex() + 1));
        }
    }


    /**
     * Gets an optional of a RecTile if it exists, at the specified screenspace
     * coordinates
     *
     * @param x The horizontal screenspace coordinate
     * @param y The vertial screenspace coordinate
     * @return An optional possibly containing the specified RecTile
     */
    @Override
    public Optional<RecTile> getTileAt(int x, int y) {
        for (RecTile tile : this) {
            if (tile.containsCoords(x, y)) {
                return Optional.of(tile);
            }
        }
        return Optional.empty();
    }

    /**
     * Returns a list of tiles with MatchCards that are marked for death,
     * for the BasicMatchController to remove and transfer to the graveyard
     */
    public List<RecTile> getDeadTiles() {
        ArrayList<RecTile> collector = new ArrayList<>();
        for (RecTile tile : this) {
            if (tile.getContents().isPresent()
                    && tile.getContents().get().isToDie()) {
                collector.add(tile);
            }
        }
        return collector;
    }

    /**
     * Updates the origin of the RecBoard
     *
     * @param boardX the new X origin
     * @param boardY the new Y origin
     */
    public void setOrigin(int boardX, int boardY) {
        origin = new Point2D(boardX, boardY);
    }

    /**
     * Gets a list of tiles owned by a specific player
     *
     * @param player The player that owns the tiles
     * @return A list of those RecTiles
     */
    public List<RecTile> getTilesOf(MatchPlayer player) {
        return tiles
                .stream()
                .filter(x -> x.getOwner().equals(player))
                .filter(x -> !x.getContents().isPresent())
                .collect(Collectors.toList());
    }
}
