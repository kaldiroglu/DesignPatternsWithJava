package dev.kaldiroglu.dp.structural.decorator.gof.visual;

import dev.kaldiroglu.dp.structural.decorator.gof.visual.problem.BorderedScrolledTextView;
import dev.kaldiroglu.dp.structural.decorator.gof.visual.problem.ScrolledBorderedTextView;
import dev.kaldiroglu.dp.structural.decorator.gof.visual.solution.BorderDecorator;
import dev.kaldiroglu.dp.structural.decorator.gof.visual.solution.ScrollDecorator;
import dev.kaldiroglu.dp.structural.decorator.gof.visual.solution.TextView;
import dev.kaldiroglu.dp.structural.decorator.gof.visual.solution.VisualComponent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * The two designs are worth comparing only if they do the same thing. These tests prove
 * they produce identical output, so every remaining difference is a difference of design.
 */
class DesignComparisonTest {

    private static final String TEXT = "hello there";

    @Test
    @DisplayName("subclassing and decorating draw the same picture — scrollbar inside")
    void sameOutputScrollbarInside() {
        String bySubclassing = dev.kaldiroglu.dp.structural.decorator.gof.visual.problem.TextView
                .render(new BorderedScrolledTextView(5, 2, TEXT));
        String byDecorating = VisualComponent
                .render(new BorderDecorator(new ScrollDecorator(new TextView(5, 2, TEXT))));

        assertEquals(bySubclassing, byDecorating);
    }

    @Test
    @DisplayName("subclassing and decorating draw the same picture — scrollbar outside")
    void sameOutputScrollbarOutside() {
        String bySubclassing = dev.kaldiroglu.dp.structural.decorator.gof.visual.problem.TextView
                .render(new ScrolledBorderedTextView(5, 2, TEXT));
        String byDecorating = VisualComponent
                .render(new ScrollDecorator(new BorderDecorator(new TextView(5, 2, TEXT))));

        assertEquals(bySubclassing, byDecorating);
    }

    @Test
    @DisplayName("the difference is in what a third embellishment would cost")
    void costOfTheNextEmbellishment() {
        // problem package: TextView, BorderedTextView, ScrolledTextView,
        //                  BorderedScrolledTextView, ScrolledBorderedTextView = 5 classes
        //                  for 2 embellishments. Adding a shadow needs 11 more.
        // solution package: TextView, Decorator, BorderDecorator, ScrollDecorator
        //                  = 4 classes. Adding a shadow needs exactly 1 more, and every
        //                  combination and order of the three comes free.
        int problemClassesForTwo = 5;
        int solutionClassesForTwo = 4;
        int problemClassesForThree = 16; // 1 + sum over k of 3!/(3-k)! = 1 + 3 + 6 + 6
        int solutionClassesForThree = 5;

        assertEquals(11, problemClassesForThree - problemClassesForTwo);
        assertEquals(1, solutionClassesForThree - solutionClassesForTwo);
    }
}
