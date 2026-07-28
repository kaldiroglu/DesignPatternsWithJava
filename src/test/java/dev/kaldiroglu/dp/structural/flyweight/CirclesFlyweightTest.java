package dev.kaldiroglu.dp.structural.flyweight;

import dev.kaldiroglu.dp.structural.flyweight.circles.solution.Circle;
import dev.kaldiroglu.dp.structural.flyweight.circles.solution.CircleField;
import dev.kaldiroglu.dp.structural.flyweight.circles.solution.CircleStyle;
import dev.kaldiroglu.dp.structural.flyweight.circles.solution.CircleStyleFactory;
import dev.kaldiroglu.dp.structural.flyweight.circles.solution.Point;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.awt.Color;
import java.lang.reflect.Modifier;
import java.util.Arrays;
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
 * The circles demo was written as an illustration of Flyweight and shares nothing. These
 * tests hold the original's defects in place and measure what the corrected version saves.
 * Nothing here needs a display: the interesting claim has nothing to do with a window.
 */
class CirclesFlyweightTest {

    // ------------------------------------------------------------------ the problem

    @Test
    @DisplayName("the original factory allocates a new circle for every request")
    void originalSharesNothing() {
        var factory = dev.kaldiroglu.dp.structural.flyweight.circles.problem
                .CircleFactory.getInstance();
        factory.setWidthAndHeight(100, 100);

        var first = factory.create();
        var second = factory.create();

        assertNotSame(first, second, "a thousand circles are a thousand objects");
    }

    @Test
    @DisplayName("the original stores every piece of state on the circle, and mutably")
    void originalHasNoIntrinsicExtrinsicSplit() {
        Class<?> circle = dev.kaldiroglu.dp.structural.flyweight.circles.problem.Circle.class;

        long instanceFields = Arrays.stream(circle.getDeclaredFields())
                .filter(f -> !Modifier.isStatic(f.getModifiers()) && !f.isSynthetic())
                .count();
        long setters = Arrays.stream(circle.getDeclaredMethods())
                .filter(m -> m.getName().startsWith("set"))
                .count();

        assertEquals(4, instanceFields, "canvas, center, color, radius — all on the object");
        assertEquals(3, setters, "and three of them can be changed under a holder");
    }

    @Test
    @DisplayName("painting the original asks for another paint, forever")
    void originalRepaintsItself() throws Exception {
        // paintComponent -> showUp() -> canvas.repaint() -> paintComponent -> ...
        var canvas = dev.kaldiroglu.dp.structural.flyweight.circles.problem.CirclesCanvas.class;
        var circle = dev.kaldiroglu.dp.structural.flyweight.circles.problem.Circle.class;

        assertTrue(Arrays.stream(canvas.getDeclaredMethods())
                .anyMatch(m -> m.getName().equals("paintComponent")));
        assertTrue(Arrays.stream(circle.getDeclaredMethods())
                .anyMatch(m -> m.getName().equals("showUp")),
                "and showUp is what paintComponent calls on every circle");
    }

    // ------------------------------------------------------------------ the solution

    @Test
    @DisplayName("the corrected factory returns one style per distinct look")
    void solutionSharesStyles() {
        CircleStyleFactory factory = new CircleStyleFactory();

        CircleStyle first = factory.getStyle(30, Color.RED);
        CircleStyle second = factory.getStyle(30, Color.RED);

        assertSame(first, second);
        assertEquals(2, factory.requestCount());
        assertEquals(1, factory.distinctStyles());
        assertEquals(1, factory.sharedCount());
    }

    @Test
    @DisplayName("radius and color are both in the key, because both change the look")
    void theKeyCoversTheWholeLook() {
        CircleStyleFactory factory = new CircleStyleFactory();

        factory.getStyle(30, Color.RED);
        factory.getStyle(50, Color.RED);
        factory.getStyle(30, Color.BLUE);
        factory.getStyle(30, Color.RED);

        assertEquals(3, factory.distinctStyles());
    }

    @Test
    @DisplayName("ten thousand circles need at most a hundred style objects")
    void tenThousandCirclesShareAHundredStyles() {
        CircleField field = new CircleField(800, 1000, 20260728L);
        field.populate(10_000);

        assertEquals(10_000, field.size());
        assertEquals(100, CircleField.possibleStyles(), "ten radii, ten colors");
        assertTrue(field.distinctStyles() <= 100,
                "however many circles, the styles cannot exceed the ways to look");
        assertEquals(10_000 - field.distinctStyles(), field.sharedCount());

        Map<CircleStyle, Boolean> distinct = new IdentityHashMap<>();
        for (int i = 0; i < field.size(); i++) {
            distinct.put(field.circle(i).style(), Boolean.TRUE);
        }
        assertEquals(field.distinctStyles(), distinct.size(),
                "and the objects on the canvas are those same styles");
    }

    @Test
    @DisplayName("moving every circle allocates no style at all")
    void animationCostsNothing() {
        CircleField field = new CircleField(800, 1000, 20260728L);
        field.populate(5_000);

        int stylesBefore = field.distinctStyles();
        Point positionBefore = field.circle(0).center();
        CircleStyle styleBefore = field.circle(0).style();

        for (int frame = 0; frame < 60; frame++) {
            field.scatter();
        }

        assertEquals(stylesBefore, field.distinctStyles(), "sixty frames, no new styles");
        assertSame(styleBefore, field.circle(0).style(), "the same style object throughout");
        assertNotSame(positionBefore, field.circle(0).center(), "only the position moved");
    }

    @Test
    @DisplayName("the style holds no position, so it can be in two places at once")
    void theStyleHoldsNoPosition() {
        boolean holdsAPoint = Arrays.stream(CircleStyle.class.getDeclaredFields())
                .anyMatch(f -> f.getType() == Point.class);
        assertFalse(holdsAPoint, "a shared style with a position could only be one circle");

        CircleStyleFactory factory = new CircleStyleFactory();
        CircleStyle style = factory.getStyle(20, Color.GREEN);
        Circle here = new Circle(new Point(10, 10), style);
        Circle there = new Circle(new Point(700, 900), style);

        assertSame(here.style(), there.style(), "one object, two places, at the same instant");
    }

    @Test
    @DisplayName("the corrected style is immutable")
    void theFlyweightIsImmutable() {
        boolean allFinal = Arrays.stream(CircleStyle.class.getDeclaredFields())
                .filter(f -> !f.isSynthetic())
                .allMatch(f -> Modifier.isFinal(f.getModifiers()));
        boolean hasSetters = Arrays.stream(CircleStyle.class.getDeclaredMethods())
                .anyMatch(m -> m.getName().startsWith("set"));

        assertTrue(allFinal, "immutability is what makes concurrent sharing safe");
        assertFalse(hasSetters);
    }
}
