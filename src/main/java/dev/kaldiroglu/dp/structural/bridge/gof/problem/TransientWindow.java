package dev.kaldiroglu.dp.structural.bridge.gof.problem;

import dev.kaldiroglu.dp.structural.bridge.gof.Canvas;

/**
 * A third kind of window — a transient dialog with a title bar.
 * <p>
 * Adding it is where the arithmetic becomes visible: this one abstract class forces
 * <em>one new leaf class per platform</em>, and neither of them contains anything new.
 */
public abstract class TransientWindow extends Window {

    private final String title;

    protected TransientWindow(int width, int height, String title) {
        super(width, height);
        this.title = title;
    }

    protected String title() {
        return title;
    }

    @Override
    public void drawContents(Canvas canvas) {
        drawBorder(canvas);
        drawText(canvas, 2, 0, " " + title + " ");
    }
}
