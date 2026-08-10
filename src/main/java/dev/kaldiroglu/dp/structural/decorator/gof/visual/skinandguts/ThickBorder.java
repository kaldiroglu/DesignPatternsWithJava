package dev.kaldiroglu.dp.structural.decorator.gof.visual.skinandguts;

import dev.kaldiroglu.dp.structural.decorator.gof.visual.Canvas;

/** A <b>ConcreteStrategy</b>: one character, all the way round. */
public final class ThickBorder implements BorderStyle {

    @Override
    public void stroke(Canvas canvas, int x, int y, int width, int height) {
        for (int i = 0; i < width; i++) {
            canvas.put(x + i, y, '#');
            canvas.put(x + i, y + height - 1, '#');
        }
        for (int i = 0; i < height; i++) {
            canvas.put(x, y + i, '#');
            canvas.put(x + width - 1, y + i, '#');
        }
    }
}
