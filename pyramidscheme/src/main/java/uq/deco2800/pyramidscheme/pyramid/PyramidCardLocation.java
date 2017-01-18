package uq.deco2800.pyramidscheme.pyramid;

/**
 * @author QuackPack
 *         <p>
 *         The following class defines a Pyramid and implements Iterable<MatchCard>.
 *         <p>
 *         The Class is set using the following variables;
 *         - x: 	      private int describing the x location of the PyramidCard.
 *         - y:	      private int describing the y location of the PyramidCard.
 *         - z: 		  private int describing the z location of the PyramidCard.
 */
public class PyramidCardLocation {
    private int x;
    private int y;
    private int z;

    /**
     * Class constructor specifying x, y and z values.
     *
     * @param x an int describing an x coordinate on the screen
     * @param y an int describing a y coordinate on the screen
     * @param z an int describing a z index
     */
    PyramidCardLocation(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Returns the x coordinate of this PyramidCardLocation.
     *
     * @return returns the x coordinate of this PyramidCardLocation
     */
    public int getX() {
        return x;
    }

    /**
     * Sets the new x coordinate of this PyramidCardLocation.
     *
     * @param x an int to be set as the new x coordinate of this
     *          PyramidCardLocation
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Returns the y coordinate of this PyramidCardLocation.
     *
     * @return returns the y coordinate of this PyramidCardLocation
     */
    public int getY() {
        return y;
    }

    /**
     * Sets the new y coordinate of this PyramidCardLocation.
     *
     * @param y an int to be set as the new x coordinate of this
     *          PyramidCardLocation
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Returns the z index of this PyramidCardLocation.
     *
     * @return returns the z index of this PyramidCardLocation
     */
    public int getZ() {
        return z;
    }

    /**
     * Sets the new z index of this PyramidCardLocation.
     *
     * @param z an int to be set as the new z index of this
     *          PyramidCardLocation
     */
    public void setZ(int z) {
        this.z = z;
    }

    /**
     * Returns a string representation of this PyramidCardLocation in the
     * form: "PyramidCardLocation: x=X, y=Y, z=Z"
     *
     * @return returns a string representation of this PyramidCardLocation.
     */
    @Override
    public String toString() {
        return "PyramidCardLocation: x=" + x + ", y=" + y + ", z=" + z;
    }


    /**
     * Returns true if the o is a PyramidCardLocation with the same x, y, and
     * z values as this PyramidCardLocation.
     *
     * @param o the object to be compared to this PyramidCardLocation
     * @return returns true if the o is a PyramidCardLocation with the same
     * x, y, and z values as this PyramidCardLocation.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PyramidCardLocation that = (PyramidCardLocation) o;

        if (x != that.x) {
            return false;
        }

        if (y != that.y) {
            return false;
        }

        return z == that.z;

    }

    /**
     * Returns a hash code value for the PyramidCardLocation.
     *
     * @return returns a hash code value for the PyramidCardLocation.
     */
    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        result = 31 * result + z;
        return result;
    }
}