package dev.kaldiroglu.dp.behavioral.mediator.gof;

import dev.kaldiroglu.dp.behavioral.mediator.gof.problem.FontDialog;
import dev.kaldiroglu.dp.behavioral.mediator.gof.solution.Button;
import dev.kaldiroglu.dp.behavioral.mediator.gof.solution.DialogDirector;
import dev.kaldiroglu.dp.behavioral.mediator.gof.solution.EntryField;
import dev.kaldiroglu.dp.behavioral.mediator.gof.solution.FontDialogDirector;
import dev.kaldiroglu.dp.behavioral.mediator.gof.solution.ListBox;
import dev.kaldiroglu.dp.behavioral.mediator.gof.solution.Widget;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static dev.kaldiroglu.dp.behavioral.mediator.Fields.heldBy;
import static dev.kaldiroglu.dp.behavioral.mediator.Fields.holdsAny;
import static dev.kaldiroglu.dp.behavioral.mediator.Printed.by;
import static org.junit.jupiter.api.Assertions.*;

/** GoF's font dialog, before and after the pattern. */
class FontDialogTest {

    @Test
    @DisplayName("the director holds each widget by its class, so a new widget is an edit to the director")
    void theDirectorKnowsItsWidgets() throws NoSuchFieldException {
        assertEquals(ListBox.class, FontDialogDirector.class.getField("fontList").getType());
        assertEquals(EntryField.class, FontDialogDirector.class.getField("fontName").getType());
        assertEquals(Button.class, FontDialogDirector.class.getField("ok").getType());
        assertEquals(Button.class, FontDialogDirector.class.getField("cancel").getType());
    }

    @Test
    @DisplayName("with a director: selecting a font fills the field and enables OK")
    void selectingAFont() {
        FontDialogDirector dialog = new FontDialogDirector();
        assertFalse(dialog.ok.enabled());

        dialog.fontList.select("Helvetica");

        assertEquals("Helvetica", dialog.fontName.text());
        assertTrue(dialog.ok.enabled());
    }

    @Test
    @DisplayName("with a director: clearing the field disables OK, and a disabled OK does nothing")
    void clearingTheField() {
        FontDialogDirector dialog = new FontDialogDirector();
        dialog.fontList.select("Helvetica");

        dialog.fontName.type("");
        dialog.ok.click();

        assertFalse(dialog.ok.enabled());
        assertTrue(dialog.log.isEmpty());
    }

    @Test
    @DisplayName("with a director: typing Times and clicking OK sets the font to Times; cancel closes the dialog")
    void clickingOk() {
        FontDialogDirector dialog = new FontDialogDirector();
        dialog.fontName.type("Times");
        dialog.ok.click();
        dialog.cancel.click();
        assertEquals(List.of("font set to Times", "dialog closed"), dialog.log);
    }

    @Test
    @DisplayName("before the pattern each widget holds the next one; after it each widget holds only the director")
    void whoKnowsWhom() {
        assertTrue(heldBy(FontDialog.ListBox.class).contains(FontDialog.EntryField.class));
        assertTrue(heldBy(FontDialog.EntryField.class).contains(FontDialog.Button.class));
        assertTrue(heldBy(FontDialog.Button.class).contains(FontDialog.class));

        List<Class<?>> widgets = List.of(ListBox.class, EntryField.class, Button.class);
        for (Class<?> widget : widgets) {
            assertFalse(holdsAny(widget, widgets), widget.getSimpleName());
        }
        assertEquals(List.of(DialogDirector.class), heldBy(Widget.class));
    }

    @Test
    @DisplayName("both designs give the same field, the same OK button and the same result for the same steps")
    void bothDesignsBehaveTheSame() {
        FontDialog before = new FontDialog();
        FontDialogDirector after = new FontDialogDirector();
        before.fontList.select("Helvetica");
        after.fontList.select("Helvetica");
        assertEquals(before.fontName.text(), after.fontName.text());
        assertEquals(before.ok.enabled(), after.ok.enabled());

        before.fontName.setText("");
        after.fontName.type("");
        assertEquals(before.ok.enabled(), after.ok.enabled());

        before.fontName.setText("Times");
        before.ok.click(before.fontName.text());
        after.fontName.type("Times");
        after.ok.click();
        assertEquals(before.log, after.log);
    }

    @Test
    @DisplayName("gof.problem.Main prints the three steps without a director")
    void problemMainOutput() {
        assertEquals(List.of(
                "After selecting: field 'Helvetica', OK enabled true",
                "After clearing:  OK enabled false",
                "After OK:        [font set to Times]",
                "ListBox holds the EntryField, and EntryField holds the Button."),
                by(() -> dev.kaldiroglu.dp.behavioral.mediator.gof.problem.Main.main(new String[0])));
    }

    @Test
    @DisplayName("gof.solution.Main prints the same three steps with a director, then Cancel")
    void solutionMainOutput() {
        assertEquals(List.of(
                "After selecting: field 'Helvetica', OK enabled true",
                "After clearing:  OK enabled false",
                "After OK and Cancel: [font set to Times, dialog closed]"),
                by(() -> dev.kaldiroglu.dp.behavioral.mediator.gof.solution.Main.main(new String[0])));
    }
}
