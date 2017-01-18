package uq.deco2800.pyramidscheme.cards.supercards;

/**
 * Created by nick on 10/09/2016.
 */
public enum CardHiddenState {
    VISIBLE(""),
    USERHIDDEN("/cardImages/hidden_user.png"),
    AIHIDDEN("/cardImages/hidden_ai.png"),
    DECKHIDDEN("/cardImages/hidden_deck.png"),
    PLACED("");

    private final String text;

    CardHiddenState(final String string) {
        this.text = string;
    }

    @Override
    public String toString() {
        return text;
    }

}
