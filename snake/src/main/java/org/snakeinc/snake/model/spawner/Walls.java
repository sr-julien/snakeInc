package org.snakeinc.snake.model.spawner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.snakeinc.snake.model.GameContext;
import org.snakeinc.snake.model.entity.Wall;
import org.snakeinc.snake.model.grid.Position;

/**
 * Spawns one wall each time the score exceeds 5 for the first time, then 20, then 35...
 * <p>
 * A wall never spawns on the tile the snake is about to enter: that would be an instant,
 * unavoidable game over.
 */
public final class Walls implements Spawner {

    private static final int FIRST_THRESHOLD = 5;
    private static final int SCORE_STEP = 15;

    private int thresholdsSpawned = 0;

    @Override
    public void spawn(GameContext context) {
        while (context.score() > FIRST_THRESHOLD + thresholdsSpawned * SCORE_STEP) {
            var tile = randomFairTile(context);
            if (tile.isEmpty()) {
                return;
            }
            context.board().add(new Wall(tile.get()));
            thresholdsSpawned++;
        }
    }

    /** Like {@link GameContext#randomFreeTile()}, minus the tile in front of the snake's head. */
    private Optional<Position> randomFairTile(GameContext context) {
        Position forbidden = context.snake().nextHead();
        List<Position> candidates = new ArrayList<>(context.freeTiles());
        candidates.remove(forbidden);
        if (candidates.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(candidates.get(context.random().nextInt(candidates.size())));
    }
}
