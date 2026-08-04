package dev.kaldiroglu.dp.structural.composite.graphic;

import java.util.Collection;

/**
 * Child management, kept off {@link Graphic} on purpose.
 * <p>
 * GoF, implementation issue 4 (p. 167): declaring {@code add} and {@code remove} on the
 * Component buys <em>transparency</em> — every element looks alike — at the cost of
 * <em>safety</em>, because adding a child to a leaf is then a call that compiles and fails at
 * run time. Declaring it here instead buys safety and costs transparency.
 * <p>
 * Neither answer is wrong. This package takes safety; {@code composite.bom} takes
 * transparency, so the two can be compared side by side.
 */
public interface CompositeGraphic {

    void addGraphic(Graphic graphic);

    void removeGraphic(Graphic graphic);

    Collection<Graphic> getGraphics();

    /** Prints the tree, indenting one level per depth. */
    void listGraphic();
}
