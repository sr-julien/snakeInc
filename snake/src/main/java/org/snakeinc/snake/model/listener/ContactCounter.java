package org.snakeinc.snake.model.listener;

import org.snakeinc.snake.model.GameListener;
import org.snakeinc.snake.model.entity.BoardEntity;
import java.util.HashMap;
import java.util.Map;

public final class ContactCounter implements GameListener {
    private Map<Class<? extends BoardEntity>, Integer> contactCountMap = new HashMap<>();


    @Override
    public void onEntityReached(BoardEntity entity) {
        contactCountMap.merge(entity.getClass(), 1, Integer::sum);
        logContactCounts();
    }
    
    @Override 
    public void onGameOver(org.snakeinc.snake.model.GameState finalState) {
        contactCountMap.clear();
    }


    private void logContactCounts() {
        System.out.println("Contact counts:");
        contactCountMap.forEach((entityClass, count) -> System.out.println("  " + entityClass.getSimpleName() + ": " + count));
    }
}   