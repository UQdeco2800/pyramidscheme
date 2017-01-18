package uq.deco2800.pyramidscheme.cards.supercards;

import javafx.scene.image.Image;
import uq.deco2800.pyramidscheme.actions.Action;
import uq.deco2800.pyramidscheme.board.RecBoard;
import uq.deco2800.pyramidscheme.board.RecTile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Abstract class to represent individual cards. MinionCard and ActionCard
 * extend it, and all cards should extend one of those two classes.
 *
 * @author Millie
 */

public abstract class Card {
    private String cardName;
    private String cardImg;
    private int cost;
    private String unboxImg;
    private int cardRank;
    private Action cardAction;
    private static HashMap<String, Image> imageCache;

    // Final card sizes so all cards are the same
    private static final int CARD_WIDTH = 65;
    private static final int CARD_HEIGHT = 90;
    private static final Logger LOGGER = Logger.getLogger(Card.class.getName());

    /**
     * Creates a new card, Card by itself is just a placeholder for further implementation by MinionCard or ActionCard
     *
     * @param name     the name of the card
     * @param imgPath  the path of the card image
     * @param unboxed  the path of the unboxed image
     * @param cardRank the rank of the card (Used for creating a new deck)
     * @param cost     the duck dust cost of the card
     */
    public Card(String name, String imgPath, String unboxed, int cardRank,
                int cost) {

        this.cost = cost;
        if (imageCache == null) {
            imageCache = new HashMap<>();
        }
        this.cardName = name;
        this.cardRank = cardRank;
        this.cardAction = null;
        cardImg = imgPath;
        unboxImg = unboxed;
    }

    /**
     * Creates a new card, Card by itself is just a placeholder for further implementation by MinionCard or ActionCard
     *
     * @param name     the name of the card
     * @param imgPath  the path of the card image
     * @param unboxed  the path of the unboxed image
     * @param cardRank the rank of the card (Used for creating a new deck)
     */
    public Card(String name, String imgPath, String unboxed, int cardRank) {
        this(name, imgPath, unboxed, cardRank, 1);
    }

    /**
     * Returns an image from the string location
     *
     * @param string the location of the image to be fetched
     * @return returns null if an error occurs, otherwise will return an Image loaded from memory
     */
    private Image getImage(String string) {
        if (imageCache.containsKey(string)) {
            return imageCache.get(string);
        } else {
            try {
                Image image = new Image(getClass().
                        getResourceAsStream(string));

                imageCache.put(string, image);
                return image;
            } catch (Exception ex) {
                LOGGER.log(Level.SEVERE, "Tried to fetch image: " + string);
                LOGGER.log(Level.SEVERE, ex.getMessage());
                return null;
            }
        }
    }

    /**
     * Getter method that returns the dust of Card instance
     *
     * @return cost
     */
    public int getCost() {
        return cost;
    }

    /**
     * Getter method that returns the name of Card instance
     *
     * @return cardName
     */
    public String getName() {
        return cardName;
    }

    /**
     * Getter method that returns the Action of Card instance
     *
     * @return cardAction
     */
    public Action getAction() {
        return cardAction;
    }

    /**
     * Getter method that returns Card Width
     *
     * @return CARD_WIDTH
     */
    public static int getCardWidth() {
        return CARD_WIDTH;
    }

    /**
     * Getter method that returns Card Height
     *
     * @return CARD_HEIGHT
     */
    public static int getCardHeight() {
        return CARD_HEIGHT;
    }

    /**
     * To be overridden by MinionCard and ActionCard
     *
     * @return returns a string literal of the representation of the card
     */
    public abstract String getType();

    /**
     * Getter method returns the rank of Card instance
     *
     * @return cardRank
     */
    public int getRank() {
        return this.cardRank;
    }

    /**
     * Creates and returns a list of random cards of length cardNum. The cards are assigned using rank weighting
     *
     * @param cardNum the number of cards to return
     * @return List of MinionCard's generated of length cardNum
     */
    public static List<MinionCard> getMinionCards(int cardNum) {
        // Arraylist to store MinionCards
        ArrayList<MinionCard> cardSet = new ArrayList<MinionCard>();

        // Add MinionCards to cardSet
        for (MinionCard card : MinionCard.getCards().values()) {
            for (int i = 0; i < card.getRank(); i++) {
                cardSet.add(card);
            }
        }

        ArrayList<MinionCard> rt = new ArrayList<>();
        Random rand = new Random();

        // Randomly add cardNum MinionCards to ArrayList rt
        for (int i = 0; i < cardNum; i++) {
            int pos = rand.nextInt(cardSet.size());
            rt.add(cardSet.get(pos).dup());
        }

        return rt;
    }

    /**
     * Sets the Action of this Card
     *
     * @param action this Card's new Action.
     */
    public void setAction(Action action) {
        this.cardAction = action;
        this.cardAction.setTarget(this);
    }

    /**
     * Checks if the card has an Action and calls said action
     *
     * @param attackingTile the tile where the turn is coming from
     * @param board         the board that the tile is on
     */
    public void processTurn(RecTile attackingTile, RecBoard board) {
        if (this.cardAction != null) {
            this.cardAction.turn(attackingTile, board);
        }
    }

    /**
     * Returns this Card's image.
     *
     * @return this Card's image.
     */
    public Image getImage() {
        return getImage(cardImg);
    }

    /**
     * Return the user's hidden card image.
     *
     * @return the user's hidden card image.
     */
    public Image getUserImg() {
        return getImage(CardHiddenState.USERHIDDEN.toString());
    }

    /**
     * Return the deck-hidden card image.
     *
     * @return return the deck-hidden card image.
     */
    public Image getDeckImg() {
        return getImage(CardHiddenState.DECKHIDDEN.toString());
    }

    /**
     * Return the AI's hidden card image
     *
     * @return the AI's hidden card image
     */
    public Image getAiImg() {
        return getImage(CardHiddenState.AIHIDDEN.toString());
    }

    /**
     * Return the path of the image, used for duplicating the card
     *
     * @return string location of the card image
     */
    public String getImgPath() {
        return cardImg;
    }

    /**
     * Return the path of the unboxed image, used for duplicating the card
     *
     * @return string location of the unboxed image
     */
    public String getUnboxPath() {
        return unboxImg;
    }

    /**
     * Gets the image of a card with the background removed
     *
     * @return returns null if there isn't an unboxed image, otherwise returns the unboxed Image. If an error occurs in
     * loading, then null is returned
     */
    public Image getUnboxImg() {
        if (unboxImg == null) {
            return null;
        } else {
            return getImage(unboxImg);
        }
    }

    @Override
    public abstract String toString();

    /**
     * Returns true if o is a Card with a cardName equal to this card's
     * cardName.
     *
     * @param o An object to be compared to this Card
     * @return true if o is a Card with a cardName equal to this card's
     * cardName.
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof Card) {
            Card c = (Card) o;
            return c.getName().equals(this.getName());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return cardName.hashCode();
    }
}
