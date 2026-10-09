package dev.kaldiroglu.dp.behavioral.memento.game.problem.copy;

/**
 * The play from the deck, with the game keeping a copy of the player. The copy shares the
 * inventory list, so after loading the player carries [shield, potion], not [sword, shield].
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
        System.out.println("Health and place are back, but the inventory is the one from the cave.");
    }
}
