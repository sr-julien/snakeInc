package org.snakeinc.snake.model.spawner;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.random.RandomGenerator;
import org.junit.jupiter.api.Test;
import org.snakeinc.snake.model.Game;
import org.snakeinc.snake.model.GameConfig;
import org.snakeinc.snake.model.entity.GoldenApple;
import org.snakeinc.snake.model.entity.RedApple;
import org.snakeinc.snake.model.grid.Direction;
import org.snakeinc.snake.model.grid.Position;

class GoldenApplesTest {

    /** Always returns 0: randomFreeTile() picks the first free tile, so placement is deterministic. */
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

        Game game = new Game(config, alwaysZero(), List.of(fivePointsPerMove, new GoldenApples()));
        // Spawners also run at construction, so the score already starts at 5.

        game.tick(); // score 10, does not exceed 10
        assertEquals(0, game.board().count(GoldenApple.class));

        game.tick(); // score 15 > 10
        assertEquals(1, game.board().count(GoldenApple.class));

        game.tick(); // score 20, does not exceed 20
        assertEquals(1, game.board().count(GoldenApple.class));

        game.tick(); // score 25 > 20
        assertEquals(2, game.board().count(GoldenApple.class));
    }

    @Test
    void golden_apples_coexist_with_regular_apples() {
        GameConfig config = new GameConfig(20, 20, new Position(10, 10), Direction.RIGHT, 100);
        Spawner fivePointsPerMove = context -> context.addScore(5);

        Game game = new Game(config, alwaysZero(),
                List.of(fivePointsPerMove, new KeepApples(1), new GoldenApples()));
        // Spawners also run at construction, so the score already starts at 5.

        game.tick(); // score 10, does not exceed 10
        game.tick(); // score 15 > 10

        assertEquals(1, game.board().count(RedApple.class));
        assertEquals(1, game.board().count(GoldenApple.class));
    }
}
