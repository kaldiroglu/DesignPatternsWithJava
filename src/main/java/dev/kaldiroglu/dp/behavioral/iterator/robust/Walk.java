package dev.kaldiroglu.dp.behavioral.iterator.robust;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

/**
 * Walks a collection, makes one change after the first element, and says what the iterator
 * did: it failed, or it went on and saw these elements.
 */
final class Walk {

    private Walk() {
    }

    static String changedAfterFirst(Iterable<String> collection, Runnable change) {
        List<String> seen = new ArrayList<>();
        try {
            for (String element : collection) {
                seen.add(element);
                if (seen.size() == 1) {
                    change.run();
                }
            }
            return "no exception, saw " + seen;
        } catch (ConcurrentModificationException e) {
            return "ConcurrentModificationException after " + seen;
        }
    }
}
