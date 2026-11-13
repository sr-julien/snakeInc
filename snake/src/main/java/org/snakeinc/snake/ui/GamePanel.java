package org.snakeinc.snake.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JPanel;
import javax.swing.Timer;
import org.snakeinc.snake.model.Game;
import org.snakeinc.snake.model.GameConfig;
import org.snakeinc.snake.model.GameListener;
import org.snakeinc.snake.model.GameState;
import org.snakeinc.snake.model.grid.Direction;

public final class GamePanel extends JPanel {

    public static final int TILE_PIXEL_SIZE = 20;

    private final Game game;
    private final GameRenderer renderer = new GameRenderer(TILE_PIXEL_SIZE);
    private final Timer timer;

    public GamePanel(GameConfig config) {
        this.game = new Game(config);
        this.setPreferredSize(new Dimension(config.width() * TILE_PIXEL_SIZE, config.height() * TILE_PIXEL_SIZE));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                onKeyPressed(e.getKeyCode());
            }
        });
        this.timer = new Timer(config.tickMillis(), e -> onTick());
        game.addListener(new GameListener() {
            @Override
            public void onGameOver(GameState finalState) {
                timer.stop();
            }
        });
        timer.start();
    }

    private void onTick() {
        game.tick();
        repaint();
    }

    private void onKeyPressed(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_LEFT -> game.changeDirection(Direction.LEFT);
            case KeyEvent.VK_RIGHT -> game.changeDirection(Direction.RIGHT);
            case KeyEvent.VK_UP -> game.changeDirection(Direction.UP);
            case KeyEvent.VK_DOWN -> game.changeDirection(Direction.DOWN);
            default -> {
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        switch (game.state()) {
            case RUNNING -> renderer.draw(g2, game);
            case LOST -> drawCenteredMessage(g2, "Game Over - Score: " + game.score(), Color.RED);
            case WON -> drawCenteredMessage(g2, "You win! - Score: " + game.score(), Color.GREEN);
        }
    }

    private void drawCenteredMessage(Graphics2D g, String message, Color color) {
        g.setColor(color);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        FontMetrics metrics = g.getFontMetrics();
        g.drawString(message, (getWidth() - metrics.stringWidth(message)) / 2, getHeight() / 2);
    }
}
