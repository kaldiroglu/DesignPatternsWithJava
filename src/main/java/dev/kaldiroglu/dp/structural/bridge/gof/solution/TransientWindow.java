package dev.kaldiroglu.dp.structural.bridge.gof.solution;

import dev.kaldiroglu.dp.structural.bridge.gof.Canvas;

/**
 * A second <b>RefinedAbstraction</b>. Adding it cost exactly one class — and it works on
 * every platform that exists now, and on every platform added later.
 */
public final class TransientWindow extends Window {

    private final String title;

    public TransientWindow(int width, int height, String title, WindowImp imp) {
        super(width, height, imp);
        this.title = title;
    }

    @Override
    public void drawContents(Canvas canvas) {
        drawBorder(canvas);
        drawText(canvas, 2, 0, " " + title + " ");
    }
}
