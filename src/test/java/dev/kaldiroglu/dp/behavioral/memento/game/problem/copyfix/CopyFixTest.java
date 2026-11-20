package dev.kaldiroglu.dp.behavioral.memento.game.problem.copyfix;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static dev.kaldiroglu.dp.behavioral.memento.Printed.by;
import static dev.kaldiroglu.dp.behavioral.memento.Printed.codeOf;
import static org.junit.jupiter.api.Assertions.*;

/** Stage three with the quick fix: the list is copied when saving and when loading. */
class CopyFixTest {

    @Test
    @DisplayName("Main: loading gives back [sword, shield], and a key picked up after the load stays out of the save")
    void mainOutput() {
        assertEquals(List.of(
                "After loading:          health 100, at bridge, carrying [sword, shield]",
                "After picking up a key: health 100, at bridge, carrying [sword, shield, key]",
                "The save still holds:   Saved game: health 100, at bridge, carrying [sword, shield]"),
                by(() -> Main.main(new String[0])));
    }

    @Test
    @DisplayName("the fix is two lines: the inventory line of the copy constructor and of restoreFrom")
    void theFixIsTwoLines() {
        List<String> before = codeOf("memento/game/problem/copy/Player.java").lines()
                .filter(line -> !line.startsWith("package")).toList();
        List<String> after = codeOf("memento/game/problem/copyfix/Player.java").lines()
                .filter(line -> !line.startsWith("package")).toList();

        assertEquals(before.size(), after.size());
        List<String> changed = new ArrayList<>();
        for (int i = 0; i < before.size(); i++) {
            if (!before.get(i).equals(after.get(i))) {
                changed.add(before.get(i).strip() + " -> " + after.get(i).strip());
            }
        }
        assertEquals(List.of(
                "this.inventory = other.inventory; -> this.inventory = new ArrayList<>(other.inventory);",
                "this.inventory = copy.inventory; -> this.inventory = new ArrayList<>(copy.inventory);"),
                changed);
    }

    @Test
    @DisplayName("the game can still read and change the save, because it is still a whole Player")
    void theSaveIsStillAPlayer() throws Exception {
        assertEquals(Player.class, Game.class.getDeclaredField("checkpoint").getType());
    }
}
