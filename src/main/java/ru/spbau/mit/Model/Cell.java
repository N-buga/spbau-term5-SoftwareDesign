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
        return mapObjects.stream().anyMatch(mapObject -> mapObject.getClass().equals(clazz));
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
        return (Mob) mapObjects.stream()
                .filter(mapObject -> mapObject.getClass().equals(Mob.class))
                .findFirst()
                .orElse(null);
    }

    public boolean containsCharacters() {
        return getCharacter() != null;
    }

    public Character getCharacter() {
        return (Character) getMapObjects()
                .stream()
                .filter(mapObject -> mapObject instanceof Character)
                .findFirst()
                .orElse(null);
    }

    public Stuff getStuff() {
        return (Stuff) getMapObjects().stream()
                .filter(mapObject -> mapObject instanceof Stuff)
                .findFirst()
                .orElse(null);
    }
}
