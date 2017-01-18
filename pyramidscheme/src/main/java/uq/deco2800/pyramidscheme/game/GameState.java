package uq.deco2800.pyramidscheme.game;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uq.deco2800.pyramidscheme.addinfo.AddInfo;
import uq.deco2800.pyramidscheme.animations.emitter.Particle;
import uq.deco2800.pyramidscheme.board.RecBoard;
import uq.deco2800.pyramidscheme.board.RecTile;
import uq.deco2800.pyramidscheme.cards.supercards.Card;
import uq.deco2800.pyramidscheme.cards.supercards.CardHiddenState;
import uq.deco2800.pyramidscheme.controllers.statemachine.StateCallback;
import uq.deco2800.pyramidscheme.duckdust.DuckDustPool;
import uq.deco2800.pyramidscheme.duckdust.Grinder;
import uq.deco2800.pyramidscheme.match.*;
import uq.deco2800.pyramidscheme.pyramid.Pyramid;
import uq.deco2800.pyramidscheme.pyramid.PyramidType;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;


/**
 * Created by nick on 11/09/2016.
 */
public class GameState {

    private static final int CANVASWIDTH = 900;
    private static final int CANVASHEIGHT = 510; // must match value in MatchScreen.fxml!!!

    private Pyramid aiPyramid;
    private Pyramid userPyramid;

    private MatchDeck playerGraveyard;
    private MatchDeck aiGraveyard;

    private MatchDeck userMatchDeck;
    private MatchDeck aiMatchDeck;

    private DuckDustPool userDuckDust;
    private DuckDustPool aiDuckDust;
    private Grinder grinder;

    private static boolean tabPaneToggle = true;

    private RecBoard board;

    private ArrayList<MatchCard> animatingCardsUser;
    private ArrayList<MatchCard> animatingCardsAI;
    private ArrayList<MatchCard> animatingFaceup;

    private Optional<AnchorPane> animationPane = Optional.empty();

    // A held MatchCard by the user
    private Optional<MatchCard> held = Optional.empty();

    // Info pane
    private Optional<AddInfo> addInfo = Optional.empty();

    private Image attackImage;
    private Point2D attackImageOrigin;
    private boolean attackImageVisable;

    // Logger for attack
    private Logger logger = LoggerFactory.getLogger(GameState.class);

    // Health labels
    private Text opponentsHealth = new Text("");
    private Text userHealth = new Text("");

    // Card labels
    private Text userDeckCount = new Text("");
    private Text userGraveyardCount = new Text("");


    // Styles
    private String standardTextStyle =
            "-fx-text-fill: #ffffff"
                    + "-fx-font-family: calibri light;"
                    + "-fx-font-size: 1.3em;";
    private String tempMargin = "       ";

    private double timerCount;


    private List<Particle> particles = new ArrayList<>();

    public GameState(Match match) {
        //Set up at default locations. These will be changed at a later date.

        playerGraveyard = new MatchDeck(800, 375);
        userMatchDeck = new MatchDeck(600, 375, match.getUser().getDeck());
        userMatchDeck.shuffle(); // Literally jumbles them up on the screen
        userMatchDeck.alignCards(); // You actually have to make them straight

        userPyramid = new Pyramid(300, 300, PyramidType.TRIANGLE, true);

        grinder = new Grinder(150, 280);
        userDuckDust = match.getUser().duckDustPool;
        aiDuckDust = match.getOpponent().duckDustPool;

        aiGraveyard = new MatchDeck(800, 125);
        aiMatchDeck = new MatchDeck(670, 75, match.getOpponent().getDeck());
        aiMatchDeck.shuffle();
        aiMatchDeck.alignCards();

        aiPyramid = new Pyramid(20, 50, PyramidType.TRIANGLE, false);

        board = new RecBoard(225, 75, match.getUser(), match.getOpponent());

        (match.getOpponent()).setBoard(board);
        (match.getOpponent()).setPyramid(aiPyramid);

        animatingCardsAI = new ArrayList<>();
        animatingCardsUser = new ArrayList<>();
        animatingFaceup = new ArrayList<>();
    }

    /**
     * Wipes and redraws all objects in the canvas. All objects that need to be
     * drawn to the screen should have their draw methods called by this method.
     */
    public void draw(GraphicsContext gc) {
        // Reset graphics context
        gc.setGlobalAlpha(1.0);
        gc.setGlobalBlendMode(BlendMode.SRC_OVER);
        gc.clearRect(0, 0, CANVASWIDTH, CANVASHEIGHT);

        board.draw(gc);

        aiPyramid.draw(gc);
        userPyramid.draw(gc);

        userMatchDeck.draw(gc);
        playerGraveyard.draw(gc);

        grinder.draw(gc, userDuckDust);

        aiGraveyard.draw(gc);
        aiMatchDeck.drawHidden(gc);

        held.ifPresent(x -> x.drawHidden(gc, CardHiddenState.PLACED));

        for (MatchCard anAnimatingCardsAI : animatingCardsAI) {
            anAnimatingCardsAI.drawHidden(gc, CardHiddenState.AIHIDDEN);
        }

        for (MatchCard anAnimatingCardsUser : animatingCardsUser) {
            anAnimatingCardsUser.drawHidden(gc, CardHiddenState.USERHIDDEN);
        }

        for (MatchCard anAnimatingFaceup : animatingFaceup) {
            anAnimatingFaceup.draw(gc);

        }

        if (addInfo.isPresent() && timerCount < 60) {
            timerCount++;
        } else if (addInfo.isPresent() && getCardView()) {
            addInfo.get().drawInfo(gc, 667, 90, 200, 297);
        } else {
            timerCount = 0;
        }

        // Draw attack image
        if (attackImageVisable) {
            gc.drawImage(getAttackImage(), attackImageOrigin.getX() + Card.getCardWidth() / 3,
                    attackImageOrigin.getY() + Card.getCardHeight() / 3, 45, 45);
        }

        particleUpdate(gc);
    }

    private void particleUpdate(GraphicsContext gc) {
        if (particles.isEmpty()) {
            return;
        }
        for (Iterator<Particle> it = particles.iterator(); it.hasNext(); ) {
            Particle p = it.next();
            p.update();
            if (!p.isAlive()) {
                it.remove();
                continue;
            }
            p.render(gc);
        }
    }

    public void addParticles(List<Particle> newParticles) {
        particles.addAll(newParticles);
    }

    public List<Particle> getParticles() {
        return particles;
    }

    public MatchDeck getUserGraveyard() {
        return playerGraveyard;
    }

    public MatchDeck getAIGraveyard() {
        return aiGraveyard;
    }

    public MatchDeck getUserMatchDeck() {
        return userMatchDeck;
    }

    public MatchDeck getAIMatchDeck() {
        return aiMatchDeck;
    }

    public DuckDustPool getUserDuckDust() {
        return userDuckDust;
    }

    public DuckDustPool getAiDuckDust() {
        return aiDuckDust;
    }

    public Grinder getGrinder() {
        return grinder;
    }

    public Pyramid getUserPyramid() {
        return userPyramid;
    }

    public Pyramid getAIPyramid() {
        return aiPyramid;
    }

    public RecBoard getBoard() {
        return board;
    }

    public Optional<MatchCard> getHeld() {
        return held;
    }

    public void dropHeld() {
        held = Optional.empty();
    }

    public void setHeld(Optional<MatchCard> card) {
        held = card;
    }

    public void addAnimatingCard(MatchCard card, CardHiddenState state) {
        switch (state) {
            case AIHIDDEN:
                animatingCardsAI.add(card);
                break;
            case USERHIDDEN:
                animatingCardsUser.add(card);
                break;
            default:
                animatingFaceup.add(card);
                break;
        }
    }

    public MatchCard getAnimatingCard(MatchCard card, CardHiddenState state) {
        switch (state) {
            case AIHIDDEN:
                return animatingCardsAI.get(animatingCardsAI.indexOf(card));
            case USERHIDDEN:
                return animatingCardsUser.get(animatingCardsUser.indexOf(card));
            default:
                return animatingFaceup.get(animatingFaceup.indexOf(card));
        }
    }

    public void removeAnimatingCard(MatchCard card, CardHiddenState state) {
        switch (state) {
            case AIHIDDEN:
                animatingCardsAI.remove(card);
                break;
            case USERHIDDEN:
                animatingCardsUser.remove(card);
                break;
            default:
                animatingFaceup.remove(card);
                break;
        }
    }

    public void addInfoPanel(MatchCard card) {
        addInfo = Optional.of(new AddInfo(card));
    }

    public Optional<AddInfo> getInfoPanel() {
        return addInfo;
    }

    public void removeInfoPanel() {
        addInfo = Optional.empty();
    }

    //Sets whether card view is available or not
    public static boolean toggleCardView() {
        tabPaneToggle = !tabPaneToggle;
        return tabPaneToggle;
    }

    public boolean getCardView() {
        return tabPaneToggle;
    }

    public void setPyramidTypes(PyramidType userType, PyramidType aiType) {
        //Change the shape to specified
        userPyramid.setShape(userType);
        aiPyramid.setShape(aiType);

        //Arrange the pyramid so it displays correctly
        userPyramid.arrangePyramid();
        aiPyramid.arrangePyramid();
    }

    public void attack(MatchPlayer user, MatchPlayer opponent, StateCallback callback) {
        for (RecTile attackingTile : board) {
            if (attackingTile.getOwner() == user && attackingTile.getContents().isPresent()) {
                // If there is a defending card there
                attackingTile.getContents().get().getCard().processTurn(attackingTile, board);
                if (attackingTile.getOppositeTile().getContents().isPresent()) {
                    MatchCard attackingCard = attackingTile.getContents().get();
                    MatchCard defendingCard = attackingTile.getOppositeTile().getContents().get();

                    logger.info("DEBUG: " + attackingCard.toString()
                            + " attacking "
                            + defendingCard.toString());
                    logger.info("DEBUG: Before attack - health attacking: "
                             + attackingCard.getHealth()
                             + ", health defending: "
                             + defendingCard.getHealth());
                    // Attack opposite tile
                    defendingCard.attackedBy(attackingCard);

                    //Update Statistics tracking
                    if (defendingCard.isToDie() && user.aiOrUser()) {
                        GameManager.getStatisticsTracking().addToMinionsKilled(1);
                    } else if (defendingCard.isToDie() && !user.aiOrUser()) {
                        GameManager.getStatisticsTracking().addToMinionsLost(1);
                    }
                    logger.info("DEBUG: After attack - health attacking: "
                             + attackingCard.getHealth()
                             + ", health defending: "
                             + defendingCard.getHealth());
                } else {
                    // No card there.
                    opponent.attackedBy(attackingTile.getContents().get());
                }
            }
        }


        // Update health and check if dead
        if (opponent instanceof MatchAI) {
            updateHealth(user, opponent);
            if (opponent.getHealth() <= 0) {
                //player wins - set next cut-scene
                GameManager.getInstance().setCutscene('b');
                callback.gameOver(true);
                return;
            }

        } else {
            updateHealth(opponent, user);

            if (opponent.getHealth() <= 0) {
                callback.gameOver(false);
                return;
            }
        }

        // Check for playable cards
        if (opponent instanceof MatchAI) {
            if (aiMatchDeck.isEmpty() && aiPyramid.isEmpty()) {
                //player wins - set next cut-scene
                GameManager.getInstance().setCutscene('b');
                callback.gameOver(true);
                return;
            }
        } else {
            if (userMatchDeck.isEmpty() && userPyramid.isEmpty()) {
                callback.gameOver(false);
                return;
            }
        }
    }

    public void updateCardCount() {
        userDeckCount.setStyle(standardTextStyle);
        userDeckCount.setFill(Color.WHITE);
        userGraveyardCount.setStyle(standardTextStyle);
        userGraveyardCount.setFill(Color.TRANSPARENT);
        userDeckCount.setText("Deck: " + userMatchDeck.size() + " Cards");
        userGraveyardCount.setText("Graveyard: " + playerGraveyard.size() + " Cards");
    }

    public void updateHealth(MatchPlayer user, MatchPlayer ai) {
        userHealth.setStyle(standardTextStyle);
        userHealth.setFill(Color.WHITE);
        opponentsHealth.setStyle(standardTextStyle);
        opponentsHealth.setFill(Color.WHITE);
        userHealth.setText(tempMargin + user.getPlayer().getName() + ": " + user.getHealth() + " HP");
        opponentsHealth.setText(tempMargin + ai.getPlayer().getName() + ": " + ai.getHealth() + " HP");
    }

    public void setAnimationPane(AnchorPane animationPane) {
        this.animationPane = Optional.ofNullable(animationPane);
    }

    /**
     * Given an x and y screenspace coordinates, if those x and y coordinates
     * lie ontop of a card, display an info panel containing that cards
     * information
     *
     * @param x The x screenspace coordinate that the game should attempt
     *          to show info for
     * @param y The y screenspace coordinate that the game should attempt
     *          to show info for
     */
    public void hoverCard(int x, int y) {
        if (getUserPyramid().getMatchCardAt(x, y).isPresent()) {
            Optional<MatchCard> cardToDisplay = getUserPyramid().getMatchCardAt(x, y);
            addInfoPanel(cardToDisplay.get());
        } else if (getBoard().getTileAt(x, y).isPresent()) {
            Optional<RecTile> tile = getBoard().getTileAt(x, y);
            if (tile.get().getContents().isPresent()) {
                addInfoPanel(tile.get().getContents().get());
            } else {
                removeInfoPanel();
            }
        } else {
            removeInfoPanel();
        }
    }


    public Optional<AnchorPane> getAnimationPane() {
        return animationPane;
    }

    public void setHealthLabels(TextFlow usersData, TextFlow opponentsData) {
        usersData.getChildren().addAll(userHealth);
        opponentsData.getChildren().addAll(opponentsHealth);
    }

    public void setCardCountLabels(TextFlow userDeck, TextFlow userGraveyard) {
        userDeck.getChildren().addAll(userDeckCount);
        userGraveyard.getChildren().addAll(userGraveyardCount);
        updateCardCount();
    }

    public void addAttackImage(int x, int y) {
        attackImageVisable = true;
        attackImageOrigin = new Point2D(x, y);
    }

    public void removeAttackImage() {
        attackImageVisable = false;
    }

    private Image getAttackImage() {
        if (attackImage == null) {
            attackImage = new Image(getClass().getResourceAsStream("/actionImages/bang.png"));
        } else {
            return attackImage;
        }
        return null;
    }
}
