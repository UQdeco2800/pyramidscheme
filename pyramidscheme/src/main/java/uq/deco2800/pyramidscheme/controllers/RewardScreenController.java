package uq.deco2800.pyramidscheme.controllers;

import javafx.animation.*;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import uq.deco2800.pyramidscheme.cards.supercards.Card;
import uq.deco2800.pyramidscheme.cards.supercards.MinionCard;
import uq.deco2800.pyramidscheme.deck.CardList;
import uq.deco2800.pyramidscheme.deck.ViewCard;
import uq.deco2800.pyramidscheme.game.GameManager;
import uq.deco2800.pyramidscheme.player.User;

import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;
import java.util.ResourceBundle;

public class RewardScreenController implements Initializable {

    // FX elements this class uses
    @FXML
    private StackPane displayPane;
    @FXML
    private StackPane packListPane;
    @FXML
    private Button openButton;
    @FXML
    private Button buyButton;
    @FXML
    private Button backButton;
    @FXML
    private Button cardViewButton;
    @FXML
    private Label numPacks;
    @FXML
    private Label currencyLabel;
    @FXML
    private ImageView goldenEggs;


    private User user;
    private int packs;
    private Image goldenEggsImage;
    private Image treasureRoom;
    private Image treasureRoomOpen;
    private Image papyrus;
    private ArrayList<DoubleProperty> packHighlightPositions;
    private Image cardPackImage;
    private Timeline packHighlightTimeline;
    private AnimationTimer packHighlightTimer;
    private Timeline openPackTimeline;
    private AnimationTimer openPackTimer;
    private Timeline hoverFeedbackTimeline;
    private AnimationTimer hoverFeedbackTimer;
    private DoubleProperty hoverFeedbackSize;
    private Timeline glowTimeline;
    private AnimationTimer glowTimer;

    private ArrayList<ViewCard> rewards;
    private DoubleProperty rewardScale;
    private Random rand = new Random();
    private Optional<ViewCard> hoverCard;
    private boolean hoverCheck = false;

    private static final int PACK_COST = 500;
    private static final int DISPLAY_CANVAS_WIDTH = 700;
    private static final int DISPLAY_CANVAS_HEIGHT = 600;
    private static final int LIST_CANVAS_WIDTH = 200;
    private static final int LIST_CANVAS_HEIGHT = 600;
    private static final Color SAND = Color.rgb(240, 195, 103);
    private static final int[] REWARD_X_POSITIONS = {125, 275, 425, 575};
    private static final int[] REWARD_Y_POSITIONS = {200, 200, 200, 200};
    private static final String YOU_HAVE = "You Have ";


    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

        // get user
        user = GameManager.getInstance().getUser();
        packs = user.getCardPacks();
        if (packs == 0) {
            openButton.setDisable(true);
        }

        rewards = new ArrayList<ViewCard>();

        // Set up display canvas
        Canvas displayCanvas = new Canvas(DISPLAY_CANVAS_WIDTH,
                DISPLAY_CANVAS_HEIGHT);
        GraphicsContext displayGC = displayCanvas.getGraphicsContext2D();
        displayPane.getChildren().add(0, displayCanvas);
        treasureRoom = new Image(getClass().
                getResourceAsStream("/gameImages/treasureRoom.png"));
        treasureRoomOpen = new Image(getClass().
                getResourceAsStream("/gameImages/treasureRoomOpen.png"));
        displayGC.drawImage(treasureRoom, 0, 0, 700, 600);
        displayGC.setFont(new Font(20));
        displayGC.setFill(Color.WHITE);
        displayGC.setTextAlign(TextAlignment.CENTER);
        displayGC.fillText("Open a pack!", DISPLAY_CANVAS_WIDTH / 2, 80);
        // Set up animations

        prepareOpenAnimations(displayGC);
        prepareHoverAnimation(displayGC);
        prepareChestAnimation(displayGC);

        displayPane.setOnMouseMoved(this::handleHover);

        // Set up Pack List Area
        goldenEggsImage = new Image(getClass().
                getResourceAsStream("/gameImages/GoldenEggs.png"));
        Canvas listCanvas = new Canvas(LIST_CANVAS_WIDTH, LIST_CANVAS_HEIGHT);
        GraphicsContext listGC = listCanvas.getGraphicsContext2D();
        packListPane.getChildren().add(0, listCanvas);
        setPackLabel();
        currencyLabel.setText(YOU_HAVE + user.getCurrency());
        goldenEggs.setImage(goldenEggsImage);

        openButton.setOnAction(e -> openPack());
        buyButton.setOnAction(e -> buyPack());
        if (user.getCurrency() < PACK_COST) {
            buyButton.setDisable(true);
        }
        backButton.setOnAction(this::handleBackButton);
        cardViewButton.setOnAction(e -> GameManager.changeScene("DeckViewScreen.fxml"));

        cardPackImage = new Image(getClass().
                getResourceAsStream("/cardImages/hidden_deck.png"));
        papyrus = new Image(getClass().
                getResourceAsStream("/gameImages/papyrus.png"));
        listGC.drawImage(papyrus, 0, 0, 200, 600);
        listGC.drawImage(cardPackImage, 25, 25, 150, 210);
        //Set up pack highlight

        preparePackAnimations(listGC);
        packHighlightTimer.start();
        packHighlightTimeline.play();
    }

    /**
     * Event handler for mouse movement. Finds the card currently
     * under the cursor and plays feedback animation.
     *
     * @param event mouse move event
     */
    private void handleHover(MouseEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        hoverCard = Optional.empty();
        for (ViewCard card : rewards) {
            if (x > card.getX() && x < card.getX() + card.getWidth() &&
                    y > card.getY() && y < card.getY() + card.getHeight()) {
                hoverCard = Optional.of(card);
            }
        }
        if (hoverCard.isPresent() && !hoverCheck) {
            //start animation only if not already going
            hoverFeedbackTimer.start();
            hoverFeedbackTimeline.play();
            hoverCheck = true;

        } else if (!hoverCard.isPresent() && hoverCheck) {
            // stop animation if going.
            hoverFeedbackTimeline.stop();
            hoverFeedbackTimer.stop();
            hoverFeedbackSize.set(2);
            hoverCheck = false;
        }
    }

    /**
     * Prepares the hover animation, ready to play when cursor hovers
     * over reward card.
     *
     * @param gc Graphics Context for canvas.
     */
    private void prepareHoverAnimation(GraphicsContext gc) {
        hoverFeedbackSize = new SimpleDoubleProperty();
        hoverFeedbackSize.set(2);
        hoverFeedbackTimeline = new Timeline();
        hoverFeedbackTimeline.setCycleCount(Timeline.INDEFINITE);

        KeyValue sizeTarget = new KeyValue(hoverFeedbackSize, 10);
        KeyFrame feedbackFrame = new KeyFrame(Duration.millis(1500), sizeTarget);
        hoverFeedbackTimeline.getKeyFrames().add(feedbackFrame);
        hoverFeedbackTimeline.setAutoReverse(true);

        hoverFeedbackTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                gc.setFill(Color.YELLOW);
                gc.setGlobalAlpha(0.5);
                gc.setEffect(new BoxBlur(5, 5, 1));
                gc.fillRoundRect(hoverCard.get().getX() - hoverFeedbackSize.get(),
                        hoverCard.get().getY() - hoverFeedbackSize.get(),
                        hoverCard.get().getWidth() + hoverFeedbackSize.get() * 2,
                        hoverCard.get().getHeight() + hoverFeedbackSize.get() * 2, 10, 10);
                gc.setGlobalAlpha(1.0);
                gc.setEffect(null);
                hoverCard.get().drawCard(gc);
            }
        };
    }

    /**
     * Sets the label that displays number of card packs the user has.
     */
    private void setPackLabel() {
        String packLabelText = (packs != 1) ? YOU_HAVE + packs + " Packs" : YOU_HAVE + packs + " Pack";
        numPacks.setText(packLabelText);
    }

    /**
     * Prepares the card pack animations (highlight and particle effects).
     *
     * @param gc Graphics Context for canvas.
     */
    private void preparePackAnimations(GraphicsContext gc) {
        packHighlightPositions = new ArrayList<DoubleProperty>();
        for (int i = 0; i < 4; i++) {
            packHighlightPositions.add(new SimpleDoubleProperty());
        }
        packHighlightPositions.get(0).set(20);
        packHighlightPositions.get(1).set(20);
        packHighlightPositions.get(2).set(175);
        packHighlightPositions.get(3).set(235);

        packHighlightTimeline = new Timeline();
        packHighlightTimeline.setCycleCount(Timeline.INDEFINITE);

        KeyValue particle1Val = new KeyValue(packHighlightPositions.get(0),
                175, Interpolator.LINEAR);
        KeyFrame particle1Frame = new KeyFrame(Duration.millis(500), particle1Val);
        KeyValue particle2Val = new KeyValue(packHighlightPositions.get(1),
                235, Interpolator.LINEAR);
        KeyFrame particle2Frame = new KeyFrame(Duration.millis(500), particle2Val);
        KeyValue particle3Val = new KeyValue(packHighlightPositions.get(2),
                20, Interpolator.LINEAR);
        KeyFrame particle3Frame = new KeyFrame(Duration.millis(500), particle3Val);
        KeyValue particle4Val = new KeyValue(packHighlightPositions.get(3),
                20, Interpolator.LINEAR);
        KeyFrame particle4Frame = new KeyFrame(Duration.millis(500), particle4Val);
        packHighlightTimeline.getKeyFrames().add(particle1Frame);
        packHighlightTimeline.getKeyFrames().add(particle2Frame);
        packHighlightTimeline.getKeyFrames().add(particle3Frame);
        packHighlightTimeline.getKeyFrames().add(particle4Frame);

        packHighlightTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                gc.setFill(SAND);
                gc.setGlobalAlpha(0.075);
                gc.drawImage(papyrus, 0, 0, 200, 600);

                gc.setGlobalAlpha(1.0);
                if (packs > 0) {
                    packParticles(rand, gc, packHighlightPositions);
                }
            }
        };
    }

    /**
     * Draws sparkle effect around the edges of card pack.
     *
     * @param rand    Random generator.
     * @param gc      Graphics Context for canvas.
     * @param corners Relative coordinates for corners of card pack.
     */
    private void packParticles(Random rand, GraphicsContext gc,
                               ArrayList<DoubleProperty> corners) {
        double r = rand.nextDouble();
        if (r < 0.25) {
            gc.setFill(Color.WHITE);
        } else if (r < 0.5) {
            gc.setFill(Color.YELLOW);
        } else if (r < 0.75) {
            gc.setFill(Color.LIGHTPINK);
        } else {
            gc.setFill(Color.GOLD);
        }
        if (rand.nextDouble() < 0.5) {
            gc.fillOval(15 + rand.nextDouble() * 170, 20 - rand.nextDouble() * 10, 3, 3);
            gc.fillOval(175 + rand.nextDouble() * 10, 20 + rand.nextDouble() * 230, 3, 3);
        } else {
            gc.fillOval(10 + rand.nextDouble() * 10, 20 + rand.nextDouble() * 225, 3, 3);
            gc.fillOval(15 + rand.nextDouble() * 170, 245 - rand.nextDouble() * 10, 3, 3);
        }
        gc.drawImage(cardPackImage, 25, 25, 150, 210);
        gc.setFill(Color.YELLOW);
        gc.setEffect(new BoxBlur(5, 5, 1));
        gc.fillOval(corners.get(0).doubleValue(), 20, 5, 5);
        gc.fillOval(175, corners.get(1).doubleValue(), 5, 5);
        gc.fillOval(corners.get(2).doubleValue(), 235, 5, 5);
        gc.fillOval(20, corners.get(3).doubleValue(), 5, 5);
        gc.setEffect(null);
    }

    /**
     * Prepares card pack opening animation (slowly inflate).
     *
     * @param gc Graphics Context for canvas.
     */
    private void prepareOpenAnimations(GraphicsContext gc) {
        rewardScale = new SimpleDoubleProperty();
        rewardScale.set(0.1);
        openPackTimeline = new Timeline();
        openPackTimeline.setCycleCount(1);
        KeyValue scaleVal = new KeyValue(rewardScale, 2.5, Interpolator.LINEAR);
        KeyFrame scaleFrame = new KeyFrame(Duration.millis(1000), scaleVal);
        openPackTimeline.getKeyFrames().add(scaleFrame);

        openPackTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (rewardScale.get() > 2.25) {
                    rewardScale.set(2.25 - (rewardScale.get() - 2.25));
                }
                gc.drawImage(treasureRoomOpen, 0, 0, 700, 600);
                for (int i = 0; i < 4; i++) {
                    ViewCard currentCard = rewards.get(i);
                    currentCard.setScale(rewardScale.get());
                    currentCard.setX((int) (REWARD_X_POSITIONS[i] -
                            rewardScale.get() * Card.getCardWidth() / 2));
                    currentCard.setY((int) (REWARD_Y_POSITIONS[i] -
                            rewardScale.get() * Card.getCardHeight() / 2));
                    currentCard.drawCard(gc);
                }
            }
        };
    }

    /**
     * Prepares treasure chest glow animation.
     *
     * @param gc Graphics Context for canvas.
     */
    private void prepareChestAnimation(GraphicsContext gc) {
        DoubleProperty glowSize = new SimpleDoubleProperty();
        glowSize.set(10);
        glowTimeline = new Timeline();
        glowTimeline.setCycleCount(Timeline.INDEFINITE);

        KeyValue size = new KeyValue(glowSize, 30);
        KeyFrame keyframe = new KeyFrame(Duration.millis(1500), size);
        glowTimeline.getKeyFrames().add(keyframe);
        glowTimeline.setAutoReverse(true);

        glowTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                gc.setFill(Color.YELLOW);
                gc.setGlobalAlpha(glowSize.get() / 60 + 0.1);
                gc.setEffect(new BoxBlur(5, 5, 1));
                double[] x = {330.0, 383.0,
                        393.0 + glowSize.get() / 3, 320.0 - glowSize.get() / 3};
                double[] y = {360.0, 360.0,
                        350.0 - glowSize.get() / 2, 350.0 - glowSize.get() / 2};
                gc.fillPolygon(x, y, 4);
                gc.setGlobalAlpha(1.0);
                gc.setEffect(null);
            }
        };
    }

    /**
     * Plays the card pack opening animation from beginning.
     */
    private void playOpenPackAnimation() {
        rewardScale.set(0.1);
        openPackTimer.start();
        openPackTimeline.play();
        glowTimer.start();
        glowTimeline.play();
    }

    /**
     * Called when opening a card pack.
     * (Updates labels, buttons and animations).
     */
    private void openPack() {
        GameManager.playClip("opening_decksection.wav");
        packs--;
        user.setCardPacks(packs);
        setPackLabel();
        if (packs == 0) {
            openButton.setDisable(true);
        }
        generateRewards();
        playOpenPackAnimation();
    }

    /**
     * Generate pack rewards (four reward cards).
     */
    private void generateRewards() {
        rewards.clear();
        CardList cards1 = new CardList();
        CardList cards2 = new CardList();
        CardList cards3 = new CardList();
        CardList cards4 = new CardList();
        CardList[] lottery = {cards1, cards2, cards3, cards4};
        fillLottery(lottery);
        Random ra = new Random();
        for (int i = 0; i < lottery.length; i++)
            fillRewardSlot(ra, i, lottery);

    }

    /**
     * Creates four pools of cards from which to select pack rewards.
     *
     * @param lottery Combined pool of reward cards.
     */
    private void fillLottery(CardList[] lottery) {
        for (MinionCard mc : MinionCard.getCards().values()) {
            if (mc.getRank() <= 2) {
                lottery[0].addCard(mc);
            }
            if (mc.getRank() <= 3) {
                lottery[1].addCard(mc);
            }
            if (mc.getRank() <= 4) {
                lottery[2].addCard(mc);
            }
            if (mc.getRank() <= 5) {
                lottery[3].addCard(mc);
            }
        }
    }

    /**
     * Fills one of the four slots in the pack rewards. Must be called for each
     * slot in ascending order. Reward card quality scales with pack slot
     * (from left to right) and with player level.
     *
     * @param rand    Random Generator.
     * @param slot    Position in card pack.
     * @param lottery Combined pool of reward cards.
     */
    private void fillRewardSlot(Random rand, int slot, CardList[] lottery) {
        rewards.add(new ViewCard(lottery[user.getLevel() - 1].getCards().get(rand.nextInt(lottery[user.getLevel() - 1].getSize())),
                REWARD_X_POSITIONS[slot], REWARD_Y_POSITIONS[slot], 0.1));
        user.unlockCard(rewards.get(slot).getCard(), 1);
    }

    /**
     * Called when buying a card pack. Updates labels, buttons, currency.
     */
    public void buyPack() {
        GameManager.playClip("buy_sell.wav");
        user.modifyCurrency(-1 * PACK_COST);
        packs++;
        user.setCardPacks(packs);
        setPackLabel();
        currencyLabel.setText(YOU_HAVE + user.getCurrency());
        if (openButton.isDisable()) {
            openButton.setDisable(false);
        }
        if (user.getCurrency() < PACK_COST) {
            buyButton.setDisable(true);
        }
    }

    /**
     * Handle the back button
     * <p>
     * If the current game is in story mode go to next cutscene if player won
     * or previous cutscene if player lost
     *
     * @param event
     */
    private void handleBackButton(ActionEvent event) {
        if (GameManager.getInstance().getBoss() > 0) {
            // boss match
            if (GameManager.getInstance().getCutscene() == 'b') {
                // player won
                int level = Integer.parseInt(GameManager.getStatisticsTracking()
                        .getUserStats().getUserLevel());
                if (level < 12) {
                    GameManager.getStatisticsTracking().getUserStats().addToUserLevel(1);
                    GameManager.changeScene("StoryCutsceneScreen"
                            + GameManager.getInstance().getBoss() + "b.fxml");
                } else {
                    // max level
                    GameManager.getInstance().setBoss(0);
                    GameManager.changeScene("StoryModeScreen.fxml");
                }

            } else {
                // player lost - try again
                GameManager.changeScene("StoryCutsceneScreen"
                        + GameManager.getInstance().getBoss() + "a.fxml");
            }
        } else {
            // not boss match
            GameManager.changeScene("MenuScreen.fxml");
        }

    }

}
