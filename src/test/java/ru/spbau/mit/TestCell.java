package ru.spbau.mit;

import ru.spbau.mit.Model.*;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by n_buga on 18.12.16.
 */
public class TestCell extends Assert {
    @Test
    public void getObjWithMaxPriority() {
        Cell cell = new Cell(new MapObject.Position(0, 0));
        Player player = new Player();
        Mob mob = new Mob();
        Empty empty = new Empty();
        Wall wall = new Wall();

        cell.add(player);
        cell.add(wall);
        cell.add(mob);
        cell.add(empty);

        assertEquals(cell.getSymbol(), wall.getSymbol());

        cell.delete(wall);

        assertEquals(cell.getSymbol(), player.getSymbol());

        cell.delete(player);

        assertEquals(cell.getSymbol(), mob.getSymbol());

        cell.delete(mob);

        assertEquals(cell.getSymbol(), empty.getSymbol());
    }
}
