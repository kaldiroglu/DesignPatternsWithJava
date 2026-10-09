package dev.kaldiroglu.dp.behavioral.memento.game.problem.history;

/**
 * The play from the deck, with the player keeping its own checkpoints. Loading is correct,
 * but the save slots now live in the player class.
 */
public final class Main {

    public static void main(String[] args) {
        Player player = new Player();
        player.pickUp("sword");
        player.pickUp("shield");
        player.moveTo("bridge");
        player.saveCheckpoint();
        System.out.println("At the checkpoint: " + player);

        player.drop("sword");
        player.pickUp("potion");
        player.moveTo("cave");
        player.takeDamage(100);
        System.out.println("In the cave:       " + player);

        player.loadCheckpoint();
        System.out.println("After loading:     " + player);
        System.out.println("The save slots are a field of Player, not of the game.");
    }
}
