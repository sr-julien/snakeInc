package org.snakeinc.snake.model.grid;

/**
 * (0, 0) is the top-left corner.
 */
public record Position(int x, int y) {

    public Position next(Direction direction) {
        return new Position(x + direction.dx(), y + direction.dy());
    }
}
