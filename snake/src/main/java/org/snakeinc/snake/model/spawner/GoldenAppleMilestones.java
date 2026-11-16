package org.snakeinc.snake.model.spawner;

import org.snakeinc.snake.model.GameContext;
import org.snakeinc.snake.model.entity.GoldenApple;

/**
 * Spawns a golden apple once for every 10-point score milestone.
 */
public final class GoldenAppleMilestones implements Spawner {

    private static final int MILESTONE_INTERVAL = 10;

    private int nextMilestone = MILESTONE_INTERVAL;

    @Override
    public void spawn(GameContext context) {
        if (context.score() < nextMilestone) {
            return;
        }

        var tile = context.randomFreeTile();
        if (tile.isEmpty()) {
            return;
        }

        context.board().add(new GoldenApple(tile.get()));
        nextMilestone += MILESTONE_INTERVAL;
    }
}
