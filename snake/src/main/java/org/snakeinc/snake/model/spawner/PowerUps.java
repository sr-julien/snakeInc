package org.snakeinc.snake.model.spawner;

import org.snakeinc.snake.model.GameContext;
import org.snakeinc.snake.model.entity.BoardEntity;
import org.snakeinc.snake.model.entity.GhostApple;
import org.snakeinc.snake.model.entity.StarApple;
import org.snakeinc.snake.model.grid.Position;
import java.util.function.Function;

/**
 * Keeps one star apple and one ghost apple on the board when free tiles exist.
 */
public final class PowerUps implements Spawner {

    @Override
    public void spawn(GameContext context) {
        spawnIfMissing(context, StarApple.class, StarApple::new);
        spawnIfMissing(context, GhostApple.class, GhostApple::new);
    }

    private <T extends BoardEntity> void spawnIfMissing(
            GameContext context, Class<T> type, Function<Position, T> factory) {
        if (context.board().count(type) > 0) {
            return;
        }
        context.randomFreeTile().map(factory).ifPresent(context.board()::add);
    }
}
