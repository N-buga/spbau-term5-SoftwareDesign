package Model;

/**
 * Created by n_buga on 15.12.16.
 */
public class Map {
    private Cell[][] cellArray;

    public Map(Cell[][] cellArray) {
        this.cellArray = cellArray;
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

    public boolean outOfMap(MapObject.Position p) {
        return (p.geti() < 0 || p.geti() >= getColLen() || p.getj() < 0 || p.getj() >= getStrLen());
    }
}
