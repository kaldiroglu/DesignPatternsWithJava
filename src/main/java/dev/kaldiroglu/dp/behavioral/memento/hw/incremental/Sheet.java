package dev.kaldiroglu.dp.behavioral.memento.hw.incremental;

import java.util.HashMap;
import java.util.Map;

/**
 * Homework 3: the <b>Originator</b>. A sheet with many cells.
 * <p>
 * Saving all ten thousand cells before every edit would be expensive. So a memento holds
 * only the old values of the cells one edit changed — an incremental memento, GoF
 * implementation issue 2 (storing incremental changes). This works because the mementos are
 * restored in the reverse order they were made.
 */
public final class Sheet {

    /** The <b>Memento</b>: the old values of the changed cells only. */
    public static final class Change {
        private final Map<String, Integer> oldValues;

        private Change(Map<String, Integer> oldValues) {
            this.oldValues = Map.copyOf(oldValues);
        }

        public int size() {
            return oldValues.size();
        }
    }

    private final Map<String, Integer> cells = new HashMap<>();

    public Sheet(int rows, int columns) {
        for (int r = 1; r <= rows; r++) {
            for (int c = 1; c <= columns; c++) {
                cells.put("R" + r + "C" + c, 0);
            }
        }
    }

    /** Sets several cells in one edit and returns what is needed to undo exactly that edit. */
    public Change set(Map<String, Integer> newValues) {
        Map<String, Integer> old = new HashMap<>();
        newValues.forEach((cell, value) -> old.put(cell, cells.put(cell, value)));
        return new Change(old);
    }

    public void undo(Change change) {
        cells.putAll(change.oldValues);
    }

    public int get(String cell) {
        return cells.get(cell);
    }

    public int cellCount() {
        return cells.size();
    }
}
