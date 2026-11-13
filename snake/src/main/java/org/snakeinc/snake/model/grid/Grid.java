package org.snakeinc.snake.model.grid;

import java.util.ArrayList;
import java.util.List;

/**
 * Bounds of the playing area. Its content is held by the snake and the board.
 */
public final class Grid {

    private final int width;
    private final int height;

    public Grid(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    public int tileCount() {
        return width * height;
    }

    public boolean contains(Position position) {
        return position.x() >= 0 && position.x() < width
                && position.y() >= 0 && position.y() < height;
    }

    public List<Position> positions() {
        List<Position> positions = new ArrayList<>(tileCount());
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                positions.add(new Position(x, y));
            }
        }
        return positions;
    }
}
