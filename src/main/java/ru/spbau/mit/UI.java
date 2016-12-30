package ru.spbau.mit;


import org.apache.log4j.Logger;
import ru.spbau.mit.Model.*;

import java.io.IOException;
import java.util.Locale;
import java.util.Scanner;

/**
 * Created by n_buga on 17.12.16.
 * UI shows the part of the map, ant implements interaction with user.
 */
public class UI {
    private static Logger log = Logger.getLogger(UI.class);
    private MapObject.Position playerPosition;

    public UI() {}

    public void repaintOut(Player player, Map map) {
        log.info("Begin repaint");
        Cell[][] visibleMap = map.getVisibleArea(player.getPosition());
        for (int i = 0; i < visibleMap.length; i++) {
            for (int j = 0; j < visibleMap[0].length; j++) {
                System.out.print(visibleMap[i][j].getSymbol());
            }
            System.out.println();
        }

        System.out.printf("Your:\nHP: %d; Power: %d\n", player.getHealPoints(), player.getPower());

        for (int i = 0; i < visibleMap.length; i++) {
            for (int j = 0; j < visibleMap[0].length; j++) {
                if (visibleMap[i][j].containsMob()) {
                    Mob mob = visibleMap[i][j].getMob();
                    System.out.printf("Mob %c:\n", mob.getSymbol());
                    System.out.printf("HP: %d; Power: %d\n", mob.getHealPoints(), mob.getPower());
                }
            }
        }
        log.info("End repaint");
    }

    /**
     * You can write any string, but it can see only on the first letter. It needs to be 'w', 'a', 's', or 'd'.
     */
    public Turn awaitTurn(Player player) throws IOException {
        log.info("Await turn from keyboard");
        playerPosition = player.getPosition();
        Scanner scanner = new Scanner(System.in);
        char key = scanner.next().charAt(0);
        switch (key) {
            case 'w':
                return new Move(playerPosition.up());
            case 'a':
                return new Move(playerPosition.left());
            case 's':
                return new Move(playerPosition.down());
            case 'd':
                return new Move(playerPosition.right());
            default:
                return new Move(playerPosition);
        }
    }

    public void paintWin() {
        System.out.println("You're winner!");
    }

    public void paintLoose() {
        System.out.println("You're failed!");
    }
}
