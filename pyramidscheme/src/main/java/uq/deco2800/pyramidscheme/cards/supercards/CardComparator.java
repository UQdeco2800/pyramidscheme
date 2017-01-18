package uq.deco2800.pyramidscheme.cards.supercards;

import java.util.Comparator;

/**
 * Compares two card, used to sort a list of cards or a deck
 *
 * @author billy-7
 */
public class CardComparator implements Comparator<Card> {
    @Override
    public int compare(Card card1, Card card2) {
        return card1.getName().compareTo(card2.getName());
    }
}
