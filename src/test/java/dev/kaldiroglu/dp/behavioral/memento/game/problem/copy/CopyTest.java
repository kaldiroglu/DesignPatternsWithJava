package dev.kaldiroglu.dp.behavioral.memento.game.problem.copy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static dev.kaldiroglu.dp.behavioral.memento.Methods.publicSettersOf;
import static org.junit.jupiter.api.Assertions.*;

/** Stage three: the game keeps a copy of the player, and the copy shares the inventory list. */
class CopyTest {

    @Test
    @DisplayName("after loading the player has health 100 at the bridge, but carries [shield, potion], not [sword, shield]")
    void theWrongInventory() {
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

        assertEquals("health 100, at bridge, carrying [shield, potion]", player.toString());
    }

    @Test
    @DisplayName("a copy shares the list: what the player picks up, the copy picks up too")
    void theListIsShared() {
        Player player = new Player();
        Player copy = new Player(player);
        player.pickUp("sword");
        assertEquals("health 100, at start, carrying [sword]", copy.toString());
    }

    @Test
    @DisplayName("stage three keeps the rules: the player has no public setters")
    void noPublicSetters() {
        assertEquals(0, publicSettersOf(Player.class).size());
    }
}
