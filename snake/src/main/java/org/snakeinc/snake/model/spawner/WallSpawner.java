package org.snakeinc.snake.model.spawner;

import org.snakeinc.snake.model.GameContext;

import org.snakeinc.snake.model.entity.Wall;

public class WallSpawner implements Spawner {
    private final int step;
    private int counter;

    public WallSpawner(int step) {
        this.step=step; 
        this.counter=0;
    }

    @Override
    public void spawn(GameContext context) {
        if (counter < context.score() / step) {
            var tile = context.randomFreeTile();
            if (tile.isEmpty()) {
                return;
            }
            context.board().add(new Wall(tile.get()));
            counter++;
        }
    }
}
