package ru.spbau.mit;

import jdk.internal.dynalink.linker.GuardedTypeConversion;
import ru.spbau.mit.Model.*;
import ru.spbau.mit.Model.Character;

import java.io.IOException;

/**
 * Created by n_buga on 17.12.16.
 * This class is responsible for interaction UI and Model.
 */
public class Controller {
    private GameState gameState;
    private UI ui;

    public static void main(String[] args) {
        Controller controller = new Controller();
        GameState gameState = new StateBuilder(controller).build();
        controller.setGameState(gameState);
        controller.setUi(new UI(controller));
        controller.repaint();
        for (int i = 0; i < 10; i++) {
            gameState.doTurn();
        }
/*        while (!gameState.isGameEnd()) {
            gameState.doTurn();
        } */
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public void setUi(UI ui) {
        this.ui = ui;
    }

    public void repaint() {
        ui.repaint(gameState.getPlayer(), gameState.getMap());
    }

    public Turn awaitTurn(Player player) {
        try {
            return ui.awaitTurn(player);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Hit();
    }
}
