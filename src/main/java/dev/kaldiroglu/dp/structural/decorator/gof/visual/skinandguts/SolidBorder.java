package dev.kaldiroglu.dp.structural.decorator.gof.visual.skinandguts;

import dev.kaldiroglu.dp.structural.decorator.gof.visual.Canvas;

/** A <b>ConcreteStrategy</b>: an unbroken outline, {@code + - |}. */
public final class SolidBorder implements BorderStyle {

    @Override
    public void stroke(Canvas canvas, int x, int y, int width, int height) {
        canvas.rectangle(x, y, width, height);
    }
}
