package ru.spbau.mit;

import ru.spbau.mit.Model.*;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by n_buga on 15.12.16.
 * Builder creates a map using some settings which was given it. It creates some random map.
 */
public class TestBuilder extends Assert{
    Controller controller = new Controller();

    @Test
    public void testSizeAndPlayerPosition() {
        final int strLen = 7;
        final int colLen = 1;

        final MapObject.Position playerPos = new MapObject.Position(0, 5);

        StateBuilder stateBuilder = new StateBuilder(colLen, strLen, controller);
        Player player = new Player();
        GameState gameState = stateBuilder
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
        GameState gameState = new StateBuilder(controller)
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

        GameState gameState = new StateBuilder(controller)
                .addMob(mob, mobPos)
                .setPlayerPos(player, playerPos)
                .setWalls(true)
                .setWall(wallPos)
                .build();

        assertTrue(mob.getPosition().equals(mobPos));
        assertTrue(player.getPosition().equals(playerPos));
        assertTrue(gameState.getMap().getCell(wallPos).isWall());
        assertTrue(gameState.getMap().getCell(playerPos).containsPlayer());
        assertTrue(gameState.getMap().getCell(mobPos).containsMob());
    }

    @Test(expected = IllegalArgumentException.class)
    public void addMobOnPlayer() {
        Mob mob = new Mob();
        Player player = new Player();

        MapObject.Position pos = new MapObject.Position(0, 0);
        new StateBuilder(controller).addMob(mob, pos).setPlayerPos(player, pos).build();
    }
}
