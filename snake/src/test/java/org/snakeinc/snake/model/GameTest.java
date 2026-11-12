package org.snakeinc.snake.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.Test;
import org.snakeinc.snake.model.entity.BoardEntity;
import org.snakeinc.snake.model.entity.RedApple;
import org.snakeinc.snake.model.entity.Wall;
import org.snakeinc.snake.model.grid.Direction;
import org.snakeinc.snake.model.grid.Position;
import org.snakeinc.snake.model.spawner.KeepApples;

class GameTest {

    private static Game emptyGame(Position start, Direction direction) {
        return new Game(new GameConfig(10, 10, start, direction, 100), new Random(0), List.of());
    }

    @Test
    void snake_moves_in_its_direction_at_each_tick() {
        Game game = emptyGame(new Position(5, 5), Direction.UP);

        game.tick();

        assertEquals(new Position(5, 4), game.snake().head());
        assertEquals(GameState.RUNNING, game.state());
    }

    @Test
    void eating_a_red_apple_grows_the_snake_and_increases_the_score() {
        Game game = emptyGame(new Position(5, 5), Direction.UP);
        game.board().add(new RedApple(new Position(5, 4)));

        game.tick();

        assertEquals(2, game.snake().size());
        assertEquals(1, game.score());
        assertTrue(game.board().isEmpty());
    }

    @Test
    void running_into_a_wall_loses_the_game_and_the_snake_does_not_move() {
        Game game = emptyGame(new Position(5, 5), Direction.RIGHT);
        game.board().add(new Wall(new Position(6, 5)));

        game.tick();

        assertEquals(GameState.LOST, game.state());
        assertEquals(new Position(5, 5), game.snake().head());
    }

    @Test
    void new_entities_can_be_added_without_modifying_game() {
        record GoldenApple(Position position) implements BoardEntity {
            @Override
            public void onHeadEnter(GameContext context) {
                context.board().remove(this);
                context.addScore(5);
            }
        }
        Game game = emptyGame(new Position(5, 5), Direction.RIGHT);
        game.board().add(new GoldenApple(new Position(6, 5)));

        game.tick();

        assertEquals(5, game.score());
        assertEquals(1, game.snake().size());
    }

    @Test
    void leaving_the_grid_loses_the_game() {
        Game game = emptyGame(new Position(0, 0), Direction.LEFT);

        game.tick();

        assertEquals(GameState.LOST, game.state());
    }

    @Test
    void no_more_moves_once_the_game_is_over() {
        Game game = emptyGame(new Position(0, 0), Direction.LEFT);
        game.tick();
        Position headAtGameOver = game.snake().head();

        game.tick();

        assertEquals(headAtGameOver, game.snake().head());
    }

    @Test
    void hitting_its_own_body_loses_the_game() {
        Game game = emptyGame(new Position(5, 5), Direction.RIGHT);
        game.snake().grow(4);
        game.tick(); // (6,5)
        game.tick(); // (7,5)
        game.changeDirection(Direction.DOWN);
        game.tick(); // (7,6)
        game.changeDirection(Direction.LEFT);
        game.tick(); // (6,6)
        game.changeDirection(Direction.UP);

        game.tick(); // (6,5) is part of the body

        assertEquals(GameState.LOST, game.state());
    }

    @Test
    void the_head_can_follow_the_tail() {
        Game game = emptyGame(new Position(5, 5), Direction.RIGHT);
        game.snake().grow(3);
        game.tick();
        game.changeDirection(Direction.DOWN);
        game.tick();
        game.changeDirection(Direction.LEFT);
        game.tick();
        game.changeDirection(Direction.UP);

        game.tick();

        assertEquals(GameState.RUNNING, game.state());
        assertEquals(4, game.snake().size());
    }

    @Test
    void two_quick_key_presses_cannot_make_the_snake_turn_back() {
        Game game = emptyGame(new Position(5, 5), Direction.RIGHT);
        game.snake().grow(2);
        game.tick();
        game.tick();

        game.changeDirection(Direction.UP);
        game.changeDirection(Direction.LEFT);
        game.tick();
        game.tick();

        assertEquals(GameState.RUNNING, game.state());
        assertEquals(new Position(6, 4), game.snake().head());
    }

    @Test
    void reversing_direction_is_ignored() {
        Game game = emptyGame(new Position(5, 5), Direction.RIGHT);

        game.changeDirection(Direction.LEFT);
        game.tick();

        assertEquals(new Position(6, 5), game.snake().head());
    }

    @Test
    void spawners_run_at_start_and_after_each_move() {
        List<Integer> calls = new ArrayList<>();
        Game game = new Game(new GameConfig(10, 10, new Position(5, 5), Direction.RIGHT, 100),
                new Random(0), List.of(context -> calls.add(context.score())));

        game.tick();
        game.tick();

        assertEquals(3, calls.size());
    }

    @Test
    void default_game_keeps_one_red_apple_on_the_board() {
        Game game = new Game(new GameConfig(10, 10, new Position(5, 5), Direction.RIGHT, 100));
        assertEquals(1, game.board().count(RedApple.class));

        game.board().remove(game.board().entities().getFirst());
        game.board().add(new RedApple(new Position(6, 5)));
        game.tick();

        assertEquals(1, game.score());
        assertEquals(1, game.board().count(RedApple.class));
    }

    @Test
    void filling_the_grid_wins_the_game() {
        Game game = new Game(new GameConfig(2, 1, new Position(0, 0), Direction.RIGHT, 100));

        game.tick();

        assertEquals(GameState.WON, game.state());
    }

    @Test
    void same_seed_gives_the_same_board() {
        GameConfig config = new GameConfig(10, 10, new Position(5, 5), Direction.RIGHT, 100);

        Game game1 = new Game(config, new Random(7), List.of(new KeepApples(3)));
        Game game2 = new Game(config, new Random(7), List.of(new KeepApples(3)));

        assertEquals(game1.board().entities(), game2.board().entities());
    }

    @Test
    void listeners_are_notified() {
        Game game = emptyGame(new Position(0, 0), Direction.RIGHT);
        game.board().add(new RedApple(new Position(1, 0)));
        game.board().add(new Wall(new Position(2, 0)));
        List<String> events = new ArrayList<>();
        game.addListener(new GameListener() {
            @Override
            public void onEntityReached(BoardEntity entity) {
                events.add("reached " + entity.getClass().getSimpleName());
            }

            @Override
            public void onGameOver(GameState finalState) {
                events.add("over " + finalState);
            }
        });

        game.tick(); // apple
        game.tick(); // wall

        assertEquals(List.of("reached RedApple", "reached Wall", "over LOST"), events);
    }

    @Test
    void invalid_config_is_rejected() {
        assertThrows(IllegalArgumentException.class,
                () -> new GameConfig(10, 10, new Position(10, 0), Direction.RIGHT, 100));
    }
}
