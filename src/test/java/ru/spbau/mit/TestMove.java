package ru.spbau.mit;

import ru.spbau.mit.Model.*;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by n_buga on 15.12.16.
 * All turns influence State somehow. It checks that Move change State in proper way.
 */
public class TestMove extends Assert{
    private Controller controller = new Controller();

    @Test
    public void simplePlayerAndMobCheckMovesWithoutWalls() {
        int iStart = StateBuilder.defaultColLen/2;
        int jStart = StateBuilder.defaultStrLen/2;

        final MapObject.Position newPos = new MapObject.Position(0, 0);

        Player player = new Player();
        Mob mob = new Mob();

        GameState gameState = new StateBuilder(controller).setPlayerPos(player, iStart, jStart).build();
        Move move = new Move(newPos);
        move.execute(gameState, player);

        gameState = new StateBuilder(controller).addMob(mob, iStart, jStart).build();
        move = new Move(newPos);
        move.execute(gameState, mob);

        assertTrue(newPos.equals(mob.getPosition()));

        gameState = new StateBuilder(controller).setPlayerPos(player, iStart, jStart).addMob(mob, iStart + 1, jStart + 1).build();
        Move move1 = new Move(newPos);
        Move move2 = new Move(new MapObject.Position(newPos.geti() + 1, newPos.getj()));

        move1.execute(gameState, player);
        move2.execute(gameState, mob);

        assertTrue(player.getPosition().equals(newPos));
        assertTrue(mob.getPosition().equals(new MapObject.Position(newPos.geti() + 1, newPos.getj())));
    }

    @Test
    public void playerAndMobGoToWall() {
        final MapObject.Position start = new MapObject.Position(StateBuilder.defaultColLen/2,
                StateBuilder.defaultStrLen/2);
        final MapObject.Position wallPos = new MapObject.Position(0, 0);

        Player player = new Player();
        Mob mob = new Mob();

        GameState gameState = new StateBuilder(controller)
                .setPlayerPos(player, start)
                .setWalls(true)
                .setWall(wallPos)
                .build();
        Move move = new Move(wallPos);
        move.execute(gameState, player);

        gameState = new StateBuilder(controller)
                .addMob(mob, start)
                .setWalls(true)
                .setWall(wallPos)
                .build();
        move = new Move(wallPos);
        move.execute(gameState, mob);

        assertTrue(start.equals(player.getPosition()));
        assertTrue(start.equals(mob.getPosition()));
    }

    @Test
    public void playerMoveOutOfBounds() {
        final int colLen = 5;
        final int strLen = 5;
        Player player = new Player();
        Move move1 = new Move(-1, 0);
        Move move2 = new Move(0, 5);
        GameState gameState = new StateBuilder(colLen, strLen, controller).setPlayer(player).build();

        assertTrue(player.getPosition().equals(new MapObject.Position(0, 0)));

        move1.execute(gameState, player);

        assertTrue(player.getPosition().equals(new MapObject.Position(0, 0)));

        move2.execute(gameState, player);

        assertTrue(player.getPosition().equals(new MapObject.Position(0, 0)));
    }
}
