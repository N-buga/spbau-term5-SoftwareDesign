package ru.spbau.mit.Model;

import org.apache.log4j.Logger;

/**
 * Created by n_buga on 15.12.16.
 * This class represents the map - the field, where places all mobs, player and stuff.
 */
public class Map {
    private static Logger log = Logger.getLogger(Map.class);

    public final int radiusVisibleArea = 4;

    private Cell[][] cellArray;

    public Map(Cell[][] cellArray) {
        this.cellArray = cellArray;
        log.info("Created map");
    }

    public Cell getCell(int i, int j) {
        return cellArray[i][j];
    }
    public Cell getCell(MapObject.Position position) {
        return this.getCell(position.geti(), position.getj());
    }

    public int getColLen() {
        return cellArray.length;
    }

    public int getStrLen() {
        return cellArray[0].length;
    }

    public void draw() {
        for (int i = 0; i < cellArray.length; i++) {
            for (int j = 0; j < cellArray[0].length; j++) {
                System.out.print(cellArray[i][j].getSymbol());
            }
            System.out.println();
        }
    }

    /**
     * It return only the area we can see. It's like if we have a fire.
     */
    public Cell[][] getVisibleArea(MapObject.Position position) {
        int countVisibleCellsLeft = Math.min(radiusVisibleArea, position.getj());
        int countVisibleCellsRight = Math.min(radiusVisibleArea, getStrLen() - 1 - position.getj());
        int countVisibleCellsUp = Math.min(radiusVisibleArea, position.geti());
        int countVisibleCellsDown = Math.min(radiusVisibleArea, getColLen() - 1 - position.geti());
        Cell[][] visibleArea = new Cell[countVisibleCellsDown + countVisibleCellsUp + 1]
                        [countVisibleCellsLeft + countVisibleCellsRight + 1];
        int begi = position.geti() - countVisibleCellsUp;
        int endi = position.geti() + countVisibleCellsDown;
        int begj = position.getj() - countVisibleCellsLeft;
        int endj = position.getj() + countVisibleCellsRight;
        for (int i = begi; i <= endi; i++) {
            for (int j = begj; j <= endj; j++) {
                visibleArea[i - begi][j - begj] = cellArray[i][j];
            }
        }
        return visibleArea;
    }

    public boolean outOfMap(MapObject.Position p) {
        return (p.geti() < 0 || p.geti() >= getColLen() || p.getj() < 0 || p.getj() >= getStrLen());
    }
}
