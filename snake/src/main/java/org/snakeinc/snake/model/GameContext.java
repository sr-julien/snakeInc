package org.snakeinc.snake.model;

import java.util.List;
import java.util.Optional;
import java.util.random.RandomGenerator;
import org.snakeinc.snake.model.entity.Board;
import org.snakeinc.snake.model.grid.Grid;
import org.snakeinc.snake.model.grid.Position;

/**
 * The part of {@link Game} that entities and spawners can access.
 */
public interface GameContext {

    Grid grid();

    Snake snake();

    Board board();

    /** Use it instead of {@code new Random()} so that seeded games stay reproducible. */
    RandomGenerator random();

    int score();

    void addScore(int points);

    void lose();

    void win();

    /** Tiles holding neither the snake nor an entity. */
    List<Position> freeTiles();

    /** Empty if the grid is full. */
    default Optional<Position> randomFreeTile() {
        List<Position> free = freeTiles();
        if (free.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(free.get(random().nextInt(free.size())));
    }
}
