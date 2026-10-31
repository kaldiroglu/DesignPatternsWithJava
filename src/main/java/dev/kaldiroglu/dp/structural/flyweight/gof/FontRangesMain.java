package dev.kaldiroglu.dp.structural.flyweight.gof;

/**
 * Created by Claude Opus 4.8 on June 22nd 2026
 * Client demo (GoF, p. 204).
 *
 * <p>Assigns fonts per position range via a {@link GlyphContext} (extrinsic state), renders
 * the document, and prints the font of every character.</p>
 */
public class FontRangesMain {

    public static void main(String[] args) {
        SampleDocument sample = new SampleDocument();
        String[] lines = sample.lines;

        // Extrinsic state: line 1 in Times, line 2 in Courier.
        GlyphContext context = new GlyphContext(new Font("Helvetica"));
        context.reset();
        context.setFont(new Font("Times"), lines[0].length());
        context.next(lines[0].length());
        context.setFont(new Font("Courier"), lines[1].length());

        // Render the document.
        Window window = new Window();
        context.reset();
        sample.document.draw(window, context);

        System.out.println("Rendered document:");
        System.out.println(window.text());
        System.out.println();
        System.out.println("Per-glyph rendering (char @ position : font):");
        for (Window.Rendered r : window.rendered()) {
            System.out.printf("  '%c' @ (%d,%d) : %s%n", r.charcode(), r.x(), r.y(), r.font());
        }
    }
}
