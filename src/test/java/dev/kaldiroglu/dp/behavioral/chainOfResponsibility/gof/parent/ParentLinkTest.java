package dev.kaldiroglu.dp.behavioral.chainOfResponsibility.gof.parent;

import dev.kaldiroglu.dp.behavioral.chainOfResponsibility.gof.solution.Application;
import dev.kaldiroglu.dp.behavioral.chainOfResponsibility.gof.solution.Button;
import dev.kaldiroglu.dp.behavioral.chainOfResponsibility.gof.solution.Dialog;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import static dev.kaldiroglu.dp.behavioral.chainOfResponsibility.Printed.by;
import static dev.kaldiroglu.dp.behavioral.chainOfResponsibility.Printed.codeOf;
import static dev.kaldiroglu.dp.behavioral.chainOfResponsibility.Printed.countOf;
import static org.junit.jupiter.api.Assertions.*;

/** The help chain made from the parent field the window already has. */
class ParentLinkTest {

    private static final String EDITOR = "this is the editor. Press F1 on any control.";
    private static final String PRINT_DIALOG = "the print dialog lets you choose a printer.";

    @Test
    @DisplayName("the three buttons get the same help as with a successor field")
    void sameAnswersAsTheSuccessorField() {
        Widget editor = new Widget(null, "editor", EDITOR);
        Widget printDialog = new Widget(editor, "print dialog", PRINT_DIALOG);
        List<String> parent = List.of(
                new Widget(printDialog, "print button", "print the document.").handleHelp(),
                new Widget(printDialog, "OK button").handleHelp(),
                new Widget(new Widget(editor, "font dialog"), "font button").handleHelp());

        Application app = new Application(EDITOR);
        Dialog dialog = new Dialog(app, PRINT_DIALOG);
        List<String> successor = List.of(
                new Button(dialog, "print the document.").handleHelp(),
                new Button(dialog).handleHelp(),
                new Button(new Dialog(app, null)).handleHelp());

        assertEquals(successor, parent);
    }

    @Test
    @DisplayName("the only field that holds another widget is parent")
    void oneLinkField() {
        List<String> links = Arrays.stream(Widget.class.getDeclaredFields())
                .filter(f -> f.getType() == Widget.class)
                .map(Field::getName)
                .toList();
        assertEquals(List.of("parent"), links);
    }

    @Test
    @DisplayName("path and handleHelp both follow parent")
    void twoMethodsUseTheParent() {
        String code = codeOf("chainOfResponsibility/gof/parent/Widget.java");
        assertEquals(1, countOf(code, "return parent.path() + \" > \" + name;"));
        assertEquals(1, countOf(code, "return parent.handleHelp();"));
    }

    @Test
    @DisplayName("Main prints where each button is, then its help")
    void mainOutput() {
        assertEquals(List.of(
                "editor > print dialog > print button: Help: print the document.",
                "editor > print dialog > OK button: Help: " + PRINT_DIALOG,
                "editor > font dialog > font button: Help: " + EDITOR), by(() -> Main.main(new String[0])));
    }
}
