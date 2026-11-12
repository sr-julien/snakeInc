package org.snakeinc.snake.model;

import org.snakeinc.snake.model.grid.Direction;
import org.snakeinc.snake.model.grid.Position;

public record GameConfig(int width, int height, Position snakeStart, Direction initialDirection, int tickMillis) {

    public GameConfig {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Grid size must be positive: " + width + "x" + height);
        }
        if (snakeStart == null || initialDirection == null) {
            throw new IllegalArgumentException("snakeStart and initialDirection are required");
        }
        if (snakeStart.x() < 0 || snakeStart.x() >= width || snakeStart.y() < 0 || snakeStart.y() >= height) {
            throw new IllegalArgumentException("Snake start " + snakeStart + " is outside the grid");
        }
        if (tickMillis <= 0) {
            throw new IllegalArgumentException("tickMillis must be > 0");
        }
    }

    public static GameConfig defaults() {
        return new GameConfig(30, 30, new Position(5, 5), Direction.RIGHT, 100);
    }
}
