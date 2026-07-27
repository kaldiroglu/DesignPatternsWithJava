package dev.kaldiroglu.dp.structural.bridge.gof.window.problem;

import dev.kaldiroglu.dp.structural.bridge.gof.window.Canvas;

/** A transient window on X. The X drawing code, for the third time. */
public class XTransientWindow extends TransientWindow {

    public XTransientWindow(int width, int height, String title) {
        super(width, height, title);
    }

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
