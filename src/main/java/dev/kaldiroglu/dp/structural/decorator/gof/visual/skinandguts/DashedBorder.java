package dev.kaldiroglu.dp.structural.decorator.gof.visual.skinandguts;

import dev.kaldiroglu.dp.structural.decorator.gof.visual.Canvas;

/** A <b>ConcreteStrategy</b>: every other cell is left blank. */
public final class DashedBorder implements BorderStyle {

    @Override
    public void stroke(Canvas canvas, int x, int y, int width, int height) {
        for (int i = 1; i < width - 1; i++) {
            char c = i % 2 == 1 ? '-' : ' ';
            canvas.put(x + i, y, c);
            canvas.put(x + i, y + height - 1, c);
        }
        for (int i = 1; i < height - 1; i++) {
            char c = i % 2 == 1 ? '|' : ' ';
            canvas.put(x, y + i, c);
            canvas.put(x + width - 1, y + i, c);
        }
        canvas.put(x, y, '+');
        canvas.put(x + width - 1, y, '+');
        canvas.put(x, y + height - 1, '+');
        canvas.put(x + width - 1, y + height - 1, '+');
    }
}
