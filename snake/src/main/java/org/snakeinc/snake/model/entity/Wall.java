package org.snakeinc.snake.model.entity;

import org.snakeinc.snake.model.GameContext;
import org.snakeinc.snake.model.grid.Position;

/**
 * Placed at the start of the default game by {@link org.snakeinc.snake.model.spawner.RandomWalls}.
 */
public record Wall(Position position) implements BoardEntity {

    @Override
    public void onHeadEnter(GameContext context) {
        context.lose();
    }
}
