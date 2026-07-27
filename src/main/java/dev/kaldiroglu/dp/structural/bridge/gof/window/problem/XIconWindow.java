package dev.kaldiroglu.dp.structural.bridge.gof.window.problem;

import dev.kaldiroglu.dp.structural.bridge.gof.window.Canvas;

/**
 * An icon window on X.
 * <p>
 * Note what is in this class: nothing about icons. It inherits the icon behavior from
 * {@link IconWindow}, and then <em>copies the X drawing code out of {@link XWindow}</em>,
 * because Java has no way to inherit from both. That copy is the whole problem, and it
 * happens once per (window kind x platform) pair.
 */
public class XIconWindow extends IconWindow {

    public XIconWindow(int width, int height, String label) {
        super(width, height, label);
    }

    // Copied, character for character, from XWindow.
    @Override
    public void drawRect(Canvas canvas, int x, int y, int w, int h) {
        canvas.rectangle(x, y, w, h, '+', '-', '|');
    }

    @Override
    public void drawText(Canvas canvas, int x, int y, String text) {
        canvas.text(x, y, text);
    }

    @Override
    public String platform() {
        return "X";
    }
}
