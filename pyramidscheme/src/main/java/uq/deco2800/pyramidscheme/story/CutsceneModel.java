package uq.deco2800.pyramidscheme.story;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * A model for the story mode cutscenes.
 *
 * @author Justin
 * @author Bianca
 */
public class CutsceneModel {

    private static Logger logger = LoggerFactory.getLogger(CutsceneModel.class);
    private ArrayList<Dialogue> moments = new ArrayList<Dialogue>();
    int currentText = 0;

    /**
     * @param level
     */
    public CutsceneModel(String level) throws IOException {
        load(level);
    }

    /**
     * opens up level.txt in resources and extracts the data
     *
     * @param level
     */
    public void load(String level) throws IOException {
        String filename = "storyCutsceneData/" + level + ".txt";
        String line;

        File file = new File(getClass().getClassLoader().
                getResource(filename).getFile());

        try (Scanner scanner = new Scanner(file)) {

            while (scanner.hasNextLine()) {
                line = scanner.nextLine();
                moments.add(createDialogue(line));
            }

            scanner.close();

        } catch (IOException e) {
            logger.error(e.getMessage());
            System.exit(1);
        }

    }

    /**
     * Takes a line and converts it to dialogue
     *
     * @param data the line from the level file
     * @return the dialogue stored on that line
     */
    private Dialogue createDialogue(String data) {

        Scanner line = new Scanner(data);
        line.useDelimiter("; ");
        Dialogue result = new Dialogue(line.next(), line.next());
        line.close();
        return result;
    }

    /**
     * @return the next string
     */
    public String nextText() {

        return moments.get(currentText++).getLine();
    }

    /**
     * @return the next image
     */
    public String nextImage() {

        return moments.get(currentText++).getImage();
    }

    /**
     * @return the current image
     */
    public String getImage() {

        return moments.get(currentText).getImage();
    }

    /**
     * @return the current line
     */
    public String getLine() {

        return moments.get(currentText).getLine();
    }

    /**
     * change to the next moment
     */
    public void getNext() {
        currentText++;
    }

    /**
     * @return the number of lines in the current scene
     */
    public int textInScene() {
        return moments.size();
    }

}



