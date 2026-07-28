package dev.kaldiroglu.dp.structural.flyweight;

import dev.kaldiroglu.dp.structural.flyweight.book.Book;
import dev.kaldiroglu.dp.structural.flyweight.book.CharacterFactory;
import dev.kaldiroglu.dp.structural.flyweight.book.Line;
import dev.kaldiroglu.dp.structural.flyweight.book.Page;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.IdentityHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The point of this class.
 * <p>
 * The book example was originally written as an illustration of Flyweight and shared
 * nothing: its factory allocated on every call, and its Character stored the line and
 * position of one occurrence. These tests hold the corrected version to what the pattern
 * actually promises — as a count, not as an adjective — and each one would fail against
 * the code as it was.
 */
class BookFlyweightTest {

    // ------------------------------------------------------------------ the solution

    @Test
    @DisplayName("the corrected factory returns one object per distinct character")
    void solutionSharesByIntrinsicState() {
        CharacterFactory factory = new CharacterFactory();

        var first = factory.createCharacter('e', false);
        var second = factory.createCharacter('e', false);

        assertSame(first, second, "same intrinsic state, same object");
        assertEquals(2, factory.requestCount());
        assertEquals(1, factory.createdCount());
        assertEquals(1, factory.savedCount());
    }

    @Test
    @DisplayName("case is part of the key, because it changes what is rendered")
    void caseIsIntrinsic() {
        CharacterFactory factory = new CharacterFactory();

        var lower = factory.createCharacter('t', false);
        var upper = factory.createCharacter('t', true);

        assertNotSame(lower, upper, "they render differently, so they are different flyweights");
        assertEquals('t', lower.getValue());
        assertEquals('T', upper.getValue(), "and the capital is actually capitalized");
        assertEquals(2, factory.createdCount());
    }

    @Test
    @DisplayName("the corrected line refuses the character that would overflow it")
    void solutionLineRespectsItsCapacity() {
        CharacterFactory factory = new CharacterFactory();
        Line line = factory.createLine(3);

        assertTrue(line.add(factory.createCharacter('a', false)));
        assertTrue(line.add(factory.createCharacter('b', false)));
        assertTrue(line.add(factory.createCharacter('c', false)));
        assertFalse(line.add(factory.createCharacter('d', false)), "the fourth is refused");
        assertEquals(3, line.length());
        assertTrue(line.isFull());
    }

    @Test
    @DisplayName("a paragraph of 236 characters costs 29 objects")
    void aParagraphCostsFarFewerObjectsThanItHasCharacters() {
        CharacterFactory factory = new CharacterFactory();
        String[] text = {
                "Flyweight uses sharing to support large numbers of",
                "fine-grained objects efficiently. A flyweight is a",
                "shared object that can be used in multiple contexts",
                "simultaneously, and it cannot make assumptions about",
                "the context in which it operates.",
        };

        Book book = factory.createBook("Design Patterns", 1);
        Page page = factory.createPage(1, text.length);
        for (String content : text) {
            Line line = factory.createLine(content.length());
            for (char c : content.toCharArray()) {
                line.add(factory.createCharacter(java.lang.Character.toLowerCase(c),
                        java.lang.Character.isUpperCase(c)));
            }
            page.add(line);
        }
        book.add(page);

        assertEquals(236, book.characterCount(), "characters typed");
        assertEquals(236, factory.requestCount(), "requests made");
        assertEquals(29, factory.createdCount(), "objects allocated");
        assertEquals(207, factory.savedCount(), "occurrences that cost nothing");
    }

    @Test
    @DisplayName("the same object really is in two places at once")
    void oneObjectAppearsInManyPlaces() {
        CharacterFactory factory = new CharacterFactory();
        Line one = factory.createLine(20);
        Line two = factory.createLine(20);

        for (char c : "hello".toCharArray()) {
            one.add(factory.createCharacter(c, false));
        }
        for (char c : "held".toCharArray()) {
            two.add(factory.createCharacter(c, false));
        }

        assertSame(one.characterAt(1), two.characterAt(1), "the 'e' on both lines");
        assertSame(one.characterAt(0), two.characterAt(0), "the 'h' on both lines");

        Map<Object, Boolean> distinct = new IdentityHashMap<>();
        for (var c : one.getChars()) {
            distinct.put(c, Boolean.TRUE);
        }
        for (var c : two.getChars()) {
            distinct.put(c, Boolean.TRUE);
        }
        assertEquals(9, one.length() + two.length(), "nine occurrences");
        assertEquals(5, distinct.size(), "five objects: h e l o d");
    }
}
