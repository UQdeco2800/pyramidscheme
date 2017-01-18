package uq.deco2800.pyramidscheme.champions.abilities;

import javafx.scene.image.Image;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uq.deco2800.pyramidscheme.animations.emitter.Emitter;

/**
 * Abstract class that represents Abilities champions can use. All specific
 * abilities should implement this class.
 *
 * @author Josh
 */

public abstract class Ability {

    // name of the ability
    private String abilityName;
    // ability icon image
    private Image abilityImg;
    // ability icon image path
    private String abilityImgPath;
    // ability icon image path
    private int manaCost;

    Logger logger = LoggerFactory.getLogger(Ability.class);

    /**
     * Create a new instance of Ability
     *
     * @param abilityName String representing the name of the new ability.
     * @param imgPath     String representing the file path to ability icon.
     */
    public Ability(String abilityName, String imgPath, int cost) {
        this.abilityName = abilityName;
        this.abilityImg = new Image(getClass().getResourceAsStream(imgPath));
        this.abilityImgPath = imgPath;
        this.manaCost = cost;
    }

    /**
     * @return The ability name
     */
    public String getName() {
        return abilityName;
    }

    /**
     * @return The abilitie's icon image
     */
    public Image getImage() {
        return abilityImg;
    }

    /**
     * @return The abilitie's icon image
     */
    public String getImagePath() {
        return abilityImgPath;
    }

    /**
     * An abstract method that takes two optional parameters.
     * Every Ability will implement this method, but not all abilities
     * will make use of both parameters.
     *
     * @param optionalParameter1
     * @param optionalParameter2
     * @return Returns false if parameters were of wrong type, else true.
     */
    public abstract boolean activateAbility(Object optionalParameter1, Object optionalParameter2);


    /**
     * An abstract method for returning the emitter type
     *
     * @return an emitter enum
     */
    public abstract Emitter getEmitter();

    public int getCost() {
        return this.manaCost;
    }
}
