package dev.kaldiroglu.dp.structural.decorator.gof.visual.solution;

import dev.kaldiroglu.dp.structural.decorator.gof.visual.Canvas;

/**
 * The <b>Component</b> of the solution: the interface for objects that can have
 * responsibilities added to them dynamically (GoF p. 178).
 * <p>
 * GoF's implementation issue 3, "keeping Component classes lightweight" (p. 180), is
 * followed here: this type defines an interface and stores no data at all. Every
 * decorator pays for whatever a Component carries, so a fat Component makes decorators
 * too expensive to use in quantity.
 */
public interface VisualComponent {

    /** The width of the component, including anything wrapped around it. */
    int width();

    /** The height of the component, including anything wrapped around it. */
    int height();

    /** Draws the component with its top-left corner at (x, y). */
    void draw(Canvas canvas, int x, int y);

    /** Renders any component on a canvas of exactly its own size. */
    static String render(VisualComponent component) {
        Canvas canvas = new Canvas(component.width(), component.height());
        component.draw(canvas, 0, 0);
        return canvas.toString();
    }
}
