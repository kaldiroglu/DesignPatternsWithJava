package dev.kaldiroglu.dp.structural.decorator.gof.visual.problem;

import dev.kaldiroglu.dp.structural.decorator.gof.visual.Canvas;

/**
 * "A TextView with a border and a scrollbar, scrollbar <em>outside</em> the border."
 * <p>
 * The same two embellishments as {@link BorderedScrolledTextView}, applied in the other
 * order, so the picture is different — and so a fifth class is needed. Order matters,
 * and under subclassing every order costs another class.
 * <p>
 * The arithmetic, with {@code n} embellishments whose order matters: the number of
 * classes needed to cover every combination is the sum over {@code k} of
 * {@code n!/(n-k)!}. For n=2 that is 4 subclasses; for n=3, 15; for n=4, 64.
 */
public class ScrolledBorderedTextView extends ScrolledTextView {

    public ScrolledBorderedTextView(int contentWidth, int contentHeight, String text) {
        super(contentWidth, contentHeight, text);
    }

    @Override
    public int width() {
        return contentWidth() + 3;
    }

    @Override
    public int height() {
        return contentHeight() + 2;
    }

    @Override
    public void draw(Canvas canvas, int x, int y) {
        drawText(canvas, x + 1, y + 1);
        canvas.rectangle(x, y, contentWidth() + 2, height()); // copied from BorderedTextView
        drawScrollbar(canvas, x + contentWidth() + 2, y, height());
    }

    // Copied from ScrolledTextView for the same reason as in BorderedScrolledTextView.
    private void drawScrollbar(Canvas canvas, int x, int y, int height) {
        canvas.put(x, y, '^');
        for (int i = 1; i < height - 1; i++) {
            canvas.put(x, y + i, '#');
        }
        canvas.put(x, y + height - 1, 'v');
    }
}
