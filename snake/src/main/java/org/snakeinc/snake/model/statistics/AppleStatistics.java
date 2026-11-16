package org.snakeinc.snake.model.statistics;

import java.io.PrintStream;
import java.util.Map;
import java.util.TreeMap;
import org.snakeinc.snake.model.GameListener;
import org.snakeinc.snake.model.GameState;
import org.snakeinc.snake.model.entity.Apple;
import org.snakeinc.snake.model.entity.BoardEntity;

/**
 * Counts the apples eaten by type and prints them when the game is over.
 */
public final class AppleStatistics implements GameListener {

    /** Sorted by type name so that the report is always printed in the same order. */
    private final Map<String, Integer> eatenByType = new TreeMap<>();
    private final PrintStream out;

    public AppleStatistics(PrintStream out) {
        this.out = out;
    }

    @Override
    public void onEntityReached(BoardEntity entity) {
        if (entity instanceof Apple) {
            eatenByType.merge(entity.getClass().getSimpleName(), 1, Integer::sum);
        }
    }

    @Override
    public void onGameOver(GameState finalState) {
        out.println(report());
    }

    public int eaten(Class<? extends Apple> type) {
        return eatenByType.getOrDefault(type.getSimpleName(), 0);
    }

    public int totalEaten() {
        return eatenByType.values().stream().mapToInt(Integer::intValue).sum();
    }

    public String report() {
        StringBuilder report = new StringBuilder("=== Apples eaten ===");
        eatenByType.forEach((type, count) -> report.append(System.lineSeparator()).append(type).append(": ").append(count));
        report.append(System.lineSeparator()).append("Total: ").append(totalEaten());
        return report.toString();
    }
}
