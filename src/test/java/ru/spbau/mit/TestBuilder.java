package ru.spbau.mit;

import org.junit.Test;
import ru.spbau.mit.Model.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 * Created by n_buga on 15.12.16.
 * Builder creates a map using some settings which was given it. It creates some random map.
 */
public class TestBuilder {
    Controller controller = new Controller();

    @Test
    public void testSizeAndPlayerPosition() {
        final int strLen = 7;
        final int colLen = 1;

        final MapObject.Position playerPos = new MapObject.Position(0, 5);

        GameBuilder gameBuilder = new GameBuilder(colLen, strLen, controller);
        Player player = new Player();
        GameState gameState = gameBuilder
                .setPlayerPos(player, playerPos)
                .build();
        assertEquals(gameState.getMap().getColLen(), colLen);
        assertEquals(gameState.getMap().getStrLen(), strLen);
        assertTrue(gameState.getMap().getCell(playerPos).containsPlayer());
        assertTrue(player.getPosition().equals(playerPos));
    }

    @Test
    public void testMobPosition() {
        Mob mob = new Mob();
        final MapObject.Position mobPos = new MapObject.Position(0, 0);
        GameState gameState = new GameBuilder(controller)
                .addMob(mob, mobPos)
                .build();

        assertTrue(mob.getPosition().equals(mobPos));
    }

    @Test
    public void testMobWallPlayerPosition() {
        Mob mob = new Mob();
        Player player = new Player();

        MapObject.Position mobPos = new MapObject.Position(0, 0);
        MapObject.Position playerPos = new MapObject.Position(0, 1);
        MapObject.Position wallPos = new MapObject.Position(0, 2);

        GameState gameState = new GameBuilder(controller)
                .addMob(mob, mobPos)
                .setPlayerPos(player, playerPos)
                .setWall(wallPos)
                .build();

        assertTrue(mob.getPosition().equals(mobPos));
        assertTrue(player.getPosition().equals(playerPos));
        assertTrue(gameState.getMap().getCell(wallPos).isWall());
        assertTrue(gameState.getMap().getCell(playerPos).containsPlayer());
        assertTrue(gameState.getMap().getCell(mobPos).containsMob());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMobOnPlayer() {
        Mob mob = new Mob();
        Player player = new Player();

        MapObject.Position pos = new MapObject.Position(0, 0);
        new GameBuilder(controller).addMob(mob, pos).setPlayerPos(player, pos).build();
    }

    @Test
    public void testStuff() {
        final int addPower = 5;
        final int addHP = 6;

        Stuff stuff = new Stuff(addHP, addPower);
        Player player = new Player();
        Mob mob = new Mob();

        int playerHP = player.getHealPoints();
        int playerPower = player.getPower();
        int mobHP = mob.getHealPoints();
        int mobPower = mob.getPower();

        MapObject.Position begPositionPlayer = new MapObject.Position(1, 0);
        MapObject.Position begPositionMob = new MapObject.Position(0, 1);
        MapObject.Position positionStuff = new MapObject.Position(0, 0);

        GameState gameState = new GameBuilder(new Controller())
                .setPlayerPos(player, begPositionPlayer)
                .addMob(mob, begPositionMob)
                .addStuff(stuff, positionStuff)
                .build();

        Move moveMob = new Move(positionStuff);
        moveMob.execute(gameState, mob);

        assertTrue(positionStuff.equals(mob.getPosition()));

        moveMob = new Move(begPositionMob);
        moveMob.execute(gameState, mob);

        assertTrue(mob.getPosition().equals(begPositionMob));
        assertTrue(gameState.getMap().getCell(positionStuff).contains(stuff));

        Move movePlayer = new Move(positionStuff);
        movePlayer.execute(gameState, player);

        assertTrue(positionStuff.equals(player.getPosition()));
        assertFalse(gameState.getMap().getCell(positionStuff).contains(stuff));

        assertEquals(mob.getHealPoints(), mobHP);
        assertEquals(mob.getPower(), mobPower);
        assertEquals(player.getPower(), playerPower + addPower);
        assertEquals(player.getHealPoints(), playerHP + addHP);

    }

    @Test
    public void testConnectionGenRand() {
        final int colLen = 80;
        final int strLen = 80;
        final int cntRect = 20;
        GameState gameState = new GameBuilder(new Controller()).getRandGame(colLen, strLen, cntRect);
        Map map = gameState.getMap();
        boolean visited[][] = new boolean[colLen][strLen];

        Cell begCell = null;

        for (int i = 0; i < colLen; i++) {
            for (int j = 0; j < strLen; j++) {
                if (!map.getCell(i, j).isWall()) {
                    begCell = map.getCell(i, j);
                    break;
                }
            }
            if (begCell != null) break;
        }

        assertTrue(begCell != null);

        dfs(begCell.getPosition(), visited, map, colLen, strLen);

        for (int i = 0; i < colLen; i++) {
            for (int j = 0; j < strLen; j++) {
                assertTrue(map.getCell(i, j).isWall() || visited[i][j]);
            }
        }
    }

    private void dfs(MapObject.Position position, boolean[][] visited, Map map, int colLen, int strLen) {
        visited[position.geti()][position.getj()] = true;
        position.getNeighbours().stream().forEach(pos -> checkAndGo(pos, visited, map, colLen, strLen));
    }

    private void checkAndGo(MapObject.Position pos, boolean[][] visited, Map map, int colLen, int strLen) {
        if (pos.checkBounds(colLen, strLen) && !map.getCell(pos).isWall() && !visited[pos.geti()][pos.getj()]) {
            dfs(pos, visited, map, colLen, strLen);
        }
    }
}
