package uq.deco2800.pyramidscheme.match;

import uq.deco2800.pyramidscheme.board.RecBoard;
import uq.deco2800.pyramidscheme.board.RecTile;
import uq.deco2800.pyramidscheme.cards.supercards.MinionCard;
import uq.deco2800.pyramidscheme.deck.Deck;
import uq.deco2800.pyramidscheme.player.Player;
import uq.deco2800.pyramidscheme.pyramid.Pyramid;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hugokawamata on 30/08/2016.
 */
public class MatchAI extends MatchPlayer {

    private RecBoard board;
    private Pyramid pyramid;

    public MatchAI(Player player) {
        super(player, false);
    }

    /**
     * Gives the AI the board to reason with, which allows AI to "see" the
     * user's board positions and make decisions.
     *
     * @param board the board being used in the current match.
     */
    public void setBoard(RecBoard board) {
        this.board = board;
    }

    /**
     * Gives the AI its own pyramid so it can see which cards it can play and
     * remove them when it plays the cards.
     *
     * @param pyramid the AI's pyramid.
     */
    public void setPyramid(Pyramid pyramid) {
        this.pyramid = pyramid;
    }

    /**
     * Gets the list of tiles that are not owned by the AI (ie, the user's tiles)
     * This makes checking tile ownership much quicker.
     *
     * @return the tiles in board that are owned by user.
     */
    public List<RecTile> getUserTiles() {
        ArrayList<RecTile> userCards = new ArrayList<>();
        for (RecTile tile : board) {
            if (!tile.getOwner().equals(this)) {
                userCards.add(tile);
            }
        }

        return userCards;
    }

    /**
     * The AI chooses a card to play and a place to play the card in.
     *
     * @return A list containing the card to play and the place to play it (respectively).
     */
    public List<Object> playCard() {
        // Set up return array
        List<Object> returnList = new ArrayList<>(2);
        // First, check if user has lethal
        RecTile scarySpot = checkDeadOnBoard();
        // If it does, pick a minion to play in that spot
        if (scarySpot != null) {
            returnList.add(pickMinion(scarySpot));
            returnList.add(scarySpot);
            return returnList;
        }

        returnList.add(yankFromPyramid());
        returnList.add(choosePosition());
        return returnList;
    }

    /**
     * Picks a minion to play to spot and returns it as a MatchCard.
     *
     * @param spot the place to play the minion card.
     * @return the best case minion to play on spot as a MatchCard.
     */
    private MatchCard pickMinion(RecTile spot) {
        MatchCard bestCase = null;
        for (MatchCard pCard : pyramid) {
            if (duckDustPool.isPlayable(pCard.getCard().getCost())) {
                if (bestCase == null) {
                    bestCase = pCard;
                } else if (imagineCombat(bestCase, spot.getOppositeTile().getContents().get())
                        > imagineCombat(pCard, spot.getOppositeTile().getContents().get())) {
                    bestCase = pCard;
                }
            }
        }
        return bestCase;
    }

    /**
     * Gets a list of cards from user board, which satisfy the best combat case.
     * This method is a little hard to understand so here are some examples.
     * See the wiki page for combat terminology if you're unfamiliar.
     * 1. The user board contains 1 minion the AI can free kill
     * and 2 minions the AI can trade with. In this case, the method will return
     * a list of 1 RecTile (the one the AI can free kill).
     * 2. The user board contains 2 minions the AI can trade with and 1 minion
     * the AI stalemates with. In this case the method will return a list of 2
     * RecTiles (the ones the AI can trade with).
     * Ping me (@hugokawamata) if you need to know more about AI predicting combat.
     *
     * @return An arraylist of cards from the user board which the AI deems the
     * best to counterplay against.
     */
    List<RecTile> getKillableCards() {

        // List of cards that the ai can kill and survive.
        ArrayList<RecTile> caseOnes = new ArrayList<>();
        // List of cards that the ai can trade with (kill its minion and the user's)
        ArrayList<RecTile> caseTwos = new ArrayList<>();
        // List of cards that stalemate with the user's cards
        ArrayList<RecTile> caseThrees = new ArrayList<>();

        for (MatchCard pCard : this.pyramid) {
            if (duckDustPool.isPlayable(pCard.getCard().getCost())) {
                for (RecTile userTile : getUserTiles()) {
                    // If there is a user card present in the tile
                    // AND there is no ai card in the opposite tile.
                    if (userTile.getContents().isPresent() &&
                            !userTile.getOppositeTile().getContents().isPresent() &&
                            !caseOnes.contains(userTile) &&
                            !caseTwos.contains(userTile) &&
                            !caseThrees.contains(userTile)) {
                        // Imagine combat between the pyramid card the ai is looking at
                        // and the user card the ai is looking at
                        switch (imagineCombat(pCard, userTile.getContents().get())) {
                            case 1:
                                caseOnes.add(userTile);
                                break;
                            case 2:
                                caseTwos.add(userTile);
                                break;
                            case 3:
                                caseThrees.add(userTile);
                                break;
                            default:
                                break;
                        }
                    }
                }
            }
        }

        if (!caseOnes.isEmpty()) {
            return caseOnes;
        } else if (!caseTwos.isEmpty()) {
            return caseTwos;
        } else if (!caseThrees.isEmpty()) {
            return caseThrees;
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * The AI chooses a card from the pyramid to play in front of a user minion
     * it deems the best one. This is determined partly by whether it can kill it
     * and also the value of the user minion.
     *
     * @return the minion it chooses from its pyramid.
     * @require highestValueEnemy is either a null card or has an empty opposite tile.
     * This is ensured in getKillableCards.
     */
    public MatchCard yankFromPyramid() {
        MatchCard highestValueEnemy = new MatchCard(Deck.getNullCard(), 0, 0);
        MatchCard highestValuePyramid = new MatchCard(Deck.getNullCard(), 0, 0);
        MatchCard pyramidCard = null;
        int bestCase = 4;

        // Goes through the cards the AI can respond to in the best possible case
        // and picks the highest value one.
        // highestValueEnemy will be a null card if no killable cards.
        for (RecTile userTile : getKillableCards()) {
            MatchCard userCard = userTile.getContents().get();
            if (userCard.getValue() > highestValueEnemy.getValue()) {
                highestValueEnemy = userCard;
            }
        }

        // Can't kill any enemies! Play AI's best card in a safe zone
        if (highestValueEnemy.getCard().equals(Deck.getNullCard())) {
            for (MatchCard pCard : pyramid) {
                if (duckDustPool.isPlayable(pCard.getCard().getCost()) &&
                        pCard.getValue() > highestValuePyramid.getValue()) {
                    highestValuePyramid = pCard;
                }
            }
            return highestValuePyramid;
        }

        for (MatchCard pCard : pyramid) {
            if (duckDustPool.isPlayable(pCard.getCard().getCost())) {
                switch (imagineCombat(pCard, highestValueEnemy)) {
                    // If one exists, grab a card that satisfies case 1
                    case 1:
                        return pCard;
                    // If there's no case 1s, make pyramidCard be the current card
                    case 2:
                        if (bestCase > 2) {
                            bestCase = 2;
                            pyramidCard = pCard;
                        }
                        break;
                    case 3: // If there's no case 2s, make pyramidCard be the current card
                        if (bestCase > 3) {
                            bestCase = 3;
                            pyramidCard = pCard;
                        }
                        break;
                    default:
                        break;
                }
            }
        }

        return pyramidCard;

    }

    /**
     * AI uses this method to choose where to play the minion it chooses.
     * This method and yankFromPyramid should be used in succession. If they are
     * not, they will not make sense. This is because highestValueEnemy is the ideal
     * enemy the AI is responding to for the current board state.
     * Assumes highestValueEnemy.getOppositeTile is an empty RecTile.
     * This is ensured in getKillableCards.
     *
     * @return an empty RecTile for the AI to play a card in.
     */
    public RecTile choosePosition() {
        RecTile highestValueEnemy = null;
        for (RecTile userTile : getKillableCards()) {
            if (highestValueEnemy == null) {
                highestValueEnemy = userTile;
            } else {
                MatchCard userCard = userTile.getContents().get();
                if (userCard.getValue() > highestValueEnemy.getContents().get().getValue()) {
                    highestValueEnemy = userTile;
                }
            }
        }

        // There was no killable cards
        if (highestValueEnemy == null) {
            for (RecTile tile : board) {
                if (!tile.getContents().isPresent() &&
                        !tile.getOppositeTile().getContents().isPresent() &&
                        tile.getOwner() == this) {
                    return tile;
                }
            }
        }

        // There was a killable card, return the opposite tile,
        // otherwise return null.
        return highestValueEnemy == null ? null : highestValueEnemy.getOppositeTile();

    }

    /**
     * This method allows the AI to imagine combat between one of its cards and
     * one of the user's cards. Thus, the AI can see the consequences of an attack
     * before it makes it.
     *
     * @param aiCard   the card that the ai is using
     * @param userCard the card the ai is imagining combat with
     * @return an int representing the result of combat.
     * -1: invalid cards (not both minions).
     * 1: AI can kill user minion and survive.
     * 2: AI kills user minion but also dies.
     * 3: Neither card kills the other.
     * 4: User card kills AI card and survives.
     */
    int imagineCombat(MatchCard aiCard, MatchCard userCard) {
        if (!(aiCard.getCard() instanceof MinionCard)
                || !(userCard.getCard() instanceof MinionCard)) {
            // Not valid cards.
            return -1;
        }
        // If AI card can kill user card
        if (((MinionCard) aiCard.getCard()).getAttack() >= userCard.getHealth()) {
            // If user card can kill AI card
            if (((MinionCard) userCard.getCard()).getAttack() >= aiCard.getHealth()) {
                // AI can kill user minion but also dies
                return 2;
            }
            // AI can kill user minion and survive
            return 1;
        }
        // AI can't kill user minion at all
        if (((MinionCard) userCard.getCard()).getAttack() < aiCard.getHealth()) {
            // User card can't kill ai card either
            return 3;
        }
        return 4;
    }

    /**
     * Checks whether the AI is dead next turn with the current minions on the board.
     *
     * @return the RecTile opposite the biggest, scariest minion owned by user
     * if the user has lethal. If they don't, return null.
     */
    RecTile checkDeadOnBoard() {
        // The amount of damage the user will do next turn
        int totalDamage = 0;
        // The scariest minion owned by user
        RecTile scary = null;

        for (RecTile userTile : board) {
            if (!userTile.getOppositeTile().getContents().isPresent() &&
                    !userTile.getOwner().equals(this) &&
                    userTile.getContents().isPresent() &&
                    userTile.getContents().get().getCard() instanceof MinionCard) {
                // All of the above is a for loop for checking the unblocked
                // user minions on the board
                totalDamage += ((MinionCard) userTile.getContents().get().getCard()).getAttack();
                if (scary == null) {
                    scary = userTile;
                } else if (((MinionCard) userTile.getContents().get().getCard()).getAttack()
                        > ((MinionCard) scary.getContents().get().getCard()).getAttack()) {
                    scary = userTile;
                }
            }
        }
        if (totalDamage >= getHealth() && scary != null) {
            return scary.getOppositeTile();
        }
        return null;
    }

    /**
     * The AI checks if it has a card it can play with the current dust in its pyramid.
     *
     * @return whether there is a playable card in the pyramid.
     */
    public boolean canContinuePlaying() {
        for (MatchCard pCard : pyramid) {
            if (duckDustPool.isPlayable(pCard.getCost())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Selects a card in the pyramid to grind to increase duck dust
     * Grinds the card before animation.
     *
     * @return returns the matchcard to be animated to the grinder
     */
    public MatchCard pickToGrind() {
        // If we can have more dust
        if (duckDustPool.getMaxDust() < duckDustPool.getDustCap()) {
            for (MatchCard pCard : pyramid) {
                // Card costs too much to viably keep it in hand
                if (pCard.getCost() > duckDustPool.getMaxDust() + 2) {
                    duckDustPool.grind();
                    return pCard;
                }
                // Card costs too little at this stage of the game
                if (pCard.getCost() < duckDustPool.getMaxDust() - 3) {
                    duckDustPool.grind();
                    return pCard;
                }
            }
            // There are no cards that are very out of bounds, now
            // Attempt to grind the first card that player cannot play.
            for (MatchCard pCard : pyramid) {
                if (pCard.getCost() > duckDustPool.getMaxDust()) {
                    duckDustPool.grind();
                    return pCard;
                }
            }
            // There are no cards that player cannot play. Grind first card in pyramid
            for (MatchCard pCard : pyramid) {
                duckDustPool.grind();
                return pCard;
            }
        }
        return null;
    }

    /**
     * Tells the duck dust pool the turn is over so a card can be ground again
     */
    public void aiTurnOver() {
        duckDustPool.playerTurnOver();
    }
}
