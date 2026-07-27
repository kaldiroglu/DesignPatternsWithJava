package dev.kaldiroglu.dp.structural.adapter.gof;

import java.util.List;

import dev.kaldiroglu.dp.structural.adapter.gof.classadapter.TextShape;

/**
 * <b>Client</b> participant (GoF p. 146).
 *
 * <p>The drawing editor collaborates with everything through the {@link Shape} interface. It never
 * learns that some of those shapes are really {@link TextView}s underneath &ndash; that knowledge
 * is sealed inside the adapters. Notice the {@code for} loop treats adapted and native shapes
 * identically: that uniformity is the whole benefit of the Adapter pattern.</p>
 */
public final class DrawingEditor {

    private DrawingEditor() {
    }

    public static void main(String[] args) {
        List<Shape> drawing = List.of(
                new LineShape(new Point(0, 0), new Point(40, 30)),
                // Object adapter: wraps an existing TextView instance.
                new dev.kaldiroglu.dp.structural.adapter.gof.TextShape(
                        new TextView(new Point(10, 10), 100, 20, "Hello, Adapter")),
                // Object adapter around empty text (isEmpty delegates to the adaptee).
                new dev.kaldiroglu.dp.structural.adapter.gof.TextShape(
                        new TextView(new Point(5, 5), 0, 0, "")),
                // Class adapter: is-a TextView, behaves-as Shape.
                new TextShape(new Point(50, 50), 80, 16, "Class adapter"));

        for (Shape shape : drawing) {
            System.out.println(shape);
            System.out.println("  boundingBox = " + shape.boundingBox());
            System.out.println("  isEmpty     = " + shape.isEmpty());
            shape.createManipulator().manipulate();
        }
    }
}
