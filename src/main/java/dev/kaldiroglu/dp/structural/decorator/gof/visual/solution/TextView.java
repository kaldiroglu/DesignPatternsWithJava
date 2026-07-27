package dev.kaldiroglu.dp.structural.decorator.gof.visual.solution;

import dev.kaldiroglu.dp.structural.decorator.gof.visual.Canvas;
import dev.kaldiroglu.dp.structural.decorator.gof.visual.TextLayout;

import java.util.List;

/**
 * The <b>ConcreteComponent</b>: the object to which responsibilities can be attached
 * (GoF p. 178).
 * <p>
 * Compare it with {@code problem.TextView}: this class knows nothing about borders or
 * scrollbars, and — this is the point — it will never need to be changed when a new
 * embellishment is invented.
 */
public final class TextView implements VisualComponent {

    private final int contentWidth;
    private final int contentHeight;
    private final String text;

    public TextView(int contentWidth, int contentHeight, String text) {
        this.contentWidth = contentWidth;
        this.contentHeight = contentHeight;
        this.text = text;
    }

    @Override
    public int width() {
        return contentWidth;
    }

    @Override
    public int height() {
        return contentHeight;
    }

    @Override
    public void draw(Canvas canvas, int x, int y) {
        List<String> lines = TextLayout.wrap(text, contentWidth, contentHeight);
        for (int i = 0; i < lines.size(); i++) {
            canvas.text(x, y + i, lines.get(i));
        }
    }
}
