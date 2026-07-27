package dev.kaldiroglu.dp.structural.adapter.gof;

/**
 * A <i>native</i> {@link Shape} &ndash; not an adapter. It exists so the demo shows the editor
 * treating adapted text and ordinary shapes <b>uniformly</b>, which is the payoff of the pattern.
 *
 * <p>A line is defined by two endpoints.</p>
 */
public class LineShape implements Shape {

    private final Point start;
    private final Point end;

    public LineShape(Point start, Point end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public BoundingBox boundingBox() {
        double left = Math.min(start.x(), end.x());
        double bottom = Math.min(start.y(), end.y());
        double right = Math.max(start.x(), end.x());
        double top = Math.max(start.y(), end.y());
        return new BoundingBox(new Point(left, bottom), new Point(right, top));
    }

    @Override
    public Manipulator createManipulator() {
        return new LineManipulator(this);
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public String toString() {
        return "LineShape " + start + "-" + end;
    }
}
