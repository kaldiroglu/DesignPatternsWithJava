package dev.kaldiroglu.dp.structural.bridge.gof.window.problem;

import dev.kaldiroglu.dp.structural.bridge.gof.window.Canvas;

/**
 * A window, in the design GoF start from: one hierarchy, and the platform is a subclass
 * of it (Design Patterns, p. 151).
 * <p>
 * The shared, platform-independent work lives here. The platform-specific drawing is left
 * abstract, so each platform provides it — which is a perfectly reasonable first design,
 * and works, right up until a second reason to subclass arrives.
 */
public abstract class Window {

    private final int width;
    private final int height;

    protected Window(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    // --- platform-specific: every platform must supply these ------------------------

    public abstract void drawRect(Canvas canvas, int x, int y, int w, int h);

    public abstract void drawText(Canvas canvas, int x, int y, String text);

    /** The name of the windowing system this window is tied to. */
    public abstract String platform();

    // --- platform-independent: written once, in terms of the operations above -------

    public void drawBorder(Canvas canvas) {
        drawRect(canvas, 0, 0, width, height);
    }

    /** What this <em>kind</em> of window shows. Overridden by the window kinds below. */
    public void drawContents(Canvas canvas) {
        drawBorder(canvas);
    }

    public static String render(Window window) {
        Canvas canvas = new Canvas(window.width(), window.height());
        window.drawContents(canvas);
        return canvas.toString();
    }
}
