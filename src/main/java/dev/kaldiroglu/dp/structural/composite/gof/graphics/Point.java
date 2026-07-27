package dev.kaldiroglu.dp.structural.composite.gof.graphics;

/**
 * A position on the drawing canvas.
 *
 * @param x horizontal coordinate
 * @param y vertical coordinate
 */
public record Point(int x, int y) {

    /** Returns this point translated by the given offsets. */
    public Point translatedBy(int dx, int dy) {
        return new Point(x + dx, y + dy);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
