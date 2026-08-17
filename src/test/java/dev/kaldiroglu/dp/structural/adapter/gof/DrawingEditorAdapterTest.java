package dev.kaldiroglu.dp.structural.adapter.gof;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The point of this class.
 * <p>
 * GoF's own example: an editor that manipulates everything through {@code Shape}, and a
 * {@code TextView} that already does the work but does not fit. Both forms of the solution are
 * here, so the difference between them can be asserted rather than described.
 */
class DrawingEditorAdapterTest {

    private static TextView textView() {
        return new TextView(new Point(10, 20), 100, 40, "hello");
    }

    @Test
    @DisplayName("the object adapter presents a TextView as a Shape")
    void objectAdapter() {
        Shape shape = new TextShape(textView());

        BoundingBox box = shape.boundingBox();
        assertEquals(10, box.bottomLeft().x());
        assertEquals(110, box.topRight().x());
        assertEquals(60, box.topRight().y());
        assertFalse(shape.isEmpty());
    }

    @Test
    @DisplayName("an editor holding Shape never learns a TextView is involved")
    void theEditorIsUnchanged() {
        java.util.List<Shape> drawing = java.util.List.of(
                new LineShape(new Point(0, 0), new Point(5, 5)),
                new TextShape(textView()));

        // The editor's whole world is this loop: one type, one call, no special case.
        for (Shape shape : drawing) {
            assertTrue(shape.createManipulator() != null);
            assertTrue(shape.boundingBox() != null);
        }
        assertEquals(2, drawing.size());
    }

    @Test
    @DisplayName("an empty text view is an empty shape — the adaptee answers, not the adapter")
    void isEmptyIsDelegated() {
        assertTrue(new TextShape(new TextView(new Point(0, 0), 10, 10, "")).isEmpty());
        assertFalse(new TextShape(textView()).isEmpty());
    }

    @Test
    @DisplayName("the class adapter inherits the adaptee instead of holding one")
    void classAdapterInherits() {
        Class<?> adapter = dev.kaldiroglu.dp.structural.adapter.gof.classadapter.TextShape.class;

        assertEquals(TextView.class, adapter.getSuperclass());
        assertTrue(Shape.class.isAssignableFrom(adapter));
        assertEquals(0, adapter.getDeclaredFields().length, "no adaptee field: it is one");
    }

    @Test
    @DisplayName("the class adapter gets isEmpty for free, and is usable as a TextView")
    void classAdapterInheritsBehavior() {
        var adapter = new dev.kaldiroglu.dp.structural.adapter.gof.classadapter.TextShape(
                new Point(10, 20), 100, 40, "hello");

        // isEmpty() was never declared on the adapter — TextView's already satisfies Shape.
        assertFalse(adapter.isEmpty());
        assertEquals(110, adapter.boundingBox().topRight().x());

        assertTrue(adapter instanceof TextView, "and it is a TextView as well as a Shape");

        // The object adapter is not, and the compiler will not even let you ask with
        // instanceof — so the assertion has to be made about the types themselves.
        assertFalse(TextView.class.isAssignableFrom(TextShape.class),
                "the object adapter holds a TextView; it is not one");
    }

    @Test
    @DisplayName("both forms produce the same bounding box")
    void bothFormsAgree() {
        Shape object = new TextShape(textView());
        Shape klass = new dev.kaldiroglu.dp.structural.adapter.gof.classadapter.TextShape(
                new Point(10, 20), 100, 40, "hello");

        assertEquals(object.boundingBox().topRight().x(), klass.boundingBox().topRight().x());
        assertEquals(object.boundingBox().topRight().y(), klass.boundingBox().topRight().y());
    }

    @Test
    @DisplayName("the pluggable adapter adapts something that is not a TextView at all")
    void pluggableAdapter() {
        var circle = new dev.kaldiroglu.dp.structural.adapter.gof.pluggable.Circle(
                new Point(50, 50), 10);

        Shape shape = new dev.kaldiroglu.dp.structural.adapter.gof.pluggable.PluggableShapeAdapter(
                "circle",
                () -> new BoundingBox(new Point(40, 40), new Point(60, 60)),
                () -> false);

        assertEquals(40, shape.boundingBox().bottomLeft().x());
        assertFalse(shape.isEmpty());
        // One adapter class, no Circle-specific type anywhere in it.
        assertEquals(0, java.util.Arrays.stream(shape.getClass().getDeclaredFields())
                .filter(f -> f.getType().getSimpleName().equals("Circle")).count());
    }
}
