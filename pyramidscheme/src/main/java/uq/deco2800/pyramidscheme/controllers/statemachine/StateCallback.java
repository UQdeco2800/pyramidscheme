package uq.deco2800.pyramidscheme.controllers.statemachine;

public interface StateCallback {

    void goToIdle();

    void gameOver(boolean userWon);

    void turnOver();
}
