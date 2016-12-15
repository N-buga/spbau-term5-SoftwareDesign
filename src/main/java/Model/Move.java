package Model;

/**
 * Created by n_buga on 15.12.16.
 */
public class Move implements Turn {

    private MapObject.Position newPosition;

    public Move(MapObject.Position newPosition) {
        this.newPosition = newPosition;
    }

    public void execute(State state, Character curCharacter) {
        Map map = state.getMap();
        if (map.getCell(newPosition).isWall()) {
            return;
        }
        MapObject.Position position = curCharacter.getPosition();
        Cell curCell = map.getCell(position);
        curCell.delete(curCharacter);
        Cell newCell = map.getCell(newPosition);
        curCharacter.handle(newCell.getMapObjects());
        newCell.add(curCharacter);
    }
}
