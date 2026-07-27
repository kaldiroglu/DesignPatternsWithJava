package dev.kaldiroglu.dp.structural.flyweight.gof;

import java.util.ArrayList;
import java.util.List;

/**
 * The drawing surface (a stand-in for the on-screen window).
 *
 * <p>It tracks a simple {@code (x, y)} cursor as the extrinsic <em>position</em>
 * is realised during drawing, and records every rendered character together
 * with the font that was active at that point.</p>
 */
public class Window {

    /** One rendered glyph: which character, in which font, at which position. */
    public record Rendered(char charcode, Font font, int x, int y) {
    }

    private final List<Rendered> rendered = new ArrayList<>();
    private int x = 0;
    private int y = 0;

    public void draw(char charcode, Font font) {
        rendered.add(new Rendered(charcode, font, x, y));
        x++;
    }

    public void newLine() {
        x = 0;
        y++;
    }

    public List<Rendered> rendered() {
        return rendered;
    }

    /** Reconstructs the plain text that was drawn, for display in the demo. */
    public String text() {
        StringBuilder sb = new StringBuilder();
        int line = 0;
        for (Rendered r : rendered) {
            while (line < r.y()) {
                sb.append('\n');
                line++;
            }
            sb.append(r.charcode());
        }
        return sb.toString();
    }
}
