package dev.kaldiroglu.dp.structural.adapter.hw.iteration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Two methods map; the third has nothing to map onto. That is the exercise. */
class CursorIteratorAdapterTest {

    private static Iterator<String> adapted() {
        return new CursorIteratorAdapter<>(new ArrayCursor<>(List.of("Ayse", "Bora", "Cem")));
    }

    @Test
    @DisplayName("the old operation model maps onto the new one")
    void mapsCleanly() {
        List<String> collected = new ArrayList<>();
        Iterator<String> iterator = adapted();
        while (iterator.hasNext()) {
            collected.add(iterator.next());
        }

        assertEquals(List.of("Ayse", "Bora", "Cem"), collected);
        assertFalse(iterator.hasNext());
    }

    @Test
    @DisplayName("and so the whole JDK works on a 1990s cursor")
    void worksWithForEach() {
        Iterable<String> iterable = CursorIteratorAdapterTest::adapted;

        List<String> collected = new ArrayList<>();
        for (String name : iterable) {
            collected.add(name);
        }
        assertEquals(3, collected.size());
    }

    @Test
    @DisplayName("running off the end throws what Iterator's contract requires")
    void exhaustionThrowsTheRightType() {
        Iterator<String> iterator = new CursorIteratorAdapter<>(new ArrayCursor<>(List.of()));

        // Not the adaptee's IllegalStateException — the target's contract wins.
        assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    @DisplayName("remove cannot be adapted, and says so instead of doing nothing")
    void removeIsUnsupported() {
        UnsupportedOperationException thrown =
                assertThrows(UnsupportedOperationException.class, () -> adapted().remove());

        assertTrue(thrown.getMessage().contains("no such operation"));
    }

    @Test
    @DisplayName("the adapter is a real Iterator, not something that resembles one")
    void substitutable() {
        assertTrue(Iterator.class.isAssignableFrom(CursorIteratorAdapter.class));
        assertFalse(Iterator.class.isAssignableFrom(ArrayCursor.class),
                "which the adaptee is not");
    }
}
