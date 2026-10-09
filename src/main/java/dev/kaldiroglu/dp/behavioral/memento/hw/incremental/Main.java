package dev.kaldiroglu.dp.behavioral.memento.hw.incremental;

import java.util.Map;

/**
 * Edits two cells of a 100 by 100 sheet and undoes the edit. The memento holds the old
 * values of the two changed cells only.
 */
public final class Main {

    public static void main(String[] args) {
        Sheet sheet = new Sheet(100, 100);
        Sheet.Change change = sheet.set(Map.of("R1C1", 5, "R1C2", 7));

        System.out.println("Cells in the sheet: " + sheet.cellCount());
        System.out.println("Cells in the memento: " + change.size());
        System.out.println("After the edit: R1C1 = " + sheet.get("R1C1") + ", R1C2 = " + sheet.get("R1C2"));

        sheet.undo(change);
        System.out.println("After undo:     R1C1 = " + sheet.get("R1C1") + ", R1C2 = " + sheet.get("R1C2"));
    }
}
