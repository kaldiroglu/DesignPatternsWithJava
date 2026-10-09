package dev.kaldiroglu.dp.behavioral.chainOfResponsibility.gof;

import dev.kaldiroglu.dp.behavioral.chainOfResponsibility.gof.problem.HelpDesk;
import dev.kaldiroglu.dp.behavioral.chainOfResponsibility.gof.solution.Application;
import dev.kaldiroglu.dp.behavioral.chainOfResponsibility.gof.solution.Button;
import dev.kaldiroglu.dp.behavioral.chainOfResponsibility.gof.solution.Dialog;

/** Asks for help on three controls in both designs. They print the same lines. */
public final class Main {

    public static void main(String[] args) {
        HelpDesk desk = new HelpDesk();
        System.out.println("Before the pattern");
        System.out.println("  print button: " + desk.helpFor("print button"));
        System.out.println("  ok button:    " + desk.helpFor("ok button"));
        System.out.println("  font button:  " + desk.helpFor("font button"));

        Application editor = new Application("this is the editor. Press F1 on any control.");
        Dialog printDialog = new Dialog(editor, "the print dialog lets you choose a printer.");
        Button print = new Button(printDialog, "print the document.");
        Button ok = new Button(printDialog);
        Dialog fontDialog = new Dialog(editor, null);
        Button font = new Button(fontDialog);

        System.out.println("With the chain");
        System.out.println("  print button: " + print.handleHelp());
        System.out.println("  ok button:    " + ok.handleHelp());
        System.out.println("  font button:  " + font.handleHelp());
    }
}
