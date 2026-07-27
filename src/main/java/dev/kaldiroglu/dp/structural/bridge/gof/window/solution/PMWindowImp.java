package dev.kaldiroglu.dp.structural.bridge.gof.window.solution;

import dev.kaldiroglu.dp.structural.bridge.gof.window.Canvas;

import java.util.ArrayList;
import java.util.List;

/**
 * A <b>ConcreteImplementor</b>: IBM's Presentation Manager (GoF p. 154).
 * <p>
 * GoF's own version is worth remembering: PM has no single call that draws a rectangle,
 * so {@code deviceRect} has to build one out of lines. The abstraction never learns this.
 */
public final class PMWindowImp implements WindowImp {

    private final List<String> journal = new ArrayList<>();

    @Override
    public String platform() {
        return "PM";
    }

    @Override
    public void deviceRect(Canvas canvas, int x, int y, int width, int height) {
        // PM draws a rectangle as a polyline of four points — GoF, p. 157.
        journal.add("GpiBeginPath()");
        journal.add("GpiPolyLine(4 points)");
        journal.add("GpiEndPath()");
        canvas.rectangle(x, y, width, height, '#', '=', '!');
    }

    @Override
    public void deviceText(Canvas canvas, int x, int y, String text) {
        journal.add("GpiCharStringAt(" + x + "," + y + ",\"" + text + "\")");
        canvas.text(x, y, text);
    }

    @Override
    public void deviceRaise() {
        journal.add("WinSetWindowPos(HWND_TOP)");
    }

    @Override
    public void deviceLower() {
        journal.add("WinSetWindowPos(HWND_BOTTOM)");
    }

    @Override
    public List<String> journal() {
        return List.copyOf(journal);
    }
}
