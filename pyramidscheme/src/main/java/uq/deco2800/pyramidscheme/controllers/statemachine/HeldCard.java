package uq.deco2800.pyramidscheme.controllers.statemachine;

import javafx.geometry.Point2D;
import org.slf4j.LoggerFactory;
import uq.deco2800.pyramidscheme.animations.emitter.Emitter;
import uq.deco2800.pyramidscheme.cards.supercards.Card;
import uq.deco2800.pyramidscheme.cards.supercards.MinionCard;
import uq.deco2800.pyramidscheme.game.GameManager;
import uq.deco2800.pyramidscheme.match.Match;
import uq.deco2800.pyramidscheme.match.MatchCard;

/**
 * A StateNode representing the state of "the user is currently dragging
 * a card around the screen"
 */
public class HeldCard extends StateNode {

    public HeldCard(Match match, StateCallback stateCallback) {
        super(match, stateCallback);
        logger.info("Held Node activated");
    }

    /**
     * Given an x and y screenspace coordinate, returns an Idle node.
     * <p>
     * In the event of that the x and y coordinates lie on a valid target for
     * the card, the appropriate action is taken, and a new Idle node is
     * returned.
     * <p>
     * Otherwise, the card is dropped, and a new Idle node is returned.
     *
     * @param x The x screenspace coordinate
     * @param y The y screenspace coordinate
     */
    public StateNode processClick(int x, int y) {
        this.getClicked(x, y);

        if (newClickOnTile.get().isPresent()
                // ^ and we click a tile
                && newClickOnTile.get().get().getOwner().equals(match.getUser())
                // ^ and we own the tile
                //&& !newClickOnTile.get().get().getContents().isPresent()
                // ^ and it's empty
                && gs.getUserDuckDust().isPlayable(gs.getHeld().get().getCost())) {
            // ^ card can be played

            if (newClickOnTile.get().get().getContents().isPresent()) {
                if (gs.getHeld().get().getCard() instanceof MinionCard
                        && newClickOnTile.get().get().getContents().get().getCard() instanceof MinionCard) {
                    MinionCard mcHeld = (MinionCard) gs.getHeld().get().getCard();
                    MinionCard mcTile = (MinionCard) newClickOnTile.get().get().getContents().get().getCard();
                    MinionCard mcNew = mcHeld.getParent(mcTile);

                    if (mcNew == null) {
                        if (gs.getHeld().isPresent()) {
                            gs.getUserPyramid().returnYankedCard(gs.getHeld().get());
                            gs.dropHeld();
                        }
                        return new Idle(match, stateCallback);
                    }

                    newClickOnTile.get().get().removeContents();
                    try {
                        newClickOnTile.get().get().setContents(new MatchCard(mcNew, 0, 0));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                    LoggerFactory.getLogger(HeldCard.class).info("Card Stacking: " + mcHeld.getName() + " + " +
                            mcTile.getName() + " -> " + mcNew.getName()
                    );
                    return finishDrop();
                } else {
                    if (gs.getHeld().isPresent()) {
                        gs.getUserPyramid().returnYankedCard(gs.getHeld().get());
                        gs.dropHeld();
                    }
                    return new Idle(match, stateCallback);
                }
            }

            // transfer the card
            newClickOnTile.get().get().setContents(gs.getHeld().get());

            // Spend the amount of duck dust the card costs
            match.getUser().duckDustPool.spend(gs.getHeld().get().getCost());

            // Transmit played card
            if (GameManager.getInstance().getMultiplayerClient().isPresent()) {
                GameManager.getInstance().getMultiplayerClient().get().sendPlayerTurn(
                        gs.getHeld().get().getUid(),
                        x,
                        y);
            }

            // Transfer to drop card
            return finishDrop();
        } else if (newClickOnDustPool
                // ^ click on the dust pool
                && gs.getUserDuckDust().canGrind()) {
            // ^ card can be ground

            // Grind the match card
            gs.getUserDuckDust().grind();

            Emitter emitter = Emitter.DUCK_DUST;

            Point2D grindCoords = gs.getGrinder().getOrigin();

            // Emit on that point
            gs.addParticles(emitter.emit(grindCoords.getX() + 115, grindCoords.getY() + 75));

            // Transmit ground card
            if (GameManager.getInstance().getMultiplayerClient().isPresent()) {
                GameManager.getInstance().getMultiplayerClient().get().sendGrindCard(gs.getHeld().get().getUid());
            }

            // Finish grind
            return finishDrop();
        } else {
            if (gs.getHeld().isPresent()) {
                gs.getUserPyramid().returnYankedCard(gs.getHeld().get());
                gs.dropHeld();
            }
        }

        // Transition to a new state
        return new Idle(match, stateCallback);
    }

    /**
     * In the event that a valid target for a card to be dropped on was
     * selected, perform the necessary housekeeping in order to successfully
     * mediate the transfer of the card between objects.
     *
     * @return The next statenode to transition to, which will either be an
     * Animating node in the event that a new stage of pyramid is reached,
     * or the pyramid needs to be refilled, or an Idle node in the event
     * that neither of those conditions are met
     */
    private StateNode finishDrop() {
        // Remove getHeld card from gameState
        gs.dropHeld();
        // tell the pyramid it's not getting that card back
        gs.getUserPyramid().confirmRemoval();

        // Add to statistics tracking
        GameManager.getStatisticsTracking().addToMinionsPlayed(1);

        // Create new animation state
        AnimatingNode state = new AnimatingNode(match, stateCallback);

        //Check if the pyramid needs a refill
        if (gs.getUserPyramid().isEmpty() && !gs.getUserMatchDeck().isEmpty()) {
            state.refillPyramid();
        } else if (!gs.getUserPyramid().getFlipCards().isEmpty()) {
            state.flipCard();
        } else {
            int count = 0;
            for (MatchCard card : gs.getUserPyramid()) {
                if (gs.getUserDuckDust().isPlayable(card.getCost())) {
                    count++;
                }
            }
            if (count == 0 && gs.getUserDuckDust().getGround()) {
                stateCallback.turnOver();
            }
            return new Idle(match, stateCallback);
        }
        return state;
    }

    /**
     * Given an x and y screenspace coordinate, returns a HeldCard node.
     * <p>
     * The currently held card is moved to match the provided x and y coordinates
     *
     * @param x The x screenspace coordinate
     * @param y The y screenspace coordinate
     */
    public StateNode processMoved(int x, int y) {
        MatchCard heldCard = gs.getHeld().get();

        // Attempt to move the card so that the mouse is at the center of
        // the card, as the card's position is from the top left hand corner
        int heldWidth = Card.getCardWidth();
        int heldHeight = Card.getCardHeight();

        heldCard.setX(x - (heldWidth / 2));
        heldCard.setY(y - (heldHeight / 2));

        // Keep holding the card
        // Stay on the current node
        return this;
    }
}
