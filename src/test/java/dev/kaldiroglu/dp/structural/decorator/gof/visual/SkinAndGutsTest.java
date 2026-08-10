package dev.kaldiroglu.dp.structural.decorator.gof.visual;

import dev.kaldiroglu.dp.structural.decorator.gof.visual.skinandguts.BorderStyle;
import dev.kaldiroglu.dp.structural.decorator.gof.visual.skinandguts.DashedBorder;
import dev.kaldiroglu.dp.structural.decorator.gof.visual.skinandguts.SolidBorder;
import dev.kaldiroglu.dp.structural.decorator.gof.visual.skinandguts.StyledBorderDecorator;
import dev.kaldiroglu.dp.structural.decorator.gof.visual.skinandguts.SwitchingBorderDecorator;
import dev.kaldiroglu.dp.structural.decorator.gof.visual.skinandguts.ThickBorder;
import dev.kaldiroglu.dp.structural.decorator.gof.visual.solution.TextView;
import dev.kaldiroglu.dp.structural.decorator.gof.visual.solution.VisualComponent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * GoF implementation issue 4, "changing the skin of an object versus changing its guts"
 * (p. 180), on the book's own border example.
 * <p>
 * Both designs draw the same borders. What differs is what a fourth style costs.
 */
class SkinAndGutsTest {

    private static TextView view() {
        return new TextView(5, 1, "hello");
    }

    @Test
    @DisplayName("the two designs draw exactly the same thing, for every style")
    void bothDesignsAgree() {
        assertEquals(
                VisualComponent.render(new SwitchingBorderDecorator(
                        view(), SwitchingBorderDecorator.Style.SOLID)),
                VisualComponent.render(new StyledBorderDecorator(view(), new SolidBorder())));

        assertEquals(
                VisualComponent.render(new SwitchingBorderDecorator(
                        view(), SwitchingBorderDecorator.Style.DASHED)),
                VisualComponent.render(new StyledBorderDecorator(view(), new DashedBorder())));

        assertEquals(
                VisualComponent.render(new SwitchingBorderDecorator(
                        view(), SwitchingBorderDecorator.Style.THICK)),
                VisualComponent.render(new StyledBorderDecorator(view(), new ThickBorder())));
    }

    @Test
    @DisplayName("the three styles really are different pictures")
    void theStylesDiffer() {
        String solid = VisualComponent.render(new StyledBorderDecorator(view(), new SolidBorder()));
        String dashed = VisualComponent.render(new StyledBorderDecorator(view(), new DashedBorder()));
        String thick = VisualComponent.render(new StyledBorderDecorator(view(), new ThickBorder()));

        assertEquals("+-----+\n|hello|\n+-----+", solid);
        assertEquals("+- - -+\n|hello|\n+- - -+", dashed);
        assertEquals("#######\n#hello#\n#######", thick);

        assertEquals(3, Stream.of(solid, dashed, thick).distinct().count());
    }

    @Test
    @DisplayName("a fourth style is a new class: StyledBorderDecorator is not touched")
    void aNewStyleNeedsNoEditToTheDecorator() {
        // Written here, in the test, without changing one line of the main source tree.
        BorderStyle dotted = (canvas, x, y, width, height) -> {
            for (int i = 0; i < width; i++) {
                canvas.put(x + i, y, '.');
                canvas.put(x + i, y + height - 1, '.');
            }
            for (int i = 0; i < height; i++) {
                canvas.put(x, y + i, '.');
                canvas.put(x + width - 1, y + i, '.');
            }
        };

        assertEquals(".......\n.hello.\n.......",
                VisualComponent.render(new StyledBorderDecorator(view(), dotted)));

        // The switching design has no equivalent: its vocabulary is closed at three, and a
        // fourth means editing the enum and the branch that reads it.
        assertEquals(3, SwitchingBorderDecorator.Style.values().length);
    }

    @Test
    @DisplayName("StyledBorderDecorator names no concrete style; the switching one names its own")
    void theDecoratorDependsOnlyOnTheAbstraction() {
        assertTrue(referencedTypes(StyledBorderDecorator.class)
                        .noneMatch(t -> t.equals(SolidBorder.class)
                                || t.equals(DashedBorder.class)
                                || t.equals(ThickBorder.class)),
                "StyledBorderDecorator must depend on BorderStyle only");

        assertTrue(referencedTypes(StyledBorderDecorator.class).anyMatch(BorderStyle.class::equals),
                "it holds the abstraction");

        assertTrue(referencedTypes(SwitchingBorderDecorator.class)
                        .anyMatch(SwitchingBorderDecorator.Style.class::equals),
                "the switching design, by contrast, is coupled to its own closed enum");
    }

    /** Field and parameter types declared by a class — enough to see what it is coupled to. */
    private static Stream<Class<?>> referencedTypes(Class<?> type) {
        return Stream.concat(
                Arrays.stream(type.getDeclaredFields()).map(Field::getType),
                Stream.concat(
                        Arrays.stream(type.getDeclaredConstructors())
                                .flatMap(c -> Arrays.stream(c.getParameterTypes())),
                        Arrays.stream(type.getDeclaredMethods())
                                .flatMap(m -> Arrays.stream(m.getParameterTypes()))));
    }

    @Test
    @DisplayName("the component is still unaware: the skin half is unchanged")
    void theWrappedComponentKnowsNothing() {
        assertTrue(Arrays.stream(TextView.class.getDeclaredFields())
                        .map(Field::getType)
                        .noneMatch(t -> t.getPackageName().endsWith("skinandguts")),
                "TextView must not know borders or styles exist");
        assertTrue(Arrays.stream(TextView.class.getDeclaredMethods())
                        .map(Method::getReturnType)
                        .noneMatch(t -> t.getPackageName().endsWith("skinandguts")),
                "nor mention them in its signatures");
    }
}
