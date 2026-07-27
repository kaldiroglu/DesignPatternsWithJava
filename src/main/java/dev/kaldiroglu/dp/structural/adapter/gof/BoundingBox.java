package dev.kaldiroglu.dp.structural.adapter.gof;

/**
 * The rectangle returned by {@link Shape#boundingBox()}.
 *
 * <p>GoF returns the box through two out-parameters ({@code bottomLeft}, {@code topRight});
 * Java has no out-parameters, so we return an immutable value object instead.</p>
 */
public record BoundingBox(Point bottomLeft, Point topRight) {

    @Override
    public String toString() {
        return "BoundingBox[" + bottomLeft + " -> " + topRight + "]";
    }
}
