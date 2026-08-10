package dev.kaldiroglu.dp.structural.decorator.gof.visual.skinandguts;

import dev.kaldiroglu.dp.structural.decorator.gof.visual.Canvas;
import dev.kaldiroglu.dp.structural.decorator.gof.visual.solution.Decorator;
import dev.kaldiroglu.dp.structural.decorator.gof.visual.solution.VisualComponent;

import java.util.Objects;

/**
 * The same border, with the styling decision moved into a {@link BorderStyle} object.
 * <p>
 * Both patterns are present, doing different jobs — which is the point of GoF
 * implementation issue 4, <i>changing the skin of an object versus changing its guts</i>
 * (p. 180):
 * <ul>
 *   <li><b>Decorator, the skin.</b> This class wraps a {@link VisualComponent}. The
 *       component is unaware of it, was not designed for it, and does not need to be.</li>
 *   <li><b>Strategy, the guts.</b> This class <em>was</em> designed with a hook, and holds
 *       the object that fills it. Adding a fourth border style is a new class; nothing
 *       here changes and nothing here is retested.</li>
 * </ul>
 * The rule of thumb follows from the two bullets: use a decorator when the object must not
 * know, and a strategy when it is yours to design and the variation is one decision inside
 * it. GoF add that Strategy is the better answer "where the Component class is
 * intrinsically heavyweight", because a decorator pays for the whole Component interface
 * and a strategy pays only for the hook.
 * <p>
 * Note what is <em>not</em> in {@link #draw}: a branch. This class names no concrete style,
 * which {@code SkinAndGutsTest} asserts by reflection.
 */
public final class StyledBorderDecorator extends Decorator {

    private final BorderStyle style;

    public StyledBorderDecorator(VisualComponent component, BorderStyle style) {
        super(component);
        this.style = Objects.requireNonNull(style, "a border needs a style");
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
        component().draw(canvas, x + 1, y + 1);  // the skin: forward to what we wrap
        style.stroke(canvas, x, y, width(), height());  // the guts: ask, do not decide
    }
}
