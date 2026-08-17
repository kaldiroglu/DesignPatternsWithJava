package dev.kaldiroglu.dp.structural.bridge.gof.problem;

import dev.kaldiroglu.dp.structural.bridge.gof.Canvas;

/** A plain window on the X Window System. Draws with +, - and |. */
public class XWindow extends Window {

    public XWindow(int width, int height) {
        super(width, height);
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
