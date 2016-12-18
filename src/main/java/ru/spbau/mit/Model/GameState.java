package ru.spbau.mit.Model;

import ru.spbau.mit.Controller;

import java.util.Set;

/**
 * Created by n_buga on 15.12.16.
 * This class stores all information about game and provides it. IT also make a one round of game(method doTurn).
 */
public class GameState {
    private Controller controller;
    private Map map;
    private Set<Mob> mobs;

    private Player player;
    private boolean gameEnd = false;

    public GameState(Map map, Set<Mob> mobs, Player player, Controller controller) {
        this.map = map;
        this.mobs = mobs;
        this.player = player;
        this.controller = controller;
    }

    public Player getPlayer() {
        return player;
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

    public void doTurn() {
        player.doTurn(this);
        controller.repaint();
        for (Mob mob: mobs) {
            mob.doTurn(this);
            controller.repaint();
        }
    }

    public void endGame() {
        gameEnd = true;
    }

    public boolean isGameEnd() {
        return gameEnd;
    }

    public Controller getController() {
        return controller;
    }
}
