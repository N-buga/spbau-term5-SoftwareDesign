package ru.spbau.mit.Model;

import javafx.util.Pair;
import org.apache.log4j.Logger;
import ru.spbau.mit.Controller;

import java.util.*;

/**
 * Created by n_buga on 15.12.16.
 * StateBuilder creates random State with some determine settings, for example we can create a certain size but
 * random map.
 */
public class GameBuilder {
    private static Logger log = Logger.getLogger(GameBuilder.class);

    public static final int defaultColLen = 5;
    public static final int defaultStrLen = 5;

    private int strLen = defaultColLen;
    private int colLen = defaultStrLen;
    private int countMobs = 0;
    private int countWalls = 0;
    private Set<Mob> mobs = new HashSet<>();
    private Controller controller;
    private final static Random random = new Random();

    private Cell[][] cellsArray;
    private Player player;

    public GameBuilder(Controller controller) {
        this.controller = controller;
        initCellArray();
    }

    public GameBuilder(int colLen, int strLen, Controller controller) {
        this.colLen = colLen;
        this.strLen = strLen;
        this.controller = controller;
        initCellArray();
    }

    /**
     * If we try to add Wall to cell where there is something else we throw exception.
     */
    public GameBuilder setWall(MapObject.Position position) {
        if (countWalls + 1 > colLen * strLen) {
            throw new IllegalStateException("Can not be too many walls");
        }
        Cell curCell = cellsArray[position.geti()][position.getj()];
        if (curCell.free()) {
            curCell.add(new Wall());
            countWalls++;
            return this;
        } else {
            throw new IllegalArgumentException("Can not set Wall in the Cell where there are something else.");
        }
    }

    /**
     * We can only add Mob in the free cells;
     */
    public GameBuilder addMob(Mob mob, MapObject.Position position) {
        if (countMobs + 1 > colLen*strLen) {
            throw new IllegalStateException("Can not add mob cause their count too much.");
        }
        Cell curCell = cellsArray[position.geti()][position.getj()];
        if (curCell.free()) {
            curCell.add(mob);
            countMobs++;
            mobs.add(mob);
            return this;
        } else {
            throw new IllegalArgumentException("Can not set Stuff in the not free Cell.");
        }
    }

    public GameBuilder addMob(Mob mob, int i, int j) {
        addMob(mob, new MapObject.Position(i, j));
        return this;
    }

    public GameBuilder addStuff(Stuff stuff, MapObject.Position position) {
        Cell curCell = cellsArray[position.geti()][position.getj()];
        if (curCell.free()) {
            curCell.add(stuff);
            return this;
        } else {
            throw new IllegalArgumentException("Can not set Stuff in the not free Cell.");
        }
    }

    public void addStuff(Stuff stuff, int i, int j) {
        addStuff(stuff, new MapObject.Position(i, j));
    }

    /**
     * If we try to add more then one Player it throws exception.
     * It try to find a free cell and then it places the player there.
     */
    public GameBuilder setPlayer(Player player) {
        if (this.player != null) {
            throw new IllegalStateException("There can be only one player");
        }
        this.player = player;
        int iPlayer = -1;
        int jPlayer = -1;
        for (int i = 0; i < colLen; i++) {
            for (int j = 0; j < strLen; j++) {
                if (cellsArray[i][j].free()) {
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

    /**
     * The same as previous but it tries to place the player to the particular cell.
     */
    public GameBuilder setPlayerPos(Player player, MapObject.Position playerPos) {
        this.player = player;
        Cell curCell = cellsArray[playerPos.geti()][playerPos.getj()];
        if (curCell.free()) {
            curCell.add(player);
            return this;
        } else {
            throw new IllegalArgumentException("Can not set player in the Cell that is not empty");
        }
    }

    public GameBuilder setPlayerPos(Player player, int i, int j) {
        return this.setPlayerPos(player, new MapObject.Position(i, j));
    }


    /**
     * In the end it builds the GameState and return it.
     */
    public GameState build() {
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

        log.info("Create user-specified GameState");

        return new GameState(new Map(cellsArray), mobs, player, controller);
    }

    /**
     * This method create a random map. Then in returns the GameState with the particular map.
     * It checks some restriction(for example count of rows and columns must be more then 3).
     * In this case it returns null.
     * Also it can be that we aren't able create map fast. Then it returns null.
     */
    public GameState getRandGame(int colLen, int strLen, int countRectangles) {
        final int maxStuffAddPower = 5;
        final int maxStuffAddHp = 10;

        if (colLen <= 4 || strLen <= 4) {
            return null;
        }

        final int MaxCountOperation = 100;
        final int MaxCountAttempts = 100;
        List<Pair<MapObject.Position, MapObject.Position>> rectangles =
                new ArrayList<>(countRectangles);

        int curCountAttempts = 0;
        while (true) {
            curCountAttempts += 1;
            int curCountOfRect = 0;
            int curCountOperation = 0;
            while (curCountOfRect < countRectangles) {
                int ldi = 2 + random.nextInt(colLen - 3);
                int ldj = 1 + random.nextInt(strLen - 3);
                MapObject.Position ld = new MapObject.Position(ldi, ldj);
                int rui = 1 + random.nextInt(ldi - 1);
                int ruj = ldj + 1 + random.nextInt(strLen - ldj - 2);
                MapObject.Position ru = new MapObject.Position(rui, ruj);
                if (!intersect(rectangles, curCountOfRect, ld, ru)) {
                    rectangles.add(curCountOfRect, new Pair<>(ld, ru));
                    curCountOfRect += 1;
                }
                curCountOperation += 1;
                if (curCountOperation == MaxCountOperation) {
                    break;
                }
            }
            if (curCountOfRect == countRectangles) break;
            rectangles.clear();
            if (curCountAttempts == MaxCountAttempts) {
                return null;
            }
        }

        this.colLen = colLen;
        this.strLen = strLen;

        initCellArray();

        for (int i = 0; i < colLen; i++) {
            for (int j = 0; j < strLen; j++) {
                setWall(new MapObject.Position(i, j));
            }
        }

        int owner[][] = new int[colLen][strLen];

        for (int i = 0; i < colLen; i++) {
            for (int j = 0; j < strLen; j++) {
                owner[i][j] = -1;
            }
        }

        for (int k = 0; k < countRectangles; k++) {
            for (int i = rectangles.get(k).getValue().geti(); i <= rectangles.get(k).getKey().geti(); i++) {
                for (int j = rectangles.get(k).getKey().getj(); j <= rectangles.get(k).getValue().getj(); j++) {
                    cellsArray[i][j].clear();
                    cellsArray[i][j].add(new Empty());
                    owner[i][j] = k;
                }
            }
        }
        connectRectangles(rectangles, owner);

        int countFreeCells = 0;

        for (int i = 0; i < colLen; i++) {
            for (int j = 0; j < strLen; j++) {
                if (!cellsArray[i][j].isWall()) {
                    countFreeCells++;
                }
            }
        }

        int countMobs = Math.max(1, countFreeCells/10);

        mobs.clear();

        while (mobs.size() < countMobs) {
            int mobPosi = random.nextInt(colLen);
            int mobPosj = random.nextInt(strLen);
            if (cellsArray[mobPosi][mobPosj].free()) {
                Mob mob = new Mob((char)('0' + (mobs.size() % 10)));
                mobs.add(mob);
                addMob(mob, mobPosi, mobPosj);
            }
        }

        int countStuff = countFreeCells/10;

        int curCountStuff = 0;
        while (curCountStuff < countStuff) {
            int stuffPosi = random.nextInt(colLen);
            int stuffPosj = random.nextInt(strLen);
            if (cellsArray[stuffPosi][stuffPosj].free()) {
                curCountStuff += 1;
                Stuff stuff = new Stuff(random.nextInt(maxStuffAddHp), random.nextInt(maxStuffAddPower));
                addStuff(stuffPosi, stuffPosj, stuff);
            }
        }

        player = new Player();

        while (true) {
            int playerPosi = random.nextInt(colLen);
            int playerPosj = random.nextInt(strLen);
            if (cellsArray[playerPosi][playerPosj].free()) {
                setPlayerPos(player, playerPosi, playerPosj);
                break;
            }
        }

        log.info("Create random GameState");

        return new GameState(new Map(cellsArray), mobs, player, controller);
    }

    /**
     * This method add concrete stuff in concrete place.
     */
    private void addStuff(int stuffPosi, int stuffPosj, Stuff stuff) {
        cellsArray[stuffPosi][stuffPosj].add(stuff);
    }

    /**
     * Our map is a lot of rectangles, that we need to connect to each over.
     */
    private void connectRectangles(List<Pair<MapObject.Position, MapObject.Position>> rectangles, int[][] owner) {
        final int numFirstRectangle = 0;
        boolean visited[][] = new boolean[colLen][strLen];
        Queue<MapObject.Position> queue = new LinkedList<>();
        fullVisitedAndAddToQueue(rectangles.get(numFirstRectangle), queue, visited);
        MapObject.Position parent[][] = new MapObject.Position[colLen][strLen];
        bfs(rectangles, visited, owner, queue, parent);
    }

    /**
     * Add to queue the boarder of rectangle, and mark all cells in rectangle visited.
     */
    private void fullVisitedAndAddToQueue(Pair<MapObject.Position, MapObject.Position> rect,
                                          Queue<MapObject.Position> queue, boolean visited[][]) {
        for (int i = rect.getValue().geti(); i <= rect.getKey().geti(); i++) {
            if (!visited[i][rect.getKey().getj()]) {
                queue.add(new MapObject.Position(i, rect.getKey().getj()));
                visited[i][rect.getKey().getj()] = true;
            }
            if (!visited[i][rect.getValue().getj()]) {
                queue.add(new MapObject.Position(i, rect.getValue().getj()));
                visited[i][rect.getValue().getj()] = true;
            }
        }

        for (int j = rect.getKey().getj(); j <= rect.getValue().getj(); j++) {
            if (!visited[rect.getKey().geti()][j]) {
                queue.add(new MapObject.Position(rect.getKey().geti(), j));
                visited[rect.getKey().geti()][j] = true;
            }
            if (!visited[rect.getValue().geti()][j]) {
                queue.add(new MapObject.Position(rect.getValue().geti(), j));
                visited[rect.getValue().geti()][j] = true;
            }
        }

        for (int i = rect.getValue().geti(); i <= rect.getKey().geti(); i++) {
            for (int j = rect.getKey().getj(); j <= rect.getValue().getj(); j++) {
                visited[i][j] = true;
            }
        }
    }

    /**
     * bfs that helps to connect rectangles
     */
    private void bfs(List<Pair<MapObject.Position, MapObject.Position>> rectangles, boolean visited[][],
                     int[][] owner, Queue<MapObject.Position> queue, MapObject.Position parent[][]) {
        while (!queue.isEmpty()) {
            MapObject.Position curPos = queue.poll();
            curPos.getNeighbours().stream().forEach(
                    pos -> handleAndAddPos(pos, queue, visited, owner, parent, rectangles, curPos));
        }
    }

    /**
     * When we find the next rectangle - we connect it to the last one.
     */
    void findWayToLast(MapObject.Position pos, MapObject.Position parent[][], int[][] owner) {
        MapObject.Position curPos = parent[pos.geti()][pos.getj()];
        while (curPos != null && owner[curPos.geti()][curPos.getj()] == -1) {
            Cell curCell = cellsArray[curPos.geti()][curPos.getj()];
            curCell.clear();
            curCell.add(new Empty());
            curPos = parent[curPos.geti()][curPos.getj()];
        }
    }

    /**
     * This method handles usual cells - it checks if a cell isn't out of map ant isn't visited and then add to queue.
     */
    private void handleAndAddPos(MapObject.Position pos, Queue<MapObject.Position> queue, boolean visited[][],
                                 int owner[][], MapObject.Position parent[][],
                                 List<Pair<MapObject.Position, MapObject.Position>> rectangles,
                                 MapObject.Position parentPos) {
        if (pos.checkBounds(colLen, strLen) && !visited[pos.geti()][pos.getj()]) {
            parent[pos.geti()][pos.getj()] = parentPos;
            if (owner[pos.geti()][pos.getj()] != -1) {
                findWayToLast(pos, parent, owner);
                fullVisitedAndAddToQueue(rectangles.get(owner[pos.geti()][pos.getj()]), queue, visited);
            } else {
                queue.add(pos);
                visited[pos.geti()][pos.getj()] = true;
            }
        }
    }

    /**
     * We need to generate the rectangles which don't intersect, So we need a method that checks it.
     * @param rectangles - all previous rectangles
     * This two arguments represents the new one.
     * @param ld
     * @param ru
     * @return - if the new rectangle intersect the previous?
     */
    private boolean intersect(List<Pair<MapObject.Position, MapObject.Position>> rectangles, int i,
                              MapObject.Position ld, MapObject.Position ru) {
        for (int j = 0; j < i; j++) {
            if (intersect(rectangles.get(j), ld, ru) ||
                    intersect(new Pair<>(ld, ru), rectangles.get(j).getKey(), rectangles.get(j).getValue())) {
                return true;
            }
        }
        return false;
    }

    /**
     * This is intersection only two rectangles - one new and one old.
     */
    private boolean intersect(Pair<MapObject.Position, MapObject.Position> rectangle,
                              MapObject.Position ld, MapObject.Position ru) {
        MapObject.Position ldRect = rectangle.getKey();
        MapObject.Position ruRect = rectangle.getValue();
        MapObject.Position luRect = new MapObject.Position(ruRect.geti(), ldRect.getj());
        MapObject.Position rdRect = new MapObject.Position(ldRect.geti(), ruRect.getj());

        MapObject.Position ldExp = ld.left().down();
        MapObject.Position ruExp = ru.right().up();

        return (isIn(ldRect, ldExp, ruExp) || isIn(ruRect, ldExp, ruExp) || isIn(luRect, ldExp, ruExp)
                || isIn(rdRect, ldExp, ruExp));
    }

    /**
     * Check if the point is in the rectangle.
     */
    private boolean isIn(MapObject.Position pnt, MapObject.Position ld, MapObject.Position ru) {
        return  (pnt.geti() >= ru.geti() && pnt.geti() <= ld.geti() && pnt.getj() >= ld.getj() &&
                pnt.getj() <= ru.getj());
    }

    /**
     * Initialise cellsArray.
     */
    private void initCellArray() {
        cellsArray = new Cell[colLen][strLen];
        for (int i = 0; i < colLen; i++) {
            for (int j = 0; j < strLen; j++) {
                cellsArray[i][j] = new Cell(new MapObject.Position(i, j));
            }
        }
    }
}
