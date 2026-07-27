package dev.kaldiroglu.dp.structural.bridge.shape.pattern;

/**
 * A ConcreteImplementor whose device draws arcs natively.
 * <p>
 * One call in, one call out. Compare with {@link XWindowsDrawer}, which has to build the same
 * arc out of line segments.
 */
public class MacOSDrawer extends AbstractShapeDrawer {

    public MacOSDrawer() {
        this("MacOS");
    }

    public MacOSDrawer(String name) {
        super(name);
    }

    @Override
    public void drawLine(int x1, int y1, int x2, int y2) {
        record("line (" + x1 + "," + y1 + ") -> (" + x2 + "," + y2 + ")");
    }

    @Override
    public void drawArc(int centerX, int centerY, int radius, int startDegrees, int sweepDegrees) {
        record("arc r=" + radius + " from " + startDegrees + " sweep " + sweepDegrees);
    }

    @Override
    public void clear(int x, int y, int width, int height) {
        record("clear " + width + "x" + height + " at (" + x + "," + y + ")");
    }
}
