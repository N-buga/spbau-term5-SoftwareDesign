package Model;

import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Created by n_buga on 15.12.16.
 * This class provide methods to work with all moving MapObjects.
 */
public abstract class Character extends MapObject {
    private int power = new Random().nextInt(100) + 1;
    private int healPoints = new Random().nextInt(300) + 1;

    public Character() {}

    public Character(int power, int healPoints) {
        this.power = power;
        this.healPoints = healPoints;
    }
    /**
     * Each moving MapObject can do something with objects in the cell it arrived. For example player can take
     * the stuff in the cell.
     * @param newNeighbors
     */
    abstract void handle(Set<MapObject> newNeighbors);

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
}
