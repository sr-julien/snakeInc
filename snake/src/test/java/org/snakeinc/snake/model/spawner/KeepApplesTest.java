package org.snakeinc.snake.model.spawner;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.snakeinc.snake.model.Game;
import org.snakeinc.snake.model.GameConfig;
import org.snakeinc.snake.model.entity.RedApple;
import org.snakeinc.snake.model.entity.Wall;
import org.snakeinc.snake.model.grid.Direction;
import org.snakeinc.snake.model.grid.Position;

class KeepApplesTest {

    @Test
    void apples_are_placed_on_free_tiles_only() {
        GameConfig config = new GameConfig(3, 1, new Position(1, 0), Direction.RIGHT, 100);
        Spawner wallOnTheLeft = context -> context.board().add(new Wall(new Position(0, 0)));

        Game game = new Game(config, new Random(0), List.of(wallOnTheLeft, new KeepApples(5)));

        assertEquals(1, game.board().count(RedApple.class));
        assertEquals(new Position(2, 0), game.board().entityAt(new Position(2, 0)).orElseThrow().position());
    }

    @Test
    void keeps_the_requested_number_of_apples() {
        GameConfig config = new GameConfig(10, 10, new Position(5, 5), Direction.RIGHT, 100);

        Game game = new Game(config, new Random(0), List.of(new KeepApples(3)));

        Set<Position> positions = new HashSet<>();
        game.board().entities().forEach(entity -> positions.add(entity.position()));
        assertEquals(3, game.board().count(RedApple.class));
        assertEquals(3, positions.size());
    }
}
