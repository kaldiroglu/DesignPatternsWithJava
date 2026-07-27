package dev.kaldiroglu.dp.structural.decorator.gof.visual.problem;

import dev.kaldiroglu.dp.structural.decorator.gof.visual.Canvas;
import dev.kaldiroglu.dp.structural.decorator.gof.visual.TextLayout;

import java.util.List;

/**
 * A view that displays text — the starting point of GoF's Motivation (p. 175).
 * <p>
 * In this package embellishments are added the obvious way: <em>by subclassing</em>.
 * Every subclass in this package inherits from this one, which is exactly what makes
 * the design go wrong once there is more than one embellishment.
 */
public class TextView {

    private final int contentWidth;
    private final int contentHeight;
    private final String text;

    public TextView(int contentWidth, int contentHeight, String text) {
        this.contentWidth = contentWidth;
        this.contentHeight = contentHeight;
        this.text = text;
    }

    /** The width of the whole view, embellishments included. */
    public int width() {
        return contentWidth;
    }

    /** The height of the whole view, embellishments included. */
    public int height() {
        return contentHeight;
    }

    public void draw(Canvas canvas, int x, int y) {
        drawText(canvas, x, y);
    }

    /** Draws the text itself. Subclasses call this after making room for their embellishment. */
    protected final void drawText(Canvas canvas, int x, int y) {
        List<String> lines = TextLayout.wrap(text, contentWidth, contentHeight);
        for (int i = 0; i < lines.size(); i++) {
            canvas.text(x, y + i, lines.get(i));
        }
    }

    protected final int contentWidth() {
        return contentWidth;
    }

    protected final int contentHeight() {
        return contentHeight;
    }

    /** Renders a view on a canvas of its own size. */
    public static String render(TextView view) {
        Canvas canvas = new Canvas(view.width(), view.height());
        view.draw(canvas, 0, 0);
        return canvas.toString();
    }
}
