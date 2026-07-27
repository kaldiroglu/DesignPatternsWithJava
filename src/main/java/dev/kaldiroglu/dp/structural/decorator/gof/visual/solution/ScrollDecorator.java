package dev.kaldiroglu.dp.structural.decorator.gof.visual.solution;

import dev.kaldiroglu.dp.structural.decorator.gof.visual.Canvas;

/**
 * A <b>ConcreteDecorator</b>: adds a vertical scrollbar to whatever it wraps (GoF p. 181).
 * <p>
 * There is exactly one scrollbar implementation in this package. The {@code problem}
 * package needed three copies of it.
 */
public final class ScrollDecorator extends Decorator {

    public ScrollDecorator(VisualComponent component) {
        super(component);
    }

    @Override
    public int width() {
        return component().width() + 1;
    }

    @Override
    public void draw(Canvas canvas, int x, int y) {
        component().draw(canvas, x, y);
        drawScrollbar(canvas, x + component().width(), y);
    }

    private void drawScrollbar(Canvas canvas, int x, int y) {
        int height = height();
        canvas.put(x, y, '^');
        for (int i = 1; i < height - 1; i++) {
            canvas.put(x, y + i, '#');
        }
        canvas.put(x, y + height - 1, 'v');
    }
}
