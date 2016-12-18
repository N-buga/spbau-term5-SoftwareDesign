package ru.spbau.mit;

import ru.spbau.mit.Model.*;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by n_buga on 17.12.16.
 */
public class UI {
    private Controller controller;
    private MapObject.Position playerPosition;

    public UI(Controller controller) {
        this.controller = controller;
    }
    public void repaint(Player player, Map map) {
        map.draw();
        System.out.printf("HP: %d; Power: %d\n", player.getHealPoints(), player.getPower());
    }

    public Turn awaitTurn(Player player) throws IOException {
        playerPosition = player.getPosition();
        Scanner scanner = new Scanner(System.in);
        char key = scanner.next().charAt(0);
        while (key != 'w' && key != 'a' && key != 's' && key != 'd') {
            key = scanner.next().charAt(0);
        }
        switch (key) {
            case 'w':
                return new Move(createNewPos(-1, 0));
            case 'a':
                return new Move(createNewPos(0, -1));
            case 's':
                return new Move(createNewPos(1, 0));
            case 'd':
                return new Move(createNewPos(0, 1));
            default:
                return new Hit();
        }
    }

    public MapObject.Position createNewPos(int deltai, int deltaj) {
        return new MapObject.Position(playerPosition.geti() + deltai, playerPosition.getj() + deltaj);
    }
/*    public void endAwaitTurn() {
        isAllowedType = false;
    }

    public void handleTurn(int deltai, int deltaj) {
        lock.lock();
        if (isAllowedType) {
            endAwaitTurn();
            lock.unlock();
            controller.gotTurn(new Move(playerPosition.geti() - 1, playerPosition.getj()));
        }
    }

    public void keyTyped(KeyEvent e) {
        int keyCode = e.getKeyCode();
        switch( keyCode ) {
            case KeyEvent.VK_UP:
                handleTurn(-1, 0);
                break;
            case KeyEvent.VK_DOWN:
                handleTurn(1, 0);
                break;
            case KeyEvent.VK_LEFT:
                handleTurn(0, -1);
                break;
            case KeyEvent.VK_RIGHT :
                handleTurn(0, 1);
                break;
        }
    }

    public void keyPressed(KeyEvent e) {

    }

    public void keyReleased(KeyEvent e) {

    } */

}
