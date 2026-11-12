package org.snakeinc.snake.model.entity;

import org.snakeinc.snake.model.GameContext;
import org.snakeinc.snake.model.grid.Position;

/**
 * Not placed in the default game: add a spawner for it.
 */
public record Wall(Position position) implements BoardEntity {

    @Override
    public void onHeadEnter(GameContext context) {
        context.lose();
    }
}
