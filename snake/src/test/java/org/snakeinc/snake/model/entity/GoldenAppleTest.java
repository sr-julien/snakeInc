package org.snakeinc.snake.model.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.Test;
import org.snakeinc.snake.model.Game;
import org.snakeinc.snake.model.GameConfig;
import org.snakeinc.snake.model.grid.Direction;
import org.snakeinc.snake.model.grid.Position;

class GoldenAppleTest {

    @Test
    void eating_a_golden_apple_gives_5_points_and_3_segments() {
        Game game = new Game(new GameConfig(10, 10, new Position(0, 0), Direction.RIGHT, 100),
                new Random(0), List.of());
        game.board().add(new GoldenApple(new Position(1, 0)));

        game.tick();
        game.tick();
        game.tick();
        game.tick();

        assertEquals(5, game.score());
        assertEquals(4, game.snake().size());
        assertTrue(game.board().isEmpty());
    }
}
