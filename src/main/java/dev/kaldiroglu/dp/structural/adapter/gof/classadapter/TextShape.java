package dev.kaldiroglu.dp.structural.adapter.gof.classadapter;

import dev.kaldiroglu.dp.structural.adapter.gof.BoundingBox;
import dev.kaldiroglu.dp.structural.adapter.gof.Manipulator;
import dev.kaldiroglu.dp.structural.adapter.gof.Point;
import dev.kaldiroglu.dp.structural.adapter.gof.Shape;
import dev.kaldiroglu.dp.structural.adapter.gof.TextManipulator;
import dev.kaldiroglu.dp.structural.adapter.gof.TextView;

/**
 * <b>Class Adapter</b> (GoF p. 146).
 *
 * <p>GoF writes this in C++ using multiple inheritance: {@code class TextShape : public Shape,
 * private TextView}. Java has only single implementation inheritance, so the idiomatic class
 * adapter <b>extends the adaptee</b> ({@link TextView}) and <b>implements the target</b>
 * ({@link Shape}).</p>
 *
 * <p>Trade-offs versus the {@link dev.kaldiroglu.adapter.gof.TextShape object adapter}:</p>
 * <ul>
 *   <li>It adapts {@code TextView} itself, committing to a subclass relationship at compile time
 *       (it cannot adapt a {@code TextView} subclass passed in at runtime).</li>
 *   <li>In exchange it can override {@code TextView} behaviour directly and needs no wrapped
 *       field.</li>
 * </ul>
 */
public class TextShape extends TextView implements Shape {

    public TextShape(Point origin, double width, double height, String text) {
        super(origin, width, height, text);
    }

    @Override
    public BoundingBox boundingBox() {
        Point origin = getOrigin();   // inherited from TextView
        Point extent = getExtent();   // inherited from TextView
        Point topRight = new Point(origin.x() + extent.x(), origin.y() + extent.y());
        return new BoundingBox(origin, topRight);
    }

    @Override
    public Manipulator createManipulator() {
        return new TextManipulator(this);
    }

    // Note: isEmpty() is inherited from TextView and already satisfies Shape.isEmpty(),
    // so the class adapter does not need to declare it. That "free" method is exactly the
    // convenience a class adapter buys you.
}
