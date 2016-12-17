package Model;

/**
 * Created by n_buga on 15.12.16.
 */
public class Move implements Turn {

    private MapObject.Position newPosition;

    public Move(MapObject.Position newPosition) {
        this.newPosition = newPosition;
    }

    public Move(int i, int j) {
        this.newPosition = new MapObject.Position(i, j);
    }

    public void execute(State state, Character curCharacter) {
        Map map = state.getMap();
        if (map.outOfMap(newPosition) || map.getCell(newPosition).isWall()) {
            return;
        }
        MapObject.Position position = curCharacter.getPosition();
        Cell curCell = map.getCell(position);
        curCell.delete(curCharacter);
        Cell newCell = map.getCell(newPosition);
        newCell.add(curCharacter);
        curCharacter.handle(state);
//
//        Hit hit = new Hit();
//        hit.execute(state, curCharacter);
    }
}
