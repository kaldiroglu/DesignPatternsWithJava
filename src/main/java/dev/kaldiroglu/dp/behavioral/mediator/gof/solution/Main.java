package dev.kaldiroglu.dp.behavioral.mediator.gof.solution;

/**
 * Selects a font, clears the field, types a font, clicks OK, then Cancel. Every widget
 * reports to the director, and the director decides what changes.
 */
public final class Main {

    public static void main(String[] args) {
        FontDialogDirector director = new FontDialogDirector();
        director.fontList.select("Helvetica");
        System.out.println("After selecting: field '" + director.fontName.text() + "', OK enabled " + director.ok.enabled());
        director.fontName.type("");
        System.out.println("After clearing:  OK enabled " + director.ok.enabled());
        director.fontName.type("Times");
        director.ok.click();
        director.cancel.click();
        System.out.println("After OK and Cancel: " + director.log);
    }
}
