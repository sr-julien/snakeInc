package org.snakeinc.snake.model.entity;

import org.snakeinc.snake.model.GameContext;
import org.snakeinc.snake.model.grid.Position;

public record RedApple(Position position) implements BoardEntity {

    @Override
    public void onHeadEnter(GameContext context) {
        context.board().remove(this);
        context.snake().grow(1);
        context.addScore(1);
    }
}
