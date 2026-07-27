package dev.kaldiroglu.dp.structural.bridge.shape.pattern;

/**
 * Three shapes, two devices, and one detail worth stopping on.
 * <p>
 * The same {@link Circle} asks for one arc. MacOS draws it with one call; XWindows has no arc
 * primitive and issues sixteen lines instead. The circle never learns why.
 */
public class Main {

    public static void main(String[] args) {
        MacOSDrawer mac = new MacOSDrawer();
        XWindowsDrawer x = new XWindowsDrawer();

        System.out.println("A circle on MacOS:");
        Shape circle = new Circle("circle", mac, 50, 50, 20);
        circle.draw();
        System.out.println("  -> " + mac.calls().size() + " device call(s)");

        System.out.println("\nThe same circle object, moved to XWindows at run time:");
        circle.setDrawer(x);
        circle.draw();
        System.out.println("  -> " + x.calls().size() + " device call(s), because XWindows has no arc");

        System.out.println("\nA rectangle, then a triangle, both on MacOS:");
        mac.resetCalls();
        new Rectangle("rectangle", mac, 10, 10, 40, 20).draw();
        new Triangle("triangle", mac, 0, 0, 20, 0, 10, 15).draw();
        System.out.println("  -> " + mac.calls().size() + " device call(s): 4 lines + 3 lines");

        System.out.println("\nTriangle was added after both drawers were written.");
        System.out.println("Neither drawer changed. That is what m + n buys.");
    }
}
