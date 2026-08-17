package dev.kaldiroglu.dp.structural.bridge.gof.window;

import dev.kaldiroglu.dp.structural.bridge.gof.Canvas;
import dev.kaldiroglu.dp.structural.bridge.gof.solution.IconWindow;
import dev.kaldiroglu.dp.structural.bridge.gof.solution.PMWindowImp;
import dev.kaldiroglu.dp.structural.bridge.gof.solution.TransientWindow;
import dev.kaldiroglu.dp.structural.bridge.gof.solution.Window;
import dev.kaldiroglu.dp.structural.bridge.gof.solution.WindowImp;
import dev.kaldiroglu.dp.structural.bridge.gof.solution.XWindowImp;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WindowSolutionTest {

    private final WindowImp x = new XWindowImp();
    private final WindowImp pm = new PMWindowImp();

    @Test
    @DisplayName("one window kind, every platform")
    void oneKindManyPlatforms() {
        assertEquals("""
                +------+
                |      |
                +------+""", Window.render(new Window(8, 3, x)));

        assertEquals("""
                #======#
                !      !
                #======#""", Window.render(new Window(8, 3, pm)));
    }

    @Test
    @DisplayName("one platform, every window kind")
    void onePlatformManyKinds() {
        String icon = Window.render(new IconWindow(14, 5, "a.txt", x));
        String transient_ = Window.render(new TransientWindow(14, 5, "Save", x));

        assertTrue(icon.contains("a.txt"));
        assertTrue(transient_.contains("Save"));
        assertNotEquals(icon, transient_);
    }

    @Test
    @DisplayName("the implementation can be swapped on a window that already exists")
    void implementationIsChosenAtRunTime() {
        Window window = new IconWindow(14, 5, "a.txt", x);
        String onX = Window.render(window);
        assertEquals("X", window.platform());

        window.setImp(pm);                       // the same object, a different platform

        assertEquals("PM", window.platform());
        assertNotEquals(onX, Window.render(window));
    }

    @Test
    @DisplayName("the abstraction speaks only in primitives, and never learns how they are met")
    void abstractionUsesPrimitivesOnly() {
        WindowImp traced = new PMWindowImp();
        new IconWindow(14, 5, "a.txt", traced).drawContents(new Canvas(14, 5));

        // The window asked twice for a rectangle and once for text. Presentation Manager
        // has no rectangle primitive, so it built each one from a polyline - a detail the
        // window neither knows nor could act on.
        assertEquals(7, traced.journal().size());
        assertTrue(traced.journal().get(0).startsWith("GpiBeginPath"));
        assertTrue(traced.journal().get(1).startsWith("GpiPolyLine"));
        assertTrue(traced.journal().getLast().startsWith("GpiCharStringAt"));

        WindowImp onX = new XWindowImp();
        new IconWindow(14, 5, "a.txt", onX).drawContents(new Canvas(14, 5));
        assertEquals(3, onX.journal().size());   // X has a rectangle call, so it uses it
    }

    @Test
    @DisplayName("a new platform costs one class and works with every window kind at once")
    void addingAPlatformCostsOneClass() {
        // A hypothetical third platform, written here in six lines, needs no change
        // anywhere else - and every existing window kind can use it immediately.
        WindowImp web = new WindowImp() {
            private final java.util.List<String> journal = new java.util.ArrayList<>();

            public String platform() {
                return "Web";
            }

            public void deviceRect(Canvas c, int px, int py, int w, int h) {
                journal.add("canvas.strokeRect");
                c.rectangle(px, py, w, h, 'o', '.', ':');
            }

            public void deviceText(Canvas c, int px, int py, String t) {
                journal.add("canvas.fillText");
                c.text(px, py, t);
            }

            public void deviceRaise() {
                journal.add("z-index++");
            }

            public void deviceLower() {
                journal.add("z-index--");
            }

            public java.util.List<String> journal() {
                return java.util.List.copyOf(journal);
            }
        };

        assertTrue(Window.render(new IconWindow(14, 5, "a.txt", web)).contains("o...."));
        assertTrue(Window.render(new TransientWindow(14, 5, "Save", web)).contains("Save"));
    }

    @Test
    @DisplayName("a window needs an implementation")
    void nullImpIsRejected() {
        assertThrows(NullPointerException.class, () -> new Window(8, 3, null));
    }
}
