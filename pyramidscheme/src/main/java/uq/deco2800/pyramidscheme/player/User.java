package uq.deco2800.pyramidscheme.player;

import uq.deco2800.pyramidscheme.deck.CardList;
import uq.deco2800.pyramidscheme.deck.Deck;
import uq.deco2800.pyramidscheme.game.GameManager;

import java.util.List;

/**
 * A representation of a real player.
 *
 * @author Millie
 */

public class User extends Player {

    protected int currency;
    protected int cardPacks;
    private static final int INITIAL_CARDPACKS = 3;
    private static final int INITIAL_CURRENCY = 100;

    public User(String name, Deck deck) {
        super(name, deck);
        currency = INITIAL_CURRENCY;
        cardPacks = INITIAL_CARDPACKS;
    }

    public User(String name, List<Deck> decks, CardList cardList) {
        super(name, decks, cardList);
        currency = INITIAL_CURRENCY;
        cardPacks = INITIAL_CARDPACKS;
    }

    /**
     * Get the player's total number of card pack
     *
     * @return integer representing number of card pack
     */
    public int getCardPacks() {
        return cardPacks;
    }

    /**
     * Set the player's total number of card pack
     *
     * @param cardPack integer representing number of card pack
     */
    public void setCardPacks(Integer cardPacks) {
        this.cardPacks = cardPacks;
    }

    /**
     * Update the player's total number of card pack.
     *
     * @param cardPack integer representing number of card pack
     */
    public void updateCardPack(Integer newCardPack) {
        this.cardPacks += newCardPack;
    }

    /**
     * Gets the players current level
     *
     * @return the players current level
     */
    public int getLevel() {
        return Integer.parseInt(GameManager.getInstance()
                .getStatisticsTracking().getUserStats().getUserLevel());
    }

    /**
     * Get the player's total currency
     *
     * @return integer representing currency
     */
    public int getCurrency() {
        return currency;
    }

    /**
     * Set the player's total currency
     *
     * @param currency integer representing currency
     */
    public void setCurrency(Integer currency) {
        this.currency = currency;
    }

    /**
     * Modify the player's total currency. If it falls below 0,
     * set it to 0.
     *
     * @param currency integer representing currency
     */
    public void modifyCurrency(Integer currency) {
        this.currency += currency;
        if (this.currency < 0) {
            this.currency = 0;
        }
    }

}
