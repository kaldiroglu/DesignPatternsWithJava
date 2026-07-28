package dev.kaldiroglu.dp.structural.adapter.hw.iteration;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/** Homework 2 — an old cursor behind {@link Iterator}, and the method that does not fit. */
public class Main {

    public static void main(String[] args) {
        Cursor<String> cursor = new ArrayCursor<>(List.of("Ayse", "Bora", "Cem"));
        Iterator<String> iterator = new CursorIteratorAdapter<>(cursor);

        List<String> collected = new ArrayList<>();
        while (iterator.hasNext()) {
            collected.add(iterator.next());
        }
        System.out.println("read through Iterator: " + collected);

        // And now the whole of the JDK works on a 1990s cursor.
        Iterable<String> iterable = () -> new CursorIteratorAdapter<>(
                new ArrayCursor<>(List.of("Ayse", "Bora", "Cem")));
        System.out.print("for-each over the adaptee: ");
        for (String name : iterable) {
            System.out.print(name + " ");
        }
        System.out.println();

        System.out.println("\n-- the operation that cannot be adapted --");
        try {
            new CursorIteratorAdapter<>(new ArrayCursor<>(List.of("x"))).remove();
        } catch (UnsupportedOperationException e) {
            System.out.println("  " + e.getMessage());
        }

        System.out.println("""

                Two of the three methods mapped. The third had nothing to map onto,
                and the honest answer is to throw rather than to do nothing quietly.

                GoF, p. 141: how useful an adapter is depends on how much of the
                target interface the adaptee can support.""");
    }
}
