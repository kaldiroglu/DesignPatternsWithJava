package dev.kaldiroglu.dp.behavioral.memento.hw.incremental;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/** Homework 3: an incremental memento for a large sheet. */
class SheetTest {

    @Test
    @DisplayName("an edit of two cells on a 10,000-cell sheet makes a memento with 2 entries")
    void twoEntries() {
        Sheet sheet = new Sheet(100, 100);
        assertEquals(10_000, sheet.cellCount());

        Sheet.Change change = sheet.set(Map.of("R1C1", 5, "R2C2", 7));

        assertEquals(2, change.size());
    }

    @Test
    @DisplayName("undoing the changes in reverse order gives the old values back")
    void undoInReverseOrder() {
        Sheet sheet = new Sheet(100, 100);
        Sheet.Change first = sheet.set(Map.of("R1C1", 5, "R2C2", 7));
        Sheet.Change second = sheet.set(Map.of("R1C1", 9));
        assertEquals(9, sheet.get("R1C1"));

        sheet.undo(second);
        assertEquals(5, sheet.get("R1C1"));
        sheet.undo(first);
        assertEquals(0, sheet.get("R1C1"));
        assertEquals(0, sheet.get("R2C2"));
    }
}
