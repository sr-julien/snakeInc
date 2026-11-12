package org.snakeinc.snake.model.entity;

import org.snakeinc.snake.model.GameContext;
import org.snakeinc.snake.model.grid.Position;

/**
 * An element on the board (apple, wall...). Implement it to add new elements to the game.
 */
public interface BoardEntity {

    Position position();

    /** Called before the snake moves onto this tile. If the game ends here, the snake does not move. */
    void onHeadEnter(GameContext context);
}
