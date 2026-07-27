package dev.kaldiroglu.dp.structural.bridge.gof.window.solution;

import dev.kaldiroglu.dp.structural.bridge.gof.window.Canvas;
import dev.kaldiroglu.dp.structural.bridge.gof.window.Display;

/**
 * Runs the Bridge design: the platform is an object the window holds.
 * <p>
 * The pictures are identical to those the {@code problem} package draws. The designs are
 * not, and the last two sections show where they part company.
 */
public final class Main {

    private Main() {
    }

    public static void main(String[] args) {
        Display.heading("GoF Bridge (p. 152) - the platform behind an implementor");

        WindowImp x = new XWindowImp();
        WindowImp pm = new PMWindowImp();

        Display.section("one window kind, both platforms");
        System.out.println("\nnew IconWindow(.., x)         new IconWindow(.., pm)");
        Display.sideBySide(
                Window.render(new IconWindow(24, 5, "readme.txt", x)),
                Window.render(new IconWindow(24, 5, "readme.txt", pm)));

        Display.section("another window kind, the same two platforms");
        System.out.println("\nnew TransientWindow(.., x)    new TransientWindow(.., pm)");
        Display.sideBySide(
                Window.render(new TransientWindow(24, 5, "Save as", x)),
                Window.render(new TransientWindow(24, 5, "Save as", pm)));

        System.out.println("""

                  One IconWindow class and one TransientWindow class produced all four
                  pictures - and both would work on a third platform without being
                  touched.

                  3 window kinds + 2 platforms = 5 classes
                  a third platform             = 6
                  a fourth window kind         = 6""");

        Display.section("the platform can change while the program runs");
        Window window = new IconWindow(24, 5, "readme.txt", x);
        System.out.println("  window.platform()  = " + window.platform());
        window.setImp(pm);
        System.out.println("  window.setImp(pm)  = " + window.platform() + "   (the same object)");

        Display.section("what each platform was actually asked to do");
        WindowImp traced = new PMWindowImp();
        new IconWindow(24, 5, "readme.txt", traced).drawContents(new Canvas(24, 5));
        traced.journal().forEach(call -> System.out.println("  PM  " + call));

        WindowImp tracedX = new XWindowImp();
        new IconWindow(24, 5, "readme.txt", tracedX).drawContents(new Canvas(24, 5));
        System.out.println();
        tracedX.journal().forEach(call -> System.out.println("  X   " + call));

        System.out.println("""

                  The window asked for two rectangles and one string, both times.
                  Presentation Manager has no rectangle call, so its implementor built
                  each one from a polyline - seven calls against three. The window never
                  found out, and could not have acted on it if it had.""");
    }
}
