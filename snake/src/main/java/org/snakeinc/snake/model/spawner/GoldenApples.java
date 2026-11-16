package org.snakeinc.snake.model.spawner;

import org.snakeinc.snake.model.GameContext;
import org.snakeinc.snake.model.entity.GoldenApple;

/**
 * Spawns one golden apple each time the score exceeds 10 for the first time, then 20, then 30...
 */
public final class GoldenApples implements Spawner {

    private static final int SCORE_STEP = 10;

    private int thresholdsSpawned = 0;

    @Override
    public void spawn(GameContext context) {
        while (context.score() > (thresholdsSpawned + 1) * SCORE_STEP) {
            var tile = context.randomFreeTile();
            if (tile.isEmpty()) {
                return;
            }
            context.board().add(new GoldenApple(tile.get()));
            thresholdsSpawned++;
        }
    }
}
