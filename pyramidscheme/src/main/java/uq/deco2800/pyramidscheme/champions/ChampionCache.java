package uq.deco2800.pyramidscheme.champions;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import uq.deco2800.pyramidscheme.cards.supercards.MinionCard;
import uq.deco2800.pyramidscheme.champions.abilities.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A class used to statically store the champions in champions.xml. To see how
 * champions / abilities are created, see the updated wikis:
 * https://github.com/UQdeco2800/deco2800-2016-pyramidscheme/wiki/Champions &&
 * https://github.com/UQdeco2800/deco2800-2016-pyramidscheme/wiki/Abilities.
 *
 * @author Josh Fry
 */
public class ChampionCache {
    private static Document doc;
    // a hashmap, key being championName, and value being Champion
    private static HashMap<String, Champion> champions;
    private static final Logger LOGGER = Logger.getLogger(ChampionCache.class.getName());
    private static int championCount;

    /**
     * Create an instance of ChampionCache.
     */
    public ChampionCache() {
        getCache();
    }

    /**
     * Generate the ChampionCache if it hasn't already been created.
     *
     * @return The ChampionCache as a HasMap
     */
    public static Map<String, Champion> getCache() {
        if (doc == null || champions == null) {
            champions = new HashMap<>();
            try {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                doc = builder.parse(ChampionCache.class.getResourceAsStream("/champions.xml"));
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, e.getMessage());
                System.exit(1);
            }

            // All of the xml champion elements from champions.xml
            NodeList championList = doc.getElementsByTagName("champion");
            championCount = championList.getLength();

            // iterate over all champions, create one champion for each found in
            // file
            for (int i = 0; i < championCount; ++i) {
                Node championNode = championList.item(i);
                if (championNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element championParams = (Element) championNode;
                    createChampion(championParams);

                }
            }
        }
        return champions;
    }

    /**
     * A method to generate a champion using text elements from championParams.
     *
     * @param champion A champion element from champions.xml
     */
    private static void createChampion(Element championParams) {
        // Get champion name
        String name = championParams.getElementsByTagName("name").item(0).getTextContent();
        // Get champion image
        String imgPath = "/championImages/" + championParams.getElementsByTagName("image").item(0).getTextContent();

        Element primaryAbilityNode = (Element) championParams.getElementsByTagName("primary").item(0);
        Element secondaryAbilityNode = (Element) championParams.getElementsByTagName("secondary").item(0);

        Ability primaryAbility = createAbility(primaryAbilityNode);
        Ability secondaryAbility = createAbility(secondaryAbilityNode);

        Champion newChampion = new Champion(name, imgPath, primaryAbility, secondaryAbility);

        // check if a champion of the same name exists
        if (!champions.containsKey(name)) {
            champions.put(name, newChampion);
        } else {
            LOGGER.log(Level.SEVERE, "Duplicate champion found");
        }
    }

    /**
     * A method to generate an ability using text elements from abilityParams.
     *
     * @param abilityParams An ability element from champions.xml
     * @return The generated Ability, null if the type was incorrect
     */
    private static Ability createAbility(Element abilityParams) {
        // all abilities have these fields, do them first
        String abilityType = abilityParams.getElementsByTagName("class").item(0).getTextContent();
        String abilityName = abilityParams.getElementsByTagName("name").item(0).getTextContent();
        String imgPath = "/championImages/" + abilityParams.getElementsByTagName("image").item(0).getTextContent();
        int cost = Integer.parseInt(abilityParams.getElementsByTagName("cost").item(0).getTextContent());

        Ability ability = null;
        // check if the ability is one of these types, and instantiate it
        switch (abilityType) {
            case "MinionRevive":
                ability = new MinionRevive(abilityName, imgPath, cost);
                break;

            case "MinionSummon":
                MinionCard minion = null;
                try {
                    minion = MinionCard.get(abilityParams.getElementsByTagName("minion").item(0).getTextContent());
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, "Minion defined in MinionSummon does not exist");
                    LOGGER.log(Level.SEVERE, e.getMessage());
                    System.exit(1);
                }
                ability = new MinionSummon(abilityName, imgPath, cost, minion);
                break;

            case "TargetAnnihilate":
                ability = new TargetAnnihilate(abilityName, imgPath, cost);
                break;

            case "TargetBerserk":
                int healthDecrement = Integer
                        .parseInt(abilityParams.getElementsByTagName("healthDecrement").item(0).getTextContent());
                int attackIncrement = Integer
                        .parseInt(abilityParams.getElementsByTagName("attackIncrement").item(0).getTextContent());
                ability = new TargetBerserk(abilityName, imgPath, cost, healthDecrement, attackIncrement);
                break;

            case "TargetDamage":
                int damage = Integer.parseInt(abilityParams.getElementsByTagName("damage").item(0).getTextContent());
                ability = new TargetDamage(abilityName, imgPath, cost, damage);
                break;

            case "TargetDefenseAura":
                int healthIncrement = Integer
                        .parseInt(abilityParams.getElementsByTagName("healthIncrement").item(0).getTextContent());
                int attackDecrement = Integer
                        .parseInt(abilityParams.getElementsByTagName("attackDecrement").item(0).getTextContent());
                ability = new TargetDefenseAura(abilityName, imgPath, cost, healthIncrement, attackDecrement);
                break;

            case "TargetEnrage":
                int damageDealt = Integer
                        .parseInt(abilityParams.getElementsByTagName("damageDealt").item(0).getTextContent());
                int attackBuffed = Integer
                        .parseInt(abilityParams.getElementsByTagName("attackBuffed").item(0).getTextContent());
                ability = new TargetEnrage(abilityName, imgPath, cost, damageDealt, attackBuffed);
                break;

            case "TargetTaunt":
                ability = new TargetTaunt(abilityName, imgPath, cost);
                break;

            case "TargetHeal":
                int health = Integer.parseInt(abilityParams.getElementsByTagName("health").item(0).getTextContent());
                ability = new TargetHeal(abilityName, imgPath, cost, health);
                break;

            case "TargetIronSkin":
                int healthMult = Integer
                        .parseInt(abilityParams.getElementsByTagName("healthMult").item(0).getTextContent());
                ability = new TargetIronSkin(abilityName, imgPath, cost, healthMult);
                break;

            default:
                LOGGER.log(Level.SEVERE, abilityType + "is an invalid type");
                System.exit(1);
        }
        return ability;
    }

    /**
     * Return the champion with the name championName
     *
     * @param championName A string containing the name of the desired champion
     * @return The champion with name championName
     */
    public Champion getChampion(String championName) {
        if (doc == null || champions == null) {
            getCache();
        }
        Champion champion = champions.get(championName);
        if (champion == null) {
            LOGGER.log(Level.SEVERE, "Champion: " + championName + " is not a valid champion");
            System.exit(1);
        }
        return champion;
    }

    /**
     * Return the champion and index
     *
     * @param index The integer index of the desired champion
     * @return The champion at index, or null if there is none
     */
    public Champion getChampionByIndex(int index) {
        if (doc == null || champions == null) {
            getCache();
        }
        int i = 0;
        for (Entry<String, Champion> entry : champions.entrySet()) {
            if (index == i) {
                return entry.getValue();
            }
            ++i;
        }
        LOGGER.log(Level.SEVERE, "No Champion at index: " + index);
        return null;
    }

    /**
     * @return The number of champions in the cache
     */
    public int championCount() {
        if (doc == null || champions == null) {
            getCache();
        }
        return championCount;
    }
}