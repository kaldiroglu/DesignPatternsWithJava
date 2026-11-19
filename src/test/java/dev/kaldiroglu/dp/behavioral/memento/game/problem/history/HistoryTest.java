package dev.kaldiroglu.dp.behavioral.memento.game.problem.history;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

import static dev.kaldiroglu.dp.behavioral.memento.Methods.publicSettersOf;
import static dev.kaldiroglu.dp.behavioral.memento.Printed.by;
import static org.junit.jupiter.api.Assertions.*;

/** Stage two: the player keeps its own checkpoints. */
class HistoryTest {

    @Test
    @DisplayName("loading is correct, and nothing outside sees the fields")
    void loadingIsCorrect() {
        Player player = new Player();
        player.pickUp("sword");
        player.pickUp("shield");
        player.moveTo("bridge");
        player.saveCheckpoint();
        player.drop("sword");
        player.pickUp("potion");
        player.moveTo("cave");
        player.takeDamage(100);
        player.loadCheckpoint();

        assertEquals("health 100, at bridge, carrying [sword, shield]", player.toString());
        assertEquals(0, publicSettersOf(Player.class).size());
        assertTrue(Arrays.stream(Player.class.getDeclaredMethods())
                .filter(m -> Modifier.isPublic(m.getModifiers()))
                .map(Method::getName)
                .noneMatch(name -> name.startsWith("get")));
    }

    @Test
    @DisplayName("the player class now also keeps the save slots")
    void thePlayerKeepsTheSlots() {
        assertTrue(Arrays.stream(Player.class.getDeclaredFields())
                .map(Field::getType)
                .anyMatch(Deque.class::equals));
    }

    @Test
    @DisplayName("the game can only say save or load, and loading always gives back the last save")
    void theGameGetsOnlyTheLastSave() {
        Player player = new Player();
        player.moveTo("bridge");
        player.saveCheckpoint();
        player.moveTo("cave");
        player.saveCheckpoint();
        player.moveTo("tower");
        player.loadCheckpoint();
        assertEquals("health 100, at cave, carrying []", player.toString());
        player.loadCheckpoint();
        assertEquals("health 100, at cave, carrying []", player.toString());

        List<Method> saveMethods = Arrays.stream(Player.class.getDeclaredMethods())
                .filter(m -> Modifier.isPublic(m.getModifiers()))
                .filter(m -> m.getName().contains("Checkpoint"))
                .sorted(java.util.Comparator.comparing(Method::getName))
                .toList();
        assertEquals(List.of("loadCheckpoint", "saveCheckpoint"),
                saveMethods.stream().map(Method::getName).toList());
        // No argument to pick a save, and no save handed back: the game never sees one.
        assertTrue(saveMethods.stream()
                .allMatch(m -> m.getParameterCount() == 0 && m.getReturnType() == void.class));
    }

    @Test
    @DisplayName("Main prints the play: loading is correct, and the player keeps the saves")
    void mainOutput() {
        assertEquals(List.of(
                "At the checkpoint: health 100, at bridge, carrying [sword, shield]",
                "In the cave:       health 0, at cave, carrying [shield, potion]",
                "After loading:     health 100, at bridge, carrying [sword, shield]",
                "The save slots are a field of Player, not of the game."),
                by(() -> Main.main(new String[0])));
    }
}
