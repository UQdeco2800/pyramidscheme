package uq.deco2800.pyramidscheme.match;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uq.deco2800.pyramidscheme.board.TileOccupant;
import uq.deco2800.pyramidscheme.cards.supercards.Card;
import uq.deco2800.pyramidscheme.cards.supercards.CardHiddenState;
import uq.deco2800.pyramidscheme.cards.supercards.MinionCard;

/**
 * A representation of cards during matches. Designed to separate card variables which can change
 * during matches (represented here) from those which shouldn't (represented in Card).
 *
 * @author Millie
 */

public class MatchCard extends TileOccupant {

    private Card card;
    private int xCoord;
    private int yCoord;
    private int attack = 0;
    private int health = 0;
    private int uid = 0;
    Logger logger = LoggerFactory.getLogger(MatchCard.class);

    public MatchCard(Card card, int xCoord, int yCoord) {
        this.card = card;
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        if (card instanceof MinionCard) {
            this.attack = ((MinionCard) card).getAttack();
            this.health = ((MinionCard) card).getDefense();
        }
    }

    /**
     * Returns the presence of the x and y coordinates within the bounding box
     * of this MatchCard
     *
     * @param x is an x screenspace coordinate.
     * @param y is a y screenspace coordinate.
     * @return returns true if the provided x and y coordinates are contained
     * within this MatchCard on screen.
     */
    public boolean containsCoords(int x, int y) {
        return !(x < xCoord || x > (xCoord + Card.getCardWidth()) ||
                y < yCoord || y > (yCoord + Card.getCardHeight()));
    }

    /**
     * Returns the card contained within this MatchCard
     *
     * @return the card contained within the MatchCard
     */
    public Card getCard() {
        return card;
    }


    /**
     * Returns the attack of this card. If this card is not a Minion, this
     * method should return 0
     *
     * @return the attack of this card
     */
    public int getAttack() {
        return this.attack;
    }

    /**
     * Returns the duck dust cost of this card.
     *
     * @return the cost of this card
     */
    public int getCost() {
        return card.getCost();
    }

    /**
     * Returns the health of this card. If this card is not a Minion, this
     * method should return 0
     *
     * @return the health of this card
     */
    public int getHealth() {
        return this.health;
    }

    /**
     * Returns the strategic of this card, to be used by the AI.
     *
     * @return the value of this card
     */
    public int getValue() {
        return getAttack() + getHealth();
    }

    /**
     * Adds a specified amount of health to the health of the MatchCard. This
     * number could be negative to take away the health of the MatchCard.
     *
     * @param diff The difference of health.
     */
    public void changeHealth(int diff) {
        if (this.card instanceof MinionCard) {
            this.health += diff;
        }
    }

    public void changeAttack(int diff) {
        if (this.card instanceof MinionCard) {
            this.attack += diff;
        }
    }

    /**
     * Sets the health of a Matchcard to a given value.
     *
     * @param health The health a card is set to have.
     */
    public void setHealth(int health) {
        this.health = health;
    }

    /**
     * Sets the attack of a Matchcard to a given value.
     *
     * @param attack The attack a card is set to have.
     */
    public void setAttack(int attack) {
        this.attack = attack;
    }

    /**
     * Multiplies the health of a Matchcard by a given value.
     *
     * @param multiplier The factor which a card's health is multiplied by.
     */
    public void multiplyHealth(double multiplier) {
        Double newHealth = new Double(Math.floor(multiplier * this.getHealth()));
        int i = newHealth.intValue();
        this.setHealth(i);
    }

    /**
     * Multiplies the attack of a Matchcard by a given value.
     *
     * @param multiplier The factor which a card's attack is multiplied by.
     */
    public void multiplyAttack(double multiplier) {
        Double newAttack = new Double(Math.floor(multiplier * this.getAttack()));
        int i = newAttack.intValue();
        this.setAttack(i);
    }


    public void resetAttack() {
        if (this.card instanceof MinionCard) {
            this.attack = ((MinionCard) this.card).getAttack();
        }
    }

    public void resetHealth() {
        if (this.card instanceof MinionCard) {
            this.health = ((MinionCard) this.card).getDefense();
        }
    }

    public int getX() {
        return xCoord;
    }

    public void setX(int x) {
        this.xCoord = x;
    }

    public int getY() {
        return yCoord;
    }

    public void setY(int y) {
        this.yCoord = y;
    }

    @Override
    public String toString() {
        return card.getName() + "[" + Integer.toString(getAttack()) + ", " + Integer.toString(getHealth()) + "]" + " at (" + Integer.toString(xCoord) + ", " + Integer.toString(yCoord) + ")";
    }

    /**
     * Draws the front face of the MatchCard to the screen
     *
     * @param gc The GraphicsContext to draw to
     */
    @Override
    public void draw(GraphicsContext gc) {
        drawCard(gc, card.getImage(), CardHiddenState.VISIBLE);
    }

    private void drawCard(GraphicsContext gc, Image img, CardHiddenState state) {
        drawCard(gc, img, xCoord, yCoord, Card.getCardWidth(), Card.getCardHeight(), state);
    }

    private void drawCard(GraphicsContext gc, Image img, int x, int y, int width, int height, CardHiddenState state) {
        gc.drawImage(img, (double) x, (double) y, (double) width, (double) height);
        if (state == CardHiddenState.VISIBLE) {
            gc.setStroke(Color.WHITE);
            gc.setEffect(new DropShadow(5, Color.BLACK));
            gc.strokeText(String.valueOf(getAttack()), x + width * 5 / 8, y + 11, width / 6);
            gc.strokeText(String.valueOf(getHealth()), x + width * 13 / 16, y + 13, width / 6);
            gc.strokeText(String.valueOf(getCost()), x + width * 1 / 16, y + height * 3 / 12, width / 6);
            gc.setEffect(null);
            gc.setStroke(Color.BLACK);
        }
    }

    /**
     * Draws the rear face of the MatchCard to the screen using the CardState enum
     *
     * @param gc The GraphicsContext to draw to
     */
    public void drawHidden(GraphicsContext gc, CardHiddenState hiddenType) {
        switch (hiddenType) {
            case VISIBLE:
                drawCard(gc, card.getImage(), hiddenType);
                break;
            case USERHIDDEN:
                drawCard(gc, card.getUserImg(), hiddenType);
                break;
            case AIHIDDEN:
                drawCard(gc, card.getAiImg(), hiddenType);
                break;
            case DECKHIDDEN:
                drawCard(gc, card.getDeckImg(), hiddenType);
                break;
            case PLACED:
                if (card.getUnboxImg() == null) {
                    drawCard(gc, card.getImage(), CardHiddenState.VISIBLE);
                } else {
                    drawCard(gc, card.getUnboxImg(), CardHiddenState.VISIBLE);
                }
                break;
            default:
                break;
        }
    }

    /**
     * Draws the front face of the MatchCard *slightly bigger* (currently
     * hardcoded as 20 pixels wider), to the screen
     *
     * @param gc The GraphicsContext to draw to
     */
    public void drawBigger(GraphicsContext gc) {
        int xDilation = 20;
        int yDilation = xDilation * Card.getCardHeight() / Card.getCardWidth();

        // Get the current dimensions and add our new dilation factor to it
        drawCard(gc, card.getImage(), xCoord - (xDilation / 2), yCoord - (yDilation / 2),
                Card.getCardWidth() + xDilation, Card.getCardHeight() + yDilation,
                CardHiddenState.VISIBLE);
    }

    public String getType() {
        return card.getType();
    }


    /**
     * Takes another MatchCard and attempts to make them fight. If both of them
     * are MinionCards, and they lose sufficient damage, they are marked for
     * death.
     *
     * @param attacker The card that needs to fight this MatchCard
     */
    public void attackedBy(MatchCard attacker) {
        if (this.getCard() instanceof MinionCard &&
                attacker.getCard() instanceof MinionCard) {

            logger.info(attacker.toString() + " attacked " + this.toString());
            if (card.getAction() != null && card.getAction().attacked(this, attacker)) {
                if (this.getHealth() <= 0) {
                    setIsToDie(true);
                }
                if (attacker.getHealth() <= 0) {
                    attacker.setIsToDie(true);
                }
                return;
            }

            // Trade damage
            this.changeHealth(-attacker.getAttack());
            attacker.changeHealth(-this.getAttack());

            if (this.getHealth() <= 0) {
                setIsToDie(true);
            }

            if (attacker.getHealth() <= 0) {
                attacker.setIsToDie(true);
            }
        }
    }

    /**
     * This returns the uid of the card. In a regular game,
     * this has no effect however in a multiplayer game,
     * it returns the uid which would have been set by
     * the server.
     *
     * @return the Card's uid
     */
    public int getUid() {
        return uid;
    }

    /**
     * This is used by the server to set the uid of the matchcard
     *
     * @param uid a random integer not colliding with any other card
     */
    public void setUid(int uid) {
        this.uid = uid;
    }

    /**
     * Sets the card to a different minioncard. Useful for multiplayer
     * Also updates the health and damage of the card.
     *
     * @param card the new card
     */
    public void setCard(MinionCard card) {
        this.card = card;
        this.attack = card.getAttack();
        this.health = card.getDefense();
    }
}
