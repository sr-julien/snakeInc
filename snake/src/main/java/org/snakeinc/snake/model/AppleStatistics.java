package org.snakeinc.snake.model;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import org.snakeinc.snake.model.entity.Board;
import org.snakeinc.snake.model.entity.BoardEntity;

/**
 * Counts the apples eaten by the snake, by type. Attach it with {@link Game#addListener}.
 * Prints the running statistics to the console after each apple, and the final ones on game over.
 */
public final class AppleStatistics implements GameListener {

    private final Board board;
    private final Map<Class<? extends BoardEntity>, Integer> eatenByType = new LinkedHashMap<>();

    public AppleStatistics(Board board) {
        this.board = board;
    }

    @Override
    public void onEntityReached(BoardEntity entity) {
        // Listeners run after onHeadEnter: an entity that removed itself was eaten,
        // while a wall (or any entity that stays) was not.
        if (board.entityAt(entity.position()).isPresent()) {
            return;
        }
        eatenByType.merge(entity.getClass(), 1, Integer::sum);
        print();
    }

    public int count(Class<? extends BoardEntity> type) {
        return eatenByType.getOrDefault(type, 0);
    }

    /** Prints the statistics to the console, e.g. {@code Apples eaten: 10 RedApple, 2 GoldenApple}. */
    public void print() {
        String counts = eatenByType.entrySet().stream()
                .map(entry -> entry.getValue() + " " + entry.getKey().getSimpleName())
                .collect(Collectors.joining(", "));
        System.out.println(counts.isEmpty() ? "Apples eaten: none" : "Apples eaten: " + counts);
    }
}
