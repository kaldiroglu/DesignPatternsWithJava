package dev.kaldiroglu.dp.behavioral.chainOfResponsibility.gof.parent;

import java.util.List;

/** Creates the editor, its dialogs and three buttons, and asks each button for help. Each line shows where the button is, then its help. */
public final class Main {

    public static void main(String[] args) {
        Widget editor = new Widget(null, "editor", "this is the editor. Press F1 on any control.");
        Widget printDialog = new Widget(editor, "print dialog", "the print dialog lets you choose a printer.");
        Widget print = new Widget(printDialog, "print button", "print the document.");
        Widget ok = new Widget(printDialog, "OK button");
        Widget font = new Widget(new Widget(editor, "font dialog"), "font button");

        for (Widget button : List.of(print, ok, font)) {
            System.out.println(button.path() + ": " + button.handleHelp());
        }
    }
}
