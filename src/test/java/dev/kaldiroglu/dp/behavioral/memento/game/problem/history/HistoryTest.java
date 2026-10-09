package dev.kaldiroglu.dp.behavioral.memento.game.problem.history;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Deque;

import static dev.kaldiroglu.dp.behavioral.memento.Methods.publicSettersOf;
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
}
