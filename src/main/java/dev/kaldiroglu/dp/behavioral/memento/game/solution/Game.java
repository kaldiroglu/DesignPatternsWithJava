package dev.kaldiroglu.dp.behavioral.memento.game.solution;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * The <b>Caretaker</b>: the game decides when to save and which checkpoint to load, and keeps
 * the checkpoints. It never looks inside one.
 */
public final class Game {

    private final Deque<Player.Checkpoint> checkpoints = new ArrayDeque<>();

    public void checkpoint(Player player) {
        checkpoints.push(player.save());
    }

    public void loadLatest(Player player) {
        player.load(checkpoints.peek());
    }

    public int checkpointCount() {
        return checkpoints.size();
    }
}
