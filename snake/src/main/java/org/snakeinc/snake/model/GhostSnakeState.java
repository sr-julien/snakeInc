package org.snakeinc.snake.model;

final class GhostSnakeState extends TimedSnakeState {

    GhostSnakeState(int remainingTicks) {
        super(remainingTicks);
    }

    @Override
    public boolean canCrossBody() {
        return false;
    }

    @Override
    public boolean canCrossWalls() {
        return true;
    }

    @Override
    protected SnakeState withRemainingTicks(int ticks) {
        return new GhostSnakeState(ticks);
    }
}
