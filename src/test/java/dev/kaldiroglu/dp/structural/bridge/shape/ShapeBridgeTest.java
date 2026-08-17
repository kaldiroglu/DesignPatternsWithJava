package dev.kaldiroglu.dp.structural.bridge.shape;

import dev.kaldiroglu.dp.structural.bridge.shape.solution.Circle;
import dev.kaldiroglu.dp.structural.bridge.shape.solution.MacOSDrawer;
import dev.kaldiroglu.dp.structural.bridge.shape.solution.Rectangle;
import dev.kaldiroglu.dp.structural.bridge.shape.solution.Shape;
import dev.kaldiroglu.dp.structural.bridge.shape.solution.ShapeDrawer;
import dev.kaldiroglu.dp.structural.bridge.shape.solution.Triangle;
import dev.kaldiroglu.dp.structural.bridge.shape.solution.XWindowsDrawer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The point of this class.
 * <p>
 * The implementor offers primitives, the abstraction composes them. The last test is the one
 * that keeps it that way: it fails the day somebody adds {@code drawCircle} to the drawer.
 */
class ShapeBridgeTest {

    @Test
    @DisplayName("one shape over two devices — and the device decides how, not what")
    void oneShapeTwoDevices() {
        MacOSDrawer mac = new MacOSDrawer();
        XWindowsDrawer x = new XWindowsDrawer();

        new Circle("c", mac, 50, 50, 20).draw();
        new Circle("c", x, 50, 50, 20).draw();

        // MacOS draws arcs natively; XWindows has no arc call and builds one from segments.
        assertEquals(1, mac.calls().size());
        assertEquals(16, x.calls().size());
        assertTrue(mac.calls().getFirst().startsWith("arc"));
        assertTrue(x.calls().stream().allMatch(call -> call.startsWith("line")));
    }

    @Test
    @DisplayName("the same shape object moves between devices at run time")
    void theDeviceCanChangeOnALiveObject() {
        MacOSDrawer mac = new MacOSDrawer();
        XWindowsDrawer x = new XWindowsDrawer();
        Shape circle = new Circle("c", mac, 50, 50, 20);

        circle.draw();
        assertEquals(1, mac.calls().size());

        circle.setDrawer(x);
        circle.draw();
        assertEquals(16, x.calls().size());
        assertEquals(1, mac.calls().size()); // nothing more went to the old device
    }

    @Test
    @DisplayName("each shape composes the primitives its own way")
    void everyShapeComposesPrimitives() {
        MacOSDrawer mac = new MacOSDrawer();

        new Rectangle("r", mac, 10, 10, 40, 20).draw();
        assertEquals(4, mac.calls().size());

        mac.resetCalls();
        new Triangle("t", mac, 0, 0, 20, 0, 10, 15).draw();
        assertEquals(3, mac.calls().size());

        mac.resetCalls();
        new Circle("c", mac, 0, 0, 5).erase();
        assertEquals(1, mac.calls().size());
        assertTrue(mac.calls().getFirst().startsWith("clear"));
    }

    @Test
    @DisplayName("adding a shape cost one class and no drawer changed")
    void aNewShapeCostsOneClass() {
        // Triangle was written after both drawers existed. If ShapeDrawer had carried a
        // method per shape, this class could not have been added without editing both.
        assertEquals(3, ShapeDrawer.class.getDeclaredMethods().length);
        assertTrue(List.of(Circle.class, Rectangle.class, Triangle.class).stream()
                .allMatch(Shape.class::isAssignableFrom));
    }

    @Test
    @DisplayName("no primitive names a shape")
    void theImplementorKnowsNothingAboutShapes() {
        List<String> shapeWords = List.of("circle", "rectangle", "triangle", "square", "shape");

        for (Method method : ShapeDrawer.class.getDeclaredMethods()) {
            String name = method.getName().toLowerCase();
            assertTrue(shapeWords.stream().noneMatch(name::contains),
                    "ShapeDrawer." + method.getName() + " names a shape, so the two hierarchies "
                            + "are no longer independent — a new shape would force every drawer "
                            + "to change");
        }
    }

    @Test
    @DisplayName("the naive design binds the device to the class instead")
    void theProblemPackageCannotSwitch() {
        var macCircle = new dev.kaldiroglu.dp.structural.bridge.shape.problem.CircleMacOS("c");
        var xCircle = new dev.kaldiroglu.dp.structural.bridge.shape.problem.CircleXWindows("c");

        // Two objects, two classes, for one shape on two devices — and no setDrawer anywhere.
        assertNotEquals(macCircle.getClass(), xCircle.getClass());
        assertTrue(java.util.Arrays.stream(
                        dev.kaldiroglu.dp.structural.bridge.shape.problem.Shape.class.getMethods())
                .noneMatch(m -> m.getName().equals("setDrawer")));
    }
}
