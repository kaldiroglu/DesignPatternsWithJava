package dev.kaldiroglu.dp.structural.adapter.gof.pluggable;

import dev.kaldiroglu.dp.structural.adapter.gof.BoundingBox;
import dev.kaldiroglu.dp.structural.adapter.gof.Point;
import dev.kaldiroglu.dp.structural.adapter.gof.Shape;
import dev.kaldiroglu.dp.structural.adapter.gof.TextView;

import java.util.List;

/**
 * Runnable demo for both pluggable-adapter domains. In each, ONE adapter class serves TWO
 * differently-shaped adaptees, with the adaptation injected as lambdas (GoF technique (c)).
 */
public final class PluggableDemo {

    private PluggableDemo() {
    }

    public static void main(String[] args) {
        System.out.println("== Domain A: drawing editor (one PluggableShapeAdapter, two adaptees) ==");

        TextView text = new TextView(new Point(10, 10), 100, 20, "Pluggable");
        Shape adaptedText = new PluggableShapeAdapter(
                "PluggableShape[" + text + "]",
                () -> {
                    Point origin = text.getOrigin();
                    Point extent = text.getExtent();
                    return new BoundingBox(origin,
                            new Point(origin.x() + extent.x(), origin.y() + extent.y()));
                },
                text::isEmpty);

        Circle circle = new Circle(new Point(50, 50), 30);
        Shape adaptedCircle = new PluggableShapeAdapter(
                "PluggableShape[" + circle + "]",
                () -> new BoundingBox(
                        new Point(circle.center().x() - circle.radius(), circle.center().y() - circle.radius()),
                        new Point(circle.center().x() + circle.radius(), circle.center().y() + circle.radius())),
                () -> false);

        for (Shape shape : List.of(adaptedText, adaptedCircle)) {
            System.out.println(shape);
            System.out.println("  boundingBox = " + shape.boundingBox());
            System.out.println("  isEmpty     = " + shape.isEmpty());
            shape.createManipulator().manipulate();
        }
    }
}
