package org.snakeinc.snake.model.spawner;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.snakeinc.snake.model.GameContext;
import org.snakeinc.snake.model.entity.Wall;
import org.snakeinc.snake.model.grid.Direction;
import org.snakeinc.snake.model.grid.Grid;
import org.snakeinc.snake.model.grid.Position;

/**
 * Places walls at random at the start of the game, on {@code density} of the tiles (0.03 = 3%).
 * To keep the game playable, a wall is never placed:
 * <ul>
 *     <li>near the head of the snake or right in front of it,</li>
 *     <li>where it would leave a free tile with less than 2 open neighbours (a dead end the snake cannot leave),</li>
 *     <li>where it would split the free tiles in several areas (an apple could then be unreachable).</li>
 * </ul>
 * Fewer walls than requested may be placed if the grid is too small.
 */
public final class RandomWalls implements Spawner {

    static final int SAFE_DISTANCE = 2;
    static final int SAFE_TILES_AHEAD = 5;

    private final double density;
    private boolean placed = false;

    public RandomWalls(double density) {
        if (density < 0 || density >= 1) {
            throw new IllegalArgumentException("density must be in [0, 1)");
        }
        this.density = density;
    }

    @Override
    public void spawn(GameContext context) {
        if (placed) {
            return;
        }
        placed = true;

        Grid grid = context.grid();
        int count = (int) (grid.tileCount() * density);
        Set<Position> walls = new HashSet<>();
        context.board().entities().stream()
                .filter(Wall.class::isInstance)
                .forEach(wall -> walls.add(wall.position()));

        List<Position> candidates = new ArrayList<>(context.freeTiles());
        candidates.removeAll(safeZone(context));
        Collections.shuffle(candidates, context.random());

        int added = 0;
        for (Position candidate : candidates) {
            if (added >= count) {
                return;
            }
            walls.add(candidate);
            if (isPlayable(grid, walls, context.snake().head())) {
                context.board().add(new Wall(candidate));
                added++;
            } else {
                walls.remove(candidate);
            }
        }
    }

    private static Set<Position> safeZone(GameContext context) {
        Position head = context.snake().head();
        Set<Position> zone = new HashSet<>();
        for (int dx = -SAFE_DISTANCE; dx <= SAFE_DISTANCE; dx++) {
            for (int dy = -SAFE_DISTANCE; dy <= SAFE_DISTANCE; dy++) {
                if (Math.abs(dx) + Math.abs(dy) <= SAFE_DISTANCE) {
                    zone.add(new Position(head.x() + dx, head.y() + dy));
                }
            }
        }
        Position ahead = head;
        for (int i = 0; i < SAFE_TILES_AHEAD; i++) {
            ahead = ahead.next(context.snake().direction());
            zone.add(ahead);
        }
        return zone;
    }

    /** Every open tile has at least 2 open neighbours and all open tiles are reachable from {@code start}. */
    private static boolean isPlayable(Grid grid, Set<Position> walls, Position start) {
        int openTiles = 0;
        for (Position position : grid.positions()) {
            if (!walls.contains(position)) {
                openTiles++;
                if (openNeighbours(grid, walls, position).size() < 2) {
                    return false;
                }
            }
        }

        Set<Position> reached = new HashSet<>();
        Deque<Position> toVisit = new ArrayDeque<>();
        reached.add(start);
        toVisit.add(start);
        while (!toVisit.isEmpty()) {
            for (Position neighbour : openNeighbours(grid, walls, toVisit.removeFirst())) {
                if (reached.add(neighbour)) {
                    toVisit.add(neighbour);
                }
            }
        }
        return reached.size() == openTiles;
    }

    private static List<Position> openNeighbours(Grid grid, Set<Position> walls, Position position) {
        List<Position> neighbours = new ArrayList<>(4);
        for (Direction direction : Direction.values()) {
            Position neighbour = position.next(direction);
            if (grid.contains(neighbour) && !walls.contains(neighbour)) {
                neighbours.add(neighbour);
            }
        }
        return neighbours;
    }
}
