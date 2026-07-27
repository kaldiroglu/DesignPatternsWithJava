package dev.kaldiroglu.dp.structural.proxy.gof;

import java.util.ArrayList;
import java.util.List;

/**
 * Client role of the Proxy pattern (GoF, p. 207) — a small stand-in for the
 * book's document editor.
 *
 * <p>A document is a sequence of {@link Graphic}s. Laying out the page only
 * needs each graphic's {@link Extent}; drawing the page is what finally forces
 * the images to load. Everything happens through the {@link Graphic} interface,
 * so the document never knows whether an element is a real image or a proxy.</p>
 */
public class TextDocument {

    private final List<Graphic> graphics = new ArrayList<>();

    public void insert(Graphic graphic) {
        graphics.add(graphic);
    }

    /** Stacks graphics vertically and returns the total page extent. */
    public Extent layoutExtent() {
        int width = 0;
        int height = 0;
        for (Graphic g : graphics) {
            Extent e = g.getExtent();
            width = Math.max(width, e.width());
            height += e.height();
        }
        return new Extent(width, height);
    }

    /** Draws every graphic, stacked from top to bottom starting at {@code at}. */
    public void draw(Point at) {
        int y = at.y();
        for (Graphic g : graphics) {
            g.draw(new Point(at.x(), y));
            y += g.getExtent().height();
        }
    }

    public int size() {
        return graphics.size();
    }
}
