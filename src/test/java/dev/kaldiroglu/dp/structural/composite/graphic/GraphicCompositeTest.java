package dev.kaldiroglu.dp.structural.composite.graphic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The point of this class.
 * <p>
 * A composite is worth having when one call answers for a whole tree. These tests measure
 * that, and pin the safety choice this package made so it cannot drift.
 */
class GraphicCompositeTest {

    private static Canvas display() {
        Canvas display = new Canvas("Display", "Light Green");
        display.addGraphic(new Circle("Red Circle", "Red"));
        display.addGraphic(new Circle("Blue Circle", "Blue"));
        display.addGraphic(new Ellipse("Black Ellipse", "Black"));

        Canvas logo = new Canvas("Logo", "White");
        logo.addGraphic(new Triangle("Triangle", "Yellow"));
        logo.addGraphic(new Rectangle("Rectangle", "Green"));
        display.addGraphic(logo);
        return display;
    }

    private static String outputOf(Runnable action) {
        PrintStream original = System.out;
        ByteArrayOutputStream captured = new ByteArrayOutputStream();
        System.setOut(new PrintStream(captured));
        try {
            action.run();
        } finally {
            System.setOut(original);
        }
        return captured.toString();
    }

    @Test
    @DisplayName("a leaf is one shape; a canvas is the sum of its tree")
    void shapeCountRecurses() {
        assertEquals(1, new Circle("c", "Red").shapeCount());
        assertEquals(5, display().shapeCount());   // three shapes, plus a group of two
    }

    @Test
    @DisplayName("a canvas inside a canvas is just another Graphic")
    void compositesNest() {
        Canvas outer = new Canvas("outer", "White");
        Canvas middle = new Canvas("middle", "White");
        Canvas inner = new Canvas("inner", "White");
        inner.addGraphic(new Circle("c", "Red"));
        middle.addGraphic(inner);
        outer.addGraphic(middle);

        assertEquals(1, outer.shapeCount());   // depth costs nothing
        inner.addGraphic(new Triangle("t", "Blue"));
        assertEquals(2, outer.shapeCount());   // and the change is seen at the top
    }

    @Test
    @DisplayName("one call reaches every leaf, at any depth")
    void operationsForwardToTheWholeTree() {
        String drawn = outputOf(() -> display().draw());

        assertTrue(drawn.contains("Red Circle"));
        assertTrue(drawn.contains("Triangle"));   // two levels down
        assertEquals(5, drawn.lines().filter(l -> l.startsWith("Drawing a")
                || l.startsWith("Drawing an")).count());
    }

    @Test
    @DisplayName("listing recurses — the first version printed nested canvases as one line")
    void listingRecurses() {
        String listed = outputOf(() -> display().listGraphic());

        assertTrue(listed.contains("Logo"));
        assertTrue(listed.contains("Triangle"), "a nested canvas must show its contents");
        assertTrue(listed.contains("        "), "depth should be visible as indentation");
    }

    @Test
    @DisplayName("child management is not on the Component, so a leaf cannot be given children")
    void theSafeVariant() {
        // GoF implementation issue 1. This package chose safety: the method does not exist
        // on Graphic, so adding a child to a Circle is a compile error rather than a
        // run-time failure. The cost is the cast a tree-building client has to make.
        assertTrue(Arrays.stream(Graphic.class.getDeclaredMethods())
                .noneMatch(m -> m.getName().equals("addGraphic")));
        assertTrue(Arrays.stream(CompositeGraphic.class.getDeclaredMethods())
                .anyMatch(m -> m.getName().equals("addGraphic")));
        assertFalse(CompositeGraphic.class.isAssignableFrom(Circle.class));
        assertTrue(CompositeGraphic.class.isAssignableFrom(Canvas.class));
    }

    @Test
    @DisplayName("the children list handed out cannot be edited behind the canvas's back")
    void childrenAreNotShared() {
        Canvas display = display();
        assertThrows(UnsupportedOperationException.class,
                () -> display.getGraphics().clear());
        assertEquals(5, display.shapeCount());
    }

    @Test
    @DisplayName("removing a child changes what the tree reports")
    void removingAChild() {
        Canvas display = new Canvas("d", "White");
        Circle circle = new Circle("c", "Red");
        display.addGraphic(circle);
        assertEquals(1, display.shapeCount());

        display.removeGraphic(circle);
        assertEquals(0, display.shapeCount());
    }
}
