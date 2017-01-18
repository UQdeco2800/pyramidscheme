package uq.deco2800.pyramidscheme.match;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import uq.deco2800.pyramidscheme.cards.supercards.CardHiddenState;
import uq.deco2800.pyramidscheme.deck.Deck;

import java.util.*;
import java.util.stream.Collectors;

public class MatchDeck implements Iterable<MatchCard> {

    /**
     * The main datastructure that stores all the cards in the deck.
     */
    private LinkedList<MatchCard> cards;

    /**
     * The position of the top left hand corner of the board
     */
    private Point2D origin;

    public MatchDeck(int x, int y) {
        this.cards = new LinkedList<>();
        this.origin = new Point2D(x, y);
    }

    public MatchDeck(int x, int y, Deck deck) {
        this(x, y);

        List<MatchCard> toAdd = deck.getCards()
                .stream()
                .map(card -> new MatchCard(card, 0, 0))
                .collect(Collectors.toCollection(ArrayList::new));

        pushCards(toAdd);
    }


    /**
     * Adds a list of MatchCards to the deck, such that the last card in the list
     * is the last card to be pushed onto the deck
     *
     * @param cards The cards to be added to the deck
     * @require The cards be sorted in the order in which they died, where
     * the card that died first is at the head of the list, in the case that
     * this MatchDeck was being used as a graveyard
     */
    public void pushCards(List<MatchCard> cards) {
        for (MatchCard card : cards) {
            pushCard(card);
        }
    }

    /**
     * Adds a MatchCard to the top of the deck
     *
     * @param card The card to be added
     */
    public void pushCard(MatchCard card) {
        cards.push(card);
        alignCards();
    }

    /**
     * Draws this MatchDeck and its children to the screen.
     * <p>
     * The card added last should be drawn last
     *
     * @param gc The GraphicsContext to draw to
     */
    public void draw(GraphicsContext gc) {
        for (int i = cards.size() - 1; i >= 0; i--) {
            cards.get(i).draw(gc);
        }
    }

    /**
     * Draws this MatchDeck and its children to the screen.
     * The cards will have their backs facing the player.
     * <p>
     * The card added last should be drawn last
     *
     * @param gc The GraphicsContext to draw to
     */
    public void drawHidden(GraphicsContext gc) {
        for (int i = cards.size() - 1; i >= 0; i--) {
            cards.get(i).drawHidden(gc, CardHiddenState.AIHIDDEN);
        }
    }

    /**
     * Neatly aligns all the cards on the deck, so the deck looks pretty
     * after you've shuffled it
     */
    public void alignCards() {
        for (int i = cards.size() - 1; i >= 0; i--) {
            MatchCard card = cards.get(i);
            card.setX((int) origin.getX() + (cards.size() - i) * 2);
            card.setY((int) origin.getY() + (cards.size() - i) * 2);
        }
    }


    /**
     * Attempts to pop the specified number of cards from the top of the deck,
     * and returns a list where the last card of the list was the last card that
     * was popped off the deck
     *
     * @param number The number of requested cards
     */
    public List<MatchCard> popCards(int number) {
        List<MatchCard> collector = new ArrayList<>();

        for (int i = 0; i < number; i++) {
            if (!cards.isEmpty()) {
                collector.add(cards.pop());
            }
        }

        return collector;
    }

    /**
     * Shuffle the cards in the deck.
     * <p>
     * The cards will stay in their current screenspace positions, but their
     * draw order will change, so repeated shuffle calls can be used for primitive
     * animation.
     */
    public void shuffle() {
        Collections.shuffle(cards);
    }

    /**
     * An iterator of all the MatchCards contained within this deck
     *
     * @return An iterator of MatchCards contained within this deck
     */
    @Override
    public Iterator<MatchCard> iterator() {
        return new ArrayList<>(cards).iterator();
    }

    /**
     * Returns the number of MatchCards contained within this deck
     *
     * @return The number of cards
     */
    public int size() {
        return cards.size();
    }

    /**
     * Returns whether this graveyard is empty
     *
     * @return A boolean of whether this graveyard is empty
     */
    public boolean isEmpty() {
        return cards.isEmpty();
    }


    /**
     * Updates the origin of the MatchDeck and aligns the cards to the new location
     *
     * @param matchDeckX the new X origin
     * @param matchDeckY the new Y origin
     */
    public void setOrigin(int matchDeckX, int matchDeckY) {
        origin = new Point2D(matchDeckX, matchDeckY);
        alignCards();
    }
}
