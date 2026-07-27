package dev.kaldiroglu.dp.structural.bridge.gof.window;

import dev.kaldiroglu.dp.structural.bridge.gof.window.problem.PMIconWindow;
import dev.kaldiroglu.dp.structural.bridge.gof.window.problem.XIconWindow;
import dev.kaldiroglu.dp.structural.bridge.gof.window.solution.IconWindow;
import dev.kaldiroglu.dp.structural.bridge.gof.window.solution.PMWindowImp;
import dev.kaldiroglu.dp.structural.bridge.gof.window.solution.Window;
import dev.kaldiroglu.dp.structural.bridge.gof.window.solution.XWindowImp;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * The two designs are worth comparing only if they do the same thing. These tests prove
 * they draw identical windows, so every remaining difference is a difference of design.
 */
class DesignComparisonTest {

    @Test
    @DisplayName("both designs draw the same icon window on X")
    void sameOutputOnX() {
        assertEquals(
                dev.kaldiroglu.dp.structural.bridge.gof.window.problem.Window.render(new XIconWindow(14, 5, "a.txt")),
                Window.render(new IconWindow(14, 5, "a.txt", new XWindowImp())));
    }

    @Test
    @DisplayName("both designs draw the same icon window on Presentation Manager")
    void sameOutputOnPm() {
        assertEquals(
                dev.kaldiroglu.dp.structural.bridge.gof.window.problem.Window.render(new PMIconWindow(14, 5, "a.txt")),
                Window.render(new IconWindow(14, 5, "a.txt", new PMWindowImp())));
    }

    @Test
    @DisplayName("kinds x platforms, against kinds + platforms")
    void theCostOfGrowth() {
        int kinds = 3, platforms = 2;

        // problem package: one leaf class per pair, and each of them repeats its
        // platform's drawing code.
        assertEquals(6, kinds * platforms);

        // solution package: Window + 2 refinements + WindowImp + 2 implementations.
        assertEquals(5, kinds + platforms);

        // The gap widens with every addition, and it widens fastest where it hurts most:
        // a fourth platform costs 4 classes on the left and 1 on the right.
        assertEquals(12, kinds * (platforms + 2));
        assertEquals(7, kinds + (platforms + 2));
    }
}
