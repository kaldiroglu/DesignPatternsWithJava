package dev.kaldiroglu.dp.behavioral.memento.game.problem.copy;

/**
 * The game saves at the bridge and shows its load screen. The save is a whole Player, so
 * the game can read every field of it.
 */
public final class ReadTheSaveMain {

    public static void main(String[] args) {
        Player player = new Player();
        Game game = new Game();
        player.pickUp("sword");
        player.pickUp("shield");
        player.moveTo("bridge");
        game.checkpoint(player);
        System.out.println(game.loadScreen());
    }
}
