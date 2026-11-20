package dev.kaldiroglu.dp.behavioral.memento.gui;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

import static dev.kaldiroglu.dp.behavioral.memento.Methods.publicSettersOf;
import static dev.kaldiroglu.dp.behavioral.memento.Printed.by;
import static org.junit.jupiter.api.Assertions.*;

/** The window whose state is one object, kept by a memento. */
class GuiTest {

    @Test
    @DisplayName("restore puts the window back where it was saved, although x and length changed after")
    void restoreGoesBackToTheSavedState() {
        GuiComponent window = new GuiComponent("window", 0, 0, 20, 10);
        GuiComponent.Memento saved = window.createMemento();
        window.moveTo(20, 0);
        window.resize(40, 10);

        window.restore(saved);

        assertEquals("GuiComponent [name=window, x=0, y=0, length=20, width=10]", window.toString());
    }

    @Test
    @DisplayName("the caretaker can keep two mementos, and the window can go back to either")
    void twoMementos() {
        GuiComponent window = new GuiComponent("window", 0, 0, 20, 10);
        GuiComponent.Memento first = window.createMemento();
        window.moveTo(20, 0);
        GuiComponent.Memento second = window.createMemento();
        window.moveTo(40, 0);

        window.restore(first);
        assertEquals("GuiComponent [name=window, x=0, y=0, length=20, width=10]", window.toString());
        window.restore(second);
        assertEquals("GuiComponent [name=window, x=20, y=0, length=20, width=10]", window.toString());
    }

    @Test
    @DisplayName("the window has no public setters: it changes only through moveTo and resize")
    void noPublicSetters() {
        assertEquals(List.of(), publicSettersOf(GuiComponent.class));
        List<String> publicMethods = Arrays.stream(GuiComponent.class.getDeclaredMethods())
                .filter(m -> Modifier.isPublic(m.getModifiers()))
                .map(Method::getName)
                .sorted()
                .toList();
        assertEquals(List.of("createMemento", "getName", "moveTo", "resize", "restore", "toString"), publicMethods);
    }

    @Test
    @DisplayName("the window holds no memento: the caretaker keeps it")
    void theWindowHoldsNoMemento() {
        assertTrue(Arrays.stream(GuiComponent.class.getDeclaredFields())
                .noneMatch(f -> f.getType() == GuiComponent.Memento.class));
    }

    @Test
    @DisplayName("the memento is closed: a private field, private constructors, no methods; the state is a record")
    void theMementoIsClosed() {
        Class<GuiComponent.Memento> type = GuiComponent.Memento.class;
        Field[] fields = type.getDeclaredFields();
        assertEquals(List.of("state"), Arrays.stream(fields).map(Field::getName).toList());
        assertTrue(Modifier.isPrivate(fields[0].getModifiers()));
        assertEquals(GuiComponentState.class, fields[0].getType());
        for (Constructor<?> constructor : type.getDeclaredConstructors()) {
            assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        }
        assertEquals(0, Arrays.stream(type.getDeclaredMethods()).filter(m -> !m.isSynthetic()).count(),
                "no getState, no methods at all");
        assertTrue(GuiComponentState.class.isRecord());
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
