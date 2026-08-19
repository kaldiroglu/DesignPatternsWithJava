package dev.kaldiroglu.dp.behavioral.strategy.gof;

import dev.kaldiroglu.dp.behavioral.strategy.gof.solution.ArrayCompositor;
import dev.kaldiroglu.dp.behavioral.strategy.gof.solution.Composition;
import dev.kaldiroglu.dp.behavioral.strategy.gof.solution.Compositor;
import dev.kaldiroglu.dp.behavioral.strategy.gof.solution.SimpleCompositor;
import dev.kaldiroglu.dp.behavioral.strategy.gof.solution.TeXCompositor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The point of this class.
 * <p>
 * GoF's own example (Design Patterns, pp. 315-316): a document that has to break text into
 * lines, and the several algorithms for doing it. Every figure the slides quote about the
 * compositors is measured here.
 * <p>
 * The paragraph is GoF's own motivation, set in a 26-column measure — a width at which the
 * two text algorithms genuinely disagree. At most widths they do not, which is worth knowing
 * before choosing an example: a demonstration that only works on one input is not a
 * demonstration.
 */
class CompositorTest {

    private static final String TEXT =
            "A document editor breaks a stream of text into lines "
            + "and there are many algorithms for it";
    private static final int WIDTH = 26;

    private static List<Component> paragraph() {
        List<Component> components = new ArrayList<>();
        for (String word : TEXT.split(" ")) {
            components.add(Component.word(word));
        }
        return List.copyOf(components);
    }

    private static Composition documentWith(Compositor compositor) {
        Composition document = new Composition(WIDTH, compositor);
        paragraph().forEach(document::insert);
        return document;
    }

    @Test
    @DisplayName("the same paragraph, two algorithms, two different sets of lines")
    void twoAlgorithmsOneParagraph() {
        Layout simple = documentWith(new SimpleCompositor()).repair();
        Layout tex = documentWith(new TeXCompositor()).repair();

        // Same number of lines. The difference is where they fall, which is the whole
        // argument for having more than one algorithm.
        assertEquals(4, simple.lineCount());
        assertEquals(4, tex.lineCount());
        assertNotEquals(simple.render(), tex.render());

        assertEquals(List.of("A document editor breaks a", "stream of text into lines",
                "and there are many", "algorithms for it"), simple.render());
        assertEquals(List.of("A document editor breaks", "a stream of text into",
                "lines and there are many", "algorithms for it"), tex.render());
    }

    @Test
    @DisplayName("TeX reads the whole paragraph, so it leaves a smaller worst gap")
    void texEvensTheLinesOut() {
        Layout simple = documentWith(new SimpleCompositor()).repair();
        Layout tex = documentWith(new TeXCompositor()).repair();

        // Greedy filling packs the first lines and strands the rest; the global pass spreads
        // the slack. Measured, not described.
        assertEquals(8, simple.worstSlack());
        assertEquals(5, tex.worstSlack());
        assertTrue(tex.worstSlack() < simple.worstSlack());

        for (Layout layout : List.of(simple, tex)) {
            for (int i = 0; i < layout.lineCount(); i++) {
                assertTrue(layout.widthOf(i) <= WIDTH, "line " + i + " overflowed the measure");
            }
        }
    }

    @Test
    @DisplayName("the algorithm can be replaced on a document that already exists")
    void theCompositorIsAField() {
        Composition document = documentWith(new SimpleCompositor());
        assertEquals("SimpleCompositor", document.compositorName());
        List<String> before = document.repair().render();

        document.setCompositor(new TeXCompositor());     // the same document

        assertEquals("TeXCompositor", document.compositorName());
        assertNotEquals(before, document.repair().render());
    }

    @Test
    @DisplayName("ArrayCompositor ignores the measure entirely, and is still a Compositor")
    void theThirdAlgorithmIsNothingLikeTheOthers() {
        Layout rows = documentWith(new ArrayCompositor(6)).repair();

        // It counts components, not columns. Six to a row whatever they are, and the first
        // row comes out 33 wide in a 26-column measure — something neither text algorithm
        // would ever produce. An interface designed around "fit text to a width" could not
        // have held this class.
        assertEquals(6, rows.lines().getFirst().size());
        assertEquals(33, rows.widthOf(0));
        assertTrue(rows.widthOf(0) > WIDTH);
        assertTrue(Compositor.class.isAssignableFrom(ArrayCompositor.class));
    }

    @Test
    @DisplayName("the context forwards one call; the flag that chose the algorithm is gone")
    void theContextOnlyForwards() {
        Class<?> naive = dev.kaldiroglu.dp.behavioral.strategy.gof.problem.Composition.class;

        // The naive class took a boolean to pick between two hard-wired algorithms.
        assertTrue(Arrays.stream(naive.getDeclaredConstructors()[0].getParameterTypes())
                .anyMatch(t -> t == boolean.class), "the flag that chooses the algorithm");

        // The context takes a Compositor instead, and holds no boolean at all.
        assertTrue(Arrays.stream(Composition.class.getDeclaredConstructors()[0].getParameterTypes())
                .anyMatch(t -> t == Compositor.class));
        assertTrue(Arrays.stream(Composition.class.getDeclaredFields())
                .noneMatch(f -> f.getType() == boolean.class));
    }

    @Test
    @DisplayName("both designs lay the same paragraph out the same way")
    void theDesignsAgree() {
        var fast = new dev.kaldiroglu.dp.behavioral.strategy.gof.problem
                .Composition(paragraph(), WIDTH, false).repair();
        var quality = new dev.kaldiroglu.dp.behavioral.strategy.gof.problem
                .Composition(paragraph(), WIDTH, true).repair();

        assertEquals(fast.render(), documentWith(new SimpleCompositor()).repair().render());
        assertEquals(quality.render(), documentWith(new TeXCompositor()).repair().render());
    }

    @Test
    @DisplayName("a row of no components is a mistake, and says so")
    void arrayCompositorRejectsZero() {
        assertThrows(IllegalArgumentException.class, () -> new ArrayCompositor(0));
    }
}
