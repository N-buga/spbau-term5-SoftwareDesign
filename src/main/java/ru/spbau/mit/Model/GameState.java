package ru.spbau.mit.Model;

import org.apache.log4j.Logger;
import ru.spbau.mit.Controller;

import java.util.Set;

/**
 * Created by n_buga on 15.12.16.
 * This class stores all information about game and provides it. IT also make a one round of game(method doTurn).
 */
public class GameState {
    private static Logger log = Logger.getLogger(GameState.class);

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
        log.info("GameState created");
    }

    public Player getPlayer() {
        return player;
    }

    public Map getMap() {
        return map;
    }

    /**
     * This method call doTurn for player and all mobs. Then it checks if someone was killed.
     * It doesn't call doTurn if Character's already been killed.
     */
    public void doTurn() {
        log.info("Begin round");

        player.doTurn(this);

        log.info("Player turned");

        mobs.stream().filter(Mob::isAlive).forEach(m -> m.doTurn(this));
        mobs.stream().filter(m -> !m.isAlive()).forEach(m -> map.getCell(m.getPosition()).delete(m));
        mobs.removeIf(m -> m.getHealPoints() <= 0);

        if (player.getHealPoints() <= 0) {
            endGame(false);
            return;
        }
        if (mobs.size() == 0) {
            endGame(true);
        }

        log.info("Ended round");
        controller.repaint();
    }

    /**
     * This method will be called when the game is end.
     * @param isWin - if we won or not?
     */
    public void endGame(boolean isWin) {
        log.info("End game");
        if (isWin) controller.win();
        else controller.loose();
        gameEnd = true;
    }

    public boolean isGameEnd() {
        return gameEnd;
    }

    public Controller getController() {
        return controller;
    }
}
