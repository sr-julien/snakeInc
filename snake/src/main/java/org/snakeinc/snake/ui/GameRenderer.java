package org.snakeinc.snake.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import org.snakeinc.snake.model.Game;
import org.snakeinc.snake.model.entity.BoardEntity;
import org.snakeinc.snake.model.entity.RedApple;
import org.snakeinc.snake.model.entity.Wall;
import org.snakeinc.snake.model.grid.Position;

public final class GameRenderer {

    private static final Color SNAKE_COLOR = Color.GREEN;
    private static final Color SNAKE_BORDER_COLOR = Color.GREEN.darker();

    private final int tileSize;

    public GameRenderer(int tileSize) {
        this.tileSize = tileSize;
    }

    public int tileSize() {
        return tileSize;
    }

    public void draw(Graphics2D g, Game game) {
        for (BoardEntity entity : game.board().entities()) {
            drawEntity(g, entity);
        }
        for (Position segment : game.snake().body()) {
            drawSnakeSegment(g, segment);
        }
    }

    // Add a case here for each new kind of entity.
    private void drawEntity(Graphics2D g, BoardEntity entity) {
        int x = pixel(entity.position().x());
        int y = pixel(entity.position().y());
        switch (entity) {
            case RedApple ignored -> {
                g.setColor(Color.RED);
                g.fillOval(x, y, tileSize, tileSize);
            }
            case Wall ignored -> {
                g.setColor(Color.GRAY);
                g.fillRect(x, y, tileSize, tileSize);
            }
            default -> {
                g.setColor(Color.WHITE);
                g.fillOval(x, y, tileSize, tileSize);
            }
        }
    }

    private void drawSnakeSegment(Graphics2D g, Position segment) {
        int x = pixel(segment.x());
        int y = pixel(segment.y());
        g.setColor(SNAKE_COLOR);
        g.fillRect(x, y, tileSize, tileSize);
        g.setColor(SNAKE_BORDER_COLOR);
        g.setStroke(new BasicStroke(2));
        g.drawRect(x, y, tileSize, tileSize);
    }

    private int pixel(int tileCoordinate) {
        return tileCoordinate * tileSize;
    }
}
