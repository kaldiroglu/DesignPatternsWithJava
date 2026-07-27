package dev.kaldiroglu.dp.structural.decorator.gof.visual.problem;

import dev.kaldiroglu.dp.structural.decorator.gof.visual.Canvas;

/**
 * "A TextView with a border." Subclass number one.
 * <p>
 * Note what the class name commits to: the border is part of the object's <em>type</em>.
 * A client that wants a border must ask for a {@code BorderedTextView} at construction
 * time, and can never take the border away again.
 */
public class BorderedTextView extends TextView {

    public BorderedTextView(int contentWidth, int contentHeight, String text) {
        super(contentWidth, contentHeight, text);
    }

    @Override
    public int width() {
        return contentWidth() + 2;
    }

    @Override
    public int height() {
        return contentHeight() + 2;
    }

    @Override
    public void draw(Canvas canvas, int x, int y) {
        drawText(canvas, x + 1, y + 1);
        canvas.rectangle(x, y, width(), height());
    }
}
