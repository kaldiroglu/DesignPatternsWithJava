package dev.kaldiroglu.dp.structural.flyweight.gof;

import java.util.ArrayList;
import java.util.List;

/**
 * UnsharedConcreteFlyweight (GoF, p. 198).
 *
 * <p>A {@code Column} stacks child glyphs (typically {@link Row}s) top-to-bottom.
 * Like {@link Row} it owns its children and is therefore not shared.</p>
 */
public class Column extends Glyph {

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
    }
}
