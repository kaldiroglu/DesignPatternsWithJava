package dev.kaldiroglu.dp.structural.bridge.shape.pattern;

/**
 * The shape that proves the point.
 * <p>
 * It was added after both drawers were written, and <strong>neither drawer changed</strong>.
 * Under the old interface — the one with {@code drawCircle} and {@code drawRectangle} on it —
 * this class would have forced a {@code drawTriangle} into every device, which is the
 * definition of two hierarchies that are not independent.
 */
public class Triangle extends AbstractShape {

    private final int x1, y1, x2, y2, x3, y3;

    public Triangle(String name, ShapeDrawer drawer,
                    int x1, int y1, int x2, int y2, int x3, int y3) {
        super(name, drawer);
        this.x1 = x1; this.y1 = y1;
        this.x2 = x2; this.y2 = y2;
        this.x3 = x3; this.y3 = y3;
    }

    @Override
    public void draw() {
        drawer.drawLine(x1, y1, x2, y2);
        drawer.drawLine(x2, y2, x3, y3);
        drawer.drawLine(x3, y3, x1, y1);
    }

    @Override
    public void erase() {
        int minX = Math.min(x1, Math.min(x2, x3));
        int minY = Math.min(y1, Math.min(y2, y3));
        int maxX = Math.max(x1, Math.max(x2, x3));
        int maxY = Math.max(y1, Math.max(y2, y3));
        drawer.clear(minX, minY, maxX - minX, maxY - minY);
    }
}
