package org.snakeinc.snake.model.entity;

import org.snakeinc.snake.model.GameContext;
import org.snakeinc.snake.model.grid.Position;

/**
 * A golden apple that the snake can eat to grow and earn points.
 */
public record GoldenApple(Position position) implements BoardEntity {

    @Override
    public void onHeadEnter(GameContext context) {
        context.board().remove(this);
        context.snake().grow(3);
        context.addScore(5);
    }
}
