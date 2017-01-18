package uq.deco2800.pyramidscheme.settings;

/**
 * A resolution (height, width)
 * NOTE: left in for when further development implements variable screen resolution
 * <p>
 * Created by billy on 6/10/16.
 */
public class Resolution {

    private double width;
    private double height;
    private String ratio;

    public Resolution(double width, double height, String ratio) {
        this.width = width;
        this.height = height;
        this.ratio = ratio;
    }

    /**
     * Get the resolution width
     *
     * @return double width
     */
    public double getWidth() {
        return width;
    }

    /**
     * Get the resolution height
     *
     * @return double height
     */
    public double getHeight() {
        return height;
    }

    /**
     * Get a string representation of this resolution
     *
     * @return String res repr
     */
    @Override
    public String toString() {
        return (int) width + " x " + (int) height + "  " + ratio;
    }

}
