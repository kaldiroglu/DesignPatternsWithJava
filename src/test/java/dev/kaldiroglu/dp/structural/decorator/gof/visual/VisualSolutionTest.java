package dev.kaldiroglu.dp.structural.decorator.gof.visual;

import dev.kaldiroglu.dp.structural.decorator.gof.visual.solution.BorderDecorator;
import dev.kaldiroglu.dp.structural.decorator.gof.visual.solution.ScrollDecorator;
import dev.kaldiroglu.dp.structural.decorator.gof.visual.solution.TextView;
import dev.kaldiroglu.dp.structural.decorator.gof.visual.solution.VisualComponent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class VisualSolutionTest {

    private static final String TEXT = "hello there";

    private VisualComponent text() {
        return new TextView(5, 2, TEXT);
    }

    @Test
    @DisplayName("a decorator alone adds exactly one responsibility")
    void singleDecorator() {
        assertEquals("""
                +-----+
                |hello|
                |there|
                +-----+""", VisualComponent.render(new BorderDecorator(text())));

        assertEquals("""
                hello^
                therev""", VisualComponent.render(new ScrollDecorator(text())));
    }

    @Test
    @DisplayName("the same two decorators in two orders give two pictures — no new classes")
    void orderIsChosenAtTheCallSite() {
        assertEquals("""
                +------+
                |hello^|
                |therev|
                +------+""", VisualComponent.render(new BorderDecorator(new ScrollDecorator(text()))));

        assertEquals("""
                +-----+^
                |hello|#
                |there|#
                +-----+v""", VisualComponent.render(new ScrollDecorator(new BorderDecorator(text()))));
    }

    @Test
    @DisplayName("GoF Consequence 1: a responsibility can be added twice")
    void aPropertyTwice() {
        assertEquals("""
                +-------+
                |+-----+|
                ||hello||
                ||there||
                |+-----+|
                +-------+""", VisualComponent.render(new BorderDecorator(new BorderDecorator(text()))));
    }

    @Test
    @DisplayName("responsibilities are attached at run time, to an object that already exists")
    void decorationHappensAtRunTime() {
        VisualComponent component = text();

        // The decision is made here, by the caller, with the object in hand — not by
        // whoever chose a class name months ago.
        boolean userWantsScrolling = true;
        VisualComponent shown = userWantsScrolling ? new ScrollDecorator(component) : component;

        assertEquals(6, shown.width());
        assertEquals(5, component.width()); // the original is untouched
    }

    @Test
    @DisplayName("GoF Consequence 3: a decorated component is not identical to the component")
    void identityIsNotPreserved() {
        VisualComponent component = text();
        VisualComponent decorated = new BorderDecorator(component);

        assertNotSame(component, decorated);
        // Anything that keys off object identity — a cache, an == check, a HashSet —
        // will treat these as two different things, because they are.
    }

    @Test
    @DisplayName("a decorator must decorate something")
    void nullComponentIsRejected() {
        assertThrows(NullPointerException.class, () -> new BorderDecorator(null));
    }
}
