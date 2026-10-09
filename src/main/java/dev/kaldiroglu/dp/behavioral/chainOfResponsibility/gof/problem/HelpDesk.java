package dev.kaldiroglu.dp.behavioral.chainOfResponsibility.gof.problem;

/**
 * GoF's motivation, before the pattern: one object knows the help for every widget.
 * <p>
 * The help desk must know every widget by name, and which dialog each one sits in, so it
 * can fall back to the dialog's help and then to the application's. A new widget, or a
 * button moved to another dialog, is an edit to this class.
 */
public final class HelpDesk {

    public String helpFor(String widget) {
        return switch (widget) {
            case "print button" -> "Help: print the document.";
            case "ok button", "printer list" -> "Help: the print dialog lets you choose a printer.";
            case "print dialog" -> "Help: the print dialog lets you choose a printer.";
            default -> "Help: this is the editor. Press F1 on any control.";
        };
    }
}
