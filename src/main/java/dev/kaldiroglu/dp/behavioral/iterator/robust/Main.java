package dev.kaldiroglu.dp.behavioral.iterator.robust;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * GoF implementation issue 3 (how robust is the iterator?): what the standard library's
 * iterators do when the collection changes during the walk. Each walk reads A, B and C, and
 * the collection changes after A.
 */
public final class Main {

    public static void main(String[] args) {
        System.out.println("Fails:");
        List<String> list = new ArrayList<>(List.of("A", "B", "C"));
        System.out.println("  ArrayList, add D: "
                + Walk.changedAfterFirst(list, () -> list.add("D")));
        Map<String, Integer> map = new HashMap<>(abc());
        System.out.println("  HashMap, remove C: "
                + Walk.changedAfterFirst(map.keySet(), () -> map.remove("C")));

        System.out.println("Ignores the change:");
        List<String> copyOnWrite = new CopyOnWriteArrayList<>(List.of("A", "B", "C"));
        System.out.println("  CopyOnWriteArrayList, add D: "
                + Walk.changedAfterFirst(copyOnWrite, () -> copyOnWrite.add("D")));

        System.out.println("May see the change, and never fails:");
        Map<String, Integer> concurrent = new ConcurrentHashMap<>(abc());
        String outcome = Walk.changedAfterFirst(concurrent.keySet(),
                () -> concurrent.put("D", 4));
        System.out.println("  ConcurrentHashMap, put D: "
                + (outcome.startsWith("no exception") ? "no exception" : outcome));

        System.out.println("Replacing an element:");
        List<String> replaced = new ArrayList<>(List.of("A", "B", "C"));
        System.out.println("  ArrayList, set C to Z: "
                + Walk.changedAfterFirst(replaced, () -> replaced.set(2, "Z")));
    }

    private static Map<String, Integer> abc() {
        Map<String, Integer> map = new LinkedHashMap<>();
        map.put("A", 1);
        map.put("B", 2);
        map.put("C", 3);
        return map;
    }
}
