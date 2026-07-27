package dev.kaldiroglu.dp.structural.flyweight.gof;

import java.util.ArrayList;
import java.util.List;

/**
 * UnsharedConcreteFlyweight (GoF, p. 198).
 *
 * <p>A {@code Row} groups child glyphs laid out left-to-right. Rows are NOT
 * shared: each one owns its list of children, so it carries state that cannot
 * be shared. Crucially, it still implements the {@link Glyph} interface, which
 * is exactly what lets a shared {@link CharacterGlyph} sit inside an unshared Row.</p>
 */
public class Row extends Glyph {

    private final List<Glyph> children = new ArrayList<>();

    @Override
    public void insert(Glyph glyph) {
        children.add(glyph);
    }

    @Override
    public Glyph child(int index) {
        return children.get(index);
    }

    @Override
    public int childCount() {
        return children.size();
    }

    @Override
    public void draw(Window window, GlyphContext context) {
        for (Glyph child : children) {
            child.draw(window, context);
        }
        window.newLine();
    }
}
