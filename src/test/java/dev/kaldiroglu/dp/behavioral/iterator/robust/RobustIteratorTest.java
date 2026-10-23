package dev.kaldiroglu.dp.behavioral.iterator.robust;

import dev.kaldiroglu.dp.behavioral.command.lender.Printed;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * GoF implementation issue 3 (how robust is the iterator?). Every cell of the Java column on
 * the robust-iterator slide is asserted here, against the standard library itself.
 */
class RobustIteratorTest {

    @Test
    @DisplayName("ArrayList fails: adding during the walk throws ConcurrentModificationException")
    void arrayListFailsOnAdd() {
        List<String> list = new ArrayList<>(List.of("A", "B", "C"));
        assertEquals("ConcurrentModificationException after [A]",
                Walk.changedAfterFirst(list, () -> list.add("D")));
    }

    @Test
    @DisplayName("HashMap fails: removing during the walk throws ConcurrentModificationException")
    void hashMapFailsOnRemove() {
        Map<String, Integer> map = new HashMap<>(Map.of("A", 1, "B", 2, "C", 3));
        assertTrue(Walk.changedAfterFirst(map.keySet(), () -> map.remove("C"))
                .startsWith("ConcurrentModificationException"));
    }

    @Test
    @DisplayName("CopyOnWriteArrayList ignores the change: it walks a snapshot")
    void copyOnWriteIgnoresTheChange() {
        List<String> list = new CopyOnWriteArrayList<>(List.of("A", "B", "C"));
        assertEquals("no exception, saw [A, B, C]",
                Walk.changedAfterFirst(list, () -> list.add("D")));
        assertEquals(List.of("A", "B", "C", "D"), list);
    }

    @Test
    @DisplayName("ConcurrentHashMap never fails, and the walk still reads the original elements")
    void concurrentHashMapNeverFails() {
        Map<String, Integer> map = new ConcurrentHashMap<>(Map.of("A", 1, "B", 2, "C", 3));
        String outcome = Walk.changedAfterFirst(map.keySet(), () -> map.put("D", 4));
        assertTrue(outcome.startsWith("no exception"), outcome);
        assertTrue(outcome.contains("A") && outcome.contains("B") && outcome.contains("C"));
    }

    @Test
    @DisplayName("replacing an element is not a change to ArrayList: set does not fail, and the walk sees it")
    void arrayListSetDoesNotFail() {
        List<String> list = new ArrayList<>(List.of("A", "B", "C"));
        assertEquals("no exception, saw [A, B, Z]",
                Walk.changedAfterFirst(list, () -> list.set(2, "Z")));
    }

    @Test
    @DisplayName("Iterator.remove changes the list during the walk without failing")
    void iteratorRemoveIsSafe() {
        List<String> list = new ArrayList<>(List.of("A", "B", "C"));
        for (Iterator<String> walk = list.iterator(); walk.hasNext(); ) {
            if (walk.next().equals("B")) {
                walk.remove();
            }
        }
        assertEquals(List.of("A", "C"), list);
    }

    @Test
    @DisplayName("walking a copy lets the original change without failing")
    void walkingACopyIsSafe() {
        List<String> list = new ArrayList<>(List.of("A", "B", "C"));
        assertEquals("no exception, saw [A, B, C]",
                Walk.changedAfterFirst(new ArrayList<>(list), () -> list.add("D")));
    }

    @Test
    @DisplayName("Main prints the outcomes the slide quotes")
    void mainPrintsTheOutcomes() {
        assertEquals(List.of(
                "Fails:",
                "  ArrayList, add D: ConcurrentModificationException after [A]",
                "  HashMap, remove C: ConcurrentModificationException after [A]",
                "Ignores the change:",
                "  CopyOnWriteArrayList, add D: no exception, saw [A, B, C]",
                "May see the change, and never fails:",
                "  ConcurrentHashMap, put D: no exception",
                "Replacing an element:",
                "  ArrayList, set C to Z: no exception, saw [A, B, Z]"),
                Printed.by(() -> Main.main(new String[0])));
    }
}
