package dev.kaldiroglu.dp.structural.bridge.gof.window.problem;

import dev.kaldiroglu.dp.structural.bridge.gof.window.Canvas;

/** An icon window on Presentation Manager. The same copy, from the other platform. */
public class PMIconWindow extends IconWindow {

    public PMIconWindow(int width, int height, String label) {
        super(width, height, label);
    }

    // Copied, character for character, from PMWindow.
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
