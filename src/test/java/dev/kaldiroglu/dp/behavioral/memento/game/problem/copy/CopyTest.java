package dev.kaldiroglu.dp.behavioral.memento.game.problem.copy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

import static dev.kaldiroglu.dp.behavioral.memento.Methods.publicSettersOf;
import static dev.kaldiroglu.dp.behavioral.memento.Printed.by;
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

    @Test
    @DisplayName("the game keeps a whole Player, so every public method of Player works on the save")
    void theSaveIsAWholePlayer() throws NoSuchFieldException {
        Field checkpoint = Game.class.getDeclaredField("checkpoint");
        assertEquals(Player.class, checkpoint.getType());
        List<String> methods = Arrays.stream(Player.class.getDeclaredMethods())
                .filter(m -> Modifier.isPublic(m.getModifiers()))
                .map(Method::getName)
                .sorted()
                .toList();
        assertEquals(List.of("drop", "moveTo", "pickUp", "restoreFrom", "takeDamage", "toString"), methods);
    }

    @Test
    @DisplayName("the load screen reads the save: health, position and inventory")
    void theGameReadsTheSave() {
        assertEquals(List.of("Saved game: health 100, at bridge, carrying [sword, shield]"),
                by(() -> ReadTheSaveMain.main(new String[0])));
    }

    @Test
    @DisplayName("Main prints the play: after loading the player carries [shield, potion]")
    void mainOutput() {
        assertEquals(List.of(
                "At the checkpoint: health 100, at bridge, carrying [sword, shield]",
                "In the cave:       health 0, at cave, carrying [shield, potion]",
                "After loading:     health 100, at bridge, carrying [shield, potion]",
                "Health and place are back, but the inventory is the one from the cave."),
                by(() -> Main.main(new String[0])));
    }
}
