package ru.spbau.mit.Model;

/**
 * Created by n_buga on 15.12.16.
 * This class represents empty cell.
 */
public class Empty extends MapObject {
    private int priority = 0;

    public Position getPosition() {
        return null;
    }

    @Override
    public char getSymbol() {
            return '.';
    }

    @Override
    public int getPriority() {
        return priority;
    }


}
