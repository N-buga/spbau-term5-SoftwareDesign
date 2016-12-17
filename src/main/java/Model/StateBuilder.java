package Model;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by n_buga on 15.12.16.
 * StateBuilder creates random State with some determine settings, for example we can create a certain size but
 * random map.
 */
public class StateBuilder {
    public static final int defaultColLen = 5;
    public static final int defaultStrLen = 5;

    private boolean walls = false;
    private int strLen = defaultColLen;
    private int colLen = defaultStrLen;
    private int countMobs = 0;
    private int countWalls = 0;
    private Set<Mob> mobs = new HashSet<Mob>();
/*    private int countStuff = defaultCountStuff; */

    private Cell[][] cellsArray;
    private Player player;

    public StateBuilder() {
        initCellArray();
    }

    public StateBuilder(int colLen, int strLen) {
        this.colLen = colLen;
        this.strLen = strLen;
        initCellArray();
    }

    public StateBuilder setWalls(boolean walls) {
        this.walls = walls;
        return this;
    }

    /**
     * When we will build we'll check if the count of Walls is less then a quarter of the map size.
     * Otherwise it throws Exception.
     * Also it works only if walls flag switches on;
     * If we try to add Wall to cell where there is something else we throw exception.
     * @param position
     * @return
     */
    public StateBuilder setWall(MapObject.Position position) {
        if (countWalls + 1 > colLen*strLen) {
            throw new IllegalStateException("Can not be too many walls");
        }
        if (!walls) {
            throw new IllegalStateException("Can not add Wall when walls are forbidden");
        }
        Cell curCell = cellsArray[position.geti()][position.getj()];
        if (curCell.getMapObjects().isEmpty()) {
            curCell.add(new Wall());
            countWalls++;
            return this;
        } else {
            throw new IllegalArgumentException("Can not set Wall in the Cell where there are something else.");
        }
    }

    /**
     * If Mob shares a cell with Player or Wall it'll throw away.
     * When we will build we'll check if the count of Mobs is less then an tenth of the map size.
     * Otherwise it throws Exception.
     * @param mob
     * @return
     */
    public StateBuilder addMob(Mob mob, MapObject.Position position) {
        if (countMobs + 1 > colLen*strLen/10) {
            throw new IllegalStateException("Can not add mob cause their count too much.");
        }
        Cell curCell = cellsArray[position.geti()][position.getj()];
        if (!curCell.isWall() && !curCell.containsPlayer()) {
            curCell.add(mob);
            countMobs++;
            mobs.add(mob);
            return this;
        } else {
            throw new IllegalArgumentException("Can not set Mob in the Cell where there are Player or Wall");
        }
    }

    public StateBuilder addMob(Mob mob, int i, int j) {
        addMob(mob, new MapObject.Position(i, j));
        return this;
    }

    /**
     * If we try to add more then one Player it throws exception.
     * @param player
     * @return
     */
    public StateBuilder setPlayer(Player player) {
        if (this.player != null) {
            throw new IllegalStateException("There can be only one player");
        }
        this.player = player;
        int iPlayer = -1;
        int jPlayer = -1;
        for (int i = 0; i < colLen; i++) {
            for (int j = 0; j < strLen; j++) {
                if (cellsArray[i][j].getMapObjects().isEmpty()) {
                    iPlayer = i;
                    jPlayer = j;
                    break;
                }
            }
            if (iPlayer != -1) break;
        }
        if (iPlayer == -1 || jPlayer == -1) {
            throw new IllegalStateException("Can not set player cause there are no free Cells in the Map");
        }
        cellsArray[iPlayer][jPlayer].add(player);
        return this;
    }

    public StateBuilder setPlayerPos(Player player, MapObject.Position playerPos) {
        this.player = player;
        Cell curCell = cellsArray[playerPos.geti()][playerPos.getj()];
        if (curCell.getMapObjects().isEmpty()) {
            curCell.add(player);
            return this;
        } else {
            throw new IllegalArgumentException("Can not set player in the Cell that is not empty");
        }
    }

    public StateBuilder setPlayerPos(Player player, int i, int j) {
        return this.setPlayerPos(player, new MapObject.Position(i, j));
    }

    public State build() {
        if (player == null) {
            setPlayer(new Player());
        }
        for (int i = 0; i < colLen; i++) {
            for (int j = 0; j < strLen; j++) {
                Cell curCell = cellsArray[i][j];
                if (!curCell.isWall()) {
                    Empty empty = new Empty();
                    empty.setPosition(i, j);
                    curCell.add(new Empty());
                }
            }
        }
        return new State(new Map(cellsArray), mobs, player);
    }

    private void initCellArray() {
        cellsArray = new Cell[colLen][strLen];
        for (int i = 0; i < colLen; i++) {
            for (int j = 0; j < strLen; j++) {
                cellsArray[i][j] = new Cell(new MapObject.Position(i, j));
            }
        }
    }
}
