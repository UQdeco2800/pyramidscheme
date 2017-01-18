package uq.deco2800.pyramidscheme.game;

import com.fasterxml.jackson.core.JsonProcessingException;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uq.deco2800.pyramidscheme.GameLauncher;
import uq.deco2800.pyramidscheme.achievements.AchievementTracking;
import uq.deco2800.pyramidscheme.champions.Champion;
import uq.deco2800.pyramidscheme.champions.ChampionCache;
import uq.deco2800.pyramidscheme.chat.ChatController;
import uq.deco2800.pyramidscheme.controllers.BasicMatchController;
import uq.deco2800.pyramidscheme.controllers.ChampionScreenController;
import uq.deco2800.pyramidscheme.network.LoginException;
import uq.deco2800.pyramidscheme.network.NetworkException;
import uq.deco2800.pyramidscheme.network.RegistrationException;
import uq.deco2800.pyramidscheme.player.User;
import uq.deco2800.pyramidscheme.pyramid.PyramidType;
import uq.deco2800.pyramidscheme.settings.ClipCache;
import uq.deco2800.pyramidscheme.settings.Sound;
import uq.deco2800.pyramidscheme.statistics.StatisticsTracking;
import uq.deco2800.singularity.clients.pyramidscheme.PyramidMultiplayerClient;
import uq.deco2800.singularity.clients.pyramidscheme.PyramidSchemeClient;
import uq.deco2800.singularity.clients.realtime.messaging.MessagingClient;
import uq.deco2800.singularity.common.ServerConstants;
import uq.deco2800.singularity.common.SessionType;
import uq.deco2800.singularity.common.representations.realtime.RealTimeSessionConfiguration;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.prefs.Preferences;

/**
 * A singleton to manage all top-level game variables, such as the user.
 *
 * @author Millie
 */

public class GameManager {
    private static final GameManager INSTANCE = new GameManager();

    /**
     * Used to manage the user statistics and track them in game
     */
    private static StatisticsTracking statsTracking;
    private static AchievementTracking achieveTracking;
    /**
     * Primary stage used for game screens.
     */
    private Stage primaryStage;

    /**
     * Used to query the server.
     * <p>
     * After the user logs in, the client contains a session token which is
     * used to authenticate requests to the server.
     */
    private PyramidSchemeClient schemeClient;

    private MessagingClient messagingClient;

    // Used for the multiplayer session
    Optional<PyramidMultiplayerClient> multiplayerClient = Optional.empty();

    /**
     * Used to toggle the chat window.
     */
    private boolean chatScreenOpen = false;
    private Stage chatScreenStage;
    private ChatController chatController;

    private User user;
    private BasicGameGenerator gameGenerator;

    private int boss;
    private char cutscene;

    private ChampionCache championCache = new ChampionCache();

    private Sound sound = initSound();
    private static AudioClip backgroundMusic;

    private static Logger logger = LoggerFactory.getLogger(GameManager.class);

    //Initialize champion to Rallard
    private Champion selectedChamp = championCache.getChampion("Rallard");

    private GameManager() {
        // Initialise a game generator so we can use its methods in SplashScreenController.java
        gameGenerator = new BasicGameGenerator();

        // Create the rest client so that users can log in and make requests
        schemeClient = new PyramidSchemeClient(ServerConstants.PRODUCTION_SERVER, ServerConstants.REST_PORT);

        statsTracking = new StatisticsTracking();
        achieveTracking = new AchievementTracking();

        //set boss to zero for no boss
        boss = 0;
        //cutscene variable either a for before boss or b for after boss
        cutscene = 'a';
    }

    /**
     * Returns the instance of {@link GameManager}.
     *
     * @return Returns an instance of GameManager.
     */
    public static GameManager getInstance() {
        return INSTANCE;
    }

    public static boolean isUserLoggedIn() {
        GameManager gameManager = GameManager.getInstance();
        PyramidSchemeClient schemeClient = gameManager.getPyramidSchemeClient();

        return schemeClient.getUsername() != null;
    }

    /**
     * Log the user in
     *
     * @return the rest client
     * @throws LoginException   if the login credentials are invalid
     * @throws NetworkException if there is a networking error
     */
    public static PyramidSchemeClient loginUser(String username, String password) throws LoginException, NetworkException {
        GameManager gameManager = GameManager.getInstance();
        PyramidSchemeClient schemeClient = gameManager.getPyramidSchemeClient();
        try {
            schemeClient.setupCredentials(username, password);
        } catch (WebApplicationException e) {
            throw new LoginException(e);
        } catch (ProcessingException e) {
            //        no server running, but this seems like an unreasonable exception.
            throw new NetworkException(e);
        }

        return schemeClient;
    }

    public MessagingClient getMessagingClient() {
        if (messagingClient == null) {
            // Set up messaging client
            RealTimeSessionConfiguration config = new RealTimeSessionConfiguration();
            config.setSession(SessionType.MESSAGING);
            config.setPort(ServerConstants.MESSAGING_PORT);

            try {
                messagingClient = new MessagingClient(config, getPyramidSchemeClient(), SessionType.PYRAMID_SCHEME);
                return messagingClient;
            } catch (IOException e) {
                logger.error("Failed to create MessagingClient", e);
                System.exit(1);
            }
        } else {
            return messagingClient;
        }
        return null;
    }


    public void setMultiplayerClient(PyramidMultiplayerClient client) {
        // Check for previous unclean shutdowns
        if (multiplayerClient.isPresent()) {
            closeMultiplayer();
        }
        multiplayerClient = Optional.ofNullable(client);
    }

    public Optional<PyramidMultiplayerClient> getMultiplayerClient() {
        return multiplayerClient;
    }


    public void closeMultiplayer() {
        if (multiplayerClient.isPresent()) {
            multiplayerClient.get().sendForfeit();
        }
        multiplayerClient = Optional.empty();
    }


    public void setMessagingClient(MessagingClient client) {
        messagingClient = client;
    }

    public void setPyramidSchemeClient(PyramidSchemeClient client) {
        this.schemeClient = client;
    }

    public static void createUser(String username, String password) throws NetworkException, RegistrationException {
        GameManager gameManager = GameManager.getInstance();
        PyramidSchemeClient schemeClient = gameManager.getPyramidSchemeClient();
        uq.deco2800.singularity.common.representations.User user =
                new uq.deco2800.singularity.common.representations.User(username, "", "", "", password);
        try {
            user = schemeClient.createUser(user);
        } catch (JsonProcessingException e) {
            throw new NetworkException(e);
        } catch (WebApplicationException e) {
            Response response = e.getResponse();
            int statusCode = response.getStatusInfo().getStatusCode();
            if (statusCode == 409) {
                throw new RegistrationException(e);
            }
            // otherwise
            e.printStackTrace();
            throw new NetworkException(e);
        }
    }

    public static void changeUser(String username, String password) throws NetworkException, RegistrationException {
        GameManager gameManager = GameManager.getInstance();
        PyramidSchemeClient schemeClient = gameManager.getPyramidSchemeClient();


        uq.deco2800.singularity.common.representations.User user =
                new uq.deco2800.singularity.common.representations.User(username, "", "", "", password);
		try {
			schemeClient.createUser(user);
		} catch (JsonProcessingException e) {
			throw new NetworkException(e);
		} catch (WebApplicationException e) {
			Response response = e.getResponse();
			int statusCode = response.getStatusInfo().getStatusCode();
			if (statusCode == 409) {
				throw new RegistrationException(e);
			}
			// otherwise
			e.printStackTrace();
			throw new NetworkException(e);
		}
    }

    /**
     * Change the scene to scene loaded from given resource
     *
     * @param resource a string of fxml resource name ("MenuScreen.fxml")
     */
    public static void changeScene(String resource) {
        playClip("switch.wav");
        Stage primaryStage = getInstance().primaryStage;

        URL url = getInstance().getClass().getResource("/fxml/" + resource);
        if (url == null) {
            logger.error("Failed to find resource " + resource);
            System.exit(1);
        }

        if (resource == "BasicMatchScreen.fxml" || resource == "ChampionScreen.fxml") {
            throw new IllegalArgumentException("Match and Champion screen should be loaded " +
                    "through the GameManager.gameScene() with pyramid types");
        }

        Parent screen = null;
        try {
            screen = FXMLLoader.load(url);
        } catch (IOException e) {
            logger.error("Failed to load resource", e);
            System.exit(1);
        }

        screen.setOnKeyPressed(e -> chatRoomListener(e.getCode()));
        Scene scene = new Scene(screen);
        primaryStage.setScene(scene);
    }

    /**
     * Save the current window settings to system.
     */
    public static void saveWindowSettings() {
        Preferences prefs = Preferences.userNodeForPackage(GameLauncher.class);
        Stage stage = getInstance().primaryStage;
        prefs.putDouble("window_x", stage.getX());
        prefs.putDouble("window_y", stage.getY());
    }

    /**
     * Set the pref value for 24hr time
     *
     * @param set Boolean of if 24hr time or not
     */
    public void set24Hour(boolean set) {
        Preferences prefs = Preferences.userNodeForPackage(GameLauncher.class);
        prefs.putBoolean("clock_24", set);
    }

    /**
     * Get the pref value for 24hr time
     *
     * @return Boolean of if 24hr time or not
     */
    public boolean is24Hour() {
        Preferences prefs = Preferences.userNodeForPackage(GameLauncher.class);
        return prefs.getBoolean("clock_24", false);
    }

    /**
     * Replace the sound object and update pref values
     *
     * @param sound Sound to replace old sound
     */
    public void setSound(Sound sound) {
        this.sound = sound;
        Preferences prefs = Preferences.userNodeForPackage(GameLauncher.class);
        prefs.putDouble("sound_sfx_volume", sound.getSFXVolume());
        prefs.putDouble("sound_background_volume", sound.getBackgroundVolume());
        prefs.putBoolean("sound_muted", sound.isMuted());
    }

    /**
     * Get the current sound object
     *
     * @return Sound current sound object
     */
    public Sound getSound() {
        return sound;
    }

    /**
     * Initialise the sound object from system prefs
     *
     * @return Sound sound object
     */
    private Sound initSound() {
        Preferences prefs = Preferences.userNodeForPackage(GameLauncher.class);
        double sfxVolume = prefs.getDouble("sound_sfx_volume", 0.5);
        double backgroundVolume = prefs.getDouble("sound_background_volume", 0.5);
        boolean muted = prefs.getBoolean("sound_muted", false);

        return new Sound(muted, sfxVolume, backgroundVolume);
    }

    /**
     * Play a sound effect from the clip cache, it will attempt to be loaded if it wasn't found
     *
     * @param resource String sound file to be played
     */
    public static void playClip(String resource) {
        AudioClip clip = ClipCache.getClip(resource);
        Sound sound = GameManager.getInstance().getSound();
        if (clip != null && sound.getSFXVolume() > 0.05 && !sound.isMuted()) {
            clip.play(sound.getSFXVolume());
        }
    }

    /**
     * start playing background music
     *
     * @param resource String sound file to be played
     */
    public static void playBackgroundMusic(String resource) {
        AudioClip clip = ClipCache.getClip(resource);
        Sound sound = GameManager.getInstance().getSound();
        if (clip != null && sound.getBackgroundVolume() > 0.05 && !sound.isMuted() && (clip != backgroundMusic ||
                !backgroundMusic.isPlaying())) {
            backgroundMusic = clip;
            backgroundMusic.setCycleCount(AudioClip.INDEFINITE);
            backgroundMusic.play(sound.getBackgroundVolume());
        } else if (clip != backgroundMusic) {
            stopBackgroundMusic();
        }
    }

    /**
     * Stop background music
     */
    public static void stopBackgroundMusic() {
        if (backgroundMusic != null) {
            backgroundMusic.stop();
        }
    }

    /**
     * Returns the current stage.
     *
     * @return the primaryStage value
     */
    public Stage getStage() {
        return primaryStage;
    }

    /**
     * Change the scene to scene loaded from given resource with pyramid specified
     *
     * @param resource a string of fxml resource name ("MenuScreen.fxml")
     * @param userType a PyramidType item
     * @param aiType   a PyramidType item
     */
    public static void gameScene(String resource, PyramidType userType, PyramidType aiType) {
        playClip("switch.wav");
        Stage primaryStage = getInstance().primaryStage;

        // getResource() looks in src/main/resources
        URL url = getInstance().getClass().getResource("/fxml/" + resource);
        if (url == null) {
            logger.error("Failed to find resource " + resource);
            System.exit(1);
        }

        // Create loader from url
        FXMLLoader fxmlLoader = new FXMLLoader(url);

        // check if pyramidType is null
        if (userType == null || aiType == null) {
            logger.error("pyramidType cannot be null");
            return;
        }

        Parent screen = null;
        try {
            screen = fxmlLoader.load();
        } catch (IOException e) {
            logger.error("Failed to load resource", e);
            System.exit(1);
        }

        // Set the pyramid type in the class
        if (fxmlLoader.getController() instanceof ChampionScreenController) {
            ChampionScreenController controller = fxmlLoader.getController();
            controller.setPyramidTypes(userType, aiType);
        } else if (fxmlLoader.getController() instanceof BasicMatchController) {
            BasicMatchController controller = fxmlLoader.getController();
            controller.setPyramidTypes(userType, aiType);
        } else {
            logger.error("Instance was of wrong type");
            System.exit(1);
        }
        screen.setOnKeyPressed(e -> chatRoomListener(e.getCode()));
        //Display the scene
        Scene scene = new Scene(screen);
        scene.setCamera(new PerspectiveCamera());
        primaryStage.setScene(scene);
    }

    /**
     * Check if chat screen is open
     *
     * @return true/false
     */
    public boolean isChatScreenOpen() {
        return chatScreenOpen;
    }

    /**
     * toggle ChatScreen open/close
     */
    public void toggleChatScreen() {
        if (isChatScreenOpen()) {
            /* Close the chat screen */
            chatScreenOpen = false;
            chatScreenStage.close();
        } else {
            /* Open the chat screen */
            chatScreenOpen = true;

            if (chatScreenStage == null) {
                chatScreenStage = new Stage();
            }

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/ChatRoomScreen.fxml"));
            Parent chatRoomScreen = null;
            try {
                chatRoomScreen = fxmlLoader.load();
            } catch (IOException e) {
                logger.error(e.getMessage());
                System.exit(1);
            }

            chatScreenStage.setTitle("Chat Room");
            chatScreenStage.setScene(new Scene(chatRoomScreen));
            chatScreenStage.show();

            chatScreenStage.setOnCloseRequest(e -> chatScreenOpen = false);
        }
    }

    /**
     * get Chat Controller
     */
    public static ChatController getChatController() {
        if (!isUserLoggedIn()) {
            logger.error("User not logged in, cannot use chat");
            System.exit(1);
        }

        GameManager gameManager = getInstance();

        if (gameManager.chatController == null) {
            PyramidSchemeClient schemeClient = gameManager.getPyramidSchemeClient();
            MessagingClient messagingClient = gameManager.getMessagingClient();
            gameManager.chatController = new ChatController(schemeClient, messagingClient);
        }

        return gameManager.chatController;
    }

    /**
     * Get user
     *
     * @return user
     */
    public User getUser() {
        return user;
    }

    /**
     * set user as user
     *
     * @param User
     * @return user
     */
    public void setUser(User user) {
        this.user = user;
    }

    public BasicGameGenerator getGameGenerator() {
        return gameGenerator;
    }

    public PyramidSchemeClient getPyramidSchemeClient() {
        return schemeClient;
    }

    public static StatisticsTracking getStatisticsTracking() {
        return statsTracking;
    }

    public static AchievementTracking getAchieveTracking() {
        return achieveTracking;
    }

    public ChampionCache getChampionCache() {
        return championCache;
    }

    /**
     * Change the cursor image to represent the champion ability clicked
     */
    public static void setCursorAbility(Integer abilityID) {
        GameManager gameManager = GameManager.getInstance();
        String cursorImagePath = gameManager.getSelectedChamp().getAbility(abilityID).getImagePath();

        // resize the image being used to fit for the cursor
        Image image = new Image(cursorImagePath, 50, 50, false, false);

        // set the hotspot of the cursor to be in the middle of the image
        // ie. the middle of the image is the "click"
        Cursor cursor = new ImageCursor(image, 25, 25);

        gameManager.primaryStage.getScene().setCursor(cursor);
    }

    /**
     * Change the cursor image to represent the default cursor
     */
    public static void setCursorDefault() {
        GameManager gameManager = GameManager.getInstance();
        if (gameManager.primaryStage != null) {
            gameManager.primaryStage.getScene().setCursor(Cursor.DEFAULT);
        }
    }


    public Champion getSelectedChamp() {
        return selectedChamp;
    }

    public void setSelectedChamp(String champName) {
        selectedChamp = championCache.getChampion(champName);
        statsTracking.switchChampionTracking(champName);
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public static Stage getPrimaryStage() {
        return getInstance().primaryStage;
    }

    /**
     * Gets the current boss number
     *
     * @return boss
     */
    public int getBoss() {
        return this.boss;
    }

    /**
     * Sets the boss number to the given integer
     *
     * @param newBoss
     */
    public void setBoss(int newBoss) {
        this.boss = newBoss;
    }

    /**
     * Gets the current cutscene
     *
     * @return cutscene
     */
    public char getCutscene() {
        return this.cutscene;
    }

    /**
     * Sets the cutscene to the given char
     *
     * @param newCutscene
     */
    public void setCutscene(char newCutscene) {
        this.cutscene = newCutscene;
    }

    private static void chatRoomListener(KeyCode k) {

        if (isUserLoggedIn() && k == KeyCode.TAB) {
            GameManager.getInstance().toggleChatScreen();
        }
    }

    public Stage getChatScreenStage() {
        return chatScreenStage;
    }
}
