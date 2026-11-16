package org.snakeinc.snake.model.spawner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.Test;
import org.snakeinc.snake.model.Game;
import org.snakeinc.snake.model.GameConfig;
import org.snakeinc.snake.model.GameContext;
import org.snakeinc.snake.model.entity.BoardEntity;
import org.snakeinc.snake.model.entity.GoldenApple;
import org.snakeinc.snake.model.entity.RedApple;
import org.snakeinc.snake.model.grid.Direction;
import org.snakeinc.snake.model.grid.Position;

class GoldenAppleEveryTest {

    /** Gives points without making the snake grow, to control the score precisely. */
    private record Points(Position position, int points) implements BoardEntity {
        @Override
        public void onHeadEnter(GameContext context) {
            context.board().remove(this);
            context.addScore(points);
        }
    }

    private static Game game(Spawner... spawners) {
        return new Game(new GameConfig(20, 20, new Position(0, 0), Direction.RIGHT, 100),
                new Random(0), List.of(spawners));
    }

    @Test
    void no_golden_apple_at_the_start() {
        Game game = game(new GoldenAppleEvery(10));

        assertEquals(0, game.board().count(GoldenApple.class));
    }

    @Test
    void no_golden_apple_below_the_first_threshold() {
        Game game = game(new GoldenAppleEvery(10));
        game.board().add(new Points(new Position(1, 0), 9));

        game.tick();

        assertEquals(0, game.board().count(GoldenApple.class));
    }

    @Test
    void a_golden_apple_appears_when_the_score_reaches_10() {
        Game game = game(new GoldenAppleEvery(10));
        game.board().add(new Points(new Position(1, 0), 10));

        game.tick();

        assertEquals(1, game.board().count(GoldenApple.class));
    }

    @Test
    void only_one_golden_apple_per_threshold() {
        Game game = game(new GoldenAppleEvery(10));
        game.board().add(new Points(new Position(1, 0), 10));
        game.board().add(new Points(new Position(2, 0), 5));

        game.tick(); // 10
        game.tick(); // 15

        assertEquals(1, game.board().count(GoldenApple.class));
    }

    @Test
    void a_new_golden_apple_appears_at_each_multiple_of_10() {
        Game game = game(new GoldenAppleEvery(10));
        game.board().add(new Points(new Position(1, 0), 12));
        game.board().add(new Points(new Position(2, 0), 9));

        game.tick(); // 12
        game.tick(); // 21

        assertEquals(2, game.board().count(GoldenApple.class));
    }

    @Test
    void golden_and_red_apples_coexist() {
        Game game = game(new KeepApples(1), new GoldenAppleEvery(10));
        game.board().add(new Points(new Position(1, 0), 10));

        game.tick();

        assertEquals(1, game.board().count(RedApple.class));
        assertEquals(1, game.board().count(GoldenApple.class));
    }

    @Test
    void the_threshold_must_be_positive() {
        assertThrows(IllegalArgumentException.class, () -> new GoldenAppleEvery(0));
    }
}
