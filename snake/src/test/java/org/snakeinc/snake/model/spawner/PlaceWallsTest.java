package org.snakeinc.snake.model.spawner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

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

class PlaceWallsTest {

    @Test
    void default_game_starts_with_walls_and_apples() {
        Game game = new Game(new GameConfig(30, 30, new Position(5, 5), Direction.RIGHT, 100));

        assertEquals(10, game.board().count(Wall.class));
        assertEquals(100, game.board().count(RedApple.class));
    }

    @Test
    void places_walls_at_game_start_without_overlapping_snake_or_apples() {
        GameConfig config = new GameConfig(20, 20, new Position(5, 5), Direction.RIGHT, 100);

        Game game = new Game(config, new Random(0), List.of(new PlaceWalls(10), new KeepApples(5)));

        assertEquals(10, game.board().count(Wall.class));
        assertEquals(5, game.board().count(RedApple.class));
        Set<Position> occupiedTiles = new HashSet<>();
        game.board().entities().forEach(entity -> {
            assertFalse(game.snake().occupies(entity.position()));
            occupiedTiles.add(entity.position());
        });
        assertEquals(game.board().size(), occupiedTiles.size());
    }

    @Test
    void skips_wall_placement_if_the_board_cannot_hold_the_full_set() {
        GameConfig config = new GameConfig(2, 1, new Position(0, 0), Direction.RIGHT, 100);

        Game game = new Game(config, new Random(0), List.of(new PlaceWalls(10), new KeepApples(1)));

        assertEquals(0, game.board().count(Wall.class));
        assertEquals(1, game.board().count(RedApple.class));
    }
}
