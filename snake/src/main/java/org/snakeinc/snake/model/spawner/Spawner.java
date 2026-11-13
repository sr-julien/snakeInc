package org.snakeinc.snake.model.spawner;

import org.snakeinc.snake.model.Game;
import org.snakeinc.snake.model.GameContext;

/**
 * Adds entities to the board. Called at game start and after each move, in the order given to {@link Game}.
 */
@FunctionalInterface
public interface Spawner {

    void spawn(GameContext context);
}
