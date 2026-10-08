package dev.kaldiroglu.dp.behavioral.templateMethod.gof;

import dev.kaldiroglu.dp.behavioral.templateMethod.gof.solution.Application;
import dev.kaldiroglu.dp.behavioral.templateMethod.gof.solution.DrawApplication;
import dev.kaldiroglu.dp.behavioral.templateMethod.gof.solution.SpreadsheetApplication;

/** Opens one file of each kind, and one that the application cannot open. */
public final class Main {

    public static void main(String[] args) {
        Application draw = new DrawApplication();
        draw.openDocument("house.draw");
        draw.openDocument("budget.sheet");          // not a drawing: nothing happens
        System.out.println("Draw:        " + draw.events());

        Application sheet = new SpreadsheetApplication();
        sheet.openDocument("budget.sheet");
        System.out.println("Spreadsheet: " + sheet.events());
    }
}
