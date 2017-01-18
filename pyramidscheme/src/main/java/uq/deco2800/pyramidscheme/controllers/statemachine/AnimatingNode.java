package uq.deco2800.pyramidscheme.controllers.statemachine;

import uq.deco2800.pyramidscheme.animations.AnimationCallback;
import uq.deco2800.pyramidscheme.animations.UserAnimations;
import uq.deco2800.pyramidscheme.game.GameManager;
import uq.deco2800.pyramidscheme.match.Match;
import uq.deco2800.pyramidscheme.match.MatchCard;

/**
 * Created by nick on 17/10/16.
 */
public class AnimatingNode extends StateNode implements AnimationCallback {

    UserAnimations userAnimations = new UserAnimations(gs, this);

    public AnimatingNode(Match match, StateCallback stateCallback) {
        super(match, stateCallback);
        logger.info("Animating Node activated");
    }

    public void refillPyramid() {
        // Transmit cards for refill card
        if (userAnimations.isAnimating(2) && gs.getUserPyramid().isEmpty()) {
            if (GameManager.getInstance().getMultiplayerClient().isPresent()) {
                GameManager.getInstance().getMultiplayerClient().get().sendPyramidRefill(
                        gs.getUserPyramid().getPyramidType().getSize());
            }
            userAnimations.animatePyramidRefill();
        }
    }

    void flipCard() {
        userAnimations.flipCards(gs.getUserPyramid().getFlipCards());
    }

    private void turnOver() {
        int count = 0;
        for (MatchCard card : gs.getUserPyramid()) {
            if (gs.getUserDuckDust().isPlayable(card.getCost())) {
                count++;
            }
        }
        if (count == 0 && gs.getUserDuckDust().getGround()) {
            stateCallback.turnOver();
        } else {
            stateCallback.goToIdle();
        }
    }

    @Override
    public void aiPlayingDone() {
        // do nothing because done
    }

    @Override
    public void userRefillDone() {
        turnOver();

    }

    @Override
    public void graveyardDone() {
        // do nothing because done
    }

    @Override
    public void grindDone() {
        // do nothing because done
    }

    @Override
    public void flipDone() {
        turnOver();
    }

    @Override
    public void aiRefillDone() {
        // do nothing because done
    }

    @Override
    public StateNode processClick(int x, int y) {
        return this;
    }

    @Override
    public StateNode processMoved(int x, int y) {
        return this;
    }
}
