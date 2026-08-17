package dev.kaldiroglu.dp.structural.bridge.shape.solution;

/**
 * The Implementor: what a shape is allowed to ask of a drawing device.
 * <p>
 * <strong>Every method here is a primitive of the device, not an operation of a shape.</strong>
 * That distinction is the whole solution, and it is the one most often got wrong. An earlier
 * version of this interface had {@code drawCircle} and {@code drawRectangle} on it, which
 * looks harmless and is not: adding a {@link Triangle} would then have forced every drawer to
 * grow a {@code drawTriangle}, and the two hierarchies would no longer be independent — which
 * is the only thing Bridge exists to buy.
 * <p>
 * GoF put it in one sentence on p. 154: the Implementor interface "doesn't have to correspond
 * exactly to Abstraction's interface; in fact the two interfaces can be quite different.
 * Typically the Implementor interface provides only primitive operations, and Abstraction
 * defines higher-level operations based on these primitives."
 */
public interface ShapeDrawer {

    void drawLine(int x1, int y1, int x2, int y2);

    void drawArc(int centerX, int centerY, int radius, int startDegrees, int sweepDegrees);

    void clear(int x, int y, int width, int height);
}
