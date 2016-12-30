package ru.spbau.mit;

import ru.spbau.mit.Model.*;
import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

/**
 * Created by n_buga on 15.12.16.
 * All turns influence State somehow. It checks that Move change State in proper way.
 */
public class TestMove {
    private Controller controller = new Controller();

    @Test
    public void simplePlayerAndMobCheckMovesWithoutWalls() {
        int iStart = GameBuilder.defaultColLen/2;
        int jStart = GameBuilder.defaultStrLen/2;

        final MapObject.Position newPos = new MapObject.Position(iStart - 1, jStart - 1);

        Player player = new Player();
        Mob mob = new Mob();

        GameState gameState = new GameBuilder(controller).setPlayerPos(player, iStart, jStart).build();
        Move move = new Move(newPos);
        move.execute(gameState, player);

        assertTrue(newPos.equals(player.getPosition()));

        gameState = new GameBuilder(controller).addMob(mob, iStart, jStart).build();
        move = new Move(newPos);
        move.execute(gameState, mob);

        assertTrue(newPos.equals(mob.getPosition()));

        gameState = new GameBuilder(controller).setPlayerPos(player, iStart, jStart).addMob(mob, iStart + 1, jStart + 1).build();
        Move move1 = new Move(newPos);
        Move move2 = new Move(new MapObject.Position(newPos.geti() + 1, newPos.getj()));

        move1.execute(gameState, player);
        move2.execute(gameState, mob);

        assertTrue(player.getPosition().equals(newPos));
        assertTrue(mob.getPosition().equals(new MapObject.Position(newPos.geti() + 1, newPos.getj())));
    }

    @Test
    public void playerAndMobGoToWall() {
        final MapObject.Position start = new MapObject.Position(GameBuilder.defaultColLen/2,
                GameBuilder.defaultStrLen/2);
        final MapObject.Position wallPos = new MapObject.Position(0, 0);

        Player player = new Player();
        Mob mob = new Mob();

        GameState gameState = new GameBuilder(controller)
                .setPlayerPos(player, start)
                .setWall(wallPos)
                .build();
        Move move = new Move(wallPos);
        move.execute(gameState, player);

        gameState = new GameBuilder(controller)
                .addMob(mob, start)
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
        GameState gameState = new GameBuilder(colLen, strLen, controller).setPlayer(player).build();

        assertTrue(player.getPosition().equals(new MapObject.Position(0, 0)));

        move1.execute(gameState, player);

        assertTrue(player.getPosition().equals(new MapObject.Position(0, 0)));

        move2.execute(gameState, player);

        assertTrue(player.getPosition().equals(new MapObject.Position(0, 0)));
    }

    @Test
    public void playerMoveToMob_MobMoveToPlayer() {
        final MapObject.Position playerPos = new MapObject.Position(GameBuilder.defaultColLen/2,
                GameBuilder.defaultStrLen/2);
        final MapObject.Position mobPos = new MapObject.Position(0, 0);
        Player player = new Player();
        Mob mob = new Mob();
        int mobHPBefore = mob.getHealPoints();
        int playerHPBefore = player.getHealPoints();
        GameState gameState = new GameBuilder(new Controller())
                .setPlayerPos(player, playerPos)
                .addMob(mob, mobPos)
                .build();

        Move playerMove = new Move(mobPos);
        playerMove.execute(gameState, player);

        assertEquals(mob.getHealPoints(), mobHPBefore - player.getPower());
        assertTrue(player.getPosition().equals(playerPos));

        Move mobMove = new Move(playerPos);
        mobMove.execute(gameState, mob);

        assertEquals(player.getHealPoints(), playerHPBefore - mob.getPower());
        assertTrue(mob.getPosition().equals(mobPos));
    }

    @Test
    public void mobMoveToPlayer() {

    }
}
