package uq.deco2800.pyramidscheme.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uq.deco2800.pyramidscheme.animations.AIAnimations;
import uq.deco2800.pyramidscheme.animations.AnimationCallback;
import uq.deco2800.pyramidscheme.board.RecTile;
import uq.deco2800.pyramidscheme.cards.supercards.CardNotFoundException;
import uq.deco2800.pyramidscheme.cards.supercards.MinionCard;
import uq.deco2800.pyramidscheme.champions.abilities.Ability;
import uq.deco2800.pyramidscheme.champions.abilities.MinionRevive;
import uq.deco2800.pyramidscheme.champions.abilities.MinionSummon;
import uq.deco2800.pyramidscheme.controllers.statemachine.*;
import uq.deco2800.pyramidscheme.game.GameManager;
import uq.deco2800.pyramidscheme.match.Match;
import uq.deco2800.pyramidscheme.match.MatchCard;
import uq.deco2800.pyramidscheme.player.AI;
import uq.deco2800.pyramidscheme.pyramid.PyramidType;
import uq.deco2800.singularity.clients.pyramidscheme.PyramidMultiplayerClient;
import uq.deco2800.singularity.clients.pyramidscheme.PyramidMultiplayerListener;
import uq.deco2800.singularity.common.representations.pyramidscheme.multiplayer.Attack;
import uq.deco2800.singularity.common.representations.pyramidscheme.multiplayer.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by nick on 21/10/16.
 */
public class MultiplayerMatchController extends BasicMatchController implements StateCallback, AnimationCallback {


    PyramidMultiplayerClient multiplayerClient;
    private String username;

    private Logger logger = LoggerFactory.getLogger(MultiplayerMatchController.class);

    @FXML
    private Button backButton;
    @FXML
    private TextFlow turnLabel;

    private Text turnText = new Text();

    StateCallback callback = this;

    AIAnimations aiAnimations;

    PyramidMultiplayerListener listener = new PyramidMultiplayerListener() {
        @Override
        public void startGame() {
            if (state instanceof MultiplayerWait) {
                state = new AnimatingNode(match, callback);
                ((AnimatingNode) state).refillPyramid();
            }
        }

        @Override
        public void lobbyFull() {
            // Method not required
        }

        @Override
        public void lobbyFound() {
            // Method not required
        }

        /**
         * Given a PyramidRefill message from Singularity, commands the
         * GameState to transfer cards from deck to pyramid, animating
         * the transfer along the way
         *
         * @param pyramidRefill A PyramidRefill object received from
         *                      Singularity
         */
        @Override
        public void pyramidRefill(PyramidRefill pyramidRefill) {
            aiAnimations.animatePyramidRefill();
        }

        /**
         * Given a PlayerForfeited message from Singularity, attempts to
         * terminate the match, running network cleanup and returning
         * the user to the main menu
         *
         * @param playerForfeited A PlayerForfeited object received from
         *                        Singularity
         */
        @Override
        public void playerForfeited(PlayerForfeited playerForfeited) {
            closeMatch();
        }

        /**
         * Given a PlayCard message from Singularity, unpacks the UID of the
         * opponents card that needs to be played, and the x and y coordinates
         * that _the opponent played the card at_, informs the GameState to move
         * the appropriate card to the appropriate translated location
         *
         * @param playCard A PlayCard object received from Singularity
         */
        @Override
        public void playCard(PlayCard playCard) {
            MatchCard card = getMatchCardFrom(playCard.cardUID);
            if (card != null && gameState.getBoard().getTileAt(playCard.x, playCard.y).isPresent()) {
                RecTile tile = gameState.getBoard().getTileAt(playCard.x, playCard.y).get().getOppositeTile();
                logger.debug("found tile: " + tile.toString());
                aiAnimations.animateAIMove(card, tile);
            }
        }

        @Override
        public void passTurn(PassTurn passTurn) {
            // Method not required

        }

        /**
         * Given a GrindCard message from Singularity, unpacks the UID of the
         * opponents card that needs to be grinded, and informs the GameState
         * of the card's inevitable demise.
         *
         * @param grindCard A GrindCard object received from Singularity
         */
        @Override
        public void grindCard(GrindCard grindCard) {
            MatchCard card = getMatchCardFrom(grindCard.cardUID);
            if (card != null) {
                aiAnimations.grindCard(card);
            }
        }

        @Override
        public void championAbility(ChampionAbility championAbility) {
            Ability ability = gameManager.getChampionCache().getChampion(championAbility.championName)
                    .getAbility(championAbility.abilityID);
            if (ability instanceof MinionRevive) {
                ability.activateAbility(gameState.getAIGraveyard(),
                        gameState.getBoard().getTilesOf(match.getOpponent()).get(0));
            } else if (ability instanceof MinionSummon) {
                ability.activateAbility(gameState.getBoard().getTilesOf(match.getOpponent()).get(0), null);
            } else {
                MatchCard card = getMatchCardFrom(championAbility.targetCardUID);
                if (card != null) {
                    ability.activateAbility(card, null);
                    if (!gameState.getBoard().getDeadTiles().isEmpty()) {
                        aiAnimations.animateGraveyard(match);
                    }
                }
            }
        }

        /**
         * Sends the users deck and pyramid cards to Singularity for UID tagging
         */
        @Override
        public void sendDeck() {
            // Send the user's deck for tagging
            ArrayList<Map<String, Integer>> deck = new ArrayList<>();
            for (MatchCard card : gameState.getUserPyramid().getCards()) {
                Map<String, Integer> map = new HashMap<String, Integer>();
                map.put(card.getCard().getName(), card.getUid());
                deck.add(map);
            }
            for (MatchCard card : gameState.getUserMatchDeck()) {
                Map<String, Integer> map = new HashMap<String, Integer>();
                map.put(card.getCard().getName(), card.getUid());
                deck.add(map);
            }
            logger.debug("sending " + deck.size() + " cards");
            multiplayerClient.sendPlayerDeck(deck);
        }

        /**
         * Given a Turn message received from Singularity, either stages an
         * Idle statenode (if it's the clients turn), or a MultiPlayerWait node
         * (if it's not)
         *
         * @param turn A Turn object received from Singularity
         */
        @Override
        public void getTurn(Turn turn) {
            logger.debug("next player to have a go is: " + turn.username);
            if (turn.username.equals(username)) {
                state = new Idle(match, callback);
                turnText.setText(turn.username + "'s turn!");
            } else {
                state = new MultiplayerWait(match, callback);
                turnText.setText(turn.username + "'s turn!");
            }
        }

        /**
         * Given an Attack message from Singularity, command the GameState to
         * resolve an attack against the opponent.
         *
         * Usually received in conjunction with a PassTurn message.
         *
         * @param attack An Attack object received from Singularity
         */
        @Override
        public void attack(Attack attack) {
            if (!attack.username.equals(username)) {
                gameState.attack(match.getOpponent(), match.getUser(), callback);
                aiAnimations.animateGraveyard(match);
            }
        }

        @Override
        public void nowGameOver(GameOver gameOver) {
            if (gameOver.username.equals(username)) {
                gameOver(gameOver.userWon);
            } else {
                gameOver(!gameOver.userWon);
            }
        }

        /**
         * Given a PlayerDeck message recieved from Singularity, attempt
         * to unpack the cards into the GameState. These cards should have
         * been appropriately tagged with UIDs.
         *
         * @param playerDeck A PlayerDeck object received from Singularity
         */
        @Override
        public void deckReceived(PlayerDeck playerDeck) {
            loadCards(playerDeck);
            logger.debug("Loaded opponents cards");
        }

        @Override
        public void sendPlayerToken() {
            username = gameManager.getPyramidSchemeClient().getUsername();
            multiplayerClient.sendToken(username);
        }
    };

    /**
     * Given a PlayerDeck message recieved from Singularity, attempts to unpack
     * UID tagged cards into the GameState. Unlike deckRecieved (the message
     * handler), this method handles the bulk of the unpacking.
     * <p>
     * UIDs are used so that both clients are able to reference the same
     * equivalent card, without difficult translation methods.
     *
     * @param playerDeck A PlayerDeck object received from Singularity
     */
    private void loadCards(PlayerDeck playerDeck) {
        logger.debug("tagging " + playerDeck.username);
        boolean isUser = playerDeck.username.equals(username);
        if (!isUser) {
            if (gameState.getAIMatchDeck().size() + gameState.getAIPyramid().size() != playerDeck.deck.size()) {
                multiplayerClient.sendForfeit();
            }
        } else {
            if (gameState.getUserPyramid().size() + gameState.getUserMatchDeck().size() != playerDeck.deck.size()) {
                multiplayerClient.sendForfeit();
            }
        }

        Iterator pyramidIterator = isUser ?
                gameState.getUserPyramid().iteratorAllCards() : gameState.getAIPyramid().iteratorAllCards();
        Iterator deckIterator = isUser ? gameState.getUserMatchDeck().iterator() : gameState.getAIMatchDeck().iterator();
        int i = 0;
        for (Map<String, Integer> serverCard : playerDeck.deck) {
            // select the correct iterator
            Iterator iterator;
            if (isUser) {
                iterator = i < gameState.getUserPyramid().size() ? pyramidIterator : deckIterator;
            } else {
                iterator = i < gameState.getAIPyramid().size() ? pyramidIterator : deckIterator;
            }
            // get next card for setting
            MatchCard localCard = (MatchCard) iterator.next();
            // Get first entry of map
            Map.Entry<String, Integer> card = serverCard.entrySet().iterator().next();
            // Set UID
            localCard.setUid(card.getValue());
            // Load card
            if (!isUser) {
                try {
                    localCard.setCard(MinionCard.get(card.getKey()));
                } catch (CardNotFoundException e) {
                    multiplayerClient.sendForfeit();
                    e.printStackTrace();
                }
            }
            i++;
        }
        logger.debug("Tagged cards for " + (isUser ? "user" : "opponent"));
    }

    @Override
    public void startMatch() {
        super.startMatch();
        turnLabel.getChildren().addAll(turnText);
        aiAnimations = new AIAnimations(gameState, this);
        state = new MultiplayerWait(match, this);
        multiplayerClient = gameManager.getMultiplayerClient().get();
        multiplayerClient.addListener(listener);
        // Close session
        backButton.setOnAction(e -> closeMatch());
    }

    @Override
    void createMatch(AI opponent) {
        opponent = gameManager.getGameGenerator().createNetworkAI("Network player");
        match = new Match(user, opponent);
    }

    /**
     * Attempts to close the multiplayer match, and once complete, returns
     * the user to the main menu
     */
    void closeMatch() {
        gameManager.closeMultiplayer();
        Platform.runLater(() -> GameManager.changeScene("MenuScreen.fxml"));
    }

    /**
     * Given a UID, attempts to return the MatchCard in GameState's _AIPyramid_,
     * with the same UID.
     *
     * @param uid The UID of the card being looked up
     * @return A MatchCard (with the the same UID as the parameter),
     * or _null_, if the lookup failed.
     */
    private MatchCard getMatchCardFrom(Integer uid) {
        for (MatchCard card : gameState.getAIPyramid().getCards()) {
            if (card.getUid() == uid) {
                return card;
            }
        }
        logger.error("failed to find card in pyramid");
        return null;
    }

    /**
     * Given a UID, attempts to return the MatchCard in GameState's
     * AIPyramid, AIMatchDeck, AIGraveyard, or RecBoard
     *
     * @param uid The UID of the card being looked up
     * @return A MatchCard (with the the same UID as the parameter),
     * or _null_, if the lookup failed.
     */
    private MatchCard getMatchCardAnywhereFrom(Integer uid) {
        for (MatchCard card : gameState.getAIPyramid().getCards()) {
            if (card.getUid() == uid) {
                return card;
            }
        }
        for (MatchCard card : gameState.getAIMatchDeck()) {
            if (card.getUid() == uid) {
                return card;
            }
        }
        for (RecTile tile : gameState.getBoard()) {
            if (tile.getContents().isPresent() && tile.getContents().get().getUid() == uid) {
                return tile.getContents().get();
            }
        }
        for (MatchCard card : gameState.getAIGraveyard()) {
            if (card.getUid() == uid) {
                return card;
            }
        }
        logger.error("failed to find card anywhere");
        return null;
    }

    @Override
    void handlePassTurn(ActionEvent event) {
        // Only allow button to work when in Idle
        if (state instanceof Idle) {
            // Attack when pass turn.
            state = new uq.deco2800.pyramidscheme.controllers.statemachine.Attack(match, callback);
            gameState.attack(match.getUser(), match.getOpponent(), callback);
            match.getUser().duckDustPool.refreshDust();
            aiAnimations.animateGraveyard(match);
            multiplayerClient.passTurn();
        }
    }

    /**
     * There is no spoon
     *
     * @param k A letter.
     */
    @Override
    void isThereASpoon(KeyCode k) {
        // Don't allow cheaters
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
    public void gameOver(boolean userWon) {
        GameManager.getStatisticsTracking().stopChampPlayTimeTracking();
        multiplayerClient.sendGameOver(userWon);
        if (userWon) {
            user = GameManager.getInstance().getUser();
            user.updateCardPack(1);
            goToEndGameScreenVictory();
            Platform.runLater(this::goToEndGameScreenVictory);
            GameManager.getStatisticsTracking().addToTotalWins(1);
            GameManager.getStatisticsTracking().pushCurrentStats();
        } else {
            GameManager.getStatisticsTracking().addToTotalLosses(1);
            GameManager.getStatisticsTracking().pushCurrentStats();
            Platform.runLater(this::goToEndGameScreenDefeat);
        }
    }

    /**
     * Given two PyramidType enums, one each for both pyramids,
     * sets both pyramid's layouts simultaneously.
     *
     * @param userType A PyramidType enum, for the user's pyramid
     * @param aiType   A PyramidType enum, for the AI's pyramid
     */
    @Override
    public void setPyramidTypes(PyramidType userType, PyramidType aiType) {
        gameState.setPyramidTypes(userType, aiType);
    }

    @Override
    public void goToIdle() {
        if (!gameState.getHeld().isPresent()) {
            multiplayerClient.getTurn();
        }
    }

    @Override
    public void aiPlayingDone() {
        // Method not required
    }

    @Override
    public void userRefillDone() {
        // Method not required
    }

    @Override
    public void graveyardDone() {
        // Method not required
    }

    @Override
    public void grindDone() {
        // Method not required
    }

    @Override
    public void flipDone() {
        // Method not required
    }

    @Override
    public void aiRefillDone() {
        // Method not required
    }
}
