package uq.deco2800.pyramidscheme.animations;

import javafx.animation.*;
import javafx.scene.image.ImageView;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uq.deco2800.pyramidscheme.board.RecTile;
import uq.deco2800.pyramidscheme.cards.supercards.Card;
import uq.deco2800.pyramidscheme.cards.supercards.CardHiddenState;
import uq.deco2800.pyramidscheme.game.GameState;
import uq.deco2800.pyramidscheme.match.Match;
import uq.deco2800.pyramidscheme.match.MatchCard;
import uq.deco2800.pyramidscheme.match.MatchDeck;
import uq.deco2800.pyramidscheme.pyramid.Pyramid;
import uq.deco2800.pyramidscheme.pyramid.PyramidCardLocation;

import java.util.ArrayList;
import java.util.List;


/**
 * This animations class is designed to be extended for a UserAnimations
 * class and a AIAnimations class. It contains methods for adding animations
 * to queues such that they play in sequence
 */
public class Animations {
    //Queue types
    static final int AI_PLAYING = 0;
    static final int AI_REFILLING = 1;
    static final int USER_REFILLING = 2;
    static final int NON_BLOCKING = 3;
    static final int GRAVEYARD = 4;
    static final int AI_GRINDER = 5;

    //Flip types
    static final int USER_PYRAMID_LOAD = 0;
    static final int USER_PYRAMID_FLIP = 1;
    static final int AI_BOARD_FLIP = 2;
    static final int GRAVEYARD_FLIP = 3;

    // Contains all the animations
    ArrayList<ArrayList<Animation>> animationQueues = new ArrayList<>();

    // GameState required for moving objects
    GameState gs;

    // The callback that is passed in. Lets the callback know when certain queues have finished
    AnimationCallback callback;

    Logger logger = LoggerFactory.getLogger(Animations.class);

    // Booleans for managing whether a queue is playing or not
    private Boolean aiPlayingAnimating = false;
    private Boolean aiRefillAnimating = false;
    private Boolean userRefillAnimating = false;
    private Boolean graveyardAnimating = false;
    private Boolean grinderAnimating = false;


    /**
     * Generates an Animations class with queues
     *
     * @param gs       the match's gamestate
     * @param callback a callback interface
     */
    public Animations(GameState gs, AnimationCallback callback) {
        this.gs = gs;
        this.callback = callback;
        // Create 6 queues
        for (int i = 0; i < 6; i++) {
            animationQueues.add(new ArrayList<>());
        }
    }

    /**
     * Adds the animation to the queue and then runs the queued animations.
     *
     * @param animation to be added to the queue
     * @param queueType contains animations to be executed
     */
    void addToAnimationQueue(Animation animation, Integer queueType) {
        animationQueues.get(queueType).add(animation);
        runAnimationQueues();
    }

    /**
     * Run the animations currently in the queue.
     */
    void runAnimationQueues() {
        // Check if ai move animations are playing and no graveyard animations
        if (!aiPlayingAnimating && !graveyardAnimating) {
            //Check if there are ai moves to be animated
            if (!isAnimating(AI_PLAYING)) {
                animationQueues.get(AI_PLAYING).get(0).play();
                aiPlayingAnimating = true;
                //Check if there are ai refill animations and it isn't animating
            } else if (!isAnimating(AI_REFILLING) && !aiRefillAnimating) {
                animationQueues.get(AI_REFILLING).get(0).play();
                aiRefillAnimating = true;
            }
        }
        // Sequence user animations
        if (!userRefillAnimating && !isAnimating(USER_REFILLING) && !graveyardAnimating) {
            animationQueues.get(USER_REFILLING).get(0).play();
            userRefillAnimating = true;
        }
        // Sequence Graveyard
        if (!graveyardAnimating && !isAnimating(GRAVEYARD)) {
            animationQueues.get(GRAVEYARD).get(0).play();
            graveyardAnimating = true;
        }
        // Animate grinder
        if (!grinderAnimating && !isAnimating(AI_GRINDER)) {
            animationQueues.get(AI_GRINDER).get(0).play();
            grinderAnimating = true;
        }
        // Play all items in non blocking queue
        for (int i = 0; i < animationQueues.get(NON_BLOCKING).size(); i++) {
            animationQueues.get(NON_BLOCKING).get(i).play();
        }
    }

    /**
     * Return a boolean result if the queue is animating.
     *
     * @param queue of animations
     * @return boolean
     */
    public boolean isAnimating(Integer queue) {
        return animationQueues.get(queue).isEmpty();
    }

    /**
     * Checks if the queue is finished animating.
     *
     * @param queue of animations
     */
    private void checkFinished(Integer queue) {
        //Check if queue is empty
        if (animationQueues.get(queue).isEmpty()) {
            // Run end of queue logic
            switch (queue) {
                case AI_PLAYING:
                    logger.debug("AI move completed");
                    callback.aiPlayingDone();
                    break;
                case USER_REFILLING:
                    logger.debug("Queuing cards to flip");
                    pyramidRefillFlipCards();
                    logger.debug("User pyramid refill completed");
                    callback.userRefillDone();
                    break;
                case AI_REFILLING:
                    gs.getAIPyramid().setLoadingCards(false);
                    callback.aiRefillDone();
                    break;
                case GRAVEYARD:
                    callback.graveyardDone();
                    break;
                case AI_GRINDER:
                    callback.grindDone();
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Remove completed animations from the queue and continue executing the rest
     * of the queue.
     *
     * @param queue of animations
     */
    void animationFinished(Integer queue) {
        //Remove animation from relevant queue
        animationQueues.get(queue).remove(0);
        switch (queue) {
            case AI_PLAYING:
                aiPlayingAnimating = false;
                break;
            case AI_REFILLING:
                aiRefillAnimating = false;
                break;
            case USER_REFILLING:
                userRefillAnimating = false;
                break;
            case GRAVEYARD:
                graveyardAnimating = false;
                break;
            case AI_GRINDER:
                grinderAnimating = false;
                break;
            default:
                break;
        }
        checkFinished(queue);
        // Run next animation
        runAnimationQueues();
    }

    /**
     * Creates the animation for a pyramid refill and adds it to the animation queue.
     *
     * @param count  is the pyramid size
     * @param isUser determine if the pyramid to be filled is for the User or AI
     */
    void animatePyramidRefill(Integer count, boolean isUser) {
        Integer queue = isUser ? USER_REFILLING : AI_REFILLING;

        Pyramid pyramid = isUser ? gs.getUserPyramid() : gs.getAIPyramid();

        if (count > pyramid.getPyramidType().getSize()) {
            throw new IllegalArgumentException("Too many pyramid cards supplied for refill");
        }

        //Update card usage count
        gs.updateCardCount();

        MatchCard card = isUser ? gs.getUserMatchDeck().popCards(1).get(0) : gs.getAIMatchDeck().popCards(1).get(0);

        CardHiddenState hiddenState = isUser ? CardHiddenState.USERHIDDEN : CardHiddenState.AIHIDDEN;
        gs.addAnimatingCard(card, hiddenState);

        if (card != null) {
            //Animate card across the screen
            List<PyramidCardLocation> coords = pyramid.calculatePyramidCoords();
            if (coords.size() - count >= 0) {
                Animation animation = animateCardMovement(card,
                        coords.get(coords.size() - (coords.size() - count + 1)).getX()
                                + (int) pyramid.getOrigin().getX(),
                        coords.get(coords.size() - (coords.size() - count + 1)).getY()
                                + (int) pyramid.getOrigin().getY());
                animation.setOnFinished(event -> {
                    // Remove card from the gamestate animation pile
                    gs.removeAnimatingCard(card, hiddenState);
                    //Load the card into the pyramid
                    ArrayList<MatchCard> loadCard = new ArrayList<>(1);
                    loadCard.add(card);
                    pyramid.loadCards(loadCard);
                    pyramid.setLoadingCards(true);
                    pyramid.arrangePyramid();
                    //Tell the animation queue that the animation has finished
                    animationFinished(queue);
                });
                addToAnimationQueue(animation, queue);
                if (count > 1) {
                    animatePyramidRefill(count - 1, isUser);
                }
            }
        }
    }

    /**
     * Creates animation for a card to be moved to a new location in game
     *
     * @param card to be moved to a new location
     * @param toX  new x location card needs to move to
     * @param toY  new y location card needs to move to
     * @return animation
     */
    Timeline animateCardMovement(MatchCard card, Integer toX, Integer toY) {
        int animationFrames = 20;
        int xIncrement = (toX - card.getX()) / animationFrames;
        int yIncrement = (toY - card.getY()) / animationFrames;
        Timeline animation = new Timeline();

        // Animate animationFrames amount of frames
        for (int i = 0; i < animationFrames; i++) {
            KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.017 * i), event -> {
                card.setX(card.getX() + xIncrement);
                card.setY(card.getY() + yIncrement);
            });
            animation.getKeyFrames().add(animation.getKeyFrames().size(), keyFrame);
        }
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.017 * (animationFrames) + 0.0001), event -> {
            card.setX(toX);
            card.setY(toY);
        });
        animation.getKeyFrames().add(animation.getKeyFrames().size(), keyFrame);
        return animation;
    }

    /**
     * Creates the animation that removes the deadCard from the board and sends it to the graveyard. Adds every card
     * to the graveyard queue for animation
     *
     * @param match contains data and variables required for the game
     */
    public void animateGraveyard(Match match) {
        // Check each tile on the board
        for (RecTile tile : gs.getBoard().getDeadTiles()) {
            MatchDeck destination = tile.getOwner().equals(match.getUser()) ?
                    gs.getUserGraveyard() : gs.getAIGraveyard();

            //Remove card from board
            MatchCard deadCard = tile.removeContents().get();
            if (deadCard.getCard().getAction() != null) {
                deadCard.getCard().getAction().killed(tile, gs.getBoard());
            }

            // Save it's initial position
            int initialX = deadCard.getX();
            int initialY = deadCard.getY();

            //Add card to animating pile in gamestate
            gs.addAnimatingCard(deadCard, CardHiddenState.VISIBLE);

            // Card has its coordinates aligned to the top of the deck,
            // *but it hasn't been drawn yet*
            // Find out where it needs to go
            destination.pushCard(deadCard);
            int endX = deadCard.getX();
            int endY = deadCard.getY();

            // Get it back so we can animate it
            destination.popCards(1);

            // Reset its position
            deadCard.setX(initialX);
            deadCard.setY(initialY);

            // Create attack animation
            Timeline attack = attackAnimation(deadCard.getX(), deadCard.getY());

            // Create card movement to graveyard
            Timeline move = animateCardMovement(deadCard, endX, endY);

            // Chain the animations together
            SequentialTransition animation = new SequentialTransition(attack, move);

            // Set the finished event for the animation
            animation.setOnFinished(event -> {
                //Remove card from animating pile in GameState
                gs.removeAnimatingCard(deadCard, CardHiddenState.VISIBLE);
                //Put the card in the graveyard
                destination.pushCard(deadCard);
                //Update graveyard count
                gs.updateCardCount();
                //Tell the animation queue this animation is done
                animationFinished(GRAVEYARD);
            });

            // Add it to the animation queue
            addToAnimationQueue(animation, GRAVEYARD);
        }
    }

    /**
     * Attack animation between two cards on the game board.
     *
     * @param x
     * @param y
     * @return animation
     */
    private Timeline attackAnimation(int x, int y) {
        Timeline animation = new Timeline();

        // Create keyframes for animating an attack card
        KeyFrame addAttackImage = new KeyFrame(Duration.ZERO, event -> gs.addAttackImage(x, y));
        KeyFrame removeAttackImage = new KeyFrame(Duration.millis(500), event -> gs.removeAttackImage());

        // Add keyframes to animation
        animation.getKeyFrames().add(addAttackImage);
        animation.getKeyFrames().add(removeAttackImage);

        return animation;
    }

    /**
     * Triggers flipCard after the user's pyramid has been refilled on top level cards
     */
    private void pyramidRefillFlipCards() {
        for (MatchCard flipCard : gs.getUserPyramid()) {
            flipCard(flipCard, CardHiddenState.USERHIDDEN, CardHiddenState.VISIBLE, USER_PYRAMID_LOAD);
        }
    }

    /**
     * Flips the card from it's fromState to be become the toState.  The type of flip depends
     * on the supplied flipType.
     *
     * @param card      to be flipped
     * @param fromState the state of the card before flip
     * @param toState   the state of the card after flip
     * @param flipType  type of flip depending on where the card is
     */
    void flipCard(MatchCard card, CardHiddenState fromState, CardHiddenState toState, final Integer flipType) {
        ImageView firstCard;
        ImageView lastCard;

        // Rotating from visible
        if (fromState.equals(CardHiddenState.VISIBLE)) {
            firstCard = new ImageView(card.getCard().getImage());
            lastCard = new ImageView(toState.toString());
        } else {
            firstCard = new ImageView(fromState.toString());
            lastCard = new ImageView(card.getCard().getImage());
        }
        // Set dimensions of cards
        firstCard.setFitWidth(Card.getCardWidth());
        firstCard.setFitHeight(Card.getCardHeight());
        lastCard.setFitWidth(Card.getCardWidth());
        lastCard.setFitHeight(Card.getCardHeight());

        // Set location of cards
        firstCard.setLayoutX(card.getX());
        firstCard.setLayoutY(card.getY());
        lastCard.setLayoutX(card.getX());
        lastCard.setLayoutY(card.getY());

        // Generate a sequential transition
        RotateTransition rotator1 = createRotator(firstCard, true);
        RotateTransition rotator2 = createRotator(lastCard, false);
        SequentialTransition sequentialTransition = new SequentialTransition(rotator1, rotator2);

        // Set second card invisible
        lastCard.setVisible(false);

        rotator1.setOnFinished(e -> {
            if (gs.getAnimationPane().isPresent()) {
                gs.getAnimationPane().get().getChildren().remove(firstCard);
            }
            lastCard.setVisible(true);
        });
        // Let the other sequence know when it's done
        rotator2.setOnFinished(e -> {
            // Check type of flip
            switch (flipType) {
                case USER_PYRAMID_FLIP:
                    gs.getUserPyramid().setPlayableCards();
                    break;
                case USER_PYRAMID_LOAD:
                    gs.getUserPyramid().setLoadingCards(false);
                    break;
                case GRAVEYARD_FLIP:
                    break;
                case AI_BOARD_FLIP:
                    // Place card on board
                    if (this instanceof AIAnimations) {
                        ((AIAnimations) this).tile.setContents(card);
                    }
                    // Finished animation
                    animationFinished(AI_PLAYING);
                    break;
                default:
                    break;
            }
            logger.debug("Finished flip card");
            if (gs.getAnimationPane().isPresent()) {
                gs.getAnimationPane().get().getChildren().remove(lastCard);
            }
            callback.flipDone();
        });

        sequentialTransition.play();

        // Add the rotation and play
        if (gs.getAnimationPane().isPresent()) {
            gs.getAnimationPane().get().getChildren().addAll(firstCard, lastCard);
        }
    }

    /**
     * Rotates the card to halfway(from 0 to 90 degrees) unless firstRotator is false in which
     * the card is rotated from halfway to the end(from 270 to 359).
     *
     * @param card         to be rotated
     * @param firstRotator determines if the card has already been flipped to halfway.
     * @return rotator
     */
    private RotateTransition createRotator(ImageView card, boolean firstRotator) {
        RotateTransition rotator = new RotateTransition(Duration.millis(200), card);
        rotator.setAxis(Rotate.Y_AXIS);
        if (firstRotator) {
            rotator.setFromAngle(0);
            rotator.setToAngle(90);
            rotator.setInterpolator(Interpolator.EASE_IN);
        } else {
            rotator.setFromAngle(270);
            rotator.setToAngle(359);
            rotator.setInterpolator(Interpolator.EASE_OUT);
        }
        rotator.setCycleCount(1);
        return rotator;
    }
}
