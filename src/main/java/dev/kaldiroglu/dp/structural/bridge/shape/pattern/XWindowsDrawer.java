package dev.kaldiroglu.dp.structural.bridge.shape.pattern;

/**
 * A ConcreteImplementor whose device has <em>no</em> arc call.
 * <p>
 * This is GoF's Presentation Manager detail (p. 157) in a second domain: the device cannot do
 * what it was asked, so it builds the result out of the primitives it does have — here,
 * sixteen short line segments. The {@link Circle} that asked for the arc never finds out, and
 * must not have to.
 */
public class XWindowsDrawer extends AbstractShapeDrawer {

    /** How finely an arc is approximated when the device cannot draw one. */
    private static final int SEGMENTS = 16;

    public XWindowsDrawer() {
        this("XWindows");
    }

    public XWindowsDrawer(String name) {
        super(name);
    }

    @Override
    public void drawLine(int x1, int y1, int x2, int y2) {
        record("line (" + x1 + "," + y1 + ") -> (" + x2 + "," + y2 + ")");
    }

    @Override
    public void drawArc(int centerX, int centerY, int radius, int startDegrees, int sweepDegrees) {
        int previousX = pointX(centerX, radius, startDegrees);
        int previousY = pointY(centerY, radius, startDegrees);
        for (int i = 1; i <= SEGMENTS; i++) {
            int angle = startDegrees + sweepDegrees * i / SEGMENTS;
            int x = pointX(centerX, radius, angle);
            int y = pointY(centerY, radius, angle);
            drawLine(previousX, previousY, x, y);
            previousX = x;
            previousY = y;
        }
    }

    @Override
    public void clear(int x, int y, int width, int height) {
        record("clear " + width + "x" + height + " at (" + x + "," + y + ")");
    }

    private static int pointX(int centerX, int radius, int degrees) {
        return centerX + (int) Math.round(radius * Math.cos(Math.toRadians(degrees)));
    }

    private static int pointY(int centerY, int radius, int degrees) {
        return centerY + (int) Math.round(radius * Math.sin(Math.toRadians(degrees)));
    }
}
