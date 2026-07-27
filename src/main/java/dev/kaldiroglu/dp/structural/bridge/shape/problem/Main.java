package dev.kaldiroglu.dp.structural.bridge.shape.problem;

import java.util.List;

/**
 * The grid, and what it costs.
 * <p>
 * Three shape kinds and two devices are nine classes here. The same three kinds and two
 * devices in {@code shape.pattern} are seven — and the gap widens with every addition.
 */
public class Main {

    public static void main(String[] args) {
        List<Shape> everything = List.of(
                new CircleMacOS("circle"), new CircleXWindows("circle"),
                new RectangleMacOS("rectangle"), new RectangleXWindows("rectangle"),
                new TriangleMacOS("triangle"), new TriangleXWindows("triangle"));

        System.out.println("Every shape the editor can draw, one class each:");
        for (Shape shape : everything) {
            System.out.println(shape);
            shape.draw();
        }

        System.out.println("""

                What this design cannot do:
                  move a shape to another device after it exists
                                              no method, and no way to write one
                  add a device                one new class per shape kind
                  add a shape kind            one new class per device

                The device is the class. An object cannot change its class, so a
                CircleMacOS is a MacOS circle for as long as it lives.""");

        int kinds = 3;
        int devices = 2;
        System.out.printf("%n%d kinds x %d devices = %d leaf classes, plus %d abstract ones.%n",
                kinds, devices, kinds * devices, kinds + 1);
        System.out.println("shape.pattern covers the same ground with 3 shapes + 2 drawers.");
    }
}
