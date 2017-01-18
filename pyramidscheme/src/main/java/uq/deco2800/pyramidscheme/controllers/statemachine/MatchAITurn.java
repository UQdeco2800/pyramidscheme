package uq.deco2800.pyramidscheme.controllers.statemachine;

import uq.deco2800.pyramidscheme.animations.AIAnimations;
import uq.deco2800.pyramidscheme.animations.AnimationCallback;
import uq.deco2800.pyramidscheme.board.RecTile;
import uq.deco2800.pyramidscheme.cards.supercards.NullCard;
import uq.deco2800.pyramidscheme.match.Match;
import uq.deco2800.pyramidscheme.match.MatchCard;

import java.util.List;

/**
 * Created by Max on 11/09/2016.
 */
public class MatchAITurn extends StateNode implements AnimationCallback {

    private AIAnimations aiAnimations;

    private boolean turnOver = false;

    public MatchAITurn(Match match, StateCallback stateCallback) {
        super(match, stateCallback);
        logger.info("MatchAI Node activated");

        // Create animations class
        aiAnimations = new AIAnimations(gs, this);

        if (!gs.getBoard().getDeadTiles().isEmpty()) {
            aiAnimations.animateGraveyard(match);
        } else {
            grindCard();
        }
    }

    @Override
    public StateNode processClick(int x, int y) {
        // Don't allow clicks during AI turn
        return this;
    }

    @Override
    public StateNode processMoved(int x, int y) {
        // Displays a hovercard
        gs.hoverCard(x, y);

        // Stay on the current node
        return this;
    }

    private void grindCard() {
        MatchCard card = match.getOpponent().pickToGrind();
        if (card != null) {
            logger.debug("Grinding card");
            aiAnimations.grindCard(card);
        } else {
            generateAttack();
        }
    }

    private void generateAttack() {
        if (!match.getOpponent().canContinuePlaying()) {
            logger.debug("Not enough duck dust");
            deployAttack();
            return;
        }
        if (gs.getAIPyramid().isEmpty()) {
            refillPyramid();
            return;
        }
        logger.debug("Creating a AI card attack");
        // Get aiMove
        List<Object> aiMove = match.getOpponent().playCard();
        MatchCard card = (MatchCard) aiMove.get(0);
        RecTile tile = (RecTile) aiMove.get(1);
        match.getOpponent().duckDustPool.spend(card.getCost());

        // Animate move else call new attack state if no new state to be called
        if (!(card.getCard() instanceof NullCard) && tile != null) {
            // Animate the move
            logger.debug("Animating move");
            aiAnimations.animateAIMove(card, tile);
        } else {
            // Failed to create attack, deploy an attack
            deployAttack();
        }
    }

    private void deployAttack() {
        logger.debug("Deploying attack");
        // Set the tell ai that the turn is over
        match.getOpponent().aiTurnOver();
        turnOver = true;
        // Attack on the board
        gs.attack(match.getOpponent(), match.getUser(), stateCallback);
        // Check if there is any tiles to animate
        if (!gs.getBoard().getDeadTiles().isEmpty()) {
            aiAnimations.animateGraveyard(match);
        }
        refillPyramid();
    }

    private void refillPyramid() {
        if (gs.getAIPyramid().isEmpty() && !gs.getAIMatchDeck().isEmpty()) {
            logger.debug("Refilling Pyramid");
            aiAnimations.animatePyramidRefill();
        } else {
            logger.debug("Going to idle");
            stateCallback.goToIdle();
        }
    }

    @Override
    public void aiPlayingDone() {
        if (turnOver) {
            stateCallback.goToIdle();
        } else {
            generateAttack();
        }
    }

    @Override
    public void userRefillDone() {
        // do nothing
    }

    @Override
    public void grindDone() {
        if (turnOver) {
            stateCallback.goToIdle();
        } else {
            generateAttack();
        }
    }

    @Override
    public void flipDone() {
        // do nothing because done
    }

    @Override
    public void aiRefillDone() {
        if (turnOver) {
            stateCallback.goToIdle();
        } else {
            deployAttack();
        }
    }

    @Override
    public void graveyardDone() {
        if (turnOver) {
            stateCallback.goToIdle();
        } else {
            grindCard();
        }
    }
}
