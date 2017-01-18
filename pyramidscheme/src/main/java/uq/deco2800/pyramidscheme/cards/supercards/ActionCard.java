package uq.deco2800.pyramidscheme.cards.supercards;

import uq.deco2800.pyramidscheme.actions.Action;

/**
 * Represents action cards. Extends Card. All specific action cards
 * should extend this class.
 *
 * @author Millie
 */

public class ActionCard extends Card {

    private Action cardAction;

    public ActionCard(String name, String img, String unbox, Action action,
                      int rank) {
        super(name, img, unbox, rank);
        this.cardAction = action;
    }

    public Action getAction() {
        return cardAction;
    }

    public void setAction(Action a) {
        cardAction = a;
    }

    /**
     * Return some String representation of this card
     */
    public String toString() {
        return "AC " +
                this.getName() +
                ": " +
                cardAction.getName();
    }

    @Override
    public String getType() {
        return "AC";
    }
}
