package dev.kaldiroglu.dp.structural.bridge.gof.window;

import dev.kaldiroglu.dp.structural.bridge.gof.window.problem.PMIconWindow;
import dev.kaldiroglu.dp.structural.bridge.gof.window.problem.PMWindow;
import dev.kaldiroglu.dp.structural.bridge.gof.window.problem.Window;
import dev.kaldiroglu.dp.structural.bridge.gof.window.problem.XIconWindow;
import dev.kaldiroglu.dp.structural.bridge.gof.window.problem.XTransientWindow;
import dev.kaldiroglu.dp.structural.bridge.gof.window.problem.XWindow;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The design GoF start from works. These tests prove it works, and then measure what it
 * costs - which is the only honest way to argue for a pattern.
 */
class WindowProblemTest {

    @Test
    @DisplayName("each platform draws its own way")
    void platformsDrawDifferently() {
        assertEquals("""
                +------+
                |      |
                +------+""", Window.render(new XWindow(8, 3)));

        assertEquals("""
                #======#
                !      !
                #======#""", Window.render(new PMWindow(8, 3)));
    }

    @Test
    @DisplayName("a window kind must be written once per platform")
    void oneClassPerCombination() {
        String onX = Window.render(new XIconWindow(14, 5, "a.txt"));
        String onPm = Window.render(new PMIconWindow(14, 5, "a.txt"));

        // Same icon layout, drawn by two classes that share no code path.
        assertTrue(onX.contains("a.txt"));
        assertTrue(onPm.contains("a.txt"));
        assertNotEquals(onX, onPm);
    }

    @Test
    @DisplayName("the platform is the class, so it cannot change once the object exists")
    void platformIsFixedAtConstruction() {
        Window window = new XIconWindow(14, 5, "a.txt");

        // There is no operation that can move this window to Presentation Manager. The
        // only way is to build a different object of a different class - and any state
        // the original held is lost with it.
        assertEquals("X", window.platform());
        assertEquals("PM", new PMIconWindow(14, 5, "a.txt").platform());
    }

    @Test
    @DisplayName("the X drawing code exists three times over")
    void platformCodeIsDuplicated() {
        // XWindow, XIconWindow and XTransientWindow each carry their own copy of the
        // same two methods. Change how X draws a rectangle and you must find all three;
        // the compiler will not tell you which one you missed.
        assertEquals("X", new XWindow(8, 3).platform());
        assertEquals("X", new XIconWindow(8, 3, "a").platform());
        assertEquals("X", new XTransientWindow(8, 3, "a").platform());
    }

    @Test
    @DisplayName("kinds multiply platforms")
    void theArithmetic() {
        int windowKinds = 3;     // plain, icon, transient
        int platforms = 2;       // X, PM
        assertEquals(6, windowKinds * platforms);

        // A third platform is not one class. It is one class per window kind.
        assertEquals(9, windowKinds * (platforms + 1));
        // A fourth window kind is not one class either.
        assertEquals(8, (windowKinds + 1) * platforms);
    }
}
