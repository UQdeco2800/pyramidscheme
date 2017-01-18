package uq.deco2800.pyramidscheme.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Callback;
import uq.deco2800.pyramidscheme.addinfo.AddInfoMarkert;
import uq.deco2800.pyramidscheme.cards.supercards.Card;
import uq.deco2800.pyramidscheme.cards.supercards.MinionCard;
import uq.deco2800.pyramidscheme.deck.CardList;
import uq.deco2800.pyramidscheme.deck.Deck;
import uq.deco2800.pyramidscheme.deck.ViewCard;
import uq.deco2800.pyramidscheme.deck.ViewCardList;
import uq.deco2800.pyramidscheme.game.GameManager;
import uq.deco2800.pyramidscheme.player.Player;
import uq.deco2800.pyramidscheme.player.User;

import javax.swing.*;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * A controller for the deck view screen.
 *
 * @author Millie
 * @author Billy-7
 */

public class DeckViewController implements Initializable {

    @FXML
    private BorderPane deckViewScreen;
    @FXML
    private StackPane cardPane;
    @FXML
    private Button backButton;
    @FXML
    private Tab builderTab;
    @FXML
    private Tab marketTab;
    @FXML
    private TextField searchBox;
    @FXML
    private ToggleButton minionButton;
    @FXML
    private ToggleButton actionButton;
    @FXML
    private TabPane tabPane;
    @FXML
    private GridPane deckGrid;
    @FXML
    private TextField deckName;
    @FXML
    private Pagination pagination;
    @FXML
    private Label deckSize;
    @FXML
    private AnchorPane marketPane;
    @FXML
    private Button buyButton;
    @FXML
    private Button sellButton;
    @FXML
    private Label cardDetail;
    @FXML
    private Label messageLabel;
    @FXML
    private ComboBox deckSelect;
    @FXML
    private Button deckNameEdit;
    @FXML
    private Label saved;
    @FXML
    private ToggleButton ownedToggle;
    @FXML
    private Button builderButton;
    @FXML
    private Button marketButton;
    @FXML
    private HBox verticleBox;

    private static final int CANVAS_WIDTH = 700;
    private static final int CANVAS_HEIGHT = 500; // Match to DeckViewScreen.fxml values.
    private static final int MARKET_CANVAS_WIDTH = 200;
    private static final int MARKET_CANVAS_HEIGHT = 475;
    private static final int CARDS_PER_PAGE = 6;
    private static final double CARD_SCALE = 1.5;
    private static final double MARKET_CARD_SCALE = 1.5;
    private static final int MIN_DECK_SIZE = 24; // subject to change
    private static final int MAX_DECK_SIZE = 0; // subject to change, if 0 then no limit
    private static final int MAX_CARD_LIMIT = 3; // subject to change, possibility of per card limit in future
    private static final String CLICK_SOUND = "click2.wav";

    private CardList allCards;
    private CardList deckCards;
    private ViewCardList shownCards;
    private int currentDeck;
    private Canvas canvas;
    private Canvas marketCanvas;
    private GraphicsContext gc;
    private GraphicsContext marketGc;
    private ViewCard hoverCard = null;
    private Card marketCard = null;
    private Image goldenEggs;
    private String separator = ": ";
    private User user;
    private Optional<AddInfoMarkert> addInfo = Optional.empty();

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

        // get user
        user = GameManager.getInstance().getUser();

        // Initialize items
        currentDeck = user.getDeckSlot();
        allCards = getAllCards();
        deckCards = new CardList();
        shownCards = new ViewCardList();

        // deck users decks
        for (int i = 0; i < Player.DECK_SLOTS; i++) {
            deckSelect.getItems().add(i, (i + 1) + separator + user.getDeck(i).getName());
        }
        deckSelect.getSelectionModel().select(currentDeck);
        hideDeckEdit(true);

        // Canvas to draw card library images and names
        pagination = new Pagination((int) Math.ceil((double) allCards.getDistinctSize() /
                CARDS_PER_PAGE), 0);
        pagination.setStyle("-fx-page-information-visible: false;");
        pagination.setPageFactory(new Callback<Integer, Node>() {
            @Override
            public Node call(Integer pageIndex) {
                return createPage();
            }
        });
        cardPane.getChildren().add(pagination);

        // Canvas to draw marketplace card images and names
        marketCanvas = new Canvas(MARKET_CANVAS_WIDTH, MARKET_CANVAS_HEIGHT);
        marketGc = marketCanvas.getGraphicsContext2D();
        marketGc.setFont(new Font(16));
        marketPane.getChildren().add(marketCanvas);
        AnchorPane.setTopAnchor(marketCanvas, 0.0);
        marketTab.setOnSelectionChanged(e -> showMarketPick());
        goldenEggs = new Image(getClass().
                getResourceAsStream("/gameImages/GoldenEggs.png"));

        backButton.setOnAction(this::handleBackButton);

        // MarketPlace buttons
        buyButton.setOnAction(e -> buy(marketCard));
        sellButton.setOnAction(e -> sell(marketCard));
        builderButton.setOnAction(e -> openBuilder());
        marketButton.setOnAction(e -> openMarket());

        //press f4 to open chat room


        // listen to search box for changes
        searchBox.textProperty().addListener(e -> filter(0));
        searchBox.setOnMouseClicked(e -> searchBox.selectAll());

        // handle filter buttons
        minionButton.setOnAction(e -> {
            actionButton.setSelected(false);
            filter(0);
        });
        actionButton.setOnAction(e -> {
            minionButton.setSelected(false);
            filter(0);
        });
        ownedToggle.setOnAction(e -> filter(0));

        // handle deck name objects
        deckName.textProperty().addListener(e -> updateDeckName());
        deckName.setOnAction(e -> hideDeckEdit(true));
        deckNameEdit.setOnAction(e -> hideDeckEdit(false));
        deckSelect.valueProperty().addListener(e -> {
            loadDeck(deckSelect.getSelectionModel().getSelectedIndex());
            GameManager.playClip(CLICK_SOUND);
        });

        // load players deck on startup
        loadDeck(currentDeck);

    }

    /**
     * save deck name to user
     */
    private void updateDeckName() {
        user.setDeck(currentDeck, new Deck(user.getDeck(currentDeck), deckName.getText()));
        deckSelect.getItems().set(currentDeck, (currentDeck + 1) + separator +
                user.getDeck(currentDeck).getName());
    }

    /**
     * set hide deck edit text field and show deck selection combo box
     */
    private void hideDeckEdit(boolean set) {
        GameManager.playClip(CLICK_SOUND);
        deckName.setVisible(!set);
        deckName.setManaged(!set);
        deckSelect.setVisible(set);
        deckSelect.setManaged(set);
        deckNameEdit.setVisible(set);
        deckNameEdit.setManaged(set);
        if (!set) {
            deckName.requestFocus();
        }
    }

    /**
     * Return a new canvas page, set graphics properties, set event handlers
     * and Refresh the card view.
     *
     * @return canvas defined for this page
     */
    private Canvas createPage() {
        canvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
        gc = canvas.getGraphicsContext2D();
        gc.setStroke(Color.BLUE);
        gc.setLineWidth(2.0);
        filter(pagination.getCurrentPageIndex());
        canvas.setOnMouseClicked(this::handleCanvasClick);
        canvas.setOnMouseMoved(this::handleCanvasHover);
        GameManager.playClip("fliping_page.wav");
        return canvas;
    }

    /**
     * Buy a card from the marketplace, modify currency accordingly,
     * and refresh card/marketplace view.
     * Cost of card will be card rank * 100
     *
     * @param card you wish to buy
     */
    private void buy(Card card) {
        if (card == null) {
            return;
        }
        GameManager.playClip(CLICK_SOUND);

        int n = JOptionPane.showConfirmDialog(null, "Please confirm your purchase: \n" + card.getName() + " for " + card.getRank() * 100 + " eggs",
                "Confirm", JOptionPane.YES_NO_OPTION);

        if (n == JOptionPane.YES_OPTION) {
            GameManager.playClip("buy_sell.wav");
            user.unlockCard(card, 1);
            user.modifyCurrency(-(card.getRank() * 100));
            filter(pagination.getCurrentPageIndex());
            showMarketPick();
        }
    }

    /**
     * Sell a card to the marketplace, modify currency accordingly,
     * and refresh card/marketplace view.
     * Selling price will be half of the buying price
     * for all cards.
     *
     * @param card you wish to sell
     */
    private void sell(Card card) {
        if (card == null) {
            return;
        }
        GameManager.playClip(CLICK_SOUND);

        int n = JOptionPane.showConfirmDialog(null, "Please confirm your are selling: \n" + card.getName() + " for " + card.getRank() * 50 + " eggs",
                "Confirm", JOptionPane.YES_NO_OPTION);

        if (n == JOptionPane.YES_OPTION) {
            GameManager.playClip("buy_sell.wav");
            user.removeCard(card, 1);
            user.modifyCurrency(card.getRank() * 50);
            filter(pagination.getCurrentPageIndex());
            showMarketPick();
        }
    }

    /**
     * Filter all cards using current type filter, search box text and owned button
     */
    private void filter(int pageIdx) {
        String type = null;
        if (minionButton.isSelected()) {
            type = "MC";
        } else if (actionButton.isSelected()) {
            type = "AC";
        }
        CardList cards = new CardList();
        if (ownedToggle.isSelected()) {
            for (Card card : allCards.getFiltered(searchBox.getText(),
                    type).getDistinctCards()) {
                if (user.hasCard(card)) {
                    cards.addCard(card);
                }
            }
        } else {
            cards = allCards.getFiltered(searchBox.getText(), type);
        }
        int pages = (int) Math.ceil((double) cards.getDistinctSize() / CARDS_PER_PAGE);
        pagination.setPageCount(pages);
        pagination.setCurrentPageIndex(Math.min(pages, pageIdx));
        showCards(cards);
    }

    /**
     * Load the user deck in slot `num`, then display it
     */
    private void loadDeck(int num) {
        currentDeck = num;
        deckCards = new CardList();
        for (Card card : user.getDeck(currentDeck).getCards()) {
            deckCards.addCard(card);
        }
        deckName.setText(user.getDeck(currentDeck).getName());
        showDeck();
    }

    /**
     * Save the current deck to user if valid
     */
    private void saveDeck() {
        // check invalid deck
        if (deckCards.getSize() < MIN_DECK_SIZE || (deckCards.getSize() > MAX_DECK_SIZE && MAX_DECK_SIZE != 0)) {
            setNotSaved(true);
        } else {
            // deck is valid
            setNotSaved(false);
            Deck deck = new Deck(deckCards, user.getDeck(currentDeck).getName());
            user.setDeck(currentDeck, deck);
        }
    }

    /**
     * Event handler for canvas clicks. Finds clicked card, adds it to deck
     * if the player owns it, opens marketplace otherwise. Also sets card
     * tooltips on single-click.
     *
     * @param event mouse click event
     */
    private void handleCanvasClick(MouseEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        gc.clearRect(519, 0, 178, 58);
        ViewCard clicked = shownCards.getClickedCard(x, y);
        if (clicked == null) {
            // mouse click was not on a card
            cardDetail.setText(null);
            return;
        }
        if (event.getClickCount() == 2) {
            // card was double clicked
            doubleClickCard(clicked.getCard());
        } else if (event.getClickCount() == 1) {
            // card was single clicked
            singleClickCard(clicked.getCard());
        }
    }

    /**
     * when a card is single clicked
     */
    private void singleClickCard(Card clicked) {
        GameManager.playClip(CLICK_SOUND);
        marketCard = clicked;
        showMarketPick();
        if (clicked instanceof MinionCard) {
            cardDetail.setTextFill(Color.WHITE);
            cardDetail.setText(clicked.getName() + "\n" + "Attack=" + ((MinionCard) clicked).getAttack()
                    + " Defense=" + ((MinionCard) clicked).getDefense() + " Rank=" + clicked.getRank());
        } else {
            cardDetail.setText(clicked.getName() + "\n" + " Rank=" + clicked.getRank());
        }
        if (!(clicked.getAction() == null)) {
            gc.drawImage(clicked.getAction().getActionTabImage(), 519, 0, 178, 58);
        }
    }

    /**
     * when a card is double clicked
     */
    private void doubleClickCard(Card clicked) {
        GameManager.playClip("card_flip.wav");
        if (builderTab.isSelected()) {
            // attempt to add card
            if (deckCards.getAmount(clicked) < MAX_CARD_LIMIT) {
                if (user.cardAmount(clicked) <= deckCards.getAmount(clicked)) {
                    // not enough cards open market view to buy
                    openMarket();
                    marketCard = clicked;
                    showMarketPick();
                } else {
                    // card can be added
                    deckCards.addCard(clicked);
                    showDeck();
                    saveDeck();
                }
            }
        } else {
            // in market view so open clicked card
            marketCard = clicked;
            showMarketPick();
        }
    }

    /**
     * Event handler for mouse movement over canvas. Finds the card currently
     * under the cursor and draws blue feedback rectangle.
     *
     * @param event mouse move event
     */
    private void handleCanvasHover(MouseEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        ViewCard hovered = shownCards.getClickedCard(x, y);
        if (hoverCard == null && hovered != null) {
            addInfoPanel(hovered.getCard(), (double) (hovered.getX() + hovered.getWidth()), (double) hovered.getY());
            // on mouse enter card
            hoverCard = hovered;
            gc.setStroke(Color.BLUE);
            gc.setLineWidth(2.0);
            gc.strokeRect(hoverCard.getX() - 2, hoverCard.getY() - 2,
                    hoverCard.getWidth() + 4, hoverCard.getHeight() + 4);

            if (addInfo.isPresent()) {
                addInfo.get().drawPanel(gc);
            }
        } else if (hovered == null && hoverCard != null) {
            removeInfoPanel();
            // on mouse leave card
            gc.setStroke(Color.web("#e2b870"));
            gc.setLineWidth(5.0);
            gc.strokeRect(hoverCard.getX() - 3, hoverCard.getY() - 3,
                    hoverCard.getWidth() + 6, hoverCard.getHeight() + 6);
            hoverCard = null;
            showCards(getAllCards());
        }
    }

    public void addInfoPanel(Card card, double x, double y) {
        addInfo = Optional.of(new AddInfoMarkert(card, x, y));
    }

    public void removeInfoPanel() {
        addInfo = Optional.empty();
    }

    /**
     * Refresh the marketplace view.
     * <p>
     * Draws currently selected card, draws labels for cost, copies and currency.
     */
    private void showMarketPick() {
        double halfWidth = marketCanvas.getWidth() / 2;
        marketGc.setTextAlign(javafx.scene.text.TextAlignment.CENTER);
        if (marketCard != null) {
            marketGc.clearRect(0, 0, MARKET_CANVAS_WIDTH, MARKET_CANVAS_HEIGHT);
            marketGc.fillText(marketCard.getName(), halfWidth, 25);
            ViewCard viewCard = new ViewCard(marketCard, (int) (halfWidth) -
                    (int) (Card.getCardWidth() * MARKET_CARD_SCALE / 2),
                    40, MARKET_CARD_SCALE);
            viewCard.drawCard(marketGc);
            marketGc.fillText(Integer.toString(marketCard.getRank() * 100), halfWidth,
                    85 + viewCard.getHeight());
            marketGc.drawImage(goldenEggs, halfWidth + 35,
                    65 + viewCard.getHeight(), 30, 25);
            marketGc.fillText("Currently have " + user.cardAmount(marketCard)
                    + " copies", halfWidth, 275);

            messageLabel.setText(null);
            // Universal cost of 100 will be replaced with individual card costs.
            if (user.getCurrency() >= marketCard.getRank() * 100) {
                buyButton.setDisable(false);
            } else {
                buyButton.setDisable(true);
            }
            if (user.maxUses(marketCard) < user.cardAmount(marketCard)) {
                sellButton.setDisable(false);
            } else {
                if (user.hasCard(marketCard)) {
                    marketGc.fillText("This card is in use", halfWidth, 470);
                }
                sellButton.setDisable(true);
            }

            //User can only purchase card that rank not more than 1 level high than his player level 
            if (marketCard.getRank() > (user.getLevel() + 1)) {
                buyButton.setDisable(true);
                messageLabel.setText("You are not allowed to buy" + "\n" + "this card at your player level!");
            }
        } else {
            buyButton.setDisable(true);
            sellButton.setDisable(true);
            marketGc.setFill(Color.RED);
            marketGc.fillText("Please select a card\n from the left", halfWidth, 150);
            marketGc.setFill(Color.WHITE);
        }
        marketGc.setFill(Color.YELLOW);
        marketGc.fillText("You Have: " + user.getCurrency(), halfWidth - 15, 425);
        marketGc.drawImage(goldenEggs, halfWidth + 50, 405, 30, 25);
        marketGc.setFill(Color.WHITE);
    }

    /**
     * display the deck in a GridPane
     */
    private void showDeck() {
        // reset deckGrid to empty
        deckGrid.getChildren().setAll();
        int counter = 0;
        for (Card card : deckCards.getDistinctCards()) {
            // create a GridPane row
            Label name = new Label(card.getName());
            Tooltip t = new Tooltip(card.getName());
            Tooltip.install(name, t);
            name.setTextOverrun(OverrunStyle.CLIP);
            Button remove = new Button("x");
            remove.setOnAction(e -> handleRemoveCard(card));
            Label count = new Label("" + deckCards.getAmount(card));
            count.setFont(Font.font("System", FontWeight.BOLD, 12));
            deckGrid.addRow(counter++, count, name, remove);
        }
        // update deck size counter
        deckSize.setText(deckCards.getSize() + "/" + MIN_DECK_SIZE);
        // reset not saved display
        setNotSaved(false);
    }

    /**
     * set deck not saved display
     */
    private void setNotSaved(boolean set) {
        if (set) {
            GameManager.playClip("error.wav");
            deckGrid.setStyle("-fx-background-color: #EC644B");
        } else {
            deckGrid.setStyle("-fx-background-color: #f5f5f5");
        }
        saved.setVisible(set);
        saved.setManaged(set);
    }

    /**
     * Remove `card` from current deck
     */
    private void handleRemoveCard(Card card) {
        GameManager.playClip("card_flip.wav");
        deckCards.removeCard(card);
        showDeck();
        saveDeck();
    }

    /**
     * Refresh/Draw the card view.
     * <p>
     * Clears the canvas, draws the filter title, draws 6 card
     * images (dependent on page) as well as their corresponding labels.
     * Also draws blue rectangle around card being hovered over.
     * Unobtained cards are drawn with alpha = 0.3;
     *
     * @param cards CardList of all library cards after filtering
     */
    private void showCards(CardList cards) {
        shownCards.removeAllCards();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setTextAlign(javafx.scene.text.TextAlignment.CENTER);
        gc.setFill(Color.WHITE);
        String font = "System";
        gc.setFont(Font.font(font, FontWeight.BOLD, 14));
        // update pagination page title to current filter
        String search = "";
        String owned = "";
        String text = "";
        if (!searchBox.getText().isEmpty()) {
            search = " \"" + searchBox.getText() + "\"";
        }
        if (minionButton.isSelected()) {
            text = "Minion ";
        } else if (actionButton.isSelected()) {
            text = "Action ";
        }
        if (ownedToggle.isSelected()) {
            owned = "Owned ";
        }
        gc.fillText(owned + text + "Cards" + search, canvas.getWidth() / 2, 25);
        gc.setFont(Font.font(font, FontWeight.NORMAL, 14));

        for (int i = pagination.getCurrentPageIndex() * CARDS_PER_PAGE;
             i < cards.getSize() &&
                     i < (pagination.getCurrentPageIndex() * CARDS_PER_PAGE +
                             CARDS_PER_PAGE); i++) {
            int x = (int) (125 + ((canvas.getWidth() - 190) / 3) *
                    ((i % CARDS_PER_PAGE) % 3));
            int y = (int) (100 + ((canvas.getHeight() - 100) / 2) *
                    ((i % CARDS_PER_PAGE) / 3));
            Card currentCard = cards.getDistinctCards().get(i);
            gc.fillText(currentCard.getName(), x + 49, y - 20);
            ViewCard scaledCard = new ViewCard(currentCard, x, y, CARD_SCALE);
            shownCards.addCard(scaledCard);
            if (user.cardAmount(scaledCard.getCard()) == 0) {
                gc.setFill(Color.web("#e2b870"));
                gc.fillRect(scaledCard.getX(), scaledCard.getY(),
                        scaledCard.getWidth(), scaledCard.getHeight());
                gc.setGlobalAlpha(0.3);
                gc.setFill(Color.WHITE);
            }
            scaledCard.drawCard(gc);
            gc.setGlobalAlpha(1.0);
        }
    }

    /**
     * Get a list of all cards in game
     */
    private CardList getAllCards() {
        CardList cardList = new CardList();
        // all cards should be listed here

        MinionCard.getCards().values().forEach(cardList::addCard);

        return cardList;
    }

    /**
     * Open the deck builder tab.
     */
    private void openBuilder() {
        GameManager.playClip("switch.wav");
        tabPane.getSelectionModel().select(builderTab);
    }

    /**
     * Open the marketplace tab.
     */
    private void openMarket() {
        GameManager.playClip("switch.wav");
        tabPane.getSelectionModel().select(marketTab);
    }

    /**
     * Handle the back button
     * <p>
     * If the current game is in story mode go to next cutscene if player won
     * or previous cutscene if player lost
     *
     * @param event
     */
    private void handleBackButton(ActionEvent event) {
        if (GameManager.getInstance().getBoss() > 0) {
            // boss match
            if (GameManager.getInstance().getCutscene() == 'b') {
                // player won
                int level = Integer.parseInt(GameManager.getStatisticsTracking()
                        .getUserStats().getUserLevel());
                if (level < 12) {
                    level++;
                    GameManager.getStatisticsTracking().getUserStats().addToUserLevel(1);
                    GameManager.changeScene("StoryCutsceneScreen"
                            + GameManager.getInstance().getBoss() + "b.fxml");
                } else {
                    // max level
                    GameManager.getInstance().setBoss(0);
                    GameManager.changeScene("StoryModeScreen.fxml");
                }

            } else {
                // player lost - try again
                GameManager.changeScene("StoryCutsceneScreen"
                        + GameManager.getInstance().getBoss() + "a.fxml");
            }
        } else {
            // basic match
            GameManager.changeScene("MenuScreen.fxml");
        }

    }

}
