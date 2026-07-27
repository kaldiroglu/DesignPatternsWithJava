package dev.kaldiroglu.dp.structural.flyweight.gof;

/**
 * Flyweight (GoF, p. 198; sample code p. 200).
 *
 * <p>Declares the interface through which flyweights can receive and act on
 * <em>extrinsic</em> state. In the Lexi editor every visible element of a
 * document is a {@code Glyph}.</p>
 *
 * <p>Note that GoF folds the Composite pattern into the same hierarchy: a Glyph
 * also declares child-management operations so that {@link Row}s and
 * {@link Column}s can contain other glyphs. Leaf flyweights ({@link CharacterGlyph})
 * simply ignore them.</p>
 */
public abstract class Glyph {

    /**
     * Renders this glyph. The {@code context} supplies the EXTRINSIC state
     * (here: the font that applies at the current position), which is never
     * stored inside the shared flyweight.
     */
    public abstract void draw(Window window, GlyphContext context);

    // --- Composite operations: default no-ops, overridden by Row / Column ---

    /** Leaf glyphs cannot contain children, so the default does nothing. */
    public void insert(Glyph glyph) {
        // no-op for leaves
    }

    public Glyph child(int index) {
        return null;
    }

    public int childCount() {
        return 0;
    }
}
