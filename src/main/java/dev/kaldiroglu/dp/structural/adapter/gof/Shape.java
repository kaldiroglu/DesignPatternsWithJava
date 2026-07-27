package dev.kaldiroglu.dp.structural.adapter.gof;

/**
 * <b>Target</b> participant (GoF p. 141 / Sample Code p. 146).
 *
 * <p>This is the interface the drawing-editor {@link DrawingEditor client} knows how to use.
 * Every graphical object the editor manages&nbsp;&ndash; lines, polygons, text&nbsp;&ndash;
 * is manipulated through {@code Shape}, so the client never needs to know an object's concrete
 * type.</p>
 */
public interface Shape {

    /** @return the smallest rectangle that encloses this pluggable. */
    BoundingBox boundingBox();

    /** @return a manipulator that animates this pluggable in response to user input. */
    Manipulator createManipulator();

    /** @return {@code true} if the pluggable currently has no visible content. */
    boolean isEmpty();
}
