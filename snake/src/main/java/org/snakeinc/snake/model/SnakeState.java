package org.snakeinc.snake.model;

/**
 * State of the snake's temporary movement abilities.
 */
interface SnakeState {

    boolean canCrossBody();

    boolean canCrossWalls();

    SnakeState withInvincibility(int ticks);

    SnakeState withGhost(int ticks);

    SnakeState afterTick();
}
