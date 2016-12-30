package ru.spbau.mit.Model;

/**
 * Created by n_buga on 15.12.16.
 * This class realise Move. But maybe we will able to do something else in the future versions.
 * For example shoot, or cast magic. Here we use only Move.
 */
public class Move implements Turn {

    private MapObject.Position newPosition;

    public Move(MapObject.Position newPosition) {
        this.newPosition = newPosition;
    }

    public Move(int i, int j) {
        this.newPosition = new MapObject.Position(i, j);
    }

    /**
     * If in the cell there are Character we will hit it, otherwise we move.
     * @param gameState
     * @param curCharacter
     */
    public void execute(GameState gameState, Character curCharacter) {
        Map map = gameState.getMap();
        if (map.outOfMap(newPosition) || map.getCell(newPosition).isWall()) {
            return;
        }
        MapObject.Position position = curCharacter.getPosition();
        Cell curCell = map.getCell(position);
        curCell.delete(curCharacter);
        if (!map.getCell(newPosition).containsCharacters()) {
            Cell newCell = map.getCell(newPosition);
            newCell.add(curCharacter);
            curCharacter.handle(gameState);
        } else {
            Character enemy = map.getCell(newPosition).getCharacter();
            enemy.getDamage(curCharacter.getPower());
            curCell.add(curCharacter);
        }
    }
}
