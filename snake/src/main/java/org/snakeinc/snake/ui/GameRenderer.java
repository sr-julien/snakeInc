package org.snakeinc.snake.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import org.snakeinc.snake.model.Game;
import org.snakeinc.snake.model.entity.BoardEntity;
import org.snakeinc.snake.model.entity.GoldenApple;
import org.snakeinc.snake.model.entity.GhostApple;
import org.snakeinc.snake.model.entity.RedApple;
import org.snakeinc.snake.model.entity.StarApple;
import org.snakeinc.snake.model.entity.Wall;
import org.snakeinc.snake.model.grid.Position;

public final class GameRenderer {

    private static final Color SNAKE_COLOR = Color.GREEN;
    private static final Color SNAKE_BORDER_COLOR = Color.GREEN.darker();
    private static final Color RED_APPLE_COLOR = new Color(220, 40, 40);
    private static final Color GOLDEN_APPLE_COLOR = new Color(255, 215, 0);
    private static final Color STAR_APPLE_COLOR = new Color(230, 70, 220);
    private static final Color GHOST_APPLE_COLOR = new Color(70, 210, 235);
    private static final Color WALL_COLOR = Color.GRAY;

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
            case RedApple ignored -> drawApple(g, x, y, RED_APPLE_COLOR);
            case GoldenApple ignored -> drawApple(g, x, y, GOLDEN_APPLE_COLOR);
            case StarApple ignored -> drawApple(g, x, y, STAR_APPLE_COLOR);
            case GhostApple ignored -> drawGhostApple(g, x, y);
            case Wall ignored -> drawWall(g, x, y);
            default -> {
                g.setColor(Color.WHITE);
                g.fillOval(x, y, tileSize, tileSize);
            }
        }
    }

    private void drawApple(Graphics2D g, int x, int y, Color color) {
        g.setColor(color);
        g.fillOval(x, y, tileSize, tileSize);
    }

    private void drawGhostApple(Graphics2D g, int x, int y) {
        g.setColor(GHOST_APPLE_COLOR);
        g.fillOval(x, y, tileSize, tileSize);
        g.setColor(Color.WHITE);
        g.setStroke(new BasicStroke(2));
        g.drawOval(x + 2, y + 2, tileSize - 4, tileSize - 4);
    }

    private void drawWall(Graphics2D g, int x, int y) {
        g.setColor(WALL_COLOR);
        g.fillRect(x, y, tileSize, tileSize);
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
