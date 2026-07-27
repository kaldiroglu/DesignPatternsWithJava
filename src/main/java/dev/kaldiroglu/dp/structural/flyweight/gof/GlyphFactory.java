package dev.kaldiroglu.dp.structural.flyweight.gof;

import java.util.HashMap;
import java.util.Map;

/**
 * FlyweightFactory (GoF, p. 203).
 *
 * <p>Guarantees that {@link CharacterGlyph} flyweights are shared: a request for
 * a letter already in the pool returns the existing instance instead of
 * allocating a new one. {@link Row}s and {@link Column}s are unshared, so the
 * factory simply creates a fresh one each time.</p>
 */
public class GlyphFactory {

    // Key is java.lang.Character (the boxed char code); value is our flyweight.
    private final Map<Character, CharacterGlyph> characters = new HashMap<>();

    /** Returns the shared flyweight for {@code c}, creating it on first use. */
    public CharacterGlyph createCharacter(char c) {
        CharacterGlyph glyph = characters.get(c);
        if (glyph == null) { // not pooled yet -> create it once and share it
            glyph = new CharacterGlyph(c);
            characters.put(c, glyph);
        }
        return glyph;
    }

    public Row createRow() {
        return new Row();
    }

    public Column createColumn() {
        return new Column();
    }

    /** Number of distinct {@code CharacterGlyph} flyweights actually allocated. */
    public int createdCharacterCount() {
        return characters.size();
    }
}
