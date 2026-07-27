package dev.kaldiroglu.dp.structural.decorator.gof.visual;

import dev.kaldiroglu.dp.structural.decorator.gof.visual.problem.BorderedScrolledTextView;
import dev.kaldiroglu.dp.structural.decorator.gof.visual.problem.BorderedTextView;
import dev.kaldiroglu.dp.structural.decorator.gof.visual.problem.ScrolledBorderedTextView;
import dev.kaldiroglu.dp.structural.decorator.gof.visual.problem.ScrolledTextView;
import dev.kaldiroglu.dp.structural.decorator.gof.visual.problem.TextView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The subclassing design works. These tests prove it works, and then measure what it
 * costs — which is the only honest way to argue for a pattern.
 */
class VisualProblemTest {

    private static final String TEXT = "hello there";

    @Test
    @DisplayName("each subclass draws what its name promises")
    void subclassesDrawCorrectly() {
        assertEquals("""
                +-----+
                |hello|
                |there|
                +-----+""", TextView.render(new BorderedTextView(5, 2, TEXT)));

        assertEquals("""
                hello^
                therev""", TextView.render(new ScrolledTextView(5, 2, TEXT)));
    }

    @Test
    @DisplayName("two embellishments in two orders need two more subclasses, and they look different")
    void orderNeedsItsOwnSubclass() {
        String scrollbarInside = TextView.render(new BorderedScrolledTextView(5, 2, TEXT));
        String scrollbarOutside = TextView.render(new ScrolledBorderedTextView(5, 2, TEXT));

        assertEquals("""
                +------+
                |hello^|
                |therev|
                +------+""", scrollbarInside);

        assertEquals("""
                +-----+^
                |hello|#
                |there|#
                +-----+v""", scrollbarOutside);
    }

    @Test
    @DisplayName("an embellishment cannot be added to an object that already exists")
    void embellishmentIsFixedAtConstruction() {
        TextView plain = new TextView(5, 2, TEXT);

        // There is no operation on `plain` that can give it a border. The only way to get
        // one is to build a different object of a different class, losing any state the
        // original had. The choice of embellishment is made at compile time by whoever
        // writes the `new`, not at run time by whoever knows what the user wants.
        TextView bordered = new BorderedTextView(5, 2, TEXT);

        assertTrue(plain.width() < bordered.width());
        assertEquals(5, plain.width());
    }

    @Test
    @DisplayName("the scrollbar is implemented three times")
    void scrollbarIsDuplicated() {
        // Every one of these draws its own scrollbar from its own copy of the code.
        // A change to how a scrollbar looks is a change in three files, and the compiler
        // will not tell you if you miss one.
        String a = TextView.render(new ScrolledTextView(5, 2, TEXT));
        String b = TextView.render(new BorderedScrolledTextView(5, 2, TEXT));
        String c = TextView.render(new ScrolledBorderedTextView(5, 2, TEXT));

        assertTrue(a.contains("^") && b.contains("^") && c.contains("^"));
    }
}
