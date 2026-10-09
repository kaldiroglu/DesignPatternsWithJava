package dev.kaldiroglu.dp.behavioral.chainOfResponsibility.gof.problem;

/**
 * Asks the help desk for help on four controls. One switch knows every control by name, and
 * a control it does not know gets the editor's general help.
 */
public final class Main {

    public static void main(String[] args) {
        HelpDesk desk = new HelpDesk();
        for (String control : new String[] {"print button", "ok button", "printer list", "font button"}) {
            System.out.println(control + ": " + desk.helpFor(control));
        }
        System.out.println("A new button is one more case in HelpDesk.helpFor.");
    }
}
