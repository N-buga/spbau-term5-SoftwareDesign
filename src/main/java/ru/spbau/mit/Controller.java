package ru.spbau.mit;

import org.apache.log4j.Logger;
import ru.spbau.mit.Model.*;

import java.io.IOException;

/**
 * Created by n_buga on 17.12.16.
 * This class is responsible for interaction UI and Model.
 */
public class Controller {
    private static Logger log = Logger.getLogger(Controller.class);
    private GameState gameState;
    private UI ui;

    public static void main(String[] args) {
        log.info("Game start");
        Controller controller = new Controller();
        GameState gameState = (new GameBuilder(controller)).getRandGame(11, 13, 5);
        if (gameState == null) {
            System.out.println("Too many rooms for size");
            return;
        }
        controller.setGameState(gameState);
        controller.setUi(new UI());
        controller.repaint();
        while (!gameState.isGameEnd()) {
            gameState.doTurn();
        }
        log.info("Game end");
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public void setUi(UI ui) {
        this.ui = ui;
    }

    public void repaint() {
        ui.repaintOut(gameState.getPlayer(), gameState.getMap());
    }

    public Turn awaitTurn(Player player) {
        log.info("Await player turn");
        try {
            return ui.awaitTurn(player);
        } catch (IOException e) {
            System.out.println("Error when was reading. Try one more time.");
        }
        log.info("Get player turn");
        return new Move(player.getPosition());
    }

    public void win() {
        ui.paintWin();
    }

    public void loose() {
        ui.paintLoose();
    }

    public void errorUI() {
        System.out.println("Error with UI!");
    }
}
