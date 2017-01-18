package uq.deco2800.pyramidscheme.addinfo;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import uq.deco2800.pyramidscheme.match.MatchCard;

/**
 * Class to create the additional information pane which consists of the
 * enlarged card, attack, health, dust and actions.
 *
 * @author Dayan
 */
public class AddInfo {
    private int cardHealth;
    private int cardAttack;
    private int cardDust;

    private Image imgAttack = new Image("/actionImages/swordIcon.png");
    private Image imgHealth = new Image("/actionImages/heartIcon.png");
    private Image imgDust = new Image("/actionImages/dustIcon.png");
    private Image imgAction;
    private Image imgCard;

    /**
     * Creates new instance of additional information which just stores the
     * required information taken from a MatchCard.
     *
     * @param card MatchCard to display additional information about.
     */
    public AddInfo(MatchCard card) {
        this.cardAttack = card.getAttack();
        this.cardHealth = card.getHealth();
        this.cardDust = card.getCost();
        this.imgCard = card.getCard().getImage();

        if (card.getCard().getAction() == null) {
            //The card has no action
        } else {
            //Get action tab image
            this.imgAction = card.getCard().getAction().getActionTabImage();
        }
    }

    /**
     * Draws the enlarged card, attack, health, dust and action for the card.
     *
     * @param gc
     *            Graphics context for which everything is drawn.
     * @param x
     *            X-coordinate for the top left corner of where the additional
     *            information pane is to be drawn.
     * @param y
     *            Y-coordinate for the top left corner of where the additional
     *            information pane is to be drawn.
     * @param w
     *            Width of the additional information pane (Just enlarged card/
     *            Not including action tab).
     * @param h
     *            Height of the additional information pane (Just enlarged card/
     *            Not including action tab).
     */

    /**
     * Returns the action image, if it exists.
     *
     * @return imgAction
     * Either Image object or null
     */
    public Image getActionImage() {
        return imgAction;
    }

    /**
     * Returns the dust image, if it exists.
     * Should exist and return the correct image
     *
     * @return imgDust
     * Image object
     */
    public Image getDustImage() {
        return imgDust;
    }

    // All x, y
    public boolean drawInfo(GraphicsContext gc, double x, double y, double w, double h) {
        try {
            // Setting the font and fill for the text.
            Font font = gc.getFont();
            // Original font from gc.
            Paint colour = gc.getFill();
            // Original fill from gc.

            // Increasing the font size.
            gc.setFont(new Font(gc.getFont().getName(), 20));
            gc.setFill(Color.WHITE);

            // Draw the enlarged card image
            gc.drawImage(imgCard, x, y, w, h);
            // Draw the sword icon representing Attack.
            gc.drawImage(imgAttack, x + 126, y + 5, 25, 30);
            // Draw the heart icon representing Health.
            gc.drawImage(imgHealth, x + 157, y + 6, 39, 33);

            // Draw the Attack number.
            gc.fillText(Integer.toString(cardAttack), x + 139, y + 41);
            // Draw the health number.
            gc.fillText(Integer.toString(cardHealth), x + 183, y + 49);

            // Reset font and fill
            gc.setFont(font);
            gc.setFill(colour);

            // Draw required duck dust for card.
            drawDust(gc, x + 5, y + 33);
        } catch (Exception e) {
            return false;
        }
        // Draw the action tab below the enlarged card.
        gc.drawImage(imgAction, x, y + h + 5, w, 120);
        return true;
    }

    /**
     * Draws the required duck dust as a vertical line of icons.
     *
     * @param gc Graphics context for which the icons are drawn.
     * @param x  X-coordinate for the top left corner of where the additional
     *           information pane is to be drawn.
     * @param y  Y-coordinate for the top left corner of where the additional
     *           information pane is to be drawn.
     */
    private void drawDust(GraphicsContext gc, double x, double y) {
        // To count how many duck dust left to draw
        int dustCounter = cardDust;

        // Loops to draw the required number of duck dust.
        double height = y;
        while (dustCounter > 0) {
            gc.drawImage(imgDust, x, height, 19, 19);
            height += 20;
            dustCounter -= 1;
        }
    }
}
