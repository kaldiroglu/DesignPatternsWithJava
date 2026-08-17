package dev.kaldiroglu.dp.structural.bridge.gof.solution;

import dev.kaldiroglu.dp.structural.bridge.gof.Canvas;

import java.util.ArrayList;
import java.util.List;

/**
 * A <b>ConcreteImplementor</b>: the X Window System (GoF p. 154).
 * <p>
 * It knows about pixels and rectangles. It has never heard of icons, dialogs or titles,
 * and it will never need to change when a new kind of window is invented.
 */
public final class XWindowImp implements WindowImp {

    private final List<String> journal = new ArrayList<>();

    @Override
    public String platform() {
        return "X";
    }

    @Override
    public void deviceRect(Canvas canvas, int x, int y, int width, int height) {
        journal.add("XDrawRectangle(" + x + "," + y + "," + width + "," + height + ")");
        canvas.rectangle(x, y, width, height, '+', '-', '|');
    }

    @Override
    public void deviceText(Canvas canvas, int x, int y, String text) {
        journal.add("XDrawString(" + x + "," + y + ",\"" + text + "\")");
        canvas.text(x, y, text);
    }

    @Override
    public void deviceRaise() {
        journal.add("XRaiseWindow()");
    }

    @Override
    public void deviceLower() {
        journal.add("XLowerWindow()");
    }

    @Override
    public List<String> journal() {
        return List.copyOf(journal);
    }
}
