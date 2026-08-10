package dev.kaldiroglu.dp.structural.decorator.gof.visual.skinandguts;

import dev.kaldiroglu.dp.structural.decorator.gof.visual.Canvas;

/**
 * The <b>Strategy</b>: how a border is drawn, as an object.
 * <p>
 * This is the "guts" half of GoF implementation issue 4, <i>changing the skin of an object
 * versus changing its guts</i> (p. 180). The border itself is a skin — a decorator wraps a
 * component that knows nothing about it. But <em>which</em> border to draw is a decision
 * inside the decorator, and a decorator that answers it with a branch has to be edited for
 * every new answer.
 * <p>
 * So the decorator is given a hook and holds one of these. The two patterns are not
 * alternatives here; they are stacked, each solving the problem it is good at.
 */
public interface BorderStyle {

    /**
     * Strokes the outline of a rectangle whose top-left corner is (x, y).
     *
     * @param canvas where to draw
     * @param x      left edge
     * @param y      top edge
     * @param width  full width, including both vertical edges
     * @param height full height, including both horizontal edges
     */
    void stroke(Canvas canvas, int x, int y, int width, int height);
}
