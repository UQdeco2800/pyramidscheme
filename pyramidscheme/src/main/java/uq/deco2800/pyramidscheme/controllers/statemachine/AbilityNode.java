package uq.deco2800.pyramidscheme.controllers.statemachine;

import uq.deco2800.pyramidscheme.animations.AnimationCallback;
import uq.deco2800.pyramidscheme.animations.UserAnimations;
import uq.deco2800.pyramidscheme.animations.emitter.Emitter;
import uq.deco2800.pyramidscheme.champions.abilities.Ability;
import uq.deco2800.pyramidscheme.champions.abilities.MinionRevive;
import uq.deco2800.pyramidscheme.champions.abilities.MinionSummon;
import uq.deco2800.pyramidscheme.champions.abilities.TargetAbility;
import uq.deco2800.pyramidscheme.duckdust.DuckDustPool;
import uq.deco2800.pyramidscheme.game.GameManager;
import uq.deco2800.pyramidscheme.match.Match;
import uq.deco2800.pyramidscheme.match.MatchCard;

/**
 * Created by Nick on 19/09/2016. The state invoked when a champion ability
 * button is pressed
 */
public class AbilityNode extends StateNode implements AnimationCallback {

    private GameManager gameManager;
    private Integer abilityId;
    private UserAnimations animations = new UserAnimations(gs, this);
    private DuckDustPool duckDust;

    public AbilityNode(Match match, StateCallback stateCallback, Integer abilityId, DuckDustPool duckDustPool) {
        super(match, stateCallback);

        logger.info("AbilityNode Node activated");

        this.gameManager = GameManager.getInstance();
        this.abilityId = abilityId;
        this.duckDust = duckDustPool;

        // change the cursor being used depending on the ability
        GameManager.setCursorAbility(abilityId);
    }

    @Override
    public StateNode processClick(int x, int y) {

        this.getClicked(x, y);
        if (newClickOnTile.get().isPresent() && newClickOnTile.get().get().getContents().isPresent()) {
            MatchCard target = newClickOnTile.get().get().getContents().get();
            Ability ability = gameManager.getSelectedChamp().getAbility(abilityId);

            // Use ability
            ability.activateAbility(target, null);
            duckDust.spend(ability.getCost());
            // Animate ability
            Emitter emitter = ability.getEmitter();
            animations.emit(emitter, target);

            // attempt to send to multiplayer
            sendMultiplayerAbility(gameManager.getSelectedChamp().getName(), abilityId, target);

            if (!gs.getBoard().getDeadTiles().isEmpty()) {
                animations.animateGraveyard(match);
            } else {
                return new Idle(match, stateCallback);
            }
        } else {
            return new Idle(match, stateCallback);
        }

        return this;
    }

    private void sendMultiplayerAbility(String name, Integer abilityId, MatchCard target) {
        if (gameManager.getMultiplayerClient().isPresent()) {
            gameManager.getMultiplayerClient().get().sendChampionAbility(name, abilityId, target.getUid());
        }
    }

    @Override
    public StateNode processMoved(int x, int y) {
        return this;
    }

    public StateNode checkAbilities(Ability ability) {
        // retrieve the selected ability
        if (ability instanceof TargetAbility) {
            return this;
        }

        if (ability instanceof MinionRevive) {
            // check if there are any available spaces on user's side
            if (gs.getBoard().getTilesOf(match.getUser()).isEmpty()) {
                logger.info("Cannot revive minion as Board is Full");
                return new Idle(match, stateCallback);
            }
            // revive a minion
            ability.activateAbility(gs.getUserGraveyard(), gs.getBoard().getTilesOf(match.getUser()).get(0));
            duckDust.spend(ability.getCost());
            return new Idle(match, stateCallback);
        }

        if (ability instanceof MinionSummon) {
            // check if there are any available spaces on user's side
            if (gs.getBoard().getTilesOf(match.getUser()).isEmpty()) {
                logger.info("Cannot summon minion as board is full");
                return new Idle(match, stateCallback);
            }
            // summon a minion
            ability.activateAbility(gs.getBoard().getTilesOf(match.getUser()).get(0), null);
            duckDust.spend(ability.getCost());
            return new Idle(match, stateCallback);
        }

        return new Idle(match, stateCallback);
    }

    @Override
    public void aiPlayingDone() {
        // do nothing because done
    }

    @Override
    public void userRefillDone() {
        // do nothing because done
    }

    @Override
    public void graveyardDone() {
        stateCallback.goToIdle();
    }

    @Override
    public void grindDone() {
        // do nothing because done
    }

    @Override
    public void flipDone() {
        // do nothing because done
    }

    @Override
    public void aiRefillDone() {
        // do nothing because done
    }
}