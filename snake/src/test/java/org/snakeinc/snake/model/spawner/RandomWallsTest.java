package org.snakeinc.snake.model.spawner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.snakeinc.snake.model.Game;
import org.snakeinc.snake.model.GameConfig;
import org.snakeinc.snake.model.GameState;
import org.snakeinc.snake.model.entity.BoardEntity;
import org.snakeinc.snake.model.entity.RedApple;
import org.snakeinc.snake.model.entity.Wall;
import org.snakeinc.snake.model.grid.Direction;
import org.snakeinc.snake.model.grid.Grid;
import org.snakeinc.snake.model.grid.Position;

class RandomWallsTest {

    private static final GameConfig CONFIG = new GameConfig(20, 20, new Position(5, 5), Direction.RIGHT, 100);

    private static Game game(long seed, Spawner... spawners) {
        return new Game(CONFIG, new Random(seed), List.of(spawners));
    }

    private static Set<Position> walls(Game game) {
        Set<Position> walls = new HashSet<>();
        game.board().entities().stream()
                .filter(Wall.class::isInstance)
                .map(BoardEntity::position)
                .forEach(walls::add);
        return walls;
    }

    @Test
    void places_the_requested_proportion_of_walls() {
        Game game = game(0, new RandomWalls(0.05));

        assertEquals(20, game.board().count(Wall.class));
    }

    @Test
    void walls_are_only_placed_at_the_start() {
        Game game = game(0, new RandomWalls(0.05));

        game.tick();
        game.tick();

        assertEquals(GameState.RUNNING, game.state());
        assertEquals(20, game.board().count(Wall.class));
    }

    @Test
    void no_wall_near_the_snake_or_in_front_of_it() {
        for (long seed = 0; seed < 20; seed++) {
            Set<Position> walls = walls(game(seed, new RandomWalls(0.2)));

            for (Position wall : walls) {
                int distance = Math.abs(wall.x() - 5) + Math.abs(wall.y() - 5);
                assertTrue(distance > RandomWalls.SAFE_DISTANCE, "wall too close: " + wall);
            }
            for (int x = 6; x <= 5 + RandomWalls.SAFE_TILES_AHEAD; x++) {
                assertFalse(walls.contains(new Position(x, 5)), "wall in front of the snake at x=" + x);
            }
        }
    }

    @Test
    void free_tiles_have_no_dead_end_and_are_all_reachable() {
        Grid grid = new Grid(CONFIG.width(), CONFIG.height());
        for (long seed = 0; seed < 20; seed++) {
            Set<Position> walls = walls(game(seed, new RandomWalls(0.2)));

            Set<Position> open = new HashSet<>(grid.positions());
            open.removeAll(walls);
            for (Position tile : open) {
                long openNeighbours = List.of(Direction.values()).stream()
                        .map(tile::next)
                        .filter(open::contains)
                        .count();
                assertTrue(openNeighbours >= 2, "dead end at " + tile + " (seed " + seed + ")");
            }
            assertEquals(open, reachableFrom(CONFIG.snakeStart(), open), "closed area (seed " + seed + ")");
        }
    }

    @Test
    void apples_are_not_placed_on_walls() {
        Game game = game(0, new RandomWalls(0.05), new KeepApples(3));

        assertEquals(20, game.board().count(Wall.class));
        assertEquals(3, game.board().count(RedApple.class));
    }

    @Test
    void same_seed_gives_the_same_walls() {
        assertEquals(walls(game(7, new RandomWalls(0.05))), walls(game(7, new RandomWalls(0.05))));
    }

    @Test
    void zero_density_places_no_wall() {
        assertTrue(game(0, new RandomWalls(0)).board().isEmpty());
    }

    @Test
    void invalid_density_is_rejected() {
        assertThrows(IllegalArgumentException.class, () -> new RandomWalls(-0.1));
        assertThrows(IllegalArgumentException.class, () -> new RandomWalls(1));
    }

    private static Set<Position> reachableFrom(Position start, Set<Position> open) {
        Set<Position> reached = new HashSet<>(Set.of(start));
        Deque<Position> toVisit = new ArrayDeque<>(reached);
        while (!toVisit.isEmpty()) {
            Position current = toVisit.removeFirst();
            for (Direction direction : Direction.values()) {
                Position next = current.next(direction);
                if (open.contains(next) && reached.add(next)) {
                    toVisit.add(next);
                }
            }
        }
        return reached;
    }
}
