package dev.kaldiroglu.dp.structural.composite.gof.graphics;

/**
 * Leaf role of the Composite pattern (GoF, p. 165) — a primitive graphic.
 *
 * <p>A {@code Line} has no children. It implements {@link #draw(Point)} by
 * doing the actual work itself; it inherits the failing child operations from
 * {@link Graphic}.</p>
 */
public class Line extends Graphic {

    private final int length;

    public Line(int length) {
        this.length = length;
    }

    public int length() {
        return length;
    }

    @Override
    public void draw(Point at) {
        System.out.println("Line of length " + length + " drawn at " + at);
    }
}
