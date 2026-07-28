package dev.kaldiroglu.dp.structural.flyweight.circles.solution;

import java.awt.Graphics2D;

/**
 * One circle on the canvas: where it is, and which shared style it wears.
 *
 * <p>Two fields. The problem version had four and a superclass. This one is not a
 * {@code JComponent}, because it never was one in any useful sense — it was drawn by hand
 * onto the canvas's {@code Graphics2D} and never added to a container.</p>
 */
public class Circle {

    private Point center;
    private final CircleStyle style;   // shared with every circle that looks the same

    public Circle(Point center, CircleStyle style) {
        this.center = center;
        this.style = style;
    }

    public void draw(Graphics2D g2) {
        style.draw(g2, center);        // the position goes in; the style holds none of it
    }

    public Point center() {
        return center;
    }

    public void moveTo(Point center) {
        this.center = center;
    }

    public CircleStyle style() {
        return style;
    }
}
