package org.snakeinc.snake.model.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.Test;
import org.snakeinc.snake.model.Game;
import org.snakeinc.snake.model.GameConfig;
import org.snakeinc.snake.model.spawner.Spawner;
import org.snakeinc.snake.model.grid.Direction;
import org.snakeinc.snake.model.grid.Position;

class GoldenAppleTest {

    @Test
    void grants_five_points_and_three_segments() {
        GameConfig config = new GameConfig(10, 10, new Position(5, 5), Direction.RIGHT, 100);
        boolean[] placed = {false};
        Spawner oneGoldenApple = context -> {
            if (!placed[0]) {
                placed[0] = true;
                context.board().add(new GoldenApple(new Position(6, 5)));
            }
        };

        Game game = new Game(config, new Random(0), List.of(oneGoldenApple));
        for (int i = 0; i < 3; i++) {
            game.tick();
        }

        assertEquals(5, game.score());
        assertEquals(4, game.snake().size());
        assertTrue(game.board().isEmpty());
    }
}
