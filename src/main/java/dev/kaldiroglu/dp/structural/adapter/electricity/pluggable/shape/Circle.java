package dev.kaldiroglu.dp.structural.adapter.electricity.pluggable.shape;


import dev.kaldiroglu.dp.structural.adapter.gof.Point;

/**
 * A second, <i>unrelated</i> adaptee with its own interface &ndash; it knows nothing about
 * {@code Shape}, {@code TextView}, or bounding boxes. It exists to prove that the single
 * {@link PluggableShapeAdapter} class can adapt adaptees that share no common base.
 */
public final class Circle {

    private final Point center;
    private final double radius;

    public Circle(Point center, double radius) {
        this.center = center;
        this.radius = radius;
    }

    public Point center() {
        return center;
    }

    public double radius() {
        return radius;
    }

    @Override
    public String toString() {
        return "Circle@" + center + " r=" + radius;
    }
}
