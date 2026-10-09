package dev.kaldiroglu.dp.behavioral.chainOfResponsibility.gof;

import dev.kaldiroglu.dp.behavioral.chainOfResponsibility.gof.problem.HelpDesk;
import dev.kaldiroglu.dp.behavioral.chainOfResponsibility.gof.solution.Application;
import dev.kaldiroglu.dp.behavioral.chainOfResponsibility.gof.solution.Button;
import dev.kaldiroglu.dp.behavioral.chainOfResponsibility.gof.solution.Dialog;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static dev.kaldiroglu.dp.behavioral.chainOfResponsibility.Printed.by;
import static dev.kaldiroglu.dp.behavioral.chainOfResponsibility.Printed.codeOf;
import static dev.kaldiroglu.dp.behavioral.chainOfResponsibility.Printed.countOf;
import static org.junit.jupiter.api.Assertions.*;

/** GoF's context-sensitive help, before and after the pattern. */
class HelpTest {

    private static final String EDITOR = "this is the editor. Press F1 on any control.";
    private static final String PRINT_DIALOG = "the print dialog lets you choose a printer.";

    @Test
    @DisplayName("the print button answers itself")
    void aButtonWithHelp() {
        Application editor = new Application(EDITOR);
        Button print = new Button(new Dialog(editor, PRINT_DIALOG), "print the document.");
        assertEquals("Help: print the document.", print.handleHelp());
    }

    @Test
    @DisplayName("the OK button has no help, so its dialog answers and the application is never asked")
    void theOkButtonGetsTheDialogsHelp() {
        Application editor = new Application(EDITOR);
        Button ok = new Button(new Dialog(editor, PRINT_DIALOG));
        assertFalse(ok.hasHelp());
        assertEquals("Help: " + PRINT_DIALOG, ok.handleHelp());
    }

    @Test
    @DisplayName("a button on a dialog with no help gets the application's help")
    void theApplicationAnswersLast() {
        Application editor = new Application(EDITOR);
        Button font = new Button(new Dialog(editor, null));
        assertEquals("Help: " + EDITOR, font.handleHelp());
    }

    @Test
    @DisplayName("a chain where nobody has help still gives an answer")
    void nobodyHasHelp() {
        Button lonely = new Button(new Dialog(new Application(null), null));
        assertEquals("No help is available.", lonely.handleHelp());
    }

    @Test
    @DisplayName("before the pattern, one help desk knows every control by name")
    void theHelpDeskKnowsEveryControl() {
        HelpDesk desk = new HelpDesk();
        assertEquals("Help: print the document.", desk.helpFor("print button"));
        assertEquals("Help: " + PRINT_DIALOG, desk.helpFor("ok button"));
        assertEquals("Help: " + EDITOR, desk.helpFor("font button"));
        String code = codeOf("chainOfResponsibility/gof/problem/HelpDesk.java");
        assertEquals(4, countOf(code, "case \"") + countOf(code, ", \""),
                "print button, ok button, printer list and print dialog");
    }

    @Test
    @DisplayName("Main asks three controls in both designs and prints the same lines")
    void mainOutput() {
        List<String> lines = by(() -> Main.main(new String[0]));
        assertEquals(List.of(
                "Before the pattern",
                "  print button: Help: print the document.",
                "  ok button:    Help: " + PRINT_DIALOG,
                "  font button:  Help: " + EDITOR,
                "With the chain",
                "  print button: Help: print the document.",
                "  ok button:    Help: " + PRINT_DIALOG,
                "  font button:  Help: " + EDITOR), lines);
        assertEquals(lines.subList(1, 4), lines.subList(5, 8).stream().toList());
    }
}
