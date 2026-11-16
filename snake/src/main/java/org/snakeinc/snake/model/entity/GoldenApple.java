package org.snakeinc.snake.model.entity;

import org.snakeinc.snake.model.GameContext;
import org.snakeinc.snake.model.grid.Position;

public record GoldenApple(Position position) implements Apple {

    public static final int POINTS = 5;
    public static final int GROWTH = 3;

    @Override
    public void onHeadEnter(GameContext context) {
        context.board().remove(this);
        context.snake().grow(GROWTH);
        context.addScore(POINTS);
    }
}
