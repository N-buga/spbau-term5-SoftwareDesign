package ru.spbau.mit.Model;

/**
 * Created by n_buga on 15.12.16.
 */
public class Mob extends Character {
    private int priority = 1;

    public Mob() {}

    public Mob(int power, int healPoints) {
        super(power, healPoints);
    }

    @Override
    public int getPriority() {
        return priority;
    }
}
