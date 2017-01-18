package uq.deco2800.pyramidscheme.deck;

import uq.deco2800.pyramidscheme.cards.supercards.Card;

import java.util.ArrayList;
import java.util.List;

/**
 * A list of the ViewCards being displayed
 *
 * @author billy-7
 */
public class ViewCardList {

    private static final int MAX_CARDS = 6;
    private List<ViewCard> cards;

    /**
     * Create empty list of ViewCards
     */
    public ViewCardList() {
        cards = new ArrayList<>();
    }

    /**
     * Get all ViewCards
     *
     * @return a list of ViewCards
     */
    public List<ViewCard> getCards() {
        return cards;
    }

    /**
     * Get a ViewCard from index idx
     *
     * @param idx index of card to retrieve
     * @return ViewCard from index idx
     */
    public ViewCard getCard(int idx) {
        return cards.get(idx);
    }

    /**
     * Add a ViewCard to the list of ViewCards if list is not full
     *
     * @param card card to be added
     * @param x    coordinate that card is placed
     * @param y    coordinate that card is placed
     */
    public void addCard(Card card, int x, int y) {
        addCard(new ViewCard(card, x, y));
    }

    /**
     * Add a ViewCard to the list if not full
     *
     * @param card ViewCard to add
     */
    public void addCard(ViewCard card) {
        if (cards.size() < MAX_CARDS) {
            cards.add(card);
        }
    }

    /**
     * Remove a card from list by index number
     *
     * @param idx index of card to be removed
     */
    public void removeCard(int idx) {
        cards.remove(idx);
    }

    /**
     * Remove all cards from list
     */
    public void removeAllCards() {
        cards = new ArrayList<ViewCard>();
    }

    /**
     * Get the card at click coordinates x,y
     *
     * @param x coordinate of click
     * @param y coordinate of click
     * @return card at coordinates x,y or null if no card found
     */
    public ViewCard getClickedCard(int x, int y) {
        for (ViewCard card : cards) {
            if (card.containsClick(x, y)) {
                return card;
            }
        }
        return null;
    }

}
