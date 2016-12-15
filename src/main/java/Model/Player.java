package Model;

import java.util.Set;

/**
 * Created by n_buga on 15.12.16.
 */
public class Player extends Character {
    public Player() {}

    public Player(int power, int healPoints) {
        super(power, healPoints);
    }

    public void handle(Set<MapObject> newNeighbors) {
    }
}
