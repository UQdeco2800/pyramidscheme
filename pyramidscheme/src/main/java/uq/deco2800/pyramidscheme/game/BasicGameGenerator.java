package uq.deco2800.pyramidscheme.game;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uq.deco2800.pyramidscheme.cards.supercards.Card;
import uq.deco2800.pyramidscheme.cards.supercards.CardNotFoundException;
import uq.deco2800.pyramidscheme.cards.supercards.MinionCard;
import uq.deco2800.pyramidscheme.deck.CardList;
import uq.deco2800.pyramidscheme.deck.Deck;
import uq.deco2800.pyramidscheme.player.AI;

import java.util.ArrayList;

/**
 * A generator class that creates various basic game elements e.g. decks and AI players.
 *
 * @author Millie
 * @author bijelo9
 */

public class BasicGameGenerator {

    private static final String USELESS_DUCK = "Useless Duck";
    private static final String WILLING_SACRIFICE = "Willing Sacrifice";
    private static final String LITTLE_FAT_OL_DUCK = "Little Fat ol' Duck";
    private static final String DUCKZILLA = "Duckzilla";
    private static final String CHARGING_DUCK = "Charging Duck";
    private static final String SQUAWK_LEADER = "Squawk Leader";
    private static final String EDGAR_ALLEN_CROW = "Edgar Allen Crow";
    private static final String ACE_HENTURA = "Ace Hentura";
    private static final String KING_PENDUCKLING = "King Penduckling";
    private static final String DUCKTHULU = "Ducthulhu";
    private static final String DUCK_NORRIS = "Duck Norris";
    private static final String ON_DUTY_ARMOURY = "On Duty Armoury";
    private static final String DUCK_TRAPPER = "Duck Trapper";
    private static final String DOUBLE_HEADED_DUCK = "Double Headed Duck";
    private static final String POISIN_TIPPED_DUCKLING = "Poisoned Tipped Duck";
    private static final String HOLY_DUCK = "Holy Duck";
    private static final String SIR_GALADUCK = "Sir Galaduck";
    private static final String ADVENTUROUS_DUCKLING = "Adventurous Duckling";

    private static Logger logger = LoggerFactory.getLogger(GameManager.class);

    public BasicGameGenerator() {
        // do nothing
    }

    /**
     * Generates a basic deck of cards.
     *
     * @return A new Deck
     */
    public Deck createBasicDeck() {
        // Create a basic starter deck
        CardList basicList = new CardList();
        ArrayList<MinionCard> minionCards = (ArrayList) Card.getMinionCards(24);
        for (MinionCard card : minionCards) {
            basicList.addCard(card);
        }

        return new Deck(basicList);
    }

    /**
     * Create a basic starter deck with starter cards
     *
     * @return Deck starter deck
     */
    public Deck createStarterDeck() {
        // basic starter deck with only cards the player owns at the start of the game
        CardList basicList = new CardList();

        for (MinionCard mc : MinionCard.getCards().values()) {
            basicList.addCard(mc, 3);
        }

        return new Deck(basicList);
    }

    /**
     * Generates a basic AI player.
     */
    public AI createBasicAI() {
        String name = "Sir Steel";
        Deck deck = createBasicDeck();
        return new AI(name, deck);
    }

    /**
     * Generates a network player with a name
     *
     * @param name the username of the other player
     */
    public AI createNetworkAI(String name) {
        Deck deck = createBasicDeck();
        return new AI(name, deck);
    }

    /**
     * generates a boss AI player
     *
     * @param boss number of boss
     * @return
     */
    public AI createBossAI(int boss, Deck user) {
        String name = "Other";
        int health = 0;
        CardList cards = new CardList();
        try {
            switch (boss) {
                case 1:
                    name = "Bro'ck";
                    cards.addCard(MinionCard.get(USELESS_DUCK), 4);
                    cards.addCard(MinionCard.get(WILLING_SACRIFICE), 3);
                    cards.addCard(MinionCard.get(LITTLE_FAT_OL_DUCK), 4);
                    cards.addCard(MinionCard.get(DUCKZILLA), 3);
                    cards.addCard(MinionCard.get(CHARGING_DUCK), 4);
                    cards.addCard(MinionCard.get(SQUAWK_LEADER), 2);
                    cards.addCard(MinionCard.get(EDGAR_ALLEN_CROW), 3);
                    cards.addCard(MinionCard.get(ACE_HENTURA), 5);
                    cards.addCard(MinionCard.get(KING_PENDUCKLING), 2);
                    break;
                case 2:
                    name = "Guard";
                    cards.addCard(MinionCard.get(CHARGING_DUCK), 5);
                    cards.addCard(MinionCard.get(ON_DUTY_ARMOURY), 5);
                    cards.addCard(MinionCard.get(DUCK_TRAPPER), 3);
                    cards.addCard(MinionCard.get(LITTLE_FAT_OL_DUCK), 3);
                    cards.addCard(MinionCard.get("Quack the Ripper"), 3);
                    cards.addCard(MinionCard.get("Big Fat ol' Duck"), 3);
                    cards.addCard(MinionCard.get("Duckling Reinforcements"), 3);
                    cards.addCard(MinionCard.get(SQUAWK_LEADER), 3);
                    cards.addCard(MinionCard.get(DUCK_NORRIS), 3);
                    break;
                case 3:
                    name = "Priestess";
                    cards.addCard(MinionCard.get(ACE_HENTURA), 5);
                    cards.addCard(MinionCard.get("Shielded Sorceress"), 4);
                    cards.addCard(MinionCard.get(DUCK_TRAPPER), 3);
                    cards.addCard(MinionCard.get(DOUBLE_HEADED_DUCK), 4);
                    cards.addCard(MinionCard.get("Duck Norris Jr"), 3);
                    cards.addCard(MinionCard.get(DUCKZILLA), 4);
                    cards.addCard(MinionCard.get(DUCK_NORRIS), 2);
                    cards.addCard(MinionCard.get(CHARGING_DUCK), 5);
                    break;
                case 4:
                    name = "Pharoah";
                    cards.addCard(MinionCard.get(CHARGING_DUCK), 5);
                    cards.addCard(MinionCard.get(ON_DUTY_ARMOURY), 5);
                    cards.addCard(MinionCard.get(DOUBLE_HEADED_DUCK), 4);
                    cards.addCard(MinionCard.get("Eggolus"), 4);
                    cards.addCard(MinionCard.get(EDGAR_ALLEN_CROW), 2);
                    cards.addCard(MinionCard.get("Duckling Reinforcements"), 4);
                    cards.addCard(MinionCard.get(SQUAWK_LEADER), 4);
                    cards.addCard(MinionCard.get(DUCK_NORRIS), 2);
                    break;
                case 5:
                    name = "Mongolian Beef";
                    cards.addCard(MinionCard.get(CHARGING_DUCK), 4);
                    cards.addCard(MinionCard.get(DUCK_TRAPPER), 2);
                    cards.addCard(MinionCard.get("Mace Wielding Duck"), 3);
                    cards.addCard(MinionCard.get("Atilla The Duck"), 3);
                    cards.addCard(MinionCard.get("Quack the Ripper"), 2);
                    cards.addCard(MinionCard.get(USELESS_DUCK), 3);
                    cards.addCard(MinionCard.get(ADVENTUROUS_DUCKLING), 2);
                    break;
                case 6:
                    name = "Hitler";
                    cards.addCard(MinionCard.get(USELESS_DUCK), 6);
                    cards.addCard(MinionCard.get(ACE_HENTURA), 6);
                    cards.addCard(MinionCard.get(WILLING_SACRIFICE), 5);
                    cards.addCard(MinionCard.get(DUCKTHULU), 2);
                    cards.addCard(MinionCard.get(DUCK_NORRIS), 3);
                    cards.addCard(MinionCard.get("Super Duck"), 3);
                    cards.addCard(MinionCard.get(SQUAWK_LEADER), 4);
                    cards.addCard(MinionCard.get(KING_PENDUCKLING), 3);
                    break;
                case 7:
                    name = "Napoleon";
                    cards.addCard(MinionCard.get(ADVENTUROUS_DUCKLING), 5);
                    cards.addCard(MinionCard.get("Charging Duck"), 4);
                    cards.addCard(MinionCard.get(WILLING_SACRIFICE), 6);
                    cards.addCard(MinionCard.get(DOUBLE_HEADED_DUCK), 5);
                    cards.addCard(MinionCard.get("Eggolus"), 4);
                    cards.addCard(MinionCard.get(ON_DUTY_ARMOURY), 5);
                    cards.addCard(MinionCard.get(POISIN_TIPPED_DUCKLING), 5);
                    cards.addCard(MinionCard.get("Exploduck"), 3);
                    break;
                case 8:
                    name = "Duck Vader";
                    cards.addCard(MinionCard.get(ACE_HENTURA), 7);
                    cards.addCard(MinionCard.get(USELESS_DUCK), 7);
                    cards.addCard(MinionCard.get(ON_DUTY_ARMOURY), 5);
                    cards.addCard(MinionCard.get("Mummy Duck"), 5);
                    cards.addCard(MinionCard.get(POISIN_TIPPED_DUCKLING), 4);
                    cards.addCard(MinionCard.get("The Black Cat"), 4);
                    break;
                case 9:
                    name = "Lion";
                    cards.addCard(MinionCard.get(DUCKTHULU), 2);
                    cards.addCard(MinionCard.get(DUCK_NORRIS), 3);
                    cards.addCard(MinionCard.get("King Duck"), 4);
                    cards.addCard(MinionCard.get("Duck Norris Jr"), 3);
                    cards.addCard(MinionCard.get(SIR_GALADUCK), 4);
                    cards.addCard(MinionCard.get(HOLY_DUCK), 4);
                    cards.addCard(MinionCard.get(DUCKZILLA), 4);
                    cards.addCard(MinionCard.get("Crazed Duckling"), 3);
                    cards.addCard(MinionCard.get("The Black Cat"), 4);

                    break;
                case 10:
                    name = "Coaster";
                    cards.addCard(MinionCard.get(SIR_GALADUCK), 3);
                    cards.addCard(MinionCard.get(DUCKTHULU), 1);
                    cards.addCard(MinionCard.get("Exploduck"), 3);
                    cards.addCard(MinionCard.get("Big Fat ol' Duck"), 2);
                    cards.addCard(MinionCard.get(LITTLE_FAT_OL_DUCK), 4);
                    cards.addCard(MinionCard.get(HOLY_DUCK), 4);
                    cards.addCard(MinionCard.get(POISIN_TIPPED_DUCKLING), 4);
                    cards.addCard(MinionCard.get(ON_DUTY_ARMOURY), 4);
                    cards.addCard(MinionCard.get(WILLING_SACRIFICE), 5);
                    break;
                case 11:
                    name = "Dark Duck";
                    for (Card i : user.getCards()) {
                        cards.addCard(i);
                    }
                    break;
                case 12:
                    name = "Apophis";
                    cards.addCard(MinionCard.get(ADVENTUROUS_DUCKLING), 5);
                    cards.addCard(MinionCard.get(CHARGING_DUCK), 5);
                    cards.addCard(MinionCard.get("Super Duck"), 5);
                    cards.addCard(MinionCard.get("Atilla The Duck"), 4);
                    cards.addCard(MinionCard.get("Sweduck"), 7);
                    cards.addCard(MinionCard.get(WILLING_SACRIFICE), 2);
                    cards.addCard(MinionCard.get(DUCK_NORRIS), 2);
                    cards.addCard(MinionCard.get("Mace Wielding Duck"), 3);
                    cards.addCard(MinionCard.get(DUCKTHULU), 1);
                    cards.addCard(MinionCard.get("Duck of the Heavens"), 3);
                    cards.addCard(MinionCard.get(SIR_GALADUCK), 2);
                    health = 50;
                    break;
                default:
                    break;
            }
            Deck deck = new Deck(cards);
            if (cards.getSize() <= 0) {
                deck = createBasicDeck();
            }
            if (health == 0) {
                return new AI(name, deck);

            } else {
                return new AI(name, deck, health);
            }
        } catch (CardNotFoundException e) {
            Deck deck = createBasicDeck();
            logger.error("failed to create boss deck: " + e);
            return new AI(name, deck);
        }
    }

}
