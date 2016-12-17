package Model;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by n_buga on 15.12.16.
 */
public class Cell {
    MapObject.Position position;

    private TreeSet<MapObject> mapObjects = new TreeSet<MapObject>(new Comparator<MapObject>() {
        public int compare(MapObject o1, MapObject o2) {
            if (o1.getPriority() - o2.getPriority() != 0) {
                return o1.getPriority() - o2.getPriority();
            } else {
                return o2.hashCode() - o1.hashCode();
            }
        }
    });

    public Cell(MapObject.Position position) {
        this.position = position;
    }

    public Set<MapObject> getMapObjects() {
        return mapObjects;
    }

    public void add(MapObject mapObject) {
        mapObject.setPosition(position);
        mapObjects.add(mapObject);
    }

    public boolean contains(MapObject mapObject) {
        return mapObjects.contains(mapObject);
    }

    public boolean contains(Class clazz) {
        for (MapObject mapObject: mapObjects) {
            if (mapObject.getClass().equals(clazz)) {
                return true;
            }
        }
        return false;
    }

    public boolean containsPlayer() {
        return contains(Player.class);
    }

    public boolean containsMob() {
        return contains(Mob.class);
    }

    public boolean isWall() {
        return contains(Wall.class);
    }

    public void delete(MapObject mapObject) {
        mapObjects.remove(mapObject);
    }

    public char getSymbol() {
        return mapObjects.last().getSymbol();
    }
}
