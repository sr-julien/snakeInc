package org.snakeinc.snake.model.spawner;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.Test;
import org.snakeinc.snake.model.Game;
import org.snakeinc.snake.model.GameConfig;
import org.snakeinc.snake.model.entity.GoldenApple;
import org.snakeinc.snake.model.entity.RedApple;
import org.snakeinc.snake.model.grid.Direction;
import org.snakeinc.snake.model.grid.Position;

class GoldenAppleMilestonesTest {

    @Test
    void spawns_a_golden_apple_at_each_ten_point_milestone_alongside_regular_apples() {
        GameConfig config = new GameConfig(30, 5, new Position(0, 0), Direction.RIGHT, 100);
        Game game = new Game(config, new Random(0), List.of(new GoldenAppleMilestones()));

        for (int x = 1; x <= 20; x++) {
            game.board().add(new RedApple(new Position(x, 0)));
        }
        game.board().add(new RedApple(new Position(25, 4)));

        for (int i = 0; i < 9; i++) {
            game.tick();
        }
        assertEquals(9, game.score());
        assertEquals(0, game.board().count(GoldenApple.class));

        game.tick();
        assertEquals(10, game.score());
        assertEquals(1, game.board().count(GoldenApple.class));
        assertEquals(11, game.board().count(RedApple.class));

        for (int i = 0; i < 10; i++) {
            game.tick();
        }
        assertEquals(20, game.score());
        assertEquals(2, game.board().count(GoldenApple.class));
        assertEquals(1, game.board().count(RedApple.class));
    }
}
