package org.snakeinc.snake.model.entity;

import org.snakeinc.snake.model.GameContext;
import org.snakeinc.snake.model.grid.Position;

public record GhostApple(Position position) implements BoardEntity {

    private static final int DURATION_TICKS = 20;

    @Override
    public void onHeadEnter(GameContext context) {
        context.board().remove(this);
        context.makeSnakeGhost(DURATION_TICKS);
    }
}
