package ru.spbau.mit.Model;

import ru.spbau.mit.Controller;

/**
 * Created by n_buga on 15.12.16.
 */
public class Player extends Character {
    private int priority = 3;

    public Player() {}

    public Player(int power, int healPoints) {
        super(power, healPoints);
    }

    @Override
    public void doTurn(GameState gameState) {
        Turn turn = gameState.getController().awaitTurn(this);
        turn.execute(gameState, this);
    }

    @Override
    public char getSymbol() {
        return '@';
    }

    @Override
    public int getPriority() {
        return priority;
    }
}
