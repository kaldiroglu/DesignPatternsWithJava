package dev.kaldiroglu.dp.structural.bridge.gof.window.solution;

import dev.kaldiroglu.dp.structural.bridge.gof.window.Canvas;

/**
 * A <b>RefinedAbstraction</b> (GoF p. 154): extends what a window can do, and says
 * nothing whatever about any platform.
 * <p>
 * Compare with {@code problem.XIconWindow} and {@code problem.PMIconWindow}: this one
 * class replaces both of them, and would replace the third and fourth as well.
 */
public final class IconWindow extends Window {

    private final String label;

    public IconWindow(int width, int height, String label, WindowImp imp) {
        super(width, height, imp);
        this.label = label;
    }

    @Override
    public void drawContents(Canvas canvas) {
        drawBorder(canvas);
        drawRect(canvas, 2, 1, 3, 2);
        drawText(canvas, 6, 2, label);
    }
}
