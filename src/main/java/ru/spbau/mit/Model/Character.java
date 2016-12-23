package ru.spbau.mit.Model;

import org.apache.log4j.Logger;

import java.util.Random;

/**
 * Created by n_buga on 15.12.16.
 * This class provide methods to work with all moving MapObjects.
 */
public abstract class Character extends MapObject {
    private static Logger log = Logger.getLogger(Character.class);

    private Random random = new Random();
    private int power = random.nextInt(100) + 1;
    private int healPoints = random.nextInt(300) + 1;

    public Character() {}

    public Character(int power, int healPoints) {
        this.power = power;
        this.healPoints = healPoints;
        log.info(String.format("Create character HP: %d; Power: %d", healPoints, power));
    }

    public void doTurn(GameState gameState) {
        Turn turn = new Move(getPosition());
        int i = random.nextInt(4);
        switch (i) {
            case 0:
                turn = new Move(getPosition().geti(), getPosition().getj() - 1);
                break;
            case 1:
                turn = new Move(getPosition().geti(), getPosition().getj() + 1);
                break;
            case 2:
                turn = new Move(getPosition().geti() + 1, getPosition().getj());
                break;
            case 3:
                turn = new Move(getPosition().geti() - 1, getPosition().getj());
                break;
        }
        turn.execute(gameState, this);
        log.info("Do turn Character " + this.getSymbol());
    }
    /**
     * Each moving MapObject can do something with objects in the cell it arrived. For example player can take
     * the stuff in the cell.
     */
    void handle(GameState gameState) {}

    public void getDamage(int damage) {
        healPoints -= damage;
        log.info("Get damage Character " + this.getSymbol());
    }

    public boolean isAlive() {
        return healPoints > 0;
    }

    public int getPower() {
        return power;
    }
    public void setPower(int newPower) {
        power = newPower;
    }
    public void setHP(int newHP) {
        healPoints = newHP;
    }
    public int getHealPoints() {
        return healPoints;
    }
    public void draw() {
        System.out.println(getSymbol());
        System.out.printf("HP: %d\nPower:%d\n", getHealPoints(), getPower());
    }
}
