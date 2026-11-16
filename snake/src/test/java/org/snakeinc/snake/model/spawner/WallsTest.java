package org.snakeinc.snake.model.spawner;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.random.RandomGenerator;
import org.junit.jupiter.api.Test;
import org.snakeinc.snake.model.Game;
import org.snakeinc.snake.model.GameConfig;
import org.snakeinc.snake.model.GameState;
import org.snakeinc.snake.model.entity.RedApple;
import org.snakeinc.snake.model.entity.Wall;
import org.snakeinc.snake.model.grid.Direction;
import org.snakeinc.snake.model.grid.Position;

class WallsTest {

    /** Always returns 0: freeTiles() and candidates are scanned in grid order, so placement is deterministic. */
    private static RandomGenerator alwaysZero() {
        return new RandomGenerator() {
            @Override
            public int nextInt() {
                return 0;
            }

            @Override
            public long nextLong() {
                return 0;
            }

            @Override
            public double nextDouble() {
                return 0;
            }
        };
    }

    @Test
    void appears_when_score_first_exceeds_each_threshold() {
        GameConfig config = new GameConfig(20, 20, new Position(10, 10), Direction.RIGHT, 100);
        Spawner fivePointsPerMove = context -> context.addScore(5);

        Game game = new Game(config, alwaysZero(), List.of(fivePointsPerMove, new Walls()));
        // Spawners also run at construction, so the score already starts at 5.

        game.tick(); // score 10 > 5
        assertEquals(1, game.board().count(Wall.class));

        game.tick(); // score 15, does not exceed 20
        game.tick(); // score 20, does not exceed 20
        assertEquals(1, game.board().count(Wall.class));

        game.tick(); // score 25 > 20
        assertEquals(2, game.board().count(Wall.class));
    }

    @Test
    void wall_never_spawns_right_in_front_of_the_snake() {
        GameConfig config = new GameConfig(3, 1, new Position(0, 0), Direction.RIGHT, 100);
        Spawner tenPointsPerMove = context -> context.addScore(10);

        Game game = new Game(config, alwaysZero(), List.of(tenPointsPerMove, new Walls()));
        // Without the fairness rule, the first free tile (1,0) — right in front of the head —
        // would receive the wall, ending the game before the first move.
        assertEquals(1, game.board().count(Wall.class));
        assertEquals(new Wall(new Position(2, 0)), game.board().entities().getFirst());

        game.tick(); // the snake moves to (1,0)
        assertEquals(1, game.board().count(Wall.class));

        game.tick(); // the snake runs into the wall at (2,0)
        assertEquals(GameState.LOST, game.state());
        assertEquals(List.of(new Position(1, 0)), game.snake().body());
    }

    @Test
    void walls_coexist_with_regular_apples() {
        GameConfig config = new GameConfig(20, 20, new Position(10, 10), Direction.RIGHT, 100);
        Spawner fivePointsPerMove = context -> context.addScore(5);

        Game game = new Game(config, alwaysZero(),
                List.of(fivePointsPerMove, new KeepApples(1), new Walls()));
        // Spawners also run at construction, so the score already starts at 5.

        game.tick(); // score 10 > 5
        game.tick(); // score 15
        game.tick(); // score 20, does not exceed 20
        game.tick(); // score 25 > 20

        assertEquals(1, game.board().count(RedApple.class));
        assertEquals(2, game.board().count(Wall.class));
    }
}
