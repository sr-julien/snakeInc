package org.snakeinc.snake.model;

final class InvincibleSnakeState extends TimedSnakeState {

    InvincibleSnakeState(int remainingTicks) {
        super(remainingTicks);
    }

    @Override
    public boolean canCrossBody() {
        return true;
    }

    @Override
    public boolean canCrossWalls() {
        return false;
    }

    @Override
    protected SnakeState withRemainingTicks(int ticks) {
        return new InvincibleSnakeState(ticks);
    }
}
