package dev.kaldiroglu.dp.structural.decorator.gof.visual.solution;

import dev.kaldiroglu.dp.structural.decorator.gof.visual.Canvas;

import java.util.Objects;

/**
 * The <b>Decorator</b>: it maintains a reference to a Component object and defines an
 * interface that conforms to Component's interface (GoF p. 178).
 * <p>
 * Two properties make the whole pattern work, and both are visible in this small class:
 * <ol>
 *   <li>it <em>is</em> a {@link VisualComponent}, so a decorated object can be used
 *       anywhere an undecorated one can — including inside another decorator; and</li>
 *   <li>it <em>has</em> a {@link VisualComponent}, and forwards to it, so by default it
 *       changes nothing.</li>
 * </ol>
 * Subclasses override a method, do their own work, and forward. Everything they do not
 * override keeps working, which is why a decorator stays small no matter how large the
 * Component interface grows.
 */
public abstract class Decorator implements VisualComponent {

    private final VisualComponent component;

    protected Decorator(VisualComponent component) {
        this.component = Objects.requireNonNull(component, "a decorator must decorate something");
    }

    /** The wrapped component. Subclasses draw it, then add their own embellishment. */
    protected final VisualComponent component() {
        return component;
    }

    @Override
    public int width() {
        return component.width();
    }

    @Override
    public int height() {
        return component.height();
    }

    @Override
    public void draw(Canvas canvas, int x, int y) {
        component.draw(canvas, x, y);
    }
}
