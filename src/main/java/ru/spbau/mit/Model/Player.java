package ru.spbau.mit.Model;

import org.apache.log4j.Logger;
import ru.spbau.mit.Controller;

/**
 * Created by n_buga on 15.12.16.
 * This class implements entity Player.
 */
public class Player extends Character {
    private static Logger log = Logger.getLogger(Player.class);
    private int priority = 3;

    public Player() {}

    public Player(int power, int healPoints) {
        super(power, healPoints);
    }

    @Override
    public void doTurn(GameState gameState) {
        log.info("Begin player turn");
        Turn turn = gameState.getController().awaitTurn(this);
        turn.execute(gameState, this);
        log.info("End player turn");
    }

    /**
     * We nee dto override handle, cause Player can get Stuff, but Mobs can't.
     */
    @Override
    void handle(GameState gameState) {
        log.info("Begin player handle cell");
        super.handle(gameState);
        Cell curCell = gameState.getMap().getCell(getPosition());
        Stuff curStuff = curCell.getStuff();
        if (curStuff != null) {
            setPower(getPower() + curStuff.getAddPower());
            setHP(getHealPoints() + curStuff.getAddHP());
            curCell.delete(curStuff);
        }
        log.info("End player handle cell");
    }

    @Override
    public char getSymbol() {
        return '@';
    }

    @Override
    public int getPriority() {
        return priority;
    }
}
