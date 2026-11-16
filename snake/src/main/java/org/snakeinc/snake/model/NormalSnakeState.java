package org.snakeinc.snake.model;

final class NormalSnakeState implements SnakeState {

    @Override
    public boolean canCrossBody() {
        return false;
    }

    @Override
    public boolean canCrossWalls() {
        return false;
    }

    @Override
    public SnakeState withInvincibility(int ticks) {
        return new InvincibleSnakeState(ticks);
    }

    @Override
    public SnakeState withGhost(int ticks) {
        return new GhostSnakeState(ticks);
    }

    @Override
    public SnakeState afterTick() {
        return this;
    }
}
