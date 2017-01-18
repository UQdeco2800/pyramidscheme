package uq.deco2800.pyramidscheme.achievements;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uq.deco2800.pyramidscheme.game.GameManager;
import uq.deco2800.singularity.clients.pyramidscheme.PyramidSchemeClient;
import uq.deco2800.singularity.common.representations.pyramidscheme.Achievements;
import uq.deco2800.singularity.common.representations.pyramidscheme.UserStatistics;

import java.util.*;

public class AchievementTracking {

    /* Constants */
    public static final List<String> achievementNames = new ArrayList<String>();

    /* Variables */
    private List<Achievements> achieves;
    private Map<String, Boolean> gotAchieves = new HashMap<String, Boolean>();

    /* For pushing/ pulling */
    private Timer retryTimer;
    private Boolean retryGrab = false;
    private static final String RETRY_MSG = "Failed to grab/ pull will retry soon";

    /* For debuging */
    private static Logger logger = LoggerFactory.getLogger(GameManager.class);

    public AchievementTracking() {
        retryTimer = new Timer();

        achievementNames.add("Ez Games");
        achievementNames.add("Just warming up");
        achievementNames.add("I am legend");
        achievementNames.add("Duck beginner");
        achievementNames.add("Duck ruler");
        achievementNames.add("Duck master");
        achievementNames.add("Duck killer");
        achievementNames.add("Duck slaughter");
        achievementNames.add("Duck massacare");

        initaliseGotList();
    }

    /**
     * Sets the achievement list
     * Note: This is not the list that stores what we have/ dont have
     *
     * @param achieves
     */
    public void setAchieves(List<Achievements> achieves) {
        this.achieves = achieves;
    }

    /**
     * Gets the achievement list
     *
     * @return the achievement list
     * Note: This is not the list that stores what we have/ dont have
     */
    public List<Achievements> getAchieves() {
        return this.achieves;
    }

    /**
     * Sets the got list of achievements
     *
     * @param gotList
     */
    public void setGotList(Map<String, Boolean> gotList) {
        this.gotAchieves = gotList;
    }

    /**
     * Gets the list of got achievements
     *
     * @return the list of got achievements
     */
    public Map<String, Boolean> getGotList() {
        return this.gotAchieves;
    }

    /**
     * Retries grabbing achievements
     */
    private void retryGrab() {
        GameManager gameManager = GameManager.getInstance();
        PyramidSchemeClient client = gameManager.getPyramidSchemeClient();

        String userID = client.getUserId();
        if (retryGrab) {
            try {
                achieves = client.getAchievements(userID);
                retryGrab = false;
            } catch (Exception e) {
                retryGrab = true;
                logger.info(RETRY_MSG + e);
                retryTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        retryGrab();
                    }
                }, (long) 120000);
                return;
            }
            addToGotList();
        }
    }

    /**
     * Grabs all the achievements for the player
     */
    public void grabAchievements() {
        GameManager gameManager = GameManager.getInstance();
        PyramidSchemeClient client = gameManager.getPyramidSchemeClient();

        String userID = client.getUserId();

        try {
            achieves = client.getAchievements(userID);
            retryGrab = false;
        } catch (Exception e) {
            retryGrab = true;
            logger.info(RETRY_MSG + e);

            retryTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    retryGrab();
                }
            }, (long) 120000);
            return;
        }

        addToGotList();
    }

    /**
     * Sets all achievements got to false
     */
    public void initaliseGotList() {
        for (int i = 0; i < achievementNames.size(); i++) {
            gotAchieves.put(achievementNames.get(i), false);
        }
    }

    /**
     * Adds achievements to the got list after pulling
     */
    public void addToGotList() {
        for (int i = 0; i < achieves.size(); i++) {
            gotAchieves.put(achieves.get(i).getAchievementName(), true);
            logger.info("Got achievement: " + achieves.get(i).getAchievementName());
        }
    }

    /**
     * Retries pushing a single achievement to the database
     */
    private void pushRetryAchievement(Achievements achieve) {
        GameManager gameManager = GameManager.getInstance();
        PyramidSchemeClient client = gameManager.getPyramidSchemeClient();

        try {
            client.createAchievement(achieve);
        } catch (Exception e) {
            logger.info(RETRY_MSG + e);
            retryTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    pushRetryAchievement(achieve);
                }
            }, 120000);
            return;
        }
    }

    /**
     * Pushes a single achievement to the database
     */
    private void pushAchievement(String achievementName) {
        GameManager gameManager = GameManager.getInstance();
        PyramidSchemeClient client = gameManager.getPyramidSchemeClient();

        String userID = client.getUserId();
        Achievements achieve = new Achievements();

        achieve.setAchievementName(achievementName);
        achieve.setUserID(userID);
        achieve.setTimestamp();

        try {
            client.createAchievement(achieve);
        } catch (Exception e) {
            logger.info(RETRY_MSG + e);
            retryTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    pushRetryAchievement(achieve);
                }
            }, 120000);
            return;
        }
    }

    /**
     * Updates the achievement list after getting an achievement
     *
     * @param name
     */
    private void updateAchievementList(String name) {
        logger.info("DEBUG: Achieved" + name);
        gotAchieves.put(name, true);
        pushAchievement(name);
    }

    /**
     * Checks if an achievement has been got based on the provided userstats
     *
     * @param stats
     */
    public void checkIfGot(UserStatistics stats) {
        logger.info("DEBUG: Checking if we got an achievement");
        if (!gotAchieves.get(achievementNames.get(0)) && Integer.parseInt(stats.getTotalWins()) >= 10) {
            updateAchievementList(achievementNames.get(0));
        }
        if (!gotAchieves.get(achievementNames.get(1)) && Integer.parseInt(stats.getTotalWins()) >= 50) {
            updateAchievementList(achievementNames.get(1));
        }
        if (!gotAchieves.get(achievementNames.get(2)) && Integer.parseInt(stats.getTotalWins()) >= 100) {
            updateAchievementList(achievementNames.get(2));
        }
        if (!gotAchieves.get(achievementNames.get(3)) && Integer.parseInt(stats.getMinionsKilled()) >= 10) {
            updateAchievementList(achievementNames.get(3));
        }
        if (!gotAchieves.get(achievementNames.get(4)) && Integer.parseInt(stats.getMinionsKilled()) >= 50) {
            updateAchievementList(achievementNames.get(4));
        }
        if (!gotAchieves.get(achievementNames.get(5)) && Integer.parseInt(stats.getMinionsKilled()) >= 100) {
            updateAchievementList(achievementNames.get(5));
        }
        if (!gotAchieves.get(achievementNames.get(6)) && Integer.parseInt(stats.getMinionsLost()) >= 10) {
            updateAchievementList(achievementNames.get(6));
        }
        if (!gotAchieves.get(achievementNames.get(7)) && Integer.parseInt(stats.getMinionsLost()) >= 50) {
            updateAchievementList(achievementNames.get(7));
        }
        if (!gotAchieves.get(achievementNames.get(8)) && Integer.parseInt(stats.getMinionsLost()) >= 100) {
            updateAchievementList(achievementNames.get(8));
        }
    }
}
