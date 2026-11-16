package org.snakeinc.snake.model;

final class CombinedSnakeState implements SnakeState {

    private final SnakeState first;
    private final SnakeState second;

    CombinedSnakeState(SnakeState first, SnakeState second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public boolean canCrossBody() {
        return first.canCrossBody() || second.canCrossBody();
    }

    @Override
    public boolean canCrossWalls() {
        return first.canCrossWalls() || second.canCrossWalls();
    }

    @Override
    public SnakeState withInvincibility(int ticks) {
        return new CombinedSnakeState(first.withInvincibility(ticks), second);
    }

    @Override
    public SnakeState withGhost(int ticks) {
        return new CombinedSnakeState(first, second.withGhost(ticks));
    }

    @Override
    public SnakeState afterTick() {
        SnakeState nextFirst = first.afterTick();
        SnakeState nextSecond = second.afterTick();
        if (nextFirst instanceof NormalSnakeState) {
            return nextSecond;
        }
        if (nextSecond instanceof NormalSnakeState) {
            return nextFirst;
        }
        return new CombinedSnakeState(nextFirst, nextSecond);
    }
}
