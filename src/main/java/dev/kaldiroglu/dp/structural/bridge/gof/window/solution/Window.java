package dev.kaldiroglu.dp.structural.bridge.gof.window.solution;

import dev.kaldiroglu.dp.structural.bridge.gof.window.Canvas;

import java.util.Objects;

/**
 * The <b>Abstraction</b> (GoF p. 154): what a window IS, to the rest of the program.
 * <p>
 * The one field is the whole pattern. This class does not extend a platform — it
 * <em>holds</em> one, and it can be handed a different one at any time. GoF calls the
 * reference {@code imp}, and this code keeps the name.
 * <p>
 * Every operation here is written in terms of the implementor's primitives, never in
 * terms of any particular platform. That is what lets the two hierarchies below and
 * beside it grow without ever meeting.
 */
public class Window {

    private final int width;
    private final int height;
    private WindowImp imp;

    public Window(int width, int height, WindowImp imp) {
        this.width = width;
        this.height = height;
        this.imp = Objects.requireNonNull(imp, "a window needs an implementation");
    }

    /** The implementation currently in use. Subclasses draw through it. */
    protected final WindowImp imp() {
        return imp;
    }

    /**
     * Swap the implementation on a window that already exists.
     * <p>
     * Nothing in the {@code problem} package can do this at any price: there, the platform
     * is the object's class.
     */
    public void setImp(WindowImp newImp) {
        this.imp = Objects.requireNonNull(newImp);
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    public String platform() {
        return imp.platform();
    }

    // --- higher-level operations, defined in terms of the primitives ----------------

    public void drawRect(Canvas canvas, int x, int y, int w, int h) {
        imp.deviceRect(canvas, x, y, w, h);
    }

    public void drawText(Canvas canvas, int x, int y, String text) {
        imp.deviceText(canvas, x, y, text);
    }

    public void drawBorder(Canvas canvas) {
        drawRect(canvas, 0, 0, width, height);
    }

    public void raiseWindow() {
        imp.deviceRaise();
    }

    public void lowerWindow() {
        imp.deviceLower();
    }

    /** What this kind of window shows. Refined abstractions override it. */
    public void drawContents(Canvas canvas) {
        drawBorder(canvas);
    }

    public static String render(Window window) {
        Canvas canvas = new Canvas(window.width(), window.height());
        window.drawContents(canvas);
        return canvas.toString();
    }
}
