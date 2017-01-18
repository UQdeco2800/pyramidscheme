package uq.deco2800.pyramidscheme.deck;

import uq.deco2800.pyramidscheme.cards.supercards.Card;
import uq.deco2800.pyramidscheme.cards.supercards.CardComparator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A list of distinct cards each with an
 *
 * @author Billy-7
 */

public class CardList {

    private Map<Card, Integer> cardMap;

    /**
     * Create an empty CardList
     */
    public CardList() {
        cardMap = new HashMap<>();
    }

    /**
     * Create a CardList from all the card in `deck`
     *
     * @param deck Deck of cards to get from
     */
    public CardList(Deck deck) {
        this();
        for (Card card : deck.getCards()) {
            addCard(card);
        }
    }

    /**
     * Check if this CardList has at least 1 `card`
     *
     * @param card Card to check
     * @return true if this contains at least 1 `card`, false otherwise
     */
    public boolean containsCard(Card card) {
        return getCard(card) != null;
    }

    /**
     * Normalise card to a card in this list or null
     *
     * @param card Card to normalise
     * @return Card found in this list or null if no card found
     */
    private Card getCard(Card card) {
        for (Card c : cardMap.keySet()) {
            if (c.equals(card)) {
                return c;
            }
        }
        return null;
    }

    /**
     * Increase the amount of card in the list by 1 if none in the list then add it
     *
     * @param card Card to add
     */
    public void addCard(Card card) {
        addCard(card, 1);
    }

    /**
     * Increase the amount of `card` in the list by `num`, if none in the list then add it
     *
     * @param card Card to add
     * @param num  int amount to add
     */
    public void addCard(Card card, int num) {
        if (card == null || num <= 0) {
            return;
        }
        Card c = getCard(card);
        if (c == null) {
            cardMap.put(card, num);
        } else {
            cardMap.replace(c, cardMap.get(c) + num);
        }
    }

    /**
     * Decrease the amount of card in the list by 1 if none left then remove card from list
     *
     * @param card Card to remove
     */
    public void removeCard(Card card) {
        removeCard(card, 1);
    }

    /**
     * Decrease the amount of `card` in the list by `num`, if none left then remove `card` from list
     *
     * @param card Card to remove
     * @param num  int amount to remove
     */
    public void removeCard(Card card, int num) {
        if (card == null || num <= 0) {
            return;
        }
        Card c = getCard(card);
        if (c == null) {
            return;
        } else if (cardMap.get(c) <= num) {
            cardMap.remove(c);
        } else {
            cardMap.replace(c, cardMap.get(c) - num);
        }
    }

    /**
     * Gets a sorted list of cards in the card list
     *
     * @return List of distinct cards int the list (sorted)
     */
    public List<Card> getDistinctCards() {
        List<Card> cards = new ArrayList<Card>();
        cards.addAll(cardMap.keySet());
        cards.sort(new CardComparator());
        return cards;
    }

    /**
     * Gets all cards in the card list (including duplicates)
     *
     * @return List of all cards in the list
     */
    public List<Card> getCards() {
        List<Card> list = new ArrayList<Card>();
        for (Card card : cardMap.keySet()) {
            for (int i = 0; i < cardMap.get(card); i++) {
                list.add(card);
            }
        }
        return list;
    }

    /**
     * Gets the amount of `card` in the list
     *
     * @param card Card to be checked
     * @return int amount of `card` in the list
     */
    public int getAmount(Card card) {
        Card c = getCard(card);
        if (c == null) {
            return 0;
        } else {
            return cardMap.get(c);
        }
    }

    /**
     * Get the total amount of cards in the list
     *
     * @return int amount of cards in the list
     */
    public int getSize() {
        int size = 0;
        for (int value : cardMap.values()) {
            size += value;
        }
        return size;
    }

    /**
     * Get the total amount of distinct cards in the list
     *
     * @return int amount of distinct cards in the list
     */
    public int getDistinctSize() {
        return cardMap.size();
    }

    /**
     * Get a filtered CardList
     * Filtered card names have contain `search` and have card type `type`
     *
     * @param search String to search in card names
     * @param type   String type of cards to get
     * @return CardList of filtered cards
     */
    public CardList getFiltered(String search, String type) {
        String searchLower = search.toLowerCase();
        CardList list = new CardList();
        for (Card card : cardMap.keySet()) {
            if ((type == null || card.getType().equals(type)) &&
                    card.getName().toLowerCase().contains(searchLower)) {
                list.addCard(card, getAmount(card));
            }
        }
        return list;
    }

    /**
     * Compare this to another CardList
     *
     * @param other CardList to compare to
     * @return true if all cards in this are in `other` and all cards in `other` are in this, false otherwise
     */
    public boolean equalTo(CardList other) {
        if (other == null || other.getSize() != getSize() ||
                other.getDistinctSize() != getDistinctSize()) {
            return false;
        }
        for (Card card : cardMap.keySet()) {
            if (other.getAmount(card) != getAmount(card)) {
                return false;
            }
        }
        return true;
    }
}
