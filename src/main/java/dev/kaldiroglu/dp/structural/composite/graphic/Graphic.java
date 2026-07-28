package dev.kaldiroglu.dp.structural.composite.graphic;

/**
 * The Component: everything on the canvas can do these, leaf or not.
 * <p>
 * Child management is deliberately <em>not</em> here — see {@link CompositeGraphic}. That is
 * GoF's implementation issue 1, and this package takes the <strong>safe</strong> side of it:
 * you cannot call {@code addGraphic} on something that has no children, because the compiler
 * will not let you name the method. The price is that a client wanting to build a tree must
 * hold a {@code CompositeGraphic}, not a {@code Graphic} — so the cast in
 * {@link Main} is not sloppiness, it is the bill for the safety.
 */
public interface Graphic {

    void draw();

    void erase();

    void paint();

    /** How many drawable shapes this is, counting recursively. A leaf is one. */
    int shapeCount();
}
