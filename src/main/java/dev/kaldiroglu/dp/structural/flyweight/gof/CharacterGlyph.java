package dev.kaldiroglu.dp.structural.flyweight.gof;

/**
 * ConcreteFlyweight (GoF, p. 201).
 *
 * <p>GoF names this participant {@code Character}; we call it
 * {@code CharacterGlyph} to avoid clashing with {@link Character}.</p>
 *
 * <p>Stores the only INTRINSIC state a character needs: its character code.
 * Because that state is independent of where the character appears, a single
 * {@code CharacterGlyph} instance can be shared by every occurrence of that
 * letter in the whole document.</p>
 *
 * <p>The class is immutable and its constructor is package-private: instances
 * are created and pooled only by {@link GlyphFactory}, which is what makes
 * sharing safe.</p>
 */
public final class CharacterGlyph extends Glyph {

    private final char charcode; // intrinsic, immutable -> safely shareable

    CharacterGlyph(char charcode) { // package-private: only the factory creates these
        this.charcode = charcode;
    }

    @Override
    public void draw(Window window, GlyphContext context) {
        // Extrinsic state (the font) is fetched from the context, not stored here.
        Font font = context.getFont();
        window.draw(charcode, font);
        context.next(); // advance the context to the next position
    }

    public char charcode() {
        return charcode;
    }
}
