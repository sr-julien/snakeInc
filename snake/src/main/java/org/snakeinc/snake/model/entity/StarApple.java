package org.snakeinc.snake.model.entity;

import org.snakeinc.snake.model.GameContext;
import org.snakeinc.snake.model.grid.Position;

public record StarApple(Position position) implements BoardEntity {

    private static final int DURATION_TICKS = 20;

    @Override
    public void onHeadEnter(GameContext context) {
        context.board().remove(this);
        context.makeSnakeInvincible(DURATION_TICKS);
    }
}
