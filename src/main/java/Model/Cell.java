package Model;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by n_buga on 15.12.16.
 */
public class Cell {
    MapObject.Position position;

    private Set<MapObject> mapObjects = new HashSet<MapObject>();

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
}
