package org.snakeinc.snake.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.Test;
import org.snakeinc.snake.model.entity.GoldenApple;
import org.snakeinc.snake.model.entity.RedApple;
import org.snakeinc.snake.model.entity.Wall;
import org.snakeinc.snake.model.grid.Direction;
import org.snakeinc.snake.model.grid.Position;
import org.snakeinc.snake.model.spawner.Spawner;

class AppleStatisticsTest {

    private static final class Setup {
        final Game game;
        final AppleStatistics statistics;

        Setup() {
            GameConfig config = new GameConfig(10, 10, new Position(5, 5), Direction.RIGHT, 100);
            boolean[] placed = {false, false, false};
            Spawner applesOnThePath = context -> {
                if (!placed[0]) {
                    placed[0] = true;
                    context.board().add(new RedApple(new Position(6, 5)));
                }
                if (!placed[1]) {
                    placed[1] = true;
                    context.board().add(new GoldenApple(new Position(7, 5)));
                }
            };
            Spawner wallAfterTheApples = context -> {
                if (!placed[2]) {
                    placed[2] = true;
                    context.board().add(new Wall(new Position(8, 5)));
                }
            };

            game = new Game(config, new Random(0), List.of(applesOnThePath, wallAfterTheApples));
            statistics = new AppleStatistics(game.board());
            game.addListener(statistics);
        }
    }

    @Test
    void counts_apples_by_type_but_not_entities_that_stay() {
        Setup setup = new Setup();

        setup.game.tick(); // eats the red apple
        setup.game.tick(); // eats the golden apple
        setup.game.tick(); // hits the wall

        assertEquals(1, setup.statistics.count(RedApple.class));
        assertEquals(1, setup.statistics.count(GoldenApple.class));
        assertEquals(0, setup.statistics.count(Wall.class));
        assertEquals(GameState.LOST, setup.game.state());
    }

    @Test
    void prints_the_statistics_to_the_console() {
        Setup setup = new Setup();
        PrintStream originalOut = System.out;
        ByteArrayOutputStream logs = new ByteArrayOutputStream();
        System.setOut(new PrintStream(logs));
        try {
            setup.game.tick(); // eats the red apple
            setup.game.tick(); // eats the golden apple
            setup.game.tick(); // hits the wall, game over prints the final statistics
        } finally {
            System.setOut(originalOut);
        }

        String output = logs.toString();
        assertTrue(output.contains("Apples eaten: 1 RedApple"), output);
        assertTrue(output.contains("1 RedApple, 1 GoldenApple"), output);
    }
}
