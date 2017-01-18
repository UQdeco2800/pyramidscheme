package uq.deco2800.pyramidscheme.player;

import uq.deco2800.pyramidscheme.cards.supercards.Card;
import uq.deco2800.pyramidscheme.cards.supercards.MinionCard;
import uq.deco2800.pyramidscheme.deck.CardList;
import uq.deco2800.pyramidscheme.deck.Deck;

import java.util.ArrayList;
import java.util.List;

/**
 * An abstract class to represent players, real and AI.
 *
 * @author Millie
 * @author Billy-7
 * @author bijelo9
 */
public abstract class Player {

    /**
     * the amount of decks a player has
     */
    public static final int DECK_SLOTS = 4;

    protected static final int MAX_HEALTH = 40;
    protected List<Deck> decks;
    protected CardList ownedCards;
    protected String name;
    protected int deckSlot;
    protected int maxHealth;

    /**
     * Constructor. Players must be created with a name and a deck!
     *
     * @param name Player's name
     * @param deck Player's deck
     */
    public Player(String name, Deck deck) {
        this(name, createDecks(deck, DECK_SLOTS), getStarterCards());
    }

    /**
     * Constructor. Players must be created with a name and a deck!
     *
     * @param name   Player's name
     * @param deck   Player's deck
     * @param health Player's health
     */
    public Player(String name, Deck deck, int health) {
        this(name, createDecks(deck, DECK_SLOTS), getStarterCards());
        maxHealth = health;
    }

    /**
     * Create a player with `name`, list of `decks` and list of cards `owned`
     *
     * @param name       Player's name
     * @param decks      Player's decks
     * @param ownedCards Player's owned cards
     */
    public Player(String name, List<Deck> decks, CardList ownedCards) {
        this.name = name;
        this.ownedCards = ownedCards;
        this.decks = decks;
        maxHealth = MAX_HEALTH;
        deckSlot = 0;
    }

    /**
     * Create a list of starter cards
     */
    private static CardList getStarterCards() {
        CardList cards = new CardList();

        for (MinionCard mc : MinionCard.getCards().values()) {
            if (mc.getRank() <= 2) {
                cards.addCard(mc);
            }
        }
        return cards;
    }

    /**
     * Returns a list of size `amount`, of copies of `deck`
     *
     * @param deck   deck to copy
     * @param amount number of copies to create
     * @return List of copied decks
     */
    public static List<Deck> createDecks(Deck deck, int amount) {
        List<Deck> deckList = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            deckList.add(new Deck(deck));
        }
        return deckList;
    }

    /**
     * Increase player owned card amount for `card` by `amount`
     *
     * @param card   card to unlock
     * @param amount amount of cards to unlock
     */
    public void unlockCard(Card card, int amount) {
        ownedCards.addCard(card, amount);
    }

    /**
     * Return boolean of if the player has at least 1 of `card`
     *
     * @param card card to check
     * @return true if player owns at least 1 of `card`, 0 otherwise
     */
    public boolean hasCard(Card card) {
        return ownedCards.containsCard(card);
    }

    /**
     * Returns the amount of `card` the player owns.
     *
     * @param card card to check
     * @return integer of the amount of `card` the player owns.
     */
    public int cardAmount(Card card) {
        return ownedCards.getAmount(card);
    }

    /**
     * Removes `amount` of `card` from the players owned cards
     *
     * @param card   card to remove
     * @param amount amount of cards to remove
     */
    public void removeCard(Card card, int amount) {
        ownedCards.removeCard(card, amount);
    }

    /**
     * Checks the maximum number of usages of this card in
     * any one deck
     *
     * @param card to be checked
     * @return integer representing the number of occurrences of this card in the deck in which it occurs the most
     */
    public int maxUses(Card card) {
        int count = 0;
        for (int i = 0; i < DECK_SLOTS; i++) {
            count = Math.max(count, this.decks.get(i).getAmount(card));
        }
        return count;
    }

    /**
     * Gets the name of the player
     *
     * @return String of name of player
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the current deck in use by the player
     *
     * @return Deck in use
     */
    public Deck getDeck() {
        return decks.get(deckSlot);
    }

    /**
     * Set the deck in current in use slot to given deck
     *
     * @param deck given deck to set
     */
    public void setDeck(Deck deck) {
        decks.set(deckSlot, deck);
    }

    /**
     * Gets the deck in slot `idx`
     *
     * @param idx slot to get
     * @return Deck in slot idx, if invalid `idx` returns null
     */
    public Deck getDeck(int idx) {
        if (0 <= idx && idx < DECK_SLOTS) {
            return decks.get(idx);
        }
        return null;
    }

    /**
     * Gets the slot number of the current deck in use
     *
     * @return integer of current slot in use
     */
    public int getDeckSlot() {
        return deckSlot;
    }

    /**
     * Set the which slot which holds the deck in use, if slot given is negative or >= DECK_SLOTS slot is unchanged
     *
     * @param num given slot to set to
     */
    public void setDeckSlot(int num) {
        if (0 <= num && num < DECK_SLOTS) {
            deckSlot = num;
        }
    }

    /**
     * Gets the max health of the player
     *
     * @return int of max health
     */
    public int getMaxHealth() {
        return maxHealth;
    }

    /**
     * Set the deck in given slot to given deck
     *
     * @param idx  given slot
     * @param deck given deck to set
     */
    public void setDeck(int idx, Deck deck) {
        if (0 <= idx && idx < DECK_SLOTS) {
            decks.set(idx, deck);
        }
    }
}
