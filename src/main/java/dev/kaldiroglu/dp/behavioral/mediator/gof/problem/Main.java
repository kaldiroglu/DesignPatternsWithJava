package dev.kaldiroglu.dp.behavioral.mediator.gof.problem;

/**
 * Selects a font, clears the field, types a font and clicks OK. The widgets call each
 * other directly: the list box sets the field, the field enables the button.
 */
public final class Main {

    public static void main(String[] args) {
        FontDialog dialog = new FontDialog();
        dialog.fontList.select("Helvetica");
        System.out.println("After selecting: field '" + dialog.fontName.text() + "', OK enabled " + dialog.ok.enabled());
        dialog.fontName.setText("");
        System.out.println("After clearing:  OK enabled " + dialog.ok.enabled());
        dialog.fontName.setText("Times");
        dialog.ok.click(dialog.fontName.text());
        System.out.println("After OK:        " + dialog.log);
        System.out.println("ListBox holds the EntryField, and EntryField holds the Button.");
    }
}
