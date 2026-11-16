package org.snakeinc.snake.model.spawner;

import org.snakeinc.snake.model.GameContext;
import org.snakeinc.snake.model.entity.GoldenApple;

/**
 * Adds a golden apple the first time the score reaches each multiple of {@code points} (10, 20, 30...).
 * If the grid is full, the apple is added as soon as a tile is free.
 */
public final class GoldenAppleEvery implements Spawner {

    private final int points;
    private int rewardedSteps = 0;

    public GoldenAppleEvery(int points) {
        if (points <= 0) {
            throw new IllegalArgumentException("points must be > 0");
        }
        this.points = points;
    }

    @Override
    public void spawn(GameContext context) {
        int reachedSteps = context.score() / points;
        while (rewardedSteps < reachedSteps) {
            var tile = context.randomFreeTile();
            if (tile.isEmpty()) {
                return;
            }
            context.board().add(new GoldenApple(tile.get()));
            rewardedSteps++;
        }
    }
}
