package TestModel.TestTurn;

import Model.*;
import javafx.geometry.Pos;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by n_buga on 15.12.16.
 * Builder creates a map using some settings which was given it. It creates some random map.
 */
public class TestBuilder extends Assert{
    @Test
    public void testSizeAndPlayerPosition() {
        final int strLen = 7;
        final int colLen = 1;

        final MapObject.Position playerPos = new MapObject.Position(0, 5);

        StateBuilder stateBuilder = new StateBuilder(colLen, strLen);
        Player player = new Player();
        State state = stateBuilder
                .setPlayerPos(player, playerPos)
                .build();
        assertEquals(state.getMap().getColLen(), colLen);
        assertEquals(state.getMap().getStrLen(), strLen);
        assertTrue(state.getMap().getCell(playerPos).containsPlayer());
        assertTrue(player.getPosition().equals(playerPos));
    }

    @Test
    public void testMobPosition() {
        Mob mob = new Mob();
        final MapObject.Position mobPos = new MapObject.Position(0, 0);
        State state = new StateBuilder()
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

        State state = new StateBuilder()
                .addMob(mob, mobPos)
                .setPlayerPos(player, playerPos)
                .setWalls(true)
                .setWall(wallPos)
                .build();

        assertTrue(mob.getPosition().equals(mobPos));
        assertTrue(player.getPosition().equals(playerPos));
        assertTrue(state.getMap().getCell(wallPos).isWall());
        assertTrue(state.getMap().getCell(playerPos).containsPlayer());
        assertTrue(state.getMap().getCell(mobPos).containsMob());
    }

    @Test(expected = IllegalArgumentException.class)
    public void addMobOnPlayer() {
        Mob mob = new Mob();
        Player player = new Player();

        MapObject.Position pos = new MapObject.Position(0, 0);
        new StateBuilder().addMob(mob, pos).setPlayerPos(player, pos).build();
    }
}
