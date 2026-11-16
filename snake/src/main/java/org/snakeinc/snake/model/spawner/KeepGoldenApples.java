package org.snakeinc.snake.model.spawner;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import org.snakeinc.snake.model.GameContext;
import org.snakeinc.snake.model.entity.GoldenApple;

public record KeepGoldenApples(int step, Set<Integer> spawnedThresholds) implements Spawner {

    public KeepGoldenApples {
        if (step <= 0) {
            throw new IllegalArgumentException("step must be > 0");
        }
        Objects.requireNonNull(spawnedThresholds, "spawnedThresholds");
        spawnedThresholds = new HashSet<>(spawnedThresholds);
    }

    public KeepGoldenApples(int step) {
        this(step, new HashSet<>());
    }

    @Override
    public void spawn(GameContext context) {
        int reachedThresholds = context.score() / step;

        for (int threshold = 1; threshold <= reachedThresholds; threshold++) {
            if (spawnedThresholds.contains(threshold)) {
                continue;
            }
            var tile = context.randomFreeTile();
            if (tile.isEmpty()) {
                return;
            }
            context.board().add(new GoldenApple(tile.get()));
            spawnedThresholds.add(threshold);
        }
    }
}
