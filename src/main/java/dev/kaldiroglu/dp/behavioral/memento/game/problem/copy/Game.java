package dev.kaldiroglu.dp.behavioral.memento.game.problem.copy;

/** Stage three: the game keeps a copy of the player as its checkpoint. */
public final class Game {

    private Player checkpoint;

    public void checkpoint(Player player) {
        checkpoint = new Player(player);
    }

    public void load(Player player) {
        player.restoreFrom(checkpoint);
    }
}
