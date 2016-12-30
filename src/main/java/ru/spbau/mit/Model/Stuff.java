package ru.spbau.mit.Model;

/**
 * Created by n_buga on 15.12.16.
 * This class represents the Stuff, that can add player Power or/and HP.
 */
public class Stuff extends MapObject{
    public static final char defaultSymbol = '$';

    private int priority = 1;
    private int addPower = 0;
    private int addHP = 0;

    public Stuff() {}

    public Stuff(int addHP) {
        this(addHP, 0);
    }

    public Stuff(int addHP, int addPower) {
        this.addHP = addHP;
        this.addPower = addPower;
    }

    @Override
    public char getSymbol() {
        return defaultSymbol;
    }

    @Override
    public int getPriority() {
        return priority;
    }

    public int getAddPower() {
        return addPower;
    }

    public int getAddHP() {
        return addHP;
    }
}
