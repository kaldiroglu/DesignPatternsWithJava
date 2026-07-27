package dev.kaldiroglu.dp.structural.flyweight.gof;

import java.util.Objects;

/**
 * A simple value object describing a font.
 *
 * <p>In Lexi the font is part of the EXTRINSIC state held by a
 * {@link GlyphContext} (GoF, p. 202), never by the {@link CharacterGlyph} flyweight.
 * That is why the same shared Character can be rendered in many different
 * fonts.</p>
 */
public final class Font {

    private final String name;

    public Font(String name) {
        this.name = name;
    }

    public String name() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Font other)) return false;
        return Objects.equals(name, other.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
