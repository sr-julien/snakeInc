package org.snakeinc.snake.model;

abstract class TimedSnakeState implements SnakeState {

    private final int remainingTicks;

    TimedSnakeState(int remainingTicks) {
        this.remainingTicks = remainingTicks;
    }

    @Override
    public final SnakeState withInvincibility(int ticks) {
        return new CombinedSnakeState(this, new InvincibleSnakeState(ticks));
    }

    @Override
    public final SnakeState withGhost(int ticks) {
        return new CombinedSnakeState(this, new GhostSnakeState(ticks));
    }

    @Override
    public final SnakeState afterTick() {
        if (remainingTicks <= 1) {
            return new NormalSnakeState();
        }
        return withRemainingTicks(remainingTicks - 1);
    }

    protected abstract SnakeState withRemainingTicks(int ticks);
}
