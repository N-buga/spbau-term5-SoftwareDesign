package ru.spbau.mit.Model;

/**
 * Created by n_buga on 15.12.16.
 * Interface for turn.
 */
public interface Turn {
    void execute(GameState gameState, Character curCharacter);
}
