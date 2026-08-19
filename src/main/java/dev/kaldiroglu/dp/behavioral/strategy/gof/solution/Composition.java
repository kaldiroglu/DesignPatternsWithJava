package dev.kaldiroglu.dp.behavioral.strategy.gof.solution;

import dev.kaldiroglu.dp.behavioral.strategy.gof.Component;
import dev.kaldiroglu.dp.behavioral.strategy.gof.Layout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The <b>Context</b>: GoF's {@code Composition}, which "maintains a reference to a
 * Compositor object" (p. 316).
 * <p>
 * Compare with {@code problem.Composition}. That class was a document that also typeset;
 * this one is a document that <em>asks</em>. The flag is gone, both algorithms are gone,
 * and what is left is the thing the class was always about: a list of components and the
 * width they have to fit into.
 * <p>
 * GoF, p. 316: "A composition maintains a collection of Component instances... When a
 * composition needs to reformat, it forwards this responsibility to its Compositor object."
 */
public final class Composition {

    private final List<Component> components = new ArrayList<>();
    private final int lineWidth;
    private Compositor compositor;

    public Composition(int lineWidth, Compositor compositor) {
        this.lineWidth = lineWidth;
        this.compositor = Objects.requireNonNull(compositor, "a composition needs a compositor");
    }

    public Composition insert(Component component) {
        components.add(component);
        return this;
    }

    /** Change the algorithm on a document that already exists, and reformat. */
    public void setCompositor(Compositor compositor) {
        this.compositor = Objects.requireNonNull(compositor);
    }

    public String compositorName() {
        return compositor.name();
    }

    /** GoF's {@code Repair()}: hand the work to whichever algorithm is in place. */
    public Layout repair() {
        return compositor.compose(List.copyOf(components), lineWidth);
    }
}
