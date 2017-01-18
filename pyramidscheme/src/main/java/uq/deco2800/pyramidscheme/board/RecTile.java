package uq.deco2800.pyramidscheme.board;

import javafx.scene.canvas.GraphicsContext;
import uq.deco2800.pyramidscheme.actions.Action;
import uq.deco2800.pyramidscheme.cards.supercards.CardHiddenState;
import uq.deco2800.pyramidscheme.match.MatchCard;
import uq.deco2800.pyramidscheme.match.MatchPlayer;

import java.util.Optional;


public class RecTile extends Tile<MatchCard> {

    static final int TILE_WIDTH = 70;
    static final int TILE_HEIGHT = 100;

    /**
     * A copy of the board so that this tile can reason about its position
     */
    private RecBoard board;

    /**
     * The x and y screenspace coordinates of this tile. The coordinates
     * represent the top left hand corner of the tile.
     */
    private int x;
    private int y;
    private int boardIndex;

    RecTile(RecBoard board, int x, int y, int boardIndex, MatchPlayer owner) {
        this.board = board;
        this.x = x;
        this.y = y;
        this.boardIndex = boardIndex;
        this.owner = owner;
    }

    /**
     * Draws a rectangle at this tiles x and y coordinates. If this tile has a
     * MatchCard inside of it, draws it too.
     *
     * @param gc The GraphicsContext to draw to
     */
    public void draw(GraphicsContext gc) {

        double[] xPoints = {x, x + TILE_WIDTH, x + TILE_WIDTH, x};
        double[] yPoints = {y + TILE_HEIGHT * 0.7, y + TILE_HEIGHT * 0.7,
                y + TILE_HEIGHT * 0.9, y + TILE_HEIGHT * 0.9};

        javafx.scene.paint.Color lighter = javafx.scene.paint.Color.rgb(255, 255, 255, 0.2);
        gc.setFill(lighter);
        gc.fillPolygon(xPoints, yPoints, 4);

        double[] xPoints2 = {x, x + TILE_WIDTH, x + TILE_WIDTH, x};
        double[] yPoints2 = {y + TILE_HEIGHT * 0.9, y + TILE_HEIGHT * 0.9,
                y + TILE_HEIGHT * 0.95, y + TILE_HEIGHT * 0.95};

        javafx.scene.paint.Color darker = javafx.scene.paint.Color.rgb(0, 0, 0, 0.2);
        gc.setFill(darker);
        gc.fillPolygon(xPoints2, yPoints2, 4);

        double[] xPoints3 = {x + TILE_WIDTH * 0.5, x + TILE_WIDTH * 0.5,
                x + TILE_WIDTH * 0.25, x + TILE_WIDTH * 0.75};
        double[] yPoints3 = {y + TILE_HEIGHT * 0.9, y + TILE_HEIGHT * 0.9,
                y + TILE_HEIGHT * 0.95, y + TILE_HEIGHT * 0.95};

        javafx.scene.paint.Color darkerAgain = javafx.scene.paint.Color.rgb(0, 0, 0, 0.3);
        gc.setFill(darkerAgain);
        gc.fillPolygon(xPoints3, yPoints3, 4);

        getContents().ifPresent(o -> o.drawHidden(gc, CardHiddenState.PLACED));
    }

    /**
     * Returns the x coordinate of the top left hand corner of this tile
     *
     * @return the x screenspace coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Returns the y coordinate of the top left hand corner of this tile
     *
     * @return the y screenspace coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * Returns the presence of the x and y coordinates within the bounding box
     * of this RecTile
     *
     * @param x is an x screenspace coordinate.
     * @param y is a y screenspace coordinate.
     * @return returns true if the provided x and y coordinates are contained
     * within this RecTile on screen.
     */
    boolean containsCoords(int x, int y) {
        boolean withinXBound = this.x <= x && x < this.x + (TILE_WIDTH);

        boolean withinYBound = this.y <= y && y <= this.y + (TILE_HEIGHT);

        return withinXBound && withinYBound;
    }

    @Override
    public void setContents(MatchCard card) {
        if (this.contents == null && card != null) {
            card.setX(x); // Latch the position of the card to the tile
            card.setY(y);

            this.contents = card;

            Action action = card.getCard().getAction();
            if (action != null) {
                action.summoned(this, board);
            }
        }
    }

    /**
     * Gets the tile opposite to this tile on the board
     *
     * @return The opposite tile
     */
    public RecTile getOppositeTile() {
        return board.getOppositeTile(this);
    }

    /**
     * Gets an Optional of the tile on the left of this tile on the board,
     * if it exists
     *
     * @return An Optional of the tile
     */
    Optional<RecTile> getLeftNeighbor() {
        return board.getLeftNeighbor(this);
    }

    /**
     * Gets an Optional of the tile on the right of this tile on the board,
     * if it exists
     *
     * @return An Optional of the tile
     */
    Optional<RecTile> getRightNeighbor() {
        return board.getRightNeighbor(this);
    }

    /**
     * Used to tell the board this tiles "board index"
     * <p>
     * Note: this is not the same as the index of the tile in the board
     * datastructure
     *
     * @return the board index
     */
    int getBoardIndex() {
        return boardIndex;
    }

    @Override
    public String toString() {
        return "RecTile{" +
                "x=" + x +
                ", y=" + y +
                ", contents=" + contents +
                '}';
    }
}
