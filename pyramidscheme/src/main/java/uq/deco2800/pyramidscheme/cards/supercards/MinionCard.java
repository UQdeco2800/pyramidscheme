package uq.deco2800.pyramidscheme.cards.supercards;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import uq.deco2800.pyramidscheme.actions.Action;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Represents minion cards. Extends Card. All specific minion cards should
 * extend this class.
 *
 * @author Millie
 */

public class MinionCard extends Card {
    private int attack;
    private int defense;
    private static Element root;
    private static Document doc;
    private static HashMap<String, MinionCard> playableCardCache;
    private static HashMap<String, MinionCard> hiddenCardCache;
    private static HashMap<String, MinionCard> allCardCache;
    private HashMap<String, String> stack;
    private static final Logger LOGGER = Logger.getLogger(Card.class.getName());


    /**
     * Used to get all the hidden cards from storage. If the cards cache hasn't been created then this will also create
     * the cache
     *
     * @return a Map of Card name to Cards
     */
    public static Map<String, MinionCard> getHiddenCards() {
        if (hiddenCardCache == null) {
            getCards();
        }
        return hiddenCardCache;
    }

    /**
     * Used to get all the cards from storage. If the cards cache hasn't been created then this will also create the
     * cache
     *
     * @return a Map of Card name to Cards
     */
    public static Map<String, MinionCard> getAllCards() {
        if (allCardCache == null) {
            getCards();
        }
        return allCardCache;
    }

    /**
     * Loads a MinionCard from the XML Element
     *
     * @param card the Element for the information to be extracted from
     * @return returns a newly created MinionCard
     */
    private static MinionCard loadMinion(Element card) {
        // Get card name
        String name = card.getElementsByTagName("name").item(0).getTextContent();
        // Get card image
        String image = "/cardImages/" + card.getElementsByTagName("image").item(0).getTextContent();

        String unboxed = null;
        // Check if the card has an unboxed image
        if (card.getElementsByTagName("unbox").getLength() == 1) {
            unboxed = "/cardImages/" + card.getElementsByTagName("unbox").item(0).getTextContent();
        }
        // Get attack, defense and rank of card
        int attack = Integer.parseInt(card.
                getElementsByTagName("attack").item(0).getTextContent());
        int defense = Integer.parseInt(card.getElementsByTagName("defense").item(0).getTextContent());
        int rank = Integer.parseInt(card.getElementsByTagName("rank").item(0).getTextContent());
        int dust = 1;
        // Get card dust
        if (card.getElementsByTagName("dust").getLength() == 1) {
            dust = Integer.parseInt(card.getElementsByTagName("dust").item(0).getTextContent());
        }
        // Create minion card instance
        MinionCard mc = new MinionCard(name, image, unboxed, attack, defense, rank, dust);
        if (card.getElementsByTagName("action").getLength() == 1) {
            String actionName = card.getElementsByTagName("action").item(0).getTextContent();
            actionName = "uq.deco2800.pyramidscheme.actions.".concat(actionName);
            try {
                Class actionClass = Class.forName(actionName);
                Action a = (Action) actionClass.newInstance();
                mc.setAction(a);
            } catch (Exception ex) {
                LOGGER.log(Level.SEVERE, "Error while trying to load action for card");
                LOGGER.log(Level.SEVERE, ex.getMessage());
                return null;
            }
        }
        if (card.getElementsByTagName("sibling").getLength() > 0 &&
                card.getElementsByTagName("parent").getLength() == 1) {
            NodeList sibling = card.getElementsByTagName("sibling");
            NodeList parent = card.getElementsByTagName("parent");
            for (int j = 0; j < sibling.getLength(); j++) {
                mc.setStack(sibling.item(j).getTextContent(),
                        parent.item(0).getTextContent());
            }
        }
        return mc;
    }

    /**
     * Creates and initializes the card cache. Also sets up the foundation for reading of the xml file
     */
    private static void createCache() {
        playableCardCache = new HashMap<>();
        hiddenCardCache = new HashMap<>();
        allCardCache = new HashMap<>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            doc = builder.parse(MinionCard.class.getResourceAsStream("/cards.xml"));
            root = doc.getDocumentElement();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error while trying to access the cards storage");
            LOGGER.log(Level.SEVERE, ex.getMessage());
        }
    }

    /**
     * Get all the visible cards. If the card cache is empty then all the cards are loaded from an xml file
     *
     * @return a Map of Card names to MinionCard's
     */
    public static Map<String, MinionCard> getCards() {
        if (root == null || doc == null || playableCardCache == null) {
            createCache();
        }
        // Get all cards from cards.xml
        NodeList cardsList = doc.getElementsByTagName("card");
        // Iterate through each element and create the card
        for (int i = 0; i < cardsList.getLength(); i++) {
            Node nNode = cardsList.item(i);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element card = (Element) nNode;
                String classType = card.getElementsByTagName("class").item(0).getTextContent();
                if ("minion".equals(classType)) {
                    String name = card.getElementsByTagName("name").item(0).getTextContent();
                    MinionCard mc = loadMinion(card);
                    if (!allCardCache.containsKey(name)) {
                        allCardCache.put(name, mc);
                    }
                    if (card.getElementsByTagName("hidden").getLength() == 1 &&
                            "true".equalsIgnoreCase(card.getElementsByTagName("hidden").item(0).getTextContent())) {
                        // set the hidden stack
                        if (!hiddenCardCache.containsKey(name)) {
                            hiddenCardCache.put(name, mc);
                        }
                    } else {
                        // set to the playable stack
                        if (!playableCardCache.containsKey(name)) {
                            playableCardCache.put(name, mc);
                        }
                    }
                }
            }
        }
        return playableCardCache;
    }

    /**
     * Returns the MinionCard object corresponding to a given card name.
     *
     * @param name of card to return.
     * @return Minion Card Object corresponding to parsed name
     */
    public static MinionCard get(String name) throws CardNotFoundException {
        HashMap<String, MinionCard> cards = (HashMap) getAllCards();
        MinionCard card = cards.get(name);
        if (card == null) {
            throw new CardNotFoundException("Card Name: " + name + " is not a "
                    + "vaild card!");
        }
        return card.dup();
    }

    /**
     * Constructor creates a MinionCard
     *
     * @param name    the name of the card
     * @param img     the image location of the card
     * @param unboxed the location of the unboxed image
     * @param att     the amount of default attack
     * @param def     the amount of default defense
     * @param rank    the rank given to the card
     * @param cost    the duck dust cost of the card
     */
    public MinionCard(String name, String img, String unboxed, int att, int def, int rank, int cost) {
        super(name, img, unboxed, rank, cost);
        this.attack = att;
        this.defense = def;
        stack = new HashMap<>();
    }

    /**
     * Duplicates the card and returns the duplication
     *
     * @return a MinionCard duplication of the current MinionCard
     */
    public MinionCard dup() {
        MinionCard mc = new MinionCard(getName(), getImgPath(), getUnboxPath(), this.attack, this.defense, getRank(), getCost());
        for (String key : stack.keySet()) {
            mc.setStack(key, stack.get(key));
        }
        if (getAction() != null) {
            mc.setAction(getAction());
        }
        return mc;
    }

    /**
     * Adds all cards that are stackable to a hashmap
     *
     * @param sibling The name if the sibling to be triggered on play
     * @param parent  The name of the new card to be replaced on stacking
     */
    public void setStack(String sibling, String parent) {
        if (stack.containsKey(sibling)) {
            return;
        }
        stack.put(sibling, parent);
    }

    /**
     * Gets the keys if the stack
     *
     * @return Returns a Set of Strings of the keys for the stack
     */
    public Set<String> getStackKeys() {
        return stack.keySet();
    }

    /**
     * Clears the cache for hidden, visible, and all
     */
    public static void clearCache() {
        hiddenCardCache = null;
        playableCardCache = null;
        allCardCache = null;
    }

    /**
     * Returns the parent of a given Minion Card
     *
     * @param cardName the name of the sibling which the parent might be triggered from
     * @return MinionCard parent of given card
     */
    public MinionCard getParent(MinionCard cardName) {
        // Check if hashmap of stackable MinionCards contains cardName
        if (stack.containsKey(cardName.getName())) {
            try {
                return MinionCard.get(stack.get(cardName.getName()));
            } catch (CardNotFoundException ex) {
                LOGGER.log(Level.SEVERE, "Error occurred while trying to fetch the parent card");
                LOGGER.log(Level.SEVERE, ex.getMessage());
                return null;
            }
        }
        return null;
    }

    /**
     * Getter method that returns the attack of MinionCard instance
     *
     * @return an int of attack for the card
     */
    public int getAttack() {
        return attack;
    }

    /**
     * Getter method that returns the defense of MinionCard instance
     *
     * @return an int of defense for the card
     */
    public int getDefense() {
        return defense;
    }

    /**
     * Return some String representation of this card
     */
    public String toString() {
        StringBuilder description = new StringBuilder();
        description.append("MC ");
        description.append(this.getName());
        description.append(": A");
        description.append(attack);
        description.append(" D");
        description.append(defense);
        description.append(" R");
        description.append(getRank());
        description.append(" Dust");
        try {
            description.append(MinionCard.get(this.getName()).getCost());
        } catch (CardNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Tried to fetch image: " + this.getName());
            LOGGER.log(Level.SEVERE, e.getMessage());
        }
        return description.toString();
    }

    /**
     * Returns the card type in string format
     *
     * @return a String of the card type representation
     */
    @Override
    public String getType() {
        return "MC";
    }

}
