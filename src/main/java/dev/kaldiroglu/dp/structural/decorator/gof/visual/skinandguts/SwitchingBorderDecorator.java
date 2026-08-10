package dev.kaldiroglu.dp.structural.decorator.gof.visual.skinandguts;

import dev.kaldiroglu.dp.structural.decorator.gof.visual.Canvas;
import dev.kaldiroglu.dp.structural.decorator.gof.visual.solution.Decorator;
import dev.kaldiroglu.dp.structural.decorator.gof.visual.solution.VisualComponent;

/**
 * A border decorator that decides how to draw with a branch. It works, and it is the
 * design most people write first.
 * <p>
 * As a <em>decorator</em> it is correct: the component it wraps still knows nothing about
 * borders. The problem is one level down. Every border style this class will ever support
 * has to be named in {@link Style} and handled in {@link #draw}, so a fourth style is an
 * edit to a class the other three already depend on — and every edit risks all of them.
 * <p>
 * Compare {@link StyledBorderDecorator}, which asks a {@link BorderStyle} instead.
 */
public final class SwitchingBorderDecorator extends Decorator {

    /** The closed vocabulary. A new style cannot be added without changing this file. */
    public enum Style { SOLID, DASHED, THICK }

    private final Style style;

    public SwitchingBorderDecorator(VisualComponent component, Style style) {
        super(component);
        this.style = style;
    }

    @Override
    public int width() {
        return component().width() + 2;
    }

    @Override
    public int height() {
        return component().height() + 2;
    }

    @Override
    public void draw(Canvas canvas, int x, int y) {
        component().draw(canvas, x + 1, y + 1);

        int width = width();
        int height = height();

        if (style == Style.SOLID) {
            canvas.rectangle(x, y, width, height);

        } else if (style == Style.DASHED) {
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

        } else if (style == Style.THICK) {
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
}
