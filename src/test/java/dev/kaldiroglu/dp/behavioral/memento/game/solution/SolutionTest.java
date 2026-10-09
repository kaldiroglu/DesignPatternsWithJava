package dev.kaldiroglu.dp.behavioral.memento.game.solution;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

import static dev.kaldiroglu.dp.behavioral.memento.Methods.publicSettersOf;
import static dev.kaldiroglu.dp.behavioral.memento.Printed.by;
import static org.junit.jupiter.api.Assertions.*;

/** The checkpoint as a memento. Every figure on the Part 3 slides is asserted here. */
class SolutionTest {

    @Test
    @DisplayName("loading gives back exactly what the player had: health 100, the bridge, sword and shield")
    void thePromiseIsKept() {
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

        assertEquals("health 100, at bridge, carrying [sword, shield]", player.toString());
        assertEquals(1, game.checkpointCount());
    }

    @Test
    @DisplayName("a checkpoint can be loaded twice, because nothing the player does changes it")
    void aCheckpointDoesNotChange() {
        Player player = new Player();
        player.pickUp("sword");
        Player.Checkpoint checkpoint = player.save();

        player.pickUp("shield");
        player.load(checkpoint);
        player.pickUp("potion");
        player.load(checkpoint);

        assertEquals("health 100, at start, carrying [sword]", player.toString());
    }

    @Test
    @DisplayName("a class outside Player cannot read Checkpoint.health: its fields and constructor are private")
    void theCheckpointIsClosed() {
        Class<Player.Checkpoint> type = Player.Checkpoint.class;
        Field[] fields = type.getDeclaredFields();
        assertEquals(List.of("health", "inventory", "position"),
                Arrays.stream(fields).map(Field::getName).sorted().toList());
        for (Field field : fields) {
            assertTrue(Modifier.isPrivate(field.getModifiers()), field.getName());
        }
        for (Constructor<?> constructor : type.getDeclaredConstructors()) {
            assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        }
        assertEquals(0, Arrays.stream(type.getDeclaredMethods())
                .filter(m -> !m.isSynthetic())
                .count(), "no getters, no methods at all");
    }

    @Test
    @DisplayName("public setters on Player: yes in stage one, no in stage three, no with the memento")
    void publicSetters() {
        assertFalse(publicSettersOf(
                dev.kaldiroglu.dp.behavioral.memento.game.problem.setters.Player.class).isEmpty());
        assertTrue(publicSettersOf(
                dev.kaldiroglu.dp.behavioral.memento.game.problem.copy.Player.class).isEmpty());
        assertTrue(publicSettersOf(Player.class).isEmpty());
    }

    @Test
    @DisplayName("Main runs the same play three ways: only the memento keeps the promise and the rules")
    void mainOutput() {
        assertEquals(List.of(
                "Stage one, after loading:   health 100, at bridge, carrying [sword, shield]",
                "  and anyone may now write: health 999, at bridge, carrying [sword, shield]",
                "Stage three, after loading: health 100, at bridge, carrying [shield, potion]",
                "Memento, after loading:     health 100, at bridge, carrying [sword, shield]"),
                by(() -> Main.main(new String[0])));
    }
}
