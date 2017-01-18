package uq.deco2800.pyramidscheme.controllers;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import uq.deco2800.pyramidscheme.achievements.AchievementTracking;
import uq.deco2800.pyramidscheme.game.GameManager;
import uq.deco2800.pyramidscheme.statistics.StatisticsTracking;
import uq.deco2800.singularity.common.representations.pyramidscheme.ChampionStatistics;
import uq.deco2800.singularity.common.representations.pyramidscheme.UserStatistics;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;


/**
 * A controller for the overview screen
 */

public class OverviewController implements Initializable {

    // Declare the FX objects this class uses
    @FXML
    private Button backButton;

    @FXML
    private StackPane overviewScreen;

    @FXML
    private TextFlow username;

    @FXML
    private TextFlow timetrack;

    @FXML
    private GridPane achievements;

    private AchievementTracking achieves;
    private StatisticsTracking tracking;
    private UserStatistics userStats;

    @FXML
    private GridPane stats;
    @FXML
    private GridPane quackubisStats;
    @FXML
    private GridPane kehpriStats;
    @FXML
    private GridPane rallardStats;
    @FXML
    private GridPane fowlSphinxStats;


    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        backButton.setOnAction(e -> GameManager.changeScene("MenuScreen.fxml"));

        GameManager gameManager = GameManager.getInstance();
        tracking = GameManager.getStatisticsTracking();
        userStats = tracking.getUserStats();

        achieves = GameManager.getAchieveTracking();
        achieves.checkIfGot(userStats);

        if (username != null) {
            username.getChildren().add(new Text(gameManager.getUser().getName()));
        }

        if (timetrack != null) {
            timetrack.getChildren().add(new Text(userStats.getTotalHours() + "Hrs" + userStats.getTotalMinutes() + "Mins"));
        }
        this.setAchieves(achievements.getChildren());
        this.populateStats(stats, GameManager.getStatisticsTracking().getUserStats());
        this.populateStats(quackubisStats, GameManager.getStatisticsTracking().getQuackubisStats());
        this.populateStats(kehpriStats, GameManager.getStatisticsTracking().getKhepriStats());
        this.populateStats(rallardStats, GameManager.getStatisticsTracking().getRallardStats());
        this.populateStats(fowlSphinxStats, GameManager.getStatisticsTracking().getFowlSphinxStats());
    }

    private void setAchieves(ObservableList<Node> achievementList) {
        for (int i = 0; i < achievementList.size(); i++) {
            GridPane currentAchievement = (GridPane) achievementList.get(i);
            if (i == 0) {
                // Ez game
                Tooltip description = new Tooltip("Win 10 Games");
                description.setStyle("-fx-background-color: FFFFFF;" + "-fx-text-fill: black;");
                Tooltip.install(currentAchievement.getChildren().get(1), description);

                if (Integer.parseInt(userStats.getTotalWins()) >= 10 ||
                        achieves.getGotList().get(AchievementTracking.achievementNames.get(0))) {
                    ((ProgressIndicator) currentAchievement.getChildren().get(2)).setProgress(1);
                    currentAchievement.setId("complete");

                    ((Text) currentAchievement.getChildren().get(3)).setText("10/10");
                } else {
                    ((ProgressIndicator) currentAchievement.getChildren().get(2))
                            .setProgress(Integer.parseInt(userStats.getTotalWins()) / 10.0);

                    ((Text) currentAchievement.getChildren().get(3)).setText(userStats.getTotalWins() + "/10");
                }
            }
            if (i == 1) {
                // Just warming up
                Tooltip description = new Tooltip("Win 50 Games");
                description.setStyle("-fx-background-color: FFFFFF;" + "-fx-text-fill: black;");
                Tooltip.install(currentAchievement.getChildren().get(1), description);

                if (Integer.parseInt(userStats.getTotalWins()) >= 50 ||
                        achieves.getGotList().get(AchievementTracking.achievementNames.get(1))) {
                    ((ProgressIndicator) currentAchievement.getChildren().get(2)).setProgress(1);
                    currentAchievement.setId("complete");

                    ((Text) currentAchievement.getChildren().get(3)).setText("50/50");
                } else {
                    ((ProgressIndicator) currentAchievement.getChildren().get(2))
                            .setProgress(Integer.parseInt(userStats.getTotalWins()) / 50.0);

                    ((Text) currentAchievement.getChildren().get(3)).setText(userStats.getTotalWins() + "/50");
                }
            }
            if (i == 2) {
                // I am legend
                Tooltip description = new Tooltip("Win 100 Games");
                description.setStyle("-fx-background-color: FFFFFF;" + "-fx-text-fill: black;");
                Tooltip.install(currentAchievement.getChildren().get(1), description);

                if (Integer.parseInt(userStats.getTotalWins()) >= 100 ||
                        achieves.getGotList().get(AchievementTracking.achievementNames.get(2))) {
                    ((ProgressIndicator) currentAchievement.getChildren().get(2)).setProgress(1);
                    currentAchievement.setId("complete");

                    ((Text) currentAchievement.getChildren().get(3)).setText("100/100");
                } else {
                    ((ProgressIndicator) currentAchievement.getChildren().get(2))
                            .setProgress(Integer.parseInt(userStats.getTotalWins()) / 100.0);

                    ((Text) currentAchievement.getChildren().get(3)).setText(userStats.getTotalWins() + "/100");
                }
            }
            if (i == 3) {
                // Duck beginner
                Tooltip description = new Tooltip("Kill 10 Minions");
                description.setStyle("-fx-background-color: FFFFFF;" + "-fx-text-fill: black;");
                Tooltip.install(currentAchievement.getChildren().get(1), description);

                if (Integer.parseInt(userStats.getMinionsKilled()) >= 10 ||
                        achieves.getGotList().get(AchievementTracking.achievementNames.get(3))) {
                    ((ProgressIndicator) currentAchievement.getChildren().get(2)).setProgress(1);
                    currentAchievement.setId("complete");

                    ((Text) currentAchievement.getChildren().get(3)).setText("10/10");
                } else {
                    ((ProgressIndicator) currentAchievement.getChildren().get(2))
                            .setProgress(Integer.parseInt(userStats.getMinionsKilled()) / 10.0);

                    ((Text) currentAchievement.getChildren().get(3)).setText(userStats.getMinionsKilled() + "/10");
                }
            }
            if (i == 4) {
                // Duck beginner
                Tooltip description = new Tooltip("Kill 50 Minions");
                description.setStyle("-fx-background-color: FFFFFF;" + "-fx-text-fill: black;");
                Tooltip.install(currentAchievement.getChildren().get(1), description);

                if (Integer.parseInt(userStats.getMinionsKilled()) >= 50 ||
                        achieves.getGotList().get(AchievementTracking.achievementNames.get(4))) {
                    ((ProgressIndicator) currentAchievement.getChildren().get(2)).setProgress(1);
                    currentAchievement.setId("complete");

                    ((Text) currentAchievement.getChildren().get(3)).setText("50/50");
                } else {
                    ((ProgressIndicator) currentAchievement.getChildren().get(2))
                            .setProgress(Integer.parseInt(userStats.getMinionsKilled()) / 50.0);

                    ((Text) currentAchievement.getChildren().get(3)).setText(userStats.getMinionsKilled() + "/50");
                }
            }
            if (i == 5) {
                // Duck beginner
                Tooltip description = new Tooltip("Kill 100 Minions");
                description.setStyle("-fx-background-color: FFFFFF;" + "-fx-text-fill: black;");
                Tooltip.install(currentAchievement.getChildren().get(1), description);

                if (Integer.parseInt(userStats.getMinionsKilled()) >= 100 ||
                        achieves.getGotList().get(AchievementTracking.achievementNames.get(5))) {
                    ((ProgressIndicator) currentAchievement.getChildren().get(2)).setProgress(1);
                    currentAchievement.setId("complete");

                    ((Text) currentAchievement.getChildren().get(3)).setText("100/100");
                } else {
                    ((ProgressIndicator) currentAchievement.getChildren().get(2))
                            .setProgress(Integer.parseInt(userStats.getMinionsKilled()) / 100.0);

                    ((Text) currentAchievement.getChildren().get(3)).setText(userStats.getMinionsKilled() + "/100");
                }
            }
            if (i == 6) {
                // Duck killer
                Tooltip description = new Tooltip("Lose 10 Minions");
                description.setStyle("-fx-background-color: FFFFFF;" + "-fx-text-fill: black;");
                Tooltip.install(currentAchievement.getChildren().get(1), description);

                if (Integer.parseInt(userStats.getMinionsLost()) >= 10 ||
                        achieves.getGotList().get(AchievementTracking.achievementNames.get(6))) {
                    ((ProgressIndicator) currentAchievement.getChildren().get(2)).setProgress(1);
                    currentAchievement.setId("complete");

                    ((Text) currentAchievement.getChildren().get(3)).setText("10/10");
                } else {
                    ((ProgressIndicator) currentAchievement.getChildren().get(2))
                            .setProgress(Integer.parseInt(userStats.getMinionsLost()) / 10.0);

                    ((Text) currentAchievement.getChildren().get(3)).setText(userStats.getMinionsLost() + "/10");
                }
            }
            if (i == 7) {
                // Duck killer
                Tooltip description = new Tooltip("Lose 50 Minions");
                description.setStyle("-fx-background-color: FFFFFF;" + "-fx-text-fill: black;");
                Tooltip.install(currentAchievement.getChildren().get(1), description);

                if (Integer.parseInt(userStats.getMinionsLost()) >= 50 ||
                        achieves.getGotList().get(AchievementTracking.achievementNames.get(7))) {
                    ((ProgressIndicator) currentAchievement.getChildren().get(2)).setProgress(1);
                    currentAchievement.setId("complete");

                    ((Text) currentAchievement.getChildren().get(3)).setText("50/50");
                } else {
                    ((ProgressIndicator) currentAchievement.getChildren().get(2))
                            .setProgress(Integer.parseInt(userStats.getMinionsLost()) / 50.0);

                    ((Text) currentAchievement.getChildren().get(3)).setText(userStats.getMinionsLost() + "/50");
                }
            }
            if (i == 8) {
                // Duck killer
                Tooltip description = new Tooltip("Lose 100 Minions");
                description.setStyle("-fx-background-color: FFFFFF;" + "-fx-text-fill: black;");
                Tooltip.install(currentAchievement.getChildren().get(1), description);

                if (Integer.parseInt(userStats.getMinionsLost()) >= 100 ||
                        achieves.getGotList().get(AchievementTracking.achievementNames.get(8))) {
                    ((ProgressIndicator) currentAchievement.getChildren().get(2)).setProgress(1);
                    currentAchievement.setId("complete");

                    ((Text) currentAchievement.getChildren().get(3)).setText("100/100");
                } else {
                    ((ProgressIndicator) currentAchievement.getChildren().get(2))
                            .setProgress(Integer.parseInt(userStats.getMinionsLost()) / 100.0);

                    ((Text) currentAchievement.getChildren().get(3)).setText(userStats.getMinionsLost() + "/100");
                }
            }
        }
    }


    private void populateStats(GridPane currentStats, UserStatistics userStatistics) {
        ArrayList<String> stats = new ArrayList<String>();
        stats.add(userStatistics.getTotalWins());
        stats.add(userStatistics.getTotalMinutes());
        stats.add(userStatistics.getTotalLosses());
        stats.add(userStatistics.getTotalHours());
        stats.add(userStatistics.getMinionsPlayed());
        stats.add(userStatistics.getMinionsKilled());
        stats.add(userStatistics.getMinionsLost());
        stats.add(userStatistics.getHealthTaken());
        stats.add(userStatistics.getHealthLost());
        stats.add(userStatistics.getLastLogin().toString());

        for (int i = 0; i < currentStats.getChildren().size(); i++) {
            Text stat = new Text(stats.get(i));
            if (i != 9) {
                stat.setStyle("-fx-font: 40px Agency;" +
                        "-fx-fill: black;");
            } else {
                stat.setStyle("-fx-font: 25px Agency;" +
                        "-fx-fill: black;");
            }
            ((TextFlow) ((GridPane) currentStats.getChildren().get(i)).getChildren().get(0)).getChildren().add(stat);
        }
    }

    private void populateStats(GridPane currentStats, ChampionStatistics userStatistics) {
        ArrayList<String> stats = new ArrayList<String>();
        stats.add(userStatistics.getTotalWins());
        stats.add(userStatistics.getTotalMinutes());
        stats.add(userStatistics.getTotalLosses());
        stats.add(userStatistics.getTotalHours());
        stats.add(userStatistics.getMinionsPlayed());
        stats.add(userStatistics.getMinionsKilled());
        stats.add(userStatistics.getMinionsLost());
        stats.add(userStatistics.getHealthTaken());
        stats.add(userStatistics.getHealthLost());
        stats.add(userStatistics.getLastLogin().toString());

        for (int i = 0; i < currentStats.getChildren().size(); i++) {
            Text stat = new Text(stats.get(i));
            if (i != 9) {
                stat.setStyle("-fx-font: 40px Agency;" +
                        "-fx-fill: black;");
            } else {
                stat.setStyle("-fx-font: 25px Agency;" +
                        "-fx-fill: black;");
            }
            ((TextFlow) ((GridPane) currentStats.getChildren().get(i)).getChildren().get(0)).getChildren().add(stat);

        }
    }
}
