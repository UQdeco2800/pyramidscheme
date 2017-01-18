package uq.deco2800.pyramidscheme.cards.supercards;

/**
 * A UI placeholder for when no card should exist.
 *
 * @author Millie
 */

public class NullCard extends Card {

    public NullCard(String name, String imgPath) {
        super(name, imgPath, null, 0);
    }

    public String toString() {
        return "NC";
    }

    public String getType() {
        return "NC";
    }
}
