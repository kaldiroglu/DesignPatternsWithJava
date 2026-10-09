package dev.kaldiroglu.dp.behavioral.templateMethod.gof.solution;

/**
 * Opens one file in each application. The order of the steps is in
 * {@code Application.openDocument}; the spreadsheet adds its step through the hook.
 */
public final class Main {

    public static void main(String[] args) {
        Application draw = new DrawApplication();
        draw.openDocument("house.draw");
        draw.openDocument("budget.sheet");          // not a drawing: nothing happens
        System.out.println("Draw:        " + draw.events());

        Application sheet = new SpreadsheetApplication();
        sheet.openDocument("budget.sheet");
        System.out.println("Spreadsheet: " + sheet.events());
        System.out.println("Documents the spreadsheet opened: " + sheet.documents().size());
    }
}
