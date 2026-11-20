package dev.kaldiroglu.dp.behavioral.memento.game.problem.copyfix;

/**
 * The play from the deck with the quick fix. Loading gives back [sword, shield]. After the
 * load the player picks up a key, and the save does not change.
 */
public final class Main {

    public static void main(String[] args) {
        Player player = new Player();
        Game game = new Game();
        player.pickUp("sword");
        player.pickUp("shield");
        player.moveTo("bridge");
        game.checkpoint(player);

        player.drop("sword");
        player.pickUp("potion");
        player.moveTo("cave");
        player.takeDamage(100);

        game.load(player);
        System.out.println("After loading:          " + player);

        player.pickUp("key");
        System.out.println("After picking up a key: " + player);
        System.out.println("The save still holds:   " + game.loadScreen());
    }
}
