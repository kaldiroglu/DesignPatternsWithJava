package dev.kaldiroglu.dp.structural.bridge.gof.window.problem;

import dev.kaldiroglu.dp.structural.bridge.gof.window.Canvas;

/**
 * A second reason to subclass arrives: a window that shows an icon and a label.
 * <p>
 * This is where the design breaks. "Icon window" is a kind of window; "X window" is also
 * a kind of window; and a class can only extend one of them. The two leaf classes below
 * therefore have to repeat their platform's drawing code, verbatim.
 */
public abstract class IconWindow extends Window {

    private final String label;

    protected IconWindow(int width, int height, String label) {
        super(width, height);
        this.label = label;
    }

    protected String label() {
        return label;
    }

    @Override
    public void drawContents(Canvas canvas) {
        drawBorder(canvas);
        drawRect(canvas, 2, 1, 3, 2);          // the icon
        drawText(canvas, 6, 2, label);
    }
}
