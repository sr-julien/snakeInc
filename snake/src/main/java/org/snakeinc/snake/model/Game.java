package org.snakeinc.snake.model;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.random.RandomGenerator;
import org.snakeinc.snake.model.entity.Board;
import org.snakeinc.snake.model.entity.BoardEntity;
import org.snakeinc.snake.model.grid.Direction;
import org.snakeinc.snake.model.grid.Grid;
import org.snakeinc.snake.model.grid.Position;
import org.snakeinc.snake.model.spawner.KeepApples;
import org.snakeinc.snake.model.spawner.Spawner;

/**
 * Game rules. To extend the game, use {@link BoardEntity}, {@link Spawner} and {@link GameListener}
 * rather than modifying this class.
 */
public final class Game {

    private static final int MAX_BUFFERED_TURNS = 2;

    private final GameConfig config;
    private final Grid grid;
    private final Snake snake;
    private final Board board = new Board();
    private final RandomGenerator random;
    private final List<Spawner> spawners;
    private final List<GameListener> listeners = new ArrayList<>();
    private final Deque<Direction> pendingTurns = new ArrayDeque<>();
    private final GameContext context = new Context();

    private GameState state = GameState.RUNNING;
    private int score = 0;

    public Game() {
        this(GameConfig.defaults());
    }

    public Game(GameConfig config) {
        this(config, new Random(), List.of(new KeepApples(1)));
    }

    public Game(GameConfig config, RandomGenerator random, List<Spawner> spawners) {
        this.config = config;
        this.grid = new Grid(config.width(), config.height());
        this.snake = new Snake(config.snakeStart(), config.initialDirection());
        this.random = random;
        this.spawners = List.copyOf(spawners);
        runSpawners();
    }

    public GameConfig config() {
        return config;
    }

    public Grid grid() {
        return grid;
    }

    public Snake snake() {
        return snake;
    }

    public Board board() {
        return board;
    }

    public GameState state() {
        return state;
    }

    public int score() {
        return score;
    }

    public void addListener(GameListener listener) {
        listeners.add(listener);
    }

    /** Applied at the next tick. Reversals are ignored. */
    public void changeDirection(Direction direction) {
        Direction last = pendingTurns.isEmpty() ? snake.direction() : pendingTurns.getLast();
        if (direction == last || direction == last.opposite() || pendingTurns.size() >= MAX_BUFFERED_TURNS) {
            return;
        }
        pendingTurns.addLast(direction);
    }

    public void tick() {
        if (state != GameState.RUNNING) {
            return;
        }
        step();
        if (state != GameState.RUNNING) {
            listeners.forEach(listener -> listener.onGameOver(state));
        }
    }

    private void step() {
        if (!pendingTurns.isEmpty()) {
            snake.turn(pendingTurns.removeFirst());
        }

        Position next = snake.nextHead();
        if (!grid.contains(next) || snake.wouldCollideWith(next)) {
            end(GameState.LOST);
            return;
        }

        board.entityAt(next).ifPresent(entity -> {
            entity.onHeadEnter(context);
            listeners.forEach(listener -> listener.onEntityReached(entity));
        });
        if (state != GameState.RUNNING) {
            return;
        }

        snake.move();
        if (snake.size() >= grid.tileCount()) {
            end(GameState.WON);
            return;
        }
        runSpawners();
    }

    private void runSpawners() {
        for (Spawner spawner : spawners) {
            spawner.spawn(context);
            if (state != GameState.RUNNING) {
                return;
            }
        }
    }

    private void end(GameState finalState) {
        if (state == GameState.RUNNING) {
            state = finalState;
        }
    }

    private List<Position> freeTiles() {
        Set<Position> snakeBody = new HashSet<>(snake.body());
        List<Position> free = new ArrayList<>();
        for (Position position : grid.positions()) {
            if (!snakeBody.contains(position) && !board.isOccupied(position)) {
                free.add(position);
            }
        }
        return free;
    }

    private final class Context implements GameContext {

        @Override
        public Grid grid() {
            return grid;
        }

        @Override
        public Snake snake() {
            return snake;
        }

        @Override
        public Board board() {
            return board;
        }

        @Override
        public RandomGenerator random() {
            return random;
        }

        @Override
        public int score() {
            return score;
        }

        @Override
        public void addScore(int points) {
            score += points;
        }

        @Override
        public void lose() {
            end(GameState.LOST);
        }

        @Override
        public void win() {
            end(GameState.WON);
        }

        @Override
        public List<Position> freeTiles() {
            return Game.this.freeTiles();
        }
    }
}
