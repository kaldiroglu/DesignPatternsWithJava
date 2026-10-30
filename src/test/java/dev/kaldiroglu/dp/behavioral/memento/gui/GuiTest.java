package dev.kaldiroglu.dp.behavioral.memento.gui;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

import static dev.kaldiroglu.dp.behavioral.memento.Printed.by;
import static org.junit.jupiter.api.Assertions.*;

/** The window whose state is one object, kept by a memento. */
class GuiTest {

    @Test
    @DisplayName("restore puts the window back where it was saved, although x and length changed after")
    void restoreGoesBackToTheSavedState() {
        GuiComponent window = new GuiComponent("window", 0, 0, 20, 10);
        GuiComponentMemento saved = window.createMemento();
        window.setX(20);
        window.setLength(40);

        window.restore(saved);

        assertEquals("GuiComponent [name=window, x=0, y=0, length=20, width=10]", window.toString());
    }

    @Test
    @DisplayName("the caretaker can keep two mementos, and the window can go back to either")
    void twoMementos() {
        GuiComponent window = new GuiComponent("window", 0, 0, 20, 10);
        GuiComponentMemento first = window.createMemento();
        window.setX(20);
        GuiComponentMemento second = window.createMemento();
        window.setX(40);

        window.restore(first);
        assertEquals(0, window.getX());
        window.restore(second);
        assertEquals(20, window.getX());
    }

    @Test
    @DisplayName("the window holds no memento: the caretaker keeps it")
    void theWindowHoldsNoMemento() {
        assertTrue(Arrays.stream(GuiComponent.class.getDeclaredFields())
                .noneMatch(f -> f.getType() == GuiComponentMemento.class));
    }

    @Test
    @DisplayName("a saved state cannot be changed, but anyone can read it through public getState")
    void theMementoIsReadable() {
        assertTrue(GuiComponentState.class.isRecord());
        List<String> publicMethods = Arrays.stream(GuiComponentMemento.class.getDeclaredMethods())
                .filter(m -> Modifier.isPublic(m.getModifiers()))
                .map(Method::getName)
                .toList();
        assertEquals(List.of("getState"), publicMethods);
    }

    @Test
    @DisplayName("Main prints the window before, after the change, and after undo")
    void mainOutput() {
        assertEquals(List.of(
                "GuiComponent [name=window, x=0, y=0, length=20, width=10]",
                "GuiComponent [name=window, x=20, y=0, length=40, width=10]",
                "GuiComponent [name=window, x=0, y=0, length=20, width=10]"),
                by(() -> Main.main(new String[0])));
    }
}
