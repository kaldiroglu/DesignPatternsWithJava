package dev.kaldiroglu.dp.structural.flyweight.gof;

/**
 * Created by Claude Opus 4.8 on June 22nd 2026
 * Client demo (GoF, p. 204).
 *
 * <p>Builds a small two-line document out of shared {@link CharacterGlyph}
 * flyweights, assigns fonts per position range via a {@link GlyphContext}
 * (extrinsic state), renders it, and then reports how many character objects
 * were saved by sharing.</p>
 */
public class Main {

    public static void main(String[] args) {
        GlyphFactory factory = new GlyphFactory();

        String[] lines = {"flyweight is a nice pattern", "lightweight is also a nice pattern"};

        // Build the document tree: a Column of Rows of (shared) Characters.
        Column document = factory.createColumn();
        int occurrences = 0;
        for (String line : lines) {
            Row row = factory.createRow();
            for (char c : line.toCharArray()) {
                row.insert(factory.createCharacter(c)); // <- sharing happens here
                occurrences++;
            }
            document.insert(row);
        }

        // Extrinsic state: line 1 in Times, line 2 in Courier.
        GlyphContext context = new GlyphContext(new Font("Helvetica"));
        context.reset();
        context.setFont(new Font("Times"), lines[0].length());
        context.next(lines[0].length());
        context.setFont(new Font("Courier"), lines[1].length());

        // Render the document.
        Window window = new Window();
        context.reset();
        document.draw(window, context);

        System.out.println("Rendered document:");
        System.out.println(window.text());
        System.out.println();
        System.out.println("Per-glyph rendering (char @ position : font):");
        for (Window.Rendered r : window.rendered()) {
            System.out.printf("  '%c' @ (%d,%d) : %s%n", r.charcode(), r.x(), r.y(), r.font());
        }
        System.out.println();
        System.out.println("Character occurrences in document : " + occurrences);
        System.out.println("Distinct Character flyweights     : " + factory.createdCharacterCount());
        System.out.println("Objects saved by sharing          : "
                + (occurrences - factory.createdCharacterCount()));
    }
}
