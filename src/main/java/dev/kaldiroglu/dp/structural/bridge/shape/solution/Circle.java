package dev.kaldiroglu.dp.structural.bridge.shape.solution;

/** A RefinedAbstraction: a circle is one arc, all the way round. */
public class Circle extends AbstractShape {

    private final int centerX;
    private final int centerY;
    private final int radius;

    public Circle(String name, ShapeDrawer drawer, int centerX, int centerY, int radius) {
        super(name, drawer);
        this.centerX = centerX;
        this.centerY = centerY;
        this.radius = radius;
    }

    @Override
    public void draw() {
        drawer.drawArc(centerX, centerY, radius, 0, 360);
    }

    @Override
    public void erase() {
        drawer.clear(centerX - radius, centerY - radius, radius * 2, radius * 2);
    }
}
