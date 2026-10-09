package dev.kaldiroglu.dp.behavioral.memento.game.solution;

/**
 * The same play through stage one, stage three and the memento.
 * <p>
 * The player picks up a sword and a shield, reaches the bridge and saves. Then it drops the
 * sword, picks up a potion, walks into the cave and takes 100 damage. The game loads the
 * checkpoint. The promise: loading gives back exactly what the player had at the
 * checkpoint — health 100, at the bridge, carrying the sword and the shield.
 */
public final class Main {

    public static void main(String[] args) {
        var p1 = new dev.kaldiroglu.dp.behavioral.memento.game.problem.setters.Player();
        var g1 = new dev.kaldiroglu.dp.behavioral.memento.game.problem.setters.Game();
        p1.pickUp("sword");
        p1.pickUp("shield");
        p1.moveTo("bridge");
        g1.checkpoint(p1);
        p1.drop("sword");
        p1.pickUp("potion");
        p1.moveTo("cave");
        p1.takeDamage(100);
        g1.load(p1);
        System.out.println("Stage one, after loading:   " + p1);
        p1.setHealth(999);
        System.out.println("  and anyone may now write: " + p1);

        var p3 = new dev.kaldiroglu.dp.behavioral.memento.game.problem.copy.Player();
        var g3 = new dev.kaldiroglu.dp.behavioral.memento.game.problem.copy.Game();
        p3.pickUp("sword");
        p3.pickUp("shield");
        p3.moveTo("bridge");
        g3.checkpoint(p3);
        p3.drop("sword");
        p3.pickUp("potion");
        p3.moveTo("cave");
        p3.takeDamage(100);
        g3.load(p3);
        System.out.println("Stage three, after loading: " + p3);

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
        game.loadLatest(player);
        System.out.println("Memento, after loading:     " + player);
    }
}
