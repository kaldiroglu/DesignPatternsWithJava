package dev.kaldiroglu.dp.behavioral.memento.game.problem.setters;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static dev.kaldiroglu.dp.behavioral.memento.Methods.publicSettersOf;
import static org.junit.jupiter.api.Assertions.*;

/** Stage one: the game reads every field out and writes every field back. */
class SettersTest {

    private static Player playedAndLoaded() {
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
        return player;
    }

    @Test
    @DisplayName("it works: loading gives back health 100, the bridge, sword and shield")
    void loadingIsCorrect() {
        Player player = playedAndLoaded();
        assertEquals(100, player.getHealth());
        assertEquals("bridge", player.getPosition());
        assertEquals(List.of("sword", "shield"), player.getInventory());
    }

    @Test
    @DisplayName("any code may now call setHealth(999), and the player cannot refuse")
    void anyoneMaySetHealth() {
        Player player = playedAndLoaded();
        player.setHealth(999);
        assertEquals(999, player.getHealth());
        assertEquals(List.of("setHealth", "setInventory", "setPosition"), publicSettersOf(Player.class));
    }
}
