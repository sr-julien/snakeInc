package org.snakeinc.snake.model.entity;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.snakeinc.snake.model.grid.Position;

/**
 * Entities on the grid, at most one per tile.
 */
public final class Board {

    private final Map<Position, BoardEntity> entities = new LinkedHashMap<>();

    /** @throws IllegalStateException if the tile is already taken. */
    public void add(BoardEntity entity) {
        BoardEntity previous = entities.putIfAbsent(entity.position(), entity);
        if (previous != null) {
            throw new IllegalStateException("Tile " + entity.position() + " already holds " + previous);
        }
    }

    public void remove(BoardEntity entity) {
        entities.remove(entity.position(), entity);
    }

    public Optional<BoardEntity> entityAt(Position position) {
        return Optional.ofNullable(entities.get(position));
    }

    public boolean isOccupied(Position position) {
        return entities.containsKey(position);
    }

    public List<BoardEntity> entities() {
        return List.copyOf(entities.values());
    }

    public int count(Class<? extends BoardEntity> type) {
        return (int) entities.values().stream().filter(type::isInstance).count();
    }

    public int size() {
        return entities.size();
    }

    public boolean isEmpty() {
        return entities.isEmpty();
    }
}
