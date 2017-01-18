package uq.deco2800.pyramidscheme.controllers;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uq.deco2800.pyramidscheme.cards.BasicMinion;
import uq.deco2800.pyramidscheme.champions.abilities.Ability;
import uq.deco2800.pyramidscheme.controllers.statemachine.*;
import uq.deco2800.pyramidscheme.duckdust.DuckDustPool;
import uq.deco2800.pyramidscheme.game.GameManager;
import uq.deco2800.pyramidscheme.game.GameState;
import uq.deco2800.pyramidscheme.match.Match;
import uq.deco2800.pyramidscheme.match.MatchCard;
import uq.deco2800.pyramidscheme.player.AI;
import uq.deco2800.pyramidscheme.player.User;
import uq.deco2800.pyramidscheme.pyramid.PyramidType;

/**
 * A controller for the Basic match mode.
 *
 * @author Millie
 */

public class BasicMatchController extends MatchController implements Initializable, StateCallback {

    private static final int CANVASWIDTH = 900;
    private static final int CANVASHEIGHT = 510; // must match value in
    // MatchScreen.fxml!!!

    private Boolean matchStarted = false;
    GameManager gameManager;
    Match match;
    private GraphicsContext gc;
    User user;

    // Holds the current state of the game
    StateNode state;

    // Where all the Class data is congregated
    GameState gameState;


    // FX elements this class uses
    @FXML
    private AnchorPane basicMatchScreen;
    @FXML
    private StackPane matchPane;
    @FXML
    private TextFlow usersData;
    @FXML
    private TextFlow opponentsData;
    @FXML
    private TextFlow userGraveyard;
    @FXML
    private TextFlow userDeck;
    @FXML
    Button primaryAbilityButton;
    @FXML
    Button secondaryAbilityButton;
    @FXML
    private Button toggleCardView;
    @FXML
    private ImageView championImage;
    @FXML
    private ImageView primaryAbilityImage;
    @FXML
    private ImageView secondaryAbilityImage;
    @FXML
    private ImageView toggleCardImage;
    @FXML
    private AnchorPane animationPane;
    @FXML
    Button passTurnButton;

    private ThereIsNoSpoon spoon;

    private Logger logger = LoggerFactory.getLogger(BasicMatchController.class);

    protected void startMatch() {
        if (!matchStarted) {
            // Initialise required variables
            gameManager = GameManager.getInstance();

            // Create a new match - needs User and an opponent
            user = gameManager.getUser();
            AI opponent;
            if (GameManager.getInstance().getBoss() == 0) {
                opponent = gameManager.getGameGenerator().createBasicAI();
            } else {
                opponent = gameManager.getGameGenerator().createBossAI(GameManager.getInstance().getBoss(), user.getDeck());
            }
            // needs user and opponent
            createMatch(opponent);

            gameState = match.gameState();

            // Set labels in gamestate
            gameState.setHealthLabels(usersData, opponentsData);
            gameState.setCardCountLabels(userDeck, userGraveyard);
            gameState.updateHealth(match.getUser(), match.getOpponent());

            // set handler for champion ability button
            primaryAbilityButton.setOnAction(this::handleChampAbilityButton);
            secondaryAbilityButton.setOnAction(this::handleChampAbilityButton);

            //set toggle for card view, game starts with it on (true)
            toggleCardView.setOnAction(this::handleToggleCardView);

            // set champion, ability and toggle button images
            championImage.setImage(gameManager.getSelectedChamp().getImage());
            primaryAbilityImage.setImage(gameManager.getSelectedChamp().getAbility(0).getImage());
            secondaryAbilityImage.setImage(gameManager.getSelectedChamp().getAbility(1).getImage());
            toggleCardImage.setImage(new Image(getClass().getResourceAsStream("/cardImages/hidden_user.png")));

            // Create canvas and animation controller
            createCanvas();

            // Set the state to idle
            state = new Idle(match, this);

            // Start animating
            startTick();

            matchStarted = true;

            primaryAbilityButton.setTooltip(new Tooltip(gameManager.getSelectedChamp().getAbility(0).toString()));
            secondaryAbilityButton.setTooltip(new Tooltip(gameManager.getSelectedChamp().getAbility(1).toString()));

            spoon = new ThereIsNoSpoon(match.getUser().duckDustPool);
            basicMatchScreen.setOnKeyReleased(e -> isThereASpoon(e.getCode()));
        }
    }

    void createMatch(AI opponent) {
        match = new Match(user, opponent);
    }

    /**
     * Builds the canvas
     */
    private void createCanvas() {
        // Create a canvas for the match
        Canvas canvas = new Canvas(CANVASWIDTH, CANVASHEIGHT);
        gc = canvas.getGraphicsContext2D();
        canvas.setOnMouseClicked(this::handleCanvasClick);
        canvas.setOnMouseMoved(this::handleMouseMoved);

        // Load the canvas
        matchPane.setStyle("");// "-fx-background-color: #4AC66D"); // so we can
        // see where the canvas ends
        matchPane.getChildren().add(canvas);

        // Set up the animation canvas in GameState
        gameState.setAnimationPane(animationPane);
        animationPane.setMouseTransparent(true);
    }

    /**
     * Starts the animation timer to redraw the game at 60fps
     */
    private void startTick() {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.millis(16), ae -> gameState.draw(gc)));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }


    /**
     * Called when the pass turn button is clicked. This causes the user to
     * attack with all their creatures, then end their turn, allowing AI to
     * play. Clean up step is called at the end of each "turn" to redraw the
     * board and remove dead minions.
     *
     * @param event
     */
    void handlePassTurn(ActionEvent event) {
        // Only allow button to work when in Idle
        if (state instanceof Idle) {
            // Attack when pass turn.
            state = new Attack(match, this);
            gameState.attack(match.getUser(), match.getOpponent(), this);

            // Hand off to AI and wait for aiDone() callback
            match.getOpponent().duckDustPool.refreshDust();
            state = new MatchAITurn(match, this);

            // Refresh dust at end of AI turn (start of your turn).
            match.getUser().duckDustPool.refreshDust();
        }
    }

    /**
     * This event occurs when mouse button has been clicked (pressed and
     * released on the canvas.
     *
     * @param event The MouseEvent that was caught
     */
    void handleCanvasClick(MouseEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();

        // Send click to state
        state = state.processClick(x, y);
    }

    /**
     * "This event occurs when mouse moves within a node and no buttons are
     * pressed. If any mouse button is pressed, MOUSE_DRAGGED event occurs
     * instead."
     *
     * @param event The MouseEvent that was caught
     */
    private void handleMouseMoved(MouseEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        state = state.processMoved(x, y);
    }

    /**
     * On champion ability button press, checks which button was pressed and
     * gets either the primary or secondary champion ability. The ability is
     * then used, depending on the users actions, and the ability type.
     *
     * @param event a button press.
     */
    void handleChampAbilityButton(ActionEvent event) {
        if (state instanceof Idle) {
            int abilityId;
            // select the ability to be used, based on which button is pressed.
            // 0 for primary ability, 1 for secondary.
            if (event.getSource() == primaryAbilityButton) {
                abilityId = 0;
            } else if (event.getSource() == secondaryAbilityButton) {
                abilityId = 1;
            } else {
                throw new IllegalArgumentException("Invalid button linked");
            }
            Ability ability = gameManager.getSelectedChamp().getAbility(abilityId);
            if (gameState.getUserDuckDust().isPlayable(ability.getCost())) {

                state = new AbilityNode(match, this, abilityId, gameState.getUserDuckDust());
                ((AbilityNode) state).checkAbilities(ability);
            } else {
                logger.info(ability.getName() + "requires " + ability.getCost() + " duck dust to play");
            }

        }
    }

    @Override
    public void goToIdle() {
        if (!gameState.getHeld().isPresent()) {
            state = new Idle(match, this);
        }
    }

    @Override
    public void gameOver(boolean userWon) {
        GameManager.getStatisticsTracking().stopChampPlayTimeTracking();
        if (userWon) {
            user = GameManager.getInstance().getUser();
            user.updateCardPack(1);
            GameManager.getStatisticsTracking().addToTotalWins(1);
            GameManager.getAchieveTracking().checkIfGot(GameManager.getStatisticsTracking().getUserStats());
            GameManager.getStatisticsTracking().pushCurrentStats();
            goToEndGameScreenVictory();
        } else {
            GameManager.getStatisticsTracking().addToTotalLosses(1);
            GameManager.getAchieveTracking().checkIfGot(GameManager.getStatisticsTracking().getUserStats());
            GameManager.getStatisticsTracking().pushCurrentStats();
            goToEndGameScreenDefeat();
        }
    }

    @Override
    public void turnOver() {
        logger.debug("out of mana, go to ai turn");
        if (state instanceof AnimatingNode || state instanceof HeldCard) {
            goToIdle();
        }
        Platform.runLater(() -> passTurnButton.fire());
    }

    /**
     * Given two PyramidType enums, one each for both pyramids,
     * sets both pyramid's layouts simultaneously.
     *
     * @param userType A PyramidType enum, for the user's pyramid
     * @param aiType   A PyramidType enum, for the AI's pyramid
     */
    public void setPyramidTypes(PyramidType userType, PyramidType aiType) {
        gameState.setPyramidTypes(userType, aiType);
        // Refill ai pyramid
        if (gameState.getAIPyramid().isEmpty()) {
            gameState.getAIPyramid().loadCards(
                    gameState.getAIMatchDeck().popCards(gameState.getAIPyramid().getPyramidType().getSize()));
        }
        gameState.getAIPyramid().arrangePyramid();
        state = new AnimatingNode(match, this);
        ((AnimatingNode) state).refillPyramid();
    }

    /**
     * Returns the currently staged (active) StateNode
     *
     * @return a StateNode that is currently appropriately routing userinput
     * into the GameState
     */
    public StateNode getState() {
        return state;
    }

    void setTestMode() {
        gameState.getUserDuckDust().avatarState();
        gameState.getAiDuckDust().avatarState();
        // Put something in the user's hand
        gameState.getUserMatchDeck().pushCard(new MatchCard(new BasicMinion(), 1, 1));
    }

    void isThereASpoon(KeyCode k) {
        spoon.checkForSpoon(k);
    }

    /**
     * thereisnospoon
     *
     * @author Fry
     */
    static class ThereIsNoSpoon {
        // the string of key presses to check
        private KeyCode[] spoon = {KeyCode.T, KeyCode.H, KeyCode.E, KeyCode.R, KeyCode.E, KeyCode.I, KeyCode.S,
                KeyCode.N, KeyCode.O, KeyCode.S, KeyCode.P, KeyCode.O, KeyCode.O, KeyCode.N};

        private int count = 0;
        private DuckDustPool dust;

        ThereIsNoSpoon(DuckDustPool userDust) {
            dust = userDust;
        }

        public void checkForSpoon(KeyCode key) {
            if (key == spoon[count]) {
                count++;
                if (count == 14) {
                    System.out.println("You're the one Neo");
                    count = 0;
                    dust.avatarState();
                    dust.refreshDust();
                }
            } else {
                count = 0;
                return;
            }
        }
    }

    /**
     * On button press, toggles whether card view is available or not.
     * Start of match value is true.
     *
     * @param event a button press.
     */
    private void handleToggleCardView(ActionEvent event) {
        boolean toggleCard = GameState.toggleCardView();
        //Requires alternate to new, to stop reloading
        if (toggleCard) {
            toggleCardImage.setImage(new Image(getClass().getResourceAsStream("/cardImages/hidden_user.png")));
        } else {
            toggleCardImage.setImage(new Image(getClass().getResourceAsStream("/cardImages/hidden_user_off.png")));
        }
    }
}