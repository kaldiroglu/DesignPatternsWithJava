package dev.kaldiroglu.dp.structural.bridge.shape.pattern;

/** A RefinedAbstraction: a rectangle is four lines. */
public class Rectangle extends AbstractShape {

    private final int x;
    private final int y;
    private final int width;
    private final int height;

    public Rectangle(String name, ShapeDrawer drawer, int x, int y, int width, int height) {
        super(name, drawer);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
    public void draw() {
        drawer.drawLine(x, y, x + width, y);
        drawer.drawLine(x + width, y, x + width, y + height);
        drawer.drawLine(x + width, y + height, x, y + height);
        drawer.drawLine(x, y + height, x, y);
    }

    @Override
    public void erase() {
        drawer.clear(x, y, width, height);
    }
}
