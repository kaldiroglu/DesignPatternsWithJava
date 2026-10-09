package dev.kaldiroglu.dp.behavioral.templateMethod.gof.problem;

/**
 * Opens one file in each application. Each application has its own copy of the steps,
 * and the spreadsheet added its extra step in a place it chose.
 */
public final class Main {

    public static void main(String[] args) {
        DrawApplication draw = new DrawApplication();
        draw.openDocument("house.draw");
        draw.openDocument("budget.sheet");          // not a drawing: nothing happens
        System.out.println("Draw:        " + draw.events());

        SpreadsheetApplication sheet = new SpreadsheetApplication();
        sheet.openDocument("budget.sheet");
        System.out.println("Spreadsheet: " + sheet.events());
        System.out.println("The same steps are written twice, once in each class.");
    }
}
