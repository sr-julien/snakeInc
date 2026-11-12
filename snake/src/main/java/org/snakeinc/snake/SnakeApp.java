package org.snakeinc.snake;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import org.snakeinc.snake.model.GameConfig;
import org.snakeinc.snake.ui.GamePanel;

public class SnakeApp {

    public static void main(String[] args) {
        // Swing components must be created on the Event Dispatch Thread.
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Snake Inc");
            frame.add(new GamePanel(GameConfig.defaults()));
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
