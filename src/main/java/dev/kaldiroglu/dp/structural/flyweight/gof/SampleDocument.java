package dev.kaldiroglu.dp.structural.flyweight.gof;

/**
 * Created by Claude Opus 4.8 on June 22nd 2026
 * The small two-line document both runnable classes use (GoF, p. 204).
 *
 * <p>It is built out of shared {@link CharacterGlyph} flyweights, and it counts the
 * characters typed so that a run can compare that count with the objects allocated.</p>
 */
final class SampleDocument {

    final String[] lines = {"flyweight is a nice solution", "lightweight is also a nice solution"};
    final GlyphFactory factory = new GlyphFactory();
    final Column document = factory.createColumn();
    final int occurrences;

    SampleDocument() {
        // Build the document tree: a Column of Rows of (shared) Characters.
        int typed = 0;
        for (String line : lines) {
            Row row = factory.createRow();
            for (char c : line.toCharArray()) {
                row.insert(factory.createCharacter(c)); // <- sharing happens here
                typed++;
            }
            document.insert(row);
        }
        occurrences = typed;
    }
}
