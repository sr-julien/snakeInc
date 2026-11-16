package org.snakeinc.snake.model;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import org.snakeinc.snake.model.grid.Direction;
import org.snakeinc.snake.model.grid.Grid;
import org.snakeinc.snake.model.grid.Position;

public final class Snake {

    private final Deque<Position> body = new ArrayDeque<>();
    private Direction direction;
    private int pendingGrowth = 0;
    private SnakeState state = new NormalSnakeState();

    public Snake(Position head, Direction direction) {
        this.body.addFirst(head);
        this.direction = direction;
    }

    public Position head() {
        return body.getFirst();
    }

    public Position tail() {
        return body.getLast();
    }

    /** Head first. */
    public List<Position> body() {
        return List.copyOf(body);
    }

    public int size() {
        return body.size();
    }

    public Direction direction() {
        return direction;
    }

    /** Ignored (returns false) if it would reverse the snake. */
    public boolean turn(Direction newDirection) {
        if (newDirection == direction.opposite()) {
            return false;
        }
        direction = newDirection;
        return true;
    }

    public Position nextHead() {
        return head().next(direction);
    }

    public Position nextHead(Grid grid) {
        Position next = nextHead();
        if (!state.canCrossWalls()) {
            return next;
        }
        return new Position(
                Math.floorMod(next.x(), grid.width()),
                Math.floorMod(next.y(), grid.height()));
    }

    public boolean canCrossBody() {
        return state.canCrossBody();
    }

    public boolean canCrossWalls() {
        return state.canCrossWalls();
    }

    public void becomeInvincible(int ticks) {
        validateDuration(ticks);
        state = state.withInvincibility(ticks);
    }

    public void becomeGhost(int ticks) {
        validateDuration(ticks);
        state = state.withGhost(ticks);
    }

    public void updateState() {
        state = state.afterTick();
    }

    /** One segment is added per move. */
    public void grow(int segments) {
        if (segments < 0) {
            throw new IllegalArgumentException("segments must be >= 0");
        }
        pendingGrowth += segments;
    }

    public boolean occupies(Position position) {
        return body.contains(position);
    }

    /** The tail does not count when not growing: it leaves its tile during the move. */
    public boolean wouldCollideWith(Position position) {
        if (pendingGrowth == 0 && position.equals(tail())) {
            return false;
        }
        return occupies(position);
    }

    public void move() {
        body.addFirst(nextHead());
        if (pendingGrowth > 0) {
            pendingGrowth--;
        } else {
            body.removeLast();
        }
    }

    public void moveTo(Position position) {
        body.addFirst(position);
        if (pendingGrowth > 0) {
            pendingGrowth--;
        } else {
            body.removeLast();
        }
    }

    private static void validateDuration(int ticks) {
        if (ticks <= 0) {
            throw new IllegalArgumentException("ticks must be > 0");
        }
    }
}
