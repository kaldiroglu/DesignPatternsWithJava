package dev.kaldiroglu.dp.structural.proxy.gof;

/**
 * Subject role of the Proxy pattern (GoF, "Design Patterns", p. 207).
 *
 * <p>A graphical element that can appear in a document. Both the real
 * {@link Image} and its stand-in {@link ImageProxy} implement this interface,
 * so the document editor works with them interchangeably and cannot tell one
 * from the other.</p>
 */
public interface Graphic {

    /** Renders the graphic at the given position. */
    void draw(Point at);

    /** Returns the bounding size, used by the editor for page layout. */
    Extent getExtent();

    /** Persists the graphic to storage. */
    void store();

    /** Restores the graphic from storage. */
    void load();
}
