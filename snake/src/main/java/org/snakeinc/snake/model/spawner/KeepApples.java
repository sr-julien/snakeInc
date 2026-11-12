package org.snakeinc.snake.model.spawner;

import org.snakeinc.snake.model.GameContext;
import org.snakeinc.snake.model.entity.RedApple;

/**
 * Keeps {@code count} red apples on the board.
 */
public record KeepApples(int count) implements Spawner {

    public KeepApples {
        if (count < 0) {
            throw new IllegalArgumentException("count must be >= 0");
        }
    }

    @Override
    public void spawn(GameContext context) {
        int missing = count - context.board().count(RedApple.class);
        for (int i = 0; i < missing; i++) {
            var tile = context.randomFreeTile();
            if (tile.isEmpty()) {
                return;
            }
            context.board().add(new RedApple(tile.get()));
        }
    }
}
