package dev.kaldiroglu.dp.structural.flyweight.gof;

import java.util.HashMap;
import java.util.Map;

/**
 * Holds the EXTRINSIC state of the flyweights (GoF, p. 202).
 *
 * <p>In the book this maps each glyph's position to its font using a compact
 * <em>BTree</em>. Here we use the simpler substitute the authors mention: a
 * plain {@code index -> Font} map. The client keeps this object and passes it
 * into {@link Glyph#draw}, so the shared {@link CharacterGlyph} flyweights never have
 * to store a font themselves.</p>
 */
public class GlyphContext {

    private int index = 0;
    private final Map<Integer, Font> fonts = new HashMap<>();
    private final Font defaultFont;

    public GlyphContext(Font defaultFont) {
        this.defaultFont = defaultFont;
    }

    /** Advances the position cursor by one glyph. */
    public void next() {
        next(1);
    }

    /** Advances the position cursor by {@code step} glyphs. */
    public void next(int step) {
        index += step;
    }

    /**
     * Assigns {@code font} to the next {@code span} positions starting at the
     * current index. This is the simplified stand-in for GoF's BTree range
     * insertion.
     */
    public void setFont(Font font, int span) {
        for (int i = 0; i < span; i++) {
            fonts.put(index + i, font);
        }
    }

    /** Returns the font active at the current position. */
    public Font getFont() {
        return fonts.getOrDefault(index, defaultFont);
    }

    /** Rewinds to the start of the document before traversing / drawing. */
    public void reset() {
        index = 0;
    }
}
