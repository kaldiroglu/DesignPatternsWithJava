package dev.kaldiroglu.dp.structural.bridge.gof.window.problem;

import dev.kaldiroglu.dp.structural.bridge.gof.window.Display;

/**
 * Runs the design GoF start from: the platform is a superclass.
 * <p>
 * It works. Every window drawn here is correct. What the output shows is what it cost to
 * get there — and what the next platform will cost.
 */
public final class Main {

    private Main() {
    }

    public static void main(String[] args) {
        Display.heading("GoF Motivation (p. 151) - the platform as a superclass");

        Display.section("the same icon window, once per platform");
        System.out.println("\nnew XIconWindow(...)          new PMIconWindow(...)");
        Display.sideBySide(
                Window.render(new XIconWindow(24, 5, "readme.txt")),
                Window.render(new PMIconWindow(24, 5, "readme.txt")));

        Display.section("and the same again for a dialog");
        System.out.println("\nnew XTransientWindow(...)     new PMTransientWindow(...)");
        Display.sideBySide(
                Window.render(new XTransientWindow(24, 5, "Save as")),
                Window.render(new PMTransientWindow(24, 5, "Save as")));

        Display.section("what that cost");
        System.out.println("""
                  Two window kinds, two platforms, and a leaf class for every pair.
                  The X drawing code is written three times: in XWindow, XIconWindow
                  and XTransientWindow. Change how X draws a rectangle and you must
                  find all three - the compiler will not tell you which one you missed.

                  3 window kinds x 2 platforms = 6 classes
                  a third platform             = 9
                  a fourth window kind         = 8""");

        Display.section("and the platform cannot change");
        Window window = new XIconWindow(24, 5, "readme.txt");
        System.out.println("  window.platform() = " + window.platform());
        System.out.println("""
                  There is no operation on this object that can move it to Presentation
                  Manager. The platform is its class, chosen when the code was compiled.
                  The only way to change it is to build a different object - and any
                  state the original held goes with it.""");
    }
}
