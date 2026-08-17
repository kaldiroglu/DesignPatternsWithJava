package dev.kaldiroglu.dp.structural.flyweight;

import dev.kaldiroglu.dp.structural.flyweight.book.correct.Book;
import dev.kaldiroglu.dp.structural.flyweight.book.correct.CharacterFactory;
import dev.kaldiroglu.dp.structural.flyweight.book.correct.Line;
import dev.kaldiroglu.dp.structural.flyweight.book.correct.Page;
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
 * Both versions of the book example are in the repository, and this class tests both. The
 * first four tests pin the defects in {@code book.wrong} so they cannot be quietly fixed
 * in the wrong place; the rest hold {@code book.correct} to what the solution actually
 * promises — as a count, not as an adjective.
 */
class BookFlyweightTest {

    // ------------------------------------------------------------------ the problem

    @Test
    @DisplayName("the original factory allocates a new object for every character")
    void originalFactorySharesNothing() {
        var factory = new dev.kaldiroglu.dp.structural.flyweight.book.wrong.BookFactory();

        var first = factory.createCharacter('e', false);
        var second = factory.createCharacter('e', false);

        assertNotSame(first, second, "a FlyweightFactory that always allocates is a constructor");
    }

    @Test
    @DisplayName("the original stores extrinsic state on the character, so it cannot be shared")
    void originalStoresPositionInsideTheFlyweight() {
        var factory = new dev.kaldiroglu.dp.structural.flyweight.book.wrong.BookFactory();
        var line = factory.createLine(10);

        var shared = factory.createCharacter('o', false);
        line.add(shared);          // position 0
        line.add(shared);          // the same object, added again

        // Both occurrences are the same object, so the second write clobbered the first.
        // This is what makes the original unshareable, and it is why fixing only the
        // factory would introduce a bug rather than the solution.
        assertEquals(1, shared.getPosition(),
                "one object cannot remember two positions");
        assertEquals(2, line.getChars().size(), "yet the line believes it holds two");
    }

    @Test
    @DisplayName("the original records upperCase and then renders lower case")
    void originalIgnoresItsOwnIntrinsicState() {
        var factory = new dev.kaldiroglu.dp.structural.flyweight.book.wrong.BookFactory();
        var capital = factory.createCharacter('t', true);

        assertTrue(capital.isUpperCase(), "it was asked for as a capital");
        assertEquals('t', capital.getValue(), "and it renders as lower case anyway");
    }

    @Test
    @DisplayName("the original line accepts one character more than it was built for")
    void originalLineIsOffByOne() {
        var factory = new dev.kaldiroglu.dp.structural.flyweight.book.wrong.BookFactory();
        var line = factory.createLine(3);

        for (int i = 0; i < 4; i++) {
            assertTrue(line.add(factory.createCharacter('x', false)), "accepted #" + (i + 1));
        }
        assertEquals(4, line.getChars().size(), "a line built for 3 holds 4");
    }

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
