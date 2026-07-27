package dev.kaldiroglu.dp.structural.bridge.gof.window.solution;

import dev.kaldiroglu.dp.structural.bridge.gof.window.Canvas;

/**
 * The <b>Implementor</b> (GoF p. 154): the interface for window implementations.
 * <p>
 * Read the method names. They are <em>device primitives</em> — the smallest operations a
 * windowing system can be asked for — not window operations. GoF is explicit about this
 * (p. 153):
 * <blockquote>
 * "WindowImp declares an interface that provides access to the low-level primitives that
 * the underlying window system supplies. The Implementor interface provides only
 * primitive operations, and Abstraction defines higher-level operations based on these
 * primitives."
 * </blockquote>
 * That split is the pattern. If this interface grew a {@code drawIcon()} method it would
 * stop being an implementor and start being a second copy of the abstraction.
 */
public interface WindowImp {

    /** The name of the windowing system behind this implementation. */
    String platform();

    void deviceRect(Canvas canvas, int x, int y, int width, int height);

    void deviceText(Canvas canvas, int x, int y, String text);

    /** Bring the window to the front. Recorded rather than drawn, so tests can see it. */
    void deviceRaise();

    void deviceLower();

    /** What the platform has been asked to do, in order. */
    java.util.List<String> journal();
}
