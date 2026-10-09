package dev.kaldiroglu.dp.behavioral.templateMethod.gof;

import dev.kaldiroglu.dp.behavioral.command.lender.Printed;
import dev.kaldiroglu.dp.behavioral.templateMethod.gof.solution.Application;
import dev.kaldiroglu.dp.behavioral.templateMethod.gof.solution.Document;
import dev.kaldiroglu.dp.behavioral.templateMethod.gof.solution.DrawApplication;
import dev.kaldiroglu.dp.behavioral.templateMethod.gof.solution.SpreadsheetApplication;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/** GoF's application framework, before and after the pattern. */
class ApplicationTest {

    private static final List<String> DRAW_EVENTS =
            List.of("open house.draw", "read shapes from house.draw");
    private static final List<String> SPREADSHEET_EVENTS = List.of(
            "remember budget.sheet as the last file", "open budget.sheet", "read cells from budget.sheet");

    @Test
    @DisplayName("gof.Main opens one file of each kind, and ignores the file the drawing program cannot open")
    void mainPrintsBothApplications() {
        List<String> lines = Printed.by(() -> Main.main(new String[0]));

        assertEquals(List.of(
                "Draw:        " + DRAW_EVENTS,
                "Spreadsheet: " + SPREADSHEET_EVENTS), lines);
    }

    @Test
    @DisplayName("before the pattern: each application writes the whole algorithm and gets the same steps")
    void theProblemApplicationsProduceTheSameSteps() {
        var draw = new dev.kaldiroglu.dp.behavioral.templateMethod.gof.problem.DrawApplication();
        draw.openDocument("house.draw");
        draw.openDocument("budget.sheet");
        var sheet = new dev.kaldiroglu.dp.behavioral.templateMethod.gof.problem.SpreadsheetApplication();
        sheet.openDocument("budget.sheet");

        assertEquals(DRAW_EVENTS, draw.events());
        assertEquals(SPREADSHEET_EVENTS, sheet.events());
    }

    @Test
    @DisplayName("with the pattern: the same steps, in the order the template method fixes")
    void theSolutionApplicationsProduceTheSameSteps() {
        Application draw = new DrawApplication();
        draw.openDocument("house.draw");
        Application sheet = new SpreadsheetApplication();
        sheet.openDocument("budget.sheet");

        assertEquals(DRAW_EVENTS, draw.events());
        assertEquals(SPREADSHEET_EVENTS, sheet.events());
        assertTrue(draw.documents().getFirst().isOpen());
        assertTrue(sheet.documents().getFirst().isOpen());
    }

    @Test
    @DisplayName("a file the application cannot open adds no document and no event")
    void aFileThatCannotBeOpenedIsIgnored() {
        Application draw = new DrawApplication();

        draw.openDocument("budget.sheet");

        assertEquals(List.of(), draw.documents());
        assertEquals(List.of(), draw.events());
    }

    @Test
    @DisplayName("openDocument is final; canOpenDocument and doCreateDocument are abstract; the hook is not")
    void theKindsOfMethod() throws Exception {
        Method openDocument = Application.class.getDeclaredMethod("openDocument", String.class);
        Method canOpen = Application.class.getDeclaredMethod("canOpenDocument", String.class);
        Method create = Application.class.getDeclaredMethod("doCreateDocument", String.class);
        Method hook = Application.class.getDeclaredMethod("aboutToOpenDocument", Document.class);

        assertTrue(Modifier.isFinal(openDocument.getModifiers()));
        assertTrue(Modifier.isAbstract(canOpen.getModifiers()));
        assertTrue(Modifier.isAbstract(create.getModifiers()));
        assertFalse(Modifier.isAbstract(hook.getModifiers()));
        assertTrue(Modifier.isProtected(hook.getModifiers()));
    }

    @Test
    @DisplayName("only the spreadsheet overrides the hook; the drawing program leaves it alone")
    void onlyTheSpreadsheetUsesTheHook() {
        assertThrows(NoSuchMethodException.class,
                () -> DrawApplication.class.getDeclaredMethod("aboutToOpenDocument", Document.class));
        assertDoesNotThrow(
                () -> SpreadsheetApplication.class.getDeclaredMethod("aboutToOpenDocument", Document.class));
    }

    @Test
    @DisplayName("when the factory method creates no document, nothing is added and nothing is read")
    void aNullDocumentStopsTheAlgorithm() {
        Application refuses = new Application() {
            @Override
            protected boolean canOpenDocument(String name) {
                return true;
            }

            @Override
            protected Document doCreateDocument(String name) {
                return null;
            }
        };

        refuses.openDocument("anything");

        assertEquals(List.of(), refuses.documents());
        assertEquals(List.of(), refuses.events());
    }
}
