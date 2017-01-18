package uq.deco2800.pyramidscheme.statistics;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uq.deco2800.pyramidscheme.game.GameManager;
import uq.deco2800.pyramidscheme.network.LoginException;
import uq.deco2800.pyramidscheme.network.NetworkException;
import uq.deco2800.singularity.clients.pyramidscheme.PyramidSchemeClient;
import uq.deco2800.singularity.common.representations.pyramidscheme.ChampionStatistics;
import uq.deco2800.singularity.common.representations.pyramidscheme.UserStatistics;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
import java.rmi.UnexpectedException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class StatisticsTracking {

    /* Constants */
    public static final String RALLARD_NAME = "Rallard";
    public static final String QUACKUBIS_NAME = "Quackubis";
    public static final String FOWLSPHINX_NAME = "Fowl Sphinx";
    public static final String KHEPRI_NAME = "Khepri";

    public static final String GUEST_NAME = "Guest";

    /* Error messages */
    private static final String FAILED_TO_CREATE = "DEBUG: Failed to create new: ";
    private static final String FAILED_TO_PUSH = "DEBUG: Failed to push: ";
    private static final String FAILED_TO_GRAB = "DEBUG: Failed to grab: ";

    private static final String USER_STATS_MESSAGE = "userstats - ";


    /* Retry messages */
    private static final String WILL_RETRY_TO_GRAB = "DEBUG: Will retry to grab in 2 mins";
    private static final String GRAB_SUCCEEDED_BEFORE = "DEBUG: Grab succeeded before retry";

    private static final String WILL_RETRY_TO_PUSH = "DEBUG: Will retry to push in 2 mins";
    private static final String PUSH_SUCCEEDED_BEFORE = "DEBUG: Push succeeded before retry";

    private static final String NO_STATS_EXIST = "DEBUG: stats did not exist - Not recreating stats";

    //Timer DELAY
    private static final long DELAY = 120000;
    // For debuging
    private static Logger logger = LoggerFactory.getLogger(GameManager.class);
    private ScheduledExecutorService userTimer = Executors.newScheduledThreadPool(1);
    private ScheduledExecutorService currentChampTimer = Executors.newScheduledThreadPool(1);
    private ScheduledExecutorService pushTimer = Executors.newScheduledThreadPool(1);
    private Boolean trackChampTime = false;
    // Users overall stats and timers/ variables
    private UserStatistics userStats;
    private Timer userStatsPullTimer;
    private Timer userStatsPushTimer;
    private Boolean userStatsFailedToPush = false;
    private Boolean userStatsFailedToPull = false;
    // Rallard stats and timers
    private ChampionStatistics rallardStats;
    private Timer rallardStatsPullTimer;
    private Timer rallardStatsPushTimer;
    private Boolean rallardStatsFailedToPush = false;
    private Boolean rallardStatsFailedToPull = false;
    // Quackubis stats and timers
    private ChampionStatistics quackubisStats;
    private Timer quackubisStatsPullTimer;
    private Timer quackubisStatsPushTimer;
    private Boolean quackubisStatsFailedToPush = false;
    private Boolean quackubisStatsFailedToPull = false;
    // Fowlsphinx stats and timers
    private ChampionStatistics fowlSphinxStats;
    private Timer fowlSphinxStatsPullTimer;
    private Timer fowlSphinxStatsPushTimer;
    private Boolean fowlSphinxStatsFailedToPush = false;
    private Boolean fowlSphinxStatsFailedToPull = false;
    // Khepei stats and timers
    private ChampionStatistics khepriStats;
    private Timer khepriStatsPullTimer;
    private Timer khepriStatsPushTimer;
    private Boolean khepriStatsFailedToPush = false;
    private Boolean khepriStatsFailedToPull = false;
    // The champion Statistics being tracked
    private ChampionStatistics currentChamp = null;

    /**
     * Empty Constructor
     */
    public StatisticsTracking() {
        userStats = new UserStatistics();
        rallardStats = new ChampionStatistics();
        quackubisStats = new ChampionStatistics();
        fowlSphinxStats = new ChampionStatistics();
        khepriStats = new ChampionStatistics();

        userStatsPullTimer = new Timer();
        userStatsPushTimer = new Timer();

        rallardStatsPullTimer = new Timer();
        rallardStatsPushTimer = new Timer();

        quackubisStatsPullTimer = new Timer();
        quackubisStatsPushTimer = new Timer();

        fowlSphinxStatsPullTimer = new Timer();
        fowlSphinxStatsPushTimer = new Timer();

        khepriStatsPullTimer = new Timer();
        khepriStatsPushTimer = new Timer();

    }

    /**
     * Sets all statistics objects User ID
     *
     * @param userID
     */
    public void setUserID(String userID) {
        userStats.setUserID(userID);
        rallardStats.setUserID(userID);
        quackubisStats.setUserID(userID);
        fowlSphinxStats.setUserID(userID);
        khepriStats.setUserID(userID);
    }


    /**
     * Sets the champion stats back
     * before switching the current champion
     */
    public void setChampionStats() {
        switch (currentChamp.getChampName()) {
            case RALLARD_NAME:
                rallardStats = currentChamp;
                break;

            case QUACKUBIS_NAME:
                quackubisStats = currentChamp;
                break;

            case FOWLSPHINX_NAME:
                fowlSphinxStats = currentChamp;
                break;

            case KHEPRI_NAME:
                khepriStats = currentChamp;
                break;

            default:
                logger.info("DEBUG: Champ name is invalid: " + currentChamp.getChampName());
        }
    }


    /**
     * Switches the champion being tracked to the provided name
     *
     * @param champName The name of the champion to be tracked
     * @throws UnexpectedException
     */
    public void switchChampionTracking(String champName) {
        switch (champName) {
            case RALLARD_NAME:
                setChampionStats();
                currentChamp = rallardStats;
                break;

            case QUACKUBIS_NAME:
                setChampionStats();
                currentChamp = quackubisStats;
                break;

            case FOWLSPHINX_NAME:
                setChampionStats();
                currentChamp = fowlSphinxStats;
                break;

            case KHEPRI_NAME:
                setChampionStats();
                currentChamp = khepriStats;
                break;

            default:
                logger.info("DEBUG: Champ name is invalid: " + champName);
        }
    }

    /**
     * Adds to the minions played of both the userStats and the current champion
     *
     * @param value to be added
     */
    public void addToMinionsPlayed(String value) {
        userStats.addToMinionsPlayed(value);
        currentChamp.addToMinionsPlayed(value);
        logger.info("DEBUG: Added to Minions played: " + value);
    }

    /**
     * Adds to the minions played of both the userStats and the current champion
     *
     * @param value to be added
     */
    public void addToMinionsPlayed(Integer value) {
        userStats.addToMinionsPlayed(value);
        currentChamp.addToMinionsPlayed(value);
        logger.info("DEBUG: Added to Minions played: " + value.toString());
    }

    /**
     * Adds to the minions lost of both the userStats and the current champion
     *
     * @param value to be added
     */
    public void addToMinionsLost(String value) {
        userStats.addToMinionsLost(value);
        currentChamp.addToMinionsLost(value);
        logger.info("DEBUG: Added to Minions lost: " + value);
    }

    /**
     * Adds to the minions lost of both the userStats and the current champion
     *
     * @param value to be added
     */
    public void addToMinionsLost(Integer value) {
        userStats.addToMinionsLost(value);
        currentChamp.addToMinionsLost(value);
        logger.info("DEBUG: Added to Minions lost: " + value.toString());
    }

    /**
     * Adds to the minions killed of both the userStats and the current champion
     *
     * @param value to be added
     */
    public void addToMinionsKilled(String value) {
        userStats.addToMinionsKilled(value);
        currentChamp.addToMinionsKilled(value);
        logger.info("DEBUG: Added to Minions killed: " + value);
    }

    /**
     * Adds to the minions killed of both the userStats and the current champion
     *
     * @param value to be added
     */
    public void addToMinionsKilled(Integer value) {
        userStats.addToMinionsKilled(value);
        currentChamp.addToMinionsKilled(value);
        logger.info("DEBUG: Added to Minions killed: " + value.toString());
    }

    /**
     * Adds to the health lost of both the userStats and the current champion
     *
     * @param value to be added
     */
    public void addToHealthLost(String value) {
        userStats.addToHealthLost(value);
        currentChamp.addToHealthLost(value);
        logger.info("DEBUG: Added to Health lost: " + value);
    }

    /**
     * Adds to the health lost of both the userStats and the current champion
     *
     * @param value to be added
     */
    public void addToHealthLost(Integer value) {
        userStats.addToHealthLost(value);
        currentChamp.addToHealthLost(value);
        logger.info("DEBUG: Added to Health lost: " + value.toString());
    }

    /**
     * Adds to the health taken of both the userStats and the current champion
     *
     * @param value to be added
     */
    public void addToHealthTaken(String value) {
        userStats.addToHealthTaken(value);
        currentChamp.addToHealthTaken(value);
        logger.info("DEBUG: Added to Health Taken: " + value);
    }

    /**
     * Adds to the health taken of both the userStats and the current champion
     *
     * @param value to be added
     */
    public void addToHealthTaken(Integer value) {
        userStats.addToHealthTaken(value);
        currentChamp.addToHealthTaken(value);
        logger.info("DEBUG: Added to Health Taken: " + value.toString());
    }

    /**
     * Adds to the total wins of both the userStats and the current champion
     *
     * @param value to be added
     */
    public void addToTotalWins(String value) {
        userStats.addToTotalWins(value);
        currentChamp.addToTotalWins(value);
        logger.info("DEBUG: Added to total wins: " + value);
    }

    /**
     * Adds to the total wins of both the userStats and the current champion
     *
     * @param value to be added
     */
    public void addToTotalWins(Integer value) {
        userStats.addToTotalWins(value);
        currentChamp.addToTotalWins(value);
        logger.info("DEBUG: Added to total wins: " + value.toString());
    }

    /**
     * Adds to the total losses of both the userStats and the current champion
     *
     * @param value to be added
     */
    public void addToTotalLosses(String value) {
        userStats.addToTotalLosses(value);
        currentChamp.addToTotalLosses(value);
        logger.info("DEBUG: Added to total losses: " + value);
    }

    /**
     * Adds to the total losses of both the userStats and the current champion
     *
     * @param value to be added
     */
    public void addToTotalLosses(Integer value) {
        userStats.addToTotalLosses(value);
        currentChamp.addToTotalLosses(value);
        logger.info("DEBUG: Added to total losses: " + value.toString());
    }

    /**
     * Adds to the users level
     *
     * @param value
     */
    public void addToUserLevel(String value) {
        userStats.addToUserLevel(value);
        logger.info("DEBUG: Added to user levle: " + value);
    }

    /**
     * Adds to the users level
     *
     * @param value
     */
    public void addToUserLevel(Integer value) {
        userStats.addToUserLevel(value);
        logger.info("DEBUG: Added to user levle: " + value.toString());
    }

    /**
     * Gets Rallard stats
     *
     * @return Rallards stats
     */
    public ChampionStatistics getRallardStats() {
        setChampionStats();
        return rallardStats;
    }

    /**
     * Sets a champions stats
     *
     * @param champStats
     */
    public void setRallardStats(ChampionStatistics champStats) {
        if (currentChamp != null && currentChamp.getChampName() == RALLARD_NAME) {
            currentChamp = champStats;
        }
        this.rallardStats = champStats;
    }

    /**
     * Gets Quackubis Stats
     *
     * @return Quackubis Stats
     */
    public ChampionStatistics getQuackubisStats() {
        setChampionStats();
        return quackubisStats;
    }

    /**
     * Sets a champions stats
     *
     * @param champStats
     */
    public void setQuackubisStats(ChampionStatistics champStats) {
        if (currentChamp != null && currentChamp.getChampName() == QUACKUBIS_NAME) {
            currentChamp = champStats;
        }
        this.quackubisStats = champStats;
    }

    /**
     * Gets fowl sphinxs stats
     *
     * @return fowlSphinx Stats
     */
    public ChampionStatistics getFowlSphinxStats() {
        setChampionStats();
        return fowlSphinxStats;
    }

    /**
     * Sets a champions stats
     *
     * @param champStats
     */
    public void setFowlSphinxStats(ChampionStatistics champStats) {
        if (currentChamp != null && currentChamp.getChampName() == FOWLSPHINX_NAME) {
            currentChamp = champStats;
        }
        this.fowlSphinxStats = champStats;
    }

    /**
     * Gets khepri stats
     *
     * @return khepri stats
     */
    public ChampionStatistics getKhepriStats() {
        setChampionStats();
        return khepriStats;
    }

    /**
     * Sets a champions stats
     *
     * @param champStats
     */
    public void setKhepriStats(ChampionStatistics champStats) {
        if (currentChamp != null && currentChamp.getChampName() == KHEPRI_NAME) {
            currentChamp = champStats;
        }
        this.khepriStats = champStats;
    }

    /**
     * Gets the users overall stats
     *
     * @return users stats
     */
    public UserStatistics getUserStats() {
        return userStats;
    }

    /**
     * Sets the user stats
     *
     * @param userStats
     */
    public void setUserStats(UserStatistics userStats) {
        this.userStats = userStats;
    }

    /**
     * Gets the current champ stats
     *
     * @return current champ
     */
    public ChampionStatistics getCurrentChamp() {
        return currentChamp;
    }

    /**
     * Creates a new empty user stats
     */
    private void createEmptyUserStats() {
        String value = "0";

        userStats.setUserLevel("1");
        userStats.setMinionsPlayed(value);
        userStats.setMinionsLost(value);
        userStats.setMinionsKilled(value);
        userStats.setHealthLost(value);
        userStats.setHealthTaken(value);
        userStats.setTotalWins(value);
        userStats.setTotalLosses(value);
        userStats.setTotalHours(value);
        userStats.setTotalMinutes(value);
    }

    /**
     * Creates a new empty rallard stats
     */
    private void createEmptyRallardStats() {
        String value = "0";
        createEmptyChampionStats(rallardStats);
        rallardStats.setChampName(RALLARD_NAME);
    }

    /**
     * Creates a new empty quackubis stats
     */
    private void createEmptyQuackubisStats() {
        String value = "0";
        createEmptyChampionStats(quackubisStats);
        quackubisStats.setChampName(QUACKUBIS_NAME);
    }

    /**
     * Creates a new empty fowlSphinx stats
     */
    private void createEmptyFowlSphinxStats() {
        String value = "0";
        createEmptyChampionStats(fowlSphinxStats);
        fowlSphinxStats.setChampName(FOWLSPHINX_NAME);
    }

    /**
     * Creates a new empty khepri stats
     */
    private void createEmptyKhepriStats() {
        String value = "0";
        createEmptyChampionStats(khepriStats);
        khepriStats.setChampName(KHEPRI_NAME);
    }

    /**
     * Creates a new empty ChampionStatistics.
     *
     * @param championStats the ChampionStatistics to be set to
     */
    private void createEmptyChampionStats(ChampionStatistics championStats) {
        String value = "0";
        championStats.setMinionsPlayed(value);
        championStats.setMinionsLost(value);
        championStats.setMinionsKilled(value);
        championStats.setHealthLost(value);
        championStats.setHealthTaken(value);
        championStats.setTotalWins(value);
        championStats.setTotalLosses(value);
        championStats.setTotalHours(value);
        championStats.setTotalMinutes(value);
    }

    /**
     * Sets all the stats to a new (empty) state
     */
    public void createEmptyStats() {
        createEmptyUserStats();
        createEmptyRallardStats();
        createEmptyQuackubisStats();
        createEmptyFowlSphinxStats();
        createEmptyKhepriStats();
    }

    /**
     * Creates a guest user
     */
    public void createGuestUser() {
        createEmptyStats();
        setUserID(GUEST_NAME);
        currentChamp = rallardStats;
    }

    /**
     * Creates a new set of user stats and
     * uploads them to the server (only tries once)
     */
    public void createNewUser(String username, String password) {
        GameManager gameManager = GameManager.getInstance();
        PyramidSchemeClient schemeClient = gameManager.getPyramidSchemeClient();

        createEmptyStats();

        try {
            GameManager.loginUser(username, password);
        } catch (NetworkException | LoginException e) {
            logger.info(FAILED_TO_CREATE + "Any stats - " + e);
            return;
        }

        setUserID(schemeClient.getUserInformationByUserName(username).getUserId());

        try {
            schemeClient.createUserStats(userStats);
        } catch (WebApplicationException | JsonProcessingException | ProcessingException e) {
            logger.info(FAILED_TO_CREATE + USER_STATS_MESSAGE + userStats + e);
        }

        try {
            schemeClient.createChampStats(rallardStats);
        } catch (WebApplicationException | JsonProcessingException | ProcessingException e) {
            logger.info(FAILED_TO_CREATE + "rallardStats - " + rallardStats + e);
        }

        try {
            schemeClient.createChampStats(quackubisStats);
        } catch (WebApplicationException | JsonProcessingException | ProcessingException e) {
            logger.info(FAILED_TO_CREATE + "quackubisStats - " + quackubisStats + e);
        }

        try {
            schemeClient.createChampStats(fowlSphinxStats);
        } catch (WebApplicationException | JsonProcessingException | ProcessingException e) {
            logger.info(FAILED_TO_CREATE + "fowlSphinxStats - " + fowlSphinxStats + e);
        }

        try {
            schemeClient.createChampStats(khepriStats);
        } catch (WebApplicationException | JsonProcessingException | ProcessingException e) {
            logger.info(FAILED_TO_CREATE + "khepriStats - " + khepriStats + e);
        }

    }

    /**
     * Retries to push user stats
     * if a push was successful before the retry occurred
     * it will do nothing
     *
     * @param username
     */
    private void pushUserStatsRetry(String username) {
        GameManager gameManager = GameManager.getInstance();
        PyramidSchemeClient schemeClient = gameManager.getPyramidSchemeClient();

        if (userStatsFailedToPush) {
            try {
                schemeClient.createUserStats(userStats);
                userStatsFailedToPush = false;
            } catch (Exception e) {
                logger.info(FAILED_TO_PUSH + USER_STATS_MESSAGE + userStats + e);
                logger.info(WILL_RETRY_TO_PUSH);
                userStatsPushTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        pushUserStatsRetry(username);
                    }
                }, DELAY);
                return;
            }
        } else {
            logger.info(PUSH_SUCCEEDED_BEFORE);
        }
    }

    /**
     * Pushes the user stats
     *
     * @param username
     */
    private void pushUserStats(String username) {

        GameManager gameManager = GameManager.getInstance();
        PyramidSchemeClient schemeClient = gameManager.getPyramidSchemeClient();

        if (GUEST_NAME.equals(userStats.getUserID())) {
            return;
        }

        try {
            schemeClient.createUserStats(userStats);
            userStatsFailedToPush = false;
        } catch (Exception e) {

            logger.info(FAILED_TO_PUSH + USER_STATS_MESSAGE + userStats);
            logger.info(WILL_RETRY_TO_PUSH);
            userStatsFailedToPush = true;

            userStatsPushTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    pushUserStatsRetry(username);
                }
            }, DELAY);
            return;
        }
    }

    /**
     * Creates a new empty user stats and pushes it
     *
     * @param username
     */
    private void createUserStats(String username) {
        GameManager gameManager = GameManager.getInstance();
        PyramidSchemeClient schemeClient = gameManager.getPyramidSchemeClient();

        this.createEmptyUserStats();

        this.setUserID(schemeClient.getUserInformationByUserName(username).getUserId());

        this.pushUserStats(username);
    }

    /**
     * Retries to push rallard stats
     * if a push was successful before the retry occurred
     * it will do nothing
     *
     * @param rallardname
     */
    private void pushRallardStatsRetry(String username) {
        GameManager gameManager = GameManager.getInstance();
        PyramidSchemeClient schemeClient = gameManager.getPyramidSchemeClient();

        if (rallardStatsFailedToPush) {
            try {
                schemeClient.createChampStats(rallardStats);
                rallardStatsFailedToPush = false;
            } catch (Exception e) {
                logger.info(FAILED_TO_PUSH + "rallardstats - " + rallardStats + e);
                logger.info(WILL_RETRY_TO_PUSH);
                rallardStatsPushTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        pushRallardStatsRetry(username);
                    }
                }, DELAY);
                return;
            }
        } else {
            logger.info(PUSH_SUCCEEDED_BEFORE);
        }
    }

    /**
     * Pushes the rallard stats
     *
     * @param rallardname
     */
    private void pushRallardStats(String username) {

        GameManager gameManager = GameManager.getInstance();
        PyramidSchemeClient schemeClient = gameManager.getPyramidSchemeClient();

        if (GUEST_NAME.equals(rallardStats.getUserID())) {
            return;
        }

        try {
            schemeClient.createChampStats(rallardStats);
            rallardStatsFailedToPush = false;
        } catch (Exception e) {

            logger.info(FAILED_TO_PUSH + "rallardstats - " + rallardStats + e);
            logger.info(WILL_RETRY_TO_PUSH);
            rallardStatsFailedToPush = true;

            rallardStatsPushTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    pushRallardStatsRetry(username);
                }
            }, DELAY);
            return;
        }
    }

    /**
     * Creates a new empty rallard stats and pushes it
     *
     * @param rallardname
     */
    private void createRallardStats(String username) {
        GameManager gameManager = GameManager.getInstance();
        PyramidSchemeClient schemeClient = gameManager.getPyramidSchemeClient();

        this.createEmptyRallardStats();

        this.setUserID(schemeClient.getUserInformationByUserName(username).getUserId());

        this.pushRallardStats(username);
    }

    /**
     * Retries to push quackubis stats
     * if a push was successful before the retry occurred
     * it will do nothing
     *
     * @param quackubisname
     */
    private void pushQuackubisStatsRetry(String username) {
        GameManager gameManager = GameManager.getInstance();
        PyramidSchemeClient schemeClient = gameManager.getPyramidSchemeClient();

        if (quackubisStatsFailedToPush) {
            try {
                schemeClient.createChampStats(quackubisStats);
                quackubisStatsFailedToPush = false;
            } catch (Exception e) {
                logger.info(FAILED_TO_PUSH + "quackubisstats - " + quackubisStats + e);
                logger.info(WILL_RETRY_TO_PUSH);
                quackubisStatsPushTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        pushQuackubisStatsRetry(username);
                    }
                }, DELAY);
                return;
            }
        } else {
            logger.info(PUSH_SUCCEEDED_BEFORE);
        }
    }

    /**
     * Pushes the quackubis stats
     *
     * @param quackubisname
     */
    private void pushQuackubisStats(String username) {

        GameManager gameManager = GameManager.getInstance();
        PyramidSchemeClient schemeClient = gameManager.getPyramidSchemeClient();

        if (quackubisStats.getUserID().equals(GUEST_NAME)) {
            return;
        }

        try {
            schemeClient.createChampStats(quackubisStats);
            quackubisStatsFailedToPush = false;
        } catch (Exception e) {

            logger.info(FAILED_TO_PUSH + "quackubisstats - " + quackubisStats + e);
            logger.info(WILL_RETRY_TO_PUSH);
            quackubisStatsFailedToPush = true;

            quackubisStatsPushTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    pushQuackubisStatsRetry(username);
                }
            }, DELAY);
            return;
        }
    }

    /**
     * Creates a new empty quackubis stats and pushes it
     *
     * @param quackubisname
     */
    private void createQuackubisStats(String username) {
        GameManager gameManager = GameManager.getInstance();
        PyramidSchemeClient schemeClient = gameManager.getPyramidSchemeClient();

        this.createEmptyQuackubisStats();

        this.setUserID(schemeClient.getUserInformationByUserName(username).getUserId());

        this.pushQuackubisStats(username);
    }

    /**
     * Retries to push fowlSphinx stats
     * if a push was successful before the retry occurred
     * it will do nothing
     *
     * @param fowlSphinxname
     */
    private void pushFowlSphinxStatsRetry(String username) {
        GameManager gameManager = GameManager.getInstance();
        PyramidSchemeClient schemeClient = gameManager.getPyramidSchemeClient();

        if (fowlSphinxStatsFailedToPush) {
            try {
                schemeClient.createChampStats(fowlSphinxStats);
                fowlSphinxStatsFailedToPush = false;
            } catch (Exception e) {
                logger.info(FAILED_TO_PUSH + "fowlSphinxstats - " + fowlSphinxStats + e);
                logger.info(WILL_RETRY_TO_PUSH);
                fowlSphinxStatsPushTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        pushFowlSphinxStatsRetry(username);
                    }
                }, DELAY);
                return;
            }
        } else {
            logger.info(PUSH_SUCCEEDED_BEFORE);
        }
    }

    /**
     * Pushes the fowlSphinx stats
     *
     * @param fowlSphinxname
     */
    private void pushFowlSphinxStats(String username) {

        GameManager gameManager = GameManager.getInstance();
        PyramidSchemeClient schemeClient = gameManager.getPyramidSchemeClient();

        if (GUEST_NAME.equals(fowlSphinxStats.getUserID())) {
            return;
        }

        try {
            schemeClient.createChampStats(fowlSphinxStats);
            fowlSphinxStatsFailedToPush = false;
        } catch (Exception e) {

            logger.info(FAILED_TO_PUSH + "fowlSphinxstats - " + fowlSphinxStats + e);
            logger.info(WILL_RETRY_TO_PUSH);
            fowlSphinxStatsFailedToPush = true;

            fowlSphinxStatsPushTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    pushFowlSphinxStatsRetry(username);
                }
            }, DELAY);
            return;
        }
    }

    /**
     * Creates a new empty fowlSphinx stats and pushes it
     *
     * @param fowlSphinxname
     */
    private void createFowlSphinxStats(String username) {
        GameManager gameManager = GameManager.getInstance();
        PyramidSchemeClient schemeClient = gameManager.getPyramidSchemeClient();

        this.createEmptyFowlSphinxStats();

        this.setUserID(schemeClient.getUserInformationByUserName(username).getUserId());

        this.pushFowlSphinxStats(username);
    }

    /**
     * Retries to push khepri stats
     * if a push was successful before the retry occurred
     * it will do nothing
     *
     * @param khepriname
     */
    private void pushKhepriStatsRetry(String username) {
        GameManager gameManager = GameManager.getInstance();
        PyramidSchemeClient schemeClient = gameManager.getPyramidSchemeClient();

        if (khepriStatsFailedToPush) {
            try {
                schemeClient.createChampStats(khepriStats);
                khepriStatsFailedToPush = false;
            } catch (Exception e) {
                logger.info(FAILED_TO_PUSH + "khepristats - " + khepriStats + e);
                logger.info(WILL_RETRY_TO_PUSH);
                khepriStatsPushTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        pushKhepriStatsRetry(username);
                    }
                }, DELAY);
                return;
            }
        } else {
            logger.info(PUSH_SUCCEEDED_BEFORE);
        }
    }

    /**
     * Pushes the khepri stats
     *
     * @param khepriname
     */
    private void pushkhepriStats(String username) {

        GameManager gameManager = GameManager.getInstance();
        PyramidSchemeClient schemeClient = gameManager.getPyramidSchemeClient();

        if (GUEST_NAME.equals(khepriStats.getUserID())) {
            return;
        }

        try {
            schemeClient.createChampStats(khepriStats);
            khepriStatsFailedToPush = false;
        } catch (Exception e) {

            logger.info(FAILED_TO_PUSH + "khepristats - " + khepriStats + e);
            logger.info(WILL_RETRY_TO_PUSH);
            khepriStatsFailedToPush = true;

            khepriStatsPushTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    pushKhepriStatsRetry(username);
                }
            }, DELAY);
            return;
        }
    }

    /**
     * Creates a new empty khepri stats and pushes it
     *
     * @param khepriname
     */
    private void createKhepriStats(String username) {
        GameManager gameManager = GameManager.getInstance();
        PyramidSchemeClient schemeClient = gameManager.getPyramidSchemeClient();

        this.createEmptyKhepriStats();

        this.setUserID(schemeClient.getUserInformationByUserName(username).getUserId());

        this.pushkhepriStats(username);
    }

    /**
     * Retrys to grab the user stats
     * Only occurs after a failed pull
     *
     * @param username
     */
    private void grabUserStatsRetry(String username) {
        GameManager gameManager = GameManager.getInstance();
        PyramidSchemeClient schemeClient = gameManager.getPyramidSchemeClient();

        UserStatistics grabbedUserStats;

        if (userStatsFailedToPull) {
            try {
                grabbedUserStats = schemeClient.getCurrentStatistics(schemeClient.getUserInformationByUserName(username).getUserId());
                userStatsFailedToPull = false;
            } catch (Exception e) {
                this.createEmptyUserStats();
                userStatsFailedToPull = true;

                logger.info(FAILED_TO_GRAB + e);
                logger.info(WILL_RETRY_TO_GRAB);
                userStatsPullTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        grabUserStatsRetry(username);
                    }
                }, DELAY);
                return;
            }

            if (grabbedUserStats == null) {
                logger.info(NO_STATS_EXIST);
            } else {
                grabbedUserStats.setLastLogin();
                logger.info("DEBUG: Grabbed userstats - " + grabbedUserStats);
                userStats.mergeStatistics(grabbedUserStats);
            }
        } else {
            logger.info(GRAB_SUCCEEDED_BEFORE);
        }
    }


    /**
     * Pulls the latest user stats from the database
     *
     * @param username
     */
    public void grabUserStats(String username) {
        GameManager gameManager = GameManager.getInstance();
        PyramidSchemeClient schemeClient = gameManager.getPyramidSchemeClient();

        UserStatistics grabbedUserStats;

        try {
            grabbedUserStats = schemeClient.getCurrentStatistics(schemeClient.getUserInformationByUserName(username).getUserId());
            userStatsFailedToPull = false;
        } catch (Exception e) {
            this.createEmptyUserStats();
            userStatsFailedToPull = true;

            logger.info(FAILED_TO_GRAB + e);
            logger.info(WILL_RETRY_TO_GRAB);
            userStatsPullTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    grabUserStatsRetry(username);
                }
            }, DELAY);
            return;
        }

        if (grabbedUserStats == null) {
            logger.info("DEBUG: Userstats did not exist, now creating stats");
            this.createUserStats(username);
        } else {
            grabbedUserStats.setLastLogin();
            logger.info("DEBUG: Grabbed userstats - " + grabbedUserStats);
            this.setUserStats(grabbedUserStats);
        }
    }

    /**
     * Retrys to grab the rallard stats
     * Only occurs after a failed pull
     *
     * @param username
     */
    private void grabRallardStatsRetry(String username) {
        GameManager gameManager = GameManager.getInstance();
        PyramidSchemeClient schemeClient = gameManager.getPyramidSchemeClient();

        ChampionStatistics champStats;

        if (rallardStatsFailedToPull) {
            try {
                champStats = schemeClient.getCurrentChampStatistics(
                        schemeClient.getUserInformationByUserName(username).getUserId(), RALLARD_NAME);
                rallardStatsFailedToPull = false;

            } catch (Exception e) {
                this.createEmptyUserStats();
                rallardStatsFailedToPull = true;

                logger.info(FAILED_TO_GRAB + e);
                logger.info(WILL_RETRY_TO_GRAB);
                rallardStatsPullTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        grabRallardStatsRetry(username);
                    }
                }, DELAY);
                return;
            }

            if (champStats == null) {
                logger.info("DEBUG: Champstats did not exist - Not recreating stats");
            } else {
                champStats.setLastLogin();
                logger.info("DEBUG: Grabbed champstats - " + champStats);

                this.rallardStats.mergeStatistics(champStats);
            }
        } else {
            logger.info(GRAB_SUCCEEDED_BEFORE);
        }
    }


    /**
     * Pulls the latest rallard stats from the database
     *
     * @param username
     */
    public void grabRallardStats(String username) {
        GameManager gameManager = GameManager.getInstance();
        PyramidSchemeClient schemeClient = gameManager.getPyramidSchemeClient();

        ChampionStatistics champStats;

        try {
            champStats = schemeClient.getCurrentChampStatistics(
                    schemeClient.getUserInformationByUserName(username).getUserId(), RALLARD_NAME);
            rallardStatsFailedToPull = false;
        } catch (Exception e) {
            this.createEmptyUserStats();
            rallardStatsFailedToPull = true;

            logger.info(FAILED_TO_GRAB + e);
            logger.info(WILL_RETRY_TO_GRAB);
            rallardStatsPullTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    grabRallardStatsRetry(username);
                }
            }, DELAY);
            return;
        }

        if (champStats == null) {
            logger.info("DEBUG: Rallard stats did not exist, now creating stats");
            this.createRallardStats(username);
        } else {
            champStats.setLastLogin();
            logger.info("DEBUG: Grabbed rallard stats - " + champStats);
            this.setRallardStats(champStats);
        }
    }

    /**
     * Retrys to grab the quackubis stats
     * Only occurs after a failed pull
     *
     * @param username
     */
    private void grabQuackubisStatsRetry(String username) {
        GameManager gameManager = GameManager.getInstance();
        PyramidSchemeClient schemeClient = gameManager.getPyramidSchemeClient();

        ChampionStatistics champStats;

        if (quackubisStatsFailedToPull) {
            try {
                champStats = schemeClient.getCurrentChampStatistics(
                        schemeClient.getUserInformationByUserName(username).getUserId(), QUACKUBIS_NAME);
                quackubisStatsFailedToPull = false;

            } catch (Exception e) {
                this.createEmptyUserStats();
                quackubisStatsFailedToPull = true;

                logger.info(FAILED_TO_GRAB + e);
                logger.info(WILL_RETRY_TO_GRAB);
                quackubisStatsPullTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        grabQuackubisStatsRetry(username);
                    }
                }, DELAY);
                return;
            }

            if (champStats == null) {
                logger.info("DEBUG: Champstats did not exist - Not recreating stats");
            } else {
                champStats.setLastLogin();
                logger.info("DEBUG: Grabbed champstats - " + champStats);

                this.quackubisStats.mergeStatistics(champStats);
            }
        } else {
            logger.info(GRAB_SUCCEEDED_BEFORE);
        }
    }


    /**
     * Pulls the latest quackubis stats from the database
     *
     * @param username
     */
    public void grabQuackubisStats(String username) {
        GameManager gameManager = GameManager.getInstance();
        PyramidSchemeClient schemeClient = gameManager.getPyramidSchemeClient();

        ChampionStatistics champStats;

        try {
            champStats = schemeClient.getCurrentChampStatistics(
                    schemeClient.getUserInformationByUserName(username).getUserId(), QUACKUBIS_NAME);
            quackubisStatsFailedToPull = false;
        } catch (Exception e) {
            this.createEmptyUserStats();
            quackubisStatsFailedToPull = true;

            logger.info(FAILED_TO_GRAB + e);
            logger.info(WILL_RETRY_TO_GRAB);
            quackubisStatsPullTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    grabQuackubisStatsRetry(username);
                }
            }, DELAY);
            return;
        }

        if (champStats == null) {
            logger.info("DEBUG: quackubis stats did not exist, now creating stats");
            this.createQuackubisStats(username);
        } else {
            champStats.setLastLogin();
            logger.info("DEBUG: Grabbed quackubis stats - " + champStats);
            this.setQuackubisStats(champStats);
        }
    }

    /**
     * Retrys to grab the fowlSphinx stats
     * Only occurs after a failed pull
     *
     * @param username
     */
    private void grabFowlSphinxStatsRetry(String username) {
        GameManager gameManager = GameManager.getInstance();
        PyramidSchemeClient schemeClient = gameManager.getPyramidSchemeClient();

        ChampionStatistics champStats;

        if (fowlSphinxStatsFailedToPull) {
            try {
                champStats = schemeClient.getCurrentChampStatistics(
                        schemeClient.getUserInformationByUserName(username).getUserId(), FOWLSPHINX_NAME);
                fowlSphinxStatsFailedToPull = false;

            } catch (Exception e) {
                this.createEmptyUserStats();
                fowlSphinxStatsFailedToPull = true;

                logger.info(FAILED_TO_GRAB + e);
                logger.info(WILL_RETRY_TO_GRAB);
                fowlSphinxStatsPullTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        grabFowlSphinxStatsRetry(username);
                    }
                }, DELAY);
                return;
            }

            if (champStats == null) {
                logger.info("DEBUG: Champstats did not exist - Not recreating stats");
            } else {
                champStats.setLastLogin();
                logger.info("DEBUG: Grabbed champstats - " + champStats);

                this.fowlSphinxStats.mergeStatistics(champStats);
            }
        } else {
            logger.info(GRAB_SUCCEEDED_BEFORE);
        }
    }


    /**
     * Pulls the latest fowlSphinx stats from the database
     *
     * @param username
     */
    public void grabFowlSphinxStats(String username) {
        GameManager gameManager = GameManager.getInstance();
        PyramidSchemeClient schemeClient = gameManager.getPyramidSchemeClient();

        ChampionStatistics champStats;

        try {
            champStats = schemeClient.getCurrentChampStatistics(
                    schemeClient.getUserInformationByUserName(username).getUserId(), FOWLSPHINX_NAME);
            fowlSphinxStatsFailedToPull = false;
        } catch (Exception e) {
            this.createEmptyUserStats();
            fowlSphinxStatsFailedToPull = true;

            logger.info(FAILED_TO_GRAB + e);
            logger.info(WILL_RETRY_TO_GRAB);
            fowlSphinxStatsPullTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    grabFowlSphinxStatsRetry(username);
                }
            }, DELAY);
            return;
        }

        if (champStats == null) {
            logger.info("DEBUG: fowlSphinx stats did not exist, now creating stats");
            this.createFowlSphinxStats(username);
        } else {
            champStats.setLastLogin();
            logger.info("DEBUG: Grabbed fowlSphinx stats - " + champStats);
            this.setFowlSphinxStats(champStats);
        }
    }

    /**
     * Retrys to grab the khepri stats
     * Only occurs after a failed pull
     *
     * @param username
     */
    private void grabKhepriStatsRetry(String username) {
        GameManager gameManager = GameManager.getInstance();
        PyramidSchemeClient schemeClient = gameManager.getPyramidSchemeClient();

        ChampionStatistics champStats;

        if (khepriStatsFailedToPull) {
            try {
                champStats = schemeClient.getCurrentChampStatistics(
                        schemeClient.getUserInformationByUserName(username).getUserId(), KHEPRI_NAME);
                khepriStatsFailedToPull = false;

            } catch (Exception e) {
                this.createEmptyUserStats();
                khepriStatsFailedToPull = true;

                logger.info(FAILED_TO_GRAB + e);
                logger.info(WILL_RETRY_TO_GRAB);
                khepriStatsPullTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        grabKhepriStatsRetry(username);
                    }
                }, DELAY);
                return;
            }

            if (champStats == null) {
                logger.info("DEBUG: Champstats did not exist - Not recreating stats");
            } else {
                champStats.setLastLogin();
                logger.info("DEBUG: Grabbed champstats - " + champStats);

                this.khepriStats.mergeStatistics(champStats);
            }
        } else {
            logger.info(GRAB_SUCCEEDED_BEFORE);
        }
    }


    /**
     * Pulls the latest khepri stats from the database
     *
     * @param username
     */
    public void grabKhepriStats(String username) {
        GameManager gameManager = GameManager.getInstance();
        PyramidSchemeClient schemeClient = gameManager.getPyramidSchemeClient();

        ChampionStatistics champStats;

        try {
            champStats = schemeClient.getCurrentChampStatistics(
                    schemeClient.getUserInformationByUserName(username).getUserId(), KHEPRI_NAME);
            khepriStatsFailedToPull = false;
        } catch (Exception e) {
            this.createEmptyUserStats();
            khepriStatsFailedToPull = true;

            logger.info(FAILED_TO_GRAB + e);
            logger.info(WILL_RETRY_TO_GRAB);
            khepriStatsPullTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    grabKhepriStatsRetry(username);
                }
            }, DELAY);
            return;
        }

        if (champStats == null) {
            logger.info("DEBUG: khepri stats did not exist, now creating stats");
            this.createKhepriStats(username);
        } else {
            champStats.setLastLogin();
            logger.info("DEBUG: Grabbed khepri stats - " + champStats);
            this.setKhepriStats(champStats);
        }
    }

    /**
     * Grabs all statistics
     *
     * @param username
     */
    public void grabAllStats(String username) {
        grabUserStats(username);
        grabRallardStats(username);
        grabQuackubisStats(username);
        grabFowlSphinxStats(username);
        grabKhepriStats(username);

        currentChamp = rallardStats;
    }

    /**
     * Pushes the current stats that are being changed
     * to the singularity server
     */
    public void pushCurrentStats() {
        String username;

        GameManager gameManager = GameManager.getInstance();
        username = gameManager.getUser().getName();

        pushUserStats(username);

        setChampionStats();
        switch (currentChamp.getChampName()) {
            case RALLARD_NAME:
                pushRallardStats(username);
                break;

            case QUACKUBIS_NAME:
                pushQuackubisStats(username);
                break;

            case FOWLSPHINX_NAME:
                pushFowlSphinxStats(username);
                break;

            case KHEPRI_NAME:
                pushkhepriStats(username);
                break;

            default:
                logger.info("DEBUG: ChampName doesn't match existing name" + currentChamp.getChampName());
        }
    }


    /**
     * Starts tracking the play time for the user
     */
    public void startUserPlayTimeTracking() {

        Runnable run = new Runnable() {
            @Override
            public void run() {
                userStats.addToTotalMinutes(1);
                if (Integer.parseInt(userStats.getTotalMinutes()) >= 60) {
                    userStats.addToTotalHours(1);
                    userStats.setTotalMinutes("0");
                }
            }
        };

        userTimer.scheduleAtFixedRate(run, 1, 1, TimeUnit.MINUTES);
    }

    public void champPlayTimeTracking() {

        Runnable run = new Runnable() {
            @Override
            public void run() {
                champPlayTimeTracking();
            }
        };

        if (trackChampTime) {
            currentChampTimer.schedule(run, 1, TimeUnit.MINUTES);
            currentChamp.addToTotalMinutes(1);
            if (Integer.parseInt(currentChamp.getTotalMinutes()) >= 60) {
                currentChamp.addToTotalHours(1);
                currentChamp.setTotalMinutes("0");
            }
        }
    }

    /**
     * Starts tracking the play time for the current champ
     */
    public void startChampPlayTimeTracking() {
        trackChampTime = true;

        Runnable run = new Runnable() {
            @Override
            public void run() {
                champPlayTimeTracking();
            }
        };

        currentChampTimer.schedule(run, 1, TimeUnit.MINUTES);
    }

    /**
     * Stops the current champs timer from running
     */
    public void stopChampPlayTimeTracking() {
        trackChampTime = false;
    }

    /**
     * Starts a timer to push statistics every 5mins
     */
    public void startPushTimer() {

        Runnable run = new Runnable() {
            @Override
            public void run() {
                pushCurrentStats();
            }
        };

        pushTimer.scheduleAtFixedRate(run, 5, 5, TimeUnit.MINUTES);
    }
}
