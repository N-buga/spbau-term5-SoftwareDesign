package ru.spbau.mit.Model;

/**
 * Created by n_buga on 15.12.16.
 */
public class Wall extends MapObject {
    private int priority = 6;

    @Override
    public int getPriority() {
        return priority;
    }
}
