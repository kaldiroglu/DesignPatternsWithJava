package dev.kaldiroglu.dp.structural.decorator.gof.visual.problem;

import dev.kaldiroglu.dp.structural.decorator.gof.visual.Canvas;

/**
 * "A TextView with a border and a scrollbar, scrollbar inside the border."
 * <p>
 * This is where subclassing starts to hurt. The class can inherit the border from
 * {@link BorderedTextView}, but Java has no multiple inheritance, so the scrollbar
 * drawing has to be <em>copied</em> from {@link ScrolledTextView}. Two copies of one
 * scrollbar now exist; fix a bug in one and the other keeps it.
 */
public class BorderedScrolledTextView extends BorderedTextView {

    public BorderedScrolledTextView(int contentWidth, int contentHeight, String text) {
        super(contentWidth, contentHeight, text);
    }

    @Override
    public int width() {
        return contentWidth() + 3; // 2 for the border, 1 for the scrollbar
    }

    @Override
    public int height() {
        return contentHeight() + 2;
    }

    @Override
    public void draw(Canvas canvas, int x, int y) {
        drawText(canvas, x + 1, y + 1);
        drawScrollbar(canvas, x + 1 + contentWidth(), y + 1, contentHeight());
        canvas.rectangle(x, y, width(), height());
    }

    // Copied verbatim from ScrolledTextView. There is nowhere else to put it: this class
    // already inherits from BorderedTextView, and a protected member of a sibling class
    // is not accessible. This duplication is the cost of embellishing by subclassing.
    private void drawScrollbar(Canvas canvas, int x, int y, int height) {
        canvas.put(x, y, '^');
        for (int i = 1; i < height - 1; i++) {
            canvas.put(x, y + i, '#');
        }
        canvas.put(x, y + height - 1, 'v');
    }
}
