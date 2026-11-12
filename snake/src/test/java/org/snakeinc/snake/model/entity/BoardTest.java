package org.snakeinc.snake.model.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.snakeinc.snake.model.grid.Position;

class BoardTest {

    @Test
    void cannot_put_two_entities_on_the_same_tile() {
        Board board = new Board();
        board.add(new RedApple(new Position(1, 1)));

        assertThrows(IllegalStateException.class, () -> board.add(new Wall(new Position(1, 1))));
        assertEquals(1, board.size());
    }

    @Test
    void remove_frees_the_tile() {
        Board board = new Board();
        RedApple apple = new RedApple(new Position(1, 1));
        board.add(apple);

        board.remove(apple);

        assertTrue(board.isEmpty());
        assertTrue(board.entityAt(new Position(1, 1)).isEmpty());
    }

    @Test
    void count_by_type() {
        Board board = new Board();
        board.add(new RedApple(new Position(0, 0)));
        board.add(new RedApple(new Position(1, 0)));
        board.add(new Wall(new Position(2, 0)));

        assertEquals(2, board.count(RedApple.class));
        assertEquals(1, board.count(Wall.class));
    }
}
