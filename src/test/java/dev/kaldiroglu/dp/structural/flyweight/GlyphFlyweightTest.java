package dev.kaldiroglu.dp.structural.flyweight;

import dev.kaldiroglu.dp.structural.flyweight.gof.CharacterGlyph;
import dev.kaldiroglu.dp.structural.flyweight.gof.Column;
import dev.kaldiroglu.dp.structural.flyweight.gof.Font;
import dev.kaldiroglu.dp.structural.flyweight.gof.Glyph;
import dev.kaldiroglu.dp.structural.flyweight.gof.GlyphContext;
import dev.kaldiroglu.dp.structural.flyweight.gof.GlyphFactory;
import dev.kaldiroglu.dp.structural.flyweight.gof.Row;
import dev.kaldiroglu.dp.structural.flyweight.gof.Window;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The point of this class.
 * <p>
 * GoF's own example, and the one that shows the solution's least obvious half: the same
 * shared object rendered in two different fonts, because the font was never inside it.
 */
class GlyphFlyweightTest {

    private static final String LINE_ONE = "flyweight is a nice solution";
    private static final String LINE_TWO = "lightweight is also a nice solution";

    private record Built(Column document, GlyphFactory factory, int occurrences) {
    }

    private static Built document() {
        GlyphFactory factory = new GlyphFactory();
        Column document = factory.createColumn();
        int occurrences = 0;
        for (String line : new String[]{LINE_ONE, LINE_TWO}) {
            Row row = factory.createRow();
            for (char c : line.toCharArray()) {
                row.insert(factory.createCharacter(c));
                occurrences++;
            }
            document.insert(row);
        }
        return new Built(document, factory, occurrences);
    }

    @Test
    @DisplayName("sixty-three characters of text cost sixteen objects")
    void sharingIsMeasured() {
        Built built = document();

        assertEquals(63, built.occurrences(), "characters typed");
        assertEquals(16, built.factory().createdCharacterCount(), "objects allocated");
        assertEquals(47, built.occurrences() - built.factory().createdCharacterCount(),
                "occurrences that cost nothing");
    }

    @Test
    @DisplayName("the factory returns the identical object for a repeated letter")
    void theFactoryShares() {
        GlyphFactory factory = new GlyphFactory();

        CharacterGlyph first = factory.createCharacter('e');
        CharacterGlyph second = factory.createCharacter('e');

        assertSame(first, second);
        assertEquals(1, factory.createdCharacterCount());
    }

    @Test
    @DisplayName("rows and columns are unshared — each createRow is a new object")
    void unsharedConcreteFlyweightsAreNotPooled() {
        GlyphFactory factory = new GlyphFactory();

        Row one = factory.createRow();
        Row two = factory.createRow();

        assertFalse(one == two, "a row owns its children, so it cannot be shared");
        assertTrue(one instanceof Glyph, "and it is still a Glyph, which is what lets it "
                + "hold shared characters");
    }

    @Test
    @DisplayName("only the factory can create a character glyph")
    void theConstructorIsNotPublic() {
        Constructor<?>[] constructors = CharacterGlyph.class.getDeclaredConstructors();

        assertEquals(1, constructors.length);
        assertFalse(Modifier.isPublic(constructors[0].getModifiers()),
                "a public constructor would let a client defeat the sharing");
    }

    @Test
    @DisplayName("the flyweight stores no font — one object renders in two of them")
    void theSameObjectRendersInTwoFonts() {
        Built built = document();

        GlyphContext context = new GlyphContext(new Font("Helvetica"));
        context.reset();
        context.setFont(new Font("Times"), LINE_ONE.length());
        context.next(LINE_ONE.length());
        context.setFont(new Font("Courier"), LINE_TWO.length());

        Window window = new Window();
        context.reset();
        built.document().draw(window, context);

        // 'i' appears on both lines, and it is one object.
        var first = window.rendered().stream()
                .filter(r -> r.charcode() == 'i' && r.y() == 0).findFirst().orElseThrow();
        var second = window.rendered().stream()
                .filter(r -> r.charcode() == 'i' && r.y() == 1).findFirst().orElseThrow();

        assertEquals(new Font("Times"), first.font());
        assertEquals(new Font("Courier"), second.font());
        assertEquals(63, window.rendered().size(), "every occurrence was drawn");
    }

    @Test
    @DisplayName("no field of the flyweight can hold a font")
    void theFlyweightHasNoFontField() {
        boolean holdsAFont = Arrays.stream(CharacterGlyph.class.getDeclaredFields())
                .anyMatch(f -> f.getType() == Font.class);

        assertFalse(holdsAFont, "the font is extrinsic; storing it would end the sharing");
        assertEquals(1, Arrays.stream(CharacterGlyph.class.getDeclaredFields())
                .filter(f -> !f.isSynthetic()).count(), "one field: the character code");
    }

    @Test
    @DisplayName("the document renders back to the text that was typed")
    void theDocumentIsCorrect() {
        Built built = document();

        Window window = new Window();
        GlyphContext context = new GlyphContext(new Font("Helvetica"));
        built.document().draw(window, context);

        assertEquals(LINE_ONE + "\n" + LINE_TWO, window.text());
    }
}
