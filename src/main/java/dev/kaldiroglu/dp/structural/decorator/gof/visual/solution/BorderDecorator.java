package dev.kaldiroglu.dp.structural.decorator.gof.visual.solution;

import dev.kaldiroglu.dp.structural.decorator.gof.visual.Canvas;

/**
 * A <b>ConcreteDecorator</b>: adds a border to whatever it wraps (GoF p. 181).
 * <p>
 * It knows nothing about text views. It will decorate a {@link TextView}, a
 * {@link ScrollDecorator}, or another {@code BorderDecorator} — because all it requires
 * is the {@link VisualComponent} interface. That is the difference between "border of a
 * text view" and "border of anything".
 */
public final class BorderDecorator extends Decorator {

    public BorderDecorator(VisualComponent component) {
        super(component);
    }

    @Override
    public int width() {
        return component().width() + 2;
    }

    @Override
    public int height() {
        return component().height() + 2;
    }

    @Override
    public void draw(Canvas canvas, int x, int y) {
        component().draw(canvas, x + 1, y + 1); // forward the request...
        drawBorder(canvas, x, y);               // ...then add our own responsibility
    }

    private void drawBorder(Canvas canvas, int x, int y) {
        canvas.rectangle(x, y, width(), height());
    }
}
