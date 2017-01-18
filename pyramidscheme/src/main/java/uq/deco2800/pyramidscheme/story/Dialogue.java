package uq.deco2800.pyramidscheme.story;

/**
 * A class to story the dialogue information for story mode
 *
 * @author Bianca
 */
public class Dialogue {
    private String image;
    private String line;

    public Dialogue(String image, String line) {
        this.image = image;
        this.line = line;
    }

    @Override
    public String toString() {
        return this.image + " " + this.line;
    }

    /**
     * Returns the image as string
     *
     * @return this.image
     */
    public String getImage() {
        return this.image;
    }

    /**
     * Returns the line of text
     *
     * @return this.line
     */
    public String getLine() {
        return this.line;
    }

    /**
     * Compares given image with this.image
     *
     * @param oImage
     * @return true if given image equals image
     */
    public Boolean compareImage(String oImage) {
        return oImage == this.image;
    }
}
