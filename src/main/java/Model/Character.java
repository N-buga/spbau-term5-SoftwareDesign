package Model;

import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Created by n_buga on 15.12.16.
 * This class provide methods to work with all moving MapObjects.
 */
public abstract class Character extends MapObject {
    private Random random = new Random();
    private int power = random.nextInt(100) + 1;
    private int healPoints = random.nextInt(300) + 1;

    public Character() {}

    public Character(int power, int healPoints) {
        this.power = power;
        this.healPoints = healPoints;
    }

    public void doTurn(State state) {
        Turn turn;
        int i = random.nextInt(5);
        switch (i) {
            case 1:
                turn = new Move(getPosition().geti(), getPosition().getj() - 1);
                break;
            case 2:
                turn = new Move(getPosition().geti(), getPosition().getj() + 1);
                break;
            case 3:
                turn = new Move(getPosition().geti() + 1, getPosition().getj());
                break;
            case 4:
                turn = new Move(getPosition().geti() - 1, getPosition().getj());
                break;
            default:
                turn = new Hit();
                break;
        }
        turn.execute(state, this);
    }
    /**
     * Each moving MapObject can do something with objects in the cell it arrived. For example player can take
     * the stuff in the cell.
     */
    void handle(State state) {
        Hit hit = new Hit();
        hit.execute(state, this);
    }

    public void getDamage(int damage) {
        healPoints -= damage;
    }

    public boolean isKilled() {
        return healPoints <= 0;
    }

    public int getPower() {
        return power;
    }
    public int getHealPoints() {
        return healPoints;
    }
    public void draw() {
        System.out.println(getSymbol());
        System.out.printf("HP: %d\nPower:%d\n", getHealPoints(), getPower());
    }
}
