package ru.spbau.mit.Model;

import javafx.geometry.Pos;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by n_buga on 15.12.16.
 * This class provides the methods to work with all objects that is placed on the map or can be placed.
 * And useful class Position - for determining the exact place on the map.
 */
public abstract class MapObject{
    private static Position position;

    public void setPosition(Position position) {
        this.position = position;
    }
    public void setPosition(int i, int j) {
        this.position = new Position(i, j);
    }
    public Position getPosition() {
        return position;
    }

    public static class Position {
        private static final Position up = new Position(-1, 0);
        private static final Position down = new Position(1, 0);
        private static final Position left = new Position(0, -1);
        private static final Position right = new Position(0, 1);


        private int i = 0;
        private int j = 0;

        public Position() {}
        public Position(int i, int j) {
            this.i = i;
            this.j = j;
        }

        public int geti() {
            return i;
        }

        public int getj() {
            return j;
        }

        public boolean equals(Position other) {
            return (i == other.geti() && j == other.getj());
        }

        public Position add(Position x) {
            return new Position(i + x.geti(), j + x.getj());
        }

        public boolean checkBounds(int down, int right) {
            return (i >= 0 && i < down && j >= 0 && j < right);
        }

        public Position up() {
            return this.add(up);
        }

        public Position down() {
            return this.add(down);
        }

        public Position left() {
            return this.add(left);
        }

        public Position right() {
            return  this.add(right);
        }

        public List<Position> getNeighbours() {
            List<Position> neighbours = new ArrayList<>();
            neighbours.add(down());
            neighbours.add(right());
            neighbours.add(left());
            neighbours.add(up());
            return neighbours;
        }
    }

    abstract public char getSymbol();

    abstract public int getPriority();
}
