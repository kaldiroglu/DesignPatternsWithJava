package dev.kaldiroglu.dp.behavioral.mediator.hw.airtraffic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/** Homework 2: the control tower gives the runway to one aircraft at a time. */
class ControlTowerTest {

    @Test
    @DisplayName("one aircraft on the runway at a time; the next one waiting gets it when it is clear")
    void oneAtATime() {
        ControlTower tower = new ControlTower();
        Aircraft tk1 = new Aircraft("TK1", "land", tower);
        Aircraft pc2 = new Aircraft("PC2", "take off", tower);
        Aircraft lh3 = new Aircraft("LH3", "land", tower);

        tk1.request();
        pc2.request();
        lh3.request();
        tk1.clear();
        pc2.clear();

        assertEquals(List.of(
                "TK1 may land",
                "PC2 waits",
                "LH3 waits",
                "TK1 clears the runway",
                "PC2 may take off",
                "PC2 clears the runway",
                "LH3 may land"), tower.log());
        assertThrows(IllegalStateException.class, tk1::clear, "TK1 is not on the runway");
    }

    @Test
    @DisplayName("every public method of the tower is synchronized")
    void everyMethodIsSynchronized() {
        List<Method> methods = Arrays.stream(ControlTower.class.getDeclaredMethods())
                .filter(m -> Modifier.isPublic(m.getModifiers()))
                .toList();
        assertEquals(3, methods.size());
        methods.forEach(m -> assertTrue(Modifier.isSynchronized(m.getModifiers()), m.getName()));
    }
}
