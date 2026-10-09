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
    @DisplayName("undo puts the window back where it was saved, although x and length changed after")
    void undoRestoresTheSavedState() {
        GuiComponent window = new GuiComponent("window", 0, 0, 20, 10);
        window.setMemento(new GuiComponentMemento());
        window.saveState();
        window.setX(20);
        window.setLength(40);

        window.undo();

        assertEquals("GuiComponent [name=window, x=0, y=0, length=20, width=10]", window.toString());
    }

    @Test
    @DisplayName("the memento has public setState and getState, so anyone can read the window's state through it")
    void theMementoIsOpen() {
        List<String> publicMethods = Arrays.stream(GuiComponentMemento.class.getDeclaredMethods())
                .filter(m -> Modifier.isPublic(m.getModifiers()))
                .map(Method::getName)
                .sorted()
                .toList();
        assertEquals(List.of("getState", "setState"), publicMethods);
    }

    @Test
    @DisplayName("Test.main prints the window before, after the change, and after undo")
    void mainOutput() {
        assertEquals(List.of(
                "GuiComponent [name=window, x=0, y=0, length=20, width=10]",
                "GuiComponent [name=window, x=20, y=0, length=40, width=10]",
                "GuiComponent [name=window, x=0, y=0, length=20, width=10]"),
                by(() -> dev.kaldiroglu.dp.behavioral.memento.gui.Test.main(new String[0])));
    }
}
