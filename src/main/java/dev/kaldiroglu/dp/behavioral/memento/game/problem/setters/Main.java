package dev.kaldiroglu.dp.behavioral.memento.game.problem.setters;

/**
 * The play from the deck: save at the bridge, lose the sword in the cave, load. Loading is
 * correct, but the public setters let any code give the player 999 health.
 */
public final class Main {

    public static void main(String[] args) {
        Player player = new Player();
        Game game = new Game();
        player.pickUp("sword");
        player.pickUp("shield");
        player.moveTo("bridge");
        game.checkpoint(player);
        System.out.println("At the checkpoint: " + player);

        player.drop("sword");
        player.pickUp("potion");
        player.moveTo("cave");
        player.takeDamage(100);
        System.out.println("In the cave:       " + player);

        game.load(player);
        System.out.println("After loading:     " + player);

        player.setHealth(999);
        System.out.println("Any code may call setHealth(999): " + player);
    }
}
