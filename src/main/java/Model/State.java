package Model;

import java.util.List;
import java.util.Set;

/**
 * Created by n_buga on 15.12.16.
 */
public class State {
    private Map map;
    private Set<Mob> mobs;
    private Player player;

    public State(Map map, Set<Mob> mobs, Player player) {
        this.map = map;
        this.mobs = mobs;
        this.player = player;
    }

    public Map getMap() {
        return map;
    }

    public void delete(Character character) {
        if (character instanceof Player) {
            endGame();
        } else if (character instanceof Mob){
            mobs.remove(character);
        }
    }

    public void endGame() {
        System.exit(0);
    }
}
