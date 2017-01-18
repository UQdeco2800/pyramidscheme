package uq.deco2800.pyramidscheme.duckdust;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

/**
 * Created by nick on 14/10/16.
 */
public class Grinder {

    private static String imgString = "/gameImages/grinder_up.png";
    private static String downImgString = "/gameImages/grinder_down.png";
    private static Image upImg;
    private static Image downImg;

    // Default location at 150, 280
    private Point2D origin = new Point2D(150, 280);

    //Image dimensions
    double width = 150;
    double height = 150;

    public Grinder(int xCoord, int yCoord) {
        setOrigin(xCoord, yCoord);
    }

    private static Image getImage(DuckDustPool pool) {
        if (pool.getGround() && !(downImg instanceof Image)) {
            downImg = new Image(downImgString);
        } else if (pool.getGround()) {
            return downImg;
        } else if (!pool.getGround() && !(upImg instanceof Image)) {
            upImg = new Image(imgString);
        }
        return upImg;
    }

    /**
     * Returns the presence of the x and y coordinates within the bounding box
     * of this DuckDustPool
     *
     * @param x is an x screenspace coordinate.
     * @param y is a y screenspace coordinate.
     * @return returns true if the provided x and y coordinates are contained
     * within this DuckDustPool on screen.
     */
    public boolean containsCoords(int x, int y) {
        return !(x < origin.getX() || x > (origin.getX() + width) ||
                y < origin.getY() || y > (origin.getY() + height));
    }

    /**
     * Updates the origin of the Dust Pool
     *
     * @param poolX the new X origin
     * @param poolY the new Y origin
     */
    public void setOrigin(int poolX, int poolY) {
        origin = new Point2D(poolX, poolY);
    }

    public void draw(GraphicsContext gc, DuckDustPool pool) {
        gc.drawImage(getImage(pool), origin.getX(), origin.getY(), width, height);
        gc.setFill(Color.WHITE);
        gc.setStroke(Color.WHITE);
        gc.strokeText(pool.getCurrentDust() + "/" + pool.getMaxDust(),
                origin.getX() + width * 4 / 8, origin.getY() + height * 1 / 2, width / 2);
    }

    public Point2D getOrigin() {
        return origin;
    }
}
