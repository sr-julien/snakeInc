package org.snakeinc.snake.model.statistics;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.Test;
import org.snakeinc.snake.model.Game;
import org.snakeinc.snake.model.GameConfig;
import org.snakeinc.snake.model.entity.GoldenApple;
import org.snakeinc.snake.model.entity.RedApple;
import org.snakeinc.snake.model.entity.Wall;
import org.snakeinc.snake.model.grid.Direction;
import org.snakeinc.snake.model.grid.Position;

class AppleStatisticsTest {

    private final ByteArrayOutputStream output = new ByteArrayOutputStream();
    private final AppleStatistics statistics = new AppleStatistics(new PrintStream(output, true, StandardCharsets.UTF_8));

    /** Snake at (0, 0) going right, the entities are placed on its way. */
    private Game gameWithStatistics() {
        Game game = new Game(new GameConfig(10, 10, new Position(0, 0), Direction.RIGHT, 100),
                new Random(0), List.of());
        game.addListener(statistics);
        return game;
    }

    @Test
    void counts_the_apples_eaten_by_type() {
        Game game = gameWithStatistics();
        game.board().add(new RedApple(new Position(1, 0)));
        game.board().add(new GoldenApple(new Position(2, 0)));
        game.board().add(new RedApple(new Position(3, 0)));

        game.tick();
        game.tick();
        game.tick();

        assertEquals(2, statistics.eaten(RedApple.class));
        assertEquals(1, statistics.eaten(GoldenApple.class));
        assertEquals(3, statistics.totalEaten());
    }

    @Test
    void walls_are_not_counted() {
        Game game = gameWithStatistics();
        game.board().add(new Wall(new Position(1, 0)));

        game.tick();

        assertEquals(0, statistics.totalEaten());
    }

    @Test
    void nothing_is_printed_while_the_game_is_running() {
        Game game = gameWithStatistics();
        game.board().add(new RedApple(new Position(1, 0)));

        game.tick();

        assertEquals("", output.toString(StandardCharsets.UTF_8));
    }

    @Test
    void the_statistics_are_printed_when_the_game_is_over() {
        Game game = gameWithStatistics();
        game.board().add(new RedApple(new Position(1, 0)));
        game.board().add(new GoldenApple(new Position(2, 0)));
        game.board().add(new Wall(new Position(3, 0)));

        game.tick();
        game.tick();
        game.tick();

        String expected = String.join(System.lineSeparator(),
                "=== Apples eaten ===",
                "GoldenApple: 1",
                "RedApple: 1",
                "Total: 2") + System.lineSeparator();
        assertEquals(expected, output.toString(StandardCharsets.UTF_8));
    }
}
