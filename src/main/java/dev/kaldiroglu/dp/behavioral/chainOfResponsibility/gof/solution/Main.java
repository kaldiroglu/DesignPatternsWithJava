package dev.kaldiroglu.dp.behavioral.chainOfResponsibility.gof.solution;

/**
 * Builds the editor's window as a chain: button, dialog, application. A control without
 * its own help passes the request to its parent.
 */
public final class Main {

    public static void main(String[] args) {
        Application editor = new Application("this is the editor. Press F1 on any control.");
        Dialog printDialog = new Dialog(editor, "the print dialog lets you choose a printer.");
        Button print = new Button(printDialog, "print the document.");
        Button ok = new Button(printDialog);
        Button font = new Button(new Dialog(editor, null));
        Button lonely = new Button(new Dialog(new Application(null), null));

        System.out.println("print button (has help):        " + print.handleHelp());
        System.out.println("ok button (asks its dialog):    " + ok.handleHelp());
        System.out.println("font button (asks the editor):  " + font.handleHelp());
        System.out.println("no help anywhere in the chain:  " + lonely.handleHelp());
    }
}
