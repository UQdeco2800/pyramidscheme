package uq.deco2800.pyramidscheme.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uq.deco2800.pyramidscheme.game.GameManager;
import uq.deco2800.pyramidscheme.pyramid.PyramidType;
import uq.deco2800.singularity.clients.pyramidscheme.PyramidMultiplayerClient;
import uq.deco2800.singularity.clients.pyramidscheme.PyramidMultiplayerListener;
import uq.deco2800.singularity.common.SessionType;
import uq.deco2800.singularity.common.representations.pyramidscheme.multiplayer.*;
import uq.deco2800.singularity.common.representations.realtime.RealTimeSessionConfiguration;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Created by nick on 22/10/16.
 */
public class LobbyController implements Initializable {

    private static final Logger logger = LoggerFactory.getLogger(LobbyController.class);

    @FXML
    private Button cancelButton;

    private GameManager gameManager;

    private ArrayList<RealTimeSessionConfiguration> lobbies = new ArrayList<>();
    private int testLobbyCount = 0;
    private PyramidMultiplayerClient multiplayerClient;

    private PyramidMultiplayerListener listener = new PyramidMultiplayerListener() {
        @Override
        public void startGame() {
            logger.debug("starting match");
            // Remove the listener
            multiplayerClient.removeListener(listener);
            gameManager.setMultiplayerClient(multiplayerClient);
            // Run later since it can't run from the client thread
            Platform.runLater(() ->
                    GameManager.gameScene("MultiplayerScreen.fxml", PyramidType.TRIANGLE, PyramidType.TRIANGLE));
        }

        @Override
        public void lobbyFull() {
            logger.debug("lobby full");
            testLobbyCount++;
            testNextLobby();
        }

        @Override
        public void lobbyFound() {
            logger.debug("lobby found");
        }

        @Override
        public void pyramidRefill(PyramidRefill pyramidRefill) {
            // Method not required
        }

        @Override
        public void playerForfeited(PlayerForfeited playerForfeited) {
            // Method not required
        }

        @Override
        public void playCard(PlayCard playCard) {
            // Method not required
        }

        @Override
        public void passTurn(PassTurn passTurn) {
            // Method not required
        }

        @Override
        public void grindCard(GrindCard grindCard) {
            // Method not required

        }

        @Override
        public void championAbility(ChampionAbility championAbility) {
            // Method not required
        }

        @Override
        public void deckReceived(PlayerDeck playerDeck) {
            // Method not required
        }

        @Override
        public void sendPlayerToken() {
            // Method not required
        }

        @Override
        public void sendDeck() {
            // Method not required
        }

        @Override
        public void getTurn(Turn turn) {
            // Method not required
        }

        @Override
        public void attack(Attack attack) {
            // Method not required
        }

        @Override
        public void nowGameOver(GameOver gameOver) {
            // Method not required
        }
    };

    /**
     * Called to initialize a controller after its root element has been
     * completely processed.
     *
     * @param location  The location used to resolve relative paths for the root object, or
     *                  <tt>null</tt> if the location is not known.
     * @param resources The resources used to localize the root object, or <tt>null</tt> if
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cancelButton.setOnAction(e -> returnToMenu());
        gameManager = GameManager.getInstance();
        gameManager.closeMultiplayer();
        findlobby();
    }

    public void findlobby() {
        logger.debug("finding lobby");
        try {
            lobbies = gameManager.getPyramidSchemeClient().requestAvailableLobbies();
            logger.debug("found " + lobbies.size() + " lobbies");
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Create a lobby
        if (lobbies.size() == 0) {
            createLobby();
        } else {
            testNextLobby();
        }
    }

    private void testNextLobby() {
        logger.debug("testing next lobby");
        if (lobbies.size() > testLobbyCount) {
            try {
                logger.info("Testing lobby");
                multiplayerClient = new PyramidMultiplayerClient(lobbies.get(testLobbyCount),
                        gameManager.getPyramidSchemeClient());
                multiplayerClient.addListener(listener);
                multiplayerClient.getStatus();
            } catch (IOException e) {
                logger.error("Failed to create multiplayerClient", e);
            }
        } else {
            createLobby();
        }
    }

    private void createLobby() {
        logger.debug("creating lobby since no lobbies exist");
        try {
            RealTimeSessionConfiguration config = gameManager.getPyramidSchemeClient().requestGameSession(SessionType.PYRAMID_SCHEME);
            multiplayerClient = new PyramidMultiplayerClient(config,
                    gameManager.getPyramidSchemeClient());
            multiplayerClient.addListener(listener);
        } catch (IOException e) {
            logger.error("Failed to create multiplayerClient", e);
        }
    }

    private void returnToMenu() {
        // Attempt to send forfeit, may not have a connection
        try {
            multiplayerClient.sendForfeit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        GameManager.changeScene("MenuScreen.fxml");
    }

}
