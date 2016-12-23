package ru.spbau.mit.Model;

import java.util.Set;
import java.util.TreeSet;

/**
 * Created by n_buga on 15.12.16.
 * This class represents one cell in the map. One cell can contains different MapObject - for example Mob and Stuff.
 * So it saves the List od MapObjects. The element to draw is chosen by its priority, so mapObjects is set.
 */
public class Cell {

    private MapObject.Position position;

    private TreeSet<MapObject> mapObjects = new TreeSet<MapObject>((o1, o2) -> {
        if (o1.getPriority() - o2.getPriority() != 0) {
            return o1.getPriority() - o2.getPriority();
        } else {
            return o2.hashCode() - o1.hashCode();
        }
    });

    public Cell(MapObject.Position position) {
        this.position = position;
    }

    public MapObject.Position getPosition() {
        return position;
    }

    private Set<MapObject> getMapObjects() {
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

    public boolean free() {
        return (mapObjects.size() == 0 || (mapObjects.size() == 1 && contains(Empty.class)));
    }

    public void clear() {
        mapObjects.clear();
    }

    public Mob getMob() {
        for (MapObject mapObject: mapObjects) {
            if (mapObject.getClass().equals(Mob.class)) {
                return (Mob)mapObject;
            }
        }
        return null;
    }

    public boolean containsCharacters() {
        return getCharacter() != null;
    }

    public Character getCharacter() {
        for (MapObject mapObject: getMapObjects()) {
            if (mapObject instanceof Character) {
                return (Character)mapObject;
            }
        }
        return null;
    }

    public Stuff getStuff() {
        for (MapObject mapObject: getMapObjects()) {
            if (mapObject instanceof Stuff) {
                return (Stuff) mapObject;
            }
        }
        return null;
    }
}
