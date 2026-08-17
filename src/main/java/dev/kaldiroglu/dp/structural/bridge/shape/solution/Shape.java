package dev.kaldiroglu.dp.structural.bridge.shape.solution;

/**
 * The Abstraction's interface: what a shape can do, whichever device it is drawn on.
 * <p>
 * {@code setDrawer} is what makes this a Bridge rather than a Strategy chosen once: the
 * device can be changed on an object that already exists, mid-program.
 */
public interface Shape {

    void draw();

    void erase();

    void setDrawer(ShapeDrawer drawer);
}
