package dev.kaldiroglu.dp.behavioral.mediator.gof;

import dev.kaldiroglu.dp.behavioral.mediator.gof.problem.FontDialog;
import dev.kaldiroglu.dp.behavioral.mediator.gof.solution.FontDialogDirector;

/** The same steps in both designs: select a font, clear the field, type one, click OK. */
public final class Main {

    public static void main(String[] args) {
        FontDialog before = new FontDialog();
        before.fontList.select("Helvetica");
        System.out.println("Before the pattern");
        System.out.println("  after selecting: field '" + before.fontName.text() + "', OK enabled " + before.ok.enabled());
        before.fontName.setText("");
        System.out.println("  after clearing:  OK enabled " + before.ok.enabled());
        before.fontName.setText("Times");
        before.ok.click(before.fontName.text());
        System.out.println("  after OK:        " + before.log);

        FontDialogDirector after = new FontDialogDirector();
        after.fontList.select("Helvetica");
        System.out.println("With a director");
        System.out.println("  after selecting: field '" + after.fontName.text() + "', OK enabled " + after.ok.enabled());
        after.fontName.type("");
        System.out.println("  after clearing:  OK enabled " + after.ok.enabled());
        after.fontName.type("Times");
        after.ok.click();
        System.out.println("  after OK:        " + after.log);
    }
}
