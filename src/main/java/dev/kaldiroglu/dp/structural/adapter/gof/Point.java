package dev.kaldiroglu.dp.structural.adapter.gof;

/**
 * A coordinate pair in the drawing space.
 *
 * <p>GoF (p. 146) uses a {@code Coord} type and a {@code Point} struct; we model both
 * as a single immutable value object.</p>
 */
public record Point(double x, double y) {

    @Override
    public String toString() {
        return String.format("(%.1f, %.1f)", x, y);
    }
}
