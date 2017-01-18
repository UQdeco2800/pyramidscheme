package uq.deco2800.pyramidscheme.champions;

import javafx.scene.image.Image;
import uq.deco2800.pyramidscheme.champions.abilities.Ability;

/**
 * Abstract class to represent individual champions.
 *
 * @author Josh Fry
 */
public class Champion {

    // Name, image, and ability to be used for the champion
    private String championName;
    private Image championImg;
    private Ability primaryChampionAbility;
    private Ability secondaryChampionAbility;

    /**
     * Create a new champion
     *
     * @param name             Champion Name
     * @param imgPath          Path to champion splash image
     * @param primaryAbility   Champion's primary (stronger) ability
     * @param secondaryAbility Champion's secondary ability
     */
    public Champion(String name, String imgPath, Ability primaryAbility, Ability secondaryAbility) {
        this.championName = name;
        this.primaryChampionAbility = primaryAbility;
        this.secondaryChampionAbility = secondaryAbility;
        championImg = new Image(getClass().getResourceAsStream(imgPath));
    }

    /**
     * Get the champion's name
     *
     * @return Champion's name
     */
    public String getName() {
        return championName;
    }

    /**
     * @return champion type
     */
    public String getType() {
        return "CH";
    }

    /**
     * @param abilityId 0 for primary ability, 1 for secondary
     * @return primary ability or secondary ability
     */
    public Ability getAbility(int abilityId) {
        if (abilityId == 0) {
            return primaryChampionAbility;
        } else {
            return secondaryChampionAbility;
        }
    }

    /**
     * @return a string representation of the champion: Type + Champion Name
     */
    @Override
    public String toString() {
        StringBuilder description = new StringBuilder();
        description.append("CH: ");
        description.append(this.getName() + "\n");
        description.append(primaryChampionAbility.toString() + "\n\n");
        description.append(secondaryChampionAbility.toString());
        return description.toString();
    }

    /**
     * @return the champions splash image
     */
    public Image getImage() {
        return championImg;
    }

}
