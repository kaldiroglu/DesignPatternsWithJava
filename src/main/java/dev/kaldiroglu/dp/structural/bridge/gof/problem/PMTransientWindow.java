package dev.kaldiroglu.dp.structural.bridge.gof.problem;

import dev.kaldiroglu.dp.structural.bridge.gof.Canvas;

/** A transient window on Presentation Manager. The PM drawing code, for the third time. */
public class PMTransientWindow extends TransientWindow {

    public PMTransientWindow(int width, int height, String title) {
        super(width, height, title);
    }

    @Override
    public void drawRect(Canvas canvas, int x, int y, int w, int h) {
        canvas.rectangle(x, y, w, h, '#', '=', '!');
    }

    @Override
    public void drawText(Canvas canvas, int x, int y, String text) {
        canvas.text(x, y, text);
    }

    @Override
    public String platform() {
        return "PM";
    }
}
