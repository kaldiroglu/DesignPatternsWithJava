package dev.kaldiroglu.dp.structural.composite.gof.graphics;

import java.util.List;

/**
 * Component role of the Composite pattern (GoF, "Design Patterns", p. 163).
 *
 * <p>The abstraction for all drawable objects in a graphics editor. Both
 * primitive graphics ({@link Line}, {@link Rectangle}, {@link Text}) and
 * containers of graphics ({@link Picture}) are {@code Graphic}s, so client code
 * can treat a single line and a whole drawing in exactly the same way.</p>
 *
 * <p><b>Design decision (GoF, "Declaring the child management operations",
 * p. 168):</b> the child operations {@code add}, {@code remove} and
 * {@code getChild} are declared here, in the Component, rather than only in
 * {@link Picture}. That is the book's own choice, and it favors
 * <em>transparency</em> — clients never need to know whether they hold a leaf or
 * a composite — at the cost of <em>safety</em>: asking a leaf to add a child is
 * meaningless, so the default implementation fails. Subclasses that really are
 * containers override these operations.</p>
 */
public abstract class Graphic {

    /** Renders this graphic at the given position. */
    public abstract void draw(Point at);

    // --- Child management: meaningful for composites, an error for leaves ----

    /**
     * Adds a child graphic.
     *
     * @throws UnsupportedOperationException by default, because a primitive
     *         graphic has no children. {@link Picture} overrides this.
     */
    public void add(Graphic child) {
        throw new UnsupportedOperationException(
                getClass().getSimpleName() + " is a leaf and cannot contain children");
    }

    /**
     * Removes a child graphic.
     *
     * @throws UnsupportedOperationException by default, for the same reason as
     *         {@link #add(Graphic)}.
     */
    public void remove(Graphic child) {
        throw new UnsupportedOperationException(
                getClass().getSimpleName() + " is a leaf and cannot contain children");
    }

    /**
     * Returns the child at {@code index}.
     *
     * @throws UnsupportedOperationException by default, for the same reason as
     *         {@link #add(Graphic)}.
     */
    public Graphic getChild(int index) {
        throw new UnsupportedOperationException(
                getClass().getSimpleName() + " is a leaf and has no children");
    }

    /**
     * Returns this graphic's children, empty for a leaf.
     *
     * <p>Unlike {@link #add(Graphic)}, a <em>read-only</em> view of the children
     * is harmless to answer for a leaf, so it has a sensible default. Recursive
     * traversals in client code can rely on it without type tests.</p>
     */
    public List<Graphic> children() {
        return List.of();
    }

    /**
     * Answers whether this graphic can contain children.
     *
     * <p>GoF ("Maximizing the Component interface", p. 167) notes that a client
     * sometimes genuinely needs to know. Offering this query is cheaper and
     * safer than letting clients test the concrete type.</p>
     */
    public boolean isComposite() {
        return false;
    }
}
