package uq.deco2800.pyramidscheme.deck;

import uq.deco2800.pyramidscheme.cards.supercards.Card;
import uq.deco2800.pyramidscheme.cards.supercards.CardComparator;
import uq.deco2800.pyramidscheme.cards.supercards.NullCard;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A basic card deck representation.
 *
 * @author Millie
 * @author Billy-7
 */

public class Deck implements Iterable<Card> {

    private static final String DEFAULT_NAME = "New Deck";
    private List<Card> cards;
    private String name;

    /**
     * Create an empty deck with default name
     */
    public Deck() {
        this(DEFAULT_NAME);
    }

    /**
     * Create an empty deck with `name`
     *
     * @param name String name of deck
     */
    public Deck(String name) {
        cards = new ArrayList<>();
        this.name = name;
    }

    /**
     * Create a deck with cards from `other` and deck name from `other`
     * Copies `other` into new deck
     *
     * @param other Deck to copy
     */
    public Deck(Deck other) {
        this(other, other.getName());
    }

    /**
     * Create a deck with cards from `other` and deck name `name`
     *
     * @param other Deck to get cards from
     * @param name  String name of deck
     */
    public Deck(Deck other, String name) {
        cards = other.getCards();
        this.name = name;
    }

    /**
     * Create a deck with cards from `cardList` and default deck name
     *
     * @param cardList
     */
    public Deck(CardList cardList) {
        this(cardList, DEFAULT_NAME);
    }

    /**
     * Create a deck with cards from `cardList` and deck name `name`
     *
     * @param cardList CardList to get cards from
     * @param name     String name of deck
     */
    public Deck(CardList cardList, String name) {
        cards = cardList.getCards();
        this.name = name;
    }

    /**
     * Get NullCard
     *
     * @return Card NullCard
     */
    public static Card getNullCard() {
        return new NullCard("NullCard", "/cardImages/nullCard.png");
    }

    /**
     * Gets the name of the deck
     *
     * @return String name of deck
     */
    public String getName() {
        return name;
    }

    /**
     * Get a list of cards in deck
     *
     * @return List<Card> of cards from deck
     */
    public List<Card> getCards() {
        return new ArrayList<>(cards);
    }

    /**
     * Get the amount of `card` in deck
     *
     * @param card Card to check
     * @return integer representing number of instances of the card in this deck
     */
    public int getAmount(Card card) {
        int count = 0;
        for (Card countCard : cards) {
            if (card.equals(countCard)) {
                count++;
            }
        }
        return count;
    }

    /**
     * Get the amount of cards in deck
     *
     * @return int amount of cards in deck
     */
    public int size() {
        return cards.size();
    }

    /**
     * Compare this deck to `other`
     *
     * @param other Deck to compare
     * @return true if all cards in this deck are in `other` and all cards in `other` are in this deck
     */
    public boolean equalTo(Deck other) {
        if (other == null || cards.size() != other.size() || !name.equals(other.getName())) {
            return false;
        }
        List<Card> thisCards = new ArrayList<>(cards);
        List<Card> otherCards = new ArrayList<>(other.getCards());
        thisCards.sort(new CardComparator());
        thisCards.sort(new CardComparator());
        for (int i = 0; i < cards.size(); i++) {
            if (!thisCards.get(i).equals(otherCards.get(i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Iterator<Card> iterator() {
        return new ArrayList<>(cards).iterator();
    }

}
