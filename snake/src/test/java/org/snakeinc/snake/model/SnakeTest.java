package org.snakeinc.snake.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.snakeinc.snake.model.grid.Direction;
import org.snakeinc.snake.model.grid.Position;

class SnakeTest {

    @Test
    void move_up_decreases_y() {
        Snake snake = new Snake(new Position(5, 5), Direction.UP);

        snake.move();

        assertEquals(new Position(5, 4), snake.head());
        assertEquals(1, snake.size());
    }

    @Test
    void grow_adds_one_segment_per_move() {
        Snake snake = new Snake(new Position(5, 5), Direction.RIGHT);

        snake.grow(2);
        snake.move();
        snake.move();
        snake.move();

        assertEquals(List.of(new Position(8, 5), new Position(7, 5), new Position(6, 5)), snake.body());
    }

    @Test
    void cannot_turn_back_on_itself() {
        Snake snake = new Snake(new Position(5, 5), Direction.RIGHT);

        assertFalse(snake.turn(Direction.LEFT));
        assertEquals(Direction.RIGHT, snake.direction());

        assertTrue(snake.turn(Direction.UP));
        assertEquals(Direction.UP, snake.direction());
    }

    @Test
    void tail_is_not_an_obstacle_when_not_growing() {
        Snake snake = new Snake(new Position(5, 5), Direction.RIGHT);
        snake.grow(1);
        snake.move(); // body: (6,5), (5,5)

        snake.move(); // body: (7,5), (6,5)

        assertFalse(snake.wouldCollideWith(snake.tail()));
    }

    @Test
    void tail_is_an_obstacle_when_growing() {
        Snake snake = new Snake(new Position(5, 5), Direction.RIGHT);
        snake.grow(1);
        snake.move();
        snake.grow(1);

        assertTrue(snake.wouldCollideWith(snake.tail()));
    }
}
