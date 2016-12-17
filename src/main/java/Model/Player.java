package Model;

import java.util.Set;

/**
 * Created by n_buga on 15.12.16.
 */
public class Player extends Character {
    private int priority = 4;

    public Player() {}

    public Player(int power, int healPoints) {
        super(power, healPoints);
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
