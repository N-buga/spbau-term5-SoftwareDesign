package ru.spbau.mit;

import ru.spbau.mit.Model.*;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by n_buga on 16.12.16.
 */
public class TestHit extends Assert{
    Controller controller = new Controller();

    @Test
    public void testOneCharacterInCell() {
        Player player = new Player();
        Mob mob = new Mob();
        GameState gameState = new StateBuilder(5, 5, controller).setPlayerPos(player, 0, 0).addMob(mob, 0, 1).build();
        int hpPlayerBefore = player.getHealPoints();
        int hpMobBefore = mob.getHealPoints();
        int powerPlayerBefore = player.getPower();
        int powerMobBefore = mob.getPower();
        MapObject.Position positionPlayerBefore = player.getPosition();
        MapObject.Position positionMobBefore = mob.getPosition();
        Hit hit = new Hit();
        MapObject.Position positionPlayerAfter = player.getPosition();
        MapObject.Position positionMobAfter = mob.getPosition();
        int hpPlayerAfter = player.getHealPoints();
        int hpMobAfter = mob.getHealPoints();
        int powerPlayerAfter = player.getPower();
        int powerMobAfter = mob.getPower();
        hit.execute(gameState, player);

        assertTrue(positionPlayerBefore.equals(positionPlayerAfter));
        assertTrue(positionMobBefore.equals(positionMobAfter));
        assertEquals(hpMobBefore, hpMobAfter);
        assertEquals(hpPlayerBefore, hpPlayerAfter);
        assertEquals(powerMobBefore, powerMobAfter);
        assertEquals(powerPlayerBefore, powerPlayerAfter);

    }

    @Test
    public void testPlayerAndMobInCell() {
        Player player = new Player();
        Mob mob = new Mob();
        MapObject.Position begPlayerPosition = new MapObject.Position(0, 0);
        MapObject.Position begMobPosition = new MapObject.Position(0, 1);
        GameState gameState = new StateBuilder(controller).addMob(mob, begMobPosition).setPlayerPos(player, begPlayerPosition).build();
        Move move = new Move(begMobPosition);
        int hpPlayerBefore = player.getHealPoints();
        int hpMobBefore = mob.getHealPoints();
        int powerPlayerBefore = player.getPower();
        int powerMobBefore = mob.getPower();
        move.execute(gameState, player);
        assertTrue(player.getPosition().equals(mob.getPosition()));
        assertEquals(hpMobBefore - powerPlayerBefore, mob.getHealPoints());
        assertEquals(hpPlayerBefore, player.getHealPoints());
        if (!mob.isKilled()) {
            Hit hit = new Hit();
            hit.execute(gameState, mob);
            assertEquals(hpPlayerBefore - powerMobBefore, player.getHealPoints());
            assertEquals(hpMobBefore - powerPlayerBefore, mob.getHealPoints());
        }
    }

    @Test
    public void testPlayerAndTwoMobs() {
        Player player = new Player();
        Mob mob1 = new Mob();
        Mob mob2 = new Mob();
        MapObject.Position begPlayerPosition = new MapObject.Position(0, 0);
        MapObject.Position begMobPosition = new MapObject.Position(0, 1);
        GameState gameState = new StateBuilder(controller)
                .addMob(mob1, begMobPosition)
                .addMob(mob2, begMobPosition)
                .setPlayerPos(player, begPlayerPosition)
                .build();
        Move move = new Move(begMobPosition);
        int hpMob1Before = mob1.getHealPoints();
        int hpMob2Before = mob2.getHealPoints();
        int powerPlayerBefore = player.getPower();

        move.execute(gameState, player);
        assertTrue(begMobPosition.equals(player.getPosition()));
        assertEquals(hpMob1Before - powerPlayerBefore, mob1.getHealPoints());
        assertEquals(hpMob2Before - powerPlayerBefore, mob2.getHealPoints());
    }
}