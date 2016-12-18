package ru.spbau.mit.Model;

/**
 * Created by n_buga on 15.12.16.
 */
public class Hit implements Turn{
    public void execute(GameState gameState, Character curCharacter) {
        MapObject.Position curPos = curCharacter.getPosition();
        Map map = gameState.getMap();
        Cell curCell = map.getCell(curPos);
        for (MapObject mapObject: curCell.getMapObjects()) {
            if (curCharacter == mapObject) continue;
            if (mapObject instanceof Character) {
                Character enemy = (Character)mapObject;
                enemy.getDamage(curCharacter.getPower());
                if (enemy.isKilled()) {
                    gameState.delete(enemy);
                }
            }
        }
    }
}
