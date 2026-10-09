package dev.kaldiroglu.dp.behavioral.command.gof.problem;

import dev.kaldiroglu.dp.behavioral.command.gof.Application;
import dev.kaldiroglu.dp.behavioral.command.gof.Document;

/**
 * Shows a menu item that branches on its own label, and a misspelled label that fails on the
 * first click.
 */
public class Main {

    public static void main(String[] args) {
        Application application = new Application();
        new MenuItem("Open", application, () -> "letter").clicked();
        Document letter = application.current();
        letter.type("Dear Deniz");

        new MenuItem("Copy", application, () -> "").clicked();
        new MenuItem("Paste", application, () -> "").clicked();
        System.out.println("Open, Copy, Paste clicked. The letter reads: " + letter.text());

        MenuItem misspelled = new MenuItem("paste", application, () -> "");
        try {
            misspelled.clicked();
        } catch (IllegalStateException e) {
            System.out.println("A menu entry labeled 'paste' compiled, and the first click failed: "
                    + e.getMessage());
        }
    }
}
