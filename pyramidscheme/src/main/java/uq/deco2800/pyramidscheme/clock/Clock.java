package uq.deco2800.pyramidscheme.clock;

import javafx.scene.text.Text;
import uq.deco2800.pyramidscheme.game.GameManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Clock class used to organise and display the time in game
 *
 * @author leeph
 */
public class Clock {

    Timer timer = new Timer();

    Text timeMinutes = new Text();
    Text timeSeconds = new Text();
    Text timeUnderFormating = new Text();

    Date currentDateNow;

    GameManager gameManager;

    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a"); //time formatting for seconds
    SimpleDateFormat mdf = new SimpleDateFormat("hh:mm a"); //time formatting for minutes
    SimpleDateFormat timeFormating = new SimpleDateFormat("hh:mm:ss a");

    /**
     * Main method for the clock class
     * <p>
     * runs the timer when the object is called
     */
    public Clock() {
        // Set current time
        updateTime();

        //Schedule updates every 950 ms
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateTime();
            }
        }, 0, 950);

        gameManager = GameManager.getInstance();
        if (gameManager.is24Hour()) {
            timeFormating = new SimpleDateFormat("kk:mm:ss");
        }

        // Set text id
        String timeText = "timeText";
        timeSeconds.setId(timeText);
        timeMinutes.setId(timeText);
        timeUnderFormating.setId(timeText);
    }

    /**
     * Get the current date and set the timeSeconds and timeMinute Text objects to a
     * to the correctly formatted date string.
     */
    private void updateTime() {
        Date currentDate = new Date();
        currentDateNow = currentDate;
        timeSeconds.setText(sdf.format(currentDate));
        timeMinutes.setText(mdf.format(currentDate));
        timeUnderFormating.setText(timeFormating.format(currentDate));
    }

    /**
     * Method to return the current time using hours, minutes, and seconds
     *
     * @return JavaTX text variable representing time in format hours:minutes:seconds.
     */
    Text getTimeSeconds() {
        return timeSeconds;
    }

    /**
     * Method to return the current time using hours and minutes
     *
     * @return JavaFX text variable representing time in format hours:minutes.
     */
    Text getTimeMinutes() {
        return timeMinutes;
    }

    /**
     * Returns the current date, does not update
     *
     * @return Date object
     */
    Date getCurrentDate() {
        return currentDateNow;
    }

    /**
     * changes the style of the getTimeUnderFormating, has no effect if the time formating is an
     * invalid SimpleDateFormat string
     *
     * @param formatStyle
     */
    public void setTimeUnderFormating(String formatStyle) {
        try {
            timeFormating = new SimpleDateFormat(formatStyle);
        } catch (IllegalArgumentException e) {
            return;
        }
    }

    /**
     * get the TimeUnderFormating, this is the time that can be set using the setTimeUnderFormating method, default is hh:mm:ss a
     *
     * @return text object
     */
    public Text getTimeUnderFormating() {
        return timeUnderFormating;
    }
}
