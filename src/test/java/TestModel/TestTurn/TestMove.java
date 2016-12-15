package TestModel.TestTurn;

import Model.*;
import Model.Character;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by n_buga on 15.12.16.
 * All turns influence State somehow. It checks that Move change State in proper way.
 */
public class TestMove extends Assert{
    @Test
    public void simplePlayerAndMobCheckMovesWithoutWalls() {
        int iStart = StateBuilder.defaultColLen/2;
        int jStart = StateBuilder.defaultStrLen/2;

        final MapObject.Position newPos = new MapObject.Position(0, 0);

        Player player = new Player();
        Mob mob = new Mob();

        State state = new StateBuilder().setPlayerPos(player, iStart, jStart).build();
        Move move = new Move(newPos);
        move.execute(state, player);

        state = new StateBuilder().addMob(mob, iStart, jStart).build();
        move = new Move(newPos);
        move.execute(state, mob);

        assertTrue(newPos.equals(mob.getPosition()));

        state = new StateBuilder().setPlayerPos(player, iStart, jStart).addMob(mob, iStart + 1, jStart + 1).build();
        Move move1 = new Move(newPos);
        Move move2 = new Move(new MapObject.Position(newPos.geti() + 1, newPos.getj()));

        move1.execute(state, player);
        move2.execute(state, mob);

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

        State state = new StateBuilder()
                .setPlayerPos(player, start)
                .setWalls(true)
                .setWall(wallPos)
                .build();
        Move move = new Move(wallPos);
        move.execute(state, player);

        state = new StateBuilder()
                .addMob(mob, start)
                .setWalls(true)
                .setWall(wallPos)
                .build();
        move = new Move(wallPos);
        move.execute(state, mob);

        assertTrue(start.equals(player.getPosition()));
        assertTrue(start.equals(mob.getPosition()));
    }

}
