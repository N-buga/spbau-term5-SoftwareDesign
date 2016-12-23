package ru.spbau.mit.Model;

import java.util.List;

/**
 * Created by n_buga on 15.12.16.
 * This class implements Mob, it realise the turn of mobs, contains the symbal for mobs and so on.
 */
public class Mob extends Character {
    public static final char defaultSymbol = '0';
    private char symbol = defaultSymbol;
    private int priority = 2;

    public Mob() {}

    public Mob(char symbol) {
        this.symbol = symbol;
    }

    public Mob(int power, int healPoints) {
        super(power, healPoints);
    }

    /**
     * If we see the Character in the cell near we try to move there, otherwise we move accidentally.
     * @param gameState
     */
    @Override
    public void doTurn(GameState gameState) {
        for (Position position: getPosition().getNeighbours()) {
            if (gameState.getMap().getCell(position).containsCharacters()) {
                new Move(position).execute(gameState, this);
                return;
            }
        }
        super.doTurn(gameState);
    }


    @Override
    public char getSymbol() {
        return symbol;
    }

    @Override
    public int getPriority() {
        return priority;
    }
}
