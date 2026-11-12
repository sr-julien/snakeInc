package org.snakeinc.snake.model;

import org.snakeinc.snake.model.entity.BoardEntity;

/**
 * Events of a {@link Game}. Override only the methods you need.
 */
public interface GameListener {

    /** Called after the entity applied its effect. */
    default void onEntityReached(BoardEntity entity) {
    }

    default void onGameOver(GameState finalState) {
    }
}
