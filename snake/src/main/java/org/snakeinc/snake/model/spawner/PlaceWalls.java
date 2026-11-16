package org.snakeinc.snake.model.spawner;

import org.snakeinc.snake.model.GameContext;
import org.snakeinc.snake.model.entity.Wall;

/**
 * Places a fixed set of walls when the game starts.
 */
public final class PlaceWalls implements Spawner {

    private final int count;
    private boolean placed;

    public PlaceWalls(int count) {
        if (count < 0) {
            throw new IllegalArgumentException("count must be >= 0");
        }
        this.count = count;
    }

    @Override
    public void spawn(GameContext context) {
        if (placed) {
            return;
        }
        placed = true;

        if (context.freeTiles().size() < count) {
            return;
        }

        for (int i = 0; i < count; i++) {
            var tile = context.randomFreeTile();
            if (tile.isEmpty()) {
                return;
            }
            context.board().add(new Wall(tile.get()));
        }
    }
}
