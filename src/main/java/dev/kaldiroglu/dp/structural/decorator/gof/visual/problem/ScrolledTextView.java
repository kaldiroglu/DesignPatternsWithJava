package dev.kaldiroglu.dp.structural.decorator.gof.visual.problem;

import dev.kaldiroglu.dp.structural.decorator.gof.visual.Canvas;

/**
 * "A TextView with a scrollbar." Subclass number two.
 * <p>
 * On its own this class is perfectly reasonable. The trouble starts one class from now,
 * when somebody wants a view that has <em>both</em> a border and a scrollbar.
 */
public class ScrolledTextView extends TextView {

    public ScrolledTextView(int contentWidth, int contentHeight, String text) {
        super(contentWidth, contentHeight, text);
    }

    @Override
    public int width() {
        return contentWidth() + 1;
    }

    @Override
    public int height() {
        return contentHeight();
    }

    @Override
    public void draw(Canvas canvas, int x, int y) {
        drawText(canvas, x, y);
        drawScrollbar(canvas, x + contentWidth(), y, height());
    }

    private void drawScrollbar(Canvas canvas, int x, int y, int height) {
        canvas.put(x, y, '^');
        for (int i = 1; i < height - 1; i++) {
            canvas.put(x, y + i, '#');
        }
        canvas.put(x, y + height - 1, 'v');
    }
}
